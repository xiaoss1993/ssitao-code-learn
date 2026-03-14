package com.ssitao.code.designpattern.chain.returnvalue;

/**
 * 带返回值的责任链模式示例
 *
 * 特点：
 * 1. 处理者可以返回处理结果
 * 2. 可以中断链条，不再继续传递
 * 3. 适用于需要获取处理结果的场景
 *
 * 场景：审批流程
 * - 用户提交报销申请
 * - 依次经过各级审批
 * - 某一级审批通过后返回结果，不再继续
 */
public class ChainWithReturnValueDemo {

    public static void main(String[] args) {
        // 构建审批链：主管 -> 部门经理 -> 总经理 -> CEO
        Approver manager = new Manager();
        Approver deptManager = new DeptManager();
        Approver generalManager = new GeneralManager();
        Approver ceo = new CEO();

        manager.setNext(deptManager);
        deptManager.setNext(generalManager);
        generalManager.setNext(ceo);

        // 测试不同金额的报销申请
        System.out.println("=== 测试报销申请 - 金额: 500 ===");
        ApprovalResult result1 = manager.approve(new ExpenseRequest(500, "团建活动"));
        System.out.println("审批结果: " + result1);

        System.out.println("\n=== 测试报销申请 - 金额: 5000 ===");
        ApprovalResult result2 = manager.approve(new ExpenseRequest(5000, "商务接待"));
        System.out.println("审批结果: " + result2);

        System.out.println("\n=== 测试报销申请 - 金额: 20000 ===");
        ApprovalResult result3 = manager.approve(new ExpenseRequest(20000, "设备采购"));
        System.out.println("审批结果: " + result3);

        System.out.println("\n=== 测试报销申请 - 金额: 100000 ===");
        ApprovalResult result4 = manager.approve(new ExpenseRequest(100000, "公司年会"));
        System.out.println("审批结果: " + result4);
    }
}

/**
 * 报销请求
 */
class ExpenseRequest {
    private double amount;
    private String description;
    private String currentApprover;

    public ExpenseRequest(double amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrentApprover() {
        return currentApprover;
    }

    public void setCurrentApprover(String currentApprover) {
        this.currentApprover = currentApprover;
    }

    @Override
    public String toString() {
        return "ExpenseRequest{" +
                "amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }
}

/**
 * 审批结果
 */
class ApprovalResult {
    private boolean approved;
    private String approver;
    private String message;

    public ApprovalResult(boolean approved, String approver, String message) {
        this.approved = approved;
        this.approver = approver;
        this.message = message;
    }

    public boolean isApproved() {
        return approved;
    }

    public String getApprover() {
        return approver;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "审批人: " + approver + ", 通过: " + approved + ", 消息: " + message;
    }
}

/**
 * 审批人抽象类
 */
abstract class Approver {
    private Approver next;

    public Approver(Approver next) {
        this.next = next;
    }

    public Approver setNext(Approver next) {
        this.next = next;
        return next;
    }

    /**
     * 审批请求
     * @return 审批结果，如果不能处理返回null继续传递
     */
    public ApprovalResult approve(ExpenseRequest request) {
        ApprovalResult result = doApprove(request);

        // 如果已经有结果（批准或拒绝），不再传递
        if (result != null) {
            return result;
        }

        // 传递给下一个审批人
        if (next != null) {
            System.out.println("  -> 传递给下一个审批人");
            return next.approve(request);
        }

        // 没有审批人可以处理
        return new ApprovalResult(false, "系统", "没有审批人");
    }

    /**
     * 子类实现具体审批逻辑
     * @return null表示不能处理，需要传递给下一个；非null表示处理完成
     */
    protected abstract ApprovalResult doApprove(ExpenseRequest request);

    protected abstract String getApproverName();
}

/**
 * 主管 - 审批金额 <= 1000
 */
class Manager extends Approver {

    public Manager() {
        super(null);
    }

    public Manager(Approver next) {
        super(next);
    }

    @Override
    protected ApprovalResult doApprove(ExpenseRequest request) {
        if (request.getAmount() <= 1000) {
            request.setCurrentApprover(getApproverName());
            System.out.println("主管审批通过: " + request);
            return new ApprovalResult(true, getApproverName(),
                    "金额较小，直接批准");
        }
        return null; // 不能处理，传递下去
    }

    @Override
    protected String getApproverName() {
        return "主管";
    }
}

/**
 * 部门经理 - 审批金额 <= 10000
 */
class DeptManager extends Approver {

    public DeptManager() {
        super(null);
    }

    public DeptManager(Approver next) {
        super(next);
    }

    @Override
    protected ApprovalResult doApprove(ExpenseRequest request) {
        if (request.getAmount() <= 10000) {
            request.setCurrentApprover(getApproverName());
            System.out.println("部门经理审批通过: " + request);
            return new ApprovalResult(true, getApproverName(),
                    "金额在部门权限内，批准");
        }
        return null;
    }

    @Override
    protected String getApproverName() {
        return "部门经理";
    }
}

/**
 * 总经理 - 审批金额 <= 50000
 */
class GeneralManager extends Approver {

    public GeneralManager() {
        super(null);
    }

    public GeneralManager(Approver next) {
        super(next);
    }

    @Override
    protected ApprovalResult doApprove(ExpenseRequest request) {
        if (request.getAmount() <= 50000) {
            request.setCurrentApprover(getApproverName());
            System.out.println("总经理审批通过: " + request);
            return new ApprovalResult(true, getApproverName(),
                    "需要总经理审批，批准");
        }
        return null;
    }

    @Override
    protected String getApproverName() {
        return "总经理";
    }
}

/**
 * CEO - 审批金额 > 50000
 */
class CEO extends Approver {

    public CEO() {
        super(null);
    }

    public CEO(Approver next) {
        super(next);
    }

    @Override
    protected ApprovalResult doApprove(ExpenseRequest request) {
        // CEO可以审批所有金额
        request.setCurrentApprover(getApproverName());
        System.out.println("CEO审批: " + request);

        if (request.getAmount() > 100000) {
            return new ApprovalResult(false, getApproverName(),
                    "金额超过CEO审批限额，拒绝");
        }
        return new ApprovalResult(true, getApproverName(),
                "CEO最终审批，通过");
    }

    @Override
    protected String getApproverName() {
        return "CEO";
    }
}
