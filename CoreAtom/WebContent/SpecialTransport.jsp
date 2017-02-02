<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
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
	<%
		String employeeId = OtherFunctions.checkUser(session);

			long empid = Long.parseLong(employeeId);
			List<SiteDto> siteList = new SiteService().getSites();
	%><%@include file="Header.jsp"%>
	<div class="wrapper">

		<div class="main-page-container">
			<div class="container">
				<div class="row">
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
							<form name="form1" method="POST"
								action="SpecialTransportationSubmission.jsp" id="Bookingform"
								onsubmit="return validate()">
								<div class="push-15 login-input-wrap">
									<div class="row">
										<div
											class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey ">Site
										</div>
										<div class="col-md-6 col-sm-7 col-xs-6 mar-top-15">
											<select name="siteId" id="siteId"
												onchange="getVehicles(this.value)" style="width: 160px"  class="form-control">
												<option value="">Select</option>
												<%
													for (SiteDto dto : siteList) {
												%>


												<option value="<%=dto.getId()%>">
													<%=dto.getName()%>
												</option>


												<%
													}
												%>
											</select>
										</div>

									</div>
									<div class="row">
										<div
											class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey ">Reason
										</div>
										<div class="col-md-6 col-sm-7 col-xs-6 mar-top-15">
											<select id="Reason1" name="Reason1" onchange="checkreason();"  class="form-control"
												style="width: 160px">
												<option value="">Select</option>
												<option value="Airport">Airport Pickup/Drop</option>
												<option value="Team Outing">Team Outing</option>
												<option value="Client Meeting">Client Meeting</option>
												<option value="Training">Training</option>
												<option value="Other">Other</option>
											</select><input type="text" name="Reason" id="Reason"  class="form-control"
												style="visibility: hidden;" placeholder="Specify reason" style="width: 160px"/>
										</div>

									</div>
									<div class="row">
										<div
											class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey ">Travel
											Date</div>
										<div class="col-md-6 col-sm-7 col-xs-6 mar-top-15">
											<input type="text" name="travelDate" id="travelDate" style="width: 160px" class="form-control"/>
										</div>

									</div>
									<div class="row">
										<div
											class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey ">Time
										</div>
										<div class="col-md-6 col-sm-7 col-xs-6 mar-top-15">
											<select name="startTime" id="startTime" style="width: 160px"  class="form-control">
												<option value="">Select</option>
												<%=OtherFunctions.FullTimeInIntervalOptions()%>
											</select>
										</div>

									</div>
									<div class="row">
										<div
											class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey ">Total
											Traveller</div>
										<div class="col-md-6 col-sm-7 col-xs-6 mar-top-15">
											<input type="text" id="Travellers" name="Travellers" style="width: 160px" class="form-control">
										</div>

									</div>
									<div class="row">
										<div
											class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey ">Vehicle
											Type</div>
										<div class="col-md-6 col-sm-7 col-xs-6 mar-top-15">
											<select name="chosenVehicleType" id="chosenVehicleType"  class="form-control"
												style="width: 160px">
												<option value=''>All</option>

											</select>
										</div>

									</div>
									<div class="row">
										<div
											class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey ">Pick
											up</div>
										<div class="col-md-6 col-sm-7 col-xs-6 mar-top-15">
											<select name="addrP" id="addrP" style="width: 160px"  class="form-control"
												onchange="showpickup();">
												<option value="">Select</option>
												<option value='Ozone Mane'>Ozone Mane</option>
												<option value='other'>Other</option>

											</select>
										</div>

										<div class="row">
											<div
												class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey ">Drop
											</div>
											<div class="col-md-6 col-sm-7 col-xs-6 mar-top-15">
												<select name="addrD" id="addrD" style="width: 160px"  class="form-control"
													onchange="showdrop();">
													<option value="">Select</option>
													<option value='Ozone Mane'>Ozone Mane</option>
													<option value='other'>Other</option>

												</select>
											</div>

										</div>
									</div>

								</div>
								<div class="push-15">
									<div class="row">
										<div
											class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">
										</div>
										<div
											class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey"
											style="visibility: hidden;" id="pickC">Pick up Point</div>
										<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15 "
											style="visibility: hidden;" id="pickI">
											<textarea class="addresstextarea form-control" name="Pickup"
												id="Pickup" placeholder="Enter PickUp Point"  class="form-control">
						</textarea>
										</div>
										<div
											class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey"
											style="visibility: hidden;" id="dropC">Drop Point</div>
										<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15 "
											style="visibility: hidden;" id="dropI">
											<textarea class="addresstextarea form-control" name="Drop"
												id="Drop" placeholder="Enter Drop Point"  class="form-control">
						</textarea>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-12 text-red text-12" style="color: red">*All
										fields are mandatory</div>
								</div>

								<div class="row text-center mar-btm-30">
									<div class="col-sm-12">
										<input type="submit" class="btn btn-blue save-btn"
											value="Book" /> &nbsp;&nbsp;&nbsp; <input type="reset"
											class="btn btn-blue save-btn" value="Reset" />
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
<script src="https://code.jquery.com/jquery-2.2.0.js"></script>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>


<script type="text/javascript">
	$(document).ready(function() {
		$("#travelDate").datepick();

	});
	function validate() {

		var SiteId = document.getElementById("siteId").value;
		var Reason = document.getElementById("Reason").value;
		var travelDate = document.getElementById("travelDate").value;
		var startTime = document.getElementById("startTime").value;
		var Travellers = document.getElementById("Travellers").value;
		var Type = document.getElementById("chosenVehicleType").value;
		var Pickup = document.getElementById("Pickup").value;
		var Drop = document.getElementById("Drop").value;
		var currentDate = new Date();
		var dateT = (currentDate.getMonth() + 1);
		if (dateT < 10) {
			dateT = "0" + dateT;
		}
		var currentDatevar = currentDate.getDate() + "/" + dateT + "/"
				+ currentDate.getFullYear();
		if (SiteId == "") {
			document.getElementById("errortag").innerHTML = "Please Choose site";
			$('.validation-required').removeClass("validation-required")
					.addClass("form-control");
			$('.san').show();
			document.getElementById("siteId").focus();
			return false;
		} else if (Reason == "") {
			document.getElementById("errortag").innerHTML = "Please Provide Proper Reason";
			$('.validation-required').removeClass("validation-required")
					.addClass("form-control");
			$('.san').show();
			document.getElementById("Reason1").focus();
			return false;
		} else if (travelDate.length < 1) {
			document.getElementById("errortag").innerHTML = "Please Choose Date";
			$('.validation-required').removeClass("validation-required")
					.addClass("form-control");
			$('.san').show();
			document.getElementById("travelDate").focus();
			//  date.focus();
			return false;

		} else if (startTime == null || startTime == "") {
			document.getElementById("errortag").innerHTML = "Please Choose Start Time";
			$('.validation-required').removeClass("validation-required")
					.addClass("form-control");
			$('.san').show();
			document.getElementById("startTime").focus();
			//  date.focus();
			return false;

		}
		if (Travellers == null || Travellers < 1) {
			document.getElementById("errortag").innerHTML = "Atlest One Employee should travel";
			$('.validation-required').removeClass("validation-required")
					.addClass("form-control");
			$('.san').show();
			document.getElementById("Travellers").focus();
			return false;

		} else if (Type == "All" || Type == null || Type == "") {
			document.getElementById("errortag").innerHTML = "Please Choose VehicleType ";
			$('.validation-required').removeClass("validation-required")
					.addClass("form-control");
			$('.san').show();
			document.getElementById("chosenVehicleType").focus();
			return false;
		} else if (Pickup == null || Pickup.length < 1 || /^\s+$/.test(Pickup)) {
			document.getElementById("errortag").innerHTML = "Enter Pickup Point";
			$('.validation-required').removeClass("validation-required")
					.addClass("form-control");
			$('.san').show();
			document.getElementById("Pickup").focus();
			return false;

		} else if (Drop == null || Drop.length < 1 || /^\s+$/.test(Drop)) {
			document.getElementById("errortag").innerHTML = "Enter Drop Point";
			$('.validation-required').removeClass("validation-required")
					.addClass("form-control");
			$('.san').show();
			document.getElementById("Drop").focus();
			return false;

		}

		else {
			return true;
		}

	}
	function getVehicles(chosenSite) {
		if (chosenSite.length > 0) {
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
						fullvehicle = fullvehicle.split("$");
						document.getElementById("chosenVehicleType").innerHTML = "<select name='chosenVehicleType' id='chosenVehicleType'  ><option value=''>All</option>"
								+ fullvehicle[1] + "</select>";

					}
				};
				xmlhttp.open("POST", "GetVehicleNotInSiteEmergency?siteId="
						+ chosenSite, true);
				xmlhttp.send();
			} catch (e) {

				alert(e);
			}
		}
	}
	function checkreason() {
		var reason = document.getElementById("Reason1").value;
		if (reason == 'Other') {
			var esc = document.getElementById("Reason");
			document.getElementById("Reason").value = '';
			esc.style.visibility = 'visible';
		} else {
			var esc = document.getElementById("Reason");
			esc.style.visibility = 'hidden';
			document.getElementById("Reason").value = reason;
		}
	}
	function showpickup() {
		var addr = document.getElementById("addrP").value;
		var coll = document.getElementById("pickC");
		var col = document.getElementById("pickI");
		var esc = document.getElementById("Pickup");
		if (addr == 'other') {
			coll.style.visibility = 'visible';
			col.style.visibility = 'visible';
			document.getElementById("Pickup").value = '';
			esc.style.visibility = 'visible';
		} else {
			coll.style.visibility = 'hidden';
			col.style.visibility = 'hidden';
			esc.style.visibility = 'hidden';
			document.getElementById("Pickup").value = addr;
		}
	}
	function showdrop() {
		var addr = document.getElementById("addrD").value;
		var coll = document.getElementById("dropC");
		var col = document.getElementById("dropI");
		var esc = document.getElementById("Drop");
		if (addr == 'other') {

			coll.style.visibility = 'visible';
			col.style.visibility = 'visible';
			document.getElementById("Drop").value = '';
			esc.style.visibility = 'visible';
		} else {
			coll.style.visibility = 'hidden';
			col.style.visibility = 'hidden';
			esc.style.visibility = 'hidden';
			document.getElementById("Drop").value = addr;
		}
	}
</script>

</html>