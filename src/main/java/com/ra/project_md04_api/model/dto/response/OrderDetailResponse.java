package com.ra.project_md04_api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDetailResponse {
    private Long orderDetailId;
    private Long productId;
    private String productName;
    private Double unitPrice;
    private Integer orderQuantity;
}
