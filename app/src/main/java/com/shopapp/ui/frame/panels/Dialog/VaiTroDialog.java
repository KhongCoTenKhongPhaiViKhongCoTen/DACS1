package com.shopapp.ui.frame.panels.Dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.shopapp.entity.Vaitro;
import com.shopapp.service.VaitroService;
import com.shopapp.ui.components.BaseForm;
import com.shopapp.ui.components.BaseDialog;

/**
 * Dialog dùng chung cho việc Thêm mới và Chỉnh sửa Vai trò.
 * Hỗ trợ giao diện GridBagLayout, đồng bộ theme và kiểm tra dữ liệu đầu vào.
 */
public class VaiTroDialog extends BaseDialog {

    // ── Services & entity ─────────────────────────────────────────────────────
    private final Vaitro role; // null = Thêm mới, khác null = Sửa thông tin
    private final VaitroService roleService;

    // ── Form fields ───────────────────────────────────────────────────────────
    private JTextField tfRoleName;
    private JTextField tfDescription;

    // ── Constructor ───────────────────────────────────────────────────────────

    public VaiTroDialog(Frame owner, Vaitro role, VaitroService roleService) {
        super(owner, role == null ? "Thêm Vai Trò Mới" : "Cập Nhật Thông Tin Vai Trò");

        // Assign fields TRƯỚC khi gọi buildAndShow()
        this.role = role;
        this.roleService = roleService;

        // Gọi SAU CÙNG — lúc này createForm() và fillData() mới được gọi
        buildAndShow();
    }

    // ── Tạo form ──────────────────────────────────────────────────────────────

    @Override
    protected JPanel createForm() {
        BaseForm form = new BaseForm();
        form.setBorder(new EmptyBorder(PADDING, PADDING + 5, PADDING, PADDING + 5));

        tfRoleName = createTextField();
        tfDescription = createTextField();
        tfDescription.setColumns(35); // Mô tả thường dài hơn

        form.addRow(new JLabel("Tên vai trò *"), tfRoleName);
        form.addRow(new JLabel("Mô tả"), tfDescription);

        return form;
    }

    // ── Điền dữ liệu khi ở chế độ Sửa ───────────────────────────────────────

    @Override
    protected void fillData() {
        if (role == null)
            return;

        tfRoleName.setText(role.getRoleName());
        tfDescription.setText(role.getDescription() != null ? role.getDescription() : "");
    }

    // ── Xác thực tùy chỉnh ────────────────────────────────────────────────────

    @Override
    protected boolean applyCustomValidation() {
        String roleName = tfRoleName.getText().trim();

        if (roleName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập tên vai trò",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (roleName.length() < 2) {
            JOptionPane.showMessageDialog(this,
                    "Tên vai trò phải có ít nhất 2 ký tự",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    // ── Xử lý lưu ────────────────────────────────────────────────────────────

    @Override
    protected void handleSave() {
        String roleName = tfRoleName.getText().trim();
        String description = tfDescription.getText().trim();

        try {
            Vaitro model = (role == null) ? new Vaitro() : role;

            model.setRoleName(roleName);
            model.setDescription(description.isEmpty() ? null : description);

            roleService.save(model);

            JOptionPane.showMessageDialog(this,
                    role == null ? "Thêm vai trò mới thành công!" : "Cập nhật thông tin thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            succeeded = true; // field protected của BaseDialog — KHÔNG khai báo lại
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lưu dữ liệu vai trò: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}