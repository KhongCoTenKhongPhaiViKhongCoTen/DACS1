package com.shopapp.service;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.SanPham;
import com.shopapp.entity.TonKho;

/**
 * Service interface for TonKho business logic.
 * Defines the contract for operations on TonKho entities.
 */
public interface TonKhoService {
    Optional<TonKho> findById(Integer id);
    List<TonKho> findAll();
    TonKho save(TonKho tonKho);
    void deleteById(Integer id);
    Optional<TonKho> findByProduct(SanPham product);
}