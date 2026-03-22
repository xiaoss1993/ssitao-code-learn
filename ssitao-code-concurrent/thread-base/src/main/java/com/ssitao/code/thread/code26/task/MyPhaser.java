package com.ssitao.code.thread.code26.task;

import java.util.concurrent.Phaser;

/**
 * 自定义Phaser - 阶段切换回调演示
 *
 * Phaser的onAdvance()方法是关键回调函数：
 * - 当Phaser中所有注册的参与者都到达当前阶段的同步点后，
 *   在放行所有线程进入下一阶段之前，会调用onAdvance()
 * - 返回false表示继续执行（Phaser不终止）
 * - 返回true表示Phaser应进入终止状态
 *
 * 阶段划分：
 * - phase 0: 等待所有学生到达考场
 * - phase 1: 第一题完成，等待所有人
 * - phase 2: 第二题完成，等待所有人
 * - phase 3: 第三题完成（exam结束），Phaser终止
 */
public class MyPhaser extends Phaser {
    /**
     * 阶段切换前的回调方法
     *
     * 此方法在每个阶段的所有参与者都到达后、新阶段开始前被调用。
     * 可以在这里执行每个阶段结束时的清理或日志工作。
     *
     * @param phase 当前即将结束的阶段号（从0开始）
     * @param registeredParties 当前注册的参与者数量
     * @return false表示Phaser继续运行，true表示Phaser应终止
     */
    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        switch (phase) {
            case 0:
                return studentArrived();
            case 1:
                return finishFirstExercise();
            case 2:
                return finishSecondExercise();
            case 3:
                return finishExam();
            default:
                // 默认返回true，使Phaser终止
                return true;
        }
    }

    /**
     * 阶段0结束回调：所有学生已到达考场
     *
     * @return false - 继续执行，Phaser不终止
     */
    private boolean studentArrived() {
        System.out.printf("Phaser: The exam are going to start. The students are ready.\n");
        System.out.printf("Phaser: We have %d students.\n", getRegisteredParties());
        return false;
    }

    /**
     * 阶段1结束回调：第一题全部完成
     *
     * @return false - 继续执行，进入第二题阶段
     */
    private boolean finishFirstExercise() {
        System.out.printf("Phaser: All the students has finished the first exercise.\n");
        System.out.printf("Phaser: It's turn for the second one.\n");
        return false;
    }

    /**
     * 阶段2结束回调：第二题全部完成
     *
     * @return false - 继续执行，进入第三题阶段
     */
    private boolean finishSecondExercise() {
        System.out.printf("Phaser: All the students has finished the second exercise.\n");
        System.out.printf("Phaser: It's turn for the third one.\n");
        return false;
    }

    /**
     * 阶段3结束回调：考试全部结束
     *
     * @return true - 返回true使Phaser进入终止状态
     */
    private boolean finishExam() {
        System.out.printf("Phaser: All the students has finished the exam.\n");
        System.out.printf("Phaser: Thank you for your time.\n");
        return true; // 考试结束，Phaser使命完成，终止
    }
}