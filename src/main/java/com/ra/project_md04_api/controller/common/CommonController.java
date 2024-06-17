package com.ra.project_md04_api.controller.common;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.model.entity.Category;
import com.ra.project_md04_api.model.entity.Product;
import com.ra.project_md04_api.service.ICategoryService;
import com.ra.project_md04_api.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1")
@RequiredArgsConstructor
public class CommonController {
    private final ICategoryService categoryService;
    private final IProductService productService;

    @GetMapping("/categories")
    public ResponseEntity<?> getEnableCategory() {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(categoryService.findEnableCategory())
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/products/search")
    public ResponseEntity<?> findProductByKeyword(@RequestBody String keyword) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.findProductByProductNameAndDescription(keyword))
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> findProductById(@PathVariable Long productId) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.getProductById(productId))
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/products/categories/{categoryId}")
    public ResponseEntity<?> getProductByCategory(@PathVariable Long categoryId) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.findProductByCategoryCategoryId(categoryId))
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProductsIsSale(@RequestBody Integer page, Integer perPage, String orderBy, String direction) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.getProductsIsSalePaging(page, perPage, orderBy, direction))
                        .build()
                , HttpStatus.OK);
    }
}
