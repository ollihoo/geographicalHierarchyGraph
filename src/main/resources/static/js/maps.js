var map, currentPosition = {lat: 52.525084, lng: 13.369423};

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: currentPosition,
        zoom: 12
    });
}

function useCurrentPosition(position) {
    currentPosition.lat = position.coords.latitude;
    currentPosition.lng = position.coords.longitude;

    if (map) {
        map.setCenter(currentPosition);
    }
}

if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(useCurrentPosition);
}
