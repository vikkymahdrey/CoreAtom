<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.TripDetailsDao"%>
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
<link
	href="http://code.google.com/apis/maps/documentation/javascript/examples/default.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript" src = "https://maps.googleapis.com/maps/api/js?sensor=true&client=gme-leptonsoftwareexport&signature=1t2jNPl7sIPdevsQdfKNrx25bko=&callback=initialize"></script>
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
   		displayVehicles(); 
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
function displayVehicles()
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
    xmlHttp.onreadystatechange=displayPath;
    xmlHttp.open("POST",url,true)
    xmlHttp.send()
}


function displayPath()
{
	if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
    	    {
    	    	
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
//    	                          var summaryPanel = document.getElementById("routedet");
    	                var datasting ='<table border="1" width="460"><thead style="color:#fff;background:#FF6633"><tr><th>SI</th><th>Emp ID</th><th>Emp Name</th></tr></thead>';
    	                datasting +='<tbody></tr>';
    	                datasting +='<tr><td>'+1+'</td><td>'+startnode[0]+'</td><td>'+startnode[1]+'</td></tr>';
    	                for (var i = 1; i < noofnodes-1; i++) {
    	                    var intnode=nodes[i].split(":");
    	                    datasting +='<tr><td>'+(i+1)+'</td><td>'+intnode[0]+'</td><td>'+intnode[1]+'</td></tr>';                                                                                                   
    	                }
    	                datasting +='<tr><td>'+(i+1)+'</td><td>'+endnode[0]+'</td><td>'+endnode[1]+'</td></tr>';
    	                datasting +='</tbody></table>';
  //  	                summaryPanel.innerHTML=datasting;
    	    
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
        markVehicle(lastlat,lastlon);
  }
function markVehicle(lastlat,lastlon)
{
	try
	{
	var myLatLng=new google.maps.LatLng(lastlat, lastlon);
	var title="";
	image = 'icons/redcar.png';
 marker = new google.maps.Marker({
	position : myLatLng,
	map : map,
	icon : image,
	title : title
});
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


function stopPanicSound(tripId)
{
//	alert("reached here");
	try{
	xmlHttp=GetXmlHttpObject()
	var url="PanicAlarmAction?tripId="+tripId+"&source=primary";
	xmlHttp.onreadystatechange=workdone;
	xmlHttp.open("POST",url,true);
	xmlHttp.send()
	}catch(e)
	{
		alert(e);
	}
}
function workdone()
{

}
</script>
</head>
<body onload="showMap()">
	<%
String tripId="";
tripId = request.getParameter("tripId");	
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
}		OtherDao ob = null;
		ob = OtherDao.getInstance();
		String site =""; 
	     try {
	    	 site = session.getAttribute("site").toString();	 
	     }catch(Exception ignor){}
	    
		String[] city = ob.getCity(site);
	%>



	<hr />
	<div id="map" style="width: 100%; height: 60%; float: left;"></div>
	<div id="body">
		<div class="content">
			<p>&nbsp;</p>
			<h3>Panic Button Activated</h3>
			<a href="#" onclick="stopPanicSound(<%=tripId %>)" >Stop The Sound</a>			
			<input type="hidden" value="<%=city[0]%>" id="cityLat"> <input
				type="hidden" value="<%=city[1]%>" id="cityLon">
			<form name="panicAction" action="PanicAlarmAction" method="post">
				<input type="hidden" value="<%=tripId%>" id="tripId" name="tripId">
				<%
				 TripDetailsDto dto= new TripDetailsDao().getPanicTripActionDetails(tripId);
				 try{
				 %>

				<table align="center">
					<tr>
						<td colspan="4" align="center"><b>Vehicle <%=dto.getVehicleNo()%>
								On <%=dto.getTrip_time() %></b></td>
					</tr>
					<tr>
						<td colspan="4" align="center"><b>Driver Details</b></td>
					</tr>
					<tr>
					    <td colspan="4" align="center">Name:<%=dto.getDriverName()%></td>
					</tr>
					<tr>
						<td colspan="4" align="center">Contact:<%=dto.getDriverContact()%></td>
						
					</tr>
					<tr>
						<td colspan="4" align="center"><b>Vendor Details</b></td>
					</tr>
					<tr>
						<td colspan="4" align="center">Name:<%=dto.getVendorName()%></td>
						
					</tr>
					<tr>
						<td colspan="4" align="center">Contact:<%=dto.getVendorContact()%></td>

					</tr>					
					<tr>
						<td colspan="4" align="center"><b>Escort Details</b></td>
					</tr>
					<%if(dto.getEscort()!=null && !dto.getEscort().equals("null") &&!dto.getEscort().equals("")){ %>
					<tr>
						<td colspan="4" align="center">Name:<%=dto.getEscortName()%></td>
						
					</tr>
					<tr>
					<td colspan="4" align="center">Clock:<%=dto.getEscortclock()%></td>
					
					</tr>
					<tr>
					<td colspan="4" align="center">Contact:<%=dto.getEscortContact()%></td>
					
					
					</tr>
					<%}else{%>
					<tr>
						<td align="center" colspan="4">No escort in the Vehicle</td>
					</tr>
					<%} %>
					<tr>
						<td colspan="4" align="center"></td>
					</tr>
					<tr>
						<td colspan="4" align="center"><b>Employees Details In
								the Vehicle</b></td>
					</tr>
					<tr>
						<td><b>Name</b></td>
						<td><b>Code</b></td>
						<td><b>Gender</b></td>
						<td><b>Contact</b></td>
					</tr>

					<%for(TripDetailsChildDto childDto:dto.getTripDetailsChildDtoList()) {%>
					<tr>
						<td><%=childDto.getEmployeeName() %></td>
						<td><%=childDto.getEmployeeId() %></td>
						<td><%=childDto.getGender()%></td>
						<td><%=childDto.getContactNumber()%></td>
					</tr>
					<%} %>
					<tr>
						<td colspan="4" align="center"><b>Action Taken details</b></td>
					</tr>
					<%
				  if(dto.getPanicdto()==null){
				  %>
					<tr>
						<td>Cause Of Alarm</td>
						<td colspan="3"><select name="causeOfalarm"><option>Select</option>
								<option value="False Alarm">
									False Alarm
									</optio>
								<option value="Accident">Accident</option>
								<option value="Rash Driving">Rash Driving</option>
								<option value="Driver Issue">Driver Issue</option>
								<option value="Break Down">Break Down</option></select></td>
					</tr>
					<tr>
						<td rowspan="3">Primary Action</td>
						<td colspan="3"><textarea name="actionDesc" rows="3"
								cols="70"></textarea></td>
					</tr>
					<tr>
						<td></td>
						<td colspan="2"><input type="submit" value="Add"
							class="formbutton">&nbsp;&nbsp;&nbsp;&nbsp;<input
							type="reset" value="Reset" class="formbutton"></td>
						<td></td>
					</tr>
					<%}else{ %>
					<tr>
						<td>Cuase Of Alarm</td>
						<td colspan="3"><%=dto.getPanicdto().getAlarmCause() %></td>
					</tr>
					<tr>
						<td rowspan="3">Primary Action</td>
						<td colspan="3"><%=dto.getPanicdto().getPrimaryAction() %></td>
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr><td>&nbsp;</td></tr>
					<tr>
						<td>Primary Action Taken By</td>
						<td colspan="3"><%=dto.getPanicdto().getPrimaryActiontakenByName() %></td>
					</tr>
					<tr>
						<td>Primary Action Taken time</td>
						<td colspan="3"><%=dto.getPanicdto().getPrimaryActiontakenTime() %></td>
					</tr>
					<% if(dto.getPanicdto().getApprovedByName()!=null && !dto.getPanicdto().getApprovedByName().equals("null") && !dto.getPanicdto().getApprovedByName().equals("")) {%>
					<tr>
					<td> Approved By</td>
					<td colspan="3"><%=dto.getPanicdto().getApprovedByName() %></td>					
					</tr>
					<tr>
					<td> Approved Time</td>
					<td colspan="3"><%=dto.getPanicdto().getApprovedByTime() %></td>					
					</tr>	
					<%}} %>							 
				 </table>
				 				 </form>
				 <p>&nbsp;&nbsp;&nbsp;&nbsp;</p>
				<%}catch(Exception e) {System.out.println("Error"+e);}
		
%>

					</div>
	</div>
	<%@include file="Footer.jsp"%>	
</body>
</html>