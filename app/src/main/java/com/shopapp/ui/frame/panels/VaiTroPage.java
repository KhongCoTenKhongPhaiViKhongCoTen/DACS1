package com.shopapp.ui.frame.panels;

import java.util.List;
import java.util.Optional;

import javax.swing.*;

import com.shopapp.entity.Vaitro;
import com.shopapp.repository.impl.VaitroRepositoryImpl;
import com.shopapp.service.VaitroService;
import com.shopapp.service.impl.VaitroServiceImpl;
import com.shopapp.ui.components.BasePage;
import com.shopapp.ui.frame.panels.Dialog.VaiTroDialog;

public class VaiTroPage extends BasePage {

    private VaitroService roleService;
    private JButton changeRole; // khởi tạo trong addCustomButtons()

    public VaiTroPage() {
        super(new String[] {
                "ID",
                "Tên vai trò",
                "Mô tả"
        });
    }

    // ── Lazy init service ─────────────────────────────────────────────────────

    private VaitroService getRoleService() {
        if (roleService == null) {
            roleService = new VaitroServiceImpl(new VaitroRepositoryImpl());
        }
        return roleService;
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
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải dữ liệu vai trò: " + e.getMessage(),
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
        VaiTroDialog dialog = new VaiTroDialog(getParentFrame(), null, getRoleService());
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
                    "Vui lòng chọn một vai trò từ bảng để chỉnh sửa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Dùng getRoleService() thay vì roleService trực tiếp để tránh NPE
            Optional<Vaitro> role = getRoleService().findById(selectedId);
            if (role.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy thông tin vai trò được chọn.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            VaiTroDialog dialog = new VaiTroDialog(getParentFrame(), role.get(), getRoleService());
            dialog.setVisible(true);
            if (dialog.isSucceeded()) {
                showTableData(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lấy thông tin vai trò: " + ex.getMessage(),
                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected void handleDelete() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một vai trò từ bảng để xóa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xóa vai trò: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ── Custom buttons & events ───────────────────────────────────────────────

    @Override
    protected void addCustomButtons() {
        changeRole = new JButton("Thay đổi quyền"); // tạo tại đây, tránh double-add
        buttonPanel.add(changeRole);
    }

    @Override
    protected void attachCustomEvents() {
        changeRole.addActionListener(e -> handleChangeRole());

        // Load dữ liệu ban đầu
        showTableData(false);
    }

    // ── Xử lý thay đổi quyền ─────────────────────────────────────────────────

    private void handleChangeRole() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một vai trò từ bảng để thay đổi quyền.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // TODO: mở dialog phân quyền cho vai trò selectedId
        JOptionPane.showMessageDialog(this,
                "Chức năng phân quyền đang được phát triển.",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}