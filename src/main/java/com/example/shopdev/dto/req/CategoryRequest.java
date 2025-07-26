package com.example.shopdev.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(min = 2, max = 100, message = "Tên danh mục phải từ 2 đến 100 ký tự")
    private String name;
    @NotBlank(message = "Mô tả danh mục không được để trống")
    @Size(min = 2, max = 100, message = "Mô tả từ 2 đến 100 ký tự")
    private String description;
    @NotNull
    private Boolean status;
}
