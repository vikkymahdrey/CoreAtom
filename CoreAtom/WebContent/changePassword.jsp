<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
function confirmValidate() {
	var flag = true;
	var oldpwd=$("input[name=oldpwd]").val();
	var newpwd = $("input[name=pwd]").val();
	var conpwd = $("input[name=conpwd]").val();
	var ogpwd = $("input[name=ogpwd]").val();
	if(oldpwd=="")
		{
		flag=false;
		alert("Please Specify Old Password!");
		}
	else if(newpwd=="")
		{
		flag=false;
		alert("Please Specify New Password!");
		}
	else if(conpwd=="")
		{
		flag=false;
		alert("Please Specify Confirm Password!");
		}
	if(newpwd!=conpwd)
		{
		flag=false;
		alert("New Password And Confirm Password Are Different!");
		}
	/*
	if(ogpwd!=oldpwd)
		{
		flag=false;
		alert("Old Password Entered Is Wrong!");
		}
	*/
	return flag;
		}
		</script>

<title>Change Password</title>
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
		}
	%>
	<input type="hidden" value="<%=session.getAttribute("password").toString()%>" name="ogpwd"/>
		<div class="container">
		<div class="header">
			<table width="100%">
				<tr>
					<td width="10%"><a href="http://www.agiledgesolutions.com"><img
							src="images/agile.png" /></a></td>

					<td width="90%">
						<h1 style="color: #FF4000;">ATOm</h1>
						<h4>Agiledge transport optimization manager</h4>
					</td>
			</table>
			<div class="clear"></div>
		</div>


		<div class="clr"></div>


	</div>
	
<h3 align="center">Change Password</h3>
<form name="ChangePassword" action="ChangePassword" method="post" onSubmit="return confirmValidate()">
<table align="center">
<tr>
<td align="right"><b>Old Password</b></td><td align="left"><input type="password" name="oldpwd" id="oldpwd"/></td>
</tr>
<tr>
<td align="right"><b>Enter New Password</b></td><td align="left"><input type="password" name="pwd" id="pwd"/></td>
</tr>
<tr>
<td align="right"><b>Confirm New Password</b></td><td align="left"><input type="password" name="conpwd" id="conpwd"/></td>
</tr>
</table>
<table align="center">
<tr align="center">
<td colspan="3">
<input type="submit" value="Save"  class="formbutton" />&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" value="Reset" class="formbutton"/>
</td>
</tr>
</table>
</form>
</body>
</html>