package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.model.dto.request.FormAddProduct;
import com.ra.project_md04_api.model.entity.Category;
import com.ra.project_md04_api.model.entity.Product;
import com.ra.project_md04_api.repository.IProductRepository;
import com.ra.project_md04_api.service.ICategoryService;
import com.ra.project_md04_api.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IProductServiceImpl implements IProductService {
    private final IProductRepository productRepository;
    private final ICategoryService categoryService;

    @Override
    public Product getProductById(Long proId) {
        return productRepository.findById(proId).orElseThrow(() -> new RuntimeException("Product not found" + proId));
    }

    @Override
    public Product addProduct(FormAddProduct formAddProduct) {
        Product product = Product.builder()
                .productName(formAddProduct.getProductName())
                .sku(UUID.randomUUID().toString())
                .image(formAddProduct.getImage())
                .description(formAddProduct.getDescription())
                .stockQuantity(formAddProduct.getStockQuantity())
                .unitPrice(formAddProduct.getUnitPrice())
                .category(categoryService.getCategoryById(formAddProduct.getCategoryId()))
                .createdAt(new Date())
                .updateAt(null)
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(FormAddProduct formAddProduct,Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found" + productId));

        product.setProductName(formAddProduct.getProductName());
        product.setDescription(formAddProduct.getDescription());
        product.setStockQuantity(formAddProduct.getStockQuantity());
        product.setUnitPrice(formAddProduct.getUnitPrice());
        product.setCategory(categoryService.getCategoryById(formAddProduct.getCategoryId()));
        product.setUpdateAt(new Date());

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long proId) {
        productRepository.findById(proId).orElseThrow(() -> new RuntimeException("Product not found" + proId));
        productRepository.deleteById(proId);
    }

    @Override
    public Page<Product> getProductPaging(String searchName, Integer page, Integer perPage, String orderBy, String direction) {
        Pageable pageable = null;

        if (orderBy != null && !orderBy.isEmpty()) {
            // co sap xep
            Sort sort = null;
            switch (direction) {
                case "ASC":
                    sort = Sort.by(orderBy).ascending();
                    break;
                case "DESC":
                    sort = Sort.by(orderBy).descending();
                    break;
            }
            pageable = PageRequest.of(page, perPage, sort);
        } else {
            //khong sap xep
            pageable = PageRequest.of(page, perPage);
        }

        //xu ly ve tim kiem
        if (searchName != null && !searchName.isEmpty()) {
            //co tim kiem
            return productRepository.findProductByProductNameAndSorting(searchName, pageable);
        } else {
            //khong tim kiem
            return productRepository.findAll(pageable);
        }
    }

    @Override
    public List<Product> findProductByProductNameAndDescription(String keyword) {
        List<Product> products = productRepository.findProductByProductNameAndDescription(keyword);
        if (products.isEmpty()) {
            log.error("Product not found by keyword: {}", keyword);
        }
        return products;
    }

    @Override
    public List<Product> findProductByCategoryCategoryId(Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            log.error("Category not found by categoryId: {}", categoryId);
            return Collections.emptyList();
        } else {
            if (!category.getStatus()) {
                log.error("Category status is not correct");
            } else {
                return productRepository.findProductByCategoryCategoryId(categoryId);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Page<Product> getProductsIsSalePaging(Integer page, Integer perPage, String orderBy, String direction) {
        Pageable pageable = null;

        if (orderBy != null && !orderBy.isEmpty()) {
            // co sap xep
            Sort sort = null;
            switch (direction) {
                case "ASC":
                    sort = Sort.by(orderBy).ascending();
                    break;
                case "DESC":
                    sort = Sort.by(orderBy).descending();
                    break;
            }
            pageable = PageRequest.of(page, perPage, sort);
        } else {
            //khong sap xep
            pageable = PageRequest.of(page, perPage);
        }

        return productRepository.findProductsIsSaleAndSorting(pageable);
    }
}
