# 访问者模式 (Visitor Pattern)

## 模式定义

访问者模式表示一个作用于某对象结构中的各元素的操作，使你可以在不改变各元素的类的前提下定义作用于这些元素的新操作。

## UML结构

```
┌─────────────────────────┐
│    Visitor             │
│   <<interface>>        │
├─────────────────────────┤
│ + visitElementA()      │
│ + visitElementB()      │
└───────────┬─────────────┘
            │
┌───────────┴─────────────┐
│      Element           │
│   <<interface>>        │
├─────────────────────────┤
│ + accept(Visitor)      │
└───────────┬─────────────┘
            △
            │
┌───────────┴─────────────┐      ┌───────────┴─────────────┐
│    ElementA            │      │    ElementB            │
├─────────────────────────┤      ├─────────────────────────┤
│ + accept(Visitor)       │      │ + accept(Visitor)       │
│ + operationA()          │      │ + operationB()          │
└─────────────────────────┘      └─────────────────────────┘
            │                           │
            └───────────┬───────────────┘
                        ▼
                ┌───────────────┐
                │ ClientCode   │
                │              │
                │ uses Visitor │
                └───────────────┘
```

## 核心思想

```
访问者模式 = Element + Visitor + double dispatch

关键点：
1. Element定义accept方法接受Visitor
2. Visitor定义visit方法处理不同Element
3. double dispatch：Element.accept(Visitor) + Visitor.visit(Element)

问题：需要对一个对象结构添加新操作，但不想修改原有类
解决：
    Element.accept(visitor) {
        visitor.visit(this);  // 调用Visitor的对应方法
    }
```

---

## 示例代码

### 基础示例：军队

```java
// 元素接口
public abstract class Unit {
    private Unit[] children;

    public Unit(Unit... children) {
        this.children = children;
    }

    public void accept(UnitVisitor visitor) {
        for (Unit child : children) {
            child.accept(visitor);
        }
    }
}

// 具体元素：士兵
public class Soldier extends Unit {
    public Soldier(Unit... children) {
        super(children);
    }

    @Override
    public void accept(UnitVisitor visitor) {
        visitor.visitSoldier(this);
        super.accept(visitor);
    }
}

// 具体元素：中士
public class Sergeant extends Unit {
    public Sergeant(Unit... children) {
        super(children);
    }

    @Override
    public void accept(UnitVisitor visitor) {
        visitor.visitSergeant(this);
        super.accept(visitor);
    }
}

// 访问者接口
public interface UnitVisitor {
    void visitSoldier(Soldier soldier);
    void visitSergeant(Sergeant sergeant);
    void visitCommander(Commander commander);
}

// 具体访问者：战斗力统计
public class BattleStatisticsVisitor implements UnitVisitor {
    private int soldierCount = 0;
    private int sergeantCount = 0;

    @Override
    public void visitSoldier(Soldier soldier) {
        soldierCount++;
    }

    @Override
    public void visitSergeant(Sergeant sergeant) {
        sergeantCount++;
    }

    @Override
    public void visitCommander(Commander commander) {
        // 指挥官统计
    }

    public int getTotalCount() {
        return soldierCount + sergeantCount;
    }
}

// 使用
Commander commander = new Commander(
    new Sergeant(
        new Soldier(),
        new Soldier(),
        new Soldier()
    )
);

BattleStatisticsVisitor stats = new BattleStatisticsVisitor();
commander.accept(stats);
```

---

## JDK中的应用

### 1. FileVisitor

```java
// java.nio.file.FileVisitor使用访问者模式
public interface FileVisitor<T> {
    // 访问文件前
    FileVisitResult preVisitDirectory(T dir, BasicFileAttributes attrs);

    // 访问文件
    FileVisitResult visitFile(T file, BasicFileAttributes attrs);

    // 访问文件失败
    FileVisitResult visitFileFailed(T file, IOException exc);

    // 访问目录后
    FileVisitResult postVisitDirectory(T dir, IOException exc);
}

// 简单实现
public class SimpleFileVisitor implements FileVisitor<Path> {
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        System.out.println("Entering: " + dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        System.out.println("File: " + file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        System.out.println("Exiting: " + dir);
        return FileVisitResult.CONTINUE;
    }
}

// 使用
Files.walkFileTree(Paths.get("/path"), new SimpleFileVisitor());
```

### 2. DOM Visitor

```java
// org.w3c.dom使用访问者模式遍历XML/HTML
public interface Node {
    Node getParentNode();
    NodeList getChildNodes();
    // ...

    // accept方法
    default void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}

// 浏览器使用访问者模式渲染DOM树
```

### 3. JavaCompiler

```java
// javax.lang.model使用访问者模式处理AST
public interface ElementVisitor<R, P> {
    R visit(Element e);
    R visit(Element e, P p);
    R visitPackage(PackageElement e, P p);
    R visitType(TypeElement e, P p);
    R visitVariable(VariableElement e, P p);
    R visitExecutable(ExecutableElement e, P p);
    R visitTypeParameter(TypeParameterElement e, P p);
}

// 编译器使用访问者模式处理语法树
public class SimpleElementVisitor implements ElementVisitor<Void, Void> {
    @Override
    public Void visit(Element e, Void p) {
        System.out.println("Element: " + e.getKind());
        return null;
    }
}
```

---

## Spring框架中的应用

### 1. BeanDefinitionVisitor

```java
// Spring使用访问者模式遍历BeanDefinition
public class BeanDefinitionVisitor {
    private String placeholderPrefix = "${";
    private String placeholderSuffix = "}";

    public void visitBeanDefinition(BeanDefinition bd) {
        visitBeanDefinition(bd.getBeanClassName(), bd);
        visitBeanDefinition(bd.getScope(), bd);
        // ...
    }

    protected void visitBeanDefinition(String value, BeanDefinition bd) {
        // 解析占位符
        String resolved = resolveStringValue(value);
        // ...
    }
}

// BeanDefinitionVisitor用于处理@Value注解中的占位符
// PropertyPlaceholderConfigurer也使用访问者模式
```

### 2. SimpleMetadataReader

```java
// Spring使用访问者模式读取类元数据
public class SimpleMetadataReader implements MetadataReader {
    private final ClassMetadata classMetadata;
    private final AnnotationMetadata annotationMetadata;

    public SimpleMetadataReader(InputStream inputStream, ClassLoader classLoader) {
        // 使用ASM访问者模式读取字节码
        ClassReader reader = new ClassReader(inputStream);
        ClassMetadataReadingVisitor visitor = new ClassMetadataReadingVisitor();
        reader.accept(visitor, ClassReader.SKIP_DEBUG);
        // ...
    }
}
```

---

## MyBatis中的应用

### 1. SqlNode

```java
// MyBatis的SqlNode使用访问者模式
// SqlNode是元素
public interface SqlNode {
    boolean apply(DynamicContext context);
}

// SqlNodeVisitor是访问者
public interface SqlNodeVisitor {
    void visit(SqlNode node);
    void visitText(TextSqlNode node);
    void visitIf(IfSqlNode node);
    void visitWhere(WhereSqlNode node);
    void visitForeach(ForEachSqlNode node);
}

// IfSqlNode.accept
public class IfSqlNode implements SqlNode {
    private final ExpressionEvaluator evaluator;
    private final String test;
    private final SqlNode contents;

    @Override
    public boolean apply(DynamicContext context) {
        if (evaluator.evaluateBoolean(test, context.getBindings())) {
            contents.apply(context);
        }
        return true;
    }
}
```

### 2. ObjectWrapper

```java
// MyBatis使用访问者模式处理对象包装
// MetaObject使用访问者模式遍历对象属性
public class MetaObject {
    private Object originalObject;
    private ObjectWrapper wrapper;

    public static MetaObject forObject(Object object) {
        if (object == null) {
            return SystemMetaObject.NULL_META_OBJECT;
        }
        ObjectWrapper wrapper = new BeanWrapper(object);
        return new MetaObject(wrapper);
    }

    // 访问对象属性
    public void setValue(String property, Object value) {
        wrapper.set(property, value);
    }

    public Object getValue(String property) {
        return wrapper.get(property);
    }
}

// MetaClass使用访问者模式收集元信息
public class MetaClass {
    public static MetaClass forClass(Class<?> type) {
        return new MetaClass(type);
    }

    public boolean hasGetter(String name) {
        // 遍历属性
    }
}
```

---

## Double Dispatch

访问者模式的核心是双重分派（double dispatch）：

```java
// 第一重分派：决定调用哪个accept
soldier.accept(visitor);  // 调用 Soldier.accept(visitor)

// 第二重分派：visitor决定调用哪个visit
visitor.visitSoldier(soldier);  // visitor调用 visitSoldier

// 最终执行具体操作
public class Soldier extends Unit {
    @Override
    public void accept(UnitVisitor visitor) {
        visitor.visitSoldier(this);  // this是Soldier类型
    }
}
```

---

## 适用场景

1. **复杂对象结构**：需要对一个对象结构添加操作
2. **操作稳定**：Element类结构稳定，但操作经常变化
3. **类型遍历**：需要遍历不同类型的对象
4. **编译器/解析器**：AST遍历

## 优点

- **开闭原则**：新增Visitor不需修改Element
- **单一职责**：相关操作集中在Visitor
- **灵活扩展**：可以组合多个Visitor

## 缺点

- **复杂性**：增加新Element困难
- **依赖耦合**：Element必须知道所有Visitor
- **类型安全**：visitor方法需要类型检查

## 对比其他模式

| 模式 | 关系 |
|------|------|
| 访问者模式 | 操作封装，双重分派 |
| 组合模式 | 树形结构，部分-整体 |
| 迭代器模式 | 遍历元素 |
| 装饰器模式 | 动态增加功能 |

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/visitor
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.visitor.App"
```

### 运行JDK访问者示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.visitor.jdk.JdkVisitorDemo"
```

### 运行Spring访问者示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.visitor.spring.SpringVisitorDemo"
```

### 运行MyBatis访问者示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.visitor.mybatis.MyBatisVisitorDemo"
```
