package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.constants.RoleName;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.FormLogin;
import com.ra.project_md04_api.model.dto.request.FormRegister;
import com.ra.project_md04_api.model.dto.response.JwtResponse;
import com.ra.project_md04_api.model.entity.Role;
import com.ra.project_md04_api.model.entity.User;
import com.ra.project_md04_api.repository.IUserRepository;
import com.ra.project_md04_api.security.jwt.JWTProvider;
import com.ra.project_md04_api.security.principals.CustomUserDetail;
import com.ra.project_md04_api.service.IAuthService;
import com.ra.project_md04_api.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IAuthenServiceImpl implements IAuthService {
    private final IRoleService roleService;
    private final IUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${default_avatar}")
    private String defaultAvatar;

    @Override
    public JwtResponse handleLogin(FormLogin formLogin) throws CustomException {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.getUsername(), formLogin.getPassword()));
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid email or password", HttpStatus.CONFLICT);
        }
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        if (!customUserDetail.getStatus()) {
            throw new CustomException("User is blocked", HttpStatus.FORBIDDEN);
        }

        String accessToken = jwtProvider.getAccessToken(customUserDetail);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .fullName(customUserDetail.getFullName())
                .username(customUserDetail.getUsername())
                .email(customUserDetail.getEmail())
                .address(customUserDetail.getAddress())
                .phone(customUserDetail.getPhone())
                .status(customUserDetail.getStatus())
                .avatar(customUserDetail.getAvatar())
                .createdAt(customUserDetail.getCreatedAt())
                .updatedAt(customUserDetail.getUpdatedAt())
                .authorities(customUserDetail.getAuthorities())
                .build();
    }

    @Override
    public void handleRegister(FormRegister formRegister) throws CustomException {
        List<Role> roles = new ArrayList<>();

        if (formRegister.getRoles() == null || formRegister.getRoles().isEmpty()) {
            roles.add(roleService.findRoleByName(RoleName.ROLE_USER));
        } else {
            for (String role : formRegister.getRoles()) {
                switch (role) {
                    case "ROLE_ADMIN":
                        try {
                            roles.add(roleService.findRoleByName(RoleName.ROLE_ADMIN));
                        } catch (CustomException e) {
                            throw new CustomException(e.getMessage(), HttpStatus.CONFLICT);
                        }
                        break;
                    case "ROLE_USER":
                        try {
                            roles.add(roleService.findRoleByName(RoleName.ROLE_USER));
                        } catch (CustomException e) {
                            throw new CustomException(e.getMessage(), HttpStatus.CONFLICT);
                        }
                        break;
                    default:
                        throw new CustomException("Invalid role" + role, HttpStatus.CONFLICT);
                }
            }
        }

        //chuyen FormRegister ve User de save vao database
        User user = User.builder()
                .username(formRegister.getUsername())
                .password(passwordEncoder.encode(formRegister.getPassword()))
                .fullName(formRegister.getFullName())
                .email(formRegister.getEmail())
                .phone(formRegister.getPhone())
                .address(formRegister.getAddress())
                .createdAt(new Date())
                .updatedAt(null)
                .avatar(defaultAvatar)
                .roles(roles)
                .status(true)
                .build();

        userRepository.save(user);
    }
}
