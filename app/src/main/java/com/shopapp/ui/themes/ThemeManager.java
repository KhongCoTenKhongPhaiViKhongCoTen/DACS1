package com.shopapp.ui.themes;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the application's theme and notifies listeners of theme changes.
 * Provides font utilities for proper Vietnamese and emoji display.
 */
public class ThemeManager {

    // Singleton Instance
    private static final ThemeManager instance = new ThemeManager();

    private Theme currentTheme = Theme.DARK;
    private final List<ThemeChangeListener> listeners = new ArrayList<>();

    private ThemeManager() {
    } // Chặn khởi tạo ngoài

    public static ThemeManager getInstance() {
        return instance;
    }

    public Theme getCurrent() {
        return currentTheme;
    }

    public void set(Theme theme) {
        if (currentTheme != theme) {
            currentTheme = theme;
            notifyListeners();
        }
    }

    public void addListener(ThemeChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(ThemeChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        listeners.forEach(l -> l.onThemeChanged(currentTheme));
    }

    // =========================================================================
    // SỬA LỖI TẠI ĐÂY: Gọi thông qua Singleton AppFont thay vì qua enum Theme cũ
    // =========================================================================
    public Font getFont(float size) {
        return AppFont.getInstance().getUIFont(Font.PLAIN, (int) size);
    }

    public Font getBoldFont(float size) {
        return AppFont.getInstance().getUIFont(Font.BOLD, (int) size);
    }

    public Font getItalicFont(float size) {
        return AppFont.getInstance().getUIFont(Font.ITALIC, (int) size);
    }

    public interface ThemeChangeListener {
        void onThemeChanged(Theme theme);
    }
}