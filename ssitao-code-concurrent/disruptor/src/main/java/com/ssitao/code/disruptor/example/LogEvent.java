package com.ssitao.code.disruptor.example;

/**
 * 日志事件
 */
public class LogEvent {
    private String timestamp;
    private String level;
    private String message;
    private String threadName;
    private String category;
    private boolean alertLevel;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(boolean alertLevel) {
        this.alertLevel = alertLevel;
    }
}
