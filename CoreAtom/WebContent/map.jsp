<%-- 
    Document   : map
    Created on : May 16, 2016, 4:55:52 PM
    Author     : sandesh
--%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.service.RouteService"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
		OtherDao ob = OtherDao.getInstance();
		String site = "1";
		String route[]=request.getParameterValues("route");
		try {
			site = request.getParameter("site");
		} catch (Exception ignor) {
		}
		String[] city = ob.getCity(site);
		ArrayList<ArrayList<RouteDto>> masterlist=null;
		if(route!=null && route.length>0){
			masterlist=new RouteService().getAllRouteDetailsWithoutAPL(route);
		}
	%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Routes</title>
<link rel="icon" href="images/agile.png" type="image/x-icon" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<script type="text/javascript">
	var geocoder;
	var map;
	var marker;
	var cityLat;
	var cityLon;
	var directionsDisplay;
	var directionsService;
	var waypts = [];
	var start = [];
	var end = [];
	var pts = [];
	var routecount=1;
	var colors=["red","green","blue","orange","black"];
	var clrcount=0;
	function loadScript() {
		cityLat = document.getElementById("cityLat").value;
		cityLon = document.getElementById("cityLon").value;
		try {
			var script = document.createElement("script");

			script.type = "text/javascript";
			script.src = "https://maps.googleapis.com/maps/api/js?sensor=true&callback=initialize&libraries=places&client=gme-leptonsoftwareexport4&signature=xghu9DIoNr63z8_al_oJCSPWQh0=";

			document.body.appendChild(script);
		} catch (e) {
			alert("ERRO" + e);
		}
	}
	function initialize() {
		
			var myLatlng = new google.maps.LatLng(cityLat, cityLon);
			var myOptions = {
				zoom : 12,
				center : myLatlng,
				mapTypeId : google.maps.MapTypeId.ROADMAP
			};
			map = new google.maps.Map(document.getElementById("map"), myOptions);
			var bottoninmap=document.getElementById("exit");
			map.controls[google.maps.ControlPosition.TOP_RIGHT].push(bottoninmap);
			directionsDisplay = new google.maps.DirectionsRenderer;
			directionsService = new google.maps.DirectionsService;
	<%if(masterlist!=null){

			for(ArrayList<RouteDto> list : masterlist){
				if(list.size()>0){
				for(RouteDto dto: list){%>
		var myLatLng12 = new google.maps.LatLng(
	<%=dto.getLattitude()%>
		,
	<%=dto.getLongitude()%>
		);
			placeMarker(myLatLng12,'<%=dto.getLandmark()%>');
	<%}%>
	routecount=routecount+1;
		var firstlatlng = new google.maps.LatLng(
	<%=list.get(0).getLattitude()%>
		,
	<%=list.get(0).getLongitude()%>
		);
			var lastlatlng = new google.maps.LatLng(
	<%=list.get(1).getLattitude()%>
		,
	<%=list.get(1).getLongitude()%>
		);

			start.push(firstlatlng);
			end.push(lastlatlng);
			pts.push(waypts);
			requestDirections(firstlatlng, lastlatlng, waypts);
			waypts = [];
	<%}
			}%>
		
	<%}%>
	}
	function placeMarker(position1, name) {
		waypts.push({
			location : position1,
			stopover : true,
		});
		marker = new google.maps.Marker({
			position : position1,
			title : name,
			map : map,
			label:''+routecount 
		});
		
	}
	function renderDirections(result) {
		var directionsRenderer = new google.maps.DirectionsRenderer({
		    polylineOptions: {
		        strokeColor: colors[clrcount]
		      }
		    });
		if(clrcount<5){
		clrcount=clrcount+1;
		}
		else{
			clrcount=0;
		}
		directionsRenderer.setMap(map);
		directionsRenderer.setDirections(result);
	}
	function requestDirections(firstlatlng, lastlatlng, waypts) {
		directionsService.route({
			origin : firstlatlng,
			destination : lastlatlng,
			waypoints : waypts,
			optimizeWaypoints : true,
			travelMode : google.maps.DirectionsTravelMode.DRIVING
		}, function(result) {
			renderDirections(result);
		});
	}
</script>
</head>
<body>
<body onload="loadScript()">

	<input type="hidden" value="<%=city[0]%>" id="cityLat">
	<input type="hidden" value="<%=city[1]%>" id="cityLon">
	<input type="button" id="exit" name="exit" value="X" onclick="window.close();" >
	<div id="map" style="width: 100%; height: 720px; float: right;"></div>
</body>

</body>
</html>