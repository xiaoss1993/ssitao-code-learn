package com.ssitao.code.thread.code58.task;

/**
 * 事件类 - 优先级队列元素
 *
 * 实现 Comparable 接口，支持优先级排序
 *
 * 优先级规则：
 * - priority 值越小，优先级越高
 * - 优先级高的元素先被消费
 *
 * compareTo 逻辑：
 * - 当前 priority < 其他 priority，返回 -1（当前优先级更高）
 * - 当前 priority > 其他 priority，返回 1（当前优先级更低）
 * - priority 相等，返回 0
 *
 * 与 PriorityBlockingQueue 的配合：
 * - 队列使用 compareTo 决定元素出队顺序
 * - 总是返回优先级最高（priority 最小）的元素
 */
public class Event implements Comparable<Event> {

    /**
     * 创建事件的线程名称
     */
    private String thread;

    /**
     * 事件优先级（值越小优先级越高）
     */
    private int priority;

    /**
     * 构造函数
     *
     * @param thread   创建事件的线程名称
     * @param priority 事件优先级（值越小优先级越高）
     */
    public Event(String thread, int priority) {
        this.thread = thread;
        this.priority = priority;
    }

    /**
     * 获取线程名称
     *
     * @return 线程名称
     */
    public String getThread() {
        return thread;
    }

    /**
     * 获取优先级
     *
     * @return 优先级数值
     */
    public int getPriority() {
        return priority;
    }

    /**
     * 比较两个事件的优先级
     *
     * 优先级规则：priority 值越小，优先级越高
     *
     * @param e 要比较的事件
     * @return 负数表示当前优先级更高，0表示相等，正数表示更低
     */
    @Override
    public int compareTo(Event e) {
        // 当前优先级更高（priority 更小）
        if (this.priority > e.getPriority()) {
            return -1;
        }
        // 当前优先级更低（priority 更大）
        else if (this.priority < e.getPriority()) {
            return 1;
        }
        // 优先级相同
        else {
            return 0;
        }
    }
}
