<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.APLService"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<%
	APLService aplService =new APLService();

List<APLDto> areaDtos=aplService.getAreas();

%>
<script type="text/javascript">
var geocoder;
var map;
 var marker;
 var cityLat;
 var cityLon;
	function loadScript() {
		cityLat = document.getElementById("cityLat").value;
		cityLon = document.getElementById("cityLon").value;
		try {
			var script = document.createElement("script");
			
			script.type = "text/javascript";
			script.src = "https://maps.googleapis.com/maps/api/js?client=gme-leptonsoftwareexport&signature=1t2jNPl7sIPdevsQdfKNrx25bko=&callback=initialize";
			//script.src = "http://maps.googleapis.com/maps/api/js?sensor=true&callback=initialize";
			document.body.appendChild(script);	
		} catch (e) {
			alert("ERRO" + e);
		}
	}
 
function initialize()
{
	try{
    geocoder = new google.maps.Geocoder();
  var myLatlng = new google.maps.LatLng(cityLat, cityLon);
  var myOptions = {
    zoom: 12,
    center: myLatlng,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  map = new google.maps.Map(document.getElementById("map"), myOptions);
  showExistingLandmarks();
  google.maps.event.addListener(map, 'click', function(event) {
   // alert(event.to);
    placeMarker(event.latLng);
    displayWindowForSetAPL(marker,event);
});
	}catch(e)
	{
	aler(e);	
	}
}

function placeMarker(location) {
	marker = new google.maps.Marker({
		position : location,
		map : map
	});
}


function showExistingLandmarks(){
	
	xmlHttp=GetXmlHttpObject();
	var location=document.getElementById("location").value;
	var area=document.getElementById("area").value;
	var place=document.getElementById("place").value;	
	var url="";
	if(place!="null")
		{
		url="GetLandmarks?place="+place;
		}
	else if(area!="null")
		{
		
		url="GetLandmarks?area="+area;
		}
	else if (location!="null")		
		{
		url="GetLandmarks?location="+location;
		}
	else
		{
		url="GetLandmarks";
		}
		xmlHttp.onreadystatechange=displayExistingLandmarks;
		xmlHttp.open("POST",url,true);
		xmlHttp.send()
	}
function displayExistingLandmarks()
{
		if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
			{
			var fullnodes = xmlHttp.responseText;
			var nodes=fullnodes.split("$");
			var noofnodes=nodes.length;
			var ltln;
			for(var i=1;i<noofnodes;i++)
			{
			ltln=nodes[i].split(":");
			var latlng = new google.maps.LatLng(ltln[4],ltln[5]);
						placeMarker(latlng);
						var message=ltln[0]+"<br>"+ltln[1]+"<br>"+ltln[3]+"<br>"+'';
						displayExistingLandmarkMarkers(marker,latlng,message);
			}
			}
}



function displayExistingLandmarkMarkers(marker,latlng,message) {	  
	  var infowindow = new google.maps.InfoWindow(
	      { content: message,
	        size: new google.maps.Size(50,50)
	      });
	     google.maps.event.addListener(marker, 'click', function() {
	  infowindow.open(map,marker);
	  });

	}
		


function displayWindowForSetAPL(marker,event) {
	   try{
	var latln=String(event.latLng);
	 var latlng=latln.split(" ");
	latlng=latlng[0]+"$"+latlng[1];	
	  var message = 'Update APL<form name="form1" action="UpdateLandmark" method="post"><table>';
	  message=message+'<tr><td>Area</td><td>';
	  message=message+'<select name="area" id="area" onchange="showPlace(this.value)">';
	  message=message+'<option value="">Select</option><%for(APLDto areaDto:areaDtos){%>';
	  message=message+'<option value=<%=areaDto.getAreaID()%>><%=areaDto.getArea()%>';
	  message=message+'</option><%}%></select></td></tr>';
	  message=message+'<tr><td>Place</td><td id="placeTd"><Select name="place"><option value="">Select</option></select></td></tr><tr><td>Land Mark</td><td id="landmark"><select name="landmarkId" ></select></td></tr><tr><td><input type="hidden" value=';
	  message=message+latlng;
	  message=message+' name="latlng"></td><td><input type="submit" value="Update"></td></tr></form>';
			var infowindow = new google.maps.InfoWindow({
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
function showPlace(area)
{
    if(document.getElementById("area").value!="")
    {
        xmlHttp=GetXmlHttpObject()
        if (xmlHttp==null)
		{
            alert ("Browser does not support HTTP Request")
            return
        }
		var url="GetPlace";
		url=url+"?area="+area;
		xmlHttp.onreadystatechange=showPlace_select;

		xmlHttp.open("GET",url,true);
		//alert('sent');
		xmlHttp.send(null);
    }
    else
    {
    	 alert("Please Select area-- ");
    }
}
function showLandmark(place)
{
    if(place!="0")
    {
        xmlHttp=GetXmlHttpObject()
        if (xmlHttp==null)
		{
            alert ("Browser does not support HTTP Request")
            return
        }
		var url="GetLandmarks";
		url=url+"?placeforLandmark="+place;
		xmlHttp.onreadystatechange=showLandmark_select;

		xmlHttp.open("GET",url,true);
		xmlHttp.send(null);
    }
    else
    {
    	 alert("Please Select Place-- ");
    }
}
function showLandmark_select()
{
    if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
    {
        document.getElementById("landmark").innerHTML=xmlHttp.responseText
    }
}

function showPlace_select()
{
	//alert("status :"+ xmlHttp.readyState);
    if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
    { 
    
        document.getElementById("placeTd").innerHTML=xmlHttp.responseText;
   	  
    }
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
	
</script>
</head>
<% OtherDao ob=OtherDao.getInstance();
String site ="";
String location= request.getParameter("location");
String areaId= request.getParameter("area");
String placeId= request.getParameter("place");

try {
	 site = session.getAttribute("site").toString();	 
}catch(Exception ignor){}

String[] city=ob.getCity(site);

%>
<input type="hidden" value="<%=city[0]%>" id="cityLat">
<input type="hidden" value="<%=city[1]%>" id="cityLon">
<input type="hidden" value="<%=areaId%>" id="area">
<input type="hidden" value="<%=placeId%>" id="place">
<input type="hidden" value="<%=location%>" id="location">
<body onload="loadScript()">
	<div id="map" style="width: 100%; height: 720px; float: right;"></div>
</body>
</html>