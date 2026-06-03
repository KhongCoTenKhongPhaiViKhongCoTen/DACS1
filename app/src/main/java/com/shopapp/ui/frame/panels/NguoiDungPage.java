package com.shopapp.ui.frame.panels;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javax.swing.*;
import com.shopapp.AppSys;
import com.shopapp.entity.NguoiDung;
import com.shopapp.entity.Vaitro;
import com.shopapp.repository.impl.NguoiDungRepositoryImpl;
import com.shopapp.repository.impl.VaitroRepositoryImpl;
import com.shopapp.service.NguoiDungService;
import com.shopapp.service.VaitroService;
import com.shopapp.service.impl.NguoiDungServiceImpl;
import com.shopapp.service.impl.VaitroServiceImpl;
import com.shopapp.ui.components.BasePage;
import com.shopapp.ui.frame.panels.Dialog.NguoiDungDialog;
import com.shopapp.ui.themes.Theme;

public class NguoiDungPage extends BasePage {

    private NguoiDungService userService;
    private VaitroService roleService;

    private JComboBox<String> cbFilterStatus;
    private JComboBox<Object> cbFilterRole;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public NguoiDungPage() {
        super(new String[] {
                "ID",
                "Username",
                "Tên người dùng",
                "Email",
                "Số điện thoại",
                "Thời gian tạo",
                "Trạng thái",
                "Vai trò"
        });
    }

    private NguoiDungService getUserService() {
        if (userService == null) {
            userService = new NguoiDungServiceImpl(new NguoiDungRepositoryImpl());
        }
        return userService;
    }

    private VaitroService getRoleService() {
        if (roleService == null) {
            roleService = new VaitroServiceImpl(new VaitroRepositoryImpl());
        }
        return roleService;
    }

    @Override
    protected void addCustomFilters() {
        // Status filter
        JLabel lblStatus = new JLabel("Trạng thái:");
        lblStatus.setFont(AppSys.themes.getFont(12));
        cbFilterStatus = new JComboBox<>(new String[] { "Tất cả", "Hoạt động", "Khóa" });
        cbFilterStatus.setFont(AppSys.themes.getFont(12));
        cbFilterStatus.addActionListener(e -> showTableData(true));

        // Role filter
        JLabel lblRole = new JLabel("Vai trò:");
        lblRole.setFont(AppSys.themes.getFont(12));

        cbFilterRole = new JComboBox<>();
        cbFilterRole.setFont(AppSys.themes.getFont(12));
        cbFilterRole.addItem("Tất cả");

        try {
            List<Vaitro> roles = getRoleService().findAll();
            for (Vaitro role : roles) {
                cbFilterRole.addItem(role);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cbFilterRole.addActionListener(e -> showTableData(true));

        filterPanel.add(lblStatus);
        filterPanel.add(cbFilterStatus);
        filterPanel.add(lblRole);
        filterPanel.add(cbFilterRole);
    }

    @Override
    protected void applyTheme() {
        super.applyTheme();
        Theme theme = AppSys.themes.getCurrent();
        if (cbFilterStatus != null) {
            cbFilterStatus.setBackground(theme.buttonBackground);
            cbFilterStatus.setForeground(theme.textPrimary);
        }
        if (cbFilterRole != null) {
            cbFilterRole.setBackground(theme.buttonBackground);
            cbFilterRole.setForeground(theme.textPrimary);
        }
    }

    @Override
    public void showTableData(boolean applyFilter) {
        tableModel.setRowCount(0);
        try {
            List<NguoiDung> users = getUserService().findAll();
            String searchQuery = tfSearch.getText().trim().toLowerCase();
            String filterStatus = cbFilterStatus != null ? (String) cbFilterStatus.getSelectedItem() : "Tất cả";
            Object filterRoleObj = cbFilterRole != null ? cbFilterRole.getSelectedItem() : "Tất cả";

            for (NguoiDung user : users) {
                // Apply search filter (ID, Username, Fullname)
                if (applyFilter && !searchQuery.isEmpty()) {
                    String idStr = String.valueOf(user.getUserId());
                    String username = user.getUsername().toLowerCase();
                    String fullName = user.getFullName().toLowerCase();
                    if (!idStr.contains(searchQuery) && !username.contains(searchQuery)
                            && !fullName.contains(searchQuery)) {
                        continue;
                    }
                }

                // Apply status filter
                if (applyFilter && !filterStatus.equals("Tất cả")) {
                    boolean activeFilter = filterStatus.equals("Hoạt động");
                    if (user.getIsActive() != activeFilter) {
                        continue;
                    }
                }

                // Apply role filter
                if (applyFilter && filterRoleObj instanceof Vaitro selectedRole) {
                    if (user.getRole() == null || !user.getRole().getRoleId().equals(selectedRole.getRoleId())) {
                        continue;
                    }
                }

                // Add row to table
                String roleName = user.getRole() != null ? user.getRole().getRoleName() : "";
                String statusStr = user.getIsActive() ? "Hoạt động" : "Khóa";
                String createdAtStr = user.getCreatedAt() != null ? user.getCreatedAt().format(DATE_TIME_FORMATTER)
                        : "";

                addRow(new Object[] {
                        user.getUserId(),
                        user.getUsername(),
                        user.getFullName(),
                        user.getEmail() != null ? user.getEmail() : "",
                        user.getPhone() != null ? user.getPhone() : "",
                        createdAtStr,
                        statusStr,
                        roleName
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu người dùng: " + e.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    protected void handleFilter() {
        showTableData(true);
    }

    @Override
    protected void attachCustomEvents() {
        // Load initial data
        showTableData(false);
    }

    @Override
    protected void handleAdd() {
        NguoiDungDialog dialog = new NguoiDungDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                null,
                getUserService(),
                getRoleService());
        dialog.setVisible(true);
        if (dialog.isSucceeded()) {
            showTableData(false);
        }
    }

    @Override
    protected void handleEdit() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một người dùng từ bảng để chỉnh sửa.", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Optional<NguoiDung> optionalUser = getUserService().findById(selectedId);
            if (optionalUser.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin người dùng được chọn.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            NguoiDung user = optionalUser.get();

            NguoiDungDialog dialog = new NguoiDungDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    user,
                    getUserService(),
                    getRoleService());
            dialog.setVisible(true);
            if (dialog.isSucceeded()) {
                showTableData(false);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin người dùng: " + ex.getMessage(), "Lỗi hệ thống",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected void handleDelete() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một người dùng từ bảng để xóa.", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (AppSys.getNguoiDung() != null && AppSys.getNguoiDung().getUserId().equals(selectedId)) {
            JOptionPane.showMessageDialog(this, "Bạn không thể xóa tài khoản của chính mình đang đăng nhập!",
                    "Lỗi bảo mật", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa người dùng này? Thao tác này không thể hoàn tác!",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                getUserService().deleteById(selectedId);
                JOptionPane.showMessageDialog(this, "Xóa người dùng thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                showTableData(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa người dùng: " + ex.getMessage(), "Lỗi hệ thống",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
