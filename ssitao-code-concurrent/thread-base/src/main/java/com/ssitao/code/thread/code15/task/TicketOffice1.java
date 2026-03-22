package com.ssitao.code.thread.code15.task;

/**
 * 售票窗口1，出售1号放映厅的票
 *
 * 该类实现了Runnable接口，可以作为线程任务运行。
 * 职责：模拟售票窗口1对两个放映厅的票务操作。
 *
 * 【注意】虽然名称是TicketOffice1，但它可以操作两个厅的票
 * 操作顺序：厅1卖3张 → 厅1卖2张 → 厅2卖2张 → 厅1退3张 →
 *          厅1卖5张 → 厅2卖2张 → 厅2卖2张 → 厅2卖2张
 *
 * 【净变化计算】
 * 厅1：+3+2-3+5 = 7张 (卖出7张)
 * 厅2：-2-2-2 = -6张 (卖出6张)
 * 期望结果：厅1剩余20-7=13张，厅2剩余20-6=14张
 */
public class TicketOffice1 implements Runnable{
    /**
     * 电影院对象
     */
    private Cinema cinema;

    /**
     * 构造函数
     * @param cinema 电影院对象
     */
    public TicketOffice1(Cinema cinema) {
        this.cinema = cinema;
    }

    @Override
    public void run() {
        // 模拟售票窗口1的票务操作流程
        cinema.sellTickets1(3);  // 厅1卖3张
        cinema.sellTickets1(2);  // 厅1卖2张
        cinema.sellTickets2(2);  // 厅2卖2张
        cinema.returnTickets1(3); // 厅1退3张
        cinema.sellTickets1(5);  // 厅1卖5张
        cinema.sellTickets2(2);  // 厅2卖2张
        cinema.sellTickets2(2);  // 厅2卖2张
        cinema.sellTickets2(2);  // 厅2卖2张
    }
}
