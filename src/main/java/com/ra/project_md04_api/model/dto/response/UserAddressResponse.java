package com.ra.project_md04_api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserAddressResponse {
    private Long addressId;
    private String fullAddress;
    private String phoneNumber;
    private String receiveName;
}
