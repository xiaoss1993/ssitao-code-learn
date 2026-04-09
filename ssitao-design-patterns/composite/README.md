# 组合模式 (Composite Pattern)

## 模式定义

组合模式将对象组合成树形结构以表示"部分-整体"的层次结构，使得用户对单个对象和组合对象的使用具有一致性。

## UML结构

```
┌─────────────────────────┐
│    Component           │
│   <<interface>>        │
├─────────────────────────┤
│ + operation()          │
│ + add(Component)       │
│ + remove(Component)    │
│ + getChild(int)        │
└───────────┬─────────────┘
            △
            │
┌───────────┴─────────────┐
│        Leaf             │
├─────────────────────────┤
│ + operation()           │
└─────────────────────────┘

            │
            │
┌───────────┴─────────────┐
│       Composite         │
├─────────────────────────┤
│ + operation()           │
│ + add(Component)        │
│ + remove(Component)     │
│ + getChild(int)         │
│ - children: List        │
└─────────────────────────┘
```

## 核心思想

```
组合模式 = Component接口 + Leaf + Composite

关键点：
1. Component定义Leaf和Composite的公共接口
2. Leaf是叶子节点，不包含子节点
3. Composite包含子节点，可以递归组合

适用场景：
- 文件系统（文件/文件夹）
- 组织架构（员工/部门）
- GUI组件（按钮/容器）
- 菜单系统（菜单项/子菜单）
```

---

## 示例代码

### 基础示例：文字组合

```java
// 组件接口
public abstract class LetterComposite {
    private List<LetterComposite> children = new ArrayList<>();

    public void add(LetterComposite letter) {
        children.add(letter);
    }

    public int count() {
        return children.size();
    }

    protected abstract void printThisBefore();
    protected abstract void printThisAfter();

    public void print() {
        printThisBefore();
        for (LetterComposite letter : children) {
            letter.print();
        }
        printThisAfter();
    }
}

// 叶子节点：字符
public class Letter extends LetterComposite {
    private char c;

    public Letter(char c) {
        this.c = c;
    }

    @Override
    protected void printThisBefore() {
        System.out.print(c);
    }

    @Override
    protected void printThisAfter() {}
}

// 叶子节点：单词
public class Word extends LetterComposite {
    public Word(List<Letter> letters) {
        for (Letter letter : letters) {
            add(letter);
        }
    }

    @Override
    protected void printThisBefore() {}

    @Override
    protected void printThisAfter() {
        System.out.print(" ");
    }
}

// 组合节点：句子
public class Sentence extends LetterComposite {
    public Sentence(List<Word> words) {
        for (Word word : words) {
            add(word);
        }
    }

    @Override
    protected void printThisBefore() {}

    @Override
    protected void printThisAfter() {
        System.out.println(".");
    }
}

// 使用
Letter l = new Letter('H');
Word w = new Word(Arrays.asList(new Letter('e'), new Letter('l')));
Sentence s = new Sentence(Arrays.asList(w));
s.print();
```

---

## JDK中的应用

### 1. AWT/Swing Component

```java
// Swing使用组合模式
// Component是抽象组件
public abstract class Component {
    private Component parent;
    private List<Component> children = new ArrayList<>();

    public void add(Component c) {
        children.add(c);
        c.parent = this;
    }

    public void remove(Component c) {
        children.remove(c);
        c.parent = null;
    }

    public abstract void paint(Graphics g);
}

// Container继承Component，是组合节点
public class Container extends Component {
    private List<Component> children = new ArrayList<>();

    @Override
    public void paint(Graphics g) {
        for (Component child : children) {
            child.paint(g);
        }
    }
}

// Button是叶子节点
public class Button extends Component {
    @Override
    public void paint(Graphics g) {
        // 绘制按钮
    }
}

// JFrame是具体组合
public class JFrame extends Container {
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // JFrame特定绘制
    }
}
```

### 2. java.xml.dom

```java
// DOM使用组合模式
// Node是抽象组件
public interface Node {
    Node getParentNode();
    NodeList getChildNodes();
    Node appendChild(Node newChild);
    Node removeChild(Node oldChild);
    String getTextContent();
}

// Element是组合节点
public class Element implements Node {
    private Node parent;
    private List<Node> children = new ArrayList<>();
    private Map<String, String> attributes = new HashMap<>();

    @Override
    public NodeList getChildNodes() {
        return new NodeList(children);
    }

    @Override
    public Node appendChild(Node newChild) {
        children.add(newChild);
        return newChild;
    }
}

// Text是叶子节点
public class Text implements Node {
    private String text;
    private Node parent;

    @Override
    public String getTextContent() {
        return text;
    }
}
```

---

## Spring框架中的应用

### 1. BeanDefinition

```java
// Spring使用组合模式管理Bean定义
// BeanDefinition是组件接口
public interface BeanDefinition extends AttributeAccessor {
    String getBeanClassName();
    void setBeanClassName(String beanClassName);
    boolean isSingleton();
    boolean isPrototype();
    String[] getDependsOn();
    void setDependsOn(String... dependsOn);
}

// RootBeanDefinition是组合节点
public class RootBeanDefinition extends AbstractBeanDefinition {
    private BeanDefinition parentName;
    private Map<String, BeanDefinition> innerBeanDefinitions = new HashMap<>();

    @Override
    public boolean isSingleton() {
        return getScope().equals("singleton");
    }
}

// ChildBeanDefinition是叶子节点
public class ChildBeanDefinition extends AbstractBeanDefinition {
    private String parentName;
}
```

### 2. View和ViewGroup

```java
// Spring MVC的视图也使用组合模式
// 视图解析器返回的视图可以组合

// InternalResourceView可以包含其他视图
public class InternalResourceView extends AbstractView {
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        // 渲染逻辑
    }
}

// JstlView继承InternalResourceView
public class JstlView extends InternalResourceView {
    // 支持JSTL标签
}
```

---

## MyBatis中的应用

### 1. SqlNode

```java
// MyBatis使用组合模式处理动态SQL
// SqlNode是组件接口
public interface SqlNode {
    boolean apply(DynamicContext context);
}

// MixedSqlNode是组合节点
public class MixedSqlNode implements SqlNode {
    private final List<SqlNode> contents;

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @Override
    public boolean apply(DynamicContext context) {
        for (SqlNode node : contents) {
            node.apply(context);
        }
        return true;
    }
}

// StaticTextSqlNode是叶子节点
public class StaticTextSqlNode implements SqlNode {
    private final String text;

    @Override
    public boolean apply(DynamicContext context) {
        context.appendSql(text);
        return true;
    }
}

// IfSqlNode是组合节点
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

// WhereSqlNode是组合节点
public class WhereSqlNode extends TrimSqlNode {
    public WhereSqlNode(SqlNode contents) {
        super(contents, "WHERE", "AND", "OR", null);
    }
}

// 使用
MixedSqlNode root = new MixedSqlNode(Arrays.asList(
    new IfSqlNode(expression, contents),
    new WhereSqlNode(contents)
));
root.apply(context);
```

### 2. OgnlCache

```java
// MyBatis使用组合模式处理OGNL表达式
// ExpressionTokenizer处理表达式树
public class ExpressionTokenizer {
    public List<String> tokenize(String expression) {
        // 递归分解表达式
        List<String> tokens = new ArrayList<>();
        tokenize(expression, tokens);
        return tokens;
    }
}
```

---

## 透明组合 vs 安全组合

### 透明组合

在Component中定义所有管理子节点的方法，对Leaf和Composite都透明。

```java
public interface Component {
    void add(Component c);
    void remove(Component c);
    Component getChild(int index);
    void operation();
}

// Leaf必须实现add/remove/getChild，但可以抛出异常或空操作
public class Leaf implements Component {
    public void add(Component c) {
        throw new UnsupportedOperationException();
    }
    // ...
}
```

### 安全组合

只在Composite中定义管理子节点的方法，更安全但不够透明。

```java
public interface Component {
    void operation();
}

public interface Composite extends Component {
    void add(Component c);
    void remove(Component c);
    Component getChild(int index);
}

public class Leaf implements Component {
    public void operation() { /* ... */ }
}
```

---

## 适用场景

1. **树形结构**：表示部分-整体关系
2. **文件系统**：文件和文件夹
3. **组织架构**：部门和员工
4. **GUI组件**：容器和控件
5. **菜单系统**：菜单项和子菜单

## 优点

- **一致性**：Leaf和Composite使用相同接口
- **扩展性**：新增Component不影响现有代码
- **灵活性**：可以动态组合对象

## 缺点

- **复杂性**：设计不当可能导致过度设计
- **限制**：不是所有Component都适合包含子组件

## 对比其他模式

| 模式 | 关系 |
|------|------|
| 组合模式 | 树形结构，部分-整体 |
| 装饰器模式 | 动态增加功能 |
| 迭代器模式 | 遍历组合对象 |
| 访问者模式 | 对组合对象操作 |

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/composite
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.composite.App"
```

### 运行组织架构示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.composite.OrganizationDemo"
```

### 运行菜单示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.composite.MenuDemo"
```

### 运行文件系统示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.composite.FileSystemDemo"
```

### 查看组合模式总结

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.composite.CompositeSummary"
```
