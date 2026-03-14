package com.ssitao.code.designpattern.decorator;

/**
 * 装饰器模式总结
 *
 * 装饰器模式(Decorator Pattern)：
 * 动态地给对象添加额外的职责。相比生成子类，装饰器模式更加灵活。
 *
 * 结构：
 * - Component(组件): 定义统一接口
 * - ConcreteComponent(具体组件): 实现组件接口
 * - Decorator(装饰器): 持有组件引用，继承组件接口
 * - ConcreteDecorator(具体装饰器): 添加额外职责
 *
 * 适用场景：
 * 1. 咖啡店订单系统（配料组合）
 * 2. JDK IO流（BufferedInputStream等）
 * 3. 数据源包装（加密、压缩、日志）
 * 4. UI组件增强
 * 5. Web过滤器/拦截器
 *
 * 优点：
 * 1. 比继承更灵活
 * 2. 可以动态添加/删除职责
 * 3. 可以组合多个装饰器
 *
 * 缺点：
 * 1. 可能产生过多小对象
 * 2. 调试困难
 *
 * 本示例包含：
 * 1. Coffee - 咖啡店系统
 * 2. DataSource - 数据源系统
 * 3. InputStreamExample - JDK IO流
 */
public class DecoratorSummary {
}
