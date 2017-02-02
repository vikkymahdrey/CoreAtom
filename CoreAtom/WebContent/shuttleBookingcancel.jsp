<%@page import="com.agiledge.atom.dto.EmployeeSubscriptionDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.ShuttleServcie"%>
<%@page contentType="text/html" pageEncoding="UTF-8"
	errorPage="error.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Subscription</title>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script src="js/dateValidation.js"></script>


<script type="text/javascript">
	function displayaddress() {
		document.getElementById("addresstd").innerHTML = document
				.getElementById("address").innerHTML;
	}
</script>
</head>
<body onload="displayaddress()">

	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>

	<div id="body">
		<div class="content">
			<%
				ArrayList<EmployeeSubscriptionDto> bookingDetails = new ShuttleServcie()
						.getShuttleBookingDatiles(employeeId);
			%>
			<table>
				<%
					for (EmployeeSubscriptionDto dto : bookingDetails) {
				%>
				<tr>
					<td>
					<td>
					<td><%=dto.getEmployeeID()%></td>
					<td><%=dto.getSubscriptionFromDate()%></td>
					<td><%=dto.getRouteName()%></td>
					<td><%=dto.getEmployee().getDisplayName()%></td>
					<td><%=dto.getSubscriptionStatus()%></td>
					<td><%=dto.getApprovalStatus()%></td>
					<td><%=dto.getIN_OUT()%></td>
					<td><%=dto.getShiftIn()%></td>
					<td><%=dto.getApl().getArea() + " -->"
						+ dto.getApl().getPlace() + " -->"
						+ dto.getApl().getLandMark()%></td>
						<td><input type="button" value="cancel"/></td>
					<%if(dto.getWaitingList()>=0&&dto.getSubscriptionStatus().equals("wl")&&dto.getReConfirmDate()!=null&&!dto.getReConfirmDate().equals("")&&!dto.getReConfirmDate().equals("null")) {%>
					<td><input type="button" value="confirm"/></td>
					<%}%>					
				</tr>
				<%
					}
				%>
			</table>

			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
