package com.ra.project_md04_api.controller.admin;


import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @GetMapping
    public ResponseEntity<?> findAll() throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(orderService.getAllOrders())
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/status/{orderStatus}")
    public ResponseEntity<?> findAllOrdersByStatus(@PathVariable("orderStatus") String orderStatus) throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(orderService.adminGetALlOrdersByStatus(orderStatus))
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> findOrderById(@PathVariable("orderId") Long orderId) throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(orderService.getOrderDetailByOrderId(orderId))
                        .build()
                , HttpStatus.OK);
    }

    @PutMapping("/change-status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@RequestParam String orderStatus, @PathVariable("orderId") Long orderId) throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(orderService.updateOrderStatus(orderStatus, orderId))
                        .build()
                , HttpStatus.OK);
    }
}
