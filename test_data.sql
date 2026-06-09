-- ============================================================
-- TEST DATA - Clothing Store Management System
-- Dữ liệu trải đều từ 2024-10 đến 2026-06 (thời gian thực)
-- ============================================================

USE dacs1;

-- ============================================================
-- 1. DANH_MUC
-- ============================================================
INSERT INTO DANH_MUC (category_name, description) VALUES
('Áo', 'Các loại áo: áo thun, sơ mi, khoác, hoodie...'),
('Quần', 'Quần jeans, kaki, short, jogger...'),
('Váy & Đầm', 'Váy ngắn, đầm dài, đầm maxi...'),
('Phụ kiện', 'Thắt lưng, mũ, tất, túi...'),
('Đồ thể thao', 'Áo & quần thể thao, trang phục gym...');

-- ============================================================
-- 2. NHA_CUNG_CAP
-- ============================================================
INSERT INTO NHA_CUNG_CAP (company_name, contact_name, phone, email, address) VALUES
('Công ty TNHH Thời Trang Việt', 'Nguyễn Văn An', '0901234567', 'contact@thoitrangviet.vn', '12 Lê Lợi, Q1, TP.HCM'),
('Xưởng May Minh Đức',          'Trần Thị Bình', '0912345678', 'minhduc.may@gmail.com',    '45 Trường Chinh, Đà Nẵng'),
('Fashion House Co., Ltd',      'Lê Minh Châu',  '0923456789', 'info@fashionhouse.vn',     '88 Nguyễn Huệ, Q1, TP.HCM'),
('Nhà phân phối Đông Nam',      'Phạm Quốc Dũng','0934567890', 'dongnam.dist@yahoo.com',   '23 Hai Bà Trưng, Huế');

-- ============================================================
-- 3. SAN_PHAM
-- ============================================================
INSERT INTO SAN_PHAM (sku, product_name, description, category_id, supplier_id, unit_price, cost_price, size, color, reorder_level) VALUES
('AO-THUN-001',  'Áo thun cổ tròn basic',    'Cotton 100%, form regular fit',          1, 1, 150000,  80000, 'M',    'Trắng',     10),
('AO-THUN-002',  'Áo thun cổ tròn basic',    'Cotton 100%, form regular fit',          1, 1, 150000,  80000, 'L',    'Đen',       10),
('AO-THUN-003',  'Áo thun cổ tròn basic',    'Cotton 100%, form regular fit',          1, 1, 150000,  80000, 'XL',   'Xanh Navy',  5),
('AO-SOMI-001',  'Áo sơ mi nam dài tay',     'Lụa pha, form slim fit',                1, 3, 320000, 180000, 'M',    'Trắng',      5),
('AO-SOMI-002',  'Áo sơ mi nam dài tay',     'Lụa pha, form slim fit',                1, 3, 320000, 180000, 'L',    'Xanh nhạt',  5),
('AO-HOODIE-001','Áo hoodie unisex',          'Cotton fleece, túi kangaroo',           1, 2, 450000, 250000, 'L',    'Xám',        8),
('AO-HOODIE-002','Áo hoodie unisex',          'Cotton fleece, túi kangaroo',           1, 2, 450000, 250000, 'XL',   'Đen',        8),
('QU-JEAN-001',  'Quần jeans nam slim',       'Denim 98% cotton, co giãn nhẹ',        2, 1, 550000, 300000, '30',   'Xanh đậm',   5),
('QU-JEAN-002',  'Quần jeans nam slim',       'Denim 98% cotton, co giãn nhẹ',        2, 1, 550000, 300000, '32',   'Xanh nhạt',  5),
('QU-KAKI-001',  'Quần kaki nam',             'Kaki cao cấp, thoáng mát',             2, 2, 380000, 200000, '30',   'Be',          8),
('QU-KAKI-002',  'Quần kaki nam',             'Kaki cao cấp, thoáng mát',             2, 2, 380000, 200000, '32',   'Nâu',         8),
('QU-SHORT-001', 'Quần short thể thao',       'Polyester, thoát hơi nhanh',           5, 3, 220000, 110000, 'M',    'Đen',        10),
('VA-MINI-001',  'Váy mini chữ A',            'Vải tweed, form A-line thanh lịch',    3, 3, 420000, 220000, 'S',    'Đỏ',          5),
('VA-MIDI-001',  'Đầm midi tay dài',          'Vải voan mềm, thiết kế nữ tính',       3, 4, 680000, 380000, 'M',    'Hồng nhạt',   5),
('PK-THAT-001',  'Thắt lưng da PU',           'Da PU cao cấp, khóa kim loại',         4, 4, 180000,  90000, 'Free', 'Đen',        15),
('PK-MU-001',    'Mũ bucket unisex',          'Vải canvas, có thể gấp gọn',           4, 2, 150000,  70000, 'Free', 'Kem',        20),
('TT-AO-001',    'Áo thể thao dry-fit',       'Polyester, thoát mồ hôi nhanh',        5, 3, 280000, 140000, 'M',    'Xanh lá',    10),
('TT-AO-002',    'Áo thể thao dry-fit',       'Polyester, thoát mồ hôi nhanh',        5, 3, 280000, 140000, 'L',    'Đen',        10);

-- ============================================================
-- 4. TON_KHO
-- ============================================================
INSERT INTO TON_KHO (product_id, quantity_on_hand, location) VALUES
(1,  45,'Kệ A1'),(2,  38,'Kệ A1'),(3,  20,'Kệ A2'),
(4,  25,'Kệ B1'),(5,  18,'Kệ B1'),(6,  30,'Kệ B2'),
(7,  22,'Kệ B2'),(8,  15,'Kệ C1'),(9,  12,'Kệ C1'),
(10, 28,'Kệ C2'),(11, 20,'Kệ C2'),(12, 50,'Kệ C3'),
(13, 10,'Kệ D1'),(14,  8,'Kệ D1'),(15, 60,'Kệ D2'),
(16, 40,'Kệ D2'),(17, 35,'Kệ E1'),(18, 30,'Kệ E1');

-- ============================================================
-- 5. KHACH_HANG
-- ============================================================
INSERT INTO KHACH_HANG (full_name, phone, email, address, loyalty_points) VALUES
('Nguyễn Thị Mai', '0901111222', 'mai.nguyen@gmail.com',  '12 Trần Phú, Huế',           250),
('Lê Văn Hùng',    '0912222333', 'hung.le@yahoo.com',     '34 Lê Lợi, Huế',             100),
('Phạm Thu Hà',    '0923333444', 'ha.pham@outlook.com',   '56 Nguyễn Huệ, Đà Nẵng',     500),
('Trần Minh Tuấn', '0934444555', 'tuan.tran@gmail.com',   '78 Hùng Vương, TP.HCM',        50),
('Hoàng Thị Lan',  '0945555666', 'lan.hoang@gmail.com',   '90 Lý Tự Trọng, Huế',         350),
('Võ Đình Khoa',   '0956666777', 'khoa.vo@gmail.com',     '21 Đinh Tiên Hoàng, Hà Nội',    0),
('Đặng Ngọc Linh', '0967777888', 'linh.dang@gmail.com',   '43 Nguyễn Trãi, Huế',         150),
('Bùi Thanh Tùng', '0978888999', 'tung.bui@hotmail.com',  '65 Phan Đình Phùng, Đà Nẵng',  80);

-- ============================================================
-- 6. DON_HANG  (trải từ 2024-10 → 2026-06)
-- ============================================================
INSERT INTO DON_HANG (order_number, customer_id, user_id, order_date, status, subtotal, tax_amount, discount_amount, total_amount, notes) VALUES
-- 2024 Q4
('ORD-2024-0001', 1, 1, '2024-10-05 09:15:00', 'Hoàn thành',  450000,  40500,      0,  490500, NULL),
('ORD-2024-0002', 2, 2, '2024-10-18 14:30:00', 'Hoàn thành',  700000,  63000,  50000,  713000, 'Khách VIP giảm 50k'),
('ORD-2024-0003', 3, 1, '2024-11-05 10:00:00', 'Hoàn thành', 1580000, 142200, 100000, 1622200, 'Thanh toán chuyển khoản'),
('ORD-2024-0004', 4, 2, '2024-11-10 16:45:00', 'Hoàn thành',  380000,  34200,      0,  414200, NULL),
('ORD-2024-0005', 5, 1, '2024-11-20 11:20:00', 'Hoàn thành',  930000,  83700,  30000,  983700, 'Giao hàng tận nơi'),
('ORD-2024-0006', 1, 2, '2024-12-03 09:00:00', 'Hoàn thành',  600000,  54000,      0,  654000, NULL),
('ORD-2024-0007', NULL,1,'2024-12-15 13:00:00', 'Hoàn thành',  300000,  27000,      0,  327000, 'Khách vãng lai'),
('ORD-2024-0008', 6, 2, '2024-12-20 15:30:00', 'Đã huỷ',      550000,      0,      0,       0, 'Khách đổi ý'),
-- 2025 Q1
('ORD-2025-0001', 7, 1, '2025-01-08 10:10:00', 'Hoàn thành',  860000,  77400,  50000,  887400, NULL),
('ORD-2025-0002', 8, 2, '2025-01-22 17:00:00', 'Hoàn thành',  430000,  38700,      0,  468700, NULL),
('ORD-2025-0003', 3, 1, '2025-02-10 09:30:00', 'Hoàn thành',  760000,  68400,      0,  828400, NULL),
('ORD-2025-0004', 5, 2, '2025-02-28 14:00:00', 'Hoàn thành',  450000,  40500,  20000,  470500, 'Giảm cuối tháng'),
('ORD-2025-0005', 2, 1, '2025-03-12 11:00:00', 'Hoàn thành',  880000,  79200,      0,  959200, NULL),
('ORD-2025-0006', 4, 2, '2025-03-25 16:30:00', 'Đã huỷ',      320000,      0,      0,       0, 'Hết hàng'),
-- 2025 Q2
('ORD-2025-0007', 1, 1, '2025-04-07 10:00:00', 'Hoàn thành',  670000,  60300,      0,  730300, NULL),
('ORD-2025-0008', 6, 2, '2025-04-19 15:00:00', 'Hoàn thành',  540000,  48600,  30000,  558600, NULL),
('ORD-2025-0009', 7, 1, '2025-05-03 09:15:00', 'Hoàn thành',  920000,  82800,      0, 1002800, NULL),
('ORD-2025-0010', 8, 2, '2025-05-17 13:45:00', 'Hoàn thành',  380000,  34200,      0,  414200, NULL),
('ORD-2025-0011', 3, 1, '2025-06-02 10:30:00', 'Hoàn thành', 1100000,  99000,  50000, 1149000, 'Khách quen'),
('ORD-2025-0012', NULL,2,'2025-06-20 14:00:00', 'Hoàn thành',  290000,  26100,      0,  316100, 'Khách vãng lai'),
-- 2025 Q3
('ORD-2025-0013', 5, 1, '2025-07-05 09:00:00', 'Hoàn thành',  750000,  67500,      0,  817500, NULL),
('ORD-2025-0014', 1, 2, '2025-07-18 16:00:00', 'Hoàn thành',  460000,  41400,      0,  501400, NULL),
('ORD-2025-0015', 2, 1, '2025-08-04 11:00:00', 'Hoàn thành',  630000,  56700,  30000,  656700, NULL),
('ORD-2025-0016', 4, 2, '2025-08-22 14:30:00', 'Hoàn thành',  840000,  75600,      0,  915600, NULL),
('ORD-2025-0017', 6, 1, '2025-09-10 10:00:00', 'Hoàn thành',  510000,  45900,      0,  555900, NULL),
('ORD-2025-0018', 7, 2, '2025-09-28 15:00:00', 'Đã huỷ',      420000,      0,      0,       0, 'Trả hàng'),
-- 2025 Q4
('ORD-2025-0019', 3, 1, '2025-10-08 09:30:00', 'Hoàn thành', 1250000, 112500,  80000, 1282500, NULL),
('ORD-2025-0020', 8, 2, '2025-10-22 13:00:00', 'Hoàn thành',  390000,  35100,      0,  425100, NULL),
('ORD-2025-0021', 5, 1, '2025-11-11 10:00:00', 'Hoàn thành',  720000,  64800,  30000,  754800, 'Sale 11/11'),
('ORD-2025-0022', 1, 2, '2025-11-25 11:30:00', 'Hoàn thành',  580000,  52200,      0,  632200, NULL),
('ORD-2025-0023', 2, 1, '2025-12-05 09:00:00', 'Hoàn thành',  960000,  86400,  50000,  996400, NULL),
('ORD-2025-0024', 4, 2, '2025-12-20 16:00:00', 'Hoàn thành',  440000,  39600,      0,  479600, 'Sale Noel'),
-- 2026 Q1
('ORD-2026-0001', 6, 1, '2026-01-10 10:00:00', 'Hoàn thành',  680000,  61200,      0,  741200, NULL),
('ORD-2026-0002', 3, 2, '2026-01-25 14:30:00', 'Hoàn thành',  850000,  76500,  40000,  886500, 'Tết'),
('ORD-2026-0003', 7, 1, '2026-02-08 09:00:00', 'Hoàn thành',  520000,  46800,      0,  566800, NULL),
('ORD-2026-0004', 1, 2, '2026-02-20 15:00:00', 'Hoàn thành',  730000,  65700,      0,  795700, NULL),
('ORD-2026-0005', 5, 1, '2026-03-07 11:00:00', 'Hoàn thành',  610000,  54900,  20000,  644900, NULL),
('ORD-2026-0006', 8, 2, '2026-03-22 13:30:00', 'Đã huỷ',      370000,      0,      0,       0, 'Khách đổi ý'),
-- 2026 Q2 (tháng 4–6/2026, tháng hiện tại)
('ORD-2026-0007', 2, 1, '2026-04-05 09:30:00', 'Hoàn thành',  490000,  44100,      0,  534100, NULL),
('ORD-2026-0008', 4, 2, '2026-04-18 14:00:00', 'Hoàn thành',  780000,  70200,  30000,  820200, NULL),
('ORD-2026-0009', 3, 1, '2026-05-03 10:00:00', 'Hoàn thành', 1050000,  94500,  50000, 1094500, 'Khách quen'),
('ORD-2026-0010', 6, 2, '2026-05-19 15:30:00', 'Hoàn thành',  360000,  32400,      0,  392400, NULL),
('ORD-2026-0011', 1, 1, '2026-06-02 09:00:00', 'Hoàn thành',  820000,  73800,  40000,  853800, NULL),
('ORD-2026-0012', 7, 2, '2026-06-09 11:00:00', 'Đang xử lý',  550000,  49500,      0,  599500, 'Đơn hôm nay');

-- ============================================================
-- 7. CHI_TIET_DON_HANG
-- ============================================================
INSERT INTO CHI_TIET_DON_HANG (order_id, product_id, quantity, unit_price, size, color)
SELECT o.order_id, v.product_id, v.quantity, v.unit_price, v.size, v.color
FROM DON_HANG o
JOIN (VALUES
    -- format: (order_number, product_id, qty, price, size, color)
    ROW('ORD-2024-0001', 1,  2, 150000, 'M',    'Trắng'),
    ROW('ORD-2024-0001', 15, 1, 180000, 'Free', 'Đen'),
    ROW('ORD-2024-0002', 8,  1, 550000, '30',   'Xanh đậm'),
    ROW('ORD-2024-0002', 16, 1, 150000, 'Free', 'Kem'),
    ROW('ORD-2024-0003', 14, 1, 680000, 'M',    'Hồng nhạt'),
    ROW('ORD-2024-0003', 13, 2, 420000, 'S',    'Đỏ'),
    ROW('ORD-2024-0003', 7,  1, 450000, 'XL',   'Đen'),
    ROW('ORD-2024-0004', 10, 1, 380000, '30',   'Be'),
    ROW('ORD-2024-0005', 4,  1, 320000, 'M',    'Trắng'),
    ROW('ORD-2024-0005', 6,  1, 450000, 'L',    'Xám'),
    ROW('ORD-2024-0005', 15, 1, 180000, 'Free', 'Đen'),
    ROW('ORD-2024-0006', 17, 2, 280000, 'M',    'Xanh lá'),
    ROW('ORD-2024-0006', 12, 1, 220000, 'M',    'Đen'),
    ROW('ORD-2024-0007', 1,  2, 150000, 'L',    'Trắng'),
    ROW('ORD-2024-0008', 9,  1, 550000, '32',   'Xanh nhạt'),
    -- 2025 Q1
    ROW('ORD-2025-0001', 5,  1, 320000, 'L',    'Xanh nhạt'),
    ROW('ORD-2025-0001', 11, 1, 380000, '32',   'Nâu'),
    ROW('ORD-2025-0001', 16, 1, 150000, 'Free', 'Kem'),
    ROW('ORD-2025-0002', 18, 1, 280000, 'L',    'Đen'),
    ROW('ORD-2025-0002', 15, 1, 180000, 'Free', 'Đen'),
    ROW('ORD-2025-0003', 1,  2, 150000, 'M',    'Trắng'),
    ROW('ORD-2025-0003', 6,  1, 450000, 'L',    'Xám'),
    ROW('ORD-2025-0004', 12, 1, 220000, 'M',    'Đen'),
    ROW('ORD-2025-0004', 15, 1, 180000, 'Free', 'Đen'),
    ROW('ORD-2025-0005', 4,  1, 320000, 'M',    'Trắng'),
    ROW('ORD-2025-0005', 8,  1, 550000, '30',   'Xanh đậm'),
    ROW('ORD-2025-0006', 1,  2, 150000, 'M',    'Trắng'),
    -- 2025 Q2
    ROW('ORD-2025-0007', 6,  1, 450000, 'L',    'Xám'),
    ROW('ORD-2025-0007', 2,  1, 150000, 'L',    'Đen'),
    ROW('ORD-2025-0008', 17, 2, 280000, 'M',    'Xanh lá'),
    ROW('ORD-2025-0008', 15, 1, 180000, 'Free', 'Đen'),
    ROW('ORD-2025-0008', 16, 1, 150000, 'Free', 'Kem'),
    ROW('ORD-2025-0009', 3,  1, 150000, 'XL',   'Xanh Navy'),
    ROW('ORD-2025-0009', 13, 2, 420000, 'S',    'Đỏ'),
    ROW('ORD-2025-0010', 12, 1, 220000, 'M',    'Đen'),
    ROW('ORD-2025-0010', 2,  2, 150000, 'L',    'Đen'),
    ROW('ORD-2025-0011', 5,  1, 320000, 'L',    'Xanh nhạt'),
    ROW('ORD-2025-0011', 6,  1, 450000, 'L',    'Xám'),
    ROW('ORD-2025-0011', 10, 1, 380000, '30',   'Be'),
    ROW('ORD-2025-0012', 15, 1, 180000, 'Free', 'Đen'),
    ROW('ORD-2025-0012', 18, 1, 280000, 'L',    'Đen'),
    -- 2025 Q3
    ROW('ORD-2025-0013', 4,  1, 320000, 'M',    'Trắng'),
    ROW('ORD-2025-0013', 14, 1, 680000, 'M',    'Hồng nhạt'),
    ROW('ORD-2025-0013', 16, 1, 150000, 'Free', 'Kem'),
    ROW('ORD-2025-0014', 1,  2, 150000, 'M',    'Trắng'),
    ROW('ORD-2025-0015', 8,  1, 550000, '30',   'Xanh đậm'),
    ROW('ORD-2025-0015', 11, 1, 380000, '32',   'Nâu'),
    ROW('ORD-2025-0015', 16, 1, 150000, 'Free', 'Kem'),
    ROW('ORD-2025-0016', 17, 1, 280000, 'M',    'Xanh lá'),
    ROW('ORD-2025-0016', 15, 1, 180000, 'Free', 'Đen'),
    ROW('ORD-2025-0017', 6,  1, 450000, 'L',    'Xám'),
    ROW('ORD-2025-0017', 12, 1, 220000, 'M',    'Đen'),
    ROW('ORD-2025-0018', 7,  1, 450000, 'XL',   'Đen'),
    -- 2025 Q4
    ROW('ORD-2025-0019', 4,  2, 320000, 'M',    'Trắng'),
    ROW('ORD-2025-0019', 18, 1, 280000, 'L',    'Đen'),
    ROW('ORD-2025-0020', 13, 1, 420000, 'S',    'Đỏ'),
    ROW('ORD-2025-0020', 15, 1, 180000, 'Free', 'Đen'),
    ROW('ORD-2025-0021', 14, 1, 680000, 'M',    'Hồng nhạt'),
    ROW('ORD-2025-0021', 4,  2, 320000, 'M',    'Trắng'),
    ROW('ORD-2025-0021', 8,  1, 550000, '30',   'Xanh đậm'),
    ROW('ORD-2025-0022', 12, 1, 220000, 'M',    'Đen'),
    ROW('ORD-2025-0022', 18, 1, 280000, 'L',    'Đen'),
    ROW('ORD-2025-0023', 17, 2, 280000, 'M',    'Xanh lá'),
    ROW('ORD-2025-0023', 16, 1, 150000, 'Free', 'Kem'),
    ROW('ORD-2025-0024', 2,  2, 150000, 'L',    'Đen'),
    ROW('ORD-2025-0024', 15, 1, 180000, 'Free', 'Đen'),
    -- 2026 Q1
    ROW('ORD-2026-0001', 8,  1, 550000, '30',   'Xanh đậm'),
    ROW('ORD-2026-0001', 6,  1, 450000, 'L',    'Xám'),
    ROW('ORD-2026-0001', 16, 1, 150000, 'Free', 'Kem'),
    ROW('ORD-2026-0002', 5,  1, 320000, 'L',    'Xanh nhạt'),
    ROW('ORD-2026-0002', 11, 1, 380000, '32',   'Nâu'),
    ROW('ORD-2026-0003', 13, 1, 420000, 'S',    'Đỏ'),
    ROW('ORD-2026-0003', 14, 1, 680000, 'M',    'Hồng nhạt'),
    ROW('ORD-2026-0003', 16, 1, 150000, 'Free', 'Kem'),
    ROW('ORD-2026-0004', 6,  1, 450000, 'L',    'Xám'),
    ROW('ORD-2026-0004', 15, 1, 180000, 'Free', 'Đen'),
    ROW('ORD-2026-0005', 4,  1, 320000, 'M',    'Trắng'),
    ROW('ORD-2026-0005', 8,  1, 550000, '30',   'Xanh đậm'),
    ROW('ORD-2026-0006', 12, 1, 220000, 'M',    'Đen'),
    -- 2026 Q2
    ROW('ORD-2026-0007', 2,  1, 150000, 'L',    'Đen'),
    ROW('ORD-2026-0007', 18, 1, 280000, 'L',    'Đen'),
    ROW('ORD-2026-0008', 7,  1, 450000, 'XL',   'Đen'),
    ROW('ORD-2026-0008', 4,  1, 320000, 'M',    'Trắng'),
    ROW('ORD-2026-0009', 14, 1, 680000, 'M',    'Hồng nhạt'),
    ROW('ORD-2026-0009', 6,  1, 450000, 'L',    'Xám'),
    ROW('ORD-2026-0009', 16, 1, 150000, 'Free', 'Kem'),
    ROW('ORD-2026-0010', 13, 1, 420000, 'S',    'Đỏ'),
    ROW('ORD-2026-0011', 1,  2, 150000, 'M',    'Trắng'),
    ROW('ORD-2026-0011', 10, 1, 380000, '30',   'Be'),
    ROW('ORD-2026-0011', 15, 1, 180000, 'Free', 'Đen'),
    ROW('ORD-2026-0012', 6,  1, 450000, 'L',    'Xám'),
    ROW('ORD-2026-0012', 17, 1, 280000, 'M',    'Xanh lá')
) AS v(order_number, product_id, quantity, unit_price, size, color)
ON o.order_number = v.order_number;

-- ============================================================
-- 8. THANH_TOAN
-- ============================================================
INSERT INTO THANH_TOAN (order_id, payment_date, amount, payment_method, transaction_id, status)
SELECT o.order_id, v.payment_date, v.amount, v.payment_method, v.transaction_id, v.status
FROM DON_HANG o
JOIN (VALUES
    ROW('ORD-2024-0001','2024-10-05 09:20:00',  490500,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2024-0002','2024-10-18 14:35:00',  713000,'Chuyển khoản',         'TXN20241018001', 'Hoàn thành'),
    ROW('ORD-2024-0003','2024-11-05 10:10:00', 1622200,'Chuyển khoản',         'TXN20241105002', 'Hoàn thành'),
    ROW('ORD-2024-0004','2024-11-10 16:50:00',  414200,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2024-0005','2024-11-20 11:25:00',  983700,'Ví điện tử (Momo)',    'MOMO241120001',  'Hoàn thành'),
    ROW('ORD-2024-0006','2024-12-03 09:05:00',  654000,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2024-0007','2024-12-15 13:05:00',  327000,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2025-0001','2025-01-08 10:15:00',  887400,'Chuyển khoản',         'TXN20250108001', 'Hoàn thành'),
    ROW('ORD-2025-0002','2025-01-22 17:05:00',  468700,'Ví điện tử (ZaloPay)', 'ZALO250122001',  'Hoàn thành'),
    ROW('ORD-2025-0003','2025-02-10 09:35:00',  828400,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2025-0004','2025-02-28 14:05:00',  470500,'Chuyển khoản',         'TXN20250228002', 'Hoàn thành'),
    ROW('ORD-2025-0005','2025-03-12 11:05:00',  959200,'Ví điện tử (Momo)',    'MOMO250312001',  'Hoàn thành'),
    ROW('ORD-2025-0007','2025-04-07 10:05:00',  730300,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2025-0008','2025-04-19 15:05:00',  558600,'Chuyển khoản',         'TXN20250419001', 'Hoàn thành'),
    ROW('ORD-2025-0009','2025-05-03 09:20:00', 1002800,'Ví điện tử (ZaloPay)', 'ZALO250503001',  'Hoàn thành'),
    ROW('ORD-2025-0010','2025-05-17 13:50:00',  414200,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2025-0011','2025-06-02 10:35:00', 1149000,'Chuyển khoản',         'TXN20250602001', 'Hoàn thành'),
    ROW('ORD-2025-0012','2025-06-20 14:05:00',  316100,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2025-0013','2025-07-05 09:05:00',  817500,'Ví điện tử (Momo)',    'MOMO250705001',  'Hoàn thành'),
    ROW('ORD-2025-0014','2025-07-18 16:05:00',  501400,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2025-0015','2025-08-04 11:05:00',  656700,'Chuyển khoản',         'TXN20250804001', 'Hoàn thành'),
    ROW('ORD-2025-0016','2025-08-22 14:35:00',  915600,'Ví điện tử (ZaloPay)', 'ZALO250822001',  'Hoàn thành'),
    ROW('ORD-2025-0017','2025-09-10 10:05:00',  555900,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2025-0019','2025-10-08 09:35:00', 1282500,'Chuyển khoản',         'TXN20251008001', 'Hoàn thành'),
    ROW('ORD-2025-0020','2025-10-22 13:05:00',  425100,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2025-0021','2025-11-11 10:05:00',  754800,'Ví điện tử (Momo)',    'MOMO251111001',  'Hoàn thành'),
    ROW('ORD-2025-0022','2025-11-25 11:35:00',  632200,'Chuyển khoản',         'TXN20251125001', 'Hoàn thành'),
    ROW('ORD-2025-0023','2025-12-05 09:05:00',  996400,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2025-0024','2025-12-20 16:05:00',  479600,'Ví điện tử (ZaloPay)', 'ZALO251220001',  'Hoàn thành'),
    ROW('ORD-2026-0001','2026-01-10 10:05:00',  741200,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2026-0002','2026-01-25 14:35:00',  886500,'Chuyển khoản',         'TXN20260125001', 'Hoàn thành'),
    ROW('ORD-2026-0003','2026-02-08 09:05:00',  566800,'Ví điện tử (Momo)',    'MOMO260208001',  'Hoàn thành'),
    ROW('ORD-2026-0004','2026-02-20 15:05:00',  795700,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2026-0005','2026-03-07 11:05:00',  644900,'Chuyển khoản',         'TXN20260307001', 'Hoàn thành'),
    ROW('ORD-2026-0007','2026-04-05 09:35:00',  534100,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2026-0008','2026-04-18 14:05:00',  820200,'Ví điện tử (ZaloPay)', 'ZALO260418001',  'Hoàn thành'),
    ROW('ORD-2026-0009','2026-05-03 10:05:00', 1094500,'Chuyển khoản',         'TXN20260503001', 'Hoàn thành'),
    ROW('ORD-2026-0010','2026-05-19 15:35:00',  392400,'Tiền mặt',             NULL,             'Hoàn thành'),
    ROW('ORD-2026-0011','2026-06-02 09:05:00',  853800,'Ví điện tử (Momo)',    'MOMO260602001',  'Hoàn thành')
    -- ORD-2026-0012 đang xử lý → không có thanh toán
) AS v(order_number, payment_date, amount, payment_method, transaction_id, status)
ON o.order_number = v.order_number;