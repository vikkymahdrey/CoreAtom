<%@page import="com.agiledge.atom.dao.BranchDao"%>
<%@page import="com.agiledge.atom.dto.BranchDto"%>
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
<script type="text/javascript" src="http://code.jquery.com/jquery-2.1.3.min.js"></script>
<link href="css/select2.css" rel="stylesheet"/>
<script src="js/select2.js"></script>
<title>Live Tracking</title>
<%
	APLService aplService =new APLService();
ArrayList<APLDto> areaDtos=aplService.getAreas();
%>
<script type="text/javascript">
			$(document).ready(function() {
				$(".vehicleclass").select2();
			});
			</script>
<script type="text/javascript">
	var geocoder;
	var map;
	var marker;
	var cityLat;
	var cityLon;
	var markersArray = [];
	var panicAlarm;
	function fullmap()
	{
		try{
			var status=document.getElementById('screanchangeanchor').innerHTML;
			if(status=="FullScreen")
			{	
			document.getElementById('screanchangeanchor').innerHTML="Exit FullScreen";			
			document.getElementById("map").style.position="absolute";
			document.getElementById("map").style.height="100%";
			document.getElementById("map").style.width="100%";
			loadScript();
			}
			else if(status=="Exit FullScreen")
				{
				location.reload();
				}			
		}catch(e){alert(e);}
	}
	function exitfullmap()
	{
		try{
			document.getElementById('screanchangeanchor').innerHTML="FullScreen";
			//document.getElementById("map").style.position="relative";
			document.getElementById("map").style.width="60%";
			loadScript();
		}catch(e){alert(e);}
	}
	
	function loadScript() {
		cityLat = document.getElementById("cityLat").value;
		cityLon = document.getElementById("cityLon").value;
		try {
			var script = document.createElement("script");

			script.type = "text/javascript";
			script.src = "https://maps.googleapis.com/maps/api/js?sensor=true&callback=initialize&client=gme-leptonsoftwareexport&signature=1t2jNPl7sIPdevsQdfKNrx25bko=";			
			document.body.appendChild(script);
		} catch (e) {

			alert("ERRO" + e);
		}
	}

	function initialize() {
		geocoder = new google.maps.Geocoder();
		var myLatlng = new google.maps.LatLng(cityLat, cityLon);
		var myOptions = {
			zoom : 12,
			center : myLatlng,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		};
		map = new google.maps.Map(document.getElementById("map"), myOptions);
		displayVehicles();
		 var myVar=setInterval(function(){removemark();},55000);
	}
	
	
	function displayVehicles() {
		xmlHttp = GetXmlHttpObject();
		if (xmlHttp == null) {
			alert("Browser does not support HTTP Request");
			return		
		}
		var branch=document.getElementById("branch").value;
		var filshift=document.getElementById("filshift").value;
		var filregno=document.getElementById("filregno").value;
		var url = "GetVehiclePosition?branch="+branch+"&shift="+filshift+"&regno="+filregno;
		
		xmlHttp.onreadystatechange = showVehiclePosition;
		xmlHttp.open("POST", url, true);
		xmlHttp.send(null);
	}
	function GetXmlHttpObject() {
		var xmlHttp = null;
		if (window.XMLHttpRequest) {
			xmlHttp = new XMLHttpRequest();
		}
		//catch (e)
		else if (window.ActiveXObject) {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}

		return xmlHttp;
	}


	
	function showVehiclePosition() {
		if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete") {
			try{
				var retString = xmlHttp.responseText;	

				fillvehicleselect();
			var filterVal=document.getElementById("filter").value; 
			var tripDetails = retString.split("#");
			if (markersArray) {
				for (i in markersArray) {
					markersArray[i].setMap(null);
				}
				markersArray.length = 0;
			}
				for ( var i = 0; i < tripDetails.length-1; i++) {
					var flag=true;
					var oneTripRoutedet = tripDetails[i].split("|");
					var oneTripdet= oneTripRoutedet[0].split("~");
					/*
					onetripdet[
					           0-tripId
					           1-Vehicle No					           
					           2-curent lat
					           3-curent long
					           4-run/danger/outofreach/stationary
					           5-female count
					           6-emp count
					           7-security
					           8-panicsound stop
					           9-trip Code
					           10-empCount
					           11-dateTime

					*/
					var no=0;
					           if(filterVal=="stationary"  )
								{
					        	     if(oneTripdet[4]=="stationary") {
										flag=false;
										image = 'icons/whitecar.png';
										showVehicles(map, oneTripdet[2],oneTripdet[3],oneTripdet[1],image,oneTripdet[0],oneTripdet[9],no);
					        	     }
								}
					           else 
					        	   {           
					if(filterVal=="priority" && oneTripdet[5]<1)
						{
						flag=false;
						no=1;
						}
					else if (oneTripdet[6] == 0) {
						no=2;
						 image = 'icons/yellowcar.png';
						   if(oneTripdet[4]=="stationary")
						 {
							   image = 'icons/whitecar.png';
						 }
					 }
					 else if(oneTripdet[4]=="danger")
						 {
						 no=3;						 
						 document.getElementById("rprttble").insertRow(-1).innerHTML = '<td><a href="#" onclick="PanicVehicle('+oneTripdet[0]+');">'
								+ oneTripdet[1]+'</a></td><td>Panic Alarm Pressed</td><td id='+oneTripdet[0]+'></td><td>'+oneTripdet[10]+'</td><td>'+oneTripdet[6]+'</td><td>'+oneTripdet[11]+'</td>';
								setaddress(oneTripdet[2],oneTripdet[3],oneTripdet[0]);
						image = 'icons/redcar.png';
						if(oneTripdet[8]=="notstop")
							{
							
						playSound("icons/alarm.wav");
							}
						 }
					 else if(oneTripdet[5]<1)
						 {
						 no=4;
						 image = 'icons/bluecar.png';
						}
					 else if(oneTripdet[5]>0 && oneTripdet[6]>1)
						 {
						 no=5;
						 image = 'icons/purplecar.png';
						 }	
					 else if(oneTripdet[5]==1&&oneTripdet[6]==1&&oneTripdet[7]=="NO")
						 {
						 no=6;
						 image = 'icons/orangecar.png';
						 }
					 else if(oneTripdet[5]==1&&oneTripdet[6]==1&&oneTripdet[7]=="YES")
							{
						 no=7; 
						 image = 'icons/blackcar.png';
							}					
					 if(flag)
						 {						
						 showVehicles(map, oneTripdet[2],oneTripdet[3],oneTripdet[1],image,oneTripdet[0],oneTripdet[9],no);					
						 }
					 if(oneTripdet[4]=="outofreach")
						 {
					//	 alert(""+oneTripdet[2]+oneTripdet[3]+oneTripdet[0]);						 
						 document.getElementById("rprttble").insertRow(-1).innerHTML='<td><a href="#" onclick="SpecificVehicle('+oneTripdet[0]+');" >'+oneTripdet[1]
							+ '</a></td><td>Out of reach now</td><td id='+oneTripdet[0]+'></td><td>'+oneTripdet[10]+'</td><td>'+oneTripdet[6]+'</td><td>'+oneTripdet[11]+'</td>';
						 setaddress(oneTripdet[2],oneTripdet[3],oneTripdet[0]);
						 }
					 else if(oneTripdet[4]!="danger")
						 {
					 checkDeviation(oneTripdet[0], oneTripRoutedet[1], oneTripdet[1],oneTripdet[2], oneTripdet[3],oneTripdet[10],oneTripdet[6],oneTripdet[11]);
						 }
					        	   }
				}
				
				
			} catch (e) {
				//alert(e);			
		}
		}
	}
		
	function setaddress(curLat,curLng,tripId)
	{
		
			var latlng = new google.maps.LatLng(curLat, curLng);
			try{
	geocoder.geocode({'latLng': latlng}, function(results, status) {
	      if (status == google.maps.GeocoderStatus.OK) {	    	  
	        if (results[1]) {
	        	var res=results[0].formatted_address.split(",");
	        	document.getElementById(tripId).innerHTML=res[0]+","+res[1]+","+res[2];
	        	
	        }	        
	      }
	      else
	    	  {
	    	  document.getElementById(tripId).innerHTML=status;
	    	  }
	});
			}catch(ee)
			{//alert(ee);
			}
		//document.getElementById(tripId).innerHTML="";
	}
	
	
	function showVehicles(map, curLat,curLng,VehicleNo,image,tripid,tripCode,no) {
try{

	var infowindow;
	if(tripid=="-1") {
		infowindow = new google.maps.InfoWindow({
			 
		      content:  VehicleNo
		  });
	} else { 
	  infowindow = new google.maps.InfoWindow({
		 
	      content: '<a href="#" onclick="SpecificVehicle('+tripid+');" > '+VehicleNo+'</a> for <b>'+tripCode+'</b>'
	  });
	}
	 /*
 if(no==2)
		 {
	 image = 'icons/yellowcar.png';	 
		 }
	 else if(no==3)
	 {
			image = 'icons/redcar.png';
	 }
	 else if(no==4)
	 {
		 image = 'icons/bluecar.png';
	 }
	 else if(no==5)
	 {
		 image = 'icons/purplecar.png';
	 }
	 else if(no==6)
	 {
		 image = 'icons/pinkcar.png';
	 }
	 else if(no==7)
	 {
		 image = 'icons/blackcar.png';
	 }
	 */
			var myLatLng = new google.maps.LatLng(curLat, curLng);

			
				var marker = new google.maps.Marker({
					position : myLatLng,
					map : map,
					icon : image
				});
				markersArray.push(marker);
				  google.maps.event.addListener(marker, 'click', function() {
					    infowindow.open(map,marker);
					  });				 

}catch(e)
{//alert(e);
}


	}
	
	
	function checkDeviation(tripId,latLngs,vehicleNo,curLat,curLng,totalEmps,inemps,datetime)
	{			
		var alllatlonsplitted = latLngs.split("~");
		var latlngLength = alllatlonsplitted.length;
		var directionsService = new google.maps.DirectionsService();
		var startnodelatlng;
		var endnodelatlng;
		var otherlatlng;
		var waypts = [];
		var flag = false;
		var addressRoute = "";
		try {
			for ( var i = 0; i < latlngLength - 1; i++) {
				var latlngsplitted = alllatlonsplitted[i].split(":");
				if (i == 0) {
					startnodelatlng = new google.maps.LatLng(latlngsplitted[0],
							latlngsplitted[1]);
				} else if (i == (latlngLength - 2)) {

					endnodelatlng = new google.maps.LatLng(latlngsplitted[0],
							latlngsplitted[1]);
				} else {
					otherlatlng = new google.maps.LatLng(latlngsplitted[0],
							latlngsplitted[1]);
					waypts.push({
						location : otherlatlng,
						stopover : false
					});
				}

			}
			var request = {
				origin : startnodelatlng,
				destination : endnodelatlng,
				waypoints : waypts,
				optimizeWaypoints : true,
				avoidHighways : false,
				travelMode : google.maps.DirectionsTravelMode.DRIVING
			};

			directionsService
					.route(
							request,
							function(response, status) {
								if (status == google.maps.DirectionsStatus.OK) {	
									
									for (var p = 0; p < response.routes[0].legs.length; p++) {
									    var steps = response.routes[0].legs[p].steps;
									    for (var j = 0; j < steps.length; j++) {
									        var nextSegment = steps[j].path;
									        for (var k = 0; k < nextSegment.length; k++) {
									        	addressRoute+=nextSegment[k]+"|";									           
									        }
									    }
									}
									var alllatlon = addressRoute.split("|");
									flag = false;
									for (i = 0; i < alllatlon.length; i++) {
										if (calculateDistance(alllatlon[i],
												curLat, curLng)) {
											flag = true;
											break;
										}
									}
									
										if (flag == false) {											
											document.getElementById("rprttble").insertRow(-1).innerHTML='<td><a href="#" onclick="SpecificVehicle('+tripId+');" >'+vehicleNo
											+ '</a></td><td>deviating</td><td id='+tripId+'></td><td>'+totalEmps+'</td><td>'+inemps+'</td><td>'+datetime+'</td>';
											setaddress(curLat,curLng,tripId);
											//document.getElementById("tripList").innerHTML += reportinnerHTML
										}
										else
											{
											
											document.getElementById("rprttble").insertRow(-1).innerHTML='<tr><td><a href="#" onclick="SpecificVehicle('+tripId+');" >'+vehicleNo
											+ '</a></td><td>going correct</td><td id='+tripId+'></td><td>'+totalEmps+'</td><td>'+inemps+'</td><td>'+datetime+'</td>';
											setaddress(curLat,curLng,tripId);
											 //document.getElementById("tripList").innerHTML +=reportinnerHTML;
											}																												
									
								} else {
									//alert("Status" + status);
								}

							});
			

			//document.getElementById("tripList").innerHTML += '</table>';
		} catch (e) {
			//alert("Error" + e);
		}

	}		
	
	function placeMarker(lat,lon) {	
		
		var markmarker=new google.maps.LatLng(lat, lon);
		marker = new google.maps.Marker({
			position : markmarker,
			map : map
		});
	}
	function calculateDistance(latlon1, curlat, curlon) {
		try {
			latlon1=latlon1.substring(1,latlon1.length-2);			
			var latlons1 = latlon1.split(",");
		//	placeMarker(latlons1[0],latlons1[1]);
			var R = 6371; // km
			var dLat = (latlons1[0] - curlat) * Math.PI / 180;
			var dLon = (latlons1[1] - curlon) * Math.PI / 180;
			var lat2 = latlons1[0] * Math.PI / 180;
			var lat1 = curlat[0] * Math.PI / 180;
			var a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
					+ Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1)
					* Math.cos(lat2);
			var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			var d = R * c;
			if (d < .5)
				return true;
			else
				return false;			
			return false;
		} catch (e) {
			//alert(e);
		}
	}
	 function playSound(soundfile) {
		 document.getElementById("dummy").innerHTML=
		 "<embed src=\""+soundfile+"\" hidden=\"true\" autostart=\"true\" loop=\"false\" />";
		 }
	 
	function removemark() {
		document.getElementById("rprttble").innerHTML='<table id="rprttble" border="1"><tr><td><b>Vehicle #</b></td><td><b>Status</b></td><td><b>Location</b></td><td><b>Total Emps</b></td><td><b>Boarded</b></td><td><b>Date-Time</b></td></tr></table>';
		displayVehicles();
	}
	  function SpecificVehicle(tripId){
    		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
    		var size = "height=500,width=900,top=100,left=100," + params;
    		var url="traceVehiclePath.jsp?tripId="+tripId;	
    	    newwindow = window.open(url, 'vehicleTrace', size);

    		if (window.focus) {
    			newwindow.focus();
    		}
    	}
	  function PanicVehicle(tripId){
  		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
  		var size = params;
  		var url="panicAlarmTrip.jsp?tripId="+tripId;	
  	    newwindow = window.open(url, 'vehicleTrace');

  		if (window.focus) {
  			newwindow.focus();
  		}
  	}
	  
	  function fillvehicleselect(){
		  var filshift=document.getElementById("filshift").value;
		  var regno=document.getElementById("filregno").value;
		  var x = document.getElementById("filregno");
		  var option;
		  document.getElementById('filregno').options.length = 0;
		  if(regno=="ALL"||regno=="all")
			  {
		  option = document.createElement("option");
		  option.text = "ALL";
		  x.add(option);
			  }
		  else
			  {
		  option1 = document.createElement("option");
		  option1.text = "ALL";
		  x.add(option1);
		  option = document.createElement("option");
		  option.text = regno;
		  x.add(option);
		  x.value=regno;
			  }
		  if(filshift!="all")
			{
			  x.value="ALL";
			}
		if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete") {
				try{
					var retString = xmlHttp.responseText;
					var tripDetails = retString.split("#");
						for ( var i = 0; i < tripDetails.length-1; i++) {
							
							var oneTripRoutedet = tripDetails[i].split("|");
							var oneTripdet= oneTripRoutedet[0].split("~");
							if(filshift=="all"||filshift=="ALL")
								{
								if(regno!=oneTripdet[1])
								{
							option = document.createElement("option");
							option.text = oneTripdet[1];
							x.add(option);
								}
								}
							else if(filshift=="in"&&oneTripdet[12]=="IN")
								{
								if(regno!=oneTripdet[1])
									{
								option = document.createElement("option");
								option.text = oneTripdet[1];
								x.add(option);
									}
								}
							else if(filshift=="out"&&oneTripdet[12]=="OUT")
							{
								if(regno!=oneTripdet[1])
								{
							option = document.createElement("option");
							option.text = oneTripdet[1];
							x.add(option);
							}
							}
						}
				}catch (e) {
					alert(e);			
				}
				}

		  
		  
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
<body onload="loadScript()">
	<%
		long empid = 0;
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
		ArrayList<BranchDto> dtos = new BranchDao().getLocations();
	%>
	<p>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Choose Location&nbsp;&nbsp;&nbsp;&nbsp;<select
			id="branch">
			<%
				for (BranchDto dto : dtos) {
			%>
			<option value="<%=dto.getId()%>"><%=dto.getLocation()%></option>
			<%
				}
			%>

		</select>
	</p>
	<div id="parent" style="width:100%">
	<div id="child1" style="width:60%">
	<table style="width float: left;">
		<tr>
			<td><img alt="" src="icons\yellowcar.png" title="Only driver in the cab"/>&nbsp;&nbsp;&nbsp;
			<img alt="" src="icons\bluecar.png" title="Only male employees in the cab">&nbsp;&nbsp;&nbsp;
			<img alt="" src="icons\redcar.png" title="Panic alarm activated cab"/>&nbsp;&nbsp;&nbsp;
			<img alt="" src="icons\orangecar.png" title="Single lady employee without Escort"/>&nbsp;&nbsp;&nbsp;
			<img alt="" src="icons\purplecar.png" title="Atleast one lady employee in the cab"/>&nbsp;&nbsp;&nbsp;
			<img alt="" src="icons\blackcar.png" title="Single lady employee with Escort"/></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td><a href="#" onclick="fullmap()" id="screanchangeanchor">FullScreen</a></td>
		</tr>
	</table>
	</div>
	<div id="child2" style="width:60%">
	<div id="map" style="width: 100%; height: 400px; float:left;"></div>
	</div>
	
	<div id="child3" style="width:38%;float: right;">
	<div id="tripList" style="width: 100%; height: 400px; float: right;overflow-y: scroll;" >		
	<table id="rprttble" border="1"><tr><td><b>Vehicle #</b></td><td><b>Status</b></td><td><b>Location</b></td><td><b>Total Emps</b></td><td><b>Boarded</b></td><td><b>Date-Time</b></td></tr></table>
	</div>	
		</div>
	</div>

	<table>
		<tr>
			<td>Priority&nbsp;&nbsp;<select id="filter" style="width: 150px">
					<option value="all">ALL</option>
					<option value="priority">Priority vehicle</option>
					<option value="stationary">Stationary vehicle</option>
			</select >&nbsp;&nbsp;Shift Type&nbsp;&nbsp;<select style="width: 150px" id="filshift" onchange='loadScript();'>
		<option value="all">ALL</option>
		<option value="in">IN</option>
		<option value="out">OUT</option>
		</select>&nbsp;&nbsp;Vehicle&nbsp;&nbsp;
		<select class="vehicleclass" id="filregno" style="width: 150px" onChange='loadScript();'>
		<option value="all">ALL</option>
		</select></td>
		</tr>
	</table>

	<div id="dummy"></div>

	<div style="display: none;"></div>

	<%@include file="Footer.jsp"%>
</body>
</html>