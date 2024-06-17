package com.ra.project_md04_api.service;

import com.ra.project_md04_api.model.dto.request.FormAddProduct;
import com.ra.project_md04_api.model.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    Product getProductById(Long proId);
    Product addProduct(FormAddProduct formAddProduct);
    Product updateProduct(FormAddProduct formAddProduct,Long productId);
    void deleteProduct(Long proId);
    Page<Product> getProductPaging(String searchName, Integer page, Integer perPage, String orderBy, String direction);
    List<Product> findProductByProductNameAndDescription(String keyword);
    List<Product> findProductByCategoryCategoryId(Long categoryId);
    Page<Product> getProductsIsSalePaging(Integer page, Integer perPage, String orderBy, String direction);
}
