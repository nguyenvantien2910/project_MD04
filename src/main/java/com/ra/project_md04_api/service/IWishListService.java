package com.ra.project_md04_api.service;

import com.ra.project_md04_api.model.entity.WishList;

import java.util.List;

public interface IWishListService {
    List<WishList> getAllWishLists(Long userId);
    void deleteWishList(Long wishListId);
    WishList addWishList(Long productId);
}
