package com.ra.project_md04_api.repository;

import com.ra.project_md04_api.model.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query("select od from order_details od where od.orderId =:orderId")
    List<OrderDetail> findAllByOrderId(Long orderId);
}
