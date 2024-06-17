package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.model.entity.ShoppingCart;
import com.ra.project_md04_api.repository.IProductRepository;
import com.ra.project_md04_api.repository.IShoppingCartRepository;
import com.ra.project_md04_api.repository.IUserRepository;
import com.ra.project_md04_api.service.IShoppingCartService;
import com.ra.project_md04_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class IShoppingCartServiceImpl implements IShoppingCartService {
    private final IShoppingCartRepository shoppingCartRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final IUserService userService;


    @Override
    public List<ShoppingCart> getAllShoppingCart(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found" + userId));
        return shoppingCartRepository.findAllByUserUserId(userId);
    }

    @Override
    public ShoppingCart getShoppingCartById(Long cartItemId) {
        return shoppingCartRepository.findById(cartItemId).orElseThrow(() -> new NoSuchElementException("Invalid shopping cart id :" + cartItemId));
    }

    @Override
    public ShoppingCart addProductToShoppingCart(Long productId, Integer quantity) {
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .product(productRepository.findById(productId).orElseThrow(()-> new NoSuchElementException("Product Id not found " + productId)))
                .orderQuantity(quantity)
                .user(userRepository.findById(userService.getCurrentUserId()).orElseThrow(() -> new NoSuchElementException("Invalid user id :" + userService.getCurrentUserId())))
                .build();
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart updateShoppingCartQuantity(Long cartItemId, Integer newQuantity) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartItemId).orElseThrow(() -> new NoSuchElementException("Invalid shopping cart id :" + cartItemId));
        if (newQuantity >= 0) {
            shoppingCart.setOrderQuantity(newQuantity);
            return shoppingCartRepository.save(shoppingCart);
        } else {
            log.error("Invalid quantity :" + newQuantity);
        }
        return null;
    }

    @Override
    public void deleteShoppingCart(Long cartItemId) {
        shoppingCartRepository.findById(cartItemId).orElseThrow(() -> new NoSuchElementException("Invalid shopping cart id :" + cartItemId));
        shoppingCartRepository.deleteById(cartItemId);
    }

    @Override
    public void deleteAllShoppingCart(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found" + userId));
        shoppingCartRepository.deleteAllByByUserUserId(userId);
    }

    @Override
    public void checkout() {

    }
}
