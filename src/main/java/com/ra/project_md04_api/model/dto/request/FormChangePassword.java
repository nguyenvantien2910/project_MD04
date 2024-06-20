package com.ra.project_md04_api.model.dto.request;

import com.ra.project_md04_api.validator.ConfirmPasswordMatching;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormChangePassword {
    @NotBlank(message = "Old password must be not blank")
    private String oldPassword;

    @NotBlank(message = "New password must be not blank")
//    @ConfirmPasswordMatching(password = "newPassword", confirmPassword = "confirmNewPassword")
    private String newPassword;

    @NotBlank(message = "Confirm password must be not blank")
    private String confirmNewPassword;
}
