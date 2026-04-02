package com.ssitao.code.datastruct.stack;

/**
 * 栈接口
 *
 * 栈特点：先进后出（Last In First Out, LIFO）
 *
 * 典型应用：
 * - 括号匹配
 * - 表达式求值
 * - 深度优先搜索
 * - 函数调用栈
 */
public interface Stack<E> {

    // 获取栈中元素个数
    int size();

    // 判断栈是否为空
    boolean isEmpty();

    // 入栈（压入元素）
    void push(E e);

    // 出栈（弹出栈顶元素）
    E pop();

    // 查看栈顶元素（不出栈）
    E peek();

    // 清空栈
    void clear();
}