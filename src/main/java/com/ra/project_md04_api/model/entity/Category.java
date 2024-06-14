package com.ra.project_md04_api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", unique = true, nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    @Max(value = 100,message = "Max lenght of category name is 100 characters")
    private String categoryName;

    private String description;

    private Boolean status;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Product> products;
}
