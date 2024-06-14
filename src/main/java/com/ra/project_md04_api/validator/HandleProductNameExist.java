package com.ra.project_md04_api.validator;

import com.ra.project_md04_api.repository.IProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HandleProductNameExist implements ConstraintValidator<ProductNameExist, String> {
    private final IProductRepository productRepository;
    @Override
    public boolean isValid(String productName, ConstraintValidatorContext constraintValidatorContext) {
        return !productRepository.existsByProductName(productName);
    }
}
