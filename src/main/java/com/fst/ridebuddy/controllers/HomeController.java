package com.fst.ridebuddy.controllers;

import com.fst.ridebuddy.entities.AppUser;
import com.fst.ridebuddy.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private AppUserService userService;

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


}
