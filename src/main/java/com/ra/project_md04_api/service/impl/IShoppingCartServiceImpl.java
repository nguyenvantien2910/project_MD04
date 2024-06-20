package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.constants.OrderStatus;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.FormCheckout;
import com.ra.project_md04_api.model.dto.response.ShoppingCartResponse;
import com.ra.project_md04_api.model.entity.*;
import com.ra.project_md04_api.repository.*;
import com.ra.project_md04_api.service.IShoppingCartService;
import com.ra.project_md04_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class IShoppingCartServiceImpl implements IShoppingCartService {
    private final IShoppingCartRepository shoppingCartRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final IUserService userService;
    private final IOrderRepository orderRepository;
    private final IOrderDetailRepository orderDetailRepository;

    @Override
    public List<ShoppingCart> getAllShoppingCart() throws CustomException {
        long userId = userService.getCurrentUserId();
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByUserUserId(userId);
        if (shoppingCarts.isEmpty()) {
            throw new CustomException("No shopping cart found", HttpStatus.NOT_FOUND);
        }
        return shoppingCarts;
    }

    @Override
    public ShoppingCart addProductToShoppingCart(Long productId, Integer quantity) throws CustomException {
        //lay userId
        Long currentUserId = userService.getCurrentUserId();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NoSuchElementException("Invalid user id: " + currentUserId));

        //Lay ra product va kiem tra so luong
        Product product = productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException("Product Id not found " + productId));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Stock quantity not sufficient to add " + quantity + " units of product " + productId);
        }

        //check xem trong cart da co san pham nay hay chua
        ShoppingCart shoppingCart = shoppingCartRepository.findAllByUserUserId(user.getUserId())
                .stream()
                .filter(cart -> cart.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElse(null);
        if (shoppingCart == null) {
            ShoppingCart newShoppingCart = ShoppingCart.builder()
                    .product(product)
                    .orderQuantity(quantity)
                    .user(user)
                    .build();
            return shoppingCartRepository.save(newShoppingCart);
        } else {
            int newQuantity = shoppingCart.getOrderQuantity() + quantity;
            if (newQuantity <= product.getStockQuantity()) {
                shoppingCart.setOrderQuantity(newQuantity);
                return shoppingCartRepository.save(shoppingCart);
            } else {
                throw new CustomException("Quantity exceeds product stock quantity", HttpStatus.CONFLICT);
            }
        }
    }

    @Override
    public ShoppingCartResponse updateShoppingCartQuantity(Long cartItemId, Integer newQuantity) throws CustomException {
        long currentUserId = userService.getCurrentUserId();
        if (newQuantity == null || newQuantity <= 0) {
            throw new CustomException("Quantity must be greater than zero", HttpStatus.BAD_REQUEST);
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartItemId)
                .orElseThrow(() -> new CustomException("Invalid shopping cart id: " + cartItemId, HttpStatus.BAD_REQUEST));

        if (!shoppingCart.getUser().getUserId().equals(currentUserId)) {
            throw new CustomException("Cart item don't belong by user", HttpStatus.BAD_REQUEST);
        }

        int productStock = shoppingCart.getProduct().getStockQuantity();
        if (newQuantity > productStock) {
            throw new CustomException("Quantity exceeds product stock quantity. Product: " + shoppingCart.getProduct().getProductName()
                    + ", Current Stock: " + productStock + ", Requested Quantity: " + newQuantity, HttpStatus.BAD_REQUEST);
        }

        shoppingCart.setOrderQuantity(newQuantity);
        shoppingCartRepository.save(shoppingCart);

        return ShoppingCartResponse.builder()
                .shoppingCartId(shoppingCart.getShoppingCartId())
                .userId(shoppingCart.getUser().getUserId())
                .fullName(shoppingCart.getUser().getFullName())
                .productId(shoppingCart.getProduct().getProductId())
                .productName(shoppingCart.getProduct().getProductName())
                .orderQuantity(shoppingCart.getOrderQuantity())
                .build();
    }

    @Override
    public void deleteShoppingCart(Long cartItemId) throws CustomException {
        shoppingCartRepository.findById(cartItemId).orElseThrow(() -> new CustomException("Invalid shopping cart id :" + cartItemId, HttpStatus.CONFLICT));
        try {
            shoppingCartRepository.deleteById(cartItemId);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @Override
    @Transactional
    public void deleteAllShoppingCart(Long userId) throws CustomException {
        userRepository.findById(userId).orElseThrow(() -> new CustomException("User Not Found: " + userId, HttpStatus.BAD_GATEWAY));
        try {
            List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByUserUserId(userId);
            if (shoppingCarts.isEmpty()) {
                throw new CustomException("User don't have any shopping cart.", HttpStatus.NOT_FOUND);
            }
            // xoa tat ca shopping cart
            shoppingCartRepository.deleteAllByByUserUserId(userId);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @Override
    @Transactional
    public Boolean checkoutCartItem(FormCheckout formCheckout) throws CustomException {
        // Fetch ShoppingCart entities by their IDs
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllById(formCheckout.getShoppingCartItems());
        if (shoppingCarts.isEmpty()) {
            throw new CustomException("No shopping cart found", HttpStatus.NOT_FOUND);
        }

        // Check if all shopping cart items belong to the current user
        Long currentUserId = userService.getCurrentUserId();
        for (ShoppingCart shoppingCart : shoppingCarts) {
            if (!shoppingCart.getUser().getUserId().equals(currentUserId)) {
                throw new CustomException("Shopping cart item does not belong to the current user", HttpStatus.FORBIDDEN);
            }
        }

        // Calculate totalPrice
        double totalPrice = 0.0;
        for (ShoppingCart shoppingCart : shoppingCarts) {
            totalPrice += shoppingCart.getOrderQuantity() * shoppingCart.getProduct().getUnitPrice();
        }

        // Verify if stock is sufficient for each product in the shopping cart
        for (ShoppingCart shoppingCart : shoppingCarts) {
            Product product = shoppingCart.getProduct();
            if (shoppingCart.getOrderQuantity() > product.getStockQuantity()) {
                throw new CustomException("Insufficient stock for product: " + product.getProductName(), HttpStatus.BAD_REQUEST);
            }
        }

        // Calculate receivedAt
        LocalDate createdAt = LocalDate.now();
        LocalDate receivedAt = createdAt.plusDays(4);

        // Create a new order
        Order order = Order.builder()
                .serialNumber(UUID.randomUUID().toString())
                .user(userRepository.findById(userService.getCurrentUserId())
                        .orElseThrow(() -> new RuntimeException("Invalid user id: " + userService.getCurrentUserId())))
                .totalPrice(totalPrice)
                .orderStatus(OrderStatus.WAITING)
                .note(formCheckout.getNote())
                .receiveName(formCheckout.getReceiveName())
                .receiveAddress(formCheckout.getReceiveAddress())
                .receivePhone(formCheckout.getReceivePhone())
                .createdAt(new Date())
                .receivedAt(Date.from(receivedAt.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();

        // Save order entity
        order = orderRepository.save(order);

        // Create order details, save them, and update product stock quantities
        for (ShoppingCart shoppingCart : shoppingCarts) {
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(shoppingCart.getProduct())
                    .productName(shoppingCart.getProduct().getProductName())
                    .unitPrice(shoppingCart.getProduct().getUnitPrice())
                    .orderQuantity(shoppingCart.getOrderQuantity())
                    .build();
            orderDetailRepository.save(orderDetail);

            // Update product stock quantity
            Product product = shoppingCart.getProduct();
            int newStockQuantity = product.getStockQuantity() - shoppingCart.getOrderQuantity();
            product.setStockQuantity(newStockQuantity);
            productRepository.save(product);
        }

        // Clean up shopping cart items
        for (ShoppingCart shoppingCart : shoppingCarts) {
            shoppingCartRepository.deleteById(shoppingCart.getShoppingCartId());
        }

        return true;
    }
}
