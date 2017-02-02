
<%@page import="com.agiledge.atom.service.RouteService"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.ShuttleSocketService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert Schedule</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
	function validate() {
		var inTime = document.getElementById("in").value;
		var outTime = document.getElementById("out").value;
		var route1 = document.getElementById("route1").value;
		var apl1 = document.getElementById("apl1").value;
		var route2 = document.getElementById("route2").value;
		var apl2 = document.getElementById("apl2").value;
		if (inTime == "NO") {
			alert("Select in Time");
			return false;
		} else if (outTime == "NO") {
			alert("Select Out Time");
			return false;
		} else if (route1 == "NO" && route2 == "NO") {
			alert("Select atleast one route");
			return false;
		} else if (apl1 == "NO" && apl2 == "NO") {
			alert("Select pick up/drop Point");
			return false;
		} else
			return true;
	}
	function deleteEmp() {
		var optionSelected = empids.selectedIndex;
		var optionNew = document.createElement('option');
		optionNew.value = empids.options[optionSelected].value;
		optionNew.text = empids.options[optionSelected].innerHTML;
		empids.remove(optionSelected);
	}
	function getAPL(i) {
		var route = $("#route" + i).val();
		try {

			var xmlhttp;
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					var APLfetched = xmlhttp.responseText;
					document.getElementById("apl" + i).innerHTML = "<select name='apl"+i+"' id='apl"+i+"'><option value='NO'>Select</option>"
							+ APLfetched + "</select>";

				}
			}
			xmlhttp.open("POST", "GetLandMarksBasedOnRoute?route=" + route,
					true);
			xmlhttp.send();
		} catch (e) {

			alert("APL call alert");
		}
	}
	function selectAll(selectBox, selectAll) {
		if (typeof selectBox == "string") {
			selectBox = document.getElementById(selectBox);
		}
		if (selectBox.type == "select-multiple") {
			for (var i = 0; i < selectBox.options.length; i++) {
				selectBox.options[i].selected = selectAll;
			}
		}
		var fl = validate();
		return fl;
	}
</script>
</head>
<body>
	<%
		long empid = 0;

		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
	%>
	<%@ include file="Header.jsp"%>
	<%
		String site = "1";
		long actEmpId = empid;
		if (session.getAttribute("delegatedId") != null) {
			employeeId = session.getAttribute("delegatedId").toString();
			empid = Long.parseLong(employeeId);
		}
		String empids[] = request.getParameterValues("empid");
		ArrayList<EmployeeDto> alterid = null;
		if (empids != null) {
			alterid = new ShuttleSocketService().getEmployeeDetails(empids);
		}
		ArrayList<LogTimeDto> inLog = new LogTimeService().getAllLogtime(
				"IN", site);
		ArrayList<LogTimeDto> outLog = new LogTimeService().getAllLogtime(
				"OUT", site);
		ArrayList<RouteDto> routes = new RouteService().getAllRoutes(site);
	%>
	<hr />

	<h3>Add Your Schedule</h3>

	<div id="body">
		<div class="content">
			<form name="employee_trip_info" method="post"
				action="employeeTripping" onsubmit="return Validate()">
				<table border="0">
					<tbody>
						<tr>
							<td valign="top" rowspan="40" width="40%"><input
								type="hidden" name="siteId" id="siteId" value="1" /> <select
								name="empids" id="empids" multiple="multiple"
								style="width: 400px;">
									<%
										if (alterid != null && alterid.size() > 0) {
											for (EmployeeDto empdto : alterid) {
									%><option value="<%=empdto.getEmployeeID()%>"><%=empdto.getDisplayName()%></option>
									<%
										}
										}
									%>
							</select> <br /> <input type="button" class="formbutton"
								value="Add Employee"
								onclick="window.open('getAllEmployee.jsp','Ratting','width=500,height=400,left=150,top=200,toolbar=1,status=1,');" />

								<input type="button" class="formbutton" value="Delete Employee"
								onclick="deleteEmp()" /></td>
						</tr>
						<tr>
							<td align="center"><strong>&nbsp;IN&nbsp;&nbsp;</strong> <select
								name="in" id="in"><option value="NO">Select</option>
									<%
										for (LogTimeDto in : inLog) {
											String selectedin = "";
											if (alterid != null && alterid.size() == 1) {
												if (in.getLogTime().equalsIgnoreCase(
														(alterid.get(0)).getLogin())) {
													selectedin = "selected";
												}
											}
									%>
									<option value="<%=in.getLogTime()%>" <%=selectedin%>><%=in.getLogTime()%>
									</option>
									<%
										}
									%></select> <strong>&nbsp;OUT&nbsp;&nbsp;</strong><select name="out"
								id="out">
									<option value="NO">Select</option>
									<%
										for (LogTimeDto Out : outLog) {
											String selectedout = "";
											if (alterid != null && alterid.size() == 1) {
												if (Out.getLogTime().equalsIgnoreCase(
														(alterid.get(0)).getLogout())) {
													selectedout = "selected";
												}
											}
									%>
									<option value="<%=Out.getLogTime()%>" <%=selectedout%>><%=Out.getLogTime()%></option>
									<%
										}
									%>
							</select></td>
						</tr>
						<tr>
							<td align="center"><strong>&nbsp;ROUTE-IN&nbsp;&nbsp;</strong>
								<select name="route1" id="route1" onchange="getAPL(1);"><option
										value="NO">Select</option>
									<%
										for (RouteDto dto : routes) {
											String selectedrt1 = "";
											if (alterid != null && alterid.size() == 1) {
												if (dto.getRouteId() == Integer.parseInt((alterid.get(0))
														.getinroute())) {
													selectedrt1 = "selected";
												}
											}
									%>
									<option value="<%=dto.getRouteId()%>" <%=selectedrt1%>><%=dto.getRouteName()%>
									</option>
									<%
										}
									%></select> <strong>&nbsp;ROUTE-OUT&nbsp;&nbsp;</strong> <select
								name="route2" id="route2" onchange="getAPL(2);"><option
										value="NO">Select</option>
									<%
										for (RouteDto dto : routes) {
											String selectedrt = "";
											if (alterid != null && alterid.size() == 1) {
												if (dto.getRouteId() == Integer.parseInt(((alterid.get(0))
														.getOutroute()))) {
													selectedrt = "selected";
												}
											}
									%>
									<option value="<%=dto.getRouteId()%>" <%=selectedrt%>><%=dto.getRouteName()%>
									</option>
									<%
										}
									%></select></td>
						</tr>
						<tr>
							<td align="center"><strong>&nbsp;Pick-up
									Point&nbsp;</strong> <select name="apl1" id="apl1"><option
										value="NO">Select</option>
									<%
										if (alterid != null && alterid.size() == 1) {
									%><option value="<%=(alterid.get(0)).getPickup()%>" selected><%=(alterid.get(0)).getAPL()%></option>
									<%
										}
									%>
							</select></td>

						</tr>
						<tr>
							<td align="center"><strong>&nbsp;Drop Point&nbsp;</strong> <select
								name="apl2" id="apl2"><option value="NO">Select</option>
									<%
										if (alterid != null && alterid.size() == 1) {
									%><option value="<%=(alterid.get(0)).getDrop()%>" selected><%=(alterid.get(0)).getAddress()%></option>
									<%
										}
									%>
							</select></td>

						</tr>
						<tr>
							<td align="center"><input type="submit" class="formbutton"
								value="Save"
								onclick="return selectAll(document.getElementById('empids'),true);" /></td>
						</tr>
						<tr>

							<td colspan="4" align="center"></td>

						</tr>
					</tbody>
				</table>
			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>