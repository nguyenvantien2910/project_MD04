package com.ra.project_md04_api.model.dto.request;

import com.ra.project_md04_api.validator.ProductNameExist;
import com.ra.project_md04_api.validator.SkuExist;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormAddProduct {

    @Max(value = 100, message = "Max lenght of product name is 100 characters!")
    @ProductNameExist
    private String productName;

    private String description;

    @NotBlank(message = "UnitPrice must be not blank")
    private Double unitPrice;

    @NotBlank(message = "New password must be not blank")
    @Min(value = 0, message = "Quantity must be greater than 0 !")
    private Integer stockQuantity;

    private String image;

    @NotEmpty(message = "New password must be not empty")
    private Long categoryId;

}
