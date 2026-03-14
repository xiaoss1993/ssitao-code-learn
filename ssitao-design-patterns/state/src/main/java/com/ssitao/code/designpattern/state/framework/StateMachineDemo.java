package com.ssitao.code.designpattern.state.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 通用状态机框架示例
 *
 * 状态机框架特点：
 * 1. 配置化定义状态和转换
 * 2. 支持多种触发事件
 * 3. 支持状态监听器
 * 4. 支持条件判断
 * 5. 支持动作执行
 */
public class StateMachineDemo {

    public static void main(String[] args) {
        System.out.println("=== 状态机框架示例 ===\n");

        // 1. 订单状态机（配置化）
        System.out.println("1. 配置化订单状态机");
        configurableOrderDemo();

        // 2. 监听器示例
        System.out.println("\n2. 状态监听器示例");
        listenerDemo();

        // 3. 条件转换示例
        System.out.println("\n3. 条件转换示例");
        conditionDemo();
    }

    /**
     * 配置化订单状态机示例
     */
    private static void configurableOrderDemo() {
        // 创建状态机配置
        StateMachineConfig<String, String> config = new StateMachineConfig<>();

        // 定义状态
        config.addState("CREATED", StateType.INITIAL);
        config.addState("PAID");
        config.addState("SHIPPED");
        config.addState("CONFIRMED");
        config.addState("CANCELLED", StateType.FINAL);

        // 定义转换
        config.addTransition("PAY", "CREATED", "PAID");
        config.addTransition("SHIP", "PAID", "SHIPPED");
        config.addTransition("CONFIRM", "SHIPPED", "CONFIRMED");
        config.addTransition("CANCEL", "CREATED", "CANCELLED");
        config.addTransition("CANCEL", "PAID", "CANCELLED");

        // 创建状态机
        StateMachine<String, String> stateMachine = new StateMachine<>(config, "CREATED");

        System.out.println("初始状态: " + stateMachine.getCurrentState());

        // 执行转换
        System.out.println("\n--- 支付 ---");
        stateMachine.fire("PAY");
        System.out.println("当前状态: " + stateMachine.getCurrentState());

        System.out.println("\n--- 发货 ---");
        stateMachine.fire("SHIP");
        System.out.println("当前状态: " + stateMachine.getCurrentState());

        System.out.println("\n--- 确认收货 ---");
        stateMachine.fire("CONFIRM");
        System.out.println("当前状态: " + stateMachine.getCurrentState());
    }

    /**
     * 状态监听器示例
     */
    private static void listenerDemo() {
        // 创建带监听器的状态机
        StateMachineConfig<String, String> config = new StateMachineConfig<>();

        config.addState("PENDING");
        config.addState("PROCESSING");
        config.addState("COMPLETED");
        config.addState("FAILED");

        config.addTransition("START", "PENDING", "PROCESSING");
        config.addTransition("SUCCESS", "PROCESSING", "COMPLETED");
        config.addTransition("FAIL", "PROCESSING", "FAILED");
        config.addTransition("RETRY", "FAILED", "PENDING");

        // 创建状态机
        StateMachine<String, String> machine = new StateMachine<>(config, "PENDING");

        // 添加监听器
        machine.addListener(new StateListener<String, String>() {
            @Override
            public void onStateChanged(String fromState, String toState, String event) {
                System.out.println("[监听器] 状态变化: " + fromState + " -> " + toState);
            }

            @Override
            public void onTransitionBlocked(String fromState, String toState, String event) {
                System.out.println("[监听器] 转换被阻止: " + fromState + " -[" + event + "]-> " + toState);
            }
        });

        System.out.println("初始状态: " + machine.getCurrentState());

        System.out.println("\n--- 开始处理 ---");
        machine.fire("START");

        System.out.println("\n--- 处理成功 ---");
        machine.fire("SUCCESS");

        // 创建新的状态机测试失败场景
        System.out.println("\n--- 测试失败场景 ---");
        StateMachine<String, String> machine2 = new StateMachine<>(config, "PENDING");
        machine2.addListener(new StateListener<String, String>() {
            @Override
            public void onStateChanged(String fromState, String toState, String event) {
                System.out.println("[监听器] 状态变化: " + fromState + " -> " + toState);
            }

            @Override
            public void onTransitionBlocked(String fromState, String toState, String event) {
                System.out.println("[监听器] 转换被阻止: " + fromState + " -[" + event + "]-> " + toState);
            }
        });

        machine2.fire("START");
        machine2.fire("FAIL");
    }

    /**
     * 条件转换示例
     */
    private static void conditionDemo() {
        StateMachineConfig<String, String> config = new StateMachineConfig<>();

        config.addState("SUBMITTED");
        config.addState("APPROVED");
        config.addState("REJECTED");
        config.addState("PAID");

        // 带条件的转换
        config.addTransitionWithCondition("APPROVE", "SUBMITTED", "APPROVED",
            event -> {
                Object amount = event.get("amount");
                return amount != null && (Integer) amount >= 1000;
            });

        config.addTransition("REJECT", "SUBMITTED", "REJECTED");
        config.addTransition("PAY", "APPROVED", "PAID");

        StateMachine<String, String> machine = new StateMachine<>(config, "SUBMITTED");

        // 测试1: 小金额
        System.out.println("--- 测试小金额(500) ---");
        Map<String, Object> event1 = new HashMap<>();
        event1.put("amount", 500);
        boolean result1 = machine.fireWithEvent("APPROVE", event1);
        System.out.println("转换结果: " + result1 + ", 当前状态: " + machine.getCurrentState());

        // 测试2: 大金额
        System.out.println("\n--- 测试大金额(2000) ---");
        machine.setState("SUBMITTED"); // 重置
        Map<String, Object> event2 = new HashMap<>();
        event2.put("amount", 2000);
        boolean result2 = machine.fireWithEvent("APPROVE", event2);
        System.out.println("转换结果: " + result2 + ", 当前状态: " + machine.getCurrentState());
    }
}

// ============================================
// 状态机框架核心类
// ============================================

/**
 * 状态类型
 */
enum StateType {
    INITIAL,    // 初始状态
    NORMAL,    // 普通状态
    FINAL      // 终态
}

/**
 * 状态机配置
 */
class StateMachineConfig<S, E> {
    private Map<S, StateType> states = new HashMap<>();
    private Map<S, Map<E, Transition<S, E>>> transitions = new HashMap<>();

    public void addState(S state) {
        states.put(state, StateType.NORMAL);
    }

    public void addState(S state, StateType type) {
        states.put(state, type);
    }

    public void addTransition(E event, S fromState, S toState) {
        addTransitionWithCondition(event, fromState, toState, null);
    }

    public void addTransitionWithCondition(E event, S fromState, S toState,
                                             java.util.function.Predicate<Map<String, Object>> condition) {
        transitions.computeIfAbsent(fromState, k -> new HashMap<>());
        transitions.get(fromState).put(event, new Transition<>(fromState, toState, condition));
    }

    public Map<S, StateType> getStates() {
        return states;
    }

    public Map<S, Map<E, Transition<S, E>>> getTransitions() {
        return transitions;
    }

    public Transition<S, E> getTransition(S state, E event) {
        Map<E, Transition<S, E>> eventTransitions = transitions.get(state);
        if (eventTransitions == null) {
            return null;
        }
        return eventTransitions.get(event);
    }
}

/**
 * 转换定义
 */
class Transition<S, E> {
    private S fromState;
    private S toState;
    private java.util.function.Predicate<Map<String, Object>> condition;

    public Transition(S fromState, S toState,
                     java.util.function.Predicate<Map<String, Object>> condition) {
        this.fromState = fromState;
        this.toState = toState;
        this.condition = condition;
    }

    public S getFromState() { return fromState; }
    public S getToState() { return toState; }

    public boolean canTransit(Map<String, Object> eventData) {
        return condition == null || condition.test(eventData);
    }
}

/**
 * 状态监听器接口
 */
interface StateListener<S, E> {
    void onStateChanged(S fromState, S toState, E event);
    void onTransitionBlocked(S fromState, S toState, E event);
}

/**
 * 状态机
 */
class StateMachine<S, E> {
    private StateMachineConfig<S, E> config;
    private S currentState;
    private List<StateListener<S, E>> listeners = new ArrayList<>();
    private Map<String, Object> currentEventData;

    public StateMachine(StateMachineConfig<S, E> config, S initialState) {
        this.config = config;
        this.currentState = initialState;
    }

    public S getCurrentState() {
        return currentState;
    }

    public void setState(S state) {
        this.currentState = state;
    }

    public void addListener(StateListener<S, E> listener) {
        listeners.add(listener);
    }

    /**
     * 触发转换（无事件数据）
     */
    public boolean fire(E event) {
        return fireWithEvent(event, null);
    }

    /**
     * 触发转换（带事件数据）
     */
    public boolean fireWithEvent(E event, Map<String, Object> eventData) {
        this.currentEventData = eventData;

        Transition<S, E> transition = config.getTransition(currentState, event);

        if (transition == null) {
            System.out.println("无法从状态 " + currentState + " 在事件 " + event + " 下进行转换");
            notifyTransitionBlocked(currentState, event);
            return false;
        }

        // 检查条件
        if (!transition.canTransit(eventData)) {
            System.out.println("转换条件不满足: " + currentState + " -[" + event + "]-> " + transition.getToState());
            notifyTransitionBlocked(currentState, event);
            return false;
        }

        // 执行转换
        S fromState = currentState;
        S toState = transition.getToState();

        currentState = toState;

        System.out.println("状态转换: " + fromState + " -> " + toState + " (事件: " + event + ")");

        // 触发监听器
        notifyStateChanged(fromState, toState, event);

        return true;
    }

    private void notifyStateChanged(S fromState, S toState, E event) {
        for (StateListener<S, E> listener : listeners) {
            listener.onStateChanged(fromState, toState, event);
        }
    }

    private void notifyTransitionBlocked(S fromState, E event) {
        for (StateListener<S, E> listener : listeners) {
            // 尝试获取目标状态
            Transition<S, E> transition = config.getTransition(fromState, event);
            S toState = transition != null ? transition.getToState() : null;
            listener.onTransitionBlocked(fromState, toState, event);
        }
    }
}

// ============================================
// Spring StateMachine 风格的使用示例
// ============================================

/**
 * Spring StateMachine 风格示例
 *
 * 实际项目中可以使用 Spring StateMachine:
 *
 * @Configuration
 * public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderEvent> {
 *
 *     @Override
 *     public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
 *         states
 *             .withStates()
 *             .initial(OrderState.CREATED)
 *             .end(OrderState.CANCELLED)
 *             .end(OrderState.COMPLETED)
 *             .states(EnumSet.allOf(OrderState.class));
 *     }
 *
 *     @Override
 *     public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception {
 *         transitions
 *             .withExternal()
 *                 .source(OrderState.CREATED).target(OrderState.PAID)
 *                 .event(OrderEvent.PAY)
 *             .and()
 *             .withExternal()
 *                 .source(OrderState.PAID).target(OrderState.SHIPPED)
 *                 .event(OrderEvent.SHIP);
 *     }
 * }
 *
 * // 使用
 * @Autowired
 * private StateMachine<OrderState, OrderEvent> stateMachine;
 *
 * public void pay(String orderId) {
 *     stateMachine.start();
 *     stateMachine.sendEvent(OrderEvent.PAY);
 * }
 *
 * 订单状态枚举
 */
enum OrderState {
    CREATED,    // 已创建
    PAID,       // 已支付
    SHIPPED,    // 已发货
    CONFIRMED,  // 已确认
    CANCELLED   // 已取消
}

/**
 * 订单事件
 */
enum OrderEvent {
    PAY,        // 支付
    SHIP,       // 发货
    CONFIRM,    // 确认收货
    CANCEL      // 取消
}
