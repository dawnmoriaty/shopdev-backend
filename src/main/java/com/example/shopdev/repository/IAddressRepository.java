package com.example.shopdev.repository;

import com.example.shopdev.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByUserId(Long userId);
}
