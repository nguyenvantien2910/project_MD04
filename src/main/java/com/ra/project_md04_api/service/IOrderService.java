package com.ra.project_md04_api.service;

import com.ra.project_md04_api.model.entity.Order;

import java.util.List;

public interface IOrderService {
    List<Order> getALlOrdersByUserId(Long userId);
    List<Order> getALlOrdersByStatus(String status);
    Order cancelOrder(Long orderId);
    Order getUserOrderBySerialNumber(String serialNumber);
    List<Order> getAllOrders();
    Order getOrderById(Long orderId);
    Order updateOrderStatus(String status,Long orderId);
}
