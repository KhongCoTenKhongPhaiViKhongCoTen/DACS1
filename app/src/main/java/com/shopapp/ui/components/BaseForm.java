package com.shopapp.ui.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;

import com.shopapp.AppSys;
import com.shopapp.ui.themes.Theme;
import com.shopapp.ui.themes.ThemeManager;

/**
 * A flexible form panel that uses GridBagLayout for organized component
 * arrangement.
 * Supports theming and provides clean spacing for form elements.
 */
public class BaseForm extends javax.swing.JPanel implements ThemeManager.ThemeChangeListener {

    private final GridBagConstraints gbc = new GridBagConstraints();
    private int row = 0;
    private static final Insets DEFAULT_INSETS = new Insets(8, 8, 8, 8);

    public BaseForm() {
        initializeForm();
    }

    private void initializeForm() {
        setLayout(new GridBagLayout());
        // Apply initial theme
        applyTheme();
    }

    /**
     * Thêm một thành phần được căn giữa trên cả hai cột của biểu mẫu.
     *
     * @param component Thành phần cần thêm
     */
    public void addRowCenter(JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = getDefaultInsets();

        add(component, gbc);

        resetGridConstraints();
    }

    /**
     * Adds a label-field pair to the form in a standard two-column layout.
     *
     * @param label The label component
     * @param field The field component (text field, combo box, etc.)
     */
    public void addRow(JComponent label, JComponent field) {
        // Add label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = getDefaultInsets();

        add(label, gbc);

        // Add field
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = getDefaultInsets();

        add(field, gbc);

        row++;
        resetGridConstraints();
    }

    @Override
    public void onThemeChanged(Theme theme) {
        applyTheme();
    }

    public void setTheme(Theme theme) {
        AppSys.themes.set(theme);
        applyTheme();
    }

    private void applyTheme() {
        Theme theme = AppSys.themes.getCurrent();
        setBackground(theme.background);
    }

    private Insets getDefaultInsets() {
        return new Insets(DEFAULT_INSETS.top, DEFAULT_INSETS.left,
                DEFAULT_INSETS.bottom, DEFAULT_INSETS.right);
    }

    private void resetGridConstraints() {
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
    }
}