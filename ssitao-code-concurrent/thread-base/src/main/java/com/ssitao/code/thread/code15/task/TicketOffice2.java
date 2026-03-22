package com.ssitao.code.thread.code15.task;

/**
 * 售票窗口2，出售2号放映厅的票
 *
 * 该类实现了Runnable接口，可以作为线程任务运行。
 * 职责：模拟售票窗口2对两个放映厅的票务操作。
 *
 * 【注意】虽然名称是TicketOffice2，但它可以操作两个厅的票
 * 操作顺序：厅2卖2张 → 厅2卖4张 → 厅1卖2张 → 厅1卖1张 →
 *          厅2退2张 → 厅1卖3张 → 厅2卖2张 → 厅1卖2张
 *
 * 【净变化计算】
 * 厅1：-2-1-3-2 = -8张 (卖出8张)
 * 厅2：-2-4+2-2 = -6张 (卖出6张)
 * 期望结果：厅1剩余20-8=12张，厅2剩余20-6=14张
 *
 * 【与TicketOffice1的并发说明】
 * 由于Cinema使用独立的锁对象控制两个厅：
 * - 厅1操作用controlCinema1锁
 * - 厅2操作用controlCinema2锁
 * 所以TicketOffice1操作厅1时，TicketOffice2可以同时操作厅2
 */
public class TicketOffice2 implements Runnable{
    /**
     * 电影院对象
     */
    private Cinema cinema;

    /**
     * 构造函数
     *
     * @param cinema 电影院对象
     */
    public TicketOffice2(Cinema cinema) {
        this.cinema = cinema;
    }

    @Override
    public void run() {
        // 模拟售票窗口2的票务操作流程
        cinema.sellTickets2(2);  // 厅2卖2张
        cinema.sellTickets2(4); // 厅2卖4张
        cinema.sellTickets1(2); // 厅1卖2张
        cinema.sellTickets1(1); // 厅1卖1张
        cinema.returnTickets2(2); // 厅2退2张
        cinema.sellTickets1(3); // 厅1卖3张
        cinema.sellTickets2(2); // 厅2卖2张
        cinema.sellTickets1(2); // 厅1卖2张
    }
}
