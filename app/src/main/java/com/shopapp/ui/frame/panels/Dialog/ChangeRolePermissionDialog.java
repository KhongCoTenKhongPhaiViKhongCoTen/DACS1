package com.shopapp.ui.frame.panels.Dialog;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.shopapp.AppSys;
import com.shopapp.entity.Quyen;
import com.shopapp.entity.RolePermissions;
import com.shopapp.entity.Vaitro;
import com.shopapp.repository.RolePermissionsRepository;
import com.shopapp.repository.impl.RolePermissionsRepositoryImpl;
import com.shopapp.repository.impl.QuyenRepositoryImpl;
import com.shopapp.service.QuyenService;
import com.shopapp.service.impl.QuyenServiceImpl;
import com.shopapp.ui.themes.Theme;

/**
 * Dialog phân quyền cho vai trò.
 * Hiển thị tất cả quyền theo nhóm module với checkbox để bật/tắt.
 */
public class ChangeRolePermissionDialog extends JDialog {

    private final Vaitro role;
    private final QuyenService quyenService;
    private final RolePermissionsRepository rolePermissionsRepository;

    // Map: permissionId → JCheckBox
    private final Map<Integer, JCheckBox> checkBoxMap = new LinkedHashMap<>();

    // Tên hiển thị cho module
    private static final Map<String, String> MODULE_LABELS = new LinkedHashMap<>();
    static {
        MODULE_LABELS.put("USER", "Người dùng");
        MODULE_LABELS.put("PRODUCT", "Sản phẩm");
        MODULE_LABELS.put("ORDER", "Đơn hàng");
        MODULE_LABELS.put("CUSTOMER", "Khách hàng");
        MODULE_LABELS.put("INVENTORY", "Tồn kho");
        MODULE_LABELS.put("REPORT", "Báo cáo");
    }

    private boolean succeeded = false;

    public ChangeRolePermissionDialog(Frame owner, Vaitro role) {
        super(owner, "Phân quyền cho vai trò: " + role.getRoleName(), true);
        this.role = role;
        this.quyenService = new QuyenServiceImpl(new QuyenRepositoryImpl());
        this.rolePermissionsRepository = new RolePermissionsRepositoryImpl();

        buildUI();
        applyTheme();
        pack();
        setMinimumSize(new Dimension(520, 400));
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────────────
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Phân quyền — " + role.getRoleName());
        titleLabel.setFont(AppSys.themes.getBoldFont(18));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // ── Content: checkbox grid theo module ────────────────────────────
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Lấy tất cả quyền và quyền hiện tại của vai trò
        List<Quyen> allPermissions = quyenService.findAll();
        List<RolePermissions> currentPerms = rolePermissionsRepository.findByRoleId(role.getRoleId());
        Set<Integer> grantedIds = currentPerms.stream()
                .map(rp -> rp.getPermission().getPermissionId())
                .collect(Collectors.toSet());

        // Nhóm quyền theo module
        Map<String, List<Quyen>> grouped = new LinkedHashMap<>();
        for (String moduleKey : MODULE_LABELS.keySet()) {
            grouped.put(moduleKey, new ArrayList<>());
        }
        for (Quyen q : allPermissions) {
            grouped.computeIfAbsent(q.getModule(), k -> new ArrayList<>()).add(q);
        }

        // Tạo panel cho từng module
        for (Map.Entry<String, List<Quyen>> entry : grouped.entrySet()) {
            List<Quyen> perms = entry.getValue();
            if (perms.isEmpty()) continue;

            String moduleLabel = MODULE_LABELS.getOrDefault(entry.getKey(), entry.getKey());
            JPanel modulePanel = new JPanel(new GridLayout(0, 2, 8, 4));
            modulePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            TitledBorder border = BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(AppSys.themes.getCurrent().borderColor, 1),
                    moduleLabel);
            border.setTitleFont(AppSys.themes.getBoldFont(13));
            border.setTitleColor(AppSys.themes.getCurrent().accent);
            modulePanel.setBorder(BorderFactory.createCompoundBorder(
                    border, new EmptyBorder(6, 10, 6, 10)));

            // Nút chọn tất cả / bỏ tất cả cho module
            JPanel moduleHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            moduleHeaderPanel.setOpaque(false);
            JButton btnSelectAll = new JButton("Chọn tất cả");
            JButton btnDeselectAll = new JButton("Bỏ tất cả");
            btnSelectAll.setFont(AppSys.themes.getFont(10));
            btnDeselectAll.setFont(AppSys.themes.getFont(10));
            btnSelectAll.setFocusPainted(false);
            btnDeselectAll.setFocusPainted(false);
            moduleHeaderPanel.add(btnSelectAll);
            moduleHeaderPanel.add(Box.createHorizontalStrut(4));
            moduleHeaderPanel.add(btnDeselectAll);

            List<JCheckBox> moduleCheckboxes = new ArrayList<>();

            for (Quyen q : perms) {
                JCheckBox cb = new JCheckBox(q.getPermissionName());
                cb.setFont(AppSys.themes.getFont(13));
                cb.setSelected(grantedIds.contains(q.getPermissionId()));
                cb.setOpaque(false);
                checkBoxMap.put(q.getPermissionId(), cb);
                modulePanel.add(cb);
                moduleCheckboxes.add(cb);
            }

            // Padding cho grid lẻ
            if (perms.size() % 2 != 0) {
                modulePanel.add(new JLabel());
            }

            btnSelectAll.addActionListener(e -> moduleCheckboxes.forEach(cb -> cb.setSelected(true)));
            btnDeselectAll.addActionListener(e -> moduleCheckboxes.forEach(cb -> cb.setSelected(false)));

            // Wrapper gộp header + grid
            JPanel wrapperPanel = new JPanel();
            wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
            wrapperPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            wrapperPanel.setOpaque(false);

            moduleHeaderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            modulePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            modulePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, modulePanel.getPreferredSize().height + 20));

            wrapperPanel.add(modulePanel);
            wrapperPanel.add(Box.createVerticalStrut(2));
            wrapperPanel.add(moduleHeaderPanel);

            contentPanel.add(wrapperPanel);
            contentPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // ── Buttons ───────────────────────────────────────────────────────
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        btnSave.setFont(AppSys.themes.getBoldFont(14));
        btnCancel.setFont(AppSys.themes.getBoldFont(14));
        btnSave.setFocusPainted(false);
        btnCancel.setFocusPainted(false);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> handleSave());
    }

    private void handleSave() {
        try {
            // Lấy quyền hiện tại
            List<RolePermissions> currentPerms = rolePermissionsRepository.findByRoleId(role.getRoleId());
            Set<Integer> currentIds = currentPerms.stream()
                    .map(rp -> rp.getPermission().getPermissionId())
                    .collect(Collectors.toSet());

            // Tính toán thay đổi
            Set<Integer> selectedIds = new HashSet<>();
            for (Map.Entry<Integer, JCheckBox> entry : checkBoxMap.entrySet()) {
                if (entry.getValue().isSelected()) {
                    selectedIds.add(entry.getKey());
                }
            }

            // Xóa quyền bị bỏ chọn
            for (Integer permId : currentIds) {
                if (!selectedIds.contains(permId)) {
                    rolePermissionsRepository.deleteByRoleIdAndPermissionId(
                            role.getRoleId(), permId);
                }
            }

            // Thêm quyền mới được chọn
            for (Integer permId : selectedIds) {
                if (!currentIds.contains(permId)) {
                    Optional<Quyen> quyenOpt = quyenService.findById(permId);
                    if (quyenOpt.isPresent()) {
                        RolePermissions rp = new RolePermissions();
                        rp.setRole(role);
                        rp.setPermission(quyenOpt.get());
                        rp.setGrantedAt(LocalDateTime.now());
                        rolePermissionsRepository.save(rp);
                    }
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Cập nhật quyền cho vai trò \"" + role.getRoleName() + "\" thành công!\n"
                    + "Đã gán " + selectedIds.size() + " quyền.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            succeeded = true;
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lưu phân quyền: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void applyTheme() {
        Theme theme = AppSys.themes.getCurrent();
        applyThemeRecursive(this, theme);
    }

    private void applyThemeRecursive(Container container, Theme theme) {
        container.setBackground(theme.background);
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel label) {
                label.setForeground(theme.textPrimary);
            } else if (comp instanceof JCheckBox chk) {
                chk.setForeground(theme.textPrimary);
            } else if (comp instanceof JButton btn) {
                btn.setBackground(theme.buttonBackground);
                btn.setForeground(theme.buttonForeground);
            } else if (comp instanceof Container child) {
                applyThemeRecursive(child, theme);
            }
        }
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
