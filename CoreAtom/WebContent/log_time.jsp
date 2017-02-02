<%-- 
    Document   : log_time
    Created on : Oct 19, 2012, 3:56:50 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="js/dateValidation.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Log time</title>
<script type="text/javascript">
	function validate() {

		var flag = true;

		if (document.getElementById("logTime").value.trim() == "") {
			alert("Time is empty ");
			flag = false;
		}

		return flag;
	}
</script>

</head>
<body>
	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>

	<div id="body">
		<div class="content">
			<h3>Add New Shift Time</h3>
			<form name="log_time" action="LogTime" onsubmit="return validate()">
				<table width="40%">
					<tr>
						<td align="center" colspan="2"><b2></b2></td>
					</tr>
					<tr>
						<td>Time</td>
						<td><input type="text" name="logTime" id="logTime" /> hh:mm</td>
					</tr>
					<tr>
						<td></td>
						<td><input class="formbutton" type="submit" value="Add" /> <input
							type="button" class="formbutton"
							onclick="javascript:history.go(-1);" value="Back" /></td>
					</tr>

				</table>
			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
