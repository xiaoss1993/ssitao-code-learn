# 步骤2：垃圾回收 - 算法与收集器

---

## 2.1 GC算法

### 2.1.1 标记-清除（Mark-Sweep）

```java
// 标记-清除算法
// 分为两个阶段：标记和清除

// 阶段1：标记
// 从GC Roots遍历，标记所有存活对象

// 阶段2：清除
// 遍历堆，回收所有未标记对象

// 优点：简单
// 缺点：
// - 效率问题：标记和清除效率都不高
// - 空间问题：产生内存碎片

// 图示：
// 标记前：  [obj] [obj] [    ] [obj] [    ] [obj]
// 标记：存活的对象被标记
// 清除后：[obj] [obj] [    ] [    ] [    ] [obj]
//                  ↑产生碎片
```

### 2.1.2 复制（Copying）

```java
// 复制算法
// 将内存分为两半，每次只使用一半
// 垃圾回收时，将存活对象复制到另一半，然后清除原半区

// 优点：
// - 效率高（移动指针）
// - 无碎片

// 缺点：
// - 浪费一半内存
// - 如果存活对象多，复制开销大

// 应用：适合新生代（存活对象少）
// 新生代对象98%都是"朝生夕死"

// 图示：
// From区: [A] [B] [  ] [C] [  ] [  ]  →  To区: [A] [B] [C]
// 清除From区
```

### 2.1.3 标记-整理（Mark-Compact）

```java
// 标记-整理算法
// 标记后，让存活对象向一端移动，然后清除边界外内存

// 优点：
// - 无内存碎片
// - 利用完整内存

// 缺点：
// - 移动存活对象开销大
// - 效率比复制算法低

// 应用：适合老年代（存活对象多）

// 图示：
// 整理前：[A] [  ] [B] [  ] [C] [  ] [D]
// 整理后：[A] [B] [C] [D] [  ] [  ] [  ]
```

### 2.1.4 分代收集（Generational Collection）

```java
// 分代收集算法
// 根据对象存活周期将内存分代，不同区域采用不同算法

// 新生代（Young Generation）
// - Eden区：对象创建区
// - S0/S1（Survivor区）：存活对象复制区
// - 算法：复制算法
// - 特点：对象存活率低

// 老年代（Old/Tenured Generation）
// - 长期存活的对象
// - 算法：标记-整理或标记-清除
// - 特点：对象存活率高

// 永久代（PermGen，JDK 7）
// - 类信息、常量
// - JDK 8改为元空间

// 对象晋升：
// 对象在Eden出生，第一次Minor GC后存活
// 复制到S0，年龄+1
// 再次Minor GC后存活，复制到S1，年龄+1
// 年龄达到阈值（默认15），晋升为老年代
// -XX:MaxTenuringThreshold=15
```

---

## 2.2 垃圾收集器

### 2.2.1 收集器关系图

```
┌─────────────┐
│ Serial GC   │ ── 单线程，Client模式
└─────────────┘
         ↓
┌─────────────┐
│ ParNew GC  │ ── Serial多线程版本
└─────────────┘
         ↓
┌─────────────────────┐
│ Parallel Scavenge   │ ── 吞吐量优先
└─────────────────────┘
         ↓
┌─────────────────────┐
│     G1 GC           │ ── 分区算法，低停顿
└─────────────────────┘

┌─────────────┐
│ Serial Old  │ ── 老年代版本
└─────────────┘
         ↓
┌─────────────────────┐
│ Parallel Old        │ ── 老年代版本
└─────────────────────┘

┌─────────────┐
│  CMS GC     │ ── 并发标记清除，低停顿
└─────────────┘
```

### 2.2.2 Serial收集器

```java
// Serial收集器
// - 单线程收集
// - 收集时必须暂停所有工作线程（Stop The World）
// - Client模式下默认的新生代收集器

// 参数
// -XX:+UseSerialGC

// 图示：
// [线程1][线程2][线程3] ──STW──→ [GC进行中] ──STW──→ [线程1][线程2][线程3]
```

### 2.2.3 ParNew收集器

```java
// ParNew收集器
// - Serial的多线程版本
// - 多CPU环境性能比Serial好
// - Server模式下默认的新生代收集器
// - 可以与CMS配合

// 参数
// -XX:+UseParNewGC
// -XX:ParallelGCThreads=n  设置GC线程数

// 图示：
// [线程1][线程2][线程3] ──STW──→ [GC][GC][GC] ──STW──→ [线程1][线程2][线程3]
//                              3个线程并行收集
```

### 2.2.4 Parallel Scavenge收集器

```java
// Parallel Scavenge收集器
// - 新生代收集器
// - 复制算法
// - 吞吐量优先（Throughput First）

// 吞吐量 = 运行用户代码时间 / (运行用户代码时间 + GC时间)

// 参数
// -XX:+UseParallelGC        使用Parallel Scavenge
// -XX:MaxGCPauseMillis=100  最大GC停顿时间（不是越小越好）
// -XX:GCTimeRatio=19        设置吞吐量大小（默认99，1/(1+GCTimeRatio)）
// -XX:+UseAdaptiveSizePolicy  开启自适应调节（推荐）

// 适用：后台计算任务，不需要太多交互
```

### 2.2.5 Serial Old收集器

```java
// Serial Old收集器
// - 老年代收集器
// - 标记-整理算法
// - 单线程
// - Client模式或JDK 5之前的配置

// 搭配：
// - Serial + Serial Old
// - ParNew + Serial Old
```

### 2.2.6 Parallel Old收集器

```java
// Parallel Old收集器
// - 老年代收集器
// - 标记-整理算法
// - 多线程
// - JDK 8默认的老年代收集器之一

// 搭配：
// - Parallel Scavenge + Parallel Old（推荐）

// 参数
// -XX:+UseParallelOldGC
```

### 2.2.7 CMS收集器

```java
// CMS（Concurrent Mark Sweep）收集器
// - 老年代收集器
// - 追求最短停顿时间
// - 并发收集，低停顿

// 工作过程：
// 1. 初始标记（Initial Mark）- STW
//    标记GC Roots直接关联的对象
//
// 2. 并发标记（Concurrent Mark）
//    进行GC Roots Tracing，标记所有存活对象
//    （与用户线程并发执行）
//
// 3. 重新标记（Remark）- STW
//    修正并发标记期间因用户线程运行产生的变动
//
// 4. 并发清除（Concurrent Sweep）
//    清除未标记的对象
//    （与用户线程并发执行）

// 参数
// -XX:+UseConcMarkSweepGC
// -XX:ParallelCMSThreads=n  CMS线程数
// -XX:CMSInitiatingOccupancyFraction=68  老年代占用多少触发GC

// 优点：并发收集，低停顿
// 缺点：
// - CPU敏感：占用CPU资源
// - 浮动垃圾：并发清理阶段产生的垃圾（concurrent mode failure）
// - 空间碎片：使用标记-清除，会产生碎片

// 备选方案：Serial Old（CMS失败时）
```

### 2.2.8 G1收集器

```java
// G1（Garbage First）收集器
// - JDK 7引入，JDK 9默认
// - 面向服务端的收集器
// - 适合大堆（6GB+）和低停顿需求

// 特点：
// 1. 并行与并发：多CPU并发，多阶段并行
// 2. 分代收集：保留新生代老年代概念
// 3. 空间整合：基于标记-整理，无碎片
// 4. 可预测停顿：可以设置期望停顿时间

// 内存布局：
// G1将堆划分为多个大小相等的Region（1MB-32MB）
// 不再是固定的新生代/老年代概念
// 每个Region可以动态扮演Eden/Survivor/Old

// 工作过程：
// 1. 初始标记（Initial Marking）- STW
//    标记GC Roots直接引用的对象
//
// 2. 并发标记（Concurrent Marking）
//    并发标记存活对象
//
// 3. 最终标记（Final Marking）- STW
//    修正并发标记期间的变化
//
// 4. 筛选回收（Live Data Counting and Cleanup）
//    根据期望停顿时间，选择价值高的Region回收

// 参数
// -XX:+UseG1GC
// -XX:MaxGCPauseMillis=200      期望最大停顿时间
// -XX:G1HeapRegionSize=4m       Region大小
// -XX:ConcGCThreads=n            并发GC线程数

// 适用场景：
// - 注重吞吐量
// - 内存6GB+
// - 期望停顿时间可控
```

---

## 2.3 ZGC与Shenandoah

### 2.3.1 ZGC（JDK 11+）

```java
// ZGC（Z Garbage Collector）
// - JDK 11引入，JDK 15成为正式特性
// - 可扩展的低延迟垃圾收集器
// - 设计目标：停顿时间不超过10ms，适合TB级堆

// 特点：
// - 并发收集
// - 基于Region（类似G1）
// - 着色指针（Colored Pointers）
// - 读屏障（Load Barrier）
// - 不分代（最新ZGC支持分代）

// 优点：
// - 停顿时间短（亚毫秒级）
// - 可扩展性强
// - 吞吐量损失小

// 参数
// -XX:+UseZGC
// -XX:MaxGCPauseMillis=10   期望最大停顿时间
// -XX:+ZCollectionInterval  设置GC间隔

// 限制：
// - 需要较多内存（至少16MB）
// - 暂不支持类卸载（JDK 15+支持）
```

### 2.3.2 Shenandoah（JDK 12+）

```java
// Shenandoah
// - JDK 12引入
// - 非Oracle实现（Red Hat开发）
// - 与ZGC类似，但实现不同

// 特点：
// - 并发收集
// - 基于Region
// -  Brooks指针（代替着色指针）
// - 不分代

// 区别于ZGC：
// - 使用Brooks指针而非着色指针
// - GC时需要移动对象，更新指针
```

---

## 2.4 GC日志分析

### 2.4.1 常用GC日志参数

```bash
# 打印GC日志
-Xlog:gc*:file=gc.log

# 更详细的配置
-Xlog:gc*=debug:file=gc.log:time:filecount=5,filesize=10M

# 经典格式
-XX:+PrintGCDetails
-XX:+PrintGCDateStamps
-XX:+PrintTenuringDistribution
```

### 2.4.2 GC日志解读

```bash
# Minor GC日志
2024-04-09T10:30:15.123+0800: [GC (Allocation Failure)
  Eden Space: 65536K->0K(98304K),
  Survivor0: 0K->16384K(16384K),
  Survivor1: 16384K->16384K(16384K),
  Old Gen: 131072K->131072K(262144K)]
  Metaspace: 51200K->51200K(1069056K)
, 0.0156789 secs]
[Times: user=0.05 sys=0.01, real=0.02 secs]

# Full GC日志
2024-04-09T10:30:20.456+0800: [Full GC (System.gc())
  Old Gen: 262144K->209715K(262144K),
  Metaspace: 51200K->50176K(1069056K)]
  , 0.1234567 secs]
[Times: user=0.45 sys=0.02, real=0.12 secs]
```

### 2.4.3 GC日志分析工具

```java
// 常用GC日志分析工具：

// 1. GCEasy（在线）
//    https://gceasy.io

// 2. GCViewer（开源）
//    https://github.com/chewiebug/GCViewer

// 3. IBM GC & Memory Visualizer
//    https://www.ibm.com/support/pages/gc-and-memory-visualizer

// 4. JDK自带
//    jstat -gcutil <pid> 1000  // 每秒打印GC统计
```

---

## 2.5 常见GC问题

### 2.5.1 Minor GC vs Full GC

```java
// Minor GC（Young GC）
// - 发生在新生代
// - Eden区满时触发
// - STW，但停顿时间短（毫秒级）
// - 频率高

// Major GC / Full GC
// - 发生在老年代
// - 老年代满或拒绝分配时触发
// - 停顿时间长（可能秒级）
// - 频率低

// Full GC触发条件：
// 1. 老年代空间不足
// 2. 永生代/元空间不足
// 3. System.gc()调用
// 4. 空间分配担保失败
```

### 2.5.2 内存分配与担保

```java
// 空间分配担保
// Minor GC前，JVM检查老年代最大可用连续空间
// 如果新生代所有对象都存活下来，老年代空间足够
// 则Minor GC是安全的

// JDK 6 Update 24之后：
// 只要老年代最大可用连续空间 > 新生代对象总大小
// 或 > 历次晋升的平均大小
// 就进行Minor GC，否则Full GC

// 为什么要担保？
// 极端情况下，所有新生代对象都存活，需要晋升到老年代
```

### 2.5.3 GC优化方向

```java
// 1. 降低Minor GC频率
// - 减少对象创建
// - 增大新生代
// - 合理设置SurvivorRatio

// 2. 降低Full GC频率
// - 增大堆内存
// - 优化代码，减少对象晋升
// - 使用大对象直接进入老年代参数
// -XX:PretenureSizeThreshold=1m

// 3. 减少GC停顿时间
// - 使用G1、CMS、ZGC
// - 降低停顿时间目标
// - 减少并发标记工作量
```

---

## 2.6 练习题

```java
// 1. 简述三种GC算法（标记-清除、复制、标记-整理）的优缺点

// 2. 什么情况下会触发Minor GC？什么情况下触发Full GC？

// 3. G1收集器相比CMS有哪些优势？

// 4. 对象如何从新生代晋升到老年代？

// 5. 如果发现频繁Full GC，应该如何排查和优化？
```

---

## 2.7 参考答案

```java
// 1. GC算法对比
// 标记-清除：简单，但效率低，有碎片
// 复制：效率高，无碎片，但浪费一半内存，适合新生代
// 标记-整理：无碎片，效率介于两者之间，适合老年代

// 2. 触发条件
// Minor GC：Eden区满
// Full GC：
//   - 老年代空间不足
//   - 永生代/元空间不足
//   - System.gc()
//   - 空间分配担保失败

// 3. G1 vs CMS
// G1: 空间整合（无碎片），可预测停顿，分区算法
// CMS: 并发标记清除，有碎片，停顿不可控

// 4. 对象晋升
// - 年龄达到MaxTenuringThreshold（默认15）
// - Survivor区空间不足，较大对象直接晋升
// - -XX:MaxTenuringThreshold设置晋升年龄

// 5. 排查优化步骤
// 1) 查看GC日志，确认GC原因和频率
// 2) jstat -gcutil查看内存使用情况
// 3) dump堆内存分析（jmap + MAT）
// 4) 优化方向：
//    - 增大堆内存
//    - 调整新生代/老年代比例
//    - 优化代码减少对象创建
//    - 选择合适的GC收集器
```

---

[返回目录](./README.md) | [下一步：性能优化](./Step03_Performance.md)
