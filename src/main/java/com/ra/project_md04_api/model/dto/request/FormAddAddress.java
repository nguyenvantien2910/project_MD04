package com.ra.project_md04_api.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormAddAddress {
    @NotEmpty(message = "Full address must be not empty")
    @Size(max = 255, message = "Max character is 255 !")
    private String fullAddress;

    @NotEmpty(message = "Phone must be not empty")
    @Size(max = 15, message = "Max character is 15 !")
    private String phoneNumber;

    @NotEmpty(message = "Receive name must be not empty")
    @Size(max = 50, message = "Max character is 50 !")
    private String receiveName;
}
