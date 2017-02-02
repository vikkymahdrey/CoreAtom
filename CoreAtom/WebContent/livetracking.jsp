<!DOCTYPE html>
<html lang="en">
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions" %>
<%@page import="com.agiledge.atom.dashboard.service.LiveTrackingService" %>
<%@page import="com.agiledge.atom.dto.LogTimeDto" %>
<%@page import="java.util.ArrayList" %>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>LiveTracking</title>
	<link rel="icon" href="images/agile.png" type="image/x-icon" />
	
	<link href='https://fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic' rel='stylesheet' type='text/css'>
	
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" integrity="sha512-dTfge/zgoMYpP7QbHy4gWMEGsbsdZeCXz7irItjcC3sPUFtf0kuFbDz/ixG7ArTxmDjLXDmezHubeNikyKGVyQ==" crossorigin="anonymous">
	
	
	<link rel="stylesheet" href="css/atom.css">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body onload="loadScript()">
  
  <div class="wrapper">
	<header>
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-4">
					<a href="#" class="header-logo">
						<img src="images/logo.png" alt="atom" />
					</a>
				</div>
			</div>
		</div>
	</header>
		<%
		 response.setIntHeader("Refresh",120); 
		long empid=0;
        String employeeId = OtherFunctions.checkUser(session);
        if (employeeId == null||employeeId.equals("null") ) {
            String param = request.getServletPath().substring(1) + "___"+ request.getQueryString(); 	response.sendRedirect("index.jsp?page=" + param);
        } else {
            empid = Long.parseLong(employeeId);
            %>
			<%
        }
        OtherDao ob = null;
        ob = OtherDao.getInstance();
        ArrayList<LogTimeDto> list=new LiveTrackingService().getLast3ShiftTimes();
        String timestring[]=new String[3];
        String shift[]=new String[3];
        String log[]=new String[3];
        
        int i=0;
        for(LogTimeDto dto:list){
        	timestring[i]=dto.getLogTime()+" "+dto.getLogtype();
        	shift[i]=dto.getLogTime();
        	log[i]=dto.getLogtype();
        	i++;
        }
        String shift1[]=new String[3];
        String shift2[]=new String[3];
        String shift3[]=new String[3];
        shift1=new LiveTrackingService().getEmployeeValues(shift[0], log[0]);
        shift2=new LiveTrackingService().getEmployeeValues(shift[1], log[1]);
        shift3=new LiveTrackingService().getEmployeeValues(shift[2], log[2]);
        String trip1[]=new String[3];
        String trip2[]=new String[3];
        String trip3[]=new String[3];
        trip1=new LiveTrackingService().getTripReport(shift[0], log[0]);
        trip2=new LiveTrackingService().getTripReport(shift[1], log[1]);
        trip3=new LiveTrackingService().getTripReport(shift[2], log[2]);
        
        
    %>
		
	<div class="page-main-heading mar-top-40 mar-btm-25 overflow-hidden">
		<div class="col-sm-7">
		
			<h2 class="text-uppercase text-blue mar-top-5 text-regular"><i>Live Tracking</i></h2>
		</div>
		<div class="col-sm-5 text-right">
		
			<button class="btn btn-blue font-16" onClick="location.reload();"><img src="images/icon_refresh.png"  />&nbsp;&nbsp;REFRESH</button>
			<a href="atomDashboard.jsp"><button class="btn btn-blue font-16" ><img src="images/icon_dashboard.png"  />&nbsp;&nbsp;DAshboard</button></a>
			<a href="employee_home.jsp"><button class="btn btn-blue font-16"><img src="images/icon_home.png"  />&nbsp;&nbsp;HOME</button>&nbsp;&nbsp;</a>
		</div>		
	</div>
	
	<div class="content-wrapper">
		<div class="col-sm-8">
			<div class="white-box">
				<div class="row">
					<div class="col-sm-6">
						<h6 class="text-uppercase text-semi-bold"><img src="images/lv_tack_emp_travel_status.png" />&nbsp;&nbsp;Employee Travel Status</h5>
					</div>
					<div class="col-sm-6">
						<ul class="legents-wrap pull-right">
							<li class="bg-color1">Scheduled</li>
							<li class="bg-color2">Boarded</li>
							<li class="bg-color3">Reached</li>
						</ul>
					</div>
				</div>
				
				<div class="row lv-track-emp-travel-status mar-top-15">
					<div class="col-sm-4">
						<div class=""><span class="glyphicon glyphicon-time"></span>&nbsp;&nbsp;<%=timestring[2] %></div>						
						<div class="progress">
							<div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="10" aria-valuemin="0" aria-valuemax="100" style="width: 90%">
								<span class="count">&nbsp;&nbsp;&nbsp;<%=shift3[0]%></span>
							</div>
						</div>
						<div class="progress">
							<div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 80%">
								<span class="count">&nbsp;&nbsp;&nbsp;<%= shift3[1] %></span>
							</div>
						</div>
						<div class="progress">
							<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 60%">
								<span class="count">&nbsp;&nbsp;&nbsp;<%= shift3[2] %></span>
							</div>
						</div>						
					</div>
					<div class="col-sm-4">
						<div class=""><span class="glyphicon glyphicon-time"></span>&nbsp;&nbsp;<%=timestring[1] %></div>						
						<div class="progress">
							<div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 90%">
								&nbsp;&nbsp;&nbsp;<span class="count">&nbsp;&nbsp;&nbsp;<%=shift2[0] %></span>
							</div>
						</div>
						<div class="progress">
							<div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 80%">
								&nbsp;&nbsp;&nbsp;<span class="count"><%=shift2[1] %></span>
							</div>
						</div>
						<div class="progress">
							<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 60%">
								&nbsp;&nbsp;&nbsp;<span class="count"><%=shift2[2] %></span>
							</div>
						</div>						
					</div>
					<div class="col-sm-4">
						<div class=""><span class="glyphicon glyphicon-time"></span>&nbsp;&nbsp;<%=timestring[0] %></div>						
						<div class="progress">
							<div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="<%=shift1[0] %>" aria-valuemin="0" aria-valuemax="100" style="width: 90%">
								&nbsp;&nbsp;&nbsp;<span class="count"><%=shift1[0] %></span>
							</div>
						</div>
						<div class="progress">
							<div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="<%=shift1[1] %>" aria-valuemin="0" aria-valuemax="100" style="width: 80%">
								&nbsp;&nbsp;&nbsp;<span class="count"><%=shift1[1] %></span>
							</div>
						</div>
						<div class="progress">
							<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="<%=shift1[2] %>" aria-valuemin="0" aria-valuemax="100" style="width: 60%">
								&nbsp;&nbsp;&nbsp;<span class="count"><%=shift1[2] %></span>
							</div>
						</div>						
					</div>
				</div>
				
			</div>
			
			
			<!-- <div class="white-box mar-top-20 lv-track-map-filter-wrap"> 
				<div class="col-sm-1">
					<img src="images/lv_track_icon.png" />
				</div>
				<div class="col-sm-5">
					 <input type="text" class="form-control" id="exampleInputAmount" placeholder="Employee Name,ID and Vehicle No">
				</div>
				<div class="col-sm-2">
					<select class="form-control">
						<option>Select Site</option>
					</select>
				</div>
				<div class="col-sm-2">
					<select class="form-control">
						<option>Select Shift</option>
					</select>
				</div>
				<div class="col-sm-2">
					<select class="form-control">
						<option>Select Status</option>
					</select>
				</div>
			</div> -->
			
			
			<div id="googleMap" style="width:100%;height:544px;"></div>
			
			
		</div>
		
		<div class="col-sm-4">
			<div class="white-box">
				<h6 class="text-uppercase text-semi-bold"><img src="images/lv_track_trip_status.png" />&nbsp;&nbsp;Trip Status</h5>
				
				<div class="mar-top-15"><span class="glyphicon glyphicon-time"></span>&nbsp;&nbsp;<%=timestring[0] %></div>
				
				<div class="lv-tracking-trip-status-counts">
					<div class="col-sm-4"><%= trip1[0]%></div>
					<div class="col-sm-4"><%= trip1[1]%></div>
					<div class="col-sm-4"><%= trip1[2]%></div>
				</div>
				
				<div class="mar-top-15"><span class="glyphicon glyphicon-time"></span>&nbsp;&nbsp;<%=timestring[1] %></div>
				
				<div class="lv-tracking-trip-status-counts">
					<div class="col-sm-4"><%=trip2[0]%></div>
					<div class="col-sm-4"><%=trip2[1]%></div>
					<div class="col-sm-4"><%=trip2[2]%></div>
				</div>
				
				
				<div class="mar-top-15"><span class="glyphicon glyphicon-time"></span>&nbsp;&nbsp;<%=timestring[2] %></div>
				
				<div class="lv-tracking-trip-status-counts">
					<div class="col-sm-4"><%=trip3[0]%></div>
					<div class="col-sm-4"><%=trip3[1]%></div>
					<div class="col-sm-4"><%=trip3[2]%></div>
				</div>
				
				<div class="legents-wrap mar-top-10">
					<ul class="legents-wrap pull-right">
						<li class="bg-color4">Allocated</li>
						<li class="bg-color5">Running</li>
						<li class="bg-color6">Completed</li>
					</ul>
				</div>
				
			</div>
			<div class="white-box mar-top-20">
				<div class="row lv-track-panic-alarm-stat-wrap">
					<div class="col-sm-4">
						<h6 class="text-semi-bold text-uppercase">Panic Alarm</h6>
						<p class="font-12">Activated</p>
						<div class="mar-top-15">
							<img src="images/lv_track_panic_alarm.png" class="pull-left" />
							<div class="text-blue pull-right font-36 text-bold" id="panicdiv">0</div>
						</div>
						
					</div>
					<div class="col-sm-4">
						<h6 class="text-semi-bold text-uppercase">Single lady </h6>
						<p class="font-12">with Escort </p>
						<div class="mar-top-15">
							<img src="images/lv_track_single_lady.png" class="pull-left" />
							<div class="text-blue pull-right font-36 text-bold" id="ladydiv">0</div>
						</div>
						
					</div>
					<div class="col-sm-4">
						<h6 class="text-semi-bold text-uppercase">Single lady </h6>
						<p class="font-12">without Escort </p>
						<div class="mar-top-15">
							<img src="images/lv_track_single_lady_wo_escort.png" class="pull-left" />
							<div class="text-blue pull-right font-36 text-bold" id="lady2div">0</div>
						</div>
						
					</div>
				</div>
			</div>
			<div class="white-box mar-top-20 lv-track-map-pointer-wrap font-12">
			<div class="tb-row">
				<div class="col-sm-6">
					<img src="images/map_pointer_pani_alarm.png" />
					Panic Alarm<br />Activated cab
				</div>
				<div class="col-sm-6">
					<img src="images/map_pointer_single_lady.png" />
					
					Single Lady <br />without escort
				</div>
			</div>
			<div class="tb-row">
				<div class="col-sm-6">
					<img src="images/map_pointer_single_lady_wo_escot.png" />
					Single Lady <br />with escort
				</div>
				<div class="col-sm-6">
					<img src="images/map_pointer_single_lady_cab.png" />
					Al least One Lady <br />Employee in the Cab	
				</div>
			</div>
			<div class="tb-row">
				<div class="col-sm-6">
					<img src="images/map_pointer_only_guys.png" />
					Only Male Employees <br />in the Cab
				</div>
				<div class="col-sm-6">
					<img src="images/map_pointer_only_driver.png" />
					Only Driver<br />in the Cab
				</div>
			</div>
			</div>
		</div>
		
		
	</div>
	
  </div>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js" integrity="sha512-K1qjQ+NcF2TYO/eI3M6v8EiNYZfA95pQumfvcVrTHtwQVDG+aHRqLi/ETn2uB+1JqwYqVG3LIvdm9lj6imS/pQ==" crossorigin="anonymous"></script>
	
	<script src="http://maps.googleapis.com/maps/api/js"></script>
	
	<script>
	var geocoder;
	var map;
	var marker;
	var cityLat;
	var cityLon;
	var markersArray = [];
	var panicAlarm;
function loadScript() {
	try {
		var script = document.createElement("script");

		script.type = "text/javascript";
		script.src = "https://maps.googleapis.com/maps/api/js?sensor=true&callback=initialize&client=gme-leptonsoftwareexport4&signature=xghu9DIoNr63z8_al_oJCSPWQh0=";			
		document.body.appendChild(script);
	} catch (e) {

		alert("ERRO" + e);
	}
}

function initialize() {
	geocoder = new google.maps.Geocoder();
	var myLatlng = new google.maps.LatLng(12.9667,77.5667);
	var myOptions = {
		zoom : 12,
		center : myLatlng,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("googleMap"), myOptions);
	displayVehicles();
	 var myVar=setInterval(function(){removemark();},55000);
}


function displayVehicles() {
	xmlHttp = GetXmlHttpObject();
	if (xmlHttp == null) {
		alert("Browser does not support HTTP Request");
		return		
	}
	var url = "GetVehiclePosition?branch=1";
	
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

			//fillvehicleselect();
		var filterVal="all"; 
		var tripDetails = retString.split("#");
		if (markersArray) {
			for (i in markersArray) {
				markersArray[i].setMap(null);
			}
			markersArray.length = 0;
		}
		var paniccount=0;
		var eladycount=0;
		var ladycount=0;
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
					 image = 'images/map_pointer_only_driver.png';
					   if(oneTripdet[4]=="stationary")
					 {
						   image = 'icons/whitecar.png';
					 }
				 }
				 else if(oneTripdet[4]=="danger")
					 {
					 no=3;						 
						image = 'images/map_pointer_pani_alarm.png';
						paniccount=paniccount+1;
					if(oneTripdet[8]=="notstop")
						{
						
					playSound("icons/alarm.wav");
						}
					 }
				 else if(oneTripdet[5]<1)
					 {
					 no=4;
					 image = 'images/map_pointer_only_guys.png';
					}
				 else if(oneTripdet[5]>0 && oneTripdet[6]>1)
					 {
					 no=5;
					 image = 'images/map_pointer_single_lady_cab.png';
					 }	
				 else if(oneTripdet[5]==1&&oneTripdet[6]==1&&oneTripdet[7]=="NO")
					 {
					 no=6;
					 image = 'images/map_pointer_single_lady.png';
					 eladycount=eladycount+1;
					 }
				 else if(oneTripdet[5]==1&&oneTripdet[6]==1&&oneTripdet[7]=="YES")
						{
					 no=7; 
					 image = 'images/map_pointer_single_lady_wo_escot.png';

					 ladycount=ladycount+1;
						}					
				 if(flag)
					 {						
					 showVehicles(map, oneTripdet[2],oneTripdet[3],oneTripdet[1],image,oneTripdet[0],oneTripdet[9],no);					
					 }
				 if(oneTripdet[4]=="outofreach")
					 {
				//	 setaddress(oneTripdet[2],oneTripdet[3],oneTripdet[0]);
					 }
				 else if(oneTripdet[4]!="danger")
					 {
				 //checkDeviation(oneTripdet[0], oneTripRoutedet[1], oneTripdet[1],oneTripdet[2], oneTripdet[3],oneTripdet[10],oneTripdet[6],oneTripdet[11]);
					 }
				        	   }
			}
			document.getElementById("panicdiv").innerHTML=paniccount;
			document.getElementById("ladydiv").innerHTML=ladycount;
			document.getElementById("lady2div").innerHTML=eladycount;
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

google.maps.event.addDomListener(window, 'load', initialize);
</script>
	
  </body>
</html>