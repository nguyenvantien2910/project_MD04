package com.ra.project_md04_api.service;

import com.ra.project_md04_api.model.dto.request.FormCheckout;
import com.ra.project_md04_api.model.entity.ShoppingCart;

import java.util.List;

public interface IShoppingCartService {
    List<ShoppingCart> getAllShoppingCart(Long userId);
    ShoppingCart addProductToShoppingCart(Long productId, Integer quantity);
    ShoppingCart updateShoppingCartQuantity(Long cartItemId,Integer newQuantity);
    void deleteShoppingCart(Long cartItemId);
    void deleteAllShoppingCart(Long userId);
    Boolean checkoutCartItem(FormCheckout formCheckout);
}
