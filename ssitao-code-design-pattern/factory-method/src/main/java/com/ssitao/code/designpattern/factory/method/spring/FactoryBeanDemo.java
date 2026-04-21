package com.ssitao.code.designpattern.factory.method.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Spring 工厂方法模式示例 - FactoryBean
 *
 * FactoryBean 是 Spring 容器中用于创建 Bean 的工厂接口，
 * 实现了工厂方法模式，让子类决定实例化哪个类
 *
 * 工厂方法模式体现：
 * - 抽象工厂：FactoryBean<T>
 * - 具体工厂：实现 FactoryBean 的类（如 MyCarFactoryBean）
 * - 抽象产品：T（如 Car 接口）
 * - 具体产品：实现 T 的类（如 Ferrari, Tesla）
 *
 * Spring 内置的 FactoryBean 实现：
 * - SqlSessionFactoryBean (MyBatis)
 * - RabbitFactoryBean (Spring AMQP)
 * - ProxyFactoryBean (AOP)
 */
public class FactoryBeanDemo {

    public static void main(String[] args) {
        System.out.println("=== Spring FactoryBean 工厂方法示例 ===\n");

        // 创建 Spring 应用上下文（自动扫描 FactoryBean）
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // 从容器获取 Car Bean - 实际返回的是 Tesla（由 FactoryBean 创建）
        Car teslaCar = context.getBean("car", Car.class);
        System.out.println("获取到的 Car Bean: " + teslaCar.getClass().getSimpleName());
        teslaCar.drive();

        // 获取 FactoryBean 本身（需要加 & 前缀）
        CarFactoryBean factoryBean = context.getBean("&car", CarFactoryBean.class);
        System.out.println("\nFactoryBean 本身: " + factoryBean.getClass().getSimpleName());
        System.out.println("FactoryBean 创建的产品类型: " + factoryBean.getObjectType().getSimpleName());
    }
}

/**
 * Car 接口 - 抽象产品
 */
interface Car {
    void drive();
}

/**
 * Tesla - 具体产品
 */
class Tesla implements Car {
    @Override
    public void drive() {
        System.out.println("  驾驶 Tesla Model Y - 自动驾驶中...");
    }
}

/**
 * Ferrari - 具体产品
 */
class Ferrari implements Car {
    @Override
    public void drive() {
        System.out.println("  驾驶 Ferrari F8 - 赛道模式启动!");
    }
}

/**
 * CarFactoryBean - 工厂方法接口实现
 *
 * 核心方法:
 * - getObject(): 返回创建的产品实例
 * - getObjectType(): 返回产品类型
 * - isSingleton(): 是否单例
 */
class CarFactoryBean implements FactoryBean<Car> {

    private String carType = "tesla"; // 默认创建 Tesla

    public void setCarType(String carType) {
        this.carType = carType;
    }

    @Override
    public Car getObject() {
        // 工厂方法：决定创建哪种汽车
        if ("ferrari".equalsIgnoreCase(carType)) {
            return new Ferrari();
        }
        return new Tesla();
    }

    @Override
    public Class<?> getObjectType() {
        return Car.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

/**
 * Spring 配置类
 */
class AppConfig {

    // 使用 @Bean 方式配置 FactoryBean
    @org.springframework.context.annotation.Bean
    public CarFactoryBean car() {
        CarFactoryBean factoryBean = new CarFactoryBean();
        factoryBean.setCarType("tesla"); // 可以通过配置切换
        return factoryBean;
    }
}
