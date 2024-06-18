package com.ra.project_md04_api.controller.admin;

import com.ra.project_md04_api.constants.EHttpStatus;
import com.ra.project_md04_api.model.dto.request.RevenueRequest;
import com.ra.project_md04_api.model.dto.response.ResponseWrapper;
import com.ra.project_md04_api.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/sales-revenue-over-time")
@RequiredArgsConstructor
public class SaleController {
    private final IOrderService orderService;

    @GetMapping
    public ResponseEntity<?> getSalesRevenueOverTime(@Valid @RequestParam("from") Date from,
                                                     @Valid @RequestParam("to") Date to) {
        RevenueRequest request = RevenueRequest.builder()
                .from(from)
                .to(to)
                .build();

        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(orderService.getSalesRevenueOverTime(request))
                        .build()
                , HttpStatus.OK);
    }
}
