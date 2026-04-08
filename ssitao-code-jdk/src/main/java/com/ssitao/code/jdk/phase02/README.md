# 第二阶段：集合框架

## 学习目标

掌握Java集合框架的核心接口和实现类，理解各集合的特点、使用场景和底层原理。

---

## 步骤列表

| 步骤 | 主题 | 文档 | 代码 |
|------|------|------|------|
| 1 | List集合 | [Step01_List.md](./Step01_List.md) | [list/*.java](./list/) |
| 2 | Set集合 | [Step02_Set.md](./Step02_Set.md) | [set/*.java](./set/) |
| 3 | Map集合 | [Step03_Map.md](./Step03_Map.md) | [map/*.java](./map/) |
| 4 | Queue集合 | [Step04_Queue.md](./Step04_Queue.md) | [queue/*.java](./queue/) |
| 5 | 迭代器 | [Step05_Iterator.md](./Step05_Iterator.md) | [iterator/*.java](./iterator/) |

---

## 集合框架体系

```
Collection
├── List (有序、可重复)
│   ├── ArrayList      - 动态数组，查询快，增删慢
│   ├── LinkedList     - 双向链表，增删快，查询慢
│   └── Vector         - 线程安全的动态数组
│
├── Set (无序、去重)
│   ├── HashSet        - 基于哈希表，无序
│   ├── LinkedHashSet  - 基于哈希表+链表，保持插入顺序
│   └── TreeSet        - 基于红黑树，有序
│
└── Queue (队列)
    ├── LinkedList      - 可作为队列使用
    ├── PriorityQueue   - 优先级队列
    └── Deque           - 双端队列
        └── ArrayDeque  - 数组实现的双端队列

Map (键值对，键唯一)
├── HashMap         - 基于哈希表，无序
├── LinkedHashMap  - 保持插入顺序
├── TreeMap         - 基于红黑树，有序
├── Hashtable       - 线程安全
└── ConcurrentHashMap - 高并发哈希表
```

---

## 核心接口对比

| 接口 | 实现类 | 有序性 | 重复元素 | 线程安全 | 使用场景 |
|------|--------|--------|----------|----------|----------|
| List | ArrayList | 是 | 是 | 否 | 顺序访问，随机访问 |
| List | LinkedList | 是 | 是 | 否 | 频繁增删操作 |
| List | Vector | 是 | 是 | 是 | 需要线程安全 |
| Set | HashSet | 否 | 否 | 否 | 去重，无顺序要求 |
| Set | LinkedHashSet | 插入顺序 | 否 | 否 | 去重，保持插入顺序 |
| Set | TreeSet | 排序顺序 | 否 | 否 | 需要排序的去重 |
| Map | HashMap | 否 | 键不重复 | 否 | 键值对存储 |
| Map | LinkedHashMap | 插入顺序 | 键不重复 | 否 | 需要保持顺序 |
| Map | TreeMap | 排序顺序 | 键不重复 | 否 | 需要按键排序 |
| Queue | PriorityQueue | 优先级 | 是 | 否 | 优先级队列 |
| Deque | ArrayDeque | 两端 | 是 | 否 | 栈和队列实现 |

---

## 学习建议

1. **理解接口vs实现**: 优先使用接口类型声明变量，如 `List<String> list = new ArrayList<>()`
2. **选择合适的集合**: 根据有序性、去重、线程安全需求选择
3. **理解底层原理**: 数组、链表、哈希表、红黑树的特性
4. **注意性能**: ArrayList vs LinkedList，HashMap vs TreeMap 的选择

---

## 运行代码

```bash
cd ssitao-code-jdk
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase02.list.ArrayListDemo"
```

---

## 下一步

[第三阶段：泛型与反射](../phase03/README.md)
