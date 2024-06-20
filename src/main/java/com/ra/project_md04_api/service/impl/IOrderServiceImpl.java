package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.constants.OrderStatus;
import com.ra.project_md04_api.constants.RoleName;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.RevenueRequest;
import com.ra.project_md04_api.model.dto.response.OrderDetailResponse;
import com.ra.project_md04_api.model.dto.response.OrderResponse;
import com.ra.project_md04_api.model.dto.response.RevenueByCategoryResponse;
import com.ra.project_md04_api.model.entity.*;
import com.ra.project_md04_api.repository.IOrderDetailRepository;
import com.ra.project_md04_api.repository.IOrderRepository;
import com.ra.project_md04_api.repository.IProductRepository;
import com.ra.project_md04_api.service.IOrderDetailService;
import com.ra.project_md04_api.service.IOrderService;
import com.ra.project_md04_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IOrderServiceImpl implements IOrderService {
    private final IOrderRepository orderRepository;
    private final IOrderDetailService orderDetailService;
    private final IProductRepository productRepository;
    private final IUserService userService;
    private final IOrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderResponse> getALlOrdersByUserId() throws CustomException {

        long userId = userService.getCurrentUserId();

        List<Order> orders = orderRepository.findAllByUserUserId(userId);
        if (orders.isEmpty()) {
            throw new CustomException("User don't have any order", HttpStatus.NOT_FOUND);
        } else {
            return orders.stream()
                    .map(order -> OrderResponse.builder()
                            .serialNumber(order.getSerialNumber())
                            .receiveName(order.getReceiveName())
                            .note(order.getNote())
                            .receivePhone(order.getReceivePhone())
                            .receiveAddress(order.getReceiveAddress())
                            .totalPrice(order.getTotalPrice())
                            .orderStatus(order.getOrderStatus())
                            .createdAt(order.getCreatedAt())
                            .receivedAt(order.getReceivedAt())
                            .build())
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<OrderResponse> getALlOrdersByStatus(String status) throws CustomException {
        long userId = userService.getCurrentUserId();
        List<Order> orders = orderRepository.findAllByUserUserId(userId);

        if (orders.isEmpty()) {
            throw new CustomException("User doesn't have any orders", HttpStatus.NOT_FOUND);
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            List<Order> filteredOrders = orderRepository.findAllByUserUserIdAndOrderStatus(userId, orderStatus);
            if (filteredOrders.isEmpty()) {
                throw new CustomException("Don't have any order with status is " + status, HttpStatus.NOT_FOUND);
            }

            return filteredOrders.stream()
                    .map(order -> OrderResponse.builder()
                            .serialNumber(order.getSerialNumber())
                            .totalPrice(order.getTotalPrice())
                            .orderStatus(order.getOrderStatus())
                            .note(order.getNote())
                            .receiveName(order.getReceiveName())
                            .receiveAddress(order.getReceiveAddress())
                            .receivePhone(order.getReceivePhone())
                            .createdAt(order.getCreatedAt())
                            .receivedAt(order.getReceivedAt())
                            .build())
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new CustomException("Invalid order status: " + status, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<OrderResponse> adminGetALlOrdersByStatus(String status) throws CustomException {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            List<Order> filteredOrdersByStatus = orderRepository.findAll().stream().filter(order -> order.getOrderStatus().equals(orderStatus)).toList();
            if (filteredOrdersByStatus.isEmpty()) {
                throw new CustomException("Don't have any order with status is " + status, HttpStatus.NOT_FOUND);
            }

            return filteredOrdersByStatus.stream()
                    .map(order -> OrderResponse.builder()
                            .serialNumber(order.getSerialNumber())
                            .totalPrice(order.getTotalPrice())
                            .orderStatus(order.getOrderStatus())
                            .note(order.getNote())
                            .receiveName(order.getReceiveName())
                            .receiveAddress(order.getReceiveAddress())
                            .receivePhone(order.getReceivePhone())
                            .createdAt(order.getCreatedAt())
                            .receivedAt(order.getReceivedAt())
                            .build())
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new CustomException("Invalid order status: " + status, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId) throws CustomException {
        long userId = userService.getCurrentUserId();
        List<Order> orders = orderRepository.findAll()
                .stream()
                .filter(order -> order.getUser().getUserId().equals(userId))
                .toList();

        if (orders.isEmpty()) {
            throw new CustomException("User doesn't have any orders", HttpStatus.NOT_FOUND);
        }

        Order order = orders.stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new CustomException("User doesn't have any order with id: " + orderId, HttpStatus.NOT_FOUND));

        if (!OrderStatus.WAITING.equals(order.getOrderStatus())) {
            throw new CustomException("Can't cancel order with status: " + order.getOrderStatus(), HttpStatus.BAD_REQUEST);
        }

        // Retrieve OrderDetail by Order object
        List<OrderDetail> orderDetails = orderDetailRepository.findAll()
                .stream()
                .filter(orderDetail -> orderDetail.getOrder().getOrderId().equals(orderId)).toList();

        // Save original product quantities
        Map<Product, Integer> originalQuantities = new HashMap<>();
        for (OrderDetail orderDetail : orderDetails) {
            originalQuantities.put(orderDetail.getProduct(), orderDetail.getOrderQuantity());
        }

        // Cancel order
        order.setOrderStatus(OrderStatus.CANCEL);
        Order cancelledOrder = orderRepository.save(order);

        // Rollback product stock quantities if cancellation is successful
        if (OrderStatus.CANCEL.equals(cancelledOrder.getOrderStatus())) {
            for (OrderDetail orderDetail : orderDetails) {
                Product product = orderDetail.getProduct();
                Integer quantityToRollback = originalQuantities.get(product);
                product.setStockQuantity(product.getStockQuantity() + quantityToRollback);
                productRepository.save(product);
            }
        } else {
            throw new CustomException("Cancellation failed for order: " + cancelledOrder.getOrderId(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return OrderResponse.builder()
                .serialNumber(cancelledOrder.getSerialNumber())
                .receiveName(cancelledOrder.getReceiveName())
                .receiveAddress(cancelledOrder.getReceiveAddress())
                .receivePhone(cancelledOrder.getReceivePhone())
                .totalPrice(cancelledOrder.getTotalPrice())
                .orderStatus(cancelledOrder.getOrderStatus())
                .note(cancelledOrder.getNote())
                .createdAt(cancelledOrder.getCreatedAt())
                .receivedAt(cancelledOrder.getReceivedAt())
                .build();
    }


    @Override
    public OrderResponse getUserOrderBySerialNumber(String serialNumber) throws CustomException {
        long userId = userService.getCurrentUserId();
        List<Order> orders = orderRepository.findAllByUserUserId(userId);

        if (orders.isEmpty()) {
            throw new CustomException("User doesn't have any orders", HttpStatus.NOT_FOUND);
        }

        return orders.stream()
                .filter(o -> o.getSerialNumber().equals(serialNumber))
                .findFirst()
                .map(order -> OrderResponse.builder()
                        .serialNumber(order.getSerialNumber())
                        .receiveName(order.getReceiveName())
                        .note(order.getNote())
                        .receivePhone(order.getReceivePhone())
                        .receiveAddress(order.getReceiveAddress())
                        .totalPrice(order.getTotalPrice())
                        .orderStatus(order.getOrderStatus())
                        .createdAt(order.getCreatedAt())
                        .receivedAt(order.getReceivedAt())
                        .build())
                .orElseThrow(() -> new CustomException("User don't have order with serial number: " + serialNumber, HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Order> getAllOrders() throws CustomException {
        try {
            return orderRepository.findAll();
        } catch (NullPointerException e) {
            throw new CustomException("Don't have any order to show", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<OrderDetailResponse> getOrderDetailByOrderId(Long orderId) throws CustomException {
        orderRepository.findById(orderId).orElseThrow(() -> new CustomException("Order not found with Id : " + orderId, HttpStatus.NOT_FOUND));
        List<OrderDetail> orderDetails = orderDetailRepository.findAll()
                .stream()
                .filter(o -> o.getOrder().getOrderId().equals(orderId))
                .toList();
        if (orderDetails.isEmpty()) {
            throw new CustomException("Order detail not found by orderId: " + orderId, HttpStatus.NOT_FOUND);
        }
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
    public OrderResponse updateOrderStatus(String status, Long orderId) throws CustomException {
        if (status == null) {
            throw new CustomException("Status cannot be null", HttpStatus.BAD_REQUEST);
        } else {
            Order order = getOrderByOrderId(orderId);
            if (order.getOrderStatus().equals(OrderStatus.SUCCESS)) {
                throw new CustomException("Order already completed", HttpStatus.CONFLICT);
            }

            // Get thong tin orderDetail de rollback
            List<OrderDetail> orderDetails = orderDetailRepository.findAll()
                    .stream()
                    .filter(orderDetail -> orderDetail.getOrder().getOrderId().equals(orderId))
                    .toList();

            // Lưu lại các thông tin Product va Quantity cua Order
            Map<Product, Integer> originalQuantities = new HashMap<>();
            for (OrderDetail orderDetail : orderDetails) {
                originalQuantities.put(orderDetail.getProduct(), orderDetail.getOrderQuantity());
            }

            // Update status cua order
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
                switch (orderStatus) {
                    case WAITING -> order.setOrderStatus(OrderStatus.WAITING);
                    case CONFIRM -> order.setOrderStatus(OrderStatus.CONFIRM);
                    case DELIVERY -> order.setOrderStatus(OrderStatus.DELIVERY);
                    case SUCCESS -> order.setOrderStatus(OrderStatus.SUCCESS);
                    case CANCEL -> order.setOrderStatus(OrderStatus.CANCEL);
                    case DENIED -> order.setOrderStatus(OrderStatus.DENIED);
                    default -> throw new CustomException("Invalid order status: " + status, HttpStatus.BAD_REQUEST);
                }
                Order updatedOrder = orderRepository.save(order);

                // Rollback quantity if the order is canceled or denied
                if (updatedOrder.getOrderStatus() == OrderStatus.CANCEL || updatedOrder.getOrderStatus() == OrderStatus.DENIED) {
                    for (OrderDetail orderDetail : orderDetails) {
                        Product product = orderDetail.getProduct();
                        Integer quantityToRollback = originalQuantities.get(product);
                        product.setStockQuantity(product.getStockQuantity() + quantityToRollback);
                        productRepository.save(product);
                    }
                }

                return OrderResponse.builder()
                        .serialNumber(order.getSerialNumber())
                        .receiveName(order.getReceiveName())
                        .note(order.getNote())
                        .receivePhone(order.getReceivePhone())
                        .receiveAddress(order.getReceiveAddress())
                        .totalPrice(order.getTotalPrice())
                        .orderStatus(order.getOrderStatus())
                        .createdAt(order.getCreatedAt())
                        .receivedAt(order.getReceivedAt())
                        .build();

            } catch (IllegalArgumentException e) {
                throw new CustomException("Invalid order status: " + status, HttpStatus.BAD_REQUEST);
            }
        }
    }


    @Override
    public Double getSalesRevenueOverTime(RevenueRequest revenueRequest) throws CustomException {
        Date fromDate = revenueRequest.getFrom();
        Date toDate = revenueRequest.getTo();

        if (fromDate.after(toDate)) {
            throw new CustomException("Invalid date range: 'from' date must be before 'to' date.", HttpStatus.BAD_REQUEST);
        }

        List<Order> orders;
        try {
            orders = orderRepository.findAllByCreatedAtBetween(fromDate, toDate);

            if (orders == null || orders.isEmpty()) {
                throw new CustomException("Don't have any order to show", HttpStatus.NOT_FOUND);
            }

            orders = orders.stream()
                    .filter(order -> OrderStatus.SUCCESS.equals(order.getOrderStatus()))
                    .toList();
        } catch (Exception e) {
            throw new CustomException("An error occurred while retrieving orders.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return orders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }


    @Override
    public Integer CountAllByCreatedAtBetween(RevenueRequest revenueRequest) {
        Date fromDate = revenueRequest.getFrom();
        Date toDate = revenueRequest.getTo();
        if (fromDate.after(toDate)) {
            throw new RuntimeException("Invalid date range: 'from' date must be before 'to' date.");
        }
        try {
            return orderRepository.findAllByCreatedAtBetween(fromDate, toDate).size();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Order not found by date: " + revenueRequest.getFrom() + " and " + revenueRequest.getTo());
        }
    }

    @Override
    public List<User> getTopSpendingCustomers(RevenueRequest revenueRequest) {
        Date fromDate = revenueRequest.getFrom();
        Date toDate = revenueRequest.getTo();
        if (fromDate.after(toDate)) {
            throw new RuntimeException("Invalid date range: 'from' date must be before 'to' date.");
        }

        List<Order> orders;
        try {
            orders = orderRepository.findAllByCreatedAtBetween(fromDate, toDate)
                    .stream()
                    .filter(order -> order.getOrderStatus().equals(OrderStatus.SUCCESS))
                    .toList();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Don't have any orders in range yet");
        }

        // dem so luong yeu thich
        Map<User, Double> OrdersMap = orders.stream()
                .collect(Collectors.groupingBy(Order::getUser, Collectors.summingDouble(Order::getTotalPrice)));

        // sap xep lai theo so luong yeu thich
        return OrdersMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<RevenueByCategoryResponse> getRevenueByCategory() throws CustomException {
        List<Order> successfulOrders = orderRepository.findAllByOrderStatus(OrderStatus.SUCCESS);
        if (successfulOrders.isEmpty()) {
            throw new NoSuchElementException("Don't have any successful orders yet");
        }
        Map<Category, Double> revenueByCategory = new HashMap<>();

        for (Order order : successfulOrders) {
            for (OrderDetail orderDetail : orderDetailRepository.findAll()
                    .stream().filter(orderDetail -> orderDetail.getOrder().getOrderId().equals(order.getOrderId())).toList()) {
                Product product = orderDetail.getProduct();
                Category category = product.getCategory();
                Double revenue = orderDetail.getUnitPrice() * orderDetail.getOrderQuantity();

                revenueByCategory.put(category, revenueByCategory.getOrDefault(category, 0.0) + revenue);
            }
        }

        List<RevenueByCategoryResponse> responses = new ArrayList<>();
        for (Map.Entry<Category, Double> entry : revenueByCategory.entrySet()) {
            RevenueByCategoryResponse response = RevenueByCategoryResponse.builder()
                    .categoryId(entry.getKey().getCategoryId())
                    .categoryName(entry.getKey().getCategoryName())
                    .totalRevenue(entry.getValue())
                    .build();
            responses.add(response);
        }

        return responses;
    }
}
