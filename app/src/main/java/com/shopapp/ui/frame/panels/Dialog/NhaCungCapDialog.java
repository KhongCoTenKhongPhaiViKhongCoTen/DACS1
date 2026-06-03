package com.shopapp.ui.frame.panels.Dialog;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.shopapp.entity.NhaCungCap;
import com.shopapp.service.NhaCungCapService;
import com.shopapp.ui.components.BaseForm;
import com.shopapp.ui.components.BaseDialog;

/**
 * Dialog dùng chung cho việc Thêm mới và Chỉnh sửa Nha Cung Cap.
 * Hỗ trợ giao diện GridBagLayout, đồng bộ theme và kiểm tra dữ liệu đầu vào.
 *
 * ⚠️ Lưu ý: gọi buildAndShow() ở cuối constructor SAU KHI assign hết fields,
 * theo yêu cầu của BaseDialog.
 */
public class NhaCungCapDialog extends BaseDialog {

    // ── Services & entity ─────────────────────────────────────────────────────
    private final NhaCungCap nhaCungCap; // null = Thêm mới, khác null = Sửa thông tin
    private final NhaCungCapService nhaCungCapService;

    // ── Form fields ───────────────────────────────────────────────────────────
    private JTextField tfCompanyName;
    private JTextField tfContactName;
    private JTextField tfPhone;
    private JTextField tfEmail;
    private JTextArea taAddress;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * @param owner       Cửa sổ cha
     * @param nhaCungCap  null = Thêm mới, khác null = Sửa thông tin
     * @param nhaCungCapService Service xử lý NhaCungCap
     */
    public NhaCungCapDialog(
            Frame owner,
            NhaCungCap nhaCungCap,
            NhaCungCapService nhaCungCapService) {
        super(owner, nhaCungCap == null ? "Thêm Nha Cung Cap Mới" : "Cập Nhật Thông Tin Nha Cung Cap");

        // Assign fields TRƯỚC khi gọi buildAndShow()
        this.nhaCungCap = nhaCungCap;
        this.nhaCungCapService = nhaCungCapService;

        // Gọi SAU CÙNG — lúc này createForm() và fillData() mới được gọi
        buildAndShow();
    }

    // ── Tạo form ──────────────────────────────────────────────────────────

    /**
     * Tạo các component form. Lúc này tất cả fields (nhaCungCap, nhaCungCapService, ...)
     * đã được assign nên có thể dùng trực tiếp.
     */
    @Override
    protected JPanel createForm() {
        BaseForm form = new BaseForm();
        form.setBorder(new EmptyBorder(PADDING, PADDING + 5, PADDING, PADDING + 5));

        tfCompanyName = createTextField();
        tfContactName = createTextField();
        tfPhone = createTextField();
        tfEmail = createTextField();
        taAddress = new JTextArea(3, 20);
        taAddress.setLineWrap(true);
        taAddress.setWrapStyleWord(true);
        JScrollPane addressScrollPane = new JScrollPane(taAddress);
        addressScrollPane.setBorder(BorderFactory.createEmptyBorder());

        form.addRow(new JLabel("Tên nhà cung cấp *"), tfCompanyName);
        form.addRow(new JLabel("Người liên hệ"), tfContactName);
        form.addRow(new JLabel("Số điện thoại"), tfPhone);
        form.addRow(new JLabel("Email"), tfEmail);
        form.addRow(new JLabel("Địa chỉ"), addressScrollPane);

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
        if (nhaCungCap == null)
            return;

        // Chế độ Sửa → điền dữ liệu entity vào form
        tfCompanyName.setText(nhaCungCap.getCompanyName());
        tfContactName.setText(nhaCungCap.getContactName() != null ? nhaCungCap.getContactName() : "");
        tfPhone.setText(nhaCungCap.getPhone() != null ? nhaCungCap.getPhone() : "");
        tfEmail.setText(nhaCungCap.getEmail() != null ? nhaCungCap.getEmail() : "");
        taAddress.setText(nhaCungCap.getAddress() != null ? nhaCungCap.getAddress() : "");
    }

    // ── Xác thực tùy chỉnh ────────────────────────────────────────────────

    @Override
    protected boolean applyCustomValidation() {
        String companyName = tfCompanyName.getText().trim();
        String phone = tfPhone.getText().trim();
        String email = tfEmail.getText().trim();

        if (companyName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền tên nhà cung cấp (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra độ dài & khoảng trắng
        if (companyName.length() < 2) {
            JOptionPane.showMessageDialog(this,
                    "Tên nhà cung cấp phải có ít nhất 2 ký tự",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra trùng Tên nhà cung cấp (chỉ khi Thêm mới)
        if (nhaCungCap == null) {
            try {
                if (nhaCungCapService.existsByCompanyName(companyName)) {
                    JOptionPane.showMessageDialog(this,
                            "Tên nhà cung cấp đã tồn tại trong hệ thống!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi kiểm tra trùng tên nhà cung cấp: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // Kiểm tra định dạng email ( nếu có nhập )
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this,
                    "Định dạng Email không hợp lệ",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra định dạng số điện thoại ( nếu có nhập )
        if (!phone.isEmpty() && !phone.matches("^\\d{10,11}$")) {
            JOptionPane.showMessageDialog(this,
                    "Số điện thoại phải là số và có từ 10 đến 11 chữ số",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    // ── Xử lý lưu ────────────────────────────────────────────────────────

    @Override
    protected void handleSave() {
        String companyName = tfCompanyName.getText().trim();
        String contactName = tfContactName.getText().trim();
        String phone = tfPhone.getText().trim();
        String email = tfEmail.getText().trim();
        String address = taAddress.getText().trim();

        try {
            NhaCungCap model = (nhaCungCap == null) ? new NhaCungCap() : nhaCungCap;

            model.setCompanyName(companyName);
            model.setContactName(contactName.isEmpty() ? null : contactName);
            model.setPhone(phone.isEmpty() ? null : phone);
            model.setEmail(email.isEmpty() ? null : email);
            model.setAddress(address.isEmpty() ? null : address);

            nhaCungCapService.save(model);

            JOptionPane.showMessageDialog(this,
                    nhaCungCap == null ? "Thêm nhà cung cấp mới thành công!" : "Cập nhật thông tin thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            succeeded = true; // field của BaseDialog (protected)
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lưu dữ liệu nhà cung cấp: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // isSucceeded() kế thừa từ BaseDialog — KHÔNG khai báo lại ở đây
}