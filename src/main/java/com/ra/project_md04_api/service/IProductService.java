package com.ra.project_md04_api.service;

import com.ra.project_md04_api.model.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    Product getProductById(Long proId);
    Product addProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(Long proId);
    Page<Product> getProductPaging(String searchName, Integer page, Integer perPage, String orderBy, String direction);
    List<Product> findProductByProductNameAndDescription(String keyword);
    List<Product> findProductByCategoryCategoryId(Long categoryId);
}
