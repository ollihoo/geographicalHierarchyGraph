var LoadCurrentPosition = React.createClass({
  currentMarker: null,
  currentCoordinate: null,
  tour: [],
  getInitialState: function() {
    this.currentCoordinate = {lat: 52.525084, lng: 13.369423};
    this.currentMarker = new google.maps.Marker({
                             map: map,
                             draggable: true,
                             animation: google.maps.Animation.DROP,
                             position: this.currentCoordinate });
    return this.currentCoordinate;
  },

  showPosition: function (position) {
        this.state.lat = position.coords.latitude;
        this.state.lng = position.coords.longitude;
        this.currentCoordinate = {lat: this.state.lat, lng: this.state.lng};
        map.setCenter(this.currentCoordinate);
        this.currentMarker.setPosition(this.currentCoordinate);
  },

  handleChange: function () {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(this.showPosition);
    }
  },

  addTourPoi: function (poi) {
  var pinColor = "9999FF",
      pinImage = new google.maps.MarkerImage(
      "http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + pinColor,
          new google.maps.Size(21, 34),
          new google.maps.Point(0,0),
          new google.maps.Point(10, 34)
      ),
      marker = new google.maps.Marker({
                       map: map,
                       icon: pinImage,
                       draggable: false,
                       animation: google.maps.Animation.DROP,
                       position: { lat: poi.lat, lng: poi.lng } //,
                       //title: poi.name
                    }),
      contentString = "<h3>"+poi.name+"</h3><p>This is a trial</p>",
      infowindow = new google.maps.InfoWindow({
                       content: contentString
      });
    this.tour.push(marker);

    marker.addListener('click', function() {
               infowindow.open(map, marker);
             });

  },

  handleTourData: function (data) {
    var currentPoi = data.poi,
        nextPoi = data.nextPoi;
    this.tour = [];
    this.addTourPoi(currentPoi);
    while (nextPoi != undefined) {
      this.addTourPoi(nextPoi.poi);
      nextPoi = nextPoi.nextPoi;
    }
  },

  getTour: function () {
    $.get("/roundtrip?lat="+this.currentCoordinate.lat+"&lng="+this.currentCoordinate.lng,
     null,
     this.handleTourData);
  },

  render: function() {
    return (
    <div>
      <p> Bitte Knopf drücken, um Position zu ändern:</p> <button onClick={this.handleChange}>Update</button>
      <p>Aktuelle Position: {this.state.lat}/{this.state.lng}</p>
      <button onClick={this.getTour}>Get Tour</button>
    </div>
    );
  }
});


ReactDOM.render(<LoadCurrentPosition/>, document.getElementById('communications'));