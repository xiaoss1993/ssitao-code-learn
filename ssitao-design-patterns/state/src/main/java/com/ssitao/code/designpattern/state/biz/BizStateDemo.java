package com.ssitao.code.designpattern.state.biz;

/**
 * 业务场景中的状态模式
 *
 * 典型应用：
 * 1. 电梯控制 - 电梯状态流转
 * 2. 交通信号灯 - 红绿灯变化
 * 3. 账户状态 - 账户启用/冻结/注销
 * 4. 门禁系统 - 门状态变化
 * 5. 饮料贩卖机 - 饮料机状态
 */
public class BizStateDemo {

    public static void main(String[] args) {
        System.out.println("=== 业务场景状态模式 ===\n");

        // 1. 电梯控制
        System.out.println("1. 电梯控制示例");
        elevatorDemo();

        // 2. 账户状态
        System.out.println("\n2. 账户状态示例");
        accountDemo();
    }

    /**
     * 电梯控制示例
     */
    private static void elevatorDemo() {
        Elevator elevator = new Elevator();

        // 电梯运行
        System.out.println("--- 按下上升按钮 ---");
        elevator.pressUpButton();

        System.out.println("\n--- 到达3楼 ---");
        elevator.floorArrived(3);

        System.out.println("\n--- 按下开门按钮 ---");
        elevator.pressOpenButton();

        System.out.println("\n--- 人员进入，关门 ---");
        elevator.pressCloseButton();

        System.out.println("\n--- 按下上升按钮 ---");
        elevator.pressUpButton();

        System.out.println("\n--- 到达5楼 ---");
        elevator.floorArrived(5);

        System.out.println("\n--- 按下报警按钮 ---");
        elevator.pressAlarmButton();

        System.out.println("\n--- 维修后复位 ---");
        elevator.repair();
    }

    /**
     * 账户状态示例
     */
    private static void accountDemo() {
        Account account = new Account("ACC-001");

        // 账户操作
        System.out.println("--- 账户开户 ---");
        account.deposit(1000);

        System.out.println("\n--- 取款500 ---");
        account.withdraw(500);

        System.out.println("\n--- 冻结账户 ---");
        account.freeze();

        System.out.println("\n--- 尝试取款 ---");
        account.withdraw(100);

        System.out.println("\n--- 解冻账户 ---");
        account.unfreeze();

        System.out.println("\n--- 再次取款 ---");
        account.withdraw(200);

        System.out.println("\n--- 注销账户 ---");
        account.close();

        System.out.println("\n--- 尝试操作已注销账户 ---");
        account.deposit(100);
    }
}

// ============================================
// 1. 电梯控制相关类
// ============================================

/**
 * 电梯
 */
class Elevator {
    private ElevatorState state;
    private int currentFloor = 1;
    private int targetFloor;

    public Elevator() {
        state = new IdleState(this);
    }

    public void setState(ElevatorState state) {
        this.state = state;
    }

    public void setTargetFloor(int floor) {
        this.targetFloor = floor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int floor) {
        this.currentFloor = floor;
    }

    public void pressUpButton() {
        state.pressUpButton();
    }

    public void pressDownButton() {
        state.pressDownButton();
    }

    public void pressOpenButton() {
        state.pressOpenButton();
    }

    public void pressCloseButton() {
        state.pressCloseButton();
    }

    public void pressAlarmButton() {
        state.pressAlarmButton();
    }

    public void floorArrived(int floor) {
        state.floorArrived(floor);
    }

    public void repair() {
        setState(new IdleState(this));
        System.out.println("电梯已修复，恢复正常运行");
    }
}

/**
 * 电梯状态接口
 */
interface ElevatorState {
    void pressUpButton();
    void pressDownButton();
    void pressOpenButton();
    void pressCloseButton();
    void pressAlarmButton();
    void floorArrived(int floor);
}

/**
 * 空闲状态
 */
class IdleState implements ElevatorState {
    private Elevator elevator;

    public IdleState(Elevator elevator) {
        this.elevator = elevator;
    }

    @Override
    public void pressUpButton() {
        System.out.println("电梯准备上升...");
        elevator.setState(new MovingUpState(elevator));
    }

    @Override
    public void pressDownButton() {
        System.out.println("电梯准备下降...");
        elevator.setState(new MovingDownState(elevator));
    }

    @Override
    public void pressOpenButton() {
        System.out.println("开门");
        elevator.setState(new DoorOpenState(elevator));
    }

    @Override
    public void pressCloseButton() {
        System.out.println("门已关闭");
    }

    @Override
    public void pressAlarmButton() {
        System.out.println("按下报警按钮，电梯停止运行");
        elevator.setState(new AlarmState(elevator));
    }

    @Override
    public void floorArrived(int floor) {
        System.out.println("电梯到达 " + floor + " 楼");
    }
}

/**
 * 上升状态
 */
class MovingUpState implements ElevatorState {
    private Elevator elevator;

    public MovingUpState(Elevator elevator) {
        this.elevator = elevator;
    }

    @Override
    public void pressUpButton() {
        System.out.println("电梯已在上升中");
    }

    @Override
    public void pressDownButton() {
        System.out.println("电梯已在上升，不能下行");
    }

    @Override
    public void pressOpenButton() {
        System.out.println("电梯运行中，不能开门");
    }

    @Override
    public void pressCloseButton() {
        System.out.println("电梯运行中");
    }

    @Override
    public void pressAlarmButton() {
        System.out.println("按下报警按钮，电梯停止");
        elevator.setState(new AlarmState(elevator));
    }

    @Override
    public void floorArrived(int floor) {
        elevator.setCurrentFloor(floor);
        System.out.println("电梯到达 " + floor + " 楼");
        elevator.setState(new StoppedState(elevator));
    }
}

/**
 * 下降状态
 */
class MovingDownState implements ElevatorState {
    private Elevator elevator;

    public MovingDownState(Elevator elevator) {
        this.elevator = elevator;
    }

    @Override
    public void pressUpButton() {
        System.out.println("电梯已在下降，不能上行");
    }

    @Override
    public void pressDownButton() {
        System.out.println("电梯已在下降中");
    }

    @Override
    public void pressOpenButton() {
        System.out.println("电梯运行中，不能开门");
    }

    @Override
    public void pressCloseButton() {
        System.out.println("电梯运行中");
    }

    @Override
    public void pressAlarmButton() {
        System.out.println("按下报警按钮，电梯停止");
        elevator.setState(new AlarmState(elevator));
    }

    @Override
    public void floorArrived(int floor) {
        elevator.setCurrentFloor(floor);
        System.out.println("电梯到达 " + floor + " 楼");
        elevator.setState(new StoppedState(elevator));
    }
}

/**
 * 停止状态
 */
class StoppedState implements ElevatorState {
    private Elevator elevator;

    public StoppedState(Elevator elevator) {
        this.elevator = elevator;
    }

    @Override
    public void pressUpButton() {
        System.out.println("电梯准备上升...");
        elevator.setState(new MovingUpState(elevator));
    }

    @Override
    public void pressDownButton() {
        System.out.println("电梯准备下降...");
        elevator.setState(new MovingDownState(elevator));
    }

    @Override
    public void pressOpenButton() {
        System.out.println("开门");
        elevator.setState(new DoorOpenState(elevator));
    }

    @Override
    public void pressCloseButton() {
        System.out.println("关门");
        elevator.setState(new IdleState(elevator));
    }

    @Override
    public void pressAlarmButton() {
        System.out.println("按下报警按钮");
        elevator.setState(new AlarmState(elevator));
    }

    @Override
    public void floorArrived(int floor) {
        System.out.println("电梯已停止在 " + floor + " 楼");
    }
}

/**
 * 门开启状态
 */
class DoorOpenState implements ElevatorState {
    private Elevator elevator;

    public DoorOpenState(Elevator elevator) {
        this.elevator = elevator;
    }

    @Override
    public void pressUpButton() {
        System.out.println("请先关门");
    }

    @Override
    public void pressDownButton() {
        System.out.println("请先关门");
    }

    @Override
    public void pressOpenButton() {
        System.out.println("门已打开");
    }

    @Override
    public void pressCloseButton() {
        System.out.println("关门");
        elevator.setState(new IdleState(elevator));
    }

    @Override
    public void pressAlarmButton() {
        System.out.println("按下报警按钮");
        elevator.setState(new AlarmState(elevator));
    }

    @Override
    public void floorArrived(int floor) {
        System.out.println("门开启时到达 " + floor + " 楼");
    }
}

/**
 * 报警状态
 */
class AlarmState implements ElevatorState {
    private Elevator elevator;

    public AlarmState(Elevator elevator) {
        this.elevator = elevator;
    }

    @Override
    public void pressUpButton() {
        System.out.println("电梯故障，无法运行");
    }

    @Override
    public void pressDownButton() {
        System.out.println("电梯故障，无法运行");
    }

    @Override
    public void pressOpenButton() {
        System.out.println("电梯故障，无法开门");
    }

    @Override
    public void pressCloseButton() {
        System.out.println("电梯故障");
    }

    @Override
    public void pressAlarmButton() {
        System.out.println("报警中...");
    }

    @Override
    public void floorArrived(int floor) {
        System.out.println("电梯故障停止在 " + floor + " 楼");
    }
}

// ============================================
// 2. 账户状态相关类
// ============================================

/**
 * 账户
 */
class Account {
    private String accountId;
    private AccountState state;
    private double balance;

    public Account(String accountId) {
        this.accountId = accountId;
        this.balance = 0;
        this.state = new ActiveState(this);
    }

    public void setState(AccountState state) {
        this.state = state;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        state.deposit(amount);
    }

    public void withdraw(double amount) {
        state.withdraw(amount);
    }

    public void freeze() {
        state.freeze();
    }

    public void unfreeze() {
        state.unfreeze();
    }

    public void close() {
        state.close();
    }
}

/**
 * 账户状态接口
 */
interface AccountState {
    void deposit(double amount);
    void withdraw(double amount);
    void freeze();
    void unfreeze();
    void close();
    String getStateName();
}

/**
 * 正常状态
 */
class ActiveState implements AccountState {
    private Account account;

    public ActiveState(Account account) {
        this.account = account;
    }

    @Override
    public void deposit(double amount) {
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        System.out.println("存款成功，当前余额: " + newBalance);
    }

    @Override
    public void withdraw(double amount) {
        if (amount > account.getBalance()) {
            System.out.println("余额不足");
            return;
        }
        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
        System.out.println("取款成功，当前余额: " + newBalance);
    }

    @Override
    public void freeze() {
        System.out.println("账户已冻结");
        account.setState(new FrozenState(account));
    }

    @Override
    public void unfreeze() {
        System.out.println("账户已是正常状态");
    }

    @Override
    public void close() {
        if (account.getBalance() > 0) {
            System.out.println("账户有余额，不能注销");
            return;
        }
        System.out.println("账户已注销");
        account.setState(new ClosedState(account));
    }

    @Override
    public String getStateName() {
        return "正常";
    }
}

/**
 * 冻结状态
 */
class FrozenState implements AccountState {
    private Account account;

    public FrozenState(Account account) {
        this.account = account;
    }

    @Override
    public void deposit(double amount) {
        System.out.println("账户已冻结，无法存款");
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("账户已冻结，无法取款");
    }

    @Override
    public void freeze() {
        System.out.println("账户已是冻结状态");
    }

    @Override
    public void unfreeze() {
        System.out.println("账户已解冻");
        account.setState(new ActiveState(account));
    }

    @Override
    public void close() {
        System.out.println("账户已冻结，不能直接注销");
    }

    @Override
    public String getStateName() {
        return "冻结";
    }
}

/**
 * 注销状态
 */
class ClosedState implements AccountState {
    private Account account;

    public ClosedState(Account account) {
        this.account = account;
    }

    @Override
    public void deposit(double amount) {
        System.out.println("账户已注销，无法存款");
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("账户已注销，无法取款");
    }

    @Override
    public void freeze() {
        System.out.println("账户已注销");
    }

    @Override
    public void unfreeze() {
        System.out.println("账户已注销");
    }

    @Override
    public void close() {
        System.out.println("账户已是注销状态");
    }

    @Override
    public String getStateName() {
        return "已注销";
    }
}
