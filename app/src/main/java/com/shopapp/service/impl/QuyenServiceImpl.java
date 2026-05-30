package com.shopapp.service.impl;

import com.shopapp.entity.Quyen;
import com.shopapp.repository.QuyenRepository;
import com.shopapp.service.QuyenService;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for Quyen.
 */
public class QuyenServiceImpl implements QuyenService {

    private final QuyenRepository quyenRepository;

    public QuyenServiceImpl(QuyenRepository quyenRepository) {
        this.quyenRepository = quyenRepository;
    }

    @Override
    public Optional<Quyen> findById(Integer id) {
        return quyenRepository.findById(id);
    }

    @Override
    public List<Quyen> findAll() {
        return quyenRepository.findAll();
    }

    @Override
    public Optional<Quyen> findByPermissionCode(String permissionCode) {
        return quyenRepository.findByPermissionCode(permissionCode);
    }

    @Override
    public List<Quyen> findByModule(String module) {
        return quyenRepository.findByModule(module);
    }

    @Override
    public Quyen save(Quyen quyen) {
        return quyenRepository.save(quyen);
    }

    @Override
    public void deleteById(Integer id) {
        quyenRepository.deleteById(id);
    }

    @Override
    public boolean existsByPermissionCode(String permissionCode) {
        return quyenRepository.existsByPermissionCode(permissionCode);
    }
}
