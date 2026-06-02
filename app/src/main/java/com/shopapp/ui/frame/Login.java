package com.shopapp.ui.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.shopapp.ui.components.BaseForm;
import com.shopapp.ui.themes.*;
import com.shopapp.AppSys;
import com.shopapp.entity.NguoiDung;
import com.shopapp.repository.NguoiDungRepository;
import com.shopapp.repository.impl.NguoiDungRepositoryImpl;
import com.shopapp.service.NguoiDungService;
import com.shopapp.service.impl.NguoiDungServiceImpl;
import com.shopapp.util.AutoLoginManager;
import org.mindrot.jbcrypt.BCrypt;

public class Login extends JFrame {

    private static BaseForm form;

    private final JTextField userTextField = new JTextField(25);
    private final JPasswordField passField = new JPasswordField(25);

    private JCheckBox rememberMeCheckbox = new JCheckBox("Nhớ thông tin đăng nhập");
    private JPanel buttonPanel;
    private JButton loginButton = new JButton("Login");

    Theme currentTheme = ThemeManager.getCurrentTheme();

    public Login() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Check auto login trước, nếu thành công thì không hiện form
        if (tryAutoLogin())
            return;

        add(createLoginPanel());
        pack();
        setLocationRelativeTo(null);
        appActionListener();
        setVisible(true);
    }

    // ==================== AUTO LOGIN ====================

    private boolean tryAutoLogin() {
        if (!AutoLoginManager.isAutoLoginAvailable())
            return false;

        String username = AutoLoginManager.getUsername();
        if (username == null)
            return false;

        try {
            NguoiDungRepository userRepository = new NguoiDungRepositoryImpl();
            NguoiDungService userService = new NguoiDungServiceImpl(userRepository);
            Optional<NguoiDung> optionalUser = userService.findByUsername(username);

            if (optionalUser.isPresent()) {
                AppSys.setNguoiDung(optionalUser.get());
                SwingUtilities.invokeLater(() -> {
                    new MainFrame().setVisible(true);
                });
                this.dispose();
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Không tìm thấy user → xóa credentials cũ
        AutoLoginManager.clear();
        return false;
    }

    // ==================== UI ====================

    private void appActionListener() {
        userTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passField.requestFocusInWindow();
            }
        });

        passField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLoginAttempt();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLoginAttempt();
            }
        });
    }

    private JPanel createLoginPanel() {
        form = new BaseForm();
        form.setTheme(Theme.LIGHT);
        userTextField.setSize(200, 30);
        passField.setSize(200, 100);

        JLabel userJLabel = new JLabel("Username:");
        JLabel passJLabel = new JLabel("Password:");
        userJLabel.setFont(ThemeManager.getFont(16));
        passJLabel.setFont(ThemeManager.getFont(16));

        rememberMeCheckbox.setSelected(AutoLoginManager.hasSavedCredentials());

        form.addRow(userJLabel, userTextField);
        form.addRow(passJLabel, passField);
        form.addRowCenter(rememberMeCheckbox);

        buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        form.addRowCenter(buttonPanel);

        return form;
    }

    // ==================== LOGIN ====================

    private void handleLoginAttempt() {
        String username = userTextField.getText().trim();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password.",
                    "Login Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            NguoiDungRepository userRepository = new NguoiDungRepositoryImpl();
            NguoiDungService userService = new NguoiDungServiceImpl(userRepository);
            Optional<NguoiDung> optionalUser = userService.findByUsername(username);

            if (optionalUser.isPresent()) {
                NguoiDung user = optionalUser.get();

                if (BCrypt.checkpw(password, user.getPasswordHash())) {
                    AppSys.setNguoiDung(user);

                    if (rememberMeCheckbox.isSelected()) {
                        // Chỉ lưu username, không cần token vì dùng SQLite local
                        AutoLoginManager.save(user.getUsername(), user.getUsername());
                    } else {
                        AutoLoginManager.clear();
                    }

                    SwingUtilities.invokeLater(() -> {
                        new MainFrame().setVisible(true);
                    });

                    clearLoginFields();
                    this.dispose();

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.",
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Login error: " + ex.getMessage(),
                    "System Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        form.setTheme(currentTheme);
    }

    private void clearLoginFields() {
        userTextField.setText("");
        passField.setText("");
    }

}