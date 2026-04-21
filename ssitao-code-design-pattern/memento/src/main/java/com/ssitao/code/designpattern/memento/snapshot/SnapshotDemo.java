package com.ssitao.code.designpattern.memento.snapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * 快照/版本管理示例
 *
 * 应用场景：
 * 1. 文档版本管理
 * 2. 配置管理 - 配置快照
 * 3. 虚拟机快照
 * 4. 游戏存档管理
 */
public class SnapshotDemo {

    public static void main(String[] args) {
        System.out.println("=== 快照/版本管理示例 ===\n");

        // 1. 文档版本管理
        System.out.println("1. 文档版本管理");
        documentVersionDemo();

        // 2. 配置快照管理
        System.out.println("\n2. 配置快照管理");
        configSnapshotDemo();
    }

    /**
     * 文档版本管理示例
     */
    private static void documentVersionDemo() {
        // 创建文档
        Document doc = new Document();
        VersionManager manager = new VersionManager();

        // 创建初始版本
        System.out.println("--- 创建文档初始版本 ---");
        doc.setContent("这是一份报告");
        doc.setAuthor("张三");
        manager.saveVersion(doc);
        System.out.println("版本1: " + doc.getContent());

        // 修改文档
        System.out.println("\n--- 修改文档 ---");
        doc.setContent("这是一份修改后的报告");
        manager.saveVersion(doc);
        System.out.println("版本2: " + doc.getContent());

        // 再次修改
        System.out.println("\n--- 再次修改 ---");
        doc.setContent("这是最终版本的报告");
        manager.saveVersion(doc);
        System.out.println("版本3: " + doc.getContent());

        // 查看历史版本
        System.out.println("\n--- 查看历史版本 ---");
        manager.showHistory();

        // 恢复到版本2
        System.out.println("\n--- 恢复到版本2 ---");
        manager.restoreVersion(doc, 2);
        System.out.println("当前内容: " + doc.getContent());

        // 恢复到版本1
        System.out.println("\n--- 恢复到版本1 ---");
        manager.restoreVersion(doc, 1);
        System.out.println("当前内容: " + doc.getContent());
    }

    /**
     * 配置快照管理示例
     */
    private static void configSnapshotDemo() {
        // 创建配置中心
        ConfigManager configManager = new ConfigManager();
        ConfigCenter config = new ConfigCenter();

        // 初始配置
        System.out.println("--- 设置初始配置 ---");
        config.set("app.name", "MyApp");
        config.set("app.version", "1.0.0");
        config.set("app.mode", "dev");
        configManager.saveSnapshot(config);
        printConfig(config);

        // 修改配置
        System.out.println("\n--- 修改为生产环境 ---");
        config.set("app.mode", "prod");
        config.set("app.version", "1.0.1");
        configManager.saveSnapshot(config);
        printConfig(config);

        // 再修改
        System.out.println("\n--- 再次修改 ---");
        config.set("app.debug", "true");
        configManager.saveSnapshot(config);
        printConfig(config);

        // 回滚到初始配置
        System.out.println("\n--- 回滚到初始配置 ---");
        configManager.restoreSnapshot(config, 1);
        printConfig(config);
    }

    private static void printConfig(ConfigCenter config) {
        System.out.println("当前配置: " + config.getAll());
    }
}

/**
 * 文档 - 原发器
 */
class Document {
    private String content;
    private String author;

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    // 创建备忘录
    public DocumentMemento createMemento() {
        return new DocumentMemento(content, author);
    }

    // 恢复备忘录
    public void restoreMemento(DocumentMemento memento) {
        this.content = memento.getContent();
        this.author = memento.getAuthor();
    }
}

/**
 * 文档备忘录
 */
class DocumentMemento {
    private String content;
    private String author;
    private long timestamp;

    public DocumentMemento(String content, String author) {
        this.content = content;
        this.author = author;
        this.timestamp = System.currentTimeMillis();
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

/**
 * 版本管理器
 */
class VersionManager {
    private List<DocumentMemento> versions = new ArrayList<>();

    // 保存版本
    public void saveVersion(Document doc) {
        versions.add(doc.createMemento());
    }

    // 恢复版本
    public void restoreVersion(Document doc, int versionNumber) {
        if (versionNumber > 0 && versionNumber <= versions.size()) {
            doc.restoreMemento(versions.get(versionNumber - 1));
        } else {
            System.out.println("无效的版本号");
        }
    }

    // 显示历史
    public void showHistory() {
        for (int i = 0; i < versions.size(); i++) {
            DocumentMemento m = versions.get(i);
            System.out.println("版本" + (i + 1) + ": " + m.getContent() +
                " (作者: " + m.getAuthor() + ")");
        }
    }
}

/**
 * 配置中心 - 原发器
 */
class ConfigCenter {
    private Map<String, String> configs = new HashMap<>();

    public void set(String key, String value) {
        configs.put(key, value);
    }

    public String get(String key) {
        return configs.get(key);
    }

    public Map<String, String> getAll() {
        return new HashMap<>(configs);
    }

    // 创建快照
    public ConfigSnapshot createSnapshot() {
        return new ConfigSnapshot(new HashMap<>(configs));
    }

    // 恢复快照
    public void restoreSnapshot(ConfigSnapshot snapshot) {
        this.configs = new HashMap<>(snapshot.getConfigs());
    }
}

/**
 * 配置快照
 */
class ConfigSnapshot {
    private Map<String, String> configs;
    private long timestamp;

    public ConfigSnapshot(Map<String, String> configs) {
        this.configs = new HashMap<>(configs);
        this.timestamp = System.currentTimeMillis();
    }

    public Map<String, String> getConfigs() {
        return configs;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

/**
 * 配置管理器
 */
class ConfigManager {
    private List<ConfigSnapshot> snapshots = new ArrayList<>();

    // 保存快照
    public void saveSnapshot(ConfigCenter config) {
        snapshots.add(config.createSnapshot());
    }

    // 恢复快照
    public void restoreSnapshot(ConfigCenter config, int snapshotNumber) {
        if (snapshotNumber > 0 && snapshotNumber <= snapshots.size()) {
            config.restoreSnapshot(snapshots.get(snapshotNumber - 1));
        } else {
            System.out.println("无效的快照编号");
        }
    }
}
