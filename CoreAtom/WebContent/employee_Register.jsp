<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@page import="com.agiledge.atom.dto.EmployeeSubscriptionDto"%>
<%@page import="com.agiledge.atom.dao.ShuttleSocketDao"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Atom </title>
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
var emplat=12.9308379;
var emplong=77.5777781;
var inlatlong1;
function loadScript() {
	  
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
		markers=[];
		//formMarkers(marker,myLatLng12);
		
		if((document.getElementById("homelatlong1").value)==null ||(document.getElementById("homelatlong1").value)=='null'){
			
		}else{
			placeMarker();	
		}
	}
	} 
}

function placeMarker(){
 	var homelatlong=document.getElementById("homelatlong1").value;
var homelong=parseFloat((((homelatlong.split(", "))[1]).split(")"))[0]);
var homelat=parseFloat((((homelatlong.split(", "))[0]).split("("))[1]);
	var position=new google.maps.LatLng(homelat,homelong);
 	marker = new google.maps.Marker({
		position : position,
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

						long empid = Long.parseLong(employeeId);
						EmployeeDto dto = new EmployeeSubscriptionService()
						.getEmpRegisterDetails(employeeId);
						/* String addrs = "";
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

						}*/
						String state=dto.getState();
					
					 
						String gender="";
						if(dto.getGender().equalsIgnoreCase("f")){
							gender="Female";
						}
						else if(dto.getGender().equalsIgnoreCase("m")){
							gender="Male";
						}
						/* 
						//currently shift information is not required
						EmployeeDto subdto1 = new ShuttleSocketDao()
						.getShuttlePickUpDrop(employeeId); */ 
						String homelatlong1 = "(" + dto.getLattitude() + ", "
								+ dto.getLongitude() + ")"; 
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
				type="hidden" name="homelatlong1" id="homelatlong1"
				value="<%=homelatlong1%>" />
	<div class="main-page-container">
		<div class="container">	
			<div class="row">
				<div class="col-sm-12">
				<%if(empDtoInHeader.getRegisterStatus()!=null && empDtoInHeader.getRegisterStatus().equals("a")){ %>
				<div class="breadcrumb-wrap">
			
					<a href="employee_home.jsp" >HOME</a>
			
					
				</div>
				<% }%>
				<!-- <div class="breadcrumb-wrap">
					<a href="employee_home.jsp"><img src="images/home.png" /></a>
					<a href="employee_home.jsp">My Information </a>
					<a href="#" class="current">Edit Information</a>
				</div> -->
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
							<a href="employee_Register_Submit.jsp" class="btn btn-light-blue has-icon"><i><img src="images/edit_icon.png" /></i>Edit Information</a>
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
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=dto.getEmployeeFirstName() %></div>
						
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
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Manager :</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15 text-break"><%=dto.getManagerName() %></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Process:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=dto.getProjectUnit() %></div>						
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
							
							<%-- <div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Zip Code:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15"><%=dto.getZip() %></div>
							</div> --%>
						
						
						</div>
											
					</div>
					</div>
					
					<%-- <div class="section-heading">
						<div class="row">
							<div class="col-sm-12">
								Shift Information
							</div>
						</div>
					</div>
					
					<div class="push-15">
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Shift Time (IN):</div>
						<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15"><%=subdto1.getLogin() %></div>
						
											
					
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Shift Time (OUT):</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=subdto1.getLogout() %></div>
						
									
					</div>
					</div> --%>
					
					 <div class="profile-google-map mar-top-30">
						<div class="row">
							<div class="col-sm-12">
								 <div id="map" class="google-map" style="height:600px;"></div>
							</div>
						</div>
					</div>
					
					
				</div>
				
				<div class="footer-wrap">
					<div class="row">
						<div class="col-sm-12 text-center">
							 <p class="text-12">The information stored on this website is maintained in accordance with the organization's Data Privacy Policy. </span><br />Copyright © 2016 Agiledge
 
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
<!-- 	<script src="http://maps.googleapis.com/maps/api/js"></script>
	<script>
		function initialize() {
		  var mapProp = {
			center:new google.maps.LatLng(12.9760559,77.5922071),
			zoom:10,
			mapTypeId:google.maps.MapTypeId.ROADMAP
		  };
		  var map=new google.maps.Map(document.getElementById("profileMap"),mapProp);
		}
		google.maps.event.addDomListener(window, 'load', initialize);
	</script>
	  -->
  </body>
</html>