package com.ssitao.code.designpattern.state.gumball;

/**
 * 糖果机示例 - 状态模式经典案例
 *
 * 糖果机有4个状态：
 * 1. NoQuarterState - 没有投入硬币
 * 2. HasQuarterState - 已投入硬币
 * 3. SoldState - 出售糖果
 * 4. SoldOutState - 糖果售罄
 */
public class GumballStateDemo {

    public static void main(String[] args) {
        System.out.println("=== 糖果机示例 ===\n");

        // 创建糖果机，有5颗糖果
        GumballMachine machine = new GumballMachine(5);

        // 展示当前状态
        System.out.println(machine);

        // 尝试直接 turn crank（没有投币）
        System.out.println("\n--- 尝试直接转动曲柄 ---");
        machine.turnCrank();

        // 投入硬币
        System.out.println("\n--- 投入硬币 ---");
        machine.insertQuarter();

        // 再次转动曲柄
        System.out.println("\n--- 转动曲柄 ---");
        machine.turnCrank();

        // 展示状态
        System.out.println("\n当前状态: " + machine);

        // 连续购买
        System.out.println("\n--- 再次投入硬币并购买 ---");
        machine.insertQuarter();
        machine.turnCrank();

        System.out.println("\n--- 又买一颗 ---");
        machine.insertQuarter();
        machine.turnCrank();

        System.out.println("\n--- 连续购买 ---");
        machine.insertQuarter();
        machine.turnCrank();
        machine.insertQuarter();
        machine.turnCrank();

        // 糖果售罄
        System.out.println("\n--- 糖果售罄后 ---");
        machine.insertQuarter();
        machine.turnCrank();
    }
}

/**
 * 糖果机
 */
class GumballMachine {
    private State noQuarterState;
    private State hasQuarterState;
    private State soldState;
    private State soldOutState;

    private State currentState;
    private int count; // 糖果数量

    public GumballMachine(int numberGumballs) {
        count = numberGumballs;

        // 创建各个状态
        noQuarterState = new NoQuarterState(this);
        hasQuarterState = new HasQuarterState(this);
        soldState = new SoldState(this);
        soldOutState = new SoldOutState(this);

        // 初始状态
        if (count > 0) {
            currentState = noQuarterState;
        } else {
            currentState = soldOutState;
        }
    }

    // 投入硬币
    public void insertQuarter() {
        currentState.insertQuarter();
    }

    // 退回硬币
    public void ejectQuarter() {
        currentState.ejectQuarter();
    }

    // 转动曲柄
    public void turnCrank() {
        currentState.turnCrank();
        currentState.dispense();
    }

    // 发放糖果
    public void releaseGumball() {
        System.out.println("一颗糖果滚出来了！");
        if (count > 0) {
            count--;
        }
    }

    // 填充糖果
    public void refill(int count) {
        this.count += count;
        System.out.println("糖果机已填充 " + count + " 颗糖果");
        if (count > 0) {
            currentState = noQuarterState;
        }
    }

    // Getter/Setter
    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public int getCount() {
        return count;
    }

    public State getNoQuarterState() {
        return noQuarterState;
    }

    public State getHasQuarterState() {
        return hasQuarterState;
    }

    public State getSoldState() {
        return soldState;
    }

    public State getSoldOutState() {
        return soldOutState;
    }

    @Override
    public String toString() {
        return "糖果机状态: " + currentState + ", 剩余糖果: " + count + "颗";
    }
}

/**
 * 状态接口
 */
interface State {
    void insertQuarter();    // 投入硬币
    void ejectQuarter();    // 退回硬币
    void turnCrank();       // 转动曲柄
    void dispense();        // 发放糖果

    String toString();
}

/**
 * 没有投币状态
 */
class NoQuarterState implements State {
    private GumballMachine machine;

    public NoQuarterState(GumballMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("你投入了一枚硬币");
        machine.setCurrentState(machine.getHasQuarterState());
    }

    @Override
    public void ejectQuarter() {
        System.out.println("你没有投入硬币，无法退回");
    }

    @Override
    public void turnCrank() {
        System.out.println("你转动了曲柄，但没有投入硬币");
    }

    @Override
    public void dispense() {
        System.out.println("需要先投入硬币");
    }

    @Override
    public String toString() {
        return "等待投币";
    }
}

/**
 * 已投币状态
 */
class HasQuarterState implements State {
    private GumballMachine machine;

    public HasQuarterState(GumballMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("你已经投过硬币了");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("硬币退回");
        machine.setCurrentState(machine.getNoQuarterState());
    }

    @Override
    public void turnCrank() {
        System.out.println("你转动了曲柄...");
        machine.setCurrentState(machine.getSoldState());
    }

    @Override
    public void dispense() {
        System.out.println("没有转动曲柄，无法发放糖果");
    }

    @Override
    public String toString() {
        return "已投币";
    }
}

/**
 * 出售状态
 */
class SoldState implements State {
    private GumballMachine machine;

    public SoldState(GumballMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("请稍等，糖果正在准备中");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("你已转动曲柄，无法退回硬币");
    }

    @Override
    public void turnCrank() {
        System.out.println("转动一次就够了");
    }

    @Override
    public void dispense() {
        machine.releaseGumball();
        if (machine.getCount() > 0) {
            machine.setCurrentState(machine.getNoQuarterState());
        } else {
            System.out.println("糖果已售罄");
            machine.setCurrentState(machine.getSoldOutState());
        }
    }

    @Override
    public String toString() {
        return "出售中";
    }
}

/**
 * 售罄状态
 */
class SoldOutState implements State {
    private GumballMachine machine;

    public SoldOutState(GumballMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("糖果已售罄，无法投币");
    }

    @Override
    public void ejectQuarter() {
        if (machine.getCount() > 0) {
            System.out.println("你没有投入硬币");
        } else {
            System.out.println("糖果已售罄");
        }
    }

    @Override
    public void turnCrank() {
        System.out.println("糖果已售罄");
    }

    @Override
    public void dispense() {
        System.out.println("糖果已售罄");
    }

    @Override
    public String toString() {
        return "已售罄";
    }
}
