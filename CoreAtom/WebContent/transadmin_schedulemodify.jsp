<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.service.PaginationService"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dto.ScheduledEmpDto"%>
<%@page import="com.agiledge.atom.dao.ScheduledEmpDao"%>
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
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Modify Schedules</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">
	var employeeCount;

	$(window).load(function() {
		$("#checkAllEmployees").click(checkOrUncheckEmployees);
		//$("#selectAllSchedules").click(selectingAllSchedules);    

		$(".errorImage").hide();
		$("input[type=text]").change(instentValidation);

		$("select").change(instentValidation);

		$(".checkEmployee").change(makeElementsInTheRowToBeValidated);

	});
	//---search using site
	function searchForm() {
		//alert('hai ');
		document.siteSearchForm.action = "transadmin_schedulemodify.jsp";
		document.siteSearchForm.submit();
		return false;
	}

	function checkOrUncheckEmployees() {
		try {
			if ($("#checkAllEmployees").is(":checked")) {
				$(".checkEmployee").attr("checked", "checked");

			} else {
				$(".checkEmployee").removeAttr("checked");

			}

			$(".checkEmployee").trigger('change');

		} catch (e) {
			alert(e.message);
		}
	}

	//------------code to validate starts

	//-------  when the check employee is clicked
	function makeElementsInTheRowToBeValidated() {
		try {
			if ($(this).is(":checked")) {
				$(this).parent().parent().children().children(".projectdesc")
						.addClass("validate");
				$(this).parent().parent().children().children(".loginTime")
						.addClass("validate");
				$(this).parent().parent().children().children(".logoutTime")
						.addClass("validate");

			} else {

				$(this).parent().parent().children().children(".projectdesc")
						.removeClass("validate");
				$(this).parent().parent().children().children(".loginTime")
						.removeClass("validate");
				$(this).parent().parent().children().children(".logoutTime")
						.removeClass("validate");
				$(this).parent().parent().children().children(".errorImage")
						.hide();

			}
		} catch (e) {
			alert(e);
		}
	}

	// validation of entrie form

	function formValidate() {
		try {
			$(".validate").each(function() {
				$(this).trigger('change');
			});

		} catch (e) {
			alert(e);
		}

	}

	// validation on time
	function instentValidation() {
		// alert("val : "+ $(this).val());
		var flag = true;

		try {
			if ($(this).hasClass("validate")) {
				if ($(this).hasClass("projectdesc")) {

					flag = validate_projectDesc($(this));

				}

				if ($(this).hasClass("loginTime")) {
					flag = validate_loginTime($(this));

				}
				if ($(this).hasClass("logoutTime")) {
					flag = validate_logoutTime($(this));
				}
			}

		} catch (e) {
			flag = false;
			alert(e);
		}

	}
	//---- validate project-

	function validate_projectDesc(projectDesc) {

		var flag = true;
		var projectDesc_img = $(projectDesc).parent("td").children("img");

		if ($(projectDesc).val() == "") {
			$(projectDesc_img).attr("title", "Please Enter Project");

			$(projectDesc_img).show();
			$(projectDesc).removeClass("valid");
			$(projectDesc).addClass("error");
			flag = false;

		} else {
			$(projectDesc_img).hide();
			$(projectDesc).removeClass("error");
			$(projectDesc).addClass("valid");
		}
		return flag;
	}

	// ---  validate login time
	function validate_loginTime(loginTime) {

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

	//-- validate logout time

	function validate_logoutTime(logoutTime) {
		var flag = true;
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
		formValidate();
		try {
			if ($(".validate").hasClass("valid")) {
				if ($(".validate").hasClass("error")) {
					alert("Validation Error");
					flag = false;
				} else {
					flag = true;
				}

			} else {
				if (!$(".checkEmployee").is(":checked")) {
					alert("Please select atleast one row ");
				} else if ($(".validate").hasClass("error")) {
					alert("Validation Error");

				}

				flag = false;
			}
		} catch (e) {
			alert(e);
		}
		return flag;

	}

	//---------code to validate ends.......

	/*     
	     function selectingAllSchedules()
	     {
	     	if( $("#selectAllSchedules").is(":checked"))
	     		{
	     		$(".selectSchedule").attr("checked","checked");
	     		
	     		}else
	     		{
	     			$(".selectSchedule").removeAttr("checked" );
	             	
	     		}
	     }
	 */
	function submitForm() {
		var checkboxflag = false;
		employeeCount = document.getElementById("employeeCount").value;
		for ( var i = 1; i <= employeeCount; i++) {
			if ($("input[id=employeecheck" + i + "]").is(":checked")) {

				checkboxflag = true;
				break;
			}

		}
		if (checkboxflag) {

			document.schedule_emp.submit();
		} else {
			alert("Please select atleat one row");
		}
	}
	function Validate() {
		var flag = false;
		var checkboxflag = false;
		employeeCount = document.getElementById("employeeCount").value;
		//alert(employeeCount);
		for ( var i = 1; i <= employeeCount; i++) {
			if ($("input[id=employeecheck" + i + "]").is(":checked")) {
				checkboxflag = true;
				if ($("select[id=projectdesc" + i + "]").val() == "") {
					alert("Please Select Project");
					$("select[id=projectbtn" + i + "]").focus();
					flag = false;
				} else if ($("select[id=logintime" + i + "]").val() == "") {
					alert("Please Select Login Time");
					$("select[id=logintime" + i + "]").focus();
					flag = false;
				} else if ($("select[id=logouttime" + i + "]").val() == "") {
					alert("Please Select logout");
					$("select[id=logouttime" + i + "]").focus();
					flag = false;
				}

				else {
					flag = true;
				}
			}
		}
		if (!checkboxflag) {
			alert("Please check the Check box");
		}
		return flag;
	}

	function showAll() {
		//alert("here");
		try{
		var site = document.getElementById("site").value;
		var listSize = document.getElementById("listSize").value;
		var searchdata = document.getElementById("searchdata").value;
		//alert(searchdata);
		var startPos = 0;
		var endPos = listSize;
		window.location = "transadmin_schedulemodify.jsp?site=" + site
				+ "&searchdata=" + searchdata + "&startPos=" + startPos
				+ "&endPos=" + endPos;
		}catch(e){alert(e);}
	}
	function next() {
		
		var site = document.getElementById("site").value;
		var startPos = document.getElementById("startPos").value;
	
		var searchdata = document.getElementById("searchdata").value;
	
		var endPos = document.getElementById("endPos").value;
		var listSize = document.getElementById("listSize").value;
		startPos = parseInt(startPos)
				+ parseInt(document.getElementById("recordsize").value);
		endPos = parseInt(startPos)
				+ parseInt(document.getElementById("recordsize").value);

		if (startPos > listSize) {
			startPos = parseInt(startPos)
					- parseInt(document.getElementById("recordsize").value);
			endPos = parseInt(startPos)
					+ parseInt(document.getElementById("recordsize").value);
		} else {
			if (endPos > listSize) {
				endPos = listSize;
			}
			window.location = "transadmin_schedulemodify.jsp?site=" + site
					+ "&searchdata=" + searchdata + "&startPos=" + startPos
					+ "&endPos=" + endPos;
		}

	}
	function pervious() {

		var site = document.getElementById("site").value;
		var startPos = document.getElementById("startPos").value;
		var endPos = document.getElementById("endPos").value;
		var searchdata = document.getElementById("searchdata").value;
		//var listSize = document.getElementById("listSize").value;
		endPos = startPos;
		startPos = parseInt(startPos)
				- parseInt(document.getElementById("recordsize").value);

		if (startPos < 0) {
			startPos = 0;
			endPos = parseInt(startPos)
					+ parseInt(document.getElementById("recordsize").value);
		} else {

			window.location = "transadmin_schedulemodify.jsp?site=" + site
					+ "&searchdata=" + searchdata + "&startPos=" + startPos
					+ "&endPos=" + endPos;
		}
	}
</script>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script>
	$(document).ready(function() {

		setActiveMenuItem();
	});

	function setActiveMenuItem() {
		var url = window.location.pathname;
		var filename = url.substring(url.lastIndexOf('/') + 1);
		//  $("li[class=active']").removeAttr("active");

		$("a[href='" + filename + "']").parent().attr("class", "active");
		$("a[href='" + filename + "']").parent().parent().parent('li').attr(
				"class", "active");

	}
</script>
<body>


	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<%
		try {
			ScheduledEmpDao ScheduledDaoObj = new ScheduledEmpDao();
			ArrayList<ScheduledEmpDto> scheduledEmpList;
			ArrayList<LogTimeDto> loginTimeList = null;
			ArrayList<LogTimeDto> logoutTimeList = null;
			ArrayList<ProjectDto> projectList = null;
			ProjectDao projectDaoObj = new ProjectDao();
			LogTimeService logTimeDaoObj = new LogTimeService();
			String site = "";
			site = request.getParameter("site");
			/*starting special section for pagination middle */
			int RECORD_SIZE = 50;
			String searchdata = request.getParameter("searchdata");
			String startPos = request.getParameter("startPos");
			String endPos = request.getParameter("endPos");

			if (startPos == null && endPos == null) {
				startPos = "0";
				endPos = "" + RECORD_SIZE;
			}

			if (searchdata != null && searchdata != "") {

				scheduledEmpList = ScheduledDaoObj.getSearchScheduledEmp(
						site, searchdata);
			}

			else {
				scheduledEmpList = ScheduledDaoObj.getScheduledEmp(site);
				/* List<ScheduledEmpDto> scheduledEmpListIterated = new PaginationService()
				.getNext(scheduledEmpList, startPos, endPos);  */
			}

			List<ScheduledEmpDto> scheduledEmpListIterated = new PaginationService()
					.getNext(scheduledEmpList, startPos, endPos);

			if (RECORD_SIZE > scheduledEmpList.size()) {
				endPos = "" + scheduledEmpList.size();
			}
			projectList = projectDaoObj.getProjects();

			int serialNo = 0;
	%>


	<div id="body">
		<div class="content">

			<br />

			<div>
				<form name="siteSearchForm">
					<table style="width: 20%; border: 0px none;">
						<tr>
							<td>Site</td>
							<td>
								<%
									List<SiteDto> siteList = new SiteService().getSites();
								%> <select name="site" id="site" onchange="return searchForm();">
									<option value="">--select--</option>
									<%
										for (SiteDto dto : siteList) {
												String siteSelect = "";
												if (dto.getId().equals(site)) {
													siteSelect = "selected";
												}
									%>




									<option <%=siteSelect%> value="<%=dto.getId()%>">
										<%=dto.getName()%>
									</option>


									<%
										}
									%>
							</select>
							</td>
							<td>Enter Employee Code</td>
							<td><input type="text" name="searchdata" id = "searchdata"> </input></td>
							<td><input type="submit" name="search" class="formbutton"
								value="Search" onclick="return searchForm();"> </input></td>

						</tr>
					</table>
				</form>
			</div>

			<br />
			<hr />
			<h3>Modify Scheduled Employee Details</h3>
			<hr />

			<%
				if (scheduledEmpList.size() > 0) {
			%>
			<table style="width: 40%">
				<tr>
					<td><a href="#" onclick="pervious()">Previous</a></td>

					<td id="pageInfo"><%=Integer.parseInt(startPos) + 1%> to <%=endPos%>
						of</td>
					<td><%=scheduledEmpList.size()%> is showing</td>
					<td><a href="#" onclick="next()">Next</a></td>
					<td><a href="#" onclick="showAll()">Show All</a></td>
				</tr>
			</table>
			<form name="schedule_emp" method="get" action="ScheduleModifyCancel"
				onsubmit="return Validate1()">
				<table width="100%">
					<input type="hidden" value="tansadmin" name="source" />
					<thead>

						<tr>
							<th><input type="checkbox" id="checkAllEmployees" /></th>
							<th>Emp Code</th>
							<th>Name</th>

							<th style="width: 250px"><%=SettingsConstant.PROJECT_TERM%></th>
							<th>From Date</th>
							<th>To Date</th>
							<th>weekly off</th>
							<th>Login</th>
							<th>Logout</th>
							<th>Scheduled By</th>
							<th>Updated on</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<tr>

							<td align="center" colspan="10"><input type="submit"
								name="modify" class="formbutton" value="Modify" /> <input
								type="button" name="Cancel" class="formbutton" value="Cancel"
								onclick="submitForm()" /> <input type="button"
								class="formbutton" onclick="javascript:history.go(-1);"
								value="Back" /></td>
						</tr>
						<%
							for (ScheduledEmpDto schduledEmployeeDto : scheduledEmpListIterated) {
										serialNo++;
										loginTimeList = logTimeDaoObj
												.getAllTimeWithProjectSpecific(
														schduledEmployeeDto.getProject(), "IN",
														site);
										logoutTimeList = logTimeDaoObj
												.getAllTimeWithProjectSpecific(
														schduledEmployeeDto.getProject(),
														"OUT", site);
						%>
						<tr>
							<td align="center">
								<%
								System.out.println("Checked date : "+ schduledEmployeeDto.getFrom_date());
								System.out.println("Checked date : "+ OtherFunctions.checkedDate(schduledEmployeeDto
										.getFrom_date()));
									if (OtherFunctions.checkedDate(schduledEmployeeDto
														.getFrom_date())) {
								%> <input type="checkbox" class="checkEmployee selectSchedule"
								id="employeecheck<%=serialNo%>" name="scheduleId"
								value="<%=schduledEmployeeDto.getScheduleId()%>" /> <%
 	}
 %>
							</td>
							<td align="center"><%=schduledEmployeeDto.getEmployeeId()%></td>
							<td align="center"><%=schduledEmployeeDto.getEmployeeName()%></td>

							<td align="center"><input class="projectdesc"
								name="projectdesc" id="projectdesc<%=serialNo%>" type="text"
								readonly
								value="<%=schduledEmployeeDto.getProjectDescription()%>"
								style="width: 150px" /> <input type="button" class="formbutton"
								value="...." id="projectbtn<%=serialNo%>" width="6"
								onclick="window.open('getproject.jsp?srno=<%=serialNo%>&projectSpecific=true','Ratting','width=400,height=250,left=150,top=200,toolbar=1,status=1,');" />
								<input type="hidden"
								name="project<%=schduledEmployeeDto.getScheduleId()%>"
								id="project<%=serialNo%>"
								value="<%=schduledEmployeeDto.getProject()%>" /> <img
								src="images/error.png" class="errorImage"
								id="projectdesc<%=serialNo%>_img" /></td>
							<td align="center"><%=OtherFunctions
								.changeDateFromatToddmmyyyy(schduledEmployeeDto
										.getFrom_date())%> <input type="hidden"
								name="fromDate<%=schduledEmployeeDto.getScheduleId()%>"
								value="<%=OtherFunctions
								.changeDateFromatToddmmyyyy(schduledEmployeeDto
										.getFrom_date())%>" /></td>
							<td align="center"><%=OtherFunctions
								.changeDateFromatToddmmyyyy(schduledEmployeeDto
										.getTo_date())%> <input type="hidden"
								name="toDate<%=schduledEmployeeDto.getScheduleId()%>"
								value="<%=OtherFunctions
								.changeDateFromatToddmmyyyy(schduledEmployeeDto
										.getTo_date())%>" /></td>
							<td align="center">
								<%
									if (schduledEmployeeDto.getMultistatus() == 0) {
													if (!OtherFunctions
															.checkedDateForAdmin(schduledEmployeeDto
																	.getFrom_date())) {
								%> &nbsp;
							</td>
							<td><input style="width: 60px" type="text" readonly
								name="logintime<%=schduledEmployeeDto.getScheduleId()%>"
								id="logintime" value="<%=schduledEmployeeDto.getLoginTime()%>">
									<%
										} else {
									%> <input type="checkbox"
									name="weeklyoff<%=schduledEmployeeDto.getScheduleId()%>"
									class="weeklyoff" /></td>
							<td><select class="loginTime"
								name="logintime<%=schduledEmployeeDto.getScheduleId()%>"
								id="logintime<%=serialNo%>">
									<option id="clearLoginTime" value="">Select</option>
									<%
										if (schduledEmployeeDto.getLoginTime()==null||schduledEmployeeDto.getLoginTime().equals(
																	"None")) {
									%>
									<option selected value="None">None</option>
									<%
										} else {
									%>
									<option value="None">None</option>
									<%
										}

															for (LogTimeDto logTimeDto : loginTimeList) {
																if ((logTimeDto.getLogTime())
																		.equals(schduledEmployeeDto
																				.getLoginTime())) {
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
							</select> <img src="images/error.png" class="errorImage"
								id="logintime<%=serialNo%>_img" /> <%
 	}
 				} else
 					out.write("&nbsp;</td><td>MULTIPLE");
 %></td>
							<td align="center">
								<%
									if (schduledEmployeeDto.getMultistatus() == 0) {
													if (!OtherFunctions
															.checkedDateForAdmin(schduledEmployeeDto
																	.getFrom_date())) {
								%> <input type="text" style="width: 60px" readonly
								name="logouttime<%=schduledEmployeeDto.getScheduleId()%>"
								id="logouttime<%=serialNo%>"
								value="<%=schduledEmployeeDto.getLogoutTime()%>"
								value="<%=schduledEmployeeDto.getLogoutTime()%>" /> <%
 	} else {
 %> <select name="logouttime<%=schduledEmployeeDto.getScheduleId()%>"
								class="logoutTime" id="logouttime<%=serialNo%>">

									<option id="clearLogoutTime" value="">Select</option>



									<%
										if (schduledEmployeeDto.getLogoutTime()==null||schduledEmployeeDto.getLogoutTime().equals(
																	"none")
																	|| schduledEmployeeDto.getLogoutTime()
																			.equals("None")) {
									%>
									<option selected value="None">None</option>
									<%
										} else {
									%>
									<option value="None">None</option>
									<%
										}
															for (LogTimeDto logTimeDto : logoutTimeList) {
																if ((logTimeDto.getLogTime())
																		.equals(schduledEmployeeDto
																				.getLogoutTime())) {
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
							</select> <img src="images/error.png" class="errorImage"
								id="logouttime<%=serialNo%>_img" /> <%
 	}
 				} else {

 					out.write("MULTIPLE");
 				}
 %>

							</td>
							<td><%=schduledEmployeeDto.getScheduledBy()%></td>
							<td><%=schduledEmployeeDto.getStatusDate()%></td>
							<td align="center"><a
								href="transadmin_schedule_alter.jsp?scheduleId=<%=schduledEmployeeDto.getScheduleId()%>&site=<%=site%>">Alter</a></td>
						</tr>
						<%
							}
						%>

						<tr>

							<td align="center" colspan="10"><input type="hidden"
								id="employeeCount" name="employeeCount" value="<%=serialNo%>" />
								<input type="submit" name="modify" class="formbutton"
								value="Modify" /> <input type="button" name="Cancel"
								class="formbutton" value="Cancel" onclick="submitForm()" /> <input
								type="button" class="formbutton"
								onclick="javascript:history.go(-1);" value="Back" /></td>
						</tr>

					</tbody>
				</table>

			</form>
			<table style="width: 40%">
				<tr>
					<td><a href="#" onclick="pervious()">Previous</a></td>

					<td id="pageInfo"><%=Integer.parseInt(startPos) + 1%> to <%=endPos%>
						of</td>
					<td><%=scheduledEmpList.size()%> is showing</td>
					<td><a href="#" onclick="next()">Next</a></td>
					<td><a href="#" onclick="showAll()">Show All</a></td>
				</tr>
				<tr>
					<td colspan="4"><input type="hidden" value="<%=RECORD_SIZE%>"
						id="recordsize" /> <input type="hidden" value="<%=endPos%>"
						id="endPos" /> <input type="hidden" value="<%=startPos%>"
						id="startPos" /> <input type="hidden"
						value="<%=scheduledEmpList.size()%>" id="listSize" /></td>
				</tr>
			</table>
			<%
				} else {
						if (site != null) {
							out.println("<P>NO Employee is Scheduled");
						}
					}
				} catch (Exception e) {
					System.out.println("error in schedule alter page " + e);
				}
			%>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
