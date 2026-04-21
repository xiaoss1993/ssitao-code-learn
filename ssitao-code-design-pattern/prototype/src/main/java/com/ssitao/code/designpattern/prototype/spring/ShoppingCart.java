package com.ssitao.code.designpattern.prototype.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 购物车 - 原型Bean
 * 每个用户应该有自己的购物车实例
 */
public class ShoppingCart {

    private String cartId;
    private String userId;
    private List<Product> products;

    public ShoppingCart() {
        this.cartId = UUID.randomUUID().toString();
        this.products = new ArrayList<>();
    }

    public ShoppingCart(String userId) {
        this.cartId = UUID.randomUUID().toString();
        this.userId = userId;
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public double getTotalPrice() {
        return products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "ShoppingCart{cartId='" + cartId + "', userId='" + userId +
                "', products=" + products + ", total=" + getTotalPrice() + "}";
    }
}
