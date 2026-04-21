package com.ssitao.code.designpattern.command.macro;

import java.util.ArrayList;
import java.util.List;

/**
 * 宏命令示例
 *
 * 宏命令：将多个命令组合成一个命令
 * 可以批量执行一系列操作
 */
public class MacroCommandDemo {

    public static void main(String[] args) {
        // 创建接收者
        Light light = new Light();
        Stereo stereo = new Stereo();
        AirConditioner ac = new AirConditioner();

        // 创建单个命令
        Command lightOn = new LightOnCommand(light);
        Command lightOff = new LightOffCommand(light);
        Command stereoOn = new StereoOnCommand(stereo);
        Command stereoOff = new StereoOffCommand(stereo);
        Command acOn = new AirConditionerOnCommand(ac);
        Command acOff = new AirConditionerOffCommand(ac);

        // 创建宏命令：回家模式
        List<Command> homeCommands = new ArrayList<>();
        homeCommands.add(lightOn);
        homeCommands.add(stereoOn);
        homeCommands.add(acOn);
        Command homeMode = new MacroCommand(homeCommands);

        // 创建宏命令：离家模式
        List<Command> awayCommands = new ArrayList<>();
        awayCommands.add(lightOff);
        awayCommands.add(stereoOff);
        awayCommands.add(acOff);
        Command awayMode = new MacroCommand(awayCommands);

        // 使用宏命令
        System.out.println("=== 执行回家模式 ===");
        homeMode.execute();

        System.out.println("\n=== 撤销回家模式 ===");
        homeMode.undo();

        System.out.println("\n=== 执行离家模式 ===");
        awayMode.execute();
    }
}

/**
 * 接收者：灯
 */
class Light {
    public void on() { System.out.println("灯打开了"); }
    public void off() { System.out.println("灯关闭了"); }
}

/**
 * 接收者：音响
 */
class Stereo {
    public void on() { System.out.println("音响打开了"); }
    public void off() { System.out.println("音响关闭了"); }
    public void setVolume(int volume) { System.out.println("音量设置为: " + volume); }
}

/**
 * 接收者：空调
 */
class AirConditioner {
    public void on() { System.out.println("空调打开了"); }
    public void off() { System.out.println("空调关闭了"); }
    public void setTemperature(int temp) { System.out.println("温度设置为: " + temp + "度"); }
}

/**
 * 命令接口
 */
interface Command {
    void execute();
    void undo();
}

/**
 * 基础命令抽象类
 */
abstract class AbstractCommand implements Command {
    protected Command[] commands;

    public AbstractCommand(Command... commands) {
        this.commands = commands;
    }

    @Override
    public void undo() {
        for (int i = commands.length - 1; i >= 0; i--) {
            commands[i].undo();
        }
    }
}

/**
 * 开灯命令
 */
class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) { this.light = light; }

    @Override
    public void execute() { light.on(); }

    @Override
    public void undo() { light.off(); }
}

/**
 * 关灯命令
 */
class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) { this.light = light; }

    @Override
    public void execute() { light.off(); }

    @Override
    public void undo() { light.on(); }
}

/**
 * 开音响命令
 */
class StereoOnCommand implements Command {
    private Stereo stereo;

    public StereoOnCommand(Stereo stereo) { this.stereo = stereo; }

    @Override
    public void execute() {
        stereo.on();
        stereo.setVolume(20);
    }

    @Override
    public void undo() { stereo.off(); }
}

/**
 * 关音响命令
 */
class StereoOffCommand implements Command {
    private Stereo stereo;

    public StereoOffCommand(Stereo stereo) { this.stereo = stereo; }

    @Override
    public void execute() { stereo.off(); }

    @Override
    public void undo() { stereo.on(); }
}

/**
 * 开空调命令
 */
class AirConditionerOnCommand implements Command {
    private AirConditioner ac;

    public AirConditionerOnCommand(AirConditioner ac) { this.ac = ac; }

    @Override
    public void execute() {
        ac.on();
        ac.setTemperature(24);
    }

    @Override
    public void undo() { ac.off(); }
}

/**
 * 关空调命令
 */
class AirConditionerOffCommand implements Command {
    private AirConditioner ac;

    public AirConditionerOffCommand(AirConditioner ac) { this.ac = ac; }

    @Override
    public void execute() { ac.off(); }

    @Override
    public void undo() { ac.on(); }
}

/**
 * 宏命令 - 组合多个命令
 */
class MacroCommand implements Command {
    private List<Command> commands;
    private List<Command> executedCommands = new ArrayList<>();

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
        // 逆序撤销
        for (int i = executedCommands.size() - 1; i >= 0; i--) {
            executedCommands.get(i).undo();
        }
        executedCommands.clear();
    }
}
