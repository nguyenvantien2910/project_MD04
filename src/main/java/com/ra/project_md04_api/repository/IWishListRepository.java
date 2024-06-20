package com.ra.project_md04_api.repository;

import com.ra.project_md04_api.model.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IWishListRepository extends JpaRepository<WishList, Integer> {
    @Query("select w from wish_list w where w.user.userId = :userId")
    List<WishList> findAllByUserUserId(Long userId);

    @Query("select w from wish_list w where w.wishListId = :wishlistId")
    Optional<WishList> findByWishListId(Long wishlistId);

    WishList findByUserUserIdAndProductProductId(Long userId, Long productId);
}
