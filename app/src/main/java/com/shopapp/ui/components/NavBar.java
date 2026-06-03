package com.shopapp.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.shopapp.ui.themes.*;
import com.shopapp.AppSys;
import com.shopapp.ui.listeners.PageChangeListener;

public class NavBar extends JPanel implements ThemeManager.ThemeChangeListener {

    private JPanel navBar;
    private JScrollPane scrollPane;
    private JButton currentSelectedButton;
    private List<PageChangeListener> pageListeners = new ArrayList<>();

    private Theme theme = AppSys.themes.getCurrent();

    int width;
    int height;

    public NavBar(int width, int height) {
        this.width = width;
        this.height = height;
        setLayout(new BorderLayout());
        initComponents();
        applyTheme();
        AppSys.themes.addListener(this);

        scrollPane = new JScrollPane(navBar);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Tốc độ cuộn
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Style
        scrollPane.getViewport().setBackground(theme.background);

        add(scrollPane, BorderLayout.WEST);
    }

    public NavBar(int width) {
        this(width, 56);
    }

    private void initComponents() {
        navBar = new JPanel();
        navBar.setLayout(new BoxLayout(navBar, BoxLayout.Y_AXIS));
        navBar.setMinimumSize(new Dimension(width, 0));
        navBar.setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
        navBar.setBorder(new EmptyBorder(14, 0, 14, 0));
        navBar.setOpaque(true);
    }

    public void addNavButton(String icon, String text, String pageKey) {
        JButton btn = createMenuButton(icon, text, pageKey);
        navBar.add(btn);
        navBar.add(Box.createVerticalStrut(6));
        if (currentSelectedButton == null) {
            selectButton(btn, pageKey);
        }
        navBar.revalidate();
        navBar.repaint();
    }

    public void addNavExpandableSection(String icon, String title, String[] itemNames, String[] pageKeys) {
        JPanel section = createExpandableSection(icon, title, itemNames, pageKeys);
        navBar.add(section);
        navBar.revalidate();
        navBar.repaint();
    }

    public void addVerticalGlue() {
        navBar.add(Box.createVerticalGlue());
        navBar.revalidate();
        navBar.repaint();
    }

    /**
     * Thêm thành phần tại đầu thanh navigation (trên cùng)
     */
    public void addHeader(Component c) {
        navBar.add(c, 0);
        navBar.revalidate();
        navBar.repaint();
    }

    public void addComponent(Component c) {
        navBar.add(c);
        navBar.revalidate();
        navBar.repaint();
    }

    /**
     * Tạo expandable section với indent style
     */
    private JPanel createExpandableSection(String icon, String title, String[] itemNames, String[] pageKeys) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionPanel.setOpaque(false);

        // State tracker
        boolean[] isExpanded = { false };

        // Content panel for header + sub-items
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.setOpaque(false);

        // Main expandable button
        IconTextButton btnToggle = new IconTextButton(icon, title, IconTextButton.Orientation.HORIZONTAL);
        btnToggle.setPreferredSize(new Dimension(width, height));
        btnToggle.setMaximumSize(new Dimension(width, height));
        btnToggle.setMinimumSize(new Dimension(width, height));
        btnToggle.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnToggle.setContentAreaFilled(false);
        btnToggle.setFocusPainted(false);
        btnToggle.setBorderPainted(false);
        btnToggle.setHorizontalAlignment(SwingConstants.LEFT);
        btnToggle.setForeground(theme.textPrimary);
        btnToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Sub-items container (indent)
        JPanel subItemsPanel = new JPanel();
        subItemsPanel.setLayout(new BoxLayout(subItemsPanel, BoxLayout.Y_AXIS));
        subItemsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subItemsPanel.setOpaque(false);
        subItemsPanel.setBorder(new EmptyBorder(0, 30, 0, 0)); // ← Indent 30px
        subItemsPanel.setVisible(false);

        // Tạo sub-buttons
        for (int i = 0; i < itemNames.length; i++) {
            JButton subBtn = createSubMenuButton(itemNames[i], pageKeys[i]);
            subItemsPanel.add(subBtn);
            subItemsPanel.add(Box.createVerticalStrut(2)); // ← Space giữa buttons
        }

        // Toggle button action
        btnToggle.addActionListener(e -> {
            isExpanded[0] = !isExpanded[0];
            subItemsPanel.setVisible(isExpanded[0]);
            sectionPanel.setMaximumSize(new Dimension(width, sectionPanel.getPreferredSize().height));

            sectionPanel.revalidate();
            sectionPanel.repaint();
        });

        // Hover effect cho toggle button
        btnToggle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btnToggle != currentSelectedButton) {
                    btnToggle.setBackground(theme.accent);
                    btnToggle.setContentAreaFilled(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btnToggle != currentSelectedButton) {
                    btnToggle.setContentAreaFilled(false);
                }
            }
        });

        contentPanel.add(btnToggle);
        contentPanel.add(Box.createVerticalStrut(5)); // ← Space trước sub-items
        contentPanel.add(subItemsPanel);

        sectionPanel.add(contentPanel);
        sectionPanel.setMaximumSize(new Dimension(width, sectionPanel.getPreferredSize().height));

        return sectionPanel;
    }

    /**
     * Tạo sub-menu button (indent inside)
     */
    private JButton createSubMenuButton(String text, String pageKey) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(width - 40, height));
        btn.setMaximumSize(new Dimension(width - 40, height));
        btn.setMinimumSize(new Dimension(width - 40, height));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        styleNavButton(btn, true);

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != currentSelectedButton) {
                    btn.setBackground(theme.accent);
                    btn.setOpaque(true);
                    btn.setContentAreaFilled(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != currentSelectedButton) {
                    btn.setOpaque(false);
                    btn.setContentAreaFilled(false);
                }
            }
        });

        btn.addActionListener(e -> selectButton(btn, pageKey));

        return btn;
    }

    /**
     * Tạo regular menu button
     */
    private JButton createMenuButton(String icon, String text, String pageKey) {
        IconTextButton btn = new IconTextButton(icon, text, IconTextButton.Orientation.HORIZONTAL);

        btn.setPreferredSize(new Dimension(width, height));
        btn.setMaximumSize(new Dimension(width, height));
        btn.setMinimumSize(new Dimension(width, height));

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        styleNavButton(btn, false);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != currentSelectedButton) {
                    btn.setBackground(theme.accent);
                    btn.setOpaque(true);
                    btn.setContentAreaFilled(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != currentSelectedButton) {
                    btn.setOpaque(false);
                    btn.setContentAreaFilled(false);
                }
            }
        });

        btn.addActionListener(e -> selectButton(btn, pageKey));

        return btn;
    }

    /**
     * Đăng ký listener
     */
    public void addPageChangeListener(PageChangeListener listener) {
        if (!pageListeners.contains(listener)) {
            pageListeners.add(listener);
        }
    }

    public void removePageChangeListener(PageChangeListener listener) {
        pageListeners.remove(listener);
    }

    private void notifyPageChange(String pageKey) {
        pageListeners.forEach(l -> l.onPageChange(pageKey));
    }

    private void applyTheme() {
        theme = AppSys.themes.getCurrent();
        navBar.setBackground(theme.background);
        setBackground(theme.background);
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, theme.borderColor));

        for (Component comp : navBar.getComponents()) {
            applyThemeToComponent(comp, theme);
        }
    }

    private void styleNavButton(JButton btn, boolean isSubButton) {
        btn.setFont(AppSys.themes.getFont(isSubButton ? 13 : 14));
        btn.setForeground(isSubButton ? theme.textSecondary : theme.textPrimary);
        btn.setBorder(new EmptyBorder(12, 18, 12, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void applyThemeToComponent(Component comp, Theme theme) {
        if (comp instanceof JButton) {
            JButton btn = (JButton) comp;
            btn.setForeground(theme.textPrimary);
        } else if (comp instanceof JPanel) {
            JPanel panel = (JPanel) comp;
            panel.setBackground(theme.background);
            for (Component child : panel.getComponents()) {
                applyThemeToComponent(child, theme);
            }
        }
    }

    @Override
    public void onThemeChanged(Theme theme) {
        applyTheme();
    }

    private void selectButton(JButton btn, String pageKey) {
        if (currentSelectedButton != null) {
            currentSelectedButton.setContentAreaFilled(false);
        }

        currentSelectedButton = btn;
        btn.setBackground(theme.accent);
        btn.setContentAreaFilled(true);

        notifyPageChange(pageKey);
    }

    public void cleanup() {
        AppSys.themes.removeListener(this);
        pageListeners.clear();
    }
}