package com.ra.project_md04_api.service;

import com.ra.project_md04_api.model.entity.ShoppingCart;

import java.util.List;

public interface IShoppingCartService {
    List<ShoppingCart> getAllShoppingCart(Long userId);
    ShoppingCart getShoppingCartById(Long id);
    ShoppingCart addProductToShoppingCart(Long productId, Integer quantity);
    ShoppingCart updateShoppingCartQuantity(Long cartItemId,Integer newQuantity);
    void deleteShoppingCart(Long cartItemId);
    void deleteAllShoppingCart(Long userId);
    void checkout();
}
