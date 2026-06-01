# ỨNG DỤNG QUẢN LÝ CỬA HÀNG
## Presentation Overview

---

## 1. TỔNG QUAN DỰ ÁN (PROJECT OVERVIEW)

**Người dùng mục tiêu:**
- Nhân viên quản lý cửa hàng quần áo
- Quản lý tồn kho và đơn hàng
- Admin người dùng hệ thống

**Vấn đề cốt lõi được giải quyết:**
- Quản lý thủ công các hoạt động cửa hàng dẫn đến lỗi và thiếu hiệu quả
- Thiếu hệ thống tập trung để theo dõi tồn kho, đơn hàng và khách hàng
- Cần kiểm soát truy cập dựa trên vai trò cho các chức năng khác nhau

**Mục tiêu chính:**
- Phát triển ứng dụngdesktop Java để tự động hoá quản lý cửa hàng
- Triển khai CRUD đầy đủ cho khách hàng, sản phẩm, đơn hàng và người dùng
- Cung cấp báo cáo vàTheo dõi tồn kho thời gian thực
- Đảm bảo bảo mật qua xác thực và phân quyền dựa trên vai trò

---

## 2. KIẾN TRÚC & CÔNG NGHỆ (ARCHITECTURE & TECH STACK)

**Backend:**
- Java 25 (phiên bản LTS mới nhất)
- Hibernate ORM 7.0.5.Final (ORM framework)
- Jakarta Persistence API 3.2.0 (JPA implementation)
- Maven 3.0+ (dependency management & build tool)

**Database:**
- MySQL Connector/J 9.3.0 (JDBC driver)
- MySQL Server (hệ quản lýCSDL quan hệ)

**Frontend/UI:**
- Java Swing (GUI toolkit)
- Custom UI components (BaseForm, BaseDialog, BasePage)
- Theme management system (light/dark mode support)
- Responsive layout managers (GridBagLayout, FlowLayout)

**Security:**
- jBCrypt 0.4 (password hashing)
- Role-based access control (RBAC) implementation
- Hibernate transaction management for data consistency

**Architecture Pattern:**
- Layered architecture (Entity, Repository, Service, UI)
- MVC-inspired separation of concerns
- DAO pattern for data access layer
- Service layer for business logic

---

## 3. SƠ ĐỒ CƠ SỞ DỮ LIỆU (DATABASE SCHEMA/MODELS)

**Entities chính và mối quan hệ:**

1. **NguoiDung** (Người dùng)
   - username (PK), password, fullName, email, phone, status
   - Relationship: Many-to-Many with Vaitro (through RolePermissions)

2. **Vaitro** (Vai trò)
   - roleId (PK), roleName, description
   - Relationship: Many-to-Many with NguoiDung (through RolePermissions)

3. **RolePermissions** (Junction table)
   - Composite PK (roleId, permissionId)
   - Relationships: Many-to-One to Vaitro and Quyen

4. **Quyen** (Permissions)
   - permissionId (PK), permission_code (unique, length=50), permission_name (length=100), module (length=50)
   - Predefined constants: PRODUCT_READ, ORDER_CREATE, USER_DELETE, etc.

5. **KhachHang** (Khách hàng)
   - customerId (PK), customerCode, fullName, email, phone, address, status

6. **SanPham** (Sản phẩm)
   - productId (PK), productCode, productName, description, categoryId (FK), price, status
   - Relationship: Many-to-One with DanhMuc

7. **DanhMuc** (Danh mục)
   - categoryId (PK), categoryName, description, status

8. **NhaCungCap** (Nhà cung cấp)
   - supplierId (PK), supplierCode, companyName, contactName, email, phone, address, status

9. **TonKho** (Tồn kho)
   - stockId (PK), productId (FK), quantity, lastUpdated
   - Relationship: Many-to-One with SanPham

10. **DonHang** (Đơn hàng)
    - orderId (PK), orderCode, customerId (FK), orderDate, status, totalAmount, paymentMethod
    - Relationship: Many-to-One with KhachHang

11. **ChiTietDonHang** (Mặt hàng trong đơn hàng)
    - Composite PK (orderId, productId)
    - Relationships: Many-to-One to DonHang and SanPham
    - quantity, unitPrice, totalPrice

12. **ThanhToan** (Thanh toán)
    - paymentId (PK), orderId (FK), paymentDate, amount, paymentMethod, transactionId, status
    - Relationship: Many-to-One with DonHang

---

## 4. CÁC CHỨC NĂNG CHÍNH (CORE FEATURES)

**Quản lý Người dùng (User Management):**
- Đăng nhập/đăng xuất với xác thực bcrypt
- Quản lý profile người dùng (thêm, sửa, xóa, xem danh sách)
- Phân quyền dựa trên vai trò (RBAC)
- Trạng thái tài khoản (kích hoạt/khóa)

**Quản lý Vai trò và Quyền hạn (Role & Permission Management):**
- Định nghĩa vai trò tùy chỉnh (Quản lý, Nhân viên, Admin)
- Gán quyền hạn específicas cho mỗi vai trò
- Quản lý ma trận quyền hạn (role-permission mapping)

**Quản lý Khách hàng (Customer Management):**
-CRUD operations cho thông tin khách hàng
- Tìm kiếm và lọc khách hàng theo tên, email, số điện thoại
- Theo dõi lịch sử mua hàng của khách hàng

**Quản lý Sản phẩm (Product Management):**
- CRUD operations cho sản phẩm
- Phân loại sản phẩm theo danh mục
- Quản lý mã vạch và mô tả chi tiết
- Theo dõi trạng thái sản phẩm (active/inactive)

**Quản lý Danh mục (Category Management):**
- Tạo và quản lý danh mục sản phẩm
- Mô tả danh mục để phân loại tốt hơn

**Quản lý Nhà cung cấp (Supplier Management):**
- Thông tin liên hệ nhà cung cấp
- Theo dõi sản phẩm được cung cấp từ mỗi nhà cung cấp
- Quản lý trạng thái hợp tác

**Quản lý Tồn kho (Inventory Management):**
- Theo dõi mức tồn kho thực thời
- Cập nhật tự động khi nhập/xuất hàng
- Cấu hình cảnh báo tồn kho thấp
- Lịch sử giao dịch nhập/xuất

**Quản lý Đơn hàng (Order Management):**
- Tạo đơn hàng mới từ giỏ hàng
- Thêm/sửa/xóa các mặt hàng trong đơn hàng
- Tính toán tự động tổng tiền và thuế
- Theo dõi trạng thái đơn hàng (mới, xác nhận, đang giao, đã giao, hủy)
- Thanh toán qua nhiều phương thức (tiền mặt, thẻ, chuyển khoản)

**Báo cáo và Thống kê (Reporting & Analytics):**
- Báo cáo bán hàng theo thời gian (ngày, tháng, năm)
- Thống kê tồn kho và vòng quay hàng
- Báo cáo khách hàng hàng tháng
- Doanh thu theo danh mục sản phẩm

**Tính năng Nâng cao:**
- Giao diện người dùng hiện đại với hỗ trợ chế độ sáng/tối
- Thông báo và cảnh báo trong-app
- Xuất báo cáo ra formato PDF/Excel (đang phát triển)
- Sao lưu và khôi phục dữ liệu
- Quản lý phiên làm việc tự động

---

## 5. HƯỚNG DẪN CÀI ĐẶT & CHẠY (SETUP & EXECUTION)

**Điều kiện tiên quyết:**
- Java JDK 25 hoặc cao hơn
- MySQL Server 8.0+
- Maven 3.0+
- Git (tùy chọn)

**Các bước cài đặt:**

1. **Clone repository:**
   ```bash
   git clone <repository-url>
   cd E:\DACS1
   ```

2. **Cấu hình cơ sở dữ liệu:**
   - Tạo database MySQL: `CREATE DATABASE shopapp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
   - Cập nhật file `app/src/main/resources/hibernate.cfg.xml`:
     ```xml
     <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/shopapp</property>
     <property name="hibernate.connection.username">your_username</property>
     <property name="hibernate.connection.password">your_password</property>
     ```

3. **Xây dựng dự án:**
   ```bash
   mvn clean install
   ```

4. **Chạy ứng dụng:**
   ```bash
   mvn javafx:run
   ```
   Hoặc chạy trực tiếp từ IDE (IntelliJ IDEA, Eclipse, VS Code) bằng cách chạy class `com.shopapp.AppSys`

**Cấu hình ban đầu:**
- Khi chạy lần đầu tiên, ứng dụng sẽ tạo tài khoản admin mặc định:
  - Username: admin
  - Password: admin123
  - Nhắc thay đổi mật khẩu khi đăng nhập lần đầu

**Lưu ý quan trọng:**
- Đảm bảo MySQL service đang chạy trước khi khởi động ứng dụng
- Cổng MySQL mặc định là 3306, có thể thay đổi trong hibernate.cfg.xml nếu cần
- Ứng dụng sử dụng một file log tại `logs/app.log` để theo dõi hoạt động
- Đối với môi trường production, рекомендуется использовать connection pool và cấu hình bảo mật nâng cao

---
*Presentation generated on: 2026-06-01*