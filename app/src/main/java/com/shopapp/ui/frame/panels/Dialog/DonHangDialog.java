package com.shopapp.ui.frame.panels.Dialog;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.shopapp.AppSys;
import com.shopapp.entity.DonHang;
import com.shopapp.entity.KhachHang;
import com.shopapp.entity.NguoiDung;
import com.shopapp.repository.impl.KhachHangRepositoryImpl;
import com.shopapp.repository.impl.NguoiDungRepositoryImpl;
import com.shopapp.service.DonHangService;
import com.shopapp.service.KhachHangService;
import com.shopapp.service.NguoiDungService;
import com.shopapp.service.impl.KhachHangServiceImpl;
import com.shopapp.service.impl.NguoiDungServiceImpl;
import com.shopapp.ui.components.BaseForm;
import com.shopapp.ui.components.BaseDialog;

/**
 * Dialog dùng chung cho việc Thêm mới và Chỉnh sửa Đơn hàng.
 * Hỗ trợ giao diện GridBagLayout, đồng bộ theme và kiểm tra dữ liệu đầu vào.
 *
 * ⚠️ Lưu ý: gọi buildAndShow() ở cuối constructor SAU KHI assign hết fields,
 * theo yêu cầu của BaseDialog.
 */
public class DonHangDialog extends BaseDialog {

    // ── Services & entity ─────────────────────────────────────────────────────
    private final DonHang donHang; // null = Thêm mới, khác null = Sửa thông tin
    private final DonHangService donHangService;
    private final KhachHangService khachHangService;
    private final NguoiDungService nguoiDungService;

    // ── Form fields ───────────────────────────────────────────────────────────
    private JTextField tfOrderNumber;
    private JComboBox<KhachHang> cbCustomer;
    private JComboBox<NguoiDung> cbUser;
    private JTextField tfOrderDate;
    private JTextField tfStatus;
    private JTextField tfSubtotal;
    private JTextField tfTaxAmount;
    private JTextField tfDiscountAmount;
    private JTextField tfTotalAmount;
    private JTextArea taNotes;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * @param owner       Cửa sổ cha
     * @param donHang     null = Thêm mới, khác null = Sửa thông tin
     * @param donHangService Service xử lý DonHang
     */
    public DonHangDialog(
            Frame owner,
            DonHang donHang,
            DonHangService donHangService) {
        super(owner, donHang == null ? "Thêm Đơn hàng Mới" : "Cập Nhật Thông Tin Đơn hàng");

        // Assign fields TRƯỚC khi gọi buildAndShow()
        this.donHang = donHang;
        this.donHangService = donHangService;
        this.khachHangService = new KhachHangServiceImpl(new KhachHangRepositoryImpl());
        this.nguoiDungService = new NguoiDungServiceImpl(new NguoiDungRepositoryImpl());

        // Gọi SAU CÙNG — lúc này createForm() và fillData() mới được gọi
        buildAndShow();
    }

    // ── Tạo form ──────────────────────────────────────────────────────────
    /**
     * Tạo các component form. Lúc này tất cả fields (donHang, donHangService, ...)
     * đã được assign nên có thể dùng trực tiếp.
     */
    @Override
    protected JPanel createForm() {
        BaseForm form = new BaseForm();
        form.setBorder(new EmptyBorder(PADDING, PADDING + 5, PADDING, PADDING + 5));

        // Load customers and users for combo boxes
        List<KhachHang> customerList = khachHangService.findAll();
        List<NguoiDung> userList = nguoiDungService.findAll();
        cbCustomer = new JComboBox<>(customerList.toArray(new KhachHang[0]));
        cbUser = new JComboBox<>(userList.toArray(new NguoiDung[0]));
        cbCustomer.setFont(AppSys.themes.getFont(FONT_FIELD));
        cbUser.setFont(AppSys.themes.getFont(FONT_FIELD));

        tfOrderNumber = createTextField();
        tfOrderDate = createTextField();
        tfStatus = createTextField();
        tfSubtotal = createTextField();
        tfTaxAmount = createTextField();
        tfDiscountAmount = createTextField();
        tfTotalAmount = createTextField();
        taNotes = new JTextArea(3, 20);
        taNotes.setLineWrap(true);
        taNotes.setWrapStyleWord(true);
        JScrollPane notesScroll = new JScrollPane(taNotes);
        notesScroll.setBorder(BorderFactory.createEmptyBorder());

        form.addRow(new JLabel("Mã đơn hàng *"), tfOrderNumber);
        form.addRow(new JLabel("Khách hàng"), cbCustomer);
        form.addRow(new JLabel("Nhân viên"), cbUser);
        form.addRow(new JLabel("Ngày đặt"), tfOrderDate);
        form.addRow(new JLabel("Trạng thái"), tfStatus);
        form.addRow(new JLabel("Tổng tiền trước thuế"), tfSubtotal);
        form.addRow(new JLabel("Số tiền thuế"), tfTaxAmount);
        form.addRow(new JLabel("Số tiền giảm giá"), tfDiscountAmount);
        form.addRow(new JLabel("Tổng tiền phải trả"), tfTotalAmount);
        form.addRow(new JLabel("Ghi chú"), notesScroll);

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
        if (donHang == null) {
            // For new order, set orderDate to now (will be overridden by service on save, but we show current time for clarity)
            tfOrderDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return;
        }

        // Chế độ Sửa → điền dữ liệu entity vào form
        tfOrderNumber.setText(donHang.getOrderNumber());
        cbCustomer.setSelectedItem(donHang.getCustomer());
        cbUser.setSelectedItem(donHang.getUser());
        tfOrderDate.setText(donHang.getOrderDate() != null ? donHang.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
        tfStatus.setText(donHang.getStatus() != null ? donHang.getStatus() : "");
        tfSubtotal.setText(donHang.getSubtotal() != null ? donHang.getSubtotal().stripTrailingZeros().toPlainString() : "");
        tfTaxAmount.setText(donHang.getTaxAmount() != null ? donHang.getTaxAmount().stripTrailingZeros().toPlainString() : "");
        tfDiscountAmount.setText(donHang.getDiscountAmount() != null ? donHang.getDiscountAmount().stripTrailingZeros().toPlainString() : "");
        tfTotalAmount.setText(donHang.getTotalAmount() != null ? donHang.getTotalAmount().stripTrailingZeros().toPlainString() : "");
        taNotes.setText(donHang.getNotes() != null ? donHang.getNotes() : "");
    }

    // ── Xác thực tùy chỉnh ────────────────────────────────────────────────
    @Override
    protected boolean applyCustomValidation() {
        // Check các field bắt buộc trước khi parse
        if (tfOrderNumber.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền mã đơn hàng (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (tfStatus.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền trạng thái (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Chỉ validate format ngày — business logic để Service lo
        String orderDateText = tfOrderDate.getText().trim();
        if (!orderDateText.isEmpty()) {
            try {
                LocalDateTime.parse(orderDateText,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Ngày đặt không hợp lệ. Định dạng đúng: yyyy-MM-dd HH:mm:ss",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    // ── Xử lý lưu ────────────────────────────────────────────────────────

    @Override
    protected void handleSave() {
        String orderNumber = tfOrderNumber.getText().trim();
        KhachHang selectedCustomer = (KhachHang) cbCustomer.getSelectedItem();
        NguoiDung selectedUser = (NguoiDung) cbUser.getSelectedItem();
        LocalDateTime orderDate = LocalDateTime.parse(tfOrderDate.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String status = tfStatus.getText().trim();
        BigDecimal subtotal = tfSubtotal.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfSubtotal.getText().trim());
        BigDecimal taxAmount = tfTaxAmount.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfTaxAmount.getText().trim());
        BigDecimal discountAmount = tfDiscountAmount.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfDiscountAmount.getText().trim());
        BigDecimal totalAmount = tfTotalAmount.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfTotalAmount.getText().trim());
        String notes = taNotes.getText().trim().isEmpty() ? null : taNotes.getText().trim();

        try {
            DonHang model = (donHang == null) ? new DonHang() : donHang;

            model.setOrderNumber(orderNumber);
            model.setCustomer(selectedCustomer);
            model.setUser(selectedUser);
            model.setOrderDate(orderDate);
            model.setStatus(status);
            model.setSubtotal(subtotal);
            model.setTaxAmount(taxAmount);
            model.setDiscountAmount(discountAmount);
            model.setTotalAmount(totalAmount);
            model.setNotes(notes);

            donHangService.save(model);

            JOptionPane.showMessageDialog(this,
                    donHang == null ? "Thêm đơn hàng mới thành công!" : "Cập nhật thông tin thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            succeeded = true; // field của BaseDialog (protected)
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lưu dữ liệu đơn hàng: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // isSucceeded() kế thừa từ BaseDialog — KHÔNG khai báo lại ở đây
}