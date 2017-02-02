<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
	<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Trace Vehicle</title>
<link
	href="https://code.google.com/apis/maps/documentation/javascript/examples/default.css"
	rel="stylesheet" type="text/css" />
 <script type="text/javascript" src = "https://maps.googleapis.com/maps/api/js?sensor=true&client=gme-leptonsoftwareexport4&signature=1t2jNPl7sIPdevsQdfKNrx25bko="></script>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">	
var geocoder;
var map;
 var marker;
 var cityLat;
 var cityLon;
 var markersArray = [];
 var tripId;
 var security;
 var startLatLng;
var	directionsDisplay ;	
var polygones=[];
var directionsService = new google.maps.DirectionsService();
function showMap() {
	cityLat = document.getElementById("cityLat").value;
	cityLon = document.getElementById("cityLon").value;
	
  	
		directionsDisplay = new google.maps.DirectionsRenderer();	
		var myLatlng = new google.maps.LatLng(cityLat, cityLon);
		
    var myOptions = {
        zoom: 14,
        center: myLatlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map"), myOptions);    
   	directionsDisplay.setMap(map);
   	tripId=document.getElementById("tripId").value;
   	displayVehiclePath();
}

	function GetXmlHttpObject()
	{
		var xmlHttp=null;
	if (window.XMLHttpRequest) {
			xmlHttp=new XMLHttpRequest();
		}
	else if (window.ActiveXObject) {
		 		xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
	 		}

	return xmlHttp;
	}
function displayVehiclePath()
{
    xmlHttp=GetXmlHttpObject()
    if (xmlHttp==null)
	{
        alert ("Browser does not support HTTP Request")
        return
    }
	var url="GetVehiclePosition?tripId="+tripId;
	xmlHttp.onreadystatechange=showVehiclePosition;
	xmlHttp.open("POST",url,true);
	xmlHttp.send(null);    
}

function showVehiclePosition()
{
    if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
    {    	
    	var retString=xmlHttp.responseText;
    	vehiclepositions=retString.split("|");
    	showActualPath();    	    	
    }
}
function  showActualPath(){
   // document.getElementById("routedet").style.display = 'block';    
    xmlHttp=GetXmlHttpObject()
    var url="GetRoute?tripIdInTrace="+tripId;
    xmlHttp.onreadystatechange=showEmployeeGetIn;
    xmlHttp.open("POST",url,true);
    xmlHttp.send()
}


function  showEmployeeGetIn(){
	xmlHttp=GetXmlHttpObject()
		var url="GetRoute?tripIdForGetIn="+tripId;
		xmlHttp.onreadystatechange=displayEmployeeGetIn;
		xmlHttp.open("POST",url,true);
		xmlHttp.send()
	}
	
function displayEmployeeGetIn()
{
		if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
			{
			var fullnodes = xmlHttp.responseText;
			var nodes=fullnodes.split("$");
			var noofnodes=nodes.length;
			var ltln;
			var centerLatlng;
			var center=(noofnodes-2)/2;
			for(var i=1;i<noofnodes-2;i++)
			{
			ltln=nodes[i].split(":");
			var latlng = new google.maps.LatLng(ltln[2],ltln[3]);			
			if(i==noofnodes/2||i==(noofnodes+1)/2)
				{
				setMapCenter(latlng);
				}
						var message="Emp Code:"+ltln[0]+"   Name:"+ltln[1];
						placeMarker(ltln[4],latlng,message);
			}
			showVehiclesPath(map,vehiclepositions);
			
			}
}

function setMapCenter(centerLatLng)
{
    map.setCenter(centerLatLng);
}

function placeMarker(gender,location,title) {
    var image='images/male.png';
    if(gender=="M")
	{
	image = 'images/male.png';
	}
else if(gender=="F")
{
	image = 'images/female.png';	
}
else if(gender=="S")
{
	image = 'images/security.gif';	
}	    
         marker = new google.maps.Marker({
            position: location,
            map: map,
            icon:image,
            title: title
        });
         markersArray.push(marker);
}
function showVehiclesPath(map, vehiclepositions) {                       	                         	  
                       	var flightPlanCoordinates=new Array();
             var lastlat;
             var lastlon;
        for(var i=1;i<vehiclepositions.length-1;i++)
			{        	
        	var locations=vehiclepositions[i].split("~");
        	lastlat=locations[1]
        	lastlon=locations[2];
        	flightPlanCoordinates[i-1]=new google.maps.LatLng(locations[1], locations[2]);
			}
        startLatLng=flightPlanCoordinates[0];
        if(security=="YES")
        	{
        placeMarker("S",startLatLng,"Security");        
        	}
        var flightPath = new google.maps.Polyline({
       	    path: flightPlanCoordinates,
       	    strokeColor: '#FF0000',
       	    strokeOpacity: 1.0,
       	    strokeWeight: 2
       	  });
        flightPath.setMap(map);
        polygones.push(flightPath);
        markVehicle(lastlat,lastlon);
  }
  
  
  
  
function markVehicle(lastlat,lastlon)
{
	try
	{
	var myLatLng=new google.maps.LatLng(lastlat, lastlon);
	var title="";
	image = 'icons/greencar.png';
 marker = new google.maps.Marker({
	position : myLatLng,
	map : map,
	icon : image,
	title : title
});
 markersArray.push(marker);
 displayVehiclePathTimes();
 
 
}catch(e){alert(e)}
}







function displayVehiclePathTimes()
{
    xmlHttp=GetXmlHttpObject()
    if (xmlHttp==null)
	{
        alert ("Browser does not support HTTP Request")
        return
    }
	var url="GetVehiclePosition?tripId="+tripId+"&tripInterval=true";
	xmlHttp.onreadystatechange=showVehiclePathTimes;
	xmlHttp.open("POST",url,true);
	xmlHttp.send(null);    
}


function showVehiclePathTimes()
{
if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
{
	var fullnodes = xmlHttp.responseText;
var nodes=fullnodes.split("|");
var noofnodes=nodes.length;
var ltln;
var centerLatlng;
var center=(noofnodes-2)/2;
for(var i=1;i<noofnodes;i++)
{
ltln=nodes[i].split("~");
var latlng = new google.maps.LatLng(ltln[1],ltln[2]);			
if(i==noofnodes-3)
	{
	centerLatlng=new google.maps.LatLng(ltln[1],ltln[2]);
	}
			var message="Time:"+ltln[0];
			placeBullets(latlng,message);
}
	
}

}

function placeBullets(location,title) {
    var image='images/bullet_blue.png';   	    
         marker = new google.maps.Marker({
            position: location,
            map: map,
            icon:image,
            title: title
        });
         markersArray.push(marker);
}


function GetXmlHttpObject()
{
    var xmlHttp=null;
    if (window.XMLHttpRequest) {
        xmlHttp=new XMLHttpRequest();
    }
    //catch (e)
    else if (window.ActiveXObject) {
        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
    }

    return xmlHttp;
} 

function rempvemarkerandpolygone() {
	if (markersArray) {
		for (i in markersArray) {
			markersArray[i].setMap(null);
		}
		markersArray.length = 0;
	}
	if (polygones) {
		for (j in polygones) {
			polygones[j].setMap(null);
		}
		polygones.length = 0;
	}
	
}
</script>
</head>
<body onload="showMap()">
	<%@include file="../../Header.jsp"%>

	<hr />
	<div id="body">
		<div class="content">
				 	</div>
	</div>
	<div id="map" style="width: 100%; height: 90%; float: left;"></div>
	<div id="routedet" style="width: 30%; height: 60%;"></div>
	<form:hidden id="tripId" path="tripId" value="${tripId}"/>
	<form:hidden id="cityLat" path="cityLat" value="${cityLat}"/>
	<form:hidden id="cityLon" path="cityLon" value="${cityLon}"/>
</body>
</html>