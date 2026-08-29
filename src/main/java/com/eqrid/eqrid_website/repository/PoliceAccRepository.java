package com.eqrid.eqrid_website.repository;

import com.eqrid.eqrid_website.model.PoliceAcc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoliceAccRepository extends JpaRepository<PoliceAcc, Long> {
    
    // Check if email already exists
    boolean existsByOfficialEmail(String officialEmail);
    
    // Find police by email and password for login
    PoliceAcc findByOfficialEmailAndPassword(String officialEmail, String password);
    
    // Check if police exists with given ID and email (for signup validation)
    boolean existsByPoliceIdAndOfficialEmail(String policeId, String officialEmail);
    
    // Optional: Find by police ID
    PoliceAcc findByPoliceId(String policeId);
    
    // Optional: Check if police ID already exists
    boolean existsByPoliceId(String policeId);
}