package com.ra.project_md04_api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserInfoResponse {
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String avatar;
    private String phone;
    private String address;
}
