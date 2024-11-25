package com.fst.ridebuddy.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeoCodingService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getCoordinates(String cityName) {
        String url = "https://nominatim.openstreetmap.org/search?q=" + cityName + "&format=json";
        String response = restTemplate.getForObject(url, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);

            JsonNode bestMatch = root.elements().next();
            for (JsonNode node : root) {
                if (node.path("importance").asDouble() > bestMatch.path("importance").asDouble()) {
                    bestMatch = node;
                }
            }

            String lat = bestMatch.path("lat").asText();
            String lon = bestMatch.path("lon").asText();
            return lon + "," + lat;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing geocoding response";
        }
    }
}
