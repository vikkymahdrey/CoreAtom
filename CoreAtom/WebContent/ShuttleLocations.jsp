<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.APLService"%>
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>Display Locations</title>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/custom_siemens.css" rel="stylesheet">
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
			script.src = "https://maps.googleapis.com/maps/api/js?client=gme-leptonsoftwareexport4&signature=xghu9DIoNr63z8_al_oJCSPWQh0=&callback=initialize";
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
 //Showing Site
  marker = new google.maps.Marker({
		position : myLatlng,
		map : map ,
		icon:'images/icon_2.png',
		
		});
  displayMarkers(marker,myLatlng,'Company Location')	;
  showLandmarks();
  }catch(e)
	{
	alert(e);	
	}
}

function placeMarker(location) {
	
	marker = new google.maps.Marker({
		position : location,
		map : map 
		});
}
function clearMarkers(){
map=null;
initialize();
}

function showLandmarks(){

	var search=document.getElementById("select").value;	
	
	var url="";
	
	xmlHttp=GetXmlHttpObject();
 if(search=='Home'){
	
	url="GetShuttleLandmarks?search="+search;
	
}else if(search=='Pickup'){
	
	url="GetShuttleLandmarks?search="+search;
}else if(search=='Drop'){
	
	url="GetShuttleLandmarks?search="+search;
}
	
	
		xmlHttp.onreadystatechange=displayExistingLandmarks;
		xmlHttp.open("POST",url,true);
		xmlHttp.send();
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
			
			var latlng = new google.maps.LatLng(ltln[1],ltln[2]);
						placeMarker(latlng);
						var message=ltln[0] +"<br>"+ltln[3];
						
						displayMarkers(marker,latlng,message);
			}
			

			}
}



function displayMarkers(marker,latlng,message) {
	
	  var infowindow = new google.maps.InfoWindow(
	      {  
	    	  content: message,
	        size: new google.maps.Size(50,50)
	      });
	     google.maps.event.addListener(marker, 'click', function() {
	  infowindow.open(map,marker);
	  /* setTimeout(function () { infowindow.close(); }, 5000); */
	     });

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
<% EmployeeDto empDtoInHeader=null; 
try{
	
	String role=session.getAttribute("role").toString();
		System.out.println("BEFORE SEsion");
	empDtoInHeader = (EmployeeDto)session.getAttribute("userDto");
	System.out.println("AFTER SEsion");
	if(empDtoInHeader==null) {
		empDtoInHeader = new EmployeeService().getEmployeeAccurate(session.getAttribute("user").toString());
	}
	
}catch(NullPointerException ne) {

	empDtoInHeader = new EmployeeService().getEmployeeAccurate(session.getAttribute("user").toString());
}

OtherDao ob=OtherDao.getInstance();
String site ="";
String location= request.getParameter("location");


try {
	 site = session.getAttribute("site").toString();	 
}catch(Exception ignor){}

String[] city=ob.getCity(site);

%>
<body  onload="loadScript()">
	
<input type="hidden" value="<%=city[0]%>" id="cityLat">
<input type="hidden" value="<%=city[1]%>" id="cityLon">
<input type="hidden" value="<%=location%>" id="location">

<div class="wrapper">
<div class="header-wrap">
		<div class="container">
			<div class="row">
				<div class="col-sm-12 text-right">
					<img src="images/user_iocn_header.png" />&nbsp;Welcome <%=empDtoInHeader.getDisplayName() %>  &nbsp;&nbsp;&nbsp;<a href="Logout"><img src="images/logout_icon_header.png" />&nbsp;Log Out</a>
				</div>
			</div>
		</div>
	</div>
<div class="breadcrumb-wrap">
					<a href="employee_home.jsp"><img src="images/home.png" /></a>
					<a href="employee_home.jsp" >My Information </a>
					<a href="#" class="current"> Locations</a>
				</div>
<p align="center"> <i>Search by Location </i>

<select  id="select"   onchange="clearMarkers()">
  
  <option value="Home">Home </option>
  <option value="Pickup">Pickup</option>
  <option value="Drop">Drop</option>

</select>
</p> 

	<div id="map" align="center" style="width: 100%; height: 720px;"></div>
<div class="footer-wrap">
					<div class="row">
						<div class="col-sm-12 text-center">
							 <p class="text-12">The information stored on this website is maintained in accordance with the organization's Data Privacy Policy. </span><br />Copyright © 2016 siemens
 
						</div>
					</div>
					
				</div>
				</div>
				
</body>
</html>