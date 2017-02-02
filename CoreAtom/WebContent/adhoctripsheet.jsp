<%@page import="com.agiledge.atom.service.TripDetailsService"%>
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
<title>Adhoc Trip Sheet</title>
<style>
.hd {
	background-color: #F4F0F5;
}
</style>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
	function printPage() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		window.location = "print_trips.jsp?siteId=" + siteId + "&tripDate="
				+ tripDate + "&tripTime=" + tripTime + "&tripMode=" + tripMode
				+ "";
	}
	
	function printBoardPage() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		window.location = "assignVendor.jsp?siteId=" + siteId + "&tripDate="
				+ tripDate + "&tripTime=" + tripTime + "&tripMode=" + tripMode
				+ "";
	}

	function exportPage() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		var newwindow = window.open("export2PDF?siteId=" + siteId
				+ "&tripDate=" + tripDate + "&tripTime=" + tripTime
				+ "&tripMode=" + tripMode + "", 'name', 'location=no');
		if (window.focus) {
			newwindow.focus();
		}
	}
</script>
</head>
<body>
	<%
		String tripDate = OtherFunctions.changeDateFromatToIso(request
				.getParameter("tripDate"));
		String siteId = request.getParameter("siteId");
		ArrayList<TripDetailsDto> tripSheetSaved = new TripDetailsService().getAdhocTripSheetSaved(tripDate, siteId);
		System.out.println("size of dto"+tripSheetSaved.size());
		%>
	<%
		long empid = 0;
		boolean flag = false;
		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>

	<div id="body">
		<div class="content">
			<hr />
			<%
				if (tripSheetSaved != null && tripSheetSaved.size() > 0) {
					flag = true;
			%>
			<br /> <input style="margin-left: 63%" class="formbutton"
				type="button" value="Back" onclick="javascript:history.go(-1);" />

			<form action="trip_details_modify.jsp" method="POST">
				<h3>Trip Sheet</h3>
				<hr />
				<p id="top"></p>
				<a href="#top" style="position: fixed;">Top</a> &nbsp;
				<div>	<input type="button" class="formbutton" style="float:left"
					value="Export As Excel" onclick="printPageUnsaved()" />  
				

				<%
					}
				%>
				</div>
				<table border="1" width="80%">
					<%
						for (TripDetailsDto tripDetailsDtoObj : tripSheetSaved) {
					%>

					<tr class="hd" >
					</tr>
					<tr class="hd">
						<td>&nbsp</td>
						<td>Vehicle type</td>
						<td colspan="3"><%=tripDetailsDtoObj.getVehicle_type()%></td>
						<td colspan="3">Trip ID</td>
						<td>
						<input type="checkbox" name="tripidcheck"
							id="tripidcheck" value="<%=tripDetailsDtoObj.getId()%>"
							 />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a href="showTripRoute.jsp?tripId=<%=tripDetailsDtoObj.getId()%>" target="_blank"><%=tripDetailsDtoObj.getTrip_code()%></a>
						</td>
					</tr>

					<tr class="hd">
						<td>&nbsp</td>
						<td>Vehicle #</td>
						<td colspan="3">&nbsp;</td>
						<td colspan="3">Date</td>
						<td><%=OtherFunctions
							.changeDateFromatToddmmyyyy(tripDetailsDtoObj
									.getTrip_date())%></td>
					</tr>
					<tr class="hd">
						<td>&nbsp</td>
						<td>Escort Trip</td>
						<td colspan="3"><%=tripDetailsDtoObj.getIsSecurity()%></td>
						<td colspan="3">IN/OUT Time</td>
						<td><%=tripDetailsDtoObj.getTrip_log() + "  "
							+ tripDetailsDtoObj.getTrip_time()%></td>
					</tr>
					<tr class="hd">
						<td>&nbsp</td>
						<td>Travel Time</td>
						<td colspan="3"><%=tripDetailsDtoObj.getTravelTime()%></td>
						<td colspan="3">TotalDistance</td>
						<td><%=tripDetailsDtoObj.getDistance()%></td>
					</tr>
					<tr class="hd">
						<td>&nbsp</td>
						<td>Escort Clock</td>
						<td colspan="3">&nbsp;</td>
						<td colspan="3" >Actual IN/OUT time</td>
						<td>&nbsp;</td>
					</tr>
					<tr class="hd" >
						<td>#</td>
						<td>Name</td>
						<td>Contact</td>
						<td>Gender</td>
						<td>Area</td>
						<td>Place</td>
						<td>landmark</td>
						<td>Dist</td>
						<td>Time</td>
					</tr>
					<%
						int i = 1;
								for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
										.getTripDetailsChildDtoList()) {
					%>
					<tr>
						<td><%=i%></td>
						<td><%=tripDetailsChildDto.getEmployeeName()%>						
						<%if(tripDetailsChildDto.getTransportType()!=null&&tripDetailsChildDto.getTransportType().equalsIgnoreCase("adhoc"))
						{%>
						--A
						<%}%></td>
						<td><%=tripDetailsChildDto.getContactNumber() %></td>
						<td><%=tripDetailsChildDto.getGender()%></td>
						
						<td><%=tripDetailsChildDto.getArea()%></td>
						<td><%=tripDetailsChildDto.getPlace()%></td>
						<td><%=tripDetailsChildDto.getLandmark()%></td>
						<td><%=tripDetailsChildDto.getDistance()%></td>
						<td><%=tripDetailsChildDto.getTime()%></td>
					</tr>
					<%
						i++;
								}
					%>

					<%
						}
					%>

					</tbody>
				</table>
				
			</form>
			<%
				if (tripSheetSaved != null && tripSheetSaved.size() > 0) {
					flag = true;
			%>
			<hr />
			<h3>Trip Sheet</h3>
			<br /> <input style="margin-left: 63%" class="formbutton"
				type="button" value="Back" onclick="javascript:history.go(-1);" />


			<p>
				<input type="button" class="formbutton" value="Show Compare"
					onclick="comparePage()" /> &nbsp;&nbsp; <input type="button"
					class="formbutton" value="Export As PDF" onclick="exportPage()" />
				&nbsp;&nbsp; <input type="button" class="formbutton"
					value="Export As Excel" onclick="printPage()" /> &nbsp;&nbsp; <input
					type="button" class="formbutton" value="Assign Vendor"
					onclick="printBoardPage()" />

			</p>

			<table border="1" widt=h="80%">
				<%
					for (TripDetailsDto tripDetailsDtoObj : tripSheetSaved) {
				%>

				<tr>
				</tr>
				<tr class="hd">
					<td>&nbsp</td>
					<td>Vehicle</td>
					<td colspan="3"><%=tripDetailsDtoObj.getVehicle_type()%></td>
					<td colspan="3">Trip ID</td>
					<td><a href="showTripRoute.jsp?tripId=<%=tripDetailsDtoObj.getId()%>" target="_blank"><%=tripDetailsDtoObj.getTrip_code()%></a></td>
				</tr>
				<tr class="hd">
					<td>&nbsp</td>
					<td>Vehicle #</td>
					<td><%=tripDetailsDtoObj.getVehicleNo() %></td>
					<td>Driver</td>
					<td><%=tripDetailsDtoObj.getDriverName()%></td>
				
					<td colspan="3">Date</td>
					<td><%=OtherFunctions
							.changeDateFromatToddmmyyyy(tripDetailsDtoObj
									.getTrip_date())%></td>
				</tr>
				<tr class="hd">
					<td>&nbsp</td>
					<td>Escort Trip</td>
					<td ><%=tripDetailsDtoObj.getIsSecurity()%></td>
						<td>Driver Contact</td>
					<td><%=tripDetailsDtoObj.getDriverContact()%></td>
					<td colspan="3">IN/OUT Time</td>
					<td><%=tripDetailsDtoObj.getTrip_log() + "  "
							+ tripDetailsDtoObj.getTrip_time()%></td>
				</tr>
				<tr class="hd">
					<td>&nbsp</td>
					<td>Travel Time</td>
					<td colspan="3"><%=tripDetailsDtoObj.getTravelTime()%></td>
					<td colspan="3">TotalDistance</td>
					<td><%=tripDetailsDtoObj.getDistance()%></td>
				</tr>
				<tr class="hd">
					<td>&nbsp</td>
					<td>Escort Clock</td>
					<td colspan="3"><%=tripDetailsDtoObj.getEscort() %></td>
					<td colspan="3">Actual IN/OUT time</td>
					<td>&nbsp;</td>
				</tr>

				<tr class="hd">
					<td>#</td>
					<td>Name</td>
					<td>Contact</td>
					<td>Gender</td>
					
					<td>Area</td>
					<td>Place</td>
					<td>landmark</td>
					<td>Dist</td>
					<td>Time</td>
				</tr>
				<%
					int i = 1;
							for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
									.getTripDetailsChildDtoList()) {
				%>
				<tr>
					<td><%=i%></td>
					<td><%=tripDetailsChildDto.getEmployeeName()%><%if(tripDetailsChildDto.getTransportType()!=null&&tripDetailsChildDto.getTransportType().equalsIgnoreCase("adhoc")){%>--A<%}%></td>
					<td><%=tripDetailsChildDto.getContactNumber() %></td>
					<td><%=tripDetailsChildDto.getGender()%></td>
					
					<td><%=tripDetailsChildDto.getArea()%></td>
					<td><%=tripDetailsChildDto.getPlace()%></td>
					<td><%=tripDetailsChildDto.getLandmark()%></td>
					<td><%=tripDetailsChildDto.getDistance()%></td>
					<td><%=tripDetailsChildDto.getTime()%></td>
				</tr>
				<%
					i++;
							}
				%>
				<tr>
					<td height='60px' colspan="4">Security Seal</td>
					<td colspan="3" align="right">Admin Seal</td>
				</tr>

				<%
					}
				%>
				</tbody>
			</table>
			<br /> <input type="button" class="formbutton" value="Show Compare"
				onclick="comparePage()" /> &nbsp;&nbsp; <input type="button"
				class="formbutton" value="Export As PDF" onclick="exportPage()" />
			&nbsp;&nbsp; <input type="button" class="formbutton"
				value="Export As Excel" onclick="printPage()" /> &nbsp;&nbsp; <input
				type="button" class="formbutton" value="Assign Vendor"
				onclick="printBoardPage()" />

			<%
				}
				if (!flag) {
			%>
			<p>No Adhoc trips Found to display</p>
			<%
				}
			%>

			<p>
				<input style="margin-left: 63%" class="formbutton" type="button"
					value="Back"
					onclick="javascript:window.location = 'view_routing.jsp';" />
			</p>
			<br /> <br />
			<hr />
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
