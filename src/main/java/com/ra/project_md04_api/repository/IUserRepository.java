package com.ra.project_md04_api.repository;

import com.ra.project_md04_api.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Boolean existsByPhone(String phone);

    Boolean existsByUsername(String username);

    @Query("select u from User u where u.username = :userName")
    Optional<User> findUserByUsername(String userName);

    @Query("select u from User u where u.fullName like concat('%',:searchName,'%')")
    Page<User> findUserByUsernameAndSorting(String searchName, Pageable pageable);
}
