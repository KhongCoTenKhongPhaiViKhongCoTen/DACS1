package com.shopapp.ui.frame;

import com.shopapp.AppSys;
import com.shopapp.entity.NguoiDung;
import com.shopapp.ui.components.NavBar;
import com.shopapp.ui.themes.Theme;
import com.shopapp.ui.themes.ThemeManager;
import com.shopapp.ui.themes.AppFont;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

public class NavbarMainFrame {

    public static void init(NavBar navBar) {

        navBar.addHeader(accountNavBar());

        navBar.addNavButton("🏠", "Trang chủ", PageKey.HOME);

        // Quản lý tài khoản section - requires user permissions
        if (AppSys.hasAnyPermissionByCode("USER_READ", "USER_CREATE", "USER_UPDATE", "USER_DELETE")) {
            navBar.addNavExpandableSection("", "Quản lý tài khoản",
                    PageKey.AccountManagement.getItemName(),
                    PageKey.AccountManagement.getListkey());
        }
        
        // Kho hàng section - requires inventory permissions
        if (AppSys.hasAnyPermissionByCode("INVENTORY_READ", "INVENTORY_UPDATE")) {
            navBar.addNavExpandableSection("", "Kho hàng",
                    PageKey.KhoHang.getItemName(),
                    PageKey.KhoHang.getListkey());
        }

        // Đơn hàng section - requires order permissions
        if (AppSys.hasAnyPermissionByCode("ORDER_READ", "ORDER_CREATE", "ORDER_UPDATE", "ORDER_DELETE")) {
            navBar.addNavButton(null, "Đơn Hàng", PageKey.DON_HANG);
        }

        // Khách hàng section - requires customer permissions
        if (AppSys.hasAnyPermissionByCode("CUSTOMER_READ", "CUSTOMER_CREATE", "CUSTOMER_UPDATE", "CUSTOMER_DELETE")) {
            navBar.addNavButton(null, "Khách Hàng", PageKey.KHACH_HANG);;
        }


        navBar.addVerticalGlue();
        navBar.addNavButton("⚙️", "Cài đặt", PageKey.SETTINGS);
    }

    private static JPanel accountNavBar() {
        Theme theme = ThemeManager.getCurrentTheme();
        NguoiDung currentUser = AppSys.getNguoiDung();

        JPanel accountNavBar = new JPanel();
        accountNavBar.setLayout(new javax.swing.BoxLayout(accountNavBar, javax.swing.BoxLayout.Y_AXIS));
        accountNavBar.setBorder(new EmptyBorder(18, 16, 18, 16));
        accountNavBar.setOpaque(true);
        accountNavBar.putClientProperty("themeBg", "background");
        accountNavBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        String username = (currentUser != null) ? currentUser.getUsername() : "Chưa đăng nhập";
        String fullName = (currentUser != null) ? currentUser.getFullName() : "";
        String role = (currentUser != null) ? currentUser.getRole().toString() : "";

        JLabel avatar = new JLabel("👤", SwingConstants.CENTER);
        avatar.setFont(AppFont.getEmojiFont(Font.PLAIN, 30));
        avatar.setPreferredSize(new Dimension(56, 56));
        avatar.setMaximumSize(new Dimension(56, 56));
        avatar.setOpaque(true);
        avatar.putClientProperty("themeBg", "accent");
        avatar.putClientProperty("themeFg", "buttonForeground");
        avatar.putClientProperty("isAvatar", Boolean.TRUE);

        JLabel lblUsername = new JLabel(username);
        lblUsername.setFont(ThemeManager.getBoldFont(15));
        lblUsername.putClientProperty("themeText", "primary");

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new javax.swing.BoxLayout(infoPanel, javax.swing.BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.putClientProperty("themeBg", "background");
        infoPanel.add(lblUsername);

        if (!fullName.isBlank()) {
            JLabel lblFullName = new JLabel(fullName);
            lblFullName.setFont(ThemeManager.getFont(13));
            lblFullName.putClientProperty("themeText", "secondary");
            infoPanel.add(lblFullName);
        }

        if (!role.isBlank()) {
            JLabel lblRole = new JLabel(role);
            lblRole.setFont(ThemeManager.getFont(13));
            lblRole.putClientProperty("themeText", "secondary");
            infoPanel.add(lblRole);
        }

        if (currentUser == null) {
            JLabel lblHint = new JLabel("Vui lòng đăng nhập để tiếp tục");
            lblHint.setFont(ThemeManager.getFont(13));
            lblHint.putClientProperty("themeText", "secondary");
            infoPanel.add(lblHint);
        }

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new javax.swing.BoxLayout(topPanel, javax.swing.BoxLayout.X_AXIS));
        topPanel.setOpaque(false);
        topPanel.putClientProperty("themeBg", "background");
        topPanel.add(avatar);
        topPanel.add(Box.createHorizontalStrut(12));
        topPanel.add(infoPanel);
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        accountNavBar.add(topPanel);
        accountNavBar.add(Box.createVerticalStrut(16));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.putClientProperty("themeFg", "borderColor");
        separator.putClientProperty("themeBg", "borderColor");
        accountNavBar.add(separator);
        accountNavBar.add(Box.createVerticalStrut(14));

        // Apply initial theme colors
        applyAccountNavBarTheme(accountNavBar, theme);

        // Add listener for theme changes
        ThemeManager.addThemeChangeListener(new ThemeManager.ThemeChangeListener() {
            @Override
            public void onThemeChanged(Theme newTheme) {
                applyAccountNavBarTheme(accountNavBar, newTheme);
            }
        });

        return accountNavBar;
    }

    private static void applyAccountNavBarTheme(JPanel accountNavBar, Theme theme) {
        applyThemeToComponentRecursive(accountNavBar, theme);
    }

    private static void applyThemeToComponentRecursive(Component comp, Theme theme) {
        if (comp instanceof JPanel) {
            JPanel panel = (JPanel) comp;
            Object bgProp = panel.getClientProperty("themeBg");
            if (bgProp != null) {
                switch (bgProp.toString()) {
                    case "background":
                        panel.setBackground(theme.background);
                        break;
                    case "accent":
                        panel.setBackground(theme.accent);
                        break;
                    case "borderColor":
                        panel.setBackground(theme.borderColor);
                        break;
                }
            }
        } else if (comp instanceof JLabel) {
            JLabel lbl = (JLabel) comp;
            Object fgProp = lbl.getClientProperty("themeFg");
            Object textProp = lbl.getClientProperty("themeText");
            Object borderProp = lbl.getClientProperty("themeBorder");
            Object isAvatar = lbl.getClientProperty("isAvatar");

            if (fgProp != null) {
                switch (fgProp.toString()) {
                    case "buttonForeground":
                        lbl.setForeground(theme.buttonForeground);
                        break;
                    case "textPrimary":
                        lbl.setForeground(theme.textPrimary);
                        break;
                    case "textSecondary":
                        lbl.setForeground(theme.textSecondary);
                        break;
                }
            } else if (textProp != null) {
                switch (textProp.toString()) {
                    case "primary":
                        lbl.setForeground(theme.textPrimary);
                        break;
                    case "secondary":
                        lbl.setForeground(theme.textSecondary);
                        break;
                }
            }

            if (borderProp != null) {
                switch (borderProp.toString()) {
                    case "borderColor":
                        lbl.setForeground(theme.borderColor);
                        break;
                }
            }

            Object bgProp = lbl.getClientProperty("themeBg");
            if (bgProp != null) {
                switch (bgProp.toString()) {
                    case "background":
                        lbl.setBackground(theme.background);
                        break;
                    case "accent":
                        lbl.setBackground(theme.accent);
                        break;
                    case "borderColor":
                        lbl.setBackground(theme.borderColor);
                        break;
                }
            }

            // Handle avatar border specially
            if (Boolean.TRUE.equals(isAvatar)) {
                lbl.setBorder(BorderFactory.createLineBorder(theme.borderColor, 1));
            }
        } else if (comp instanceof JSeparator) {
            JSeparator separator = (JSeparator) comp;
            Object fgProp = separator.getClientProperty("themeFg");
            Object bgProp = separator.getClientProperty("themeBg");

            if (fgProp != null && "borderColor".equals(fgProp.toString())) {
                separator.setForeground(theme.borderColor);
            }
            if (bgProp != null && "borderColor".equals(bgProp.toString())) {
                separator.setBackground(theme.borderColor);
            }
        }

        if (comp instanceof Container) {
            Container container = (Container) comp;
            for (Component child : container.getComponents()) {
                applyThemeToComponentRecursive(child, theme);
            }
        }
    }
}