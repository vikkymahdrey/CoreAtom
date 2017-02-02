<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.dao.ShuttleSocketDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.service.RouteService"%>
<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@page import="com.agiledge.atom.dto.EmployeeSubscriptionDto"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Siemens</title>
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
var emplat=28.6124471;
var emplong=77.3664039;
var inlatlong1;
function loadScript() {
	if( document.getElementById("cityLat").value!=null){
		emplat = document.getElementById("cityLat").value;
		emplong = document.getElementById("cityLon").value;
	}
	 if((document.getElementById("latlong").value)==null ||(document.getElementById("latlong").value)=='null' ||(document.getElementById("latlong").value)=='#'){
	 }else{
		 inlatlong1=document.getElementById("latlong").value;
	 var inlong1=parseFloat((inlatlong1.split("#"))[1]);
	var inlat1=parseFloat((inlatlong1.split("#"))[0]);
	
		emplat = inlat1;
		emplong = inlong1; 
	}  
	 	cityLat = emplat;
		cityLon = emplong;
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
	map = new google.maps.Map(document.getElementById("map"), myOptions);
	
	 if(emplat!='null' || emplong!='null'){
		if(emplat!=null || emplong!=null){
		var myLatLng12 = new google.maps.LatLng(emplat, emplong);
		markers=[];
		//formMarkers(marker,myLatLng12);
		if(inlatlong1==null ||inlatlong1=='null'){
		 }else{
		
		placeMarker2();
		placeMarker();
		 }
		if((document.getElementById("homelatlong1").value)==null ||(document.getElementById("homelatlong1").value)=='null'){
			
		}else{
			placeMarker3();	
		}
	}
	} 
}
function placeMarker() {
	 var inlatlong=document.getElementById("latlong2").value;
	
	var inlong=parseFloat((inlatlong.split("#"))[1]);
	var inlat=parseFloat((inlatlong.split("#"))[0]); 
	 var position1=new google.maps.LatLng(inlat,inlong);
	marker = new google.maps.Marker({
		position : position1,
		title:  'PICK-UP',
		map : map,
		//animation: google.maps.Animation.BOUNCE,
		icon:'images/icon_3.png'
	});
	
 }
function placeMarker2(){
	 	var outlatlong=document.getElementById("latlong1").value;
	var outlong=parseFloat((outlatlong.split("#"))[1]);
	var outlat=parseFloat((outlatlong.split("#"))[0]);
		var position2=new google.maps.LatLng(outlat,outlong);
	 	marker = new google.maps.Marker({
			position : position2,
			title:  'DROP',
			map : map,
			//animation: google.maps.Animation.BOUNCE,
			icon:'images/icon_2.png'
		}); 
 	}
function placeMarker3(){
 	var homelatlong=document.getElementById("homelatlong1").value;
var homelong=parseFloat((((homelatlong.split(", "))[1]).split(")"))[0]);
var homelat=parseFloat((((homelatlong.split(", "))[0]).split("("))[1]);
	var position3=new google.maps.LatLng(homelat,homelong);
 	marker = new google.maps.Marker({
		position : position3,
		title:  'HOME',
		map : map,
		//animation: google.maps.Animation.BOUNCE,
		icon:'images/icon_1.png'
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
  		OtherDao ob = OtherDao.getInstance();
						String message="";
						EmployeeDto empDtoInHeader=null; 
						try{
							
								String role=session.getAttribute("role").toString();
						 		System.out.println("BEFORE SEsion");
								empDtoInHeader = (EmployeeDto)session.getAttribute("userDto");
								System.out.println("AFTER SEsion");
								if(empDtoInHeader==null) {
									empDtoInHeader = new EmployeeService().getEmployeeAccurate(session.getAttribute("user").toString());
								}
									
							}catch(NullPointerException ne) {
						 
								empDtoInHeader = new EmployeeService().getEmployeeAccurate(session.getAttribute("user").toString());
							}
						String employeeId = OtherFunctions.checkUser(session);
						String[] sitecity = ob.getCity(empDtoInHeader.getSite());
						long empid = Long.parseLong(employeeId);
						EmployeeDto dto = new EmployeeSubscriptionService()
						.getShuttleEmpSubscriptionDetails(employeeId);
						EmployeeDto subdto1 = new ShuttleSocketDao()
						.getShuttlePickUpDrop(employeeId);
						String addrs = "";
						if (dto.getAddress() != null
								&& !dto.getAddress().equalsIgnoreCase(
										"null;null;null;null")
								&& !dto.getAddress().equalsIgnoreCase("")) {
							addrs = dto.getAddress();
						}
						String street1 = "", street2 = "", city = "", zip = "";
						if (addrs != null && !addrs.equalsIgnoreCase("")) {
							street1 = (addrs.split(";"))[0];
							if ((addrs.split(";")).length == 4) {
								street2 = (addrs.split(";"))[1];
								city = (addrs.split(";"))[2];
								zip = (addrs.split(";"))[3];
							}

						}
						String state = "UP";
						if(dto.getState().equalsIgnoreCase("KA")){
							state="KARNATAKA";
						}else if(dto.getState().equalsIgnoreCase("TN")){
							state="TAMILNADU";
						}else if(dto.getState().equalsIgnoreCase("UP")){
							state="UP";
						}else if(dto.getState().equalsIgnoreCase("HR")){
							state="HARYANA";
						}else if(dto.getState().equalsIgnoreCase("DL")){
							state="DELHI";
						}
					
					 ArrayList<RouteDto> routesIn = new RouteService()
						.getAllRoutesWithlog("1", "IN");
				ArrayList<RouteDto> routesOut = new RouteService()
						.getAllRoutesWithlog("1", "OUT");
					 String inrouteVal = "", outrouteVal = "";

						if (!subdto1.getinroute().equalsIgnoreCase("")) {
							for (RouteDto route : routesIn) {
								if ((route.getRouteId()) == Integer.parseInt(subdto1
										.getinroute())) {
									inrouteVal = route.getRouteName();
								}
							}
						}
						if (!subdto1.getOutroute().equalsIgnoreCase("")) {
							for (RouteDto route : routesOut) {
								if ((route.getRouteId()) == Integer.parseInt(subdto1
										.getOutroute())) {
									outrouteVal = route.getRouteName();
								}
							}
						}
						String gender="Male";
						if(dto.getGender().equalsIgnoreCase("f")){
							gender="Female";
						}
						String homelatlong1 = "(" + dto.getLattitude() + ", "
								+ dto.getLongitude() + ")";
						//List<GeoTagDto> listd=new ShuttleSocketService().getemployeeGeoTagDetails();
						%>
  
<div class="wrapper">
	<div class="header-wrap">
		<div class="container">
			<div class="row">
				<div class="col-sm-12 text-right">
					<img src="images/user_iocn_header.png" />&nbsp;Welcome <%=empDtoInHeader.getDisplayName() %>  &nbsp;&nbsp;&nbsp;<a href="Logout"><img src="images/logout_icon_header.png" />&nbsp;Log Out</a>
				</div>
			</div>
			
		</div>
	</div>
	<input type="hidden" id="latlong" name="latlong"
				value="<%=dto.getLattitude() + "#" + dto.getLongitude()%>" /><input
				type="hidden" name="latlong2" id="latlong2"
				value="<%=subdto1.getPickup()%>" /><input type="hidden"
				name="latlong1" id="latlong1" value="<%=subdto1.getDrop()%>" /><input
				type="hidden" name="homelatlong1" id="homelatlong1"
				value="<%=homelatlong1%>" />
	<div class="main-page-container">
		<div class="container">	
			<div class="row">
				<div class="col-sm-12">
				
				<div class="breadcrumb-wrap">
					<a href="employee_home.jsp"><img src="images/home.png" /></a>
					<a href="#" class="current">My Information </a>
					<%if(session.getAttribute("roleId").equals("2") || session.getAttribute("roleId").equals("4")){ %>
					<a href="createShuttleRouteKeo.jsp" >Create Route</a>
					<%}%>
<%if(session.getAttribute("roleId").equals("2") ){ %>
					<a href="surveyStats.jsp" >Survey Status</a>
					<a href="geoTagReport.jsp">GeoTag Report </a>
					<a href="ShuttleLocations.jsp">Locations </a>
					<a href="compareShuttleRouteKeo.jsp">Compare Routes </a>
					<%} %>
				</div>
				
				<div class="content-wrap">
				
				<%if(session.getAttribute("status")!=null){ %>
						<div class="row mar-top-40">
						<div class="col-sm-12">
							<div class="alert alert-success"><%= session.getAttribute("status")%></div>
						</div>
					</div>
						<%} %>
					<div class="row">
						<div class="col-sm-8 page-heading mar-top-20">
							<i class="page-heading-icon"><img src="images/user_icon.png" /></i>
							<h5 class="text-blue text-semi-bold">My Information </h5>
						</div>
						<div class="col-sm-4 text-right  mar-top-20">
						<% if(empDtoInHeader.getSite().equalsIgnoreCase("1")){%>
							<a href="employee_details.jsp" class="btn btn-light-blue has-icon"><i><img src="images/edit_icon.png" /></i>Edit Information</a>
							<%}else{ %>
							<a href="employee_details1.jsp" class="btn btn-light-blue has-icon"><i><img src="images/edit_icon.png" /></i>Edit Information</a>
							<%} %>
						</div>						
					</div>
				
				
					<div class="section-heading">
						<div class="row">
							<div class="col-sm-12">
								Personal Information
							</div>
						</div>
					</div>
					
					
					<div class="push-15">
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">First Name:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><input type="hidden" value="<%=sitecity[0]%>" id="cityLat">
											<input type="hidden" value="<%=sitecity[1]%>" id="cityLon"><%=dto.getEmployeeFirstName() %></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Last Name:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=dto.getEmployeeLastName() %></div>						
					</div>
					
					
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Employee ID:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=dto.getPersonnelNo() %></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Gender:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=gender %></div>						
					</div>
					
					
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Email:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15 text-break"><%=dto.getEmailAddress() %></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Mobile No:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=dto.getContactNo() %></div>						
					</div>
					
					</div>
					
					
					<div class="section-heading">
						<div class="row">
							<div class="col-sm-12">
							Current Residence Address
							</div>
						</div>
					</div>
					
					<div class="push-15">
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Street Address:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=dto.getAddress() %></div>
						
						<div class="col-md-5 col-sm-12 col-md-offset-2">
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">City:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15"><%=dto.getCity() %></div>
							</div>
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">State:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15"><%=state %></div>
							</div>
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Zip Code:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15"><%=dto.getZip() %></div>
							</div>
						
						
						</div>
											
					</div>
					</div>
					<% if(empDtoInHeader.getSite().equalsIgnoreCase("1")){%>
					<div class="section-heading">
						<div class="row">
							<div class="col-sm-12">
								Shift / Route Information
							</div>
						</div>
					</div>
					
					<div class="push-15">
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">In Time:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=subdto1.getLogin() %></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Route (IN):</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=inrouteVal %></div>						
					</div>
					
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Out Time:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=subdto1.getLogout() %></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Route (OUT):</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=outrouteVal %></div>						
					</div>
					</div>
					<% } else {%>
						<div class="section-heading">
						<div class="row">
							<div class="col-sm-12">
								Shift Information
							</div>
						</div>
					</div>
					
					<div class="push-15">
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">In Time:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=subdto1.getLogin() %></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Out Time:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=subdto1.getLogout() %></div>						
					</div>
					
					
					</div>
					<%} %>
					<div class="profile-google-map mar-top-30">
						<div class="row">
							<div class="col-sm-12">
								 <div id="map" class="google-map" style="height:480px;"></div>
							</div>
						</div>
					</div>
					
					
				</div>
				
				<div class="footer-wrap">
					<div class="row">
						<div class="col-sm-12 text-center">
							 <p class="text-12">The information stored on this website is maintained in accordance with the organization's Data Privacy Policy. </span><br />Copyright © 2016 siemens
 
						</div>
					</div>
					
				</div>
				
		
				</div>
			</div>
	
		</div>
	</div>
</div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>

  </body>
</html>