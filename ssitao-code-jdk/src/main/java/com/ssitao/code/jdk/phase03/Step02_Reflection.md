# 步骤2：反射 - 运行时动态操作

---

## 2.1 反射概述

### 2.1.1 什么是反射

```
反射（Reflection）：在运行时动态获取类的信息并操作对象

正常流程：编译时知道类 -> 创建对象 -> 调用方法
反射流程：运行时才知道类 -> 获取Class -> 创建对象/调用方法

反射的核心类：
- java.lang.Class
- java.lang.reflect.Field
- java.lang.reflect.Method
- java.lang.reflect.Constructor
```

### 2.1.2 Class对象

```java
// Class对象是反射的入口
// 每个类在编译后会生成一个Class对象

// 三种获取Class对象的方式
Class<?> clazz1 = Class.forName("com.example.Person");  // 1. 通过类名字符串
Class<?> clazz2 = Person.class;                          // 2. 通过.class语法
Class<?> clazz3 = person.getClass();                     // 3. 通过对象

// 同一个类的Class对象是单例的
Person p1 = new Person();
Person p2 = new Person();
p1.getClass() == p2.getClass();  // true
```

### 2.1.3 Class对象的常用方法

```java
Class<?> clazz = Person.class;

// 获取类信息
clazz.getName();           // 全限定名：com.example.Person
clazz.getSimpleName();     // 简单类名：Person
clazz.getPackage();        // 包信息
clazz.getSuperclass();     // 父类
clazz.getInterfaces();     // 实现的接口
clazz.getModifiers();      // 访问修饰符

// 判断类型
clazz.isArray();           // 是否是数组
clazz.isEnum();            // 是否是枚举
clazz.isInterface();       // 是否是接口
clazz.isPrimitive();       // 是否是基本类型
clazz.isAnnotation();      // 是否是注解
```

---

## 2.2 运行时获取类信息

### 2.2.1 获取构造方法

```java
Class<?> clazz = Person.class;

// 获取所有public构造方法
Constructor<?>[] constructors = clazz.getConstructors();

// 获取所有构造方法（包括private）
Constructor<?>[] allConstructors = clazz.getDeclaredConstructors();

// 获取指定参数类型的构造方法
Constructor<?> constructor = clazz.getConstructor(String.class, int.class);

// 获取private构造方法
Constructor<?> privateConstructor = clazz.getDeclaredConstructor(String.class);
```

### 2.2.2 获取成员变量

```java
Class<?> clazz = Person.class;

// 获取所有public字段（包括继承的）
Field[] fields = clazz.getFields();

// 获取所有字段（包括private，不包括继承的）
Field[] declaredFields = clazz.getDeclaredFields();

// 获取指定名称的public字段
Field nameField = clazz.getField("name");

// 获取指定名称的字段（包括private）
Field ageField = clazz.getDeclaredField("age");
```

### 2.2.3 获取成员方法

```java
Class<?> clazz = Person.class;

// 获取所有public方法（包括继承的）
Method[] methods = clazz.getMethods();

// 获取所有方法（包括private，不包括继承的）
Method[] declaredMethods = clazz.getDeclaredMethods();

// 获取指定名称和参数类型的方法
Method setNameMethod = clazz.getMethod("setName", String.class);
Method getNameMethod = clazz.getMethod("getName");

// 获取private方法
Method privateMethod = clazz.getDeclaredMethod("privateMethod");
```

---

## 2.3 动态创建对象

### 2.3.1 使用Constructor创建对象

```java
Class<?> clazz = Person.class;

// 使用public构造方法创建对象
Constructor<?> constructor = clazz.getConstructor(String.class, int.class);
Person person = (Person) constructor.newInstance("Alice", 25);

// 使用private构造方法创建对象（需要设置访问权限）
Constructor<?> privateConstructor = clazz.getDeclaredConstructor(String.class);
privateConstructor.setAccessible(true);  // 打破封装
Person person2 = (Person) privateConstructor.newInstance("Bob");
```

### 2.3.2 使用Class创建对象

```java
// 使用默认构造方法
Class<?> clazz = Class.forName("com.example.Person");
Person person = (Person) clazz.newInstance();  // 调用无参构造

// 注意：如果没有无参构造或构造方法是private，会抛出异常
```

### 2.3.3 工厂模式与反射

```java
// 反射实现工厂模式
class Factory {
    public static <T> T create(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

// 使用
Vehicle car = Factory.create(Car.class);
Vehicle bike = Factory.create(Bike.class);
```

---

## 2.4 动态调用方法

### 2.4.1 调用public方法

```java
Person person = new Person("Alice", 25);
Class<?> clazz = person.getClass();

// 调用getName方法
Method getNameMethod = clazz.getMethod("getName");
String name = (String) getNameMethod.invoke(person);

// 调用setName方法
Method setNameMethod = clazz.getMethod("setName", String.class);
setNameMethod.invoke(person, "Bob");

// 调用静态方法
Method staticMethod = clazz.getMethod("staticMethod");
staticMethod.invoke(null);  // 静态方法不需要对象实例
```

### 2.4.2 调用private方法

```java
Person person = new Person("Alice", 25);
Class<?> clazz = person.getClass();

// 获取private方法
Method privateMethod = clazz.getDeclaredMethod("privateMethod");
privateMethod.setAccessible(true);  // 打破封装

// 调用
privateMethod.invoke(person);
```

### 2.4.3 调用可变参数方法

```java
Class<?> clazz = Person.class;
Method method = clazz.getMethod("printArgs", String[lass);

// JDK 9之前需要这样处理
method.invoke(person, new Object[] { new String[] {"a", "b", "c"} });

// JDK 9+
method.invoke(person, (Object) new String[] {"a", "b", "c"});
```

---

## 2.5 动态操作字段

### 2.5.1 读取字段值

```java
Person person = new Person("Alice", 25);
Class<?> clazz = person.getClass();

// 读取public字段
Field nameField = clazz.getField("name");
String name = (String) nameField.get(person);

// 读取private字段
Field ageField = clazz.getDeclaredField("age");
ageField.setAccessible(true);
int age = (int) ageField.get(person);

// 读取静态字段
Field staticField = clazz.getDeclaredField("COUNT");
staticField.setAccessible(true);
Object value = staticField.get(null);  // 静态字段不需要对象实例
```

### 2.5.2 修改字段值

```java
Person person = new Person("Alice", 25);
Class<?> clazz = person.getClass();

// 修改public字段
Field nameField = clazz.getField("name");
nameField.set(person, "Bob");

// 修改private字段
Field ageField = clazz.getDeclaredField("age");
ageField.setAccessible(true);
ageField.set(person, 30);

// 修改静态字段
Field staticField = clazz.getDeclaredField("COUNT");
staticField.setAccessible(true);
staticField.set(null, 100);
```

---

## 2.6 反射的典型应用

### 2.6.1 JSON序列化框架

```java
// 简化的JSON序列化实现
class JsonSerializer {
    public static String serialize(Object obj) throws Exception {
        Class<?> clazz = obj.getClass();
        StringBuilder sb = new StringBuilder("{");

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);

            sb.append("\"").append(fieldName).append("\":");
            if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else {
                sb.append(value);
            }
            sb.append(",");
        }

        sb.setLength(sb.length() - 1);  // 移除最后一个逗号
        sb.append("}");
        return sb.toString();
    }
}

// 使用
Person person = new Person("Alice", 25);
String json = JsonSerializer.serialize(person);
```

### 2.6.2 依赖注入容器

```java
// 简化的依赖注入容器
class Container {
    private Map<Class<?>, Object> beans = new HashMap<>();

    public void register(Class<?> clazz, Object instance) {
        beans.put(clazz, instance);
    }

    public <T> T get(Class<T> clazz) {
        return clazz.cast(beans.get(clazz));
    }

    public void autowire(Object obj) throws Exception {
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                Object dependency = get(field.getType());
                field.set(obj, dependency);
            }
        }
    }
}

// 使用
@Retention(RetentionPolicy.RUNTIME)
@interface Autowired { }

class ContainerDemo {
    public static void main(String[] args) throws Exception {
        Container container = new Container();
        container.register(PersonDAO.class, new PersonDAOImpl());
        container.register(PersonService.class, new PersonServiceImpl());

        PersonService service = container.get(PersonService.class);
        container.autowire(service);
    }
}
```

### 2.6.3 通用对象拷贝

```java
// 使用反射实现对象拷贝
class ReflectionUtil {
    public static void copyProperties(Object src, Object dest) throws Exception {
        Class<?> clazz = src.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(src);
            field.set(dest, value);
        }
    }
}
```

---

## 2.7 反射与注解

### 2.7.1 获取注解信息

```java
// 定义注解
@Retention(RetentionPolicy.RUNTIME)
@interface Column {
    String name();
}

@Retention(RetentionPolicy.RUNTIME)
@interface Table {
    String name();
}

// 使用注解
@Table(name = "users")
class User {
    @Column(name = "user_name")
    private String name;

    @Column(name = "user_age")
    private int age;
}

// 通过反射获取注解
Class<?> clazz = User.class;

// 获取类注解
Table table = clazz.getAnnotation(Table.class);
if (table != null) {
    System.out.println("Table name: " + table.name());
}

// 获取字段注解
for (Field field : clazz.getDeclaredFields()) {
    Column column = field.getAnnotation(Column.class);
    if (column != null) {
        System.out.println("Column: " + column.name() + " -> " + field.getName());
    }
}
```

### 2.7.2 注解处理器示例

```java
// 获取所有带特定注解的方法
class AnnotationProcessor {
    public static void processMethodsWithAnnotation(
            Class<?> clazz, Class<? extends Annotation> annotationClass) {

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                System.out.println("Found: " + method.getName());
                // 处理注解...
            }
        }
    }
}
```

---

## 2.8 反射的性能

### 2.8.1 反射的性能问题

```java
// 反射的性能开销
// 1. 检查访问权限
// 2. 类型检查
// 3. 方法签名解析
// 4. 自动装箱/拆箱（如果涉及基本类型）

// 性能对比示例
class PerformanceDemo {
    public static void main(String[] args) throws Exception {
        int iterations = 1000000;

        // 直接调用
        Person person = new Person("Alice", 25);
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            person.getName();
        }
        System.out.println("Direct call: " + (System.nanoTime() - start) / 1_000_000 + "ms");

        // 反射调用
        Method getNameMethod = Person.class.getMethod("getName");
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            getNameMethod.invoke(person);
        }
        System.out.println("Reflection call: " + (System.nanoTime() - start) / 1_000_000 + "ms");
    }
}
```

### 2.8.2 优化反射性能

```java
// 优化1：缓存Method/Field对象
class CachedReflection {
    private static final Map<String, Method> METHOD_CACHE = new ConcurrentHashMap<>();

    public static Method getMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        String key = clazz.getName() + "." + name + Arrays.toString(paramTypes);
        return METHOD_CACHE.computeIfAbsent(key, k -> {
            try {
                return clazz.getMethod(name, paramTypes);
            } catch (NoSuchMethodException e) {
                return null;
            }
        });
    }
}

// 优化2：使用setAccessible(true)
// 关闭字节码访问检查，提升性能

// 优化3：MethodHandles（JDK 7+）
// 比反射更快的动态调用
class MethodHandlesDemo {
    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = lookup.findVirtual(Person.class, "getName", MethodType.methodType(String.class));

        Person person = new Person("Alice", 25);
        String name = (String) mh.invokeExact(person);
        System.out.println(name);
    }
}
```

---

## 2.9 反射的注意事项

### 2.9.1 安全性问题

```java
// 反射可以打破封装，访问private成员
// 在安全管理器下可能受限

// 沙箱环境中禁止反射的例子
class SecurityDemo {
    public static void main(String[] args) throws Exception {
        // 在安全管理器下可能被拒绝
        Class<?> clazz = Class.forName("java.lang.System");
        Method exitMethod = clazz.getMethod("exit", int.class);
        exitMethod.invoke(null, 0);  // 可能被禁止！
    }
}
```

### 2.9.2 泛型擦除后的反射

```java
// 泛型信息在运行时被擦除
class GenericReflection<T> {
    T value;
}

// 获取的是原始类型
class Demo {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = GenericReflection.class;
        Field valueField = clazz.getDeclaredField("value");

        // value字段类型是Object，不是T
        System.out.println(valueField.getType());  // class java.lang.Object
    }
}
```

---

## 2.10 练习题

```java
// 1. 使用反射实现：给定一个对象，返回其所有字段名和值的Map

// 2. 使用反射实现：一个简单的对象克隆方法

// 3. 以下代码的输出是什么？
class Test {
    private int value = 10;
    public void print() { System.out.println(value); }
}

public class Exercise3 {
    public static void main(String[] args) throws Exception {
        Test obj = new Test();
        Field field = Test.class.getDeclaredField("value");
        field.setAccessible(true);
        System.out.println(field.get(obj));
    }
}

// 4. 说明反射在Spring框架中的应用

// 5. 实现一个通用的toString方法，使用反射打印对象所有字段
```

---

## 2.11 参考答案

```java
// 1. 获取对象所有字段名和值
public static Map<String, Object> getFieldsAndValues(Object obj) throws Exception {
    Map<String, Object> result = new LinkedHashMap<>();
    Class<?> clazz = obj.getClass();
    for (Field field : clazz.getDeclaredFields()) {
        field.setAccessible(true);
        result.put(field.getName(), field.get(obj));
    }
    return result;
}

// 2. 使用反射实现克隆
public static Object cloneObject(Object obj) throws Exception {
    Class<?> clazz = obj.getClass();
    Object clone = clazz.getDeclaredConstructor().newInstance();
    for (Field field : clazz.getDeclaredFields()) {
        field.setAccessible(true);
        Object value = field.get(obj);
        if (value instanceof Cloneable && field.getType().isPrimitive()) {
            // 对于基本类型和不可变对象，直接复制引用即可
        }
        field.set(clone, value);
    }
    return clone;
}

// 3. 输出: 10
// field.get(obj)读取的是obj对象的value字段值

// 4. Spring中反射的应用：
//    - BeanWrapper：使用反射设置属性值
//    - ComponentScan：扫描类上的注解
//    - Autowired：注入依赖
//    - AOP：动态代理

// 5. 通用toString
public static String toString(Object obj) {
    if (obj == null) return "null";
    StringBuilder sb = new StringBuilder();
    Class<?> clazz = obj.getClass();
    sb.append(clazz.getSimpleName()).append("{");
    for (Field field : clazz.getDeclaredFields()) {
        field.setAccessible(true);
        sb.append(field.getName()).append("=");
        try {
            sb.append(field.get(obj));
        } catch (IllegalAccessException e) {
            sb.append("?");
        }
        sb.append(", ");
    }
    if (sb.length() > clazz.getSimpleName().length() + 1) {
        sb.setLength(sb.length() - 2);
    }
    sb.append("}");
    return sb.toString();
}
```

---

[返回目录](./README.md)

## 第三阶段总结

### 核心知识点

| 步骤 | 主题 | 核心概念 |
|------|------|----------|
| 1 | 泛型 | 类型擦除、通配符PECS、泛型方法 |
| 2 | 反射 | Class对象、动态调用、安全性 |
