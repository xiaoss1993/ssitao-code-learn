package com.ssitao.code.designpattern.bridge.jdk;

/**
 * JDK中的桥接模式示例
 *
 * JDK中使用桥接模式的场景：
 * 1. JDBC Driver架构 - DriverManager与具体Driver
 * 2. AWT Peer架构 - Component与Peer
 * 3. NIO Channel架构 - Channel与ChannelImpl
 */
public class JDKBridgeDemo {

    public static void main(String[] args) {
        System.out.println("=== JDK Bridge Pattern Demo ===\n");

        // 1. JDBC Driver架构
        demonstrateJDBCDriver();

        // 2. AWT Peer架构
        demonstrateAWTPeer();

        // 3. NIO Channel架构
        demonstrateNIOChannel();
    }

    /**
     * 1. JDBC Driver架构
     * DriverManager(抽象) <--------> Driver(实现)
     */
    private static void demonstrateJDBCDriver() {
        System.out.println("--- 1. JDBC Driver架构 ---\n");

        System.out.println("JDBC使用桥接模式分离DriverManager和具体Driver：");
        System.out.println();
        System.out.println("  抽象部分 (Abstraction)");
        System.out.println("    - DriverManager: 管理所有Driver，提供统一接口");
        System.out.println("    - getConnection(url): 根据URL选择合适的Driver");
        System.out.println();
        System.out.println("  实现部分 (Implementor)");
        System.out.println("    - Driver接口: 定义connect()方法");
        System.out.println("    - MySQLDriver: MySQL协议实现");
        System.out.println("    - OracleDriver: Oracle协议实现");
        System.out.println("    - PostgreSQLDriver: PostgreSQL协议实现");
        System.out.println();
        System.out.println("  桥接方式：");
        System.out.println("    DriverManager持有List<Driver>引用");
        System.out.println("    根据URL自动选择对应Driver实现");
        System.out.println("    客户端只需使用DriverManager，无需关心具体Driver");
        System.out.println();

        // 模拟Driver注册和选择
        System.out.println("JDBC连接示例：");
        System.out.println("  String url = \"jdbc:mysql://localhost:3306/test\";");
        System.out.println("  Connection conn = DriverManager.getConnection(url);");
        System.out.println("  // 内部自动选择MySQLDriver");
        System.out.println();

        // 演示不同Driver可以互换
        System.out.println("Driver可互换：");
        System.out.println("  MySQL: jdbc:mysql://localhost:3306/test");
        System.out.println("  Oracle: jdbc:oracle:thin:@localhost:1521:orcl");
        System.out.println("  PostgreSQL: jdbc:postgresql://localhost:5432/test");
        System.out.println();
    }

    /**
     * 2. AWT Peer架构
     * Component(抽象) <--------> ComponentPeer(实现)
     */
    private static void demonstrateAWTPeer() {
        System.out.println("--- 2. AWT Peer架构 ---\n");

        System.out.println("Java AWT使用桥接模式分离平台无关的Component和平台相关的Peer：");
        System.out.println();
        System.out.println("  抽象部分 (Abstraction)");
        System.out.println("    - Component: 抽象UI组件，如Button、TextField");
        System.out.println("    - 提供平台无关的API");
        System.out.println();
        System.out.println("  实现部分 (Implementor)");
        System.out.println("    - ComponentPeer接口: 定义paint()、setBounds()等");
        System.out.println("    - WComponentPeer: Windows平台实现");
        System.out.println("    - XComponentPeer: Unix/X11平台实现");
        System.out.println("    - LWComponentPeer: Mac平台实现");
        System.out.println();
        System.out.println("  桥接方式：");
        System.out.println("    Component持有ComponentPeer引用");
        System.out.println("    运行时JVM自动选择对应平台的Peer实现");
        System.out.println("    同一份Java代码在不同平台表现一致");
        System.out.println();

        System.out.println("AWT组件示例：");
        System.out.println("  Button button = new Button(\"Click me\");");
        System.out.println("  // button.getPeer() 返回平台特定的Peer");
        System.out.println("  // Windows返回WButtonPeer");
        System.out.println("  // Mac返回LWButtonPeer");
        System.out.println();
    }

    /**
     * 3. NIO Channel架构
     * Channel(抽象) <--------> SelChanelImpl(实现)
     */
    private static void demonstrateNIOChannel() {
        System.out.println("--- 3. NIO Channel架构 ---\n");

        System.out.println("NIO使用桥接模式分离Channel和具体实现：");
        System.out.println();
        System.out.println("  抽象部分 (Abstraction)");
        System.out.println("    - Channel接口: 定义isOpen()、close()等通用方法");
        System.out.println("    - ReadableByteChannel: 可读通道抽象");
        System.out.println("    - WritableByteChannel: 可写通道抽象");
        System.out.println("    - ByteChannel: 双向通道抽象");
        System.out.println();
        System.out.println("  实现部分 (Implementor)");
        System.out.println("    - SelChanelImpl: 基于Selector的实现");
        System.out.println("    - FileChannelImpl: 文件通道实现");
        System.out.println("    - SocketChannelImpl: Socket通道实现");
        System.out.println("    - DatagramChannelImpl: UDP通道实现");
        System.out.println();
        System.out.println("  桥接方式：");
        System.out.println("    Channel接口定义抽象方法");
        System.out.println("    具体实现类(如SocketChannelImpl)持有原生Socket引用");
        System.out.println("    可以在运行时切换不同的Channel实现");
        System.out.println();

        System.out.println("Channel使用示例：");
        System.out.println("  FileChannel fileChannel = FileInputStream.getChannel();");
        System.out.println("  SocketChannel socketChannel = SocketChannel.open();");
        System.out.println("  DatagramChannel udpChannel = DatagramChannel.open();");
        System.out.println("  // 所有Channel都实现统一的Channel接口");
        System.out.println();
    }
}
