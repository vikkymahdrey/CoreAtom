
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="com.agiledge.atom.service.SchedulingService"%>
<%@page import="com.agiledge.atom.dto.ScheduledEmpDto"%>
<%@page import="com.agiledge.atom.dto.ScheduleAlterDto"%>
<%@page import="com.agiledge.atom.service.SchedulingAlterService"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="com.agiledge.atom.dao.ProjectDao"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dto.ProjectDto"%>
<%@page import="com.agiledge.atom.dto.SchedulingDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.SchedulingDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="error.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- InstanceBegin template="/Templates/hrmanager_temp.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Modify Schedule</title>


<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">

	$(document).ready(function(){
		$("input[type=checkbox]").click(function(){
			if($(this).is(":checked"))
			{
				$("select[name=logintime"+ $(this).val() + "]").removeAttr("disabled");
				$("input[name=oldlogintime"+ $(this).val() + "]").val($("select[name=logintime"+ $(this).val() + "]").val());
				$("select[name=logouttime"+ $(this).val() + "]").removeAttr("disabled");
				$("input[name=oldlogouttime"+ $(this).val() + "]").val($("select[name=logouttime"+ $(this).val() + "]").val());
			}else
				{
				$("select[name=logintime"+ $(this).val() + "]").val($("input[name=oldlogintime"+ $(this).val() + "]").val());
				$("select[name=logintime"+ $(this).val() + "]").attr("disabled",":disabled");
				$("select[name=logouttime"+ $(this).val() + "]").val($("input[name=oldlogouttime"+ $(this).val() + "]").val());
				$("select[name=logouttime"+ $(this).val() + "]").attr("disabled",":disabled");
				}
		});
	});
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

	function submitForm() {
		if (Validate() == true) {
			try {
				document.schedule_alter.submit();
			} catch (e) {
				alert(e)
			}
		}
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
	long scheduleId = Long.parseLong(""
			+ request.getParameter("scheduleId"));
	String site=request.getParameter("site");
	SchedulingAlterService schedulingAlterServiceObj = new SchedulingAlterService();
	ArrayList<ScheduleAlterDto> scheduleDetailsList = new ArrayList<ScheduleAlterDto>();
	scheduleDetailsList = schedulingAlterServiceObj
			.getScheduleDetalsByDate(scheduleId);
	ArrayList<LogTimeDto> loginTimeList = null;
	ArrayList<LogTimeDto> logoutTimeList = null;
	ArrayList<LogTimeDto> logoutProjectSpecificTimeList = null;
	ArrayList<LogTimeDto> loginProjectSpecificTimeList = null;
	LogTimeDao logTimeDaoObj = new LogTimeDao();
	loginTimeList = logTimeDaoObj.getAllGeneralLogtime("IN",site);	
	logoutTimeList = logTimeDaoObj.getAllGeneralLogtime("OUT",site);
	
	
	ScheduledEmpDto schduledEmployeeDto = new SchedulingService()
			.getSchedulingDetailBySchedule(scheduleId);
	
	loginProjectSpecificTimeList= new LogTimeService().getProjectSpecificTime(schduledEmployeeDto.getProject(), "IN");
	logoutProjectSpecificTimeList= new LogTimeService().getProjectSpecificTime(schduledEmployeeDto.getProject(), "OUT");
	loginTimeList.addAll(loginProjectSpecificTimeList);
	logoutTimeList.addAll(logoutProjectSpecificTimeList);
	int serialNo = 1;
%>

	<div id="body">
		<div class="content">



			<table align="center" border="0" style="width: 100%">
				<tr>
					<td colspan="6" align="left"><h3>Modify Employee Schedule</h3></td>
				</tr>
				<tr>
					<td align="center">Employee Id</td>
					<td><%=schduledEmployeeDto.getEmployeePersonnelNo()%></td>
					<td align="center">Employee Name</td>
					<td><%=schduledEmployeeDto.getEmployeeName()%></td>

					<td align="center">Scheduled By</td>
					<td><%=schduledEmployeeDto.getScheduledBy()%></td>
					<td align="center"><%=SettingsConstant.PROJECT_TERM%></td>
					<td><%=schduledEmployeeDto.getProjectDescription()%></td>
				</tr>
				<tr>
					<td align="center">Scheduled From</td>
					<td><%=OtherFunctions.changeDateFromatToddmmyyyy(schduledEmployeeDto
					.getFrom_date())%></td>
					<td align="center">Scheduled To</td>
					<td><%=OtherFunctions.changeDateFromatToddmmyyyy(schduledEmployeeDto
					.getTo_date())%></td>
					<td align="center">Updated on</td>
					<td><%=schduledEmployeeDto.getStatusDate()%></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>


				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
			</table>


			<form name="schedule_alter" method="post" action="ScheduleAlter"
				onsubmit="return Validate()">
				<input type="hidden" value="transadmin" name="source" /> <input
					type="hidden" name="scheduleid" value="<%=scheduleId%>" />
				<table border="0" width="40%" align="center">

					<thead>
						<tr>
							<th align="center">Date</th>
							<th align="center">Day</th>
							<th align="center">Login</th>
							<th align="center">Logout</th>
							<th align="center">Updated on</th>
							<th align="center">Updated By</th>
							<th></th>
						</tr>
					</thead>
					<tbody>




						<tr>

							<%
								for (ScheduleAlterDto scheduleAlterDtoObj : scheduleDetailsList) {
							%>


							<td align="center"><%=OtherFunctions.changeDateFromatToddmmyyyy(scheduleAlterDtoObj
						.getDate())%></td>
							<td align="center"><%=OtherFunctions.getWeekDay(scheduleAlterDtoObj
						.getDate())%></td>
							<td align="center"><input type="hidden"
								name="oldlogintime<%=scheduleAlterDtoObj.getDate()%>" value="" />
								<select disabled
								name="logintime<%=scheduleAlterDtoObj.getDate()%>"
								id="logintime">
									<option value="">Cancel</option>

									<%
										if (scheduleAlterDtoObj.getLoginTime().equals("weekly off")) {
									%>
									<option selected value="weekly off">Weekly OFF</option>
									<%
										} else {
									%>
									<option value="weekly off">Weekly OFF</option>
									<%
										}
										for (LogTimeDto logTimeDto : loginTimeList) {
													if (scheduleAlterDtoObj.getLoginTime().equals(
															logTimeDto.getLogTime())) {
									%>
									<option selected value="<%=logTimeDto.getLogTime()%>"><%=logTimeDto.getLogTime()%></option>
									<%
										} else {
									%>
									<option value="<%=logTimeDto.getLogTime()%>"><%=logTimeDto.getLogTime()%></option>
									<%
										}												
											}
									%>
							</select></td>
							<td align="center"><input type="hidden"
								name="oldlogouttime<%=scheduleAlterDtoObj.getDate()%>" value="" />
								<select disabled
								name="logouttime<%=scheduleAlterDtoObj.getDate()%>"
								id="logouttime""><option value="">Cancel</option>

									<%
										if (scheduleAlterDtoObj.getLogoutTime().equals("weekly off")) {
									%>
									<option selected value="weekly off">Weekly OFF</option>
									<%
										} else {
									%>
									<option value="weekly off">Weekly OFF</option>
									<%
										}
										for (LogTimeDto logTimeDto : logoutTimeList) {
													if (scheduleAlterDtoObj.getLogoutTime().equals(
															logTimeDto.getLogTime())) {
									%>
									<option selected value="<%=logTimeDto.getLogTime()%>"><%=logTimeDto.getLogTime()%></option>
									<%
										} else {
									%>
									<option value="<%=logTimeDto.getLogTime()%>"><%=logTimeDto.getLogTime()%></option>
									<%
										}												
											}
									%>
							</select></td>
							<td><%=scheduleAlterDtoObj.getStatusDate() %></td>
							<td><%=scheduleAlterDtoObj.getUpdatedByDisplayName()%></td>
							<%
								if (OtherFunctions.checkedDateForAdmin(scheduleAlterDtoObj.getDate())) {
							%>
							<td align="center"><input type="checkbox" name="date"
								id="date<%=serialNo%>"
								value="<%=scheduleAlterDtoObj.getDate()%>" /></td>
							<%
								} else {
							%>
							<td></td>
							<%
								}
							%>

							<input type="hidden"
								name="status<%=scheduleAlterDtoObj.getDate()%>"
								value="<%=scheduleAlterDtoObj.getScheduleStates()%>" />

						</tr>
						<%
							serialNo++;
							}
						%>








						<tr>


							<td colspan="4" align="center"><input type="hidden"
								id="datecount" value="<%=serialNo%>" /> <input type="button"
								class="formbutton" value="Back"
								onclick="javascript:history.go(-1)" /> <input type="button"
								class="formbutton" name="cancel" value="Cancel"
								onclick="submitForm()" /> <input type="submit"
								class="formbutton" name="book" value="Update" /></td>


						</tr>

					</tbody>
				</table>
			</form>


			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
