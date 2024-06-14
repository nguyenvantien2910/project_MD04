package com.ra.project_md04_api.controller.common;

import com.ra.project_md04_api.model.entity.Category;
import com.ra.project_md04_api.model.entity.Product;
import com.ra.project_md04_api.service.ICategoryService;
import com.ra.project_md04_api.service.IProductService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<Category>> getEnableCategory() {
        return new ResponseEntity<>(categoryService.findEnableCategory(), HttpStatus.OK);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> findProductByKeyword(@RequestBody String keyword) {
        return new ResponseEntity<>(productService.findProductByProductNameAndDescription(keyword), HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> findProductById(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    @GetMapping("/products/categories/{categoryId}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable Long categoryId) {
        return new ResponseEntity<>(productService.findProductByCategoryCategoryId(categoryId), HttpStatus.OK);
    }
}
