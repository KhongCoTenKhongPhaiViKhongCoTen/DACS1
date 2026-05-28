package com.shopapp.ui.frame;

import javax.swing.*;
import java.awt.*;

import com.shopapp.ui.components.NavBar;
import com.shopapp.ui.frame.panels.HomePage;
import com.shopapp.ui.frame.panels.SettingsPage;
import com.shopapp.ui.themes.*;
import com.shopapp.ui.listeners.PageChangeListener;

public class MainFrame extends JFrame
        implements ThemeManager.ThemeChangeListener, PageChangeListener {

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private NavBar navbar = new NavBar(300, 56);

    // Pages
    private HomePage homePage = new HomePage();
    private SettingsPage settingsPage = new SettingsPage();

    public MainFrame() {
        initFrame();
        initComponents();
        applyTheme();
        ThemeManager.addThemeChangeListener(this);
    }

    private void initFrame() {
        setTitle("Shop Quần Áo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(900, 700));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void initComponents() {
        NavbarMainFrame.init(navbar);

        // Get the navbar panel from NavbarManager (includes account login)
        add(navbar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Initialize and add pages
        homePage = new HomePage();
        settingsPage = new SettingsPage();

        // Add pages to content panel
        contentPanel.add(homePage, "home");
        contentPanel.add(settingsPage, "settings");

        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "home");

        // Register page change listener with NavbarManager
        navbar.addPageChangeListener(this);
    }

    private JPanel createPlaceholder(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(ThemeManager.getBoldFont(32));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitle, BorderLayout.CENTER);
        return panel;
    }

    private void applyTheme() {
        Theme theme = ThemeManager.getCurrentTheme();
        getContentPane().setBackground(theme.background);
        contentPanel.setBackground(theme.background);
        applyThemeToComponent(contentPanel, theme);
    }

    private void applyThemeToComponent(Component comp, Theme theme) {
        if (comp instanceof JLabel) {
            ((JLabel) comp).setForeground(theme.textPrimary);
        } else if (comp instanceof JPanel) {
            ((JPanel) comp).setBackground(theme.background);
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
    }

    @Override
    public void onPageChange(String pageKey) {
        System.out.println("Switching to page: " + pageKey);
        cardLayout.show(contentPanel, pageKey);
    }

    @Override
    public void dispose() {
        homePage.cleanup();
        settingsPage.cleanup();
        ThemeManager.removeThemeChangeListener(this);
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}