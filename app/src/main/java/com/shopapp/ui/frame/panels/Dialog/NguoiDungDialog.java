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
import com.shopapp.ui.components.BaseDialog;
/**
 * Dialog dùng chung cho việc Thêm mới và Chỉnh sửa Người dùng.
 * Hỗ trợ giao diện GridBagLayout, đồng bộ theme và kiểm tra dữ liệu đầu vào.
 *
 * ⚠️ Lưu ý: gọi buildAndShow() ở cuối constructor SAU KHI assign hết fields,
 * theo yêu cầu của BaseDialog.
 */
public class NguoiDungDialog extends BaseDialog {

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
    private JComboBox<Vaitro> cbRole; // khởi tạo trong createForm()
    private JCheckBox chkActive;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * @param owner       Cửa sổ cha
     * @param user        null = Thêm mới, khác null = Sửa thông tin
     * @param userService Service xử lý NguoiDung
     * @param roleService Service xử lý Vaitro
     */
    public NguoiDungDialog(
            Frame owner,
            NguoiDung user,
            NguoiDungService userService,
            VaitroService roleService) {
        super(owner, user == null ? "Thêm Người Dùng Mới" : "Cập Nhật Thông Tin Người Dùng");

        // Assign fields TRƯỚC khi gọi buildAndShow()
        this.user = user;
        this.userService = userService;
        this.roleService = roleService;

        // Gọi SAU CÙNG — lúc này createForm() và fillData() mới được gọi
        buildAndShow();
    }

    // ── Tạo form ──────────────────────────────────────────────────────────────

    /**
     * Tạo các component form. Lúc này tất cả fields (user, roleService, ...)
     * đã được assign nên có thể dùng trực tiếp.
     */
    @Override
    protected JPanel createForm() {
        BaseForm form = new BaseForm();
        form.setBorder(new EmptyBorder(PADDING, PADDING + 5, PADDING, PADDING + 5));

        tfUsername = createTextField();
        pfPassword = createPasswordField();
        tfFullName = createTextField();
        tfEmail = createTextField();
        tfPhone = createTextField();

        cbRole = new JComboBox<>();
        cbRole.setFont(AppSys.themes.getFont(FONT_FIELD));

        chkActive = new JCheckBox("Hoạt động");
        chkActive.setSelected(true);
        chkActive.setFont(AppSys.themes.getFont(FONT_FIELD));

        form.addRow(new JLabel(user == null ? "Username *" : "Username"), tfUsername);
        form.addRow(new JLabel(user == null ? "Mật khẩu *" : "Mật khẩu mới"), pfPassword);
        form.addRow(new JLabel("Họ tên *"), tfFullName);
        form.addRow(new JLabel("Email"), tfEmail);
        form.addRow(new JLabel("Số điện thoại"), tfPhone);
        form.addRow(new JLabel("Vai trò *"), cbRole);
        form.addRow(new JLabel("Trạng thái"), chkActive);

        return form;
    }

    // ── Điền dữ liệu khi ở chế độ Sửa ───────────────────────────────────────

    /**
     * Populate cbRole từ roleService, sau đó điền dữ liệu entity (nếu ở chế độ
     * Sửa).
     * Được gọi bởi buildAndShow() SAU createForm(), nên cbRole đã tồn tại.
     */
    @Override
    protected void fillData() {
        // 1. Populate danh sách vai trò
        try {
            List<Vaitro> roles = roleService.findAll();
            for (Vaitro role : roles) {
                cbRole.addItem(role);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Chế độ Thêm mới → không cần điền thêm
        if (user == null)
            return;

        // 3. Chế độ Sửa → điền dữ liệu entity vào form
        tfUsername.setText(user.getUsername());
        tfUsername.setEnabled(false); // Không cho sửa Username

        tfFullName.setText(user.getFullName());
        tfEmail.setText(user.getEmail() != null ? user.getEmail() : "");
        tfPhone.setText(user.getPhone() != null ? user.getPhone() : "");
        chkActive.setSelected(user.getIsActive());

        // Chọn vai trò hiện tại trong combo box
        for (int i = 0; i < cbRole.getItemCount(); i++) {
            Vaitro r = cbRole.getItemAt(i);
            if (user.getRole() != null && r.getRoleId().equals(user.getRole().getRoleId())) {
                cbRole.setSelectedIndex(i);
                break;
            }
        }

        // Không cho tự khóa chính mình
        if (AppSys.getNguoiDung() != null
                && user.getUserId().equals(AppSys.getNguoiDung().getUserId())) {
            chkActive.setEnabled(false);
        }
    }

    // ── Xác thực tùy chỉnh ────────────────────────────────────────────────────

    @Override
    protected boolean applyCustomValidation() {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();
        String fullName = tfFullName.getText().trim();
        String email = tfEmail.getText().trim();
        String phone = tfPhone.getText().trim();
        Vaitro selectedRole = (Vaitro) cbRole.getSelectedItem();

        if (user == null) {
            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || selectedRole == null) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng điền đầy đủ các trường bắt buộc (*)",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } else {
            if (fullName.isEmpty() || selectedRole == null) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng điền đầy đủ các trường bắt buộc (*)",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        // 2. Kiểm tra độ dài & khoảng trắng
        if (user == null) {
            if (username.contains(" ")) {
                JOptionPane.showMessageDialog(this,
                        "Username không được chứa khoảng trắng",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (username.length() < 3) {
                JOptionPane.showMessageDialog(this,
                        "Username phải có ít nhất 3 ký tự",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this,
                        "Mật khẩu phải có ít nhất 6 ký tự",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } else {
            if (!password.isEmpty() && password.length() < 6) {
                JOptionPane.showMessageDialog(this,
                        "Mật khẩu mới phải có ít nhất 6 ký tự",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        // 3. Kiểm tra trùng Username (chỉ khi Thêm mới)
        if (user == null) {
            try {
                if (userService.existsByUsername(username)) {
                    JOptionPane.showMessageDialog(this,
                            "Tên tài khoản (Username) đã tồn tại trong hệ thống!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi kiểm tra trùng username: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // 4. Định dạng Email & SĐT
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this,
                    "Định dạng Email không hợp lệ",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!phone.isEmpty() && !phone.matches("^\\d{10,11}$")) {
            JOptionPane.showMessageDialog(this,
                    "Số điện thoại phải là số và có từ 10 đến 11 chữ số",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    // ── Xử lý lưu ────────────────────────────────────────────────────────────

    @Override
    protected void handleSave() {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();
        String fullName = tfFullName.getText().trim();
        String email = tfEmail.getText().trim();
        String phone = tfPhone.getText().trim();
        Vaitro selectedRole = (Vaitro) cbRole.getSelectedItem();

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

            if (chkActive.isEnabled()) {
                model.setIsActive(chkActive.isSelected());
            }

            userService.save(model);

            JOptionPane.showMessageDialog(this,
                    user == null ? "Thêm người dùng mới thành công!" : "Cập nhật thông tin thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            succeeded = true; // field của BaseDialog (protected)
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lưu dữ liệu người dùng: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // isSucceeded() kế thừa từ BaseDialog — KHÔNG khai báo lại ở đây
}