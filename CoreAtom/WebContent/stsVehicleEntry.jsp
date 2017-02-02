<%-- 
    Document   : SpecialTransport
    Created on : Apr 11, 2016, 4:05:50 PM
    Author     : Sandesh
--%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="com.agiledge.atom.dto.VehicleDto"%>
<%@page import="com.agiledge.atom.service.VehicleService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.service.StsService"%>
<%@page import="com.agiledge.atom.dto.EmergencyDto"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<!DOCTYPE html >
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>Sts Requiest</title>
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
<body>

	<%@include file="Header.jsp"%>
	<%
		OtherDao ob = null;
		long empid = 0;

		String employeeId = OtherFunctions.checkUser(session);

		empid = Long.parseLong(employeeId);
		ob = OtherDao.getInstance();
		String empRole = session.getAttribute("role").toString();
		String id = request.getParameter("id");
		EmergencyDto dto = null;
		ArrayList<VehicleTypeDto> vehicletype= new VehicleService().getAllVehicleTypeBySite("1");
		if (id != null) {
			dto = new StsService().getStsApprovedTrip(id);
		}
	%>
	<div class="wrapper">

		<div class="main-page-container">
			<div class="container">
				<div class="row">
					<div class="col-sm-12">

						<div class="content-wrap">
							<form name="form1" method="POST" action="StsDriverAllocation"
								id="Bookingform" onsubmit="return validate()">
								<input type="hidden" id="tripid" name="tripid" value="<%=id%>" />
								<input type="hidden" id="empid" name="empid"
									value="<%=employeeId%>" />
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


								<div class="section-heading">
									<div class="row">
										<div class="col-sm-12">Special Transportation Service</div>
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
								<div class="push-15">

									<div class="row" style="padding-left: 20%; padding-right: 20%;">
										<div
											class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Travel
											Date :</div>
										<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=dto.getTravelDate()%></div>

										<div
											class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Time
											:</div>
										<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=dto.getStartTime()%></div>
									</div>
									<div class="row" style="padding-left: 20%; padding-right: 20%;">
										<div
											class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Reason
											:</div>
										<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=dto.getReason()%></div>

										<div
											class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Travellers:</div>
										<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=dto.getEmpCount()%></div>
									</div>
									<div class="row" style="padding-left: 20%; padding-right: 20%;">
										<div
											class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Vehicle
											Type Requiested :</div>
										<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=dto.getVehicleType()%></div>

										<div
											class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey"></div>
										<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"></div>
									</div>
									<div class="row" style="padding-left: 20%; padding-right: 20%;">
										<div
											class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Pick
											up :</div>
										<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=dto.getArea()%></div>

										<div
											class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Drop
											:</div>
										<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=dto.getDrop()%></div>
									</div>
								</div>
								<div class="section-heading">
									<div class="row">
										<div class="col-sm-12">
											<strong>Vehicle Details</strong>
										</div>
									</div>
								</div>
								<div class="row" style="padding-left: 20%; padding-right: 20%;">
									<div
										class="col-md-3 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">select
										vehicle from</div>
									<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15">
										<select name="details" id="details" style="width: 160px" class="form-control"
											onchange="showVehices();">
											<option value="">Select</option>
											<option value='registered'>Registered Vehicles</option>
											<option value='Ola'>Ola</option>
											<option value='Uber'>Uber</option>
										</select>
									</div>

									<div
										class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey"></div>
									<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"></div>
								</div>
								<div class="push-15" id="registered" style="visibility: hidden;">
									<div class="row" style="padding-left: 20%; padding-right: 20%;">
										<div
											class="col-md-3 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Vehicle
											Type Approved :</div>
										<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15">
											<select name="vehicletype" id="vehicletype" 
												style="width: 160px" onchange="getvehicles();" class="form-control">
												<option value="">Select</option>
												<%
													for(VehicleTypeDto type : vehicletype){
												%>
												<option value="<%=type.getId()%>"><%=type.getType()%></option>
												<%
													}
												%>
											</select>
										</div>



									</div>
									<div class="row" style="padding-left: 20%; padding-right: 20%;">
										<div
											class="col-md-3 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Vehicle
											Number :</div>
										<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">
											<select name="regno" id="regno1" style="width: 160px" class="form-control"
												onchange="GetVehicleDriver();">
												<option value="">Select</option>
											</select>
										</div>
										<div
											class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Driver
											Name :</div>
										<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">
											<select name="drivername" id="drivername" class="form-control" style="width: 160px"></select>
										</div>

									</div>
								</div>
								<div class="row text-right mar-btm-30">
									<div class="col-sm-12 mar-top-15" align="center">
										<input type="submit" class="btn btn-blue save-btn"
											value="Allocate" /> &nbsp;&nbsp;&nbsp;&nbsp; <input
											type="reset" class="btn btn-blue save-btn" value="Reset" />
									</div>
								</div>
							</form>
						</div>

						<%@include file="Footer.jsp"%>

					</div>
				</div>

			</div>
		</div>
	</div>

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/bootstrap.min.js"></script>

</body>
<script type="text/javascript" src="js/validate.js"></script>
<script src="js/JavaScriptUtil.js"></script>
<script src="js/Parsers.js"></script>
<script src="js/InputMask.js"></script>
<script type="text/javascript">
	function validate() {
		var vehicletype = document.getElementById("vehicletype").value;
		var vehicleNumber = document.getElementById("regno1").value;
		var drivername = document.getElementById("drivername").value;
		var detiailstype = document.getElementById("details").value;
		if (detiailstype == '') {
			document.getElementById("errortag").innerHTML = "Please Select Details type";
			$('.validation-required').removeClass("validation-required")
					.addClass("form-control");
			$('.san').show();
			$('#details').removeClass("form-control").addClass(
					"validation-required");
			document.getElementById("details").focus();
			return false;
		} else if (detiailstype == 'registered') {
			 if (vehicletype == null || vehicletype.length < 1
					|| /^\s+$/.test(vehicletype)) {
				document.getElementById("errortag").innerHTML = "Provide Vehicle Type";
				$('.validation-required').removeClass("validation-required")
						.addClass("form-control");
				$('.san').show();
				$('#vehicletype').removeClass("form-control").addClass(
						"validation-required");
				document.getElementById("vehicletype").focus();
				return false;
			} else if (vehicleNumber == null || vehicleNumber == "") {
				document.getElementById("errortag").innerHTML = "Please Provide Vehicle Number";
				$('.validation-required').removeClass("validation-required")
						.addClass("form-control");
				$('.san').show();
				$('#regno').removeClass("form-control").addClass(
						"validation-required");
				document.getElementById("regno1").focus();
				return false;
			} else if (drivername == null || drivername.length < 1
					|| /^\s+$/.test(drivername)) {
				document.getElementById("errortag").innerHTML = "Please Provide Driver Name";
				$('.validation-required').removeClass("validation-required")
						.addClass("form-control");
				$('.san').show();
				$('#drivername').removeClass("form-control").addClass(
						"validation-required");
				document.getElementById("drivername").focus();
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}

	}
	function showVehices() {

		var status = document.getElementById("details").value;
		var res = document.getElementById("registered");
		if (status == 'registered') {
			res.style.visibility = 'visible';
		} else {
			res.style.visibility = 'hidden';
		}

	}
	function getvehicles() {
		var type = document.getElementById("vehicletype").value;
		try {
			var xmlhttp;
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					var fullvehicle = xmlhttp.responseText;
					fullvehicle = fullvehicle.split("#");
					document.getElementById("regno1").innerHTML = "<select name='regno' id='regno1' style='width: 160px' ><option value=''>Select</option>"
							+ fullvehicle[1] + "</select>";

				}
			}
			xmlhttp.open("POST", "GetVehicleNotInSiteAndType?siteId=1&typeId="
					+ type, true);
			xmlhttp.send();
		} catch (e) {

			alert(e);
		}
	}
	function GetVehicleDriver() {
		var vehicle = document.getElementById("regno1").value;
		try {
			var xmlhttp;
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					var drivers = xmlhttp.responseText;
					document.getElementById("drivername").innerHTML = "<select  name='drivername' id='drivername' style='width: 160px' ><option value=''>Select</option>"
							+ drivers + "</select>";

				}
			}
			xmlhttp.open("POST", "GetVehicleDriver?&vehicle=" + vehicle, true);
			xmlhttp.send();
		} catch (e) {

			alert(e);
		}
	}
</script>
</html>