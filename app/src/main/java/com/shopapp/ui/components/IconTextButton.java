package com.shopapp.ui.components;

import javax.swing.*;
import java.awt.*;
import com.shopapp.ui.themes.*;

public class IconTextButton extends JButton {
    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    private JLabel textLabel; // ← Lưu reference
    private JLabel iconLabel;

    private final ThemeManager.ThemeChangeListener themeChangeListener = new ThemeManager.ThemeChangeListener() {
        @Override
        public void onThemeChanged(Theme theme) {
            updateColorsAndFont(theme);
        }
    };

    public IconTextButton(String icon, String text, Orientation orientation) {
        super();
        setFocusPainted(false);
        setContentAreaFilled(true);
        setMargin(new Insets(0, 0, 0, 0));

        // icon - set size cụ thể
        iconLabel = new JLabel(icon);
        iconLabel.setFont(AppFont.getEmojiFont(Font.PLAIN, 16));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(30, 30));

        // text
        textLabel = new JLabel(text);
        textLabel.setFont(ThemeManager.getFont(14));
        textLabel.setHorizontalAlignment(SwingConstants.LEFT);
        textLabel.setVerticalAlignment(SwingConstants.CENTER);

        if (orientation == Orientation.HORIZONTAL) {
            // Icon | Text (ngang)
            setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
            add(iconLabel);
            add(textLabel);
        } else {
            // Icon (trên)
            // Text (dưới)
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(false);

            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(iconLabel);
            panel.add(textLabel);
            add(panel);
        }

        updateColorsAndFont(ThemeManager.getCurrentTheme());
        ThemeManager.addThemeChangeListener(themeChangeListener);
    }

    public IconTextButton(String icon, String text) {
        this(icon, text, Orientation.HORIZONTAL);
    }

    // ✅ Thêm method này để update text
    public void setButtonText(String newText) {
        if (textLabel != null) {
            textLabel.setText(newText);
        }
    }

    // ✅ Thêm method này để update icon
    public void setButtonIcon(String newIcon) {
        if (iconLabel != null) {
            iconLabel.setText(newIcon);
        }
    }

    private void updateColorsAndFont(Theme theme) {
        setBackground(theme.buttonBackground);
        setForeground(theme.buttonForeground);

        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JLabel label) {
                label.setForeground(theme.textPrimary);
            } else if (component instanceof JPanel panel) {
                for (Component comp : panel.getComponents()) {
                    if (comp instanceof JLabel label) {
                        label.setForeground(theme.textPrimary);
                    }
                }
            }
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        ThemeManager.removeThemeChangeListener(themeChangeListener);
    }
}