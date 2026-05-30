package com.shopapp.ui.components;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.shopapp.ui.themes.Theme;
import com.shopapp.ui.themes.ThemeManager;

/**
 * Abstract class chung cho các dialog Thêm mới và Chỉnh sửa trong giao diện
 * người dùng.
 * Các class con chỉ cần implement các phương thức abstract và override
 *
 * Các chức năng mặc định:
 * - Header với tiêu đề
 * - Panel nút Lưu và Hủy
 * - Áp dụng theme đồng bộ
 * - Kiểm tra dữ liệu đầu vào cơ bản (có thể override)
 *
 * ⚠️ QUAN TRỌNG - Thứ tự khởi tạo:
 * Vì Java gọi constructor của superclass trước khi subclass assign fields,
 * createForm() KHÔNG được gọi trong super(). Thay vào đó, subclass PHẢI
 * gọi buildAndShow() ở cuối constructor của mình, sau khi đã assign hết fields.
 *
 *
 * Các Override methods bắt buộc:
 * 
 * @see #createForm() Tạo panel chứa các trường dữ liệu
 * @see #fillData() Điền dữ liệu khi ở chế độ Chỉnh sửa
 * @see #handleSave() Xử lý sự kiện nút Lưu
 *
 *      Các Override methods tuỳ chọn:
 * @see #addCustomButtons() Thêm các nút tùy chỉnh vào buttonPanel
 * @see #applyCustomValidation() Thực hiện kiểm tra xác thực tùy chỉnh
 */
public abstract class BaseDialog extends JDialog {

    // ── Hằng số font/size dùng chung ──────────────────────────────────────────
    protected static final int FIELD_COLUMNS = 25;
    protected static final int FONT_FIELD = 18;
    protected static final int FONT_LABEL = 18;
    protected static final int FONT_TITLE = 22;
    protected static final int FONT_BUTTON = 14;
    protected static final int PADDING = 10;

    // ── Form fields & components ─────────────────────────────────────────────
    protected JButton btnSave;
    protected JButton btnCancel;
    protected JLabel titleLabel;
    protected JPanel headerPanel;
    protected JPanel buttonPanel;

    protected boolean succeeded = false;

    /**
     * Constructor - Chỉ thiết lập layout skeleton, KHÔNG gọi createForm().
     * Subclass phải gọi buildAndShow() ở cuối constructor của mình.
     *
     * @param owner Cửa sổ cha
     * @param title Tiêu đề dialog
     */
    public BaseDialog(Frame owner, String title) {
        super(owner, title, true);
        initializeUIBase();
    }

    // ── Khởi tạo layout skeleton (không gọi createForm) ───────────────────────

    private void initializeUIBase() {
        setLayout(new BorderLayout());

        // Header
        headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titleLabel = new JLabel(getTitle());
        titleLabel.setFont(ThemeManager.getBoldFont(FONT_TITLE));
        headerPanel.add(titleLabel);

        // Buttons
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Xử lý sự kiện
        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(this::handleSaveAction);
    }

    /**
     * Hoàn tất việc xây dựng UI và hiển thị dialog.
     * Subclass PHẢI gọi phương thức này ở cuối constructor của mình,
     * SAU KHI đã assign hết tất cả các fields cần thiết.
     */
    protected final void buildAndShow() {
        // Form (implemented by subclass)
        JPanel formPanel = createForm();
        if (formPanel != null) {
            formPanel.setBorder(new EmptyBorder(PADDING, PADDING + 5, PADDING, PADDING + 5));
            add(formPanel, BorderLayout.CENTER);
        }

        // Cho phép class con thêm nút tùy chỉnh vào buttonPanel
        addCustomButtons();

        // Apply theme, điền dữ liệu, pack
        applyCurrentTheme();
        fillData();
        pack();
        setLocationRelativeTo(getOwner());
    }

    // ── Xử lý sự kiện nút Lưu ────────────────────────────────────────────────

    private void handleSaveAction(ActionEvent e) {
        if (applyCustomValidation()) {
            handleSave();
        }
    }

    // ── Factory methods cho form fields ──────────────────────────────────────

    /** Tạo JTextField với thông số chung */
    protected JTextField createTextField() {
        JTextField tf = new JTextField(FIELD_COLUMNS);
        tf.setFont(ThemeManager.getFont(FONT_FIELD));
        return tf;
    }

    /** Tạo JPasswordField với thông số chung */
    protected JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField(FIELD_COLUMNS);
        pf.setFont(ThemeManager.getFont(FONT_FIELD));
        return pf;
    }

    /** Tạo JTextArea với thông số chung */
    protected JTextArea createTextArea(int rows, int columns) {
        JTextArea ta = new JTextArea(rows, columns);
        ta.setFont(ThemeManager.getFont(FONT_FIELD));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        return ta;
    }

    // ── Abstract methods - Subclass phải implement ────────────────────────────

    /**
     * Tạo panel chứa các trường dữ liệu form.
     * Lúc này tất cả fields của subclass đã được assign.
     *
     * @return JPanel chứa các trường dữ liệu form
     */
    protected abstract JPanel createForm();

    /**
     * Điền dữ liệu vào form khi dialog ở chế độ Chỉnh sửa.
     * Lúc này cbRole và các component đã được khởi tạo trong createForm().
     */
    protected abstract void fillData();

    /**
     * Xử lý lưu dữ liệu.
     */
    protected abstract void handleSave();

    // ── Optional methods - Subclass có thể override ───────────────────────────

    /**
     * Thực hiện kiểm tra xác thực tùy chỉnh trước khi lưu.
     * Mặc định trả về true (không có kiểm tra cụ thể nào).
     *
     * @return true nếu dữ liệu hợp lệ, false nếu không
     */
    protected boolean applyCustomValidation() {
        return true;
    }

    /**
     * Thêm các nút tùy chỉnh vào buttonPanel.
     * Override phương thức này để thêm nút của riêng bạn.
     * Dùng buttonPanel.add(...) để thêm nút.
     */
    protected void addCustomButtons() {
        // Mặc định không làm gì
    }

    // ── Theme ─────────────────────────────────────────────────────────────────

    public boolean isSucceeded() {
        return succeeded;
    }

    protected void applyCurrentTheme() {
        Theme theme = ThemeManager.getCurrentTheme();
        styleComponents(this, theme);
        headerPanel.setBackground(theme.background);
        buttonPanel.setBackground(theme.background);
        titleLabel.setForeground(theme.textPrimary);
    }

    private void styleComponents(Container container, Theme theme) {
        container.setBackground(theme.background);
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel label) {
                label.setForeground(theme.textPrimary);
                label.setFont(ThemeManager.getFont(FONT_LABEL));
            } else if (comp instanceof JPasswordField pf) {
                // Check JPasswordField TRƯỚC JTextField vì nó là subclass
                pf.setBackground(theme.buttonBackground);
                pf.setForeground(theme.textPrimary);
                pf.setCaretColor(theme.textPrimary);
                pf.setFont(ThemeManager.getFont(FONT_FIELD));
            } else if (comp instanceof JTextField tf) {
                tf.setBackground(theme.buttonBackground);
                tf.setForeground(theme.textPrimary);
                tf.setCaretColor(theme.textPrimary);
                tf.setFont(ThemeManager.getFont(FONT_FIELD));
            } else if (comp instanceof JTextArea ta) {
                ta.setBackground(theme.buttonBackground);
                ta.setForeground(theme.textPrimary);
                ta.setCaretColor(theme.textPrimary);
                ta.setFont(ThemeManager.getFont(FONT_FIELD));
            } else if (comp instanceof JComboBox<?> cb) {
                cb.setBackground(theme.buttonBackground);
                cb.setForeground(theme.textPrimary);
                cb.setFont(ThemeManager.getFont(FONT_FIELD));
            } else if (comp instanceof JCheckBox chk) {
                chk.setOpaque(false);
                chk.setForeground(theme.textPrimary);
                chk.setFont(ThemeManager.getFont(FONT_FIELD));
            } else if (comp instanceof JButton btn) {
                btn.setBackground(theme.buttonBackground);
                btn.setForeground(theme.buttonForeground);
                btn.setFont(ThemeManager.getBoldFont(FONT_BUTTON));
                btn.setFocusPainted(false);
            } else if (comp instanceof Container childContainer) {
                styleComponents(childContainer, theme);
            }
        }
    }
}