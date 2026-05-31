package com.shopapp.ui.frame.panels.Dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.shopapp.entity.DanhMuc;
import com.shopapp.service.DanhMucService;
import com.shopapp.ui.components.BaseForm;
import com.shopapp.ui.components.BaseDialog;

/**
 * Dialog dùng chung cho việc Thêm mới và Chỉnh sửa Danh Muc.
 * Hỗ trợ giao diện GridBagLayout, đồng bộ theme và kiểm tra dữ liệu đầu vào.
 *
 * ⚠️ Lưu ý: gọi buildAndShow() ở cuối constructor SAU KHI assign hết fields,
 * theo yêu cầu của BaseDialog.
 */
public class DanhMucDialog extends BaseDialog {

    // ── Services & entity ─────────────────────────────────────────────────────
    private final DanhMuc danhMuc; // null = Thêm mới, khác null = Sửa thông tin
    private final DanhMucService danhMucService;

    // ── Form fields ───────────────────────────────────────────────────────────
    private JTextField tfCategoryName;
    private JTextArea taDescription;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * @param owner       Cửa sổ cha
     * @param danhMuc     null = Thêm mới, khác null = Sửa thông tin
     * @param danhMucService Service xử lý DanhMuc
     */
    public DanhMucDialog(
            Frame owner,
            DanhMuc danhMuc,
            DanhMucService danhMucService) {
        super(owner, danhMuc == null ? "Thêm Danh Muc Mới" : "Cập Nhật Thông Tin Danh Muc");

        // Assign fields TRƯỚC khi gọi buildAndShow()
        this.danhMuc = danhMuc;
        this.danhMucService = danhMucService;

        // Gọi SAU CÙNG — lúc này createForm() và fillData() mới được gọi
        buildAndShow();
    }

    // ── Tạo form ──────────────────────────────────────────────────────────

    /**
     * Tạo các component form. Lúc này tất cả fields (danhMuc, danhMucService, ...)
     * đã được assign nên có thể dùng trực tiếp.
     */
    @Override
    protected JPanel createForm() {
        BaseForm form = new BaseForm();
        form.setBorder(new EmptyBorder(PADDING, PADDING + 5, PADDING, PADDING + 5));

        tfCategoryName = createTextField();
        taDescription = new JTextArea(3, 20);
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(taDescription);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        form.addRow(new JLabel("Tên danh mục *"), tfCategoryName);
        form.addRow(new JLabel("Mô tả"), scrollPane);

        return form;
    }

    // ── Điền dữ liệu khi ở chế độ Sửa ───────────────────────────────────────

    /**
     * Điền dữ liệu entity vào form (nếu ở chế độ Sửa).
     * Được gọi bởi buildAndShow() SAU createForm(), nên các fields đã tồn tại.
     */
    @Override
    protected void fillData() {
        // Chế độ Thêm mới → không cần điền thêm
        if (danhMuc == null)
            return;

        // Chế độ Sửa → điền dữ liệu entity vào form
        tfCategoryName.setText(danhMuc.getCategoryName());
        taDescription.setText(danhMuc.getDescription() != null ? danhMuc.getDescription() : "");
    }

    // ── Xác thực tùy chỉnh ────────────────────────────────────────────────

    @Override
    protected boolean applyCustomValidation() {
        String categoryName = tfCategoryName.getText().trim();

        if (categoryName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền tên danh mục (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra độ dài & khoảng trắng
        if (categoryName.length() < 2) {
            JOptionPane.showMessageDialog(this,
                    "Tên danh mục phải có ít nhất 2 ký tự",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra trùng Tên danh mục (chỉ khi Thêm mới)
        if (danhMuc == null) {
            try {
                if (danhMucService.existsByCategoryName(categoryName)) {
                    JOptionPane.showMessageDialog(this,
                            "Tên danh mục đã tồn tại trong hệ thống!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi kiểm tra trùng tên danh mục: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    // ── Xử lý lưu ────────────────────────────────────────────────────────

    @Override
    protected void handleSave() {
        String categoryName = tfCategoryName.getText().trim();
        String description = taDescription.getText().trim();

        try {
            DanhMuc model = (danhMuc == null) ? new DanhMuc() : danhMuc;

            model.setCategoryName(categoryName);
            model.setDescription(description.isEmpty() ? null : description);

            danhMucService.save(model);

            JOptionPane.showMessageDialog(this,
                    danhMuc == null ? "Thêm danh mục mới thành công!" : "Cập nhật thông tin thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            succeeded = true; // field của BaseDialog (protected)
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lưu dữ liệu danh mục: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // isSucceeded() kế thừa từ BaseDialog — KHÔNG khai báo lại ở đây
}