package com.shopapp.ui.frame.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

import com.shopapp.ui.themes.*;

/**
 * Trang Settings với khả năng đổi theme
 */
public class SettingsPage extends JPanel
        implements ThemeManager.ThemeChangeListener {

    private JRadioButton rdoDark;
    private JRadioButton rdoLight;
    private JLabel lblThemePreview;

    public SettingsPage() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel lblHeader = new JLabel("⚙️ Settings");
        lblHeader.setFont(ThemeManager.getBoldFont(28));
        add(lblHeader, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Theme section
        JPanel themeSection = createThemeSection();
        contentPanel.add(themeSection);
        contentPanel.add(Box.createVerticalStrut(30));

        // About section
        JPanel aboutSection = createAboutSection();
        contentPanel.add(aboutSection);
        contentPanel.add(Box.createVerticalGlue());

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Apply theme
        applyTheme();
        ThemeManager.addThemeChangeListener(this);
    }

    /**
     * Tạo phần Theme settings
     */
    private JPanel createThemeSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Theme"));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTheme = new JLabel("Select Theme:");
        lblTheme.setFont(ThemeManager.getBoldFont(14));
        panel.add(lblTheme);
        panel.add(Box.createVerticalStrut(10));

        rdoDark = new JRadioButton("Dark Mode");
        rdoLight = new JRadioButton("Light Mode");

        // Config radio buttons
        for (JRadioButton rdo : new JRadioButton[] { rdoDark, rdoLight }) {
            rdo.setFont(ThemeManager.getFont(12));
            rdo.setFocusPainted(false);
            rdo.setOpaque(false); // ← Transparent background
        }

        Theme currentTheme = ThemeManager.getCurrentTheme();
        if (currentTheme == Theme.DARK) {
            rdoDark.setSelected(true);
        } else {
            rdoLight.setSelected(true);
        }

        ButtonGroup group = new ButtonGroup();
        group.add(rdoDark);
        group.add(rdoLight);

        rdoDark.addActionListener(e -> {
            if (rdoDark.isSelected()) {
                ThemeManager.setTheme(Theme.DARK);
            }
        });

        rdoLight.addActionListener(e -> {
            if (rdoLight.isSelected()) {
                ThemeManager.setTheme(Theme.LIGHT);
            }
        });

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        radioPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        radioPanel.setOpaque(false); // ← Transparent

        radioPanel.add(rdoDark);
        radioPanel.add(Box.createVerticalStrut(5));
        radioPanel.add(rdoLight);

        panel.add(radioPanel);
        panel.add(Box.createVerticalStrut(15));

        JLabel lblPreviewTitle = new JLabel("Preview:");
        lblPreviewTitle.setFont(ThemeManager.getBoldFont(12));
        panel.add(lblPreviewTitle);

        lblThemePreview = new JLabel("This is preview text");
        lblThemePreview.setFont(ThemeManager.getFont(12));
        lblThemePreview.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(lblThemePreview);

        panel.setMaximumSize(new Dimension(400, 220));
        panel.setOpaque(false); // ← Transparent background

        return panel;
    }

    /**
     * Tạo phần About
     */
    private JPanel createAboutSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("About"));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblAppName = new JLabel("Shop Quần Áo");
        lblAppName.setFont(ThemeManager.getBoldFont(14));
        panel.add(lblAppName);

        JLabel lblVersion = new JLabel("Version: 1.0.0");
        lblVersion.setFont(ThemeManager.getFont(12));
        panel.add(lblVersion);

        panel.add(Box.createVerticalStrut(5));

        JLabel lblAuthor = new JLabel("Author: Your Name");
        lblAuthor.setFont(ThemeManager.getFont(12));
        panel.add(lblAuthor);

        panel.setMaximumSize(new Dimension(400, 120));

        return panel;
    }

    private void applyTheme() {
        Theme theme = ThemeManager.getCurrentTheme();

        // Apply to panel
        setBackground(theme.background);
        setForeground(theme.foreground);

        // Apply to all components
        applyThemeToComponent(this, theme);

        // Update preview text color
        if (lblThemePreview != null) {
            lblThemePreview.setForeground(theme.textPrimary);
        }

        // Update radio buttons
        if (rdoDark != null) {
            rdoDark.setForeground(theme.textPrimary);
        }
        if (rdoLight != null) {
            rdoLight.setForeground(theme.textPrimary);
        }
    }

    /**
     * Recursively apply theme to all components
     */
    private void applyThemeToComponent(Component comp, Theme theme) {
        if (comp instanceof JLabel) {
            ((JLabel) comp).setForeground(theme.textPrimary);
        } else if (comp instanceof JRadioButton) {
            ((JRadioButton) comp).setForeground(theme.textPrimary);
        } else if (comp instanceof JPanel) {
            ((JPanel) comp).setBackground(theme.background);
            ((JPanel) comp).setForeground(theme.foreground);
        }

        if (comp instanceof Container) {
            Container container = (Container) comp;
            for (Component child : container.getComponents()) {
                applyThemeToComponent(child, theme);
            }
        }
    }

    @Override
    public void onThemeChanged(Theme theme) {
        applyTheme();

        // Update radio button selection
        if (theme == Theme.DARK) {
            rdoDark.setSelected(true);
        } else {
            rdoLight.setSelected(true);
        }
    }

    public void cleanup() {
        ThemeManager.removeThemeChangeListener(this);
    }
}
