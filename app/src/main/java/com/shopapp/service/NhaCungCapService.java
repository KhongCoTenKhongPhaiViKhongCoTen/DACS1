package com.shopapp.service;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.NhaCungCap;

/**
 * Service interface for NhaCungCap business logic.
 * Defines the contract for operations on NhaCungCap entities.
 */
public interface NhaCungCapService {
    Optional<NhaCungCap> findById(Integer id);
    List<NhaCungCap> findAll();
    NhaCungCap save(NhaCungCap nhaCungCap);
    void deleteById(Integer id);
    boolean existsByCompanyName(String companyName);
}