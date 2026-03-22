package com.ssitao.code.thread.code15.task;

/**
 * 影院类，管理两个独立放映厅的票务
 *
 * 【与Code14的区别】
 * Code14使用synchronized修饰方法，整个对象共用一把锁。
 * 本类使用synchronized代码块，为不同资源使用不同的锁对象，
 * 实现更细粒度的并发控制，提高并发性能。
 *
 * 【锁策略】
 * - vacanciesCinema1(厅1票数) 使用 controlCinema1 作为锁
 * - vacanciesCinema2(厅2票数) 使用 controlCinema2 作为锁
 *
 * 【并发优势】
 * 当TicketOffice1操作厅1时，TicketOffice2可以同时操作厅2
 * 两个厅的操作互不阻塞，并发执行
 */
public class Cinema {
    /**
     * 影院厅1的剩余电影票数
     */
    private long vacanciesCinema1;
    /**
     * 影院厅2的剩余电影票数
     */
    private long vacanciesCinema2;
    /**
     * 控制vacanciesCinema1同步访问的锁对象
     * 使用独立的锁对象，实现对厅1的细粒度控制
     */
    private final Object controlCinema1;
    /**
     * 控制vacanciesCinema2同步访问的锁对象
     * 使用独立的锁对象，实现对厅2的细粒度控制
     */
    private final Object controlCinema2;

    /**
     * 构造函数，初始化电影院
     * 每个厅初始有20张票
     */
    public Cinema() {
        controlCinema1 = new Object(); // 为厅1创建独立锁对象
        controlCinema2 = new Object(); // 为厅2创建独立锁对象
        vacanciesCinema1 = 20; // 厅1初始票数
        vacanciesCinema2 = 20; // 厅2初始票数
    }

    /**
     * 出售影院厅1的门票（同步代码块）
     *
     * 使用synchronized(controlCinema1)同步代码块，
     * 只锁定与厅1相关的操作，不影响厅2的操作。
     *
     * @param number 出售的门票张数
     * @return true出售成功，false票数不足
     */
    public boolean sellTickets1(int number) {
        synchronized (controlCinema1) {
            if (number < vacanciesCinema1) {
                vacanciesCinema1 -= number;
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 出售影院厅2的门票（同步代码块）
     *
     * 使用synchronized(controlCinema2)同步代码块，
     * 只锁定与厅2相关的操作，不影响厅1的操作。
     *
     * @param number 出售的门票张数
     * @return true出售成功，false票数不足
     */
    public boolean sellTickets2(int number) {
        synchronized (controlCinema2) {
            if (number < vacanciesCinema2) {
                vacanciesCinema2 -= number;
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 向影院厅1退门票（同步代码块）
     *
     * 使用synchronized(controlCinema1)同步代码块，
     * 与sellTickets1()互斥，保证票数安全更新。
     *
     * @param number 退的门票张数
     * @return true退票成功
     */
    public boolean returnTickets1(int number) {
        synchronized (controlCinema1) {
            vacanciesCinema1 += number;
            return true;
        }
    }

    /**
     * 向影院厅2退门票（同步代码块）
     *
     * 使用synchronized(controlCinema2)同步代码块，
     * 与sellTickets2()互斥，保证票数安全更新。
     *
     * @param number 退的门票张数
     * @return true退票成功
     */
    public boolean returnTickets2(int number) {
        synchronized (controlCinema2) {
            vacanciesCinema2 += number;
            return true;
        }
    }

    /**
     * 获取影院厅1剩余的门票数
     * 注意：get方法未同步，存在短暂不一致风险，但最终会一致
     * @return 影院1剩余的门票数
     */
    public long getVacanciesCinema1() {
        return vacanciesCinema1;
    }

    /**
     * 获取影院厅2剩余的门票数
     * 注意：get方法未同步，存在短暂不一致风险，但最终会一致
     * @return 影院2剩余的门票数
     */
    public long getVacanciesCinema2() {
        return vacanciesCinema2;
    }
}
