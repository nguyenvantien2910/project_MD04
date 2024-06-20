package com.ra.project_md04_api.service;

import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.FormAddAddress;
import com.ra.project_md04_api.model.dto.response.UserAddressResponse;
import com.ra.project_md04_api.model.entity.Address;

import java.util.List;

public interface IAddressService {
    Address getAddressByID(Long addressId) throws CustomException;
    Address addAddress(FormAddAddress formAddAddress) throws CustomException;
    void deleteAddress(Long addressId) throws CustomException;
    List<UserAddressResponse> getAllAddressByUserId() throws CustomException;
}
