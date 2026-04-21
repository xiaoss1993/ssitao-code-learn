package com.ssitao.code.designpattern.mediator.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI中介者模式示例
 *
 * 模拟Swing/AWT中的中介者模式应用：
 * - JButton, JTextField, JLabel等组件不直接交互
 * - 通过中介者（Dialog/Frame）协调组件之间的交互
 *
 * 实际应用：
 * - Swing: JDialog, JFrame作为中介者
 * - SWT: Composite作为中介者
 * - Vaadin: UI作为中介者
 */
public class GuiMediatorDemo {

    public static void main(String[] args) {
        System.out.println("=== GUI中介者模式示例 ===\n");

        // 创建登录对话框中介者
        LoginDialogMediator dialog = new LoginDialogMediator();

        // 创建UI组件
        TextField usernameField = new TextField("username", dialog);
        TextField passwordField = new TextField("password", dialog);
        Button loginButton = new Button("登录", dialog);
        Button cancelButton = new Button("取消", dialog);
        Label statusLabel = new Label("请登录", dialog);

        // 设置组件关系
        dialog.setUsernameField(usernameField);
        dialog.setPasswordField(passwordField);
        dialog.setLoginButton(loginButton);
        dialog.setCancelButton(cancelButton);
        dialog.setStatusLabel(statusLabel);

        // 模拟用户操作
        System.out.println("--- 场景1: 用户输入用户名 ---");
        usernameField.setText("admin");
        usernameField.onChange();

        System.out.println("\n--- 场景2: 用户输入密码 ---");
        passwordField.setText("123456");
        passwordField.onChange();

        System.out.println("\n--- 场景3: 点击登录按钮 ---");
        loginButton.click();

        System.out.println("\n--- 场景4: 点击取消按钮 ---");
        cancelButton.click();

        System.out.println("\n--- 场景5: 用户清空用户名 ---");
        usernameField.setText("");
        usernameField.onChange();
    }
}

/**
 * UI组件中介者接口
 */
interface Mediator {
    void componentChanged(Component component);
}

/**
 * UI组件基类
 */
abstract class Component {
    protected Mediator mediator;
    protected String name;

    public Component(String name, Mediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }

    public String getName() {
        return name;
    }
}

/**
 * 文本框组件
 */
class TextField extends Component {
    private String text = "";

    public TextField(String name, Mediator mediator) {
        super(name, mediator);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        System.out.println("[" + name + "] 文本改变: " + text);
    }

    public void onChange() {
        System.out.println("  -> 通知中介者");
        mediator.componentChanged(this);
    }
}

/**
 * 按钮组件
 */
class Button extends Component {
    private boolean enabled = true;

    public Button(String name, Mediator mediator) {
        super(name, mediator);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        System.out.println("[" + name + "] 状态: " + (enabled ? "启用" : "禁用"));
    }

    public void click() {
        if (enabled) {
            System.out.println("[" + name + "] 被点击");
            System.out.println("  -> 通知中介者");
            mediator.componentChanged(this);
        } else {
            System.out.println("[" + name + "] 被禁用，无法点击");
        }
    }
}

/**
 * 标签组件
 */
class Label extends Component {
    private String text = "";

    public Label(String name, Mediator mediator) {
        super(name, mediator);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        System.out.println("[" + name + "] 显示: " + text);
    }
}

/**
 * 具体中介者 - 登录对话框
 */
class LoginDialogMediator implements Mediator {
    private TextField usernameField;
    private TextField passwordField;
    private Button loginButton;
    private Button cancelButton;
    private Label statusLabel;

    public void setUsernameField(TextField usernameField) {
        this.usernameField = usernameField;
    }

    public void setPasswordField(TextField passwordField) {
        this.passwordField = passwordField;
    }

    public void setLoginButton(Button loginButton) {
        this.loginButton = loginButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    @Override
    public void componentChanged(Component component) {
        if (component instanceof TextField) {
            handleTextFieldChange((TextField) component);
        } else if (component instanceof Button) {
            handleButtonClick((Button) component);
        }
    }

    private void handleTextFieldChange(TextField field) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // 用户名和密码都不为空时，登录按钮可用
        boolean canLogin = !username.isEmpty() && !password.isEmpty();
        loginButton.setEnabled(canLogin);

        if (username.isEmpty()) {
            statusLabel.setText("请输入用户名");
        } else if (password.isEmpty()) {
            statusLabel.setText("请输入密码");
        } else {
            statusLabel.setText("可以登录");
        }
    }

    private void handleButtonClick(Button button) {
        if (button.getName().equals("登录")) {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // 模拟登录验证
            if ("admin".equals(username) && "123456".equals(password)) {
                statusLabel.setText("登录成功！");
                loginButton.setEnabled(false);
            } else {
                statusLabel.setText("用户名或密码错误！");
            }
        } else if (button.getName().equals("取消")) {
            usernameField.setText("");
            passwordField.setText("");
            statusLabel.setText("已取消");
            loginButton.setEnabled(false);
        }
    }
}
