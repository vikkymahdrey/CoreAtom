<%--
    Document   : emp_subscription
    Created on : Aug 28, 2012, 12:51:01 PM
    Author     : 123
--%>

<%@page import="com.agiledge.atom.service.APLService"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@page import="com.agiledge.atom.dto.EmployeeSubscriptionDto"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.lang.Exception"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="com.agiledge.atom.dao.APLDao"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"
	errorPage="error.jsp"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<link href="css/jquery.datepick.css" rel="stylesheet" type="text/css" />
<script src="js/dateValidation.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
						hideDiv();
						$('#fromDate').datepick();
						$('.route').change(function() {
											var routeId = $(this).val();
											if (routeId != null && routeId != "") {
												$.ajax({
															type : 'POST',
															url : 'GetShuttleRoute?routeId='
																	+ routeId,
															success : function(data) {
																var xmlstr = data.xml ? data.xml
																		: (new XMLSerializer())
																				.serializeToString(data);
																showDiv(xmlstr);
															}
														});
											}
										});
						$('#login').change(function() {
							var loginTime = $(this).val();
							var routeId = $("#inRoute").val();
							if (routeId != null && routeId != "") {
								$.ajax({
											type : 'POST',
											url : 'GetShuttleRoute?routeId='
													+ routeId+'&loginTime='+loginTime,
											success : function(
													data) {
												$("#inTimeStatus").append(data);
												
											}
										});
							}
						});
						$('#logout').change(function() {
							var logoutTime = $(this).val();
							var routeId = $("#outRoute").val();
							if (routeId != null && routeId != "") {
								$.ajax({
											type : 'POST',
											url : 'GetShuttleRoute?routeId='
													+ routeId+'&logoutTime='+logoutTime,
											success : function(
													data) {
												$("#outTimeStatus").append(data);
											}
										});
							}
						});
					});
	function showPopup(chkbx) {
			url = "landmarkSearchForRegister.jsp?transport=shuttle";
			var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
			size = "height=450,width=600,top=200,left=300," + params;
			var site = document.getElementById("site").value;
			url += "&site=" + site;
			newwindow = window.open(url, 'name', size);
			if (window.focus) {
				newwindow.focus();
			}		
	}

</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Subscription</title>



<script type="text/javascript">
	function hideDiv() {
		$("#entryDiv").hide();
	}
	function showDiv(data) {
		$("#entryDiv").show();
		//alert(data);
		$("#routeList").html(data);
	}

	//  validate to prompt user about the payment
	function confirmValidate() {
		$("#fromDateStatus").html("");
		$("#routeStatus").html("");
		var flag = true;
		var currentDate = new Date();

		var nextnextnextMonthDateSupporter = new Date();
		//nextnextnextMonthDateSupporter.setDate(2);
		nextnextnextMonthDateSupporter.setMonth(nextnextnextMonthDateSupporter
				.getMonth() + 2);
		var currentDatevar = currentDate.getDate() + "/"
				+ currentDate.getMonth() + "/" + currentDate.getFullYear();
		var fromDate = $("#fromDate").val();
		var site = $("select[name=site]").val();
		if (fromDate == "") {
			flag = false;
			$("#fromDateStatus").html("Please Specify Effective Date!");
		} else if (CompareTwoDatesddmmyyyy(fromDate, currentDatevar)) {
			$("#fromDateStatus").html(
					"Effective date must be after current date !");
			flag = false;
		} else if ($("#inRoute").val() == "" && $("#outRoute").val() == "") {
			$("#inRouteStatus").html("select Route");
			flag = false;

		} else if ($("#inRoute").val() != "" && $("#login").val() == "") {
			$("#inTimeStatus").html("Choose In Time");
			flag = false;
		} else if ($("#outRoute").val() != "" && $("#logout").val() == "") {
			$("#outTimeStatus").html("Choose Out Time");
			flag = false;
		}
		return flag;

	}
</script>
</head>
<body onload="displayaddress()">

	<%
		try {
			long empid = 0;
			String employeeId = OtherFunctions.checkUser(session);
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="../../Header.jsp"%>
	<%
		OtherDao ob = null;
			ob = OtherDao.getInstance();
	%>
	<div id="body">
		<div class="content">
			<%
				// code to show employee information in page
					String code = "";
					try {
						code = ob.getEmployeeDet(empid);
					} catch (Exception e) {
						System.out.println("Exception  " + e);
					}
			%>






			<hr />
			<h3>Subscription</h3>



			<div class="commentPanel" id="entryDiv"
				style="padding-top: 2px; display: block; width: 40%;height:60%; margin-left:20px; margin-top: -6%;position: fixed;overflow: scroll;">
				<div class="commentPanelTitle">
					<p style="float: left; margin-top: 0px; margin-left: 30%;" >
						<b>Route Details</b>
					</p>
					<div style="float: right;" id="closeAddArea">
						<img id="closeAddArea_img" style="float: right; padding: 3px;"
							src="images/close.png" onclick="hideDiv()" title="Close">
					</div>
				</div>
				<div id="routeList" class="commentPanelContent"></div>
			</div>
			<form:form action="SubscribeForShuttleSubmit.do"
				commandName="shuttleSubscription" method="POST"
				onsubmit="return confirmValidate()">
				<table align="center">

					<tr>

						<td align="right" style="width: 300px">Effective Date</td>
						<td><form:input type="text" path="subscriptionFromDate"
								id="fromDate" /></td>
						<td id="fromDateStatus"></td>
					</tr>
							<tr>
						<td align="right">Area</td>
						<td><input type="text" value=""
							onclick="showPopup('LandMarkSearch.jsp') " readonly name="area"
							id="area" /> <form:input path="landMark" type="hidden" id="landMarkID"
							name="landMarkID"  /> <label for="area" class="requiredLabel">*</label>

						</td>

					</tr>
					<tr>
						<td align="right">Place</td>
						<td><input type="text" value=""
							onclick="showPopup('LandMarkSearch.jsp') " readonly name="place"
							id="place" /> <label for="place" class="requiredLabel">*</label>
						</td>

					</tr>
					<tr>
						<td align="right">Landmark</td>
						<td><input type="text"
							onclick="showPopup('LandMarkSearch.jsp') " readonly
							id="landmark" /> <label for="landMark"
							class="requiredLabel">*</label></td>

					</tr>
					<tr>

						<td align="right">In Routes</td>
						<td><form:select path="inRoute" items="${inRoutes}"
								class="route" id="inRoute"></form:select></td>
						<td id="inRouteStatus"></td>

					</tr>
					<tr>

						<td align="right">Out Routes</td>
						<td><form:select path="outRoute" items="${outRoutes}"
								class="route" id="outRoute"></form:select></td>
						<td></td>

					</tr>
					<tr>

						<td align="right" style="width: 300px">In</td>
						<td id="inTimeStatus"><form:select path="shiftIn" items="${INshifts}"
								id="login"></form:select></td>
						<td ></td>
					</tr>
					<tr>

						<td align="right">Out</td>
						<td id="outTimeStatus"><form:select path="shiftOut" items="${OUTshifts}"
								id="logout"></form:select></td>
						<td  ></td>

					</tr>


					<tr>

						<td>&nbsp;<form:input type="hidden" path="site" id="site" />
							<form:input type="hidden" path="employeeID" /></td>
						<td><input type="submit" value="Subscribe" class="formbutton"/></td>
					</tr>
				</table>
			</form:form>

			<%@include file="../../Footer.jsp"%>
			<%
				} catch (Exception e) {
					System.out.println("Error" + e);
				}
			%>
		</div>
	</div>

</body>
</html>
