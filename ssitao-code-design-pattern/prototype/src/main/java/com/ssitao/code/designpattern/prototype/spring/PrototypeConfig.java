package com.ssitao.code.designpattern.prototype.spring;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Spring中原型模式示例 - 配置类方式
 */
@Configuration
public class PrototypeConfig {

    /**
     * 使用@Scope("prototype")注解，生成原型Bean
     */
    @Bean
    @Scope("prototype")
    public Product product() {
        return new Product();
    }

    /**
     * 使用SCOPE_PROTOTYPE常量，效果相同
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ShoppingCart shoppingCart() {
        return new ShoppingCart();
    }
}
