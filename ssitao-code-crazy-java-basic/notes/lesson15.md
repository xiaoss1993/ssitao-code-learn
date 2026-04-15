# 第15课：函数式编程

## 核心概念

### 15.1 函数式编程思想
- 函数是一等公民
- 声明式编程
- 避免副作用
- 参数化行为

### 15.2 函数式接口
- 只含一个抽象方法的接口
- @FunctionalInterface注解

### 15.3 高阶函数
- 返回函数的函数
- 接收函数参数的函数

### 15.4 闭包
- 函数可以访问外部变量
- 外部变量被"捕获"

## 代码示例

### 示例1：函数作为返回值
```java
import java.util.function.*;

public class FunctionReturnDemo {
    // 返回函数的函数（高阶函数）
    public static Function<String, Function<Integer, String>> makeFormatter() {
        return (String prefix) -> (Integer number) -> prefix + number;
    }

    public static void main(String[] args) {
        Function<String, Function<Integer, String>> formatterFactory = makeFormatter();

        Function<Integer, String> intFormatter = formatterFactory.apply("数字：");
        System.out.println(intFormatter.apply(100));  // 数字：100

        Function<Integer, String> chFormatter = formatterFactory.apply("第");
        System.out.println(chFormatter.apply(1));  // 第1
    }
}
```

### 示例2：函数组合
```java
import java.util.function.*;

public class FunctionCompositionDemo {
    public static void main(String[] args) {
        Function<Integer, Integer> add1 = x -> x + 1;
        Function<Integer, Integer> multiply2 = x -> x * 2;
        Function<Integer, Integer> addThenMultiply = add1.andThen(multiply2);
        Function<Integer, Integer> multiplyThenAdd = add1.compose(multiply2);

        // andThen: 先执行调用者，再执行参数
        System.out.println(addThenMultiply.apply(5));  // (5+1)*2 = 12

        // compose: 先执行参数，再执行调用者
        System.out.println(multiplyThenAdd.apply(5));  // 5*2+1 = 11

        // Predicate组合
        Predicate<String> startsWithA = s -> s.startsWith("A");
        Predicate<String> endsWithZ = s -> s.endsWith("Z");
        Predicate<String> containsX = s -> s.contains("X");

        Predicate<String> AZ = startsWithA.and(endsWithZ);
        Predicate<String> AorZ = startsWithA.or(endsWithZ);
        Predicate<String> notA = startsWithA.negate();

        System.out.println(AZ.test("ABC"));    // true
        System.out.println(AorZ.test("Hello")); // false
        System.out.println(notA.test("Apple")); // false
    }
}
```

### 示例3：闭包
```java
public class ClosureDemo {
    public static void main(String[] args) {
        int outerVar = 10;  // 外部变量

        // Lambda捕获外部变量
        Runnable r = () -> {
            // 内部可以访问outerVar
            System.out.println("outerVar = " + outerVar);
            // outerVar = 20;  // 不能修改（ Effectively Final）
        };

        r.run();

        // 方法返回捕获变量的函数
        Function<Integer, Integer> makeAdder(int base) {
            // base被捕获（ Effectively Final）
            return x -> x + base;
        }

        Function<Integer, Integer> add5 = makeAdder(5);
        Function<Integer, Integer> add10 = makeAdder(10);

        System.out.println(add5.apply(100));   // 105
        System.out.println(add10.apply(100));  // 110
    }
}
```

### 示例4：柯里化
```java
import java.util.function.*;

public class CurryingDemo {
    // 柯里化：将多参数函数转为一系列单参数函数
    public static void main(String[] args) {
        // 普通函数
        IntBinaryOperator sum = (a, b) -> a + b;
        System.out.println(sum.applyAsInt(1, 2));  // 3

        // 柯里化版本
        IntFunction<IntUnaryOperator> curriedSum = a -> b -> a + b;

        IntUnaryOperator add5 = curriedSum.apply(5);
        System.out.println(add5.applyAsInt(10));  // 15
        System.out.println(add5.applyAsInt(20));  // 25

        // 三参数柯里化
        Function<Integer, Function<Integer, Function<Integer, Integer>>>
            tripleSum = a -> b -> c -> a + b + c;

        System.out.println(tripleSum.apply(1).apply(2).apply(3));  // 6

        // 部分应用
        Function<Integer, Function<Integer, Integer>> addTo100 = tripleSum.apply(100);
        System.out.println(addTo100.apply(1).apply(2));  // 103
    }
}
```

### 示例5：命令式vs函数式
```java
import java.util.*;

public class ImperativeVsFunctionalDemo {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 命令式
        System.out.println("=== 命令式 ===");
        List<Integer> evens1 = new ArrayList<>();
        for (int n : numbers) {
            if (n % 2 == 0) {
                evens1.add(n * 2);
            }
        }
        Collections.sort(evens1, Collections.reverseOrder());
        for (int n : evens1) {
            System.out.println(n);
        }

        // 函数式
        System.out.println("=== 函数式 ===");
        numbers.stream()
            .filter(n -> n % 2 == 0)
            .map(n -> n * 2)
            .sorted(Comparator.reverseOrder())
            .forEach(System.out::println);
    }
}
```

### 示例6：副作用与纯函数
```java
import java.util.*;

public class SideEffectDemo {
    // 有副作用：修改外部状态
    private static int total = 0;

    public static int addWithSideEffect(int value) {
        total += value;  // 修改外部变量
        return total;
    }

    // 纯函数：无副作用
    public static int addPure(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        // 纯函数测试
        System.out.println(addPure(1, 2));  // 3
        System.out.println(addPure(1, 2));  // 3（结果确定）

        // 有副作用的函数
        System.out.println(addWithSideEffect(1));  // 1
        System.out.println(addWithSideEffect(2));  // 3（依赖状态）
    }
}
```

### 示例7：递归优化
```java
public class RecursionDemo {
    // 普通递归（栈溢出风险）
    public static long factorialRecursive(int n) {
        if (n <= 1) return 1;
        return n * factorialRecursive(n - 1);
    }

    // 尾递归优化（JVM不自动优化，需手动改写）
    public static long factorialTail(int n, long acc) {
        if (n <= 1) return acc;
        return factorialTail(n - 1, n * acc);
    }

    public static long factorial(int n) {
        return factorialTail(n, 1);
    }

    public static void main(String[] args) {
        // 递归
        System.out.println(factorialRecursive(5));  // 120

        // 尾递归
        System.out.println(factorial(5));  // 120

        // Stream实现（更安全）
        long result = java.util.stream.LongStream.rangeClosed(1, 5)
            .reduce(1, (a, b) -> a * b);
        System.out.println(result);  // 120
    }
}
```

## 设计模式函数式实现

### 策略模式
```java
import java.util.function.*;

public class StrategyPatternDemo {
    public static void main(String[] args) {
        // 不同策略
        Function<Integer, Integer> doubleStrategy = x -> x * 2;
        Function<Integer, Integer> squareStrategy = x -> x * x;

        // 使用策略
        int result = applyStrategy(5, doubleStrategy);
        System.out.println(result);  // 10

        result = applyStrategy(5, squareStrategy);
        System.out.println(result);  // 25
    }

    static int applyStrategy(int value, Function<Integer, Integer> strategy) {
        return strategy.apply(value);
    }
}
```

## 常见面试题

1. **什么是闭包？**
   - 函数能访问外部变量
   - 外部变量被捕获

2. **Lambda和闭包的区别？**
   - Lambda是闭包的一种实现
   - 闭包是概念，Lambda是语法

3. **函数式编程的优点？**
   - 代码简洁
   - 易于并行
   - 减少bug

## 练习题

1. 实现函数组合工具类
2. 用函数式风格实现策略模式
3. 实现柯里化函数

## 要点总结

- 函数是一等公民
- 避免副作用，使用纯函数
- 函数组合简化代码
- 闭包捕获外部变量
- 递归可替代循环
