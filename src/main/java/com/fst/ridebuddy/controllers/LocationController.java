package com.fst.ridebuddy.controllers;

import com.fst.ridebuddy.models.LocationDto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/save-location")
public class LocationController {

    @PostMapping
    public String saveLocation(@RequestBody LocationDto location) {

        System.out.println("Stored location: Latitude = " + location.getLatitude() + ", Longitude = " + location.getLongitude());

        return "{ \"status\": \"success\", \"message\": \"Coordinates saved locally!\" }";
    }

}
