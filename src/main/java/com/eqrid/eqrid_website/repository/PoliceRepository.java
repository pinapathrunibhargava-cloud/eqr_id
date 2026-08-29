package com.eqrid.eqrid_website.repository;

import com.eqrid.eqrid_website.model.Police;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoliceRepository extends JpaRepository<Police, String> {

    // Find a predefined police record by policeId and email (used to validate police signup)
    Police findByPoliceIdAndOfficialEmail(String policeId, String officialEmail);
}