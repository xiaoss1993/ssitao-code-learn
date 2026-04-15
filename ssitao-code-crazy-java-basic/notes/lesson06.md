# 第6课：泛型

## 核心概念

### 6.1 泛型的本质
- 参数化类型
- 编译时类型检查
- 运行时类型擦除

### 6.2 泛型使用场景
- 泛型类
- 泛型方法
- 泛型接口

### 6.3 泛型限定
- `<?>` 无限定通配符
- `<? extends T>` 上限通配符（读取T及子类）
- `<? super T>` 下限通配符（写入T及父类）

## 代码示例

### 示例1：泛型类
```java
public class GenericClass<T> {
    private T value;

    public GenericClass(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public static void main(String[] args) {
        GenericClass<String> obj1 = new GenericClass<>("hello");
        GenericClass<Integer> obj2 = new GenericClass<>(100);

        System.out.println(obj1.getValue());  // hello
        System.out.println(obj2.getValue());   // 100
    }
}
```

### 示例2：泛型方法
```java
public class GenericMethod {
    // 泛型方法
    public static <T> void printArray(T[] array) {
        for (T element : array) {
            System.out.println(element);
        }
    }

    // 泛型方法限定
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    public static void main(String[] args) {
        Integer[] ints = {1, 2, 3};
        String[] strs = {"a", "b", "c"};

        printArray(ints);
        printArray(strs);

        System.out.println(max(10, 20));        // 20
        System.out.println(max("apple", "banana"));  // banana
    }
}
```

### 示例3：泛型接口
```java
// 泛型接口
public interface Generator<T> {
    T next();
}

// 实现泛型接口
public class StringGenerator implements Generator<String> {
    private String[] fruits = {"apple", "banana", "orange"};
    private int index = 0;

    @Override
    public String next() {
        return fruits[index++ % fruits.length];
    }
}
```

### 示例4：通配符使用
```java
import java.util.*;

public class WildcardDemo {
    // 上限：读取数据
    public static void printList(List<? extends Number> list) {
        for (Number num : list) {
            System.out.println(num);
        }
        // list.add(1);  // 编译错误，不能写入
    }

    // 下限：写入数据
    public static void addNumbers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        // Number n = list.get(0);  // 读取类型不安全
    }

    public static void main(String[] args) {
        List<Integer> intList = Arrays.asList(1, 2, 3);
        List<Double> doubleList = Arrays.asList(1.1, 2.2, 3.3);

        printList(intList);      // OK
        printList(doubleList);   // OK

        List<Number> numList = new ArrayList<>();
        addNumbers(numList);      // OK
        // addNumbers(doubleList); // 编译错误
    }
}
```

### 示例5：类型擦除
```java
public class TypeEraseDemo {
    // 编译后类型擦除为Object
    public static <T> T getValue(T value) {
        return value;
    }

    // 编译后类型擦除为Comparable
    public static <T extends Comparable<T>> int compare(T a, T b) {
        return a.compareTo(b);
    }

    public static void main(String[] args) {
        // 编译器自动插入类型转换
        String s = getValue("hello");
        // 实际执行: String s = (String) getValue("hello");

        Integer i = getValue(100);
        // 实际执行: Integer i = (Integer) getValue(100);
    }
}
```

## PECS原则

> Producer-Extends, Consumer-Super

- **读数据**用`extends`（生产者）
- **写数据**用`super`（消费者）
- 既读又写则不用通配符

## 常见面试题

1. **泛型类型擦除是什么？**
   - 编译时使用泛型，运行时移除类型信息
   - 泛型变成Object或上限类型

2. **为什么泛型不能使用基本类型？**
   - 类型擦除后是Object，基本类型不是Object
   - 使用包装类替代

3. **ArrayList<int>和ArrayList<Integer>的区别？**
   - ArrayList<int>编译不通过
   - 泛型不支持基本类型

## 练习题

1. 设计一个泛型栈类
2. 实现一个方法，交换数组中两个元素的位置
3. 设计一个Pair类，存储键值对

## 要点总结

- 泛型提供编译时类型安全
- 类型擦除后使用Object或上限类型
- 通配符解决泛型参数化引用问题
- PECS原则指导通配符使用
