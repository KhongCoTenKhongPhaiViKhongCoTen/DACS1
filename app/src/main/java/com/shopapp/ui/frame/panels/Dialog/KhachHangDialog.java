package com.shopapp.ui.frame.panels.Dialog;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.shopapp.entity.KhachHang;
import com.shopapp.service.KhachHangService;
import com.shopapp.ui.components.BaseForm;
import com.shopapp.ui.components.BaseDialog;

/**
 * Dialog dùng chung cho việc Thêm mới và Chỉnh sửa Khách hàng.
 * Hỗ trợ giao diện GridBagLayout, đồng bộ theme và kiểm tra dữ liệu đầu vào.
 *
 * ⚠️ Lưu ý: gọi buildAndShow() ở cuối constructor SAU KHI assign hết fields,
 * theo yêu cầu của BaseDialog.
 */
public class KhachHangDialog extends BaseDialog {

    // ── Services & entity ─────────────────────────────────────────────────────
    private final KhachHang khachHang; // null = Thêm mới, khác null = Sửa thông tin
    private final KhachHangService khachHangService;

    // ── Form fields ───────────────────────────────────────────────────────────
    private JTextField tfFullName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextArea taAddress;
    private JTextField tfLoyaltyPoints;
    private JTextField tfCreatedAt; // read-only, displays the createdAt timestamp

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * @param owner       Cửa sổ cha
     * @param khachHang   null = Thêm mới, khác null = Sửa thông tin
     * @param khachHangService Service xử lý KhachHang
     */
    public KhachHangDialog(
            Frame owner,
            KhachHang khachHang,
            KhachHangService khachHangService) {
        super(owner, khachHang == null ? "Thêm Khách hàng Mới" : "Cập Nhật Thông Tin Khách hàng");

        // Assign fields TRƯỚC khi gọi buildAndShow()
        this.khachHang = khachHang;
        this.khachHangService = khachHangService;

        // Gọi SAU CÙNG — lúc này createForm() và fillData() mới được gọi
        buildAndShow();
    }

    // ── Tạo form ──────────────────────────────────────────────────────────
    /**
     * Tạo các component form. Lúc này tất cả fields (khachHang, khachHangService, ...)
     * đã được assign nên có thể dùng trực tiếp.
     */
    @Override
    protected JPanel createForm() {
        BaseForm form = new BaseForm();
        form.setBorder(new EmptyBorder(PADDING, PADDING + 5, PADDING, PADDING + 5));

        tfFullName = createTextField();
        tfEmail = createTextField();
        tfPhone = createTextField();
        taAddress = new JTextArea(3, 20);
        taAddress.setLineWrap(true);
        taAddress.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(taAddress);
        addressScroll.setBorder(BorderFactory.createEmptyBorder());
        tfLoyaltyPoints = createTextField();
        tfCreatedAt = createTextField();
        tfCreatedAt.setEditable(false); // read-only

        form.addRow(new JLabel("Họ tên *"), tfFullName);
        form.addRow(new JLabel("Email"), tfEmail);
        form.addRow(new JLabel("Số điện thoại"), tfPhone);
        form.addRow(new JLabel("Địa chỉ"), addressScroll);
        form.addRow(new JLabel("Điểm thành viên *"), tfLoyaltyPoints);
        form.addRow(new JLabel("Ngày tạo"), tfCreatedAt);

        return form;
    }

    // ── Điền dữ liệu khi ở chế độ Sửa ───────────────────────────────────────

    /**
     * Điền dữ liệu entity vào form (nếu ở chế độ Sửa).
     * Được gọi bởi buildAndShow() SAU createForm(), поэтому các fields đã tồn tại.
     */
    @Override
    protected void fillData() {
        // Chế độ Thêm mới → không cần điền thêm
        if (khachHang == null) {
            // For new customer, set createdAt to now (will be overridden by service on save, but we show current time for clarity)
            tfCreatedAt.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return;
        }

        // Chế độ Sửa → điền dữ liệu entity vào form
        tfFullName.setText(khachHang.getFullName());
        tfEmail.setText(khachHang.getEmail() != null ? khachHang.getEmail() : "");
        tfPhone.setText(khachHang.getPhone() != null ? khachHang.getPhone() : "");
        taAddress.setText(khachHang.getAddress() != null ? khachHang.getAddress() : "");
        tfLoyaltyPoints.setText(String.valueOf(khachHang.getLoyaltyPoints()));
        tfCreatedAt.setText(khachHang.getCreatedAt() != null ? khachHang.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
    }

    // ── Xác thực tùy chỉnh ────────────────────────────────────────────────
    @Override
    protected boolean applyCustomValidation() {
        String fullName = tfFullName.getText().trim();
        String email = tfEmail.getText().trim();
        String phone = tfPhone.getText().trim();
        String address = taAddress.getText().trim();
        String loyaltyPointsText = tfLoyaltyPoints.getText().trim();

        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền họ tên (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra độ dài họ tên
        if (fullName.length() < 2) {
            JOptionPane.showMessageDialog(this,
                    "Họ tên phải có ít nhất 2 ký tự",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
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

        if (loyaltyPointsText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền điểm thành viên (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        Integer loyaltyPoints;
        try {
            loyaltyPoints = Integer.parseInt(loyaltyPointsText);
            if (loyaltyPoints < 0) {
                JOptionPane.showMessageDialog(this,
                        "Điểm thành viên phải là số nguyên không âm",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Điểm thành viên phải là số nguyên",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    // ── Xử lý lưu ────────────────────────────────────────────────────────

    @Override
    protected void handleSave() {
        String fullName = tfFullName.getText().trim();
        String email = tfEmail.getText().trim().isEmpty() ? null : tfEmail.getText().trim();
        String phone = tfPhone.getText().trim().isEmpty() ? null : tfPhone.getText().trim();
        String address = taAddress.getText().trim().isEmpty() ? null : taAddress.getText().trim();
        Integer loyaltyPoints = Integer.parseInt(tfLoyaltyPoints.getText().trim());

        try {
            KhachHang model = (khachHang == null) ? new KhachHang() : khachHang;

            model.setFullName(fullName);
            model.setEmail(email);
            model.setPhone(phone);
            model.setAddress(address);
            model.setLoyaltyPoints(loyaltyPoints);
            // For new customer, set createdAt to now (if not set)
            if (khachHang == null) {
                model.setCreatedAt(LocalDateTime.now());
            }

            khachHangService.save(model);

            JOptionPane.showMessageDialog(this,
                    khachHang == null ? "Thêm khách hàng mới thành công!" : "Cập nhật thông tin thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            succeeded = true; // field của BaseDialog (protected)
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lưu dữ liệu khách hàng: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // isSucceeded() kế thừa từ BaseDialog — KHÔNG khai báo lại ở đây
}