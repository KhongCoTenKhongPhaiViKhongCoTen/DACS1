package com.shopapp.repository.impl;

import com.shopapp.entity.NguoiDung;
import com.shopapp.entity.RolePermissions;
import com.shopapp.entity.Vaitro;
import com.shopapp.repository.NguoiDungRepository;
import com.shopapp.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Hibernate implementation of NguoiDungRepository.
 * Uses Hibernate for database persistence.
 */
public class NguoiDungRepositoryImpl implements NguoiDungRepository {

    @Override
    public Optional<NguoiDung> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NguoiDung nguoiDung = session.find(NguoiDung.class, id);
            // Initialize lazy associations to avoid issues when session is closed
            if (nguoiDung != null && nguoiDung.getRole() != null) {
                // Force initialization of role and its permissions
                Vaitro role = nguoiDung.getRole();
                role.getRoleName(); // Initialize role proxy
                if (role.getRolePermissions() != null) {
                    // Initialize the collection and the permission inside each RolePermissions
                    for (RolePermissions rp : role.getRolePermissions()) {
                        // Initialize the permission inside the RolePermissions
                        rp.getPermission().getPermissionCode();
                    }
                }
            }
            return Optional.ofNullable(nguoiDung);
        }
    }

    @Override
    public List<NguoiDung> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<NguoiDung> nguoiDungList = session.createQuery("from NguoiDung", NguoiDung.class).list();
            // Initialize lazy associations for all users
            for (NguoiDung nguoiDung : nguoiDungList) {
                if (nguoiDung != null && nguoiDung.getRole() != null) {
                    // Force initialization of role and its permissions
                    Vaitro role = nguoiDung.getRole();
                    role.getRoleName(); // Initialize role proxy
                    if (role.getRolePermissions() != null) {
                        // Initialize the collection and the permission inside each RolePermissions
                        for (RolePermissions rp : role.getRolePermissions()) {
                            // Initialize the permission inside the RolePermissions
                            rp.getPermission().getPermissionCode();
                        }
                    }
                }
            }
            return nguoiDungList;
        }
    }

    @Override
    public Optional<NguoiDung> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NguoiDung nguoiDung = session.createQuery(
                    "from NguoiDung where username = :username", NguoiDung.class)
                    .setParameter("username", username)
                    .uniqueResult();
            // Initialize lazy associations to avoid issues when session is closed
            if (nguoiDung != null && nguoiDung.getRole() != null) {
                // Force initialization of role and its permissions
                Vaitro role = nguoiDung.getRole();
                role.getRoleName(); // Initialize role proxy
                if (role.getRolePermissions() != null) {
                    // Initialize the collection and the permission inside each RolePermissions
                    for (RolePermissions rp : role.getRolePermissions()) {
                        // Initialize the permission inside the RolePermissions
                        rp.getPermission().getPermissionCode();
                    }
                }
            }
            return Optional.ofNullable(nguoiDung);
        }
    }

    @Override
    public NguoiDung save(NguoiDung nguoiDung) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            NguoiDung saved = session.merge(nguoiDung);
            transaction.commit();
            return saved;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void deleteById(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            NguoiDung nguoiDung = session.find(NguoiDung.class, id);
            if (nguoiDung != null) {
                session.remove(nguoiDung);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                    "select count(n) from NguoiDung n where n.username = :username", Long.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }
}