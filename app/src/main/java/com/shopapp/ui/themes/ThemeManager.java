package com.shopapp.ui.themes;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the application's theme and notifies listeners of theme changes.
 * Provides font utilities for proper Vietnamese and emoji display.
 */
public class ThemeManager {
    private static Theme currentTheme = Theme.DARK;
    private static final List<ThemeChangeListener> listeners = new ArrayList<>();

    public static Theme getCurrentTheme() {
        return currentTheme;
    }

    public static void setTheme(Theme theme) {
        if (currentTheme != theme) {
            currentTheme = theme;
            notifyListeners();
        }
    }

    public static void addThemeChangeListener(ThemeChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public static void removeThemeChangeListener(ThemeChangeListener listener) {
        listeners.remove(listener);
    }

    private static void notifyListeners() {
        listeners.forEach(l -> l.onThemeChanged(currentTheme));
    }

    /**
     * Gets the default UI font for the current theme.
     * @param size Font size in points
     * @return Font suitable for UI elements
     */
    public static Font getFont(float size) {
        return currentTheme.getFont(size);
    }

    /**
     * Gets the bold UI font for the current theme.
     * @param size Font size in points
     * @return Bold font suitable for UI elements
     */
    public static Font getBoldFont(float size) {
        return currentTheme.getBoldFont(size);
    }

    /**
     * Gets the italic UI font for the current theme.
     * @param size Font size in points
     * @return Italic font suitable for UI elements
     */
    public static Font getItalicFont(float size) {
        return currentTheme.getItalicFont(size);
    }

    public interface ThemeChangeListener {
        void onThemeChanged(Theme theme);
    }
}
