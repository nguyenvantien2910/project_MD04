package com.ra.project_md04_api.validator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HandleSkuExist.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SkuExist {
    String message() default "Sku already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
