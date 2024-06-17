package com.ra.project_md04_api.repository;

import com.ra.project_md04_api.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Long> {
    Boolean existsBySku(String sku);
    Boolean existsByProductName(String productName);

    @Query("select p from Product p where p.productName like concat('%',:productName,'%')")
    Page<Product> findProductByProductNameAndSorting (String productName, Pageable pageable);

    @Query("select p from Product p where p.productName like concat('%',:keyword,'%') or p.description like concat('%',:keyword,'%')")
    List<Product> findProductByProductNameAndDescription(String keyword);

    @Query("select p from Product p where p.category.categoryId = :categoryId")
    List<Product> findProductByCategoryCategoryId(Long categoryId);

    @Query("select OD.product from order_details OD")
    Page<Product> findProductsIsSaleAndSorting (Pageable pageable);


}
