package com.ra.project_md04_api.service;

import com.ra.project_md04_api.model.dto.request.FormUpdateUserInfo;
import com.ra.project_md04_api.model.dto.request.FormChangePassword;
import com.ra.project_md04_api.model.dto.response.UserInfoResponse;
import com.ra.project_md04_api.model.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserService {
    Page<User> getUserPaging(String searchName, Integer page, Integer perPage, String orderBy, String direction);
    User updateRoleToUser(Long userId, Long roleId);
    User deleteRoleFromUser(Long userId, Long roleId);
    Boolean updateUserStatus(Long userId);
    List<User> findUserByFullName(String searchName);
    Long getCurrentUserId();
    UserInfoResponse getUserInfo(Long userId);
    User updateUserInfo(FormUpdateUserInfo formUpdateUserInfo);
    User updatePassword(FormChangePassword formChangePassword);
}
