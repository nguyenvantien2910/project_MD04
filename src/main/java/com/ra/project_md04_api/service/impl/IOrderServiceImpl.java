package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.constants.OrderStatus;
import com.ra.project_md04_api.model.dto.request.RevenueRequest;
import com.ra.project_md04_api.model.entity.Order;
import com.ra.project_md04_api.model.entity.OrderDetail;
import com.ra.project_md04_api.model.entity.Product;
import com.ra.project_md04_api.repository.IOrderRepository;
import com.ra.project_md04_api.repository.IProductRepository;
import com.ra.project_md04_api.service.IOrderDetailService;
import com.ra.project_md04_api.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class IOrderServiceImpl implements IOrderService {
    private final IOrderRepository orderRepository;
    private final IOrderDetailService orderDetailService;
    private final IProductRepository productRepository;

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
            };
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
    }

    @Override
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));
        if (!OrderStatus.WAITING.equals(order.getOrderStatus())) {
            throw new IllegalStateException("Can't cancel order with status: " + order.getOrderStatus());
        }
        //Tìm các OrderDetail của order
        List<OrderDetail> orderDetails = orderDetailService.getALlOrderDetailsByOrderId(orderId);

        //Lưu lại các thông tin Product va Quantity cua Order
        Map<Product, Integer> originalQuantities = new HashMap<>();
        for (OrderDetail orderDetail : orderDetails) {
            originalQuantities.put(orderDetail.getProduct(), orderDetail.getOrderQuantity());
        }

        //Cancel order
        order.setOrderStatus(OrderStatus.CANCEL);
        Order cancelledOrder = orderRepository.save(order);

        //Logic check rollback quantity if fail
        if (cancelledOrder.getOrderStatus() != OrderStatus.CANCEL) {
            throw new IllegalStateException("Cancellation failed for order: " + cancelledOrder.getOrderId());
        } else {
            for (OrderDetail orderDetail : orderDetails) {
                Product product = orderDetail.getProduct();
                Integer quantityToRollback = originalQuantities.get(product) - orderDetail.getOrderQuantity();
                product.setStockQuantity(quantityToRollback);
                productRepository.save(product);
            }
        }

        return cancelledOrder;
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
    public List<OrderDetail> getOrderDetailByOrderId(Long orderId) {
        orderRepository.findById(orderId).orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));
        List<OrderDetail> orderDetails = orderDetailService.getALlOrderDetailsByOrderId(orderId);
        if (orderDetails.isEmpty()) {
            throw new NoSuchElementException("Order detail not found by orderId: " + orderId);
        } else {
            return orderDetails;
        }
    }

    @Override
    public Order getOrderByOrderId(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        } else {
            throw new NoSuchElementException("Order not found by id: " + orderId);
        }
    }

    @Override
    @Transactional
    public Order updateOrderStatus(String status, Long orderId) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        } else {
            Order order = getOrderByOrderId(orderId);

            //Get thong tin orderDetail de rollback
            List<OrderDetail> orderDetails = orderDetailService.getALlOrderDetailsByOrderId(orderId);

            //Lưu lại các thông tin Product va Quantity cua Order
            Map<Product, Integer> originalQuantities = new HashMap<>();
            for (OrderDetail orderDetail : orderDetails) {
                originalQuantities.put(orderDetail.getProduct(), orderDetail.getOrderQuantity());
            }

            //update status cua order
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
                Order updateOrder = orderRepository.save(order);

                //Rollback quantity
                if (updateOrder.getOrderStatus() == OrderStatus.CANCEL || updateOrder.getOrderStatus() == OrderStatus.DENIED) {
                    for (OrderDetail orderDetail : orderDetails) {
                        Product product = orderDetail.getProduct();
                        Integer quantityToRollback = originalQuantities.get(product) - orderDetail.getOrderQuantity();
                        product.setStockQuantity(quantityToRollback);
                        productRepository.save(product);
                    }
                }
                return updateOrder;

            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid order status: " + status);
            }
        }
    }

    @Override
    public Double getSalesRevenueOverTime(RevenueRequest revenueRequest) {
        Date fromDate = revenueRequest.getFrom();
        Date toDate = revenueRequest.getTo();

        List<Order> orders = orderRepository.findAllByCreatedAtBetween(fromDate, toDate);

        return orders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }
}
