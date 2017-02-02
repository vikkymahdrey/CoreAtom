<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Edit Role</title>
<script type="text/javascript">
	function validate() {
		var rolename = document.getElementById("roleName").value;
		var roledescription = document.getElementById("roleDescription").value;
		document.getElementById("namevalid").innerHTML = "";
		document.getElementById("descvalid").innerHTML = "";
		if (rolename.length < 1) {
			document.getElementById("namevalid").innerHTML = "Role Name is blank!";
			return false
		}
		if (roledescription.length < 1) {
			document.getElementById("descvalid").innerHTML = "Role Description is blank !";
			return false
		}
		window.close();
		opener.location.reload();
	}
	 
</script>
</head>
<body>
<h3 align="center">Edit Role Information</h3>
<%
int roleId=Integer.parseInt(request.getParameter("roleId"));
String roleName=request.getParameter("roleName");
String roleDescription=request.getParameter("roleDescription");
%>
<form name="EditRole" action="EditRole" method="post" onsubmit="return validate()">
<table align="center">
<tr><td>Role Id</td>
<td><input type="text" value="<%=roleId %>" name="roleId" id="roleId" readonly/></td></tr>
<tr><td>Role Name</td>
<td><input type="text" value="<%=roleName %>" name="roleName" id="roleName"/>
<label id="namevalid" style="color: red;"></label></td></tr>
<tr><td>Role Description</td>
<td><input type="text" value="<%=roleDescription %>" name="roleDescription" id="roleDescription"/>
<label id="descvalid" style="color: red;"></label></td></tr>
</table>
<table>
<td align="center"><input type="submit" class="formbutton" value="Update"/></td>
</table>
</form>
</body>
</html>