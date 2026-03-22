package com.ssitao.code.thread.code23.task;

import java.util.concurrent.TimeUnit;

/**
 * 参与者类
 *
 * 代表视频会议中的一个参与者，每个参与者运行在自己的线程中。
 * 参与者会等待一个随机时间后"到达"会议。
 */
public class Participant implements Runnable {
    /**
     * 视频会议对象引用
     * 用于通知会议主持人自己已到达
     */
    private VideoConference conference;

    /**
     * 参与者的名称
     * 仅用于日志输出，便于追踪是哪个参与者
     */
    private String name;

    /**
     * 构造函数
     *
     * @param conference 视频会议对象
     * @param name       参与者的名称
     */
    public Participant(VideoConference conference, String name) {
        this.conference = conference;
        this.name = name;
    }


    /**
     * 核心方法：模拟参与者到达会议的过程
     *
     * 执行流程：
     * 1. 生成一个随机等待时间（0-10秒），模拟真实场景中参与者的时间不确定性
     * 2. 在这段时间内模拟"准备"过程（通过 sleep 实现）
     * 3. 等待完成后，调用 conference.arrive() 通知会议主持人自己已到达
     */
    @Override
    public void run() {
        // 生成随机等待时间（0-10秒）
        // 模拟不同参与者从不同地方接入会议的时间差异
        long duration = (long) (Math.random() * 10);

        try {
            // 模拟参与者"准备"参加会议的过程
            // 在实际场景中，这可能包括：登录、打开摄像头、检查音频等
            TimeUnit.SECONDS.sleep(duration);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ========== 到达会议 ==========
        // 通知 VideoConference 这个参与者已经到达
        // 这会触发 VideoConference 的 countDown()，将计数器减1
        conference.arrive(name);
    }
}
