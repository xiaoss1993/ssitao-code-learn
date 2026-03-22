package com.ssitao.code.thread.code15.core;

import com.ssitao.code.thread.code15.task.Cinema;
import com.ssitao.code.thread.code15.task.TicketOffice1;
import com.ssitao.code.thread.code15.task.TicketOffice2;

/**
 * Code15: 使用同步代码块实现更细粒度的锁
 *
 * 本示例演示如何使用synchronized代码块实现更细粒度的并发控制。
 * 与code14(整个对象锁)不同，本例使用不同的锁对象控制不同的资源，
 * 从而允许对不同资源的操作并发执行，提高并发性能。
 *
 * 场景说明：
 * - 一个电影院有两个独立放映厅(厅1和厅2)
 * - 两个售票窗口同时工作：TicketOffice1和TicketOffice2
 * - 每个窗口可以售卖和退票两个厅的票
 *
 * 锁策略：
 * - 厅1的操作使用 controlCinema1 作为锁
 * - 厅2的操作使用 controlCinema2 作为锁
 * - 这样TicketOffice1操作厅1时，TicketOffice2仍可以操作厅2
 *
 * 预期结果：两个厅的票数最终应该与初始值一致（20）
 */
public class Main {
    public static void main(String[] args) {
        // 创建一个电影院对象
        Cinema cinema = new Cinema();
        // 初始时每个厅有20张票

        // 创建一个出售一号影院厅票的售票窗口对象，并且让其在一个线程中运行
        TicketOffice1 ticketOffice1 = new TicketOffice1(cinema);
        Thread thread1 = new Thread(ticketOffice1, "TicketOffice1");

        // 创建一个出售二号影院厅票的售票窗口对象，并且让其在一个线程中运行
        TicketOffice2 ticketOffice2 = new TicketOffice2(cinema);
        Thread thread2 = new Thread(ticketOffice2, "TicketOffice2");

        // 启动两个售票窗口线程
        thread1.start();
        thread2.start();

        try {
            // 等待两个线程完成
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 输出电影院剩余的票数
        System.out.printf("Room 1 Vacancies: %d\n", cinema.getVacanciesCinema1());
        System.out.printf("Room 2 Vacancies: %d\n", cinema.getVacanciesCinema2());
    }
}
