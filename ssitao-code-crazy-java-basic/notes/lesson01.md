# 第1课：类和对象

## 核心概念

### 1.1 类的定义
```java
[修饰符] class 类名 {
    // 属性（字段）
    // 方法
    // 构造器
    // 内部类
    // 代码块
}
```

### 1.2 对象的创建
```java
类名 对象名 = new 类构造器();
```

### 1.3 构造器（Constructor）
- 与类名相同
- 没有返回类型
- 对象创建时自动调用
- 若未定义构造器，编译器提供默认无参构造器

### 1.4 this关键字
- 指代当前对象
- 解决成员变量与局部变量同名问题
- 构造器间调用（this(args)）

### 1.5 封装
- 私有化属性（private）
- 提供getter/setter方法
- 必要时加入业务逻辑验证

## 关键知识点

| 知识点 | 说明 |
|--------|------|
| 修饰符 | public, protected, private, default |
| static | 静态成员属于类，类加载时初始化 |
| final | 修饰类不能继承，修饰方法不能重写，修饰变量不能修改 |
| 代码块 | 实例代码块、静态代码块 |

## 代码示例

### 示例1：类的定义与对象创建
```java
public class Person {
    private String name;
    private int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age >= 0 && age <= 150) {
            this.age = age;
        }
    }

    public void speak() {
        System.out.println(name + "说：我今年" + age + "岁");
    }
}
```

### 示例2：静态代码块
```java
public class StaticBlockDemo {
    static {
        System.out.println("静态代码块执行");
    }

    public StaticBlockDemo() {
        System.out.println("构造器执行");
    }

    public static void main(String[] args) {
        new StaticBlockDemo();
        new StaticBlockDemo();
    }
}
```

### 示例3：构造器链
```java
public class ConstructorChain {
    private int id;
    private String name;

    public ConstructorChain() {
        this(0, "默认");
    }

    public ConstructorChain(int id) {
        this(id, "未知");
    }

    public ConstructorChain(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
```

## 练习题

1. 设计一个银行账户类，包含账号、户主、余额属性，提供存钱、取钱方法
2. 创建一个学生类，包含学号、姓名、成绩，实现成绩涨跌功能
3. 用代码块初始化一个数组，计算并输出其元素之和

## 要点总结

- 类是抽象模板，对象是具体实例
- 构造器用于初始化对象
- this解决成员变量遮蔽问题
- 封装是面向对象三大特征之一
- static成员属于类而非对象
