package com.ssitao.code.designpattern.command.undo;

import java.util.ArrayList;
import java.util.List;

/**
 * 撤销/重做功能示例
 *
 * 演示如何实现命令的撤销(undo)和重做(redo)功能
 * 使用两个栈：undoStack用于撤销，redoStack用于重做
 */
public class UndoRedoDemo {

    public static void main(String[] args) {
        // 创建文本编辑器
        TextEditor editor = new TextEditor();

        // 创建命令管理器（调用者）
        CommandManager manager = new CommandManager();

        // 执行一系列命令
        System.out.println("=== 执行命令 ===");
        manager.executeCommand(new InsertTextCommand(editor, "Hello "));
        System.out.println("当前文本: " + editor.getContent());

        manager.executeCommand(new InsertTextCommand(editor, "World"));
        System.out.println("当前文本: " + editor.getContent());

        manager.executeCommand(new DeleteTextCommand(editor, 5));
        System.out.println("当前文本: " + editor.getContent());

        // 撤销操作
        System.out.println("\n=== 撤销操作 ===");
        manager.undo();
        System.out.println("当前文本: " + editor.getContent());

        manager.undo();
        System.out.println("当前文本: " + editor.getContent());

        // 重做操作
        System.out.println("\n=== 重做操作 ===");
        manager.redo();
        System.out.println("当前文本: " + editor.getContent());

        manager.redo();
        System.out.println("当前文本: " + editor.getContent());

        // 再撤销
        System.out.println("\n=== 再撤销 ===");
        manager.undo();
        System.out.println("当前文本: " + editor.getContent());

        // 执行新命令，会清空重做栈
        System.out.println("\n=== 执行新命令(清空重做栈) ===");
        manager.executeCommand(new InsertTextCommand(editor, "!"));
        System.out.println("当前文本: " + editor.getContent());

        // 尝试重做（应该无效）
        System.out.println("\n=== 尝试重做 ===");
        manager.redo();
        System.out.println("当前文本: " + editor.getContent());
    }
}

/**
 * 文本编辑器 - 接收者
 */
class TextEditor {
    private StringBuilder content = new StringBuilder();

    public void insertText(String text) {
        content.append(text);
    }

    public void deleteText(int count) {
        int length = content.length();
        if (count > length) {
            count = length;
        }
        content.delete(length - count, length);
    }

    public String getContent() {
        return content.toString();
    }

    public int getLength() {
        return content.length();
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
 * 插入文本命令
 */
class InsertTextCommand implements Command {
    private TextEditor editor;
    private String text;

    public InsertTextCommand(TextEditor editor, String text) {
        this.editor = editor;
        this.text = text;
    }

    @Override
    public void execute() {
        editor.insertText(text);
    }

    @Override
    public void undo() {
        editor.deleteText(text.length());
    }
}

/**
 * 删除文本命令
 */
class DeleteTextCommand implements Command {
    private TextEditor editor;
    private int count;
    private String deletedText;

    public DeleteTextCommand(TextEditor editor, int count) {
        this.editor = editor;
        this.count = count;
    }

    @Override
    public void execute() {
        // 保存被删除的文本，以便撤销
        int length = editor.getLength();
        int start = length - count;
        if (start < 0) {
            start = 0;
            count = length;
        }
        deletedText = editor.getContent().substring(start, length);
        editor.deleteText(count);
    }

    @Override
    public void undo() {
        if (deletedText != null) {
            editor.insertText(deletedText);
        }
    }
}

/**
 * 命令管理器 - 调用者
 * 负责执行命令、管理撤销/重做栈
 */
class CommandManager {
    private List<Command> undoStack = new ArrayList<>();
    private List<Command> redoStack = new ArrayList<>();

    /**
     * 执行命令
     */
    public void executeCommand(Command command) {
        command.execute();
        undoStack.add(command);
        // 执行新命令后，清空重做栈
        redoStack.clear();
    }

    /**
     * 撤销上一个命令
     */
    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("没有可撤销的命令");
            return;
        }

        Command command = undoStack.remove(undoStack.size() - 1);
        command.undo();
        redoStack.add(command);
        System.out.println("已撤销");
    }

    /**
     * 重做上一个撤销的命令
     */
    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("没有可重做的命令");
            return;
        }

        Command command = redoStack.remove(redoStack.size() - 1);
        command.execute();
        undoStack.add(command);
        System.out.println("已重做");
    }

    /**
     * 显示撤销栈大小
     */
    public int getUndoSize() {
        return undoStack.size();
    }

    /**
     * 显示重做栈大小
     */
    public int getRedoSize() {
        return redoStack.size();
    }
}
