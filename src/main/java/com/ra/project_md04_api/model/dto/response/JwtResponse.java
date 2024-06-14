package com.ra.project_md04_api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JwtResponse {
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private Boolean status;
    private Date createdAt;
    private Date updatedAt;
    private Collection<? extends GrantedAuthority> authorities;
    private String accessToken;
}
