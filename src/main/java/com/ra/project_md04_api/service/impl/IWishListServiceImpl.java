package com.ra.project_md04_api.service.impl;

import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.entity.Product;
import com.ra.project_md04_api.model.entity.User;
import com.ra.project_md04_api.model.entity.WishList;
import com.ra.project_md04_api.repository.IUserRepository;
import com.ra.project_md04_api.repository.IWishListRepository;
import com.ra.project_md04_api.service.IProductService;
import com.ra.project_md04_api.service.IUserService;
import com.ra.project_md04_api.service.IWishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IWishListServiceImpl implements IWishListService {
    private final IWishListRepository wishListRepository;
    private final IProductService productService;
    private final IUserService userService;
    private final IUserRepository userRepository;

    @Override
    public List<WishList> getAllWishLists() throws CustomException {
        long userId = userService.getCurrentUserId();
        List<WishList> wishLists = wishListRepository.findAllByUserUserId(userId);
        if (wishLists.isEmpty()) {
            throw new CustomException("User don't have any product wish lists", HttpStatus.NOT_FOUND);
        } else {
            return wishLists;
        }
    }

    @Override
    @Transactional
    public void deleteWishList(Long wishListId) throws CustomException {
        Long userId = userService.getCurrentUserId();

        WishList wishList = wishListRepository.findByWishListId(wishListId)
                .orElseThrow(() -> new CustomException("Wish list with ID " + wishListId + " not found", HttpStatus.NOT_FOUND));

        if (!wishList.getUser().getUserId().equals(userId)) {
            throw new CustomException("Wish list with ID " + wishListId + " does not belong to the current user", HttpStatus.FORBIDDEN);
        }

        wishListRepository.delete(wishList);
    }

    @Override
    @Transactional
    public WishList addWishList(Long productId) throws CustomException {
        Long currentUserId = userService.getCurrentUserId();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new CustomException("User not found: " + currentUserId, HttpStatus.NOT_FOUND));

        WishList existingWishList = wishListRepository.findByUserUserIdAndProductProductId(currentUserId, productId);

        if (existingWishList == null) {
            Product product = productService.getProductById(productId);
            WishList newWishList = WishList.builder()
                    .product(product)
                    .user(user)
                    .build();
            return wishListRepository.save(newWishList);
        } else {
            deleteWishList(existingWishList.getWishListId());
            return null;
        }
    }
}
