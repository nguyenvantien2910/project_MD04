package com.ra.project_md04_api.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormAddShoppingCart {
    private Long productId;
    private Integer quantity;
}
