package com.shopapp;

import com.shopapp.entity.NguoiDung;
import com.shopapp.entity.Quyen;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AppSys {
    private AppSys() {
    };

    private static final AppSys Self = new AppSys();
    private NguoiDung nguoiDung = null;
    private Set<Quyen> quyenSet = new HashSet<>();

    public static void setNguoiDung(NguoiDung nguoiDung) {
        if (Self.nguoiDung != null) {
            throw new IllegalStateException("NguoiDung đã được thiết lập. Không thể thay đổi.");
        }

        Self.nguoiDung = nguoiDung;
        /**
         * Khi thiết lập người dùng, tự động lấy quyền của người dùng đó
         * và lưu vào quyenSet để tiện kiểm tra quyền trong suốt phiên làm việc của ứng dụng.
         * 
         */
        if (nguoiDung != null) {
            Self.quyenSet = nguoiDung.getPermissions();

            IO.println("Quyen Truy cap nguoi dung " + nguoiDung.getUsername() + " " + Self.quyenSet.toString());
        }
    }

    public static NguoiDung getNguoiDung() {
        return Self.nguoiDung;
    }

    public static Set<Quyen> getQuyenSet() {
        return Self.quyenSet;
    }

    /**
     * Check if the current user has a specific permission
     * 
     * @param permission The permission to check
     * @return true if the user has the permission, false otherwise
     */
    public static boolean hasPermission(Quyen permission) {
        return Self.quyenSet.contains(permission);
    }

    /**
     * Check if the current user has a specific permission by its code
     * 
     * @param permissionCode The permission code to check
     * @return true if the user has the permission, false otherwise
     */
    public static boolean hasPermission(String permissionCode) {
        for (Quyen q : Self.quyenSet) {
            if (q.getPermissionCode().equals(permissionCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a permission by its code from the current user's permission set
     * 
     * @param permissionCode The permission code to find
     * @return The permission object if found, null otherwise
     */
    public static Quyen getPermissionByCode(String permissionCode) {
        for (Quyen q : Self.quyenSet) {
            if (q.getPermissionCode().equals(permissionCode)) {
                return q;
            }
        }
        return null;
    }

    /**
     * Check if the current user has any of the specified permissions
     * 
     * @param permissions The permissions to check
     * @return true if the user has at least one of the permissions, false otherwise
     */
    public static boolean hasAnyPermission(Set<Quyen> permissions) {
        for (Quyen permission : permissions) {
            if (Self.quyenSet.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the user has any of the specified permissions by their codes
     * 
     * @param permissionCodes The permission codes to check
     * @return true if the user has at least one of the permissions, false otherwise
     */
    public static boolean hasAnyPermissionByCode(Set<String> permissionCodes) {
        for (String code : permissionCodes) {
            if (hasPermission(code)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the user has any of the specified permissions by their codes
     * (varargs version)
     * 
     * @param permissionCodes The permission codes to check
     * @return true if the user has at least one of the permissions, false otherwise
     */
    public static boolean hasAnyPermissionByCode(String... permissionCodes) {
        return hasAnyPermissionByCode(new HashSet<>(Arrays.asList(permissionCodes)));
    }

    /**
     * Kiểm tra xem người dùng hiện tại có tất cả các quyền được chỉ định hay không.
     *
     * @param permissions Các quyền cần kiểm tra
     * @return Trả về true nếu người dùng có đầy đủ quyền, ngược lại trả về false.
     */
    public static boolean hasAllPermissions(Set<Quyen> permissions) {
        return Self.quyenSet.containsAll(permissions);
    }
}