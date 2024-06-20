package com.ra.project_md04_api.controller.user;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.FormAddShoppingCart;
import com.ra.project_md04_api.model.dto.request.FormCheckout;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.service.IShoppingCartService;
import com.ra.project_md04_api.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/user/cart")
@RequiredArgsConstructor
public class UserCartController {
    private final IShoppingCartService shoppingCartService;
    private final IUserService userService;

    @GetMapping("/list")
    public ResponseEntity<?> getShoppingCartList() throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(shoppingCartService.getAllShoppingCart())
                        .build()
                , HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addShoppingCart(@Valid @RequestBody FormAddShoppingCart formAddShoppingCart) throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(shoppingCartService.addProductToShoppingCart(formAddShoppingCart.getProductId(), formAddShoppingCart.getQuantity()))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<?> updateCartItemQuantity(@RequestParam Integer quantity, @PathVariable Long cartItemId) throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(shoppingCartService.updateShoppingCartQuantity(cartItemId, quantity))
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId) throws CustomException {
        shoppingCartService.deleteShoppingCart(cartItemId);
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data("Delete successfully!")
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> deleteAllCartItem() throws CustomException {
        shoppingCartService.deleteAllShoppingCart(userService.getCurrentUserId());
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data("Delete successfully!")
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@Valid @RequestBody FormCheckout formCheckout) throws CustomException {
        boolean isCheckOutSuccess = shoppingCartService.checkoutCartItem(formCheckout);
        if (isCheckOutSuccess) {
            return new ResponseEntity<>(
                    ResponseWrapper.builder()
                            .eHttpStatus(EHttpStatus.SUCCESS)
                            .statusCode(HttpStatus.OK.value())
                            .data("Checkout successfully!")
                            .build(),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    ResponseWrapper.builder()
                            .eHttpStatus(EHttpStatus.SUCCESS)
                            .statusCode(HttpStatus.OK.value())
                            .data("Checkout failed!")
                            .build(),
                    HttpStatus.OK
            );
        }
    }
}
