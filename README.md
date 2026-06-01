# ShopApp - Hệ thống quản lý cửa hàng java desktop

Hệ thống quản lý cửa hàng đầy đủ được xây dựng bằng Java 25 với giao diện desktop sử dụng Swing, cung cấp các tính năng quản lý người dùng, sản phẩm, đơn hàng, khách hàng, và hàng tồn kho với bảo mật mạnh mẽ.

## Tổng quan dự án

ShopApp là một ứng dụng Java desktop giúp quản lý hiệu quả các hoạt động hàng ngày của cửa hàng bán lẻ, bao gồm quản lý người dùng với vai trò, quản lý sản phẩm và danh mục, xử lý đơn hàng, quản lý khách hàng và theo dõi hàng tồn kho. Ứng dụng được thiết kế với kiến trúc lớp rõ ràng và tuân thủ các nguyên tắc thiết kế phần mềm hiện đại.

## Công nghệ sử dụng

- **Ngôn ngữ**: Java 25 (JDK 25)
- **Framework ORM**: Hibernate 7.0.5.Final
- **Cơ sở dữ liệu**: MySQL Connector/J 9.3.0
- **API tiêu chuẩn**: Jakarta Persistence API 3.2.0 (JPA)
- **Bảo mật**: jBCrypt 0.4 (băm mật khẩu)
- **Công cụ xây dựng**: Apache Maven
- **Giao diện người dùng**: Java Swing
- **Quản lý theme**: Hệ thống theme tùy chỉnh

## Cấu trúc dự án

```
app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/shopapp/
│   │   │       ├── entity/          # JPA entity classes (bản đồ đối tượng quan hệ)
│   │   │       ├── repository/      # Interface truy cập dữ liệu (Data Access Layer)
│   │   │       ├── repository/impl/ # Triển khai interface repository
│   │   │       ├── service/         # Interface logic kinh doanh
│   │   │       ├── service/impl/    # Triển khai service (chứa logic nghiệp vụ)
│   │   │       ├── ui/              # Thành phần giao diện người dùng
│   │   │       │   ├── components/  # Thành phần UI có thể tái sử dụng
│   │   │       │   ├── frame/       # Khung chính vàPanel ứng dụng
│   │   │       │   │   └── panels/  # Triển khai từng panel cụ thể
│   │   │       │   ├── listeners/   # Nghe sự kiện UI
│   │   │       │   └── themes/      # Quản lý giao diện/theme
│   │   │       └── util/            # Các lớp tiện ích
│   │   └── resources/               # Tập tin cấu hình
│   │       └── hibernate.cfg.xml    # Cấu hình Hibernate
│   └── test/                        # Lớp kiểm thử (đang phát triển)
└── pom.xml                          # Cấu hình Maven
```

## Tính năng chính

### 🔐 Xác thực & Ủy quyền
- Hệ thống đăng nhập/đăng xuất sécure
- Mã hóa mật khẩu bằng BCrypt
- Kiểm soát truy cập dựa trên vai trò (RBAC)
- Quản lý quyền chi tiết cho mỗi tính năng

### 👥 Quản lý người dùng
- Tạo, đọc, cập nhật, xóa (CRUD) người dùng
- Quản lý vai trò và phân quyền
- Xem hồ sơ người dùng hiện tại

### 📦 Quản lý kho hàng
- Quản lý danh mục sản phẩm
- Quản lý nhà cung cấp
- Quản lý sản phẩm (tên, giá, mô tả, danh mục)
- Theo dõi mức tồn kho
- Cập nhật tồn kho tự động khi có đơn hàng

### 🛒 Xử lý đơn hàng
- Tạo và quản lý đơn hàng
- Thêm/sửa/xóachi tiết đơn hàng
- Theo dõi trạng thái đơn hàng
- Tính toán tổng tiền tự động
- Quản lý thanh toán

### 👤 Quản lý khách hàng
- Thông tin khách hàng chi tiết
- Lịch sử mua hàng
- Tìm kiếm và lọc khách hàng

### 🎨 Giao diện người dùng
- Giao diện desktop hiện đại bằng Swing
- Hỗ trợ chuyển đổi theme (sáng/tối)
- Thiết kế phản hồi, dễ sử dụng
- Thanh điều hướng bên với biểu tượng直观
- Hệ thống thông báo và phản hồi

## Hướng dẫn cài đặt

### Điều kiện tiên quyết
- Java JDK 25 hoặc cao hơn
- MySQL Server 8.0+
- Apache Maven 3.6.0+
- Git (tùy chọn, để clone dự án)

### Các bước cài đặt

1. **Clone repository**
   ```bash
   git clone https://github.com/yourusername/shopapp.git
   cd shopapp
   ```

2. **Cấu hình cơ sở dữ liệu**
   - Tạo cơ sở dữ liệu MySQL mới (ví dụ: `shopapp`)
   - Cập nhật thông tin kết nối trong `app/src/main/resources/hibernate.cfg.xml`:
     ```xml
     <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/shopapp</property>
     <property name="hibernate.connection.username">your_mysql_username</property>
     <property name="hibernate.connection.password">your_mysql_password</property>
     ```

3. **Xây dựng dự án**
   ```bash
   mvn clean install
   ```

4. **Chạy ứng dụng**
   ```bash
   cd app
   mvn javafx:run
   ```
   hoặc chạy trực tiếp lớp main:
   ```bash
   java -cp target/classes:target/dependency/* com.shopapp.AppSys
   ```

## Mô hình Entity

Hệ thống sử dụng JPA/Hibernate để ánh xạ đối tượng quan hệ với các entity chính:

- **NguoiDung** - Người dùng hệ thống (username, password, role, etc.)
- **KhachHang** - Khách hàng cửa hàng (tên, liên hệ, địa chỉ, etc.)
- **SanPham** - Sản phẩm bán hàng (tên, giá, mô tả, danh mục)
- **DonHang** - Đơn hàng từ khách hàng (ngày, tổng tiền, trạng thái)
- **ChiTietDonHang** - Sản phẩm trong đơn hàng (số lượng, giá)
- **Vaitro** - Vai trò trong hệ thống (ADMIN, STAFF, etc.)
- **RolePermissions** - Ánh xạ vai trò → quyền限
- **DanhMuc** - Danh mục sản phẩm
- **NhaCungCap** - Nhà cung cấp sản phẩm
- **TonKho** - Lượng tồn kho của sản phẩm
- **ThanhToan** - Thông tin thanh toán đơn hàng

## Tính năng bảo mật

- **Mã hóa mật khẩu**: Tất cả mật khẩu được băm bằng BCrypt trước khi lưu vào DB
- **Kiểm soát truy cập**: Hệ thống RBAC (Role-Based Access Control) chi tiết
- **Quản lý phiên**: Theo dõi phiên đăng nhập và tự động تسجيل xuất
- **Bảo mật giao diện**: Các nút và menu chỉ hiển thị khi usuário có đủ quyền hạn

## Cấu trúc chức năng theo vai trò

### Vai trò ADMIN
- Truy cập toàn hệ thống
- Quản lý người dùng, vai trò, quyền限
- Quản lý tất cả mô hình dữ liệu
- Cấu hình hệ thống

### Vai trò STAFF/NHÂN VIÊN
- Quản lý sản phẩm, đơn hàng, khách hàng
- Xem báo cáo cơ bản
- Không thể thay đổi cài đặt hệ thống

### Vai trò KHÁCH HÀNG (nếu có)
- Xem sản phẩm và đặt hàng
- Xem lịch sử đơn hàng của mình

## Hướng dẫn phát triển

### Quy tắc написания кода
- Tuấn theo chuẩn Java Code Conventions
- Tên lớp: PascalCase
- Tên phương thức và biến: camelCase
- Hằng số: UPPER_SNAKE_CASE
- Viết comment Javadoc cho các classe và phương thức công cộng
- Đơn vị test: JUnit 5

### Quản lý dependencies
Tất cả dependencies được khai báo trong `pom.xml`. Để thêm thư viện mới:
```xml
<dependency>
    <groupId>group.id</groupId>
    <artifactId>artifact-id</artifactId>
    <version>x.y.z</version>
</dependency>
```

### Cấu hình Hibernate
Tập tin cấu hình chính: `app/src/main/resources/hibernate.cfg.xml`
- Cấu hình kết nối DB
- Cấu hình dialect (MySQL)
- Cấu hình hbm2ddl (tự động tạo/update bảng)
- Cấu hình pool kết nối

## Cơ chế phiên làm việc

Ứng dụng sử dụng lớp `AppSys` làm container singleton để lưu trữ:
- Người dùng hiện tại đăng nhập (`NguoiDung`)
- Quản lý quyền限 (`QuyenManager`)
- Cung cấp phương thức tĩnh để truy cập từ mọi nơi trong ứng dụng

## Hệ thống giao diện và theme

- `ThemeManager`: Quản lý các theme khác nhau (sáng, tối, tùy chỉnh)
- `NavBar`: Thanh điều hướng bên dựa trên quyền限 của người dùng
- `MainFrame`: Cửa sổ chính chứa các panel chuyển đổi qua CardLayout
- Tุก component UI được thiết kế để phản hồi tự động khi thay đổi theme

## Công lề đóng góp

1. Fork repository này
2. Tạo nhánh tính năng mới (`git checkout -b feature/ten-tinh-nang`)
3. Thực hiện thay đổi và commit (`git commit -m 'Mô tả ngắn gọn về thay đổi'`)
4. Push lên nhánh của bạn (`git push origin feature/ten-tinh-nang`)
5. Tạo Pull Request để review

### Nguyên tắc PR
- Mô tả rõ ràng về vấn đề và giải pháp
- Đảm bảo code tuân thủ quy tắc hiện tại
- Viết unit tests cho chức năng mới (nếu có)
- Không làm thay đổi gì không liên quan trong cùng một commit

## Liên hệ và hỗ trợ

Báo lỗi và đề xuất tính năng: Mở Issue trong repository này
Thảo luận chung: Tham khảo các hướng dẫn trong codebase

---

*Phát triển bởi團隊 ShopApp - Ứng dụng quản lý cửa hàng chuyên nghiệp*
