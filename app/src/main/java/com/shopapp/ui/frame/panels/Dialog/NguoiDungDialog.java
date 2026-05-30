package com.shopapp.ui.frame.panels.Dialog;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.mindrot.jbcrypt.BCrypt;

import com.shopapp.AppSys;
import com.shopapp.entity.NguoiDung;
import com.shopapp.entity.Vaitro;
import com.shopapp.service.NguoiDungService;
import com.shopapp.service.VaitroService;
import com.shopapp.ui.components.BaseForm;
import com.shopapp.ui.themes.Theme;
import com.shopapp.ui.themes.ThemeManager;

/**
 * Dialog dùng chung cho việc Thêm mới và Chỉnh sửa Người dùng.
 * Hỗ trợ giao diện GridBagLayout, đồng bộ theme và kiểm tra dữ liệu đầu vào.
 */
public class NguoiDungDialog extends JDialog {

    // ── Hằng số font/size dùng chung ──────────────────────────────────────────
    private static final int FIELD_COLUMNS = 28;
    private static final int FONT_FIELD = 18;
    private static final int FONT_LABEL = 18;
    private static final int FONT_TITLE = 22;
    private static final int FONT_BUTTON = 14;
    private static final int PADDING = 10;

    // ── Services & entity ─────────────────────────────────────────────────────
    private final NguoiDung user; // null = Thêm mới, khác null = Sửa thông tin
    private final NguoiDungService userService;
    private final VaitroService roleService;

    // ── Form fields ───────────────────────────────────────────────────────────
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JTextField tfFullName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JComboBox<Vaitro> cbRole;
    private JCheckBox chkActive;

    // ── Layout components ─────────────────────────────────────────────────────
    private JButton btnSave;
    private JButton btnCancel;
    private JLabel titleLabel;
    private JPanel headerPanel;
    private JPanel buttonPanel;

    private boolean succeeded = false;

    public NguoiDungDialog(Frame owner, NguoiDung user, NguoiDungService userService, VaitroService roleService) {
        super(owner, user == null ? "Thêm Người Dùng Mới" : "Cập Nhật Thông Tin Người Dùng", true);
        this.user = user;
        this.userService = userService;
        this.roleService = roleService;

        initializeUI();
        fillData();
        applyCurrentTheme();

        pack();
        setLocationRelativeTo(owner);
    }

    // ── Khởi tạo giao diện ────────────────────────────────────────────────────

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Header
        headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titleLabel = new JLabel(user == null ? "THÊM NGƯỜI DÙNG MỚI" : "CẬP NHẬT THÔNG TIN NGƯỜI DÙNG");
        titleLabel.setFont(ThemeManager.getBoldFont(FONT_TITLE));
        headerPanel.add(titleLabel);

        // Form
        BaseForm form = new BaseForm();
        form.setBorder(new EmptyBorder(PADDING, PADDING + 5, PADDING, PADDING + 5));

        tfUsername = createTextField();
        pfPassword = createPasswordField();
        tfFullName = createTextField();
        tfEmail = createTextField();
        tfPhone = createTextField();

        cbRole = new JComboBox<>();
        cbRole.setFont(ThemeManager.getFont(FONT_FIELD));
        try {
            List<Vaitro> roles = roleService.findAll();
            for (Vaitro role : roles)
                cbRole.addItem(role);
        } catch (Exception e) {
            e.printStackTrace();
        }

        chkActive = new JCheckBox("Hoạt động");
        chkActive.setSelected(true);
        chkActive.setFont(ThemeManager.getFont(FONT_FIELD));

        form.addRow(new JLabel(user == null ? "Username *" : "Username"), tfUsername);
        form.addRow(new JLabel(user == null ? "Mật khẩu *" : "Mật khẩu mới"), pfPassword);
        form.addRow(new JLabel("Họ tên *"), tfFullName);
        form.addRow(new JLabel("Email"), tfEmail);
        form.addRow(new JLabel("Số điện thoại"), tfPhone);
        form.addRow(new JLabel("Vai trò *"), cbRole);
        form.addRow(new JLabel("Trạng thái"), chkActive);

        // Buttons
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(headerPanel, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> handleSave());
    }

    /** Tạo JTextField với thông số chung */
    private JTextField createTextField() {
        JTextField tf = new JTextField(FIELD_COLUMNS);
        tf.setFont(ThemeManager.getFont(FONT_FIELD));
        return tf;
    }

    /** Tạo JPasswordField với thông số chung */
    private JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField(FIELD_COLUMNS);
        pf.setFont(ThemeManager.getFont(FONT_FIELD));
        return pf;
    }

    // ── Điền dữ liệu khi ở chế độ Sửa ───────────────────────────────────────

    private void fillData() {
        if (user == null)
            return;

        tfUsername.setText(user.getUsername());
        tfUsername.setEnabled(false); // Không cho sửa Username

        tfFullName.setText(user.getFullName());
        tfEmail.setText(user.getEmail() != null ? user.getEmail() : "");
        tfPhone.setText(user.getPhone() != null ? user.getPhone() : "");
        chkActive.setSelected(user.getIsActive());

        for (int i = 0; i < cbRole.getItemCount(); i++) {
            Vaitro r = cbRole.getItemAt(i);
            if (user.getRole() != null && r.getRoleId().equals(user.getRole().getRoleId())) {
                cbRole.setSelectedIndex(i);
                break;
            }
        }

        // Không cho tự khóa chính mình
        if (AppSys.getNguoiDung() != null && user.getUserId().equals(AppSys.getNguoiDung().getUserId())) {
            chkActive.setEnabled(false);
        }
    }

    // ── Xử lý lưu ────────────────────────────────────────────────────────────

    private void handleSave() {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();
        String fullName = tfFullName.getText().trim();
        String email = tfEmail.getText().trim();
        String phone = tfPhone.getText().trim();
        Vaitro selectedRole = (Vaitro) cbRole.getSelectedItem();

        // 1. Validation bắt buộc
        if (user == null) {
            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || selectedRole == null) {
                showWarning("Vui lòng điền đầy đủ các trường bắt buộc (*)");
                return;
            }
        } else {
            if (fullName.isEmpty() || selectedRole == null) {
                showWarning("Vui lòng điền đầy đủ các trường bắt buộc (*)");
                return;
            }
        }

        // 2. Kiểm tra độ dài & khoảng trắng
        if (user == null) {
            if (username.contains(" ")) {
                showWarning("Username không được chứa khoảng trắng");
                return;
            }
            if (username.length() < 3) {
                showWarning("Username phải có ít nhất 3 ký tự");
                return;
            }
            if (password.length() < 6) {
                showWarning("Mật khẩu phải có ít nhất 6 ký tự");
                return;
            }
        } else {
            if (!password.isEmpty() && password.length() < 6) {
                showWarning("Mật khẩu mới phải có ít nhất 6 ký tự");
                return;
            }
        }

        // 3. Kiểm tra trùng Username
        if (user == null) {
            try {
                if (userService.existsByUsername(username)) {
                    showError("Tên tài khoản (Username) đã tồn tại trong hệ thống!");
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // 4. Định dạng Email & SĐT
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showWarning("Định dạng Email không hợp lệ");
            return;
        }
        if (!phone.isEmpty() && !phone.matches("^\\d{10,11}$")) {
            showWarning("Số điện thoại phải là số và có từ 10 đến 11 chữ số");
            return;
        }

        // 5. Lưu dữ liệu
        try {
            NguoiDung model = (user == null) ? new NguoiDung() : user;

            if (user == null) {
                model.setUsername(username);
                model.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt(10)));
                model.setCreatedAt(LocalDateTime.now());
            } else if (!password.isEmpty()) {
                model.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt(10)));
            }

            model.setFullName(fullName);
            model.setEmail(email.isEmpty() ? null : email);
            model.setPhone(phone.isEmpty() ? null : phone);
            model.setRole(selectedRole);

            if (chkActive.isEnabled())
                model.setIsActive(chkActive.isSelected());

            userService.save(model);

            JOptionPane.showMessageDialog(this,
                    user == null ? "Thêm người dùng mới thành công!" : "Cập nhật thông tin thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            succeeded = true;
            dispose();
        } catch (Exception ex) {
            showError("Lỗi khi lưu dữ liệu người dùng: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // ── Helper hiển thị thông báo ─────────────────────────────────────────────

    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    // ── Theme ─────────────────────────────────────────────────────────────────

    public boolean isSucceeded() {
        return succeeded;
    }

    private void applyCurrentTheme() {
        Theme theme = ThemeManager.getCurrentTheme();
        styleComponents(this, theme);
        headerPanel.setBackground(theme.background);
        buttonPanel.setBackground(theme.background);
        titleLabel.setForeground(theme.textPrimary);
    }

    private void styleComponents(Container container, Theme theme) {
        container.setBackground(theme.background);
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel label) {
                label.setForeground(theme.textPrimary);
                label.setFont(ThemeManager.getFont(FONT_LABEL));
            } else if (comp instanceof JPasswordField pf) {
                // Check JPasswordField TRƯỚC JTextField vì nó là subclass
                pf.setBackground(theme.buttonBackground);
                pf.setForeground(theme.textPrimary);
                pf.setCaretColor(theme.textPrimary);
                pf.setFont(ThemeManager.getFont(FONT_FIELD));
            } else if (comp instanceof JTextField tf) {
                tf.setBackground(theme.buttonBackground);
                tf.setForeground(theme.textPrimary);
                tf.setCaretColor(theme.textPrimary);
                tf.setFont(ThemeManager.getFont(FONT_FIELD));
            } else if (comp instanceof JComboBox<?> cb) {
                cb.setBackground(theme.buttonBackground);
                cb.setForeground(theme.textPrimary);
                cb.setFont(ThemeManager.getFont(FONT_FIELD));
            } else if (comp instanceof JCheckBox chk) {
                chk.setOpaque(false);
                chk.setForeground(theme.textPrimary);
                chk.setFont(ThemeManager.getFont(FONT_FIELD));
            } else if (comp instanceof JButton btn) {
                btn.setBackground(theme.buttonBackground);
                btn.setForeground(theme.buttonForeground);
                btn.setFont(ThemeManager.getBoldFont(FONT_BUTTON));
                btn.setFocusPainted(false);
            } else if (comp instanceof Container childContainer) {
                styleComponents(childContainer, theme);
            }
        }
    }
}
