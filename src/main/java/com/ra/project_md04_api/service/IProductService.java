package com.ra.project_md04_api.service;

import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.FormAddProduct;
import com.ra.project_md04_api.model.dto.request.RevenueRequest;
import com.ra.project_md04_api.model.dto.response.FeaturedProductsResponse;
import com.ra.project_md04_api.model.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    Product getProductById(Long proId) throws CustomException;
    Product addProduct(FormAddProduct formAddProduct) throws CustomException;
    Product updateProduct(FormAddProduct formAddProduct,Long productId) throws CustomException;
    void deleteProduct(Long proId) throws CustomException;
    Page<Product> getProductPaging(String searchName, Integer page, Integer perPage, String orderBy, String direction);
    List<Product> findProductByProductNameAndDescription(String keyword) throws CustomException;
    List<Product> findProductByCategoryCategoryId(Long categoryId) throws CustomException;
    Page<Product> getProductsIsSalePaging(Integer page, Integer perPage, String orderBy, String direction);
    List<Product> getNewProduct() throws CustomException;
    List<Product> getBestSellerProducts() throws CustomException;
    List<FeaturedProductsResponse> getFeaturedProducts() throws CustomException;
    List<Product> getBestSellerProductsFromAndTo(RevenueRequest revenueRequest) throws CustomException;
}
