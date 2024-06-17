package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.model.entity.WishList;
import com.ra.project_md04_api.repository.IUserRepository;
import com.ra.project_md04_api.repository.IWishListRepository;
import com.ra.project_md04_api.service.IProductService;
import com.ra.project_md04_api.service.IUserService;
import com.ra.project_md04_api.service.IWishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class IWishListServiceImpl implements IWishListService {
    private final IWishListRepository wishListRepository;
    private final IProductService productService;
    private final IUserService userService;
    private final IUserRepository userRepository;

    @Override
    public List<WishList> getAllWishLists(Long userId) {
        List<WishList> wishLists = wishListRepository.findAllByUserUserId(userId);
        if (wishLists.isEmpty()) {
            throw new NoSuchElementException("User don't have any product wish lists");
        } else {
            return wishLists;
        }
    }

    @Override
    public void deleteWishList(Long wishListId) {
        wishListRepository.delete(wishListRepository.findByWishListId(wishListId).orElseThrow(() -> new NoSuchElementException("WishList not found: " + wishListId)));
    }

    @Override
    public WishList addWishList(Long productId) {
        WishList wishList = WishList.builder()
                .product(productService.getProductById(productId))
                .user(userRepository.findById(userService.getCurrentUserId()).orElseThrow(() -> new NoSuchElementException("User not found: " + userService.getCurrentUserId())))
                .build();
        return wishListRepository.save(wishList);
    }
}
