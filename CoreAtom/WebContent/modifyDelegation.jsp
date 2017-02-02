<%--
    Document   : emp_schedulingHistory
    Created on : Oct 21, 2012, 10:09PM
    Author     : Noufal C C
--%>
<%@page import="com.agiledge.atom.dao.DelegateRoleDao"%>
<%@page import="com.agiledge.atom.dto.DelegateRoleDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>

<%@page contentType="text/html" pageEncoding="UTF-8"
	errorPage="error.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Delegate Role</title>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script src="js/dateValidation.js"></script>

<script type="text/javascript">
	$(function() {


		$('input[name=toDate]').datepick();

	});
</script>


<script type="text/javascript">
	function showPopup(url) {

		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		var size = "height=124,width=300,top=200,left=300," + params;
		if (url == "LandMarkSearch.jsp") {
			size = "height=450,width=600,top=200,left=300," + params;
		} else if (url == "SupervisorSearch1.jsp"
				|| url == "SupervisorSearch2.jsp") {
			size = "height=450,width=700,top=200,left=300," + params;
		} else if (url == "termsAndConditions.html") {
			size = "height=450,width=520,top=200,left=300," + params;
		}

		newwindow = window.open(url, 'name', size);

		if (window.focus) {
			newwindow.focus();
		}
	}

	//  validate to prompt user about the payment
	function confirmValidate() {
		var flag = true;
		var currentDate = new Date();
		var currentDatevar = currentDate.getDate() + "/"
				+ currentDate.getMonth() + "/" + currentDate.getFullYear();
		var fromDate = $("input[name=fromDate]").val();
		var toDate = $("input[name=toDate]").val();

		var delegatedEmpName = $("input[name=delegatedEmpName]").val();
		if (delegatedEmpName == "") {
			alert("Please specify Employee To Delegate");
			flag = false;		
		} else if (toDate == "") {
			alert("Please specify To Date");
			flag = false;
		} else if (CompareTwoDatesddmmyyyy(toDate, currentDatevar)) {
			alert("From Date date must be after current date !");
			flag = false;
		} else if (CompareTwodiffDates(toDate, fromDate)) {
			alert("From Date date must be after to date !");
			flag = false;
		}

		return flag;

	}
</script>



</head>
<body>
	<div id="body">
		<div class="content">
			<%
				long empid = 0;
				String employeeId = OtherFunctions.checkUser(session);
				DelegateRoleDto  delegateRoleDto=null;
				int id=Integer.parseInt(request.getParameter("Id"));
				delegateRoleDto=new DelegateRoleDao().getDelegatedRolesById(id);
			%>
				<%@include file="Header.jsp"%>
			<br>
			<h3>Modify Delegate Role</h3>
			<form name="ModifyDelegateRole" action="ModifyDelegateRole" method="post"
				onsubmit="return confirmValidate()">
				<table>

					<tr>
					
						<td align="right">Employee To Delegate</td>
						<td><input type="hidden" name="delegatedEmpId" value="<%=delegateRoleDto.getDelegatedEmpId()%>"
							id="supervisorID2" /> <input type="text" readonly
							name="delegatedEmpName" id="supervisorName2" value="<%=delegateRoleDto.getDelegatedemployeeName()%>"
							 />
							 <input type="hidden" name="Id" value="<%=id%>"/>
							 </td>
					</tr>
					<tr>
						<td align="right">From</td>
						<td><input type="text" name="fromDate" id="fromDate" value="<%=delegateRoleDto.getFromDate()%>" readonly /></td>
					</tr>
					<tr>
						<td align="right">To</td>
						<td><input type="text" name="toDate" id="toDate" readonly value="<%=delegateRoleDto.getToDate()%>" /></td>
					</tr>
					<tr>
						<td colspan="2" align="center"><input type="submit"
							class="formbutton" value="Submit" /></td>
					</tr>
				</table>
			</form>

			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
