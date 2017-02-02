<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.service.ShuttleSocketService"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Employee Schedules</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#checkAllEmployees').click(function(event) { //on click 
			if (this.checked) { // check select status
				$('.checkEmployee').each(function() { //loop through each checkbox
					this.checked = true; //select all checkboxes with class "checkbox1"               
				});
			} else {
				$('.checkEmployee').each(function() { //loop through each checkbox
					this.checked = false; //deselect all checkboxes with class "checkbox1"                       
				});
			}
		});

	});
	function searchForm() {
		document.siteSearchForm.action = "displayTrippingSchedule.jsp";
		document.siteSearchForm.submit();
		return false;
	}
</script>
</head>
<body>
	<%
		long empid = 0;

		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
	%>
	<%@ include file="Header.jsp"%>
	<%
		String site = request.getParameter("site");
		long actEmpId = empid;
		if (session.getAttribute("delegatedId") != null) {
			employeeId = session.getAttribute("delegatedId").toString();
			empid = Long.parseLong(employeeId);
		}
		ArrayList<EmployeeDto> emplist = null;
		int serialNo = 1;
		if (site != null) {
			emplist = new ShuttleSocketService()
					.getEmployeeTrippingDetails(site);
		}
	%>

	<div id="body">
		<div class="content">
			<form name="siteSearchForm">
				<table style="width: 40%; border: 0px none;">
					<tr>
						<td width="20%">&nbsp;&nbsp;Site</td>
						<td>
							<%
								List<SiteDto> siteList = new SiteService().getSites();
							%> <select name="site" id="site" onchange="return searchForm();">
								<option value="">--select--</option>
								<%
									for (SiteDto dto : siteList) {
								%>

								<option value="<%=dto.getId()%>">
									<%=dto.getName()%>
								</option>


								<%
									}
								%>
						</select>
						</td>
					</tr>
				</table>
			</form>
			<form method="post" action="addShuttleSchedule.jsp"
				onsubmit="return Validate()">
				<%
					if (emplist != null && emplist.size() > 0) {
				%>
				<table
					style="width: 100%; border: 0px none; padding-right: 10%; padding-left: 10%;">
					<tr>
						<th width="5%"><input type="checkbox" class="selectAll"
							id="checkAllEmployees" /></th>
						<th align="center" width="20%"><strong><strong>&nbsp;PersonnelNo&nbsp;</strong></strong></th>
						<th width="20%"><strong><strong>&nbsp;Employee&nbsp;</strong></strong></th>
						<th align="center"><strong><strong>&nbsp;Log-In
									Time&nbsp;</strong></strong></th>
						<th align="center"><strong><strong>&nbsp;Log-Out
									Time&nbsp;</strong></strong></th>
						<th align="center"><strong><strong>&nbsp;Route-IN&nbsp;&nbsp;</strong></strong></th>
						<th align="center"><strong><strong>&nbsp;Route-OUT&nbsp;&nbsp;</strong></strong></th>
					</tr>
					<%
						for (EmployeeDto dto : emplist) {
					%>
					<tr id="<%=serialNo%>">
						<td><input class="checkEmployee" size="1" type="checkbox"
							id="employeecheck<%=serialNo%>" name="empid"
							value="<%=dto.getEmployeeID()%>" /></td>
						<td align="center"><%=dto.getPersonnelNo()%></td>
						<td><%=dto.getDisplayName()%></td>
						<td align="center"><%=dto.getLogin()%></td>
						<td align="center"><%=dto.getLogout()%></td>
						<td align="center"><%=dto.getinroute()%></td>
						<td align="center"><%=dto.getOutroute()%></td>
					</tr>
					<%
						serialNo++;
							}
					%>
					<tr>
						<td colspan="5" align="center"><input type="submit"
							value="Alter" class="formbutton" name="submit"
							style="width: 70px" onclick="return Validate();">&nbsp;&nbsp;&nbsp;&nbsp;<input
							type="reset" class="formbutton" name="reset" style="width: 70px"
							onclick="return Validate();"></td>
					</tr>
				</table>
				<%} %>
			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>