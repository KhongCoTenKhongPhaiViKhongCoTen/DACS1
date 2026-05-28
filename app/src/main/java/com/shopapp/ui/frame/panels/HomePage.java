package com.shopapp.ui.frame.panels;

import javax.swing.*;
import java.awt.*;

import com.shopapp.ui.themes.*;

/**
 * Trang Home
 */
public class HomePage extends JPanel
        implements ThemeManager.ThemeChangeListener {

    private JLabel lblWelcome;
    private JLabel lblDesc;

    public HomePage() {
        setLayout(new BorderLayout());

        // Center panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Welcome label
        lblWelcome = new JLabel("Welcome to Shop Quần Áo");
        lblWelcome.setFont(ThemeManager.getBoldFont(32.0f));
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(lblWelcome, gbc);

        // Description
        lblDesc = new JLabel("Quản lý cửa hàng quần áo dễ dàng");
        lblDesc.setFont(ThemeManager.getFont(16.0f));
        lblDesc.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(lblDesc, gbc);

        // Add some space
        centerPanel.add(Box.createVerticalStrut(30), gbc);

        // Features
        JLabel lblFeatures = new JLabel("Tính năng:");
        lblFeatures.setFont(ThemeManager.getBoldFont(14.0f));
        centerPanel.add(lblFeatures, gbc);

        JLabel lblFeature1 = new JLabel("✓ Quản lý khách hàng");
        lblFeature1.setFont(ThemeManager.getFont(12.0f));
        centerPanel.add(lblFeature1, gbc);

        JLabel lblFeature2 = new JLabel("✓ Theo dõi sản phẩm");
        lblFeature2.setFont(ThemeManager.getFont(12.0f));
        centerPanel.add(lblFeature2, gbc);

        JLabel lblFeature3 = new JLabel("✓ Đổi theme dễ dàng");
        lblFeature3.setFont(ThemeManager.getFont(12.0f));
        centerPanel.add(lblFeature3, gbc);

        add(centerPanel, BorderLayout.CENTER);

        applyTheme();
        ThemeManager.addThemeChangeListener(this);
    }

    private void applyTheme() {
        Theme theme = ThemeManager.getCurrentTheme();
        setBackground(theme.background);

    }

    @Override
    public void onThemeChanged(Theme theme) {
        applyTheme();
    }

    public void cleanup() {
        ThemeManager.removeThemeChangeListener(this);
    }
}
