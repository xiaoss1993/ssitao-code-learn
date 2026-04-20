package com.ssitao.code.disruptor.spring.config;

/**
 * 订单事件 - 用于 Spring 集成示例
 */
public class OrderEvent {
    private String orderId;
    private String symbol;
    private double price;
    private int quantity;
    private boolean rejected;
    private String rejectReason;
    private boolean matched;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isRejected() { return rejected; }
    public void setRejected(boolean rejected) { this.rejected = rejected; }

    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }

    public boolean isMatched() { return matched; }
    public void setMatched(boolean matched) { this.matched = matched; }
}