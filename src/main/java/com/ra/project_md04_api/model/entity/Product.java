package com.ra.project_md04_api.model.entity;

import com.ra.project_md04_api.validator.ProductNameExist;
import com.ra.project_md04_api.validator.SkuExist;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false,unique = true)
    private Long productId;

    @Column(unique = true)
    @Max(value = 100,message = "Max lenght of sku is 100 characters!")
    @SkuExist
    private String sku;

    @Column(unique = true)
    @Max(value = 100,message = "Max lenght of product name is 100 characters!")
    @ProductNameExist
    private String productName;

    private String description;

    @Column(columnDefinition = "Decimal(10,2)")
    private Double unitPrice;

    @Min(value = 0,message = "Quantity must be greater than 0 !")
    private Integer stockQuantity;

    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "category_id")
    private Category category;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateAt;
}
