# 步骤3：性能优化 - 诊断与调优

---

## 3.1 性能监控工具

### 3.1.1 JDK命令行工具

```bash
# jps - Java进程状态
jps -l          # 显示主类名和完整包名
jps -m          # 显示传递给main方法的参数
jps -v          # 显示JVM参数

# jstat - JVM统计信息
jstat -gcutil <pid> 1000  # 每秒打印GC统计（百分比）
jstat -gc <pid> 1000      # 打印GC容量统计（字节）
jstat -class <pid>        # 类加载统计

# jinfo - JVM配置信息
jinfo -flags <pid>        # 查看所有JVM参数
jinfo -sysprops <pid>     # 查看系统属性

# jstack - 线程堆栈
jstack <pid>              # 打印线程堆栈
jstack -l <pid>           # 打印更多锁信息

# jmap - 内存映射
jmap -heap <pid>          # 打印堆内存使用情况
jmap -histo <pid>         # 打印对象统计
jmap -dump:format=b,file=heap.hprof <pid>  # dump堆内存

# jcmd - 诊断命令
jcmd <pid> VM.version     # 查看JVM版本
jcmd <pid> VM.flags        # 查看JVM参数
jcmd <pid> GC.heap_dump    # 生成heap dump
```

### 3.1.2 jstat详细用法

```bash
# jstat -gcutil示例
$ jstat -gcutil 12345 1000

S0     S1     E      O      M     YGC     YGCT    FGC    FGCT     GCT
0.00  65.42  45.23  78.54  92.34    123    1.234    45    2.345    3.579

# 列说明：
# S0/S1 - Survivor区使用百分比
# E      - Eden区使用百分比
# O      - Old区使用百分比
# M      - Metaspace使用百分比
# YGC    - Young GC次数
# YGCT   - Young GC总时间（秒）
# FGC    - Full GC次数
# FGCT   - Full GC总时间（秒）
# GCT    - 总GC时间（秒）
```

### 3.1.3 VisualVM

```bash
# VisualVM（Java VisualVM）
# 位置：$JAVA_HOME/bin/jvisualvm

# 功能：
# - 监控CPU、内存、线程
# - 生成和分析heap dump
# - 采样分析CPU
# - 线程分析

# 启动
$ jvisualvm

# 连接远程JVM
# 1. 在远程服务器启动JMX
java -Dcom.sun.management.jmxremote \
     -Dcom.sun.management.jmxremote.port=9010 \
     -Dcom.sun.management.jmxremote.authenticate=false \
     -Dcom.sun.management.jmxremote.ssl=false \
     -jar yourapp.jar
```

---

## 3.2 性能分析

### 3.2.1 CPU分析

```java
// CPU高排查步骤：
// 1. top - 查看CPU占用最高的进程
top -p <pid>

// 2. top -Hp <pid> - 查看CPU占用最高的线程
top -Hp <pid>

// 3. jstack <pid> - 查看线程堆栈
jstack <pid> > thread.log

// 4. 转换线程ID为十六进制
printf "%x\n" <tid>

// 5. 在thread.log中搜索十六进制线程ID
```

### 3.2.2 内存分析

```java
// 内存高排查步骤：
// 1. jmap -heap <pid> - 查看堆内存使用
jmap -heap <pid>

// 2. jmap -histo <pid> - 查看对象统计
jmap -histo <pid> | head -30  # 前30个最大的对象

// 3. jmap -dump - 生成heap dump
jmap -dump:format=b,file=heap.hprof <pid>

// 4. 使用MAT分析heap dump
# 下载Eclipse MAT: https://www.eclipse.org/mat/

# MAT功能：
# - Dominator Tree - 查找大对象
# - Histogram - 按类统计对象
# - Leak Suspects - 内存泄漏怀疑点
```

### 3.2.3 线程分析

```java
// 线程状态分析
// RUNNABLE - 正在运行
// BLOCKED - 等待获取锁
// WAITING - 无限等待（wait/join/park）
// TIMED_WAITING - 限时等待（sleep/yield/wait(timeout)）

// 常见问题：
// 1. 死锁
//    jstack -l <pid> | grep -A 10 "Deadlock"

// 2. 线程数过多
//    jstack <pid> | grep "java.lang.Thread.State" | wc -l

// 3. 线程阻塞
//    jstack <pid> | grep -B 5 "BLOCKED"
```

---

## 3.3 常见性能问题

### 3.3.1 内存泄漏

```java
// 内存泄漏的常见原因：

// 1. 静态集合类持有对象引用
class LeakExample {
    static List<Object> cache = new ArrayList<>();  // 永不清理

    public void add(Object obj) {
        cache.add(obj);  // 对象无法释放
    }
}

// 2. 未关闭的资源
class LeakExample2 {
    public void read() throws IOException {
        InputStream is = new FileInputStream("file.txt");
        // 如果不关闭，stream持有的内存不会被释放
        // 应该用try-with-resources
    }
}

// 3. 监听器未注销
class LeakExample3 {
    public void register() {
        EventBus.getDefault().register(this);  // 不注销
    }
}

// 4. ThreadLocal未清理
class LeakExample4 {
    static ThreadLocal<Object> tl = new ThreadLocal<>();
    // 如果线程池复用，ThreadLocal中的对象不会被清理
}

// 解决方案：
// 1. 使用WeakHashMap
// 2. 及时清理资源（try-finally, try-with-resources）
// 3. 注销监听器和订阅
// 4. 在finally中调用ThreadLocal.remove()
```

### 3.3.2 OOM问题

```java
// 常见OOM类型：

// 1. Java heap space
// 堆内存不足
// 解决：-Xmx调大，检查内存泄漏

// 2. GC overhead limit exceeded
// GC时间过长但回收不了多少内存
// 解决：-XX:-UseGCOverheadLimit，检查内存泄漏

// 3. PermGen/Metaspace space
// 方法区/元空间不足
// JDK 7: -XX:MaxPermSize
// JDK 8+: -XX:MaxMetaspaceSize

// 4. Unable to create new native thread
// 线程数过多
// 解决：减少线程数，检查线程泄漏

// 5. Direct buffer memory
// NIO直接内存不足
// 解决：-XX:MaxDirectMemorySize

// 6. stack overflow
// 栈溢出（递归调用过深）
// 解决：-Xss调大，修复递归
```

### 3.3.3 死锁

```java
// 死锁示例
class DeadLock {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public void method1() {
        synchronized (lock1) {
            synchronized (lock2) {
                // do something
            }
        }
    }

    public void method2() {
        synchronized (lock2) {  // 反向加锁
            synchronized (lock1) {
                // do something
            }
        }
    }
}

// 死锁检测：
// jstack -l <pid> | grep -A 10 "Found one Java-level deadlock"

// 预防死锁：
// 1. 避免嵌套锁
// 2. 固定加锁顺序
// 3. 使用tryLock带超时
// 4. 减少锁持有时间
```

---

## 3.4 性能优化建议

### 3.4.1 内存优化

```java
// 1. 合理设置堆大小
// -Xms4g -Xmx4g  # 初始和最大相同，避免动态扩展

// 2. 合理设置新生代
// -Xmn2g         # 新生代2GB
// -XX:NewRatio=2 # 老年代/新生代=2:1

// 3. 减少对象大小
// - 使用基本类型而非包装类型
// - 避免使用BigInteger/BigDecimal（大对象）
// - 减少对象字段数量

// 4. 对象复用
// - 使用对象池（数据库连接池、线程池）
// - 避免在循环中创建对象
// - 使用StringBuilder而非字符串拼接

// 5. 弱引用/软引用
// - 缓存使用软引用
// - 观察者模式使用弱引用
```

### 3.4.2 GC优化

```java
// 1. 选择合适的GC收集器
// 吞吐量优先：Parallel Scavenge + Parallel Old
// 低延迟优先：CMS / G1 / ZGC

// 2. 优化GC参数
// -XX:MaxGCPauseMillis=200   # 设置目标停顿时间
// -XX:GCTimeRatio=19         # 吞吐量目标

// 3. 减少GC频率
// 减少对象创建
// 合理使用数据结构
// 避免大对象短期创建

// 4. 减少Full GC
// 对象预分配
// -XX:PretenureSizeThreshold=1m  # 超过1MB直接进老年代
// 避免频繁创建大对象
```

### 3.4.3 代码优化

```java
// 1. 循环优化
// 避免循环中创建对象
for (int i = 0; i < 1000; i++) {
    // 不好：每次创建对象
    list.add(new Object());

    // 好：对象在循环外创建
    Object obj = new Object();
    for (int i = 0; i < 1000; i++) {
        list.add(obj);
    }
}

// 2. 字符串优化
// 使用StringBuilder而非+
String s = "a" + "b" + "c";  // 不好
String s = new StringBuilder().append("a").append("b").append("c").toString(); // 好

// JDK 9+ 字符串拼接已优化

// 3. 集合优化
// 指定初始容量
new ArrayList<>(100);  // 预估大小
new HashMap<>(100);    // 减少扩容

// 4. IO优化
// 使用缓冲
BufferedReader br = new BufferedReader(new FileReader("file.txt"));
// 或
Files.lines(Paths.get("file.txt"));

// 5. 并行化
// 使用并行流
list.parallelStream()
    .map(Object::method)
    .collect(Collectors.toList());
```

### 3.4.4 线程优化

```java
// 1. 合理使用线程池
// CPU密集型：线程数 = CPU核心数 + 1
// IO密集型：线程数 = CPU核心数 * 2（或更多）

// 2. 避免过度同步
// 缩小synchronized块范围
synchronized (lock) {
    // 只同步必要代码
}

// 3. 使用并发容器
// ConcurrentHashMap vs HashMap
// CopyOnWriteArrayList vs ArrayList

// 4. 减少上下文切换
// 避免大量小任务
// 使用合适的任务粒度
```

---

## 3.5 实战案例

### 3.5.1 Full GC频繁

```bash
# 症状：Full GC频繁，每次回收很少
# 原因：内存分配过快或内存泄漏

# 排查步骤：
# 1. jstat -gcutil <pid> 1000
#    观察FGC次数是否快速增长

# 2. jmap -histo <pid> | head -30
#    查看是否有大量重复对象

# 3. jmap -dump生成dump，MAT分析
jmap -dump:format=b,file=heap.hprof <pid>

# 4. 常见原因：
#    - 内存泄漏
#    - 频繁创建大对象
#    - 缓存没有限制
#    - 静态集合类持有引用

# 解决：
# 1. 修复内存泄漏
# 2. 增大堆内存
# 3. 优化代码减少对象创建
# 4. 使用软引用做缓存
```

### 3.5.2 CPU高但GC正常

```bash
# 症状：CPU高，但GC正常
# 原因：代码效率问题（死循环、计算密集）

# 排查步骤：
# 1. top -Hp <pid> 找到高CPU线程ID
top -Hp <pid>

# 2. jstack <pid> > thread.log
#    保存线程快照

# 3. printf "%x\n" <tid>
#    转换线程ID为十六进制

# 4. 在thread.log中搜索
grep -A 20 "nid=0x<hex>" thread.log

# 5. 分析代码热点
#    - 死循环
#    - 正则表达式复杂
#    - 递归调用过深
#    - 序列化/反序列化
```

---

## 3.6 常用JVM参数配置

### 3.6.1 开发环境

```bash
# 开发环境（JDK 8+）
-Xms256m -Xmx512m
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-Xlog:gc*:file=gc.log
```

### 3.6.2 生产环境（通用）

```bash
# 生产环境推荐配置（JDK 8+）
-server
-Xms4g -Xmx4g
-Xmn2g
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/var/log/myapp
-Xlog:gc*:file=/var/log/myapp-gc.log:time:filecount=10,filesize=50M
```

### 3.6.3 低延迟配置

```bash
# 低延迟应用（JDK 11+）
-server
-Xms4g -Xmx4g
-XX:+UseZGC
-XX:+AlwaysPreTouch
-XX:MaxGCPauseMillis=10
-Xlog:gc:/var/log/gc.log
```

---

## 3.7 练习题

```java
// 1. 如果CPU使用率100%，如何定位问题？

// 2. 如何排查Java内存泄漏？

// 3. Full GC频繁应该如何优化？

// 4. 如何检测死锁？

// 5. 什么情况下会抛出OutOfMemoryError？如何解决？
```

---

## 3.8 参考答案

```java
// 1. CPU 100%排查
// a) top -p <pid> 找到进程
// b) top -Hp <pid> 找到高CPU线程
// c) jstack <pid> 保存线程快照
// d) 转换线程ID为十六进制
// e) 在快照中搜索，分析热点代码

// 2. 内存泄漏排查
// a) jstat -gcutil 观察内存趋势
// b) jmap -histo 查看对象统计
// c) jmap -dump 生成heap dump
// d) MAT分析heap dump
// e) 重点关注：
//    - 静态集合类
//    - 未关闭的资源
//    - 未注销的监听器
//    - ThreadLocal

// 3. Full GC频繁优化
// a) 确认是否内存泄漏
// b) 调整堆大小 -Xmx
// c) 调整新生代大小 -Xmn
// d) 调整对象晋升阈值
// e) 使用合适的收集器

// 4. 死锁检测
// jstack -l <pid> | grep -A 10 "Found one Java-level deadlock"

// 5. OOM解决方案
// heap space: 增大堆、修复泄漏
// metaspace: 增大元空间、优化类加载
// unable to create thread: 减少线程、检查泄漏
// direct memory: 增大直接内存、检查NIO使用
```

---

[返回目录](./README.md)

## 第七阶段总结

### 核心知识点

| 步骤 | 主题 | 核心概念 |
|------|------|----------|
| 1 | JVM基础 | 运行时数据区、对象创建、引用类型 |
| 2 | 垃圾回收 | GC算法（标记-清除/复制/标记-整理）、收集器（Serial/ParNew/Parallel/CMS/G1/ZGC） |
| 3 | 性能优化 | 监控工具、问题排查、参数调优 |
