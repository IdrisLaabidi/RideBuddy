package com.fst.ridebuddy.services;

import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.repositories.AppUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements UserDetailsService {
    @Autowired
    private AppUsersRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieve user by email
        AppUser user = repo.findByEmail(email);

        // Handle case where the user is not found
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Ensure email is verified before allowing login
        if (!user.isEmailVerified()) {
            throw new UsernameNotFoundException("Email not verified for: " + email);
        }

        // Return UserDetails object for Spring Security
        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public AppUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return repo.findByEmail(userDetails.getUsername());
    }
}
