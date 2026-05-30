package com.shopapp.ui.frame.panels;

import java.util.List;
import java.util.Optional;
import java.awt.Frame;
import javax.swing.*;
import com.shopapp.entity.Quyen;
import com.shopapp.repository.impl.QuyenRepositoryImpl;
import com.shopapp.service.QuyenService;
import com.shopapp.service.impl.QuyenServiceImpl;
import com.shopapp.ui.components.BasePage;
import com.shopapp.ui.frame.panels.Dialog.QuyenDialog;
import com.shopapp.ui.themes.ThemeManager;

public class QuyenPage extends BasePage {

    private QuyenService quyenService;

    private JComboBox<String> cbFilterModule;

    public QuyenPage() {
        super(new String[] {
                "ID",
                "Mã quyền",
                "Tên quyền",
                "Module"
        });
    }

    private QuyenService getQuyenService() {
        if (quyenService == null) {
            quyenService = new QuyenServiceImpl(new QuyenRepositoryImpl());
        }
        return quyenService;
    }

    @Override
    protected void addCustomFilters() {
        JLabel lblModule = new JLabel("Module:");
        lblModule.setFont(ThemeManager.getFont(12));

        cbFilterModule = new JComboBox<>(new String[] {
                "Tất cả", "PRODUCT", "ORDER", "USER", "REPORT"
        });
        cbFilterModule.setFont(ThemeManager.getFont(12));
        cbFilterModule.addActionListener(e -> showTableData(true));

        filterPanel.add(lblModule);
        filterPanel.add(cbFilterModule);
    }

    @Override
    protected void applyTheme() {
        super.applyTheme();
        com.shopapp.ui.themes.Theme theme = ThemeManager.getCurrentTheme();
        if (cbFilterModule != null) {
            cbFilterModule.setBackground(theme.buttonBackground);
            cbFilterModule.setForeground(theme.textPrimary);
        }
    }

    @Override
    public void showTableData(boolean applyFilter) {
        tableModel.setRowCount(0);
        try {
            List<Quyen> list = getQuyenService().findAll();
            String searchQuery = tfSearch.getText().trim().toLowerCase();
            String filterModule = cbFilterModule != null
                    ? (String) cbFilterModule.getSelectedItem()
                    : "Tất cả";

            for (Quyen q : list) {
                // Search filter: ID, code, name
                if (applyFilter && !searchQuery.isEmpty()) {
                    String idStr = String.valueOf(q.getPermissionId());
                    String code = q.getPermissionCode() != null ? q.getPermissionCode().toLowerCase() : "";
                    String name = q.getPermissionName() != null ? q.getPermissionName().toLowerCase() : "";
                    if (!idStr.contains(searchQuery)
                            && !code.contains(searchQuery)
                            && !name.contains(searchQuery)) {
                        continue;
                    }
                }

                // Module filter
                if (applyFilter && filterModule != null && !filterModule.equals("Tất cả")) {
                    if (!filterModule.equalsIgnoreCase(q.getModule())) {
                        continue;
                    }
                }

                addRow(new Object[] {
                        q.getPermissionId(),
                        q.getPermissionCode(),
                        q.getPermissionName(),
                        q.getModule()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải dữ liệu quyền: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    protected void handleFilter() {
        showTableData(true);
    }

    @Override
    protected void attachCustomEvents() {
        showTableData(false);
    }

    @Override
    protected void handleAdd() {
        QuyenDialog dialog = new QuyenDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                null,
                getQuyenService());
        dialog.setVisible(true);
        if (dialog.isSucceeded()) {
            showTableData(false);
        }
    }

    @Override
    protected void handleEdit() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một quyền từ bảng để chỉnh sửa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Optional<Quyen> optional = getQuyenService().findById(selectedId);
            if (optional.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy quyền được chọn.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            QuyenDialog dialog = new QuyenDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    optional.get(),
                    getQuyenService());
            dialog.setVisible(true);
            if (dialog.isSucceeded()) {
                showTableData(false);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lấy thông tin quyền: " + ex.getMessage(),
                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected void handleDelete() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một quyền từ bảng để xóa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa quyền này?\nThao tác này không thể hoàn tác!",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                getQuyenService().deleteById(selectedId);
                JOptionPane.showMessageDialog(this,
                        "Xóa quyền thành công!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                showTableData(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xóa quyền: " + ex.getMessage(),
                        "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
