package com.shopapp.ui.frame.panels;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.shopapp.AppSys;
import com.shopapp.entity.*;
import com.shopapp.repository.impl.*;
import com.shopapp.service.*;
import com.shopapp.service.impl.*;
import com.shopapp.ui.themes.*;

/**
 * Trang chủ với dashboard thống kê tổng quan.
 * Hiển thị KPI, biểu đồ doanh thu, sản phẩm bán chạy và cảnh báo tồn kho.
 */
public class HomePage extends JPanel implements ThemeManager.ThemeChangeListener {

    // ── Services ──────────────────────────────────────────────────────────────
    private DonHangService donHangService;
    private SanPhamService sanPhamService;
    private KhachHangService khachHangService;

    // ── KPI Labels ────────────────────────────────────────────────────────────
    private JLabel lblDoanhThuVal;
    private JLabel lblDonHangVal;
    private JLabel lblKhachMoiVal;
    private JLabel lblLoiNhuanVal;
    private JLabel lblTBDonVal;
    private JLabel lblTraHangVal;

    // ── Biểu đồ cột doanh thu ────────────────────────────────────────────────
    private BarChartPanel barChartPanel;

    // ── Bảng sản phẩm bán chạy ───────────────────────────────────────────────
    private JTable tblTopProducts;
    private javax.swing.table.DefaultTableModel tableModel;

    // ── Cảnh báo tồn kho ─────────────────────────────────────────────────────
    private JPanel alertPanel;

    // ── Panels cần đổi màu khi đổi theme ─────────────────────────────────────
    private JPanel mainPanel;
    private JLabel lblHeader, lblSubtitle;

    // ── Thời gian cập nhật ────────────────────────────────────────────────────
    private JLabel lblLastUpdate;
    private Timer refreshTimer;

    public HomePage() {
        setLayout(new BorderLayout());
        initServices();
        initUI();
        loadData();
        applyTheme();
        ThemeManager.addThemeChangeListener(this);

        // Tự động làm mới mỗi 5 phút
        refreshTimer = new Timer(5 * 60 * 1000, e -> loadData());
        refreshTimer.start();
    }

    // ── Khởi tạo services (lazy init an toàn) ────────────────────────────────

    private void initServices() {
        try {
            donHangService = new DonHangServiceImpl(new DonHangRepositoryImpl());
            sanPhamService = new SanPhamServiceImpl(new SanPhamRepositoryImpl());
            khachHangService = new KhachHangServiceImpl(new KhachHangRepositoryImpl());
        } catch (Exception e) {
            // DB chưa cấu hình, chạy ở chế độ demo
            donHangService = null;
            sanPhamService = null;
            khachHangService = null;
        }
    }

    // ── Xây dựng giao diện ───────────────────────────────────────────────────

    private void initUI() {
        setBorder(new EmptyBorder(0, 0, 0, 0));

        // Scroll wrapper bọc toàn bộ nội dung
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(18, 20, 18, 20));

        // Header
        mainPanel.add(buildHeader());
        mainPanel.add(Box.createVerticalStrut(16));

        // KPI Cards (2 hàng × 3 cột)
        mainPanel.add(buildSectionLabel("Tổng quan"));
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buildKpiGrid());
        mainPanel.add(Box.createVerticalStrut(20));

        // Biểu đồ cột
        mainPanel.add(buildSectionLabel("Doanh thu 7 ngày qua"));
        mainPanel.add(Box.createVerticalStrut(8));
        barChartPanel = new BarChartPanel();
        barChartPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        barChartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        mainPanel.add(barChartPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Bảng bán chạy + Cảnh báo tồn kho (2 cột)
        mainPanel.add(buildSectionLabel("Sản phẩm & Tồn kho"));
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buildBottomRow());
        mainPanel.add(Box.createVerticalStrut(12));

        // Dòng cập nhật cuối
        lblLastUpdate = new JLabel("Cập nhật lúc: --");
        lblLastUpdate.setFont(ThemeManager.getFont(11));
        lblLastUpdate.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblLastUpdate);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    // ── Header ────────────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel textBlock = new JPanel();
        textBlock.setLayout(new BoxLayout(textBlock, BoxLayout.Y_AXIS));
        textBlock.setOpaque(false);

        String username = AppSys.getNguoiDung() != null
                ? AppSys.getNguoiDung().getFullName()
                : "Bạn";

        lblHeader = new JLabel("Xin chào, " + username + "!");
        lblHeader.setFont(ThemeManager.getBoldFont(22));

        lblSubtitle = new JLabel("Đây là tổng quan hoạt động cửa hàng hôm nay.");
        lblSubtitle.setFont(ThemeManager.getFont(12));

        textBlock.add(lblHeader);
        textBlock.add(Box.createVerticalStrut(3));
        textBlock.add(lblSubtitle);

        // Nút làm mới thủ công
        JButton btnRefresh = new JButton("⟳  Làm mới");
        btnRefresh.setFont(ThemeManager.getFont(12));
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> loadData());

        p.add(textBlock, BorderLayout.CENTER);
        p.add(btnRefresh, BorderLayout.EAST);
        return p;
    }

    // ── Section label ──────────────────────────────────────────────────────

    private JLabel buildSectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(ThemeManager.getBoldFont(13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    // ── KPI Grid ──────────────────────────────────────────────────────────────

    private JPanel buildKpiGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 3, 12, 12));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Tạo 6 KPI cards
        Object[] r1 = kpiCard("Doanh thu tháng", "84.200.000đ", "▲ 12% so tháng trước", true);
        Object[] r2 = kpiCard("Đơn hàng", "1.340", "▲ 8% so tháng trước", true);
        Object[] r3 = kpiCard("Khách mới", "213", "▲ 5% so tháng trước", true);
        Object[] r4 = kpiCard("Lợi nhuận gộp", "38,6%", "▲ 1,2% so tháng trước", true);
        Object[] r5 = kpiCard("Giá trị TB / đơn", "628.000đ", "▼ 2% so tháng trước", false);
        Object[] r6 = kpiCard("Tỷ lệ trả hàng", "3,5%", "▼ 0,3% so tháng trước", false);

        // Gán các label để cập nhật từ loadData()
        lblDoanhThuVal = (JLabel) r1[1];
        lblDonHangVal = (JLabel) r2[1];
        lblKhachMoiVal = (JLabel) r3[1];
        lblLoiNhuanVal = (JLabel) r4[1];
        lblTBDonVal = (JLabel) r5[1];
        lblTraHangVal = (JLabel) r6[1];

        // Thêm vào grid
        grid.add((JPanel) r1[0]);
        grid.add((JPanel) r2[0]);
        grid.add((JPanel) r3[0]);
        grid.add((JPanel) r4[0]);
        grid.add((JPanel) r5[0]);
        grid.add((JPanel) r6[0]);

        return grid;
    }

    /**
     * Tạo một KPI card, trả về [JPanel card, JLabel value, JLabel change].
     */
    private Object[] kpiCard(String label, String value, String change, boolean positiveGood) {
        Theme theme = ThemeManager.getCurrentTheme();

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(theme.buttonBackground);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(theme.borderColor, 1),
                new EmptyBorder(10, 12, 10, 12)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel(label);
        lblTitle.setFont(ThemeManager.getFont(10));
        lblTitle.setForeground(theme.textSecondary);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));

        JLabel lblVal = new JLabel(value);
        lblVal.setFont(ThemeManager.getBoldFont(17));
        lblVal.setForeground(theme.textPrimary);
        lblVal.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblVal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));

        boolean isUp = change.startsWith("▲");
        Color changeColor = isUp
                ? (positiveGood ? new Color(34, 139, 34) : new Color(180, 30, 30))
                : (positiveGood ? new Color(180, 30, 30) : new Color(34, 139, 34));

        JLabel lblChange = new JLabel(change);
        lblChange.setFont(ThemeManager.getFont(9));
        lblChange.setForeground(changeColor);
        lblChange.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblChange.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(3));
        card.add(lblVal);
        card.add(Box.createVerticalStrut(2));
        card.add(lblChange);
        card.add(Box.createVerticalGlue());

        return new Object[] { card, lblVal, lblChange };
    }

    // Overload không dùng (chỉ để tránh lỗi compile từ code cũ)
    // → Đã xóa, sử dụng kpiCard() thay thế

    // ── Biểu đồ cột (custom paint) ────────────────────────────────────────────

    /**
     * Panel vẽ biểu đồ cột 7 ngày bằng Graphics2D.
     * Dữ liệu có thể truyền vào qua setData().
     */
    static class BarChartPanel extends JPanel {
        private String[] labels = { "T2", "T3", "T4", "T5", "T6", "T7", "CN" };
        private long[] values = { 11_200_000, 14_500_000, 9_800_000, 17_300_000,
                21_000_000, 19_400_000, 16_100_000 };
        private int hoveredBar = -1;

        BarChartPanel() {
            setPreferredSize(new Dimension(600, 150));
            setOpaque(false);
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int bar = getBarAt(e.getX());
                    if (bar != hoveredBar) {
                        hoveredBar = bar;
                        repaint();
                    }
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    hoveredBar = -1;
                    repaint();
                }
            });
        }

        void setData(String[] labels, long[] values) {
            this.labels = labels;
            this.values = values;
            repaint();
        }

        private int getBarAt(int x) {
            int n = values.length;
            int padL = 10, padR = 10;
            int totalW = getWidth() - padL - padR;
            int barW = totalW / n;
            for (int i = 0; i < n; i++) {
                int bx = padL + i * barW;
                if (x >= bx && x < bx + barW)
                    return i;
            }
            return -1;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int padL = 10, padR = 10, padTop = 10, padBot = 26;
            int chartH = h - padTop - padBot;
            int n = values.length;
            int totalW = w - padL - padR;
            int barW = totalW / n;
            int gap = (int) (barW * 0.22);

            // Tìm max
            long max = 1;
            for (long v : values)
                if (v > max)
                    max = v;

            // Màu theo theme
            Theme theme = ThemeManager.getCurrentTheme();
            Color barColor = theme.accent;
            Color hoverColor = barColor.brighter();
            Color labelColor = theme.textSecondary;
            Color gridColor = theme.borderColor;
            Color valueColor = theme.textPrimary;

            // Đường grid ngang (3 mức)
            g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 1f, new float[] { 4, 4 }, 0));
            g2.setColor(gridColor);
            for (int step = 1; step <= 3; step++) {
                int yg = padTop + chartH - (int) ((double) step / 3 * chartH);
                g2.drawLine(padL, yg, w - padR, yg);
            }

            g2.setStroke(new BasicStroke(1f));

            // Vẽ từng cột
            for (int i = 0; i < n; i++) {
                int bx = padL + i * barW + gap;
                int bw = barW - gap * 2;
                int bh = (int) ((double) values[i] / max * chartH);
                int by = padTop + chartH - bh;

                // Màu cột
                g2.setColor(i == hoveredBar ? hoverColor : barColor);

                // Bo góc trên
                g2.fillRoundRect(bx, by, bw, bh, 4, 4);
                // Che phần bo ở dưới để chỉ bo trên
                g2.fillRect(bx, by + bh / 2, bw, bh / 2);

                // Giá trị trên đỉnh cột
                String valStr = formatMillion(values[i]);
                g2.setFont(ThemeManager.getFont(10));
                g2.setColor(valueColor);
                FontMetrics fm = g2.getFontMetrics();
                int tx = bx + (bw - fm.stringWidth(valStr)) / 2;
                g2.drawString(valStr, tx, by - 3);

                // Nhãn ngày
                g2.setFont(ThemeManager.getFont(11));
                g2.setColor(labelColor);
                FontMetrics fmL = g2.getFontMetrics();
                int lx = bx + (bw - fmL.stringWidth(labels[i])) / 2;
                g2.drawString(labels[i], lx, h - 6);
            }

            g2.dispose();
        }

        private static String formatMillion(long val) {
            if (val >= 1_000_000)
                return String.format("%.1ftr", val / 1_000_000.0);
            if (val >= 1_000)
                return String.format("%dk", val / 1_000);
            return String.valueOf(val);
        }
    }

    // ── Bottom row: bảng top SP + cảnh báo tồn kho ───────────────────────────

    private JPanel buildBottomRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        // --- Bảng top sản phẩm ---
        JPanel tableCard = buildCard();
        tableCard.setLayout(new BorderLayout(0, 8));

        JLabel tblTitle = new JLabel("🏆  Sản phẩm bán chạy");
        tblTitle.setFont(ThemeManager.getBoldFont(12));
        tableCard.add(tblTitle, BorderLayout.NORTH);

        String[] cols = { "Sản phẩm", "Đã bán", "Tồn kho", "Trạng thái" };
        tableModel = new javax.swing.table.DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblTopProducts = new JTable(tableModel);
        tblTopProducts.setFont(ThemeManager.getFont(12));
        tblTopProducts.setRowHeight(24);
        tblTopProducts.setShowGrid(false);
        tblTopProducts.setIntercellSpacing(new Dimension(0, 0));
        tblTopProducts.getTableHeader().setFont(ThemeManager.getBoldFont(11));
        tblTopProducts.setFillsViewportHeight(true);
        // Render màu ô trạng thái
        tblTopProducts.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());

        JScrollPane sp = new JScrollPane(tblTopProducts);
        sp.setBorder(null);
        tableCard.add(sp, BorderLayout.CENTER);

        // --- Panel cảnh báo tồn kho ---
        JPanel alertCard = buildCard();
        alertCard.setLayout(new BorderLayout(0, 8));

        JLabel alertTitle = new JLabel("⚠️  Cảnh báo tồn kho thấp");
        alertTitle.setFont(ThemeManager.getBoldFont(12));
        alertCard.add(alertTitle, BorderLayout.NORTH);

        alertPanel = new JPanel();
        alertPanel.setLayout(new BoxLayout(alertPanel, BoxLayout.Y_AXIS));
        Theme themeAlert = ThemeManager.getCurrentTheme();
        alertPanel.setBackground(themeAlert.background);
        alertPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JScrollPane alertScroll = new JScrollPane(alertPanel);
        alertScroll.setBorder(null);
        alertScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        alertScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        alertCard.add(alertScroll, BorderLayout.CENTER);

        row.add(tableCard);
        row.add(alertCard);
        return row;
    }

    /** Tạo card có border và padding chuẩn theo theme */
    private JPanel buildCard() {
        Theme theme = ThemeManager.getCurrentTheme();
        JPanel card = new JPanel();
        card.setBackground(theme.buttonBackground);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(theme.borderColor, 1),
                new EmptyBorder(10, 12, 10, 12)));
        return card;
    }

    // ── Renderer trạng thái tồn kho ──────────────────────────────────────────

    static class StatusCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean selected, boolean hasFocus, int row, int col) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                    table, value, selected, hasFocus, row, col);
            String s = value != null ? value.toString() : "";
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setOpaque(true);
            if (!selected) {
                switch (s) {
                    case "Còn hàng" -> {
                        lbl.setBackground(new Color(220, 255, 220));
                        lbl.setForeground(new Color(0, 100, 0));
                    }
                    case "Sắp hết" -> {
                        lbl.setBackground(new Color(255, 250, 210));
                        lbl.setForeground(new Color(120, 80, 0));
                    }
                    case "Nguy hiểm" -> {
                        lbl.setBackground(new Color(255, 230, 200));
                        lbl.setForeground(new Color(160, 50, 0));
                    }
                    default -> {
                        lbl.setBackground(new Color(255, 220, 220));
                        lbl.setForeground(new Color(140, 0, 0));
                    }
                }
            }
            return lbl;
        }
    }

    // ── Tải dữ liệu thực từ DB ───────────────────────────────────────────────

    private void loadData() {
        // Chạy nền để không block UI
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            // Kết quả tính toán
            long doanhThu = 0;
            int soDon = 0;
            int khachMoi = 0;
            long tbDon = 0;
            List<SanPham> topSP = null;
            long[] chartData = new long[7];
            boolean demoMode = false;

            @Override
            protected Void doInBackground() {
                try {
                    if (donHangService == null) {
                        demoMode = true;
                        return null;
                    }

                    LocalDateTime startOfMonth = LocalDateTime.now()
                            .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                    LocalDateTime now = LocalDateTime.now();

                    List<DonHang> donHangs = donHangService.findByOrderDateBetween(startOfMonth, now);
                    soDon = donHangs.size();

                    for (DonHang dh : donHangs) {
                        if (dh.getTotalAmount() != null)
                            doanhThu += dh.getTotalAmount().longValue();
                    }
                    tbDon = soDon > 0 ? doanhThu / soDon : 0;

                    if (khachHangService != null)
                        khachMoi = khachHangService.findAll().size();

                    if (sanPhamService != null) {
                        topSP = sanPhamService.findAll();
                    }

                    // Biểu đồ 7 ngày
                    for (int i = 6; i >= 0; i--) {
                        LocalDateTime dayStart = now.minusDays(i).withHour(0).withMinute(0).withSecond(0);
                        LocalDateTime dayEnd = dayStart.withHour(23).withMinute(59).withSecond(59);
                        List<DonHang> dayOrders = donHangService.findByOrderDateBetween(dayStart, dayEnd);
                        long dayTotal = 0;
                        for (DonHang dh : dayOrders)
                            if (dh.getTotalAmount() != null)
                                dayTotal += dh.getTotalAmount().longValue();
                        chartData[6 - i] = dayTotal;
                    }

                } catch (Exception e) {
                    demoMode = true;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                if (demoMode) {
                    loadDemoData();
                    return;
                }
                // Cập nhật KPI
                if (lblDoanhThuVal != null)
                    lblDoanhThuVal.setText(formatCurrency(doanhThu));
                if (lblDonHangVal != null)
                    lblDonHangVal.setText(String.valueOf(soDon));
                if (lblKhachMoiVal != null)
                    lblKhachMoiVal.setText(String.valueOf(khachMoi));
                if (lblTBDonVal != null)
                    lblTBDonVal.setText(formatCurrency(tbDon));

                // Biểu đồ
                String[] dayLabels = { "T2", "T3", "T4", "T5", "T6", "T7", "CN" };
                if (barChartPanel != null)
                    barChartPanel.setData(dayLabels, chartData);

                // Bảng top sản phẩm
                tableModel.setRowCount(0);
                if (topSP != null) {
                    int count = 0;
                    for (SanPham sp : topSP) {
                        if (count++ >= 6)
                            break;
                        String status = "Còn hàng"; // Cần join với TonKho
                        tableModel.addRow(new Object[] {
                                sp.getProductName(), "—", "—", status
                        });
                    }
                }

                // Cập nhật thời gian
                if (lblLastUpdate != null)
                    lblLastUpdate.setText("Cập nhật lúc: " +
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")));
            }
        };
        worker.execute();
    }

    /** Dữ liệu mẫu khi chưa có DB */
    private void loadDemoData() {
        if (lblDoanhThuVal != null)
            lblDoanhThuVal.setText("84.200.000đ");
        if (lblDonHangVal != null)
            lblDonHangVal.setText("1.340");
        if (lblKhachMoiVal != null)
            lblKhachMoiVal.setText("213");
        if (lblLoiNhuanVal != null)
            lblLoiNhuanVal.setText("38,6%");
        if (lblTBDonVal != null)
            lblTBDonVal.setText("628.000đ");
        if (lblTraHangVal != null)
            lblTraHangVal.setText("3,5%");

        // Biểu đồ demo
        long[] demo = { 11_200_000, 14_500_000, 9_800_000, 17_300_000,
                21_000_000, 19_400_000, 16_100_000 };
        String[] days = { "T2", "T3", "T4", "T5", "T6", "T7", "CN" };
        if (barChartPanel != null)
            barChartPanel.setData(days, demo);

        // Bảng demo
        tableModel.setRowCount(0);
        Object[][] rows = {
                { "Áo polo nam basic", "312", "48", "Sắp hết" },
                { "Quần jean nữ slim", "278", "120", "Còn hàng" },
                { "Váy hoa mùa hè", "241", "12", "Nguy hiểm" },
                { "Áo thun oversize", "198", "85", "Còn hàng" },
                { "Áo khoác bomber", "154", "0", "Hết hàng" },
        };
        for (Object[] r : rows)
            tableModel.addRow(r);

        // Cảnh báo tồn kho demo
        alertPanel.removeAll();
        String[][] alerts = {
                { "⛔", "Áo khoác bomber", "Hết hàng — cần nhập gấp" },
                { "🔴", "Váy hoa mùa hè", "Tồn kho: 12 (dưới ngưỡng 15)" },
                { "🟠", "Áo polo nam basic", "Tồn kho: 48 (dưới ngưỡng 50)" },
        };
        for (String[] a : alerts) {
            alertPanel.add(buildAlertRow(a[0], a[1], a[2]));
            alertPanel.add(Box.createVerticalStrut(6));
        }
        alertPanel.revalidate();
        alertPanel.repaint();

        if (lblLastUpdate != null)
            lblLastUpdate.setText("Dữ liệu mẫu — Kết nối DB để xem dữ liệu thực");
    }

    private JPanel buildAlertRow(String icon, String name, String detail) {
        Theme theme = ThemeManager.getCurrentTheme();
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        row.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setOpaque(true);
        row.setBackground(theme.buttonBackground);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(theme.borderColor, 1),
                new EmptyBorder(5, 10, 5, 10)));

        JLabel ico = new JLabel(icon);
        ico.setFont(ThemeManager.getFont(16));
        ico.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setOpaque(false);
        text.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel lName = new JLabel(name);
        lName.setFont(ThemeManager.getBoldFont(12));
        lName.setForeground(theme.textPrimary);
        lName.setAlignmentX(Component.LEFT_ALIGNMENT);
        lName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        JLabel lDetail = new JLabel(detail);
        lDetail.setFont(ThemeManager.getFont(11));
        lDetail.setForeground(theme.textSecondary);
        lDetail.setAlignmentX(Component.LEFT_ALIGNMENT);
        lDetail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));

        text.add(lName);
        text.add(Box.createVerticalStrut(2));
        text.add(lDetail);

        row.add(ico, BorderLayout.WEST);
        row.add(text, BorderLayout.CENTER);
        return row;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static String formatCurrency(long val) {
        if (val >= 1_000_000)
            return String.format("%,.0ftr", val / 1_000_000.0).replace(",", ".");
        if (val >= 1_000)
            return String.format("%,dđ", val).replace(",", ".");
        return val + "đ";
    }

    // ── Theme ─────────────────────────────────────────────────────────────────

    private void applyTheme() {
        Theme theme = ThemeManager.getCurrentTheme();
        setBackground(theme.background);
        if (mainPanel != null)
            mainPanel.setBackground(theme.background);
        if (lblHeader != null)
            lblHeader.setForeground(theme.textPrimary);
        if (lblSubtitle != null)
            lblSubtitle.setForeground(theme.textSecondary);
        if (lblLastUpdate != null)
            lblLastUpdate.setForeground(theme.textSecondary);
        if (tblTopProducts != null) {
            tblTopProducts.setBackground(theme.background);
            tblTopProducts.setForeground(theme.textPrimary);
            tblTopProducts.setGridColor(theme.borderColor);
            tblTopProducts.setSelectionBackground(theme.accent);
            tblTopProducts.getTableHeader().setBackground(theme.buttonBackground);
            tblTopProducts.getTableHeader().setForeground(theme.textPrimary);
        }
        if (barChartPanel != null)
            barChartPanel.repaint();
        repaint();
    }

    @Override
    public void onThemeChanged(Theme theme) {
        applyTheme();
    }

    public void cleanup() {
        if (refreshTimer != null)
            refreshTimer.stop();
        ThemeManager.removeThemeChangeListener(this);
    }
}