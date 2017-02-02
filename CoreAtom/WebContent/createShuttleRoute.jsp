<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@page import="com.agiledge.atom.dto.EmployeeSubscriptionDto"%>
<%@page import="com.agiledge.atom.service.RouteService"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" href="images/agile.png" type="image/x-icon" />
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>Create Routes</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>


<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/custom_siemens.css" rel="stylesheet">
<link href="css/toastr.min.css" rel="stylesheet" />

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
		OtherDao ob = OtherDao.getInstance();
		String message = "";
		String site = "1";
			
			try {
				site = session.getAttribute("site").toString();
			} catch (Exception ignor) {
			}
			String[] city = ob.getCity(site);
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
			ArrayList<RouteDto> list1 =new RouteService().getShuttleRoutes();
			String type=null;
			String time=null;
			String intype="",outtype="";
			if(request.getParameter("type")!=null){
		type=request.getParameter("type");
		time=request.getParameter("time");
		if(type.equalsIgnoreCase("out")){
			intype="";
			outtype="selected";
		}else if(type.equalsIgnoreCase("IN")){
			intype="selected";
			outtype="";
		}
			}
	%>

	<div class="wrapper">


		<div class="main-page-container">
			<div class="container">

				<div class="row">
					<form action="createShuttleRoute.jsp" method="post"
						onsubmit="return validate1();">
						<div class="col-sm-12">

							<div class="content-wrap">

								<%
									if (session.getAttribute("status") != null) {
								%>
								<div class="row mar-top-40">
									<div class="col-sm-12">
										<div class="alert alert-success"><%=session.getAttribute("status")%></div>
									</div>
								</div>
								<%
									}
								%>
								<div class="row">
									<div class="col-sm-8 page-heading mar-top-20">
										<i class="page-heading-icon"><img
											src="images/user_icon.png" /></i>
										<h5 class="text-blue text-semi-bold">Create Route</h5>
									</div>

								</div>
								<div class="row mar-top-20">
									<div class="col-sm-12">
										<div class="alert alert-danger san" hidden="hidden"
											style="color: red">
											<p id="errortag"></p>
										</div>
									</div>
								</div>
								<div class="push">
									<div class="row">
										<div
											class="col-md-2 col-sm-2 col-xs-6 mar-top-15 text-lightgrey">Log
											Type</div>
										<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15">
											<select id="type" name="type" onchange="getTime();"
												class="form-control" style="width: 50%;">
												<option value="ALL">SELECT</option>
												<option value="IN" <%=intype%>>IN</option>
												<option value="OUT" <%=outtype%>>OUT</option>
											</select>
										</div>

										<div
											class="col-md-2 col-sm-2 col-xs-6 mar-top-15  text-lightgrey">Log
											Time</div>
										<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">
											<select id="time" name="time" class="form-control"
												style="width: 100%;">
												<option value="All">SELECT</option>

											</select>
										</div>
										<div class="col-md-2 col-sm-4 col-xs-6 mar-top-15 ">
											<input type="submit" class="btn btn-blue save-btn"
												value="Submit" />
										</div>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<%
					if(type!=null){
				%>
				<div class="row">
					<form action="AddRouteInMap1" method="post"
						onsubmit="return validate();">
						<div class="col-sm-12">
							<div class="content-wrap">

								<input type="hidden" id="time1" name="time1" value="<%=time%>" />
								<input type="hidden" id="type" name="type" value="<%=type%>" />
								<select name="cmpnydist" id="cmpnydist" multiple="multiple"
									style="display: none;"></select> <select name="noodledist"
									id="noodledist" multiple="multiple" style="display: none;"></select>

								<select name="empid" id="empid" multiple="multiple"
									style="display: none;"></select> <select name="points"
									multiple="multiple" id="points" style="display: none;"><option
										value="<%=city[0]%>*<%=city[1]%>">(<%=city[0]%>,
										<%=city[1]%>)
									</option>
								</select>
								<div class="push">
									<div class="row">
										<div
											class="col-md-2 col-sm-2 col-xs-3 mar-top-15 text-lightgrey mandatory">Compare
											Distance</div>
										<div class="col-md-8 col-sm-2 col-xs-3 mar-top-15">

											<input type="checkbox" name="showdist" id="showdist"
												value="0.500" onclick="getEmployee1();" checked="checked">
											< 500 meter &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
												type="checkbox" name="showdist1" id="showdist1" value="1"
												onclick="getEmployee1();"> 500 meter to 1 KM
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox"
												name="showdist2" id="showdist2" value="2"
												onclick="getEmployee1();"> 1 KM to 2 KM
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox"
												name="showdist3" id="showdist3" value="5"
												onclick="getEmployee1();"> 2 KM to 5 KM
										</div>

									</div>
									<div class="row">


										<div
											class="col-md-2 col-sm-2 col-xs-6 mar-top-15  text-lightgrey">Include
											Employees from</div>
										<div class="col-md-2 col-sm-2 col-xs-6 mar-top-15">
											<select class="form-control" name="iroute" id="iroute"
												multiple="multiple">
												<%
													for(RouteDto dto : list1){
												%>
												<option value="<%=dto.getRouteId()%>"><%=dto.getRouteName()%></option>
												<%
													}
												%>
											</select>
										</div>
										<div
											class="col-md-1 col-sm-2 col-xs-6 mar-top-15  text-lightgrey">
											<div>
												<input type="button" class="formbutton" name="right"
													value="&rArr;" onclick="listMoveRight()" />
											</div>
											<div class="mar-top-5">
												<input type="button" class="formbutton" name="left"
													value="&lArr;" onclick="listMoveLeft()" />
											</div>
										</div>
										<div class="col-md-2 col-sm-4 col-xs-6 mar-top-15">
											<select class="form-control" name="selectedroute"
												id="selectedroute" multiple="multiple"><option
													value="0">With no Route</option>
											</select>
										</div>
									</div>
								</div>
								<div class="row">
									<div
										class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Travel
										Distance</div>
									<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><500 M</div>
									<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">500 M
										- 1 KM</div>
									<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">1 KM -
										2 KM</div>
									<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">>2 KM</div>
									<!-- <div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">>3 KM</div> -->
								</div>
								<div class="row">
									<div
										class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Employee
										Count</div>
									<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">
										<input type="text" class="form-control" id="fvmtr"
											readonly="readonly" onclick="popupemp('fvmtr');">
									</div>
									<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">
										<input type="text" class="form-control" id="fvmt1"
											readonly="readonly" onclick="popupemp('fvmt1');">
									</div>
									<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">
										<input type="text" class="form-control" id="1to2"
											readonly="readonly" onclick="popupemp('1to2');">
									</div>
									<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">
										<input type="text" class="form-control" id="2"
											readonly="readonly" onclick="popupemp('2');">
									</div>
									<!-- <div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">
										<input type="text" class="form-control" id="3"
											readonly="readonly" onclick="popupemp('3');">
									</div> -->
								</div>
								<div id="showemp"></div>
								<div class="row">
									<div
										class="col-md-2 col-sm-2 col-xs-3 mar-top-15 text-lightgrey mandatory">Vehicle
										Type</div>
									<div class="col-md-2 col-sm-2 col-xs-3 mar-top-15">
										<select name="vt" id="vt" class="form-control"
											onchange="checkcapacity();">
											<option value="0">Select</option>
											<option value="4">Indica (4)</option>
											<option value="7">Sumo (7)</option>
											<option value="13">Tempo (13)</option>
											<option value="18">Mazda (18)</option>
											<option value="30">Mini Bus (30)</option>
											<option value="50">Bus (50)</option>
											<option value="80">Bus (80)</option>
										</select>
									</div>
								</div>

								<div class="push">

									<div class="row">
										<div
											class="col-md-2 col-sm-2 col-xs-3 mar-top-15 text-lightgrey mandatory">Route
											Name</div>
										<div class="col-md-2 col-sm-2 col-xs-3 mar-top-15">
											<input type="text" name="routename" id="routename" value=""
												class="form-control" />
										</div>

										<div class="col-md-2 col-sm-4 col-xs-6 mar-top-15 ">
											<input type="submit" class="btn btn-blue save-btn"
												value="Save" />
										</div>
									</div>
								</div>
								<div class="profile-google-map mar-top-30">
									<div class="row">
										<div class="col-sm-12">
											<input type="hidden" value="<%=city[0]%>" id="cityLat">
											<input type="hidden" value="<%=city[1]%>" id="cityLon">
											<input type="button" id="removepoint" name="removepoint"
												value="Remove Points" class="btn btn-blue save-btn"
												onclick="removepoints();"> <input id="pac-input"
												class="controls" type="text" placeholder="Search Box">
											<div id="map" class="google-map" style="height: 880px;"></div>
										</div>
									</div>
								</div>


							</div>

							<div class="footer-wrap">
								<div class="row">
									<div class="col-sm-12 text-center">
										<p class="text-12">
											The information stored on this website is maintained in
											accordance with the organization's Data Privacy Policy. <br />Copyright
											� 2016 siemens
									</div>
								</div>

							</div>


						</div>
					</form>
				</div>
				<%
					}
				%>
			</div>
		</div>
		<div class="modal fade" tabindex="-1" role="dialog"
			id="validationAlertModal">
			<div class="modal-dialog modal-md">
				<div class="modal-content">

					<div class="modal-body">
						<p class="alert alert-warning" id="alertwarn">
							<img src="images/alert_icon.png" />
						</p>
					</div>
					<div class="modal-footer text-center">
						<button type="button" class="btn btn-blue" data-dismiss="modal">OK</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<!-- /.modal -->

		<div class="modal fade" tabindex="-1" role="dialog"
			id="validationAlertModal1">
			<div class="modal-dialog modal-md">
				<div class="modal-content">

					<div class="modal-body">
						<p class="alert alert-warning" id="alertwarn1">
							<img src="images/alert_icon.png" />
						</p>
					</div>
					<div class="modal-footer text-center">
						<button type="button" class="btn btn-blue" data-dismiss="modal" onclick="removepoint1();">Remove</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
	</div>

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/bootstrap.min.js"></script>
	<script src="js/toastr.min.js"></script>

</body>
<script>
	var total = 0;
	var count = 0;
	var cityLat;
	var cityLon;
	var emplat = 12.6810066; //tokai
	var emplong = 77.4353909; //tokai
	var place;
	var directionsDisplay;
	var directionsService;
	var dest;
	var start;
	var waypts = [];
	var map;
	var marker;
	var marker1;
	var emplist = [];
	var compdist = [];
	var needledist = [];
	var details;
	var noofnodes;
	var input;
	var removedlist = [];
	var cnt = 0;
	var buttoninmap;
	var nodpointslat = [];
	var nodpointslng = [];
	var addExtEmp = [];
	var fullnodes;
	var extemp = "";
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
			zoom : 12,
			center : myLatlng,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		};
		start = myLatlng;
		map = new google.maps.Map(document.getElementById("map"), myOptions);
		directionsDisplay = new google.maps.DirectionsRenderer;
		directionsService = new google.maps.DirectionsService;
		google.maps.event.addListener(map, 'click', function(event) {
			placeMarker(event.latLng);
			//toastr.info('Clicked on map!...Please wait...');

		});
		input = document.getElementById('pac-input');
		buttoninmap = document.getElementById("removepoint");
		var searchBox = new google.maps.places.SearchBox(input);
		map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
		map.controls[google.maps.ControlPosition.TOP_RIGHT].push(buttoninmap);
		map.addListener('bounds_changed', function() {
			searchBox.setBounds(map.getBounds());
		});
		markers = [];
		searchBox.addListener('places_changed', function() {
			var places = searchBox.getPlaces();

			if (places.length == 0) {
				return;
			}

			// Clear out the old markers.
			markers.forEach(function(marker1) {
				marker1.setMap(null);
				marker.setMap(null);
			});
			markers = [];

			// For each place, get the icon, name and location.
			var bounds = new google.maps.LatLngBounds();
			places.forEach(function(place) {
				marker.setMap(null);
				var icon = {
					url : place.icon,
					size : new google.maps.Size(71, 71),
					origin : new google.maps.Point(0, 0),
					anchor : new google.maps.Point(17, 34),
					scaledSize : new google.maps.Size(50, 50)
				};

				finalvalue = place.geometry.location;
				if (place.geometry.viewport) {
					// Only geocodes have viewport.
					bounds.union(place.geometry.viewport);
				} else {
					bounds.extend(place.geometry.location);
				}
			});
			map.fitBounds(bounds);
		});
		// [END region_getplaces]
		showLandmarks();
	}
	function showLandmarks() {
		var logtype=document.getElementById("type").value;
		var logtime=document.getElementById("time1").value;
		var param1 = "shuttle";
		var param2="&route=0";
		var route = document.getElementById("iroute").value;
		if (route != "") {
			param2+="&route="+ route;
		}
		xmlHttp = GetXmlHttpObject();
		url = "GetShuttleLandmarks?logtype="+logtype+"&time="+logtime+"&search=" + param1+param2;
		xmlHttp.onreadystatechange = displayExistingLandmarks;
		xmlHttp.open("POST", url, true);
		xmlHttp.send();
	}
	function displayExistingLandmarks() {
		if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete") {

			fullnodes = xmlHttp.responseText;
			var nodes = fullnodes.split("$");
			noofnodes = nodes.length;
			var ltln;
			for (var i = 1; i < noofnodes; i++) {
				ltln = nodes[i].split(":");

				var latlng = new google.maps.LatLng(ltln[1], ltln[2]);
				markEmployee(latlng);
				var message = ltln[0] + "<br>" + ltln[3];
				var id = ltln[4];
				var name = ltln[0];
				displayMarkers(marker, latlng, message, id);
			}

		}
	}

	function displayMarkers(marker, latlng, message, id) {
		message = "<table><tr><td>"
				+ message
				+ "<td></tr><tr><td><input type='button' value='Add' class='btn btn-blue save-btn' style='text-align: center;' onclick='addEmpManual("
				+ id + ");'></td></tr></table>";
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
	function placeMarker(location) {

		var x = document.getElementById("points");
		var option = document.createElement("option");
		var lat = location.lat();
		var lng = location.lng();
		option.value = lat + "*" + lng;
		option.text = location;
		x.add(option);

		if (dest == null) {
			dest = location;
			getEmployee();
			DisplayRoute(directionsService, directionsDisplay, start, dest);
		} else {
			nodpointslat.push(location.lat());
			nodpointslng.push(location.lng());
			waypts.push({
				location : location,
				stopover : true
			});
			getEmployee();
			DisplayRoute(directionsService, directionsDisplay, start, dest);
		}

		directionsDisplay.setMap(map);

	}
	function DisplayRoute(directionsService, directionsDisplay, start, end) {
		//waypts.push({location:end,stopover:true});
		directionsService.route({
			origin : start, // Haight.
			destination : end,
			waypoints : waypts,
			optimizeWaypoints : true,// Ocean Beach.
			// Note that Javascript allows us to access the constant
			// using square brackets and a string value as its
			// "property."
			travelMode : google.maps.TravelMode.DRIVING
		}, function(response, status) {
			if (status == google.maps.DirectionsStatus.OK) {
				directionsDisplay.setDirections(response);
				/* alert(response.routes[0].legs[0]); */
				/* alert(json[0].lat); */
			} else {
				window.alert('Directions request failed due to ' + status);
			}
		});
	}
	function getEmployee() {
		toastr.info('Please wait...');
		var logtype=document.getElementById("type").value;
		var logtime=document.getElementById("time1").value;
		var elements = document.getElementById("points").options;
		var selectedroute = document.getElementById("selectedroute");
		var routeconst = "";
		for (var k = 0; k < selectedroute.options.length; k++) {
			routeconst += "&route=" + selectedroute.options[k].value;
		}
		var compareconst = 0;//document.getElementById("dist").value;
		var data = "";
		for (var m = 0; m < elements.length; m++) {
			elements[m].selected = true;
			data = data + "&points=" + elements[m].value;
		}
		try {
			var xmlhttp;
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					details = extemp + xmlhttp.responseText;
					var sumShow = document.getElementById("showemp");
					var fmtr = 0, fmtr1 = 0, oneto2 = 0, twmtr = 0;
					emplist = [];
					compdist = [];
					needledist = [];
					if (addExtEmp.length > 0) {
						for (var mk = 0; mk < addExtEmp.length; mk++) {
							emplist.push(addExtEmp[mk]);
						}
					}
					var dataString = "<table width='80%' ><thead><th>ID</th><th>Employee Name</th><th> No </th><th> Distance</th><th> Distance</th><th> </th></thead>";
					var splited = details.split("/");
					for (var i = 1; i < splited.length; i++) {
						var datadet = splited[i].split("#");
						if (datadet[3] == "NA") {
							dataString += "<tr><td>"
									+ datadet[0]
									+ "</td><td>"
									+ datadet[1]
									+ "</td><td>"
									+ datadet[2]
									+ "</td><td>"
									+ datadet[3]
									+ "</td><td>"
									+ datadet[4]
									+ "</td><td><input type='checkbox' name='rmuser' id='"+datadet[0]+"' value='"+datadet[0]+"'></td></tr>";
						} else if (document.getElementById("showdist").checked == true) {

							if (datadet[3] <= 0.5) {
								emplist.push(datadet[0]);
								compdist.push(datadet[4]);
								needledist.push(datadet[3]);
								dataString += "<tr><td>"
										+ datadet[0]
										+ "</td><td>"
										+ datadet[1]
										+ "</td><td>"
										+ datadet[2]
										+ "</td><td>"
										+ datadet[3]
										+ "</td><td>"
										+ datadet[4]
										+ "</td><td><input type='checkbox' name='rmuser' id='"+datadet[0]+"' value='"+datadet[0]+"'></td></tr>";
							}
						}
						if (document.getElementById("showdist1").checked == true) {

							if (datadet[3] > 0.500) {
								if (datadet[3] <= 1) {

									emplist.push(datadet[0]);
									compdist.push(datadet[4]);
									needledist.push(datadet[3]);
									dataString += "<tr><td>"
											+ datadet[0]
											+ "</td><td>"
											+ datadet[1]
											+ "</td><td>"
											+ datadet[2]
											+ "</td><td>"
											+ datadet[3]
											+ "</td><td>"
											+ datadet[4]
											+ "</td><td><input type='checkbox' name='rmuser' id='"+datadet[0]+"' value='"+datadet[0]+"'></td></tr>";

								}
							}
						}
						if (document.getElementById("showdist2").checked == true) {

							if (datadet[3] > 1) {
								if (datadet[3] <= 2) {
									emplist.push(datadet[0]);
									compdist.push(datadet[4]);
									needledist.push(datadet[3]);
									dataString += "<tr><td>"
											+ datadet[0]
											+ "</td><td>"
											+ datadet[1]
											+ "</td><td>"
											+ datadet[2]
											+ "</td><td>"
											+ datadet[3]
											+ "</td><td>"
											+ datadet[4]
											+ "</td><td><input type='checkbox' name='rmuser' id='"+datadet[0]+"' value='"+datadet[0]+"'></td></tr>";

								}
							}
						}
						if (document.getElementById("showdist3").checked == true) {

							if (datadet[3] > 2) {
								if (datadet[3] <= 5) {
									emplist.push(datadet[0]);
									compdist.push(datadet[4]);
									needledist.push(datadet[3]);
									dataString += "<tr><td>"
											+ datadet[0]
											+ "</td><td>"
											+ datadet[1]
											+ "</td><td>"
											+ datadet[2]
											+ "</td><td>"
											+ datadet[3]
											+ "</td><td>"
											+ datadet[4]
											+ "</td><td><input type='checkbox' name='rmuser' id='"+datadet[0]+"' value='"+datadet[0]+"'></td></tr>";

								}
							}
						}
						if (datadet[3] <= 0.5) {
							fmtr++;
						} else if (datadet[3] <= 1) {
							fmtr1++;
						} else if (datadet[3] <= 2) {
							oneto2++;
						} else {
							twmtr++;
						}
					}
					document.getElementById("fvmtr").value = fmtr;
					document.getElementById("fvmt1").value = fmtr1;
					document.getElementById("1to2").value = oneto2;
					document.getElementById("2").value = twmtr;
					dataString += "<tr><td></td><td></td><td></td><td></td><td></td><td><input type='button' value='removeuser' onclick='removeuser()'></td></tr></table>";
					dataString = "<Strong>Total Employee</Strong> : "
							+ noofnodes
							+ "  <Strong> &nbsp; Employee In Route</Strong> :"
							+ emplist.length + " <br/>" + dataString;
					total = emplist.length;
					sumShow.innerHTML = dataString;
					showRouteEmpl(details);
				}
			}
			xmlhttp.open("POST", "GetEmployeeNearNoodle?type=shuttle&logtype="+logtype+"&logtime="+logtime+"&dist=" + compareconst
					+ routeconst + data, true);
			xmlhttp.send();
		} catch (e) {

			alert(e);
		}

	}
	function listMoveRight() {
		var trip = document.getElementById("iroute");
		var selectedtrip = document.getElementById("selectedroute");
		var totLen = trip.options.length;
		for (var count = 0; count < totLen; count++) {

			if (trip.options[count].selected == true) {
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
		var trip = document.getElementById("iroute");
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
	function checkcapacity() {
		var cap = document.getElementById("vt").value;
		var sumShow = document.getElementById("showemp");
		var temp = [];
		var temp1=[];
		var temp2=[];
		if (cap < emplist.length) {
			if (confirm("Vehicle capacity is less than employee count\n Do you want to remove some employees from list?") == true) {
				toastr.info('Please wait...Removing some employees');
				document.getElementById("showemp").innerHTML = "";
				var dataString = "<table width='80%' ><thead><th>ID</th><th>Employee Name</th><th> No </th><th> Distance</th><th> Distance</th><th> </th></thead>";
				var splited = details.split("/");
				for (var i = 1; i <= cap; i++) {
					var datadet = splited[i].split("#");
					temp.push(datadet[0]);
					temp1.push(datadet[4]);
					temp2.push(datadet[3]);
					dataString += "<tr><td>"
							+ datadet[0]
							+ "</td><td>"
							+ datadet[1]
							+ "</td><td>"
							+ datadet[2]
							+ "</td><td>"
							+ datadet[3]
							+ "</td><td>"
							+ datadet[4]
							+ "</td><td><input type='checkbox' name='rmuser' id='"+datadet[0]+"' value='"+datadet[0]+"'></td></tr>";
				}
				emplist = temp;
				compdist = temp1;
				needledist= temp2;
				dataString += "<tr><td></td><td></td><td></td><td></td><td></td><td><input type='button' value='removeuser' onclick='removeuser()'></td></tr></table>";
				dataString = "<Strong>Total Employee</Strong> : " + noofnodes
						+ "  <Strong> &nbsp; Employee In Route</Strong> :"
						+ emplist.length + " <br/>" + dataString;
				sumShow.innerHTML = dataString;
				alert("Some Employees removed from route");
			} else {

				alert("Please choose higher ceat capacity vehicle");

			}

		}
		marker.setMap(null);
	}
	function showRouteEmpl(details) {
		createNewMap();
		showLandmarks();
		showcreatedroute();
		showextAddedEmp();
		var splited = details.split("/");
		for (var i = 1; i < splited.length; i++) {
			var datadet = splited[i].split("#");
			if (document.getElementById("showdist").checked == true) {

				if (datadet[3] <= 0.5) {
					var location = new google.maps.LatLng(datadet[5],
							datadet[6]);
					marker1 = new google.maps.Marker({
						position : location,
						map : map,
						icon : 'images/icon_2.png',
						title : datadet[1]
					});
				}
			}
			if (document.getElementById("showdist1").checked == true) {

				if (datadet[3] > 0.500) {
					if (datadet[3] <= 1) {

						var location = new google.maps.LatLng(datadet[5],
								datadet[6]);
						marker1 = new google.maps.Marker({
							position : location,
							map : map,
							icon : 'images/icon_2.png',
							title : datadet[1]
						});
					}
				}
			}
			if (document.getElementById("showdist2").checked == true) {

				if (datadet[3] > 1) {
					if (datadet[3] <= 2) {

						var location = new google.maps.LatLng(datadet[5],
								datadet[6]);
						marker1 = new google.maps.Marker({
							position : location,
							map : map,
							icon : 'images/icon_2.png',
							title : datadet[1]
						});
					}
				}
			}
			if (document.getElementById("showdist3").checked == true) {

				if (datadet[3] > 2) {
					if (datadet[3] <= 5) {

						var location = new google.maps.LatLng(datadet[5],
								datadet[6]);
						marker1 = new google.maps.Marker({
							position : location,
							map : map,
							icon : 'images/icon_2.png',
							title : datadet[1]
						});
					}
				}
			}

		}
		DisplayRoute(directionsService, directionsDisplay, start, dest);
		directionsDisplay.setMap(map);
	}
	function createNewMap() {
		try {
			map = null;
			var myLatlng = new google.maps.LatLng(cityLat, cityLon);
			var myOptions = {
				zoom : 12,
				center : myLatlng,
				mapTypeId : google.maps.MapTypeId.ROADMAP
			};
			map = new google.maps.Map(document.getElementById("map"), myOptions);

			google.maps.event.addListener(map, 'click', function(event) {
				placeMarker(event.latLng);
				//toastr.info('Clicked on map!...Please wait...');
			});
			//var input = document.getElementById('pac-input');
			//var bottoninmap=document.getElementById("removepoint");
			var searchBox = new google.maps.places.SearchBox(input);
			map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
			map.controls[google.maps.ControlPosition.TOP_RIGHT]
					.push(buttoninmap);
			map.addListener('bounds_changed', function() {
				searchBox.setBounds(map.getBounds());
			});
			markers = [];
			searchBox.addListener('places_changed', function() {
				var places = searchBox.getPlaces();

				if (places.length == 0) {
					return;
				}

				// Clear out the old markers.
				markers.forEach(function(marker1) {
					marker1.setMap(null);
					marker.setMap(null);
				});
				markers = [];

				// For each place, get the icon, name and location.
				var bounds = new google.maps.LatLngBounds();
				places.forEach(function(place) {
					marker.setMap(null);
					var icon = {
						url : place.icon,
						size : new google.maps.Size(71, 71),
						origin : new google.maps.Point(0, 0),
						anchor : new google.maps.Point(17, 34),
						scaledSize : new google.maps.Size(50, 50)
					};

					finalvalue = place.geometry.location;
					if (place.geometry.viewport) {
						// Only geocodes have viewport.
						bounds.union(place.geometry.viewport);
					} else {
						bounds.extend(place.geometry.location);
					}
				});
				map.fitBounds(bounds);
			});
			// [END region_getplaces]

		} catch (e) {
			//alert(e);
		}
	}
	function removeuser() {
		toastr.info('Please wait...Removing some employees');
		var temp = emplist;
		var temp1=compdist;
		var temp2=needledist;
		var k = 0;
		var ab = 0;
		$('input[name="rmuser"]:checked').each(function() {

			for (var s = k; s < emplist.length; s++) {
				if (emplist[s] == this.value) {
					removedlist.push(emplist[s]);
					emplist.splice(s, 1);
					compdist.splice(s, 1);
					needledist.splice(s, 1);
					k++;
				}
			}
			for (var mn = ab; mn < addExtEmp.length; mn++) {
				if (addExtEmp[mn] == this.value) {
					addExtEmp.splice(mn, 1);
					ab++;
				}
			}

		});
		var tempdetails = "/";
		var splited = details.split("/");
		for (var i = 1; i < splited.length; i++) {
			var datadet = splited[i].split("#");
			var cnt11 = 0;
			for (var jl = 0; jl < removedlist.length; jl++) {
				if (removedlist[jl] == datadet[0]) {
					cnt11++;
				}
			}
			if (cnt11 == 0) {
				tempdetails += splited[i] + "/";
			}

		}
		var tempextemp = "";
		var expsplit = extemp.split("/");
		for (var sc = 1; sc < expsplit.length; sc++) {
			var count32 = 0;
			var dtd = expsplit[sc].split("#");
			for (var jl = 0; jl < removedlist.length; jl++) {
				if (dtd[0] == removedlist[jl]) {
					count32++;
				}
			}
			if (count32 == 0) {
				tempextemp += "/" + expsplit[sc];
			}
		}
		details = tempdetails;
		extemp = tempextemp;
		document.getElementById("showemp").innerHTML = "";
		var sumShow = document.getElementById("showemp");
		var dataString = "<table width='80%' ><thead><th>ID</th><th>Employee Name</th><th> No </th><th> Distance</th><th> Distance</th><th> </th></thead>";
		var splited = details.split("/");
		for (var i = 1; i < splited.length; i++) {
			var flag = false;
			var datadet = splited[i].split("#");
			for (var m = 0; m < emplist.length; m++) {
				if (emplist[m] == datadet[0]) {
					flag = true;
				}
			}
			if (flag) {
				dataString += "<tr><td>"
						+ datadet[0]
						+ "</td><td>"
						+ datadet[1]
						+ "</td><td>"
						+ datadet[2]
						+ "</td><td>"
						+ datadet[3]
						+ "</td><td>"
						+ datadet[4]
						+ "</td><td><input type='checkbox' name='rmuser' id='"+datadet[0]+"' value='"+datadet[0]+"'></td></tr>";
			}
		}
		dataString += "<tr><td></td><td></td><td></td><td></td><td></td><td><input type='button' value='removeuser' onclick='removeuser()'></td></tr></table>";
		dataString = "<Strong>Total Employee</Strong> : " + noofnodes
				+ "  <Strong> &nbsp; Employee In Route</Strong> :"
				+ emplist.length + " <br/>" + dataString;
		sumShow.innerHTML = dataString;
		createNewMap();
		showLandmarks();
		showcreatedroute();
		showextAddedEmp();
		alert("Some Employees removed from route");
	}
	function validate() {
		var datalist = "";

		$('.san').show();
		var emp = document.getElementById("empid");
		for (var m = 0; m < emplist.length; m++) {
			var optionNew = document.createElement('option');
			optionNew.value = emplist[m];
			optionNew.text = emplist[m];
			emp.add(optionNew);
		}
		var cmpnydist=document.getElementById("cmpnydist");
		for (var m = 0; m < emplist.length; m++) {
			var optionNew = document.createElement('option');
			optionNew.value = compdist[m];
			optionNew.text = compdist[m];
			cmpnydist.add(optionNew);
		}
		var ndldist=document.getElementById("noodledist");
		for (var m = 0; m < emplist.length; m++) {
			var optionNew = document.createElement('option');
			optionNew.value = needledist[m];
			optionNew.text = needledist[m];
			ndldist.add(optionNew);
		}
		var empidelements = document.getElementById("empid").options;
		var cmpelements = document.getElementById("cmpnydist").options;
		var ndlelements = document.getElementById("noodledist").options;
		
		var data = "";
		for (var m = 0; m < empidelements.length; m++) {
			empidelements[m].selected = true;
		}
		
		for (var m = 0; m < cmpelements.length; m++) {
			cmpelements[m].selected = true;
		}
		
		for (var m = 0; m < ndlelements.length; m++) {
			ndlelements[m].selected = true;
		}
		
		var route = document.getElementById("routename").value;
		var vehicleType = document.getElementById("vt").value;
		var noodle = document.getElementById("points");
		if (route == "" || /^\s+$/.test(route)) {
			document.getElementById("errortag").innerHTML = "Please enter Route name";
			$('.validation-required').removeClass("validation-required")
					.addClass("form-control");
			$('#routename').removeClass("form-control").addClass(
					"validation-required");
			document.getElementById("routename").focus();
			return false;
		} else if (vehicleType == "0") {
			document.getElementById("errortag").innerHTML = "Please select Vehicle Type";
			$('.validation-required').removeClass("validation-required")
					.addClass("form-control");
			$('#vt').removeClass("form-control")
					.addClass("validation-required");
			document.getElementById("vt").focus();
			return false;
		} else if (emplist.length > vehicleType) {
			document.getElementById("errortag").innerHTML = "Vehicle Capacity is less than employees in list";
			$('.validation-required').removeClass("validation-required")
					.addClass("form-control");
			$('#vt').removeClass("form-control")
					.addClass("validation-required");
			document.getElementById("vt").focus();
			return false;
		} else if (noodle.options.length < 2) {
			document.getElementById("errortag").innerHTML = "Please create Route on map";
			$('.validation-required').removeClass("validation-required")
					.addClass("form-control");
			return false;
		} else if (emp.options.length < 1) {
			document.getElementById("errortag").innerHTML = "No Employees in route";
			return false;
		} else {
			toastr.info('Please wait...Creating Route');
			return true;
		}
	}
	function popupemp(type) {
		var srno = 1;
		var tablebody = "<table  width='80%' align='center'><tr><th>Sl No</th><th>Employee Id</th><th>Employee Name</th></tr>";
		var splited = details.split("/");
		for (var i = 1; i < splited.length; i++) {
			var flag = false;
			var datadet = splited[i].split("#");
			if (type == "fvmtr") {
				if (datadet[3] <= 0.5) {
					tablebody += "<tr><td>" + srno + "</td><td>" + datadet[1]
							+ "</td><td>" + datadet[2] + "</td></tr>";
					srno++;
				}
			} else if (type == "fvmt1") {
				if (datadet[3] > 0.500) {
					if (datadet[3] <= 1) {
						tablebody += "<tr><td>" + srno + "</td><td>"
								+ datadet[1] + "</td><td>" + datadet[2]
								+ "</td></tr>";
						srno++;
					}
				}
			} else if (type == "1to2") {
				if (datadet[3] > 1) {
					if (datadet[3] <= 2) {
						tablebody += "<tr><td>" + srno + "</td><td>"
								+ datadet[1] + "</td><td>" + datadet[2]
								+ "</td></tr>";
						srno++;
					}
				}
			} else if (type == "2") {
				if (datadet[3] > 2) {
					if (datadet[3] <= 5) {
						tablebody += "<tr><td>" + srno + "</td><td>"
								+ datadet[1] + "</td><td>" + datadet[2]
								+ "</td></tr>";
						srno++;
					}
				}
			}
		}

		tablebody += "</table>";
		document.getElementById("alertwarn").innerHTML = tablebody;
		$('#validationAlertModal').modal();
	}
	function showcreatedroute() {
		DisplayRoute(directionsService, directionsDisplay, start, dest);
		directionsDisplay.setMap(map);
		var splited = details.split("/");
		for (var i = 1; i < splited.length; i++) {
			var flag = false;
			var datadet = splited[i].split("#");
			for (var m = 0; m < emplist.length; m++) {
				if (emplist[m] == datadet[0]) {
					flag = true;
				}
			}
			if (flag) {
				var location = new google.maps.LatLng(datadet[5], datadet[6]);
				marker1 = new google.maps.Marker({
					position : location,
					map : map,
					icon : 'images/icon_2.png',
					title : datadet[1]
				});
			}
		}
	}
	function removepoints() {
		toastr.info('Please wait...');
		var tablebody = "<table  width='100%' align='center'><tr><th>Sl No</th><th>Location</th><th>Remove</th></tr>";
		var tempp = [];
		for (var l = 0; l < waypts.length; l++) {
			sleep(1000);
			var latlng34 = {
				lat : nodpointslat[l],
				lng : nodpointslng[l]
			};
			geocoder
					.geocode(
							{
								'location' : latlng34
							},
							function(results, status) {
								if (status === google.maps.GeocoderStatus.OK) {
									if (results[1]) {

										tempp
												.push(results[1].formatted_address);
										if (tempp.length == waypts.length) {
											for (var sk = 0; sk < waypts.length; sk++) {
												tablebody += "<tr><td>"
														+ (sk + 1)
														+ "</td><td>"
														+ tempp[sk]
														+ "</td><td><input type='checkbox' name='rmpoint' id='"+sk+"' value='"+sk+"'></td</tr>";
											}
											tablebody += "</table>";
											document
													.getElementById("alertwarn1").innerHTML = tablebody;
											$('#validationAlertModal1').modal();
										}
									} else {
										window.alert('No results found');
									}
								} else {
									window.alert('Geocoder failed due to: '
											+ status);
								}
							});
		}

	}
	function removepoint1() {
		var cboxes = document.getElementsByName('rmpoint');
		var elements = document.getElementById("points").options;
		var len = cboxes.length;
		if (len > 1) {
			for (var i = len - 1; i >= 0; i--) {
				if (cboxes[i].checked == true) {
					waypts.splice(i, 1);
					elements.remove((i + 2));
				}
			}

			toastr.info('Please wait...');
			getEmployee();
			DisplayRoute(directionsService, directionsDisplay, start, dest);
			directionsDisplay.setMap(map);
		}
	}
	function sleep(milliseconds) {
		var start = new Date().getTime();
		for (var i = 0; i < 1e7; i++) {
			if ((new Date().getTime() - start) > milliseconds) {
				break;
			}
		}
	}
	function addEmpManual(id) {
		var nodes = fullnodes.split("$");
		noofnodes = nodes.length;
		var ltln;
		for (var i = 1; i < noofnodes; i++) {
			ltln = nodes[i].split(":");
			if (ltln[4] == id) {

				addExtEmp.push(ltln[4]);
				extemp = "/" + id + "#" + ltln[0] + "#" + ltln[5] + "#NA#NA"
						+ extemp;

			}
		}
		toastr.info('Please wait...');
		createNewMap();
		showLandmarks();
		showcreatedroute();
		getEmployee();
		showextAddedEmp();
	}
	function showextAddedEmp() {
		var nodes = fullnodes.split("$");
		noofnodes = nodes.length;
		for (var i = 1; i < noofnodes; i++) {
			ltln = nodes[i].split(":");
			if (addExtEmp.length > 0) {
				for (var ll = 0; ll < addExtEmp.length; ll++) {
					if (addExtEmp[ll] == ltln[4]) {
						var emptpos = new google.maps.LatLng(ltln[1], ltln[2]);
						marker = new google.maps.Marker({
							position : emptpos,
							map : map,
							icon : 'images/icon_2.png',
							title : ltln[0]
						});

					}
				}
			}
		}
		/* 		createNewMap();
		 showLandmarks();
		 showcreatedroute(); */
	}
	function getEmployee1() {
		getEmployee();
		showextAddedEmp();
	}
	
	function getTime()
	{                    
		var logtype=document.getElementById("type").value;
	    var site=1;
	    var url="GetLogTime?logtype="+logtype+"&site="+site;                                    
	    xmlHttp=GetXmlHttpObject();
	    if (xmlHttp==null)
	    {
	        alert ("Browser does not support HTTP Request");
	        return
	    }                    
	    xmlHttp.onreadystatechange=setLogTime	;
	    xmlHttp.open("GET",url,true) ;               
	    xmlHttp.send(null);
	}
	function GetXmlHttpObject()
	{
	    var xmlHttp=null;
	    if (window.XMLHttpRequest) 
	    {
	        xmlHttp=new XMLHttpRequest();
	    }                
	    else if (window.ActiveXObject) 
	    { 
	        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
	    }

	    return xmlHttp;
	}

	function setLogTime() 
	{                      
	    if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
	    { 
	        var returnText=xmlHttp.responseText;
	        var Time=document.getElementById("time");
	        Time.innerHTML='<select name="Time" id="Time"> <option value="ALL">ALL</option> '+returnText+'</select>';                                             
	    }
	}
function validate1(){
	var logtype=document.getElementById("type").value;
	if(logtype=="all"){
		alert("Please select shift type");
		$('.san').show();
		document.getElementById("errortag").innerHTML = "Please select shift type";
		$('.validation-required').removeClass("validation-required")
				.addClass("form-control");
		$('#type').removeClass("form-control")
				.addClass("validation-required");
		document.getElementById("type").focus();
		return false;
	}else{
		return true;
	}
}

</script>
</html>