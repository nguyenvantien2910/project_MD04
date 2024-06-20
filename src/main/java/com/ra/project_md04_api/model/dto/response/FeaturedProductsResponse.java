package com.ra.project_md04_api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FeaturedProductsResponse {
    private Long productId;
    private String sku;
    private String productName;
    private String description;
    private Double unitPrice;
    private Integer stockQuantity;
    private String image;
    private String categoryName;
    private Date createdAt;
    private Date updateAt;
    private Integer wishlistNumber;
}
