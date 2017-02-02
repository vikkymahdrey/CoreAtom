<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.service.SchedulingService"%>
<%@page import="com.agiledge.atom.service.PaginationService"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.List"%>
<%@page
	import="com.agiledge.atom.servlets.subscription.SubscriptionSelector"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dto.SchedulingDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.SchedulingDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="error.jsp"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
	<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>





<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Schedule Employee</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/jquery-latest.js"></script>

<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script src="js/dateValidation.js"></script>
<!-- Beginning of compulsory code below -->
<script type="text/javascript">
$(document).ready(function() {
	$("#from_date").datepick();
	$("#to_date").datepick();
});

	$(window).load(function() {		
		$("#from_date").val("");
		$("#to_date").val("");		
		$(".errorImage").hide();	
		$("#loginTime").change(validate_loginTime);
		$("#logoutTime").change(validate_logoutTime);
		$("#from_date").change(validate_FromDateToDate);
		$("#to_date").change(validate_FromDateToDate);
	});
	function validate_AllField() {
		validate_FromDateToDate();
		validate_loginTime();
		validate_logoutTime();
	}
	
	function validate_fromDate(fromDate, toDate) {
		var flag = true;
		try {
			var fromDate_img = $(fromDate).parent("td").children("img");
			if ($(fromDate).val() == "") {
				$(fromDate_img).attr("title", "Please Enter from date");
				$(fromDate_img).show();
				$(fromDate).addClass("error");
				$(fromDate).removeClass("valid");
				flag = false;
			} else {
				$(fromDate_img).hide();
				$(fromDate).addClass("valid");
				$(fromDate).removeClass("error");
			}
		} catch (e) {
			alert("exception " + e);
		}
		return flag;
	}
	function validateWithSubscriptionEffectiveDates1(row) {
		var flag = true;

		try {
			var fromDate=$("#from_date");
			var effectiveDate = $("#subscriptionEffectiveDate").val();
			//alert("fromdate: "+ fromDate + " effective date : "+ effectiveDate);
			//alert(CompareTwoDatesyyyymmddddmmyyyy(fromDate,effectiveDate));
			if (CompareTwoDatesyyyymmddddmmyyyy(fromDate.val(), effectiveDate)) {
				flag = false;
			}
		} catch (e) {
			flag = false;
			alert(e.message);

		}
		return flag;
	}
	function validate_FromDateToDate() {

		var flag = true;
		try {
			var fromDate=$("#from_date");
			var toDate=$("#to_date");
			flag = validate_fromDate($(fromDate), $(toDate));
			if(flag)
				flag=validate_toDate($(fromDate), $(toDate));
			if (flag == true) {
				var cur_date = new Date();
				var today = new Date();
				var todayevar = today.getDate() + "/" + today.getMonth() + "/"
						+ today.getFullYear();
				var current_hour = cur_date.getHours();
				var next_day = new Date(cur_date
						.setDate(cur_date.getDate() + 1));
				var next_dayvar = next_day.getDate() + "/"
						+ next_day.getMonth() + "/" + next_day.getFullYear();
				var advFifteen_day = new Date(cur_date.setDate(cur_date
						.getDate() + 15));
				var advFifteen_dayvar = advFifteen_day.getDate() + "/"
						+ advFifteen_day.getMonth() + "/"
						+ advFifteen_day.getFullYear();
				var fromDate_img = $(fromDate).parent("td").children("img");
				var toDate_img = $(toDate).parent("td").children("img");
				var from_date = $(fromDate).val();
				var to_date = $(toDate).val();
				var mon1 = parseInt(from_date.substring(3, 5), 10) - 1;
				var dt1 = parseInt(from_date.substring(0, 2), 10);
				var yr1 = parseInt(from_date.substring(6, 10), 10);
				var date1 = new Date(yr1, mon1, dt1);				
				var advThirt_day = new Date(date1.setDate(date1.getDate() + 30));

				var advThirt_dayvar = advThirt_day.getDate() + "/"
						+ advThirt_day.getMonth() + "/"
						+ advThirt_day.getFullYear();
				from_date = $(fromDate).val();
			//	var scheduled_To = $(scheduledTo).val();
			//	var scheduled_From = $(scheduledFrom).val();
				var subscriptionID = $("#SubscriptionId").val();
				//alert(from_date+"from date"+scheduled_To);				

				if (flag == true) {
					if (CompareTwodiffDates(to_date, from_date)) {
						$(fromDate_img)
								.attr("title",
										"Invalid Date Range!\nFrom Date cannot be after To Date!");
						$(fromDate_img).show();
						flag = false;
						$(fromDate).addClass("error");
						$(fromDate).removeClass("valid");
					} else if (validateWithSubscriptionEffectiveDates1($(fromDate)) == true) {
						$(fromDate_img)
								.attr("title",
										"Scheduling from date should be after subscription effective date");
						flag = false;
						$(fromDate_img).show();
						$(fromDate).addClass("error");
						$(fromDate).removeClass("valid");
					} else if (CompareTwoDatesddmmyyyy(from_date, todayevar)) {
						$(fromDate_img)
								.attr("title",
										"Invalid Date Range!\n From date Should  be after Today!");
						$(fromDate_img).show();
						flag = false;
						$(fromDate).addClass("error");
						$(fromDate).removeClass("valid");
					} else if (CompareTwoDatesddmmyyyy(from_date, next_dayvar)
							&& current_hour >= 23) {
						$(fromDate_img)
								.attr("title",
										"Invalid date. Cut off time is 11 pm to schedule for next day.");

						$(fromDate_img).show();
						flag = false;
						$(fromDate).addClass("error");
						$(fromDate).removeClass("valid");
					}else if ((to_date != "")
							&& (CompareTwoDatesddmmyyyy(to_date,
									advThirt_dayvar) == false)) {
						$(toDate_img)
								.attr("title",
										"Invalid Date Range!\n Date range should be 30 Days!");
						$(toDate_img).show();
						flag = false;
						$(toDate).addClass("error");
						$(toDate).removeClass("valid");
					} else {
						checkDatesAreOutOfExistingSchedules(subscriptionID,
								from_date, to_date, fromDate, fromDate_img);

					}
				}

			}		
		
		} catch (e) {
			alert("Error in validate_fromDateToDate " + e);
		}
		return flag;
	}

	function checkDatesAreOutOfExistingSchedules(subscriptionID, fromDateParam,
			toDateParam, fromDate, fromDate_img) {

		var flag = true;

		try {
			if (subscriptionID != "" && fromDateParam != ""
					&& toDateParam != "") {

				var param = "?subscriptionID=" + subscriptionID + "&fromDate="
						+ fromDateParam + "&toDate=" + toDateParam;
				//alert(param);
				$
						.ajax({
							type : 'POST',
							url : 'IsWithinSchedules' + param,
							success : function(data) {
								var sss = data.trim();
								//	alert(sss);
								if (sss == "true") {
									//	alert('schedule exists'+flag);	
									$(fromDate_img)
											.attr("title",
													"Duplicate! Schedule exists on the date range");
									$(fromDate_img).show();
									flag = false;
									$(fromDate).addClass("error");
									$(fromDate).removeClass("valid");
									flag = false;

								} else {
									$(fromDate_img).hide();
									$(fromDate).addClass("valid");
									$(fromDate).removeClass("error");
								}

							},
							error : function(data) {
								var data = $.parseJSON(data);
								$.each(data.errors, function(index, value) {
									alert(value);
								});
								//	 flag=true;
								alert('error');
							}
						//dataType: 'json'

						});
			}
		} catch (e) {
			alert(e);
		}

		return flag;
	}

	function validate_toDate(toDate) {

		var flag = true;
		try {
			var toDate_img = $(toDate).parent("td").children("img");
			if ($(toDate).val() == "") {
				$(toDate_img).attr("title", "Please Enter to date");

				$(toDate_img).show();
				flag = false;

				$(toDate).addClass("error");
				$(toDate).removeClass("valid");

			} else {
				$(toDate_img).hide();

				$(toDate).addClass("valid");
				$(toDate).removeClass("error");
			}
		} catch (e) {
			alert(e);
		}
		return flag;
	}
	function validate_loginTime(loginTime, logoutTime) {
		var logoutTime=$("#logoutTime");
		var loginTime=$("#loginTime");
		var flag = true;
		try {
			var loginTime_img = $(loginTime).parent("td").children(
					".errorImage");

			if ($(loginTime).val() == "") {
				$(loginTime_img).attr("title", "Please Select Login Time");

				$(loginTime_img).show();

				flag = false;

				$(loginTime).addClass("error");
				$(loginTime).removeClass("valid");

			} else if ($(loginTime).val() != "None"
					&& ($(logoutTime).val() == $(loginTime).val())) {

				$(loginTime_img).attr("title", "Login & logout are same");

				$(loginTime_img).show();

				flag = false;

				$(loginTime).addClass("error");
				$(loginTime).removeClass("valid");

			} else {

				$(loginTime_img).hide();

				$(loginTime).addClass("valid");
				$(loginTime).removeClass("error");
			}

		} catch (e) {
			alert(e);
		}

		return flag;
	}

	function validate_logoutTime(logoutTime, loginTime) {
		var flag = true;
		var logoutTime=$("#logoutTime");
		var loginTime=$("#loginTime");
		try {
			var logoutTime_img = $(logoutTime).parent("td").children(
					".errorImage");
			//$(logoutTime_img).show();
			if ($(logoutTime).val() == "") {
				$(logoutTime_img).attr("title", "Please Select logout");

				$(logoutTime_img).show();
				flag = false;

				$(logoutTime).addClass("error");
				$(logoutTime).removeClass("valid");
			}
			if ($(logoutTime).val() != "None"
					&& ($(logoutTime).val() == $(loginTime).val())) {

				$(logoutTime_img).attr("title", "Login & logout are same");

				$(logoutTime_img).show();

				flag = false;

				$(logoutTime).addClass("error");
				$(logoutTime).removeClass("valid");

			} else {
				$(logoutTime_img).hide();

				$(logoutTime).addClass("valid");
				$(logoutTime).removeClass("error");
			}
		} catch (e) {
			alert(e);
		}
		return flag;
	}

	// validate entirform
	function Validate1() {

		var flag = true;
		validate_AllField();
		try {
			if ($(".validate").hasClass("valid")) {
				if ($(".validate").hasClass("error")) {
					alert("Validation Error.Move mouse pointer to error mark");
					flag = false;
				} else {
					flag = true;
				}

			} 
		} catch (e) {
			alert(e);
		}
		return flag;

	}
	
	
	
</script>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
</head>
<body>
	
	<%@ include file="../../Header.jsp"%>
	<%

		OtherDao ob = null;
		ob = OtherDao.getInstance();
		/*starting special section for pagination middle */
		
		
	%>
	<div id="body">
		<div class="content">
			<h3>Schedule</h3>

			<hr />
			<br /> <br />
			<hr />
			<form:form commandName="SelfScheduling"  action="SelfScheduleSubmit.do" onsubmit="return Validate1()"  method="POST">
				
				<table>
					<tr>

						<td>Emp ID</td>
						<td>${SelfScheduling.employeeId}</td>
					</tr>
					<tr>
						<td>Name</td>
						<td>${SelfScheduling.employeeName}</td>
					</tr>
					<tr>
						<td>Effective Date (dd/mm/yyyy)</td>
						<td class="td-EffectiveDate">${SubscriptionEffective}
						<form:input type="hidden"
							id="subscriptionEffectiveDate" path="subscriptionDate"/></td>
					</tr>
					<tr>
						<td><%=SettingsConstant.PROJECT_TERM%></td>
						<td>${SelfScheduling.description}</td>
					</tr>
					<tr>

						<td>From Date</td>
						<td class="td-schedulingFromDate"><form:input readonly="readonly" id="from_date" path="schedulingFromDate" class="validate fromDate"/>
							<img src="images/error.png" class="errorImage" 
							id="from_date_img" /></td>
					</tr>
					<tr>
						<td>To Date</td>
						<td><form:input class="toDate validate" readonly="readonly" id="to_date" type="text" path="schedulingToDate" />
							<img src="images/error.png" class="errorImage"
							id="to_date_img" /></td>
					</tr>
					<tr>
						<td>Weekly Off</td>
						<td><input type="checkbox" name="weeklyOff" /></td>
					</tr>
					<tr>
						<td>Login</td>
						<td id="td-login"><select name="loginTime"
								id="loginTime" class="validate loginTime">
								<option value="">Select</option>
									 <c:forEach var="loginTime" items="${SelfScheduling.loginTimeDto}">
							 <option value="${loginTime.logTime}">${loginTime.logTime}</option>
								</c:forEach>
								</select><img src="images/error.png" class="errorImage"
							id="logintime_img" /></td>
					</tr>
					<tr>
						<td>Logout</td>
						<td id="td-logout">
						<select name="logoutTime" id="logoutTime" class="validate logoutTime">
						<option value="">Select</option>
								 <c:forEach var="logoutTime" items="${SelfScheduling.logoutTimeDto}">
							 <option value="${logoutTime.logTime}">${logoutTime.logTime}</option>
								</c:forEach>
								</select>
						
						 <img src="images/error.png" class="errorImage"
							id="logouttime_img" /></td>
					</tr>

					<tr>
						<td>Status <form:input type="hidden" class="scheduledTo"
							id="scheduledTo"
							path="schedulingToDate"/> <form:input
							type="hidden" class="scheduledFrom"
							id="scheduledFrom"
							path="scheduledFromDate" /> 
							<form:input
							type="hidden" class="SubscriptionId"
							id="SubscriptionId"
							path="SubscriptionId" />
							<form:input type="hidden"	path="project"/> 
							
							<c:if test="${not empty SelfScheduling.scheduledToDate}">
							</td><td>Active</td><tr><td>Scheduled Upto (dd/mm/yyyy)</td><td>${SelfScheduling.schedulingToDate}</td></tr><tr><td>Scheduled By</td><td>${SchedulededPerson}
							</c:if>
								<c:if test="${empty SelfScheduling.scheduledToDate}">
								</td><td>Not Scheduled</td><td>
								</c:if> 
								</td>
								</tr>								
					<tr>						
						<td  align="center"><input type="submit"
							class="formbutton" name="submit" value="Schedule" style="width: 70px" />&nbsp;
							<input type="button" class="formbutton" value="Back"
							onclick="javascript:history.go(-1);" />
						</td>
						<td>&nbsp;</td>
					</tr>					
				</table>
			</form:form>
		<%@include file="../../Footer.jsp"%>
		</div>
	</div>

</body>
</html>
