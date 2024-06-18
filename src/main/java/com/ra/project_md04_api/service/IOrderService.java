package com.ra.project_md04_api.service;

import com.ra.project_md04_api.model.dto.request.RevenueRequest;
import com.ra.project_md04_api.model.entity.Order;
import com.ra.project_md04_api.model.entity.OrderDetail;

import java.util.List;

public interface IOrderService {
    List<Order> getALlOrdersByUserId(Long userId);
    List<Order> getALlOrdersByStatus(String status);
    Order cancelOrder(Long orderId);
    Order getUserOrderBySerialNumber(String serialNumber);
    List<Order> getAllOrders();
    List<OrderDetail> getOrderDetailByOrderId(Long orderId);
    Order getOrderByOrderId(Long orderId);
    Order updateOrderStatus(String status,Long orderId);
    Double getSalesRevenueOverTime(RevenueRequest revenueRequest);
}
