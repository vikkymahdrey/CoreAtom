<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<script type="text/javascript">
function loadEmployeeDetails()

{
	if (confirm()) {

		try {

			var xmlhttp;
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					document.getElementById("EmployeeDetails").innerHTML = xmlhttp.responseText;
				}
			}
			xmlhttp.open("POST", "GetEmployeeSpecial?employeeID="
					+ document.SearchForm.employeeID.value + "&firstName="
					+ document.SearchForm.firstName.value + "&lastName="
					+ document.SearchForm.lastName.value+ "&projectid="
					+ document.SearchForm.project.value, true);
			xmlhttp.send();
		} catch (e) {
			alert(e.message);
		}

	} else {

		alert("Please type texts in search fields");

	}

	return false;
}
function confirm() {
	var flag = true;

	if ($("input[name=employeeID]").val().trim() == ""
			&& $("input[name=firstName]").val().trim() == ""
			&& $("input[name=lastName]").val().trim() == ""&&$("input[name=projectdesc]").val().trim()=="") {
	//	flag = false;

	}

	return flag;

}

function selectRow(id) {

	var name = document.getElementById("firstName-" + id).innerHTML;
	opener.subscriptionForm.supervisorID1.value = id;
	opener.subscriptionForm.supervisorName1.value = name;
    
	this.close();
}
function openWindow(url) {
	window.open(url, 'Ratting',
			'width=400,height=350,left=150,top=200,toolbar=1,status=1,');

}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Set As Special</title>
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
	%>
<h3 align="center">Set Special</h3>
			<form name="SearchForm" action=""
				onsubmit="return loadEmployeeDetails()">


				<table border="0" align="center">

					<tr>
						<td>Employee ID</td>
						<td><input type="text" name="employeeID" /></td>
						<td></td>
					</tr>
					<tr>
						<td>First Name</td>
						<td><input type="text" name="firstName" /></td>
						<td></td>
					</tr>
					<tr>
						<td>Last Name</td>
						<td><input type="text" name="lastName" /></td>
						<td></td>
					</tr>
					<tr>
						<td><%=SettingsConstant.PROJECT_TERM%></td>
						<td><input readonly type="text" value=""   name="projectdesc"
							id="projectdesc" />
							<input type="button" class="formbutton" value="..." width="6" onclick="openWindow('getproject.jsp')" />
							 <input type="hidden" id="project" value="" name="project" />
						</td>
					</tr>
					<tr>
						<td></td>
						<td><input type="submit" class="formbutton" name="Search"
							value="Search" /></td>
						<td></td>
					</tr>


				</table>
			</form>
			<form name="UpdateEmployeesRoleForm" action="SetSpecial">
				<div id="EmployeeDetails"></div>

			</form>
			<%@include file='Footer.jsp'%>
		</div>
		</div>
</body>
</html>