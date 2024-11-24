package com.fst.ridebuddy.models;

import com.fst.ridebuddy.entities.AppUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class UpdateProfileDto {

    private String firstName;

    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @Size(min = 6, message = "Minimum Password length is 6 characters")
    private String password;

    private String confirmPassword;

    private MultipartFile profilePic;

    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


    public MultipartFile getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(MultipartFile profilePic) {
        this.profilePic = profilePic;
    }

    public static UpdateProfileDto fromUser(AppUser user) {
        UpdateProfileDto dto = new UpdateProfileDto();
        dto.setFirstName(user.getFirst_name());
        dto.setLastName(user.getLast_name());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setPassword(""); // Leave blank for security reasons
        dto.setConfirmPassword("");
        dto.setProfilePic(null);
        return dto;
    }
}
