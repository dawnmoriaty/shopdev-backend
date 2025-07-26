package com.example.shopdev.service;

import com.example.shopdev.dto.req.ProductRequest;
import com.example.shopdev.dto.res.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    /**
     * Tạo một sản phẩm mới.
     * @param request DTO chứa thông tin sản phẩm mới.
     * @return DTO chứa thông tin sản phẩm đã được tạo.
     */
    ProductResponse createProduct(ProductRequest request);

    /**
     * Cập nhật thông tin một sản phẩm đã có.
     * @param productId ID của sản phẩm cần cập nhật.
     * @param request DTO chứa thông tin mới.
     * @return DTO chứa thông tin sản phẩm sau khi cập nhật.
     */
    ProductResponse updateProduct(Long productId, ProductRequest request);

    /**
     * Lấy thông tin một sản phẩm theo ID.
     * @param productId ID của sản phẩm cần lấy.
     * @return DTO chứa thông tin sản phẩm.
     */
    ProductResponse getProductById(Long productId);

    /**
     * Lấy danh sách tất cả sản phẩm.
     * @return List các DTO sản phẩm.
     */
    List<ProductResponse> getAllProducts();

    /**
     * Xóa một sản phẩm.
     * @param productId ID của sản phẩm cần xóa.
     */
    void deleteProduct(Long productId);
    List<ProductResponse> getProductsByCategoryId(Long categoryId);
}
