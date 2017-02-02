<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<title>Plot on Map</title>
 <style>
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
      #map {
      
      
      
        height: 100%;
      }
      .controls {
        margin-top: 10px;
        border: 1px solid transparent;
        border-radius: 2px 0 0 2px;
        box-sizing: border-box;
        -moz-box-sizing: border-box;
        height: 32px;
        outline: none;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
      }

      #pac-input {
        background-color: #fff;
        font-family: Roboto;
        font-size: 15px;
        font-weight: 300;
        margin-left: 12px;
        padding: 0 11px 0 13px;
        text-overflow: ellipsis;
        width: 300px;
      }

      #pac-input:focus {
        border-color: #4d90fe;
      }

      .pac-container {
        font-family: Roboto;
      }

      #type-selector {
        color: #fff;
        background-color: #4d90fe;
        padding: 5px 11px 0px 11px;
      }

      #type-selector label {
        font-family: Roboto;
        font-size: 13px;
        font-weight: 300;
      }
      #target {
        width: 345px;
      }
    </style>
<script type="text/javascript">
var i=1,j=1;
var lat,lng;
var labels = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
var labelIndex = 0;
var directionsDisplay;
var directionsService;
var waypts = []; 
var sk=0;
var cordinates=new Array();
function loadScript() {
	cityLat = document.getElementById("cityLat").value;
	cityLon = document.getElementById("cityLon").value;
	try {
		var script = document.createElement("script");

		script.type = "text/javascript";
		script.src = "https://maps.googleapis.com/maps/api/js?sensor=true&libraries=places&callback=initialize&client=gme-leptonsoftwareexport&signature=1t2jNPl7sIPdevsQdfKNrx25bko=";			
		document.body.appendChild(script);
	} catch (e) {

		alert("ERRO" + e);
	}
}

function initialize() {
	geocoder = new google.maps.Geocoder();
	var myLatlng = new google.maps.LatLng(cityLat, cityLon);
	var myOptions = {
		zoom : 15,
		center : myLatlng,
		mapTypeId : google.maps.MapTypeId.ROADMAP,
	};
	map = new google.maps.Map(document.getElementById("map"), myOptions);
	  directionsDisplay = new google.maps.DirectionsRenderer;
      directionsService = new google.maps.DirectionsService;
	google.maps.event.addListener(map, 'click', function(event) {
		    placeMarker(event.latLng);
		    displayWindowForSetAPL(marker,event.latLng);
		});
	var input = document.getElementById('pac-input');
    var searchBox = new google.maps.places.SearchBox(input);
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

    // Bias the SearchBox results towards current map's viewport.
    map.addListener('bounds_changed', function() {
      searchBox.setBounds(map.getBounds());
    });

    var markers = [];
    // Listen for the event fired when the user selects a prediction and retrieve
    // more details for that place.
    searchBox.addListener('places_changed', function() {
      var places = searchBox.getPlaces();

      if (places.length == 0) {
        return;
      }

      // Clear out the old markers.
      markers.forEach(function(marker) {
        marker.setMap(null);
      });
      markers = [];

      // For each place, get the icon, name and location.
      var bounds = new google.maps.LatLngBounds();
      places.forEach(function(place) {
        var icon = {
          url: place.icon,
          size: new google.maps.Size(71, 71),
          origin: new google.maps.Point(0, 0),
          anchor: new google.maps.Point(17, 34),
          scaledSize: new google.maps.Size(25, 25)
        };

        // Create a marker for each place.
        markers.push(new google.maps.Marker({
          map: map,
          icon: icon,
          title: place.name,
          position: place.geometry.location
        }));

        if (place.geometry.viewport) {
          // Only geocodes have viewport.
          bounds.union(place.geometry.viewport);
        } else {
          bounds.extend(place.geometry.location);
        }
      });
      map.fitBounds(bounds);
    });
}
function placeMarker(location) {
 	marker = new google.maps.Marker({
		position : location,
		label: labels[labelIndex++ % labels.length],
		map : map
	}); 
 	var table = document.getElementById("latlongtbl");
	var row = table.insertRow(i);
	var cell1 = row.insertCell(0);
	var cell2 = row.insertCell(1);
	var cell3 = row.insertCell(2);
	cell1.innerHTML = i;
	cell2.innerHTML = location.lat();
	cell3.innerHTML = location.lng();
/* 	var cordinates=new Array();
	cordinates[1]=location;
	cordinates[0]=new google.maps.LatLng(lat, lng);
	 var linepath = new google.maps.Polyline({
         path: cordinates,
         geodesic: true,
         strokeColor: '#0000FF',
         strokeOpacity: 2.0,
         strokeWeight: 3
       });

       linepath.setMap(map); */
	
}

function formsubmit(){
	var name=document.getElementById("routename").value;
	var elements = document.getElementById("points").options;
	var lanmark=document.getElementById("landmarkname").options;
	for(var m = 0; m < elements.length; m++){
	     elements[m].selected = true;
	   }
	for(var n = 0; n < lanmark.length; n++){
		lanmark[n].selected = true;
	   }
	if(name.length<1){
		alert("Please enter route name!");
		return false;
	}
	else if(elements.length<1){
		alert("Points Not Added!");
		return false;
	}
	else{
		return true;
	}
}
var infowindow;
var lc;
function displayWindowForSetAPL(marker,loc) {
lc=loc;
	   try{   
	var message = '<table>';
	  message=message+'<tr><td>Name</td><td><input type="text" name="lname" id="lname"/>';
	  message=message+'</td><td><input type="button" value="Add" onclick="addname();"></td></tr></table>';
			infowindow = new google.maps.InfoWindow({
				content : message,
				size : new google.maps.Size(50, 50)
			});
			google.maps.event.addListener(marker, 'click', function() {
				infowindow.open(map, marker);
			});
			google.maps.event.addListener(marker, 'dblclick', function() {
				marker.setMap(null);
			});
		} catch (e) {
			alert("ERORQ" + e);
		}
	}
	
	function addname(){
		
		
		var name=document.getElementById("lname").value;
		var z = document.getElementById("landmarkname");
		var option = document.createElement("option");
		option.value =name;
		option.text=sk;
		z.add(option);
		sk++;
		if(i==1){
		cordinates[0]=lc;
		lat=lc.lat();
	   	lng=lc.lng();
		}else{
			 lat=lc.lat();
			   	lng=lc.lng();
		cordinates[1]=new google.maps.LatLng(lat, lng);
		 DisplayRoute(directionsService, directionsDisplay,cordinates[0],cordinates[1]); 
		  directionsDisplay.setMap(map);
	      
		}
	   	var x = document.getElementById("points");
	   	var option = document.createElement("option");
	   	option.value =i+"-"+lat+":"+lng;
	   	option.text=i;
	   	x.add(option);
	   	i++;
		lc=null;
		infowindow.close();
	}
	 function DisplayRoute(directionsService, directionsDisplay,start,end) {
		 waypts.push({location:end,stopover:true});
	        directionsService.route({
	          origin: start,  // Haight.
	          destination: end, 
	          waypoints: waypts,
	          optimizeWaypoints: true,// Ocean Beach.
	          // Note that Javascript allows us to access the constant
	          // using square brackets and a string value as its
	          // "property."
	          travelMode: google.maps.TravelMode.DRIVING
	        }, function(response, status) {
	          if (status == google.maps.DirectionsStatus.OK) {
	            directionsDisplay.setDirections(response);
	          } else {
	            window.alert('Directions request failed due to ' + status);
	          }
	        });
	      }
</script>
</head>
<body onload='loadScript();'>
<%	
long empid = 0;
String siteID=request.getParameter("siteID");
String employeeId = OtherFunctions.checkUser(session);
if (employeeId == null || employeeId.equals("null")) {
	String param = request.getServletPath().substring(1) + "___"
			+ request.getQueryString();
	response.sendRedirect("index.jsp?page=" + param);
} else {
	empid = Long.parseLong(employeeId);
%>
<%@include file="Header.jsp"%>
<% 
}

OtherDao ob = OtherDao.getInstance();
String[] city = ob.getCity(siteID);
	%>
	<h2 align="center">Create Route In Map</h2>
		<input type="hidden" value="<%=city[0]%>" id="cityLat">
<input type="hidden" value="<%=city[1]%>" id="cityLon">
 <input id="pac-input" class="controls" type="text" placeholder="Search Box">
	<div id="map" style="width: 100%; height: 600px; float:left;"></div>
	<form action="AddRouteInMap" method="post" onSubmit="return formsubmit();" name="form2">
	<input type="hidden" id="totalcount" name="totalcount" value="0"/>
	<table id="latlongtbl" align="center">
	<tr align="center"><th>No</th><th>Latitude</th><th>Longitude</th></tr>
	</table>
	<table align="center">
	<tr><td>Route Name</td><td><input type="text" name="routename" id="routename"/>  </td></tr>
	<tr><td>Type</td><td><select name="type"><option value="IN">IN</option><option value="OUT">OUT</option><option value="IN/OUT">IN/OUT</option></select></td></tr>
	<tr align="center"><td><input type="submit" class="formbutton" value="Submit"/> </td> </tr> 
	</table>
	<select name="points" multiple="multiple" id="points" style="display:none;" ></select>
	<select name="landmarkname" multiple="multiple" id="landmarkname" style="display:none;" >
	</select>
	
	</form>
	<%@include file="Footer.jsp"%>
</body>
</html>