package com.learn.crazyjava.lesson07_exception;

/**
 * 第7课：异常处理 - 自定义异常
 */
public class AgeException extends Exception {
    public AgeException() {
        super("年龄异常");
    }

    public AgeException(String message) {
        super(message);
    }
}
