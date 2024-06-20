package com.ra.project_md04_api.repository;

import com.ra.project_md04_api.constants.RoleName;
import com.ra.project_md04_api.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {
//    @Query("select r from Role r where r.name =: roleName")
    Optional<Role> findByRoleName(RoleName roleName);
}
