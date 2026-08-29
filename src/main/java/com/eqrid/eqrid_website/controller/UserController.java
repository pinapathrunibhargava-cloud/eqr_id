package com.eqrid.eqrid_website.controller;

import com.eqrid.eqrid_website.model.User;
import com.eqrid.eqrid_website.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
 public String home() {
        return "index"; // Loads templates/index.html
    }


    @GetMapping("/signup")
    public String signup() {
        return "signup"; // Loads templates/signup.html
    }

    // === HANDLE SIGNUP ===
    @PostMapping("/signup")
    public String handleSignup(
            @RequestParam("full_name") String fullName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirm_password") String confirmPassword,
            @RequestParam("phone_number") String phoneNumber,
            @RequestParam("vehicle_number") String vehicleNumber,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            // --- VALIDATION ---
            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
                 return "redirect:/signup";
            }

            if (userRepository.existsByEmail(email)) {
                redirectAttributes.addFlashAttribute("error", "Email already exists.");
               return "redirect:/signup";
            }

            if (userRepository.existsByVehicleNumber(vehicleNumber)) {
                redirectAttributes.addFlashAttribute("error", "Vehicle number already exists.");
               return "redirect:/signup";
            }

            // --- SAVE USER ---
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPassword(password);
            user.setPhoneNumber(phoneNumber);
            user.setVehicleNumber(vehicleNumber);

            User savedUser = userRepository.save(user);

            // --- CREATE SESSION ---
            HttpSession session = request.getSession();
            session.setAttribute("username", savedUser.getFullName());
            session.setAttribute("email", savedUser.getEmail());
            session.setAttribute("vehicleNumber", savedUser.getVehicleNumber());
            session.setAttribute("userId", savedUser.getId());
            session.setAttribute("userType", "user");

            redirectAttributes.addFlashAttribute("success", "Account created successfully!");
            return "redirect:/"; // Go back to homepage
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Something went wrong while signing up.");
            return "redirect:/signup";
        }
    }

    // === HANDLE LOGIN ===
    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        try {
            User user = userRepository.findByEmailAndPassword(email, password);

            if (user != null) {
                // Create session
                HttpSession session = request.getSession();
                session.setAttribute("username", user.getFullName());
                session.setAttribute("email", user.getEmail());
                session.setAttribute("vehicleNumber", user.getVehicleNumber());
                session.setAttribute("userId", user.getId());
                session.setAttribute("userType", "user");

                return "redirect:/userhome";
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid email or password.");
                return "redirect:/";
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Login error. Please try again.");
            return "redirect:/";
        }
    }

    // === USER HOME PAGE ===
    @GetMapping("/userhome")
    public String userHome(HttpSession session) {
       if (session.getAttribute("username") == null) {
            return "redirect:/"; // redirect to home if not logged in
        }
        return "userhome"; // Loads templates/userhome.html
    }

    // === LOGOUT ===
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
    
}
