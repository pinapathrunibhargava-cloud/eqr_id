package com.eqrid.eqrid_website.controller;

import com.eqrid.eqrid_website.model.Police;
import com.eqrid.eqrid_website.model.PoliceAcc;
import com.eqrid.eqrid_website.model.QrDetails;
import com.eqrid.eqrid_website.repository.PoliceAccRepository;
import com.eqrid.eqrid_website.repository.PoliceRepository;
import com.eqrid.eqrid_website.service.QrDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class PoliceController {

    @Autowired
    private PoliceRepository policeRepository;

    @Autowired
    private PoliceAccRepository policeAccRepository;

    @Autowired
    private QrDetailsService qrDetailsService;

     @GetMapping("/police-signup")
    public String showPoliceSignupPage() {
        // Return Thymeleaf template in src/main/resources/templates/
        return "signpolice"; // e.g., signpolice.html
    }
    @PostMapping("/police-signup")
    public String handlePoliceSignup(
            @RequestParam("full_name") String fullName,
            @RequestParam("police_id") String policeId,
            @RequestParam("department") String department,
            @RequestParam("official_email") String officialEmail,
            @RequestParam("password") String password,
            @RequestParam("confirm_password") String confirmPassword,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        // Check password match
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords Mismatch");
            return "redirect:/police-signup";
        }

        // Check if police exists in predefined table
        Police predefinedPolice = policeRepository.findByPoliceIdAndOfficialEmail(policeId, officialEmail);
        if (predefinedPolice == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid Police Details");
            return "redirect:/police-signup";
        }

        // Check if account already exists in police accounts
        if (policeAccRepository.existsByOfficialEmail(officialEmail)) {
            redirectAttributes.addFlashAttribute("error", "email already Exists");
            return "redirect:/police-signup";
        }

        // Check if police ID already used
        if (policeAccRepository.existsByPoliceId(policeId)) {
            redirectAttributes.addFlashAttribute("error", "aacount already exist");
            return "redirect:/police-signup";
        }

        try {
            PoliceAcc acc = new PoliceAcc();
            acc.setFullName(fullName);
            acc.setPoliceId(policeId);
            acc.setDepartment(department);
            acc.setOfficialEmail(officialEmail);
            acc.setPassword(password);
            policeAccRepository.save(acc);

            // Store police details in session after successful signup
            HttpSession session = request.getSession();
            session.setAttribute("policeName", acc.getFullName());
            session.setAttribute("policeId", acc.getPoliceId());
            session.setAttribute("policeDepartment", acc.getDepartment());
            session.setAttribute("userType", "police");

            redirectAttributes.addFlashAttribute("success", "AccountCreated");
            redirectAttributes.addFlashAttribute("type", "police");
            return "redirect:/";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "DatabaseError");
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/police-signup";
        }
    }

    // ADDED: Police login endpoint
    @PostMapping("/police-login")
    public String handlePoliceLogin(
            @RequestParam("official_email") String officialEmail,
            @RequestParam("password") String password,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        
        try {
            PoliceAcc police = policeAccRepository.findByOfficialEmailAndPassword(officialEmail, password);
            if (police != null) {
                // Store police details in session
                HttpSession session = request.getSession();
                session.setAttribute("policeName", police.getFullName());
                session.setAttribute("policeId", police.getPoliceId());
                session.setAttribute("policeDepartment", police.getDepartment());
                session.setAttribute("policeEmail", police.getOfficialEmail());
                session.setAttribute("userType", "police");
                
                return "redirect:/policedashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "InvalidPoliceCredentials");
                return "redirect:/";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "LoginError");
            return "redirect:/";
        }
    }

    // ADDED: Police dashboard endpoint
    @GetMapping("/policedashboard")
    public String policeDashboard(HttpSession session) {
        // Check if police is logged in
        if (session.getAttribute("policeName") == null) {
            return "redirect:/";
        }
       return "policedashboard";

    }

    // ADDED: Police logout endpoint
    @GetMapping("/police-logout")
    public String policeLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    // Search API endpoint - searches only in qr_details table
    @GetMapping("/api/police/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchUser(
            @RequestParam(required = false) String uniqueId,
            @RequestParam(required = false) String vehicleNo,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Check if police is authenticated
        if (session.getAttribute("policeName") == null) {
            response.put("found", false);
            response.put("error", "Unauthorized access. Please login.");
            return ResponseEntity.status(401).body(response);
        }
        
        try {
            Optional<QrDetails> searchResult;
            String searchBy = "";
            
            if (uniqueId != null && !uniqueId.trim().isEmpty()) {
                // Search by Unique ID in qr_details table
                searchResult = qrDetailsService.findByUniqueId(uniqueId.trim());
                searchBy = "Unique ID: " + uniqueId;
            } else if (vehicleNo != null && !vehicleNo.trim().isEmpty()) {
                // Search by Vehicle Number in qr_details table
                searchResult = qrDetailsService.findByVehicleNo(vehicleNo.trim());
                searchBy = "Vehicle Number: " + vehicleNo;
            } else {
                response.put("found", false);
                response.put("error", "Please provide either Unique ID or Vehicle Number");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (searchResult.isPresent()) {
                QrDetails userDetails = searchResult.get();
                response.put("found", true);
                response.put("searchBy", searchBy);
                response.put("userDetails", userDetails);
                response.put("searchedBy", session.getAttribute("policeName") + " (" + session.getAttribute("policeId") + ")");
            } else {
                response.put("found", false);
                response.put("error", "No user found in QR records with " + searchBy);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("found", false);
            response.put("error", "Search failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}