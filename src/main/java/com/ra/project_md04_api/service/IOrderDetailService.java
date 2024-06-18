package com.ra.project_md04_api.service;

import com.ra.project_md04_api.model.dto.request.RevenueRequest;
import com.ra.project_md04_api.model.entity.OrderDetail;
import com.ra.project_md04_api.model.entity.Product;

import java.util.List;

public interface IOrderDetailService {
    List<OrderDetail> getALlOrderDetailsBySerialNumber(String serialNumber);
    List<OrderDetail> getALlOrderDetailsByOrderId(Long orderId);
    List<OrderDetail> getBestSellerProductsFromAndTo(RevenueRequest revenueRequest);
}
