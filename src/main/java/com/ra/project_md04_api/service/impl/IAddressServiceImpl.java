package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.FormAddAddress;
import com.ra.project_md04_api.model.dto.response.UserAddressResponse;
import com.ra.project_md04_api.model.entity.Address;
import com.ra.project_md04_api.repository.IAddressRepository;
import com.ra.project_md04_api.repository.IUserRepository;
import com.ra.project_md04_api.service.IAddressService;
import com.ra.project_md04_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IAddressServiceImpl implements IAddressService {
    private final IAddressRepository addressRepository;
    private final IUserRepository userRepository;
    private final IUserService userService;

    @Override
    public Address getAddressByID(Long addressId) throws CustomException {
        long userId = userService.getCurrentUserId();
        Address address = addressRepository.findByAddressId(addressId)
                .orElseThrow(() -> new CustomException("Address Not Found: " + addressId, HttpStatus.NOT_FOUND));
        if (userId != address.getUser().getUserId()) {
            throw new CustomException("User don't have any address with id : " + addressId, HttpStatus.FORBIDDEN);
        } else {
            return address;
        }
    }

    @Override
    public Address addAddress(FormAddAddress formAddAddress) throws CustomException {
        Address address = Address.builder()
                .fullAddress(formAddAddress.getFullAddress())
                .phoneNumber(formAddAddress.getPhoneNumber())
                .receiveName(formAddAddress.getReceiveName())
                .user(
                        userRepository.findById(userService.getCurrentUserId())
                                .orElseThrow(() -> new CustomException("User Not Found", HttpStatus.NOT_FOUND))
                )
                .build();
        try {
            return addressRepository.save(address);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId) throws CustomException {
        Address address = addressRepository.findByAddressId(addressId)
                .orElseThrow(() -> new CustomException("Address Not Found: " + addressId, HttpStatus.NOT_FOUND));

        Long userId = userService.getCurrentUserId();
        if (address.getUser().getUserId().equals(userId)) {
            addressRepository.delete(address);
        } else {
            throw new CustomException("Address Id doesn't belong to user", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<UserAddressResponse> getAllAddressByUserId() throws CustomException {
        Long userId = userService.getCurrentUserId();
        try {
            List<Address> addresses = addressRepository.findAllByUserUserId(userId);
            if (addresses.isEmpty()) {
                throw new CustomException("User doesn't have any address", HttpStatus.NOT_FOUND);
            }
            return addresses.stream()
                    .map(address -> UserAddressResponse.builder()
                            .addressId(address.getAddressId())
                            .fullAddress(address.getFullAddress())
                            .phoneNumber(address.getPhoneNumber())
                            .receiveName(address.getReceiveName())
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
