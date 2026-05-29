package com.shopapp.repository.impl;

import com.shopapp.entity.RolePermissions;
import com.shopapp.repository.RolePermissionsRepository;
import com.shopapp.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Hibernate implementation of RolePermissionsRepository.
 * Uses Hibernate for database persistence.
 */
public class RolePermissionsRepositoryImpl implements RolePermissionsRepository {

    @Override
    public List<RolePermissions> findByRoleId(Integer roleId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from RolePermissions where role.roleId = :roleId", RolePermissions.class)
                    .setParameter("roleId", roleId)
                    .list();
        }
    }

    @Override
    public Optional<RolePermissions> findByRoleIdAndPermissionId(Integer roleId, Integer permissionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            RolePermissions rolePermissions = session.createQuery(
                    "from RolePermissions where role.roleId = :roleId and permission.permissionId = :permissionId",
                    RolePermissions.class)
                    .setParameter("roleId", roleId)
                    .setParameter("permissionId", permissionId)
                    .uniqueResult();
            return Optional.ofNullable(rolePermissions);
        }
    }

    @Override
    public RolePermissions save(RolePermissions rolePermissions) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            RolePermissions saved = session.merge(rolePermissions);
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
    public void deleteByRoleIdAndPermissionId(Integer roleId, Integer permissionId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            RolePermissions rolePermissions = session.createQuery(
                    "from RolePermissions where role.roleId = :roleId and permission.permissionId = :permissionId",
                    RolePermissions.class)
                    .setParameter("roleId", roleId)
                    .setParameter("permissionId", permissionId)
                    .uniqueResult();
            if (rolePermissions != null) {
                session.remove(rolePermissions);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}