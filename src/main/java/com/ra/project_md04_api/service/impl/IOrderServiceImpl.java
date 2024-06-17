package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.constants.OrderStatus;
import com.ra.project_md04_api.model.entity.Order;
import com.ra.project_md04_api.repository.IOrderRepository;
import com.ra.project_md04_api.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IOrderServiceImpl implements IOrderService {
    private final IOrderRepository orderRepository;

    @Override
    public List<Order> getALlOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findAllByUserUserId(userId);
        if (orders.isEmpty()) {
            throw new NoSuchElementException("User don't have any order");
        } else {
            return orders;
        }
    }

    @Override
    public List<Order> getALlOrdersByStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return switch (orderStatus) {
                case WAITING -> orderRepository.findAllByOrderStatus(OrderStatus.WAITING);
                case CONFIRM -> orderRepository.findAllByOrderStatus(OrderStatus.CONFIRM);
                case DELIVERY -> orderRepository.findAllByOrderStatus(OrderStatus.DELIVERY);
                case SUCCESS -> orderRepository.findAllByOrderStatus(OrderStatus.SUCCESS);
                case CANCEL -> orderRepository.findAllByOrderStatus(OrderStatus.CANCEL);
                case DENIED -> orderRepository.findAllByOrderStatus(OrderStatus.DENIED);
                default -> new ArrayList<>();
            };
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
    }

    @Override
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));
        if (OrderStatus.WAITING.equals(order.getOrderStatus())) {
            order.setOrderStatus(OrderStatus.CANCEL);
            return orderRepository.save(order);
        }else {
            throw new NoSuchElementException("Can't cancel order by status: " + order.getOrderStatus());
        }
    }

    @Override
    public Order getUserOrderBySerialNumber(String serialNumber) {
        Order order = orderRepository.findBySerialNumber(serialNumber);
        if (order == null) {
            throw new NoSuchElementException("Order not found by serialNumber: " + serialNumber);
        } else {
            return order;
        }
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new NoSuchElementException("Don't have any order to show");
        } else {
            return orders;
        }
    }

    @Override
    public Order getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        } else {
            throw new NoSuchElementException("Order not found by id: " + orderId);
        }
    }

    @Override
    public Order updateOrderStatus(String status,Long orderId) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        } else {
            Order order = getOrderById(orderId);
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
                switch (orderStatus) {
                    case WAITING -> order.setOrderStatus(OrderStatus.WAITING);
                    case CONFIRM -> order.setOrderStatus(OrderStatus.CONFIRM);
                    case DELIVERY -> order.setOrderStatus(OrderStatus.DELIVERY);
                    case SUCCESS -> order.setOrderStatus(OrderStatus.SUCCESS);
                    case CANCEL -> order.setOrderStatus(OrderStatus.CANCEL);
                    case DENIED -> order.setOrderStatus(OrderStatus.DENIED);
                    default -> throw new NoSuchElementException("Invalid order status: " + status);
                }
                return orderRepository.save(order);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid order status: " + status);
            }
        }
    }
}
