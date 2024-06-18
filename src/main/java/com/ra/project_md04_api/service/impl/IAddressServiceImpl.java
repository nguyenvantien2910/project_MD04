package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.model.entity.Address;
import com.ra.project_md04_api.repository.IAddressRepository;
import com.ra.project_md04_api.repository.IUserRepository;
import com.ra.project_md04_api.service.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class IAddressServiceImpl implements IAddressService {
    private final IAddressRepository addressRepository;
    private final IUserRepository userRepository;

    @Override
    public Address getAddressByID(Long addressId) {
        return addressRepository.findByAddressId(addressId).orElseThrow(() -> new NoSuchElementException("Invalid addressId" + addressId));
    }

    @Override
    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId) {
        addressRepository.delete(addressRepository.findByAddressId(addressId).orElseThrow(() -> new NoSuchElementException("Invalid addressId: " + addressId)));
    }

    @Override
    public List<Address> getAllAddressByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("Invalid userId" + userId));
        List<Address> addresses = addressRepository.findAllByUserUserId(userId);
        if (addresses.isEmpty()) {
            throw new NoSuchElementException("User don't have any address");
        } else {
            return addresses;
        }
    }
}
