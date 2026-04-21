package com.ssitao.code.designpattern.observer.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式基础示例
 *
 * 观察者模式特点：
 * 1. 定义对象间的一对多依赖关系
 * 2. 当对象状态改变时，所有依赖它的对象都会收到通知
 * 3. 主题与观察者之间是松耦合
 *
 * 组成：
 * - Subject: 主题/被观察者
 * - Observer: 观察者
 * - ConcreteSubject: 具体主题
 * - ConcreteObserver: 具体观察者
 */
public class BasicObserverDemo {

    public static void main(String[] args) {
        System.out.println("=== 观察者模式基础示例 ===\n");

        // 创建天气主题
        WeatherStation station = new WeatherStation();

        // 创建观察者
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay();
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay();
        ForecastDisplay forecastDisplay = new ForecastDisplay();

        // 注册观察者
        station.registerObserver(currentDisplay);
        station.registerObserver(statisticsDisplay);
        station.registerObserver(forecastDisplay);

        // 模拟天气变化
        System.out.println("--- 第一次更新 ---");
        station.setMeasurements(25, 65, 1013);

        System.out.println("\n--- 第二次更新 ---");
        station.setMeasurements(28, 70, 1012);

        System.out.println("\n--- 第三次更新 ---");
        station.setMeasurements(22, 90, 1010);

        // 移除一个观察者
        System.out.println("\n--- 移除统计观察者 ---");
        station.removeObserver(statisticsDisplay);

        System.out.println("\n--- 第四次更新 ---");
        station.setMeasurements(30, 60, 1015);
    }
}

/**
 * 观察者接口
 */
interface Observer {
    void update(float temperature, float humidity, float pressure);
}

/**
 * 主题接口
 */
interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}

/**
 * 具体主题 - 天气站
 */
class WeatherStation implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private float temperature;
    private float humidity;
    private float pressure;

    public void registerObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(temperature, humidity, pressure);
        }
    }

    public void measurementsChanged() {
        notifyObservers();
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }
}

/**
 * 当前天气显示 - 具体观察者
 */
class CurrentConditionsDisplay implements Observer {

    @Override
    public void update(float temperature, float humidity, float pressure) {
        System.out.println("[当前天气] 温度: " + temperature + "°C, 湿度: " + humidity + "%, 气压: " + pressure + "hPa");
    }
}

/**
 * 统计数据显示 - 具体观察者
 */
class StatisticsDisplay implements Observer {
    private float sumTemperature = 0;
    private int count = 0;

    @Override
    public void update(float temperature, float humidity, float pressure) {
        sumTemperature += temperature;
        count++;
        float avg = sumTemperature / count;
        System.out.println("[统计数据] 平均温度: " + avg + "°C, 记录数: " + count);
    }
}

/**
 * 天气预报显示 - 具体观察者
 */
class ForecastDisplay implements Observer {
    private float lastPressure;

    @Override
    public void update(float temperature, float humidity, float pressure) {
        String forecast;
        if (pressure > lastPressure) {
            forecast = "天气转晴";
        } else if (pressure < lastPressure) {
            forecast = "天气将变阴";
        } else {
            forecast = "天气保持不变";
        }
        lastPressure = pressure;
        System.out.println("[天气预报] " + forecast + ", 气压: " + pressure + "hPa");
    }
}
