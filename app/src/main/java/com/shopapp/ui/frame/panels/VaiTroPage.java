package com.shopapp.ui.frame.panels;

import java.util.List;
import java.util.Optional;

import javax.swing.*;
import javax.swing.SwingUtilities;

import com.shopapp.entity.Vaitro;
import com.shopapp.repository.impl.VaitroRepositoryImpl;
import com.shopapp.service.VaitroService;
import com.shopapp.service.impl.VaitroServiceImpl;
import com.shopapp.ui.components.BasePage;
import com.shopapp.ui.frame.panels.Dialog.VaiTroDialog;

public class VaiTroPage extends BasePage {

    private VaitroService roleService;

    public VaiTroPage() {
        super(new String[] {
                "ID",
                "Tên vai trò",
                "Mô tả"
        });
    }

    private VaitroService getRoleService() {
        if (roleService == null) {
            roleService = new VaitroServiceImpl(new VaitroRepositoryImpl());
        }
        return roleService;
    }

    @Override
    protected void addCustomFilters() {
        // showTableData(false);
    }

    @Override
    public void showTableData(boolean applyFilters) {
        tableModel.setRowCount(0);
        try {
            List<Vaitro> roles = getRoleService().findAll();
            String searchQuery = tfSearch.getText().trim().toLowerCase();

            for (Vaitro vaitro : roles) {
                if (applyFilters && !searchQuery.isEmpty()) {
                    String idStr = String.valueOf(vaitro.getRoleId());
                    String nameStr = vaitro.getRoleName().toLowerCase();

                    if (!idStr.contains(searchQuery) && !nameStr.contains(searchQuery)) {
                        continue;
                    }
                }

                tableModel.addRow(new Object[] {
                        vaitro.getRoleId(),
                        vaitro.getRoleName(),
                        vaitro.getDescription()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu vai trò: " + e.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    protected void handleFilter() {
        showTableData(true);
    }

    @Override
    protected void handleAdd() {
        VaiTroDialog dialog = new VaiTroDialog(null, null, getRoleService());
        dialog.setVisible(true);
        if (dialog.isSucceeded()) {
            showTableData(true);
        }
    }

    @Override
    protected void handleEdit() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một vai trò từ bảng để chỉnh sửa.", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Optional<Vaitro> role = roleService.findById(selectedId);
            if (role.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin vai trò được chọn.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            VaiTroDialog dialog = new VaiTroDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    role.get(),
                    roleService);
            dialog.setVisible(true);
            if (dialog.isSucceeded()) {
                showTableData(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin vai trò: " + ex.getMessage(), "Lỗi hệ thống",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected void handleDelete() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một vai trò từ bảng để xóa", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa vai trò này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                getRoleService().deleteById(selectedId);
                showTableData(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa vai trò: " + e.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void attachCustomEvents() {
        // Load initial data
        showTableData(false);
    }
}
