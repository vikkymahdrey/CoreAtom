<%-- 
    Document   : ManagerSearch
    Created on : Oct 17, 2012, 2:28:15 PM
    Author     : 123
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<script type="text/javascript">
	function loadEmployeeDetails()

	{
			var param = document.getElementById("param").value;
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
				xmlhttp.open("POST", "EscalationMatrix?employeeID="
						+ document.SearchForm.employeeID.value + "&name="
						+ document.SearchForm.name.value + "&param="+param, true);
				xmlhttp.send();
			} catch (e) {
				alert(e.message);
			}	
	}

	function confirm() {
		var flag = true;

		if ($("input[name=employeeID]").val().trim() == ""
				&& $("input[name=firstName]").val().trim() == ""
				&& $("input[name=lastName]").val().trim() == "") {
			flag = false;

		}

		return flag;

	}

	function onsubmitfn() {
		loadEmployeeDetails();
		return false;
	}
	function selectRow(id) {
		try {
			var name = document.getElementById("employeeNameCell-" + id).innerHTML;
			var param = document.getElementById("param").value;
			opener.document.getElementById(param + "ID").value = id;
			opener.document.getElementById(param + "name").value = name;

			this.close();
		} catch (e) {
			alert(e);
		}
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search Escalation</title>
</head>
<body>
	<%
		String param = request.getParameter("param");
	%>
	<input type="hidden" id="param" value=<%=param%> />
	<form name="SearchForm" action="" onsubmit="return onsubmitfn()">


		<table border="0">

			<tr>
				<td>ID</td>
				<td><input type="text" name="employeeID" /></td>
				<td></td>
			</tr>
			<tr>
				<td>Name</td>
				<td><input type="text" name="name" /></td>
				<td></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" class="formbutton" name="Search"
					value="Search" /></td>
				<td></td>
			</tr>


		</table>
		<div id="EmployeeDetails"></div>

	</form>

</body>
</html>




