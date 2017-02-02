<%@page import="com.agiledge.atom.service.ShuttleServcie"%>
<%@page import="com.agiledge.atom.dto.GeneralShiftDTO"%>
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dto.AdhocDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.AdhocService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Shuttle Booking</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";

.bordered {
	width: 70%;
	border-style: solid;
}
</style>

<title>Subscription</title>
</head>
<body>

	<script type="text/javascript">
		$(document).ready(function() {
		});

		function approveRejectBooking(bookingId, appReject) {
			window.location.href = "ApproveShuttleRequest?bookingId="
					+ bookingId + "&appReject=" + appReject;
		}
	</script>

	<%
		long empid = 0;

		String employeeId = OtherFunctions.checkUser(session);

		empid = Long.parseLong(employeeId);
	%>
	<%@ include file="Header.jsp"%>
	<%
		ArrayList<GeneralShiftDTO> bookingDetails = new ShuttleServcie()
				.getBookingDetailsForManager(employeeId);
	%>
	<br />
	<h3 style="margin-left: 100px">Adhoc Requests</h3>
	<form name="adhocApprrovereject" action="ApproveRejectAdhoc"
		method="post">
		<table>
			<thead>
				<tr>
					<th>Employee Code</th>
					<th>Employee Name</th>
					<th>Effective date</th>
					<th>IN/OUT</th>
					<th>IN/OUT Time</th>
					<th>Status</th>
					<th></th>
				</tr>
			</thead>
			<%
				for (GeneralShiftDTO shuttleDto : bookingDetails) {
			%>

			<tr>


				<td><%=shuttleDto.getEmployeeId()%></td>
				<td><%=shuttleDto.getEmployeeName()%></td>

				<td><%=OtherFunctions.changeDateFromatddmmyy(shuttleDto
						.getFrom_date())%></td>
				<td><%=shuttleDto.getLogtype()%></td>
				<td><%=shuttleDto.getLogtime()%></td>
				<td><%=shuttleDto.getStatus()%></td>
				<%
					if (shuttleDto.getApproval_req().equals(
								SettingsConstant.NOT_APPROVED)) {
				%>
				<td><input id="modify" type="button" value="Approve"
					class="formbutton"
					onclick='approveRejectBooking("<%=shuttleDto.getBookingId()%>","approve")' />
					&nbsp;&nbsp;&nbsp;</td>
				<%
					}
				%>
			</tr>
			<%
				}
			%>
		</table>
	</form>
</body>
</html>