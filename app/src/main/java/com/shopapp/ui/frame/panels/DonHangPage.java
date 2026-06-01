package com.shopapp.ui.frame.panels;

import java.util.List;
import java.util.Optional;

import javax.swing.*;
import com.shopapp.entity.DonHang;
import com.shopapp.entity.KhachHang;
import com.shopapp.entity.NguoiDung;
import com.shopapp.repository.impl.DonHangRepositoryImpl;
import com.shopapp.service.DonHangService;
import com.shopapp.service.impl.DonHangServiceImpl;
import com.shopapp.ui.components.BasePage;
import com.shopapp.ui.frame.panels.Dialog.DonHangDialog;

/**
 * Panel for managing Don Hang (Orders)
 */
public class DonHangPage extends BasePage {

    private DonHangService donHangService;

    public DonHangPage() {
        super(new String[] {
                "ID",
                "Mã đơn hàng",
                "Khách hàng",
                "Nhân viên",
                "Ngày đặt",
                "Trạng thái",
                "Tổng tiền"
        });
    }

    // ── Lazy init service ─────────────────────────────────────────────────────

    private DonHangService getDonHangService() {
        if (donHangService == null) {
            donHangService = new DonHangServiceImpl(new DonHangRepositoryImpl());
        }
        return donHangService;
    }

    // ── Lấy Frame cha ─────────────────────────────────────────────────────────

    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }

    // ── Filter & Table ────────────────────────────────────────────────────────

    @Override
    protected void addCustomFilters() {
        // Không có filter tùy chỉnh
    }

    @Override
    public void showTableData(boolean applyFilters) {
        tableModel.setRowCount(0);
        try {
            List<DonHang> donHangList = getDonHangService().findAll();
            String searchQuery = tfSearch.getText().trim().toLowerCase();

            for (DonHang donHang : donHangList) {
                if (applyFilters && !searchQuery.isEmpty()) {
                    String idStr = String.valueOf(donHang.getOrderId());
                    String orderNumberStr = donHang.getOrderNumber() != null ? donHang.getOrderNumber().toLowerCase() : "";
                    String customerName = donHang.getCustomer() != null ? donHang.getCustomer().getFullName().toLowerCase() : "";
                    String userName = donHang.getUser() != null ? donHang.getUser().getFullName().toLowerCase() : "";
                    String dateStr = donHang.getOrderDate() != null ? donHang.getOrderDate().toString().toLowerCase() : "";
                    String statusStr = donHang.getStatus() != null ? donHang.getStatus().toLowerCase() : "";
                    String totalStr = donHang.getTotalAmount() != null ? donHang.getTotalAmount().toString().toLowerCase() : "";

                    if (!idStr.contains(searchQuery) && !orderNumberStr.contains(searchQuery) &&
                        !customerName.contains(searchQuery) && !userName.contains(searchQuery) &&
                        !dateStr.contains(searchQuery) && !statusStr.contains(searchQuery) &&
                        !totalStr.contains(searchQuery)) {
                        continue;
                    }
                }

                tableModel.addRow(new Object[] {
                        donHang.getOrderId(),
                        donHang.getOrderNumber(),
                        donHang.getCustomer() != null ? donHang.getCustomer().getFullName() : "",
                        donHang.getUser() != null ? donHang.getUser().getFullName() : "",
                        donHang.getOrderDate(),
                        donHang.getStatus(),
                        donHang.getTotalAmount()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải dữ liệu đơn hàng: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    protected void handleFilter() {
        showTableData(true);
    }

    // ── CRUD handlers ─────────────────────────────────────────────────────────

    @Override
    protected void handleAdd() {
        DonHangDialog dialog = new DonHangDialog(getParentFrame(), null, getDonHangService());
        dialog.setVisible(true);
        if (dialog.isSucceeded()) {
            showTableData(true);
        }
    }

    @Override
    protected void handleEdit() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một đơn hàng từ bảng để chỉnh sửa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Optional<DonHang> donHang = getDonHangService().findById(selectedId);
            if (donHang.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy thông tin đơn hàng được chọn.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DonHangDialog dialog = new DonHangDialog(getParentFrame(), donHang.get(), getDonHangService());
            dialog.setVisible(true);
            if (dialog.isSucceeded()) {
                showTableData(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lấy thông tin đơn hàng: " + ex.getMessage(),
                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected void handleDelete() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một đơn hàng từ bảng để xóa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa đơn hàng này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                getDonHangService().deleteById(selectedId);
                showTableData(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xóa đơn hàng: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ── Custom buttons & events ───────────────────────────────────────────────

    @Override
    protected void addCustomButtons() {
        // No custom buttons for now
    }

    @Override
    protected void attachCustomEvents() {
        // Load dữ liệu ban đầu
        showTableData(false);
    }
}