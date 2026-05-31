package com.shopapp.ui.frame.panels.Dialog;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.shopapp.entity.SanPham;
import com.shopapp.entity.TonKho;
import com.shopapp.repository.SanPhamRepository;
import com.shopapp.repository.impl.SanPhamRepositoryImpl;
import com.shopapp.service.SanPhamService;
import com.shopapp.service.TonKhoService;
import com.shopapp.service.impl.SanPhamServiceImpl;
import com.shopapp.ui.components.BaseForm;
import com.shopapp.ui.themes.ThemeManager;
import com.shopapp.ui.components.BaseDialog;

/**
 * Dialog dùng chung cho việc Thêm mới và Chỉnh sửa Tồn Kho.
 * Hỗ trợ giao diện GridBagLayout, đồng bộ theme và kiểm tra dữ liệu đầu vào.
 *
 * ⚠️ Lưu ý: gọi buildAndShow() ở cuối constructor SAU KHI assign hết fields,
 * theo yêu cầu của BaseDialog.
 */
public class TonKhoDialog extends BaseDialog {

    // ── Services & entity ─────────────────────────────────────────────────────
    private final TonKho tonKho; // null = Thêm mới, khác null = Sửa thông tin
    private final TonKhoService tonKhoService;
    private final SanPhamService sanPhamService; // for loading products

    // ── Form fields ───────────────────────────────────────────────────────────
    private JComboBox<SanPham> cbProduct;
    private JTextField tfQuantityOnHand;
    private JTextField tfLocation;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * @param owner       Cửa sổ cha
     * @param tonKho      null = Thêm mới, khác null = Sửa thông tin
     * @param tonKhoService Service xử lý TonKho
     */
    public TonKhoDialog(
            Frame owner,
            TonKho tonKho,
            TonKhoService tonKhoService) {
        super(owner, tonKho == null ? "Thêm Tồn Kho Mới" : "Cập Nhật Thông Tin Tồn Kho");

        // Assign fields TRƯỚC khi gọi buildAndShow()
        this.tonKho = tonKho;
        this.tonKhoService = tonKhoService;
        this.sanPhamService = new SanPhamServiceImpl(new SanPhamRepositoryImpl());

        // Gọi SAU CÙNG — lúc này createForm() và fillData() mới được gọi
        buildAndShow();
    }

    // ── Tạo form ──────────────────────────────────────────────────────────
    /**
     * Tạo các component form. Lúc này tất cả fields (tonKho, tonKhoService, ...)
     * đã được assign nên có thể dùng trực tiếp.
     */
    @Override
    protected JPanel createForm() {
        BaseForm form = new BaseForm();
        form.setBorder(new EmptyBorder(PADDING, PADDING + 5, PADDING, PADDING + 5));

        // Load products for combo box
        List<SanPham> productList = sanPhamService.findAll();
        cbProduct = new JComboBox<>(productList.toArray(new SanPham[0]));
        cbProduct.setFont(ThemeManager.getFont(FONT_FIELD));
        tfQuantityOnHand = createTextField();
        tfLocation = createTextField();

        form.addRow(new JLabel("Sản phẩm *"), cbProduct);
        form.addRow(new JLabel("Số lượng tồn *"), tfQuantityOnHand);
        form.addRow(new JLabel("Vị trí"), tfLocation);

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
        if (tonKho == null)
            return;

        // Chế độ Sửa → điền dữ liệu entity vào form
        cbProduct.setSelectedItem(tonKho.getProduct());
        tfQuantityOnHand.setText(String.valueOf(tonKho.getQuantityOnHand()));
        tfLocation.setText(tonKho.getLocation() != null ? tonKho.getLocation() : "");
    }

    // ── Xác thực tùy chỉnh ────────────────────────────────────────────────

    @Override
    protected boolean applyCustomValidation() {
        SanPham selectedProduct = (SanPham) cbProduct.getSelectedItem();
        String quantityText = tfQuantityOnHand.getText().trim();
        String location = tfLocation.getText().trim();

        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn sản phẩm (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền số lượng tồn (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            Integer quantity = Integer.parseInt(quantityText);
            if (quantity < 0) {
                JOptionPane.showMessageDialog(this,
                        "Số lượng tồn phải là số nguyên không âm",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Số lượng tồn phải là số nguyên",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    // ── Xử lý lưu ────────────────────────────────────────────────────────

    @Override
    protected void handleSave() {
        SanPham selectedProduct = (SanPham) cbProduct.getSelectedItem();
        int quantityOnHand = Integer.parseInt(tfQuantityOnHand.getText().trim());
        String location = tfLocation.getText().trim();

        try {
            TonKho model = (tonKho == null) ? new TonKho() : tonKho;

            model.setProduct(selectedProduct);
            model.setQuantityOnHand(quantityOnHand);
            model.setLocation(location.isEmpty() ? null : location);
            // Set lastUpdated to current time
            model.setLastUpdated(java.time.LocalDateTime.now());

            tonKhoService.save(model);

            JOptionPane.showMessageDialog(this,
                    tonKho == null ? "Thêm tồn kho mới thành công!" : "Cập nhật thông tin thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            succeeded = true; // field của BaseDialog (protected)
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lưu dữ liệu tồn kho: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // isSucceeded() kế thừa từ BaseDialog — KHÔNG khai báo lại ở đây
}