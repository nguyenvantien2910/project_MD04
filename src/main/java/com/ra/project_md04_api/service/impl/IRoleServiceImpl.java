package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.constants.RoleName;
import com.ra.project_md04_api.model.entity.Role;
import com.ra.project_md04_api.repository.IRoleRepository;
import com.ra.project_md04_api.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IRoleServiceImpl implements IRoleService {
    private final IRoleRepository roleRepository;
    @Override
    public Role findRoleByName(RoleName roleName) {
        return roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found!"));
    }
}
