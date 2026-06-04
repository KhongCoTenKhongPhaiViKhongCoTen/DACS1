package com.shopapp.ui.frame.panels;

import java.util.List;
import java.util.Optional;

import javax.swing.*;
import com.shopapp.entity.TonKho;
import com.shopapp.repository.impl.TonKhoRepositoryImpl;
import com.shopapp.service.TonKhoService;
import com.shopapp.service.impl.TonKhoServiceImpl;
import com.shopapp.ui.components.BasePage;
import com.shopapp.ui.frame.panels.Dialog.TonKhoDialog;
import com.shopapp.AppSys;

/**
 * Panel for managing Ton Kho (Inventory)
*/
public class TonKhoPage extends BasePage {
    
    private JComboBox<String> cbFilterStockLevel;
    
    private TonKhoService tonKhoService;
    
    public TonKhoPage() {
        super(new String[] {
                "ID",
                "Sản phẩm",
                "Số lượng tồn",
                "Vị trí",
                "Cập nhật lần cuối"
        });
    }

    // ── Lazy init service ─────────────────────────────────────────────────────

    private TonKhoService getTonKhoService() {
        if (tonKhoService == null) {
            tonKhoService = new TonKhoServiceImpl(new TonKhoRepositoryImpl());
        }
        return tonKhoService;
    }

    // ── Lấy Frame cha ─────────────────────────────────────────────────────────

    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }

    // ── Filter loading ─────────────────────────────────────────────────────

    // Product filter removed - searching handled via global search box

    
    @Override
    public void showTableData(boolean applyFilters) {
        tableModel.setRowCount(0);
        try {
            List<TonKho> tonKhoList = getTonKhoService().findAll();
            String searchQuery = tfSearch.getText().trim().toLowerCase();
            String stockLevelFilter = cbFilterStockLevel.getSelectedItem() != null ? cbFilterStockLevel.getSelectedItem().toString() : "Tất cả";

            for (TonKho tonKho : tonKhoList) {
                if (applyFilters) {
                    // Apply global search (ID, Product Name, Quantity, Location, Last Updated)
                    if (!searchQuery.isEmpty()) {
                        String idStr = String.valueOf(tonKho.getInventoryId());
                        String productName = tonKho.getProduct() != null ? tonKho.getProduct().getProductName().toLowerCase() : "";
                        String quantityStr = String.valueOf(tonKho.getQuantityOnHand());
                        String location = tonKho.getLocation() != null ? tonKho.getLocation().toLowerCase() : "";
                        String lastUpdatedStr = tonKho.getLastUpdated() != null ? tonKho.getLastUpdated().toString().toLowerCase() : "";

                        if (!idStr.contains(searchQuery) && !productName.contains(searchQuery) &&
                            !quantityStr.contains(searchQuery) && !location.contains(searchQuery) &&
                            !lastUpdatedStr.contains(searchQuery)) {
                            continue;
                        }
                    }

                    // Apply stock level filter
                    if (!"Tất cả".equals(stockLevelFilter)) {
                        int quantity = tonKho.getQuantityOnHand();
                        int reorderLevel = tonKho.getProduct() != null ? tonKho.getProduct().getReorderLevel() : 0;

                        boolean matchesStockLevel = false;
                        if ("S thấp (< mức báo)".equals(stockLevelFilter)) {
                            matchesStockLevel = quantity < reorderLevel;
                        } else if ("Bình thường".equals(stockLevelFilter)) {
                            matchesStockLevel = quantity >= reorderLevel && quantity <= (2 * reorderLevel);
                        } else if ("S cao (> 2x mức báo)".equals(stockLevelFilter)) {
                            matchesStockLevel = quantity > (2 * reorderLevel);
                        }

                        if (!matchesStockLevel) {
                            continue;
                        }
                    }

                    // Product and location filters removed - searching handled via global search box
                }

                tableModel.addRow(new Object[] {
                        tonKho.getInventoryId(),
                        tonKho.getProduct() != null ? tonKho.getProduct().getProductName() : "",
                        tonKho.getQuantityOnHand(),
                        tonKho.getLocation(),
                        tonKho.getLastUpdated()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải dữ liệu tồn kho: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ── Filter & Table ────────────────────────────────────────────────────────

    @Override
    protected void addCustomFilters() {
        // Stock level filter
        JLabel lblStockLevel = new JLabel("Mức tồn:");
        lblStockLevel.setFont(AppSys.themes.getFont(12));
        cbFilterStockLevel = new JComboBox<>(new String[] {
            "Tất cả", "S thấp (< mức báo)", "Bình thường", "S cao (> 2x mức báo)"
        });
        cbFilterStockLevel.setFont(AppSys.themes.getFont(12));
        cbFilterStockLevel.addActionListener(e -> showTableData(true));

        filterPanel.add(lblStockLevel);
        filterPanel.add(cbFilterStockLevel);
    }


    @Override
    protected void handleFilter() {
        showTableData(true);
    }

    // ── CRUD handlers ─────────────────────────────────────────────────────────

    @Override
    protected void handleAdd() {
        TonKhoDialog dialog = new TonKhoDialog(getParentFrame(), null, getTonKhoService());
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
                    "Vui lòng chọn một tồn kho từ bảng để chỉnh sửa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Optional<TonKho> tonKho = getTonKhoService().findById(selectedId);
            if (tonKho.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy thông tin tồn kho được chọn.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TonKhoDialog dialog = new TonKhoDialog(getParentFrame(), tonKho.get(), getTonKhoService());
            dialog.setVisible(true);
            if (dialog.isSucceeded()) {
                showTableData(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lấy thông tin tồn kho: " + ex.getMessage(),
                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected void handleDelete() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một tồn kho từ bảng để xóa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa tồn kho này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                getTonKhoService().deleteById(selectedId);
                showTableData(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xóa tồn kho: " + e.getMessage(),
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