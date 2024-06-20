package com.ra.project_md04_api.controller.user;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.FormUpdateUserInfo;
import com.ra.project_md04_api.model.dto.request.FormChangePassword;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/user/account")
@RequiredArgsConstructor
public class UserInfoController {
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<?> getUserInfo() throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.getUserInfo(userService.getCurrentUserId()))
                        .build()
                , HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateUserInfo(@Valid @RequestBody FormUpdateUserInfo formUpdateUserInfo) throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.updateUserInfo(formUpdateUserInfo))
                        .build(),
                HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody FormChangePassword formChangePassword) throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.updatePassword(formChangePassword))
                        .build(),
                HttpStatus.OK);
    }


}
