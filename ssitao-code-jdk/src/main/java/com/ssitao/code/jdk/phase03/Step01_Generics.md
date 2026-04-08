# 步骤1：泛型 - 编译时类型安全

---

## 1.1 泛型概述

### 1.1.1 为什么需要泛型

```java
// 没有泛型时 - 使用Object
class Box {
    private Object value;

    public void set(Object value) {
        this.value = value;
    }

    public Object get() {
        return value;
    }
}

// 使用时需要强制类型转换
Box box = new Box();
box.set("hello");
String str = (String) box.get();  // 必须强制转换
box.set(123);
Integer num = (Integer) box.get();  // 可能ClassCastException

// 有泛型后 - 类型安全
class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

// 使用时不需要类型转换，且类型安全
Box<String> stringBox = new Box<>();
stringBox.set("hello");
String str = stringBox.get();  // 无需类型转换，编译器保证类型
stringBox.set(123);  // 编译错误！类型不安全
```

### 1.1.2 泛型的优势

1. **类型安全**: 编译时检测类型错误
2. **消除强制类型转换**: 代码更简洁
3. **提高代码可读性**: 类型信息明确
4. **支持通用算法**: 编写与类型无关的代码

---

## 1.2 泛型类

### 1.2.1 基本语法

```java
// 单类型参数
class Box<T> {
    private T value;

    public Box(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}

// 使用
Box<String> stringBox = new Box<>("hello");
Box<Integer> intBox = new Box<>(123);
Box<Double> doubleBox = new Box<>(3.14);

// 注意：Box<String>和Box<Integer>是两个完全不同的类型
```

### 1.2.2 多类型参数

```java
// 多类型参数
class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}

// 使用
Pair<String, Integer> pair = new Pair<>("age", 25);
String key = pair.getKey();
Integer value = pair.getValue();
```

### 1.2.3 泛型类的继承

```java
// 父类
class Animal {
    void eat() { }
}

// 子类延续泛型
class Dog<T> extends Animal {
    T breed;
}

// 子类具体化泛型
class StringDog extends Animal {
    String breed;
}

// 子类增加泛型
class GenericDog<T> extends Dog<T> {
    T owner;
}
```

---

## 1.3 泛型接口

### 1.3.1 基本语法

```java
// 定义泛型接口
interface Container<T> {
    void add(T item);
    T get(int index);
    int size();
}

// 实现泛型接口
class ListContainer<T> implements Container<T> {
    private List<T> list = new ArrayList<>();

    @Override
    public void add(T item) {
        list.add(item);
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }
}

// 实现时指定具体类型
class StringContainer implements Container<String> {
    private List<String> list = new ArrayList<>();

    @Override
    public void add(String item) {
        list.add(item);
    }

    @Override
    public String get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }
}
```

### 1.3.2 泛型接口的常见例子

```java
// JDK中的泛型接口
public interface List<E> extends Collection<E> {
    E get(int index);
    E set(int index, E element);
    void add(int index, E element);
    // ...
}

public interface Map<K, V> {
    V get(K key);
    V put(K key, V value);
    // ...
}

public interface Comparable<T> {
    int compareTo(T o);
}

public interface Comparator<T> {
    int compare(T o1, T o2);
}
```

---

## 1.4 泛型方法

### 1.4.1 基本语法

```java
// 泛型方法的定义
public static <T> T getFirst(List<T> list) {
    if (list == null || list.isEmpty()) {
        return null;
    }
    return list.get(0);
}

// 调用时指定类型（编译器推断）
String first = getFirst(Arrays.asList("a", "b", "c"));
Integer firstNum = getFirst(Arrays.asList(1, 2, 3));

// 显式指定类型
String first2 = GenericDemo.<String>getFirst(Arrays.asList("a", "b"));
```

### 1.4.2 多个类型参数

```java
// 多个类型参数的泛型方法
public static <K, V> Map<K, V> createMap(K key, V value) {
    Map<K, V> map = new HashMap<>();
    map.put(key, value);
    return map;
}

// 使用
Map<String, Integer> map = createMap("age", 25);
```

### 1.4.3 泛型方法与泛型类的区别

```java
// 泛型类 - 类型参数在类级别声明
class GenericClass<T> {
    T value;
    public T get() { return value; }
}

// 泛型方法 - 类型参数在方法级别声明
class GenericMethodDemo {
    // 静态泛型方法
    public static <T> void print(T item) {
        System.out.println(item);
    }

    // 非静态泛型方法
    public <T> T convert(String str, Class<T> clazz) throws Exception {
        // ...
        return clazz.getDeclaredConstructor().newInstance();
    }
}
```

---

## 1.5 类型擦除

### 1.5.1 什么是类型擦除

```java
// 源代码
class Box<T> {
    private T value;
    public T get() { return value; }
    public void set(T value) { this.value = value; }
}

// 编译后（等价于）
class Box {
    private Object value;  // T被替换为Object
    public Object get() { return value; }
    public void set(Object value) { this.value = value; }
}

// 如果指定了上限
class Box<T extends Number> {
    private T value;
    public T get() { return value; }
}

// 编译后
class Box {
    private Number value;  // T被替换为上限Number
    public Number get() { return value; }
}
```

### 1.5.2 类型擦除的详细过程

```java
// 原始类型（Raw Type）
class Node<T> {
    private T data;
    private Node<T> next;
}

// 编译后
class Node {
    private Object data;      // T -> Object
    private Node next;        // Node<T> -> Node (raw type)
}
```

### 1.5.3 类型擦除导致的问题

```java
// 问题1：泛型不能用于重载（类型擦除后签名相同）
class Example {
    // 编译错误！这两个方法类型擦除后都是 void method(List)
    // void method(List<String> list) { }
    // void method(List<Integer> list) { }
}

// 问题2：泛型类型不能作为静态变量
class Test<T> {
    // 编译错误！
    // private static T staticValue;

    // 正确：静态变量应该是所有实例共享的，不能依赖泛型
    private static Object staticValue;
}

// 问题3：instanceof不能使用泛型
class Test {
    public void method(Object obj) {
        // 编译错误！
        // if (obj instanceof List<String>) { }
    }
}
```

---

## 1.6 通配符

### 1.6.1 无界通配符 `<?>`

```java
// 无界通配符表示未知类型
public void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

// 使用
List<String> stringList = Arrays.asList("a", "b", "c");
List<Integer> intList = Arrays.asList(1, 2, 3);
printList(stringList);  // OK
printList(intList);     // OK

// ? vs Object 的区别
public void printList1(List<Object> list) {
    // 只能接受List<Object>，不能接受List<String>
}

public void printList2(List<?> list) {
    // 可以接受List<String>、List<Integer>等任何List
}
```

### 1.6.2 上界通配符 `<? extends T>`

```java
// 上界通配符 - 用于读取（生产）
public double sumOfList(List<? extends Number> list) {
    double sum = 0;
    for (Number n : list) {  // 可以读取为Number
        sum += n.doubleValue();
    }
    return sum;
}

// 使用
List<Integer> intList = Arrays.asList(1, 2, 3);
List<Double> doubleList = Arrays.asList(1.1, 2.2, 3.3);
sumOfList(intList);    // OK
sumOfList(doubleList); // OK

// 不能写入（除了null）
public void addNumber(List<? extends Number> list) {
    list.add(1);   // 编译错误！不知道具体类型
    list.add(1.0); // 编译错误！
    list.add(null); // 唯一可以添加的
}
```

### 1.6.3 下界通配符 `<? super T>`

```java
// 下界通配符 - 用于写入（消费）
public void addNumbers(List<? super Integer> list) {
    list.add(1);      // 可以添加Integer
    list.add(2);      // 可以添加Integer
    list.add(3);      // 可以添加Integer
    // list.add(1.0); // 编译错误！Double不是Integer
}

public void printIntegers(List<? super Integer> list) {
    for (Object obj : list) {  // 只能读取为Object
        System.out.println(obj);
    }
}

// 使用
List<Number> numberList = new ArrayList<>();
List<Object> objectList = new ArrayList<>();
addNumbers(numberList);  // OK
addNumbers(objectList);  // OK
```

### 1.6.4 PECS原则

```java
// Producer Extends, Consumer Super (PECS)

/*
场景：数据提供者（读取数据）-> 用 extends
场景：数据消费者（写入数据）-> 用 super
*/

// 示例：栈的pushAll和popAll
class Stack<E> {
    // 从另一个栈获取元素（读取）-> 用 extends
    public void pushAll(Iterable<? extends E> src) {
        for (E e : src) {
            push(e);
        }
    }

    // 复制到另一个集合（写入）-> 用 super
    public void popAll(Collection<? super E> dest) {
        while (!isEmpty()) {
            dest.add(pop());
        }
    }
}
```

### 1.6.5 通配符对比

| 通配符 | 含义 | 可读 | 可写 | 适用场景 |
|--------|------|------|------|----------|
| `<?>` | 未知类型 | 是（Object） | 否 | 极度通用 |
| `<? extends T>` | T或T的子类 | 是 | 否 | 生产数据 |
| `<? super T>` | T或T的父类 | 是（Object） | 是 | 消费数据 |

---

## 1.7 类型边界

### 1.7.1 单边界

```java
// T必须是Number或Number的子类
class Calculator<T extends Number> {
    public double doubleValue(T value) {
        return value.doubleValue();  // 可以调用Number的方法
    }
}

// 使用
Calculator<Integer> calc1 = new Calculator<>();  // OK
Calculator<Double> calc2 = new Calculator<>();    // OK
Calculator<String> calc3 = new Calculator<>();    // 编译错误！
```

### 1.7.2 多边界

```java
// T必须是Comparable且Serializable
class Data<T extends Comparable<T> & Serializable> {
    T data;
}

// 如果多个边界都是类，排在第一位的作为擦除后的类型
class Demo<T extends Number & Runnable> {
    // 擦除后T变为Number，Runnable作为额外接口保留
}
```

---

## 1.8 泛型与数组

### 1.8.1 泛型数组的限制

```java
// 不能创建泛型数组
class Test<T> {
    // 编译错误！
    // T[] array = new T[10];

    // 正确做法1：使用反射
    T[] array = (T[]) new Object[10];

    // 正确做法2：传递Class对象
    class Test2<T> {
        T[] array;
        public Test2(Class<T> clazz) {
            array = (T[]) java.lang.reflect.Array.newInstance(clazz, 10);
        }
    }
}

// JDK中的例子：ArrayList
public class ArrayList<E> {
    private Object[] elementData;

    public ArrayList(int initialCapacity) {
        elementData = new Object[initialCapacity];
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) elementData[index];
    }
}
```

---

## 1.9 泛型的实际应用

### 1.9.1 工具类中的泛型

```java
// 泛型工具方法
public class Collections {
    public static <T extends Comparable<? super T>> void sort(List<T> list) {
        // ...
    }

    public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T key) {
        // ...
    }

    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        // ...
    }
}

// 使用
List<Integer> list = Arrays.asList(3, 1, 2);
Collections.sort(list);  // 自动推断T为Integer
```

### 1.9.2 通用DAO接口

```java
// 泛型DAO
public interface GenericDAO<T, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    void delete(ID id);
}

public interface UserDAO extends GenericDAO<User, Long> {
    User findByUsername(String username);
}

public class User {
    Long id;
    String username;
    // ...
}
```

---

## 1.10 练习题

```java
// 1. 实现一个泛型方法，找出数组中的最大值

// 2. 实现一个泛型栈 GenericStack<T>

// 3. 解释以下代码能否编译
class A { }
class B extends A { }
class C extends B { }

void method1(List<? extends B> list) { }
void method2() {
    List<A> listA = new ArrayList<>();
    List<B> listB = new ArrayList<>();
    List<C> listC = new ArrayList<>();
    method1(listA);  // ?
    method1(listB);  // ?
    method1(listC);  // ?
}

// 4. 实现一个泛型方法，交换数组中两个位置的元素

// 5. 说明类型擦除后，以下类的形式
class Node<T extends Comparable<T>> {
    T data;
    Node<T> next;
}
```

---

## 1.11 参考答案

```java
// 1. 找出最大值
public static <T extends Comparable<? super T>> T findMax(T[] arr) {
    if (arr == null || arr.length == 0) return null;
    T max = arr[0];
    for (int i = 1; i < arr.length; i++) {
        if (arr[i].compareTo(max) > 0) {
            max = arr[i];
        }
    }
    return max;
}

// 2. 泛型栈
public class GenericStack<E> {
    private LinkedList<E> list = new LinkedList<>();

    public void push(E item) {
        list.addFirst(item);
    }

    public E pop() {
        return list.removeFirst();
    }

    public E peek() {
        return list.peekFirst();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}

// 3. 答案
method1(listA);  // 编译错误！List<A>不是List<? extends B>
method1(listB);  // OK
method1(listC);  // OK

// 4. 交换元素
public static <T> void swap(T[] arr, int i, int j) {
    T temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}

// 5. 类型擦除后
class Node {
    Comparable data;      // T被替换为Comparable（上限）
    Node next;            // Node<T>被替换为Node（raw type）
}
```

---

[返回目录](./README.md) | [下一步：反射](./Step02_Reflection.md)
