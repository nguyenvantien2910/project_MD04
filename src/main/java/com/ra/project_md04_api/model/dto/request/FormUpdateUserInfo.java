package com.ra.project_md04_api.model.dto.request;

import com.ra.project_md04_api.validator.PhoneExist;
import com.ra.project_md04_api.validator.ValidUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormUpdateUserInfo {
    @NotBlank(message = "Username must be not blank")
    @Size(min = 6, max = 100, message = "Username must be between 6 and 100 characters!")
    @ValidUsername
    private String username;

    @NotBlank(message = "Email must be not blank")
    @Pattern(regexp = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$", message = "Invalid email format!")
    private String email;

    @NotBlank(message = "FullName must be not blank")
    @Column(nullable = false, length = 100)
    private String fullName;

    private String avatar;

    @NotBlank(message = "Phone must be not blank")
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", message = "Invalid phone format!")
    @Max(value = 15,message = "Max characters is 15")
    @PhoneExist
    private String phone;

    @NotBlank(message = "Address must be not blank")
    private String address;
}
