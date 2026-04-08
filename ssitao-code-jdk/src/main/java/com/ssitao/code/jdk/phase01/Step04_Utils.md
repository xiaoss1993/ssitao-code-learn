# 步骤4：常用工具类 - Arrays、Collections、Objects

---

## 4.1 Arrays工具类

### 4.1.1 数组基本操作

```java
int[] arr = {5, 2, 8, 1, 9};

// 转换为字符串
String str = Arrays.toString(arr);  // "[5, 2, 8, 1, 9]"

// 数组复制
int[] copy = Arrays.copyOf(arr, arr.length);  // 完整复制
int[] copy2 = Arrays.copyOf(arr, 3);         // 前3个元素

// 数组填充
int[] filled = new int[5];
Arrays.fill(filled, 10);  // [10, 10, 10, 10, 10]

// 数组比较
int[] a = {1, 2, 3};
int[] b = {1, 2, 3};
Arrays.equals(a, b);       // true (比较内容)
a == b;                    // false (比较引用)

// 二分查找(必须先排序)
int[] sorted = {1, 3, 5, 7, 9};
Arrays.binarySearch(sorted, 5);  // 2 (返回索引)
Arrays.binarySearch(sorted, 6); // -3 (返回负数,表示插入点)
```

### 4.1.2 数组排序

```java
int[] arr = {5, 2, 8, 1, 9};

// 基本类型使用双轴快速排序(Dual-Pivot Quicksort)
Arrays.sort(arr);  // [1, 2, 5, 8, 9] (原地排序)

// 对象数组使用归并排序(TimSort)
String[] words = {"banana", "apple", "cherry"};
Arrays.sort(words);  // 按字典顺序

// 自定义排序(降序)
Integer[] arr2 = {5, 2, 8, 1, 9};
Arrays.sort(arr2, Comparator.reverseOrder());  // JDK 8+

// 部分排序
int[] partial = {5, 2, 8, 1, 9, 3};
Arrays.sort(partial, 0, 3);  // 只排序前3个: [2, 5, 8, 1, 9, 3]

// 并行排序(JDK 8+)
Arrays.parallelSort(arr);  // 多线程并行排序,大数据量时更快

// 自定义比较器
Arrays.sort(arr, (a, b) -> b - a);  // 降序
Arrays.sort(arr, Comparator.comparingInt(a -> a));  // 升序
```

### 4.1.3 数组查找

```java
int[] arr = {1, 3, 5, 7, 9};

Arrays.binarySearch(arr, 5);      // 2
Arrays.binarySearch(arr, 6);      // -3 (插入点)

// 未排序数组查找
int index = Arrays.asList(1, 3, 5, 7, 9).indexOf(5);  // 先转List

// 二分查找指定范围
Arrays.binarySearch(arr, 0, 4, 5);  // 在[0,4)范围内查找
```

### 4.1.4 数组与集合转换

```java
// 数组转List (注意:得到的List不支持增删)
String[] array = {"a", "b", "c"};
List<String> list = Arrays.asList(array);  // 固定长度List
List<String> list2 = new ArrayList<>(Arrays.asList(array));  // 可变List

// List转数组
List<String> list3 = Arrays.asList("a", "b", "c");
String[] array2 = list3.toArray(new String[0]);  // 推荐写法
String[] array3 = list3.toArray(String[]::new);  // 方法引用写法

// JDK 8+ Stream
int[] arr = {1, 2, 3, 4, 5};
int sum = Arrays.stream(arr).sum();
int[] doubled = Arrays.stream(arr).map(x -> x * 2).toArray();
```

### 4.1.5 多维数组

```java
// 二维数组创建
int[][] matrix = new int[3][4];  // 3行4列

// 不规则二维数组
int[][] ragged = new int[3][];
ragged[0] = new int[2];
ragged[1] = new int[4];
ragged[2] = new int[1];

// 深拷贝
int[][] original = {{1, 2}, {3, 4}};
int[][] copy = Arrays.copyOf(original, original.length);
for (int i = 0; i < copy.length; i++) {
    copy[i] = Arrays.copyOf(original[i], original[i].length);
}

// 深比较
int[][] a = {{1, 2}, {3, 4}};
int[][] b = {{1, 2}, {3, 4}};
Arrays.deepEquals(a, b);  // true

// 深toString
System.out.println(Arrays.deepToString(matrix));
```

### 4.1.6 JDK 9+ 新方法

```java
// JDK 9+ 比JDK 8更高效的flopMap
int[] arr = {1, 2, 3, 4, 5};
int[][] blocks = Arrays.flopMap(arr, chunk -> new int[]{chunk, chunk * 2});

// JDK 11+ 转为Set
Set<Integer> set = Arrays.stream(arr).boxed()
    .collect(Collectors.toSet());
```

---

## 4.2 Collections工具类

### 4.2.1 集合基本操作

```java
List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));

// 集合复制
List<String> copy = new ArrayList<>(list);

// 集合填充
List<String> filled = new ArrayList<>(Arrays.asList("", "", ""));
Collections.fill(filled, "x");  // [x, x, x]

// 集合比较
Collections.disjoint(list1, list2);  // true表示无共同元素

// 集合最值
Collections.max(list);  // 最大值
Collections.min(list);  // 最小值
```

### 4.2.2 集合排序

```java
List<Integer> list = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9));

// 正序排序
Collections.sort(list);  // [1, 2, 5, 8, 9]

// 降序排序
Collections.sort(list, Comparator.reverseOrder());  // [9, 8, 5, 2, 1]

// JDK 8+
list.sort(Comparator.naturalOrder());   // 正序
list.sort(Comparator.reverseOrder());  // 降序

// 打乱顺序
Collections.shuffle(list);  // 随机打乱

// 反转
Collections.reverse(list);  // [9, 8, 5, 2, 1]
```

### 4.2.3 集合查找

```java
List<String> list = Arrays.asList("a", "b", "c", "d", "e");

// 二分查找(必须先排序)
Collections.binarySearch(list, "c");  // 2

// 查找元素位置
Collections.indexOfSublist(list, Arrays.asList("c", "d"));  // 2

// 查找出现次数
Collections.frequency(list, "a");  // 1
```

### 4.2.4 集合替换

```java
List<String> list = new ArrayList<>(Arrays.asList("a", "b", "a", "c"));

// 替换所有
Collections.replaceAll(list, "a", "x");  // [x, b, x, c]

// 交换
Collections.swap(list, 0, 1);  // [b, x, a, c]

// 批量添加
List<String> toAdd = Arrays.asList("1", "2", "3");
Collections.addAll(list, "1", "2", "3");  // [x, b, x, c, 1, 2, 3]
```

### 4.2.5 不可变集合

```java
List<String> original = Arrays.asList("a", "b", "c");

// 创建不可变集合
List<String> immutable = Collections.unmodifiableList(original);
Set<String> immutableSet = Collections.unmodifiableSet(new HashSet<>(original));
Map<String, Integer> immutableMap = Collections.unmodifiableMap(new HashMap<>());

// JDK 9+ 简洁写法
List<String> list = List.of("a", "b", "c");
Set<String> set = Set.of("a", "b", "c");
Map<String, Integer> map = Map.of("a", 1, "b", 2);

// 单元素不可变集合
List<String> singleton = Collections.singletonList("only");
Set<String> singletonSet = Collections.singleton("only");
```

### 4.2.6 JDK 10+ 新方法

```java
// JDK 10+ 集合复制
List<String> dest = new ArrayList<>();
Collections.copy(dest, source);  // dest容量必须>=source

// JDK 8+ 检查
List<String> list = Arrays.asList("a", "b", "c");
Collections.checkedList(list, String.class);  // 类型安全的List
```

---

## 4.3 Objects工具类

### 4.3.1 空值检查

```java
Objects.isNull(obj);      // true表示为null
Objects.nonNull(obj);     // true表示不为null

// JDK 9+
Objects.requireNonNull(obj);           // null则抛NPE
Objects.requireNonNull(obj, "message"); // null则抛指定消息NPE
Objects.requireNonNullElse(obj, defaultObj);  // null返回默认值

// JDK 9+
Objects.requireNonNullElseGet(obj, () -> defaultObj);  // 支持Supplier
```

### 4.3.2 对象比较

```java
// equals (处理null)
Objects.equals("a", "a");    // true
Objects.equals(null, null);  // true
Objects.equals(null, "a");   // false

// JDK 9+
Objects.equals(null, null);  // 不抛NPE

// deepEquals (用于数组)
Objects.deepEquals(arr1, arr2);  // 递归比较数组元素

// compare (JDK 7+)
Objects.compare(o1, o2, comparator);

// hashCode
Objects.hashCode(obj);  // null返回0,否则返回obj.hashCode()
Objects.hash(obj1, obj2, obj3);  // 生成混合hash
```

### 4.3.3 toString

```java
Objects.toString(obj);           // null返回"null"
Objects.toString(obj, "default"); // null返回"default"

String s = "hello";
Objects.toString(s);  // "hello"
```

### 4.3.4 JDK 12+ 新方法

```java
// JDK 12+ 判断并计算
Objects.checkIndex(5, 10);  // 5在[0,10)范围内,返回5
Objects.checkFromToIndex(2, 8, 10);  // [2,8)在[0,10)范围内
Objects.checkRange(2, 5, 10);  // [2,7)在[0,10)范围内
```

---

## 4.4 System类

### 4.4.1 常用方法

```java
// 数组复制
System.arraycopy(src, srcPos, dest, destPos, length);

// 获取当前时间(纳秒)
long start = System.nanoTime();
// ...
long end = System.nanoTime();

// 获取环境变量
System.getenv("JAVA_HOME");

// 获取系统属性
System.getProperty("java.version");
System.getProperty("os.name");
System.getProperty("user.dir");

// 设置系统属性
System.setProperty("key", "value");

// GC
System.gc();  // 建议JVM执行GC

// 退出
System.exit(0);  // 0表示正常退出

// 获取hashCode
System.identityHashCode(obj);  // 不考虑类的hashCode重写
```

---

## 4.5 练习题

```java
// 1. 将两个有序数组合并为一个有序数组

// 2. 找出数组中的第二大数

// 3. 实现一个方法,去除List中的重复元素(保持顺序)

// 4. 实现集合的交集、并集、差集操作

// 5. 将字符串数组按长度排序
```

---

## 4.6 参考答案

```java
// 1. 合并两个有序数组
public static int[] mergeSortedArrays(int[] a, int[] b) {
    int[] result = new int[a.length + b.length];
    int i = 0, j = 0, k = 0;
    while (i < a.length && j < b.length) {
        result[k++] = a[i] < b[j] ? a[i++] : b[j++];
    }
    while (i < a.length) result[k++] = a[i++];
    while (j < b.length) result[k++] = b[j++];
    return result;
}

// 2. 找出第二大数
public static int findSecondMax(int[] arr) {
    int max = Integer.MIN_VALUE, second = Integer.MIN_VALUE;
    for (int num : arr) {
        if (num > max) {
            second = max;
            max = num;
        } else if (num > second && num != max) {
            second = num;
        }
    }
    return second;
}

// 3. 去重(保持顺序)
public static <T> List<T> removeDuplicates(List<T> list) {
    return new ArrayList<>(new LinkedHashSet<>(list));
}

// 4. 交集、并集、差集
public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
    Set<T> result = new HashSet<>(a);
    result.retainAll(b);
    return result;
}

public static <T> Set<T> union(Set<T> a, Set<T> b) {
    Set<T> result = new HashSet<>(a);
    result.addAll(b);
    return result;
}

public static <T> Set<T> difference(Set<T> a, Set<T> b) {
    Set<T> result = new HashSet<>(a);
    result.removeAll(b);
    return result;
}
```

---

## 第一阶段总结

### 核心知识点

| 步骤 | 主题 | 核心概念 |
|------|------|----------|
| 1 | Object | equals, hashCode, toString, clone, wait/notify |
| 2 | String | 不可变性, 常量池, StringBuilder, StringBuffer |
| 3 | 包装类 | 自动装箱/拆箱, 缓存机制, Math |
| 4 | 工具类 | Arrays, Collections, Objects |

### 学习要点

1. **equals与hashCode必须配对重写**
2. **String是不可变类,拼接用StringBuilder**
3. **避免装箱类型的==比较**
4. **善用工具类简化代码**

---

[返回目录](./README.md)
