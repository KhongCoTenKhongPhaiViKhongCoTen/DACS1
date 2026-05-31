package com.shopapp.repository;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.SanPham;
import com.shopapp.entity.TonKho;

/**
 * Repository interface for TonKho entity.
 * Defines the contract for data access operations.
 */
public interface TonKhoRepository {
    Optional<TonKho> findById(Integer id);
    List<TonKho> findAll();
    TonKho save(TonKho tonKho);
    void deleteById(Integer id);
    Optional<TonKho> findByProduct(SanPham product);
}