package com.shopapp.service;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.KhachHang;

/**
 * Service interface for KhachHang business logic.
 * Defines the contract for operations on KhachHang entities.
 */
public interface KhachHangService {
    Optional<KhachHang> findById(Integer id);
    List<KhachHang> findAll();
    Optional<KhachHang> findByPhone(String phone);
    Optional<KhachHang> findByEmail(String email);
    KhachHang save(KhachHang khachHang);
    void deleteById(Integer id);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
}