package com.eqrid.eqrid_website.repository;

import com.eqrid.eqrid_website.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
     boolean existsByVehicleNumber(String vehicleNumber);
}