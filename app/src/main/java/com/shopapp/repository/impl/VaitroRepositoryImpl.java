package com.shopapp.repository.impl;

import com.shopapp.entity.Vaitro;
import com.shopapp.repository.VaitroRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * In-memory implementation of VaitroRepository for demonstration purposes.
 * In a real application, this would be replaced with a JPA/Hibernate implementation.
 */
public class VaitroRepositoryImpl implements VaitroRepository {

    private final List<Vaitro> vaitroList = new CopyOnWriteArrayList<>();
    private int nextId = 1;

    @Override
    public Optional<Vaitro> findById(Integer id) {
        return vaitroList.stream()
                .filter(vaitro -> vaitro.getRoleId().equals(id))
                .findFirst();
    }

    @Override
    public List<Vaitro> findAll() {
        return new ArrayList<>(vaitroList);
    }

    @Override
    public Optional<Vaitro> findByTenVaiTro(String tenVaiTro) {
        return vaitroList.stream()
                .filter(vaitro -> vaitro.getRoleName().equals(tenVaiTro))
                .findFirst();
    }

    @Override
    public Vaitro save(Vaitro vaitro) {
        if (vaitro.getRoleId() == null) {
            // New entity
            vaitro.setRoleId(nextId++);
            vaitroList.add(vaitro);
        } else {
            // Existing entity - update
            for (int i = 0; i < vaitroList.size(); i++) {
                if (vaitroList.get(i).getRoleId().equals(vaitro.getRoleId())) {
                    vaitroList.set(i, vaitro);
                    break;
                }
            }
        }
        return vaitro;
    }

    @Override
    public void deleteById(Integer id) {
        vaitroList.removeIf(vaitro -> vaitro.getRoleId().equals(id));
    }

    @Override
    public boolean existsByTenVaiTro(String tenVaiTro) {
        return vaitroList.stream()
                .anyMatch(vaitro -> vaitro.getRoleName().equals(tenVaiTro));
    }
}