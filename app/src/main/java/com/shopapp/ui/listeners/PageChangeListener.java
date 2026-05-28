package com.shopapp.ui.listeners;

/**
 * Interface để lắng nghe sự kiện đổi trang
 */
public interface PageChangeListener {
    /**
     * Gọi khi cần đổi trang
     * 
     * @param pageKey Key của trang (home, student, course, settings)
     */
    void onPageChange(String pageKey);
}
