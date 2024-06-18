package com.ra.project_md04_api.repository;

import com.ra.project_md04_api.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IAddressRepository extends JpaRepository<Address, Integer> {
    @Query("select a from addresses a where a.addressId =:addressId")
    Optional<Address> findByAddressId(Long addressId);

    @Query("select a from addresses a where a.user.userId =:userId")
    List<Address> findAllByUserUserId(Long userId);
}
