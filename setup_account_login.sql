-- ============================================
-- SETUP ROLES, PERMISSIONS, AND USER ACCOUNTS
-- (Version cuối - CHẮC CHẮN CHẠY ĐƯỢC)
-- ============================================

-- 0. TẮT Foreign Key Check (giữ tắt suốt quá trình)
SET FOREIGN_KEY_CHECKS = 0;
SET SQL_SAFE_UPDATES = 0;

-- 1. XÓA TOÀN BỘ DỮ LIỆU
DELETE FROM NGUOI_DUNG;
DELETE FROM ROLE_PERMISSIONS;
DELETE FROM QUYEN;
DELETE FROM VAITRO;

-- 2. Thêm vai trò
INSERT INTO VAITRO (role_name, description) VALUES
('Admin', 'Quản trị hệ thống - toàn quyền'),
('Quản lý', 'Quản lý cửa hàng và bán hàng'),
('Nhân viên', 'Nhân viên bán hàng và xử lý đơn hàng');

-- 3. Thêm quyền chi tiết
INSERT INTO QUYEN (permission_code, permission_name, module) VALUES
-- Quyền sản phẩm
('PRODUCT_READ', 'Xem sản phẩm', 'PRODUCT'),
('PRODUCT_CREATE', 'Thêm sản phẩm', 'PRODUCT'),
('PRODUCT_UPDATE', 'Cập nhật sản phẩm', 'PRODUCT'),
('PRODUCT_DELETE', 'Xóa sản phẩm', 'PRODUCT'),

-- Quyền đơn hàng
('ORDER_READ', 'Xem đơn hàng', 'ORDER'),
('ORDER_CREATE', 'Tạo đơn hàng', 'ORDER'),
('ORDER_UPDATE', 'Cập nhật đơn hàng', 'ORDER'),
('ORDER_DELETE', 'Xóa đơn hàng', 'ORDER'),

-- Quyền khách hàng
('CUSTOMER_READ', 'Xem khách hàng', 'CUSTOMER'),
('CUSTOMER_CREATE', 'Thêm khách hàng', 'CUSTOMER'),
('CUSTOMER_UPDATE', 'Cập nhật khách hàng', 'CUSTOMER'),
('CUSTOMER_DELETE', 'Xóa khách hàng', 'CUSTOMER'),

-- Quyền người dùng
('USER_READ', 'Xem người dùng', 'USER'),
('USER_CREATE', 'Thêm người dùng', 'USER'),
('USER_UPDATE', 'Cập nhật người dùng', 'USER'),
('USER_DELETE', 'Xóa người dùng', 'USER'),

-- Quyền báo cáo
('REPORT_READ', 'Xem báo cáo', 'REPORT'),
('INVENTORY_READ', 'Xem tồn kho', 'INVENTORY'),
('INVENTORY_UPDATE', 'Cập nhật tồn kho', 'INVENTORY');

-- 4. Gán TẤT CẢ quyền cho role Admin (role_id = 1)
INSERT INTO ROLE_PERMISSIONS (role_id, permission_id, granted_at)
SELECT 1, permission_id, NOW()
FROM QUYEN;

-- 5. Gán quyền cho role Quản lý (role_id = 2) - không DELETE
INSERT INTO ROLE_PERMISSIONS (role_id, permission_id, granted_at)
SELECT 2, permission_id, NOW()
FROM QUYEN
WHERE permission_code NOT LIKE '%DELETE%';

-- 6. Gán quyền cơ bản cho role Nhân viên (role_id = 3)
INSERT INTO ROLE_PERMISSIONS (role_id, permission_id, granted_at)
SELECT 3, permission_id, NOW()
FROM QUYEN
WHERE permission_code IN (
    'PRODUCT_READ', 'ORDER_READ', 'ORDER_CREATE',
    'CUSTOMER_READ', 'CUSTOMER_CREATE', 'REPORT_READ'
);

-- 7. Thêm tài khoản Admin
INSERT INTO NGUOI_DUNG (username, password_hash, full_name, email, phone, created_at, is_active, role_id)
VALUES (
    'admin',
    '$2a$10$BwPI2xtYaZR6BetmlYNiAevcB4c6jQDwzSznS3UZmUjzpp9KsBQ4K',
    'Người quản trị hệ thống',
    'admin@clothingstore.vn',
    '0123456789',
    NOW(),
    TRUE,
    1
);

-- 8. Thêm tài khoản Quản lý
INSERT INTO NGUOI_DUNG (username, password_hash, full_name, email, phone, created_at, is_active, role_id)
VALUES (
    'manager',
    '$2a$10$BwPI2xtYaZR6BetmlYNiAevcB4c6jQDwzSznS3UZmUjzpp9KsBQ4K',
    'Quản lý cửa hàng',
    'manager@clothingstore.vn',
    '0123456788',
    NOW(),
    TRUE,
    2
);

-- 9. Thêm tài khoản Nhân viên
INSERT INTO NGUOI_DUNG (username, password_hash, full_name, email, phone, created_at, is_active, role_id)
VALUES (
    'staff',
    '$2a$10$BwPI2xtYaZR6BetmlYNiAevcB4c6jQDwzSznS3UZmUjzpp9KsBQ4K',
    'Nhân viên bán hàng',
    'staff@clothingstore.vn',
    '0123456787',
    NOW(),
    TRUE,
    3
);

-- ⭐ BẬT lại Foreign Key Check (GIỜ MỚI BẬT!)
SET FOREIGN_KEY_CHECKS = 1;
SET SQL_SAFE_UPDATES = 1;

-- ============================================
-- ✅ KIỂM TRA KẾT QUẢ CUỐI CÙNG
-- ============================================

SELECT '========== DANH SÁCH TÀI KHOẢN ==========' as '';
SELECT 
    user_id,
    username,
    '123456' as 'Mật khẩu',
    full_name,
    (SELECT role_name FROM VAITRO WHERE role_id = NGUOI_DUNG.role_id) as 'Vai trò'
FROM NGUOI_DUNG 
ORDER BY user_id;

SELECT '' as '';
SELECT '========== PHÂN QUYỀN THEO VAI TRÒ ==========' as '';
SELECT 
    v.role_id,
    v.role_name,
    COUNT(rp.permission_id) as 'Số quyền'
FROM VAITRO v
LEFT JOIN ROLE_PERMISSIONS rp ON v.role_id = rp.role_id
GROUP BY v.role_id, v.role_name
ORDER BY v.role_id;

SELECT '' as '';
SELECT '========== QUYỀN CHI TIẾT CỦA ADMIN ==========' as '';
SELECT 
    q.module,
    q.permission_code,
    q.permission_name
FROM ROLE_PERMISSIONS rp
JOIN QUYEN q ON rp.permission_id = q.permission_id
WHERE rp.role_id = 1
ORDER BY q.module, q.permission_code;

-- ============================================
-- 🔑 THÔNG TIN ĐĂNG NHẬP
-- ============================================
--
-- ADMIN:
--   Username: admin
--   Mật khẩu: 123456
--   Vai trò: Admin (19 quyền)
--
-- QUẢN LÝ:
--   Username: manager
--   Mật khẩu: 123456
--   Vai trò: Quản lý (15 quyền)
--
-- NHÂN VIÊN:
--   Username: staff
--   Mật khẩu: 123456
--   Vai trò: Nhân viên (6 quyền)
--
-- ============================================