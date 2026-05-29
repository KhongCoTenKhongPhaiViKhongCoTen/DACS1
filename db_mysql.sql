-- MySQL Database Schema for Clothing Store Management System
-- Converted from ER design in db_design_clothing_store.md
create schema dacs1;
use dacs1;

-- Drop tables if they exist (for clean rebuild)
DROP TABLE IF EXISTS THANH_TOAN;
DROP TABLE IF EXISTS CHI_TIET_DON_HANG;
DROP TABLE IF EXISTS DON_HANG;
DROP TABLE IF EXISTS KHACH_HANG;
DROP TABLE IF EXISTS TON_KHO;
DROP TABLE IF EXISTS ROLE_PERMISSIONS;
DROP TABLE IF EXISTS SAN_PHAM;
DROP TABLE IF EXISTS NHA_CUNG_CAP;
DROP TABLE IF EXISTS DANH_MUC;
DROP TABLE IF EXISTS QUYEN;
DROP TABLE IF EXISTS VAITRO;
DROP TABLE IF EXISTS NGUOI_DUNG;

-- 1. Vai trò (Roles)
CREATE TABLE VAITRO (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL,
    description TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Quyền (Permissions)
CREATE TABLE QUYEN (
    permission_id INT PRIMARY KEY AUTO_INCREMENT,
    permission_code VARCHAR(50) NOT NULL UNIQUE,
    permission_name VARCHAR(100) NOT NULL,
    module VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Danh mục (Categories)
CREATE TABLE DANH_MUC (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL,
    description TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Nhà cung cấp (Suppliers)
CREATE TABLE NHA_CUNG_CAP (
    supplier_id INT PRIMARY KEY AUTO_INCREMENT,
    company_name VARCHAR(150) NOT NULL,
    contact_name VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Người dùng (Users)
CREATE TABLE NGUOI_DUNG (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    role_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES VAITRO(role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Sản phẩm (Products)
CREATE TABLE SAN_PHAM (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    sku VARCHAR(50) NOT NULL UNIQUE,
    product_name VARCHAR(200) NOT NULL,
    description TEXT,
    category_id INT NOT NULL,
    supplier_id INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    cost_price DECIMAL(10,2),
    size VARCHAR(20),
    color VARCHAR(50),
    reorder_level INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (category_id) REFERENCES DANH_MUC(category_id),
    FOREIGN KEY (supplier_id) REFERENCES NHA_CUNG_CAP(supplier_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. RolePermissions (Junction table for Roles and Permissions)
CREATE TABLE ROLE_PERMISSIONS (
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    granted_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES VAITRO(role_id),
    FOREIGN KEY (permission_id) REFERENCES QUYEN(permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. Tồn kho (Inventory)
CREATE TABLE TON_KHO (
    inventory_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    quantity_on_hand INT NOT NULL DEFAULT 0,
    last_updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    location VARCHAR(100),
    FOREIGN KEY (product_id) REFERENCES SAN_PHAM(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. Khách hàng (Customers)
CREATE TABLE KHACH_HANG (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    loyalty_points INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. Đơn hàng (Orders)
CREATE TABLE DON_HANG (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    customer_id INT,
    user_id INT NOT NULL,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'Đang xử lý',
    subtotal DECIMAL(10,2) DEFAULT 0.00,
    tax_amount DECIMAL(10,2) DEFAULT 0.00,
    discount_amount DECIMAL(10,2) DEFAULT 0.00,
    total_amount DECIMAL(10,2) DEFAULT 0.00,
    notes TEXT,
    FOREIGN KEY (customer_id) REFERENCES KHACH_HANG(customer_id),
    FOREIGN KEY (user_id) REFERENCES NGUOI_DUNG(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 11. Chi tiết đơn hàng (Order Items)
CREATE TABLE CHI_TIET_DON_HANG (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) AS (quantity * unit_price) STORED,
    size VARCHAR(20),
    color VARCHAR(50),
    FOREIGN KEY (order_id) REFERENCES DON_HANG(order_id),
    FOREIGN KEY (product_id) REFERENCES SAN_PHAM(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 12. Thanh toán (Payments)
CREATE TABLE THANH_TOAN (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(100),
    status VARCHAR(20) DEFAULT 'Đang chờ',
    FOREIGN KEY (order_id) REFERENCES DON_HANG(order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Indexes for better performance (optional but recommended)
CREATE INDEX idx_donhang_customer ON DON_HANG(customer_id);
CREATE INDEX idx_donhang_user ON DON_HANG(user_id);
CREATE INDEX idx_donhang_status ON DON_HANG(status);
CREATE INDEX idx_chitiet_donhang_order ON CHI_TIET_DON_HANG(order_id);
CREATE INDEX idx_chitiet_donhang_product ON CHI_TIET_DON_HANG(product_id);
CREATE INDEX idx_thanhtoan_order ON THANH_TOAN(order_id);
CREATE INDEX idx_thanhtoan_status ON THANH_TOAN(status);
CREATE INDEX idx_sanpham_category ON SAN_PHAM(category_id);
CREATE INDEX idx_sanpham_supplier ON SAN_PHAM(supplier_id);
CREATE INDEX idx_sanpham_sku ON SAN_PHAM(sku);
CREATE INDEX idx_tonkho_product ON TON_KHO(product_id);
CREATE INDEX idx_tonkho_location ON TON_KHO(location);
