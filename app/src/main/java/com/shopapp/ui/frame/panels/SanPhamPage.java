package com.shopapp.ui.frame.panels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.*;
import javax.swing.SpinnerDateModel;
import com.shopapp.entity.DanhMuc;
import com.shopapp.entity.NhaCungCap;
import com.shopapp.entity.SanPham;
import com.shopapp.repository.impl.SanPhamRepositoryImpl;
import com.shopapp.repository.impl.DanhMucRepositoryImpl;
import com.shopapp.repository.impl.NhaCungCapRepositoryImpl;
import com.shopapp.service.SanPhamService;
import com.shopapp.service.DanhMucService;
import com.shopapp.service.NhaCungCapService;
import com.shopapp.service.impl.SanPhamServiceImpl;
import com.shopapp.service.impl.DanhMucServiceImpl;
import com.shopapp.service.impl.NhaCungCapServiceImpl;
import com.shopapp.ui.components.BasePage;
import com.shopapp.ui.frame.panels.Dialog.SanPhamDialog;
import com.shopapp.AppSys;

/**
 * Panel for managing San Pham (Products)
 */
public class SanPhamPage extends BasePage {

    private JComboBox<DanhMuc> cbFilterCategory;
    private JComboBox<NhaCungCap> cbFilterSupplier;
    private JComboBox<String> cbFilterStatus;

    private SanPhamService sanPhamService;

    public SanPhamPage() {
        super(new String[] {
                "ID",
                "Tên sản phẩm",
                "Mã SKU",
                "Giá bán",
                "Nhà cung cấp",
                "Danh mục"
        });
        // Load filter data after UI initialization
        loadFilterData();
    }

    // ── Lazy init service ─────────────────────────────────────────────────────

    private SanPhamService getSanPhamService() {
        if (sanPhamService == null) {
            sanPhamService = new SanPhamServiceImpl(new SanPhamRepositoryImpl());
        }
        return sanPhamService;
    }

    private DanhMucService getDanhMucService() {
        return new DanhMucServiceImpl(new DanhMucRepositoryImpl());
    }

    private NhaCungCapService getNhaCungCapService() {
        return new NhaCungCapServiceImpl(new NhaCungCapRepositoryImpl());
    }

    // ── Filter loading ─────────────────────────────────────────────────────

    private void loadFilterData() {
        try {
            // Load categories
            List<DanhMuc> categories = getDanhMucService().findAll();
            cbFilterCategory.removeAllItems();
            cbFilterCategory.addItem(new DanhMuc()); // "Tất cả" item
            for (DanhMuc category : categories) {
                cbFilterCategory.addItem(category);
            }

            // Load suppliers
            List<NhaCungCap> suppliers = getNhaCungCapService().findAll();
            cbFilterSupplier.removeAllItems();
            cbFilterSupplier.addItem(new NhaCungCap()); // "Tất cả" item
            for (NhaCungCap supplier : suppliers) {
                cbFilterSupplier.addItem(supplier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ── Lấy Frame cha ─────────────────────────────────────────────────────────

    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }

    // ── Filter & Table ────────────────────────────────────────────────────────

    @Override
    protected void addCustomFilters() {
        // Category filter
        JLabel lblCategory = new JLabel("Danh mục:");
        lblCategory.setFont(AppSys.themes.getFont(12));
        cbFilterCategory = new JComboBox<>();
        cbFilterCategory.setFont(AppSys.themes.getFont(12));
        cbFilterCategory.addActionListener(e -> showTableData(true));

        // Supplier filter
        JLabel lblSupplier = new JLabel("Nhà cung cấp:");
        lblSupplier.setFont(AppSys.themes.getFont(12));
        cbFilterSupplier = new JComboBox<>();
        cbFilterSupplier.setFont(AppSys.themes.getFont(12));
        cbFilterSupplier.addActionListener(e -> showTableData(true));

        // Status filter
        JLabel lblStatus = new JLabel("Trạng thái:");
        lblStatus.setFont(AppSys.themes.getFont(12));
        cbFilterStatus = new JComboBox<>(new String[] {
            "Tất cả", "Đang bán", "Ngừng bán"
        });
        cbFilterStatus.setFont(AppSys.themes.getFont(12));
        cbFilterStatus.addActionListener(e -> showTableData(true));

        filterPanel.add(lblCategory);
        filterPanel.add(cbFilterCategory);
        filterPanel.add(lblSupplier);
        filterPanel.add(cbFilterSupplier);
        filterPanel.add(lblStatus);
        filterPanel.add(cbFilterStatus);

        // Load filter data
        loadFilterData();
    }

    @Override
    public void showTableData(boolean applyFilters) {
        tableModel.setRowCount(0);
        try {
            List<SanPham> sanPhamList = getSanPhamService().findAll();
            String searchQuery = tfSearch.getText().trim().toLowerCase();
            Object selectedCategory = cbFilterCategory.getSelectedItem();
            Object selectedSupplier = cbFilterSupplier.getSelectedItem();
            String statusFilter = cbFilterStatus.getSelectedItem() != null ? cbFilterStatus.getSelectedItem().toString() : "Tất cả";

            for (SanPham sanPham : sanPhamList) {
                if (applyFilters) {
                    // Apply global search (ID, Name, SKU, Unit Price, Supplier, Category)
                    if (!searchQuery.isEmpty()) {
                        String idStr = String.valueOf(sanPham.getProductId());
                        String nameStr = sanPham.getProductName() != null ? sanPham.getProductName().toLowerCase() : "";
                        String skuStr = sanPham.getSku() != null ? sanPham.getSku().toLowerCase() : "";
                        String unitPriceStr = sanPham.getUnitPrice() != null ? sanPham.getUnitPrice().toString().toLowerCase() : "";
                        String supplierName = sanPham.getSupplier() != null ? sanPham.getSupplier().getCompanyName().toLowerCase() : "";
                        String categoryName = sanPham.getCategory() != null ? sanPham.getCategory().getCategoryName().toLowerCase() : "";

                        if (!idStr.contains(searchQuery) && !nameStr.contains(searchQuery) &&
                            !skuStr.contains(searchQuery) && !unitPriceStr.contains(searchQuery) &&
                            !supplierName.contains(searchQuery) && !categoryName.contains(searchQuery)) {
                            continue;
                        }
                    }

                    // Apply category filter
                    if (selectedCategory != null && selectedCategory instanceof DanhMuc &&
                        ((DanhMuc) selectedCategory).getCategoryId() != null) {
                        if (sanPham.getCategory() == null || !sanPham.getCategory().getCategoryId().equals(((DanhMuc) selectedCategory).getCategoryId())) {
                            continue;
                        }
                    } else if (selectedCategory instanceof DanhMuc &&
                               ((DanhMuc) selectedCategory).getCategoryId() == null) {
                        // "Tất cả" selected - no filter
                    }

                    // Apply supplier filter
                    if (selectedSupplier != null && selectedSupplier instanceof NhaCungCap &&
                        ((NhaCungCap) selectedSupplier).getSupplierId() != null) {
                        if (sanPham.getSupplier() == null || !sanPham.getSupplier().getSupplierId().equals(((NhaCungCap) selectedSupplier).getSupplierId())) {
                            continue;
                        }
                    } else if (selectedSupplier instanceof NhaCungCap &&
                               ((NhaCungCap) selectedSupplier).getSupplierId() == null) {
                        // "Tất cả" selected - no filter
                    }

                    // Apply status filter
                    if (!"Tất cả".equals(statusFilter)) {
                        boolean isActive = sanPham.getIsActive() != null ? sanPham.getIsActive() : false;
                        boolean matchesStatus = "Đang bán".equals(statusFilter) ? isActive : !isActive;
                        if (!matchesStatus) {
                            continue;
                        }
                    }
                }

                tableModel.addRow(new Object[] {
                        sanPham.getProductId(),
                        sanPham.getProductName(),
                        sanPham.getSku(),
                        sanPham.getUnitPrice(),
                        sanPham.getSupplier() != null ? sanPham.getSupplier().getCompanyName() : "",
                        sanPham.getCategory() != null ? sanPham.getCategory().getCategoryName() : ""
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải dữ liệu sản phẩm: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    protected void handleFilter() {
        showTableData(true);
    }

    // ── CRUD handlers ─────────────────────────────────────────────────────────

    @Override
    protected void handleAdd() {
        SanPhamDialog dialog = new SanPhamDialog(getParentFrame(), null, getSanPhamService());
        dialog.setVisible(true);
        if (dialog.isSucceeded()) {
            showTableData(true);
        }
    }

    @Override
    protected void handleEdit() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một sản phẩm từ bảng để chỉnh sửa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Optional<SanPham> sanPham = getSanPhamService().findById(selectedId);
            if (sanPham.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy thông tin sản phẩm được chọn.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SanPhamDialog dialog = new SanPhamDialog(getParentFrame(), sanPham.get(), getSanPhamService());
            dialog.setVisible(true);
            if (dialog.isSucceeded()) {
                showTableData(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lấy thông tin sản phẩm: " + ex.getMessage(),
                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected void handleDelete() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một sản phẩm từ bảng để xóa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa sản phẩm này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                getSanPhamService().deleteById(selectedId);
                showTableData(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xóa sản phẩm: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ── Custom buttons & events ───────────────────────────────────────────────

    @Override
    protected void addCustomButtons() {
        // No custom buttons for now
    }

    @Override
    protected void attachCustomEvents() {
        // Load dữ liệu ban đầu
        showTableData(false);
    }
}