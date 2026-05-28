package com.shopapp.repository.impl;

import com.shopapp.entity.NguoiDung;
import com.shopapp.entity.Vaitro;
import com.shopapp.repository.NguoiDungRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mindrot.jbcrypt.BCrypt;

/**
 * In-memory implementation of NguoiDungRepository for demonstration purposes.
 * In a real application, this would be replaced with a JPA/Hibernate implementation.
 */
public class NguoiDungRepositoryImpl implements NguoiDungRepository {

    private final List<NguoiDung> nguoiDungList = new CopyOnWriteArrayList<>();
    private int nextId = 1;

    public NguoiDungRepositoryImpl() {
        // Add some test data for demonstration
        initializeTestData();
    }

    private void initializeTestData() {
        // Create test role (assuming role_id 1 exists for Admin)
        Vaitro adminRole = new Vaitro();
        adminRole.setRoleId(1);
        adminRole.setRoleName("Admin");
        adminRole.setDescription("Quản trị hệ thống");

        // Create test user: admin / 123456
        NguoiDung adminUser = new NguoiDung();
        adminUser.setUsername("admin");
        adminUser.setPasswordHash(BCrypt.hashpw("123456", BCrypt.gensalt()));
        adminUser.setFullName("Administrator");
        adminUser.setEmail("admin@example.com");
        adminUser.setPhone("0123456789");
        adminUser.setIsActive(true);
        adminUser.setRole(adminRole);

        // Create test user: user / user123
        Vaitro userRole = new Vaitro();
        userRole.setRoleId(2);
        userRole.setRoleName("Nhân viên");
        userRole.setDescription("Nhân viên bán hàng");

        NguoiDung regularUser = new NguoiDung();
        regularUser.setUsername("user");
        regularUser.setPasswordHash(BCrypt.hashpw("user123", BCrypt.gensalt()));
        regularUser.setFullName("Regular User");
        regularUser.setEmail("user@example.com");
        regularUser.setPhone("0987654321");
        regularUser.setIsActive(true);
        regularUser.setRole(userRole);

        nguoiDungList.add(adminUser);
        nguoiDungList.add(regularUser);
    }

    @Override
    public Optional<NguoiDung> findById(Integer id) {
        return nguoiDungList.stream()
                .filter(nguoiDung -> nguoiDung.getUserId().equals(id))
                .findFirst();
    }

    @Override
    public List<NguoiDung> findAll() {
        return new ArrayList<>(nguoiDungList);
    }

    @Override
    public Optional<NguoiDung> findByUsername(String username) {
        return nguoiDungList.stream()
                .filter(nguoiDung -> nguoiDung.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public NguoiDung save(NguoiDung nguoiDung) {
        if (nguoiDung.getUserId() == null) {
            // New entity
            nguoiDung.setUserId(nextId++);
            nguoiDungList.add(nguoiDung);
        } else {
            // Existing entity - update
            for (int i = 0; i < nguoiDungList.size(); i++) {
                if (nguoiDungList.get(i).getUserId().equals(nguoiDung.getUserId())) {
                    nguoiDungList.set(i, nguoiDung);
                    break;
                }
            }
        }
        return nguoiDung;
    }

    @Override
    public void deleteById(Integer id) {
        nguoiDungList.removeIf(nguoiDung -> nguoiDung.getUserId().equals(id));
    }

    @Override
    public boolean existsByUsername(String username) {
        return nguoiDungList.stream()
                .anyMatch(nguoiDung -> nguoiDung.getUsername().equals(username));
    }
}