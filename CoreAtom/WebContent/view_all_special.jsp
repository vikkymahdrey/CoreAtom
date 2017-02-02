<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View All Employees</title>
<script type="text/javascript">
function searchForm() {
	document.specialRouteForm.submit();
	return false;
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
	%>
	<%@include file="Header.jsp"%>
	<%
		}
		String site = request.getParameter("site");
	%>
	<h3 align="center">View All Employees</h3>
	<form action="view_all_special.jsp" name="specialRouteForm" method="post">
		<table style="width: 20%; border: 0px none;">
			<tr>
				<td>Choose Site</td>
				<td>
					<%
						List<SiteDto> siteList = new SiteService().getSites();
					%> <select name="site" id="site" onchange="return searchForm();">
						<option value="">--select--</option>
						<%
							for (SiteDto dto : siteList) {
								String siteSelect = "";
								if (dto.getId().equals(site)) {
									siteSelect = "selected";
								}
						%>
						<option <%=siteSelect%> value="<%=dto.getId()%>">
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
	<%
		if (site != null) {
	%>
	<table>
		<tr>
			<th align="center">Personnel Number</th>
			<th align="center">First Name</th>
			<th align="center">Last Name</th>
			<th align="center">Department</th>
			<th align="center">Routing</th>
		</tr>
		<%
			List<EmployeeDto> Dtolist = new EmployeeService()
						.searchEmployeeFromSubscription(site);
				for (EmployeeDto dto : Dtolist) {
		%>
		<tr>
			<td align="center"><%=dto.getPersonnelNo()%></td>
			<td align="center"><%=dto.getEmployeeFirstName()%></td>
			<td align="center"><%=dto.getEmployeeLastName()%></td>
			<td align="center"><%=dto.getDeptName()%></td>
			<td align="center"><%=dto.getRoutingType()%></td>
		</tr>
		<%
			}
		%>
	</table>
	<%
		}
	%>
	<%@include file='Footer.jsp'%>
</body>
</html>