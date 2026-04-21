# 抽象工厂模式在JDK、Spring、MyBatis中的应用

## 概述

抽象工厂模式（Abstract Factory Pattern）是一种创建型设计模式，它提供了一个接口，用于创建相关或依赖对象的家族，而不需要明确指定具体类。

## 一、JDK中的抽象工厂模式

### 1. DocumentBuilderFactory
```java
// 抽象工厂
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
// 创建具体产品
DocumentBuilder builder = factory.newDocumentBuilder();
```

### 2. SAXParserFactory
```java
SAXParserFactory factory = SAXParserFactory.newInstance();
SAXParser parser = factory.newSAXParser();
```

### 3. TransformerFactory
```java
TransformerFactory factory = TransformerFactory.newInstance();
Transformer transformer = factory.newTransformer();
```

### 4. Charset (SPI机制)
```java
Charset utf8 = Charset.forName("UTF-8");
```

### 5. ServiceLoader
```java
ServiceLoader<FileSystemProvider> providers =
    ServiceLoader.load(FileSystemProvider.class);
```

## 二、Spring中的抽象工厂模式

### 1. BeanFactory（核心工厂）
```java
BeanFactory factory = new DefaultListableBeanFactory();
User user = factory.getBean(User.class);
```

### 2. ApplicationContext（扩展工厂）
```java
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
UserService service = context.getBean(UserService.class);
```

### 3. AutowireCapableBeanFactory（自动装配工厂）
```java
AutowireCapableBeanFactory factory = ...;
OrderService service = factory.createBean(OrderService.class);
```

### 4. ObjectProvider（延迟查找工厂）
```java
ObjectProvider<UserService> provider = context.getBeanProvider(UserService.class);
UserService service = provider.getIfAvailable();
```

## 三、MyBatis中的抽象工厂模式

### 1. SqlSessionFactory（SqlSession工厂）
```java
SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(config);
SqlSession session = factory.openSession();
```

### 2. ObjectFactory（对象创建工厂）
```java
ObjectFactory factory = new DefaultObjectFactory();
User user = factory.create(User.class);
```

### 3. DataSourceFactory（数据源工厂）
```java
// Unpooled数据源工厂
UnpooledDataSourceFactory factory = new UnpooledDataSourceFactory();
DataSource dataSource = factory.getDataSource();

// Pooled数据源工厂
PooledDataSourceFactory pooledFactory = new PooledDataSourceFactory();
```

### 4. TransactionFactory（事务工厂）
```java
// JDBC事务工厂
TransactionFactory jdbcFactory = new JdbcTransactionFactory();
Transaction tx = jdbcFactory.newTransaction(dataSource, ...);

// Managed事务工厂
TransactionFactory managedFactory = new ManagedTransactionFactory();
```

### 5. MapperProxyFactory（Mapper代理工厂）
```java
// 内部使用，创建Mapper接口的代理实例
configuration.addMapper(UserMapper.class);
UserMapper mapper = sqlSession.getMapper(UserMapper.class);
```

### 6. TypeHandlerRegistry（类型处理器工厂）
```java
TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
TypeHandler<String> handler = registry.getTypeHandler(String.class);
```

## 四、抽象工厂模式结构

```
                    AbstractFactory
                           |
                           +--- createProductA()
                           +--- createProductB()
                                 |
                --------------------------------
                |                              |
      ConcreteFactory1              ConcreteFactory2
                |                              |
                +--- createProductA()          +--- createProductA()
                +--- createProductB()          +--- createProductB()
```

## 五、使用场景总结

| 框架 | 抽象工厂 | 产品族 | 典型应用 |
|-----|---------|-------|---------|
| JDK | DocumentBuilderFactory | DocumentBuilder | XML解析 |
| JDK | SAXParserFactory | SAXParser | 事件驱动XML解析 |
| JDK | TransformerFactory | Transformer | XML转换 |
| Spring | BeanFactory | Bean对象 | IoC容器 |
| Spring | ApplicationContext | Bean+环境信息 | 企业级IoC |
| MyBatis | SqlSessionFactory | SqlSession | 数据库会话 |
| MyBatis | DataSourceFactory | DataSource | 连接管理 |
| MyBatis | TransactionFactory | Transaction | 事务管理 |

## 六、运行示例

每个包下都有对应的Demo类，可以直接运行：

```bash
# JDK示例
java com.ssitao.code.designpattern.abstractfactory.jdk.AbstractFactoryDemo

# Spring示例
java com.ssitao.code.designpattern.abstractfactory.spring.AbstractFactoryDemo

# MyBatis示例
java com.ssitao.code.designpattern.abstractfactory.mybatis.AbstractFactoryDemo
```
