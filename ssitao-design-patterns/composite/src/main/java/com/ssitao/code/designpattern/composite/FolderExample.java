package com.ssitao.code.designpattern.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹类 - 组合节点
 * 组合模式中的Composite(组合)角色
 */
public class FolderExample implements FileSystemComponent {

    private String name;
    private List<FileSystemComponent> children = new ArrayList<>();

    public FolderExample(String name) {
        this.name = name;
    }

    // 添加子组件
    public void add(FileSystemComponent component) {
        children.add(component);
    }

    // 移除子组件
    public void remove(FileSystemComponent component) {
        children.remove(component);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        long totalSize = 0;
        for (FileSystemComponent component : children) {
            totalSize += component.getSize();
        }
        return totalSize;
    }

    @Override
    public void print(String prefix) {
        System.out.println(prefix + "|- " + name + "/ (" + getSize() + " bytes)");
        for (FileSystemComponent component : children) {
            component.print(prefix + "   ");
        }
    }
}
