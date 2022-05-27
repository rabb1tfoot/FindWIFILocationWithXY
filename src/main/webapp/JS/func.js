/**
 * 
 */

const COORDS = "coords";
var x = "123";
var y = "321";
var arrWifiInfo = new Array();
var arrHistory = new Array();
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

function FindaroundWifi(){
	
	var wifiTable = document.getElementById("wifiInfoTable");
	var historyTable = document.getElementById("historyTable");
	//wifi정보 업데이트
	//GetData();
	//history 저장	
}

function GetData()
{
	const mysql = require('mysql');
	
	const pool = mysql.createConnection({
		host : 'localhost',
		user : 'root',
		database : 'wifiinfo',
		password : '232723',
	});
	connection.connect();
	connection.query("SELECT * ", function(err, results) {
		
	});
	connection.end();
	
}



