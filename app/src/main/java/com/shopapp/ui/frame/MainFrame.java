package com.shopapp.ui.frame;

import javax.swing.*;
import java.awt.*;

import com.shopapp.AppSys;
import com.shopapp.ui.components.NavBar;
import com.shopapp.ui.frame.panels.DanhMucPage;
import com.shopapp.ui.frame.panels.DonHangPage;
import com.shopapp.ui.frame.panels.HomePage;
import com.shopapp.ui.frame.panels.KhachHangPage;
import com.shopapp.ui.frame.panels.NguoiDungPage;
import com.shopapp.ui.frame.panels.NhaCungCapPage;
import com.shopapp.ui.frame.panels.QuyenPage;
import com.shopapp.ui.frame.panels.SettingsPage;
import com.shopapp.ui.frame.panels.SanPhamPage;
import com.shopapp.ui.frame.panels.TonKhoPage;
import com.shopapp.ui.frame.panels.VaiTroPage;
import com.shopapp.ui.themes.*;
import com.shopapp.ui.listeners.PageChangeListener;

public class MainFrame extends JFrame
        implements ThemeManager.ThemeChangeListener, PageChangeListener {

    private CardLayout cardLayout = new CardLayout();
    private JPanel contentPanel = new JPanel(cardLayout);
    private NavBar navbar = new NavBar(300, 56);

    // Pages
    private HomePage homePage = new HomePage();
    private SettingsPage settingsPage = new SettingsPage();
    private NguoiDungPage nguoiDungPage;
    private VaiTroPage vaiTroPage;
    private QuyenPage quyenPage;

    // Kho Hang pages
    private DanhMucPage danhMucPage;
    private NhaCungCapPage nhaCungCapPage;
    private SanPhamPage sanPhamPage;
    private TonKhoPage tonKhoPage;
    private DonHangPage donHangPage;
    private KhachHangPage khachHangPage;

    public MainFrame() {
        initFrame();
        initComponents();
        applyTheme();
        AppSys.themes.addListener(this);
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

        // Add pages to content panel
        contentPanel.add(homePage, PageKey.HOME);
        contentPanel.add(settingsPage, PageKey.SETTINGS);

        if (AppSys.quyen().hasAnyCode("USER_READ")) {
            nguoiDungPage = new NguoiDungPage();
            contentPanel.add(nguoiDungPage, PageKey.AccountManagement.NGUOI_DUNG);

            vaiTroPage = new VaiTroPage();
            contentPanel.add(vaiTroPage, PageKey.AccountManagement.VAI_TRO);

            quyenPage = new QuyenPage();
            contentPanel.add(quyenPage, PageKey.AccountManagement.QUYEN);
        }

        // Kho Hang pages
        if (AppSys.quyen().hasAnyCode("USER_READ")) {
            danhMucPage = new DanhMucPage();
            contentPanel.add(danhMucPage, PageKey.KhoHang.DANH_MUC);

            nhaCungCapPage = new NhaCungCapPage();
            contentPanel.add(nhaCungCapPage, PageKey.KhoHang.NHA_CUNG_CAP);

            sanPhamPage = new SanPhamPage();
            contentPanel.add(sanPhamPage, PageKey.KhoHang.SAN_PHAM);

            tonKhoPage = new TonKhoPage();
            contentPanel.add(tonKhoPage, PageKey.KhoHang.TON_KHO);

        }

        if (AppSys.quyen().hasAnyCode("ORDER_READ")) {
            donHangPage = new DonHangPage();
            contentPanel.add(donHangPage, PageKey.DON_HANG);

        }
        
        if (AppSys.quyen().hasAnyCode("CUSTOMER_READ")) {
            khachHangPage = new KhachHangPage();
            contentPanel.add(khachHangPage, PageKey.KHACH_HANG); 
        }

        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, PageKey.DON_HANG);

        navbar.addPageChangeListener(this);
    }

    private void applyTheme() {
        Theme theme = AppSys.themes.getCurrent();
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
        AppSys.themes.removeListener(this);
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}