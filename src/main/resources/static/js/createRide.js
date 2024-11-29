const map = L.map('map').setView([36.8, 10.2], 7);
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

let startMarker = null;
let endMarker = null;
let routeLine = null;
let currentStep = 'start';
const apiKey = window.apiKey;

const instructions = document.getElementById('instructions');
const startCoordinateInput = document.getElementById('startCoordinate');
const endCoordinateInput = document.getElementById('endCoordinate');
const resetStartButton = document.getElementById('resetStart');
const resetEndButton = document.getElementById('resetEnd');
const submitButton = document.getElementById('submitButton');

function isValidCoordinate(coord) {
    return coord && coord.lng >= -180 && coord.lng <= 180 && coord.lat >= -90 && coord.lat <= 90;
}


async function fetchRoute(start, end) {
    try {
        if (!isValidCoordinate(start) || !isValidCoordinate(end)) {
            throw new Error('Invalid coordinates provided.');
        }

        const response = await fetch(
            `https://api.openrouteservice.org/v2/directions/driving-car?api_key=${apiKey}&start=${start.lng},${start.lat}&end=${end.lng},${end.lat}`
        );

        if (!response.ok) {
            throw new Error(`API request failed with status ${response.status}: ${response.statusText}`);
        }

        const data = await response.json();

        if (!data.features || !data.features[0] || !data.features[0].geometry) {
            throw new Error('No route found for the provided coordinates.');
        }

        const coordinates = data.features[0].geometry.coordinates.map(coord => [coord[1], coord[0]]);

        // Remove existing route
        if (routeLine) map.removeLayer(routeLine);

        // Draw new route
        routeLine = L.polyline(coordinates, { color: 'blue' }).addTo(map);
        map.fitBounds(routeLine.getBounds());
    } catch (error) {
        console.error('Error fetching route:', error.message);
        alert('Route could not be determined. Please choose different locations.');
    }
}



function setMarker(latlng, isStart) {
    if (isStart) {
        if (startMarker) map.removeLayer(startMarker);
        startMarker = L.marker(latlng, { draggable: true })
            .addTo(map)
            .bindPopup('Start Point')
            .openPopup();
        startCoordinateInput.value = `${latlng.lng},${latlng.lat}`;
        resetStartButton.disabled = false;
    } else {
        if (endMarker) map.removeLayer(endMarker);
        endMarker = L.marker(latlng, { draggable: true })
            .addTo(map)
            .bindPopup('End Point')
            .openPopup();
        endCoordinateInput.value = `${latlng.lng},${latlng.lat}`;
        resetEndButton.disabled = false;
    }
    if (startMarker && endMarker) fetchRoute(startMarker.getLatLng(), endMarker.getLatLng());
}

// Map click handler
map.on('click', function (e) {
    if (currentStep === 'start') {
        setMarker(e.latlng, true);
        instructions.innerText = 'Now click on the map to select your end location.';
        currentStep = 'end';
    } else if (currentStep === 'end') {
        setMarker(e.latlng, false);
        instructions.innerText = 'Both locations selected! Adjust markers if needed, then submit the form.';
    }
});

// Reset buttons
resetStartButton.addEventListener('click', () => {
    if (startMarker) map.removeLayer(startMarker);
    startMarker = null;
    startCoordinateInput.value = '';
    instructions.innerText = 'Click on the map to select your start location.';
    currentStep = 'start';
    resetStartButton.disabled = true;
    if (routeLine) map.removeLayer(routeLine);
});

resetEndButton.addEventListener('click', () => {
    if (endMarker) map.removeLayer(endMarker);
    endMarker = null;
    endCoordinateInput.value = '';
    instructions.innerText = 'Click on the map to select your end location.';
    currentStep = 'end';
    resetEndButton.disabled = true;
    if (routeLine) map.removeLayer(routeLine);
});