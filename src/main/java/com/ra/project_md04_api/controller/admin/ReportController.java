package com.ra.project_md04_api.controller.admin;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.RevenueRequest;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.service.IOrderService;
import com.ra.project_md04_api.service.IProductService;
import com.ra.project_md04_api.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api.myservice.com/v1/admin/reports")
@RequiredArgsConstructor
public class ReportController {
    private final IProductService productService;
    private final IUserService userService;
    private final IOrderService orderService;

    @GetMapping("/best-seller-products")
    public ResponseEntity<?> getBestSellerProducts(@Valid @RequestBody RevenueRequest revenueRequest) throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.getBestSellerProductsFromAndTo(revenueRequest))
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/most-liked-products")
    public ResponseEntity<?> getMostLikedProducts() throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.getFeaturedProducts())
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/revenue-by-category")
    public ResponseEntity<?> getRevenueByCategory() throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(orderService.getRevenueByCategory())
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/new-accounts-this-month")
    public ResponseEntity<?> getNewAccountsThisMonth() {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.getNewAccountsThisMonth())
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/invoices-over-time")
    public ResponseEntity<?> getInvoicesOverTime(@Valid @RequestBody RevenueRequest revenueRequest) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data("Tổng số đơn hàng trong khoảng " + revenueRequest.getFrom() + " đến " + revenueRequest.getTo() + " là: " + orderService.CountAllByCreatedAtBetween(revenueRequest) + " đơn hàng")
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("/top-spending-customers")
    public ResponseEntity<?> getTopSpendingCustomers(@Valid @RequestBody RevenueRequest revenueRequest) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(orderService.getTopSpendingCustomers(revenueRequest))
                        .build()
                , HttpStatus.OK);
    }
}