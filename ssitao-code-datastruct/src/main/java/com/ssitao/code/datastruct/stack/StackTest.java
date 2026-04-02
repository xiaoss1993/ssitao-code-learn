package com.ssitao.code.datastruct.stack;

/**
 * 栈测试类 - 演示栈的基本操作和应用
 */
public class StackTest {

    public static void main(String[] args) {
        System.out.println("========== 栈学习 ==========\n");

        // 测试数组栈
        System.out.println("=== 数组栈 ===");
        ArrayStack<Integer> stack1 = new ArrayStack<>();
        testStack(stack1);

        // 测试链表栈
        System.out.println("\n=== 链表栈 ===");
        LinkedListStack<Integer> stack2 = new LinkedListStack<>();
        testStack(stack2);

        // 括号匹配示例
        System.out.println("\n========== 括号匹配应用 ==========\n");
        bracketMatching();
    }

    // 测试栈的基本操作
    private static void testStack(Stack<Integer> stack) {
        System.out.println("isEmpty: " + stack.isEmpty() + ", size: " + stack.size());

        // 入栈
        System.out.println("\n--- 入栈 push ---");
        stack.push(10);
        System.out.println("push(10): " + stack);

        stack.push(20);
        System.out.println("push(20): " + stack);

        stack.push(30);
        System.out.println("push(30): " + stack);

        // 查看栈顶
        System.out.println("\n--- 查看栈顶 peek ---");
        System.out.println("peek(): " + stack.peek());
        System.out.println("栈状态未变: " + stack);

        // 出栈
        System.out.println("\n--- 出栈 pop ---");
        System.out.println("pop(): " + stack.pop());
        System.out.println("pop()后: " + stack);

        System.out.println("pop(): " + stack.pop());
        System.out.println("pop()后: " + stack);

        System.out.println("pop(): " + stack.pop());
        System.out.println("pop()后: " + stack);

        System.out.println("\nisEmpty: " + stack.isEmpty() + ", size: " + stack.size());
    }

    // 括号匹配 - 栈的经典应用
    private static void bracketMatching() {
        String[] testCases = {
            "()",
            "()[]{}",
            "(]",
            "([)]",
            "{[]}",
            "((()))",
            "((",
            "){",
            ""
        };

        System.out.println("括号匹配测试:");
        System.out.println(repeat("-", 40));

        for (String s : testCases) {
            boolean result = isValid(s);
            System.out.printf("%-10s -> %s%n", "\"" + s + "\"", result ? "✓ 匹配" : "✗ 不匹配");
        }
    }

    /**
     * 判断括号字符串是否有效
     * 有效条件：
     * 1. 左括号必须用相同类型的右括号闭合
     * 2. 左括号必须以正确的顺序闭合
     */
    private static boolean isValid(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }

        ArrayStack<Character> stack = new ArrayStack<>();

        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                // 左括号入栈
                stack.push(c);
            } else {
                // 右括号，尝试匹配
                if (stack.isEmpty()) {
                    return false;  // 没有左括号可匹配
                }

                char top = stack.pop();
                if ((c == ')' && top != '(') ||
                    (c == ']' && top != '[') ||
                    (c == '}' && top != '{')) {
                    return false;  // 括号类型不匹配
                }
            }
        }

        // 所有字符处理完后，栈应该为空
        return stack.isEmpty();
    }

    // 字符串重复工具方法（兼容Java 8）
    private static String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}