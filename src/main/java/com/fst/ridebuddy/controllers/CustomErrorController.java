package com.fst.ridebuddy.controllers;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    @GetMapping
    public String handleError(WebRequest webRequest, Model model) {
        Map<String, Object> errors = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());
        Integer status = (Integer) errors.get("status");

        model.addAttribute("status", status);
        model.addAttribute("message", errors.get("message"));

        if (status == 404) {
            return "error/404"; // Return 404.html for 404 errors
        } else if (status == 500) {
            return "error/500"; // Return 500.html for 500 errors
        }
        return "error/general"; // Fallback for other errors
    }

    public String getErrorPath() {
        return "/error";
    }
}
