
<%-- <%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%> --%>
<%@page import="com.agiledge.atom.service.ShuttleSocketService"%>
<%@page import="com.agiledge.atom.dto.GeoTagDto"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.dao.ShuttleSocketDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.RouteService"%>
<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@page import="com.agiledge.atom.dto.EmployeeSubscriptionDto"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" href="images/agile.png" type="image/x-icon" />
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>Survey Status</title>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://www.nfl.com" prefix="disp"%>
<%

	
	String time = request.getParameter("time");
	ArrayList<RouteDto> routedto = null;
	int lessthanfivetm=0,fivemtoonek=0,onetotwo=0,grthantwo=0;
	List<GeoTagDto> list= null;
	List<GeoTagDto> empSameasNoodle= null;
	List<GeoTagDto> allDistPerAdmin= null;
	if(time!=null){
		int routeId= Integer.parseInt(request.getParameter("route"));
		String logtype=request.getParameter("type");
		String comp=request.getParameter("comp");
		routedto=new RouteService().getRouteDetailsWithoutAPL(routeId);
		list=new ShuttleSocketService().getemployeeGeoTagDetails1(routeId,logtype,time);
		empSameasNoodle=new ShuttleSocketService().employeeValueMatchWithNoodle(routeId, logtype, time, comp);
		allDistPerAdmin=new ShuttleSocketService().employeeValueDifrrWthNoodle(routeId, logtype, time, comp);
		for(GeoTagDto dto : allDistPerAdmin){
			if(dto.getDistanceperadmin()<=500){
				lessthanfivetm++;
			}else if(dto.getDistanceperadmin()>500 && dto.getDistanceperadmin()<=1000){
				fivemtoonek++;
			}else if(dto.getDistanceperadmin()>1000 && dto.getDistanceperadmin()<=2000){
				onetotwo++;
			}else{
				grthantwo++;
			}
		}
	}
%>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script>
var i=1;
var position;
var lat,lng;
var labelIndex = 0;
var map;
var marker;
var markers;
var count=0;
var cityLat;
var cityLon;
var emplat=12.8452347780636;
var emplong=77.66308307647705;
var inlatlong1;
var directionsDisplay;
var directionsService;
var stepDisplay;
var markerArray = [];
function loadScript() {
	 
	 	cityLat = emplat;
		cityLon = emplong;
		try {
			var script = document.createElement("script");
			script.type = "text/javascript";
			script.src = "https://maps.googleapis.com/maps/api/js?sensor=true&callback=initialize&libraries=places&client=gme-leptonsoftwareexport&signature=1t2jNPl7sIPdevsQdfKNrx25bko=";			
			document.body.appendChild(script);
	
		} catch (e) {

			alert("ERRO" + e);
		}
		<%if(request.getParameter("type")!=null){%>
		getRouteAndTime('<%=request.getParameter("time")%>','<%=request.getParameter("route")%>');
		<%}%>
	}
function initialize() {
	geocoder = new google.maps.Geocoder();
	var myLatlng = new google.maps.LatLng(cityLat, cityLon);
	var myOptions = {
		zoom : 10,
		center : myLatlng,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map"), myOptions);
	directionsDisplay = new google.maps.DirectionsRenderer;
    directionsService = new google.maps.DirectionsService;
    stepDisplay = new google.maps.InfoWindow();
	<%if(routedto!=null && routedto.size()>0){
	 for(RouteDto dto: routedto){%>
		var myLatLng12 = new google.maps.LatLng(<%=dto.getLattitude()%>, <%=dto.getLongitude()%>);
		placeMarker(myLatLng12,'<%=dto.getLandmark()%>');
		<%}%>
	 var firstlatlng=new google.maps.LatLng(<%=routedto.get(0).getLattitude()%>, <%=routedto.get(0).getLongitude()%>);
	 var lastlatlng=new google.maps.LatLng(<%=routedto.get(routedto.size()-1).getLattitude()%>, <%=routedto.get(routedto.size()-1).getLongitude()%>);
	  DisplayRoute(directionsService, directionsDisplay,firstlatlng,lastlatlng);
	  directionsDisplay.setMap(map);<%
	}
		if(list!=null){
			for(GeoTagDto gdto : list){
			if(request.getParameter("comp").equalsIgnoreCase("HOME")){%>
				var emphouse=new google.maps.LatLng(<%=gdto.getHomelat()%>,<%=gdto.getHomelong()%>);
				placeMarker3(emphouse,'<%=gdto.getEmpName()%>');
			<%}else if(request.getParameter("type").equalsIgnoreCase("IN")){%>
			
				var empadd=new google.maps.LatLng(<%=gdto.getPicklat()%>,<%=gdto.getPicklong()%>);
				placeMarker2(empadd,'<%=gdto.getEmpName()%>');
			<%}else{%>
			var empdrop=new google.maps.LatLng(<%=gdto.getDroplat()%>,<%=gdto.getDreoplong()%>);
			placeMarker2(empdrop,'<%=gdto.getEmpName()%>');
<%}
					
			}
		}%>
	}
	var waypts = [];
	var lat, lng;
	function placeMarker(position1, landmark) {

/* 		marker = new google.maps.Marker({
			position : position1,
			title : landmark,
			map : map,
			//animation: google.maps.Animation.BOUNCE,
			icon : 'images/icon_3.png'
		});
		var cordinates = new Array();
		cordinates[1] = position1;
		cordinates[0] = new google.maps.LatLng(lat, lng);
		var linepath = new google.maps.Polyline({
			path : cordinates,
			geodesic : true,
			strokeColor : '#FF0000',
			strokeOpacity : 2.0,
			strokeWeight : 3
		});

		linepath.setMap(map);
		lat = position1.lat();
		lng = position1.lng();  */
	 	waypts.push({location:position1,stopover:true});
		
	}
	function placeMarker2(position2, landmark) {
		marker = new google.maps.Marker({
			position : position2,
			title : landmark,
			map : map,
			//animation : google.maps.Animation.BOUNCE,
			icon : 'images/icon_2.png'
		});
	}
	function placeMarker3(position3, landmark) {
		marker = new google.maps.Marker({
			position : position3,
			title : landmark,
			map : map,
			//animation: google.maps.Animation.BOUNCE,
			icon : 'images/icon_1.png'
		});
	}
	function getRouteAndTime(previoustime,previousroute) {
		var type = document.getElementById("type").value;
		try {
			var xmlhttp;
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					var result = xmlhttp.responseText;
					resultlist = result.split("$");
					document.getElementById("time").innerHTML = "<select name='time' id='time' ><option value=''>All</option>"
							+ resultlist[0] + "</select>";
					document.getElementById("route").innerHTML = "<select name='route' id='route' ><option value=''>All</option>"
							+ resultlist[1] + "</select>";
				}
			}
			xmlhttp.open("POST", "GetLogTimeAndRouteBasedOnType?logtype="
					+ type+"&previoustime="+previoustime+"&previousroute="+previousroute, true);
			xmlhttp.send();
		} catch (e) {

			alert(e);
		}
	}
	function validate(){
		var shift=  document.getElementById("type").value;
		var time = document.getElementById("time").value;
		var route=document.getElementById("route").value;
		var com=document.getElementById("comp").value;
		if(shift==""){
			document.getElementById("errortag").innerHTML ="Please select log type";
			$('.san').show();
			document.getElementById("type").focus();
			return false;
		}else if(route==""){
			document.getElementById("errortag").innerHTML ="Please select route";
			$('.san').show();
			document.getElementById("route").focus();
			return false;
		}else if(time==""){
			document.getElementById("errortag").innerHTML ="Please select shift time";
			$('.san').show();
			document.getElementById("time").focus();
			return false;
		}else if(com==""){
			document.getElementById("errortag").innerHTML ="Please choose constant for compare";
			$('.san').show();
			document.getElementById("comp").focus();
			return false;
		}else{
			return true;
		}
		
	}
	 function DisplayRoute(directionsService, directionsDisplay,start,end) {
	        directionsService.route({
	          origin: start,  // Haight.
	          destination: end, 
	          waypoints: waypts,
	          optimizeWaypoints: true,
	          travelMode: google.maps.TravelMode.DRIVING
	        }, function(response, status) {
	          if (status == google.maps.DirectionsStatus.OK) {
	            directionsDisplay.setDirections(response);
	            showSteps(response, markerArray, stepDisplay, map);
	          } else {
	            window.alert('Directions request failed due to ' + status);
	          }
	        });
	      }
	 function showSteps(directionResult, markerArray, stepDisplay, map) {
	        // For each step, place a marker, and add the text to the marker's infowindow.
	        // Also attach the marker to an array so we can keep track of it and remove it
	        // when calculating new routes.
	        var myRoute = directionResult.routes[0].legs[0];
	        for (var i = 0; i < myRoute.steps.length; i++) {
	          var marker = markerArray[i] = markerArray[i] || new google.maps.Marker;
	          marker.setMap(map);
	          marker.setPosition(myRoute.steps[i].start_location);
	          attachInstructionText(
	              stepDisplay, marker, myRoute.steps[i].instructions, map);
	        }
	      }

     function attachInstructionText(stepDisplay, marker, text, map) {
       google.maps.event.addListener(marker, 'click', function() {
         stepDisplay.setContent(text);
         stepDisplay.open(map, marker);
       });
     }

</script>

<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/custom_siemens.css" rel="stylesheet">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body onload="loadScript();">
	<%
		EmployeeDto empDtoInHeader = null;
		try {

			String role = session.getAttribute("role").toString();
			System.out.println("BEFORE SEsion");
			empDtoInHeader = (EmployeeDto) session.getAttribute("userDto");
			System.out.println("AFTER SEsion");
			if (empDtoInHeader == null) {
				empDtoInHeader = new EmployeeService()
						.getEmployeeAccurate(session.getAttribute("user")
								.toString());
			}

		} catch (NullPointerException ne) {

			empDtoInHeader = new EmployeeService()
					.getEmployeeAccurate(session.getAttribute("user")
							.toString());
		}
		String employeeId = OtherFunctions.checkUser(session);

		long empid = Long.parseLong(employeeId);
		String reqlogtype="",reqroute="",reqtime="",reqcompr="";
		if(request.getParameter("type")!=null){
			reqlogtype=request.getParameter("type");
			reqroute=request.getParameter("route");
			reqtime=request.getParameter("time");
			reqcompr=request.getParameter("comp");
		}
	%>

	<div class="wrapper">
		<div class="header-wrap">
			<div class="container">
				<div class="row">
					<div class="col-sm-12 text-right">
						<img src="images/user_iocn_header.png" />&nbsp;Welcome
						<%=empDtoInHeader.getDisplayName()%>
						&nbsp;&nbsp;&nbsp;<a href="Logout"><img
							src="images/logout_icon_header.png" />&nbsp;Log Out</a>
					</div>
				</div>
			</div>
		</div>
		<div class="main-page-container">
			<div class="container">
				<form action="surveyStats.jsp" method="post" onsubmit="return validate();">
					<div class="row">
						<div class="col-sm-12">

							<div class="breadcrumb-wrap">
								<a href="employee_home.jsp"><img src="images/home.png" /></a> <a
									href="employee_home.jsp">My Information </a> <a href="#"
									class="current">Survey Stats</a>
									<a href="geoTagReport.jsp">Detailed Report</a>
							</div>
							<div class="row mar-top-20">
						<div class="col-sm-12">
							<div class="alert alert-danger san" hidden="hidden" style="color: red" ><p id="errortag"></p></div>
						</div>
					</div>
							<div class="row">
								<div
									class="col-md-2 col-sm-4 col-xs-6 mar-top-15 text-lightgrey">Shift
									Type:</div>
								<div class="col-md-2 col-sm-4 col-xs-6 mar-top-15">
									<select class="form-control" name="type" id="type"
										onchange="getRouteAndTime(0,0);">
											<% String ins="",outs="",nos="selected";
											 if(reqlogtype.equalsIgnoreCase("IN")){
											   	ins="selected";nos="";
											   }else if(reqlogtype.equalsIgnoreCase("OUT")){
											   	outs="selected";nos="";}
											   %>
										<option value="" <%=nos %>>SELECT</option>
										<option value="IN" <%=ins %>>IN</option>
										<option value="OUT" <%=outs %>>OUT</option>
									</select>
								</div>

								<div
									class="col-md-2 col-sm-4 col-xs-6 mar-top-15  text-lightgrey">Select
									Route:</div>
								<div class="col-md-2 col-sm-4 col-xs-6 mar-top-15">
									<select class="form-control" name="route" id="route">
									</select>
								</div>
								<div
									class="col-md-2 col-sm-4 col-xs-6 mar-top-15  text-lightgrey">Select
									Time:</div>
								<div class="col-md-2 col-sm-4 col-xs-6 mar-top-15">
									<select class="form-control" name="time" id="time">

									</select>
								</div>
								<div
									class="col-md-2 col-sm-4 col-xs-6 mar-top-15 text-lightgrey">Compare
									By:</div>
								<div class="col-md-2 col-sm-4 col-xs-6 mar-top-15">
									<select class="form-control" name="comp" id="comp">
									<% String home="",pd="",noc="selected";
											 if(reqcompr.equalsIgnoreCase("HOME")){
												 home="selected";noc="";
											   }else if(reqlogtype.equalsIgnoreCase("P/D")){
											   	pd="selected";noc="";}
											   %>
										<option value="" >SELECT</option>
										<option value="HOME" <%=home %>>HOME ADDRESS</option>
										<option value="P/D" <%=pd %>>PICK UP/DROP</option>
									</select>
								</div>
							</div>

							<div class="row text-right mar-btm-30">
								<div class="col-sm-12" align="right">
									<input type="submit" class="btn btn-blue save-btn"
										value="Show Status" />
								</div>
							</div>
								<%if(list !=null){ 
								double ratio=(empSameasNoodle.size()/(double)list.size())*100;%>
							<div class="row">
								<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Total Employees</div>
								<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=list.size() %></div>
						</div>
						<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Number of employee coded points match with nodal points</div>
								<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=empSameasNoodle.size() %></div>
								<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">(<%=ratio %>%)</div>						
						</div>
						<br/>
						<div class="row">
								<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Travel Distance</div>
								<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"> <500 M</div>
								<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">500 M - 1 KM</div>	
								<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">1 KM - 2 KM</div>
								<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"> >2 KM</div>						
						</div>
						<div class="row">
								<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Employee Count</div>
								<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"> <%=lessthanfivetm %></div>
								<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%= fivemtoonek%></div>	
								<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=onetotwo %></div>
								<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"> ><%= grthantwo%></div>						
						</div>
						<div class="row text-right mar-btm-30">
								<div class="col-sm-12" align="right">
									<a href="geoTagReport.jsp" class="btn btn-blue save-btn">Complete Report</a>
								</div>
							</div>
							<%} %>
							<div class="profile-google-map mar-top-30">
								<div class="row">
									<div class="col-sm-12">

										<div id="map" class="google-map" style="height: 480px;"></div>

									</div>
								</div>
							</div>
						
							<div class="footer-wrap">
								<div class="row">
									<div class="col-sm-12 text-center">
										<p class="text-12">
											The information stored on this website is maintained in
											accordance with the organization's Data Privacy Policy. </span><br />Copyright
											© 2016 siemens
									</div>
								</div>

							</div>


						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/bootstrap.min.js"></script>
	<!-- 	<script src="http://maps.googleapis.com/maps/api/js"></script>
	<script>
		function initialize() {
		  var mapProp = {
			center:new google.maps.LatLng(12.9760559,77.5922071),
			zoom:12,
			mapTypeId:google.maps.MapTypeId.ROADMAP
		  };
		  var map=new google.maps.Map(document.getElementById("profileMap"),mapProp);
		}
		google.maps.event.addDomListener(window, 'load', initialize);
	</script>
	  -->
</body>
</html>