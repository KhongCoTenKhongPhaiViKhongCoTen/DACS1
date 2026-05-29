package com.shopapp.test;

import com.shopapp.entity.NguoiDung;
import com.shopapp.repository.impl.NguoiDungRepositoryImpl;
import com.shopapp.util.HibernateUtil;

public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");

        try {
            // Test Hibernate configuration
            System.out.println("HibernateUtil loaded successfully");

            // Test user repository
            NguoiDungRepositoryImpl userRepository = new NguoiDungRepositoryImpl();
            System.out.println("UserRepository created successfully");

            // Try to find a user
            var user = userRepository.findByUsername("admin");
            if (user.isPresent()) {
                System.out.println("Found user: " + user.get().getUsername());
                System.out.println("User role: " + user.get().getRole().getRoleName());
            } else {
                System.out.println("No user found with username 'admin'");

                // Let's check all users
                var allUsers = userRepository.findAll();
                System.out.println("Total users in database: " + allUsers.size());
                for (NguoiDung u : allUsers) {
                    System.out.println("User: " + u.getUsername() +
                                     ", Role: " + (u.getRole() != null ? u.getRole().getRoleName() : "No role"));
                }
            }

        } catch (Exception e) {
            System.err.println("Database test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clean up
            HibernateUtil.shutdown();
        }
    }
}