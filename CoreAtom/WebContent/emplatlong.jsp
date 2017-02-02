<%@page import="com.agiledge.atom.service.EmployeeService" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>My Drop Location</title>
<script type="text/javascript">
var geocoder;
var map;
 var marker;
 var cityLat;
 var cityLon;
 var count=0;
	function loadScript() {
		cityLat = document.getElementById("cityLat").value;
		cityLon = document.getElementById("cityLon").value;
		try {
			var script = document.createElement("script");
			
			script.type = "text/javascript";
			script.src = "https://maps.googleapis.com/maps/api/js?client=gme-leptonsoftwareexport&signature=1t2jNPl7sIPdevsQdfKNrx25bko=&callback=initialize";
			document.body.appendChild(script);	
		} catch (e) {
			alert("ERRO" + e);
		}
	}
 
function initialize()
{
	try{
		var emplat=document.getElementById("emp_lat").value;
		var emplong=document.getElementById("emp_long").value;
		if(emplat!=0&&emplat!=""&&emplong!=0&&emplong!="")
		  {
			var myLatlng=new google.maps.LatLng(emplat, emplong);
		  }
		else{
			 myLatlng = new google.maps.LatLng(cityLat, cityLon);
		}
    geocoder = new google.maps.Geocoder();
  var myOptions = {
    zoom: 12,
    center: myLatlng,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  map = new google.maps.Map(document.getElementById("map"), myOptions);
  if(emplat!=0&&emplat!=""&&emplong!=0&&emplong!="")
	  {
	  count=1;
	  var emplatlong = new google.maps.LatLng(emplat, emplong);
	  marker = new google.maps.Marker({
			position : emplatlong,
			map : map,
			draggable:true,
			animation: google.maps.Animation.BOUNCE,
			icon:'images/map_home.png'
		});
	  }
  google.maps.event.addListener(map, 'click', function(event) {
    placeMarker(event.latLng);
});
	}catch(e)
	{
	alert(e);	
	}
}

function placeMarker(location) {
	if(count>0)
		{
	marker.setMap(null);
		}
	marker = new google.maps.Marker({
		position : location,
		map : map,
		draggable:true,
		animation: google.maps.Animation.BOUNCE,
		icon:'images/map_home.png'
	});
	document.getElementById("emp_long").value=location.lng();
	document.getElementById("emp_lat").value=location.lat();
	count=count+1;
}

function formvalidate()
{
	var emplat=document.getElementById("emp_lat").value;
	var emplong=document.getElementById("emp_long").value;
	var empcode=document.getElementById("emp_code").value;
	if(emplat==""||emplat==0||emplong==""||emplong==0||empcode==""||empcode==0)
		{
		alert("Please check that pointer is placed correctly!");
		return false;
		}
	else{
		return true;
	}
	}
	
</script>
</head>
<body  onload="loadScript()">

	<%@include file="Header.jsp"%>
<center><h3>Please Select Pickup/Drop Point! </h3></center><div></div>
<h2>Instructions : Please click in your home location in map (marker will be added if not done) and after that hit save button down. </h2><br/>
<%
		long empid = 0;

		String employeeId = OtherFunctions.checkUser(session);

		empid = Long.parseLong(employeeId);
		String latlongs[]=new EmployeeService().getEmpLatLong(employeeId);
	%>
	<center><form id="savedata"  action="UpdateEmpGeocode"
							onsubmit="return formvalidate();">
	<input type="hidden" id="emp_code" name="emp_code" value="<%=employeeId %>" />
	<input type="hidden" id="emp_lat" name="emp_lat" value="<%=latlongs[0] %>" />
	<input type="hidden" id="emp_long" name="emp_long" value="<%=latlongs[1] %>" />
	<input type="submit" class="formbutton" value="Save" onClick='submitform();'/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="formbutton" id="" value="Cancel" onClick='window.history.back();'/>
	</form></center><br/>
<input type="hidden" id="cityLat" name="cityLat" value="12.9667" />

<input type="hidden" id="cityLon" name="cityLon" value="77.5667" />

	
	<div id="map" style="width: 100%; height: 520px; float: right;"></div>
	<%@include file="Footer.jsp"%>
</body>
</html>