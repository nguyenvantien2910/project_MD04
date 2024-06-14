package com.ra.project_md04_api.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HandleProductNameExist.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProductNameExist {
    String message() default "Product name already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
