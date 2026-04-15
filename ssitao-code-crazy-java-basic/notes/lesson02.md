# 第2课：继承与多态

## 核心概念

### 2.1 继承
- 使用`extends`关键字
- 子类继承父类的属性和方法
- Java不支持多继承（单继承、多接口）
- 子类可以新增属性和方法，可以重写父类方法

### 2.2 super关键字
- 指代父类对象
- 调用父类构造器：`super(args)`
- 调用父类方法：`super.method(args)`

### 2.3 方法重写（Override）
- 方法名相同
- 参数列表相同
- 返回类型相同或其子类（协变返回类型）
- 不能降低访问权限

### 2.4 多态
- **向上转型**：父类引用指向子类对象（自动）
- **向下转型**：强制类型转换，需instanceof检查
- 多态调用方法时，实际执行的是子类版本

## 继承层级

```
Object
  └── Person
        └── Student
              └── Undergraduate
```

## 代码示例

### 示例1：继承基础
```java
public class Person {
    protected String name;
    protected int age;

    public Person() {}

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void display() {
        System.out.println("姓名：" + name + "，年龄：" + age);
    }
}

public class Student extends Person {
    private double score;

    public Student() {}

    public Student(String name, int age, double score) {
        super(name, age);
        this.score = score;
    }

    @Override
    public void display() {
        super.display();
        System.out.println("成绩：" + score);
    }

    public double getScore() {
        return score;
    }
}
```

### 示例2：多态
```java
public class PolyDemo {
    public static void main(String[] args) {
        // 向上转型
        Person p = new Student("张三", 20, 95.5);
        p.display();  // 调用Student的display

        // 向下转型
        if (p instanceof Student) {
            Student s = (Student) p;
            System.out.println(s.getScore());
        }
    }
}
```

### 示例3：final关键字
```java
public final class FinalClass {
    // final类不能被继承
}

public class Parent {
    public final void method() {
        // final方法不能被重写
    }
}
```

## 重写vs重载

| 区别 | 重写(Override) | 重载(Overload) |
|------|---------------|----------------|
| 参数列表 | 必须相同 | 必须不同 |
| 返回类型 | 必须相同或其子类 | 可以不同 |
| 访问修饰符 | 不能降低 | 无限制 |
| 发生位置 | 父子类之间 | 同一类中 |
| 编译时/运行时 | 运行时绑定 | 编译时绑定 |

## 练习题

1. 设计一个动物类层次结构，包含父类Animal和子类Dog、Cat，实现各自的eat()方法
2. 创建一个Shape基类和Circle、Square子类，计算各自面积
3. 演示多态的运用：定义一个方法接受父类类型，传入不同子类对象调用

## 要点总结

- 继承实现代码复用
- super调用父类成员
- 方法重写实现多态
- 向上转型安全，向下转型需检查
- 多态是"一个接口，多种实现"
