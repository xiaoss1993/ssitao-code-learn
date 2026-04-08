# 第三阶段：泛型与反射

## 学习目标

掌握Java泛型的使用和原理，理解反射机制及其在框架设计中的应用。

---

## 步骤列表

| 步骤 | 主题 | 文档 | 代码 |
|------|------|------|------|
| 1 | 泛型 | [Step01_Generics.md](./Step01_Generics.md) | [generics/*.java](./generics/) |
| 2 | 反射 | [Step02_Reflection.md](./Step02_Reflection.md) | [reflection/*.java](./reflection/) |

---

## 核心概念概览

### 泛型（Generics）

```
泛型的核心目的：编译时类型安全检测

泛型类: class Box<T> { }
泛型接口: interface Pair<K, V> { }
泛型方法: <T> T getFirst(List<T> list) { }

类型参数:
  T - Type
  K - Key
  V - Value
  E - Element
  N - Number
  ? - 通配符

通配符:
  <?>       - 无界通配符
  <? extends T> - 上界通配符
  <? super T>   - 下界通配符

类型擦除:
  泛型信息在编译后被擦除
  Object -> T (或指定上界)
```

### 反射（Reflection）

```
反射的核心目的：运行时动态操作对象

Class对象: 代表类的元数据
  - Class.forName("类名")
  - 类名.class
  - 对象.getClass()

关键类:
  java.lang.Class
  java.lang.reflect.Field
  java.lang.reflect.Method
  java.lang.reflect.Constructor
  java.lang.reflect.Modifier

常用场景:
  - 框架设计（Spring、Hibernate）
  - 注解处理器
  - 动态代理
  - 依赖注入
```

---

## 学习建议

1. **泛型**: 重点理解类型擦除和通配符的上下界
2. **反射**: 理解Class对象的获取方式和动态调用
3. **实践**: 通过框架源码理解反射的应用

---

## 运行代码

```bash
cd ssitao-code-jdk
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase03.generics.GenericBoxDemo"
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase03.reflection.ReflectionDemo"
```

---

## 下一步

[第四阶段：Lambda与Stream API](../phase04/README.md)
