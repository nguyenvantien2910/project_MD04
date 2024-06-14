package com.ra.project_md04_api.controller;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.FormLogin;
import com.ra.project_md04_api.model.dto.request.FormRegister;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody FormLogin formLogin) throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(authService.handleLogin(formLogin))
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody FormRegister formRegister) throws CustomException {
        authService.handleRegister(formRegister);
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.CREATED.value())
                        .data("Register successfully!")
                        .build(),
                HttpStatus.CREATED
        );
    }
}
