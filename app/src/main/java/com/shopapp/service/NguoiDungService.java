package com.shopapp.service;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.NguoiDung;

/**
 * Service interface for NguoiDung business logic.
 * Defines the contract for operations on NguoiDung entities.
 */
public interface NguoiDungService {
    Optional<NguoiDung> findById(Integer id);
    List<NguoiDung> findAll();
    Optional<NguoiDung> findByUsername(String username);
    NguoiDung save(NguoiDung nguoiDung);
    void deleteById(Integer id);
    boolean existsByUsername(String username);
}