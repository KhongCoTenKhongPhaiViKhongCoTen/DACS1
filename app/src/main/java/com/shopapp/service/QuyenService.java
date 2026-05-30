package com.shopapp.service;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.Quyen;

/**
 * Service interface for Quyen business logic.
 */
public interface QuyenService {
    Optional<Quyen> findById(Integer id);
    List<Quyen> findAll();
    Optional<Quyen> findByPermissionCode(String permissionCode);
    List<Quyen> findByModule(String module);
    Quyen save(Quyen quyen);
    void deleteById(Integer id);
    boolean existsByPermissionCode(String permissionCode);
}
