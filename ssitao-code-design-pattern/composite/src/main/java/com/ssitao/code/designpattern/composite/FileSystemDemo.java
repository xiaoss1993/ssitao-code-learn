package com.ssitao.code.designpattern.composite;

/**
 * 文件系统组合模式演示
 */
public class FileSystemDemo {

    public static void main(String[] args) {
        System.out.println("========== 组合模式 - 文件系统示例 ==========\n");

        // 创建文件
        FileExample file1 = new FileExample("readme.txt", 1024);
        FileExample file2 = new FileExample("config.xml", 2048);
        FileExample file3 = new FileExample("app.jar", 1024000);
        FileExample file4 = new FileExample("log.txt", 5120);
        FileExample file5 = new FileExample("data.csv", 51200);

        // 创建文件夹
        FolderExample root = new FolderExample("root");
        FolderExample bin = new FolderExample("bin");
        FolderExample etc = new FolderExample("etc");
        FolderExample var = new FolderExample("var");
        FolderExample log = new FolderExample("log");
        FolderExample app = new FolderExample("app");

        // 构建目录结构
        bin.add(file1);  // readme.txt
        bin.add(file2);  // config.xml

        etc.add(file2);  // config.xml (同一个文件对象)

        log.add(file4);  // log.txt

        app.add(file3);  // app.jar
        app.add(file5);  // data.csv

        var.add(log);
        var.add(app);

        root.add(bin);
        root.add(etc);
        root.add(var);

        // 打印目录结构
        System.out.println("目录结构:");
        root.print("");

        System.out.println("\n--- 各目录大小 ---");
        System.out.println("root大小: " + root.getSize() + " bytes");
        System.out.println("bin大小: " + bin.getSize() + " bytes");
        System.out.println("var大小: " + var.getSize() + " bytes");
        System.out.println("app大小: " + app.getSize() + " bytes");

        // 统一接口操作
        System.out.println("\n--- 统一接口操作 ---");
        printComponent(root);
    }

    // 统一的组件打印方法，展示了组合模式的威力
    private static void printComponent(FileSystemComponent component) {
        System.out.println("组件: " + component.getName() +
                           ", 大小: " + component.getSize() + " bytes");
    }
}
