package com.shopapp.service;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.DanhMuc;

/**
 * Service interface for DanhMuc business logic.
 * Defines the contract for operations on DanhMuc entities.
 */
public interface DanhMucService {
    Optional<DanhMuc> findById(Integer id);
    List<DanhMuc> findAll();
    DanhMuc save(DanhMuc danhMuc);
    void deleteById(Integer id);
    boolean existsByCategoryName(String categoryName);
}