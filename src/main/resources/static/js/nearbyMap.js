// Map initialization
var map = L.map('map').setView([36.8, 10.2], 7); // Default center for Tunisia
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

// Load rides from global variable
var rides = window.rides || [];
console.log("Rides passed to map:", rides);

if (rides.length === 0) {
    console.log("No rides to display.");
} else {
    // Add markers for the first 5 rides
    rides.slice(0, 5).forEach(function(ride) {
        try {
            var coords = ride.startCoordinate.split(",");
            var lon = parseFloat(coords[0]);
            var lat = parseFloat(coords[1]);

            // Validate coordinates
            if (coords.length !== 2 || isNaN(lon) || isNaN(lat)) {
                console.error("Invalid coordinates for ride:", ride);
                return;
            }

            L.marker([lat, lon]).addTo(map)
                .bindPopup(
                    `<b>From:</b> ${ride.departureLocation}<br>
                     <b>To:</b> ${ride.destination}<br>
                     <b>Time:</b> ${ride.departureTime}<br>
                     <b>Places:</b> ${ride.availablePlaces}`
                );
        } catch (error) {
            console.error("Error processing ride:", ride, error);
        }
    });

    // Fit map bounds to show all markers
    var bounds = rides.slice(0, 5).map(function(ride) {
        var coords = ride.startCoordinate.split(",");
        return [parseFloat(coords[1]), parseFloat(coords[0])]; // [latitude, longitude]
    });
    if (bounds.length) {
        map.fitBounds(bounds);
    }
}
