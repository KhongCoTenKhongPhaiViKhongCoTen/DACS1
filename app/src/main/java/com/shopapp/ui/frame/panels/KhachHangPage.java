package com.shopapp.ui.frame.panels;

import java.util.List;
import java.util.Optional;

import javax.swing.*;
import com.shopapp.entity.KhachHang;
import com.shopapp.repository.impl.KhachHangRepositoryImpl;
import com.shopapp.service.KhachHangService;
import com.shopapp.service.impl.KhachHangServiceImpl;
import com.shopapp.ui.components.BasePage;
import com.shopapp.ui.frame.panels.Dialog.KhachHangDialog;

public class KhachHangPage extends BasePage {

    private KhachHangService khachHangService;

    public KhachHangPage() {
        super(new String[] {
                "ID",
                "Tên khách hàng",
                "Email",
                "Số điện thoại",
                "Địa chỉ",
                "Điểm thành viên",
                "Ngày tạo"
        });
    }

    private KhachHangService getKhachHangService() {
        if (khachHangService == null) {
            khachHangService = new KhachHangServiceImpl(new KhachHangRepositoryImpl());
        }
        return khachHangService;
    }

    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }

    @Override
    protected void addCustomFilters() {
    }

    @Override
    public void showTableData(boolean applyFilters) {
        tableModel.setRowCount(0);
        try {
            List<KhachHang> khachHangList = getKhachHangService().findAll();
            String searchQuery = tfSearch.getText().trim().toLowerCase();

            for (KhachHang khachHang : khachHangList) {
                if (applyFilters && !searchQuery.isEmpty()) {
                    String idStr = String.valueOf(khachHang.getCustomerId());
                    String nameStr = khachHang.getFullName() != null ? khachHang.getFullName().toLowerCase() : "";
                    String email = khachHang.getEmail() != null ? khachHang.getEmail().toLowerCase() : "";
                    String phone = khachHang.getPhone() != null ? khachHang.getPhone().toLowerCase() : "";
                    String address = khachHang.getAddress() != null ? khachHang.getAddress().toLowerCase() : "";
                    String loyaltyStr = String.valueOf(khachHang.getLoyaltyPoints());
                    String createdStr = khachHang.getCreatedAt() != null
                            ? khachHang.getCreatedAt().toString().toLowerCase()
                            : "";

                    if (!idStr.contains(searchQuery) && !nameStr.contains(searchQuery) &&
                            !email.contains(searchQuery) && !phone.contains(searchQuery) &&
                            !address.contains(searchQuery) && !loyaltyStr.contains(searchQuery) &&
                            !createdStr.contains(searchQuery)) {
                        continue;
                    }
                }

                tableModel.addRow(new Object[] {
                        khachHang.getCustomerId(),
                        khachHang.getFullName(),
                        khachHang.getEmail(),
                        khachHang.getPhone(),
                        khachHang.getAddress(),
                        khachHang.getLoyaltyPoints(),
                        khachHang.getCreatedAt()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải dữ liệu khách hàng: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    protected void handleFilter() {
        showTableData(true);
    }

    @Override
    protected void handleAdd() {
        KhachHangDialog dialog = new KhachHangDialog(getParentFrame(), null, getKhachHangService());
        if (dialog.isSucceeded()) {
            showTableData(true);
        }
    }

    @Override
    protected void handleEdit() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một khách hàng từ bảng để chỉnh sửa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Optional<KhachHang> khachHang = getKhachHangService().findById(selectedId);
            if (khachHang.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy thông tin khách hàng được chọn.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            KhachHangDialog dialog = new KhachHangDialog(getParentFrame(), khachHang.get(), getKhachHangService());
            if (dialog.isSucceeded()) {
                showTableData(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi lấy thông tin khách hàng: " + ex.getMessage(),
                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected void handleDelete() {
        int selectedId = getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một khách hàng từ bảng để xóa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa khách hàng này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                getKhachHangService().deleteById(selectedId);
                showTableData(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xóa khách hàng: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void addCustomButtons() {
    }

    @Override
    protected void attachCustomEvents() {
        showTableData(false);
    }
}