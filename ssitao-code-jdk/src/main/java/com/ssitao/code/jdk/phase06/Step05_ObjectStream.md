# 步骤5：对象序列化 - 对象的持久化

---

## 5.1 序列化概述

### 5.1.1 什么是序列化

```java
// 序列化（Serialization）：
// 将Java对象转换为字节流，以便存储或传输
// 反序列化（Deserialization）：
// 将字节流恢复为Java对象

// 应用场景：
// - 对象持久化到文件
// - 网络传输对象
// - 缓存对象
// - RMI（远程方法调用）
```

### 5.1.2 Serializable接口

```java
// 要序列化的类必须实现Serializable接口
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    private transient String password;  // transient: 不序列化

    // getter/setter
}

// serialVersionUID作用：
// - 版本号，序列化/反序列化时必须匹配
// - 不匹配会抛出InvalidClassException
// - 建议显式声明
```

---

## 5.2 ObjectOutputStream

### 5.2.1 基本使用

```java
// ObjectOutputStream：序列化对象

// 创建
try (ObjectOutputStream oos = new ObjectOutputStream(
        new BufferedOutputStream(new FileOutputStream("person.dat")))) {

    Person person = new Person("Alice", 25);
    oos.writeObject(person);
    oos.flush();
}

// 常用方法
oos.writeObject(obj);           // 写入对象
oos.writeInt(int);              // 写入基本类型
oos.writeUTF(String);           // 写入字符串
oos.writeObject(array);         // 写入数组
oos.writeObject(list);          // 写入集合
```

### 5.2.2 序列化过程

```java
// 序列化过程：
// 1. 写入类描述信息（类名、serialVersionUID）
// 2. 递归写入对象的每个字段
// 3. 基本类型直接写入
// 4. 引用类型写入引用或递归序列化
// 5. transient字段被忽略

// 示例
public class SerializationDemo {
    public static void main(String[] args) throws Exception {
        // 创建一个包含引用的对象
        Department dept = new Department("Engineering");
        Employee emp = new Employee("Alice", 25, dept);

        // 序列化
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("employee.dat"))) {
            oos.writeObject(emp);
        }

        // 反序列化
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("employee.dat"))) {
            Employee emp2 = (Employee) ois.readObject();
            System.out.println(emp2);
        }
    }
}
```

---

## 5.3 ObjectInputStream

### 5.3.1 基本使用

```java
// ObjectInputStream：反序列化对象

// 创建
try (ObjectInputStream ois = new ObjectInputStream(
        new BufferedInputStream(new FileInputStream("person.dat")))) {

    Person person = (Person) ois.readObject();
}

// 常用方法
Object obj = ois.readObject();  // 读取对象
int num = ois.readInt();        // 读取基本类型
String str = ois.readUTF();     // 读取字符串
```

### 5.3.2 readObject详解

```java
// readObject()注意事项：
// 1. 返回Object，需要强制类型转换
// 2. 可能抛出ClassNotFoundException（类找不到）
// 3. 反序列化不会调用构造方法
// 4. transient字段会被赋予默认值（0, null, false）

// 安全反序列化
try (ObjectInputStream ois = new ObjectInputStream(
        new BufferedInputStream(new FileInputStream("data.dat")))) {

    Object obj = ois.readObject();
    if (obj instanceof Person) {
        Person p = (Person) obj;
        // 处理
    }
}
```

---

## 5.4 序列化规则

### 5.4.1 哪些字段会被序列化

```java
// 会序列化的字段：
// - 非transient的实例字段
// - 非static的实例字段

// 不会序列化的字段：
// - transient字段
// - static字段
// - 父类字段（默认不序列化，除非父类也实现Serializable）

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;           // 会序列化
    private int age;               // 会序列化
    private transient String password;  // 不会序列化
    private static String species; // 不会序列化（static）

    private Address address;       // 如果Address实现Serializable，会序列化
}

// transient字段：密码、敏感信息
// static字段：类级别数据，不属于对象
```

### 5.4.2 引用问题

```java
// 同一个对象引用多次出现时：
// - 第一次序列化整个对象
// - 后续只写入引用
// - 反序列化时保持引用关系

public class Node implements Serializable {
    String value;
    Node next;  // 自引用

    public Node(String value) {
        this.value = value;
    }
}

// 序列化和反序列化后，两个next引用指向同一个Node对象
```

### 5.4.3 父类和子类

```java
// 父类字段默认不序列化
public class Person {  // 父类没有实现Serializable
    private String name;
}

public class Employee extends Person implements Serializable {
    private int salary;

    // name字段不会被序列化！
}

// 如果需要序列化父类字段，父类也应实现Serializable
```

---

## 5.5 自定义序列化

### 5.5.1 writeObject和readObject

```java
// 通过writeObject/readObject自定义序列化

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    private transient String password;  // 敏感字段

    // 自定义序列化
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();  // 先序列化非transient字段
        oos.writeUTF(password);    // 自定义处理密码（如加密）
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();   // 先反序列化非transient字段
        password = ois.readUTF();  // 自定义处理（如解密）
    }
}
```

### 5.5.2 Externalizable接口

```java
// Externalizable：完全自定义序列化

public class Person implements Externalizable {
    private String name;
    private int age;

    // 必须有无参构造
    public Person() { }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeInt(age);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        age = in.readInt();
    }
}
```

---

## 5.6 版本控制

### 5.6.1 serialVersionUID

```java
// serialVersionUID：版本控制

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;  // 版本1
    private String name;
    private int age;
}

// 如果类结构改变，应该更新版本号
public class Person implements Serializable {
    private static final long serialVersionUID = 2L;  // 版本2
    private String name;
    private int age;
    private String address;  // 新增字段
}

// 版本不匹配时
// - 旧版本序列化的对象 -> 新版本反序列化：
//   - 新增字段使用默认值
//   - 旧版本没有的字段会丢失
// - 新版本序列化的对象 -> 旧版本反序列化：
//   - 抛出InvalidClassException
```

### 5.6.2 兼容性修改

```java
// 兼容性修改建议：
// - 只添加新字段（使用默认值）
// - 不要删除或重命名字段
// - 不要修改serialVersionUID
// - 不要修改字段类型
// - 不要修改serializableStaticVariable

// 不兼容的修改：
// - 删除字段
// - 重命名字段
// - 修改字段类型
// - 修改类的继承关系
```

---

## 5.7 序列化注意事项

### 5.7.1 单例模式

```java
// 反序列化会破坏单例模式
// 需要实现readResolve方法

public class Singleton implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Singleton INSTANCE = new Singleton();

    private Singleton() { }

    public static Singleton getInstance() {
        return INSTANCE;
    }

    // 反序列化时返回单例
    protected Object readResolve() {
        return INSTANCE;
    }
}
```

### 5.7.2 安全性

```java
// 序列化安全风险：
// - 恶意构造的序列化数据
// - 敏感信息明文存储

// 解决方案：
// - 使用自定义加密
// - 使用白名单（如ObjectInputStream的setExternalValidationHandler）
// - 使用JSON/XML代替（无代码执行风险）

// 安全反序列化（JDK 9+）
ObjectInputStream ois = new ObjectInputStream(in) {
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc)
            throws IOException, ClassNotFoundException {
        // 检查类名
        if (!allowedClasses.contains(desc.getName())) {
            throw new InvalidClassException("Unauthorized class: " + desc.getName());
        }
        return super.resolveClass(desc);
    }
};
```

---

## 5.8 练习题

```java
// 1. 什么是序列化？有什么用途？

// 2. 实现一个序列化的User类，包含用户名、密码（transient）

// 3. 如果一个类实现了Serializable，但它的父类没有，会序列化父类字段吗？

// 4. serialVersionUID的作用是什么？

// 5. 如何自定义序列化过程？
```

---

## 5.9 参考答案

```java
// 1. 序列化
// 将对象转换为字节流的过程，用于存储或传输对象
// 用途：持久化、网络传输、缓存

// 2. User类
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private transient String password;  // 不序列化

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

// 3. 父类字段
// 默认不序列化父类字段
// 如果需要序列化父类，父类也应实现Serializable

// 4. serialVersionUID作用
// 版本控制标识
// 序列化/反序列化时必须匹配
// 不匹配会抛出InvalidClassException

// 5. 自定义序列化
// 实现writeObject和readObject方法
private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
    // 自定义处理
}
private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    oos.defaultReadObject();
    // 自定义处理
}
// 或实现Externalizable接口
```

---

[返回目录](./README.md) | [下一步：NIO](./Step06_NIO.md)
