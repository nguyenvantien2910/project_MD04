package com.ra.project_md04_api.model.entity;

import com.ra.project_md04_api.constants.RoleName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id",unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;
}
