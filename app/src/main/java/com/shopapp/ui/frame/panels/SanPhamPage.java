package com.shopapp.ui.frame.panels;

import com.shopapp.ui.components.BasePage;

/**
 * Panel for managing San Pham (Products)
 */
public class SanPhamPage extends BasePage {

    public SanPhamPage() {
        super(new String[] {
                "ID",
                "Tên sản phẩm",
                "MãCode",
                "Giá bán",
                "Nhà cung cấp",
                "Danh mục"
        });
    }

    @Override
    public void showTableData(boolean applyFilter) {
        tableModel.setRowCount(0);
        // Placeholder: add sample data if needed
    }

    @Override
    protected void handleAdd() {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Chức năng thêm sẽ được triển khai sau",
                "Thông báo",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected void handleEdit() {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Chức năng sửa sẽ được triển khai sau",
                "Thông báo",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected void handleDelete() {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Chức năng xóa sẽ được triển khai sau",
                "Thông báo",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected void handleFilter() {
        showTableData(true);
    }

    @Override
    protected void attachCustomEvents() {
        // Load initial data
        showTableData(false);
    }

    @Override
    protected void addCustomButtons() {
        // No custom buttons for now
    }

    @Override
    protected void addCustomFilters() {
        // No custom filters for now
    }
}