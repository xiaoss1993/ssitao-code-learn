# Atomic 原子类原理详解

## 1. 概述

java.util.concurrent.atomic 包提供了一组原子操作类，基于CAS (Compare-And-Swap) 指令实现。

### 核心类

| 类别 | 类 |
|------|-----|
| 基本类型 | AtomicInteger, AtomicLong, AtomicBoolean |
| 引用类型 | AtomicReference, AtomicMarkableReference, AtomicStampedReference |
| 数组类型 | AtomicIntegerArray, AtomicLongArray, AtomicReferenceArray |
| 字段更新器 | AtomicIntegerFieldUpdater, AtomicLongFieldUpdater, AtomicReferenceFieldUpdater |

---

## 2. CAS 原理

### 硬件指令

CAS是CPU提供的原子指令：
```
Compare-And-Swap(addr, expect, update)
如果 addr == expect, 则 addr = update, 返回 true
否则 返回 false
```

### Java实现

```java
// Unsafe类的CAS操作
public final int getAndIncrement() {
    return unsafe.getAndAddInt(this, valueOffset, 1);
}

// unsafe.getAndAddInt伪代码
public int getAndAddInt(Object obj, long valueOffset, int delta) {
    int expect;
    do {
        expect = this.getIntVolatile(obj, valueOffset);  // 获取当前值
    } while (!this.compareAndSwapInt(obj, valueOffset, expect, expect + delta));
    // CAS失败则重试，直到成功
    return expect;
}
```

### ABA问题

```
线程A: 读取 value = 1
线程B: value = 1 -> 2 -> 1 (ABA问题)
线程A: CAS(1, 10) 成功，但value实际被改过
```

解决方案：AtomicStampedReference (带版本号)

---

## 3. Unsafe 类

### 核心方法

```java
// 获取字段内存偏移量
public native long objectFieldOffset(Field f);

// volatile读
public native int getIntVolatile(Object obj, long offset);

// CAS
public native boolean compareAndSwapInt(Object obj, long offset, int expect, int update);
public native boolean compareAndSwapLong(Object obj, long offset, long expect, long update);
public native boolean compareAndSwapObject(Object obj, long offset, Object expect, Object update);
```

---

## 4. AtomicInteger 源码

### 数据结构

```java
public class AtomicInteger extends Number {
    private volatile int value;  // volatile保证可见性

    private static final long valueOffset;  // 内存偏移量

    static {
        valueOffset = unsafe.objectFieldOffset(
            AtomicInteger.class.getDeclaredField("value")
        );
    }
}
```

### 常用方法实现

```java
// getAndSet - 设置新值，返回旧值
public final int getAndSet(int newValue) {
    return unsafe.getAndSetInt(this, valueOffset, newValue);
}

// compareAndSet - CAS设置
public final boolean compareAndSet(int expect, int update) {
    return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
}

// getAndIncrement - 先返回后自增
public final int getAndIncrement() {
    return unsafe.getAndAddInt(this, valueOffset, 1);
}

// incrementAndGet - 先自增后返回
public final int incrementAndGet() {
    return unsafe.getAndAddInt(this, valueOffset, 1) + 1;
}

// updateAndGet - 更新操作
public final int updateAndGet(IntUnaryOperator updateFunction) {
    int prev, next;
    do {
        prev = get();
        next = updateFunction.applyAsInt(prev);
    } while (!compareAndSet(prev, next));
    return next;
}
```

---

## 5. AtomicReference 源码

### 对象引用原子操作

```java
public class AtomicReference<V> {
    private volatile V value;

    public final boolean compareAndSet(V expect, V update) {
        return unsafe.compareAndSwapObject(this, valueOffset, expect, update);
    }

    public final V getAndSet(V newValue) {
        V prev;
        do {
            prev = get();
        } while (!compareAndSet(prev, newValue));
        return prev;
    }
}
```

---

## 6. AtomicStampedReference (带版本号)

解决ABA问题：

```java
public class AtomicStampedReference<V> {
    private static class Pair<T> {
        final T reference;
        final int stamp;  // 版本号
        private Pair(T reference, int stamp) {
            this.reference = reference;
            this.stamp = stamp;
        }
    }

    private volatile Pair<V> pair;

    public boolean compareAndSet(V expectedReference, V newReference,
                                  int expectedStamp, int newStamp) {
        Pair<V> current = pair;
        return expectedReference == current.reference &&
               expectedStamp == current.stamp &&
               ((newReference == current.reference &&
                 newStamp == current.stamp) ||
                casPair(current, Pair.of(newReference, newStamp)));
    }
}
```

---

## 7. AtomicIntegerArray

数组元素的原子操作：

```java
public class AtomicIntegerArray {
    private final int[] array;

    static {
        // 计算数组第一个元素的偏移量
        baseOffset = unsafe.arrayBaseOffset(int[].class);
        // 计算索引i的元素偏移量 = baseOffset + i * scale
    }

    public final int getAndSet(int i, int newValue) {
        long offset = checkedByteOffset(i);
        return unsafe.getAndSetInt(array, offset, newValue);
    }

    public final boolean compareAndSet(int i, int expect, int update) {
        long offset = checkedByteOffset(i);
        return unsafe.compareAndSwapInt(array, offset, expect, update);
    }

    public final int incrementAndGet(int i) {
        return unsafe.getAndAddInt(array, checkedByteOffset(i), 1) + 1;
    }
}
```

---

## 8. LongAdder (JDK8)

比AtomicLong性能更好的计数器：

```java
// 核心思想：分散热点，将一个value变成多个cell
public class LongAdder extends Striped64 {
    @sun.misc.Contended static final class Cell {
        volatile long value;
        Cell(long x) { value = x; }
        final boolean cas(long cmp, long val) {
            return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
        }
    }

    transient volatile Cell[] cells;  // 分散的计数器
    transient volatile long base;     // 基础值

    public void increment() {
        Cell[] cs;
        long b, v;
        int m;
        Cell c;
        if ((cs = cells) != null ||
            !casBase(b = base, b + 1)) {
            // 竞争激烈，尝试使用cells
            if (cs != null) {
                long u = UNSAFE.getLongVolatile(cs, m);
                if (cs[m].cas(u, u + 1))
                    return;
            }
            // 可能需要扩容或创建cells
            internalAccumulate(1, true);
        }
    }
}
```

### 为什么快？

| AtomicLong | LongAdder |
|------------|-----------|
| CAS single value | CAS multiple cells |
| 高竞争时大量自旋 | 分散热点，减少冲突 |

---

## 9. 字段更新器

无锁修改对象的字段：

```java
// 必须使用volatile字段
public class Person {
    volatile int age;
    volatile String name;
}

public class FieldUpdaterDemo {
    public static void main(String[] args) {
        AtomicIntegerFieldUpdater<Person> ageUpdater =
            AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");

        Person person = new Person();

        ageUpdater.compareAndSet(person, 0, 30);
        System.out.println(person.age);  // 30
    }
}
```

---

## 10. 性能对比

### 单线程increment

```
AtomicLong:  ~300ms
LongAdder:   ~200ms
```

### 多线程高竞争

```
AtomicLong:  ~5000ms (大量CAS失败)
LongAdder:   ~300ms (基本无冲突)
```
