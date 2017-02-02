<%@page import="com.itextpdf.text.log.SysoLogger"%>
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
<title>Insert title here</title>
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
			/*			
						$(".adhoctypes")
								.click(
										function() {
											if ($(this).val() == "shiftExtension") {
												if ($(this).prop('checked')) {
													$(this)
															.parent()
															.next()
															.append(
																	"<input id='effectiveDate' class='effectiveDate' type='text'>");
													$("#effectiveDate")
															.datepick();
												} else {
													$(this).parent().next()
															.html("");
												}
											}

										});
			 */

		});

		function approveRejectBooking(bookingId, appReject) {
			window.location.href = "ApproveRejectAdhoc?bookingId=" + bookingId
					+ "&appReject=" + appReject+"&src=transport";
		}
	</script>

	<%
		long empid = 0;

		String employeeId = OtherFunctions.checkUser(session);

		empid = Long.parseLong(employeeId);
	%>
	<%@ include file="Header.jsp"%>
	<%
	String roleId=session.getAttribute("roleId").toString();

		ArrayList<AdhocDto> bookingDetails = new AdhocService()
				.getBookingDetailsForTransportManager();
	%>
	<br />
	<h3 style="margin-left: 100px">Adhoc Requests</h3>
	<%if(bookingDetails!=null && bookingDetails.size()>0){
		 %>
	<form name="adhocApprrovereject" action="ApproveRejectAdhoc"
		method="post">
		
		<table>
			<thead>
				<tr>
					<th width="20%">Adhoc Type</th>
					<th>Employee Code</th>
					<th>Employee Name</th>
					<th>Travel date</th>
					<th>Time</th>
					<th>Status</th>
					<th></th>
				</tr>
			</thead>
			<%
				for (AdhocDto adhocDto : bookingDetails) {
					
			%>

			<tr>

				<td><%=SettingsConstant.getVal(adhocDto.getAdhocType())%></td>
			<td><%=adhocDto.getEmployeeCode() %></td>
			<td><%=adhocDto.getEmployeeName() %></td>

				<td><%=OtherFunctions.changeDateFromatddmmyy(adhocDto
						.getTravelDate())%></td>
				<%
					if (adhocDto.getAdhocType().equalsIgnoreCase(
								SettingsConstant.SHIFT_EXTENSTION)) {
				%>
				<td><%=adhocDto.getPickupDrop()%>-<%=adhocDto.getShiftTime()%></td>
				<%
					} else {
				%>
				<td><%=adhocDto.getStartTime()%></td>
				<%
					}
				%>
				<td><%=adhocDto.getStatus()%></td>
				<%
					if (new AdhocService().checkIsRightToApprove(employeeId,
								adhocDto.getAdhocType(),adhocDto.getBookedFor() ,adhocDto.getSiteId(),adhocDto.getProjectUnit(),roleId)) {
				%>
				<td>
				<%
				
				if(adhocDto.getStatus()==null||!adhocDto.getStatus().equalsIgnoreCase("approved")){%>
				<input id="approve" type="button" value="Approve"
					class="formbutton"
					onclick='approveRejectBooking("<%=adhocDto.getBookingId()%>","approve")' />
					<%} %>
					&nbsp;&nbsp;&nbsp;
					<%if(adhocDto.getStatus()==null||!adhocDto.getStatus().equalsIgnoreCase("Rejected")){%>
					<input id="reject" type="button" value="Reject"
					class="formbutton"
					onclick='approveRejectBooking("<%=adhocDto.getBookingId()%>","reject")' />
					<%} %>
					</td>

				<%
					}
				%>
			</tr>
			<%
				}
			%>

		</table>
	</form>
	<%}else
		{
		out.print("<p>No booking request</p>");
		}%>
</body>
</html>