package com.ra.project_md04_api.model.dto.request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CategoryFromRequest {
    @Size(max = 100, message = "Max length of category name is 100 characters")
    private String categoryName;

    private String description;

    @NotNull(message = "Status must be not null")
    private Boolean status;
}
