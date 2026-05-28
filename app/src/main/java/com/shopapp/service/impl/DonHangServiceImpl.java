package com.shopapp.service.impl;

import com.shopapp.entity.DonHang;
import com.shopapp.entity.KhachHang;
import com.shopapp.entity.NguoiDung;
import com.shopapp.repository.DonHangRepository;
import com.shopapp.service.DonHangService;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for DonHang.
 * Contains the business logic for DonHang operations.
 */
public class DonHangServiceImpl implements DonHangService {

    private final DonHangRepository donHangRepository;

    public DonHangServiceImpl(DonHangRepository donHangRepository) {
        this.donHangRepository = donHangRepository;
    }

    @Override
    public Optional<DonHang> findById(Integer id) {
        return donHangRepository.findById(id);
    }

    @Override
    public List<DonHang> findAll() {
        return donHangRepository.findAll();
    }

    @Override
    public List<DonHang> findByCustomer(KhachHang customer) {
        return donHangRepository.findByCustomer(customer);
    }

    @Override
    public List<DonHang> findByUser(NguoiDung user) {
        return donHangRepository.findByUser(user);
    }

    @Override
    public List<DonHang> findByStatus(String status) {
        return donHangRepository.findByStatus(status);
    }

    @Override
    public Optional<DonHang> findByOrderNumber(String orderNumber) {
        return donHangRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public DonHang save(DonHang donHang) {
        return donHangRepository.save(donHang);
    }

    @Override
    public void deleteById(Integer id) {
        donHangRepository.deleteById(id);
    }

    @Override
    public boolean existsByOrderNumber(String orderNumber) {
        return donHangRepository.existsByOrderNumber(orderNumber);
    }

    @Override
    public List<DonHang> findByOrderDateBetween(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        return donHangRepository.findByOrderDateBetween(start, end);
    }
}