package com.shopapp.repository;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.DonHang;
import com.shopapp.entity.KhachHang;
import com.shopapp.entity.NguoiDung;

/**
 * Repository interface for DonHang entity.
 * Defines the contract for data access operations.
 */
public interface DonHangRepository {
    Optional<DonHang> findById(Integer id);
    List<DonHang> findAll();
    List<DonHang> findByCustomer(KhachHang customer);
    List<DonHang> findByUser(NguoiDung user);
    List<DonHang> findByStatus(String status);
    Optional<DonHang> findByOrderNumber(String orderNumber);
    DonHang save(DonHang donHang);
    void deleteById(Integer id);
    boolean existsByOrderNumber(String orderNumber);
    List<DonHang> findByOrderDateBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
}