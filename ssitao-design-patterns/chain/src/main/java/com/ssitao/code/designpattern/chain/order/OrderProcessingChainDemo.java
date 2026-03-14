package com.ssitao.code.designpattern.chain.order;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 订单处理责任链 - 业务场景示例
 *
 * 订单处理流程：
 * 1. 订单验证 - 检查订单基本信息
 * 2. 库存检查 - 检查商品库存是否充足
 * 3. 价格计算 - 计算订单价格和优惠
 * 4. 支付处理 - 处理支付
 * 5. 物流处理 - 创建物流订单
 * 6. 订单完成 - 更新订单状态
 *
 * 每个处理者都可以：
 * - 处理成功，继续下一个
 * - 处理失败，中断链条
 */
public class OrderProcessingChainDemo {

    public static void main(String[] args) {
        // 构建订单处理链
        OrderProcessor processor = new OrderProcessor();
        processor.addHandler(new OrderValidationHandler())
                 .addHandler(new InventoryCheckHandler())
                 .addHandler(new PriceCalculationHandler())
                 .addHandler(new PaymentHandler())
                 .addHandler(new LogisticsHandler())
                 .addHandler(new OrderCompletionHandler());

        // 测试不同场景
        System.out.println("=== 场景1: 正常订单 ===");
        Order order1 = new Order("ORD001", Arrays.asList(
            new OrderItem("商品A", 2, 100.0),
            new OrderItem("商品B", 1, 200.0)
        ));
        OrderResult result1 = processor.process(order1);
        printResult(result1);

        System.out.println("\n=== 场景2: 库存不足 ===");
        Order order2 = new Order("ORD002", Arrays.asList(
            new OrderItem("商品C", 100, 50.0) // 库存只有50
        ));
        OrderResult result2 = processor.process(order2);
        printResult(result2);

        System.out.println("\n=== 场景3: 订单为空 ===");
        Order order3 = new Order("ORD003", new ArrayList<>());
        OrderResult result3 = processor.process(order3);
        printResult(result3);

        System.out.println("\n=== 场景4: 使用优惠券 ===");
        Order order4 = new Order("ORD004", Arrays.asList(
            new OrderItem("商品A", 1, 100.0)
        ));
        order4.setCouponCode("SAVE10");
        OrderResult result4 = processor.process(order4);
        printResult(result4);
    }

    private static void printResult(OrderResult result) {
        System.out.println("处理结果: " + (result.isSuccess() ? "成功" : "失败"));
        System.out.println("消息: " + result.getMessage());
        System.out.println("订单状态: " + result.getOrder().getStatus());
        System.out.println("订单金额: " + result.getOrder().getTotalAmount());
    }
}

/**
 * 订单
 */
class Order {
    private String orderId;
    private List<OrderItem> items;
    private OrderStatus status;
    private double totalAmount;
    private double discountedAmount;
    private String couponCode;
    private String errorMessage;

    public Order(String orderId, List<OrderItem> items) {
        this.orderId = orderId;
        this.items = items;
        this.status = OrderStatus.CREATED;
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public double getDiscountedAmount() { return discountedAmount; }
    public void setDiscountedAmount(double discountedAmount) { this.discountedAmount = discountedAmount; }
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}

/**
 * 订单项
 */
class OrderItem {
    private String productName;
    private int quantity;
    private double price;

    public OrderItem(String productName, int quantity, double price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getSubTotal() { return quantity * price; }
}

/**
 * 订单状态
 */
enum OrderStatus {
    CREATED,
    VALIDATED,
    INVENTORY_CHECKED,
    PRICED,
    PAID,
    SHIPPED,
    COMPLETED,
    FAILED
}

/**
 * 订单处理结果
 */
class OrderResult {
    private Order order;
    private boolean success;
    private String message;
    private List<String> processedSteps;

    public OrderResult(Order order, boolean success, String message) {
        this.order = order;
        this.success = success;
        this.message = message;
        this.processedSteps = new ArrayList<>();
    }

    public Order getOrder() { return order; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<String> getProcessedSteps() { return processedSteps; }
    public void addProcessedStep(String step) { processedSteps.add(step); }
}

/**
 * 订单处理器接口
 */
interface OrderHandler {
    /**
     * 处理订单
     * @param order 订单
     * @return 处理结果，如果返回null表示继续传递；如果返回OrderResult表示处理完成
     */
    OrderResult handle(Order order);
}

/**
 * 订单处理器链
 */
class OrderProcessor {
    private final List<OrderHandler> handlers = new ArrayList<>();

    public OrderProcessor addHandler(OrderHandler handler) {
        handlers.add(handler);
        return this;
    }

    public OrderResult process(Order order) {
        System.out.println("\n开始处理订单: " + order.getOrderId());

        OrderResult result = null;

        for (OrderHandler handler : handlers) {
            System.out.println("  -> 执行: " + handler.getClass().getSimpleName());
            result = handler.handle(order);

            // 如果处理失败，中断链条
            if (!result.isSuccess()) {
                System.out.println("  -> 处理失败: " + result.getMessage());
                order.setStatus(OrderStatus.FAILED);
                order.setErrorMessage(result.getMessage());
                return result;
            }

            result.addProcessedStep(handler.getClass().getSimpleName());
        }

        return result;
    }
}

/**
 * 1. 订单验证处理器
 */
class OrderValidationHandler implements OrderHandler {
    @Override
    public OrderResult handle(Order order) {
        // 检查订单ID
        if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
            return new OrderResult(order, false, "订单ID不能为空");
        }

        // 检查订单项
        if (order.getItems() == null || order.getItems().isEmpty()) {
            return new OrderResult(order, false, "订单不能为空");
        }

        // 检查每个订单项
        for (OrderItem item : order.getItems()) {
            if (item.getQuantity() <= 0) {
                return new OrderResult(order, false, "商品数量必须大于0");
            }
            if (item.getPrice() < 0) {
                return new OrderResult(order, false, "商品价格不能为负数");
            }
        }

        System.out.println("    订单验证通过");
        order.setStatus(OrderStatus.VALIDATED);
        return new OrderResult(order, true, "订单验证通过");
    }
}

/**
 * 2. 库存检查处理器
 */
class InventoryCheckHandler implements OrderHandler {
    // 模拟库存数据
    private static final Map<String, Integer> INVENTORY = new HashMap<String, Integer>() {{
        put("商品A", 100);
        put("商品B", 50);
        put("商品C", 50);
        put("商品D", 200);
    }};

    @Override
    public OrderResult handle(Order order) {
        for (OrderItem item : order.getItems()) {
            int available = INVENTORY.getOrDefault(item.getProductName(), 0);
            if (available < item.getQuantity()) {
                return new OrderResult(order, false,
                    "商品[" + item.getProductName() + "]库存不足，当前库存: " + available + "，需要: " + item.getQuantity());
            }
        }

        System.out.println("    库存检查通过");
        order.setStatus(OrderStatus.INVENTORY_CHECKED);
        return new OrderResult(order, true, "库存检查通过");
    }
}

/**
 * 3. 价格计算处理器
 */
class PriceCalculationHandler implements OrderHandler {

    @Override
    public OrderResult handle(Order order) {
        // 计算原价
        double originalAmount = order.getItems().stream()
            .mapToDouble(OrderItem::getSubTotal)
            .sum();

        order.setTotalAmount(originalAmount);

        // 应用优惠券
        double discount = 0;
        if (order.getCouponCode() != null && !order.getCouponCode().isEmpty()) {
            discount = calculateDiscount(order.getCouponCode(), originalAmount);
            order.setDiscountedAmount(discount);
            System.out.println("    应用优惠券: " + order.getCouponCode() + "，折扣: " + discount);
        }

        double finalAmount = originalAmount - discount;
        order.setTotalAmount(finalAmount);

        System.out.println("    价格计算: 原价=" + originalAmount + "，折扣=" + discount + "，最终=" + finalAmount);
        order.setStatus(OrderStatus.PRICED);
        return new OrderResult(order, true, "价格计算完成");
    }

    private double calculateDiscount(String couponCode, double amount) {
        if ("SAVE10".equals(couponCode)) {
            return amount * 0.1;  // 9折
        } else if ("SAVE20".equals(couponCode)) {
            return amount * 0.2;  // 8折
        } else if ("HALF".equals(couponCode)) {
            return amount * 0.5;  // 半价
        }
        return 0;
    }
}

/**
 * 4. 支付处理器
 */
class PaymentHandler implements OrderHandler {

    @Override
    public OrderResult handle(Order order) {
        double amount = order.getTotalAmount();

        // 模拟支付处理
        System.out.println("    发起支付，金额: " + amount);

        // 假设支付成功
        boolean paymentSuccess = true;

        if (paymentSuccess) {
            System.out.println("    支付成功");
            order.setStatus(OrderStatus.PAID);
            return new OrderResult(order, true, "支付成功");
        } else {
            return new OrderResult(order, false, "支付失败");
        }
    }
}

/**
 * 5. 物流处理器
 */
class LogisticsHandler implements OrderHandler {

    @Override
    public OrderResult handle(Order order) {
        // 模拟创建物流订单
        String trackingNumber = "LP" + System.currentTimeMillis();
        System.out.println("    创建物流订单，运单号: " + trackingNumber);

        order.setStatus(OrderStatus.SHIPPED);
        return new OrderResult(order, true, "物流订单已创建");
    }
}

/**
 * 6. 订单完成处理器
 */
class OrderCompletionHandler implements OrderHandler {

    @Override
    public OrderResult handle(Order order) {
        order.setStatus(OrderStatus.COMPLETED);
        System.out.println("    订单已完成");
        return new OrderResult(order, true, "订单处理完成");
    }
}
