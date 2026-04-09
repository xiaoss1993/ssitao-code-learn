# 建造者模式 (Builder Pattern)

## 模式定义

建造者模式将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。

## UML结构

```
┌─────────────────┐       ┌─────────────────┐
│    Director     │──────▶│    Builder     │
│                 │       │  <<interface>> │
├─────────────────┤       └────────┬────────┘
│ + construct()  │                │
└─────────────────┘        ┌──────┴──────┐
                            │             │
                   ┌────────┴────┐ ┌──────┴─────┐
                   │ ConcreteA  │ │ ConcreteB  │
                   │ +build()   │ │ +build()   │
                   └─────────────┘ └─────────────┘
                            │             │
                            └──────┬──────┘
                                   ▼
                           ┌─────────────┐
                           │   Product   │
                           │             │
                           └─────────────┘
```

## 核心思想

```
建造者模式 = 产品 + 抽象建造者 + 具体建造者 + 指挥者

问题：创建复杂对象，构造函数参数过多，可选参数处理困难
解决：
    Product (产品)
        └── 包含多个组件
    Builder (抽象建造者)
        ├── buildPartA()
        ├── buildPartB()
        └── getResult()
    ConcreteBuilder (具体建造者)
        └── 实现各部件的构建
    Director (指挥者)
        └── 决定构建顺序
```

---

## 示例代码

### 基础示例：角色创建

```java
// 产品：角色
public class Hero {
    private final Profession profession;
    private final String name;
    private final HairType hairType;
    private final HairColor hairColor;
    private final Armor armor;
    private final Weapon weapon;

    private Hero(Builder builder) {
        this.profession = builder.profession;
        this.name = builder.name;
        this.hairType = builder.hairType;
        this.hairColor = builder.hairColor;
        this.armor = builder.armor;
        this.weapon = builder.weapon;
    }

    // 静态内部Builder类
    public static class Builder {
        private Profession profession;
        private String name;
        private HairType hairType;
        private HairColor hairColor;
        private Armor armor;
        private Weapon weapon;

        public Builder profession(Profession profession) {
            this.profession = profession;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder hairType(HairType hairType) {
            this.hairType = hairType;
            return this;
        }

        public Builder hairColor(HairColor hairColor) {
            this.hairColor = hairColor;
            return this;
        }

        public Builder armor(Armor armor) {
            this.armor = armor;
            return this;
        }

        public Builder weapon(Weapon weapon) {
            this.weapon = weapon;
            return this;
        }

        public Hero build() {
            return new Hero(this);
        }
    }
}

// 使用
Hero warrior = new Hero.Builder()
    .profession(Profession.WARRIOR)
    .name("阿喀琉斯")
    .hairType(HairType.SHORT)
    .armor(Armor.CHAIN_MAIL)
    .weapon(Weapon.SWORD)
    .build();
```

---

## JDK中的应用

### 1. StringBuilder / StringBuffer

```java
// StringBuilder是最常见的建造者模式应用
StringBuilder sb = new StringBuilder();
sb.append("Hello")
  .append(" ")
  .append("World")
  .append("!");
String result = sb.toString();

// append方法返回StringBuilder，支持链式调用
```

### 2. DocumentBuilder

```java
// javax.xml.parsers.DocumentBuilder使用建造者模式
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
Document doc = builder.newDocument();
doc.appendChild(doc.createElement("root"));
```

### 3. Locale

```java
// Locale使用静态工厂方法（类似建造者）
Locale locale = new Locale.Builder()
    .setLanguage("zh")
    .setScript("Hans")
    .setRegion("CN")
    .build();
```

### 4. Stream.Builder

```java
// Java 8引入的Stream.Builder
Stream.Builder<String> builder = Stream.builder();
builder.add("a")
       .add("b")
       .add("c");
Stream<String> stream = builder.build();
```

---

## Spring框架中的应用

### 1. UriComponentsBuilder

```java
// UriComponentsBuilder使用建造者模式构建URI
UriComponents uriComponents = UriComponentsBuilder.newInstance()
    .scheme("https")
    .host("api.example.com")
    .path("/users")
    .queryParam("page", 1)
    .queryParam("size", 20)
    .build()
    .expand(variables)
    .encode();

// 生成: https://api.example.com/users?page=1&size=20
```

### 2. MockMvcWebRequestBuilder

```java
// Spring Test中的MockMvc使用建造者模式
MvcResult result = MockMvcBuilders.webAppContextSetup(context)
    .defaultRequest(get("/").accept(MediaType.APPLICATION_JSON))
    .build()
    .perform(get("/api/users")
        .param("page", "1")
        .accept(MediaType.APPLICATION_JSON))
    .andExpect(status().isOk())
    .andReturn();
```

### 3. UriBuilder

```java
// UriComponentsBuilder实现了UriBuilder接口
UriBuilder uriBuilder = UriComponentsBuilder.newInstance()
    .scheme("http")
    .host("localhost")
    .port(8080)
    .path("/api");

URI uri = uriBuilder.build();
```

---

## MyBatis中的应用

### 1. SqlSessionFactoryBuilder

```java
// MyBatis使用建造者模式构建SqlSessionFactory
SqlSessionFactory factory = new SqlSessionFactoryBuilder()
    .build(inputStream);  // 使用InputStream构建

// 也可以使用XMLConfigBuilder
SqlSessionFactory factory = new SqlSessionFactoryBuilder()
    .build(reader, environment, properties);

// 内部使用XMLConfigBuilder
private SqlSessionFactory build(InputStream inputStream) {
    XMLConfigBuilder parser = new XMLConfigBuilder(inputStream);
    return build(parser.parse());
}
```

### 2. XMLConfigBuilder

```java
// XMLConfigBuilder使用建造者模式解析配置
public class XMLConfigBuilder extends BaseBuilder {
    private final XPathParser parser;
    private String environment;

    public Configuration parse() {
        parseConfiguration(parser.evalNode("/configuration"));
        return configuration;
    }

    private void parseConfiguration(XNode root) {
        propertiesElement(root.evalNode("properties"));
        typeAliasesElement(root.evalNode("typeAliases"));
        pluginElement(root.evalNode("plugins"));
        objectFactoryElement(root.evalNode("objectFactory"));
        objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
        settingsElement(evalNode);
        environmentsElement(root.evalNode("environments"));
        // ...
    }
}
```

### 3. SqlBuilder (MyBatis内部)

```java
// MyBatis内部也使用类似的建造器
// 这是一个模拟示例，展示MyBatis如何构建SQL
public class SelectBuilder {
    private StringBuilder sql = new StringBuilder();

    public SelectBuilder select(String... columns) {
        sql.append("SELECT ");
        sql.append(String.join(", ", columns));
        return this;
    }

    public SelectBuilder from(String table) {
        sql.append(" FROM ").append(table);
        return this;
    }

    public SelectBuilder where(String condition) {
        sql.append(" WHERE ").append(condition);
        return this;
    }

    public String build() {
        return sql.toString();
    }
}

// 使用
String sql = new SelectBuilder()
    .select("id", "name", "email")
    .from("users")
    .where("id = 1")
    .build();
// SELECT id, name, email FROM users WHERE id = 1
```

---

## 建造者模式变体

### 1. 链式调用 (Fluent Builder)

```java
// 链式调用是最常见的变体
public class Person {
    private String name;
    private int age;
    private String address;

    private Person(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.address = builder.address;
    }

    public static class Builder {
        private String name;
        private int age;
        private String address;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }
}
```

### 2. 递归泛型建造者

```java
// 递归泛型支持更复杂的链式调用
public class PersonBuilder<SELF extends PersonBuilder<SELF>> {
    protected Person person = new Person();

    public SELF withName(String name) {
        person.setName(name);
        return self();
    }

    public SELF withAge(int age) {
        person.setAge(age);
        return self();
    }

    protected SELF self() {
        return (SELF) this;
    }
}

public class EmployeeBuilder extends PersonBuilder<EmployeeBuilder> {
    public EmployeeBuilder withCompany(String company) {
        person.setCompany(company);
        return this;
    }
}

// 使用
Employee employee = new EmployeeBuilder()
    .withName("张三")
    .withAge(30)
    .withCompany("某公司")
    .build();
```

---

## 适用场景

1. **复杂对象构建**：构造函数参数过多
2. **可选参数多**：使用重载构造函数不优雅
3. **创建过程固定**：但表示可以变化
4. **不可变对象**：需要Builder来构建

## 优点

- **代码清晰**：链式调用易于阅读
- **参数明确**：命名参数不会混淆
- **不可变对象**：保证线程安全
- **灵活性**：可以构建不同表示

## 缺点

- **复杂性**：需要创建多个类
- **开销**：比直接构造函数稍慢
- **冗长**：简单场景可能过度设计

## 对比工厂模式

| 特性 | 建造者模式 | 抽象工厂 |
|------|-----------|----------|
| 目的 | 构建复杂对象 | 创建产品族 |
| 参数 | 逐步设置 | 一次性创建 |
| 产品 | 单个产品 | 多个相关产品 |
| 过程 | 逐步构建 | 一次性创建 |

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/builder
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.builder.App"
```

### 运行JDK建造者示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.builder.jdk.JdkBuilderDemo"
```

### 运行Spring建造者示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.builder.spring.SpringBuilderDemo"
```

### 运行MyBatis建造者示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.builder.mybatis.MyBatisBuilderDemo"
```
