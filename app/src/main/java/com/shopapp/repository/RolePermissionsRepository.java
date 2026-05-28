package com.shopapp.repository;

import java.util.List;
import java.util.Optional;

import com.shopapp.entity.RolePermissions;

/**
 * Repository interface for RolePermissions entity.
 * Defines the contract for data access operations.
 */
public interface RolePermissionsRepository {
    List<RolePermissions> findByRoleId(Integer roleId);
    Optional<RolePermissions> findByRoleIdAndPermissionId(Integer roleId, Integer permissionId);
    RolePermissions save(RolePermissions rolePermissions);
    void deleteByRoleIdAndPermissionId(Integer roleId, Integer permissionId);
}