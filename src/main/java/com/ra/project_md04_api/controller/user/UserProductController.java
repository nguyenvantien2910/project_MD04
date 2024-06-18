package com.ra.project_md04_api.controller.user;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api.myservice.com/v1/products/")
@RequiredArgsConstructor
public class UserProductController {
    private final IProductService productService;

    @GetMapping("/new-products")
    public ResponseEntity<?> getNewProducts() {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.getNewProduct())
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/best-seller-products")
    public ResponseEntity<?> getBestSellerProducts() {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.getBestSellerProducts())
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/featured-products")
    public ResponseEntity<?> getFeaturedProducts() {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.getFeaturedProducts())
                        .build()
                , HttpStatus.OK);
    }
}
