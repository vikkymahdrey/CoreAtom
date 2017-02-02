
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dto.GeneralShiftDTO"%>
<%@page import="com.agiledge.atom.service.GeneralShiftService"%>
<%@page import="com.agiledge.atom.usermanagement.service.ViewManagementService"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.usermanagement.dto.ViewManagementDto"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.List"%>

<%@page import="java.util.ArrayList"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<script src="js/dateValidation.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script src="js/dateValidation.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#from_date').datepick();
	});

	function showhide(param) {
		var selvalue;
		if (param == "appselect") {
			selvalue = $("#approvalselect").val();
			if (selvalue == "n") {
				$("#tr1").hide();
			} else {
				$("#tr1").show();
			}
		} else if (param == "amt") {
			selvalue = $("#deductionselect").val();
			if (selvalue == "n") {
				$("#tr2").hide();
				$("#tr3").hide();
			} else {
				$("#tr2").show();
				$("#tr3").show();
			}
		} else if (param == "wait") {
			selvalue = $("#waitselect").val();
			if (selvalue == "n") {
				$("#tr4").hide();
			} else {
				$("#tr4").show();
			}
		}
	}

	function validate() {
		var selectedshiftin = document.getElementById("selectin");
		var selectedshiftinLength = document.getElementById("selectin").options.length;
		var selectedshiftout = document.getElementById("selectout");
		var selectedshiftoutLength = document.getElementById("selectout").options.length;
		var flag = true;
		var site = $('#siteselect').val();
		var fromdate = $('#from_date').val();
		var cdays = $('#cutoffdays').val();
		var ccan = $('#cutoffcancel').val();
		var dtype = $('#deductionselect').val();
		var wlist = $('#waitselect').val();
		var currentDate = new Date();
		var currentDatevar = currentDate.getDate() + "/"
				+ currentDate.getMonth() + 1 + "/" + currentDate.getFullYear();
		if (site == "select") {
			alert("Please Select Site!");
			flag = false;
		} else if (fromdate == "") {
			alert("Please Specify From Date!");
			flag = false;
		} else if (cdays == "") {
			alert("Please Specify Cut-Off Time For Subscribing(In Days)!");
			flag = false;
		} else if (ccan == "") {
			alert("Please Specify Cut-Off Time For Cancelling(In Days)!");
			flag = false;
		} else if (dtype == "y") {
			if ($('#deductionamt').val() == "") {
				alert("Please Specify Deduction Amount!");
				flag = false;
			}
		} else if (!CompareTwodiffDates(currentDatevar, fromdate)) {
			alert("From Date Must Be After Current Date!");
			flag = false;
		}
		if (wlist == "y") {
			if ($('#waitlist').val() == "") {
				alert("Please Specify Cut-Off Time For Waiting List Re-confirmation(In Days)!");
				flag = false;
			}
		}
		if (selectedshiftinLength == 0 && selectedshiftinLength == 0) {
			alert("Select Atleast One In Or Out Log Time!");
			flag = false;
		}
		for ( var i = 0; i < selectedshiftinLength; i++) {
			selectedshiftin.options[i].selected = true;
		}
		for (i = 0; i < selectedshiftoutLength; i++) {
			selectedshiftout.options[i].selected = true;
		}
		return flag;
	}
	function listMoveRight() {
		var shift = document.getElementById("shiftin");
		var selectedshift = document.getElementById("selectin");
		var optionSelected = shift.selectedIndex;
		var optionNew = document.createElement('option');
		optionNew.value = shift.options[optionSelected].value;
		optionNew.text = shift.options[optionSelected].innerHTML;
		try {
			selectedshift.add(optionNew, null);
		} catch (e) {
			selectedshift.add(optionNew);
		}
		shift.options[optionSelected] = null;
	}

	function listMoveLeft() {
		var selectedshift = document.getElementById("selectin");
		var shift = document.getElementById("shiftin");
		var optionSelected = selectedshift.selectedIndex;
		var optionNew = document.createElement('option');
		optionNew.value = selectedshift.options[optionSelected].value;
		optionNew.text = selectedshift.options[optionSelected].innerHTML;
		try {
			shift.add(optionNew, null);
		} catch (e) {
			shift.add(optionNew);
		}
		selectedshift.options[optionSelected] = null;
	}

	function listMoveRightout() {
		var shift = document.getElementById("shiftout");
		var selectedshift = document.getElementById("selectout");
		var optionSelected = shift.selectedIndex;
		var optionNew = document.createElement('option');
		optionNew.value = shift.options[optionSelected].value;
		optionNew.text = shift.options[optionSelected].innerHTML;
		try {
			selectedshift.add(optionNew, null);
		} catch (e) {
			selectedshift.add(optionNew);
		}
		shift.options[optionSelected] = null;
	}

	function listMoveLeftout() {
		var selectedshift = document.getElementById("selectout");
		var shift = document.getElementById("shiftout");
		var optionSelected = selectedshift.selectedIndex;
		var optionNew = document.createElement('option');
		optionNew.value = selectedshift.options[optionSelected].value;
		optionNew.text = selectedshift.options[optionSelected].innerHTML;
		try {
			shift.add(optionNew, null);
		} catch (e) {
			shift.add(optionNew);
		}
		selectedshift.options[optionSelected] = null;
	}
</script>
<title>Add General Shift Configuration</title>
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
		List<SiteDto> siteDtoList = new SiteDao().getSites();
		ArrayList<ViewManagementDto> roles =  new ViewManagementService().getRoleList();
		ArrayList<GeneralShiftDTO> dlist = new GeneralShiftService()
				.getDeductionTypes();
		ArrayList<LogTimeDto> inslist = new LogTimeService()
				.getAllLogtime("IN");
		ArrayList<LogTimeDto> outslist = new LogTimeService()
				.getAllLogtime("OUT");
	%>
	<div id="body">
		<div class="content">
			<h3 align="center">Add General Shift Configuration</h3>
			<div>
				<br>
				<br>
				<br>
				<form action="AddGeneralShiftConfig" method="post"
					onSubmit="return validate()">
					<table align="center">
						<tr>
							<td align="left">Select Site</td>
							<td align="left"><select id="siteselect" name="siteselect">
									<option value="select">Select</option>
									<%
										for (SiteDto siteDto : siteDtoList) {
									%>
									<option value="<%=siteDto.getId()%>"><%=siteDto.getName()%></option>
									<%
										}
									%>
							</select></td>
						</tr>
						<tr>
							<td align="left">Shift Time In</td>
							<td align="left"><br>
							<select id="shiftin" name="shiftin" multiple="multiple">
									<%
										for (LogTimeDto ldto : inslist) {
									%>
									<option value=<%=ldto.getLogTime()%>><%=ldto.getLogTime()%></option>
									<%
										}
									%>
							</select></td>
							<td width="10%"><br>
							<input type="button" class="formbutton" name="right"
								value="&rArr;" onclick="listMoveRight()" /> <br>
							<br>
							<input type="button" class="formbutton" name="left"
								value="&lArr;" onclick="listMoveLeft()" /></td>
							<td>Selected Shifts&nbsp;&nbsp;&nbsp;&nbsp;<br>
							<select id="selectin" name="selectin" multiple="multiple"></select>
							</td>
						</tr>
						<tr>
							<td align="left">Shift Time Out</td>
							<td align="left"><br>
							<select id="shiftout" name="shiftout" multiple="multiple">
									<%
										for (LogTimeDto lgdto : outslist) {
									%>
									<option value=<%=lgdto.getLogTime()%>><%=lgdto.getLogTime()%></option>
									<%
										}
									%>
							</select></td>
							<td width="10%"><br>
							<input type="button" class="formbutton" name="right"
								value="&rArr;" onclick="listMoveRightout()" /> <br>
							<br>
							<input type="button" class="formbutton" name="left"
								value="&lArr;" onclick="listMoveLeftout()" /></td>
							<td>Selected Shifts&nbsp;&nbsp;&nbsp;&nbsp;<br>
							<select id="selectout" name="selectout" multiple="multiple"></select>
							</td>
						</tr>
						<tr>
							<td align="left">From Date</td>
							<td align="left"><input type="text" id="from_date"
								name="from_date" readOnly
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2012,  1, 25)}" /></td>
						</tr>
						<tr>
							<td align="left">Approval Required</td>
							<td align="left"><select id="approvalselect"
								name="approvalselect" onChange="showhide('appselect')"><option
										value="y">Yes</option>
									<option value="n">No</option></select></td>
						</tr>
						<tr id="tr1">
							<td align="left">Approval By</td>
							<td align="left"><select id="roleselect" name="roleselect">
									<%
										for (ViewManagementDto udto : roles) {
									%><option value="<%=udto.getRoleId()%>"><%=udto.getRoleName()%></option>
									<%
										}
									%>
							</select></td>
						</tr>
						<tr>
							<td align="left">Cut-Off Time For Subscribing(In Days)</td>
							<td align="left"><input type="text" id="cutoffdays"
								name="cutoffdays" /></td>
						</tr>
						<tr>
							<td align="left">Cut-Off Time For Cancelling(In Days)</td>
							<td align="left"><input type="text" id="cutoffcancel"
								name="cutoffcancel" />
						<tr>
							<td align="left">Amount Deduction</td>
							<td align="left"><select id="deductionselect"
								name="deductionselect" onChange="showhide('amt')"><option
										value="y">Yes</option>
									<option value="n">No</option></select></td>
						<tr id="tr2">
							<td align="left">Deduction Type</td>
							<td align="left"><select id="deductiontype"
								name="deductiontype">
									<%
										for (GeneralShiftDTO gdto : dlist) {
									%>
									<option value=<%=gdto.getDeductionid()%>><%=gdto.getDeductiontype()%></option>
									<%
										}
									%>
							</select></td>
						</tr>
						<tr id="tr3">
							<td align="left">Amount(In Rupees)</td>
							<td align="left"><input type="text" id="deductionamt"
								name="deductionamt" /></td>
						</tr>
						<tr>
							<td align="left">Waiting List Re-Confirmation Required</td>
							<td align="left"><select id="waitselect" name="waitselect"
								onChange="showhide('wait')"><option value="y">Yes</option>
									<option value="n">No</option></select>&nbsp;&nbsp;&nbsp;&nbsp;</td>
						</tr>
						<tr id="tr4">
							<td align="left">Cut-Off Time For Waiting List
								Re-confirmation(In Days)</td>
							<td align="left"><input type="text" id="waitlist"
								name="waitlist"></td>
						</tr>
						<tr align="center">
							<td align="center" colspan="4"><br>
							<br>
							<input type="submit" class="formbutton" value="Save" />&nbsp;&nbsp;&nbsp;&nbsp;<input
								type="reset" value="Reset" class="formbutton" /></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	<%@include file='Footer.jsp'%>
</body>
</html>