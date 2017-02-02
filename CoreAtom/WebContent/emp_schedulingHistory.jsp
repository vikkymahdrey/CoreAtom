<%--
    Document   : emp_schedulingHistory
    Created on : Oct 21, 2012, 10:09PM
    Author     : Noufal C C
--%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@page import="com.agiledge.atom.dao.SchedulingDao"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.service.SchedulingService"%>
<%@page import="com.agiledge.atom.dto.ScheduledEmpDto"%>
<%@page import="com.agiledge.atom.dao.ScheduledEmpDao"%>
<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@page import="com.agiledge.atom.dto.EmployeeSubscriptionDto"%>
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.service.APLService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.lang.Exception"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="com.agiledge.atom.dao.APLDao"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"
	errorPage="error.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Scheduling History</title>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script src="js/dateValidation.js"></script>

<script type="text/javascript">
	$(function() {

		$('input[name=fromDate]').datepick();

		$('input[name=toDate]').datepick();

	});
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
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<%		
		OtherDao ob = null;
		ob = OtherDao.getInstance();
	%>



	<div id="body">
		<div class="content">



			<form action="emp_schedulingHistory.jsp" name="schedulingHistoryForm">


				<hr />
				<h3>Employee Scheduled History</h3>
				<%
					String subscriptionID = "";
					try {
						subscriptionID = request.getParameter("subid");
						if (subscriptionID == null || subscriptionID.equals("")) {
							subscriptionID = request.getParameter("subscriptionID");
						}

					} catch (Exception ex) {
						subscriptionID = request.getParameter("subscriptionID");
					}

					String fromDate = "";
					String toDate = "";
					fromDate = request.getParameter("fromDate");
					toDate = request.getParameter("toDate");
					if ((fromDate == null || fromDate.equals(""))
							&& (toDate == null || toDate.equals(""))) {

						try {
						System.out.println("Subscriotion "+subscriptionID);
						
							fromDate = OtherFunctions
									.changeDateFromat(new SchedulingDao()
											.getFirstSchedulingFromDate(subscriptionID));
						
							toDate = OtherFunctions
									.changeDateFromat(new SchedulingDao()
											.getLastSchedulingToDate(subscriptionID));


						} catch (Exception e) {
							  //    System.out.println("---------------------"+e);
							toDate = OtherFunctions.changeDateFromat(new Date());
							Calendar cal = Calendar.getInstance();
							cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 30);
							fromDate = OtherFunctions.changeDateFromat(cal.getTime());

						}

					}

					else {
						toDate = OtherFunctions.changeDateFromatToIso(toDate);
						fromDate = OtherFunctions.changeDateFromatToIso(fromDate);
						//    fromDate=OtherFunctions.changeDateFromatToddmmyyyy(fromDate);
						//   toDate=OtherFunctions.changeDateFromatToddmmyyyy(toDate);
						System.out.println("FromDate : " + fromDate + "\n To Date : "
								+ toDate);

					}
				%>

				<div>


					<table>
						<tr>
							<td>From</td>
							<td><input type="hidden" name="subscriptionID"
								value="<%=subscriptionID%>" /> <input type="text"
								name="fromDate" readonly
								value="<%=OtherFunctions.changeDateFromatToddmmyyyy(fromDate)%>" /></td>
						</tr>
						<tr>
							<td>To</td>
							<td><input type="text" name="toDate" readonly
								value="<%=OtherFunctions.changeDateFromatToddmmyyyy(toDate)%>" /></td>

						</tr>
						<tr>

							<td colspan="2" align="center"><input type="submit"
								class="formbutton" value="Search" name="Search" /> <input
								type="button" class="formbutton"
								onclick="javascript:history.go(-1);" value="Back" /></td>

						</tr>
					</table>

				</div>
				<%
					List<ScheduledEmpDto> scheduledList = null;

					scheduledList = new SchedulingService().getScheduledHistory(
							subscriptionID, fromDate, toDate);

					if (scheduledList.size() > 0) {
				%>




				<table class="dataTable" width="70%">

					<thead>
						<tr>

							<th>Effective From(dd/mm/yyyy)</th>
							<th>To(dd/mm/yyyy)</th>
							<th>Login</th>
							<th>Logout</th>

							<th>Status</th>
							<th>Audit Log</th>
						</tr>
					</thead>
					<tbody>
						<%
							ScheduledEmpDto dto = null;
								for (int i = scheduledList.size() - 1; i >= 0; i--) {
									dto = scheduledList.get(i);
									String rId=dto.getScheduleId();
									if(dto.getScheduleId()==null)
									{
										rId=dto.getScheduleAlterId();
									}
									
						%>

						<tr>

							<td><%=OtherFunctions.changeDateFromatToddmmyyyy(dto
							.getFrom_date())%></td>
							<td><%=OtherFunctions.changeDateFromatToddmmyyyy(dto
							.getTo_date())%></td>
							<td><%=dto.getLoginTime()%></td>
							<td><%=dto.getLogoutTime()%></td>
							<td><%=dto.getStatus()%></td>
							<td><input type="button" class="formbutton"
								onclick="showAuditLog(<%=rId %>,'<%=AuditLogConstants.SCHEDULE_EMP%>');"
								value="Audit Log" /></td>
						</tr>
						<%
							}
						%>
					</tbody>
				</table>

				<%
					}
				%>


			</form>


			<%@include file="Footer.jsp"%>

		</div>
	</div>

</body>
</html>
