package com.ssitao.code.designpattern.composite;

/**
 * 组合模式总结
 *
 * 组合模式(Composite Pattern)：
 * 将对象组合成树形结构以表示"部分-整体"的层次结构。
 * 使得用户对单个对象和组合对象的使用具有一致性。
 *
 * 结构：
 * - Component(组件): 抽象构件，定义统一的接口
 * - Leaf(叶子): 表示叶子节点，没有子节点
 * - Composite(组合): 表示容器节点，可以包含子节点
 *
 * 适用场景：
 * 1. 文件系统（文件/文件夹）
 * 2. 组织架构（员工/部门）
 * 3. UI组件（按钮/容器）
 * 4. 菜单系统
 * 5. XML/HTML DOM树
 *
 * 优点：
 * 1. 统一处理单对象和组合对象
 * 2. 简化客户端代码
 * 3. 容易添加新组件
 *
 * 缺点：
 * 1. 设计复杂
 * 2. 可能产生过多对象
 *
 * 本示例包含：
 * 1. FileSystemExample - 文件系统（文件/文件夹）
 * 2. Employee - 组织架构（员工/经理）
 * 3. MenuComponent - 菜单系统
 */
public class CompositeSummary {
}
