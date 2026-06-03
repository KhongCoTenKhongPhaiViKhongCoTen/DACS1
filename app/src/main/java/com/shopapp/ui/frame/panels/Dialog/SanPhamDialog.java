package com.shopapp.ui.frame.panels.Dialog;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.shopapp.AppSys;
import com.shopapp.entity.DanhMuc;
import com.shopapp.entity.NhaCungCap;
import com.shopapp.entity.SanPham;
import com.shopapp.repository.impl.DanhMucRepositoryImpl;
import com.shopapp.repository.impl.NhaCungCapRepositoryImpl;
import com.shopapp.service.SanPhamService;
import com.shopapp.service.DanhMucService;
import com.shopapp.service.NhaCungCapService;
import com.shopapp.service.impl.DanhMucServiceImpl;
import com.shopapp.service.impl.NhaCungCapServiceImpl;
import com.shopapp.ui.components.BaseForm;
import com.shopapp.ui.themes.ThemeManager;
import com.shopapp.ui.components.BaseDialog;

/**
 * Dialog dùng chung cho việc Thêm mới và Chỉnh sửa San Pham (Product).
 * Hỗ trợ giao diện GridBagLayout, đồng bộ theme và kiểm tra dữ liệu đầu vào.
 *
 * ⚠️ Lưu ý: gọi buildAndShow() ở cuối constructor SAU KHI assign hết fields,
 * theo yêu cầu của BaseDialog.
 */
public class SanPhamDialog extends BaseDialog {

    // ── Services & entity ─────────────────────────────────────────────────────
    private final SanPham sanPham; // null = Thêm mới, khác null = Sửa thông tin
    private final SanPhamService sanPhamService;
    private final DanhMucService danhMucService;
    private final NhaCungCapService nhaCungCapService;

    // ── Form fields ───────────────────────────────────────────────────────────
    private JTextField tfSku;
    private JTextField tfProductName;
    private JTextArea taDescription;
    private JComboBox<DanhMuc> cbCategory;
    private JComboBox<NhaCungCap> cbSupplier;
    private JTextField tfUnitPrice;
    private JTextField tfCostPrice;
    private JTextField tfSize;
    private JTextField tfColor;
    private JTextField tfReorderLevel;
    private JCheckBox cbIsActive;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * @param owner       Cửa sổ cha
     * @param sanPham     null = Thêm mới, khác null = Sửa thông tin
     * @param sanPhamService Service xử lý SanPham
     */
    public SanPhamDialog(
            Frame owner,
            SanPham sanPham,
            SanPhamService sanPhamService) {
        super(owner, sanPham == null ? "Thêm Sản Phẩm Mới" : "Cập Nhật Thông Tin Sản Phẩm");

        // Assign fields TRƯỚC khi gọi buildAndShow()
        this.sanPham = sanPham;
        this.sanPhamService = sanPhamService;
        this.danhMucService = new DanhMucServiceImpl(new DanhMucRepositoryImpl());
        this.nhaCungCapService = new NhaCungCapServiceImpl(new NhaCungCapRepositoryImpl());

        // Gọi SAU CÙNG — lúc này createForm() và fillData() mới được gọi
        buildAndShow();
    }

    // ── Tạo form ──────────────────────────────────────────────────────────
    /**
     * Tạo các component form. Lúc này tất cả fields (sanPham, sanPhamService, ...)
     * đã được assign nên có thể dùng trực tiếp.
     */
    @Override
    protected JPanel createForm() {
        BaseForm form = new BaseForm();
        form.setBorder(new EmptyBorder(PADDING, PADDING + 5, PADDING, PADDING + 5));

        // Load categories and suppliers for combo boxes
        List<DanhMuc> categoryList = danhMucService.findAll();
        List<NhaCungCap> supplierList = nhaCungCapService.findAll();
        cbCategory = new JComboBox<>(categoryList.toArray(new DanhMuc[0]));
        cbSupplier = new JComboBox<>(supplierList.toArray(new NhaCungCap[0]));
        cbCategory.setFont(AppSys.themes.getFont(FONT_FIELD));
        cbSupplier.setFont(AppSys.themes.getFont(FONT_FIELD));

        tfSku = createTextField();
        tfProductName = createTextField();
        taDescription = new JTextArea(3, 20);
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(taDescription);
        descScroll.setBorder(BorderFactory.createEmptyBorder());
        tfUnitPrice = createTextField();
        tfCostPrice = createTextField();
        tfSize = createTextField();
        tfColor = createTextField();
        tfReorderLevel = createTextField();
        cbIsActive = new JCheckBox("Hoạt động");

        form.addRow(new JLabel("Mã SKU *"), tfSku);
        form.addRow(new JLabel("Tên sản phẩm *"), tfProductName);
        form.addRow(new JLabel("Mô tả"), descScroll);
        form.addRow(new JLabel("Danh mục *"), cbCategory);
        form.addRow(new JLabel("Nhà cung cấp *"), cbSupplier);
        form.addRow(new JLabel("Giá bán *"), tfUnitPrice);
        form.addRow(new JLabel("Giá nhập"), tfCostPrice);
        form.addRow(new JLabel("Kích thước"), tfSize);
        form.addRow(new JLabel("Màu sắc"), tfColor);
        form.addRow(new JLabel("Mức đặt lại *"), tfReorderLevel);
        form.addRowCenter(cbIsActive);

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
        if (sanPham == null)
            return;

        // Chế độ Sửa → điền dữ liệu entity vào form
        tfSku.setText(sanPham.getSku());
        tfProductName.setText(sanPham.getProductName());
        taDescription.setText(sanPham.getDescription() != null ? sanPham.getDescription() : "");
        cbCategory.setSelectedItem(sanPham.getCategory());
        cbSupplier.setSelectedItem(sanPham.getSupplier());
        tfUnitPrice.setText(sanPham.getUnitPrice() != null ? sanPham.getUnitPrice().stripTrailingZeros().toPlainString() : "");
        tfCostPrice.setText(sanPham.getCostPrice() != null ? sanPham.getCostPrice().stripTrailingZeros().toPlainString() : "");
        tfSize.setText(sanPham.getSize() != null ? sanPham.getSize() : "");
        tfColor.setText(sanPham.getColor() != null ? sanPham.getColor() : "");
        tfReorderLevel.setText(String.valueOf(sanPham.getReorderLevel()));
        cbIsActive.setSelected(sanPham.getIsActive() != null && sanPham.getIsActive());
    }

    // ── Xác thực tùy chỉnh ────────────────────────────────────────────────
    @Override
    protected boolean applyCustomValidation() {
        String sku = tfSku.getText().trim();
        String productName = tfProductName.getText().trim();
        DanhMuc selectedCategory = (DanhMuc) cbCategory.getSelectedItem();
        NhaCungCap selectedSupplier = (NhaCungCap) cbSupplier.getSelectedItem();
        String unitPriceText = tfUnitPrice.getText().trim();
        String costPriceText = tfCostPrice.getText().trim();
        String reorderLevelText = tfReorderLevel.getText().trim();

        if (sku.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền mã SKU (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra trùng SKU (chỉ khi Thêm mới)
        if (sanPham == null) {
            try {
                if (sanPhamService.existsBySku(sku)) {
                    JOptionPane.showMessageDialog(this,
                            "Mã SKU đã tồn tại trong hệ thống!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi kiểm tra trùng mã SKU: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        if (productName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền tên sản phẩm (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra độ dài tên sản phẩm
        if (productName.length() < 2) {
            JOptionPane.showMessageDialog(this,
                    "Tên sản phẩm phải có ít nhất 2 ký tự",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (selectedCategory == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn danh mục (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (selectedSupplier == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn nhà cung cấp (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (unitPriceText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền giá bán (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        BigDecimal unitPrice;
        try {
            unitPrice = new BigDecimal(unitPriceText);
            if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this,
                        "Giá bán phải là số không âm",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Giá bán phải là số",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        BigDecimal costPrice = null;
        if (!costPriceText.isEmpty()) {
            try {
                costPrice = new BigDecimal(costPriceText);
                if (costPrice.compareTo(BigDecimal.ZERO) < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Giá thành phải là số không âm",
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Giá thành phải là số",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        if (reorderLevelText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền mức đặt lại (*)",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        Integer reorderLevel;
        try {
            reorderLevel = Integer.parseInt(reorderLevelText);
            if (reorderLevel < 0) {
                JOptionPane.showMessageDialog(this,
                        "Mức đặt lại phải là số nguyên không âm",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Mức đặt lại phải là số nguyên",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    // ── Xử lý lưu ────────────────────────────────────────────────────────

    @Override
    protected void handleSave() {
        String sku = tfSku.getText().trim();
        String productName = tfProductName.getText().trim();
        String description = taDescription.getText().trim();
        DanhMuc selectedCategory = (DanhMuc) cbCategory.getSelectedItem();
        NhaCungCap selectedSupplier = (NhaCungCap) cbSupplier.getSelectedItem();
        BigDecimal unitPrice = new BigDecimal(tfUnitPrice.getText().trim());
        BigDecimal costPrice = tfCostPrice.getText().trim().isEmpty() ? null : new BigDecimal(tfCostPrice.getText().trim());
        String size = tfSize.getText().trim();
        String color = tfColor.getText().trim();
        Integer reorderLevel = Integer.parseInt(tfReorderLevel.getText().trim());
        boolean isActive = cbIsActive.isSelected();

        try {
            SanPham model = (sanPham == null) ? new SanPham() : sanPham;

            model.setSku(sku);
            model.setProductName(productName);
            model.setDescription(description.isEmpty() ? null : description);
            model.setCategory(selectedCategory);
            model.setSupplier(selectedSupplier);
            model.setUnitPrice(unitPrice);
            model.setCostPrice(costPrice);
            model.setSize(size.isEmpty() ? null : size);
            model.setColor(color.isEmpty() ? null : color);
            model.setReorderLevel(reorderLevel);
            model.setIsActive(isActive);
            // Set createdAt only when adding
            if (sanPham == null) {
                model.setCreatedAt(java.time.LocalDateTime.now());
            }

            sanPhamService.save(model);

            JOptionPane.showMessageDialog(this,
                    sanPham == null ? "Thêm sản phẩm mới thành công!" : "Cập nhật thông tin thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            succeeded = true; // field của BaseDialog (protected)
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lưu dữ liệu sản phẩm: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // isSucceeded() kế thừa từ BaseDialog — KHÔNG khai báo lại ở đây
}