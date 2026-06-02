package com.shopapp.service.impl;

import com.shopapp.entity.DonHang;
import com.shopapp.entity.KhachHang;
import com.shopapp.entity.NguoiDung;
import com.shopapp.repository.DonHangRepository;
import com.shopapp.service.DonHangService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for DonHang.
 * Contains the business logic for DonHang operations.
 */
public class DonHangServiceImpl implements DonHangService {

    private final DonHangRepository donHangRepository;

    public DonHangServiceImpl(DonHangRepository donHangRepository) {
        this.donHangRepository = donHangRepository;
    }

    @Override
    public Optional<DonHang> findById(Integer id) {
        return donHangRepository.findById(id);
    }

    @Override
    public List<DonHang> findAll() {
        return donHangRepository.findAll();
    }

    @Override
    public List<DonHang> findByCustomer(KhachHang customer) {
        return donHangRepository.findByCustomer(customer);
    }

    @Override
    public List<DonHang> findByUser(NguoiDung user) {
        return donHangRepository.findByUser(user);
    }

    @Override
    public List<DonHang> findByStatus(String status) {
        return donHangRepository.findByStatus(status);
    }

    @Override
    public Optional<DonHang> findByOrderNumber(String orderNumber) {
        return donHangRepository.findByOrderNumber(orderNumber);
    }

@Override
public DonHang save(DonHang donHang) {
    // Validate bắt buộc
    if (donHang.getOrderNumber() == null || donHang.getOrderNumber().isBlank())
        throw new IllegalArgumentException("Vui lòng điền mã đơn hàng (*)"); // 59

    if (donHang.getCustomer() == null)
        throw new IllegalArgumentException("Vui lòng chọn khách hàng (*)");

    if (donHang.getUser() == null)
        throw new IllegalArgumentException("Vui lòng chọn nhân viên (*)");

    if (donHang.getOrderDate() == null)
        throw new IllegalArgumentException("Vui lòng điền ngày đặt (*)");

    if (donHang.getStatus() == null || donHang.getStatus().isBlank())
        throw new IllegalArgumentException("Vui lòng điền trạng thái (*)");

    // Validate số tiền
    if (donHang.getSubtotal() != null && donHang.getSubtotal().compareTo(BigDecimal.ZERO) < 0)
        throw new IllegalArgumentException("Tổng tiền trước thuế phải là số không âm");

    if (donHang.getTaxAmount() != null && donHang.getTaxAmount().compareTo(BigDecimal.ZERO) < 0)
        throw new IllegalArgumentException("Số tiền thuế phải là số không âm");

    if (donHang.getDiscountAmount() != null && donHang.getDiscountAmount().compareTo(BigDecimal.ZERO) < 0)
        throw new IllegalArgumentException("Số tiền giảm giá phải là số không âm");

    if (donHang.getTotalAmount() != null && donHang.getTotalAmount().compareTo(BigDecimal.ZERO) < 0)
        throw new IllegalArgumentException("Tổng tiền phải trả phải là số không âm");

    // Kiểm tra trùng mã (chỉ khi thêm mới — id == null)
    if (donHang.getOrderId() == null && existsByOrderNumber(donHang.getOrderNumber()))
        throw new IllegalArgumentException("Mã đơn hàng đã tồn tại");

    return donHangRepository.save(donHang);
}

    @Override
    public void deleteById(Integer id) {
        donHangRepository.deleteById(id);
    }

    @Override
    public boolean existsByOrderNumber(String orderNumber) {
        return donHangRepository.existsByOrderNumber(orderNumber);
    }

    @Override
    public List<DonHang> findByOrderDateBetween(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        return donHangRepository.findByOrderDateBetween(start, end);
    }
}