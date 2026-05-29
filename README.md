# Ứng dụng quản lý cửa hàng

Ứng dụng Java desktop để quản lý các hoạt động của cửa hàng bao gồm quản lý người dùng, khách hàng, tồn kho sản phẩm và xử lý đơn hàng.

## Các công nghệ được sử dụng

- **Java 25** (cito trong pom.xml)
- **Hibernate ORM 7.0.5.Final** - Framework ánh chiếu đối quan hệ
- **MySQL Connector/J 9.3.0** - Kết nối cơ sở dữ liệu
- **Jakarta Persistence API 3.2.0** - Triển khai JPA
- **jBCrypt 0.4** - Băm mật khẩu
- **Maven** - Tự động hoá xây dựng và quản lý phụ thuộc

## Cấu trúc dự án

```
app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/shopapp/
│   │   │       ├── entity/          # JPA entity classes
│   │   │       ├── repository/      # Data access layer interfaces
│   │   │       ├── repository/impl/ # Repository implementations
│   │   │       ├── service/         # Business logic interfaces
│   │   │       ├── service/impl/    # Service implementations
│   │   │       ├── ui/              # User interface components
│   │   │       │   ├── components/  # Reusable UI components
│   │   │       │   ├── frame/       # Main application frames and panels
│   │   │       │   │   └── panels/  # Individual panel implementations
│   │   │       │   ├── listeners/   # UI event listeners
│   │   │       │   └── themes/      # UI theme management
│   │   │       └── util/            # Utility classes
│   │   └── resources/               # Configuration files
│   │       └── hibernate.cfg.xml    # Hibernate configuration
│   └── test/                        # Test classes
└── pom.xml                          # Maven project configuration
```

## Tính năng chính

- Xác thực và ủy quyền người dùng
- Quản lý khách hàng (thao tác CRUD)
- Quản lý tồn kho sản phẩm
- Xử lý và theo dõi đơn hàng
- Kiểm soát truy cập dựa trên vai trò
- Bảo mật mật khẩu bằng cách băm bcrypt
- Lớp持久性 dựa trên Hibernate
- Giao diện người dùng desktop dựa trên Swing

## Hướng dẫn cài đặt

1. **Điều kiện tiên quyết**
   - Java JDK 25 hoặc cao hơn
   - MySQL Server
   - Maven 3.0+

2. **Cấu hình cơ sở dữ liệu**
   - Tạo cơ sở dữ liệu MySQL cho ứng dụng
   - Cập nhật chi tiết kết nối cơ sở dữ liệu trong `hibernate.cfg.xml`:
     ```xml
     <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/your_database</property>
     <property name="hibernate.connection.username">your_username</property>
     <property name="hibernate.connection.password">your_password</property>
     ```

## Mối quan điểm entidad

Ứng dụng bao gồm các entidad chính sau:

- **NguoiDung** (Người dùng) - Người dùng ứng dụng có xác thực
- **KhachHang** (Khách hàng) - Khách hàng của cửa hàng
- **SanPham** (Sản phẩm) - Các mặt hàng có sẵn để bán
- **DonHang** (Đơn hàng) - Đơn hàng của khách hàng
- **ChiTietDonHang** (Mặt hàng trong đơn hàng) - Các mục riêng lẻ trong một đơn hàng
- **Vaitro** (Vai trò) - Vai trò người dùng để quản lý quyền限
- **RolePermissions** - Bảng liên kết cho quyền hạn dựa trên vai trò
- **ThanhToan** (Thanh toán) - Thông tin thanh toán
- **TonKho** (Tồn kho) - Mức độ tồn kho
- **DanhMuc** (Danh mục) - Danh mục sản phẩm
- **NhaCungCap** (Nhà cung cấp) - Nhà cung cấp sản phẩm

## Tính năng bảo mật

- Mật khẩu được băm bằng BCrypt trước khi lưu trữ
- Kiểm soát truy cập dựa trên vai trò được thực hiện qua các entidad Vaitro và RolePermissions
- Quản lý giao dịch Hibernate để đảm bảo tính nhất quán dữ liệu

## Giấy phép

Dự án này được phát triển như là một phần của bài tập môn học hoặc dự án nội bộ. Vui lòng tham khảo tập tin LICENSE để biết các quyền利用 cụ thể.

## Đóng góp

1. Fork kho lưu trữ
2. Tạo nhánh tính năng (`git checkout -b feature/amazing-feature`)
3. Cam kết các thay đổi của bạn (`git commit -m 'Thêm tính năng惊人'`)
4. Đẩy lên nhánh (`git push origin feature/amazing-feature`)
5. Mở một Pull Request

## Liên hệ

Đối với câu hỏi hoặc hỗ trợ, vui lòng mở một issue trong kho lưu trữ.