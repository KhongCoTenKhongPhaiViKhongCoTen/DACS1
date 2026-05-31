package com.shopapp.service.impl;

import com.shopapp.entity.SanPham;
import com.shopapp.entity.TonKho;
import com.shopapp.repository.TonKhoRepository;
import com.shopapp.service.TonKhoService;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for TonKho.
 * Contains the business logic for TonKho operations.
 */
public class TonKhoServiceImpl implements TonKhoService {

    private final TonKhoRepository tonKhoRepository;

    public TonKhoServiceImpl(TonKhoRepository tonKhoRepository) {
        this.tonKhoRepository = tonKhoRepository;
    }

    @Override
    public Optional<TonKho> findById(Integer id) {
        return tonKhoRepository.findById(id);
    }

    @Override
    public List<TonKho> findAll() {
        return tonKhoRepository.findAll();
    }

    @Override
    public TonKho save(TonKho tonKho) {
        return tonKhoRepository.save(tonKho);
    }

    @Override
    public void deleteById(Integer id) {
        tonKhoRepository.deleteById(id);
    }

    @Override
    public Optional<TonKho> findByProduct(SanPham product) {
        return tonKhoRepository.findByProduct(product);
    }
}