# AQS 核心原理详解

## 1. AQS 概述

AbstractQueuedSynchronizer (AQS) 是JUClocks包的核心基础类。

### 核心位置
```
java.util.concurrent.locks.AbstractQueuedSynchronizer
```

### 设计目标
- 实现阻塞锁和同步器
- 支持独占模式和共享模式
- 提供FIFO等待队列

---

## 2. 核心数据结构

### CLH队列 (Craig, Landin, Hagersten)

AQS使用CLH队列的变体来管理等待线程：

```
        +------+  prev +-----+  prev +-----+
HEAD --> | Node | <---- | Node | <---- | Node | <---- TAIL
         |state |       |state |       |state |
         +------+  next +-----+  next +-----+

         HEAD: 已获取锁的节点
         TAIL: 队尾节点
```

### 节点状态

| 状态值 | 名称 | 含义 |
|--------|------|------|
| 1 | CANCELLED | 等待超时或中断，踢出队列 |
| -1 | SIGNAL | 后继节点需要被唤醒 |
| -2 | CONDITION | 在条件队列中等待 |
| -3 | PROPAGATE | 共享模式下向后传播 |

---

## 3. 状态管理

```java
// AQS内部使用单个int管理状态
private volatile int state;

// 获取状态
protected final int getState() {
    return state;
}

// 设置状态
protected final void setState(int newState) {
    state = newState;
}

// CAS设置状态 (原子操作)
protected final boolean compareAndSetState(int expect, int update) {
    return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
}
```

### 状态含义

| 同步器 | 状态含义 |
|--------|----------|
| ReentrantLock | 1=已获取, >1=重入次数 |
| ReentrantReadWriteLock | 高16位=读锁, 低16位=写锁 |
| Semaphore | 剩余许可证数量 |
| CountDownLatch | 倒计时初始值 |
| CyclicBarrier | 等待方数量 |

---

## 4. 独占模式

### 获取锁

```java
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) {
        selfInterrupt();
    }
}
```

流程：
1. `tryAcquire()` 尝试获取锁
2. 失败则 `addWaiter()` 创建节点加入队列
3. `acquireQueued()` 自旋等待锁释放

### 释放锁

```java
public final boolean release(int arg) {
    if (tryRelease(arg)) {
        Node h = head;
        if (h != null && h.waitStatus != 0) {
            unparkSuccessor(h);  // 唤醒后继节点
        }
        return true;
    }
    return false;
}
```

---

## 5. 共享模式

### 获取锁

```java
public final void acquireShared(int arg) {
    if (tryAcquireShared(arg) < 0) {
        doAcquireShared(arg);  // 入队并阻塞
    }
}
```

### 释放锁

```java
public final boolean releaseShared(int arg) {
    if (tryReleaseShared(arg)) {
        doReleaseShared();  // 唤醒后继节点
        return true;
    }
    return false;
}
```

---

## 6. 等待队列操作

### 入队

```java
private Node addWaiter(Node mode) {
    Node node = new Node(Thread.currentThread(), mode);
    Node pred = tail;

    if (pred != null) {
        node.prev = pred;
        if (compareAndSetTail(pred, node)) {
            pred.next = node;
            return node;
        }
    }
    enq(node);  // 自旋入队
    return node;
}
```

### 出队

```java
private void unparkSuccessor(Node node) {
    int ws = node.waitStatus;
    if (ws < 0) {
        compareAndSetWaitStatus(node, ws, 0);
    }
    Node s = node.next;
    if (s == null || s.waitStatus > 0) {
        // 找到有效的后继节点
        s = null;
        for (Node t = tail; t != null && t != node; t = t.prev) {
            if (t.waitStatus <= 0) {
                s = t;
            }
        }
    }
    if (s != null) {
        LockSupport.unpark(s.thread);  // 唤醒
    }
}
```

---

## 7. 自定义AQS同步器

### 模板方法模式

AQS定义好流程，子类实现核心方法：

```java
// 独占模式
protected boolean tryAcquire(int arg)      // 尝试获取
protected boolean tryRelease(int arg)       // 尝试释放

// 共享模式
protected int tryAcquireShared(int arg)     // 返回值: <0失败, >=0成功
protected boolean tryReleaseShared(int arg) // 是否完全释放
```

### 示例：互斥锁

```java
class Mutex {
    private final Sync sync = new Sync();

    public void lock() {
        sync.acquire(1);
    }

    public void unlock() {
        sync.release(1);
    }

    static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            // CAS尝试获取锁
            return compareAndSetState(0, 1);
        }

        @Override
        protected boolean tryRelease(int arg) {
            setState(0);
            return true;
        }
    }
}
```

---

## 8. 公平 vs 非公平

### 非公平锁

```java
final void lock() {
    if (compareAndSetState(0, 1))
        setExclusiveOwnerThread(Thread.currentThread());
    else
        acquire(1);
}
```

### 公平锁

```java
final void lock() {
    acquire(1);
}

protected final boolean tryAcquire(int acquires) {
    if (hasQueuedPredecessors())  // 检查是否有人在排队
        return false;
    // ...
}
```

---

## 9. 条件变量

ConditionObject 是AQS的内部类，实现了Condition接口。

### 等待队列 vs 阻塞队列

```
AQS阻塞队列:           Condition条件队列:
  head --> Node --> Node
           |              |
           v              v
         tail          first CJ

signal() 将节点从条件队列移到阻塞队列
```

### await()

```java
public final void await() throws InterruptedException {
    if (Thread.interrupted()) throw new InterruptedException();
    Node node = addConditionWaiter();  // 加入条件队列
    int savedState = getState();
    release(savedState);              // 释放锁
    while (!isOnSyncQueue(node)) {     // 等待signal
        LockSupport.park(this);
    }
    // 被唤醒后重新获取锁
    acquireQueued(node, savedState);
}
```

### signal()

```java
public final void signal() {
    if (!isHeldExclusively())  // 必须持有锁
        throw new IllegalMonitorStateException();
    Node first = firstWaiter;
    if (first != null)
        doSignal(first);        // 移到阻塞队列
}
```
