# Cấu Trúc Dự Án ShopApp

Tài liệu này giải thích cấu trúc thư mục và sự tổ chức của ứng dụng Java Swing ShopApp.

## Tổng Quan

ShopApp là một ứng dụng máy tính để bàn dựa trên Java để quản lý các hoạt động của cửa hàng bao gồm sản phẩm, khách hàng, đơn hàng, người dùng và vai trò. Ứng dụng tuân thủ kiến trúc طبقه lớp với việc tách biệt rõ ràng về trách nhiệm.

## Cấu Trúc Thư Mục

```
app/
├── pom.xml                     # Cấu hình dự án Maven
├── src/
│   ├── main/
│   │   ├── java/               # Mã nguồn Java
│   │   │   ├── com/            # Gói cơ sở
│   │   │   │   ├── Main.java   # Điểm vào ứng dụng
│   │   │   │   └── shopapp/    # Gói ứng dụng chính
│   │   │   │       ├── domain/         # Các đối tượng kinh doanh (mô hình JPA/Hibernate)
│   │   │   │       │   ├── model/      # Các lớp thực thể
│   │   │   │       │   │   ├── SanPham.java          # Thực thể sản phẩm
│   │   │   │       │   │   ├── DanhMuc.java          # Thực thể danh mục
│   │   │   │       │   │   ├── NhaCungCap.java       # Thực thể nhà cung cấp
│   │   │   │       │   │   ├── KhachHang.java        # Thực thể khách hàng
│   │   │   │       │   │   ├── DonHang.java          # Thực thể đơn hàng
│   │   │   │       │   │   ├── ChiTietDonHang.java   # Thực thể chi tiết đơn hàng
│   │   │   │       │   │   ├── Vaitro.java           # Thực thể vai trò
│   │   │   │       │   │   ├── NguoiDung.java        # Thực thể người dùng
│   │   │   │       │   │   ├── ThanhToan.java        # Thực thể thanh toán
│   │   │   │       │   │   ├── TonKho.java           # Thực thể tồn kho
│   │   │   │       │   │   ├── Quyen.java            # Thực thể quyền限限
│   │   │   │       │   │   ├── RolePermissions.java  # Ánh xạ Vai trò-Quyền
│   │   │   │       │   │   └── RolePermissionsId.java# Khóa hợp thành cho RolePermissions
│   │   │   │       │
│   │   │   │       ├── repository/     # Giao diện lớp truy cập dữ liệu
│   │   │   │       │   ├── SanPhamRepository.java
│   │   │   │       │   ├── DanhMucRepository.java
│   │   │   │       │   ├── NhaCungCapRepository.java
│   │   │   │       │   ├── KhachHangRepository.java
│   │   │   │       │   ├── DonHangRepository.java
│   │   │   │       │   ├── ChiTietDonHangRepository.java
│   │   │   │       │   ├── VaitroRepository.java
│   │   │   │       │   ├── NguoiDungRepository.java
│   │   │   │       │   ├── ThanhToanRepository.java
│   │   │   │       │   ├── TonKhoRepository.java
│   │   │   │       │   └── impl/       # Triển khai repository (bộ nhớ trong để демон lý)
│   │   │   │       │       ├── SanPhamRepositoryImpl.java
│   │   │   │       │       ├── VaitroRepositoryImpl.java
│   │   │   │       │       └── NguoiDungRepositoryImpl.java
│   │   │   │       │
│   │   │   │       ├── service/        # Giao diện lớp logic nghiệp vụ
│   │   │   │       │   ├── SanPhamService.java
│   │   │   │       │   ├── DanhMucService.java
│   │   │   │       │   ├── NhaCungCapService.java
│   │   │   │       │   ├── KhachHangService.java
│   │   │   │       │   ├── DonHangService.java
│   │   │   │       │   ├── VaitroService.java
│   │   │   │       │   ├── NguoiDungService.java
│   │   │   │       │   └── impl/       # Triển khai dịch vụ
│   │   │   │       │       ├── SanPhamServiceImpl.java
│   │   │   │       │       ├── VaitroServiceImpl.java
│   │   │   │       │       └── NguoiDungServiceImpl.java
│   │   │   │       │
│   │   │   │       └── ui/             # Lớp giao diện người dùng (Swing components)
│   │   │   │           ├── frame/      # Các khung và bảng الرئيسي
│   │   │   │           │   ├── MainFrame.java          # Cửa sổ ứng dụng chính
│   │   │   │           │   ├── NavBar.java             # Thanh điều hướng
│   │   │   │           │   └── panels/               # Các bảng riêng cho các mô-đun khác nhau
│   │   │   │           │       ├── HomePage.java
│   │   │   │           │       ├── SanPhamPanel.java
│   │   │   │           │       ├── KhachHangPanel.java
│   │   │   │           │       ├── DonHangPanel.java
│   │   │   │           │       ├── VaitroPanel.java
│   │   │   │           │       ├── NguoiDungPanel.java
│   │   │   │           │       ├── SettingsPage.java
│   │   │   │           │       └── AccountManagementPanel.java
│   │   │   │           │
│   │   │   │           ├── themes/     # Quản lý chủ đề giao diện
│   │   │   │           │   ├── Theme.java
│   │   │   │           │   └── ThemeManager.java
│   │   │   │           │
│   │   │   │           └── listeners/  # Bộ lắng nghe sự kiện
│   │   │   │               └── PageChangeListener.java
│   │   │   │
│   │   └── resources/              # Các tệp cấu hình
│   │       └── hibernate.cfg.xml   # Cấu hình ORM Hibernate
│   │
│   └── test/                   # Mã nguồn kiểm thử (không hiển thị trong cấu trúc hiện tại)
│
└── target/                     # Đầu ra đã biên dịch (tạo bởi Maven)
    ├── classes/                # Các tệp .class đã biên dịch
    │   ├── com/
    │   └── ... (giống cấu trúc src/main/java)
    └── maven-status/           # Các tệp trạng thái Maven
```

## Giải Thích Kiến Trúc Ứng Dụng

### 1. Lớp Domain (`com.shopapp.domain.model`)
- Chứa các lớp thực thể JPA repräsent các đối tượng kinh doanh
- Mỗi thực thể ánh xạ đến một bảng trong cơ sở dữ liệu
- Sử dụng các annotations Hibernate để ánh xạ ORM
- Chứa các trường, getter, setter và mối quan hệ

### 2. Lớp Repository (`com.shopapp.repository`)
- Xác định các giao diện truy cập dữ liệu (mẫu DAO)
- Khai báo các thao tác CRUD và các phương thức truy vấn
- Các giao diện xác định hợp đồng cho truy cập dữ liệu
- Tách logic truy cập dữ liệu ra khỏi logic nghiệp vụ

### 3. Triển Khai Repository (`com.shopapp.repository.impl`)
- Các triển khai bộ nhớ trong để mục đích giải thích
- Sử dụng các bộ sưu tập Java (CopyOnWriteArrayList để an toàn luồng)
- Trong một ứng dụng thực tế, những triển khai này sẽ được thay thế bằng triển khai JPA/Hibernate
- Cung cấp các triển khai cụ thể của các giao diện repository

### 4. Lớp Dịch Vụ (`com.shopapp.service`)
- Xác định các giao diện logic nghiệp vụ
- Chứa các hoạt động cụ thể theo trường hợp sử dụng
- Điều phối các thao tác repository
- Khai báo ranh giới giao dịch (trong một triển khai JPA thực tế)

### 5. Triển Khai Dịch Vụ (`com.shopapp.service.impl`)
- Triển khai logic nghiệp vụ
- Ủy nhiệm cho các triển khai repository
- Không chứa mã cụ thể liên quan đến giao diện người dùng
- Có thể được kiểm thử đơn vị độc lập với giao diện người dùng

### 6. Lớp Giao Diện Người Dùng (`com.shopapp.ui`)
- Giao diện người dùng dựa trên Swing
- Được tổ chức theo khung, bảng và thành phần
- Tuân theo mô hình MVC-like nơi giao diện người dùng tương tác với lớp dịch vụ
- Chứa quản lý chủ đề để có giao diện nhất quán
- Bao gồm các bộ lắng nghe sự kiện để xử lý tương tác người dùng

## Các Mẫu Thiết Kế Chính Được Sử Dụng

1. **Kiến Trúc Tập Lớp**: Tách biệt rõ ràng về trách nhiệm thành các lớp domain, repository, service và UI
2. **Mẫu Repository**: Trừu tượng hoá các cơ chế truy cập dữ liệu
3. **Lớp Dịch Vụ**: Đóng gói logic nghiệp vụ
4. **Tiêm Phụ thuộc**: Các dịch vụ nhận repository qua việc tiêm vào constructor
5. **Phân Đoạn Giao diện**: Các giao diện nhỏ, tập trung cho repository và dịch vụ
6. **Dịch vụ Đơn Hình**: Các phiên bản dịch vụ thường là các đơn hình trong ứng dụng

## Ví Dụ Luồng Dữ liệu

Khi người dùng thực hiện một hành động (ví dụ: lưu một sản phẩm mới):

1. Lớp UI (ví dụ: SanPhamPanel) thu thập dữ liệu đầu vào từ người dùng
2. UI gọi phương thức dịch vụ thích hợp (ví dụ: sanPhamService.save(sanPham))
3. Lớp dịch vụ xác thực dữ liệu và gọi repository (ví dụ: sanPhamRepository.save(sanPham))
4. Triển khai repository lưu dữ liệu (tập hợp bộ nhớ trong hoặc cơ sở dữ liệu)
5. Lớp dịch vụ trả kết quả về UI
6. UI cập nhật hiển thị dựa trên kết quả

## Lưu Ý Về Triển khai Hiện Tại

- Các triển khai repository hiện tại là bộ nhớ trong để mục đích giải thích
- Trong môi trường sản xuất, những triển khai này sẽ được thay thế bằng Spring Data JPA hoặc Hibernate implementations
- Cấu hình Hibernate có sẵn trong `src/main/resources/hibernate.cfg.xml`
- Ứng dụng sử dụng framework Swing cho giao diện người dùng
- Maven được sử dụng để quản lý phụ thuộc và xây dựng

## Mở Rộng Ứng dụng

Để thêm thực thể/mô-đun mới:
1. Tạo lớp thực thể trong `domain/model/`
2. Tạo giao diện repository trong `repository/`
3. Tạo giao diện dịch vụ trong `service/`
4. Triển khai dịch vụ trong `service/impl/`
5. Tạo thành phần UI trong `ui/frame/panels/`
6. Thêm điều hướng vào khung chính nếu cần
7. Cập nhật cấu hình Hibernate nếu sử dụng cơ sở dữ liệu thực
