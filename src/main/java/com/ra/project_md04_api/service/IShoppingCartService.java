package com.ra.project_md04_api.service;

import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.FormCheckout;
import com.ra.project_md04_api.model.dto.response.ShoppingCartResponse;
import com.ra.project_md04_api.model.entity.ShoppingCart;

import java.util.List;

public interface IShoppingCartService {
    List<ShoppingCart> getAllShoppingCart() throws CustomException;
    ShoppingCart addProductToShoppingCart(Long productId, Integer quantity) throws CustomException;
    ShoppingCartResponse updateShoppingCartQuantity(Long cartItemId, Integer newQuantity) throws CustomException;
    void deleteShoppingCart(Long cartItemId) throws CustomException;
    void deleteAllShoppingCart(Long userId) throws CustomException;
    Boolean checkoutCartItem(FormCheckout formCheckout) throws CustomException;
}
