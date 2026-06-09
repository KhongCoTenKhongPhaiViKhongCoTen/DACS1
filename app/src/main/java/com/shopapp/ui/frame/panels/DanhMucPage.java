package com.shopapp.ui.frame.panels;

import java.util.List;
import java.util.Optional;

import javax.swing.*;

import com.shopapp.entity.DanhMuc;
import com.shopapp.repository.impl.DanhMucRepositoryImpl;
import com.shopapp.service.DanhMucService;
import com.shopapp.service.impl.DanhMucServiceImpl;
import com.shopapp.ui.components.BasePage;
import com.shopapp.ui.frame.panels.Dialog.DanhMucDialog;

public class DanhMucPage extends BasePage {

    private DanhMucService danhMucService;

    public DanhMucPage() {
        super(new String[] {
                "ID",
                "Tên danh mục",
                "Mô tả"
        });
    }

    private DanhMucService getDanhMucService() {
        if (danhMucService == null) {
            danhMucService = new DanhMucServiceImpl(new DanhMucRepositoryImpl());
        }
        return danhMucService;
    }

    // ── Lấy Frame cha ─────────────────────────────────────────────────────────

    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }

    @Override
    protected void addCustomFilters() {

    }

    @Override
    public void showTableData(boolean applyFilters) {
        tableModel.setRowCount(0);
        try {
            List<DanhMuc> danhMucList = getDanhMucService().findAll();
            String searchQuery = tfSearch.getText().trim().toLowerCase();

            for (DanhMuc danhMuc : danhMucList) {
                if (applyFilters) {
                    // Apply global search (ID, Name)
                    if (!searchQuery.isEmpty()) {
                        String idStr = String.valueOf(danhMuc.getCategoryId());
                        String nameStr = danhMuc.getCategoryName() != null ? danhMuc.getCategoryName().toLowerCase() : "";

                        if (!idStr.contains(searchQuery) && !nameStr.contains(searchQuery)) {
                            continue;
                        }
                    }
                }

                tableModel.addRow(new Object[] {
                        danhMuc.getCategoryId(),
                        danhMuc.getCategoryName(),
                        danhMuc.getDescription()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải dữ liệu danh mục: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    protected void handleFilter() {
        showTableData(true);
    }

    @Override
    protected void handleAdd() {
        DanhMucDialog dialog = new DanhMucDialog(getParentFrame(), null, getDanhMucService());
        if (dialog.isSucceeded()) {
            showTableData(true);
        }
    }

    @Override
    protected void handleEdit() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một danh mục từ bảng để chỉnh sửa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Optional<DanhMuc> danhMuc = getDanhMucService().findById(selectedId);
            if (danhMuc.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy thông tin danh mục được chọn.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DanhMucDialog dialog = new DanhMucDialog(getParentFrame(), danhMuc.get(), getDanhMucService());
            if (dialog.isSucceeded()) {
                showTableData(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lấy thông tin danh mục: " + ex.getMessage(),
                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected void handleDelete() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một danh mục từ bảng để xóa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa danh mục này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                getDanhMucService().deleteById(selectedId);
                showTableData(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xóa danh mục: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void addCustomButtons() {
    }

    @Override
    protected void attachCustomEvents() {
        showTableData(false);
    }
}