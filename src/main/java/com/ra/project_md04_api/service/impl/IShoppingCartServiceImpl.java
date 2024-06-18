package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.constants.OrderStatus;
import com.ra.project_md04_api.model.dto.request.FormCheckout;
import com.ra.project_md04_api.model.entity.*;
import com.ra.project_md04_api.repository.*;
import com.ra.project_md04_api.service.IShoppingCartService;
import com.ra.project_md04_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

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
    public List<ShoppingCart> getAllShoppingCart(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found" + userId));
        return shoppingCartRepository.findAllByUserUserId(userId);
    }

    @Override
    @Transactional
    public ShoppingCart addProductToShoppingCart(Long productId, Integer quantity) {
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
                throw new RuntimeException("Quantity exceeds product stock quantity");
            }
        }
    }

    @Override
    @Transactional
    public ShoppingCart updateShoppingCartQuantity(Long cartItemId, Integer newQuantity) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Invalid shopping cart id: " + cartItemId));

        if (newQuantity != null && newQuantity >= 0) {
            int productStock = shoppingCart.getProduct().getStockQuantity();
            int quantity = shoppingCart.getOrderQuantity() + newQuantity;

            if (quantity > productStock) {
                throw new RuntimeException("Quantity exceeds product stock quantity. Product: " + shoppingCart.getProduct().getProductName()
                        + ", Current Stock: " + productStock + ", Requested Quantity: " + quantity);
            } else {
                shoppingCart.setOrderQuantity(quantity);
                return shoppingCartRepository.save(shoppingCart);
            }
        } else {
            throw new IllegalArgumentException("Invalid quantity: " + newQuantity);
        }
    }


    @Override
    @Transactional
    public void deleteShoppingCart(Long cartItemId) {
        shoppingCartRepository.findById(cartItemId).orElseThrow(() -> new NoSuchElementException("Invalid shopping cart id :" + cartItemId));
        shoppingCartRepository.deleteById(cartItemId);
    }

    @Override
    @Transactional
    public void deleteAllShoppingCart(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found" + userId));
        shoppingCartRepository.deleteAllByByUserUserId(userId);
    }

    @Override
    @Transactional
    public Boolean checkoutCartItem(FormCheckout formCheckout) {
        // Tinh totalPrice
        double totalPrice = 0.0;
        for (ShoppingCart shoppingCart : formCheckout.getShoppingCarts()) {
            totalPrice += shoppingCart.getOrderQuantity() * shoppingCart.getProduct().getUnitPrice();
        }

        // Calculate receivedAt
        LocalDate createdAt = LocalDate.now();
        LocalDate receivedAt = createdAt.plusDays(4);

        // Tao moi order
        Order order = Order.builder()
                .serialNumber(UUID.randomUUID().toString())
                .user(userRepository.findById(userService.getCurrentUserId())
                        .orElseThrow(() -> new RuntimeException("Invalid user id :" + userService.getCurrentUserId())))
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

        // Create order details and save them
        for (ShoppingCart shoppingCart : formCheckout.getShoppingCarts()) {
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(shoppingCart.getProduct())
                    .productName(shoppingCart.getProduct().getProductName())
                    .unitPrice(shoppingCart.getProduct().getUnitPrice())
                    .orderQuantity(shoppingCart.getOrderQuantity())
                    .build();
            orderDetailRepository.save(orderDetail);
        }
        // clean up shopping cart items
        for (ShoppingCart shoppingCart : formCheckout.getShoppingCarts()) {
            shoppingCartRepository.deleteById(shoppingCart.getShoppingCartId());
        }
        return true;
    }
}
