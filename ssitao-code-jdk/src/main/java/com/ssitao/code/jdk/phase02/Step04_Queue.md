# 步骤4：Queue集合 - 队列和双端队列

---

## 4.1 Queue接口概述

### 4.1.1 Queue的特点

- **FIFO（先进先出）**: 元素从队尾入队，从队首出队
- **队列长度**: 可固定、可无限

### 4.1.2 Queue的实现类

```
Queue
├── LinkedList        - 可作为FIFO队列
├── PriorityQueue     - 优先级队列
├── ArrayQueue        - 数组实现的队列
└── Deque
    ├── LinkedList    - 可作为Deque
    └── ArrayDeque    - 数组实现的双端队列
```

### 4.1.3 Queue接口定义

```java
public interface Queue<E> extends Collection<E> {
    // 添加到队尾，失败时抛异常
    boolean add(E e);

    // 添加到队尾，失败时返回false
    boolean offer(E e);

    // 移除并返回队首，失败时抛异常
    E remove();

    // 移除并返回队首，失败时返回null
    E poll();

    // 查看但不移除队首，失败时抛异常
    E element();

    // 查看但不移除队首，失败时返回null
    E peek();
}
```

---

## 4.2 Deque接口

### 4.2.1 Deque的特点

- **双端队列**: 两端都可以入队和出队
- **既可作队列也可作栈**

### 4.2.2 Deque接口定义

```java
public interface Deque<E> extends Queue<E> {
    // 队首操作
    void addFirst(E e);
    void offerFirst(E e);
    E removeFirst();
    E pollFirst();
    E getFirst();
    E peekFirst();

    // 队尾操作
    void addLast(E e);
    void offerLast(E e);
    E removeLast();
    E pollLast();
    E getLast();
    E peekLast();

    // 栈操作（LIFO）
    void push(E e);   // 等价于 addFirst
    E pop();          // 等价于 removeFirst

    // 反向迭代
    Iterator<E> descendingIterator();
}
```

---

## 4.3 LinkedList（作为Queue/Deque）

### 4.3.1 核心特性

| 特性 | 说明 |
|------|------|
| 底层结构 | 双向链表 |
| 线程安全 | 否 |
| 允许null | 是 |
| 性能 | 头尾操作 O(1)，中间操作 O(n) |

### 4.3.2 作为Queue使用

```java
Queue<String> queue = new LinkedList<>();

// 入队（队尾）
queue.offer("a");
queue.offer("b");
queue.offer("c");

// 出队（队首）
while (!queue.isEmpty()) {
    System.out.println(queue.poll());  // a, b, c
}

// 检查但不出队
queue.peek();  // 查看队首
queue.element(); // 查看队首（空队列抛异常）
```

### 4.3.3 作为Deque使用

```java
Deque<String> deque = new LinkedList<>();

// 队首操作
deque.addFirst("first");
deque.offerFirst("second");
deque.getFirst();    // first
deque.peekFirst();   // first
deque.removeFirst(); // first
deque.pollFirst();   // second

// 队尾操作
deque.addLast("last");
deque.offerLast("secondLast");
deque.getLast();     // last
deque.peekLast();    // last
deque.removeLast();  // last
deque.pollLast();    // secondLast
```

### 4.3.4 作为Stack使用（替代Stack）

```java
Deque<String> stack = new LinkedList<>();

// 压栈
stack.push("a");
stack.push("b");
stack.push("c");

// 弹栈（LIFO）
while (!stack.isEmpty()) {
    System.out.println(stack.pop());  // c, b, a
}

// 栈顶检查
stack.peek();  // 查看栈顶不出栈
```

---

## 4.4 ArrayDeque

### 4.4.1 核心特性

| 特性 | 说明 |
|------|------|
| 底层结构 | 循环数组 |
| 线程安全 | 否 |
| 允许null | JDK 8不允许，JDK 7允许 |
| 性能 | 头尾操作 O(1) |
| 初始容量 | 16（或根据构造参数） |

### 4.4.2 源码分析

```java
public class ArrayDeque<E> extends AbstractCollection<E>
        implements Deque<E>, Cloneable, Serializable {

    // 循环数组
    transient Object[] elements;

    // 头尾指针
    transient int head;  // 下一个出队的元素位置
    transient int tail;  // 下一个入队的位置

    // 构造方法
    public ArrayDeque() {
        elements = new Object[16];
    }

    public ArrayDeque(int numElements) {
        elements = new Object[numElements];
    }

    // 循环数组索引计算
    private int inc(int i) {
        return (i + 1) & (elements.length - 1);  // & 比 % 更快
    }

    private int dec(int i) {
        return (i - 1) & (elements.length - 1);
    }

    // 添加到队尾
    public void addLast(E e) {
        if (e == null)
            throw new NullPointerException();
        elements[tail] = e;
        tail = inc(tail);
        if (tail == head)  // 容量满，扩容
            doubleCapacity();
    }

    // 扩容
    private void doubleCapacity() {
        int p = head;
        int n = elements.length;
        int r = n - p;  // head右边的元素数量
        int newCapacity = n << 1;
        Object[] a = new Object[newCapacity];
        System.arraycopy(elements, p, a, 0, r);
        System.arraycopy(elements, 0, a, r, p);
        elements = a;
        head = 0;
        tail = n;
    }

    // 获取队首
    public E pollFirst() {
        if (head == tail)
            return null;
        E result = (E) elements[head];
        elements[head] = null;
        head = inc(head);
        return result;
    }
}
```

### 4.4.3 ArrayDeque vs LinkedList

| 操作 | ArrayDeque | LinkedList |
|------|------------|------------|
| 头尾操作 | O(1) | O(1) |
| 中间操作 | O(n) | O(n) |
| 内存效率 | 更高（数组连续） | 需额外指针空间 |
| 迭代 | 数组索引更快 | 需要指针遍历 |
| 扩容 | 需复制数组 | 无需扩容 |

**选择建议**: 首选 ArrayDeque（性能更好），除非需要中间插入删除

---

## 4.5 PriorityQueue

### 4.5.1 核心特性

| 特性 | 说明 |
|------|------|
| 底层结构 | 最小堆（数组实现的小顶堆） |
| 元素顺序 | 优先级顺序（非FIFO） |
| 线程安全 | 否 |
| 允许null | 否 |
| 性能 | 入队/出队 O(log n) |

### 4.5.2 堆的特性

```
最小堆（小顶堆）：
        1
       / \
      2   3
     / \  / \
    4   5 6  7

特点：
- 每个节点的值都 <= 子节点的值
- 完全二叉树，可以用数组存储
- 队首元素是最小值

数组表示：[1, 2, 3, 4, 5, 6, 7]
         0  1  2  3  4  5  6
父节点: (i-1) / 2
左子节点: 2*i + 1
右子节点: 2*i + 2
```

### 4.5.3 源码分析

```java
public class PriorityQueue<E> extends AbstractQueue<E>
        implements Serializable {

    // 堆数组
    transient Object[] queue;

    // 元素数量
    private int size = 0;

    // 比较器（null表示自然顺序）
    private final Comparator<? super E> comparator;

    // 构造方法
    public PriorityQueue() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    public PriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.queue = new Object[initialCapacity];
    }

    // 入队
    public boolean offer(E e) {
        if (e == null)
            throw new NullPointerException();
        int i = size;
        if (i >= queue.length)
            grow(i + 1);
        size = i + 1;
        if (i == 0)
            queue[0] = e;
        else
            siftUp(i, e);  // 上浮调整
        return true;
    }

    // 上浮
    private void siftUp(int k, E x) {
        if (comparator != null)
            siftUpUsingComparator(k, x);
        else
            siftUpComparable(k, x);
    }

    @SuppressWarnings("unchecked")
    private void siftUpComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = queue[parent];
            if (key.compareTo((E) e) >= 0)
                break;
            queue[k] = e;
            k = parent;
        }
        queue[k] = key;
    }

    // 出队（队首是最小元素）
    public E poll() {
        if (size == 0)
            return null;
        int s = --size;
        E result = (E) queue[0];
        E x = (E) queue[s];
        queue[s] = null;
        if (s > 0)
            siftDown(0, x);  // 下沉调整
        return result;
    }

    // 下沉
    private void siftDown(int k, E x) {
        if (comparator != null)
            siftDownUsingComparator(k, x);
        else
            siftDownComparable(k, x);
    }

    @SuppressWarnings("unchecked")
    private void siftDownComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x;
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = queue[child];
            int right = child + 1;
            if (right < size &&
                ((Comparable<? super E>) c).compareTo((E) queue[right]) > 0)
                c = queue[child = right];
            if (key.compareTo((E) c) <= 0)
                break;
            queue[k] = c;
            k = child;
        }
        queue[k] = key;
    }
}
```

### 4.5.4 PriorityQueue使用示例

```java
// 默认是最小堆（自然顺序）
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
minHeap.add(5);
minHeap.add(2);
minHeap.add(8);
minHeap.add(1);
minHeap.add(9);

while (!minHeap.isEmpty()) {
    System.out.println(minHeap.poll());  // 1, 2, 5, 8, 9
}

// 最大堆（使用Comparator反转）
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
maxHeap.addAll(Arrays.asList(5, 2, 8, 1, 9));

while (!maxHeap.isEmpty()) {
    System.out.println(maxHeap.poll());  // 9, 8, 5, 2, 1
}

// 自定义优先级（按字符串长度）
PriorityQueue<String> lenHeap = new PriorityQueue<>(
    Comparator.comparingInt(String::length)
);
lenHeap.add("apple");
lenHeap.add("banana");
lenHeap.add("pear");
lenHeap.add("hi");

while (!lenHeap.isEmpty()) {
    System.out.println(lenHeap.poll());  // hi, pear, apple, banana
}

// 自定义对象
class Task {
    String name;
    int priority;
}

PriorityQueue<Task> taskQueue = new PriorityQueue<>(
    Comparator.comparingInt(t -> t.priority)
);
```

---

## 4.6 阻塞队列（BlockingQueue）

### 4.6.1 接口定义

```java
public interface BlockingQueue<E> extends Queue<E> {
    // 阻塞式入队
    void put(E e) throws InterruptedException;
    boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException;

    // 阻塞式出队
    V take() throws InterruptedException;
    V poll(long timeout, TimeUnit unit) throws InterruptedException;

    // 其他
    int remainingCapacity();
    int drainTo(Collection<? super E> c);
    int drainTo(Collection<? super E> c, int maxElements);
}
```

### 4.6.2 常用实现类

| 类 | 特性 |
|------|------|
| ArrayBlockingQueue | 数组实现，固定容量，需指定容量 |
| LinkedBlockingQueue | 链表实现，可选容量（默认Integer.MAX_VALUE） |
| PriorityBlockingQueue | 无界优先级队列 |
| SynchronousQueue | 不存储元素，每个put必须等待一个take |
| DelayQueue | 元素需实现Delayed接口，延迟出队 |

### 4.6.3 使用示例

```java
BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);

// 生产者
new Thread(() -> {
    for (int i = 0; i < 20; i++) {
        try {
            queue.put(i);
            System.out.println("Produced: " + i);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}).start();

// 消费者
new Thread(() -> {
    while (true) {
        try {
            Integer item = queue.take();  // 阻塞等待
            System.out.println("Consumed: " + item);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}).start();
```

---

## 4.7 队列选择指南

### 4.7.1 核心对比

| 队列类型 | 实现类 | 特点 | 使用场景 |
|----------|--------|------|----------|
| FIFO | LinkedList | 通用队列 | 一般队列需求 |
| FIFO | ArrayDeque | 性能更好 | 一般队列需求（推荐） |
| 优先级 | PriorityQueue | 优先级顺序 | 任务调度 |
| 阻塞 | LinkedBlockingQueue | 线程安全，阻塞 | 生产者-消费者 |
| 阻塞 | ArrayBlockingQueue | 固定容量 | 生产者-消费者 |
| 双端 | LinkedList/ArrayDeque | 两端操作 | 栈、Deque需求 |

### 4.7.2 选择建议

```java
// 一般队列需求
Queue<E> queue = new ArrayDeque<>();  // 推荐，性能好

// 需要FIFO且经常操作头部
Queue<E> queue = new LinkedList<>();  // 头部操作O(1)

// 优先级队列
Queue<E> pq = new PriorityQueue<>();  // 最小优先

// 线程安全的生产者-消费者
BlockingQueue<E> bq = new LinkedBlockingQueue<>();  // 推荐

// 实现栈（LIFO）
Deque<E> stack = new ArrayDeque<>();  // 推荐，比Stack好
```

---

## 4.8 练习题

```java
// 1. 用Queue实现两个栈（Stack1和Stack2，队列实现）

// 2. 用PriorityQueue实现Top K问题（找出一组数中最大的K个）

// 3. 实现一个简单的任务调度器（按优先级执行任务）

// 4. ArrayDeque<Object> deque = new ArrayDeque<>();
//    deque.add(null);
//    System.out.println(deque.size());
//    运行结果是什么？为什么？

// 5. 用Queue实现杨辉三角
```

---

## 4.9 参考答案

```java
// 1. 两个栈实现队列
class MyQueue<E> {
    private Stack<E> in = new Stack<>();
    private Stack<E> out = new Stack<>();

    public void push(E e) { in.push(e); }

    public E pop() {
        if (out.isEmpty()) {
            while (!in.isEmpty()) out.push(in.pop());
        }
        return out.pop();
    }

    public E peek() {
        if (out.isEmpty()) {
            while (!in.isEmpty()) out.push(in.pop());
        }
        return out.peek();
    }
}

// 2. Top K问题
public static List<Integer> topK(int[] arr, int k) {
    PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);
    for (int n : arr) {
        if (minHeap.size() < k) {
            minHeap.offer(n);
        } else if (n > minHeap.peek()) {
            minHeap.poll();
            minHeap.offer(n);
        }
    }
    return new ArrayList<>(minHeap);
}

// 3. 任务调度器
class Task {
    String name;
    int priority;
    Runnable runnable;
}

class TaskScheduler {
    private PriorityQueue<Task> queue = new PriorityQueue<>(
        Comparator.comparingInt(t -> t.priority)
    );

    public void submit(Task task) {
        queue.offer(task);
    }

    public void run() {
        while (!queue.isEmpty()) {
            Task task = queue.poll();
            task.runnable.run();
        }
    }
}

// 4. 运行结果: 0（ArrayDeque不允许null）
// JDK 7开始ArrayDeque不允许null元素

// 5. 杨辉三角
public static List<List<Integer>> yangHui(int numRows) {
    List<List<Integer>> result = new ArrayList<>();
    Queue<Integer> row = new LinkedList<>();

    for (int i = 0; i < numRows; i++) {
        row.add(1);
        List<Integer> r = new ArrayList<>(row);

        // 生成下一行（先加一个0）
        row.add(0);
        int size = row.size();
        for (int j = 0; j < size - 1; j++) {
            row.offer(row.poll() + row.peek());
        }

        result.add(r);
    }
    return result;
}
```

---

[返回目录](./README.md) | [下一步：迭代器](./Step05_Iterator.md)
