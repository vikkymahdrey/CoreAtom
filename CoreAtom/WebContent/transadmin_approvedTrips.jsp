<%@page import="com.agiledge.atom.service.TripDetailsService"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dao.TripDetailsDao"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="java.sql.*"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.text.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Tracked Trip Sheet</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>

<script type="text/javascript">
	function searchForm() {
		//alert('hai ');
		document.vendorTripSheetForm.action = "transadmin_trackedTrips.jsp";
		document.vendorTripSheetForm.submit();
		return false;
	}

	function disapprove() {
		$("#approvalStatus").val("Rejected by Transport Admin");
		//	alert($("#approvalStatus").val());
		return true;
	}

	function approve() {

		$("#approvalStatus").val("Approved by Transport Admin");
		alert($("#approvalStatus").val());
		return true;

	}
</script>
</head>
<body>
	<%
		String tripDate = OtherFunctions.changeDateFromatToIso(request
				.getParameter("tripDate"));
		String siteId = request.getParameter("siteId");
		String tripMode = "" + request.getParameter("tripMode");
		String tripTime = "" + request.getParameter("tripTime");
		String employeePersonnelNo = request.getParameter("personnelNo");
		String vendor = request.getParameter("vendor");
		System.out.println("tripDate" + tripDate + "siteId" + siteId
				+ "tripMode" + tripMode);

		String status[] = { "Approved by Transport Admin",
				"Dispproved by Transport Admin" };
		String tripTimeArray[]= new String[1];
		tripTimeArray[0]=tripTime;
		ArrayList<TripDetailsDto> tripSheetList = new TripDetailsService()
				.getTrackedTripSheet(tripDate, tripMode, siteId, tripTimeArray,
						employeePersonnelNo, vendor, status);
		ArrayList<TripDetailsDto> tripSheetSaved = tripSheetList;
	%>
	<%
		boolean flag = false;
		long empid = 0;

		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<hr />


			<form method="POST" name="vendorTripSheetForm" action="ApproveTrips">
				<div>
					<table style="width: 20%; border: 0px none;">
						<tr>
							<td>Employee ID</td>
							<td><input type="text" name="personnelNo" /></td>
						</tr>
						<tr>
							<td>vendor Name</td>
							<td><input type="text" name="vendor" /></td>
						</tr>
						<tr>
							<td></td>
							<td><input type="button" value="Search"
								onclick="return searchForm();" class="formbutton" /></td>
						</tr>
					</table>

				</div>

				<input type="hidden" name="tripDate" id="tripDate"
					value="<%=tripDate%>" /> <input type="hidden" name="siteId"
					id="siteId" value="<%=siteId%>" /> <input type="hidden"
					name="tripMode" id="tripMode" value="<%=tripMode%>" /> <input
					type="hidden" name="tripTime" id="tripTime" value="<%=tripTime%>" />

				<%
					String disable = "";
					boolean totalDisable = true;
					if (tripSheetSaved != null && tripSheetSaved.size() > 0) {

						System.out.println(" in saved jsp " + tripSheetSaved.size());
						flag = true;
				%>
				<!-- From here modified trip sheet starts -->

				<h3>Trip Sheet</h3>

				<%
					for (TripDetailsDto tripDetailsDtoObj : tripSheetSaved) {
				%>
				<table border="1" style="width: 80%;">
					<tr>
					</tr>
					<tr>
						<td colspan="8">
							<table style="background-color: #E8E8E6;">
								<tr>
									<td width="20%">
										<%
											if (tripDetailsDtoObj.isCanTravel()) {
										%> <input type="hidden" name="tripId"
										value="<%=tripDetailsDtoObj.getId()%>" /> <%
 	}
 %>
									</td>
									<td width="20%">Vehicle type</td>
									<td width="20%"><%=tripDetailsDtoObj.getVehicle_type()%></td>
									<td width="20%">Trip ID</td>
									<td width="20%"><%=tripDetailsDtoObj.getTrip_code()%></td>
								</tr>


								<tr>
									<td>&nbsp;</td>
									<td>IN/OUT Time</td>
									<td><%=tripDetailsDtoObj.getTrip_log() + "  "
							+ tripDetailsDtoObj.getTrip_time()%></td>
									<td>Actual IN/OUT time</td>
									<td>
										<%
											String actualLogTime = "";

													if (tripDetailsDtoObj.getActualLogTime() != null
															&& !tripDetailsDtoObj.getActualLogTime().equals("")) {
														actualLogTime = tripDetailsDtoObj.getActualLogTime();
													}
													String vehicleNo = "";

													if (tripDetailsDtoObj.getVehicleNo() != null
															&& !tripDetailsDtoObj.getVehicleNo().equals("")) {
														vehicleNo = tripDetailsDtoObj.getVehicleNo();
													}
													disable = "";
										%> <input type="text" readOnly value="<%=actualLogTime%>"
										name="actualLogTime-<%=tripDetailsDtoObj.getId()%>" /> <input
										type="hidden"
										name="actualTripLog-<%=tripDetailsDtoObj.getId()%>"
										value="<%=tripDetailsDtoObj.getTrip_log()%>" />

									</td>
								</tr>
								<tr>
									<td width="20%"><i><%=tripDetailsDtoObj.getApprovalStatus()%></i>
									</td>
									<td width="20%"></td>
									<td width="20%"></td>
									<td width="20%">Vehicle No</td>
									<td width="20%"><input type="text" readOnly
										value="<%=vehicleNo%>"
										name="vehiclNo-<%=tripDetailsDtoObj.getId()%>" /></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<th width="4%">#</th>
						<th width="20%">Name</th>
						<th width="20%">Area</th>
						<th width="20%">Place</th>
						<th width="20%">landmark</th>
						<th width="10%">Contact No</th>
						<th width="6%">Present</th>

					</tr>
					<%
						int i = 1;
								for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
										.getTripDetailsChildDtoList()) {

									String approved = "";
									String disapproved = "checked";
					%>
					<tr>
						<td><%=i%></td>
						<td><%=tripDetailsChildDto.getEmployeeName()%></td>
						<td><%=tripDetailsChildDto.getArea()%></td>
						<td><%=tripDetailsChildDto.getPlace()%></td>
						<td><%=tripDetailsChildDto.getLandmark()%></td>
						<td><%=tripDetailsChildDto.getContactNumber()%></td>
						<td>
							<%
								String checked = "";

											if (tripDetailsChildDto.getApprovedEmployee() != null
													&& tripDetailsChildDto.getApprovedEmployee()
															.equals(tripDetailsChildDto
																	.getEmployeeId())) {
												checked = "checked";
												disapproved = "";
											}
											String type = "";
											if (tripDetailsDtoObj.getTrip_code().contains("P")) {
												type = "P";
											} else {
												type = "D";
											}
							%> <input type="checkbox" disabled <%=checked%> name="approve"
							value="<%=tripDetailsChildDto.getEmployeeId() + "-"
								+ type%>" />
						</td>



					</tr>
					<%
						i++;

								}
					%>


					</tbody>
				</table>

				<%
					}
					}
				%>

				<%
					if (!flag) {
				%>
				<p>No trip Is is to display</p>


				<%
					}
				%>
				<br />
				<%
					if (tripSheetSaved != null && tripSheetSaved.size() > 0) {
				%>
				<input type="hidden" name="approvalStatus" id="approvalStatus"
					value="Approved by Transport Admin" />
				<!-- 	<input style="margin-left: 63%" class="formbutton" type="submit" value="Approve" onclick="return approve()"/>
						 -->
				<input type="submit" value="Reject" class="formbutton"
					onclick="return disapprove()">
				<%
					}
				%>

				&nbsp; <br /> <br />
				<hr />

			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>

