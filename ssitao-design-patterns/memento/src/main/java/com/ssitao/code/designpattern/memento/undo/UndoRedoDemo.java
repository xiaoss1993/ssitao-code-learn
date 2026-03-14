package com.ssitao.code.designpattern.memento.undo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 撤销/重做示例
 *
 * 应用场景：
 * 1. 文本编辑器 - 撤销/重做
 * 2. 图形编辑器 - 撤销/重做操作
 * 3. 表单填写 - 撤销输入
 */
public class UndoRedoDemo {

    public static void main(String[] args) {
        System.out.println("=== 撤销/重做示例 ===\n");

        // 创建文本编辑器
        TextEditor editor = new TextEditor();

        // 输入文本
        System.out.println("--- 输入文本 ---");
        editor.type("Hello");
        System.out.println("当前内容: " + editor.getContent());

        editor.type(" World");
        System.out.println("当前内容: " + editor.getContent());

        editor.type("!");
        System.out.println("当前内容: " + editor.getContent());

        // 撤销
        System.out.println("\n--- 撤销 ---");
        editor.undo();
        System.out.println("撤销后: " + editor.getContent());

        editor.undo();
        System.out.println("撤销后: " + editor.getContent());

        // 重做
        System.out.println("\n--- 重做 ---");
        editor.redo();
        System.out.println("重做后: " + editor.getContent());

        editor.redo();
        System.out.println("重做后: " + editor.getContent());

        // 再撤销
        System.out.println("\n--- 再次撤销 ---");
        editor.undo();
        System.out.println("撤销后: " + editor.getContent());

        // 输入新内容（会清空重做栈）
        System.out.println("\n--- 输入新内容 ---");
        editor.type(" Java");
        System.out.println("当前内容: " + editor.getContent());

        // 无法重做
        System.out.println("\n--- 尝试重做 ---");
        editor.redo();
        System.out.println("重做后: " + editor.getContent());
    }
}

/**
 * 文本编辑器 - 原发器
 */
class TextEditor {
    private StringBuilder content = new StringBuilder();
    private Stack<Memento> undoStack = new Stack<>();
    private Stack<Memento> redoStack = new Stack<>();

    // 输入文本
    public void type(String text) {
        // 保存当前状态用于撤销
        saveState();

        content.append(text);

        // 新输入后清空重做栈
        redoStack.clear();
    }

    // 撤销
    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("没有可撤销的操作");
            return;
        }

        // 保存当前状态用于重做
        redoStack.push(createMemento());

        // 恢复之前的状态
        restoreMemento(undoStack.pop());
    }

    // 重做
    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("没有可重做的操作");
            return;
        }

        // 保存当前状态用于撤销
        undoStack.push(createMemento());

        // 恢复重做状态
        restoreMemento(redoStack.pop());
    }

    // 保存当前状态
    private void saveState() {
        undoStack.push(createMemento());
    }

    // 创建备忘录
    private Memento createMemento() {
        return new Memento(content.toString());
    }

    // 恢复备忘录
    private void restoreMemento(Memento memento) {
        content = new StringBuilder(memento.getContent());
    }

    public String getContent() {
        return content.toString();
    }
}

/**
 * 备忘录
 */
class Memento {
    private String content;
    private long timestamp;

    public Memento(String content) {
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
