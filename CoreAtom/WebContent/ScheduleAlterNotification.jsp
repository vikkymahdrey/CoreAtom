<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dto.AdhocDto"%>
<%@page import="com.agiledge.atom.dto.ScheduleAlterDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.ScheduleAlterDao"%>
<%@page import="com.agiledge.atom.service.AdhocService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Schedule Alter Requests</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<style type="text/css">
@import "css/jquery.datepick.css";

.bordered {
	width: 70%;
	border-style: solid;
}
</style>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
	function respondRequest(id, date,login,logout,i, status) {
		var ApprovedBy = $("#ApprovedBy").val();
		try {
			
			var xmlhttp;
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					var result= xmlhttp.responseText;
					if(result>0){
					$('#'+i).hide();
					alert(status+" sccessfully");
					document.getElementById(id+i).innerHTML=status;
					}
				}
			}
			xmlhttp.open("POST", "ApproveScheduleAlter?ApprovedBy=" + ApprovedBy+"&date="+date+"&scheduleId="+id+"&status="+status+"&login="+login+"&logout="+logout, true);
			xmlhttp.send();
		} catch (e) {

			alert("Failed!");
		}
	}
</script>



<title>Schedule Alter Requests</title>
</head>
<body>
	<%
		long empid = 0;

		String employeeId = OtherFunctions.checkUser(session);

		empid = Long.parseLong(employeeId);
	%>
	<%@ include file="Header.jsp"%>
	<%
		String roleId = session.getAttribute("roleId").toString();
		String role = session.getAttribute("role").toString();

		ArrayList<ScheduleAlterDto> alterList = new ScheduleAlterDao()
				.getPendingAlterRequest(role);
	%>
	<br />
	<h3 style="margin-left: 100px">Schedule Alter Requests</h3>
	<%
		if (alterList != null && alterList.size() > 0) {
	%>
	<form name="adhocApprrovereject">
		<input type="hidden" id="ApprovedBy" name="ApprovedBy" value="<%=employeeId %>" />
		<table>
			<thead>
				<tr>
					<th width="20%">Employee Name</th>
					<th>Date</th>
					<th>Log In</th>
					<th>Log Out</th>
					<th>Status</th>
					<th></th>
				</tr>
			</thead>
			<%
			int i=0;
				for (ScheduleAlterDto dto : alterList) {
			%>
			<tr>
				<td><%=dto.getScheduledfor()%></td>
				<td><%=dto.getDate()%></td>
				<td><%=dto.getLoginTime()%></td>
				<td><%=dto.getLogoutTime()%></td>
				<td id="<%=dto.getScheduleId()%><%=i%>"><%=dto.getStatus()%></td>
				<td id="<%=i%>"><input type="button"
					class="formbutton" value="Approve"
					onclick='respondRequest("<%=dto.getScheduleId()%>","<%=dto.getDate()%>","<%=dto.getLoginTime()%>","<%=dto.getLogoutTime()%>","<%=i%>","Approved");'>
					<input type="button" class="formbutton" value="Reject"
					onclick='respondRequest("<%=dto.getScheduleId()%>","<%=dto.getDate()%>",<%=dto.getLoginTime()%>,"<%=dto.getLogoutTime()%>","<%=i%>","Rejected");'></td>
			</tr>
			<%
			i++;
				}
			%>

		</table>
	</form>
	<%
		} else {
			out.print("<p>No request</p>");
		}
	%>
</body>
</html>