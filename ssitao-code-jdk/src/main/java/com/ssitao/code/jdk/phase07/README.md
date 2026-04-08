# 第七阶段：JVM与性能优化

## 学习目标

理解JVM内存结构、垃圾回收机制，掌握性能分析和优化方法。

---

## 步骤列表

| 步骤 | 主题 | 文档 | 代码 |
|------|------|------|------|
| 1 | JVM基础 | [Step01_JVM.md](./Step01_JVM.md) | - |
| 2 | 垃圾回收 | [Step02_GC.md](./Step02_GC.md) | [gc/*.java](./gc/) |
| 3 | 性能优化 | [Step03_Performance.md](./Step03_Performance.md) | [performance/*.java](./performance/) |

---

## 核心概念概览

### JVM内存结构

```
JVM内存结构
├── 程序计数器      - 当前线程执行的字节码行号
├── 虚拟机栈        - 方法调用栈帧
│    ├── 局部变量表
│    ├── 操作数栈
│    ├── 动态链接
│    └── 方法返回地址
├── 本地方法栈      - Native方法
├── 堆              - 对象实例（GC主要区域）
│    ├── Young区
│    │    ├── Eden
│    │    └── Survivor(S0, S1)
│    └── Old区
└── 方法区          - 类信息、运行时常量池（JDK 8元空间）
```

### 垃圾回收算法

```
标记-清除    - 效率问题，产生碎片
复制         - 适合新生代，浪费一半空间
标记-整理    - 适合老年代
分代收集     - 主流算法，结合多种策略
```

### 常用GC

```
Serial         - 单线程，简单，适合Client模式
ParNew         - Serial的多线程版本
Parallel Scavenge - 吞吐量优先
CMS            - 并发标记清除，停顿时间短
G1            - 分区算法，可预测停顿时间
ZGC           - JDK 11+，大堆低延迟
Shenandoah    - JDK 12+，低停顿
```

---

## 学习建议

1. **JVM基础**: 理解内存结构和对象创建过程
2. **垃圾回收**: 理解各种GC算法和收集器的特点
3. **性能优化**: 掌握分析工具和常见优化方法

---

## 运行代码

```bash
cd ssitao-code-jdk
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase07.gc.GCDemo"
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase07.performance.PerformanceDemo"
```

---

## 下一步

[第八阶段：设计模式](../phase08/README.md)
