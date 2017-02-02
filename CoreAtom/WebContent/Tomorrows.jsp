<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script type="text/javascript">
	$(function() {
		$('#date').datepick();

	});
</script>
</head>
<body>

	<%
long empid = 0;
String employeeId = OtherFunctions.checkUser(session);
if (employeeId == null||employeeId.equals("null") ) {
	String param = request.getServletPath().substring(1) + "___"+ request.getQueryString(); 	response.sendRedirect("index.jsp?page=" + param);
} else {
    empid = Long.parseLong(employeeId);
} 
%>
	<form action="downloadnotschedule.jsp">
		Date : <input type="text" readonly="readonly" name="date" id="date" />
		<input type="submit" Value="OK" />
	</form>
	<br />
	<a href="index.jsp" />Back To ATOm
	</a>

</body>
</html>