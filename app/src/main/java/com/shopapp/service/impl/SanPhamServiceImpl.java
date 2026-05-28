package com.shopapp.service.impl;

import com.shopapp.entity.DanhMuc;
import com.shopapp.entity.NhaCungCap;
import com.shopapp.entity.SanPham;
import com.shopapp.repository.SanPhamRepository;
import com.shopapp.service.SanPhamService;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for SanPham.
 * Contains the business logic for SanPham operations.
 */
public class SanPhamServiceImpl implements SanPhamService {

    private final SanPhamRepository sanPhamRepository;

    public SanPhamServiceImpl(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }

    @Override
    public Optional<SanPham> findById(Integer id) {
        return sanPhamRepository.findById(id);
    }

    @Override
    public List<SanPham> findAll() {
        return sanPhamRepository.findAll();
    }

    @Override
    public List<SanPham> findByCategory(DanhMuc category) {
        return sanPhamRepository.findByCategory(category);
    }

    @Override
    public List<SanPham> findBySupplier(NhaCungCap supplier) {
        return sanPhamRepository.findBySupplier(supplier);
    }

    @Override
    public Optional<SanPham> findBySku(String sku) {
        return sanPhamRepository.findBySku(sku);
    }

    @Override
    public SanPham save(SanPham sanPham) {
        return sanPhamRepository.save(sanPham);
    }

    @Override
    public void deleteById(Integer id) {
        sanPhamRepository.deleteById(id);
    }

    @Override
    public boolean existsBySku(String sku) {
        return sanPhamRepository.existsBySku(sku);
    }

    @Override
    public List<SanPham> findByProductNameContaining(String productName) {
        return sanPhamRepository.findByProductNameContaining(productName);
    }
}