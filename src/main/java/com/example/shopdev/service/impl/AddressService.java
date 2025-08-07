package com.example.shopdev.service.impl;

import com.example.shopdev.dto.req.AddressRequestDTO;
import com.example.shopdev.dto.res.AddressResponseDTO;
import com.example.shopdev.exception.ForbiddenException;
import com.example.shopdev.exception.ResourceNotFoundException;
import com.example.shopdev.model.Address;
import com.example.shopdev.model.User;
import com.example.shopdev.repository.IAddressRepository;
import com.example.shopdev.repository.IUserRepository;
import com.example.shopdev.security.jwt.JwtCache;
import com.example.shopdev.service.IAddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService implements IAddressService {
    private final IAddressRepository addressRepository;
    private final IUserRepository userRepository;
    private final JwtCache jwtCache;
    @Override
    public List<AddressResponseDTO> getAddressesByUserId() {
        List<Address> addresses = addressRepository.findAllByUserId(jwtCache.getCurrentUserId());
        return addresses.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponseDTO createAddress(AddressRequestDTO requestDTO) {
        User user = userRepository.findById(jwtCache.getCurrentUserId()).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        Address address = new Address();
        address.setReceiveName(requestDTO.getReceiveName());
        address.setPhone(requestDTO.getPhone());
        address.setAddress(requestDTO.getAddress());
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return convertToResponseDTO(savedAddress);
    }

    @Override
    public AddressResponseDTO updateAddress(Long addressId, AddressRequestDTO requestDTO) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));
        // check user xem có đúng address của mình không
        if (!address.getUser().getId().equals(jwtCache.getCurrentUserId())) {
            throw new ForbiddenException("You do not have permission to update this address.");
        }
        address.setReceiveName(requestDTO.getReceiveName());
        address.setPhone(requestDTO.getPhone());
        address.setAddress(requestDTO.getAddress());
        Address updatedAddress = addressRepository.save(address);
        return convertToResponseDTO(updatedAddress);
    }

    @Override
    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));
        if (!address.getUser().getId().equals(jwtCache.getCurrentUserId())) {
            throw new ForbiddenException("You do not have permission to delete this address.");
        }

        addressRepository.delete(address);
    }
    private AddressResponseDTO convertToResponseDTO(Address address) {
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setId(address.getId());
        dto.setReceiveName(address.getReceiveName());
        dto.setPhone(address.getPhone());
        dto.setAddress(address.getAddress());
        dto.setUserId(address.getUser().getId());
        return dto;
    }
}
