
// Check if geolocation is available in the browser
if ("geolocation" in navigator) {
    navigator.geolocation.getCurrentPosition(function(position) {
        // Get latitude and longitude
        var lat = position.coords.latitude;
        var lon = position.coords.longitude;

        // Redirect to the rides page with location info
        redirectToRides(lat, lon);
    }, function(error) {
        console.error("Error getting location: " + error.message);
    });
} else {
    console.log("Geolocation is not supported by this browser.");
}

function redirectToRides(lat, lon) {
    // Construct the URL with the latitude and longitude
    const locationPath = `/rides/rides-near-me/${lat},${lon}`;

    // Redirect to the new URL
    window.location.href = locationPath;
}