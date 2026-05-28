package com.shopapp.service;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.DonHang;
import com.shopapp.entity.KhachHang;
import com.shopapp.entity.NguoiDung;

/**
 * Service interface for DonHang business logic.
 * Defines the contract for operations on DonHang entities.
 */
public interface DonHangService {
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