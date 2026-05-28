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
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

public class NavbarMainFrame {
    static NguoiDung currentUser = AppSys.getNguoiDung();

    public static void init(NavBar navBar) {

        navBar.addHeader(accountNavBar());

        navBar.addNavButton("🏠", "Trang chủ", PageKey.HOME);

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
        accountNavBar.setBackground(theme.background);
        accountNavBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        String username = (currentUser != null) ? currentUser.getUsername() : "Chưa đăng nhập";
        String fullName = (currentUser != null) ? currentUser.getFullName() : "";
        String role = (currentUser != null) ? currentUser.getRole().toString() : "";

        JLabel avatar = new JLabel("👤", SwingConstants.CENTER);
        avatar.setFont(AppFont.getEmojiFont(Font.PLAIN, 30));
        avatar.setPreferredSize(new Dimension(56, 56));
        avatar.setMaximumSize(new Dimension(56, 56));
        avatar.setOpaque(true);
        avatar.setBackground(theme.accent);
        avatar.setForeground(theme.buttonForeground);
        avatar.setBorder(BorderFactory.createLineBorder(theme.borderColor, 1));

        JLabel lblUsername = new JLabel(username);
        lblUsername.setFont(ThemeManager.getBoldFont(15));
        lblUsername.setForeground(theme.textPrimary);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new javax.swing.BoxLayout(infoPanel, javax.swing.BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.add(lblUsername);

        if (!fullName.isBlank()) {
            JLabel lblFullName = new JLabel(fullName);
            lblFullName.setFont(ThemeManager.getFont(13));
            lblFullName.setForeground(theme.textSecondary);
            infoPanel.add(lblFullName);
        }

        if (!role.isBlank()) {
            JLabel lblRole = new JLabel(role);
            lblRole.setFont(ThemeManager.getFont(13));
            lblRole.setForeground(theme.textSecondary);
            infoPanel.add(lblRole);
        }

        if (currentUser == null) {
            JLabel lblHint = new JLabel("Vui lòng đăng nhập để tiếp tục");
            lblHint.setFont(ThemeManager.getFont(13));
            lblHint.setForeground(theme.textSecondary);
            infoPanel.add(lblHint);
        }

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new javax.swing.BoxLayout(topPanel, javax.swing.BoxLayout.X_AXIS));
        topPanel.setOpaque(false);
        topPanel.add(avatar);
        topPanel.add(Box.createHorizontalStrut(12));
        topPanel.add(infoPanel);
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        accountNavBar.add(topPanel);
        accountNavBar.add(Box.createVerticalStrut(16));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(theme.borderColor);
        separator.setOpaque(true);
        separator.setBackground(theme.borderColor);
        accountNavBar.add(separator);
        accountNavBar.add(Box.createVerticalStrut(14));

        return accountNavBar;
    }
}