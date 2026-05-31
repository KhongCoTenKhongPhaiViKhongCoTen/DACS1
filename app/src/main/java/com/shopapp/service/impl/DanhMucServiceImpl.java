package com.shopapp.service.impl;

import com.shopapp.entity.DanhMuc;
import com.shopapp.repository.DanhMucRepository;
import com.shopapp.repository.impl.DanhMucRepositoryImpl;
import com.shopapp.service.DanhMucService;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for DanhMuc.
 * Contains the business logic for DanhMuc operations.
 */
public class DanhMucServiceImpl implements DanhMucService {

    private final DanhMucRepository danhMucRepository;

    public DanhMucServiceImpl(DanhMucRepository danhMucRepository) {
        this.danhMucRepository = danhMucRepository;
    }

    @Override
    public Optional<DanhMuc> findById(Integer id) {
        return danhMucRepository.findById(id);
    }

    @Override
    public List<DanhMuc> findAll() {
        return danhMucRepository.findAll();
    }

    @Override
    public DanhMuc save(DanhMuc danhMuc) {
        return danhMucRepository.save(danhMuc);
    }

    @Override
    public void deleteById(Integer id) {
        danhMucRepository.deleteById(id);
    }

    @Override
    public boolean existsByCategoryName(String categoryName) {
        // Delegate to repository implementation
        DanhMucRepository repository = new DanhMucRepositoryImpl();
        return repository.existsByCategoryName(categoryName);
    }
}