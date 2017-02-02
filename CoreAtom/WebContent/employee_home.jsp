<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.service.AdhocService"%>
<%@page import="com.agiledge.atom.dto.AdhocDto"%>
<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dto.EmployeeSubscriptionDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="com.agiledge.atom.dto.ScheduledEmpDto"%>
<%@page import="com.agiledge.atom.service.SchedulingService"%>
<%@page import="com.agiledge.atom.service.TripDetailsService"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.SchedulingDao"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="error.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<title>Employee Home</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />


<script type="text/javascript" src="js/jquery-latest.js"></script>
<script>
	$(document).ready(function() {

		setActiveMenuItem();
	});

	function setActiveMenuItem() {
		var url = window.location.pathname;
		var filename = url.substring(url.lastIndexOf('/') + 1);
		//  $("li[class=active']").removeAttr("active");

		$("a[href='" + filename + "']").parent().attr("class", "active");
		$("a[href='" + filename + "']").parent().parent().parent('li').attr(
				"class", "active");

	}
	function showAuditLog(relatedId, moduleName) {
		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		var size = "height=450,width=900,top=200,left=300," + params;
		var url = "ShowAuditLog.jsp?relatedNodeId=" + relatedId
				+ "&moduleName=" + moduleName;
		newwindow = window.open(url, 'AuditLog', size);

		if (window.focus) {
			newwindow.focus();
		}
	}
	function displayAddress()
	{	
		document.getElementById("address").style.display = "block";
		var data="<table style='width: 90%'><tr><td align='right' width='20%'> <b>Address </b>" + "" + "</td> " + "<td width='1%'> "
		+ "<b>: </b>" + "</td> "
		+ " <td  width='20%' align='left'> "
		+ document.getElementById("address").innerHTML + "</td> "
		+ " <td width='20%'></td><td width='20%'></td>" 
		+" <td width='19%'></td> "
		+ "</tr></table>";		
		document.getElementById("address").innerHTML=data;
		
		
	}
</script>

</head>
<body onload="displayAddress()">
	<%
		long empid = 0;

		String employeeId = OtherFunctions.checkUser(session);

		empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<%
		OtherDao ob = null;
		ob = OtherDao.getInstance();
		ArrayList<TripDetailsDto> tripDetailslist = new TripDetailsService()
				.getEmployeeTripSheet(employeeId);
	%>



	<div id="body">
		<div id="content">



			<%
				out.println(ob.getEmployeeDet(empid));
			String roleId=session.getAttribute("roleId").toString();
			String role=session.getAttribute("role").toString();

			
		%>	
			
			
			 <%
				String[] apl=new EmployeeDao().getApl(empid);
			%>
			 <%if (apl[0]!=null){  %>
			
				<p align="left" style="padding-left:11%" > 	 <b>Area : </b><%=apl[0] %><b>  Place : </b><%=apl[1] %><b>  LandMark : </b><%=apl[2] %>	 </p>
	<%} %>
</div>
			
			<% 
			
			int AdhocNotificationCount[] = new AdhocService()
					.getAdhocNotificationCount(employeeId, roleId,role);


			
			if(AdhocNotificationCount[0]!=0){
%>

<p><a href="viewNotificationForTransport.jsp"> (<%=AdhocNotificationCount[0] %>) Adhoc Bookings</a>

</p>
	
<%		}
			if(AdhocNotificationCount[1]==0){
%>
<p>You have (0) Notifications</p>
<%}else
{%>
<p><a href="viewNotification.jsp">You have (<%=AdhocNotificationCount[1] %>) Notifications</a> </p>	
<%}

					
				if (tripDetailslist.size() > 0) {
			%>

			<p>
				<h3><%=OtherFunctions
						.changeDateFromatToddmmyyyy(tripDetailslist.get(0)
								.getTrip_date())%>
					Routing information
				</h3>
				<hr></hr>
			</p>

			<p>
				<b> LOG <%=tripDetailslist.get(0).getTrip_log()%>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					Vehicle :<%=tripDetailslist.get(0).getVehicle_type()%> &nbsp;&nbsp;
					Time : <%=tripDetailslist.get(0).getTrip_time()%>
				</b>
			</p>
			<table style="width: 80%">
				<tr>
					<th>Employee</th>
					<th>Area</th>
					<th>Place</th>
					<th>landmark</th>
				</tr>
				<%
					for (TripDetailsChildDto tripDetailsChildDto : tripDetailslist
								.get(0).getTripDetailsChildDtoList()) {
				%>
				<tr>
					<td><%=tripDetailsChildDto.getEmployeeName()%></td>
					<td><%=tripDetailsChildDto.getArea()%></td>
					<td><%=tripDetailsChildDto.getPlace()%></td>
					<td><%=tripDetailsChildDto.getLandmark()%></td>
				</tr>
				<%
					}
				%>
			</table>
			<%
				if (tripDetailslist.size() > 1) {
			%>

			<p>&nbsp;</p>
			<h3><%=OtherFunctions
							.changeDateFromatToddmmyyyy(tripDetailslist.get(1)
									.getTrip_date())%>
				Routing information
			</h3>
			<p>
				<b> LOG <%=tripDetailslist.get(1).getTrip_log()%>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					Vehicle :<%=tripDetailslist.get(1).getVehicle_type()%></b> Time :
				<%=tripDetailslist.get(1).getTrip_time()%>
			</p>
			<table style="width: 80%">
				<tr>
					<th>Employee</th>
					<th>Area</th>
					<th>Place</th>
					<th>landmark</th>
				</tr>
				<%
					for (TripDetailsChildDto tripDetailsChildDto : tripDetailslist
									.get(1).getTripDetailsChildDtoList()) {
				%>


				<tr>
					<td><%=tripDetailsChildDto.getEmployeeName()%></td>
					<td><%=tripDetailsChildDto.getArea()%></td>
					<td><%=tripDetailsChildDto.getPlace()%></td>
					<td><%=tripDetailsChildDto.getLandmark()%></td>
				</tr>
				<%
					}
				%>
			</table>
			<%
				}
				}
			%>
			<br />
			<hr />
			<div>
				<%
					try {
						EmployeeSubscriptionDto subscriptionDto = new EmployeeSubscriptionService()
								.getEmployeeSubscriptionDetails("" + empid);

						if (subscriptionDto != null) {

							String subscriptionID = subscriptionDto.getSubscriptionID();
							String fromDate = "";
							fromDate = OtherFunctions
									.changeDateFromat(new SchedulingDao()
											.getFirstSchedulingFromDate(subscriptionID));
							String toDate = "";
							toDate = OtherFunctions
									.changeDateFromat(new SchedulingDao()
											.getLastSchedulingToDate(subscriptionID));

							List<ScheduledEmpDto> scheduledList = null;

							scheduledList = new SchedulingService()
									.getScheduledHistory(subscriptionID, fromDate,
											toDate);

							if (scheduledList.size() > 0) {
				%>


				<h3>Active Schedules</h3>
				<hr />

				<table class="dataTable" width="70%">
					<thead>
						<tr>
							<th>Effective From(dd/mm/yyyy)</th>
							<th>To(dd/mm/yyyy)</th>
							<th>Login</th>
							<th>Logout</th>
							<th>Status</th>
						</tr>
					</thead>
					<tbody>
						<%
							ScheduledEmpDto dto = null;
										for (int i = scheduledList.size() - 1; i >= 0; i--) {
											dto = scheduledList.get(i);
											String rId = dto.getScheduleId();
											if (dto.getScheduleId() == null) {
												rId = dto.getScheduleAlterId();
											}
						%>

						<tr>
							<td><%=OtherFunctions
									.changeDateFromatToddmmyyyy(dto
											.getFrom_date())%></td>
							<td><%=OtherFunctions
									.changeDateFromatToddmmyyyy(dto
											.getTo_date())%></td>
							<td><%=dto.getLoginTime()%></td>
							<td><%=dto.getLogoutTime()%></td>
							<td><%=dto.getStatus()%></td>
						</tr>
						<%
							}
						%>
					</tbody>
				</table>

				<%
					}

						}
					} catch (Exception e) {
						System.out.println("Data missing in home page like scheduling/Routing : " + e);
					}
				%>

			</div>
			<br/>
			<br/>
			<br/>
			<br/>
			<br/>
			<br/>
			<br/>
			<br/>
			<br/>
			<br/>
			<br/>
			<%@include file="Footer.jsp"%>
		</div>

	</div>





</body>
</html>
