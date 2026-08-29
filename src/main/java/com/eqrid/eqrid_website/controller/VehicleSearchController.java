package com.eqrid.eqrid_website.controller;

import com.eqrid.eqrid_website.model.QrDetails;
import com.eqrid.eqrid_website.repository.QrDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class VehicleSearchController {

    @Autowired
    private QrDetailsRepository qrDetailsRepository;

    @PostMapping("/search-vehicle")
    public String searchVehicle(@RequestParam(required = false, name = "vehicle_number") String vehicleNumberParam,
                                @RequestParam(required = false, name = "vehicleNo") String vehicleNoParam,
                                Model model) {

        String vehicleNo = null;
        if (vehicleNumberParam != null && !vehicleNumberParam.trim().isEmpty()) {
            vehicleNo = vehicleNumberParam.trim().toUpperCase();
        } else if (vehicleNoParam != null && !vehicleNoParam.trim().isEmpty()) {
            vehicleNo = vehicleNoParam.trim().toUpperCase();
        }

        if (vehicleNo == null || vehicleNo.isEmpty()) {
            model.addAttribute("vehicleError", "Please provide a vehicle number");
            return "index";
        }

        Optional<QrDetails> qrDetailsOpt = qrDetailsRepository.findByVehicleNo(vehicleNo);

        if (qrDetailsOpt.isPresent()) {
            QrDetails qrDetails = qrDetailsOpt.get();

            // Send only limited fields (use existing getters)
            model.addAttribute("uniqueId", qrDetails.getUniqueId());
            model.addAttribute("fullName", qrDetails.getFullName());
            model.addAttribute("age", qrDetails.getAge());
            model.addAttribute("bloodGroup", qrDetails.getBloodGroup());
            model.addAttribute("address", qrDetails.getAddress());
            model.addAttribute("nearestPoliceStation", qrDetails.getNearestPoliceStation());

            return "limited_info"; // limited_info.html
        } else {
            model.addAttribute("vehicleError", "Vehicle not found!");
            return "limited_info"; // show error on limited_info page
        }
    }
}
