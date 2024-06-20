package com.ra.project_md04_api.repository;

import com.ra.project_md04_api.constants.OrderStatus;
import com.ra.project_md04_api.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface IOrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.user.userId =:userId")
    List<Order> findAllByUserUserId(Long userId);

    @Query("select o from  Order o where o.serialNumber =:serialNumber")
    Order findBySerialNumber(String serialNumber);

    @Query("select o from Order o where o.orderStatus =: orderStatus")
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    @Query("select o from Order o where o.createdAt between :from and :to")
    List<Order> findAllByCreatedAtBetween(Date from, Date to);

    List<Order> findAllByUserUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
}
