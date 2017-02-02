<%@page import="com.agiledge.atom.service.AdhocService" %>
<%@page import="com.agiledge.atom.dto.EmployeeDto" %>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Adhoc Passengers</title>
<link rel="icon" href="images/agile.png" type="image/x-icon" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<h3 align="center">Passenger Information</h3>
	<hr />
	<%
	String bookingid=request.getParameter("bookingId");
	ArrayList<EmployeeDto> list=new AdhocService().getAdhocPassengers(bookingid);
	int i=1;
	String gender="";
	%>
	<table><tr>
	<th align="center">No</th>
	<th align="center">Name</th>
	<th align="center">Gender</th>
	<th align="center">Email</th>
	<th align="center">Contact Number</th></tr>
	<%
	for(EmployeeDto dto:list)
	{
		if(dto.getGender().equalsIgnoreCase("m"))
		{
			gender="Male";
		}
		else
		{
			gender="Female";
		}
	%>
	<tr>
	<td align="center"><%=i%></td>
	<td align="center"><%=dto.getDisplayName() %></td>
	<td align="center"><%=gender%></td>
	<td align="center"><%=dto.getEmailAddress() %></td>
	<td align="center"><%=dto.getContactNo() %></td>
	</tr>
	<%
	i=i+1;
	}
	%>
	</table>
	<table>
	<tr align="center"><td align="center"><input type="button" value="Close" class="formbutton" onclick="javascript:window.close();"/> </td></tr>
	</table>
</body>
</html>