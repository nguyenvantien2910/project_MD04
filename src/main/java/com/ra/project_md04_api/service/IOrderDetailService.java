package com.ra.project_md04_api.service;

import com.ra.project_md04_api.model.entity.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    List<OrderDetail> getALlOrderDetailsBySerialNumber(String serialNumber);
}
