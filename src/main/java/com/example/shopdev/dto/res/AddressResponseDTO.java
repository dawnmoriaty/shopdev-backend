package com.example.shopdev.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressResponseDTO {
    private Long id;
    private String receiveName;
    private String phone;
    private String address;
    private Long userId;
}
