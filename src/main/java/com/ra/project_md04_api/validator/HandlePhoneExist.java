package com.ra.project_md04_api.validator;

import com.ra.project_md04_api.repository.IUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class HandlePhoneExist implements ConstraintValidator<PhoneExist, String> {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (userRepository == null) {
            return true;
        }
        return !userRepository.existsByPhone(phone);
    }
}
