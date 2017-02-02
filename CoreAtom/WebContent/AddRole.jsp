<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<title>Add Role</title>
<script type="text/javascript">
	function validate() {
		var rolename = document.getElementById("rolename").value;
		var roledescription = document.getElementById("roledescription").value;
		document.getElementById("namevalid").innerHTML = "";
		document.getElementById("descvalid").innerHTML = "";
		if (rolename.length < 1) {
			document.getElementById("namevalid").innerHTML = "Role Name can't be blank !";
			return false
		}
		if (roledescription.length < 1) {
			document.getElementById("descvalid").innerHTML = "Role Description can't be blank !";
			return false
		}
	}
	 
</script>
<div class="content">
<div class="content_resize">
		<%
          long empid=0;
        String employeeId = OtherFunctions.checkUser(session);
        if (employeeId == null||employeeId.equals("null") ) {
            String param = request.getServletPath().substring(1) + "___"+ request.getQueryString(); 	response.sendRedirect("index.jsp?page=" + param);
        } else {
            empid = Long.parseLong(employeeId);
            %>
			<%@include file="Header.jsp"%>
			<%
        }
        OtherDao ob = null;
        ob = OtherDao.getInstance();
    %>
    <h3>Add Role</h3>
			<br /> 
<form name="AddRole" action="AddRole" method="post" onsubmit="return validate()">
<table align="center">
<tr>
<td>Role Name</td>
<td><input type="text" name="rolename" id="rolename"/>
<label id="namevalid" style="color: red;"></label></td>
</tr>
<tr>
<td>Role Description</td>
<td><input type="text" name="roledescription" id="roledescription"/>
<label id="descvalid" style="color: red;"></label></td>
</tr>
<tr>
<td align="center"><input type="submit" class="formbutton"/></td>
</tr>
</table>
</form>
</head>
<body>

</body>
</html>