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
<title>Live Tracking</title>
<%
	APLService aplService =new APLService();
ArrayList<APLDto> areaDtos=aplService.getAreas();
%>
<script type="text/javascript">
	var geocoder;
	var map;
	var marker;
	var cityLat;
	var cityLon;
	var markersArray = [];
	var panicAlarm;

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
		 var myVar=setInterval(function(){removemark();},35000);
	}
	
	
	function displayVehicles() {
		xmlHttp = GetXmlHttpObject();
		if (xmlHttp == null) {
			alert("Browser does not support HTTP Request");
			return		
		}
		var branch=document.getElementById("branch").value; 
		var url = "GetVehiclePosition?branch="+branch;
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
					           4-run/danger/outofreach
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
/*						 document.getElementById("rprttble").insertRow(-1).innerHTML = '<td><a href="#" onclick="PanicVehicle('+oneTripdet[0]+');">'
								+ oneTripdet[1]+'</a></td><td>Panic Alarm Pressed</td><td id='+oneTripdet[0]+'></td><td>'+oneTripdet[10]+'</td><td>'+oneTripdet[6]+'</td><td>'+oneTripdet[11]+'</td>';
								setaddress(oneTripdet[2],oneTripdet[3],oneTripdet[0]);
								*/
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
						 image = 'icons/pinkcar.png';
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
					// if(oneTripdet[4]=="outofreach")
					//	 {
					//	 alert(""+oneTripdet[2]+oneTripdet[3]+oneTripdet[0]);						 
						 /*document.getElementById("rprttble").insertRow(-1).innerHTML='<td><a href="#" onclick="SpecificVehicle('+oneTripdet[0]+');" >'+oneTripdet[1]
							+ '</a></td><td>Out of reach now</td><td id='+oneTripdet[0]+'></td><td>'+oneTripdet[10]+'</td><td>'+oneTripdet[6]+'</td><td>'+oneTripdet[11]+'</td>';
						 setaddress(oneTripdet[2],oneTripdet[3],oneTripdet[0]);
						 */
					//	 }
					 //else
					//	 {
					 //checkDeviation(oneTripdet[0], oneTripRoutedet[1], oneTripdet[1],oneTripdet[2], oneTripdet[3],oneTripdet[10],oneTripdet[6],oneTripdet[11]);
					//	 }
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
<p><a href="trackVehicle.jsp">Exit Full Screen</a></p>

	<div id="map" style="width: 100%; height: 800px; float:left;"></div>




	<div id="dummy"></div>

	<div style="display: none;"></div>

	
</body>
</html>