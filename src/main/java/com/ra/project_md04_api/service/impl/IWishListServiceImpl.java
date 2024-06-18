package com.ra.project_md04_api.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<WishList> getAllWishLists() {
        long userId = userService.getCurrentUserId();
        List<WishList> wishLists = wishListRepository.findAllByUserUserId(userId);
        if (wishLists.isEmpty()) {
            throw new NoSuchElementException("User don't have any product wish lists");
        } else {
            return wishLists;
        }
    }

    @Override
    @Transactional
    public void deleteWishList(Long wishListId) {
        wishListRepository.delete(wishListRepository.findByWishListId(wishListId).orElseThrow(() -> new NoSuchElementException("WishList not found: " + wishListId)));
    }

    @Override
    @Transactional
    public WishList addWishList(Long productId) {
        // get current user
        User user = userRepository.findById(userService.getCurrentUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userService.getCurrentUserId()));

        // check wish list da ton tai hay chua
        WishList existingWishList = getAllWishLists()
                .stream()
                .filter(w -> w.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingWishList == null) {
            // tao moi
            Product product = productService.getProductById(productId);
            if (product == null) {
                throw new NoSuchElementException("Product not found with id: " + productId);
            }
            WishList newWishList = WishList.builder()
                    .product(product)
                    .user(user)
                    .build();
            return wishListRepository.save(newWishList);
        } else {
            // da ton tai thi xoa
            deleteWishList(existingWishList.getWishListId());
            return null;
        }
    }
}
