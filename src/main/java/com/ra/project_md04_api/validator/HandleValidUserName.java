package com.ra.project_md04_api.validator;

import com.ra.project_md04_api.repository.IUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
//@RequiredArgsConstructor
public class HandleValidUserName implements ConstraintValidator<ValidUsername, String> {

    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]*$";
    private static final Pattern pattern = Pattern.compile(USERNAME_PATTERN);
    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (userRepository == null) {
            return true;
        }
        return username != null && pattern.matcher(username).matches() && !userRepository.existsByUsername(username);
    }
}
