package com.ssitao.code.designpattern.memento.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * 备忘录模式基础示例
 *
 * 备忘录模式特点：
 * 1. 保存对象当前状态
 * 2. 可恢复到之前的状态
 * 3. 不破坏对象封装性
 *
 * 组成：
 * - Originator: 原发器 - 需要保存状态的对象
 * - Memento: 备忘录 - 存储原发器状态
 * - Caretaker: 管理者 - 负责保存备忘录
 */
public class BasicMementoDemo {

    public static void main(String[] args) {
        System.out.println("=== 备忘录模式基础示例 ===\n");

        // 创建游戏角色
        GameRole hero = new GameRole("亚瑟", 100, 50);

        // 创建存档管理器
        Caretaker caretaker = new Caretaker();

        // 初始状态
        System.out.println("初始状态: " + hero);

        // 存档1
        System.out.println("\n--- 打怪前存档 ---");
        caretaker.save(hero.createMemento());
        hero.fightMonster();

        // 存档2
        System.out.println("\n--- 打怪后存档 ---");
        caretaker.save(hero.createMemento());
        System.out.println("当前状态: " + hero);

        // 继续战斗
        System.out.println("\n--- 继续战斗 ---");
        hero.fightMonster();
        System.out.println("当前状态: " + hero);

        // 读取存档
        System.out.println("\n--- 读取最新存档 ---");
        hero.restoreMemento(caretaker.getLastSave());
        System.out.println("恢复后状态: " + hero);

        // 读取第一个存档
        System.out.println("\n--- 读取第一个存档 ---");
        hero.restoreMemento(caretaker.getFirstSave());
        System.out.println("恢复后状态: " + hero);
    }
}

/**
 * 原发器 - 游戏角色
 */
class GameRole {
    private String name;
    private int hp;    // 生命值
    private int mp;    // 魔法值

    public GameRole(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
    }

    // 战斗
    public void fightMonster() {
        hp -= 30;
        mp -= 20;
        if (hp < 0) hp = 0;
        if (mp < 0) mp = 0;
        System.out.println(name + " 与怪物战斗，生命值-" + 30 + "，魔法值-" + 20);
    }

    // 创建备忘录（保存状态）
    public RoleMemento createMemento() {
        return new RoleMemento(name, hp, mp);
    }

    // 恢复备忘录（恢复状态）
    public void restoreMemento(RoleMemento memento) {
        this.name = memento.getName();
        this.hp = memento.getHp();
        this.mp = memento.getMp();
    }

    @Override
    public String toString() {
        return "GameRole{name='" + name + "', hp=" + hp + ", mp=" + mp + "}";
    }
}

/**
 * 备忘录 - 游戏存档
 */
class RoleMemento {
    private String name;
    private int hp;
    private int mp;
    private long saveTime;

    public RoleMemento(String name, int hp, int mp) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
        this.saveTime = System.currentTimeMillis();
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMp() { return mp; }
    public long getSaveTime() { return saveTime; }
}

/**
 * 管理者 - 存档管理器
 */
class Caretaker {
    private List<RoleMemento> saves = new ArrayList<>();

    // 保存存档
    public void save(RoleMemento memento) {
        saves.add(memento);
        System.out.println("存档已保存: " + new java.util.Date(memento.getSaveTime()));
    }

    // 获取最新存档
    public RoleMemento getLastSave() {
        if (saves.isEmpty()) {
            return null;
        }
        return saves.get(saves.size() - 1);
    }

    // 获取第一个存档
    public RoleMemento getFirstSave() {
        if (saves.isEmpty()) {
            return null;
        }
        return saves.get(0);
    }

    // 获取所有存档
    public List<RoleMemento> getAllSaves() {
        return saves;
    }
}
