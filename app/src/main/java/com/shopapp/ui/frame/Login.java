package com.shopapp.ui.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.shopapp.ui.components.Form;
import com.shopapp.ui.themes.*;
import com.shopapp.AppSys;
import com.shopapp.entity.NguoiDung;
import com.shopapp.repository.NguoiDungRepository;
import com.shopapp.repository.impl.NguoiDungRepositoryImpl;
import com.shopapp.service.NguoiDungService;
import com.shopapp.service.impl.NguoiDungServiceImpl;

import org.mindrot.jbcrypt.BCrypt;

public class Login extends JFrame {

    private static Form form;

    private final JTextField userTextField = new JTextField(25);
    private final JPasswordField passField = new JPasswordField(25);

    private JPanel buttonPanel;
    private JButton loginButton = new JButton("Login");

    Theme currentTheme = ThemeManager.getCurrentTheme();

    public Login() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(createLoginPanel());

        pack();
        setLocationRelativeTo(null);

        // Add action listener to login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLoginAttempt();
            }
        });

    }

    private JPanel createLoginPanel() {
        form = new Form();
        form.setTheme(Theme.LIGHT);
        userTextField.setSize(200, 30);
        passField.setSize(200, 100);
        JLabel userJLabel = new JLabel("Username:");
        JLabel passJLabel = new JLabel("Password:");
        userJLabel.setFont(ThemeManager.getFont(16));
        passJLabel.setFont(ThemeManager.getFont(16));
        form.addRow(userJLabel, userTextField);
        form.addRow(passJLabel, passField);

        buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        form.addRowCenter(buttonPanel);
        return form;
    }

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
                    SwingUtilities.invokeLater(() -> {
                        MainFrame mainFrame = new MainFrame();
                        mainFrame.setVisible(true);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login loginFrame = new Login();
            loginFrame.setVisible(true);
        });
    }
}
