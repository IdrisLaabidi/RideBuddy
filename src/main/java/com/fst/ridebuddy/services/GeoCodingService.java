package com.fst.ridebuddy.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeoCodingService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String GEOCODING_API_URL = "https://api.openrouteservice.org/geocode/reverse?api_key={apiKey}&point.lat={lat}&point.lon={lon}";

    private final String apiKey = System.getenv("ORS_token");

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

    public String getAddressFromCoordinates(String coordinates) {
        String[] coords = coordinates.split(",");
        if (coords.length != 2) {
            throw new IllegalArgumentException("Invalid coordinates format. Expected 'latitude,longitude'");
        }

        String lat = coords[1].trim();
        String lon = coords[0].trim();

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    GEOCODING_API_URL,
                    String.class,
                    apiKey,
                    lat,
                    lon
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode features = root.path("features");

                if (features.isArray() && features.size() > 0) {
                    // Logic to select the best result
                    JsonNode bestFeature = selectBestFeature(features);
                    if (bestFeature != null) {
                        return bestFeature.path("properties").path("label").asText();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching address from coordinates", e);
        }
        throw new RuntimeException("Could not determine address");
    }

    /**
     * Selects the best feature based on confidence and distance.
     */
    private JsonNode selectBestFeature(JsonNode features) {
        JsonNode bestFeature = null;
        double highestConfidence = 0.0;
        double lowestDistance = Double.MAX_VALUE;

        for (JsonNode feature : features) {
            double confidence = feature.path("properties").path("confidence").asDouble(0.0);
            double distance = feature.path("properties").path("distance").asDouble(Double.MAX_VALUE);

            // Prioritize high confidence and low distance
            if (confidence > highestConfidence ||
                    (confidence == highestConfidence && distance < lowestDistance)) {
                bestFeature = feature;
                highestConfidence = confidence;
                lowestDistance = distance;
            }
        }
        return bestFeature;
    }
}

