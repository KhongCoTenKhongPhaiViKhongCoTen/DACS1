-- ============================================================
-- TEST DATA - Clothing Store Management System
-- Bỏ qua: VAITRO, QUYEN, ROLE_PERMISSIONS, NGUOI_DUNG (đã có)
-- Giả định user_id = 1 (admin), user_id = 2 (nhân viên) đã tồn tại
-- ============================================================

USE dacs1;

-- ============================================================
-- 1. DANH_MUC (Categories)
-- ============================================================
INSERT INTO DANH_MUC (category_name, description) VALUES
('Áo', 'Các loại áo: áo thun, sơ mi, khoác, hoodie...'),
('Quần', 'Quần jeans, kaki, short, jogger...'),
('Váy & Đầm', 'Váy ngắn, đầm dài, đầm maxi...'),
('Phụ kiện', 'Thắt lưng, mũ, tất, túi...'),
('Đồ thể thao', 'Áo & quần thể thao, trang phục gym...');

-- ============================================================
-- 2. NHA_CUNG_CAP (Suppliers)
-- ============================================================
INSERT INTO NHA_CUNG_CAP (company_name, contact_name, phone, email, address) VALUES
('Công ty TNHH Thời Trang Việt', 'Nguyễn Văn An', '0901234567', 'contact@thoitrangviet.vn', '12 Lê Lợi, Q1, TP.HCM'),
('Xưởng May Minh Đức', 'Trần Thị Bình', '0912345678', 'minhduc.may@gmail.com', '45 Trường Chinh, Đà Nẵng'),
('Fashion House Co., Ltd', 'Lê Minh Châu', '0923456789', 'info@fashionhouse.vn', '88 Nguyễn Huệ, Q1, TP.HCM'),
('Nhà phân phối Đông Nam', 'Phạm Quốc Dũng', '0934567890', 'dongnam.dist@yahoo.com', '23 Hai Bà Trưng, Huế');

-- ============================================================
-- 3. SAN_PHAM (Products)
-- ============================================================
INSERT INTO SAN_PHAM (sku, product_name, description, category_id, supplier_id, unit_price, cost_price, size, color, reorder_level) VALUES
-- Áo (category_id = 1)
('AO-THUN-001', 'Áo thun cổ tròn basic', 'Chất liệu cotton 100%, form regular fit', 1, 1, 150000, 80000, 'M', 'Trắng', 10),
('AO-THUN-002', 'Áo thun cổ tròn basic', 'Chất liệu cotton 100%, form regular fit', 1, 1, 150000, 80000, 'L', 'Đen', 10),
('AO-THUN-003', 'Áo thun cổ tròn basic', 'Chất liệu cotton 100%, form regular fit', 1, 1, 150000, 80000, 'XL', 'Xanh Navy', 5),
('AO-SOMI-001', 'Áo sơ mi nam dài tay', 'Chất liệu lụa pha, form slim fit', 1, 3, 320000, 180000, 'M', 'Trắng', 5),
('AO-SOMI-002', 'Áo sơ mi nam dài tay', 'Chất liệu lụa pha, form slim fit', 1, 3, 320000, 180000, 'L', 'Xanh nhạt', 5),
('AO-HOODIE-001', 'Áo hoodie unisex', 'Cotton fleece, có túi kangaroo', 1, 2, 450000, 250000, 'L', 'Xám', 8),
('AO-HOODIE-002', 'Áo hoodie unisex', 'Cotton fleece, có túi kangaroo', 1, 2, 450000, 250000, 'XL', 'Đen', 8),
-- Quần (category_id = 2)
('QU-JEAN-001', 'Quần jeans nam slim', 'Denim 98% cotton, co giãn nhẹ', 2, 1, 550000, 300000, '30', 'Xanh đậm', 5),
('QU-JEAN-002', 'Quần jeans nam slim', 'Denim 98% cotton, co giãn nhẹ', 2, 1, 550000, 300000, '32', 'Xanh nhạt', 5),
('QU-KAKI-001', 'Quần kaki nam', 'Kaki cao cấp, thoáng mát', 2, 2, 380000, 200000, '30', 'Be', 8),
('QU-KAKI-002', 'Quần kaki nam', 'Kaki cao cấp, thoáng mát', 2, 2, 380000, 200000, '32', 'Nâu', 8),
('QU-SHORT-001', 'Quần short thể thao', 'Polyester, thoát hơi nhanh', 5, 3, 220000, 110000, 'M', 'Đen', 10),
-- Váy & Đầm (category_id = 3)
('VA-MINI-001', 'Váy mini chữ A', 'Vải tweed, form A-line thanh lịch', 3, 3, 420000, 220000, 'S', 'Đỏ', 5),
('VA-MIDI-001', 'Đầm midi tay dài', 'Vải voan mềm, thiết kế nữ tính', 3, 4, 680000, 380000, 'M', 'Hồng nhạt', 5),
-- Phụ kiện (category_id = 4)
('PK-THAT-001', 'Thắt lưng da PU', 'Da PU cao cấp, khóa kim loại', 4, 4, 180000, 90000, 'Free', 'Đen', 15),
('PK-MU-001', 'Mũ bucket unisex', 'Vải canvas, có thể gấp gọn', 4, 2, 150000, 70000, 'Free', 'Kem', 20),
-- Đồ thể thao (category_id = 5)
('TT-AO-001', 'Áo thể thao dry-fit', 'Polyester, thoát mồ hôi nhanh', 5, 3, 280000, 140000, 'M', 'Xanh lá', 10),
('TT-AO-002', 'Áo thể thao dry-fit', 'Polyester, thoát mồ hôi nhanh', 5, 3, 280000, 140000, 'L', 'Đen', 10);

-- ============================================================
-- 4. TON_KHO (Inventory)
-- ============================================================
INSERT INTO TON_KHO (product_id, quantity_on_hand, location) VALUES
(1,  45, 'Kệ A1'),
(2,  38, 'Kệ A1'),
(3,  20, 'Kệ A2'),
(4,  25, 'Kệ B1'),
(5,  18, 'Kệ B1'),
(6,  30, 'Kệ B2'),
(7,  22, 'Kệ B2'),
(8,  15, 'Kệ C1'),
(9,  12, 'Kệ C1'),
(10, 28, 'Kệ C2'),
(11, 20, 'Kệ C2'),
(12, 50, 'Kệ C3'),
(13, 10, 'Kệ D1'),
(14,  8, 'Kệ D1'),
(15, 60, 'Kệ D2'),
(16, 40, 'Kệ D2'),
(17, 35, 'Kệ E1'),
(18, 30, 'Kệ E1');

-- ============================================================
-- 5. KHACH_HANG (Customers)
-- ============================================================
INSERT INTO KHACH_HANG (full_name, phone, email, address, loyalty_points) VALUES
('Nguyễn Thị Mai',    '0901111222', 'mai.nguyen@gmail.com',  '12 Trần Phú, Huế',             250),
('Lê Văn Hùng',       '0912222333', 'hung.le@yahoo.com',     '34 Lê Lợi, Huế',               100),
('Phạm Thu Hà',       '0923333444', 'ha.pham@outlook.com',   '56 Nguyễn Huệ, Đà Nẵng',       500),
('Trần Minh Tuấn',    '0934444555', 'tuan.tran@gmail.com',   '78 Hùng Vương, TP.HCM',          50),
('Hoàng Thị Lan',     '0945555666', 'lan.hoang@gmail.com',   '90 Lý Tự Trọng, Huế',           350),
('Võ Đình Khoa',      '0956666777', 'khoa.vo@gmail.com',     '21 Đinh Tiên Hoàng, Hà Nội',      0),
('Đặng Ngọc Linh',    '0967777888', 'linh.dang@gmail.com',   '43 Nguyễn Trãi, Huế',           150),
('Bùi Thanh Tùng',    '0978888999', 'tung.bui@hotmail.com',  '65 Phan Đình Phùng, Đà Nẵng',    80);

-- ============================================================
-- 6. DON_HANG (Orders)
-- Giả sử user_id 1 = admin, user_id 2 = nhân viên đã tồn tại
-- ============================================================
INSERT INTO DON_HANG (order_number, customer_id, user_id, order_date, status, subtotal, tax_amount, discount_amount, total_amount, notes) VALUES
('ORD-2024-0001', 1, 1, '2024-11-01 09:15:00', 'Hoàn thành',  450000,  40500,  0,       490500,  NULL),
('ORD-2024-0002', 2, 2, '2024-11-03 14:30:00', 'Hoàn thành',  700000,  63000, 50000,   713000,  'Khách VIP giảm 50k'),
('ORD-2024-0003', 3, 1, '2024-11-05 10:00:00', 'Hoàn thành', 1580000, 142200, 100000, 1622200,  'Thanh toán chuyển khoản'),
('ORD-2024-0004', 4, 2, '2024-11-10 16:45:00', 'Hoàn thành',  380000,  34200,  0,       414200,  NULL),
('ORD-2024-0005', 5, 1, '2024-11-15 11:20:00', 'Đang xử lý',  930000,  83700, 30000,   983700,  'Giao hàng tận nơi'),
('ORD-2024-0006', 1, 2, '2024-11-18 09:00:00', 'Hoàn thành',  600000,  54000,  0,       654000,  NULL),
('ORD-2024-0007', NULL, 1, '2024-11-20 13:00:00', 'Hoàn thành', 300000, 27000,  0,       327000,  'Khách vãng lai'),
('ORD-2024-0008', 6, 2, '2024-11-22 15:30:00', 'Đã huỷ',      550000,      0,  0,            0,  'Khách đổi ý'),
('ORD-2024-0009', 7, 1, '2024-11-25 10:10:00', 'Đang xử lý',  860000,  77400, 50000,   887400,  NULL),
('ORD-2024-0010', 8, 2, '2024-11-28 17:00:00', 'Hoàn thành',  430000,  38700,  0,       468700,  NULL);

-- ============================================================
-- 7. CHI_TIET_DON_HANG (Order Items)
-- ============================================================
INSERT INTO CHI_TIET_DON_HANG (order_id, product_id, quantity, unit_price, size, color) VALUES
-- ORD-0001: Nguyễn Thị Mai
(1, 1,  2, 150000, 'M', 'Trắng'),
(1, 15, 1, 150000, 'Free', 'Đen'),
-- ORD-0002: Lê Văn Hùng
(2, 8,  1, 550000, '30', 'Xanh đậm'),
(2, 16, 1, 150000, 'Free', 'Kem'),
-- ORD-0003: Phạm Thu Hà
(3, 14, 1, 680000, 'M', 'Hồng nhạt'),
(3, 13, 2, 420000, 'S', 'Đỏ'),
(3, 7,  1, 450000, 'XL', 'Đen'),
-- ORD-0004: Trần Minh Tuấn
(4, 10, 1, 380000, '30', 'Be'),
-- ORD-0005: Hoàng Thị Lan
(5, 4,  1, 320000, 'M', 'Trắng'),
(5, 6,  1, 450000, 'L', 'Xám'),
(5, 15, 1, 180000, 'Free', 'Đen'),
-- ORD-0006: Nguyễn Thị Mai (mua lần 2)
(6, 17, 2, 280000, 'M', 'Xanh lá'),
(6, 12, 1, 220000, 'M', 'Đen'),
-- ORD-0007: Khách vãng lai
(7, 1,  2, 150000, 'L', 'Trắng'),
-- ORD-0008: Võ Đình Khoa (đã huỷ - vẫn insert để test)
(8, 9,  1, 550000, '32', 'Xanh nhạt'),
-- ORD-0009: Đặng Ngọc Linh
(9, 5,  1, 320000, 'L', 'Xanh nhạt'),
(9, 11, 1, 380000, '32', 'Nâu'),
(9, 16, 1, 150000, 'Free', 'Kem'),
-- ORD-0010: Bùi Thanh Tùng
(10, 18, 1, 280000, 'L', 'Đen'),
(10, 15, 1, 180000, 'Free', 'Đen');

-- ============================================================
-- 8. THANH_TOAN (Payments)
-- ============================================================
INSERT INTO THANH_TOAN (order_id, payment_date, amount, payment_method, transaction_id, status) VALUES
(1,  '2024-11-01 09:20:00',  490500, 'Tiền mặt',         NULL,            'Hoàn thành'),
(2,  '2024-11-03 14:35:00',  713000, 'Chuyển khoản',     'TXN20241103001', 'Hoàn thành'),
(3,  '2024-11-05 10:10:00', 1622200, 'Chuyển khoản',     'TXN20241105002', 'Hoàn thành'),
(4,  '2024-11-10 16:50:00',  414200, 'Tiền mặt',         NULL,            'Hoàn thành'),
(5,  '2024-11-15 11:25:00',  983700, 'Ví điện tử (Momo)', 'MOMO241115001', 'Đang chờ'),
(6,  '2024-11-18 09:05:00',  654000, 'Tiền mặt',         NULL,            'Hoàn thành'),
(7,  '2024-11-20 13:05:00',  327000, 'Tiền mặt',         NULL,            'Hoàn thành'),
(9,  '2024-11-25 10:15:00',  887400, 'Chuyển khoản',     'TXN20241125003', 'Đang chờ'),
(10, '2024-11-28 17:05:00',  468700, 'Ví điện tử (ZaloPay)', 'ZALO241128001', 'Hoàn thành');
-- Order 8 (đã huỷ) không có thanh toán

-- ============================================================
-- Verify nhanh
-- ============================================================
-- SELECT 'DANH_MUC'    AS tbl, COUNT(*) AS rows FROM DANH_MUC
-- UNION ALL SELECT 'NHA_CUNG_CAP', COUNT(*) FROM NHA_CUNG_CAP
-- UNION ALL SELECT 'SAN_PHAM',     COUNT(*) FROM SAN_PHAM
-- UNION ALL SELECT 'TON_KHO',      COUNT(*) FROM TON_KHO
-- UNION ALL SELECT 'KHACH_HANG',   COUNT(*) FROM KHACH_HANG
-- UNION ALL SELECT 'DON_HANG',     COUNT(*) FROM DON_HANG
-- UNION ALL SELECT 'CHI_TIET',     COUNT(*) FROM CHI_TIET_DON_HANG
-- UNION ALL SELECT 'THANH_TOAN',   COUNT(*) FROM THANH_TOAN;