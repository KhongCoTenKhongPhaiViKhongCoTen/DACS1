package com.shopapp.ui.themes;

import java.awt.Color;
import java.awt.Font;

public enum Theme {
    DARK(
        new Color(30, 30, 30),      // background
        new Color(220, 220, 220),   // foreground
        new Color(42, 157, 143),    // accent
        new Color(60, 63, 65),      // buttonBackground
        new Color(255, 255, 255),   // buttonForeground
        new Color(255, 255, 255),   // textPrimary
        new Color(180, 180, 180),   // textSecondary
        new Color(80, 80, 80)       // borderColor
    ),
    LIGHT(
        new Color(255, 255, 255),   // background
        new Color(33, 33, 33),      // foreground
        new Color(33, 150, 243),    // accent
        new Color(245, 245, 245),   // buttonBackground
        new Color(33, 33, 33),      // buttonForeground
        new Color(22, 22, 22),      // textPrimary
        new Color(117, 117, 117),   // textSecondary
        new Color(200, 200, 200)    // borderColor
    );

    public final Color background;
    public final Color foreground;
    public final Color accent;
    public final Color buttonBackground;
    public final Color buttonForeground;
    public final Color textPrimary;
    public final Color textSecondary;
    public final Color borderColor;

    Theme(Color background, Color foreground, Color accent,
          Color buttonBackground, Color buttonForeground,
          Color textPrimary, Color textSecondary, Color borderColor) {
        this.background = background;
        this.foreground = foreground;
        this.accent = accent;
        this.buttonBackground = buttonBackground;
        this.buttonForeground = buttonForeground;
        this.textPrimary = textPrimary;
        this.textSecondary = textSecondary;
        this.borderColor = borderColor;
    }

    /**
     * Gets the default UI font for this theme.
     * @param size Font size in points
     * @return Font suitable for UI elements
     */
    public Font getFont(float size) {
        return AppFont.getUIFont(Font.PLAIN, (int) size);
    }

    /**
     * Gets the bold UI font for this theme.
     * @param size Font size in points
     * @return Bold font suitable for UI elements
     */
    public Font getBoldFont(float size) {
        return AppFont.getUIFont(Font.BOLD, (int) size);
    }

    /**
     * Gets the italic UI font for this theme.
     * @param size Font size in points
     * @return Italic font suitable for UI elements
     */
    public Font getItalicFont(float size) {
        return AppFont.getUIFont(Font.ITALIC, (int) size);
    }
}
