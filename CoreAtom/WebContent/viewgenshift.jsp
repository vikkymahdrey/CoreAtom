<%@page import="com.agiledge.atom.service.GeneralShiftService"%>
<%@page import="com.agiledge.atom.dto.GeneralShiftDTO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Assigned General Shift Timings</title>
</head>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
</script>
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
	%>
<h3 align="center">View Assigned General Shift Timings</h3>
<%
ArrayList<GeneralShiftDTO> list =new GeneralShiftService().getAssignedLogData(request.getParameter("id"));
%>
<table>
					<tr>
						<th align="center">Log Time</th>
						<th align="center">Log Type</th>
					</tr>
					<%
					for(GeneralShiftDTO dto:list)
					{
					%>
					<tr>
					<td align="center"><%=dto.getLogtime() %></td>
					<td align="center"><%=dto.getLogtype() %></td>
					</tr>
					<%
					} %>
				</table>
<table>
<tr></tr>
</table>
<%@include file='Footer.jsp'%>
</body>
</html>