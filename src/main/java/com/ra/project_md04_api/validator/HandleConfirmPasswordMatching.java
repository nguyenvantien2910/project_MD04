package com.ra.project_md04_api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class HandleConfirmPasswordMatching implements ConstraintValidator<ConfirmPasswordMatching, String> {
    private String password;
    private String confirmPassword;

    @Override
    public void initialize(ConfirmPasswordMatching matching) {
        password = matching.password();
        confirmPassword = matching.confirmPassword();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Object passwordValue = new BeanWrapperImpl(s).getPropertyValue(password);
        Object confirmPasswordValue = new BeanWrapperImpl(s).getPropertyValue(confirmPassword);
        return Objects.equals(passwordValue,confirmPasswordValue);
    }
}
