package com.ra.project_md04_api.model.dto.response;

import com.ra.project_md04_api.constants.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponse {
    private String serialNumber;
    private Double totalPrice;
    private OrderStatus orderStatus;
    private String note;
    private String receiveName;
    private String receiveAddress;
    private String receivePhone;
    private Date createdAt;
    private Date receivedAt;
}
