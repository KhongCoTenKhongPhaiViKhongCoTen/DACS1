package com.shopapp.repository;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.DanhMuc;


public interface DanhMucRepository {
    Optional<DanhMuc> findById(Integer id);
    List<DanhMuc> findAll();
    DanhMuc save(DanhMuc danhMuc);
    void deleteById(Integer id);
    boolean existsByCategoryName(String categoryName);
}