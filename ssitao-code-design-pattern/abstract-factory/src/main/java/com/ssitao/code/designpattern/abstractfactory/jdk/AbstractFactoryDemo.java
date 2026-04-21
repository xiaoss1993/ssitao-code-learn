package com.ssitao.code.designpattern.abstractfactory.jdk;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import java.nio.charset.Charset;
import java.util.*;

/**
 * JDK中的抽象工厂模式示例
 *
 * 抽象工厂模式在JDK中有多个经典应用，这里展示几个典型例子：
 *
 * 1. DocumentBuilderFactory - XML解析器工厂
 * 2. SAXParserFactory - SAX解析器工厂
 * 3. TransformerFactory - XML转换器工厂
 * 4. CharsetProvider - 字符集提供者工厂
 */
public class AbstractFactoryDemo {

    /**
     * 1. DocumentBuilderFactory 示例
     *
     * DocumentBuilderFactory 是一个抽象工厂，用于创建 XML 解析器（DocumentBuilder）。
     * 不同的实现可以创建不同的 DocumentBuilder 实例。
     */
    public static void documentBuilderFactoryExample() {
        System.out.println("=== DocumentBuilderFactory 示例 ===");
        try {
            // 获取抽象工厂实例
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // 配置工厂属性
            factory.setNamespaceAware(true);
            factory.setValidating(false);

            // 通过工厂创建具体产品
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();

            System.out.println("DocumentBuilder 类: " + builder.getClass().getName());
            System.out.println("DocumentBuilderFactory 类: " + factory.getClass().getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 2. SAXParserFactory 示例
     *
     * SAXParserFactory 是用于创建 SAX 解析器的抽象工厂
     */
    public static void saxParserFactoryExample() {
        System.out.println("\n=== SAXParserFactory 示例 ===");
        try {
            // 获取抽象工厂实例
            SAXParserFactory factory = SAXParserFactory.newInstance();

            // 配置工厂
            factory.setNamespaceAware(true);

            // 创建 SAX 解析器
            javax.xml.parsers.SAXParser parser = factory.newSAXParser();

            System.out.println("SAXParser 类: " + parser.getClass().getName());
            System.out.println("SAXParserFactory 类: " + factory.getClass().getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 3. TransformerFactory 示例
     *
     * TransformerFactory 用于创建 XML 转换器
     */
    public static void transformerFactoryExample() {
        System.out.println("\n=== TransformerFactory 示例 ===");
        try {
            // 获取抽象工厂
            javax.xml.transform.TransformerFactory factory =
                javax.xml.transform.TransformerFactory.newInstance();

            // 创建转换器
            javax.xml.transform.Transformer transformer = factory.newTransformer();

            System.out.println("Transformer 类: " + transformer.getClass().getName());
            System.out.println("TransformerFactory 类: " + factory.getClass().getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 4. CharsetProvider 模拟示例
     *
     * Charset 使用 SPI (Service Provider Interface) 机制，
     * 本质上也是一种抽象工厂模式的应用
     */
    public static void charsetExample() {
        System.out.println("\n=== Charset 提供者示例 ===");

        // 获取所有可用的字符集
        SortedMap<String, Charset> availableCharsets = Charset.availableCharsets();

        System.out.println("可用字符集数量: " + availableCharsets.size());
        System.out.println("常用字符集:");
        availableCharsets.keySet().stream()
            .filter(name -> name.startsWith("UTF") || name.equals("GBK") || name.equals("ISO-8859-1"))
            .limit(10)
            .forEach(System.out::println);

        // 通过名称创建 Charset 实例
        Charset utf8 = Charset.forName("UTF-8");
        System.out.println("\nUTF-8 Charset 类: " + utf8.getClass().getName());
    }

    /**
     * 5. java.util.ServiceLoader (SPI机制)
     *
     * ServiceLoader 是 Java SPI 机制的核心类，实现了一种特殊的工厂模式
     */
    public static void serviceLoaderExample() {
        System.out.println("\n=== ServiceLoader (SPI) 示例 ===");

        // ServiceLoader 可以加载特定接口的所有实现
        // 这里演示如何获取文件系统提供者
        ServiceLoader<java.nio.file.spi.FileSystemProvider> providers =
            ServiceLoader.load(java.nio.file.spi.FileSystemProvider.class);

        System.out.println("文件系统提供者:");
        for (java.nio.file.spi.FileSystemProvider provider : providers) {
            System.out.println("  - " + provider.getClass().getName());
        }
    }

    /**
     * 6. javax.sql.DataSource 示例
     *
     * DataSource 接口的实现通常通过连接池工厂创建
     * 这里展示连接池工厂的概念
     */
    public static void dataSourceFactoryExample() {
        System.out.println("\n=== DataSource 工厂示例 ===");

        // 模拟数据源工厂
        DataSourceFactory factory = new HikariDataSourceFactory();
        javax.sql.DataSource dataSource = factory.createDataSource();

        System.out.println("DataSource 类: " + dataSource.getClass().getName());
    }

    // ==================== 辅助类 ====================

    /**
     * 数据源工厂接口
     */
    interface DataSourceFactory {
        javax.sql.DataSource createDataSource();
    }

    /**
     * HikariCP 数据源工厂（模拟）
     */
    static class HikariDataSourceFactory implements DataSourceFactory {
        @Override
        public javax.sql.DataSource createDataSource() {
            // 模拟创建 HikariDataSource
            return new javax.sql.DataSource() {
                @Override
                public java.sql.Connection getConnection() throws java.sql.SQLException {
                    return null;
                }

                @Override
                public java.sql.Connection getConnection(String username, String password)
                        throws java.sql.SQLException {
                    return null;
                }

                // 其他接口方法省略...
                @Override
                public java.io.PrintWriter getLogWriter() throws java.sql.SQLException {
                    return null;
                }

                @Override
                public void setLogWriter(java.io.PrintWriter out) throws java.sql.SQLException {
                }

                @Override
                public void setLoginTimeout(int seconds) throws java.sql.SQLException {
                }

                @Override
                public int getLoginTimeout() throws java.sql.SQLException {
                    return 0;
                }

                @Override
                public java.util.logging.Logger getParentLogger() throws java.sql.SQLFeatureNotSupportedException {
                    return null;
                }

                @Override
                public <T> T unwrap(Class<T> iface) throws java.sql.SQLException {
                    return null;
                }

                @Override
                public boolean isWrapperFor(Class<?> iface) throws java.sql.SQLException {
                    return false;
                }
            };
        }
    }

    public static void main(String[] args) {
        documentBuilderFactoryExample();
        saxParserFactoryExample();
        transformerFactoryExample();
        charsetExample();
        serviceLoaderExample();
        dataSourceFactoryExample();
    }
}
