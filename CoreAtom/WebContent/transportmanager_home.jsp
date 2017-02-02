<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="error.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Transport Manager Home</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>

<body>


	<%
        long empid=0;
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

			<% out.println(ob.getEmployeeDet(empid));%>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
