package com.ssitao.code.designpattern.memento.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Web应用中的备忘录模式示例
 *
 * 应用场景：
 * 1. 表单数据暂存 - 用户填写表单时可保存/恢复
 * 2. 购物车状态 - 临时保存购物车
 * 3. 页面状态保存 - SPA应用路由状态
 * 4. 分布式事务补偿 - TCC模式
 * 5. 缓存失效恢复 - 缓存回滚
 */
public class WebMementoDemo {

    public static void main(String[] args) {
        System.out.println("=== Web备忘录模式示例 ===\n");

        // 1. 表单暂存
        System.out.println("1. 表单暂存示例");
        formSaveDemo();

        // 2. 购物车状态
        System.out.println("\n2. 购物车状态示例");
        cartDemo();

        // 3. 分布式事务补偿
        System.out.println("\n3. 分布式事务补偿示例");
        transactionCompensationDemo();
    }

    /**
     * 表单暂存示例 - 类似草稿箱功能
     */
    private static void formSaveDemo() {
        // 创建表单管理器
        FormManager formManager = new FormManager();

        // 用户填写表单
        System.out.println("--- 用户填写基本信息 ---");
        UserRegistrationForm form = new UserRegistrationForm();
        form.setUsername("john");
        form.setEmail("john@example.com");
        form.setPhone("13800138000");

        // 暂存表单
        System.out.println("暂存表单: " + form);
        formManager.saveDraft(form);

        // 用户修改
        System.out.println("\n--- 用户修改表单 ---");
        form.setEmail("john2@example.com");
        form.setPhone("13900139000");
        System.out.println("当前表单: " + form);

        // 恢复草稿
        System.out.println("\n--- 恢复草稿 ---");
        formManager.restoreDraft(form);
        System.out.println("恢复后: " + form);

        // 再次修改并提交
        System.out.println("\n--- 提交表单 ---");
        form.setAge(25);
        formManager.saveDraft(form);
        System.out.println("提交成功，清除草稿");
        formManager.clearDraft();
    }

    /**
     * 购物车状态示例 - 类似京东购物车
     */
    private static void cartDemo() {
        // 创建购物车
        ShoppingCart cart = new ShoppingCart();
        CartSnapshotManager snapshotManager = new CartSnapshotManager();

        // 添加商品
        System.out.println("--- 添加商品到购物车 ---");
        cart.addItem("iPhone 15", 1, 6999.0);
        cart.addItem("MacBook Pro", 1, 14999.0);
        System.out.println("购物车: " + cart);
        snapshotManager.saveSnapshot(cart);

        // 用户修改数量
        System.out.println("\n--- 修改商品数量 ---");
        cart.updateQuantity("iPhone 15", 2);
        System.out.println("购物车: " + cart);

        // 保存快照
        snapshotManager.saveSnapshot(cart);

        // 删除商品
        System.out.println("\n--- 删除MacBook ---");
        cart.removeItem("MacBook Pro");
        System.out.println("购物车: " + cart);

        // 恢复到上一个状态
        System.out.println("\n--- 撤销删除操作 ---");
        snapshotManager.undo(cart);
        System.out.println("恢复后: " + cart);

        // 恢复到最初状态
        System.out.println("\n--- 恢复最初状态 ---");
        snapshotManager.undoToFirst(cart);
        System.out.println("恢复后: " + cart);
    }

    /**
     * 分布式事务补偿示例 - 类似TCC模式
     */
    private static void transactionCompensationDemo() {
        // 创建分布式事务管理器
        DistributedTransactionManager txManager = new DistributedTransactionManager();

        // 创建订单服务
        OrderService orderService = new OrderService();
        InventoryService inventoryService = new InventoryService();
        PaymentService paymentService = new PaymentService();

        // 开始分布式事务
        System.out.println("--- 开始创建订单 ---");
        String orderId = "ORD-" + System.currentTimeMillis();

        try {
            // 1. 创建订单（保存用于补偿的状态）
            System.out.println("步骤1: 创建订单");
            orderService.createOrder(orderId, "用户123", 500.0);
            txManager.registerCompensate(orderService, orderId);

            // 保存检查点
            txManager.savepoint();

            // 2. 扣减库存
            System.out.println("步骤2: 扣减库存");
            inventoryService.deductInventory("商品A", 2);
            txManager.registerCompensate(inventoryService, "商品A", 2);

            // 3. 支付（模拟可能失败）
            System.out.println("步骤3: 支付");
            // 这里故意让支付失败
            throw new RuntimeException("支付网关超时");

        } catch (Exception e) {
            System.out.println("\n--- 事务失败，开始补偿 ---");
            System.out.println("错误: " + e.getMessage());

            // 执行补偿（回滚）
            txManager.compensate();
            System.out.println("补偿完成，订单已取消，库存已恢复");
        }
    }
}

// ============================================
// 1. 表单暂存相关类
// ============================================

/**
 * 用户注册表单
 */
class UserRegistrationForm {
    private String username;
    private String email;
    private String phone;
    private Integer age;
    private String address;

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // 创建备忘录
    public FormMemento createMemento() {
        return new FormMemento(username, email, phone, age, address);
    }

    // 恢复备忘录
    public void restoreMemento(FormMemento memento) {
        this.username = memento.getUsername();
        this.email = memento.getEmail();
        this.phone = memento.getPhone();
        this.age = memento.getAge();
        this.address = memento.getAddress();
    }

    @Override
    public String toString() {
        return "Form{username='" + username + "', email='" + email +
            "', phone='" + phone + "', age=" + age + "}";
    }
}

/**
 * 表单备忘录
 */
class FormMemento {
    private String username;
    private String email;
    private String phone;
    private Integer age;
    private String address;
    private long timestamp;

    public FormMemento(String username, String email, String phone,
                       Integer age, String address) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.address = address;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Integer getAge() { return age; }
    public String getAddress() { return address; }
    public long getTimestamp() { return timestamp; }
}

/**
 * 表单管理器 - 草稿箱
 */
class FormManager {
    private FormMemento draft;

    // 保存草稿
    public void saveDraft(UserRegistrationForm form) {
        draft = form.createMemento();
        System.out.println("草稿已保存");
    }

    // 恢复草稿
    public void restoreDraft(UserRegistrationForm form) {
        if (draft != null) {
            form.restoreMemento(draft);
            System.out.println("草稿已恢复");
        } else {
            System.out.println("没有草稿");
        }
    }

    // 清除草稿
    public void clearDraft() {
        draft = null;
    }
}

// ============================================
// 2. 购物车相关类
// ============================================

/**
 * 购物车
 */
class ShoppingCart {
    private Map<String, CartItem> items = new HashMap<>();

    public void addItem(String productName, int quantity, double price) {
        items.put(productName, new CartItem(productName, quantity, price));
    }

    public void updateQuantity(String productName, int quantity) {
        CartItem item = items.get(productName);
        if (item != null) {
            item.setQuantity(quantity);
        }
    }

    public void removeItem(String productName) {
        items.remove(productName);
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items.values()) {
            total += item.getSubTotal();
        }
        return total;
    }

    // 创建快照
    public CartMemento createSnapshot() {
        Map<String, CartItem> copy = new HashMap<>();
        for (Map.Entry<String, CartItem> entry : items.entrySet()) {
            CartItem item = entry.getValue();
            copy.put(entry.getKey(), new CartItem(item.getName(), item.getQuantity(), item.getPrice()));
        }
        return new CartMemento(copy);
    }

    // 恢复快照
    public void restoreSnapshot(CartMemento memento) {
        items = new HashMap<>(memento.getItems());
    }

    @Override
    public String toString() {
        return "ShoppingCart{items=" + items.keySet() + ", total=" + getTotal() + "}";
    }
}

/**
 * 购物车项
 */
class CartItem {
    private String name;
    private int quantity;
    private double price;

    public CartItem(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public double getSubTotal() { return quantity * price; }
}

/**
 * 购物车快照
 */
class CartMemento {
    private Map<String, CartItem> items;
    private long timestamp;

    public CartMemento(Map<String, CartItem> items) {
        this.items = items;
        this.timestamp = System.currentTimeMillis();
    }

    public Map<String, CartItem> getItems() { return items; }
    public long getTimestamp() { return timestamp; }
}

/**
 * 购物车快照管理器
 */
class CartSnapshotManager {
    private Stack<CartMemento> history = new Stack<>();

    public void saveSnapshot(ShoppingCart cart) {
        history.push(cart.createSnapshot());
    }

    public void undo(ShoppingCart cart) {
        if (!history.isEmpty()) {
            cart.restoreSnapshot(history.pop());
        }
    }

    public void undoToFirst(ShoppingCart cart) {
        if (!history.isEmpty()) {
            cart.restoreSnapshot(history.get(0));
            history.clear();
        }
    }
}

// ============================================
// 3. 分布式事务补偿相关类
// ============================================

/**
 * 分布式事务管理器
 */
class DistributedTransactionManager {
    private List<CompensateAction> actions = new ArrayList<>();
    private Stack<List<CompensateAction>> savepoints = new Stack<>();

    // 注册补偿操作
    public void registerCompensate(Object service, Object... params) {
        actions.add(new CompensateAction(service, params));
    }

    // 保存检查点
    public void savepoint() {
        savepoints.push(new ArrayList<>(actions));
    }

    // 执行补偿
    public void compensate() {
        // 逆序执行补偿操作
        for (int i = actions.size() - 1; i >= 0; i--) {
            CompensateAction action = actions.get(i);
            action.compensate();
        }
        actions.clear();
    }

    // 回滚到检查点
    public void rollbackToSavepoint() {
        if (!savepoints.isEmpty()) {
            List<CompensateAction> savepointActions = savepoints.pop();
            for (int i = actions.size() - 1; i >= savepointActions.size(); i--) {
                actions.get(i).compensate();
            }
            actions = new ArrayList<>(savepointActions);
        }
    }
}

/**
 * 补偿操作
 */
class CompensateAction {
    private Object service;
    private Object[] params;

    public CompensateAction(Object service, Object[] params) {
        this.service = service;
        this.params = params;
    }

    public void compensate() {
        if (service instanceof OrderService) {
            System.out.println("  [补偿] 取消订单: " + params[0]);
        } else if (service instanceof InventoryService) {
            System.out.println("  [补偿] 恢复库存: 商品=" + params[0] + ", 数量=" + params[1]);
        } else if (service instanceof PaymentService) {
            System.out.println("  [补偿] 退款: " + params[0]);
        }
    }
}

/**
 * 订单服务
 */
class OrderService {
    public void createOrder(String orderId, String userId, double amount) {
        System.out.println("  创建订单: " + orderId + ", 金额: " + amount);
    }

    public void cancelOrder(String orderId) {
        System.out.println("  取消订单: " + orderId);
    }
}

/**
 * 库存服务
 */
class InventoryService {
    public void deductInventory(String productName, int quantity) {
        System.out.println("  扣减库存: " + productName + ", 数量: " + quantity);
    }

    public void restoreInventory(String productName, int quantity) {
        System.out.println("  恢复库存: " + productName + ", 数量: " + quantity);
    }
}

/**
 * 支付服务
 */
class PaymentService {
    public void pay(String orderId, double amount) {
        System.out.println("  支付: " + orderId + ", 金额: " + amount);
    }

    public void refund(double amount) {
        System.out.println("  退款: " + amount);
    }
}
