package com.fst.ridebuddy.controllers;

import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private AppUserService userService;

    @GetMapping("/profile-pic")
    public ResponseEntity<byte[]> getProfilePic() {
        AppUser user = userService.getAuthenticatedUser();
        if (user != null && user.getProfilePic() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Or MediaType.IMAGE_PNG depending on the format
                    .body(user.getProfilePic());
        }
        // Return a placeholder image if the user has no profile picture
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}


