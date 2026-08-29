package com.eqrid.eqrid_website.service;

import com.eqrid.eqrid_website.model.QrDetails;
import com.eqrid.eqrid_website.repository.QrDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QrDetailsService {

    @Autowired
    private QrDetailsRepository repository;

    public QrDetails saveDetails(QrDetails details) {
        // Generate unique ID (vehicleNo + first 3 letters of name)
        String uniqueId = details.getVehicleNo() + "_" +
                details.getFullName().substring(0, Math.min(3, details.getFullName().length())).toUpperCase();
        details.setUniqueId(uniqueId);
        return repository.save(details);
    }

    public java.util.Optional<QrDetails> findByUniqueId(String uniqueId) {
        return repository.findByUniqueId(uniqueId);
    }

    public java.util.Optional<QrDetails> findByVehicleNo(String vehicleNo) {
        return repository.findByVehicleNo(vehicleNo);
    }
}
