package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.response.OrderDetailResponse;
import com.ra.project_md04_api.model.entity.OrderDetail;
import com.ra.project_md04_api.repository.IOrderDetailRepository;
import com.ra.project_md04_api.repository.IOrderRepository;
import com.ra.project_md04_api.service.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IOrderDetailServiceImpl implements IOrderDetailService {
    private final IOrderDetailRepository orderDetailRepository;
    private final IOrderRepository orderRepository;

    @Override
    public List<OrderDetail> getALlOrderDetailsBySerialNumber(String serialNumber) {
        Long orderId = orderRepository.findBySerialNumber(serialNumber).getOrderId();
        if (orderId == null) {
            throw new NoSuchElementException("Serial number don't exist");
        } else {
            List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(orderId);
            if (orderDetails.isEmpty()) {
                throw new NoSuchElementException("Don't have any order detail by serial number: " + serialNumber);
            } else {
                return orderDetails;
            }
        }
    }

    @Override
    public List<OrderDetailResponse> getALlOrderDetailsByOrderId(Long orderId) throws CustomException {
        orderRepository.findById(orderId).orElseThrow(() -> new NoSuchElementException("Order id don't exist" + orderId));
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(orderId);
        if (orderDetails.isEmpty()) {
            throw new CustomException("Don't have any order detail by order id: " + orderId, HttpStatus.NOT_FOUND);
        } else {
            return orderDetails.stream().map(
                    orderDetail -> OrderDetailResponse.builder()
                            .orderDetailId(orderDetail.getOrderId())
                            .productId(orderDetail.getProduct().getProductId())
                            .productName(orderDetail.getProduct().getProductName())
                            .unitPrice(orderDetail.getUnitPrice())
                            .orderQuantity(orderDetail.getOrderQuantity())
                            .build()
            ).collect(Collectors.toList());
        }
    }
}
