package com.shopapp.repository;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.NguoiDung;

/**
 * Repository interface for NguoiDung entity.
 * Defines the contract for data access operations.
 */
public interface NguoiDungRepository {
    Optional<NguoiDung> findById(Integer id);
    List<NguoiDung> findAll();
    Optional<NguoiDung> findByUsername(String username);
    NguoiDung save(NguoiDung nguoiDung);
    void deleteById(Integer id);
    boolean existsByUsername(String username);
}