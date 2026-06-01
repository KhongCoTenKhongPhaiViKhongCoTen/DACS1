package com.shopapp.ui.frame.panels.Dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.shopapp.entity.Quyen;
import com.shopapp.service.QuyenService;
import com.shopapp.ui.components.BaseForm;
import com.shopapp.ui.components.BaseDialog;
import com.shopapp.ui.themes.ThemeManager;

/**
 * Dialog dùng chung cho việc Thêm mới và Chỉnh sửa Quyền.
 * Hỗ trợ giao diện GridBagLayout, đồng bộ theme và kiểm tra dữ liệu đầu vào.
 */
public class QuyenDialog extends BaseDialog {

    // ── Services & entity ─────────────────────────────────────────────────────
    private final Quyen quyen; // null = Thêm mới, khác null = Sửa thông tin
    private final QuyenService quyenService;

    // ── Form fields ───────────────────────────────────────────────────────────
    private JTextField tfPermissionCode;
    private JTextField tfPermissionName;
    private JTextField tfModule;

    private boolean succeeded = false;

    public QuyenDialog(Frame owner, Quyen quyen, QuyenService quyenService) {
        super(owner, quyen == null ? "Thêm Quyền Mới" : "Cập Nhật Thông Tin Quyền");
        this.quyen = quyen;
        this.quyenService = quyenService;

        // Fields will be initialized in createForm()
        // Gọi SAU CÙNG — lúc này createForm() và fillData() mới được gọi
        buildAndShow();
    }

    // ── Tạo form ─────────────────────────────────────────────────────────────
    @Override
    protected JPanel createForm() {
        BaseForm form = new BaseForm();
        form.setBorder(new EmptyBorder(PADDING, PADDING + 5, PADDING, PADDING + 5));

        tfPermissionCode = createTextField();
        tfPermissionName = createTextField();
        tfModule = createTextField();

        form.addRow(new JLabel("Mã quyền *"), tfPermissionCode);
        form.addRow(new JLabel("Tên quyền *"), tfPermissionName);
        form.addRow(new JLabel("Module *"), tfModule);

        return form;
    }

    // ── Điền dữ liệu khi ở chế độ Sửa ───────────────────────────────────────
    @Override
    protected void fillData() {
        if (quyen == null) {
            return;
        }

        tfPermissionCode.setText(quyen.getPermissionCode());
        tfPermissionName.setText(quyen.getPermissionName());
        tfModule.setText(quyen.getModule());
    }

    // ── Xác thực tùy chỉnh ────────────────────────────────────────────────────
    @Override
    protected boolean applyCustomValidation() {
        String permissionCode = tfPermissionCode.getText().trim();
        String permissionName = tfPermissionName.getText().trim();
        String module = tfModule.getText().trim();

        // 1. Validation bắt buộc
        if (permissionCode.isEmpty() || permissionName.isEmpty() || module.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền đầy đủ các trường bắt buộc (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 2. Kiểm tra độ dài (tối thiểu và tối đa)
        if (permissionCode.length() < 2) {
            JOptionPane.showMessageDialog(this,
                    "Mã quyền phải có ít nhất 2 ký tự",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (permissionCode.length() > 50) {
            JOptionPane.showMessageDialog(this,
                    "Mã quyền không được vượt quá 50 ký tự",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (permissionName.length() < 2) {
            JOptionPane.showMessageDialog(this,
                    "Tên quyền phải có ít nhất 2 ký tự",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (permissionName.length() > 100) {
            JOptionPane.showMessageDialog(this,
                    "Tên quyền không được vượt quá 100 ký tự",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (module.length() < 2) {
            JOptionPane.showMessageDialog(this,
                    "Module phải có ít nhất 2 ký tự",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (module.length() > 50) {
            JOptionPane.showMessageDialog(this,
                    "Module không được vượt quá 50 ký tự",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 3. Kiểm tra trùng mã quyền
        if (quyen == null) { // Chỉ kiểm tra khi Thêm mới
            try {
                if (quyenService.existsByPermissionCode(permissionCode)) {
                    JOptionPane.showMessageDialog(this,
                            "Mã quyền đã tồn tại trong hệ thống!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi kiểm tra trùng mã quyền: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    // ── Xử lý lưu ────────────────────────────────────────────────────────────
    @Override
    protected void handleSave() {
        String permissionCode = tfPermissionCode.getText().trim();
        String permissionName = tfPermissionName.getText().trim();
        String module = tfModule.getText().trim();

        try {
            Quyen model = (quyen == null) ? new Quyen() : quyen;

            model.setPermissionCode(permissionCode);
            model.setPermissionName(permissionName);
            model.setModule(module);

            quyenService.save(model);

            JOptionPane.showMessageDialog(this,
                    quyen == null ? "Thêm quyền mới thành công!" : "Cập nhật thông tin thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            succeeded = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lưu dữ liệu quyền: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // ── Getter ───────────────────────────────────────────────────────────────
    public boolean isSucceeded() {
        return succeeded;
    }

    // ── Helper methods ───────────────────────────────────────────────────────
    /** Tạo JTextField với thông số chung */
    protected JTextField createTextField() {
        JTextField tf = new JTextField(FIELD_COLUMNS);
        tf.setFont(ThemeManager.getFont(FONT_FIELD));
        return tf;
    }
}