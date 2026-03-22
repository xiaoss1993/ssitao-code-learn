package com.ssitao.code.thread.code20.utils;

/**
 * 文件模拟类，用于生成随机内容的模拟文件数据
 * 模拟一个包含多行随机字符的文件
 */
public class FileMock {
    /**
     * 存储模拟文件的内容，每行是一个字符串
     */
    private String[] content;

    /**
     * 当前读取到的行索引
     */
    private int index;

    /**
     * 构造函数，随机生成文件的内容
     * 生成指定行数和每行字符数的随机ASCII字符内容
     *
     * @param size   文件的行数
     * @param length 每行文件的字符数
     */
    public FileMock(int size, int length) {
        this.content = new String[size];
        for (int i = 0; i < size; i++) {
            StringBuilder builder = new StringBuilder(length);
            for (int j = 0; j < length; j++) {
                // 生成0-254之间的随机ASCII字符
                int indice = (int) (Math.random() * 255);
                builder.append((char) indice);
            }
            content[i] = builder.toString();
        }
        this.index = 0;
    }

    /**
     * 判断是否还有文件的行数需要处理
     *
     * @return true表示还有行可以读取，false表示已读取完毕
     */
    public boolean hasMoreLines() {
        return this.index < this.content.length;
    }

    /**
     * 返回下一行的文件内容
     * 每次调用返回下一行内容，并递增索引
     *
     * @return 有返回文件内容，没有返回null
     */
    public String getLine() {
        if (this.hasMoreLines()) {
            System.out.println("Mock: " + (this.content.length - this.index));
            return this.content[this.index++];
        }
        return null;
    }
}
