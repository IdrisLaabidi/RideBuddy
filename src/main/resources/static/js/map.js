// Initialize the map
var apiKey = window.apiKey
var start = window.start
var end = window.end

console.log(start)

var map = L.map('map').setView([36.8, 10.2], 7);
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

// Fetch and display the route using the start and end coordinates passed from the HTML
fetch(`https://api.openrouteservice.org/v2/directions/driving-car?api_key=${apiKey}&start=${start}&end=${end}`)
    .then(response => response.json())
    .then(data => {
        var coords = data.features[0].geometry.coordinates.map(coord => [coord[1], coord[0]]);
        var route = L.polyline(coords, { color: 'blue' }).addTo(map);
        map.fitBounds(route.getBounds());
    })
    .catch(error => console.error('Error fetching route data:', error));
