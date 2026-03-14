package com.ssitao.code.designpattern.command.remote;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务场景：多功能遥控器
 *
 * 模拟智能家居遥控器，支持：
 * - 多个设备（灯、空调、电视、风扇）
 * - 多个命令（开、关、调亮度、调温度等）
 * - 批量操作（离家模式、回家模式）
 * - 撤销功能
 */
public class RemoteControlDemo {

    public static void main(String[] args) {
        // 创建各个设备
        Light livingRoomLight = new Light("客厅灯");
        Light bedroomLight = new Light("卧室灯");
        AirConditioner ac = new AirConditioner("空调");
        TV tv = new TV("电视");
        Fan fan = new Fan("风扇");

        // 创建命令
        Command lightOn = new LightOnCommand(livingRoomLight);
        Command lightOff = new LightOffCommand(livingRoomLight);
        Command bedroomLightOn = new LightOnCommand(bedroomLight);
        Command bedroomLightOff = new LightOffCommand(bedroomLight);
        Command acOn = new AirConditionerOnCommand(ac, 24);
        Command acOff = new AirConditionerOffCommand(ac);
        Command tvOn = new TVOnCommand(tv);
        Command tvOff = new TVOffCommand(tv);
        Command fanOn = new FanOnCommand(fan, 2);
        Command fanOff = new FanOffCommand(fan);

        // 创建遥控器
        RemoteControl remote = new RemoteControl();

        // 设置命令到按钮
        remote.setCommand(0, lightOn, lightOff);
        remote.setCommand(1, bedroomLightOn, bedroomLightOff);
        remote.setCommand(2, acOn, acOff);
        remote.setCommand(3, tvOn, tvOff);
        remote.setCommand(4, fanOn, fanOff);

        // 显示遥控器状态
        System.out.println(remote);

        // 测试各个按钮
        System.out.println("\n=== 测试按钮 ===");
        remote.onButtonWasPressed(0);
        remote.onButtonWasPressed(1);
        remote.onButtonWasPressed(2);
        remote.onButtonWasPressed(3);

        // 撤销
        System.out.println("\n=== 撤销 ===");
        remote.undoButtonWasPressed();

        // 创建宏命令：离家模式
        System.out.println("\n=== 执行离家模式 ===");
        Command leaveHomeCommand = new MacroCommand(Arrays.asList(lightOff, bedroomLightOff, acOff, tvOff, fanOff));
        remote.setCommand(6, leaveHomeCommand, null);
        remote.onButtonWasPressed(6);

        // 创建宏命令：回家模式
        System.out.println("\n=== 执行回家模式 ===");
        Command goHomeCommand = new MacroCommand(Arrays.asList(lightOn, acOn, tvOn));
        remote.setCommand(7, goHomeCommand, null);
        remote.onButtonWasPressed(7);
    }
}

/**
 * 设备基类
 */
class Device {
    protected String name;
    protected boolean on;

    public Device(String name) {
        this.name = name;
    }

    public void on() { on = true; }
    public void off() { on = false; }
    public boolean isOn() { return on; }
    public String getName() { return name; }
}

/**
 * 灯
 */
class Light extends Device {
    private int brightness = 0;

    public Light(String name) {
        super(name);
    }

    public void on() {
        on = true;
        brightness = 100;
        System.out.println(name + " 打开了，亮度: " + brightness + "%");
    }

    public void off() {
        on = false;
        brightness = 0;
        System.out.println(name + " 关闭了");
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
        System.out.println(name + " 亮度设置为: " + brightness + "%");
    }

    public int getBrightness() { return brightness; }
}

/**
 * 空调
 */
class AirConditioner extends Device {
    private int temperature = 26;

    public AirConditioner(String name) {
        super(name);
    }

    public void on() {
        on = true;
        System.out.println(name + " 打开了，温度: " + temperature + "度");
    }

    public void off() {
        on = false;
        System.out.println(name + " 关闭了");
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        if (on) {
            System.out.println(name + " 温度设置为: " + temperature + "度");
        }
    }

    public int getTemperature() { return temperature; }
}

/**
 * 电视
 */
class TV extends Device {
    private int volume = 20;
    private int channel = 1;

    public TV(String name) {
        super(name);
    }

    public void on() {
        on = true;
        System.out.println(name + " 打开了，频道: " + channel + "，音量: " + volume);
    }

    public void off() {
        on = false;
        System.out.println(name + " 关闭了");
    }

    public void setVolume(int volume) {
        this.volume = volume;
        if (on) {
            System.out.println(name + " 音量设置为: " + volume);
        }
    }

    public void setChannel(int channel) {
        this.channel = channel;
        if (on) {
            System.out.println(name + " 频道切换到: " + channel);
        }
    }
}

/**
 * 风扇
 */
class Fan extends Device {
    private int speed = 0;

    public Fan(String name) {
        super(name);
    }

    public void on() {
        on = true;
        speed = 1;
        System.out.println(name + " 打开了，风速: " + speed);
    }

    public void off() {
        on = false;
        speed = 0;
        System.out.println(name + " 关闭了");
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        if (on) {
            System.out.println(name + " 风速设置为: " + speed);
        }
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
 * 开空调命令
 */
class AirConditionerOnCommand implements Command {
    private AirConditioner ac;
    private int temperature;

    public AirConditionerOnCommand(AirConditioner ac, int temperature) {
        this.ac = ac;
        this.temperature = temperature;
    }

    @Override
    public void execute() {
        ac.on();
        ac.setTemperature(temperature);
    }

    @Override
    public void undo() {
        ac.off();
    }
}

/**
 * 关空调命令
 */
class AirConditionerOffCommand implements Command {
    private AirConditioner ac;

    public AirConditionerOffCommand(AirConditioner ac) {
        this.ac = ac;
    }

    @Override
    public void execute() {
        ac.off();
    }

    @Override
    public void undo() {
        ac.on();
    }
}

/**
 * 开电视命令
 */
class TVOnCommand implements Command {
    private TV tv;

    public TVOnCommand(TV tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.on();
    }

    @Override
    public void undo() {
        tv.off();
    }
}

/**
 * 关电视命令
 */
class TVOffCommand implements Command {
    private TV tv;

    public TVOffCommand(TV tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.off();
    }

    @Override
    public void undo() {
        tv.on();
    }
}

/**
 * 开风扇命令
 */
class FanOnCommand implements Command {
    private Fan fan;
    private int speed;

    public FanOnCommand(Fan fan, int speed) {
        this.fan = fan;
        this.speed = speed;
    }

    @Override
    public void execute() {
        fan.on();
        fan.setSpeed(speed);
    }

    @Override
    public void undo() {
        fan.off();
    }
}

/**
 * 关风扇命令
 */
class FanOffCommand implements Command {
    private Fan fan;

    public FanOffCommand(Fan fan) {
        this.fan = fan;
    }

    @Override
    public void execute() {
        fan.off();
    }

    @Override
    public void undo() {
        fan.on();
    }
}

/**
 * 宏命令
 */
class MacroCommand implements Command {
    private List<Command> commands;
    private List<Command> executedCommands = new java.util.ArrayList<>();

    public MacroCommand(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute() {
        for (Command command : commands) {
            command.execute();
            executedCommands.add(command);
        }
    }

    @Override
    public void undo() {
        for (int i = executedCommands.size() - 1; i >= 0; i--) {
            executedCommands.get(i).undo();
        }
        executedCommands.clear();
    }
}

/**
 * 遥控器 - 调用者
 * 支持7个设备按钮 + 多个宏命令按钮
 */
class RemoteControl {
    private Command[] onCommands;
    private Command[] offCommands;
    private Command undoCommand;

    public RemoteControl() {
        onCommands = new Command[8];
        offCommands = new Command[8];

        // 初始化为空命令
        Command noCommand = new NoCommand();
        for (int i = 0; i < 8; i++) {
            onCommands[i] = noCommand;
            offCommands[i] = noCommand;
        }
        undoCommand = noCommand;
    }

    public void setCommand(int slot, Command onCommand, Command offCommand) {
        onCommands[slot] = onCommand;
        offCommands[slot] = offCommand;
    }

    public void onButtonWasPressed(int slot) {
        onCommands[slot].execute();
        undoCommand = onCommands[slot];
    }

    public void offButtonWasPressed(int slot) {
        offCommands[slot].execute();
        undoCommand = offCommands[slot];
    }

    public void undoButtonWasPressed() {
        undoCommand.undo();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== 遥控器状态 =====\n");
        for (int i = 0; i < 8; i++) {
            sb.append("按钮[" + i + "] 开: " + onCommands[i].getClass().getSimpleName());
            sb.append(" 关: " + offCommands[i].getClass().getSimpleName() + "\n");
        }
        return sb.toString();
    }
}

/**
 * 空命令 - 用于初始化
 */
class NoCommand implements Command {
    @Override
    public void execute() {
        // 什么都不做
    }

    @Override
    public void undo() {
        // 什么都不做
    }
}
