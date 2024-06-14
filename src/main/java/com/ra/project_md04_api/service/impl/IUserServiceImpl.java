package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.model.entity.Role;
import com.ra.project_md04_api.model.entity.User;
import com.ra.project_md04_api.repository.IRoleRepository;
import com.ra.project_md04_api.repository.IUserRepository;
import com.ra.project_md04_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IUserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    @Override
    public Page<User> getUserPaging(String searchName, Integer page, Integer perPage, String orderBy, String direction) {
        return null;
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
    public User deleteRoleFromUser(Long userId, Long roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isPresent()) {
            Role updateRole = optionalRole.get();
            User updateUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));
            if (updateUser.getRoles().contains(updateRole)) {
                updateUser.getRoles().remove(updateRole);
                return userRepository.save(updateUser);
            } else {
                log.error("Role does not exist in user: {}", updateUser.getFullName());
            }
        } else {
            throw new RuntimeException("Role not found with ID: " + roleId);
        }
        return null;
    }


    @Override
    public Boolean updateUserStatus(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus(!user.getStatus());
            userRepository.save(user);
            return true;
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
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

}
