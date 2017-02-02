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
var directionDisplay;
var directionsService = new google.maps.DirectionsService();
var map;
var cityLat;
var cityLon;
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
    	
}




function showRoute() {
	directionsDisplay.setMap(map);    
    var tripIdSec=(document.getElementById("trip").value).split(":");	
    var tripId=tripIdSec[0];
	//security=tripIdSec[1];
    getPath(tripId);
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
function  getPath(tripId){
    document.getElementById("routedet").style.display = 'block';    
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
        var startnodelatlng=new google.maps.LatLng(startnode[2],startnode[3]);            
        var endnode=nodes[noofnodes-1].split(":");                    
        var endnodelatlng=new google.maps.LatLng(endnode[2],endnode[3]);              
        var waypts = [];                
        
        for(var i=1;i<noofnodes-1;i++)
        {            
            intnode=nodes[i].split(":");
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
                var datasting ='<table border="1" width="460"><thead style="color:#fff;background:#FF6633"><tr><th>SI</th><th>Emp ID</th><th>Emp Name</th></tr></thead>';
                datasting +='<tbody><tr><td colspane="3">Trip Start</td></tr>';                
                for (var i = 1; i < noofnodes-1; i++) {
                    var intnode=nodes[i].split(":");
                    datasting +='<tr><td>'+(i)+'</td><td>'+intnode[0]+'</td><td>'+intnode[1]+'</td></tr>';                                                                                                   
                }
                datasting +='<tr><td colspane="3">Trip Stop</td></tr></tbody></table>';
                summaryPanel.innerHTML=datasting;
                
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
        tripTimeId.innerHTML='<select name="vehicle" id="vehicle" onChange="getTrips()">'+returnText+'</select>';                                             
    }
}
function setTrips() 
{                      
    if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
    { 
        var returnText=xmlHttp.responseText;
        
        var tripTimeId=document.getElementById("tripTd");
        tripTimeId.innerHTML='<select name="trip" id="trip" onChange="showRoute()"><option>Select</option>'+returnText+'</select>';                                             
    }
}
</script>
</head>
<body onload="showMap()">

	<%

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
		
			<h3>View Trips</h3>
			<input type="hidden" value="<%=city[0]%>" id="cityLat"> <input
				type="hidden" value="<%=city[1]%>" id="cityLon">
			<table>
				<tr>
					<td>Date</td>
					<td><input name="tripDate" id="tripDate" type="text" size="6"
						class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}" onchange="getVehicles()" /></td>
					<td>Vehicle</td>
					<td id="vehicleTd"><select name="vehicle"></select></td>
					<td>Trip</td>
					<td id="tripTd"><select name="trip"></select></td>
				</tr>

			</table>

			
			
		
		
</div>
	</div>
	<div id="map" style="width: 50%; height: 60%; float: right;"></div>
	<div id="routedet" style="width: 30%; height: 60%; float: left;"></div>
</body>
</html>