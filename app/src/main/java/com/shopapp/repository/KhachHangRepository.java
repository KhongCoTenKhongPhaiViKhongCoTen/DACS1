package com.shopapp.repository;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.KhachHang;

/**
 * Repository interface for KhachHang entity.
 * Defines the contract for data access operations.
 */
public interface KhachHangRepository {
    Optional<KhachHang> findById(Integer id);
    List<KhachHang> findAll();
    Optional<KhachHang> findByPhone(String phone);
    Optional<KhachHang> findByEmail(String email);
    KhachHang save(KhachHang khachHang);
    void deleteById(Integer id);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
}