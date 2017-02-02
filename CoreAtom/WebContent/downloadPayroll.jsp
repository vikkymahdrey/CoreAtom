<%@page import="com.agiledge.atom.service.PayrollService"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Payroll Report</title>
<%
String month=request.getParameter("month");
String year = request.getParameter("year");
String site = request.getParameter("site");
	String mimeType="application/vnd.ms-excel";
	response.setContentType(mimeType);
	response.setHeader("Content-Disposition","inline; filename = Payroll "+ site +" " + year +" " + month+".xls");
	
%>
</head>
<body>
<%
	out.write(new PayrollService().getPayrollForExcel(Integer.parseInt( month) ,Integer.parseInt(  year), Integer.parseInt( site)));
%>
</body>
</html>