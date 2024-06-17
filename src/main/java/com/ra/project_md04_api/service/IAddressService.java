package com.ra.project_md04_api.service;

import com.ra.project_md04_api.model.entity.Address;

import java.util.List;

public interface IAddressService {
    Address getAddressByID(Long addressId);
    Address addAddress(Address address);
    void deleteAddress(Long addressId);
    List<Address> getAllAddressByUserId(Long userId);
}
