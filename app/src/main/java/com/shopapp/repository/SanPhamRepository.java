package com.shopapp.repository;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.DanhMuc;
import com.shopapp.entity.NhaCungCap;
import com.shopapp.entity.SanPham;

/**
 * Repository interface for SanPham entity.
 * Defines the contract for data access operations.
 */
public interface SanPhamRepository {
    Optional<SanPham> findById(Integer id);
    List<SanPham> findAll();
    List<SanPham> findByCategory(DanhMuc category);
    List<SanPham> findBySupplier(NhaCungCap supplier);
    Optional<SanPham> findBySku(String sku);
    SanPham save(SanPham sanPham);
    void deleteById(Integer id);
    boolean existsBySku(String sku);
    List<SanPham> findByProductNameContaining(String productName);
}