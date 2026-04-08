# 第九阶段：网络编程

## 学习目标

掌握Java网络编程基础，理解TCP/UDP协议原理，学会使用Socket进行网络通信。

---

## 步骤列表

| 步骤 | 主题 | 文档 | 代码 |
|------|------|------|------|
| 1 | TCP Socket | [Step01_TCP_Socket.md](./Step01_TCP_Socket.md) | [socket/*.java](./socket/) |
| 2 | UDP Socket | [Step02_UDP_Socket.md](./Step02_UDP_Socket.md) | [socket/*.java](./socket/) |
| 3 | NIO网络编程 | [Step03_NIO_Network.md](./Step03_NIO_Network.md) | [nio/*.java](./nio/) |

---

## 核心概念概览

### 网络协议

```
TCP (Transmission Control Protocol)
- 面向连接
- 可靠传输
- 字节流
- 用于HTTP、FTP、SMTP等

UDP (User Datagram Protocol)
- 无连接
- 不可靠传输
- 数据报
- 用于DNS、视频流等
```

### 核心类

```
ServerSocket        - TCP服务端
Socket             - TCP客户端/服务端连接
DatagramSocket     - UDP Socket
DatagramPacket      - UDP数据包
InetAddress        - IP地址
URL/URLConnection  - HTTP资源访问
```

---

## 学习建议

1. **TCP Socket**: 理解Socket/ServerSocket通信模型
2. **UDP Socket**: 理解无连接通信特点
3. **NIO网络**: 理解非阻塞IO在网络中的应用

---

## 运行代码

```bash
cd ssitao-code-jdk
mvn compile

# TCP示例（需要先启动Server，再启动Client）
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase09.socket.TCPServer"
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase09.socket.TCPClient"

# UDP示例
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase09.socket.UDPServer"
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase09.socket.UDPClient"
```

---

*JDK核心API学习路径完结！*
