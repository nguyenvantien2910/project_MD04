package com.ra.project_md04_api.controller.admin;

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
    public ResponseEntity<Page<User>> getUsers(@RequestBody String searchName, Integer page, Integer perPage, String orderBy, String direction) {
        return new ResponseEntity<>(userService.getUserPaging(searchName, page, perPage, orderBy, direction), HttpStatus.OK);
    }

    @PutMapping("/{userId}/role/{roleId}")
    public ResponseEntity<User> updateUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
        return new ResponseEntity<>(userService.updateRoleToUser(userId,roleId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/role/{roleId}")
    public ResponseEntity<User> deleteUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
        return new ResponseEntity<>(userService.deleteRoleFromUser(userId,roleId), HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.updateUserStatus(userId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUserByFullName(@RequestBody String searchName) {
        return new ResponseEntity<>(userService.findUserByFullName(searchName), HttpStatus.OK);
    }
}
