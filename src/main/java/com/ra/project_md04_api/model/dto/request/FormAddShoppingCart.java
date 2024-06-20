package com.ra.project_md04_api.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormAddShoppingCart {
    @NotNull(message = "Product Id must be not null")
    private Long productId;
    @NotNull(message = "Quantity must be not null")
    private Integer quantity;
}
