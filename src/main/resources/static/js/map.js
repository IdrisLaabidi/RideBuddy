// Initialize the map
var apiKey = window.apiKey;
var start = window.start; // "longitude,latitude" format
var end = window.end;     // "longitude,latitude" format

var map = L.map('map').setView([36.8, 10.2], 7); // Default center for Tunisia
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

var startCoords = start.split(",").map(Number);
var endCoords = end.split(",").map(Number);

L.marker([startCoords[1], startCoords[0]], { title: "Start Location" })
    .addTo(map)
    .bindPopup("<b>Start:</b> Your starting point")
    .openPopup();

L.marker([endCoords[1], endCoords[0]], { title: "End Location" })
    .addTo(map)
    .bindPopup("<b>End:</b> Your destination");

fetch(`https://api.openrouteservice.org/v2/directions/driving-car?api_key=${apiKey}&start=${start}&end=${end}`)
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log("Route Data:", data); // Debug API response
        if (!data.features || data.features.length === 0) {
            console.error("No route found in response");
            return;
        }

        var coords = data.features[0].geometry.coordinates.map(coord => [coord[1], coord[0]]);

        var route = L.polyline(coords, { color: 'blue', weight: 5 }).addTo(map);

        map.fitBounds(route.getBounds());
    })
    .catch(error => console.error('Error fetching route data:', error));
