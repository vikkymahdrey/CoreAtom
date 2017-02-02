<%@page import="com.agiledge.atom.dto.ScheduleAlterDto"%>
<%@page import="com.agiledge.atom.dto.EmployeeSubscriptionDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="com.agiledge.atom.dto.ScheduledEmpDto"%>
<%@page import="com.agiledge.atom.service.SchedulingService"%>
<%@page import="com.agiledge.atom.service.SchedulingAlterService"%>
<%@page import="com.agiledge.atom.service.TripDetailsService"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.SchedulingDao"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="error.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<title>Employee Schedule Cancel</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />


<script type="text/javascript" src="js/jquery-latest.js"></script>
<script>
	$(document)
			.ready(
					function() {

						setActiveMenuItem();
						$("input[type=checkbox]")
								.click(
										function() {
											if ($(this).is(":checked")) {
												try {
													$(
															"select[name='loginTime"
																	+ $(this)
																			.val()
																	+ "']")
															.removeAttr(
																	"disabled");

													$(
															"input[name='oldlogintime"
																	+ $(this)
																			.val()
																	+ "']")
															.val(
																	$(
																			"select[name='loginTime"
																					+ $(
																							this)
																							.val()
																					+ "']")
																			.val());
													$(
															"select[name='logoutTime"
																	+ $(this)
																			.val()
																	+ "']")
															.removeAttr(
																	"disabled");
													$(
															"input[name='oldlogouttime"
																	+ $(this)
																			.val()
																	+ "']")
															.val(
																	$(
																			"select[name='logoutTime"
																					+ $(
																							this)
																							.val()
																					+ "']")
																			.val());

												} catch (e) {
													alert(e);
												}
											} else {
												$(
														"select[name='loginTime"
																+ $(this).val()
																+ "']")
														.val(
																$(
																		"input[name='oldlogintime"
																				+ $(
																						this)
																						.val()
																				+ "']")
																		.val());
												$(
														"select[name='loginTime"
																+ $(this).val()
																+ "']")
														.attr("disabled",
																":disabled");
												$(
														"select[name='logoutTime"
																+ $(this).val()
																+ "']")
														.val(
																$(
																		"input[name='oldlogoutime"
																				+ $(
																						this)
																						.val()
																				+ "']")
																		.val());
												$(
														"select[name='logoutTime"
																+ $(this).val()
																+ "']")
														.attr("disabled",
																":disabled");
											}
										});
					});

	function setActiveMenuItem() {
		var url = window.location.pathname;
		var filename = url.substring(url.lastIndexOf('/') + 1);
		//  $("li[class=active']").removeAttr("active");

		$("a[href='" + filename + "']").parent().attr("class", "active");
		$("a[href='" + filename + "']").parent().parent().parent('li').attr(
				"class", "active");

	}
	function Validate() {
		var checkboxflag = false;
		var datecount = document.getElementById("datecount").value;
		for ( var i = 1; i <= datecount; i++) {
			if ($("input[id=date" + i + "]").is(":checked")) {
				checkboxflag = true;
				break;
			}

		}
		if (!checkboxflag) {
			alert("select atleast One date")
			return false
		}
		return true;
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
	<%
		OtherDao ob = null;
		ob = OtherDao.getInstance();
	%>



	<div id="body">
		<div id="content">



			<%
				out.println(ob.getEmployeeDet(empid));
			%>
			<hr />
			<div>
				<%
					try {
						long schedulingIds[] = new SchedulingService()
								.getSchedulingDetailByEmployee(empid);
						ArrayList<ScheduleAlterDto> scheduleDetailsList = new SchedulingAlterService()
								.getScheduleEmployeeDetalsByDate(schedulingIds);

						if (scheduleDetailsList.size() > 0) {
				%>


				<h3>Active Schedules</h3>
				<hr />
				<form name="empschedulecancel" action="EmployeeScheduleCancel"
					method="post" onsubmit="return Validate()">
					<table class="dataTable" width="70%">

						<thead>
							<tr>
								<th></th>
								<th>Date(dd/mm/yyyy)</th>
								<th>Login</th>
								<th>Logout</th>
							</tr>
						</thead>
						<tbody>
							<%
								int serialNo = 1;
										for (ScheduleAlterDto scheduleAlterDto : scheduleDetailsList) {
							%>

							<tr>
								<%
									if (OtherFunctions.checkedDate(scheduleAlterDto
														.getDate())) {
								%>
								<td><input type="checkbox" name="scheduleIdDate"
									id="date<%=serialNo%>"
									value=<%=scheduleAlterDto.getScheduleId() + "@"
									+ scheduleAlterDto.getDate()%> />
									<%
										} else {
														out.print("<td></td>");
													}
									%></td>
								<td><%=OtherFunctions
								.changeDateFromatToddmmyyyy(scheduleAlterDto
										.getDate())%></td>
								<td><select disabled
									name="loginTime<%=scheduleAlterDto.getScheduleId() + "@"
								+ scheduleAlterDto.getDate()%>">

										<%
											if (scheduleAlterDto.getLoginTime().equals("")
																|| scheduleAlterDto.getLoginTime()
																		.equalsIgnoreCase("none")) {
										%>
										<option selected="selected" value="">Cancel</option>
										<option value="weekly off">Weekly OFF</option>
										<%
											} else if (scheduleAlterDto.getLoginTime().equals(
																"weekly off")) {
										%>
										<option value="">Cancel</option>
										<option selected="selected" value="weekly off">Weekly
											OFF</option>
										<%
											} else {
										%>
										<option value="">Cancel</option>
										<option value="weekly off">Weekly OFF</option>
										<option selected="selected"
											value="<%=scheduleAlterDto.getLoginTime()%>"><%=scheduleAlterDto.getLoginTime()%></option>
										<%
											}
										%>
								</select> <input type="hidden"
									name="oldlogintime<%=scheduleAlterDto.getScheduleId() + "@"
								+ scheduleAlterDto.getDate()%>" />
								</td>
								<td><select disabled
									name="logoutTime<%=scheduleAlterDto.getScheduleId() + "@"
								+ scheduleAlterDto.getDate()%>">
										<%
											if (scheduleAlterDto.getLogoutTime().equals("")
																|| scheduleAlterDto.getLogoutTime()
																		.equalsIgnoreCase("none")) {
										%>
										<option selected="selected" value="">Cancel</option>
										<option value="weekly off">Weekly OFF</option>
										<%
											} else if (scheduleAlterDto.getLogoutTime().equals(
																"weekly off")) {
										%>
										<option value="">Cancel</option>
										<option selected="selected" value="weekly off">Weekly
											OFF</option>
										<%
											} else {
										%>
										<option value="">Cancel</option>
										<option value="weekly off">Weekly OFF</option>
										<option selected="selected"
											value="<%=scheduleAlterDto.getLogoutTime()%>"><%=scheduleAlterDto.getLogoutTime()%></option>
										<%
											}
										%>
								</select> <input type="hidden"
									name="oldlogouttime<%=scheduleAlterDto.getScheduleId() + "@"
								+ scheduleAlterDto.getDate()%>" />

									<input type="hidden"
									name="status<%=scheduleAlterDto.getScheduleId() + "@"
								+ scheduleAlterDto.getDate()%>"
									value="<%=scheduleAlterDto.getScheduleStates()%>" /></td>
							</tr>
							<%
								serialNo++;
										}
							%>
							<tr>
								<td colspan="2"></td>
								<td colspan="2"><input type="hidden" id="datecount"
									value="<%=serialNo%>" /> <input type="button"
									class="formbutton" value="Back"
									onclick="javascript:history.go(-1)" /> <input
									class="formbutton" type="submit" value="Update" /></td>
							</tr>
						</tbody>
					</table>
				</form>

				<%
					}
					} catch (Exception e) {
						System.out.println("Exception : " + e);
					}
				%>

			</div>
			<%@include file="Footer.jsp"%>
		</div>

	</div>





</body>
</html>
