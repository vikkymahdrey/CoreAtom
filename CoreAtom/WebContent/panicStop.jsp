<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.TripDetailsDao"%>
<%@page import="com.agiledge.atom.dao.PanicDao"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Trace Vehicle</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>

</head>
<body>
	<%
		String tripId = "";
		tripId = request.getParameter("tripId");
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<%
		}
	%>



	<hr />
	</div>
	<div id="body">
		<div class="content">
			<p>&nbsp;</p>
			<h3>Panic Trips</h3>



			<%
				ArrayList<TripDetailsDto> dtos = new PanicDao().getPanicTripDetails();
				try {
					for (TripDetailsDto dto : dtos) {
			%>
			<form name="panicActionStop" action="PanicAlarmAction" method="post">
				<input type="hidden" value="stop" name="stop" /> <input
					type="hidden" value="<%=dto.getId()%>" id="tripId" name="tripId">
				<table align="center">
					<tr>
						<td colspan="4" align="center"><b>Vehicle <%=dto.getVehicleNo()%>
								On <%=dto.getTrip_time()%></b></td>
					</tr>
					<tr>
						<td colspan="4" align="center"><b>Driver Details</b></td>
					</tr>
					<tr>
					<td colspan="4" align="center">Name:<%=dto.getDriverName()%></td>
					</tr>
					<tr>
					<td colspan="4" align="center">Contact:<%=dto.getDriverContact()%></td>
						
					</tr>
					<tr>
						<td colspan="4" align="center"><b>Vendor Details</b></td>
					</tr>
					<tr>
						<td colspan="4" align="center">Name:<%=dto.getVendorName()%></td>
						
					</tr>
					<tr>
					<td colspan="4" align="center">Contact:<%=dto.getVendorContact()%></td>
					</tr>
					
					<tr>
						<td colspan="4" align="center"><b>Escort Details</b></td>
					</tr>
					<%
						if (dto.getEscort() != null
										&& !dto.getEscort().equals("null")
										&& !dto.getEscort().equals("")) {
					%>
					<tr>
			      		<td colspan="4" align="center">Name:<%=dto.getEscortName()%></td>
					</tr>
					<tr>
						<td colspan="4" align="center">Clock:<%=dto.getEscortclock()%></td>
						
					</tr>
					<tr>
					<td colspan="4" align="center">Contact:<%=dto.getEscortContact()%></td>
						
					</tr>
					<%
						} else {
					%>
					<tr>
						<td align="center" colspan="4">No escort In the Vehicle</td>
					</tr>
					<%
						}
					%>
					<tr>
						<td colspan="4" align="center"></td>
					</tr>
					<tr>
						<td colspan="4" align="center"><b>Employees Details In
								the Vehicle</b></td>
					</tr>
					<tr>
						<td><b>Name</b></td>
						<td><b>Code</b></td>
						<td><b>Gender</b></td>
						<td><b>Contact</b></td>
					</tr>

					<%
						for (TripDetailsChildDto childDto : dto
										.getTripDetailsChildDtoList()) {
					%>
					<tr>
						<td><%=childDto.getEmployeeName()%></td>
						<td><%=childDto.getEmployeeId()%></td>
						<td><%=childDto.getGender()%></td>
						<td><%=childDto.getContactNumber()%></td>
					</tr>
					<%
						}
					System.out.println("Herer");
				
					%>
					<tr>
						<td colspan="4" align="center"><b>Action Take details</b></td>
					</tr>
					<tr>
						
						<td colspan="4" align="center">PrimaryAction Taken By:<%=dto.getPanicdto().getPrimaryActiontakenByName()%></td>
					</tr>
					<tr>
						<td colspan="4" align="center">Cauase Of Alarm<%=dto.getPanicdto().getAlarmCause()%></td>
					</tr>
					<tr>
						<td colspan="4" align="center">Action Description:<%=dto.getPanicdto().getPrimaryAction()%></td>
					</tr>
					<tr>
						<td><input type="hidden"
							value="<%=dto.getPanicdto().getId()%>" name="panicid" /></td>
						<td colspan="2" align="center"><input type="submit" value="Approve"
							class="formbutton">&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td></td>
					</tr>

				</table>

			</form>
			<%
				}
			%>
			<p>&nbsp;&nbsp;&nbsp;&nbsp;</p>
			<%
				} catch (Exception e) {
					System.out.println("Error" + e);
				}
			%>

		</div>
	</div>
	<%@include file="Footer.jsp"%>
</body>
</html>