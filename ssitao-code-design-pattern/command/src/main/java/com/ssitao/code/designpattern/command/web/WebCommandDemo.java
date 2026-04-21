package com.ssitao.code.designpattern.command.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Web项目中的命令模式使用示例
 *
 * 典型应用场景：
 * 1. HttpServlet - 请求封装为命令对象
 * 2. Spring MVC @Controller - 参数封装为命令对象
 * 3. CQRS命令处理 - 命令总线
 * 4. 分布式命令 - 命令序列化传输
 * 5. 异步任务 - 命令队列
 */
public class WebCommandDemo {

    public static void main(String[] args) {
        System.out.println("=== Web命令模式示例 ===\n");

        // 1. 模拟Spring MVC命令对象
        System.out.println("1. Spring MVC命令对象示例");
        mvcCommandDemo();

        // 2. CQRS命令总线示例
        System.out.println("\n2. CQRS命令总线示例");
        cqrsCommandDemo();

        // 3. 分布式命令示例
        System.out.println("\n3. 分布式命令示例");
        distributedCommandDemo();

        // 4. 异步任务命令示例
        System.out.println("\n4. 异步任务命令示例");
        asyncCommandDemo();
    }

    /**
     * 1. Spring MVC命令对象示例
     * 类似于Spring MVC中表单绑定对象
     */
    private static void mvcCommandDemo() {
        // 模拟用户提交的表单数据
        UserForm userForm = new UserForm();
        userForm.setUsername("john");
        userForm.setEmail("john@example.com");
        userForm.setPassword("123456");

        // 模拟Controller处理
        UserController controller = new UserController();
        String view = controller.register(userForm);
        System.out.println("返回视图: " + view);
    }

    /**
     * 2. CQRS命令总线示例
     * 命令查询职责分离
     */
    private static void cqrsCommandDemo() {
        // 创建命令总线
        CommandBus commandBus = new CommandBus();

        // 注册命令处理器
        commandBus.register(CreateOrderCommand.class, new CreateOrderHandler());
        commandBus.register(UpdateOrderCommand.class, new UpdateOrderHandler());
        commandBus.register(CancelOrderCommand.class, new CancelOrderHandler());

        // 执行命令
        System.out.println("--- 创建订单命令 ---");
        CreateOrderCommand createCmd = new CreateOrderCommand("USER001", 1000.0);
        commandBus.send(createCmd);

        System.out.println("--- 更新订单命令 ---");
        UpdateOrderCommand updateCmd = new UpdateOrderCommand("ORDER001", 1500.0);
        commandBus.send(updateCmd);

        System.out.println("--- 取消订单命令 ---");
        CancelOrderCommand cancelCmd = new CancelOrderCommand("ORDER001");
        commandBus.send(cancelCmd);
    }

    /**
     * 3. 分布式命令示例
     * 命令序列化后通过网络传输
     */
    private static void distributedCommandDemo() {
        // 创建分布式命令
        DistributedCommand cmd = new DistributedCommand();
        cmd.setCommandId("CMD-" + System.currentTimeMillis());
        cmd.setCommandType("CREATE_USER");
        cmd.setPayload("{\"username\":\"alice\",\"email\":\"alice@example.com\"}");

        // 序列化（模拟）
        String serialized = cmd.serialize();
        System.out.println("序列化命令: " + serialized);

        // 网络传输（模拟）

        // 反序列化
        DistributedCommand deserialized = DistributedCommand.deserialize(serialized);
        System.out.println("反序列化: " + deserialized.getCommandType());

        // 执行
        CommandExecutor executor = new CommandExecutor();
        executor.execute(deserialized);
    }

    /**
     * 4. 异步任务命令示例
     * 类似于消息队列或异步任务
     */
    private static void asyncCommandDemo() {
        // 创建任务队列
        TaskQueue taskQueue = new TaskQueue();

        // 添加任务命令
        taskQueue.addTask(new SendEmailCommand("user@example.com", "欢迎注册"));
        taskQueue.addTask(new SendSmsCommand("13800138000", "验证码:123456"));
        taskQueue.addTask(new GenerateReportCommand("销售报表", "2024-01"));

        // 处理任务
        System.out.println("--- 处理队列任务 ---");
        taskQueue.processTasks();
    }
}

// ============================================
// 1. Spring MVC命令对象示例相关类
// ============================================

/**
 * 命令对象 - 用户注册表单
 */
class UserForm {
    private String username;
    private String email;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

/**
 * Controller - 调用者
 */
class UserController {
    private UserService userService = new UserService();

    public String register(UserForm form) {
        // 验证
        if (form.getUsername() == null || form.getUsername().isEmpty()) {
            return "error:username_required";
        }
        if (form.getEmail() == null || !form.getEmail().contains("@")) {
            return "error:email_invalid";
        }

        // 调用服务
        userService.createUser(form.getUsername(), form.getEmail(), form.getPassword());
        return "success:user_registered";
    }
}

/**
 * Service - 接收者
 */
class UserService {
    public void createUser(String username, String email, String password) {
        System.out.println("创建用户: " + username + ", Email: " + email);
    }
}

// ============================================
// 2. CQRS命令相关类
// ============================================

/**
 * 命令接口
 */
interface Command {
    String getId();
}

/**
 * 命令处理器接口
 */
interface CommandHandler<T extends Command> {
    void handle(T command);
}

/**
 * 命令总线
 */
class CommandBus {
    private Map<Class<? extends Command>, CommandHandler> handlers = new HashMap<>();

    public void register(Class<? extends Command> commandClass, CommandHandler handler) {
        handlers.put(commandClass, handler);
    }

    public void send(Command command) {
        CommandHandler handler = handlers.get(command.getClass());
        if (handler != null) {
            handler.handle(command);
        } else {
            System.out.println("未找到处理器: " + command.getClass().getSimpleName());
        }
    }
}

/**
 * 创建订单命令
 */
class CreateOrderCommand implements Command {
    private String id = "ORDER-" + System.currentTimeMillis();
    private String userId;
    private double amount;

    public CreateOrderCommand(String userId, double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
}

/**
 * 更新订单命令
 */
class UpdateOrderCommand implements Command {
    private String orderId;
    private double newAmount;

    public UpdateOrderCommand(String orderId, double newAmount) {
        this.orderId = orderId;
        this.newAmount = newAmount;
    }

    public String getId() { return orderId; }
    public String getOrderId() { return orderId; }
    public double getNewAmount() { return newAmount; }
}

/**
 * 取消订单命令
 */
class CancelOrderCommand implements Command {
    private String orderId;

    public CancelOrderCommand(String orderId) {
        this.orderId = orderId;
    }

    public String getId() { return orderId; }
    public String getOrderId() { return orderId; }
}

/**
 * 创建订单处理器
 */
class CreateOrderHandler implements CommandHandler<CreateOrderCommand> {
    @Override
    public void handle(CreateOrderCommand command) {
        System.out.println("创建订单: ID=" + command.getId() +
            ", 用户=" + command.getUserId() +
            ", 金额=" + command.getAmount());
    }
}

/**
 * 更新订单处理器
 */
class UpdateOrderHandler implements CommandHandler<UpdateOrderCommand> {
    @Override
    public void handle(UpdateOrderCommand command) {
        System.out.println("更新订单: ID=" + command.getOrderId() +
            ", 新金额=" + command.getNewAmount());
    }
}

/**
 * 取消订单处理器
 */
class CancelOrderHandler implements CommandHandler<CancelOrderCommand> {
    @Override
    public void handle(CancelOrderCommand command) {
        System.out.println("取消订单: ID=" + command.getOrderId());
    }
}

// ============================================
// 3. 分布式命令相关类
// ============================================

/**
 * 分布式命令 - 可序列化
 */
class DistributedCommand {
    private String commandId;
    private String commandType;
    private String payload;

    public String getCommandId() { return commandId; }
    public void setCommandId(String commandId) { this.commandId = commandId; }
    public String getCommandType() { return commandType; }
    public void setCommandType(String commandType) { this.commandType = commandType; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

    // 序列化
    public String serialize() {
        return commandId + "|" + commandType + "|" + payload;
    }

    // 反序列化
    public static DistributedCommand deserialize(String data) {
        String[] parts = data.split("\\|", 3);
        DistributedCommand cmd = new DistributedCommand();
        cmd.commandId = parts[0];
        cmd.commandType = parts[1];
        cmd.payload = parts[2];
        return cmd;
    }
}

/**
 * 命令执行器
 */
class CommandExecutor {
    public void execute(DistributedCommand command) {
        System.out.println("执行命令: " + command.getCommandType());
        System.out.println("载荷: " + command.getPayload());
    }
}

// ============================================
// 4. 异步任务命令相关类
// ============================================

/**
 * 任务命令接口
 */
interface TaskCommand {
    void execute();
}

/**
 * 发送邮件命令
 */
class SendEmailCommand implements TaskCommand {
    private String to;
    private String content;

    public SendEmailCommand(String to, String content) {
        this.to = to;
        this.content = content;
    }

    @Override
    public void execute() {
        System.out.println("发送邮件到: " + to + ", 内容: " + content);
    }
}

/**
 * 发送短信命令
 */
class SendSmsCommand implements TaskCommand {
    private String phone;
    private String message;

    public SendSmsCommand(String phone, String message) {
        this.phone = phone;
        this.message = message;
    }

    @Override
    public void execute() {
        System.out.println("发送短信到: " + phone + ", 内容: " + message);
    }
}

/**
 * 生成报表命令
 */
class GenerateReportCommand implements TaskCommand {
    private String reportName;
    private String period;

    public GenerateReportCommand(String reportName, String period) {
        this.reportName = reportName;
        this.period = period;
    }

    @Override
    public void execute() {
        System.out.println("生成报表: " + reportName + ", 周期: " + period);
    }
}

/**
 * 任务队列
 */
class TaskQueue {
    private List<TaskCommand> tasks = new ArrayList<>();

    public void addTask(TaskCommand task) {
        tasks.add(task);
    }

    public void processTasks() {
        for (TaskCommand task : tasks) {
            task.execute();
        }
        tasks.clear();
    }
}
