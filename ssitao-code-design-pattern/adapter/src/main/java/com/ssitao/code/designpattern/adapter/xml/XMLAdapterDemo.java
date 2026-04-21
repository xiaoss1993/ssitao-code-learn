package com.ssitao.code.designpattern.adapter.xml;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import org.w3c.dom.*;

/**
 * XML解析中的适配器模式示例
 *
 * JDK和Spring中使用适配器模式处理XML：
 * 1. DocumentBuilder - 将XML文档适配为DOM对象
 * 2. SAXParser - 将XML适配为SAX事件流
 * 3. Transformer - 将一种DOM转换为另一种格式
 */
public class XMLAdapterDemo {

    public static void main(String[] args) {
        System.out.println("=== XML Parser Adapter Demo ===\n");

        // 1. DocumentBuilder - XML到DOM的适配
        demonstrateDocumentBuilder();

        // 2. SAXParser - XML到事件的适配
        demonstrateSAXParser();

        // 3. StAX - 另一种XML流式解析
        demonstrateStAX();
    }

    /**
     * 1. DocumentBuilder示例
     * DocumentBuilder将XML InputStream适配为Document（DOM对象）
     */
    private static void demonstrateDocumentBuilder() {
        System.out.println("--- 1. DocumentBuilder (XML -> DOM) ---");

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<users>\n");
        sb.append("    <user id=\"1\">\n");
        sb.append("        <name>Alice</name>\n");
        sb.append("        <age>25</age>\n");
        sb.append("    </user>\n");
        sb.append("    <user id=\"2\">\n");
        sb.append("        <name>Bob</name>\n");
        sb.append("        <age>30</age>\n");
        sb.append("    </user>\n");
        sb.append("</users>");

        try {
            // DocumentBuilder是适配器：将XML适配为DOM Document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // parse方法接受InputStream/File/String，输出Document
            Document document = builder.parse(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));

            // 遍历DOM树
            NodeList userNodes = document.getElementsByTagName("user");
            System.out.println("用户数量: " + userNodes.getLength());

            for (int i = 0; i < userNodes.getLength(); i++) {
                Element user = (Element) userNodes.item(i);
                String id = user.getAttribute("id");
                String name = user.getElementsByTagName("name").item(0).getTextContent();
                String age = user.getElementsByTagName("age").item(0).getTextContent();
                System.out.println("  User " + id + ": " + name + ", " + age);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * 2. SAXParser示例
     * SAXParser将XML InputStream适配为SAX事件序列
     */
    private static void demonstrateSAXParser() {
        System.out.println("--- 2. SAXParser (XML -> SAX Events) ---");

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<users>\n");
        sb.append("    <user id=\"1\">\n");
        sb.append("        <name>Alice</name>\n");
        sb.append("    </user>\n");
        sb.append("</users>");

        try {
            // SAXParser是适配器：将XML适配为SAX事件
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            // 创建自定义Handler来处理SAX事件
            MySAXHandler handler = new MySAXHandler();

            // parse方法：InputStream -> SAX Events -> Handler回调
            parser.parse(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")), handler);

            System.out.println("SAX事件: " + handler.getEvents());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * 3. StAX (Streaming API for XML) 示例
     * StAX提供基于拉取（pull）的XML解析方式
     */
    private static void demonstrateStAX() {
        System.out.println("--- 3. StAX (Pull Parser) ---");

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<config>\n");
        sb.append("    <database>\n");
        sb.append("        <host>localhost</host>\n");
        sb.append("        <port>3306</port>\n");
        sb.append("    </database>\n");
        sb.append("</config>");

        try {
            // StAX使用XMLStreamReader进行拉取式解析
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(
                new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));

            List<String> events = new ArrayList<>();

            // 手动拉取每个事件
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    events.add("START: " + reader.getLocalName());
                } else if (event == XMLStreamConstants.END_ELEMENT) {
                    events.add("END: " + reader.getLocalName());
                } else if (event == XMLStreamConstants.CHARACTERS) {
                    String text = reader.getText().trim();
                    if (!text.isEmpty()) {
                        events.add("TEXT: " + text);
                    }
                }
            }

            System.out.println("StAX事件流:");
            for (String e : events) {
                System.out.println("  " + e);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
    }
}

/**
 * 自定义SAX处理器
 */
class MySAXHandler extends org.xml.sax.helpers.DefaultHandler {
    private List<String> events = new ArrayList<>();

    @Override
    public void startElement(String uri, String localName, String qName,
                            org.xml.sax.Attributes attributes) {
        events.add("startElement: " + qName);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        events.add("endElement: " + qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String text = new String(ch, start, length).trim();
        if (!text.isEmpty()) {
            events.add("characters: " + text);
        }
    }

    public List<String> getEvents() {
        return events;
    }
}
