# 第8课：反射

## 核心概念

### 8.1 Class类
- 程序运行时，Java虚拟机为所有类创建Class对象
- 获取Class对象的三种方式：
  1. `类名.class`
  2. `对象.getClass()`
  3. `Class.forName("全限定类名")`

### 8.2 反射操作
- 构造器反射：创建对象
- 方法反射：调用方法
- 属性反射：读写属性

### 8.3 动态代理
- Proxy.newProxyInstance()
- InvocationHandler接口

## 代码示例

### 示例1：获取Class对象
```java
public class ClassDemo {
    public static void main(String[] args) throws Exception {
        // 方式1：.class
        Class<String> c1 = String.class;

        // 方式2：getClass()
        String s = "hello";
        Class<? extends String> c2 = s.getClass();

        // 方式3：forName
        Class<?> c3 = Class.forName("java.lang.String");

        // 三种方式获取的是同一个Class对象
        System.out.println(c1 == c2);  // true
        System.out.println(c1 == c3);  // true
    }
}
```

### 示例2：反射创建对象
```java
import java.lang.reflect.*;

public class ReflectConstructorDemo {
    public static void main(String[] args) throws Exception {
        Class<Person> clazz = Person.class;

        // 获取无参构造器
        Constructor<Person> c1 = clazz.getConstructor();
        Person p1 = c1.newInstance();

        // 获取有参构造器
        Constructor<Person> c2 = clazz.getConstructor(String.class, int.class);
        Person p2 = c2.newInstance("张三", 25);

        System.out.println(p1);
        System.out.println(p2);
    }
}

class Person {
    private String name;
    private int age;

    public Person() {}

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}
```

### 示例3：反射调用方法
```java
import java.lang.reflect.*;

public class ReflectMethodDemo {
    public static void main(String[] args) throws Exception {
        Class<Person> clazz = Person.class;
        Person person = clazz.newInstance();

        // 获取方法
        Method setName = clazz.getMethod("setName", String.class);
        Method getName = clazz.getMethod("getName");
        Method speak = clazz.getMethod("speak");

        // 调用方法
        setName.invoke(person, "李四");
        String name = (String) getName.invoke(person);
        speak.invoke(person);

        // 调用私有方法
        Method privateMethod = clazz.getDeclaredMethod("privateMethod");
        privateMethod.setAccessible(true);
        privateMethod.invoke(person);
    }
}

class Person {
    private String name = "默认";

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void speak() {
        System.out.println(name + "正在说话");
    }

    private void privateMethod() {
        System.out.println("私有方法被调用");
    }
}
```

### 示例4：反射读写属性
```java
import java.lang.reflect.*;

public class ReflectFieldDemo {
    public static void main(String[] args) throws Exception {
        Class<Person> clazz = Person.class;
        Person person = new Person("张三", 25);

        // 获取属性
        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");

        // 设置可访问（私有属性）
        nameField.setAccessible(true);
        ageField.setAccessible(true);

        // 读取属性
        String name = (String) nameField.get(person);
        int age = (int) ageField.get(person);
        System.out.println("name=" + name + ", age=" + age);

        // 修改属性
        nameField.set(person, "王五");
        ageField.setInt(person, 30);
        System.out.println(person);
    }
}
```

### 示例5：动态代理
```java
import java.lang.reflect.*;

public class DynamicProxyDemo {
    public static void main(String[] args) {
        // 创建被代理对象
        UserServiceImpl userService = new UserServiceImpl();

        // 创建代理对象
        UserService proxy = (UserService) Proxy.newProxyInstance(
            userService.getClass().getClassLoader(),
            userService.getClass().getInterfaces(),
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("方法 " + method.getName() + " 开始执行");
                    Object result = method.invoke(userService, args);
                    System.out.println("方法 " + method.getName() + " 执行结束");
                    return result;
                }
            }
        );

        proxy.addUser("张三");
        proxy.deleteUser(1);
    }
}

interface UserService {
    void addUser(String name);
    void deleteUser(int id);
}

class UserServiceImpl implements UserService {
    @Override
    public void addUser(String name) {
        System.out.println("添加用户：" + name);
    }

    @Override
    public void deleteUser(int id) {
        System.out.println("删除用户：" + id);
    }
}
```

## 反射应用场景

| 场景 | 说明 |
|------|------|
| 框架 | Spring、Hibernate等大量使用反射 |
| 注解处理 | 注解处理器利用反射读取注解信息 |
| 动态加载 | 根据配置动态加载类 |
| 序列化 | JSON、XML转换库使用反射访问属性 |

## 常见面试题

1. **反射的优缺点？**
   - 优点：灵活、可扩展、框架依赖
   - 缺点：性能损耗、安全问题、代码复杂

2. **Class.forName和ClassLoader的区别？**
   - forName：加载类并执行静态初始化块
   - ClassLoader：只加载类，不执行初始化

3. **反射能访问私有成员吗？**
   - 可以，通过setAccessible(true)设置可访问

## 练习题

1. 使用反射实现对象拷贝（类似clone）
2. 编写一个通用toString方法（反射版）
3. 使用动态代理实现方法耗时统计

## 要点总结

- Class对象是反射的入口
- 反射可以动态操作类
- 私有成员需要setAccessible
- 动态代理在框架中广泛应用
- 反射带来灵活性的同时也带来开销
