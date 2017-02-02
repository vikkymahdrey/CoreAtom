<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.service.StsService"%>
<%@page import="com.agiledge.atom.dto.EmergencyDto"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.nfl.com" prefix="disp"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="icon" href="images/agile.png" type="image/x-icon" />
<title>Special Transport Approve</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
	function approval(status,id) {
		try {
			var xmlhttp;
			var empid=document.getElementById("empid").value;
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					var resp = xmlhttp.responseText;
					alert(resp);
					location.reload();
				}
			}
			xmlhttp.open("POST", "ApproveStsRequest?status="
					+ status+"&id="+id+"&approvedby="+empid, true);
			xmlhttp.send();
		} catch (e) {

			alert(e);
		}

	}
</script>
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
		List<EmergencyDto> list=null;
		String roleid=session.getAttribute("roleId").toString();
		if(roleid.equals("4")){
			 list= new StsService()
			.getStsEmployeesForApprovalUnderTL(employeeId);
		}else{
			 list = new StsService()
				.getStsEmployeesForApproval();
		}
	%>
	<div id="body">
		<div class="content">
<input type="hidden" id="empid" name="empid" value="<%= employeeId%>"/>

			<h3>Special Transport Approval</h3>
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
					<input type="button" class="formbutton" value="APPROVE"
						onclick="location.href='stsVehicleEntry.jsp?id=${row.tripcode}'" />&nbsp;&nbsp;&nbsp;&nbsp;<input
						type="button" class="formbutton" value="REJECT"
						onclick="approval('rejected','${row.tripcode}')" />
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