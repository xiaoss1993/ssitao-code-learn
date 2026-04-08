package com.ssitao.code.jdk.phase01.object;

/**
 * 第一阶段步骤1: Object类 - 演示深拷贝
 */
public class DeepCopyDemo implements Cloneable {
    private int[] data;

    public DeepCopyDemo(int[] data) {
        // 复制数组,避免外部修改影响内部
        this.data = data.clone();
    }

    /**
     * 深拷贝 - 复制引用的对象,创建独立副本
     */
    @Override
    protected DeepCopyDemo clone() throws CloneNotSupportedException {
        DeepCopyDemo result = (DeepCopyDemo) super.clone();
        result.data = this.data.clone();  // 关键:复制数组本身
        return result;
    }

    public void setData(int index, int value) {
        data[index] = value;
    }

    public int getData(int index) {
        return data[index];
    }

    public static void main(String[] args) {
        System.out.println("=== 深拷贝演示 ===\n");

        int[] originalData = {1, 2, 3};
        DeepCopyDemo original = new DeepCopyDemo(originalData);

        try {
            DeepCopyDemo deepCopy = original.clone();

            System.out.println("--- 深拷贝 ---");
            System.out.println("原始对象data[0] = " + original.getData(0));
            System.out.println("深拷贝对象data[0] = " + deepCopy.getData(0));

            // 修改原始对象的数据
            original.setData(0, 100);
            System.out.println("\n修改原始对象data[0] = 100后:");
            System.out.println("原始对象data[0] = " + original.getData(0));
            System.out.println("深拷贝对象data[0] = " + deepCopy.getData(0));
            System.out.println("(深拷贝不受影响,因为是独立的数据副本!)");

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
