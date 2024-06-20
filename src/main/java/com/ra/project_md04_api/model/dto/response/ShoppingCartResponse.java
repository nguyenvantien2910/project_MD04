package com.ra.project_md04_api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShoppingCartResponse {
    private Long shoppingCartId;
    private Long productId;
    private String productName;
    private Long userId;
    private String fullName;
    private Integer orderQuantity;
}
