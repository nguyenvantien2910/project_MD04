package com.ra.project_md04_api.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RevenueRequest {
    @NotNull(message = "From date cannot be null")
    @Past(message = "From date must be in the past")
    private Date from;

    @NotNull(message = "To date cannot be null")
    @Past(message = "To date must be in the past")
    private Date to;
}
