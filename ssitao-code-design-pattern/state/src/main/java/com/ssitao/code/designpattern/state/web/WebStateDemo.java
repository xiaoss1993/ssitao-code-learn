package com.ssitao.code.designpattern.state.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Web应用中的状态模式示例
 *
 * Web应用场景：
 * 1. 用户登录状态 - 未登录/已登录/管理员
 * 2. 订单状态机 - 订单流转
 * 3. 审批流程 - 审批状态流转
 * 4. 文件上传 - 上传中/成功/失败
 * 5. 支付状态 - 待支付/支付中/已支付/已取消
 */
public class WebStateDemo {

    public static void main(String[] args) {
        System.out.println("=== Web状态模式示例 ===\n");

        // 1. 用户登录状态
        System.out.println("1. 用户登录状态");
        userLoginDemo();

        // 2. 文件上传状态
        System.out.println("\n2. 文件上传状态");
        fileUploadDemo();

        // 3. 审批流程状态
        System.out.println("\n3. 审批流程状态");
        approvalDemo();
    }

    /**
     * 用户登录状态示例
     */
    private static void userLoginDemo() {
        UserSession session = new UserSession();

        // 未登录状态
        System.out.println("--- 当前状态 ---");
        System.out.println(session.getCurrentState());

        // 尝试访问需要登录的页面
        System.out.println("\n--- 访问首页 ---");
        session.accessPage("index");

        System.out.println("\n--- 尝试访问用户中心 ---");
        session.accessPage("user");

        // 登录
        System.out.println("\n--- 用户登录 ---");
        session.login("zhangsan");

        System.out.println("\n--- 访问用户中心 ---");
        session.accessPage("user");

        System.out.println("\n--- 尝试访问后台管理 ---");
        session.accessPage("admin");

        // 登出
        System.out.println("\n--- 用户登出 ---");
        session.logout();

        System.out.println("\n--- 再次访问用户中心 ---");
        session.accessPage("user");
    }

    /**
     * 文件上传状态示例
     */
    private static void fileUploadDemo() {
        FileUploader uploader = new FileUploader();

        // 初始状态
        System.out.println("--- 初始状态 ---");
        System.out.println("当前状态: " + uploader.getStatus());

        // 上传文件
        System.out.println("\n--- 上传文件 ---");
        uploader.selectFile("document.pdf");
        uploader.upload();

        // 模拟上传过程
        System.out.println("\n--- 上传中 ---");
        uploader.upload();

        System.out.println("\n--- 上传完成 ---");
        uploader.upload();

        // 再上传
        System.out.println("\n--- 再次上传 ---");
        uploader.reset();
        uploader.selectFile("image.jpg");
        uploader.upload();
    }

    /**
     * 审批流程示例
     */
    private static void approvalDemo() {
        ApprovalWorkflow workflow = new ApprovalWorkflow();

        // 提交审批
        System.out.println("--- 提交审批 ---");
        workflow.submit();

        // 主管审批
        System.out.println("\n--- 主管审批 ---");
        workflow.approveByManager();

        // 部门经理审批
        System.out.println("\n--- 部门经理审批 ---");
        workflow.approveByDeptManager();

        // 完成
        System.out.println("\n--- 最终结果 ---");
        workflow.complete();
    }
}

// ============================================
// 1. 用户登录状态相关类
// ============================================

/**
 * 用户会话
 */
class UserSession {
    private UserState state;
    private String username;
    private Map<String, UserState> stateMap = new HashMap<>();

    public UserSession() {
        // 初始化状态
        stateMap.put("GUEST", new GuestState(this));
        stateMap.put("USER", new LoggedInState(this));
        stateMap.put("ADMIN", new AdminState(this));
        state = stateMap.get("GUEST");
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public UserState getCurrentState() {
        return state;
    }

    public void login(String username) {
        state.login(username);
    }

    public void logout() {
        state.logout();
    }

    public void accessPage(String page) {
        state.accessPage(page);
    }
}

/**
 * 用户状态接口
 */
interface UserState {
    void login(String username);
    void logout();
    void accessPage(String page);
    String getStateName();
}

/**
 * 访客状态
 */
class GuestState implements UserState {
    private UserSession session;

    public GuestState(UserSession session) {
        this.session = session;
    }

    @Override
    public void login(String username) {
        session.setUsername(username);
        session.setState(new LoggedInState(session));
        System.out.println("用户 " + username + " 登录成功");
    }

    @Override
    public void logout() {
        System.out.println("用户未登录");
    }

    @Override
    public void accessPage(String page) {
        if ("index".equals(page) || "login".equals(page)) {
            System.out.println("允许访问: " + page);
        } else {
            System.out.println("拒绝访问: " + page + "，请先登录");
        }
    }

    @Override
    public String getStateName() {
        return "访客";
    }
}

/**
 * 普通用户状态
 */
class LoggedInState implements UserState {
    private UserSession session;

    public LoggedInState(UserSession session) {
        this.session = session;
    }

    @Override
    public void login(String username) {
        System.out.println("用户已登录");
    }

    @Override
    public void logout() {
        session.setUsername(null);
        session.setState(new GuestState(session));
        System.out.println("用户已登出");
    }

    @Override
    public void accessPage(String page) {
        if ("admin".equals(page)) {
            System.out.println("拒绝访问: " + page + "，需要管理员权限");
        } else {
            System.out.println("允许访问: " + page);
        }
    }

    @Override
    public String getStateName() {
        return "普通用户";
    }
}

/**
 * 管理员状态
 */
class AdminState implements UserState {
    private UserSession session;

    public AdminState(UserSession session) {
        this.session = session;
    }

    @Override
    public void login(String username) {
        System.out.println("管理员已登录");
    }

    @Override
    public void logout() {
        session.setUsername(null);
        session.setState(new GuestState(session));
        System.out.println("管理员已登出");
    }

    @Override
    public void accessPage(String page) {
        System.out.println("管理员允许访问所有页面: " + page);
    }

    @Override
    public String getStateName() {
        return "管理员";
    }
}

// ============================================
// 2. 文件上传状态相关类
// ============================================

/**
 * 文件上传器
 */
class FileUploader {
    private UploadState state;
    private String fileName;
    private Map<String, UploadState> stateMap = new HashMap<>();

    public FileUploader() {
        stateMap.put("IDLE", new IdleState(this));
        stateMap.put("UPLOADING", new UploadingState(this));
        stateMap.put("SUCCESS", new SuccessState(this));
        stateMap.put("ERROR", new ErrorState(this));
        state = stateMap.get("IDLE");
    }

    public void setState(UploadState state) {
        this.state = state;
    }

    public String getStatus() {
        return state.getStateName();
    }

    public void selectFile(String fileName) {
        this.fileName = fileName;
        System.out.println("选择了文件: " + fileName);
    }

    public void upload() {
        state.upload();
    }

    public void reset() {
        state = stateMap.get("IDLE");
        fileName = null;
        System.out.println("重置上传器");
    }
}

/**
 * 上传状态接口
 */
interface UploadState {
    void upload();
    String getStateName();
}

/**
 * 空闲状态
 */
class IdleState implements UploadState {
    private FileUploader uploader;

    public IdleState(FileUploader uploader) {
        this.uploader = uploader;
    }

    @Override
    public void upload() {
        System.out.println("开始上传...");
        uploader.setState(new UploadingState(uploader));
    }

    @Override
    public String getStateName() {
        return "空闲";
    }
}

/**
 * 上传中状态
 */
class UploadingState implements UploadState {
    private FileUploader uploader;

    public UploadingState(FileUploader uploader) {
        this.uploader = uploader;
    }

    @Override
    public void upload() {
        // 模拟上传
        System.out.println("上传中... 50%");
        System.out.println("上传中... 100%");
        uploader.setState(new SuccessState(uploader));
    }

    @Override
    public String getStateName() {
        return "上传中";
    }
}

/**
 * 上传成功状态
 */
class SuccessState implements UploadState {
    private FileUploader uploader;

    public SuccessState(FileUploader uploader) {
        this.uploader = uploader;
    }

    @Override
    public void upload() {
        System.out.println("文件已上传成功");
    }

    @Override
    public String getStateName() {
        return "上传成功";
    }
}

/**
 * 上传失败状态
 */
class ErrorState implements UploadState {
    private FileUploader uploader;

    public ErrorState(FileUploader uploader) {
        this.uploader = uploader;
    }

    @Override
    public void upload() {
        System.out.println("上传失败，请重试");
    }

    @Override
    public String getStateName() {
        return "上传失败";
    }
}

// ============================================
// 3. 审批流程相关类
// ============================================

/**
 * 审批工作流
 */
class ApprovalWorkflow {
    private ApprovalState state;
    private Map<String, ApprovalState> stateMap = new HashMap<>();

    public ApprovalWorkflow() {
        stateMap.put("DRAFT", new DraftState(this));
        stateMap.put("PENDING_MANAGER", new PendingManagerState(this));
        stateMap.put("PENDING_DEPT", new PendingDeptState(this));
        stateMap.put("APPROVED", new ApprovedState(this));
        stateMap.put("REJECTED", new RejectedState(this));
        state = stateMap.get("DRAFT");
    }

    public void setState(ApprovalState state) {
        this.state = state;
    }

    public void submit() {
        state.submit();
    }

    public void approveByManager() {
        state.approveByManager();
    }

    public void approveByDeptManager() {
        state.approveByDeptManager();
    }

    public void complete() {
        System.out.println("审批流程结束，最终状态: " + state.getStateName());
    }
}

/**
 * 审批状态接口
 */
interface ApprovalState {
    void submit();
    void approveByManager();
    void approveByDeptManager();
    String getStateName();
}

/**
 * 草稿状态
 */
class DraftState implements ApprovalState {
    private ApprovalWorkflow workflow;

    public DraftState(ApprovalWorkflow workflow) {
        this.workflow = workflow;
    }

    @Override
    public void submit() {
        System.out.println("提交审批申请");
        workflow.setState(new PendingManagerState(workflow));
    }

    @Override
    public void approveByManager() {
        System.out.println("请先提交审批");
    }

    @Override
    public void approveByDeptManager() {
        System.out.println("请先提交审批");
    }

    @Override
    public String getStateName() {
        return "草稿";
    }
}

/**
 * 待主管审批状态
 */
class PendingManagerState implements ApprovalState {
    private ApprovalWorkflow workflow;

    public PendingManagerState(ApprovalWorkflow workflow) {
        this.workflow = workflow;
    }

    @Override
    public void submit() {
        System.out.println("已提交，等待审批");
    }

    @Override
    public void approveByManager() {
        System.out.println("主管审批通过");
        workflow.setState(new PendingDeptState(workflow));
    }

    @Override
    public void approveByDeptManager() {
        System.out.println("等待主管先审批");
    }

    @Override
    public String getStateName() {
        return "待主管审批";
    }
}

/**
 * 待部门经理审批状态
 */
class PendingDeptState implements ApprovalState {
    private ApprovalWorkflow workflow;

    public PendingDeptState(ApprovalWorkflow workflow) {
        this.workflow = workflow;
    }

    @Override
    public void submit() {
        System.out.println("已提交，等待审批");
    }

    @Override
    public void approveByManager() {
        System.out.println("主管已审批");
    }

    @Override
    public void approveByDeptManager() {
        System.out.println("部门经理审批通过");
        workflow.setState(new ApprovedState(workflow));
    }

    @Override
    public String getStateName() {
        return "待部门经理审批";
    }
}

/**
 * 已通过状态
 */
class ApprovedState implements ApprovalState {
    private ApprovalWorkflow workflow;

    public ApprovedState(ApprovalWorkflow workflow) {
        this.workflow = workflow;
    }

    @Override
    public void submit() {
        System.out.println("审批已通过");
    }

    @Override
    public void approveByManager() {
        System.out.println("审批已通过");
    }

    @Override
    public void approveByDeptManager() {
        System.out.println("审批已通过");
    }

    @Override
    public String getStateName() {
        return "已通过";
    }
}

/**
 * 已拒绝状态
 */
class RejectedState implements ApprovalState {
    private ApprovalWorkflow workflow;

    public RejectedState(ApprovalWorkflow workflow) {
        this.workflow = workflow;
    }

    @Override
    public void submit() {
        System.out.println("审批已拒绝，无法再次提交");
    }

    @Override
    public void approveByManager() {
        System.out.println("审批已拒绝");
    }

    @Override
    public void approveByDeptManager() {
        System.out.println("审批已拒绝");
    }

    @Override
    public String getStateName() {
        return "已拒绝";
    }
}
