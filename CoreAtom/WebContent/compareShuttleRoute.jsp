
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.service.RouteService"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<% 
OtherDao ob = OtherDao.getInstance();
String site = "1";
String route[]=request.getParameterValues("route");
try {
	site = session.getAttribute("site").toString();
} catch (Exception ignor) {
}
String[] city = ob.getCity(site);
String[] routeids=request.getParameterValues("route");
ArrayList<ArrayList<RouteDto>> masterlist=null;
	if(routeids!=null && routeids.length>0){
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
<%@include file="Header.jsp"%>
	<%
		

			String role = session.getAttribute("role").toString();
			System.out.println("BEFORE SEsion");

		String employeeId = OtherFunctions.checkUser(session);
		ArrayList<RouteDto> list1 =new RouteService().getShuttleRoutes();
	%>

	<div class="wrapper">
	
		<div class="main-page-container">
			<div class="container">
				<form action="compareShuttleRoute.jsp" method="post"
					onsubmit="return validate();">
					<div class="row">
						<div class="col-sm-12">
							
							<div class="row mar-top-20">
								<div class="col-sm-12">
									<div class="alert alert-danger san" hidden="hidden"
										style="color: red">
										<p id="errortag"></p>
									</div>
								</div>
							</div>
							<div>
								<div
									class="col-md-1 col-sm-4 col-xs-6 mar-top-15  text-lightgrey">Select
									Route:</div>
								<div class="col-md-2 col-sm-4 col-xs-6 mar-top-15">
									<select class="form-control" name="route" id="route"
										multiple="multiple">
										<%
											for(RouteDto dto : list1){
												%>
										<option value="<%=dto.getRouteId() %>"><%=dto.getRouteName() %></option>
										<%}
										%>
									</select>
								</div>
								<div class="col-md-2 col-sm-4 col-xs-6 mar-top-15">
									<input type="submit" class="btn btn-blue save-btn"
										value="Show Routes" />
								</div>
								<div id="showroutename"></div>
								<div class="profile-google-map mar-top-30">
									<div class="row">
										<div class="col-sm-12">
											<input type="hidden" value="<%=city[0]%>" id="cityLat">
											<input type="hidden" value="<%=city[1]%>" id="cityLon">
											<input type="button" id="open" name="open"
												value="Full Screen" class="btn btn-blue save-btn"
												onclick="gofullScreen();">
											<div id="map" class="google-map" style="height: 880px;"></div>

										</div>
									</div>
								</div>
								<div class="footer-wrap">
									<div class="row">
										<div class="col-sm-12 text-center">
											<p class="text-12">
												The information stored on this website is maintained in
												accordance with the organization's Data Privacy Policy. <br />Copyright
												© 2016 siemens
										</div>
									</div>

								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
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
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script>
	var map;
	var marker;
	var markers;
	var count = 0;
	var cityLat;
	var cityLon;
	var emplat = 12.6810066; //tokai
	var emplong = 77.4353909; //tokai
	var directionsDisplay;
	var directionsService;
	var waypts = [];
	var start = [];
	var end = [];
	var pts = [];
	var routes = [];
	var stepDisplay;
	var markerArray = [];
	var colors=["red","green","blue","orange","black"];
	var clrcount=0;
	var routecount=1;
	function loadScript() {
		if( document.getElementById("cityLat").value!=null){
			cityLat = document.getElementById("cityLat").value;
			cityLon = document.getElementById("cityLon").value;
		}else{
		cityLat = emplat;
		cityLon = emplong;
		}
		try {
			var script = document.createElement("script");
			script.type = "text/javascript";
			script.src = "https://maps.googleapis.com/maps/api/js?sensor=true&callback=initialize&libraries=places&client=gme-leptonsoftwareexport4&signature=xghu9DIoNr63z8_al_oJCSPWQh0=";
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
		var bottoninmap=document.getElementById("open");
		map.controls[google.maps.ControlPosition.TOP_RIGHT].push(bottoninmap);
		directionsDisplay = new google.maps.DirectionsRenderer;
		directionsService = new google.maps.DirectionsService;
<%if(masterlist!=null){

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
routecount=routecount+1;
	var firstlatlng = new google.maps.LatLng(
<%=list.get(0).getLattitude()%>
	,
<%=list.get(0).getLongitude()%>
	);
		var lastlatlng = new google.maps.LatLng(
<%=list.get(1).getLattitude()%>
	,
<%=list.get(1).getLongitude()%>
	);

		start.push(firstlatlng);
		end.push(lastlatlng);
		pts.push(waypts);
		requestDirections(firstlatlng, lastlatlng, waypts);
		waypts = [];
<%}
		}%>
	
<%}%>
showLandmarks();
showroutename();
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
			label:''+routecount 

			//animation : google.maps.Animation.BOUNCE,
			//icon : 'images/icon_2.png'
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
		var directionsRenderer = new google.maps.DirectionsRenderer({
		    polylineOptions: {
		        strokeColor: colors[clrcount]
		      }
		    });
		if(clrcount<5){
		clrcount=clrcount+1;
		}
		else{
			clrcount=0;
		}
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
	function showLandmarks() {
		var param="Route";
		/* var route=document.getElementById("route").value;
		if(route!=""){
			param=route;
		} */
		xmlHttp = GetXmlHttpObject();
		url = "GetShuttleLandmarks?search="+param;
		xmlHttp.onreadystatechange = displayExistingLandmarks;
		xmlHttp.open("POST", url, true);
		xmlHttp.send();
	}
	function displayExistingLandmarks() {
		if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete") {

			var fullnodes = xmlHttp.responseText;
			var nodes = fullnodes.split("$");
			noofnodes = nodes.length;
			var ltln;
			for (var i = 1; i < noofnodes; i++) {
				ltln = nodes[i].split(":");

				var latlng = new google.maps.LatLng(ltln[1], ltln[2]);
				markEmployee(latlng);
				var message = ltln[0] + "<br>" + ltln[3];

				displayMarkers(marker, latlng, message);
			}

		}
	}

	function displayMarkers(marker, latlng, message) {

		var infowindow = new google.maps.InfoWindow({
			content : message,
			size : new google.maps.Size(50, 50)
		});
		google.maps.event.addListener(marker, 'click', function() {
			infowindow.open(map, marker);
			
		});
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
	function markEmployee(location) {

		marker = new google.maps.Marker({
			position : location,
			map : map,
			icon : 'images/tool.png'
		});
	}
	function showroutename(){
		var cnt=1;
		var msgtable="<table width='80%' ><thead><th> Sl No </th><th> Route Name </th><th> Shift </th><th> Vehicle </th><th> Capacity </th><th> EmployeeCount </th><th> Route Log </th><th> Route Time </th><th> </th></thead>";
		<%for(RouteDto dto : list1){
			String cap="";
			if(dto.getVehicleType().equalsIgnoreCase("Indica")){
				cap="4";
			}else if(dto.getVehicleType().equalsIgnoreCase("Sumo")){ cap="7"; }
			else if(dto.getVehicleType().equalsIgnoreCase("Tempo")){ cap="13"; }
			else if(dto.getVehicleType().equalsIgnoreCase("Mazda")){ cap="18"; }
			else if(dto.getVehicleType().equalsIgnoreCase("Mini Bus")){ cap="30"; }
			else if(dto.getVehicleType().equalsIgnoreCase("Bus")){ cap="50"; }
			else if(dto.getVehicleType().equalsIgnoreCase("Bus1")){ cap="80"; }
		%>
		msgtable+="<tr><td>"+cnt+"</td><td><%=dto.getRouteName()%></td><td><%=dto.getInOut()%></td><td><%=dto.getVehicleType()%></td><td><%=cap%></td><td><%=dto.getVehicleCount()%></td><td><%=dto.getInOut()%></td><td><%=dto.getTime()%></td><td><a href='updateShuttleRoute.jsp?routeid=<%=dto.getRouteId()%>'>Edit</a></td></tr>";
		cnt++;
	<%}%>
		msgtable+="</table>";
		var sumShow = document.getElementById("showroutename");
		sumShow.innerHTML = msgtable;
	}
	function validate(){
		
	}
	function gofullScreen(){
		var suburl="";
		<%if(routeids!=null){
				for(int i=0; i<routeids.length;i++){%>
					suburl+="&route=<%=routeids[i]%>";
					<%}
			}%>
	window.open("map.jsp?site=1" + suburl);
	}
</script>
</html>