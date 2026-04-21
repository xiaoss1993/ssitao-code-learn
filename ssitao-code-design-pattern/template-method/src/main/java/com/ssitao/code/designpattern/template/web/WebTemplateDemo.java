package com.ssitao.code.designpattern.template.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Web应用中的模板方法模式示例
 *
 * Web应用场景：
 * 1. Controller基类 - 请求处理流程
 * 2. Service基类 - 业务处理流程
 * 3. Filter链 - 请求过滤流程
 * 4. Interceptor - 拦截器流程
 * 5. 页面渲染 - 模板引擎
 */
public class WebTemplateDemo {

    public static void main(String[] args) {
        System.out.println("=== Web模板方法模式示例 ===\n");

        // 1. Controller模板
        System.out.println("1. Controller请求处理");
        controllerDemo();

        // 2. Service模板
        System.out.println("\n2. Service业务处理");
        serviceDemo();

        // 3. 验证器模板
        System.out.println("\n3. 参数验证器");
        validatorDemo();
    }

    /**
     * Controller模板示例
     */
    private static void controllerDemo() {
        // 用户Controller
        UserController userController = new UserController();
        System.out.println("--- 创建用户 ---");
        Map<String, Object> params = new HashMap<>();
        params.put("username", "zhangsan");
        params.put("email", "zhangsan@example.com");
        userController.handleRequest(params);

        System.out.println("\n--- 获取用户 ---");
        userController.handleRequest(null);
    }

    /**
     * Service模板示例
     */
    private static void serviceDemo() {
        OrderService orderService = new OrderService();

        System.out.println("--- 创建订单 ---");
        Order order = new Order("ORD-001", 100.0);
        orderService.createOrder(order);

        System.out.println("\n--- 更新订单 ---");
        order.setAmount(200.0);
        orderService.updateOrder(order);

        System.out.println("\n--- 删除订单 ---");
        orderService.deleteOrder("ORD-001");
    }

    /**
     * 验证器模板示例
     */
    private static void validatorDemo() {
        // 用户注册验证
        UserValidator userValidator = new UserValidator();
        System.out.println("--- 验证有效用户 ---");
        Map<String, Object> validUser = new HashMap<>();
        validUser.put("username", "john");
        validUser.put("email", "john@example.com");
        validUser.put("password", "123456");
        boolean result1 = userValidator.validate(validUser);
        System.out.println("验证结果: " + (result1 ? "通过" : "失败"));

        System.out.println("\n--- 验证无效用户 ---");
        Map<String, Object> invalidUser = new HashMap<>();
        invalidUser.put("username", "j");
        invalidUser.put("email", "invalid");
        boolean result2 = userValidator.validate(invalidUser);
        System.out.println("验证结果: " + (result2 ? "通过" : "失败"));
    }
}

// ============================================
// 1. Controller相关类
// ============================================

/**
 * Controller基类 - 模板方法
 */
abstract class BaseController {
    /**
     * 模板方法 - 请求处理流程
     */
    public final void handleRequest(Map<String, Object> params) {
        // 1. 参数验证
        if (!validateParams(params)) {
            System.out.println("[Controller] 参数验证失败");
            return;
        }

        // 2. 权限检查
        if (!checkPermission()) {
            System.out.println("[Controller] 权限检查失败");
            return;
        }

        // 3. 预处理
        Object preResult = preProcess(params);
        if (preResult != null) {
            System.out.println("[Controller] 预处理结果: " + preResult);
        }

        // 4. 执行具体业务
        Object result = doProcess(params);

        // 5. 后处理
        postProcess(result);

        // 6. 返回响应
        sendResponse(result);
    }

    // 参数验证 - 子类实现
    protected abstract boolean validateParams(Map<String, Object> params);

    // 权限检查 - 子类可重写
    protected boolean checkPermission() {
        return true;
    }

    // 预处理 - 子类可重写
    protected Object preProcess(Map<String, Object> params) {
        System.out.println("[Controller] 执行预处理");
        return null;
    }

    // 业务处理 - 子类实现
    protected abstract Object doProcess(Map<String, Object> params);

    // 后处理 - 子类可重写
    protected void postProcess(Object result) {
        System.out.println("[Controller] 执行后处理");
    }

    // 发送响应 - 子类可重写
    protected void sendResponse(Object result) {
        System.out.println("[Controller] 发送响应: " + result);
    }
}

/**
 * 用户Controller
 */
class UserController extends BaseController {

    @Override
    protected boolean validateParams(Map<String, Object> params) {
        if (params == null) {
            System.out.println("[UserController] 处理GET请求，无需参数验证");
            return true;
        }
        System.out.println("[UserController] 验证参数: " + params);
        return params.containsKey("username") && params.containsKey("email");
    }

    @Override
    protected Object doProcess(Map<String, Object> params) {
        if (params == null) {
            System.out.println("[UserController] 执行查询用户逻辑");
            return "用户列表";
        }
        System.out.println("[UserController] 执行创建用户逻辑");
        return "用户创建成功";
    }
}

// ============================================
// 2. Service相关类
// ============================================

/**
 * Service基类 - 模板方法
 */
abstract class BaseService {
    /**
     * 模板方法 - 业务处理流程
     */
    public final void process(Object request) {
        // 1. 参数校验
        if (!validate(request)) {
            throw new IllegalArgumentException("参数验证失败");
        }

        // 2. 权限校验
        if (!checkAuth(request)) {
            throw new IllegalArgumentException("权限不足");
        }

        // 3. 数据转换
        Object data = convert(request);

        // 4. 执行业务逻辑
        Object result = execute(data);

        // 5. 结果转换
        transform(result);
    }

    // 参数校验 - 子类实现
    protected abstract boolean validate(Object request);

    // 权限校验 - 子类可重写
    protected boolean checkAuth(Object request) {
        System.out.println("[Service] 检查权限");
        return true;
    }

    // 数据转换 - 子类可重写
    protected Object convert(Object request) {
        System.out.println("[Service] 转换数据");
        return request;
    }

    // 执行业务 - 子类实现
    protected abstract Object execute(Object data);

    // 结果转换 - 子类可重写
    protected void transform(Object result) {
        System.out.println("[Service] 转换结果: " + result);
    }
}

/**
 * 订单
 */
class Order {
    private String orderId;
    private double amount;

    public Order(String orderId, double amount) {
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getOrderId() { return orderId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}

/**
 * 订单Service
 */
class OrderService extends BaseService {

    @Override
    protected boolean validate(Object request) {
        Order order = (Order) request;
        System.out.println("[OrderService] 验证订单: " + order.getOrderId());
        return order.getOrderId() != null && order.getAmount() > 0;
    }

    @Override
    protected Object execute(Object data) {
        Order order = (Order) data;
        System.out.println("[OrderService] 执行订单业务: " + order.getOrderId());
        return "订单处理成功";
    }

    public void createOrder(Order order) {
        System.out.println("=== 创建订单流程 ===");
        process(order);
    }

    public void updateOrder(Order order) {
        System.out.println("=== 更新订单流程 ===");
        process(order);
    }

    public void deleteOrder(String orderId) {
        System.out.println("=== 删除订单流程 ===");
        Order order = new Order(orderId, 0);
        process(order);
    }
}

// ============================================
// 3. 验证器相关类
// ============================================

/**
 * 验证器基类 - 模板方法
 */
abstract class BaseValidator {
    /**
     * 模板方法 - 验证流程
     */
    public final boolean validate(Map<String, Object> data) {
        System.out.println("[Validator] 开始验证");

        // 1. 基础验证
        if (!validateBasic(data)) {
            return false;
        }

        // 2. 字段验证
        if (!validateFields(data)) {
            return false;
        }

        // 3. 业务验证
        if (!validateBusiness(data)) {
            return false;
        }

        System.out.println("[Validator] 验证通过");
        return true;
    }

    // 基础验证 - 子类可重写
    protected boolean validateBasic(Map<String, Object> data) {
        System.out.println("[Validator] 基础验证");
        return data != null && !data.isEmpty();
    }

    // 字段验证 - 子类实现
    protected abstract boolean validateFields(Map<String, Object> data);

    // 业务验证 - 子类可重写
    protected boolean validateBusiness(Map<String, Object> data) {
        System.out.println("[Validator] 业务验证");
        return true;
    }
}

/**
 * 用户验证器
 */
class UserValidator extends BaseValidator {

    @Override
    protected boolean validateFields(Map<String, Object> data) {
        // 验证用户名
        Object username = data.get("username");
        if (username == null || username.toString().length() < 3) {
            System.out.println("[Validator] 用户名长度不足");
            return false;
        }

        // 验证邮箱
        Object email = data.get("email");
        if (email == null || !email.toString().contains("@")) {
            System.out.println("[Validator] 邮箱格式错误");
            return false;
        }

        // 验证密码
        Object password = data.get("password");
        if (password == null || password.toString().length() < 6) {
            System.out.println("[Validator] 密码长度不足");
            return false;
        }

        return true;
    }

    @Override
    protected boolean validateBusiness(Map<String, Object> data) {
        // 可以添加更多业务验证
        System.out.println("[UserValidator] 用户业务验证");
        return true;
    }
}

/**
 * Spring MVC中的模板方法使用示例
 *
 * 1. AbstractController
 * public class MyController extends AbstractController {
 *     @Override
 *     protected ModelAndView handleRequestInternal(HttpServletRequest request,
 *                                                 HttpServletResponse response) {
 *         // 实现具体逻辑
 *     }
 * }
 *
 * 2. AbstractCommandController
 * public class UserController extends AbstractCommandController {
 *     public UserController() {
 *         setCommandClass(UserCommand.class);
 *         setCommandName("user");
 *     }
 *     @Override
 *     protected ModelAndView handle(HttpServletRequest request,
 *                                   HttpServletResponse response,
 *                                   Object command) {
 *         UserCommand user = (UserCommand) command;
 *         // 处理请求
 *     }
 * }
 *
 * 3. @ControllerAdvice
 * @ControllerAdvice
 * public class GlobalExceptionHandler {
 *     @ExceptionHandler(Exception.class)
 *     public ModelAndView handleException(Exception e) {
 *         // 统一异常处理
 *     }
 * }
 */
