<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="com.agiledge.atom.service.RouteService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<title>Select pick/drop point</title>
<%-- 
<%		ArrayList<LogTimeDto> inLog= new LogTimeService().getAllLogtime("IN");
ArrayList<LogTimeDto> outLog= new LogTimeService().getAllLogtime("OUT");
ArrayList<RouteDto> routes = new RouteService().getAllRoutes("1"); %> --%>
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
</style>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
var emplat='12.121212';
var emplong='77.2536575';
var map;
var marker;
var markers;
var count=0;
var type;
function loadScript() {
	try {
		var script = document.createElement("script");

		script.type = "text/javascript";
		script.src = "https://maps.googleapis.com/maps/api/js?sensor=true&callback=initialize&libraries=places&client=gme-leptonsoftwareexport&signature=1t2jNPl7sIPdevsQdfKNrx25bko=";			
		document.body.appendChild(script);
	} catch (e) {

		alert("ERRO" + e);
	}
}
function initialize() {
	type=document.getElementById("type").value;
	
	 if(document.getElementById(type+"lat").value!='null' || document.getElementById(type+"long").value!='null'){
			if(document.getElementById(type+"lat").value!=null || document.getElementById(type+"long").value!=null){
				var lat=document.getElementById(type+"lat").value;
				var lng=document.getElementById(type+"long").value;
				emplat=lat;
				emplong=lng;
			}
	 }
	geocoder = new google.maps.Geocoder();
	var myLatlng = new google.maps.LatLng(emplat, emplong);
	var myOptions = {
		zoom : 15,
		center : myLatlng,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map"), myOptions);
	 if(emplat!='null' || emplong!='null'){
		if(emplat!=null || emplong!=null){
		var myLatLng12 = new google.maps.LatLng(emplat, emplong);
		markers=[];
		placeMarker(myLatLng12);
	}
	} 
	google.maps.event.addListener(map, 'click', function(event) {
		    placeMarker(event.latLng);
			formMarkers(marker,event.latLng);
		});
	var input = document.getElementById('pac-input');
	  var searchBox = new google.maps.places.SearchBox(input);
	  map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

	  map.addListener('bounds_changed', function() {
		    searchBox.setBounds(map.getBounds());
		  });
	 	markers = [];
	  searchBox.addListener('places_changed', function() {
		    var places = searchBox.getPlaces();

		    if (places.length == 0) {
		      return;
		    }

		    // Clear out the old markers.
		    markers.forEach(function(marker1) {
		    	marker1.setMap(null);
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
		      //  icon: icon,
		        position: place.geometry.location
				//icon:'images/house.png'
		      }));

				formMarkers(markers[0],place.geometry.location);
		      //document.getElementById("lat").value=place.geometry.location;
		      if (place.geometry.viewport) {
		        // Only geocodes have viewport.
		        bounds.union(place.geometry.viewport);
		      } else {
		        bounds.extend(place.geometry.location);
		      }
		    });
		    map.fitBounds(bounds);
		  });
		  // [END region_getplaces]

			 
}
function placeMarker(location) {

	if(count>0)
	{
marker.setMap(null);
	}
	for(var i = 0; i<markers.length;i++){
		markers[i].setMap(null);

		}

	marker = new google.maps.Marker({
		position : location,
		map : map,
		title:  type
		//icon:'images/house.png'
	});
	count=count+1;
}
function formMarkers(marker,plc) {
var message='<input type="button" id="sub" value="SET" class="formbutton" onclick="setValues()"/>';
	  var infowindow = new google.maps.InfoWindow(
	      { content: message,
	        size: new google.maps.Size(50,50)
	      });
	     google.maps.event.addListener(marker, 'click', function() {
	  infowindow.open(map,marker);
	  });
		 var type=document.getElementById("type").value;
		 opener.document.getElementById(type+"latlong").value = plc;
	}
function setValues(){

	 window.close();
}		
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@page import="com.agiledge.atom.dao.ShuttleSocketDao"%>
<title>Drop Point</title>
</head>
<body onload='loadScript();'>
	<%
		long empid = 0;
	
		String employeeId=request.getParameter("empid");
		EmployeeDto subdto1=new ShuttleSocketDao().getShuttlePickUpDrop(employeeId);
		EmployeeDto edto = new EmployeeSubscriptionService().getShuttleEmpSubscriptionDetails(employeeId);
		 String inlat="",inlong="",outlat="",outlong="",homelat="",homelong="";
		 if(subdto1.getPickup()!=null&&!subdto1.getPickup().equalsIgnoreCase("")&&!subdto1.getPickup().equalsIgnoreCase("null")){
			inlat=subdto1.getPickup().split("#")[0];
			inlong=subdto1.getPickup().split("#")[1];
		 }
			 if(subdto1.getDrop()!=null&&!subdto1.getDrop().equalsIgnoreCase("")&&!subdto1.getDrop().equalsIgnoreCase("null")){
					outlat=subdto1.getDrop().split("#")[0];
			 		outlong=subdto1.getDrop().split("#")[1];
			 }
		if(edto!=null && edto.getLattitude()!=null && !(edto.getLattitude().equalsIgnoreCase("null")) && edto.getLongitude()!=null && !(edto.getLongitude().equalsIgnoreCase("null"))){
			homelat=edto.getLattitude();
			homelong=edto.getLongitude();
		}
	if(request.getParameter("type").equalsIgnoreCase("home")){
		%>
	<h2 align="center">Choose Home Location</h2>
	<%
	}else{
	%>
	<h2 align="center">Choose Pick up/Drop Point</h2>
	<%} %>
	<input id="pac-input" class="controls" type="text"
		placeholder="Search Box">
	<div id="map" style="width: 100%; height: 600px; float: left;"></div>
	<input type="hidden" id="inlat" name="inlat" value="<%=inlat%>" />
	<input type="hidden" name="inlong" id="inlong" value="<%=inlong %>" />
	<input type="hidden" id="outlat" name="outlat" value="<%=outlat %>" />
	<input type="hidden" name="outlong" id="outlong" value="<%=outlong %>" />
	<input type="hidden" id="homelat" name="homelat"
		value="<%=homelat %>" />
	<input type="hidden" name="homelong" id="homelong"
		value="<%=homelong %>" />
	<input type="hidden" name="type" id="type"
		value="<%=request.getParameter("type")%>" />
</body>
</html>