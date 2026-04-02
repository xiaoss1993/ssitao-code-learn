package com.ssitao.code.datastruct.queue;

/**
 * 队列接口
 *
 * 队列特点：先进先出（First In First Out, FIFO）
 *
 * 典型应用：
 * - 任务调度
 * - 广度优先搜索（BFS）
 * - 消息队列
 * - 打印队列
 */
public interface Queue<E> {

    // 获取队列中元素个数
    int size();

    // 判断队列是否为空
    boolean isEmpty();

    // 入队（从队尾添加）
    void enqueue(E e);

    // 出队（从队首移除）
    E dequeue();

    // 查看队首元素（不出队）
    E getFront();

    // 清空队列
    void clear();
}