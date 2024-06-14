package com.ra.project_md04_api.service;

import com.ra.project_md04_api.constants.RoleName;
import com.ra.project_md04_api.model.entity.Role;

public interface IRoleService {
    Role findRoleByName(RoleName roleName);
}
