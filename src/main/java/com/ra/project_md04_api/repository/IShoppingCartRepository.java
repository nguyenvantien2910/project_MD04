package com.ra.project_md04_api.repository;

import com.ra.project_md04_api.model.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("select sp from shopping_cart sp where sp.user.userId = :userId")
    List<ShoppingCart> findAllByUserUserId(Long userId);

    @Query("delete from shopping_cart sp where sp.user.userId = :userId")
    void deleteAllByByUserUserId(Long userId);
}
