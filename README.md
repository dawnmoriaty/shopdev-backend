# ShopDev Backend - E-commerce API

Đây là mã nguồn backend cho dự án thương mại điện tử ShopDev, cung cấp một hệ thống API RESTful để quản lý sản phẩm, danh mục, đơn hàng, và người dùng.

## ✨ Tính năng chính

-   **Xác thực & Phân quyền**: Đăng ký, đăng nhập cho người dùng. Sử dụng **JWT (JSON Web Tokens)** và **Spring Security** để bảo vệ các API và phân quyền truy cập (ROLE_ADMIN, ROLE_USER).
-   **Quản lý Sản phẩm**: Các API CRUD (Tạo, Đọc, Cập nhật, Xóa) cho sản phẩm.
-   **Quản lý Danh mục**: Các API CRUD cho danh mục sản phẩm.
-   **Quản lý Giỏ hàng**: Thêm, sửa, xóa sản phẩm trong giỏ hàng cho người dùng đã đăng nhập.
-   **Quản lý Đơn hàng**:
    -   **Người dùng**: Đặt hàng từ giỏ hàng, xem lịch sử đơn hàng, hủy đơn hàng.
    -   **Admin**: Quản lý tất cả đơn hàng, cập nhật trạng thái đơn hàng.
-   **Quản lý Địa chỉ**: Người dùng có thể quản lý danh sách địa chỉ giao hàng của mình.
-   **Tải ảnh**: API để tải ảnh sản phẩm lên **Firebase Cloud Storage**.

## 🚀 Công nghệ sử dụng

-   **Ngôn ngữ**: Java 21
-   **Framework**: Spring Boot 3
-   **Bảo mật**: Spring Security, JWT
-   **Cơ sở dữ liệu**: MySQL
-   **ORM**: Spring Data JPA / Hibernate
-   **Build Tool**: Gradle
-   **Validation**: Jakarta Bean Validation

## 🔧 Cài đặt và Chạy dự án

1.  **Clone repository:**
    ```bash
    git clone <URL_CUA_REPOSITORY>
    cd shopdev-backend
    ```

2.  **Cấu hình cơ sở dữ liệu:**
    -   Mở file `src/main/resources/application.properties`.
    -   Thay đổi các thông tin kết nối tới MySQL của bạn:
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/shopdev?createDatabaseIfNotExist=true
        spring.datasource.username=root
        spring.datasource.password=yourpassword
        ```

3.  **Cấu hình Firebase:**
    -   Tải file JSON chứa credentials của Firebase Service Account.
    -   Đặt file JSON vào thư mục `src/main/resources`.
    -   Cập nhật đường dẫn file trong `application.properties`:
        ```properties
        firebase.credentials.path=classpath:your-firebase-adminsdk-file.json
        ```

4.  **Chạy ứng dụng:**
    -   Sử dụng Gradle Wrapper để build và chạy:
        ```bash
        ./gradlew bootRun
        ```
    -   Hoặc build file JAR và chạy:
        ```bash
        ./gradlew build
        java -jar build/libs/shopdev-0.0.1-SNAPSHOT.jar
        ```

5.  **API Server** sẽ chạy tại `http://localhost:8080`.
