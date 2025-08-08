package com.example.shopdev.controller;

import com.example.shopdev.dto.req.AddressRequestDTO;
import com.example.shopdev.dto.res.AddressResponseDTO;
import com.example.shopdev.dto.res.ApiResponse;
import com.example.shopdev.service.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService addressService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponseDTO>>> getAllAddresses() {
        List<AddressResponseDTO> addresses = addressService.getAddressesByUserId();

        ApiResponse<List<AddressResponseDTO>> response = ApiResponse.<List<AddressResponseDTO>>builder()
                .success(true)
                .status(200)
                .message("Lấy danh sách địa chỉ thành công")
                .data(addresses)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponseDTO>> createAddress(@Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressResponseDTO addressResponseDTO = addressService.createAddress(addressRequestDTO);

        ApiResponse<AddressResponseDTO> response = ApiResponse.<AddressResponseDTO>builder()
                .success(true)
                .status(201)
                .message("Tạo địa chỉ thành công")
                .data(addressResponseDTO)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> updateAddress(
            @PathVariable("id") Long id,
            @Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressResponseDTO updatedAddress = addressService.updateAddress(id, addressRequestDTO);

        ApiResponse<AddressResponseDTO> response = ApiResponse.<AddressResponseDTO>builder()
                .success(true)
                .status(200)
                .message("Cập nhật địa chỉ thành công")
                .data(updatedAddress)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable("id") Long id) {
        addressService.deleteAddress(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .status(200)
                .message("Xóa địa chỉ thành công")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}