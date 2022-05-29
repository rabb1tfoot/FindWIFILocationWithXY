/**
 * 
 */

const COORDS = "coords";
var x = "123";
var y = "321";
function handleGeoSucces(position) {
x = position.coords.latitude;
y = position.coords.longitude;
document.getElementById("locationX").setAttribute('value', x);
document.getElementById("locationY").setAttribute('value', y);
}

function handleGeoError() {
}

function askForCoords() {
  navigator.geolocation.getCurrentPosition(handleGeoSucces, handleGeoError);
}

function loadCoords() {
  const loadedCoords = localStorage.getItem(COORDS);
    askForCoords();
}

function GetLoc() {
  loadCoords();
}

