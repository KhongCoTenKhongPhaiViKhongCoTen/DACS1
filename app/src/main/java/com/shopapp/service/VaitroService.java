package com.shopapp.service;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.Vaitro;

/**
 * Service interface for Vaitro business logic.
 * Defines the contract for operations on Vaitro entities.
 */
public interface VaitroService {
    Optional<Vaitro> findById(Integer id);
    List<Vaitro> findAll();
    Optional<Vaitro> findByRoleName(String roleName);
    Vaitro save(Vaitro vaitro);
    void deleteById(Integer id);
    boolean existsByRoleName(String roleName);
}