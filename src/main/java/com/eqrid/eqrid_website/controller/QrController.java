package com.eqrid.eqrid_website.controller;

import com.eqrid.eqrid_website.model.QrDetails;
import com.eqrid.eqrid_website.repository.QrDetailsRepository;
import com.eqrid.eqrid_website.service.QrDetailsService;
import com.eqrid.eqrid_website.service.QrService;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class QrController {

    @Autowired
    private QrDetailsService detailsService;

    @Autowired
    private QrService qrService;
@PostMapping("/generateQR")
@ResponseBody
public Map<String, String> generateQR(@ModelAttribute QrDetails details, HttpSession session) {
    Map<String, String> response = new HashMap<>();

    try {
        String fullName = (String) session.getAttribute("username");
        String vehicleNumber = (String) session.getAttribute("vehicleNumber");

        if (fullName == null || vehicleNumber == null) {
            response.put("error", "You must be logged in to generate QR");
            return response;
        }

        details.setFullName(fullName);
        details.setVehicleNo(vehicleNumber);

        Optional<QrDetails> existing = detailsService.findByVehicleNo(vehicleNumber);
        QrDetails saved;

        if (existing.isPresent()) {
            // Update existing record
            QrDetails qrToUpdate = existing.get();
            qrToUpdate.setAge(details.getAge());
            qrToUpdate.setGender(details.getGender());
            qrToUpdate.setBloodGroup(details.getBloodGroup());
            qrToUpdate.setAddress(details.getAddress());
            qrToUpdate.setNearestPoliceStation(details.getNearestPoliceStation());
            qrToUpdate.setEmergencyContact(details.getEmergencyContact());

            saved = detailsService.saveDetails(qrToUpdate);
        } else {
            // Insert new record
            String uniqueId = generateUniqueId(vehicleNumber, fullName);
            details.setUniqueId(uniqueId);
            saved = detailsService.saveDetails(details);
        }

        // Generate QR content
        StringBuilder qrText = new StringBuilder();
        qrText.append("ID: ").append(safe(saved.getUniqueId())).append("\n");
        qrText.append("Name: ").append(safe(saved.getFullName())).append("\n");
        qrText.append("Age: ").append(safe(saved.getAge())).append("\n");
        qrText.append("Vehicle: ").append(safe(saved.getVehicleNo())).append("\n");
        qrText.append("Blood Group: ").append(safe(saved.getBloodGroup())).append("\n");
        qrText.append("Address: ").append(safe(saved.getAddress())).append("\n");
        qrText.append("Nearest Police Station: ").append(safe(saved.getNearestPoliceStation()));

        String qrBase64 = qrService.generateQrCode(qrText.toString());
        saved.setQrImageBase64(qrBase64);
        detailsService.saveDetails(saved);

        response.put("qrImage", "data:image/png;base64," + qrBase64);
        response.put("uniqueId", saved.getUniqueId());

    } catch (Exception e) {
        e.printStackTrace();
        response.put("error", "Failed to generate QR: " + e.getMessage());
    }

    return response;
}



    @GetMapping("/qr/{uniqueId}")
    public String viewQrDetails(@PathVariable String uniqueId, Model model) {
        Optional<QrDetails> opt = detailsService.findByUniqueId(uniqueId);
        if (opt.isEmpty()) {
            model.addAttribute("error", "No details found for this QR.");
            return "qrNotFound"; // create this page
        }
        model.addAttribute("details", opt.get());
        return "qrFullDetails"; // create this template page
    }

    // Generate unique ID from vehicle + name
    private String generateUniqueId(String vehicleNumber, String fullName) {
        if (vehicleNumber == null || vehicleNumber.trim().isEmpty()) vehicleNumber = "UNKNOWN";
        if (fullName == null || fullName.trim().isEmpty()) fullName = "UNK";

        String vehiclePart = vehicleNumber.replaceAll("\\s+", "").toUpperCase();
        String cleanedName = fullName.replaceAll("\\s+", "").toUpperCase();

        String namePart = (cleanedName.length() >= 3)
                ? cleanedName.substring(0, 3)
                : String.format("%-3s", cleanedName).replace(' ', 'X');

        String uniqueId = vehiclePart + namePart;
        return uniqueId.length() > 50 ? uniqueId.substring(0, 50) : uniqueId;
    }

    // Safely clean any null or special characters
    private String safe(Object value) {
        if (value == null) return "N/A";
        String text = value.toString().trim();
        return text.replaceAll("[^\\p{Print}]", "");
    }

@GetMapping("/history")
public String showUserQrHistory(HttpSession session, Model model) {
    String vehicleNumber = (String) session.getAttribute("vehicleNumber");

    if (vehicleNumber == null) {
        return "redirect:/login"; // user not logged in
    }

    Optional<QrDetails> qrOpt = detailsService.findByVehicleNo(vehicleNumber);

    if (qrOpt.isPresent()) {
        model.addAttribute("qrDetails", qrOpt.get());
    } else {
        model.addAttribute("message", "No QR code generated yet for this vehicle.");
    }

    return "history"; // Thymeleaf template
}

}
