package com.shopapp.ui.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.shopapp.AppSys;
import com.shopapp.ui.themes.Theme;
import com.shopapp.ui.themes.ThemeManager;
import com.shopapp.ui.themes.ThemeManager.ThemeChangeListener;

/**
 * Abstract class chung cho các trang trong giao diện người dùng.
 * Các class con chỉ cần implement các phương thức abstract và override
 *
 * Các chức năng mặc định:
 * - Hiển thị bảng với dữ liệu
 * - Nút Thêm, Sửa, Xóa
 * - Bộ lọc tìm kiếm theo ID
 *
 * Các Override methods bắt buộc
 * 
 * @see #showTableData(boolean) Hiển thị dữ liệu bảng với/không áp dụng bộ lọc
 * @see #handleAdd() Xử lý sự kiện nút Thêm
 * @see #handleEdit() Xử lý sự kiện nút Sửa
 * @see #handleDelete() Xử lý sự kiện nút Xóa
 *
 *      Các Override methods tùy chọn
 * @see #addCustomButtons() Thêm các nút tùy chỉnh vào buttonPanel
 * @see #addCustomFilters() Thêm các filters tùy chỉnh vào filterPanel
 * @see #attachCustomEvents() Gắn sự kiện tùy chỉnh
 */
public abstract class BasePage extends JPanel implements ThemeChangeListener {

    // ===== TABLE =====
    protected JTable table;
    protected DefaultTableModel tableModel;
    protected JScrollPane scrollPane;

    // ===== PANEL =====
    protected JPanel topPanel;
    protected JPanel buttonPanel;
    protected JPanel filterPanel;

    // ===== Default Buttons =====
    protected JButton btnAdd;
    protected JButton btnEdit;
    protected JButton btnDelete;
    protected JButton btnFilter;
    protected JButton btnRefresh;

    // ===== Filters =====
    protected JTextField tfSearch;
    protected JLabel tfSearchLabel;

    protected int rowHeightTable = 30;

    /**
     * Constructor - Khởi tạo giao diện
     *
     * @param columnTableNames Tên các cột của bảng
     */
    public BasePage(String[] columnTableNames) {
        initBase();
        initUI(columnTableNames);
        initEvents();
        applyTheme();
        AppSys.themes.addListener(this);
    }

    protected void initBase() {
        setLayout(new BorderLayout(5, 5));
    }

    protected void initUI(String[] columnTableNames) {
        initButtons();
        initTopPanel();
        initTable(columnTableNames);
        table.setRowHeight(rowHeightTable);
    }

    protected void initEvents() {
        attachDefaultEvents();
        attachCustomEvents();
    }

    // ── Khởi tạo buttons ──────────────────────────────────────────────────────

    private void initButtons() {
        btnRefresh = new JButton("Làm mới");
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnFilter = new JButton("Lọc");
    }

    // ── Khởi tạo top panel ────────────────────────────────────────────────────

    private void initTopPanel() {
        topPanel = new JPanel(new BorderLayout(5, 5));

        // HÀNG 1: Panel chứa các nút thao tác
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Cho phép class con thêm nút tùy chỉnh vào buttonPanel
        addCustomButtons();

        // HÀNG 2: Panel chứa bộ lọc
        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tfSearch = new JTextField(15);
        tfSearchLabel = new JLabel("Tìm kiếm");
        filterPanel.add(btnFilter);
        filterPanel.add(tfSearchLabel);
        filterPanel.add(tfSearch);

        // Cho phép class con thêm filters tùy chỉnh
        addCustomFilters();

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(buttonPanel, BorderLayout.NORTH);
        containerPanel.add(filterPanel, BorderLayout.CENTER);

        topPanel.add(containerPanel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);
    }

    // ── Khởi tạo bảng ────────────────────────────────────────────────────────

    private void initTable(String[] columnNames) {
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ── Abstract methods ──────────────────────────────────────────────────────

    public abstract void showTableData(boolean applyFilter);

    protected abstract void handleAdd();

    protected abstract void handleEdit();

    protected abstract void handleDelete();

    protected abstract void handleFilter();

    // ── Default events ────────────────────────────────────────────────────────

    private void attachDefaultEvents() {
        btnRefresh.addActionListener(e -> showTableData(false));
        btnFilter.addActionListener(e -> handleFilter());
        btnAdd.addActionListener(e -> handleAdd());
        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());
        tfSearch.addActionListener(e -> showTableData(true));
    }

    // ── Optional override methods ─────────────────────────────────────────────

    /** Thêm nút tùy chỉnh vào buttonPanel. Dùng buttonPanel.add(...) */
    protected void addCustomButtons() {
    }

    /** Thêm filter tùy chỉnh vào filterPanel. Dùng filterPanel.add(...) */
    protected void addCustomFilters() {
    }

    /** Gắn sự kiện tùy chỉnh */
    protected void attachCustomEvents() {
    }

    // ── Theme ─────────────────────────────────────────────────────────────────

    protected void applyTheme() {
        Theme theme = AppSys.themes.getCurrent();

        if (topPanel != null)
            topPanel.setBackground(theme.background);
        if (filterPanel != null)
            filterPanel.setBackground(theme.background);

        if (buttonPanel != null) {
            buttonPanel.setBackground(theme.background);
            // Loop tất cả buttons trong buttonPanel — bao gồm cả custom buttons
            for (java.awt.Component comp : buttonPanel.getComponents()) {
                if (comp instanceof JButton btn) {
                    applyButtonTheme(btn, theme);
                }
            }
        }

        // btnFilter nằm trong filterPanel, xử lý riêng
        if (btnFilter != null)
            applyButtonTheme(btnFilter, theme);

        if (table != null) {
            table.setBackground(theme.background);
            table.setForeground(theme.foreground);
            table.setGridColor(theme.borderColor);
            table.setFont(AppSys.themes.getFont(15));
            table.setSelectionBackground(theme.accent);
            table.setSelectionForeground(theme.buttonForeground);
        }
        if (scrollPane != null) {
            scrollPane.getViewport().setBackground(theme.background);
        }
        if (tfSearch != null) {
            tfSearch.setBackground(theme.buttonBackground);
            tfSearch.setForeground(theme.textPrimary);
            tfSearch.setFont(AppSys.themes.getFont(12));
        }
        if (tfSearchLabel != null) {
            tfSearchLabel.setForeground(theme.textPrimary);
            tfSearchLabel.setFont(AppSys.themes.getFont(12));
        }
    }

    private void applyButtonTheme(JButton button, Theme theme) {
        button.setBackground(theme.buttonBackground);
        button.setForeground(theme.buttonForeground);
        button.setFont(AppSys.themes.getFont(12));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    protected int getSelectedRow() {
        return table.getSelectedRow();
    }

    protected int getSelectedId() {
        int selectedRow = getSelectedRow();
        if (selectedRow == -1)
            return -1;
        Object value = tableModel.getValueAt(selectedRow, 0);
        if (value instanceof Number)
            return ((Number) value).intValue();
        return -1;
    }

    protected void addRow(Object[] rowData) {
        tableModel.addRow(rowData);
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public void setRowHeightTable(int rowHeightTable) {
        this.rowHeightTable = rowHeightTable;
        if (table != null)
            table.setRowHeight(rowHeightTable);
    }

    @Override
    public void onThemeChanged(Theme theme) {
        applyTheme();
    }
}