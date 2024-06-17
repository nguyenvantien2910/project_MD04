package com.ra.project_md04_api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormLogin {
    @NotBlank(message = "Username must be not blank")
    private String username;
    @NotBlank(message = "Password must be not blank")
    private String password;
}
