package com.ra.project_md04_api.controller.user;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.service.IOrderService;
import com.ra.project_md04_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/user/history")
@RequiredArgsConstructor
public class UserOrderController {
    private final IOrderService orderService;
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUserOrders() {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(orderService.getALlOrdersByUserId(userService.getCurrentUserId()))
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/{orderStatus}")
    public ResponseEntity<?> getUserOrderStatus(@PathVariable("orderStatus") String orderStatus) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(orderService.getALlOrdersByStatus(orderStatus))
                        .build()
                , HttpStatus.OK);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable("orderId") Long orderId) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(orderService.cancelOrder(orderId))
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/{serialNumber}")
    public ResponseEntity<?> getUserOrderBySerialNumber(@PathVariable("serialNumber") String serialNumber) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(orderService.getUserOrderBySerialNumber(serialNumber))
                        .build()
                , HttpStatus.OK);
    }
}
