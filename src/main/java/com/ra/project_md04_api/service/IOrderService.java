package com.ra.project_md04_api.service;

import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.RevenueRequest;
import com.ra.project_md04_api.model.dto.response.OrderDetailResponse;
import com.ra.project_md04_api.model.dto.response.OrderResponse;
import com.ra.project_md04_api.model.dto.response.RevenueByCategoryResponse;
import com.ra.project_md04_api.model.entity.Order;
import com.ra.project_md04_api.model.entity.User;

import java.util.List;

public interface IOrderService {
    List<OrderResponse> getALlOrdersByUserId() throws CustomException;
    List<OrderResponse> getALlOrdersByStatus(String status) throws CustomException;
    List<OrderResponse> adminGetALlOrdersByStatus(String status) throws CustomException;
    OrderResponse cancelOrder(Long orderId) throws CustomException;
    OrderResponse getUserOrderBySerialNumber(String serialNumber) throws CustomException;
    List<Order> getAllOrders() throws CustomException;
    List<OrderDetailResponse> getOrderDetailByOrderId(Long orderId) throws CustomException;
    Order getOrderByOrderId(Long orderId);
    OrderResponse updateOrderStatus(String status,Long orderId) throws CustomException;
    Double getSalesRevenueOverTime(RevenueRequest revenueRequest) throws CustomException;
    Integer CountAllByCreatedAtBetween(RevenueRequest revenueRequest);
    List<User> getTopSpendingCustomers(RevenueRequest revenueRequest);
    List<RevenueByCategoryResponse> getRevenueByCategory() throws CustomException;
}
