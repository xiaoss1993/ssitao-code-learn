package com.ssitao.code.designpattern.template.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务场景中的模板方法模式
 *
 * 典型应用：
 * 1. 报表生成 - 不同格式不同实现
 * 2. 数据导出 - Excel/CSV/PDF
 * 3. 支付流程 - 不同支付方式流程
 * 4. 业务流程 - 审批/通知流程
 * 5. 测试框架 - 测试用例执行
 */
public class BizTemplateDemo {

    public static void main(String[] args) {
        System.out.println("=== 业务场景模板方法模式 ===\n");

        // 1. 报表生成
        System.out.println("1. 报表生成");
        reportDemo();

        // 2. 数据导出
        System.out.println("\n2. 数据导出");
        exportDemo();

        // 3. 支付流程
        System.out.println("\n3. 支付流程");
        paymentDemo();
    }

    /**
     * 报表生成示例
     */
    private static void reportDemo() {
        // 生成Excel报表
        System.out.println("--- Excel报表 ---");
        ExcelReportGenerator excelReport = new ExcelReportGenerator();
        excelReport.generate();

        // 生成PDF报表
        System.out.println("\n--- PDF报表 ---");
        PdfReportGenerator pdfReport = new PdfReportGenerator();
        pdfReport.generate();

        // 生成HTML报表
        System.out.println("\n--- HTML报表 ---");
        HtmlReportGenerator htmlReport = new HtmlReportGenerator();
        htmlReport.generate();
    }

    /**
     * 数据导出示例
     */
    private static void exportDemo() {
        // 模拟数据
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> row1 = new HashMap<>();
        row1.put("name", "张三");
        row1.put("age", 25);
        data.add(row1);

        // 导出为CSV
        System.out.println("--- CSV导出 ---");
        CsvExporter csvExporter = new CsvExporter();
        csvExporter.export(data);

        // 导出为JSON
        System.out.println("\n--- JSON导出 ---");
        JsonExporter jsonExporter = new JsonExporter();
        jsonExporter.export(data);

        // 导出为XML
        System.out.println("\n--- XML导出 ---");
        XmlExporter xmlExporter = new XmlExporter();
        xmlExporter.export(data);
    }

    /**
     * 支付流程示例
     */
    private static void paymentDemo() {
        PaymentProcessor alipay = new AlipayProcessor();
        System.out.println("--- 支付宝支付 ---");
        alipay.process("ORD-001", 100.0);

        System.out.println("\n--- 微信支付 ---");
        PaymentProcessor wechat = new WechatPayProcessor();
        wechat.process("ORD-001", 100.0);

        System.out.println("\n--- 银行卡支付 ---");
        PaymentProcessor card = new CardPaymentProcessor();
        card.process("ORD-001", 100.0);
    }
}

// ============================================
// 1. 报表生成相关类
// ============================================

/**
 * 报表生成器基类
 */
abstract class ReportGenerator {
    /**
     * 模板方法 - 生成报表流程
     */
    public final void generate() {
        // 1. 获取数据
        List<Map<String, Object>> data = fetchData();

        // 2. 数据处理
        List<Map<String, Object>> processedData = processData(data);

        // 3. 生成报表
        String report = buildReport(processedData);

        // 4. 输出报表
        output(report);
    }

    // 获取数据
    protected List<Map<String, Object>> fetchData() {
        System.out.println("[报表] 获取数据");
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();
        row.put("name", "销售额");
        row.put("value", "10000");
        data.add(row);
        return data;
    }

    // 数据处理 - 子类可重写
    protected List<Map<String, Object>> processData(List<Map<String, Object>> data) {
        System.out.println("[报表] 处理数据");
        return data;
    }

    // 构建报表 - 子类实现
    protected abstract String buildReport(List<Map<String, Object>> data);

    // 输出报表 - 子类可重写
    protected void output(String report) {
        System.out.println("[报表] 输出: " + report);
    }
}

/**
 * Excel报表生成器
 */
class ExcelReportGenerator extends ReportGenerator {

    @Override
    protected String buildReport(List<Map<String, Object>> data) {
        System.out.println("[Excel报表] 构建Excel");
        return "Excel文件: report.xlsx";
    }
}

/**
 * PDF报表生成器
 */
class PdfReportGenerator extends ReportGenerator {

    @Override
    protected String buildReport(List<Map<String, Object>> data) {
        System.out.println("[PDF报表] 构建PDF");
        return "PDF文件: report.pdf";
    }
}

/**
 * HTML报表生成器
 */
class HtmlReportGenerator extends ReportGenerator {

    @Override
    protected List<Map<String, Object>> processData(List<Map<String, Object>> data) {
        System.out.println("[HTML报表] 处理HTML数据");
        return data;
    }

    @Override
    protected String buildReport(List<Map<String, Object>> data) {
        System.out.println("[HTML报表] 构建HTML");
        return "HTML文件: report.html";
    }
}

// ============================================
// 2. 数据导出相关类
// ============================================

/**
 * 数据导出器基类
 */
abstract class DataExporter {
    /**
     * 模板方法 - 导出流程
     */
    public final void export(List<Map<String, Object>> data) {
        // 1. 验证数据
        if (!validateData(data)) {
            System.out.println("[导出] 数据验证失败");
            return;
        }

        // 2. 转换数据
        String converted = convert(data);

        // 3. 写入文件
        write(converted);
    }

    // 验证数据
    protected boolean validateData(List<Map<String, Object>> data) {
        System.out.println("[导出] 验证数据");
        return data != null && !data.isEmpty();
    }

    // 转换数据 - 子类实现
    protected abstract String convert(List<Map<String, Object>> data);

    // 写入文件 - 子类可重写
    protected void write(String data) {
        System.out.println("[导出] 写入: " + data);
    }
}

/**
 * CSV导出器
 */
class CsvExporter extends DataExporter {

    @Override
    protected String convert(List<Map<String, Object>> data) {
        System.out.println("[CSV] 转换为CSV格式");
        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> row : data) {
            sb.append(row.values()).append("\n");
        }
        return sb.toString();
    }
}

/**
 * JSON导出器
 */
class JsonExporter extends DataExporter {

    @Override
    protected String convert(List<Map<String, Object>> data) {
        System.out.println("[JSON] 转换为JSON格式");
        return "[{\"name\":\"张三\",\"age\":25}]";
    }
}

/**
 * XML导出器
 */
class XmlExporter extends DataExporter {

    @Override
    protected String convert(List<Map<String, Object>> data) {
        System.out.println("[XML] 转换为XML格式");
        return "<root><row><name>张三</name><age>25</age></row></root>";
    }
}

// ============================================
// 3. 支付流程相关类
// ============================================

/**
 * 支付处理器基类
 */
abstract class PaymentProcessor {
    /**
     * 模板方法 - 支付流程
     */
    public final void process(String orderId, double amount) {
        // 1. 验证订单
        if (!validateOrder(orderId, amount)) {
            System.out.println("[支付] 订单验证失败");
            return;
        }

        // 2. 计算金额
        double finalAmount = calculateAmount(amount);

        // 3. 扣款
        boolean success = deduct(finalAmount);

        // 4. 处理结果
        handleResult(success, orderId);
    }

    // 验证订单
    protected boolean validateOrder(String orderId, double amount) {
        System.out.println("[支付] 验证订单: " + orderId);
        return orderId != null && amount > 0;
    }

    // 计算金额 - 子类可重写
    protected double calculateAmount(double amount) {
        System.out.println("[支付] 计算金额: " + amount);
        return amount;
    }

    // 扣款 - 子类实现
    protected abstract boolean deduct(double amount);

    // 处理结果 - 子类可重写
    protected void handleResult(boolean success, String orderId) {
        if (success) {
            System.out.println("[支付] 支付成功: " + orderId);
        } else {
            System.out.println("[支付] 支付失败: " + orderId);
        }
    }
}

/**
 * 支付宝
 */
class AlipayProcessor extends PaymentProcessor {

    @Override
    protected double calculateAmount(double amount) {
        System.out.println("[支付宝] 计算实际支付金额");
        return amount * 0.99; // 99折
    }

    @Override
    protected boolean deduct(double amount) {
        System.out.println("[支付宝] 扣款: " + amount);
        return true;
    }
}

/**
 * 微信支付
 */
class WechatPayProcessor extends PaymentProcessor {

    @Override
    protected boolean deduct(double amount) {
        System.out.println("[微信支付] 扣款: " + amount);
        return true;
    }
}

/**
 * 银行卡支付
 */
class CardPaymentProcessor extends PaymentProcessor {

    @Override
    protected double calculateAmount(double amount) {
        System.out.println("[银行卡] 计算手续费");
        return amount + 1; // 1元手续费
    }

    @Override
    protected boolean deduct(double amount) {
        System.out.println("[银行卡] 扣款: " + amount);
        return true;
    }

    @Override
    protected void handleResult(boolean success, String orderId) {
        System.out.println("[银行卡] 发送短信通知");
        super.handleResult(success, orderId);
    }
}

/**
 * Spring Batch中的模板方法
 *
 * 1. ItemReader
 * public class CustomItemReader extends ItemReader {
 *     public Object read() {
 *         // 读取数据
 *     }
 * }
 *
 * 2. ItemProcessor
 * public class CustomItemProcessor implements ItemProcessor {
 *     public Object process(Object item) {
 *         // 处理数据
 *         return item;
 *     }
 * }
 *
 * 3. ItemWriter
 * public class CustomItemWriter extends ItemWriter {
 *     public void write(List items) {
 *         // 写入数据
 *     }
 * }
 *
 * 4. StepBuilder
 * @Bean
 * public Step step(StepBuilderFactory stepBuilderFactory) {
 *     return stepBuilderFactory.get("step")
 *         .chunk(10)
 *         .reader(reader())
 *         .processor(processor())
 *         .writer(writer())
 *         .build();
 * }
 */
