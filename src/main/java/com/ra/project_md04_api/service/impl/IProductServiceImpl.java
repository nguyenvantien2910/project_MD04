package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.model.entity.Category;
import com.ra.project_md04_api.model.entity.Product;
import com.ra.project_md04_api.repository.ICategoryRepository;
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
import java.util.List;

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
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        productRepository.findById(product.getProductId()).orElseThrow(() -> new RuntimeException("Product not found" + product.getProductName()));
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
            return productRepository.findProductByCategoryCategoryId(categoryId);
        }
    }

}
