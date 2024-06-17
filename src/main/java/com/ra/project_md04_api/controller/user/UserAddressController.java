package com.ra.project_md04_api.controller.user;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.model.entity.Address;
import com.ra.project_md04_api.service.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api.myservice.com/v1/user/account/addresses")
@RequiredArgsConstructor
public class UserAddressController {
    private final IAddressService addressService;

    @GetMapping
    public ResponseEntity<?> getAllAddressesByUserId(@RequestBody Long userId) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(addressService.getAllAddressByUserId(userId))
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<?> getAddressById(@PathVariable Long addressId) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(addressService.getAddressByID(addressId))
                        .build()
                , HttpStatus.OK);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> deleteAddressById(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data("Delete successfully!")
                        .build()
                , HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addNewAddress(@RequestBody Address newAddress) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(addressService.addAddress(newAddress))
                        .build()
                , HttpStatus.OK);
    }

}
