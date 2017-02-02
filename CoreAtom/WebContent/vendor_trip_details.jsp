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
<title>Trip Sheet</title>
<script type="text/javascript">
	function saveTrip() {

		if (confirm("Are you sure you want to save this tripsheet?") == true) {
			var tripDate = document.getElementById("tripDate").value;
			var tripTime = document.getElementById("tripTime").value;
			var tripMode = document.getElementById("tripMode").value;
			var siteId = document.getElementById("siteId").value;
			window.location = "SaveTrip?siteId=" + siteId + "&tripDate="
					+ tripDate + "&tripTime=" + tripTime + "&tripMode="
					+ tripMode + "";
		}
	}
	function printPage() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		window.location = "print_trips.jsp?siteId=" + siteId + "&tripDate="
				+ tripDate + "&tripTime=" + tripTime + "&tripMode=" + tripMode
				+ "";
	}
	function comparePage() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		window.location = "compare_trips.jsp?siteId=" + siteId + "&tripDate="
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
			newwindow.focus()
		}
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
		// System.out.println("tripDate" + tripDate + "siteId" + siteId + "tripMode" + tripMode);
		//ArrayList<TripDetailsDto> tripSheetListActual = new TripDetailsDao().getTripSheetActual(tripDate, tripMode, siteId, tripTime);
		ArrayList<TripDetailsDto> tripSheetList = new TripDetailsDao()
				.getTripSheetModified(tripDate, tripMode, siteId, tripTime);
		ArrayList<TripDetailsDto> tripSheetSaved = new TripDetailsService()
				.getTripSheetSaved(tripDate, tripMode, siteId, tripTime);
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
			<input type="hidden" name="tripDate" id="tripDate"
				value="<%=tripDate%>" /> <input type="hidden" name="siteId"
				id="siteId" value="<%=siteId%>" /> <input type="hidden"
				name="tripMode" id="tripMode" value="<%=tripMode%>" /> <input
				type="hidden" name="tripTime" id="tripTime" value="<%=tripTime%>" />
			<%
				if (tripSheetList != null && tripSheetList.size() > 0) {
					flag = true;
			%>
			<br /> <input style="margin-left: 63%" class="formbutton"
				type="button" value="Back" onclick="javascript:history.go(-1);" />

				<h3>Trip Sheet</h3>
				<hr />
				<input type="hidden" name="tripDate" value="<%=tripDate%>" /> <input
					type="hidden" name="siteId" value="<%=siteId%>" /> <input
					type="hidden" name="tripMode" value="<%=tripMode%>" /> <input
					type="hidden" name="tripTime" value="<%=tripTime%>" />
				<%
					if (!(tripTime.equalsIgnoreCase("all"))) {
				%>
				<p>
					 <input
						type="button" class="formbutton" value="Save" onclick="saveTrip()" />
				</p>
				
				<%
					}
				%>
				<table border="1" width="80%">
				<%
						for (TripDetailsDto tripDetailsDtoObj : tripSheetList) {
				%>
				
					<tr>
					</tr>
					<tr>
						<td>&nbsp</td>
						<td>Vehicle type</td>
						<td colspan="3"><%=tripDetailsDtoObj.getVehicle_type()%></td>
						<td>Trip ID</td>
						<td><%=tripDetailsDtoObj.getTrip_code()%></td>
					</tr>

					<tr>
						<td>&nbsp</td>
						<td>Vehicle #</td>
						<td colspan="3">&nbsp;</td>
						<td>Date</td>
						<td><%=OtherFunctions
							.changeDateFromatToddmmyyyy(tripDetailsDtoObj
									.getTrip_date())%></td>
					</tr>
					<tr>
						<td>&nbsp</td>
						<td>Escort Trip</td>
						<td colspan="3"><%=tripDetailsDtoObj.getIsSecurity()%></td>
						<td>IN/OUT Time</td>
						<td><%=tripDetailsDtoObj.getTrip_log() + "  "
							+ tripDetailsDtoObj.getTrip_time()%></td>
					</tr>
					<tr>
						<td>&nbsp</td>
						<td>Escort Clock</td>
						<td colspan="3">&nbsp;</td>
						<td>Actual IN/OUT time</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>#</td>
						<td>Name</td>
						<td>Area</td>
						<td>Place</td>
						<td>landmark</td>
						<td>Status remark</td>
						<td>Signature</td>
					</tr>
					<%
						int i = 1;
								for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
										.getTripDetailsChildDtoList()) {
					%>
					<tr>
						<td><%=i%></td>
						<td><%=tripDetailsChildDto.getEmployeeName()%></td>
						<td><%=tripDetailsChildDto.getArea()%></td>
						<td><%=tripDetailsChildDto.getPlace()%></td>
						<td><%=tripDetailsChildDto.getLandmark()%></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
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
				<%
						if (!(tripTime.equalsIgnoreCase("all"))) {
				%>
				<p>
					 <input
						type="button" class="formbutton" value="Save" onclick="saveTrip()" />
				</p>
				<%
					}
				%>
			<%
				}
			%>
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
					value="Export As Excel" onclick="printPage()" />
			</p>

<table border="1" width="80%">
			<%
				for (TripDetailsDto tripDetailsDtoObj : tripSheetSaved) {
			%>
			
				<tr>
				</tr>
				<tr>
					<td>&nbsp</td>
					<td>Vehicle type</td>
					<td colspan="3"><%=tripDetailsDtoObj.getVehicle_type()%></td>
					<td>Trip ID</td>
					<td><%=tripDetailsDtoObj.getTrip_code()%></td>
				</tr>
				<tr>
					<td>&nbsp</td>
					<td>Vehicle #</td>
					<td colspan="3">&nbsp;</td>
					<td>Date</td>
					<td><%=OtherFunctions
							.changeDateFromatToddmmyyyy(tripDetailsDtoObj
									.getTrip_date())%></td>
				</tr>
				<tr>
					<td>&nbsp</td>
					<td>Escort Trip</td>
					<td colspan="3"><%=tripDetailsDtoObj.getIsSecurity()%></td>
					<td>IN/OUT Time</td>
					<td><%=tripDetailsDtoObj.getTrip_log() + "  "
							+ tripDetailsDtoObj.getTrip_time()%></td>
				</tr>
				<tr>
					<td>&nbsp</td>
					<td>Escort Clock</td>
					<td colspan="3">&nbsp;</td>
					<td>Actual IN/OUT time</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>#</td>
					<td>Name</td>
					<td>Area</td>
					<td>Place</td>
					<td>landmark</td>
					<td>Status remark</td>
					<td>Signature</td>
				</tr>
				<%
					int i = 1;
							for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
									.getTripDetailsChildDtoList()) {
				%>
				<tr>
					<td><%=i%></td>
					<td><%=tripDetailsChildDto.getEmployeeName()%></td>
					<td><%=tripDetailsChildDto.getArea()%></td>
					<td><%=tripDetailsChildDto.getPlace()%></td>
					<td><%=tripDetailsChildDto.getLandmark()%></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
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
				value="Export As Excel" onclick="printPage()" />

			<%
				}
				if (!flag) {
			%>
			<p>No trip Is is to display</p>
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
