package com.ra.project_md04_api.security.principals;

import com.ra.project_md04_api.model.entity.User;
import com.ra.project_md04_api.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Get username
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        //chuyen tu user ve CustomUserDetail de luu vao SecurityContextHolder
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return CustomUserDetail.builder()
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .address(user.getAddress())
                    .avatar(user.getAvatar())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .status(user.getStatus())
                    .authorities(user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).toList())
                    .build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
