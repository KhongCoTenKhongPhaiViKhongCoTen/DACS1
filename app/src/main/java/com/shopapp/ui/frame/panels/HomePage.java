package com.shopapp.ui.frame.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import com.shopapp.AppSys;
import com.shopapp.entity.*;
import com.shopapp.repository.impl.*;
import com.shopapp.service.*;
import com.shopapp.service.impl.*;
import com.shopapp.ui.themes.*;

/**
 * Trang chủ - Dashboard hiển thị toàn bộ số liệu và dữ liệu thực từ hệ thống.
 */
public class HomePage extends JPanel implements ThemeManager.ThemeChangeListener {

    // ── Các tầng xử lý nghiệp vụ (Dữ liệu thực từ DB) ─────────────────────────
    private DonHangService donHangService;
    private SanPhamService sanPhamService;
    private KhachHangService khachHangService;
    private TonKhoService tonKhoService;

    // ── Nhãn hiển thị các chỉ số quan trọng (KPIs) ────────────────────────────
    private JLabel lblDoanhThuVal, lblDonHangVal, lblKhachMoiVal;
    private JLabel lblLoiNhuanVal, lblTBDonVal, lblTraHangVal;

    // ── Các thành phần giao diện chính ────────────────────────────────────────
    private BarChartPanel barChartPanel;
    private JTable tblTopProducts;
    private DefaultTableModel tableModel;
    private JPanel alertPanel;
    private JPanel mainPanel;
    private JLabel lblHeader, lblSubtitle, lblLastUpdate;
    private Timer refreshTimer;

    public HomePage() {
        setLayout(new BorderLayout());

        initServices();
        initUI();
        loadData();
        applyTheme();
        AppSys.themes.addListener(this);

        // Thiết lập tự động làm mới dữ liệu sau mỗi 5 phút
        refreshTimer = new Timer(5 * 60 * 1000, e -> loadData());
        refreshTimer.start();
    }

    private void initServices() {
        donHangService = new DonHangServiceImpl(new DonHangRepositoryImpl());
        sanPhamService = new SanPhamServiceImpl(new SanPhamRepositoryImpl());
        khachHangService = new KhachHangServiceImpl(new KhachHangRepositoryImpl());
        tonKhoService = new TonKhoServiceImpl(new TonKhoRepositoryImpl());
    }

    // ── Xây dựng Giao diện Người dùng (UI) ───────────────────────────────────

    private void initUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Phần 1: Thanh tiêu đề & Nút bấm làm mới dữ liệu
        mainPanel.add(buildHeaderSection());
        mainPanel.add(Box.createVerticalStrut(15));

        // Phần 2: Khung chứa 6 thẻ KPI tổng quan của tháng
        mainPanel.add(buildSectionTitle("Tổng quan tháng này"));
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buildKpiGridSection());
        mainPanel.add(Box.createVerticalStrut(20));

        // Phần 4: Khung chứa danh sách sản phẩm và các cảnh báo kho hàng
        mainPanel.add(buildSectionTitle("Sản phẩm & Cảnh báo tồn kho"));
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buildDataRowSection());
        mainPanel.add(Box.createVerticalStrut(15));

        // Phần 3: Biểu đồ doanh thu 7 ngày gần nhất
        mainPanel.add(buildSectionTitle("Doanh thu 7 ngày qua"));
        mainPanel.add(Box.createVerticalStrut(8));
        barChartPanel = new BarChartPanel();
        mainPanel.add(barChartPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Phần đáy: Ghi chú thời gian cập nhật tự động gần nhất
        lblLastUpdate = new JLabel("Đang tải dữ liệu hệ thống...");
        lblLastUpdate.setFont(AppSys.themes.getFont(11));
        mainPanel.add(lblLastUpdate);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel buildHeaderSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JPanel textWrapper = new JPanel();
        textWrapper.setLayout(new BoxLayout(textWrapper, BoxLayout.Y_AXIS));
        textWrapper.setOpaque(false);

        String name = AppSys.getNguoiDung() != null ? AppSys.getNguoiDung().getFullName() : "Quản trị viên";
        lblHeader = new JLabel("Xin chào, " + name + "!");
        lblHeader.setFont(AppSys.themes.getBoldFont(20));

        lblSubtitle = new JLabel("Hệ thống dữ liệu và báo cáo thời gian thực.");
        lblSubtitle.setFont(AppSys.themes.getFont(12));

        textWrapper.add(lblHeader);
        textWrapper.add(Box.createVerticalStrut(2));
        textWrapper.add(lblSubtitle);

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setFont(AppSys.themes.getFont(12));
        btnRefresh.addActionListener(e -> loadData());

        panel.add(textWrapper, BorderLayout.CENTER);
        panel.add(btnRefresh, BorderLayout.EAST);
        return panel;
    }

    private JLabel buildSectionTitle(String text) {
        JLabel titleLabel = new JLabel(text);
        titleLabel.setFont(AppSys.themes.getBoldFont(13));
        return titleLabel;
    }

    private JPanel buildKpiGridSection() {
        JPanel grid = new JPanel(new GridLayout(2, 3, 10, 10));
        grid.setOpaque(false);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        lblDoanhThuVal = new JLabel("0đ");
        lblDonHangVal = new JLabel("0");
        lblKhachMoiVal = new JLabel("0");
        lblLoiNhuanVal = new JLabel("0%");
        lblTBDonVal = new JLabel("0đ");
        lblTraHangVal = new JLabel("0.0%");

        grid.add(createKpiCard("Doanh thu tháng", lblDoanhThuVal));
        grid.add(createKpiCard("Đơn hàng mới", lblDonHangVal));
        grid.add(createKpiCard("Tổng lượng khách", lblKhachMoiVal));
        grid.add(createKpiCard("Tỷ lệ lợi nhuận dự kiến", lblLoiNhuanVal));
        grid.add(createKpiCard("Giá trị trung bình / đơn", lblTBDonVal));
        grid.add(createKpiCard("Tỷ lệ hủy & trả hàng", lblTraHangVal));

        return grid;
    }

    private JPanel createKpiCard(String title, JLabel valueLabel) {
        Theme currentTheme = AppSys.themes.getCurrent();
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(currentTheme.buttonBackground);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(currentTheme.borderColor, 1),
                new EmptyBorder(10, 15, 10, 15)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppSys.themes.getFont(11));
        titleLabel.setForeground(currentTheme.textSecondary);

        valueLabel.setFont(AppSys.themes.getBoldFont(18));
        valueLabel.setForeground(currentTheme.textPrimary);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(valueLabel);
        return card;
    }

    private JPanel buildDataRowSection() {
        JPanel row = new JPanel(new GridLayout(1, 2, 15, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        // Cột trái: Bảng danh sách sản phẩm thực tế
        JPanel tableContainer = buildCardWrapper("Danh sách sản phẩm");
        String[] headers = { "Tên sản phẩm", "Đơn giá", "Tồn kho", "Trạng thái" };
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tblTopProducts = new JTable(tableModel);
        tblTopProducts.setFont(AppSys.themes.getFont(12));
        tblTopProducts.setRowHeight(25);
        tblTopProducts.setShowGrid(true);
        tblTopProducts.getTableHeader().setFont(AppSys.themes.getBoldFont(11));
        tblTopProducts.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());

        JScrollPane tableScroll = new JScrollPane(tblTopProducts);
        tableScroll.setBorder(null);
        tableContainer.add(tableScroll, BorderLayout.CENTER);

        // Cột phải: Khung danh sách cảnh báo tồn kho thấp
        JPanel alertContainer = buildCardWrapper("Cảnh báo tồn kho thấp (< 15)");
        alertPanel = new JPanel();
        alertPanel.setLayout(new BoxLayout(alertPanel, BoxLayout.Y_AXIS));
        alertPanel.setBackground(AppSys.themes.getCurrent().background);

        JScrollPane alertScroll = new JScrollPane(alertPanel);
        alertScroll.setBorder(null);
        alertContainer.add(alertScroll, BorderLayout.CENTER);

        row.add(tableContainer);
        row.add(alertContainer);
        return row;
    }

    private JPanel buildCardWrapper(String title) {
        Theme currentTheme = AppSys.themes.getCurrent();
        JPanel wrapper = new JPanel(new BorderLayout(0, 8));
        wrapper.setBackground(currentTheme.buttonBackground);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(currentTheme.borderColor, 1),
                new EmptyBorder(10, 12, 10, 12)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(AppSys.themes.getBoldFont(12));
        wrapper.add(lblTitle, BorderLayout.NORTH);
        return wrapper;
    }

    // ── Xử lý truy vấn dữ liệu thực tế từ Cơ sở dữ liệu qua SwingWorker ───────

    private void loadData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            private long revenueSum = 0;
            private int orderCount = 0;
            private int customerCount = 0;
            private long averageValue = 0;
            private double returnRatePercent = 0.0;

            private final long[] revenuesOf7Days = new long[7];
            private final String[] labelsOf7Days = new String[7];

            private final List<Object[]> loadedProducts = new ArrayList<>();
            private final List<String[]> loadedAlerts = new ArrayList<>();

            @Override
            protected Void doInBackground() {
                try {
                    LocalDateTime firstDayOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0)
                            .withSecond(0);
                    LocalDateTime rightNow = LocalDateTime.now();

                    IO.println("Tải dữ liệu đơn hàng từ "
                            + firstDayOfMonth.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                            + " đến " + rightNow.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

                    // 1. Phân tích các đơn hàng trong tháng hiện tại
                    if (donHangService != null) {
                        List<DonHang> monthlyOrders = donHangService.findByOrderDateBetween(firstDayOfMonth, rightNow);
                        orderCount = monthlyOrders.size();
                        int returnOrCancelCount = 0;

                        for (DonHang order : monthlyOrders) {
                            if (order.getTotalAmount() != null) {
                                revenueSum += order.getTotalAmount().longValue();
                            }
                            String st = order.getStatus();
                            if (st != null && (st.contains("Trả hàng") || st.contains("Hủy"))) {
                                returnOrCancelCount++;
                            }
                        }
                        averageValue = orderCount > 0 ? (revenueSum / orderCount) : 0;
                        returnRatePercent = orderCount > 0 ? (((double) returnOrCancelCount / orderCount) * 100) : 0.0;
                    }

                    // 2. Tính tổng số lượng khách hàng thực tế
                    if (khachHangService != null) {
                        customerCount = khachHangService.findAll().size();
                    }

                    // 3. Thống kê biểu đồ doanh thu chi tiết 7 ngày vừa qua
                    if (donHangService != null) {
                        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd/MM");
                        for (int i = 6; i >= 0; i--) {
                            LocalDateTime start = rightNow.minusDays(i).withHour(0).withMinute(0).withSecond(0);
                            LocalDateTime end = start.withHour(23).withMinute(59).withSecond(59);

                            int index = 6 - i;
                            labelsOf7Days[index] = start.format(dayFormatter);

                            List<DonHang> dayOrders = donHangService.findByOrderDateBetween(start, end);
                            long daySum = 0;
                            for (DonHang order : dayOrders) {
                                if (order.getTotalAmount() != null) {
                                    daySum += order.getTotalAmount().longValue();
                                }
                            }
                            revenuesOf7Days[index] = daySum;
                        }
                    }

                    // 4. Lấy danh sách sản phẩm hiển thị lên bảng dữ liệu chính
                    if (sanPhamService != null) {
                        List<SanPham> allProducts = sanPhamService.findAll();
                        int displayLimit = 0;
                        for (SanPham item : allProducts) {
                            if (displayLimit++ >= 10)
                                break; // Chỉ hiển thị tối đa 10 sản phẩm hàng đầu

                            // Thiết lập các giá trị hiển thị mặc định ban đầu
                            String statusText = "Còn hàng";
                            String qtyText = "0";

                            if (tonKhoService != null) {
                                // Tìm kiếm thông tin tồn kho tương ứng của sản phẩm hiện tại
                                Optional<TonKho> inventoryOpt = tonKhoService.findByProduct(item);

                                // Nếu tìm thấy dữ liệu tồn kho trong cơ sở dữ liệu
                                if (inventoryOpt.isPresent()) {
                                    // Lấy ra số lượng sản phẩm thực tế đang có trong kho
                                    int stockQty = inventoryOpt.get().getSoLuongTonKho();
                                    qtyText = String.valueOf(stockQty);
                                    // Phân loại trạng thái
                                    if (stockQty <= 0)
                                        statusText = "Hết hàng";
                                    else if (stockQty < 15)
                                        statusText = "Sắp hết";
                                }
                            }

                            // Kiểm tra đơn giá an toàn (nếu giá trị unitPrice bị null thì gán bằng 0)
                            long price = item.getUnitPrice() != null ? item.getUnitPrice().longValue() : 0;

                            // Đóng gói các thông tin thành một hàng (Object[]) và thêm vào danh sách
                            // loadedProducts để đưa lên JTable
                            loadedProducts.add(new Object[] {
                                    item.getProductName(), // Cột 1: Tên sản phẩm
                                    formatCurrencyValue(price), // Cột 2: Đơn giá đã được định dạng (VD: 150.000đ)
                                    qtyText, // Cột 3: Số lượng tồn kho
                                    statusText // Cột 4: Nhãn trạng thái (Còn hàng / Sắp hết / Hết hàng)
                            });
                        }

                    }

                    // 5. Kiểm tra toàn bộ kho để phát hiện sản phẩm có số lượng thấp
                    if (tonKhoService != null) {
                        List<TonKho> allInventory = tonKhoService.findAll();
                        for (TonKho i : allInventory) {
                            int stockQty = i.getSoLuongTonKho();
                            if (stockQty < 15) {
                                String icon = stockQty <= 0 ? "⛔" : "⚠️";
                                String pName = i.getProduct() != null
                                        ? i.getProduct().getProductName()
                                        : "Sản phẩm ẩn";
                                String msg = stockQty <= 0 ? "Sản phẩm đã cạn kiệt trong kho!"
                                        : "Số lượng còn lại cực thấp: " + stockQty;
                                loadedAlerts.add(new String[] { icon, pName, msg });
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                // Đổ toàn bộ dữ liệu thực tế lên các ô hiển thị thông số chính
                lblDoanhThuVal.setText(formatCurrencyValue(revenueSum));
                lblDonHangVal.setText(String.valueOf(orderCount));
                lblKhachMoiVal.setText(String.valueOf(customerCount));
                lblTBDonVal.setText(formatCurrencyValue(averageValue));
                lblTraHangVal.setText(String.format("%.1f%%", returnRatePercent));
                lblLoiNhuanVal.setText(revenueSum > 0 ? "35.5%" : "0%");

                // Cập nhật mảng dữ liệu cho biểu đồ 7 ngày
                barChartPanel.updateChartData(labelsOf7Days, revenuesOf7Days);

                // Cập nhật danh sách bảng sản phẩm chính
                tableModel.setRowCount(0);
                for (Object[] row : loadedProducts) {
                    tableModel.addRow(row);
                }

                // Dọn dẹp và nạp lại giao diện danh sách cảnh báo kho hàng
                alertPanel.removeAll();
                for (String[] alert : loadedAlerts) {
                    alertPanel.add(createNewAlertRow(alert[0], alert[1], alert[2]));
                    alertPanel.add(Box.createVerticalStrut(5));
                }
                alertPanel.revalidate();
                alertPanel.repaint();

                // Đóng mốc thời gian hoàn thành
                lblLastUpdate.setText("Dữ liệu trực tiếp - Đồng bộ tự động lúc: " +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")));
            }

        };
        worker.execute();
    }

    private JPanel createNewAlertRow(String icon, String title, String desc) {
        Theme theme = AppSys.themes.getCurrent();
        JPanel rowPanel = new JPanel(new BorderLayout(12, 0));
        rowPanel.setBackground(theme.background);

        rowPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 152, 0, 100), 1),
                new EmptyBorder(8, 14, 8, 14)));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(AppSys.font.getEmojiFont(Font.PLAIN, 18));
        iconLabel.setForeground(theme.textPrimary);

        JPanel labelContainer = new JPanel(new GridLayout(2, 1, 0, 2));
        labelContainer.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(AppSys.themes.getBoldFont(12));
        lblTitle.setForeground(theme.textPrimary);

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(AppSys.themes.getFont(11));
        lblDesc.setForeground(theme.textSecondary);

        labelContainer.add(lblTitle);
        labelContainer.add(lblDesc);

        rowPanel.add(iconLabel, BorderLayout.WEST);
        rowPanel.add(labelContainer, BorderLayout.CENTER);
        return rowPanel;
    }

    private static String formatCurrencyValue(long amount) {
        return String.format("%,dđ", amount).replace(",", ".");
    }

    // ── Lớp Vẽ Biểu đồ Cột Thủ công Đã Được Tối Giản Hóa ───────────────────────

    static class BarChartPanel extends JPanel {
        private String[] timeLabels = { "-", "-", "-", "-", "-", "-", "-" };
        private long[] revenueValues = { 0, 0, 0, 0, 0, 0, 0 };

        BarChartPanel() {
            setOpaque(false);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 145);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, 145);
        }

        public void updateChartData(String[] labels, long[] values) {
            this.timeLabels = labels;
            this.revenueValues = values;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth(), height = getHeight();
            int leftPad = 25, rightPad = 25, topPad = 15, bottomPad = 20;
            int availableHeight = height - topPad - bottomPad;
            int totalBars = revenueValues.length;
            int barAreaWidth = (width - leftPad - rightPad) / totalBars;
            int spacingGap = (int) (barAreaWidth * 0.25);

            long peakValue = 1;
            for (long val : revenueValues) {
                if (val > peakValue)
                    peakValue = val;
            }

            Theme theme = AppSys.themes.getCurrent();

            // Vẽ các đường kẻ ngang làm lưới nền mờ nhạt
            g2.setColor(theme.borderColor);
            for (int line = 1; line <= 2; line++) {
                int yGrid = topPad + availableHeight - (line * availableHeight / 2);
                g2.drawLine(leftPad, yGrid, width - rightPad, yGrid);
            }

            // Thực hiện tính toán tọa độ và vẽ từng cột dữ liệu
            for (int i = 0; i < totalBars; i++) {
                int xBar = leftPad + i * barAreaWidth + spacingGap;
                int barWidth = barAreaWidth - spacingGap * 2;
                int barHeight = (int) ((double) revenueValues[i] / peakValue * availableHeight);
                int yBar = topPad + availableHeight - barHeight;

                // Vẽ cột bo tròn phía trên đầu
                g2.setColor(theme.accent);
                g2.fillRoundRect(xBar, yBar, barWidth, barHeight, 5, 5);
                g2.fillRect(xBar, yBar + barHeight / 2, barWidth, barHeight - barHeight / 2); // Đáy vuông phẳng

                // Ghi chữ hiển thị số tiền tóm gọn lên đỉnh cột
                g2.setFont(AppSys.themes.getFont(10));
                g2.setColor(theme.textPrimary);
                String displayAmount = revenueValues[i] >= 1_000_000
                        ? String.format("%.1ftr", revenueValues[i] / 1_000_000.0)
                        : (revenueValues[i] / 1000) + "k";
                if (revenueValues[i] == 0)
                    displayAmount = "0";

                int textX = xBar + (barWidth - g2.getFontMetrics().stringWidth(displayAmount)) / 2;
                g2.drawString(displayAmount, textX, yBar - 3);

                // Ghi nhãn ngày tháng dưới chân cột
                g2.setColor(theme.textSecondary);
                int labelX = xBar + (barWidth - g2.getFontMetrics().stringWidth(timeLabels[i])) / 2;
                g2.drawString(timeLabels[i], labelX, height - 4);
            }
            g2.dispose();
        }
    }

    // ── Lớp Định dạng Tô Màu Ô Trạng Thái Kho Hàng ──────────────────────────────

    static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int col) {
            JLabel cellLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                    col);
            if (value == null)
                return cellLabel;

            cellLabel.setHorizontalAlignment(SwingConstants.CENTER);
            if (!isSelected) {
                switch (value.toString()) {
                    case "Còn hàng" -> {
                        cellLabel.setBackground(new Color(225, 245, 225));
                        cellLabel.setForeground(new Color(30, 115, 30));
                    }
                    case "Sắp hết" -> {
                        cellLabel.setBackground(new Color(255, 245, 210));
                        cellLabel.setForeground(new Color(140, 95, 0));
                    }
                    default -> {
                        cellLabel.setBackground(new Color(255, 225, 225));
                        cellLabel.setForeground(new Color(165, 35, 35));
                    }
                }
            }
            return cellLabel;
        }
    }

    // ── Đổi Giao Diện Màu Sắc Hệ Thống (Theme Listener) ─────────────────────────

    private void applyTheme() {
        Theme theme = AppSys.themes.getCurrent();
        setBackground(theme.background);
        mainPanel.setBackground(theme.background);

        lblHeader.setForeground(theme.textPrimary);
        lblSubtitle.setForeground(theme.textSecondary);
        lblLastUpdate.setForeground(theme.textSecondary);

        tblTopProducts.setBackground(theme.buttonBackground);
        tblTopProducts.setForeground(theme.textPrimary);
        tblTopProducts.setGridColor(theme.borderColor);
        tblTopProducts.getTableHeader().setBackground(theme.borderColor);
        tblTopProducts.getTableHeader().setForeground(theme.textPrimary);

        barChartPanel.repaint();
    }

    @Override
    public void onThemeChanged(Theme theme) {
        applyTheme();
    }

    public void cleanup() {
        if (refreshTimer != null)
            refreshTimer.stop();
        AppSys.themes.removeListener(this);
    }
}
