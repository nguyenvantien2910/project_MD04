package com.ra.project_md04_api.controller.admin;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam String searchName, Integer page, Integer perPage, String orderBy, String direction) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.getUserPaging(searchName, page, perPage, orderBy, direction))
                        .build()
                , HttpStatus.OK);
    }

    @PutMapping("/{userId}/role/{roleId}")
    public ResponseEntity<?> updateUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.updateRoleToUser(userId, roleId))
                        .build()
                , HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/role/{roleId}")
    public ResponseEntity<?> deleteUserRole(@PathVariable Long userId, @PathVariable Long roleId) throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.deleteRoleFromUser(userId, roleId))
                        .build()
                , HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long userId) throws CustomException {
        userService.updateUserStatus(userId);
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data("Change user status successfully")
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUserByFullName(@RequestParam String searchName) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.findUserByFullName(searchName))
                        .build()
                , HttpStatus.OK);
    }
}
