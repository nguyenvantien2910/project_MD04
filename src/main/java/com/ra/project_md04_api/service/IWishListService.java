package com.ra.project_md04_api.service;

import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.entity.WishList;

import java.util.List;

public interface IWishListService {
    List<WishList> getAllWishLists() throws CustomException;
    void deleteWishList(Long wishListId) throws CustomException;
    WishList addWishList(Long productId) throws CustomException;
}
