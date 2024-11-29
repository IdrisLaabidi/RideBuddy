package com.fst.ridebuddy.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS setup
                .authorizeHttpRequests(auth -> auth
                        // Routes accessible to everyone
                        .requestMatchers("/resources/**", "/static/**", "/photos/**").permitAll()

                        .requestMatchers("/", "/contact", "/register", "/login").permitAll()

                        // Authenticated users
                        .requestMatchers("/profile").authenticated()
                        .requestMatchers("/reservations/manage").authenticated()

                        // Routes for PASSENGER role
                        .requestMatchers(
                                "/reservations/update/{id}",
                                "/reservations/create",
                                "/reservations/create/{id}",
                                "/reservations/delete/{id}",
                                "/rides/rides-near-me",
                                "/rides/rides-near-me/{cords}",
                                "/rides/ride-details/{id}",
                                "/rides/all-rides"
                        ).hasRole("PASSENGER")

                        // Routes for CONDUCTOR role
                        .requestMatchers(
                                "/rides/create",
                                "/rides/ride-visualize/{id}",
                                "/rides/manage/edit/{id}",
                                "/rides/manage/delete/{id}",
                                "/rides/myRides",
                                "rides/manage/make-over/",
                                "/reservations/manage/passengerReservations",
                                "/reservations/accept/{id}",
                                "/reservations/status/{id}",
                                "/rides/create-select",
                                "/rate/{rideId}"
                        ).hasRole("CONDUCTOR")

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Custom login page URL
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(config -> config
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

