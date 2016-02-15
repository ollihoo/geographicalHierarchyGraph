var LoadCurrentPosition = React.createClass({
getInitialState: function() {
    return {lat: 52.525084, lng: 13.369423};
  },

  showPosition: function (position) {
        this.state.lat = position.coords.latitude;
        this.state.lng = position.coords.longitude;
        var currentCoordinate = {lat: this.state.lat, lng: this.state.lng};
        console.log(currentCoordinate);
        map.setCenter(currentCoordinate);
  },

  handleChange: function () {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(this.showPosition);
    }
  },

  render: function() {
    return (<div>
      <p> Bitte Knopf drücken, um Position zu ändern:</p> <button onClick={this.handleChange}>Update</button>
      <p>Aktuelle Position: {this.state.lat}/{this.state.lng}</p>
      </div>
    );
  }
});


ReactDOM.render(<LoadCurrentPosition/>, document.getElementById('communications'));