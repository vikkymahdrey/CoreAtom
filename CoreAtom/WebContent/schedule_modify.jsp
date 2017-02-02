
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.service.SchedulingAlterService"%>
<%@page import="com.agiledge.atom.dto.ScheduleAlterDto"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="com.agiledge.atom.dao.ProjectDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dto.ProjectDto"%>
<%@page import="com.agiledge.atom.dto.ScheduledEmpDto"%>
<%@page import="com.agiledge.atom.service.SchedulingService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="error.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Modify Schedule</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">
            function submitForm()
            {                
                document.shedule_modify.submit();    
            }
            
        </script>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script>   
    $(document).ready(function(){
    		 
    	 setActiveMenuItem();
   });
    
    function setActiveMenuItem()
    {
    	var url=window.location.pathname;
    	var filename=url.substring(url.lastIndexOf('/') + 1);
   //  $("li[class=active']").removeAttr("active");
  		 
  		
  		 
  		 
   		
    	 $("a[href='"+ filename + "']").parent().attr("class", "active");
    		 $("a[href='"+ filename + "']").parent().parent().parent('li').attr("class", "active") ;
    		// alert(filename);
    	 
    }
    </script>
<%
            long empid = 0;
            String employeeId = OtherFunctions.checkUser(session);          
                empid = Long.parseLong(employeeId);
                %>
<%@include file="Header.jsp"%>
<body>
	<div id="body">
		<div class="content">




			<%
                        long scheduleId = Long.parseLong(request.getParameter("scheduleId"));

                        ScheduledEmpDto schduledEmployeeDto = new SchedulingService().getSchedulingDetailBySchedule(scheduleId);
                        ArrayList<ProjectDto> projectList = null;
                        ProjectDao projectDaoObj = new ProjectDao();
                        projectList = projectDaoObj.getProjects();
                        LogTimeDao logTimeDaoObj = new LogTimeDao();

                        SchedulingAlterService schedulingAlterServiceObj = new SchedulingAlterService();
                        ArrayList<ScheduleAlterDto> scheduleDetailsList = new ArrayList<ScheduleAlterDto>();
                        scheduleDetailsList = schedulingAlterServiceObj.getScheduleDetalsByDate(scheduleId);




                    %>
			<form name="shedule_modify" action="ScheduleModify">
				<h3>Scheduled Details</h3>
				<div style="margin-left: 16%;">
					<table align="center" border="0" style="width: 100%">


						<tr>
							<td><input type="hidden" name="scheduleId"
								value="<%=scheduleId%>" /> Employee Id</td>
							<td><%=schduledEmployeeDto.getEmployeePersonnelNo()%></td>
						</tr>
						<tr>
							<td>Employee Name</td>
							<td><%=schduledEmployeeDto.getEmployeeName()%></td>
						</tr>
						<tr>
							<td>Scheduled By</td>
							<td><%=schduledEmployeeDto.getScheduledBy()%></td>
						</tr>
						<tr>
							<td>Scheduled From(dd/mm/yyyy)</td>
							<td><%=OtherFunctions.changeDateFromatToddmmyyyy(schduledEmployeeDto.getFrom_date())%></td>
						</tr>
						<tr>
							<td>Scheduled To(dd/mm/yyyy)</td>
							<td><%=OtherFunctions.changeDateFromatToddmmyyyy(schduledEmployeeDto.getTo_date())%></td>
						</tr>
						<tr>
							<td><%=SettingsConstant.PROJECT_TERM%></td>

							<td><%=schduledEmployeeDto.getProjectDescription()%> <%/*
							 if(!OtherFunctions.checkDate(schduledEmployeeDto.getFrom_date())){
								<input type="button" class="formbutton" value="...."
								id="projectbtn" width="6"
								onclick="window.open('getproject.jsp','Ratting','width=400,height=250,left=150,top=200,toolbar=1,status=1,');" />
								} <input type="hidden" name="project" id="project"
								value="<%=schduledEmployeeDto.getProject()" />*/ %></td>
						</tr>
						
						<%if(schduledEmployeeDto.getMultistatus()==0) {
						out.write("<tr><td>Login Time</td><td>"+schduledEmployeeDto.getLoginTime()+"</td></tr><tr><td>Logout Time</td><td>"+schduledEmployeeDto.getLogoutTime()+"</td></tr>");
						}
						else
						{
							out.write("<tr><td>Login Time</td><td>MULTIPLE</td></tr><tr><td>Logout Time</td><td>MULTIPLE</td></tr>");													
						}
						%>


					</table>
				</div>

			</form>

			<table border="0" style="width: 100%;" align="center">
				<thead>
					<tr>
						<td colspan="3"><h3>Scheduled Details For All Dates</h3></td>
					</tr>
					<tr>
						<th>Date(dd/mm/yyyy)</th>
						<th>Login</th>
						<th>Logout</th>
						<th>Updated on</th>
						<th>Updated By</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<tr>

						<%
                                    for (ScheduleAlterDto scheduleAlterDtoObj : scheduleDetailsList) {%>

						<td align="center"><%=OtherFunctions.changeDateFromatToddmmyyyy(scheduleAlterDtoObj.getDate())%></td>
						<td align="center"><%=scheduleAlterDtoObj.getLoginTime()%></td>
						<td align="center"><%=scheduleAlterDtoObj.getLogoutTime()%></td>
						<td align="center"><%=scheduleAlterDtoObj.getStatusDate()%></td>
						<td align="center"><%=scheduleAlterDtoObj.getUpdatedByDisplayName()%></td>
					</tr>
					<%}%>
					<tr>
						<td></td>
						<td></td>
						<td align="center">
							<form>
								<input class="formbutton" type="button"
									onclick="javascript:history.go(-1);" value="Back" />
							</form>
						</td>
					</tr>

				</tbody>
			</table>

			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
