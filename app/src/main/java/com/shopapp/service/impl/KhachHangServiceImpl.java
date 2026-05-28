package com.shopapp.service.impl;

import com.shopapp.entity.KhachHang;
import com.shopapp.repository.KhachHangRepository;
import com.shopapp.service.KhachHangService;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for KhachHang.
 * Contains the business logic for KhachHang operations.
 */
public class KhachHangServiceImpl implements KhachHangService {

    private final KhachHangRepository khachHangRepository;

    public KhachHangServiceImpl(KhachHangRepository khachHangRepository) {
        this.khachHangRepository = khachHangRepository;
    }

    @Override
    public Optional<KhachHang> findById(Integer id) {
        return khachHangRepository.findById(id);
    }

    @Override
    public List<KhachHang> findAll() {
        return khachHangRepository.findAll();
    }

    @Override
    public Optional<KhachHang> findByPhone(String phone) {
        return khachHangRepository.findByPhone(phone);
    }

    @Override
    public Optional<KhachHang> findByEmail(String email) {
        return khachHangRepository.findByEmail(email);
    }

    @Override
    public KhachHang save(KhachHang khachHang) {
        return khachHangRepository.save(khachHang);
    }

    @Override
    public void deleteById(Integer id) {
        khachHangRepository.deleteById(id);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return khachHangRepository.existsByPhone(phone);
    }

    @Override
    public boolean existsByEmail(String email) {
        return khachHangRepository.existsByEmail(email);
    }
}