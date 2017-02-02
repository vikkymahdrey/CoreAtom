<%@page import="com.agiledge.atom.dto.GeneralShiftDTO"%>
<%@page import="com.agiledge.atom.dao.GeneralShiftDAO"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.VehicleTypeService"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="css/style.css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<title>Assign Shuttle vehicle</title>
</head>
<body>
	<script type="text/javascript">
		var rowNumber = 1;
		$(document)
				.ready(
						function() {
							$('#shiftTime')
									.change(
											function() {
												$(".vehiclecountclass").val(0);
												$.ajax({
															type : 'POST',
															url : 'GetShuttleVehicles?shiftTime='
																	+ $('#shiftTime').val()+ '&routeId='+ $('#routeId').val(),
															success : function(data) {
																var splittedDat = data
																		.split("|");
																for ( var i = 0; i < splittedDat.length; i++) {
																	var typeCount = splittedDat[i]
																			.split("~");
																	if (i == splittedDat.length - 1) {
																		$("#vehicleDet").html(typeCount[0]+ " Seats  "+ typeCount[1]+ " Bookings");
																		$("#totBooking").val(typeCount[1]);
																	} else {
																		$("#count"+ typeCount[0]).val(typeCount[1]);
																	}
																}

															}
														});
											});
							$('.vehicletype').change(
									function() {
										if ($(this).attr("checked")) {
											$(this).parent().next().next()
													.children().removeAttr("readonly");
										} else {
											$(this).parent().next().next()
													.children().prop("readonly", true);
										}
									});
						});
		function validate() {
			var results = [];
			var ret = true;
			$(".vehiclecountclass").each(function() {
				if (isNaN($(this).val())) {
					ret = false;
					$(this).parent().append("Not a number");
				}
			});
			if (ret) {
				return true;
			} else {
				return false;
			}

		}
	</script>


	<div id='body'>
		<div class='content'>
			<%
				long empid = 0;
				String employeeId = OtherFunctions.checkUser(session);
				String routeId = request.getParameter("routeId");
				String inOut = request.getParameter("inOut");

				String siteId = request.getParameter("site");
				ArrayList<VehicleTypeDto> vehicleTypes = new VehicleTypeService()
						.getAllVehicleTypeBySite(siteId);
				ArrayList<GeneralShiftDTO> logData = new GeneralShiftDAO()
						.getActiveLogData(inOut);
				if (employeeId == null || employeeId.equals("null")) {
					response.sendRedirect("index.jsp");
				} else {
					empid = Long.parseLong(employeeId);
			%>
			<%@include file="Header.jsp"%>
			<%
				}
			%>
			<h3>Assign vehicles</h3>
			<form name="form1" method="post" action="AssignShuttleVehicle"
				onsubmit="return validate()">
				<table>
					<tr>
						<td><input type="hidden" value="<%=inOut%>" name="inOut" />
							<input type="hidden" name="totBooking" id="totBooking" /> <input
							type="hidden" value="<%=routeId%>" name="routeId" id="routeId" />
							Shift Time</td>
						<td><select name="shiftTime" id="shiftTime">
								<option>Select</option>
								<%
									for (GeneralShiftDTO dto : logData) {
								%>
								<option value="<%=dto.getLogtime()%>"><%=dto.getLogtime()%></option>
								<%
									}
								%>
						</select></td>
						<td id="vehicleDet"></td>
					</tr>
				</table>
				<table id="myTable" style="border: 10">
					<thead>

						<tr>
							<th>Vehicle Type</th>
							<th>Count <input type="hidden" name="routeId"
								value="<%=routeId%>" />
							</th>
						</tr>

					</thead>

					<tbody>
						<%
							for (VehicleTypeDto dto : vehicleTypes) {
						%>
						<tr>
							<td><input type="checkbox" name="vehicletype"
								class="vehicletype" value="<%=dto.getId()%>" /></td>
							<td><%=dto.getType()%>-<%=dto.getSittingCopacity()%> Seat</td>
							<td><input type="text" readonly="readonly"
								name="count<%=dto.getId()%>" id="count<%=dto.getId()%>"
								class="vehiclecountclass" value="<%=dto.getCount()%>" /> <input
								type="hidden" name="seat<%=dto.getId()%>"
								value="<%=dto.getSittingCopacity()%>" /></td>

						</tr>
						<%
							}
						%>
					</tbody>

				</table>

				<p align="center">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="#" id="AddResults">Add</a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="submit"
						name="submit" value="Submit" class="formbutton" />
				</p>
			</form>


		</div>
		<%@include file="Footer.jsp"%>
	</div>



</body>
</html>
