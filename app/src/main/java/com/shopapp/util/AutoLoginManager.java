package com.shopapp.util;

import java.util.prefs.Preferences;

public class AutoLoginManager {

    private static final Preferences prefs = Preferences.userNodeForPackage(AutoLoginManager.class);

    // Thời gian token hết hạn: 7 ngày (ms)
    private static final long TOKEN_EXPIRY_MS = 1L * 24 * 60 * 60 * 1000;

    // ==================== SAVE ====================

    public static void save(String username, String token) {
        prefs.put("username", username);
        prefs.put("token", token);
        prefs.putLong("savedAt", System.currentTimeMillis());
    }

    // ==================== GET ====================

    public static String getToken() {
        return prefs.get("token", null);
    }

    public static String getUsername() {
        return prefs.get("username", null);
    }

    public static long getSavedAt() {
        return prefs.getLong("savedAt", -1L);
    }

    // ==================== CHECK ====================

    /**
     * Kiểm tra có thông tin đăng nhập đã lưu không
     */
    public static boolean hasSavedCredentials() {
        return getToken() != null && getUsername() != null;
    }

    /**
     * Kiểm tra token còn trong thời hạn không (mặc định 7 ngày)
     */
    public static boolean isTokenExpired() {
        long savedAt = getSavedAt();
        if (savedAt == -1L)
            return true;
        return System.currentTimeMillis() - savedAt > TOKEN_EXPIRY_MS;
    }

    /**
     * Kiểm tra tổng hợp: có credentials + chưa hết hạn
     */
    public static boolean isAutoLoginAvailable() {
        return hasSavedCredentials() && !isTokenExpired();
    }

    // ==================== CLEAR ====================

    /**
     * Xóa toàn bộ thông tin đăng nhập đã lưu (dùng khi logout)
     */
    public static void clear() {
        prefs.remove("username");
        prefs.remove("token");
        prefs.remove("savedAt");
    }

    // ==================== UTIL ====================

    /**
     * Debug: in ra toàn bộ thông tin đang lưu
     */
    public static void printAll() {
        System.out.println("=== AutoLoginManager ===");
        System.out.println("Username : " + getUsername());
        System.out.println("Token    : " + getToken());
        System.out.println("SavedAt  : " + getSavedAt());
        System.out.println("Expired  : " + isTokenExpired());
        System.out.println("========================");
    }
}