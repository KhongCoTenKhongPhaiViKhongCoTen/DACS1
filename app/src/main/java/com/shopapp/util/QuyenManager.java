package com.shopapp.util;

import com.shopapp.entity.Quyen;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class QuyenManager {
    private Map<String, Quyen> quyenMap = new HashMap<>();

    public void update(Set<Quyen> newPermissions) {
        this.quyenMap = (newPermissions != null)
                ? newPermissions.stream()
                        .collect(Collectors.toMap(Quyen::getPermissionCode, q -> q))
                : new HashMap<>();
    }

    public boolean has(Quyen permission) {
        return quyenMap.containsKey(permission.getPermissionCode());
    }

    public boolean has(String code) {
        return quyenMap.containsKey(code);
    }

    public boolean hasAny(Set<Quyen> permissions) {
        return permissions.stream()
                .anyMatch(q -> quyenMap.containsKey(q.getPermissionCode()));
    }

    public boolean hasAnyCode(String... codes) {
        for (String code : codes) {
            if (quyenMap.containsKey(code))
                return true;
        }
        return false;
    }

    public boolean hasAll(Set<Quyen> permissions) {
        return permissions.stream()
                .allMatch(q -> quyenMap.containsKey(q.getPermissionCode()));
    }

    public Quyen getByCode(String code) {
        return quyenMap.get(code);
    }

    public Map<String, Quyen> getAll() {
        return Collections.unmodifiableMap(quyenMap);
    }
}