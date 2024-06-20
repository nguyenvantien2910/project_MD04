package com.ra.project_md04_api.controller.user;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.model.entity.WishList;
import com.ra.project_md04_api.service.IWishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/user/wish-list")
@RequiredArgsConstructor
public class UserWishListController {
    private final IWishListService wishListService;

    @GetMapping
    public ResponseEntity<?> getUserWishList() throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(wishListService.getAllWishLists())
                        .build()
                , HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addProductToWishList(@RequestParam Long productId) throws CustomException {
        WishList wishList = wishListService.addWishList(productId);
        if (wishList == null) {
            return new ResponseEntity<>(
                    ResponseWrapper.builder()
                            .eHttpStatus(EHttpStatus.SUCCESS)
                            .statusCode(HttpStatus.OK.value())
                            .data("UnBookmarked successfully")
                            .build()
                    , HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    ResponseWrapper.builder()
                            .eHttpStatus(EHttpStatus.SUCCESS)
                            .statusCode(HttpStatus.OK.value())
                            .data("Bookmarked successfully")
                            .build()
                    , HttpStatus.OK);
        }
    }

    @DeleteMapping("/{wishListId}")
    public ResponseEntity<?> deleteProductFromWishList(@PathVariable Long wishListId) throws CustomException {
        wishListService.deleteWishList(wishListId);
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data("Delete successfully")
                        .build()
                , HttpStatus.OK);
    }
}
