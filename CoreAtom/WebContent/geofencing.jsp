<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.APLService"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	APLService aplService =new APLService();
ArrayList<APLDto> areaDtos=aplService.getAreas();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title>Google Maps</title>
	<script src = "https://maps.googleapis.com/maps/api/js?sensor=true&client=gme-leptonsoftwareexport&signature=1t2jNPl7sIPdevsQdfKNrx25bko=&callback=initialize"/>
<script src="js/epolys.js" type="text/javascript">
	
</script>

<script type="text/javascript">
	var map;
	var marker;
	var cityLat;
	var cityLon;
	var boundaryColor = '#ED1B24'; // initialize color of polyline
	var polyCoordinates = []; // initialize an array where we store latitude and longitude pair
	var count = 0;

	function initialize() {
		try {
			cityLat = document.getElementById("cityLat").value;
			cityLon = document.getElementById("cityLon").value;
			var myLatlng = new google.maps.LatLng(cityLat, cityLon);
			var myOptions = {
				zoom : 12,
				center : myLatlng,
				mapTypeId : google.maps.MapTypeId.ROADMAP
			};
			map = new google.maps.Map(document.getElementById("map"), myOptions);
			google.maps.event.addListener(map, 'click', function(event) {
				polyCoordinates[count] = event.latLng;
				// alert(event.latLng);
				createPolyline(polyCoordinates);
				count++;
			});

		} catch (e) {
			aler(e);
		}
	}
	function createPolyline(polyC) {
		Path = new google.maps.Polyline({
			path : polyC,
			strokeColor : boundaryColor,
			strokeOpacity : 1.0,
			strokeWeight : 2
		});

		Path.setMap(map);
	}

	function connectPoints() {
		try {
			/*
			var polygon;
			var triangleCoords = [
			                    new google.maps.LatLng(12.96506, 77.59712),
			                    new google.maps.LatLng(12.96524, 77.59831),
			                    new google.maps.LatLng(12.96549, 77.60006),
			                    new google.maps.LatLng(12.96516, 77.59996),
			                    new google.maps.LatLng(12.96479, 77.59880),
			                    new google.maps.LatLng(12.96480, 77.59764),
			                    new google.maps.LatLng(12.96506, 77.59712)
			                  ];	
			polygon = new google.maps.Polygon({
			    paths: triangleCoords,
			    strokeColor: '#FF0000',
			    strokeOpacity: 0.8,
			    strokeWeight: 2,
			    fillColor: '#FF0000',
			    fillOpacity: 0.35
			  });
			polygon.setMap(map);
			var isWithinPolygon = polygon.containsLatLng(12.96503,77.59812);
			alert(isWithinPolygon);
			}catch(e){alert(e);}
			/*	
			var point_add = []; // initialize an array
			var start = polyCoordinates[0]; // storing start point
			var end = polyCoordinates[(polyCoordinates.length-1)]; // storing end point
			// pushing start and end point to an array
			point_add.push(start);
			point_add.push(end);
			Path.setMap(null);
			 */
			// createPolylineFinal(point_add); // function to join points
			alert(polyCoordinates);
			 var url="GetLogTime?logtype="+logtype+"&type=disabled&site="+site;                                    
             xmlHttp=GetXmlHttpObject()
		} catch (e) {
			alert(e);
		}

	}

	function GetXmlHttpObject() {
		var xmlHttp = null;
		if (window.XMLHttpRequest) {
			xmlHttp = new XMLHttpRequest();
		} else {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		return xmlHttp;
	}

	function fencerequest() {

	}
</script>
</head>
<%
	OtherDao ob = OtherDao.getInstance();
    String site =""; 
     try {
    	 site = session.getAttribute("site").toString();	 
     }catch(Exception ignor){}
    
	String[] city = ob.getCity(site);
%>
<input type="hidden" value="<%=city[0]%>" id="cityLat">
<input type="hidden" value="<%=city[1]%>" id="cityLon">
<body onload="initialize()">
	<p>
		<input type=button name="btnconnect" value="Connect Points"
			onclick="connectPoints();" />
	</p>
	<div id="map" style="width: 100%; height: 500px; float: right;"></div>
</body>
</html>