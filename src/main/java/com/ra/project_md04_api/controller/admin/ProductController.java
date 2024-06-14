package com.ra.project_md04_api.controller.admin;

import com.ra.project_md04_api.model.entity.Product;
import com.ra.project_md04_api.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getCategories(@RequestBody String searchName, Integer page, Integer perPage, String orderBy, String direction) {
        return new ResponseEntity<>(productService.getProductPaging(searchName, page, perPage, orderBy, direction), HttpStatus.OK);
    }

    @GetMapping("/{ProductId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long ProductId) {
        return new ResponseEntity<>(productService.getProductById(ProductId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> insertProduct(@RequestBody Product Product) {
        return new ResponseEntity<>(productService.addProduct(Product), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestBody Product Product) {
        return new ResponseEntity<>(productService.updateProduct(Product), HttpStatus.OK);
    }

    @DeleteMapping("/{ProductId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long ProductId) {
        productService.deleteProduct(ProductId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
