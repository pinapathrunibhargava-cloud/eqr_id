package com.eqrid.eqrid_website.repository;

import com.eqrid.eqrid_website.model.QrDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface QrDetailsRepository extends JpaRepository<QrDetails, String> {
	Optional<QrDetails> findByUniqueId(String uniqueId);
	Optional<QrDetails> findByVehicleNo(String vehicleNo);
}
