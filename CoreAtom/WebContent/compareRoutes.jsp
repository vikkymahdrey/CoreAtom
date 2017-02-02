<%-- 
    Document   : compareRoute
    Created on : Mar 27, 2016, 6:58:21 PM
    Author     : sandesh
--%>

<%@page import="org.apache.poi.hslf.model.MasterSheet"%>
<%@page import="com.agiledge.atom.service.RouteService"%>
<%-- <%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%> --%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="java.util.ArrayList"%>
<%
	String logtype = request.getParameter("type");
	String ins="",outs="";
	String[] routeids=request.getParameterValues("selectedroute");
	ArrayList<ArrayList<RouteDto>> masterlist=null;
	if(logtype!=null){
		if(logtype.equalsIgnoreCase("IN")){
			ins="selected";
		}else{
			outs="selected";
		}
		masterlist=new RouteService().getAllRouteDetailsWithoutAPL(routeids);
	}
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" href="images/agile.png" type="image/x-icon" />
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>View Routes</title>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://www.nfl.com" prefix="disp"%>
<script type="text/javascript" src="js/jquery-latest.js"></script>

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
				<form action="compareRoutes.jsp" method="post"
					onsubmit="return validate();">
					<div class="row">
						<div class="col-sm-12">

							<div class="breadcrumb-wrap">
					<a href="employee_home.jsp"><img src="images/home.png" /></a>
					<a href="employee_home.jsp" >My Information </a>
					<%if(session.getAttribute("roleId").equals("2") || session.getAttribute("roleId").equals("4")){ %>
					<a href="create_route.jsp" >Create Route</a>
					<%}%>
<%if(session.getAttribute("roleId").equals("2") ){ %>
					<a href="surveyStats.jsp" >Survey Status</a>
					<a href="geoTagReport.jsp">GeoTag Report </a>
					<a href="ShuttleLocations.jsp">Locations </a>
					<a href="#" class="current">Compare Routes </a>
					<%} %>
				</div>
							<div class="row mar-top-20">
								<div class="col-sm-12">
									<div class="alert alert-danger san" hidden="hidden"
										style="color: red">
										<p id="errortag"></p>
									</div>
								</div>
							</div>
							<div class="row">
								<div
									class="col-md-1 col-sm-4 col-xs-6 mar-top-15 text-lightgrey">Log
									Type:</div>
								<div class="col-md-2 col-sm-4 col-xs-6 mar-top-15">
									<select class="form-control" name="type" id="type"
										onchange="getRouteAndTime(0,0);">

										<option value="">SELECT</option>
										<option value="IN" <%= ins%>>IN</option>
										<option value="OUT" <%= outs%>>OUT</option>
									</select>
								</div>

								<div
									class="col-md-1 col-sm-4 col-xs-6 mar-top-15  text-lightgrey">Select
									Route:</div>
								<div class="col-md-2 col-sm-4 col-xs-6 mar-top-15">
									<select class="form-control" name="route" id="route"
										multiple="multiple">
									</select>
								</div>
								<div
									class="col-md-1 col-sm-2 col-xs-6 mar-top-15  text-lightgrey"><div>
									<input type="button" class="formbutton" name="right"
										value="&rArr;" onclick="listMoveRight()" />
								</div>
								<div class="mar-top-5">
									<input type="button" class="formbutton" name="left"
										value="&lArr;" onclick="listMoveLeft()" />
								</div></div>
								<div class="col-md-2 col-sm-4 col-xs-6 mar-top-15">
									<select class="form-control" name="selectedroute" id="selectedroute"
										multiple="multiple">
									</select>
								</div>
								<div class="col-md-2 col-sm-4 col-xs-6 mar-top-15">
									<input type="submit" class="btn btn-blue save-btn"
										value="Show Routes" />
								</div>
								<div
									class="col-md-2 col-sm-4 col-xs-6 mar-top-15  text-lightgrey"></div>
							</div>
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
	'
	<script type="text/javascript" src="js/jquery-latest.js"></script>
	<script>
		var map;
		var marker;
		var markers;
		var count = 0;
		var cityLat;
		var cityLon;
		var emplat = 12.8452347780636;
		var emplong = 77.66308307647705;
		var directionsDisplay;
		var directionsService;
		var waypts = [];
		var start = [];
		var end = [];
		var pts = [];
		var routes = [];
		var stepDisplay;
		var markerArray = [];
		function getRouteAndTime(previoustime, previousroute) {
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
						document.getElementById("route").innerHTML = "<select name='route' id='route' multiple='multiple'>"
								+ result + "</select>";
					}
				}
				xmlhttp.open("POST", "GetRoutesWithNoSite?logtype="
						+ type + "&previoustime=" + previoustime
						+ "&previousroute=" + previousroute, true);
				xmlhttp.send();
			} catch (e) {

				alert(e);
			}
		}
		function validate() {
			var shift = document.getElementById("type").value;
			var selectedroute = document.getElementById("selectedroute");
			var selectedrouteLength = document.getElementById("selectedroute").options.length;
			for ( var i = 0; i < selectedrouteLength; i++) {
				
				selectedroute.options[i].selected = true;
			}
			if (shift == "") {
				document.getElementById("errortag").innerHTML = "Please select log type";
				$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
				$('.san').show();
				document.getElementById("type").focus();
				return false;
			} else if(selectedrouteLength <= 0){
				document.getElementById("errortag").innerHTML ="Please select route name";
				$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
				$('#route').removeClass( "form-control" ).addClass( "validation-required" );
				$('.san').show();
				document.getElementById("route").focus();
				return false;
			}else {
				return true;
			}
		}
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
	<%if(logtype!=null){

			for(ArrayList<RouteDto> list : masterlist){
				if(list.size()>0){
				for(RouteDto dto: list){%>
		var myLatLng12 = new google.maps.LatLng(
	<%=dto.getLattitude()%>
		,
	<%=dto.getLongitude()%>
		);
			placeMarker(myLatLng12,'<%=dto.getLandmark()%>');
	<%}%>
		var firstlatlng = new google.maps.LatLng(
	<%=list.get(0).getLattitude()%>
		,
	<%=list.get(0).getLongitude()%>
		);
			var lastlatlng = new google.maps.LatLng(
	<%=list.get(list.size()-1).getLattitude()%>
		,
	<%=list.get(list.size()-1).getLongitude()%>
		);

			start.push(firstlatlng);
			end.push(lastlatlng);
			pts.push(waypts);
			requestDirections(firstlatlng, lastlatlng, waypts);
			waypts = [];
	<%}
			}%>
		
	<%}%>
		}
		function placeMarker(position1, name) {
			waypts.push({
				location : position1,
				stopover : true,
			});
			marker = new google.maps.Marker({
				position : position1,
				title : name,
				map : map,
				//animation : google.maps.Animation.BOUNCE,
				icon : 'images/icon_2.png'
			});
		}
		function DisplayRoute(directionsService, directionsDisplay) {

			/* 	for(var l=0;l<start.length;l++){
			 directionsService.route({
			 origin: start[l],  // Haight.
			 destination: end[l], 
			 waypoints: pts[l],
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
			 directionsDisplay.setDirections(m);
			 directionsDisplay.setDirections(n); */

		}
		function renderDirections(result) {
			var directionsRenderer = new google.maps.DirectionsRenderer;
			directionsRenderer.setMap(map);
			directionsRenderer.setDirections(result);
		}
		function requestDirections(firstlatlng, lastlatlng, waypts) {
			directionsService.route({
				origin : firstlatlng,
				destination : lastlatlng,
				waypoints : waypts,
				optimizeWaypoints : true,
				travelMode : google.maps.DirectionsTravelMode.DRIVING
			}, function(result) {
				renderDirections(result);
			});
		}
		function showSteps(directionResult, markerArray, stepDisplay, map) {
			// For each step, place a marker, and add the text to the marker's infowindow.
			// Also attach the marker to an array so we can keep track of it and remove it
			// when calculating new routes.
			var myRoute = directionResult.routes[0].legs[0];
			for (var i = 0; i < myRoute.steps.length; i++) {
				var marker = markerArray[i] = markerArray[i]
						|| new google.maps.Marker;
				marker.setMap(map);
				marker.setPosition(myRoute.steps[i].start_location);
				attachInstructionText(stepDisplay, marker,
						myRoute.steps[i].instructions, map);
			}
		}

		function attachInstructionText(stepDisplay, marker, text, map) {
			google.maps.event.addListener(marker, 'click', function() {
				stepDisplay.setContent(text);
				stepDisplay.open(map, marker);
			});
		}
		function listMoveRight() {
			var trip = document.getElementById("route");
			var selectedtrip = document.getElementById("selectedroute");
			var totLen=trip.options.length;
			for(var count=0; count < totLen; count++) {
				
				if(trip.options[count].selected == true) {
			var optionNew = document.createElement('option');
			optionNew.value = trip.options[count].value;
			optionNew.text = trip.options[count].innerHTML;
			try {
				selectedtrip.add(optionNew, null);
			} catch (e) {
				selectedtrip.add(optionNew);
			}
			trip.options[count] = null;
			count--;
				}
			}
		}
	 	function listMoveLeft() {
			var selectedtrip = document.getElementById("selectedroute");
			var trip = document.getElementById("route");
			var optionSelected = selectedtrip.selectedIndex;
			var optionNew = document.createElement('option');
			optionNew.value = selectedtrip.options[optionSelected].value;
			optionNew.text = selectedtrip.options[optionSelected].innerHTML;
			try {
				trip.add(optionNew, null);
			} catch (e) {
				trip.add(optionNew);
			}
			selectedtrip.options[optionSelected] = null;
		} 
	</script>
</body>
</html>