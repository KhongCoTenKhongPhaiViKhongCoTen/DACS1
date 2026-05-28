package com.shopapp.service.impl;

import com.shopapp.entity.Vaitro;
import com.shopapp.repository.VaitroRepository;
import com.shopapp.service.VaitroService;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for Vaitro.
 * Contains the business logic for Vaitro operations.
 */
public class VaitroServiceImpl implements VaitroService {

    private final VaitroRepository vaitroRepository;

    public VaitroServiceImpl(VaitroRepository vaitroRepository) {
        this.vaitroRepository = vaitroRepository;
    }

    @Override
    public Optional<Vaitro> findById(Integer id) {
        return vaitroRepository.findById(id);
    }

    @Override
    public List<Vaitro> findAll() {
        return vaitroRepository.findAll();
    }

    @Override
    public Vaitro save(Vaitro vaitro) {
        return vaitroRepository.save(vaitro);
    }

    @Override
    public void deleteById(Integer id) {
        vaitroRepository.deleteById(id);
    }

    @Override
    public Optional<Vaitro> findByRoleName(String roleName) {
        return vaitroRepository.findByTenVaiTro(roleName);
    }

    @Override
    public boolean existsByRoleName(String roleName) {
        return vaitroRepository.existsByTenVaiTro(roleName);
    }
}