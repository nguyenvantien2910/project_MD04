package com.ra.project_md04_api.model.entity;

import com.ra.project_md04_api.validator.PhoneExist;
import com.ra.project_md04_api.validator.ValidUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",nullable = false, unique = true)
    private Long userId;

    @Column(unique = true, nullable = false, length = 100)
    @Size(min = 4, max = 100, message = "Username must be between 6 and 100 characters!")
    @ValidUsername
    private String username;

    @Column(nullable = false)
    @Pattern(regexp = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$", message = "Invalid email format!")
    private String email;

    @Column(nullable = false, length = 100)
    private String fullName;

    private Boolean status;

    @Column(nullable = false)
    private String password;

    private String avatar;

    @Column(nullable = false, unique = true, length = 15)
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", message = "Invalid phone format!")
    @PhoneExist
    private String phone;

    @Column(nullable = false)
    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
}
