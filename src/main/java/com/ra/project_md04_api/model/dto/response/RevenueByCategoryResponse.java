package com.ra.project_md04_api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RevenueByCategoryResponse {
    private Long categoryId;
    private String categoryName;
    private Double totalRevenue;
}
