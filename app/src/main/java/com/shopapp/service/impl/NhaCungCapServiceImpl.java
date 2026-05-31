package com.shopapp.service.impl;

import com.shopapp.entity.NhaCungCap;
import com.shopapp.repository.NhaCungCapRepository;
import com.shopapp.repository.impl.NhaCungCapRepositoryImpl;
import com.shopapp.service.NhaCungCapService;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for NhaCungCap.
 * Contains the business logic for NhaCungCap operations.
 */
public class NhaCungCapServiceImpl implements NhaCungCapService {

    private final NhaCungCapRepository nhaCungCapRepository;

    public NhaCungCapServiceImpl(NhaCungCapRepository nhaCungCapRepository) {
        this.nhaCungCapRepository = nhaCungCapRepository;
    }

    @Override
    public Optional<NhaCungCap> findById(Integer id) {
        return nhaCungCapRepository.findById(id);
    }

    @Override
    public List<NhaCungCap> findAll() {
        return nhaCungCapRepository.findAll();
    }

    @Override
    public NhaCungCap save(NhaCungCap nhaCungCap) {
        return nhaCungCapRepository.save(nhaCungCap);
    }

    @Override
    public void deleteById(Integer id) {
        nhaCungCapRepository.deleteById(id);
    }

    @Override
    public boolean existsByCompanyName(String companyName) {
        // Delegate to repository implementation
        NhaCungCapRepository repository = new NhaCungCapRepositoryImpl();
        return repository.existsByCompanyName(companyName);
    }
}