package com.ssitao.code.designpattern.command.basic;

/**
 * 基础命令模式示例
 *
 * 命令模式核心要素：
 * 1. Command（命令接口）- 定义执行操作的接口
 * 2. ConcreteCommand（具体命令）- 绑定命令接收者，执行具体操作
 * 3. Receiver（接收者）- 知道如何执行操作
 * 4. Invoker（调用者）- 请求命令执行
 * 5. Client（客户端）- 创建命令对象并设置其接收者
 *
 * 适用场景：
 * - 需要将请求封装为对象
 * - 需要支持撤销操作
 * - 需要支持日志记录
 * - 需要队列化请求
 */
public class BasicCommandDemo {

    public static void main(String[] args) {
        // 创建接收者
        Light light = new Light();

        // 创建具体命令
        Command lightOn = new LightOnCommand(light);
        Command lightOff = new LightOffCommand(light);

        // 创建调用者（遥控器）
        RemoteControl remote = new RemoteControl();

        // 测试开灯
        System.out.println("=== 开灯 ===");
        remote.setCommand(lightOn);
        remote.pressButton();

        // 测试关灯
        System.out.println("\n=== 关灯 ===");
        remote.setCommand(lightOff);
        remote.pressButton();
    }
}

/**
 * 命令接口
 */
interface Command {
    void execute();
    void undo();
}

/**
 * 灯光 - 接收者
 */
class Light {
    private boolean isOn = false;

    public void on() {
        isOn = true;
        System.out.println("灯已打开");
    }

    public void off() {
        isOn = false;
        System.out.println("灯已关闭");
    }

    public boolean isOn() {
        return isOn;
    }
}

/**
 * 开灯命令
 */
class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}

/**
 * 关灯命令
 */
class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.off();
    }

    @Override
    public void undo() {
        light.on();
    }
}

/**
 * 遥控器 - 调用者
 */
class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        if (command != null) {
            command.execute();
        }
    }

    public void pressUndo() {
        if (command != null) {
            command.undo();
        }
    }
}
