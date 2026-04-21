package com.ssitao.code.designpattern.chain.basic;

/**
 * 基础责任链模式示例
 *
 * 特点：
 * 1. 抽象处理者定义处理接口和下一个处理者
 * 2. 具体处理者实现处理逻辑
 * 3. 请求沿着链传递，直到被处理
 *
 * 适用场景：
 * - 多个对象可以处理同一请求，具体哪个对象处理由运行时决定
 * - 可动态指定处理者链
 * - 需要解耦请求发送者和接收者
 */
public class BasicChainDemo {

    public static void main(String[] args) {
        // 构建责任链
        Handler handlerA = new ConcreteHandlerA();
        Handler handlerB = new ConcreteHandlerB();
        Handler handlerC = new ConcreteHandlerC();

        handlerA.setNext(handlerB);
        handlerB.setNext(handlerC);

        // 发送请求
        System.out.println("=== 测试请求类型 A ===");
        handlerA.handleRequest("A");

        System.out.println("\n=== 测试请求类型 B ===");
        handlerA.handleRequest("B");

        System.out.println("\n=== 测试请求类型 C ===");
        handlerA.handleRequest("C");

        System.out.println("\n=== 测试未知类型 ===");
        handlerA.handleRequest("D");
    }
}

/**
 * 抽象处理者
 * 定义处理请求的接口和设置下一个处理者的方法
 */
abstract class Handler {
    private Handler next;

    public Handler(Handler next) {
        this.next = next;
    }

    public Handler setNext(Handler next) {
        this.next = next;
        return next; // 链式调用
    }

    /**
     * 处理请求模板方法
     */
    public final void handleRequest(String request) {
        if (canHandle(request)) {
            handle(request);
        } else if (next != null) {
            System.out.println("  -> 传递给下一个处理器");
            next.handleRequest(request);
        } else {
            System.out.println("  -> 没有处理器能处理此请求");
        }
    }

    /**
     * 判断当前处理器是否能处理该请求
     */
    protected abstract boolean canHandle(String request);

    /**
     * 具体处理逻辑
     */
    protected abstract void handle(String request);
}

/**
 * 具体处理器A - 处理类型A的请求
 */
class ConcreteHandlerA extends Handler {

    public ConcreteHandlerA() {
        super(null);
    }

    public ConcreteHandlerA(Handler next) {
        super(next);
    }

    @Override
    protected boolean canHandle(String request) {
        return "A".equals(request);
    }

    @Override
    protected void handle(String request) {
        System.out.println("ConcreteHandlerA 处理了请求: " + request);
    }
}

/**
 * 具体处理器B - 处理类型B的请求
 */
class ConcreteHandlerB extends Handler {

    public ConcreteHandlerB() {
        super(null);
    }

    public ConcreteHandlerB(Handler next) {
        super(next);
    }

    @Override
    protected boolean canHandle(String request) {
        return "B".equals(request);
    }

    @Override
    protected void handle(String request) {
        System.out.println("ConcreteHandlerB 处理了请求: " + request);
    }
}

/**
 * 具体处理器C - 处理类型C的请求
 */
class ConcreteHandlerC extends Handler {

    public ConcreteHandlerC() {
        super(null);
    }

    public ConcreteHandlerC(Handler next) {
        super(next);
    }

    @Override
    protected boolean canHandle(String request) {
        return "C".equals(request);
    }

    @Override
    protected void handle(String request) {
        System.out.println("ConcreteHandlerC 处理了请求: " + request);
    }
}
