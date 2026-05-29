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

import com.shopapp.ui.themes.Theme;
import com.shopapp.ui.themes.ThemeManager;
import com.shopapp.ui.themes.ThemeManager.ThemeChangeListener;

/**
 * Abstract class chung cho các trang CRUD trong giao diện người dùng.
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
 * Các Override methods tùy chọn
 * @see #addCustomButtons() Thêm các nút tùy chỉnh vào buttonPanel
 * @see #addCustomFilters() Thêm các filters tùy chỉnh vào filterPanel
 * @see #attachCustomEvents() Gắn sự kiện tùy chỉnh
 * 
 */

public abstract class AbstractTablePage extends JPanel implements ThemeChangeListener {

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
     * @since 1.0
     */
    public AbstractTablePage(String[] columnTableNames) {
        initBase();
        initUI(columnTableNames);
        initEvents();
        // Apply initial theme
        applyTheme();
        // Register as theme change listener to automatically update theme
        ThemeManager.addThemeChangeListener(this);
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

    /**
     * Khởi tạo các buttons
     */
    private void initButtons() {
        btnRefresh = new JButton("Làm mới");
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnFilter = new JButton("Lọc");
    }

    /**
     * Khởi tạo panel chứa buttons và filters
     */
    private void initTopPanel() {
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(5, 5));

        // ===== HÀNG 1: Panel chứa các nút thao tác =====
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        // Cho phép class con thêm nút tùy chỉnh vào buttonPanel
        addCustomButtons();

        // ===== HÀNG 2: Panel chứa bộ lọc =====
        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        // Khởi tạo components của filter
        tfSearch = new JTextField(15);
        tfSearchLabel = new JLabel("Tìm theo ID:");

        filterPanel.add(btnFilter);
        filterPanel.add(tfSearchLabel);
        filterPanel.add(tfSearch);
        // Cho phép class con thêm filters tùy chỉnh
        addCustomFilters();

        // Thêm cả 2 panel vào topPanel
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BorderLayout());
        containerPanel.add(buttonPanel, BorderLayout.NORTH);
        containerPanel.add(filterPanel, BorderLayout.CENTER);

        topPanel.add(containerPanel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Khởi tạo bảng
     */
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

    /**
     * Hiển thị danh sách Table với các bộ lọc tùy chọn.
     * 
     * @param applyFilter true = áp dụng bộ lọc, false = hiển thị tất cả
     */
    public abstract void showTableData(boolean applyFilter);

    /**
     * Gắn sự kiện cho các nút mặc định
     */
    private void attachDefaultEvents() {
        btnRefresh.addActionListener(e -> showTableData(false));
        btnFilter.addActionListener(e -> handleFilter());
        btnAdd.addActionListener(e -> handleAdd());
        btnEdit.addActionListener(e -> {
            handleEdit();
        });
        btnDelete.addActionListener(e -> {
            handleDelete();
        });
        tfSearch.addActionListener(e -> showTableData(true));

    }

    /***********************************************
     * ABSTRACT METHODS - Class con phải implement *
     ***********************************************/

    protected abstract void handleAdd();

    protected abstract void handleEdit();

    protected abstract void handleDelete();

    /**
     * Xử lý sự kiện nút Lọc
     */
    protected abstract void handleFilter();

    // ========================================
    // OPTIONAL METHODS - Class con có thể override
    // ========================================

    /**
     * Thêm các nút tùy chỉnh vào buttonPanel.
     * Override phương thức này để thêm nút của riêng bạn.
     */
    protected void addCustomButtons() {
        // Mặc định không làm gì
        // Class con override để thêm nút
        // dùng buttonPanel.add(...) để thêm nút
    }

    /**
     * Thêm các filters tùy chỉnh vào filterPanel.
     * Override phương thức này để thêm filter của riêng bạn.
     */
    protected void addCustomFilters() {
        // Mặc định không làm gì
        // Class con override để thêm filter
        // dùng filterPanel.add(...) để thêm filter
    }

    /**
     * Gắn sự kiện tùy chỉnh.
     * Override phương thức này để thêm sự kiện của riêng bạn.
     */
    protected void attachCustomEvents() {
        // Mặc định không làm gì
        // Class con override để thêm sự kiện
    }

    // ========================================
    // THEME METHODS
    // ========================================

    /**
     * Áp dụng theme hiện tại cho các component.
     */
    protected void applyTheme() {
        Theme theme = ThemeManager.getCurrentTheme();

        // Áp dụng theme cho các panel
        if (topPanel != null) {
            topPanel.setBackground(theme.background);
        }
        if (buttonPanel != null) {
            buttonPanel.setBackground(theme.background);
        }
        if (filterPanel != null) {
            filterPanel.setBackground(theme.background);
        }
        if (table != null) {
            table.setBackground(theme.background);
            table.setForeground(theme.foreground);
            table.setGridColor(theme.borderColor);
            table.setFont(theme.getFont(12));
            table.setSelectionBackground(theme.accent);
            table.setSelectionForeground(theme.buttonForeground);
        }
        if (scrollPane != null) {
            scrollPane.getViewport().setBackground(theme.background);
        }

        // Áp dụng theme cho buttons
        if (btnRefresh != null) {
            applyButtonTheme(btnRefresh, theme);
        }
        if (btnAdd != null) {
            applyButtonTheme(btnAdd, theme);
        }
        if (btnEdit != null) {
            applyButtonTheme(btnEdit, theme);
        }
        if (btnDelete != null) {
            applyButtonTheme(btnDelete, theme);
        }
        if (btnFilter != null) {
            applyButtonTheme(btnFilter, theme);
        }

        // Áp dụng theme cho text field và label
        if (tfSearch != null) {
            tfSearch.setBackground(theme.buttonBackground);
            tfSearch.setForeground(theme.textPrimary);
            tfSearch.setFont(theme.getFont(12));
        }
        if (tfSearchLabel != null) {
            tfSearchLabel.setForeground(theme.textPrimary);
            tfSearchLabel.setFont(theme.getFont(12));
        }
    }

    /**
     * Áp dụng theme cho một button.
     *
     * @param button Button cần áp dụng theme
     * @param theme Theme hiện tại
     */
    private void applyButtonTheme(javax.swing.JButton button, Theme theme) {
        button.setBackground(theme.buttonBackground);
        button.setForeground(theme.buttonForeground);
        button.setFont(theme.getFont(12));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
    }

    // ========================================
    // UTILITY METHODS
    // ========================================

    /**
     * Kiểm tra xem có dòng nào được chọn không
     * 
     * @return true nếu có dòng được chọn
     */
    protected boolean hasSelectedRow() {
        return table.getSelectedRow() != -1;
    }

    /**
     * Lấy index của dòng được chọn
     * 
     * @return index hoặc -1 nếu không có dòng nào được chọn
     */
    protected int getSelectedRow() {
        return table.getSelectedRow();
    }

    /**
     * Lấy ID từ dòng được chọn (giả sử cột 0 là ID)
     * 
     * @return ID hoặc -1 nếu không có dòng nào được chọn
     */
    protected int getSelectedId() {
        int selectedRow = getSelectedRow();
        if (selectedRow == -1) {
            return -1;
        }
        Object value = tableModel.getValueAt(selectedRow, 0);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return -1;
    }

    /**
     * Lấy giá trị tại cột chỉ định của dòng được chọn
     * 
     * @param column index của cột
     * @return giá trị hoặc null nếu không có dòng nào được chọn
     */
    protected Object getSelectedValue(int column) {
        int selectedRow = getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }
        return tableModel.getValueAt(selectedRow, column);
    }

    /**
     * Thêm một dòng vào bảng
     * 
     * @param rowData dữ liệu dòng
     */
    protected void addRow(Object[] rowData) {
        tableModel.addRow(rowData);
    }

    /**
     * Cập nhật dòng tại vị trí chỉ định
     * 
     * @param row     index của dòng
     * @param rowData dữ liệu mới
     */
    protected void updateRow(int row, Object[] rowData) {
        for (int i = 0; i < rowData.length && i < tableModel.getColumnCount(); i++) {
            tableModel.setValueAt(rowData[i], row, i);
        }
    }

    /**
     * Xóa dòng tại vị trí chỉ định
     * 
     * @param row index của dòng
     */
    protected void removeRow(int row) {
        tableModel.removeRow(row);
    }

    // ========================================
    // GETTERS & SETTERS
    // ========================================

    public void setRowHeightTable(int rowHeightTable) {
        this.rowHeightTable = rowHeightTable;
        if (table != null) {
            table.setRowHeight(rowHeightTable);
        }
    }

    /**
     * Called when the theme changes.
     * Override this method in subclasses to handle theme changes.
     *
     * @param theme The new theme
     */
    @Override
    public void onThemeChanged(Theme theme) {
        applyTheme(); // Fixed: This line was being interpreted as a duplicate method declaration.
    }
}
