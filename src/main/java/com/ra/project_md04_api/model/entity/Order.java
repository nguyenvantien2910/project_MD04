package com.ra.project_md04_api.model.entity;

import com.ra.project_md04_api.constants.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", unique = true, nullable = false)
    private Long orderId;

    @Column(nullable = false,length = 100)
    @Size(max = 100, message = "Max lenght of serial number is 100 characters!")
    private String serialNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "note")
    @Size(max = 100, message = "Max character is 100 !")
    private String note;

    @Column(name = "receive_name")
    @Size(max = 100, message = "Max character is 100 !")
    private String receiveName;

    @Column(name = "receive_address")
    @Size(max = 255, message = "Max character is 255 !")
    private String receiveAddress;

    @Column(name = "receive_phone")
    @Size(max = 15, message = "Max character is 15 !")
    private String receivePhone;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @Column(name = "received_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date receivedAt;
}
