package com.fst.ridebuddy.controllers;

import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    private AppUserService userService;

    private final String apiKey = System.getenv("ORS_token");

    @GetMapping({"","/"})
    public String home(Model model) {
        AppUser user = userService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "index";
    }

    @GetMapping("/contact")
    public String contact () {
        return "contact";
    }

    @GetMapping("/ride-details/{id}")
    public String rideDetail (Model model, @PathVariable Long id) {
        AppUser user = userService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        String startCoordinates = "10.294355505265166,36.847999099999996"; // example start
        String endCoordinates = "10.3181518,36.8348312"; // example end
        model.addAttribute("start", startCoordinates);
        model.addAttribute("end", endCoordinates);
        model.addAttribute("apiKey", apiKey);
        return "rideDetails";
    }
}
