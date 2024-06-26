package com.ra.project_md04_api.controller.admin;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.exception.CustomException;
import com.ra.project_md04_api.model.dto.request.RevenueRequest;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/sales-revenue-over-time")
@RequiredArgsConstructor
public class SaleController {
    private final IOrderService orderService;

    @GetMapping
    public ResponseEntity<?> getSalesRevenueOverTime(@Valid @RequestBody RevenueRequest revenueRequest) throws CustomException {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(orderService.getSalesRevenueOverTime(revenueRequest))
                        .build()
                , HttpStatus.OK);
    }
}
