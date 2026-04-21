package com.ssitao.code.designpattern.composite;

/**
 * 文件类 - 叶子节点
 * 组合模式中的Leaf(叶子)角色
 */
public class FileExample implements FileSystemComponent {

    private String name;
    private long size;

    public FileExample(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void print(String prefix) {
        System.out.println(prefix + "|- " + name + " (" + size + " bytes)");
    }
}
