package com.ra.project_md04_api.validator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
@Documented
@Constraint(validatedBy = HandleValidUserName.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    String message() default "Invalid username!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
