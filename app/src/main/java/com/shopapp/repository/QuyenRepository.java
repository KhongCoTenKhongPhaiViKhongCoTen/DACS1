package com.shopapp.repository;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.Quyen;

/**
 * Repository interface for Quyen entity.
 */
public interface QuyenRepository {
    Optional<Quyen> findById(Integer id);
    List<Quyen> findAll();
    Optional<Quyen> findByPermissionCode(String permissionCode);
    List<Quyen> findByModule(String module);
    Quyen save(Quyen quyen);
    void deleteById(Integer id);
    boolean existsByPermissionCode(String permissionCode);
}
