package com.ssitao.code.jdk.phase01.object;

/**
 * 第一阶段步骤1: Object类 - 演示浅拷贝与深拷贝
 */
public class ShallowCopyDemo implements Cloneable {
    private int[] data;

    public ShallowCopyDemo(int[] data) {
        this.data = data;
    }

    /**
     * 浅拷贝 - 只复制引用,不复制引用的对象
     */
    @Override
    protected ShallowCopyDemo clone() throws CloneNotSupportedException {
        return (ShallowCopyDemo) super.clone();
    }

    public void setData(int index, int value) {
        data[index] = value;
    }

    public int getData(int index) {
        return data[index];
    }

    public int[] getDataArray() {
        return data;
    }

    public static void main(String[] args) {
        System.out.println("=== 浅拷贝 vs 深拷贝 ===\n");

        int[] originalData = {1, 2, 3};
        ShallowCopyDemo original = new ShallowCopyDemo(originalData);

        try {
            // 浅拷贝
            ShallowCopyDemo shallowCopy = original.clone();

            System.out.println("--- 浅拷贝 ---");
            System.out.println("原始对象data[0] = " + original.getData(0));
            System.out.println("浅拷贝对象data[0] = " + shallowCopy.getData(0));

            // 修改原始对象的数据
            original.setData(0, 100);
            System.out.println("\n修改原始对象data[0] = 100后:");
            System.out.println("原始对象data[0] = " + original.getData(0));
            System.out.println("浅拷贝对象data[0] = " + shallowCopy.getData(0));
            System.out.println("(浅拷贝也被影响了,因为共享同一个数组引用!)");

            System.out.println("\n原始数组data[0] = " + originalData[0]);
            System.out.println("(原始数组也被影响了)");

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
