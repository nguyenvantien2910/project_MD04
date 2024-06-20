package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.constants.RoleName;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.entity.Role;
import com.ra.project_md04_api.repository.IRoleRepository;
import com.ra.project_md04_api.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IRoleServiceImpl implements IRoleService {
    private final IRoleRepository roleRepository;

    @Override
    public Role findRoleByName(RoleName roleName) throws CustomException {
        return roleRepository.findByRoleName(roleName).orElseThrow(() -> new CustomException("Role not found!", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Role> findAllRoles() throws CustomException {
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            throw new CustomException("Don't have any role", HttpStatus.NOT_FOUND);
        }

        return roles;
    }
}
