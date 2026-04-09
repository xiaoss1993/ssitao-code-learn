# 备忘录模式 (Memento Pattern)

## 模式定义

备忘录模式在不破坏封装性的前提下，捕获一个对象的内部状态，并在这个对象之外保存这个状态，这样以后就可将该对象恢复到原先保存的状态。

## UML结构

```
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│     Originator  │──────▶│    Memento     │◀──────│     Caretaker   │
│                 │       │                 │       │                 │
│ + createMemento │       │ - state        │       │ + memento       │
│ + restore()    │       │ + getState()   │       │ + setMemento()  │
└─────────────────┘       └─────────────────┘       └─────────────────┘
```

## 核心思想

```
备忘录模式 = Originator + Memento + Caretaker

关键点：
1. Originator创建Memento保存当前状态
2. Memento存储Originator的内部状态
3. Caretaker保管Memento，但不能操作其内容
4. 恢复状态时，Originator从Memento获取状态

应用场景：
- 撤销/恢复功能
- 游戏存档
- 数据库事务回滚
- 快照功能
```

---

## 示例代码

### 基础示例：星星演化

```java
// 备忘录接口
public interface StarMemento {
}

// 发起人：星星
public class Star {
    private StarType type;
    private int ageYears;
    private int massTons;

    public Star(StarType startType, int startAge, int startMass) {
        this.type = startType;
        this.ageYears = startAge;
        this.massTons = startMass;
    }

    // 时间流逝，状态改变
    public void timePasses() {
        ageYears *= 2;
        massTons *= 8;
        switch (type) {
            case RED_GIANT:
                type = StarType.WHITE_DWARF;
                break;
            case SUN:
                type = StarType.RED_GIANT;
                break;
            // ...
        }
    }

    // 创建备忘录
    public StarMemento getMemento() {
        return new StarMementoInternal(type, ageYears, massTons);
    }

    // 恢复状态
    public void setMemento(StarMemento memento) {
        StarMementoInternal state = (StarMementoInternal) memento;
        this.type = state.getType();
        this.ageYears = state.getAgeYears();
        this.massTons = state.getMassTons();
    }

    // 备忘录实现（内部类，私有）
    private static class StarMementoInternal implements StarMemento {
        private StarType type;
        private int ageYears;
        private int massTons;

        public StarMementoInternal(StarType type, int ageYears, int massTons) {
            this.type = type;
            this.ageYears = ageYears;
            this.massTons = massTons;
        }

        public StarType getType() { return type; }
        public int getAgeYears() { return ageYears; }
        public int getMassTons() { return massTons; }
    }
}

// 负责人：管理备忘录
public class StarCaretaker {
    private List<StarMemento> mementos = new ArrayList<>();

    public void saveMemento(StarMemento memento) {
        mementos.add(memento);
    }

    public StarMemento getMemento(int index) {
        return mementos.get(index);
    }
}

// 使用
Star sun = new Star(StarType.SUN, 1000000, 500000);
StarCaretaker caretaker = new StarCaretaker();

caretaker.saveMemento(sun.getMemento());
sun.timePasses();
System.out.println(sun);  // 当前状态

sun.setMemento(caretaker.getMemento(0));  // 恢复到之前状态
System.out.println(sun);  // 恢复后的状态
```

---

## JDK中的应用

### 1. java.util.Date

```java
// Date内部存储状态，可以看作备忘录模式的应用
public class Date implements Serializable, Cloneable, Comparable<Date> {
    private transient long fastTime;

    // clone方法创建对象副本
    public Object clone() {
        Date d = new Date();
        d.fastTime = this.fastTime;
        return d;
    }
}

// 使用clone进行状态保存
Date now = new Date();
Date backup = (Date) now.clone();
now.setTime(newTime);
// 通过clone恢复
```

### 2. java.io.Serializable

```java
// Serializable可以保存对象状态
public class GameState implements Serializable {
    private int level;
    private int score;
    private List<String> inventory;

    // 保存游戏状态
    public void save(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    // 加载游戏状态
    public static GameState load(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return (GameState) ois.readObject();
        }
    }
}
```

---

## Spring框架中的应用

### 1. Session状态管理

```java
// Spring Session使用备忘录模式管理会话状态
public class SessionFacade {
    private Map<String, Object> sessionState = new HashMap<>();

    // 保存状态快照
    public void saveSnapshot() {
        Map<String, Object> snapshot = new HashMap<>(sessionState);
        // 保存到外部存储
        sessionRepository.save(snapshot);
    }

    // 恢复状态
    public void restoreSnapshot(Map<String, Object> snapshot) {
        this.sessionState = new HashMap<>(snapshot);
    }
}

// HttpSession也被Spring包装
public class HttpSessionAdapter implements HttpSession {
    private HttpSession delegate;

    // 获取原始会话的快照
    public Map<String, Object> getSnapshot() {
        Map<String, Object> snapshot = new HashMap<>();
        Enumeration<String> names = delegate.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            snapshot.put(name, delegate.getAttribute(name));
        }
        return snapshot;
    }
}
```

### 2. TransactionAspectSupport

```java
// Spring使用备忘录保存事务状态
public abstract class TransactionAspectSupport {
    private static final ThreadLocal<TransactionInfo> transactionInfoHolder =
        new ThreadLocal<>();

    // 保存事务状态
    protected void commitTransactionAfterReturning(TransactionInfo txInfo) {
        if (txInfo != null && txInfo.hasTransaction()) {
            // 恢复之前的事务状态
        }
    }

    // 备忘录保存了当前事务信息
    private static class TransactionInfo {
        private final TransactionStatus transaction;
        private final TransactionAspectSupport aspect;
        private final TransactionAttribute attribute;

        // 保存之前的TransactionInfo
        private final TransactionInfo oldTransactionInfo;
    }
}
```

---

## MyBatis中的应用

### 1. MetaObject

```java
// MyBatis使用备忘录模式管理对象属性快照
public class MetaObject {
    private ObjectWrapper wrapper;
    private Map<String, Object> propertyMapping = new HashMap<>();

    // 获取属性值
    public Object getValue(String property) {
        Object value = wrapper.get(property);
        propertyMapping.put(property, value);  // 保存快照
        return value;
    }

    // 恢复属性值
    public void restoreValue(String property, Object value) {
        wrapper.set(property, value);
    }
}
```

### 2. ResultSetWrapper

```java
// MyBatis缓存列信息作为备忘录
public class ResultSetWrapper {
    private final ResultSet resultSet;
    private Map<String, Object> columnValues = new HashMap<>();

    // 缓存当前行数据
    public void cacheRowData() {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();
        for (int i = 1; i <= count; i++) {
            String columnName = metaData.getColumnName(i);
            Object value = resultSet.getObject(i);
            columnValues.put(columnName, value);
        }
    }

    // 恢复行数据
    public void restoreRowData() {
        // 从缓存恢复
    }
}
```

---

## 适用场景

1. **撤销/恢复**：编辑器、游戏
2. **游戏存档**：保存游戏进度
3. **数据库事务**：回滚操作
4. **快照**：对象状态备份
5. **会话状态**：Web会话管理

## 优点

- **封装性**：不破坏Originator的封装性
- **状态保存**：可以保存多个状态点
- **简化恢复**：恢复操作简单

## 缺点

- **内存消耗**：大量状态保存可能消耗内存
- **备忘录管理**：Caretaker需要管理备忘录生命周期
- **性能开销**：频繁保存状态影响性能

## 对比其他模式

| 模式 | 关系 |
|------|------|
| 备忘录模式 | 状态快照，恢复 |
| 命令模式 | 操作记录，撤销 |
| 迭代器模式 | 遍历状态 |
| 状态模式 | 状态转换 |

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/memento
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.memento.App"
```

### 运行基本备忘录示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.memento.basic.BasicMementoDemo"
```

### 运行撤销重做示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.memento.undo.UndoRedoDemo"
```

### 运行快照示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.memento.snapshot.SnapshotDemo"
```

### 运行事务示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.memento.transaction.TransactionDemo"
```
