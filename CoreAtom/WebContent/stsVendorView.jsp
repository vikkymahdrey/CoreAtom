<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.service.StsService"%>
<%@page import="com.agiledge.atom.dto.EmergencyDto"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.nfl.com" prefix="disp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="icon" href="images/agile.png" type="image/x-icon" />
<title>Special Transport</title>
</head>
<body>
	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);

		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
	%><%@include file="Header.jsp"%>
	<%
		}
		List<EmergencyDto> list = new StsService()
				.getStsApprovedEmployees();
	%>
	<div id="body">
		<div class="content">
		<h3>Special Transport Approval</h3>
			<hr />
			<hr />
			<%
				if (list.size() > 0) {
			%>
			<disp:dipxTable id="row" list="<%=list%>" style="align=center">
				<disp:dipxColumn title="Booked By" sortable="true"
					property="bookingFor"></disp:dipxColumn>
				<disp:dipxColumn title="Log Type" sortable="true" property="logtype"></disp:dipxColumn>
				<disp:dipxColumn title="Date" sortable="true" property="travelDate"></disp:dipxColumn>
				<disp:dipxColumn title="Time" sortable="true" property="startTime"></disp:dipxColumn>
				<disp:dipxColumn title="Requested Vehicle Type" sortable="true"
					property="vehicleType"></disp:dipxColumn>
				<disp:dipxColumn title="Total Travellers" sortable="true"
					property="empCount"></disp:dipxColumn>
				<disp:dipxColumn title="Pick Up Point" sortable="true"
					property="area"></disp:dipxColumn>
				<disp:dipxColumn title="Drop Point" sortable="true" property="drop"></disp:dipxColumn>
				<disp:dipxColumn title="" style="align=center">
					<a href="stsVehicleEntry.jsp?id=${row.tripcode}">Allocate Vehicle</a>
				</disp:dipxColumn>
			</disp:dipxTable>
			<%
				} else {
			%>
			<p>
			<h6>No Trips for Approval</h6>
			</p>
			<%
				}
			%>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>