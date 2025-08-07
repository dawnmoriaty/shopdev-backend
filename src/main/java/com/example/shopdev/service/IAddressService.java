package com.example.shopdev.service;

import com.example.shopdev.dto.req.AddressRequestDTO;
import com.example.shopdev.dto.res.AddressResponseDTO;

import java.util.List;

public interface IAddressService {

    List<AddressResponseDTO> getAddressesByUserId();

    AddressResponseDTO createAddress(AddressRequestDTO requestDTO);

    AddressResponseDTO updateAddress(Long addressId, AddressRequestDTO requestDTO);

    void deleteAddress(Long addressId);
}
