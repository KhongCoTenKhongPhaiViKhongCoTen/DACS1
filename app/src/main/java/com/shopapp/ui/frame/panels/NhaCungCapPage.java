package com.shopapp.ui.frame.panels;

import java.util.List;
import java.util.Optional;

import javax.swing.*;
import com.shopapp.entity.NhaCungCap;
import com.shopapp.repository.impl.NhaCungCapRepositoryImpl;
import com.shopapp.service.NhaCungCapService;
import com.shopapp.service.impl.NhaCungCapServiceImpl;
import com.shopapp.ui.components.BasePage;
import com.shopapp.ui.frame.panels.Dialog.NhaCungCapDialog;
import com.shopapp.AppSys;

public class NhaCungCapPage extends BasePage {


    private NhaCungCapService nhaCungCapService;

    public NhaCungCapPage() {
        super(new String[] {
                "ID",
                "Tên nhà cung cấp",
                "Người liên hệ",
                "Điện thoại",
                "Email",
                "Địa chỉ"
        });
    }

    // ── Lazy init service ─────────────────────────────────────────────────────

    private NhaCungCapService getNhaCungCapService() {
        if (nhaCungCapService == null) {
            nhaCungCapService = new NhaCungCapServiceImpl(new NhaCungCapRepositoryImpl());
        }
        return nhaCungCapService;
    }


    // ── Lấy Frame cha ─────────────────────────────────────────────────────────

    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }

    // ── Filter & Table ────────────────────────────────────────────────────────

    @Override
    protected void addCustomFilters() {
        // No custom filters needed - using base search functionality
    }

    @Override
    public void showTableData(boolean applyFilters) {
        tableModel.setRowCount(0);
        try {
            List<NhaCungCap> nhaCungCapList = getNhaCungCapService().findAll();
            String searchQuery = tfSearch.getText().trim().toLowerCase();

            for (NhaCungCap nhaCungCap : nhaCungCapList) {
                if (applyFilters && !searchQuery.isEmpty()) {
                    String idStr = String.valueOf(nhaCungCap.getSupplierId());
                    String nameStr = nhaCungCap.getCompanyName() != null ? nhaCungCap.getCompanyName().toLowerCase() : "";
                    String contactStr = nhaCungCap.getContactName() != null ? nhaCungCap.getContactName().toLowerCase() : "";
                    String emailStr = nhaCungCap.getEmail() != null ? nhaCungCap.getEmail().toLowerCase() : "";

                    if (!idStr.contains(searchQuery) && !nameStr.contains(searchQuery) && !contactStr.contains(searchQuery) && !emailStr.contains(searchQuery)) {
                        continue;
                    }
                }

                tableModel.addRow(new Object[] {
                        nhaCungCap.getSupplierId(),
                        nhaCungCap.getCompanyName(),
                        nhaCungCap.getContactName(),
                        nhaCungCap.getPhone(),
                        nhaCungCap.getEmail(),
                        nhaCungCap.getAddress()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải dữ liệu nhà cung cấp: " + e.getMessage(),
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
        NhaCungCapDialog dialog = new NhaCungCapDialog(getParentFrame(), null, getNhaCungCapService());
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
                    "Vui lòng chọn một nhà cung cấp từ bảng để chỉnh sửa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Optional<NhaCungCap> nhaCungCap = getNhaCungCapService().findById(selectedId);
            if (nhaCungCap.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy thông tin nhà cung cấp được chọn.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            NhaCungCapDialog dialog = new NhaCungCapDialog(getParentFrame(), nhaCungCap.get(), getNhaCungCapService());
            dialog.setVisible(true);
            if (dialog.isSucceeded()) {
                showTableData(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lấy thông tin nhà cung cấp: " + ex.getMessage(),
                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected void handleDelete() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một nhà cung cấp từ bảng để xóa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa nhà cung cấp này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                getNhaCungCapService().deleteById(selectedId);
                showTableData(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xóa nhà cung cấp: " + e.getMessage(),
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