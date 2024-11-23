package com.fst.ridebuddy.utils;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests ( auth -> auth
                        . requestMatchers ("/") .permitAll ()
                        . requestMatchers ("/contact") .permitAll ()
                        .requestMatchers ("/rides/ ** ") .permitAll ()
                        . requestMatchers ("/register") .permitAll ()
                        . requestMatchers ("/login") .permitAll ()
                        .requestMatchers ("/logout") .permitAll ()
                        . anyRequest () . authenticated ()
                )
                .formLogin (form -> form
                        .defaultSuccessUrl ("/", true)
                )
                .logout (config -> config.logoutSuccessUrl ("/"))
                .build ();
    }
}
