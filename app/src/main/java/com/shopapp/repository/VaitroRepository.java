package com.shopapp.repository;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.Vaitro;

/**
 * Repository interface for Vaitro entity.
 * Defines the contract for data access operations.
 */
public interface VaitroRepository {
    Optional<Vaitro> findById(Integer id);
    List<Vaitro> findAll();
    Optional<Vaitro> findByTenVaiTro(String tenVaiTro); // Assuming there's a tenVaiTro field
    Vaitro save(Vaitro vaitro);
    void deleteById(Integer id);
    boolean existsByTenVaiTro(String tenVaiTro);
}