package com.ra.project_md04_api.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HandlePhoneExist.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneExist {
    String message() default "Phone already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
