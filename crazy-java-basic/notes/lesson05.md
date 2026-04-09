# 第5课：集合框架

## 核心概念

### 5.1 集合体系
```
Collection (接口)
  ├── List (接口) - 有序、可重复
  │     ├── ArrayList
  │     ├── LinkedList
  │     └── Vector
  └── Set (接口) - 无序、不可重复
        ├── HashSet
        ├── TreeSet
        └── LinkedHashSet

Map (接口) - 键值对
  ├── HashMap
  ├── TreeMap
  ├── Hashtable
  └── LinkedHashMap
```

### 5.2 List实现类
| 类 | 特点 | 适用场景 |
|----|------|----------|
| ArrayList | 数组实现，随机访问快 | 频繁遍历 |
| LinkedList | 链表实现，增删快 | 频繁增删 |
| Vector | 线程安全，效率低 | 多线程 |

### 5.3 Set实现类
| 类 | 特点 |
|----|------|
| HashSet | 基于HashMap，无序 |
| LinkedHashSet | 保持插入顺序 |
| TreeSet | 基于红黑树，有序 |

### 5.4 Map实现类
| 类 | 特点 |
|----|------|
| HashMap | 线程不安全，允许null键值 |
| Hashtable | 线程安全，不允许null |
| TreeMap | 有序，基于红黑树 |
| LinkedHashMap | 保持插入顺序 |

## 代码示例

### 示例1：List基本操作
```java
import java.util.*;

public class ListDemo {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("apple");
        list.add("banana");
        list.add("orange");
        list.add(1, "grape");  // 插入

        System.out.println(list.get(0));  // apple
        System.out.println(list.size());   // 4

        list.remove("banana");  // 按对象删除
        list.remove(0);         // 按索引删除

        // 遍历
        for (String s : list) {
            System.out.println(s);
        }

        // 使用迭代器
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (s.equals("orange")) {
                it.remove();
            }
        }
    }
}
```

### 示例2：Set去重
```java
import java.util.*;

public class SetDemo {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("a");
        set.add("b");
        set.add("a");  // 重复，不会添加

        System.out.println(set.size());  // 2

        // TreeSet保持有序
        Set<Integer> treeSet = new TreeSet<>();
        treeSet.add(3);
        treeSet.add(1);
        treeSet.add(2);
        System.out.println(treeSet);  // [1, 2, 3]
    }
}
```

### 示例3：Map操作
```java
import java.util.*;

public class MapDemo {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("语文", 90);
        map.put("数学", 95);
        map.put("英语", 88);

        // 获取
        System.out.println(map.get("数学"));  // 95
        System.out.println(map.getOrDefault("物理", 0));  // 0

        // 遍历方式1：keySet
        for (String key : map.keySet()) {
            System.out.println(key + ":" + map.get(key));
        }

        // 遍历方式2：entrySet
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        // 遍历方式3：forEach (JDK 8+)
        map.forEach((k, v) -> System.out.println(k + ":" + v));
    }
}
```

### 示例4：集合排序
```java
import java.util.*;

public class SortDemo {
    public static void main(String[] args) {
        // 基本类型排序
        List<Integer> nums = Arrays.asList(3, 1, 2);
        Collections.sort(nums);
        System.out.println(nums);  // [1, 2, 3]

        // 对象排序
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("张三", 25));
        persons.add(new Person("李四", 20));
        persons.add(new Person("王五", 30));

        // 使用Comparator
        persons.sort((p1, p2) -> p1.getAge() - p2.getAge());
        persons.sort(Comparator.comparingInt(Person::getAge));

        // 按姓名排序
        persons.sort(Comparator.comparing(Person::getName));
    }
}

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
}
```

## 常见面试题

1. **ArrayList和LinkedList的区别？**
   - ArrayList基于数组，LinkedList基于双向链表
   - ArrayList随机访问O(1)，LinkedList随机访问O(n)
   - LinkedList增删O(1)，ArrayList增删可能需要数组复制

2. **HashSet如何判断元素重复？**
   - 先比较hashCode，再比较equals
   - 自定义对象需重写hashCode和equals

3. **HashMap的底层原理？**
   - JDK8：数组+链表+红黑树
   - 当链表长度>8且桶数>64时转为红黑树

## 练习题

1. 使用List实现栈和队列功能
2. 统计字符串中每个字符出现的次数（使用Map）
3. 去重一个自定义对象列表
4. 实现List逆序输出

## 要点总结

- List有序可重复，Set无序不可重复
- HashMap是最常用的Map实现
- 集合只能存储引用类型
- 注意泛型类型安全
