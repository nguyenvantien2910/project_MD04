package com.ra.project_md04_api.model.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", unique = true, nullable = false)
    private Long addressId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "full_address")
    @Size(max = 255, message = "Max character is 255 !")
    private String fullAddress;

    @Column(name = "phone")
    @Size(max = 15, message = "Max character is 15 !")
    private String phoneNumber;

    @Column(name = "receive_name")
    @Size(max = 50, message = "Max character is 50 !")
    private String receiveName;
}
