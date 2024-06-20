package com.ra.project_md04_api.validator;

import com.ra.project_md04_api.repository.IProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HandleSkuExist implements ConstraintValidator<SkuExist,String> {
    private final IProductRepository productRepository;
    @Override
    public boolean isValid(String sku, ConstraintValidatorContext constraintValidatorContext) {
        return !productRepository.existsBySku(sku);
    }
}
