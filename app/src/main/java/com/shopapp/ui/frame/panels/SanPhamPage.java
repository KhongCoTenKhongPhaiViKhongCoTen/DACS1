package com.shopapp.ui.frame.panels;

import java.util.List;
import java.util.Optional;

import javax.swing.*;
import com.shopapp.entity.DanhMuc;
import com.shopapp.entity.NhaCungCap;
import com.shopapp.entity.SanPham;
import com.shopapp.repository.impl.SanPhamRepositoryImpl;
import com.shopapp.service.SanPhamService;
import com.shopapp.service.impl.SanPhamServiceImpl;
import com.shopapp.ui.components.BasePage;
import com.shopapp.ui.frame.panels.Dialog.SanPhamDialog;

/**
 * Panel for managing San Pham (Products)
 */
public class SanPhamPage extends BasePage {

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
    }

    // ── Lazy init service ─────────────────────────────────────────────────────

    private SanPhamService getSanPhamService() {
        if (sanPhamService == null) {
            sanPhamService = new SanPhamServiceImpl(new SanPhamRepositoryImpl());
        }
        return sanPhamService;
    }

    // ── Lấy Frame cha ─────────────────────────────────────────────────────────

    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }

    // ── Filter & Table ────────────────────────────────────────────────────────

    @Override
    protected void addCustomFilters() {
        // Không có filter tùy chỉnh
    }

    @Override
    public void showTableData(boolean applyFilters) {
        tableModel.setRowCount(0);
        try {
            List<SanPham> sanPhamList = getSanPhamService().findAll();
            String searchQuery = tfSearch.getText().trim().toLowerCase();

            for (SanPham sanPham : sanPhamList) {
                if (applyFilters && !searchQuery.isEmpty()) {
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