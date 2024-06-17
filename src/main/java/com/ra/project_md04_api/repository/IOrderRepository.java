package com.ra.project_md04_api.repository;

import com.ra.project_md04_api.constants.OrderStatus;
import com.ra.project_md04_api.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserUserId(Long userId);
    Order findBySerialNumber(String serialNumber);
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);
}
