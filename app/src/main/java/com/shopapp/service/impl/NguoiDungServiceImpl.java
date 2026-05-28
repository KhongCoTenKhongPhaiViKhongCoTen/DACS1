package com.shopapp.service.impl;

import com.shopapp.entity.NguoiDung;
import com.shopapp.repository.NguoiDungRepository;
import com.shopapp.service.NguoiDungService;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for NguoiDung.
 * Contains the business logic for NguoiDung operations.
 */
public class NguoiDungServiceImpl implements NguoiDungService {

    private final NguoiDungRepository nguoiDungRepository;

    public NguoiDungServiceImpl(NguoiDungRepository nguoiDungRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
    }

    @Override
    public Optional<NguoiDung> findById(Integer id) {
        return nguoiDungRepository.findById(id);
    }

    @Override
    public List<NguoiDung> findAll() {
        return nguoiDungRepository.findAll();
    }

    @Override
    public Optional<NguoiDung> findByUsername(String username) {
        return nguoiDungRepository.findByUsername(username);
    }

    @Override
    public NguoiDung save(NguoiDung nguoiDung) {
        return nguoiDungRepository.save(nguoiDung);
    }

    @Override
    public void deleteById(Integer id) {
        nguoiDungRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return nguoiDungRepository.existsByUsername(username);
    }
}