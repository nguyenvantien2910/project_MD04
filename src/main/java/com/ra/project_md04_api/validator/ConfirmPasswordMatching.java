package com.ra.project_md04_api.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HandleConfirmPasswordMatching.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmPasswordMatching {
    String password();

    String confirmPassword();

    String message() default "Confirm password not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
