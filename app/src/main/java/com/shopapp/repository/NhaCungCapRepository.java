package com.shopapp.repository;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.NhaCungCap;

/**
 * Repository interface for NhaCungCap entity.
 * Defines the contract for data access operations.
 */
public interface NhaCungCapRepository {
    Optional<NhaCungCap> findById(Integer id);
    List<NhaCungCap> findAll();
    NhaCungCap save(NhaCungCap nhaCungCap);
    void deleteById(Integer id);
    boolean existsByCompanyName(String companyName);
}