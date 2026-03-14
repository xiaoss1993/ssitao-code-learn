package com.ssitao.code.designpattern.mediator.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * 中介者模式基础示例
 *
 * 中介者模式特点：
 * 1. 用一个中介对象封装一系列对象交互
 * 2. 使各对象不需要显式相互引用
 * 3. 降低耦合度
 *
 * 组成：
 * - Mediator: 中介者接口
 * - ConcreteMediator: 具体中介者
 * - Colleague: 同事类接口
 * - ConcreteColleague: 具体同事类
 */
public class BasicMediatorDemo {

    public static void main(String[] args) {
        System.out.println("=== 中介者模式基础示例 ===\n");

        // 创建中介者
        ChatMediator mediator = new ChatMediatorImpl();

        // 创建用户
        User alice = new UserImpl("Alice", mediator);
        User bob = new UserImpl("Bob", mediator);
        User charlie = new UserImpl("Charlie", mediator);

        // 添加用户到聊天室
        mediator.addUser(alice);
        mediator.addUser(bob);
        mediator.addUser(charlie);

        // 用户发送消息
        System.out.println("--- 用户发送消息 ---");
        alice.sendMessage("大家好！");
        System.out.println();
        bob.sendMessage("你好Alice！");
        System.out.println();
        charlie.sendMessage("欢迎加入聊天室！");
    }
}

/**
 * 中介者接口
 */
interface ChatMediator {
    void sendMessage(String message, User sender);
    void addUser(User user);
}

/**
 * 同事接口
 */
interface User {
    void receiveMessage(String message);
    void sendMessage(String message);
    String getName();
}

/**
 * 具体中介者 - 聊天室
 */
class ChatMediatorImpl implements ChatMediator {
    private List<User> users = new ArrayList<>();

    @Override
    public void addUser(User user) {
        users.add(user);
        System.out.println(user.getName() + " 加入聊天室");
    }

    @Override
    public void sendMessage(String message, User sender) {
        for (User user : users) {
            // 不给自己发消息
            if (user != sender) {
                user.receiveMessage(sender.getName() + " 说: " + message);
            }
        }
    }
}

/**
 * 具体同事 - 用户
 */
class UserImpl implements User {
    private String name;
    private ChatMediator mediator;

    public UserImpl(String name, ChatMediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }

    @Override
    public void receiveMessage(String message) {
        System.out.println("[" + name + "] 收到: " + message);
    }

    @Override
    public void sendMessage(String message) {
        System.out.println("[" + name + "] 发送: " + message);
        mediator.sendMessage(message, this);
    }

    @Override
    public String getName() {
        return name;
    }
}
