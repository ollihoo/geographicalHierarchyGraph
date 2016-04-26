var LoadCurrentPosition = React.createClass({
  currentMarker: null,
  getInitialState: function() {
    var initialCoordinates = {lat: 52.525084, lng: 13.369423};
    this.currentMarker = new google.maps.Marker({
                             map: map,
                             draggable: true,
                             animation: google.maps.Animation.DROP,
                             position: initialCoordinates });
    return initialCoordinates;
  },

  showPosition: function (position) {
        this.state.lat = position.coords.latitude;
        this.state.lng = position.coords.longitude;
        var currentCoordinate = {lat: this.state.lat, lng: this.state.lng};
        map.setCenter(currentCoordinate);
        this.currentMarker.setPosition(currentCoordinate);
  },

  handleChange: function () {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(this.showPosition);
    }
  },

  render: function() {
    return (
    <div>
      <p> Bitte Knopf drücken, um Position zu ändern:</p> <button onClick={this.handleChange}>Update</button>
      <p>Aktuelle Position: {this.state.lat}/{this.state.lng}</p>
    </div>
    <div>
    <button onClick={this.getTour}>Get Tour</button>
    </div>
    );
  }
});


ReactDOM.render(<LoadCurrentPosition/>, document.getElementById('communications'));