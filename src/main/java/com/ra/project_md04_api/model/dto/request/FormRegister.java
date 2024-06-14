package com.ra.project_md04_api.model.dto.request;

import com.ra.project_md04_api.validator.PhoneExist;
import com.ra.project_md04_api.validator.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormRegister {
    @NotBlank(message = "Fullname must be not blank")
    private String fullName;
    @NotBlank(message = "Username must be not blank")
    @ValidUsername(message = "Username contain special characters or already existed !")
    private String username;
    @NotBlank(message = "Password must be not blank")
    private String password;
    @NotBlank(message = "Address must be not blank")
    private String address;
    @NotBlank(message = "Email must be not blank")
    private String email;
    @PhoneExist
    private String phone;
    private List<String> roles;
}
