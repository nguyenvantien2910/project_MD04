package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.model.entity.Order;
import com.ra.project_md04_api.model.entity.OrderDetail;
import com.ra.project_md04_api.repository.IOrderDetailRepository;
import com.ra.project_md04_api.repository.IOrderRepository;
import com.ra.project_md04_api.service.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class IOrderDetailServiceImpl implements IOrderDetailService {
    private final IOrderDetailRepository orderDetailRepository;
    private final IOrderRepository orderRepository;

    @Override
    public List<OrderDetail> getALlOrderDetailsBySerialNumber(String serialNumber) {
        Order order = orderRepository.findBySerialNumber(serialNumber);
        if (order == null) {
            throw new NoSuchElementException("Serial number don't exist");
        } else {
            List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(order.getOrderId());
            if (orderDetails.isEmpty()) {
                throw new NoSuchElementException("Don't have any order detail by serial number: " + serialNumber);
            } else {
                return orderDetails;
            }
        }
    }
}
