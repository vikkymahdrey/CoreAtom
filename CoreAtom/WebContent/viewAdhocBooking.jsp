<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
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
<title>View adhoc booking</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script src="js/dateValidation1.js"></script>
<title>Subscription</title>
</head>
<body>

	<script type="text/javascript">
		$(document).ready(function() {
			//$("#travelDate").datepick();	
			$("#startTime")
			.change(
					function() {														
						validateTime($("#startTime").val());
					});
	$("#shiftTime").change(function() {
				validateTime($("#shiftTime").val());										
			});					
		});
		
		
		
		var validationStatus=0;
		function validateForm() {
			validationStatus=0;
			if (validationStatus == 0) {
	if($("#adhocType").val()!="shiftExtension")
		{
				if ($("#orgination").val() == null
						|| $("#orgination").val() == "") {
					alert("Null value");
					return false;
				} else {
					return true;
				}
		}	
		} else {
				alert("validation Error");
				return false;
			}
		}
		function validateTime(selectedTime) {	
			
			
			try{
			var currentDate = document.getElementById("curdate").value;
			
			var currentDateparted=currentDate.split(" ");
			
			var currentDatevar=currentDateparted[0].split("/")[0]+"/"+currentDateparted[0].split("/")[1]+"/"+currentDateparted[0].split("/")[2];
			var dateSelectedVal = CheckDateEqualOrGreaterDatesddmmyyyy(
					$("#travelDate").val(),
					currentDatevar);			
			if (dateSelectedVal == 0) {			
				var bookingCutOff = $(
						"#cutoff")
						.val();
				var bookingCutOffSplit = bookingCutOff
						.split(":");
				var currentTime = (parseInt(currentDateparted[1].split(":")[0]) + parseInt(bookingCutOffSplit[0])) * 60;
				currentTime += parseInt(currentDateparted[1].split(":")[1])
						+ parseInt(bookingCutOffSplit[1]);
				var selectedTimeMnt = parseInt(selectedTime
						.split(":")[0]) * 60;
				selectedTimeMnt += parseInt(selectedTime
						.split(":")[1]);
				if (selectedTimeMnt < currentTime) {					
					validationStatus = 2;
					alert("Time choosen incorrect");
				}

			} else if (dateSelectedVal == -1) {
				validationStatus = 1;
			} else {
			}
			}catch(e)
			{alert(e);}
			}
		function displayModifyDiv(bookingId,cutoff) {		
			window.location.href = "viewAdhocBooking.jsp?bookingId=" + bookingId+"&cutoff="+cutoff;
		}
		function cancelBooking(bookingId,adhoctype) {
			window.location.href = "ApproveRejectAdhoc?bookingId=" + bookingId+"&adhoctype="+adhoctype;
		}
	</script>

	<%
		long empid = 0;

		String employeeId = OtherFunctions.checkUser(session);

		empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<%
		OtherDao ob = null;
		ob = OtherDao.getInstance();
		if (!ob.isRegisterd(employeeId)) {
	%>

	<%
		}
		EmployeeDto empDto = (EmployeeDto) session.getAttribute("userDto");
		//		out.println(ob.getEmployeeDet(empid));
		String roleId=session.getAttribute("roleId").toString();
		ArrayList<AdhocDto> adhoctypes = new AdhocService()
				.getAdhocTypes(empDto.getSite());
		String bookingId = request.getParameter("bookingId");
		String cutoff = request.getParameter("cutoff");
		System.out.println("bookingId" + bookingId);

		ArrayList<AdhocDto> adhocList = new AdhocService()
				.getBookingDetailsForManager(employeeId,roleId);
		AdhocDto bookingDetails = new AdhocService()
				.getBookingDetails(bookingId);
		//System.out.println(adhocList.size() + " here is the size adhoc booking list");
	%>
	<br />
	<h3 style="margin-left: 100px">Adhoc Booking</h3>
	<table>
		<thead>
			<tr>
				<th width="20%">Adhoc Type</th>
				<th>Employee Code</th>
				<th>Employee Name</th>
				<th>Travel date</th>
				<th>Time</th>
				<th>Action</th>
			</tr>
		</thead>
		<%
			for (AdhocDto adhocDto : adhocList) {
		%>

		<tr>
			<td><%=SettingsConstant.getVal(adhocDto.getAdhocType())%></td>
						<td><%=adhocDto.getEmployeeCode()%></td>
									<td><%=adhocDto.getEmployeeName()%></td>									

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
					if (new AdhocService().checkIsRightToModify(employeeId,
							adhocDto.getAdhocType(), adhocDto.getBookedFor(),
							empDto.getSite(), empDto.getProjectUnit(),roleId)) {
			%>
			<%
				for (AdhocDto adhoctypeDto : adhoctypes) {
					
					 if(adhoctypeDto.getAdhocType().equalsIgnoreCase(SettingsConstant.SHIFT_EXTENSTION))
						 
					{ //System.out.println("in if " +  adhoctypeDto.getAdhocType() + " " + adhoctypeDto.getPickupDrop());
						 String flag ="";
						 if(adhoctypeDto.getPickupDrop().equalsIgnoreCase("pick up")&& adhoctypeDto.getPickupDrop() != null)
						 {
							 flag = "IN";
						 }
						 else
						 {
							 flag = "OUT";
						 }
					
					
							if (adhoctypeDto.getAdhocType().equals(
									adhocDto.getAdhocType()) && adhocDto.getPickupDrop().equalsIgnoreCase(flag)) {
								//System.out.println(adhoctypeDto.getId() + "here " + adhocDto.getId() +adhocDto.getPickupDrop() + flag);
			%>
			<td>
			<%if(OtherFunctions.isTimePast(adhocDto.getTravelDate(),adhoctypeDto.getRequestCutoff(),adhocDto.getStartTime())){ %>
			<input id="modify" type="button" value="Modify"
				onclick='displayModifyDiv("<%=adhocDto.getBookingId()%>","<%=adhoctypeDto.getRequestCutoff()%>")'
				class="formbutton" /> &nbsp;&nbsp;&nbsp; 
				<%}if(OtherFunctions.isTimePast(adhocDto.getTravelDate(),adhoctypeDto.getCancelCutoff(),adhocDto.getStartTime())){ %>
				<input id="cancel"
				type="button" value="Cancel" class="formbutton"
				onclick='cancelBooking(<%=adhocDto.getBookingId()%>,"<%=adhocDto.getAdhocType()%>")' />
				<%} %>
				</td>

			<%
				}
			    } 
					 else
					 { 
					 if (adhoctypeDto.getAdhocType().equals(
								adhocDto.getAdhocType())){%><td>
						<%if(OtherFunctions.isTimePast(adhocDto.getTravelDate(),adhoctypeDto.getRequestCutoff(),adhocDto.getStartTime())){ %>
						<input id="modify" type="button" value="Modify"
							onclick='displayModifyDiv("<%=adhocDto.getBookingId()%>","<%=adhoctypeDto.getRequestCutoff()%>")'
							class="formbutton" /> &nbsp;&nbsp;&nbsp; 
							<%}if(OtherFunctions.isTimePast(adhocDto.getTravelDate(),adhoctypeDto.getCancelCutoff(),adhocDto.getStartTime())){ %>
							<input id="cancel"
							type="button" value="Cancel" class="formbutton"
							onclick='cancelBooking(<%=adhocDto.getBookingId()%>,"<%=adhocDto.getAdhocType()%>")' />
							<%} %>
							</td>
						 
					<%} }
				}
					}
			%>
		</tr>
		<%
			}
		%>
	</table>

	<%
	try{
		if (bookingId != null) {
	%>
	<h5>Modify Booking</h5>
	<form method="POST" action="AdhocBooking">
		<table>
<tr>
				<td width="20%">Emp Code</td>
				<td><%=bookingDetails.getEmployeeCode() %></td>
				</tr>
				<tr>
				<td width="20%">Emp Name</td>
				<td><%=bookingDetails.getEmployeeName() %></td>
				</tr>
	
			<%
				if (bookingDetails.getAdhocType().equalsIgnoreCase(
							SettingsConstant.SHIFT_EXTENSTION)) {
			%>
			<tr>
				<td width="20%">Date</td>
				<td><input type="text" id="travelDate" name="travelDate"
					value="<%=OtherFunctions
							.changeDateFromatddmmyy(bookingDetails
									.getTravelDate())%>"
					readonly="readonly" /></td>
			</tr>
			<tr>
				<td width="20%">Pick Up/Drop</td>
				<td><input type="text" name="pickdrop"
					value="<%=bookingDetails.getPickupDrop()%>" readonly="readonly" /></td>
				<%
					ArrayList<LogTimeDto> logdtos = new LogTimeDao()
									.getAdhocShift(bookingDetails.getPickupDrop());
				%>
			</tr>
			<tr>
				<td>Time</td>
				<td><select name="shiftTime" id="shiftTime">
						<%
							for (LogTimeDto logdto : logdtos) {
										if (logdto.getLogTime().equals(
												bookingDetails.getStartTime())) {
						%>
						<option selected="selected" value="<%=logdto.getLogTime()%>"><%=logdto.getLogTime()%></option>
						<%
							} else {
						%>
						<option value="<%=logdto.getLogTime()%>"><%=logdto.getLogTime()%></option>
						<%
							}
									}
						%>
				</select></td>
			</tr>

			<%
				} else {
			%>
			<tr>
				<td width="20%">Start Time</td>
				<td><select name="startTime" id="startTime">
						<%=OtherFunctions
							.FullTimeInIntervalOptionsWithSelect(bookingDetails
									.getStartTime())%>
				</select></td>
				<td>End Time</td>
				<td><select name="endTime" id="endTime">
						<%=OtherFunctions
							.FullTimeInIntervalOptionsWithSelect(bookingDetails
									.getEndTime())%>
				</select></td>
			</tr>
			<tr>
				<td width="20%">Orgination</td>
				<td><input type="text" name="orgination" id="orgination"
					value="<%=bookingDetails.getOrgination()%>" /></td>
				<td>Destination</td>
				<td><input type="text" name="destination" id="destination"
					value="<%=bookingDetails.getDestination()%>" /></td>
			</tr>
			<tr>
				<td>Reason</td>
				<td><input type="text" name="reason" id="reason"
					value="<%=bookingDetails.getReason()%>" /></td>
				<td>Comment</td>
				<td><textarea name="comment" id="comment"><%=bookingDetails.getComment()%></textarea></td>
			</tr>
			<%
				}
			%>
			<tr>
			
				<td>
				<input type="hidden" id="curdate"
					value="<%=OtherFunctions.getTodaysDate()%>" />
				<input type="hidden" name="adhocType"
					value="<%=bookingDetails.getAdhocType()%>" /> <input type="hidden"
					name="bookingId" value="<%=bookingId%>" />
					<input type="hidden" id="cutoff"
					name="cutoff" value="<%=cutoff%>" />
					
					</td>
				<td><input type="submit" value="Update" class="formbutton" /></td>
			</tr>
		</table>
	</form>
	<%
		}
	}catch(Exception e)
	{
	System.out.println("Errror"+e);	
	}
	%>
		<%@include file="Footer.jsp"%>
</body>
</html>