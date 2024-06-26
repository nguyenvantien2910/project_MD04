package com.ra.project_md04_api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "order_details_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "product_id")
    private Product product;

    @Column(name = "name")
    @Size(max = 100,message = "Max character is 100 !")
    private String productName;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "order_quantity")
    @Min(value = 0,message = "Order quantity must greater than 0 !")
    private Integer orderQuantity;
}
