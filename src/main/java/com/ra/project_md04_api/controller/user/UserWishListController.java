package com.ra.project_md04_api.controller.user;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
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
    public ResponseEntity<?> getUserWishList(@RequestParam Long userId) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(wishListService.getAllWishLists(userId))
                        .build()
                , HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addProductToWishList(@RequestBody Long productId) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(wishListService.addWishList(productId))
                        .build()
                , HttpStatus.OK);
    }

    @DeleteMapping("/{wishListId}")
    public ResponseEntity<?> deleteProductFromWishList(@PathVariable Long wishListId) {
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
