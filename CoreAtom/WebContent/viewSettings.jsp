<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
function loadSettingsData()

{

		try {

			var xmlhttp;
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					document.getElementById("SettingsDetails").innerHTML = xmlhttp.responseText;
				}
			}
			xmlhttp.open("POST", "GetSettings?property="+document.selectform.property.value, true);
			xmlhttp.send();
		} catch (e) {
			alert(e.message);
		}

	return false;
}
function deletesettings(deleteid){
	
	 $("input[name=deleteId]").val(deleteid);
	 if(confirm("Do you really want to delete ?"))
		 { 
			$("form[name=deletesettingsform]").submit();
			
		 }
 
 
}
</script>
<title>View Settings</title>
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
	`
		<div class="content">
<h3 align="center">View Settings</h3>
<%

%>
<form name="selectform" action="" onSubmit="return loadSettingsData()">
<table width="20%" align="center">
						<tr>
						<td align="center">
<select name="property" id="property">
<option value="All">All</option>
<option value="payroll"> Payroll </option>
</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="View" class="formbutton"/>
</td>
</tr>
</table>
</form>
<div id="SettingsDetails"></div>
<form name="deletesettingsform" action="DeleteSettings" method="post">
<input type="hidden" name=deleteId id=deleteId/>
</form>
<div>
</div>
</div>
<%@include file='Footer.jsp'%>

</body>
</html>