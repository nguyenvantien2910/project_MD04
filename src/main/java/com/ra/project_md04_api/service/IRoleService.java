package com.ra.project_md04_api.service;

import com.ra.project_md04_api.constants.RoleName;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.entity.Role;

import java.util.List;

public interface IRoleService {
    Role findRoleByName(RoleName roleName) throws CustomException;
    List<Role> findAllRoles() throws CustomException;
}
