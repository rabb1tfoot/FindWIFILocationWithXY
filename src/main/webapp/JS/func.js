/**
 * 
 */

const COORDS = "coords";
var x = "123";
var y = "321";
function handleGeoSucces(position) {
console.log("getLoc success");
x = position.coords.latitude;
y = position.coords.longitude;
document.getElementById("locationX").setAttribute('value', x);
document.getElementById("locationY").setAttribute('value', y);
}

function handleGeoError() {
  console.log("Can't access geo location");
}

function askForCoords() {
  navigator.geolocation.getCurrentPosition(handleGeoSucces, handleGeoError);
}

function loadCoords() {
  const loadedCoords = localStorage.getItem(COORDS);
    askForCoords();
}

function GetLoc() {
  console.log("call getLoc geo location");
  loadCoords();
}
console.log("init ok");

