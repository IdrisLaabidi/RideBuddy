package com.fst.ridebuddy.controllers;

import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.models.RegisterDto;
import com.fst.ridebuddy.repositories.AppUsersRepository;
import com.fst.ridebuddy.services.AppUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.fst.ridebuddy.models.UpdateProfileDto;

import java.io.IOException;
import java.util.Objects;

@Controller
public class AccountController {

    @Autowired
    private AppUsersRepository repository;
    @Autowired
    private AppUserService userService;
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2 MB

    @GetMapping("/register")
    public String register(Model model) {
        RegisterDto registerDto = new RegisterDto();
        model.addAttribute("registerDto", registerDto);
        model.addAttribute("success", false);
        return "register";
    }

    @PostMapping("/register")
    public String register(
            Model model,
            @Valid @ModelAttribute RegisterDto registerDto,
            BindingResult result) {

        if(Objects.equals(registerDto.getFirstName(), "")) {
            result.addError(new FieldError("registerDto", "firstName", "First name is required"));
        }

        if(Objects.equals(registerDto.getLastName(), "")) {
            result.addError(new FieldError("registerDto", "lastName", "last name is required"));
        }

        if(Objects.equals(registerDto.getEmail(), "")) {
            result.addError(new FieldError("registerDto", "email", "Email is required"));
        }

        if(Objects.equals(registerDto.getPassword(), "")) {
            result.addError(new FieldError("registerDto", "password", "Password is required"));
        }

        if(Objects.equals(registerDto.getConfirmPassword(), "")) {
            result.addError(new FieldError("registerDto", "confirmPassword", "please, confirm password"));
        }

        if(Objects.equals(registerDto.getRole(), "")) {
            result.addError(new FieldError("registerDto", "role", "Role is required"));
        }

        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            result.addError(new FieldError("registerDto", "confirmPassword", "Passwords do not match"));
        }

        AppUser user = repository.findByEmail(registerDto.getEmail());
        if (user != null) {
            result.addError(new FieldError("registerDto", "email", "Email is already in use"));
        }

        // Profile picture validation
        if (registerDto.getProfilePic() == null || registerDto.getProfilePic().isEmpty()) {
            result.addError(new FieldError("registerDto", "profilePic", "Profile picture is required"));
        } else if (!registerDto.getProfilePic().getContentType().startsWith("image")) {
            result.addError(new FieldError("registerDto", "profilePic", "Only image files are allowed"));
        } else if (registerDto.getProfilePic().getSize() > MAX_FILE_SIZE) { // Size limit validation
            result.addError(new FieldError("registerDto", "profilePic",
                    "File size must not exceed " + (MAX_FILE_SIZE / (1024 * 1024)) + " MB"));
        }

        if (result.hasErrors()) {
            return "register";
        }

        try {
            BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
            AppUser newUser = new AppUser();
            newUser.setFirst_name(registerDto.getFirstName());
            newUser.setLast_name(registerDto.getLastName());
            newUser.setRole(registerDto.getRole());
            newUser.setEmail(registerDto.getEmail());
            newUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));

            if (registerDto.getProfilePic() != null && !registerDto.getProfilePic().isEmpty()) {
                newUser.setProfilePic(registerDto.getProfilePic().getBytes());
            }

            repository.save(newUser);
            model.addAttribute("registerDto", new RegisterDto());
            model.addAttribute("success", true);
        } catch (Exception e) {
            result.addError(new FieldError("registerDto", "firstName", e.getMessage()));
        }

        return "register";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        AppUser user = userService.getAuthenticatedUser();
        model.addAttribute("updateProfileDto",UpdateProfileDto.fromUser(user));
        model.addAttribute("success", false);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @ModelAttribute UpdateProfileDto updateProfileDto,
            BindingResult result,
            Model model) {

        AppUser currentUser = userService.getAuthenticatedUser();

        // Update first name if changed
        if (updateProfileDto.getFirstName() != null
                && !updateProfileDto.getFirstName().equals(currentUser.getFirst_name())) {
            currentUser.setFirst_name(updateProfileDto.getFirstName());
        }

        // Update last name if changed
        if (updateProfileDto.getLastName() != null
                && !updateProfileDto.getLastName().equals(currentUser.getLast_name())) {
            currentUser.setLast_name(updateProfileDto.getLastName());
        }

        // Handle password update
        String newPassword = updateProfileDto.getPassword();
        String confirmPassword = updateProfileDto.getConfirmPassword();
        if (newPassword != null && !newPassword.isBlank()) {
            if (!newPassword.equals(confirmPassword)) {
                result.addError(new FieldError("updateProfileDto", "password", "Passwords do not match"));
            } else {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                currentUser.setPassword(passwordEncoder.encode(newPassword));
            }
        }

        // Handle profile picture update
        if (updateProfileDto.getProfilePic() != null && !updateProfileDto.getProfilePic().isEmpty()) {
            try {
                if (!updateProfileDto.getProfilePic().getContentType().startsWith("image")) {
                    result.addError(new FieldError("updateProfileDto", "profilePic", "Only image files are allowed"));
                } else if (updateProfileDto.getProfilePic().getSize() > MAX_FILE_SIZE) { // Size limit validation
                    result.addError(new FieldError("updateProfileDto", "profilePic",
                            "File size must not exceed " + (MAX_FILE_SIZE / (1024 * 1024)) + " MB"));
                } else {
                    currentUser.setProfilePic(updateProfileDto.getProfilePic().getBytes());
                }
            } catch (IOException e) {
                result.addError(new FieldError("updateProfileDto", "profilePic", "Error reading the uploaded file"));
            }
        }

        // Handle validation errors
        if (result.hasErrors()) {
            model.addAttribute("updateProfileDto", updateProfileDto);
            return "profile"; // Return to the profile page with error messages
        }

        // Save updated user details
        try {
            repository.save(currentUser);
            model.addAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error updating profile: " + e.getMessage());
        }

        model.addAttribute("user", currentUser); // Add updated user back to the model
        return "profile"; // Return to the profile page
    }

}
