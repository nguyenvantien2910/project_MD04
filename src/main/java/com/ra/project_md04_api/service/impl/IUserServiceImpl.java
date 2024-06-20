package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.constants.RoleName;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.FormUpdateUserInfo;
import com.ra.project_md04_api.model.dto.request.FormChangePassword;
import com.ra.project_md04_api.model.dto.response.UserInfoResponse;
import com.ra.project_md04_api.model.entity.Role;
import com.ra.project_md04_api.model.entity.User;
import com.ra.project_md04_api.repository.IRoleRepository;
import com.ra.project_md04_api.repository.IUserRepository;
import com.ra.project_md04_api.security.principals.CustomUserDetail;
import com.ra.project_md04_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IUserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<User> getUserPaging(String searchName, Integer page, Integer perPage, String orderBy, String direction) {
        Pageable pageable = null;

        if (orderBy != null && !orderBy.isEmpty()) {
            // co sap xep
            Sort sort = null;
            switch (direction) {
                case "ASC":
                    sort = Sort.by(orderBy).ascending();
                    break;
                case "DESC":
                    sort = Sort.by(orderBy).descending();
                    break;
            }
            pageable = PageRequest.of(page, perPage, sort);
        } else {
            //khong sap xep
            pageable = PageRequest.of(page, perPage);
        }

        //xu ly ve tim kiem
        if (searchName != null && !searchName.isEmpty()) {
            //co tim kiem
            return userRepository.findUserByUsernameAndSorting(searchName, pageable);
        } else {
            //khong tim kiem
            return userRepository.findAll(pageable);
        }
    }

    @Override
    public User updateRoleToUser(Long userId, Long roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isPresent()) {
            Role updateRole = optionalRole.get();
            User updateUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));
            if (!updateUser.getRoles().contains(updateRole)) {
                updateUser.getRoles().add(updateRole);
                return userRepository.save(updateUser);
            } else {
                log.error("Role already exists in user: {}", updateUser.getFullName());
            }
        } else {
            throw new RuntimeException("Role not found with ID: " + roleId);
        }
        return null;
    }

    @Override
    public User deleteRoleFromUser(Long userId, Long roleId) throws CustomException {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isPresent()) {
            Role updateRole = optionalRole.get();
            User updateUser = userRepository.findById(userId).orElseThrow(() -> new CustomException("User not found: " + userId,HttpStatus.BAD_REQUEST));
            if (updateUser.getRoles().contains(updateRole)) {
                updateUser.getRoles().remove(updateRole);
                return userRepository.save(updateUser);
            } else {
                throw new CustomException("Role does not exist in user: " + updateUser.getFullName(),HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new CustomException("Role not found with ID: " + roleId,HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public void updateUserStatus(Long userId) throws CustomException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(role -> role.getRoleName() == RoleName.ROLE_ADMIN);
            if (isAdmin) {
                throw new CustomException("Cannot update status for user with role ROLE_ADMIN: " + userId, HttpStatus.FORBIDDEN);
            }
            user.setStatus(!user.getStatus());
            userRepository.save(user);
        } else {
            throw new CustomException("User not found with ID: " + userId,HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public List<User> findUserByFullName(String searchName) {
        List<User> usersFilteredBySearchName = userRepository.findAll()
                .stream()
                .filter(user -> user.getFullName().toLowerCase().contains(searchName.toLowerCase()))
                .toList();

        if (usersFilteredBySearchName.isEmpty()) {
            log.error("User not found by search name: {}", searchName);
        }

        return usersFilteredBySearchName;
    }

    @Override
    public Long getCurrentUserId() {
        CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customUserDetail.getUserId();
    }

    @Override
    public UserInfoResponse getUserInfo(Long userId) throws CustomException {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException("User not found with ID: " + userId, HttpStatus.BAD_REQUEST));

        return UserInfoResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .address(user.getAddress())
                .avatar(user.getAvatar())
                .build();
    }

    @Override
    public User updateUserInfo(FormUpdateUserInfo formUpdateUserInfo) throws CustomException {
        Long currentUserId = getCurrentUserId();
        //Lay user tuong ung tu DB
        User oldUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new CustomException("User not found with ID: " + currentUserId, HttpStatus.BAD_REQUEST));

        //gan gia tri thay doi
        if (!oldUser.getUsername().equals(formUpdateUserInfo.getUsername())) {
            oldUser.setUsername(formUpdateUserInfo.getUsername());
        }
        if (!oldUser.getPhone().equals(formUpdateUserInfo.getPhone())) {
            oldUser.setFullName(formUpdateUserInfo.getFullName());
        }
        if (!oldUser.getEmail().equals(formUpdateUserInfo.getEmail())) {
            oldUser.setEmail(formUpdateUserInfo.getEmail());
        }
        if (!oldUser.getAddress().equals(formUpdateUserInfo.getAddress())) {
            oldUser.setAddress(formUpdateUserInfo.getAddress());
        }
        if (!oldUser.getAvatar().equals(formUpdateUserInfo.getAvatar())) {
            oldUser.setAvatar(formUpdateUserInfo.getAvatar());
        }
        oldUser.setUpdatedAt(new Date());

        //lưu lại vào db
        return userRepository.save(oldUser);
    }

    @Override
    public User updatePassword(FormChangePassword formChangePassword) throws CustomException {
        User user = userRepository.findById(getCurrentUserId()).orElseThrow(() -> new CustomException("User not found with ID: " + getCurrentUserId(),HttpStatus.BAD_REQUEST));

        // check xem PW cũ nhap co dung khong
        boolean passwordIsMatch = passwordEncoder.matches(formChangePassword.getOldPassword(), user.getPassword());
        if (passwordIsMatch) {
            if (formChangePassword.getNewPassword().equals(formChangePassword.getConfirmNewPassword())) {
                user.setPassword(passwordEncoder.encode(formChangePassword.getNewPassword()));
                user.setUpdatedAt(new Date());
                userRepository.save(user);
            } else {
                throw new CustomException("Password not match!",HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new CustomException("Old password does not match!", HttpStatus.BAD_REQUEST);
        }
        return user;
    }

    @Override
    public List<User> getNewAccountsThisMonth() {
        // startDate la ngay bat dau cua thang
        LocalDate startDateLocal = LocalDate.now().withDayOfMonth(1);
        Date startDate = Date.from(startDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // endDate la ngay hien tai
        LocalDate endDateLocal = LocalDate.now();
        Date endDate = Date.from(endDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return userRepository.findAll().stream()
                .filter(user -> user.getCreatedAt().compareTo(startDate) >= 0 && user.getCreatedAt()
                        .compareTo(endDate) < 0)
                .toList();
    }
}
