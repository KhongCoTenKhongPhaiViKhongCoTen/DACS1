package com.shopapp.ui.themes;

import java.awt.Font;

/**
 * Utility class for handling fonts in the application.
 * Provides consistent font handling for Vietnamese characters and emojis.
 */
public class AppFont {

    // Singleton Instance
    private static final AppFont instance = new AppFont();

    private AppFont() {
    } // Chặn khởi tạo ngoài

    public static AppFont getInstance() {
        return instance;
    }

    private final String[] VIETNAMESE_FONT_NAMES = {
            "Arial", "Tahoma", "Segoe UI", "SansSerif"
    };

    private final String[] EMOJI_FONT_NAMES = {
            "Segoe UI Emoji", "Apple Color Emoji", "Noto Color Emoji",
            "Android Emoji", "EmojiOne Color", "SansSerif"
    };

    public Font getVietnameseFont(int style, int size) {
        Font font = null;
        for (String fontName : VIETNAMESE_FONT_NAMES) {
            font = new Font(fontName, style, size);
            if (canDisplayVietnamese(font)) {
                return font;
            }
        }
        return new Font(Font.SANS_SERIF, style, size);
    }

    public Font getEmojiFont(int style, int size) {
        Font font = null;
        for (String fontName : EMOJI_FONT_NAMES) {
            font = new Font(fontName, style, size);
            if (canDisplayEmoji(font)) {
                return font;
            }
        }
        return new Font(Font.SANS_SERIF, style, size);
    }

    public Font getUIFont(int style, int size) {
        Font font = getVietnameseFont(style, size);
        if (!canDisplayEmoji(font)) {
            Font emojiFont = getEmojiFont(style, size);
            if (canDisplayVietnamese(emojiFont)) {
                return emojiFont;
            }
        }
        return font;
    }

    // Các hàm bổ sung để bạn gọi cho ngắn gọn: .plain(), .bold(), .italic()
    public Font plain(float size) {
        return ThemeManager.getInstance().getFont(size);
    }

    public Font bold(float size) {
        return ThemeManager.getInstance().getBoldFont(size);
    }

    public Font italic(float size) {
        return ThemeManager.getInstance().getItalicFont(size);
    }

    private boolean canDisplayVietnamese(Font font) {
        String testChars = "ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôôơđơưă";
        return font.canDisplayUpTo(testChars) == -1;
    }

    private boolean canDisplayEmoji(Font font) {
        String testChars = "👤🏠⚙️👥📊💾➕✏️🗑️";
        return font.canDisplayUpTo(testChars) == -1;
    }
}