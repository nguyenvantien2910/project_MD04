package com.ra.project_md04_api.controller.admin;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.model.entity.User;
import com.ra.project_md04_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestBody String searchName, Integer page, Integer perPage, String orderBy, String direction) {
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
    public ResponseEntity<?> deleteUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.deleteRoleFromUser(userId, roleId))
                        .build()
                , HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long userId) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.updateUserStatus(userId))
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUserByFullName(@RequestBody String searchName) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.findUserByFullName(searchName))
                        .build()
                , HttpStatus.OK);
    }
}
