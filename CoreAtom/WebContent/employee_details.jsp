<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.dao.ShuttleSocketDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
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
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Employee Details</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script>
function showPopup1(url) {
	var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	size = "height=450,width=520,top=200,left=300," + params;

	newwindow = window.open(url, 'name', size);

}
function validateDrop(status) {
	var points = document.getElementsByName('points');
	var point;
	for (var i = 0; i < points.length; i++) {
		if (points[i].checked) {
			point = points[i].value;
		}
	}
	 if (point == "diffr") {
		 $(".loc").show();
			$("#samedrop").val("no");
			$("#outlatlong").val("");
	} else if(point == "same"){
		$(".loc").hide();
		$("#samedrop").val("yes");
	} else{
		$("#samedrop").val("ignorecase");
		$("#outlatlong").val("");
		$("#inlatlong").val("");
	}
	 if(status=='hide'){
			$('.tripping').hide();
			document.getElementById("inRoute").value="0";
			document.getElementById("outTime").value="0";
			document.getElementById("outRoute").value="0";
			}else{
				if($('.tripping').is(':hidden')){
				$('.tripping').show();
				document.getElementById("inTime").value="";
				document.getElementById("inRoute").value="";
				document.getElementById("outTime").value="";
				document.getElementById("outRoute").value="";
			}
			}
}
function validate(){
$('.san').show();
 var fname=document.getElementById("fname").value;
var lname=document.getElementById("lname").value;
var gender=document.getElementById("gender").value;
var email=document.getElementById("email").value;
var mob=document.getElementById("mob").value;
var addr=document.getElementById("addr").value;
var city=document.getElementById("city").value;
var state=document.getElementById("state").value;
var zip=document.getElementById("zip").value;
 var inTime=document.getElementById("inTime").value;
var inRoute=document.getElementById("inRoute").value;
var outTime=document.getElementById("outTime").value;

var outRoute=document.getElementById("outRoute").value;
var inlatlong=document.getElementById("inlatlong").value;
var outlatlong=document.getElementById("outlatlong").value;
var homelatlong=document.getElementById("homelatlong").value;
var points=document.getElementById("points").value;
var samepoint=document.getElementById("samedrop").value;
if(fname=="" || fname==null || fname==" "){
	document.getElementById("errortag").innerHTML ="Please enter your first name";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#fname').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("fname").focus();
	return false;
}else if(lname=="" || lname==null || lname==" "){
	document.getElementById("errortag").innerHTML ="Please enter your last name";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#lname').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("lname").focus();
	return false;
}else if(mob=="" || mob==null || mob.length!=10 ||mob<0){
	document.getElementById("errortag").innerHTML ="Please enter a valid mobile number";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#mob').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("mob").focus();
	return false;
}   else if(addr=="" || addr==null || addr==" " ){
	document.getElementById("errortag").innerHTML ="Please enter your address";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#addr').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("addr").focus();
	return false;
}else if (/^\s+$/.test(addr)){
	document.getElementById("errortag").innerHTML ="Please enter your address";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#addr').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("addr").focus();
	return false;
}else if(city=="" || city==null || city==" "){
	document.getElementById("errortag").innerHTML ="Please enter city name";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#city').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("city").focus();
	return false;
}else if(zip=="" || zip==null || zip==" " || isNaN(zip) || zip.length>6){
	document.getElementById("errortag").innerHTML ="Please enter zip code";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#zip').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("zip").focus();
	return false;
}else if((state=="KA" && zip.substring(0, 3)!="560") ||(state=="TN" && zip.substring(0, 3)!="635" || zip<100000)){
	document.getElementById("errortag").innerHTML ="Please enter the correct zip code";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#zip').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("zip").focus();
	return false;
}else if(homelatlong=="" || homelatlong==null || homelatlong==" " ||homelatlong=="(null, null)" ||homelatlong=="(, )" ){
	document.getElementById("errortag").innerHTML ="Please pin your address on map";
	document.getElementById("alertwarn").innerHTML ="Please pin your address on map";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#validationAlertModal').modal();
	return false;
}else if(samepoint!="ignorecase" && (inTime=="" || inTime==null || inTime==" ")){
	document.getElementById("errortag").innerHTML ="Please select shift time[IN]";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#inTime').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("inTime").focus();
	return false;
}else if(samepoint!="ignorecase" && (outTime=="" || outTime==null || outTime==" ")){
	document.getElementById("errortag").innerHTML ="Please select shift time[OUT]";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#outTime').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("outTime").focus();
	return false;
}else if(samepoint!="ignorecase" && (inRoute=="" || inRoute==null || inRoute==" ")){
	document.getElementById("errortag").innerHTML ="Please select route";
	$('#inRoute').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("inRoute").focus();
	return false;
} else if(samepoint!="ignorecase" && (outRoute=="" || outRoute==null || outRoute==" ")){ 
	document.getElementById("errortag").innerHTML ="Please select route";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#outRoute').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("outRoute").focus();
	return false;
}else if(samepoint!="ignorecase" && (inlatlong=="" || inlatlong==null || inlatlong==" ")){
	document.getElementById("errortag").innerHTML ="Please pin your pick up point on map";
	document.getElementById("alertwarn").innerHTML ="Please pin your pick up point on map";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#validationAlertModal').modal();
	return false;
}else if(samepoint!="ignorecase" && (samepoint=="no" && (outlatlong=="" || outlatlong==null || outlatlong==" "))){
	document.getElementById("errortag").innerHTML ="Please pin your drop point on map";
	document.getElementById("alertwarn").innerHTML ="Please pin your drop point on map";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#validationAlertModal').modal();
	return false;
}else{
	return true;
}  
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
  <body>
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
						.getShuttleEmpSubscriptionDetails(employeeId);
						EmployeeDto subdto1 = new ShuttleSocketDao()
						.getShuttlePickUpDrop(employeeId);
						String genderm="checked='checked'",genderf="";
						if(dto.getGender().equalsIgnoreCase("f")){
							genderf="checked='checked'" ;
							genderm="";
						}
						String stateK="selected",stateT="";
						 if(dto.getState().equalsIgnoreCase("TN")){
							 stateT="selected";
							 stateK="";
						 }
						 ArrayList<RouteDto> routesIn = new RouteService()
							.getAllRoutesWithlog("1", "IN");
					ArrayList<RouteDto> routesOut = new RouteService()
							.getAllRoutesWithlog("1", "OUT");
						 ArrayList<LogTimeDto> inLog = new LogTimeService()
							.getAllLogtime("IN");
					ArrayList<LogTimeDto> outLog = new LogTimeService()
							.getAllLogtime("OUT");
					String inlatlong = "", outlatlong = "";
		 			if (subdto1.getPickup() != null
		 					&& !subdto1.getPickup().equalsIgnoreCase("")
		 					&& !subdto1.getPickup().equalsIgnoreCase("null"))
		 				inlatlong = "(" + subdto1.getPickup().split("#")[0]
		 						+ ", " + subdto1.getPickup().split("#")[1]
		 						+ ")";
		 			if (subdto1.getDrop() != null
		 					&& !subdto1.getDrop().equalsIgnoreCase("")
		 					&& !subdto1.getDrop().equalsIgnoreCase("null"))
		 				outlatlong = "(" + subdto1.getDrop().split("#")[0]
		 						+ ", " + subdto1.getDrop().split("#")[1] + ")";
		 			String homelatlong = "(" + dto.getLattitude() + ", "
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
	
	<div class="main-page-container">
		<div class="container">	
			<div class="row">
				<div class="col-sm-12">
				
				<div class="breadcrumb-wrap">
					<a href="employee_home.jsp"><img src="images/home.png" /></a>
					<a href="employee_home.jsp" >My Information </a>
					<a href="#" class="current">Edit Information</a>
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
				<form action="shuttlesubDetails.jsp" id="UserDetails" method="post"
				onsubmit="return validate();">
				
					<div class="row">
						<div class="col-sm-8 page-heading mar-top-20">
							<i class="page-heading-icon"><img src="images/edit_profile.png" /></i>
							<h5 class="text-blue text-semi-bold">Edit Information</h5>
						</div>
									
					</div>
				<div class="row mar-top-20">
						<div class="col-sm-12">
							<div class="alert alert-danger san" hidden="hidden" style="color: red" ><p id="errortag"></p></div>
						</div>
					</div>
				<%if(request.getParameter("id")==null) { %>
					<div class="section-heading">
						<div class="row">
							<div class="col-sm-12">
								Personal Information
							</div>
						</div>
					</div>
					
					
					<div class="push-15">
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">First Name:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><input type="text" name="fname" id="fname"
							value="<%=dto.getEmployeeFirstName()%>" /><input type="hidden"
							id="rollno" name="rollno" value="<%=dto.getPersonnelNo()%>"><input
							type="hidden" id="email" name="email"
							value="<%=dto.getEmailAddress()%>"><input type="hidden" name="inlatlong" id="inlatlong"
							value="<%=inlatlong%>" /> <input type="hidden" name="outlatlong"
							id="outlatlong" value="<%=outlatlong%>" />  <input
							type="hidden" name="homelatlong" id="homelatlong"
							value="<%=homelatlong%>" /><input
							type="hidden" name="sessionvar" id="sessionvar"
							value="<%= session.getAttribute("responsecode") %>" /><input
							type="hidden" name="backtosurvey" id="backtosurvey"
							value="<%= session.getAttribute("BacktoSurvey") %>" /></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey mandatory">Last Name:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><input type="text" name="lname" id="lname" value="<%=dto.getEmployeeLastName()%>" /></div>						
					</div>
					
					
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Employee ID:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=dto.getPersonnelNo() %></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Email:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=dto.getEmailAddress() %></div>						
					</div>
					
					
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">Gender:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15 text-break">Male&nbsp;&nbsp;<input type="radio" name="gender" id="gender"
							value="M" <%=genderm %> />&nbsp;&nbsp;&nbsp;&nbsp;Female&nbsp;&nbsp;<input
							type="radio" name="gender" id="gender" value="F" <%=genderf %>/></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey mandatory">Mobile No:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">
							<input type="text" name="mob" id="mob" value="<%=dto.getContactNo()%>" class="form-control" />
						</div>						
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
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">Street Address:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15 ">						
						<textarea class="addresstextarea form-control" name="addr" id="addr"><%=dto.getAddress() %>
						</textarea>
						</div>
						
						<div class="col-md-5 col-sm-12 col-md-offset-2">
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">City:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15">
									<input type="text" name="city" id="city" class="form-control" value="<%=dto.getCity()%>" />
								</div>
							</div>
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">State:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15">
									<select class="form-control" name="state" id="state">
										<option value="KA" <%=stateK %>>KARNATAKA</option>
										<option value="TN" <%=stateT %>>TAMILNADU</option>
									</select>
								</div>
							</div>
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">Zip Code:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15">
									<input type="text" class="form-control" name="zip" id="zip" value="<%=dto.getZip() %>" />
								</div>
							</div>
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey"></div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15">
									<a href="#" class="btn btn-light-blue has-icon map-popup-link"  onclick="initialize('home')"  ><i><img src="images/map_icon.png" /></i>Pin Your Location on Map</a>
								</div>
								
							</div>
						
						
						</div>
											
					</div>
					</div>
					
					<div class="section-heading">
						<div class="row">
							<div class="col-sm-12">
								Shift / Route Information
							</div>
						</div>
					</div>
					
					<div class="push-15">
					
					<div class="row">
						<div class="col-md-3 col-sm-5 mandatory">Is your Pick up and Drop point same :</div>
						<div class="col-md-3 col-sm-4">
						<!-- 	<select class="form-control">
								<option>Select One</option>
							</select> -->YES&nbsp;&nbsp;<input type="radio" name="points" id="points"
							value="same"   onChange="validateDrop('show');"/>&nbsp;&nbsp;NO&nbsp;&nbsp;<input
							type="radio" name="points" id="points" value="diffr" checked="checked" onChange="validateDrop('show');"/>&nbsp;&nbsp;Not a regular user&nbsp;&nbsp;<input type="radio"
							name="points" id="points" value="ignorecase"
							onChange="validateDrop('hide');" /><input type="hidden"
							id="samedrop" name="samedrop" value="no" />						
						</div>
						
						<div class="col-md-6 col-sm-12 text-right tripping">
							<a href="#" class="btn btn-light-blue has-icon mar-top-5 map-popup-link" onclick="initialize('in')" ><i><img src="images/map_icon.png" /></i>Pin Your Pickup Point on Map</a>&nbsp;&nbsp;
							<a href="#" class="btn btn-light-blue has-icon mar-top-5 loc map-popup-link" onclick="initialize('out')" ><i><img src="images/map_icon.png" /></i>Pin Your Drop Point on Map</a>
						</div>
						
					</div>
					
					<div class="row tripping">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">In Time:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><select name="inTime"
							id="inTime"><option value="">Select Time</option>
						<% for (LogTimeDto indto : inLog) {
												String selected = "";
												if ((indto.getLogTime()).equalsIgnoreCase(subdto1
														.getLogin())) {
													selected = "selected";
												}
												%><option value="<%=indto.getLogTime()%>" <%=selected%>><%=indto.getLogTime()%></option>
												<%
												
						}%></select>
						</div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey mandatory">Route (IN):</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><select name="inRoute" id="inRoute"><option
									value="">Select Route</option>
								<%
									for (RouteDto route : routesIn) {
												String selected = "";
												if (!subdto1.getinroute().equalsIgnoreCase("")
														&& (route.getRouteId()) == Integer
																.parseInt(subdto1.getinroute())) {
													selected = "selected";
												}
								%><option value="<%=route.getRouteId()%>" <%=selected%>><%=route.getRouteName()%></option>
								<%
									}
								%></select></div>						
					</div>
					
					<div class="row tripping">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">Out Time:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><select name="outTime"
							id="outTime"><option value="">Select Time</option>
								<%
									for (LogTimeDto outdto : outLog) {
												String selected = "";
												if ((outdto.getLogTime()).equalsIgnoreCase(subdto1
														.getLogout())) {
													selected = "selected";
												}
								%><option value="<%=outdto.getLogTime()%>" <%=selected%>><%=outdto.getLogTime()%></option>
								<%
									}
								%></select></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey mandatory">Route (OUT):</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><select name="outRoute" id="outRoute"><option
									value="">Select Route</option>
								<%
									for (RouteDto route : routesOut) {
												String selected = "";
												if (!subdto1.getOutroute().equalsIgnoreCase("")
														&& (route.getRouteId()) == Integer
																.parseInt(subdto1.getOutroute())) {
													selected = "selected";
												}
								%><option value="<%=route.getRouteId()%>" <%=selected%>><%=route.getRouteName()%></option>
								<%
									}
								%></select></div>						
					</div>
					</div>
					<%}else{
						String male1="checked='checked'",female1="";
						if(request.getParameter("gender").equalsIgnoreCase("female")){
							female1="checked='checked'";
							male1="";
						}
						String stateK1="checked='checked'",stateT1="",same1="",diff1="",ignore1="",hidn="";
						if(request.getParameter("state").equalsIgnoreCase("TN")){
							stateT1="checked='checked'";
							stateK1="";
						}
						if(request.getParameter("samedrop").equalsIgnoreCase("yes")){
							same1="checked='checked'";
							hidn="";
						}else if(request.getParameter("samedrop").equalsIgnoreCase("ignorecase")){
							ignore1="checked='checked'";
						}else{
							diff1="checked='checked'";
						}
						
						%>
					<div class="section-heading">
						<div class="row">
							<div class="col-sm-12">
								Personal Information
							</div>
						</div>
					</div>
					<div class="push-15">
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">First Name:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><input type="text" name="fname" id="fname"
							value="<%=request.getParameter("fname")%>" /><input type="hidden"
							id="rollno" name="rollno" value="<%=request.getParameter("rollno")%>"><input
							type="hidden" id="email" name="email"
							value="<%=request.getParameter("email")%>"><input type="hidden" name="inlatlong" id="inlatlong"
							value="<%=request.getParameter("inlatlong")%>" /> <input type="hidden" name="outlatlong"
							id="outlatlong" value="<%=request.getParameter("outlatlong")%>" /><input
							type="hidden" name="homelatlong" id="homelatlong"
							value="<%=request.getParameter("homelatlong")%>" /></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey mandatory">Last Name:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><input type="text" name="lname" id="lname"
							value="<%=request.getParameter("lname")%>" /></div>						
					</div>
					
					
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Employee ID:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=request.getParameter("rollno") %></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Email:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=request.getParameter("email") %></div>						
					</div>
					
					
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">Gender:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15 text-break">Male&nbsp;&nbsp;<input type="radio" name="gender" id="gender"
							value="M" <%=male1 %> />&nbsp;&nbsp;&nbsp;&nbsp;Female&nbsp;&nbsp;<input
							type="radio" name="gender" id="gender" value="F" <%=female1 %>/></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey mandatory">Mobile No:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">
							<input type="text" name="mob" id="mob" value="<%=request.getParameter("mob")%>" class="form-control" />
						</div>						
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
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">Street Address:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15 ">						
						<textarea class="addresstextarea form-control" name="addr" id="addr" ><%=request.getParameter("addr") %>
						</textarea>
						</div>
						
						<div class="col-md-5 col-sm-12 col-md-offset-2">
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">City:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15">
									<input type="text" name="city" id="city" class="form-control" value="<%=request.getParameter("city")%>" />
								</div>
							</div>
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">State:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15">
									<select class="form-control" name="state" id="state">
										<option value="KA" <%=stateK1 %>>KARNATAKA</option>
										<option value="TN" <%=stateT1 %>>TAMILNADU</option>
									</select>
								</div>
							</div>
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">Zip Code:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15">
									<input type="text" class="form-control" name="zip" id="zip"  value="<%=request.getParameter("zip")%>"/>
								</div>
							</div>
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey"></div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15">
									<a href="#" class="btn btn-light-blue has-icon map-popup-link"  onclick="initialize('home')"  ><i><img src="images/map_icon.png" /></i>Pin Your Location on Map</a>
								</div>
								
							</div>
						
						
						</div>
											
					</div>
					</div>
					<div class="section-heading">
						<div class="row">
							<div class="col-sm-12">
								Shift / Route Information
							</div>
						</div>
					</div>
					<div class="push-15">
					
					<div class="row">
						<div class="col-md-3 col-sm-5 mandatory">Is your Pick up and Drop point same :</div>
						<div class="col-md-3 col-sm-4">
						<!-- 	<select class="form-control">
								<option>Select One</option>
							</select> -->YES&nbsp;&nbsp;<input type="radio" name="points" id="points"
							value="same"   onChange="validateDrop('show');" <%=same1 %>/>&nbsp;&nbsp;NO&nbsp;&nbsp;<input
							type="radio" name="points" id="points" value="diffr" onChange="validateDrop('show');" <%=diff1 %>/>&nbsp;&nbsp;Not a regular user&nbsp;&nbsp;<input type="radio"
							name="points" id="points" value="ignorecase"
							onChange="validateDrop('hide');" <%=ignore1 %>/><input type="hidden"
							id="samedrop" name="samedrop" value="no" />						
						</div>
						<div class="col-md-6 col-sm-12 text-right tripping">
							<a href="#" class="btn btn-light-blue has-icon mar-top-5 map-popup-link" onclick="initialize('in')" ><i><img src="images/map_icon.png" /></i>Pin Your Pickup Point on Map</a>&nbsp;&nbsp;
							<a href="#" class="btn btn-light-blue has-icon mar-top-5 loc map-popup-link" onclick="initialize('out')"><i><img src="images/map_icon.png" /></i>Pin Your Drop Point on Map</a>
						</div>
						
					</div>					
					<div class="row tripping">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">In Time:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><select name="inTime"
							id="inTime"><option value="">Select Time</option>
						<% for (LogTimeDto indto : inLog) {
												String selected = "";
												if(!request.getParameter("samedrop").equalsIgnoreCase("ignorecase")){
												if ((indto.getLogTime()).equalsIgnoreCase(request.getParameter("inTime"))) {
													selected = "selected";
												}}
												%><option value="<%=indto.getLogTime()%>" <%=selected%>><%=indto.getLogTime()%></option>
												<%
												
						}%></select>
						</div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey mandatory">Route (IN):</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><select name="inRoute" id="inRoute"><option
									value="">Select Route</option>
								<%
									for (RouteDto route : routesIn) {
												String selected = "";
												if(!request.getParameter("samedrop").equalsIgnoreCase("ignorecase")){
												if (route.getRouteId() == Integer
																.parseInt(request.getParameter("inRoute"))) {
													selected = "selected";
												}}
								%><option value="<%=route.getRouteId()%>" <%=selected%>><%=route.getRouteName()%></option>
								<%
									}
								%></select></div>						
					</div>

					<div class="row tripping">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">Out Time:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><select name="outTime"
							id="outTime"><option value="">Select Time</option>
								<%
									for (LogTimeDto outdto : outLog) {
												String selected = "";
												if(!request.getParameter("samedrop").equalsIgnoreCase("ignorecase")){
												if ((outdto.getLogTime()).equalsIgnoreCase(request.getParameter("outTime"))) {
													selected = "selected";
												}}
								%><option value="<%=outdto.getLogTime()%>" <%=selected%>><%=outdto.getLogTime()%></option>
								<%
									}
								%></select></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey mandatory">Route (OUT):</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><select name="outRoute" id="outRoute"><option
									value="">Select Route</option>
								<%
									for (RouteDto route : routesOut) {
												String selected = "";
												if(!request.getParameter("samedrop").equalsIgnoreCase("ignorecase")){
												if (route.getRouteId() == Integer
																.parseInt(request.getParameter("outRoute"))) {
													selected = "selected";
												}}
								%><option value="<%=route.getRouteId()%>" <%=selected%>><%=route.getRouteName()%></option>
								<%
									}
								%></select></div>						
					</div>
					</div>
					<%} %>
					<div class="border-line mar-top-30 mar-btm-10"></div>
					
					<div class="row">
						<div class="col-sm-12 text-red text-12" style="color: red">*All fields are mandatory</div>
					</div>
					
					<div class="row text-right mar-btm-30">
						<div class="col-sm-12">
							<input type="submit" class="btn btn-blue save-btn" value="Review" />
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
	
	
	
	<div class="popup-wrapper">
		<div class="popup-content-wrap">
			<div class="popup-header">
				Choose Home Location
				<a href="#" class="close-btn">X</a>
			</div>
			
			<div class="popup-inner-content-wrap">
				<div class="row">
					<div class="col-sm-6 mar-top-15">
						<input type="text" id="pac-input" placeholder="Search Box"  class="form-control map-search-input"/>
					</div>
					<div class="col-sm-6 text-right mar-top-15"><a href="#" class="btn btn-blue text-uppercase close-btn" onclick="return setvalues()">Set the location</a></div>
				</div>
				
				<div class="map-wrapper" id="editProfileMap">
				
				</div>
			</div>
			
		</div>
	</div>
	
	
	<div class="modal fade" tabindex="-1" role="dialog" id="validationAlertModal">
	  <div class="modal-dialog modal-md">
		<div class="modal-content">
		  
		  <div class="modal-body">
			<p class="alert alert-warning" id="alertwarn"><img src="images/alert_icon.png" /></p>
		  </div>
		  <div class="modal-footer text-center">
			<button type="button" class="btn btn-blue" data-dismiss="modal">Ok</button>
		  </div>
		</div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	
</div>
 <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
   
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
<script  src="https://code.jquery.com/jquery-2.2.0.js"></script>
    <script src="js/bootstrap.min.js"></script>
	<script src="https://maps.googleapis.com/maps/api/js?sensor=true&libraries=places&client=gme-leptonsoftwareexport&signature=1t2jNPl7sIPdevsQdfKNrx25bko="></script>
	<script>
	$( document ).ready(function() {
		$(".map-popup-link").click(function(e){
			e.preventDefault();
			$(".popup-wrapper").fadeIn("slow");
		//	initialize();
		});
		
		$(".popup-wrapper .close-btn").click(function(e){
			e.preventDefault();
			$(".popup-wrapper").fadeOut("slow")
		});
		
		/* 
		 $(".save-btn").click(function(e){
			e.preventDefault();
			$('#validationAlertModal').modal();
		});
		  */
		
	});
		
	</script>
	
	 <script type="text/javascript"  >
	 var lat=12.9760559;
		var lng=77.5922071;
		var marker;
		var markers;
		var count=0;
		var  map;
		var name;
		var i=0;
		var finalvalue;
			 function initialize(type) {
	if(count>0){
	for (var k = 0; k < markers.length; k++) {
	    markers[k].setMap(null);
	  }}
				 name=type;
				 if(document.getElementById(type+"latlong").value!='null'){
						if(document.getElementById(type+"latlong").value!=null){
							if(document.getElementById(type+"latlong").value!=''){	
							var latlong=document.getElementById(type+"latlong").value;
							lat=((latlong.split("("))[1]).split(", ")[0];
							lng=(((latlong.split("("))[1]).split(", ")[1]).split(")")[0];
							}
						}
				 }
				 $(".map-popup-link").click(function(e){
						e.preventDefault();
						$(".popup-wrapper").fadeIn("slow");
					});
					
					$(".popup-wrapper .close-btn").click(function(e){
						e.preventDefault();
						$(".popup-wrapper").fadeOut("slow")
					});
					
					google.maps.event.addDomListener(window, 'load', initialize);
					var mapProp;
					var myLatLng12 = new google.maps.LatLng(lat,lng);
					if(i==0){
						 i=i+1;
			  mapProp = {
				center:new google.maps.LatLng(lat,lng),
				zoom:12,
				mapTypeId:google.maps.MapTypeId.ROADMAP
			  };
			  map=new google.maps.Map(document.getElementById("editProfileMap"),mapProp);
			  google.maps.event.addListener(map, 'click', function(event) {
				    placeMarker(event.latLng);
				});
			  
					}else{
						
			/*			mapProp = {
									center:new google.maps.LatLng(lat,lng),
									zoom:12,
									mapTypeId:google.maps.MapTypeId.ROADMAP
								  };*/
								  $('#pac-input').val('');
						map.panTo(new google.maps.LatLng(lat,lng));
					}
			  markers=[];
			  
			 placeMarker(myLatLng12);
			 var input = document.getElementById('pac-input');
			  var searchBox = new google.maps.places.SearchBox(input);
			 // map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

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
				        url: place.icon,
				        size: new google.maps.Size(71, 71),
				        origin: new google.maps.Point(0, 0),
				        anchor: new google.maps.Point(17, 34),
				        scaledSize: new google.maps.Size(25, 25)
				      };
				      
				      // Create a marker for each place.
				      markers.push(new google.maps.Marker({
				        map: map,
				      //  icon: icon,
				        position: place.geometry.location
						//icon:'images/house.png'
				      }));

				      finalvalue=place.geometry.location;
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

			} 
		
			
			function placeMarker(location) {
	for (var k = 0; k < markers.length; k++) {
	    markers[k].setMap(null);
	  }
				if(count>0)
				{
			marker.setMap(null);
				}
	 markers.forEach(function(marker1) {
				     	marker1.setMap(null); 
				        marker.setMap(null);
				    });
				for(var j = 0; j<markers.length;j++){
					markers[j].setMap(null);

					}
				marker = new google.maps.Marker({
					position : location,
					map : map,
					title:  name
					//icon:'images/house.png'
				});

				count=count+1;
				finalvalue=location;
			}
			function setvalues(){
				document.getElementById(name+"latlong").value=finalvalue;
				name="";
				finalvalue="";
				
			}

		
	 </script> 
  </body>
</html>