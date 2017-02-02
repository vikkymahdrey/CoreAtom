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
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="error.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Your Team Schedule</title>


<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
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
    		 
    	 
    }
  //---search using site
	function searchForm()
	{
		//alert('hai ');
		 document.siteSearchForm.action="transadmin_scheduledemployee.jsp";
		  document.siteSearchForm.submit();
		return false;
	}
	
    </script>
<script type="text/javascript">
            var employeeCount;           
            function submitForm()
            {                
                var checkboxflag=false;
                employeeCount=document.getElementById("employeeCount").value;                            
                for(var i=1;i<=employeeCount;i++)
                {                                          
                    if($("input[id=employeecheck"+i+"]").is(":checked"))
                    {
                        checkboxflag=true;
                        break;
                    }
                                                     
                }
                if(checkboxflag)
                {
                        
                    document.schedule_emp.submit();    
                }
            }
            
            function Validate()
            {           
                var flag=false;                               
                var checkboxflag=false;
                employeeCount=document.getElementById("employeeCount").value;
                //alert(employeeCount);
                for(var i=1;i<=employeeCount;i++)
                {                                          
                    if($("input[id=employeecheck"+i+"]").is(":checked"))
                    {
                        checkboxflag=true;
                        if($("select[id=project"+i+"]").val()=="")
                        {
                            alert("Please Select Project");
                            $("select[id=project"+i+"]").focus();
                            flag=false;
                        }                                            
                        else if($("select[id=logintime"+i+"]").val()=="")
                        {
                            alert("Please Select Login Time");
                            $("select[id=logintime"+i+"]").focus();
                            flag=false;
                        }
                        else if($("select[id=logouttime"+i+"]").val()=="")
                        {
                            alert("Please Select logout");
                            $("select[id=logouttime"+i+"]").focus();                            
                            flag=false;
                        }
                       
                        else 
                        {    
                            flag=true;        
                        }                        
                    }    
                }
                if(!checkboxflag)
                {
                    alert("Please check the Check box");
                }
                return flag;                        
            }    
			 function showAuditLog(relatedId,moduleName){
            	var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
            	var size = "height=450,width=900,top=200,left=300," + params;
            	var url="ShowAuditLog.jsp?relatedNodeId="+relatedId+"&moduleName="+moduleName;	
                newwindow = window.open(url, 'AuditLog', size);

            	if (window.focus) {
            	newwindow.focus();
            }
            }
			 function showAll() {
	        		var site = document.getElementById("site").value;
	        		var listSize = document.getElementById("listSize").value;
	        		var startPos = 0;
	        		var endPos = listSize;
	        		window.location = "transadmin_scheduledemployee.jsp?site=" + site
	        				+ "&startPos=" + startPos + "&endPos=" + endPos;
	        	}
            function next() {	
        		var site = document.getElementById("site").value;
        		var startPos = document.getElementById("startPos").value;
        		var endPos = document.getElementById("endPos").value;
        		var listSize = document.getElementById("listSize").value;
        		startPos = parseInt(startPos) + parseInt(document.getElementById("recordsize").value);
        		endPos = parseInt(startPos) + parseInt(document.getElementById("recordsize").value);
        		
        		if (startPos > listSize) {
        			startPos = parseInt(startPos) - parseInt(document.getElementById("recordsize").value);
        			endPos = parseInt(startPos) + parseInt(document.getElementById("recordsize").value);
        		} else {
        			if (endPos > listSize) {
        				endPos = listSize;
        			}
        			window.location = "transadmin_scheduledemployee.jsp?site=" + site + "&startPos="
        					+ startPos + "&endPos=" + endPos;
        		}

        	}
        	function pervious() {

        		var site = document.getElementById("site").value;
        		var startPos = document.getElementById("startPos").value;
        		var endPos = document.getElementById("endPos").value;
        		//var listSize = document.getElementById("listSize").value;
        		endPos = startPos;
        		startPos = parseInt(startPos) - parseInt(document.getElementById("recordsize").value);
        		
        		if (startPos < 0) {
        			startPos = 0;			
        			endPos = parseInt(startPos) + parseInt(document.getElementById("recordsize").value);
        		} 
        		else
        			{

        			window.location = "transadmin_scheduledemployee.jsp?site=" + site + "&startPos="
        					+ startPos + "&endPos=" + endPos;
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
		ScheduledEmpDao ScheduledDaoObj = new ScheduledEmpDao();
		ArrayList<ScheduledEmpDto> scheduledEmpList;
		ArrayList<ProjectDto> projectList = null;
		ProjectDao projectDaoObj = new ProjectDao();
		LogTimeDao logTimeDaoObj = new LogTimeDao();
		String site = "";
		site = request.getParameter("site");
		/*starting special section for pagination middle */
		int RECORD_SIZE = 50;
		String startPos = request.getParameter("startPos");
		String endPos = request.getParameter("endPos");
		if (startPos == null && endPos == null) {
			startPos = "0";
			endPos = "" + RECORD_SIZE;
		}
		scheduledEmpList = ScheduledDaoObj.getScheduledEmp(site);
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
			<h3>Your Team Schedule</h3>
			<hr />
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
						</tr>
					</table>
				</form>
			</div>

			<br />
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
			<form name="schedule_emp" method="post" action="ScheduleModifyCancel"
				onsubmit="return Validate()">
				<table>
					<thead>
						<tr>
							<th>Emp Code</th>
							<th>Name</th>
							<th><%=SettingsConstant.PROJECT_TERM%></th>

							<th>From Date</th>
							<th>To Date</th>
							<th>Login</th>
							<th>Logout</th>
							<th>Scheduled By</th>
							<th>Updated on</th>
							<th></th>
							<th>Audit Log</th>
						</tr>
					</thead>
					<tbody>
						<tr height="30">
							<td colspan="9">&nbsp;</td>
						</tr>
						<%
							for (ScheduledEmpDto schduledEmployeeDto : scheduledEmpListIterated) {
						%>
						<tr>
							<td><%=schduledEmployeeDto.getEmployeeId()%></td>
							<td><%=schduledEmployeeDto.getEmployeeName()%></td>
							<td><%=schduledEmployeeDto.getProjectDescription()%></td>

							<td><%=OtherFunctions
							.changeDateFromatToddmmyyyy(schduledEmployeeDto
									.getFrom_date())%></td>
							<td><%=OtherFunctions
							.changeDateFromatToddmmyyyy(schduledEmployeeDto
									.getTo_date())%></td>
							
							
							<%if(schduledEmployeeDto.getMultistatus()==0)
							{
								out.write("<td>"+schduledEmployeeDto.getLoginTime()+"</td>");
								out.write("<td>"+schduledEmployeeDto.getLogoutTime()+"</td>");
							}
							else
							{
								out.write("<td>MULTIPLE</td>");
								out.write("<td>MULTIPLE</td>");
							}
							%>
							<td><%=schduledEmployeeDto.getScheduledBy()%></td>
							<td><%=schduledEmployeeDto.getStatusDate()%></td>
							<td><a
								href="transadmin_viewschedule.jsp?scheduleId=<%=schduledEmployeeDto.getScheduleId()%>">View</a></td>
							<td><input type="button" class="formbutton"
								onclick="showAuditLog(<%=schduledEmployeeDto.getScheduleId()%>,'<%=AuditLogConstants.SCHEDULE_EMP%>');"
								value="Audit Log" /></td>
						</tr>
						<%
							}
						%>
						<tr>
							<td colspan="9">&nbsp;</td>
						</tr>
					</tbody>
				</table>
				<div align="center">
					<input type="button" class="formbutton" class="formbutton"
						onclick="javascript:history.go(-1);" value="Back" />
				</div>
				<hr />

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
					if(site!=null){
					out.println("<P>NO Employee is Scheduled");
				}}
			%>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
