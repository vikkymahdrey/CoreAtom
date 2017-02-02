<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Trace Vehicle</title>
<link
	href="https://code.google.com/apis/maps/documentation/javascript/examples/default.css"
	rel="stylesheet" type="text/css" />
 <script type="text/javascript" src = "https://maps.googleapis.com/maps/api/js?sensor=true&client=gme-leptonsoftwareexport&signature=1t2jNPl7sIPdevsQdfKNrx25bko="></script>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">        
            $(document).ready(function()
            {                                                                        
                $("#tripDate").datepick();                                                            
            });     
        </script>
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
        zoom: 12,
        center: myLatlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map"), myOptions);    
   	directionsDisplay.setMap(map);
 
   	try
   	{
   	tripId=document.getElementById("tripId").value
   	if(tripId.length>0)
   		{
   		displayVehicles();
   		}
}catch(e)
{
	alert(e)
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
	function setTripIdSecurity()
	{
		rempvemarkerandpolygone();
		var tripIdSec=(document.getElementById("trip").value).split(":");	
		tripId=tripIdSec[0];
		security=tripIdSec[1];
		displayVehicles()
	}
function displayVehicles()
{
//	loadScript();
	
	
	//alert("tripId"+tripId+"security"+security);
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
    xmlHttp.onreadystatechange=displayPath;
    xmlHttp.open("POST",url,true)
    xmlHttp.send()
}


function displayPath()
{
	if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
    	    {
    	    	var tripCode=document.getElementById("tripCode").value;
    	    	var vehNo=document.getElementById("vehNo").value;
    	    	var displayCode="";
    	    	if(tripCode!=null && tripCode!="null" && tripCode!="")
    	    		{
    	    		displayCode="<tr><td>TripCode</td><td>"+tripCode+"</td><td>"+vehNo+"</td></tr>";
    	    		}
    	    	try{
    	        var fullnodes = xmlHttp.responseText;    
    	        
    	        var nodes=fullnodes.split("$");
    	        var noofnodes=(nodes.length)-1;          
    	        var startnode=nodes[0].split(":");       
    	      //  alert(startnode[2]+"     "+startnode[3]);
    	        var startnodelatlng=new google.maps.LatLng(startnode[2],startnode[3]);            
    	        var endnode=nodes[noofnodes-1].split(":");
    	     //   alert(endnode[2]+"     "+endnode[3]);
    	        var endnodelatlng=new google.maps.LatLng(endnode[2],endnode[3]);              
    	        var waypts = [];                
    	        
    	        for(var i=1;i<noofnodes-1;i++)
    	        {            
    	            intnode=nodes[i].split(":");
    	         //   alert("lat and long "+intnode[2]+"  "+intnode[3]);
    	            var intnodeltln=new google.maps.LatLng(intnode[2],intnode[3]);                        
    	            waypts.push({location:intnodeltln,stopover:true});
    	        }       
    	        
    	     
    	        var request = {
    	            origin: startnodelatlng,
    	            destination: endnodelatlng,
    	            waypoints: waypts,
    	            optimizeWaypoints: true,
    	            avoidHighways:false,
    	            travelMode: google.maps.DirectionsTravelMode.DRIVING
    	        };
    	        var addressRoute="";
    	        directionsService.route(request, function(response, status) {
    	            if (status == google.maps.DirectionsStatus.OK) {
    	                directionsDisplay.setDirections(response);
    	                          var summaryPanel = document.getElementById("routedet");
    	                var datasting ='<table  border="0" width="330%" align="center"><thead style="color:#fff;background:#FF6633 "><tr><th>SI</th><th>Emp ID</th><th>Emp Name</th></tr></thead>';
    	                datasting +='<tbody></tr>';
    	                for (var i = 1; i < noofnodes-1; i++) {
    	                    var intnode=nodes[i].split(":");
    	                    datasting +='<tr align="center"><td>'+(i)+'</td><td>'+intnode[0]+'</td><td>'+intnode[1]+'</td></tr>';                                                                                                   
    	                }
    	                datasting +='<tr align="center"><td>'+(i)+'</td><td>'+endnode[0]+'</td><td>'+endnode[1]+'</td></tr>';
    	                datasting +=displayCode;
    	                datasting +='</tbody></table>';
    	                summaryPanel.innerHTML=datasting;
    	    
    				showEmployeeGetIn();                
    	            }
    	            else
    	            {
    	                alert(status);}
    	        });
    	            
    	    	}catch(e)
    	    	{
    	    	alert("error"+e);	
    	    	}
    	                            
    	    }
    	    
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
    //    alert(security)
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
}catch(e){alert(e)}
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

function getTrips()
{         	
    var tripDate=document.getElementById("tripDate").value;
    var vehicleId=document.getElementById("vehicle").value;
    var url="GetVehicleTrip?tripDate="+tripDate+"&vehicleId="+vehicleId;                                    
    xmlHttp=GetXmlHttpObject()
    if (xmlHttp==null)
    {
        alert ("Browser does not support HTTP Request");
        return
    }                    
    xmlHttp.onreadystatechange=setTrips	
    xmlHttp.open("POST",url,true)                
    xmlHttp.send(null)
}	
	
function getVehicles()
{                    
    var tripDate=document.getElementById("tripDate").value;
    var url="GetVehicleTrip?tripDate="+tripDate;                                    
    xmlHttp=GetXmlHttpObject()
    if (xmlHttp==null)
    {
        alert ("Browser does not support HTTP Request");
        return
    }                    
    xmlHttp.onreadystatechange=setVehicle	
    xmlHttp.open("POST",url,true)                
    xmlHttp.send(null)
}
    


function setVehicle() 
{                      
    if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
    { 
        var returnText=xmlHttp.responseText;
        var tripTimeId=document.getElementById("vehicleTd");
        tripTimeId.innerHTML='<select name="vehicle" id="vehicle" onChange="getTrips()"><option>Select</option>'+returnText+'</select>';                                             
    }
}
function setTrips() 
{                      
    if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
    { 
        var returnText=xmlHttp.responseText;
        var tripTimeId=document.getElementById("tripTd");
        tripTimeId.innerHTML='<select name="trip" id="trip" onChange="setTripIdSecurity()"><option>Select</option>'+returnText+'</select>';                                             
    }
}
function  showEmployeeGetIn(){
	xmlHttp=GetXmlHttpObject()
		var url="GetRoute?tripIdForGetIn="+tripId;
	//alert(url);
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
			if(i==noofnodes-3)
				{
				centerLatlng=new google.maps.LatLng(ltln[2],ltln[3]);
				}
						var message="Emp Code:"+ltln[0]+"   Name:"+ltln[1];
						placeMarker(ltln[4],latlng,message);
						//displayEmployeeGetInMarkers(marker,latlng,message);
			}
			showVehiclesPath(map,vehiclepositions);
			//setMapCenter(centerLatlng);
			
			}
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
function setMapCenter(centerLatLng)
{
    map.setCenter(centerLatLng);
}

function displayEmployeeGetInMarkers(marker,latlng,message) {	  
	  var infowindow = new google.maps.InfoWindow(
	      { content: message,
	        size: new google.maps.Size(50,50)
	      });
	     google.maps.event.addListener(marker, 'click', function() {
	  infowindow.open(map,marker);
	  });

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
<%
String tripId="";
String vehNo="";
String tripCode="";
if(request.getParameter("tripId")!=null)
{
	tripId = request.getParameter("tripId");
	vehNo = request.getParameter("vehNo");
	tripCode = request.getParameter("tripCode");

}
else
{
        long empid=0;
        String employeeId = OtherFunctions.checkUser(session);
        if (employeeId == null||employeeId.equals("null") ) {
            String param = request.getServletPath().substring(1) + "___"+ request.getQueryString(); 	response.sendRedirect("index.jsp?page=" + param);
        } else {
            empid = Long.parseLong(employeeId);
            %>
	<%@include file="Header.jsp"%>
	<%
        }
}
 
		OtherDao ob = null;
		ob = OtherDao.getInstance();
		String site =""; 
	     try {
	    	 site = session.getAttribute("site").toString();	 
	     }catch(Exception ignor){}
	    
		String[] city = ob.getCity(site);
	%>



	<hr />
	<div id="body">
		<div class="content">

			<h2 align="center"><i>Trace Vehicle Path</i></h2>
			<input type="hidden" value="<%=city[0]%>" id="cityLat"> <input
				type="hidden" value="<%=city[1]%>" id="cityLon">
				 <input	type="hidden" value="<%=tripId%>" id="tripId" name="tripId">
				 <input	type="hidden" value="<%=tripCode%>" id="tripCode" name="tripCode">
				 <input	type="hidden" value="<%=vehNo%>" id="vehNo" name="vehNo">
				 <%if(request.getParameter("tripId")==null){ %>
			<table style="width: 40%">
				<tr>
					<td>Date</td>
					<td><input name="tripDate" id="tripDate" type="text" size="6"
						class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}"
						onchange="getVehicles()" /></td>
					<td>Vehicle</td>
					<td id="vehicleTd"><select name="vehicle"></select></td>
					<td>Trip</td>
					<td id="tripTd"><select name="trip"></select></td>
				</tr>

			</table>
			<%} %>
		</div>
	</div>
	<div id="map" style="width: 100%; height: 60%; float: left;"></div>
	<div id="routedet" style="width: 30%; height: 60%;"></div>
	<%
	if(request.getParameter("tripId")==null){ %>
	<%@include file="Footer.jsp"%>
	 <%}%>
</body>
</html>