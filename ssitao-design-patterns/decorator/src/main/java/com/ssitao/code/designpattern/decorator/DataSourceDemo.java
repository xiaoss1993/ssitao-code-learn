package com.ssitao.code.designpattern.decorator;

/**
 * 数据源装饰器模式演示
 */
public class DataSourceDemo {

    public static void main(String[] args) {
        System.out.println("========== 装饰器模式 - 数据源示例 ==========\n");

        String filename = "data.txt";

        // 1. 简单文件写入
        System.out.println("--- 简单文件写入 ---");
        DataSource source = new FileDataSource(filename);
        source.writeData("Hello World!");
        System.out.println("读取: " + source.readData());

        // 2. 带加密的文件写入
        System.out.println("\n--- 加密文件写入 ---");
        DataSource encryptedSource = new EncryptionDecorator(new FileDataSource(filename));
        encryptedSource.writeData("敏感数据");
        System.out.println("读取: " + encryptedSource.readData());

        // 3. 带日志的文件写入
        System.out.println("\n--- 日志文件写入 ---");
        DataSource loggedSource = new LoggingDecorator(new FileDataSource(filename));
        loggedSource.writeData("测试数据");
        loggedSource.readData();

        // 4. 组合装饰器: 压缩 + 加密 + 日志
        System.out.println("\n--- 组合装饰器: 压缩 + 加密 + 日志 ---");
        DataSource fancySource = new LoggingDecorator(
                                    new EncryptionDecorator(
                                        new CompressionDecorator(
                                            new FileDataSource(filename))));
        fancySource.writeData("这是一个很长的测试数据，用于演示压缩效果！");
        fancySource.readData();
        // 清理
    }
}
