<%@page import="com.agiledge.atom.service.ShuttleRoutingService"%>
<%@page import="com.agiledge.atom.dao.ShuttleRoutingDao"%>
<%@page import="com.agiledge.atom.dto.GeneralShiftDTO"%>
<%@page import="com.agiledge.atom.service.TripDetailsService"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
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
	function printBoardPage() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		window.location = "assignVendor.jsp?siteId=" + siteId + "&tripDate="
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
	function showAuditLog(relatedId,moduleName){
  		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
  		var size = "height=450,width=900,top=200,left=300," + params;
  		var url="ShowAuditLog.jsp?relatedNodeId="+relatedId+"&moduleName="+moduleName;	
  	    newwindow = window.open(url, 'AuditLog', size);

  		if (window.focus) {
  			newwindow.focus();
  		}
  	}
</script>
</head>
<body>
	<%
		String tripDate = request.getParameter("tripDate");
		String siteId = request.getParameter("siteId");
		String tripMode = "" + request.getParameter("tripMode");
		String tripTime = "" + request.getParameter("tripTime");		
		ArrayList<GeneralShiftDTO> tripSheetList = new ShuttleRoutingService().getShuttleTrip( siteId,tripDate, tripMode, tripTime);		
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

				<table border="1" width="80%">
					<%
						for (GeneralShiftDTO tripDetailsDtoObj : tripSheetList) {
					%>

					<tr>
					</tr>
					<tr>
						<td>&nbsp</td>
						<td>Vehicle type</td>
						<td ><%=tripDetailsDtoObj.getVehicleType()%></td>
						<td>&nbsp</td>				
						<td>Date</td>
						<td><%=OtherFunctions
							.changeDateFromatToddmmyyyy(tripDate)%></td>
					</tr>
					<tr>
						<td>&nbsp</td>
						<td>IN/OUT</td>
						<td><%=tripDetailsDtoObj.getLogtype()%></td>
						<td>&nbsp</td>
						<td>IN/OUT Time</td>
						<td><%=tripDetailsDtoObj.getLogtime()%></td>
					</tr>
					<tr>
						<td>#</td>
						<td><b>EmpCode</b></td>
						<td><b>Name</b></td>
						<td><b>Area</b></td>
						<td><b>Place</b></td>
						<td><b>landmark</b></td>						
					</tr>
					<%
						int i = 1;
								for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
										.getEmpDtoList()) {
					%>
					<tr>
						<td><%=i%></td>
						<td><%=tripDetailsChildDto.getEmployeeId()%></td>
						<td><%=tripDetailsChildDto.getEmployeeName()%></td>
						<td><%=tripDetailsChildDto.getArea()%></td>
						<td><%=tripDetailsChildDto.getPlace()%></td>
						<td><%=tripDetailsChildDto.getLandmark()%></td>
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
				<%} %>
			<br /> <br />
			<hr />
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
