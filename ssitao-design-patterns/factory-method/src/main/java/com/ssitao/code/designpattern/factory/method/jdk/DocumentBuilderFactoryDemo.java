package com.ssitao.code.designpattern.factory.method.jdk;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * JDK 工厂方法模式示例 - DocumentBuilderFactory.newInstance()
 *
 * DocumentBuilderFactory 是抽象工厂，newInstance() 是工厂方法，
 * 返回 DocumentBuilder 实例，用于解析 XML 文档
 *
 * 工厂方法模式体现：
 * - 抽象工厂：DocumentBuilderFactory
 * - 具体工厂：DefaultDocumentBuilderFactory, XML11DocumentBuilderFactory 等
 * - 抽象产品：DocumentBuilder
 * - 具体产品：XML11DocumentBuilder 等
 *
 * 相关工厂类：
 * - DocumentBuilderFactory.newInstance() -> DocumentBuilder
 * - SAXParserFactory.newInstance() -> SAXParser
 * - TransformerFactory.newInstance() -> Transformer
 * - XPathFactory.newInstance() -> XPath
 */
public class DocumentBuilderFactoryDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== DocumentBuilderFactory.newInstance() 工厂方法示例 ===\n");

        // 1. 创建默认的 DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        System.out.println("Factory 类型: " + factory.getClass().getName());

        // 2. 创建 DocumentBuilder（工厂生产的产品）
        DocumentBuilder builder = factory.newDocumentBuilder();
        System.out.println("Builder 类型: " + builder.getClass().getName());

        // 3. 使用 DocumentBuilder 解析 XML
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<root>" +
                "  <user id=\"1\">" +
                "    <name>张三</name>" +
                "    <email>zhangsan@example.com</email>" +
                "  </user>" +
                "</root>";

        Document document = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        Element root = document.getDocumentElement();
        System.out.println("\n解析结果:");
        System.out.println("  根元素: " + root.getTagName());

        // 4. 演示其他工厂方法变体
        System.out.println("\n=== 其他 XML 解析相关工厂 ===");

        // SAXParserFactory
        javax.xml.parsers.SAXParserFactory saxFactory = javax.xml.parsers.SAXParserFactory.newInstance();
        javax.xml.parsers.SAXParser saxParser = saxFactory.newSAXParser();
        System.out.println("SAXParser 类型: " + saxParser.getClass().getName());

        // TransformerFactory (用于 XSLT 转换)
        javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
        System.out.println("Transformer 类型: " + transformer.getClass().getName());

        // XPathFactory
        javax.xml.xpath.XPathFactory xpathFactory = javax.xml.xpath.XPathFactory.newInstance();
        javax.xml.xpath.XPath xpath = xpathFactory.newXPath();
        System.out.println("XPath 类型: " + xpath.getClass().getName());
    }
}
