package com.shopapp.ui.themes;

import java.awt.Font;

/**
 * Utility class for handling fonts in the application.
 * Provides consistent font handling for Vietnamese characters and emojis.
 */
public class AppFont {

    // Font names that support Vietnamese characters well
    private static final String[] VIETNAMESE_FONT_NAMES = {
        "Arial",
        "Tahoma",
        "Segoe UI",
        "SansSerif"
    };

    // Fallback font for emojis
    private static final String[] EMOJI_FONT_NAMES = {
        "Segoe UI Emoji",
        "Apple Color Emoji",
        "Noto Color Emoji",
        "Android Emoji",
        "EmojiOne Color",
        "SansSerif"
    };

    /**
     * Gets a font that supports Vietnamese characters well.
     *
     * @param style Font style (Font.PLAIN, Font.BOLD, Font.ITALIC)
     * @param size Font size in points
     * @return Font suitable for Vietnamese text
     */
    public static Font getVietnameseFont(int style, int size) {
        Font font = null;

        // Try to find a font that supports Vietnamese
        for (String fontName : VIETNAMESE_FONT_NAMES) {
            font = new Font(fontName, style, size);
            // Check if the font can actually render Vietnamese characters
            if (canDisplayVietnamese(font)) {
                return font;
            }
        }

        // Fallback to default font if none found
        return new Font(Font.SANS_SERIF, style, size);
    }

    /**
     * Gets a font that supports emojis well.
     *
     * @param style Font style (Font.PLAIN, Font.BOLD, Font.ITALIC)
     * @param size Font size in points
     * @return Font suitable for emojis
     */
    public static Font getEmojiFont(int style, int size) {
        Font font = null;

        // Try to find a font that supports emojis
        for (String fontName : EMOJI_FONT_NAMES) {
            font = new Font(fontName, style, size);
            // Check if the font can actually render emojis
            if (canDisplayEmoji(font)) {
                return font;
            }
        }

        // Fallback to default font if none found
        return new Font(Font.SANS_SERIF, style, size);
    }

    /**
     * Gets a font that supports both Vietnamese and emojis.
     *
     * @param style Font style (Font.PLAIN, Font.BOLD, Font.ITALIC)
     * @param size Font size in points
     * @return Font suitable for both Vietnamese and emojis
     */
    public static Font getUIFont(int style, int size) {
        // Try Vietnamese font first
        Font font = getVietnameseFont(style, size);

        // If it doesn't support emojis well, try to find a combined solution
        if (!canDisplayEmoji(font)) {
            // Try emoji font
            Font emojiFont = getEmojiFont(style, size);
            if (canDisplayVietnamese(emojiFont)) {
                return emojiFont;
            }
        }

        return font;
    }

    /**
     * Checks if a font can display Vietnamese characters properly.
     *
     * @param font Font to test
     * @return true if font can display Vietnamese characters
     */
    private static boolean canDisplayVietnamese(Font font) {
        // Test characters that are problematic in some fonts
        String testChars = "ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôôơđơưă";
        return canDisplayString(font, testChars);
    }

    /**
     * Checks if a font can display emojis properly.
     *
     * @param font Font to test
     * @return true if font can display emojis
     */
    private static boolean canDisplayEmoji(Font font) {
        // Test common emojis
        String testChars = "👤🏠⚙️👥📊💾➕✏️🗑️";
        return canDisplayString(font, testChars);
    }

    /**
     * Checks if a font can display a given string.
     *
     * @param font Font to test
     * @param text String to test
     * @return true if font can display all characters in the string
     */
    private static boolean canDisplayString(Font font, String text) {
        return font.canDisplayUpTo(text) == -1;
    }

    /**
     * Gets the default UI font for the application.
     *
     * @param size Font size in points
     * @return Default UI font
     */
    public static Font getDefaultFont(float size) {
        return getUIFont(Font.PLAIN, (int) size);
    }

    /**
     * Gets the bold UI font for the application.
     *
     * @param size Font size in points
     * @return Bold UI font
     */
    public static Font getBoldFont(float size) {
        return getUIFont(Font.BOLD, (int) size);
    }

    /**
     * Gets the italic UI font for the application.
     *
     * @param size Font size in points
     * @return Italic UI font
     */
    public static Font getItalicFont(float size) {
        return getUIFont(Font.ITALIC, (int) size);
    }
}