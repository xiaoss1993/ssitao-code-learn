package com.ssitao.code.designpattern.composite;

/**
 * 文件系统组件接口
 * 组合模式：定义文件和文件夹的统一接口
 */
public interface FileSystemComponent {

    // 获取名称
    String getName();

    // 获取大小
    long getSize();

    // 打印信息
    void print(String prefix);

    // 深度遍历
    default void printTree() {
        print("");
    }
}
