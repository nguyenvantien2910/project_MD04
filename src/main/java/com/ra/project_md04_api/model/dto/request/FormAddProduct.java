package com.ra.project_md04_api.model.dto.request;

import com.ra.project_md04_api.validator.ProductNameExist;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormAddProduct {

    @NotBlank(message = "Product name must be not blank")
    @Size(max = 100, message = "Max length of product name is 100 characters!")
    @ProductNameExist
    private String productName;

    private String description;

    @NotNull(message = "UnitPrice must be not null")
    private Double unitPrice;

    @NotNull(message = "New password must be not null")
    @Min(value = 0, message = "Quantity must be greater than 0 !")
    private Integer stockQuantity;

    private String image;

    @NotNull(message = "New password must be not null")
    private Long categoryId;

}
