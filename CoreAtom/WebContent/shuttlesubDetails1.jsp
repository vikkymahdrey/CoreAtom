<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.RouteService"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Summary</title>

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
  <body onload="focusmap();">
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
						String fname = request.getParameter("fname").toUpperCase()
								.charAt(0)
								+ request.getParameter("fname").substring(1).toLowerCase();
						String lname = request.getParameter("lname").toUpperCase()
								.charAt(0)
								+ request.getParameter("lname").substring(1).toLowerCase();
						String State = "NOIDA";
						if(request.getParameter("state").equalsIgnoreCase("KA")){
							State="KARNATAKA";
						}else if(request.getParameter("state").equalsIgnoreCase("TN")){
							State="TAMILNADU";
						}else if(request.getParameter("state").equalsIgnoreCase("UP")){
							State="UP";
						}else if(request.getParameter("state").equalsIgnoreCase("HR")){
							State="HARYANA";
						}else if(request.getParameter("state").equalsIgnoreCase("DL")){
							State="DELHI";
						}
						
					
						String gender="Male";
						if(request.getParameter("gender").equalsIgnoreCase("F")){
							gender="Female";
						}
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
	
	<div class="main-page-container">
		<div class="container">	
			<div class="row">
				<div class="col-sm-12">
				
				<div class="breadcrumb-wrap">
					<a href="employee_home.jsp"><img src="images/home.png" /></a>
					<a href="employee_home.jsp" >My Information </a>
					<a href="employee_details.jsp">Edit Information</a>
					<a href="#" class="current">Summary</a>
					<%if(session.getAttribute("roleId").equals("2") || session.getAttribute("roleId").equals("4")){ %>
					<a href="create_route.jsp" >Create Route</a>
					<%}%>
<%if(session.getAttribute("roleId").equals("2") ){ %>
					<a href="surveyStats.jsp" >Survey Status</a>
					<a href="geoTagReport.jsp">GeoTag Report </a>
					<a href="ShuttleLocations.jsp">Locations </a>
					<a href="compareRoutes.jsp">Compare Routes </a>
					<%} %>
				</div>
				<div class="content-wrap">
				
				<form action="ShuttleSubscribe" name="form1" method="post">
					<div class="row">
						<div class="col-sm-8 page-heading mar-top-20">
							<i class="page-heading-icon"><img src="images/subscription.png" /></i>
							<h5 class="text-blue text-semi-bold">Summary</h5>
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
					<input type="hidden" name="site" value="<%=empDtoInHeader.getSite() %>" />
					<input type="hidden" name="id" value="2" /> <input
					type="hidden" name="formNo" id="formNo" value="2" /> <input
					type="hidden" name="empid" id="empid" value="<%=employeeId%>" />  <input
					type="hidden" name="homelatlong" id="homelatlong"
					value="<%=request.getParameter("homelatlong") ==null?"":request.getParameter("homelatlong")%>" /> <input
					type="hidden" name="rollno" id="rollno"
					value="<%=request.getParameter("rollno")%>" /> <input
					type="hidden" name="fname" id="fname" value="<%=fname%>" /> <input
					type="hidden" name="lname" id="lname" value="<%=lname%>" /> <input
					type="hidden" name="gender" id="gender"
					value="<%=request.getParameter("gender")%>" /> <input
					type="hidden" name="email" id="email"
					value="<%=request.getParameter("email")%>" /> <input type="hidden"
					name="mob" id="mob" value="<%=request.getParameter("mob")%>" /><input
					type="hidden" name="addr" id="addr"
					value="<%=request.getParameter("addr")%>" /> <input
					type="hidden" name="city" id="city"
					value="<%=request.getParameter("city")%>" /> <input type="hidden"
					name="state" id="state" value="<%=request.getParameter("state")%>" />
					<input type="hidden" name="zip" id="zip"
					value="<%=request.getParameter("zip")%>" /> <input type="hidden"
					name="inTime" id="inTime"
					value="<%=request.getParameter("inTime") == null ? "" : request
					.getParameter("inTime")%>" />
					
					<input type="hidden" name="outTime" id="outTime"
					value="<%=request.getParameter("outTime") == null ? "" : request
					.getParameter("outTime")%>" />
					
					<input type="hidden" name="interest" id="interest"
					value="<%=request.getParameter("interest") == null ? "" : request
					.getParameter("interest")%>" />
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">First Name:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=fname %></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Last Name:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=lname %></div>						
					</div>
					
					
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Employee ID:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=request.getParameter("rollno") %></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Gender:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=gender %></div>						
					</div>
					
					
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Email:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15 text-break"><%=request.getParameter("email") %></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Mobile No:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=request.getParameter("mob") %></div>						
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
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=request.getParameter("addr") %></div>
						
						<div class="col-md-5 col-sm-12 col-md-offset-2">
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">City:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15"><%=request.getParameter("city") %></div>
							</div>
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">State:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15"><%=State %></div>
							</div>
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Zip Code:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15"><%=request.getParameter("zip") %></div>
							</div>
						
						
						</div>
											
					</div>
					</div>
			
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
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%= request.getParameter("inTime")%></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Out Time:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%= request.getParameter("outTime")%></div>						
					</div>
					
					
					</div>
					<div class="profile-google-map mar-top-30">
						<div class="row">
							<div class="col-sm-12">
								 <div id="profileMap" class="google-map" style="height:480px;"></div>
							</div>
						</div>
					</div>
					
					
					<div class="border-line mar-top-20 mar-btm-20"></div>
					
					<div class="row text-right mar-btm-10">
						<div class="col-sm-12">
							<input type="button" class="btn btn-default" value="Edit" onclick="goback();">&nbsp;&nbsp;<input type="submit" class="btn btn-blue" value="Submit" id="sub"/>
							
						</div>
					</div>
					</form>
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
	<script src="https://maps.googleapis.com/maps/api/js?sensor=true&callback=initialize&libraries=places&client=gme-leptonsoftwareexport4&signature=xghu9DIoNr63z8_al_oJCSPWQh0="></script>
	<script>
	var homelatlong1;
	var homelat1=28.6124471;
	var homelong1=77.3664039;
	var map;
		function initialize() {
			if (document.getElementById("homelatlong").value != null) {
				homelatlong1 = document.getElementById("homelatlong").value;
				homelong1 = parseFloat((((homelatlong1.split(", "))[1]).split(")"))[0]);
				homelat1 = parseFloat((((homelatlong1.split(", "))[0]).split("("))[1]);
			}
		  var mapProp = {
			center:new google.maps.LatLng(homelat1,homelong1),
			zoom:12,
			mapTypeId:google.maps.MapTypeId.ROADMAP
		  };
		  map=new google.maps.Map(document.getElementById("profileMap"),mapProp);
		  if (homelat1 != 'null' || homelong1 != 'null') {
				if (homelat1 != null || homelong1 != null) {
					var myLatLng12 = new google.maps.LatLng(homelat1, homelong1);
					markers = [];
				
					placeMarker3();
				}
			}
		}
		google.maps.event.addDomListener(window, 'load', initialize);
		function placeMarker() {
			var inlatlong = document.getElementById("inlatlong").value;
			var inlong = parseFloat((((inlatlong.split(", "))[1]).split(")"))[0]);
			var inlat = parseFloat((((inlatlong.split(", "))[0]).split("("))[1]);

			var position1 = new google.maps.LatLng(inlat, inlong);
			marker = new google.maps.Marker({
				position : position1,
				title : 'PICK-UP',
				map : map,
				//animation: google.maps.Animation.BOUNCE,
				icon : 'images/icon_3.png'
			});

		}
		function placeMarker2() {
			var points = document.getElementById("points").value;
			if (points == "diffr") {
				var outlatlong = document.getElementById("outlatlong").value;
				var outlong = parseFloat((((outlatlong.split(", "))[1]).split(")"))[0]);
				var outlat = parseFloat((((outlatlong.split(", "))[0]).split("("))[1]);
				var position2 = new google.maps.LatLng(outlat, outlong);
				marker = new google.maps.Marker({
					position : position2,
					title : 'DROP',
					map : map,
					//animation: google.maps.Animation.BOUNCE,
					icon : 'images/icon_2.png'
				});
			}
		}
		function placeMarker3() {
			var homelatlong = document.getElementById("homelatlong").value;
			var homelong = parseFloat((((homelatlong.split(", "))[1]).split(")"))[0]);
			var homelat = parseFloat((((homelatlong.split(", "))[0]).split("("))[1]);
			var position3 = new google.maps.LatLng(homelat, homelong);
			marker = new google.maps.Marker({
				position : position3,
				title : 'HOME',
				map : map,
				//animation: google.maps.Animation.BOUNCE,
				icon : 'images/icon_1.png'
			});
		}
		function goback() {
			document.form1.action = "employee_details1.jsp";
			document.form1.submit();
		}
		function focusmap(){
			document.getElementById("sub").focus();
		}

	</script>
	 
  </body>
</html>