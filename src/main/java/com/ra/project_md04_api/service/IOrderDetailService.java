package com.ra.project_md04_api.service;

import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.RevenueRequest;
import com.ra.project_md04_api.model.dto.response.OrderDetailResponse;
import com.ra.project_md04_api.model.entity.OrderDetail;
import com.ra.project_md04_api.model.entity.Product;

import java.util.List;

public interface IOrderDetailService {
    List<OrderDetail> getALlOrderDetailsBySerialNumber(String serialNumber);
    List<OrderDetailResponse> getALlOrderDetailsByOrderId(Long orderId) throws CustomException;
//    List<OrderDetail> getBestSellerProductsFromAndTo(RevenueRequest revenueRequest);
}
