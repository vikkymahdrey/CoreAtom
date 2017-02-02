<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.service.PaginationService"%>
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
    
  //---search using site
	function searchForm()
	{
		//alert('hai ');
		 document.siteSearchForm.action="scheduled_employee.jsp";
		  document.siteSearchForm.submit();
		return false;
	}
    function setActiveMenuItem()
    {
    	var url=window.location.pathname;
    	var filename=url.substring(url.lastIndexOf('/') + 1);
   //  $("li[class=active']").removeAttr("active");
  		 
  		
  		 
  		 
   		
    	 $("a[href='"+ filename + "']").parent().attr("class", "active");
    		 $("a[href='"+ filename + "']").parent().parent().parent('li').attr("class", "active") ;
    		 
    	 
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
            
            
        	function showAll() {	
        		var site = document.getElementById("site").value;	
        		var listSize = document.getElementById("listSize").value;
        		var endPos = document.getElementById("endPos").value;
        		var empFilter = document.getElementById("empFilter").value;
        		var Filter = document.getElementById("Filter").value;
        		var startPos = 0;
        		var endPos = listSize;		
        			/* window.location = "scheduled_employee.jsp?site=" + site + "&startPos="
        					+ startPos + "&endPos=" + endPos; */	
					window.location = "scheduled_employee.jsp?site=" + site + "&empFilter=" + empFilter + "&Filter=" +
        			
        			Filter + "&startPos="+ startPos + "&endPos=" + endPos;
        	}
            function next() {	
        		var site = document.getElementById("site").value;
        		var startPos = document.getElementById("startPos").value;
        		var endPos = document.getElementById("endPos").value;
        		var endPos = document.getElementById("endPos").value;
        		var empFilter = document.getElementById("empFilter").value;
        		var Filter = document.getElementById("Filter").value;
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
        			/* window.location = "scheduled_employee.jsp?site=" + site + "&startPos="
        					+ startPos + "&endPos=" + endPos; */
        			window.location = "scheduled_employee.jsp?site=" + site + "&empFilter=" + empFilter + "&Filter=" +
        			
        			Filter + "&startPos="+ startPos + "&endPos=" + endPos;
        		}

        	}
        	function pervious() {

        		var site = document.getElementById("site").value;
        		var startPos = document.getElementById("startPos").value;
        		var endPos = document.getElementById("endPos").value;
        		var endPos = document.getElementById("endPos").value;
        		var empFilter = document.getElementById("empFilter").value;
        		var Filter = document.getElementById("Filter").value;
        		//var listSize = document.getElementById("listSize").value;
        		endPos = startPos;
        		startPos = parseInt(startPos) - parseInt(document.getElementById("recordsize").value);
        		
        		if (startPos < 0) {
        			startPos = 0;			
        			endPos = parseInt(startPos) + parseInt(document.getElementById("recordsize").value);
        		} 
        		else
        			{

        			/* window.location = "schedule_employee.jsp?site=" + site + "&startPos="
        					+ startPos + "&endPos=" + endPos; */
					window.location = "scheduled_employee.jsp?site=" + site + "&empFilter=" + empFilter + "&Filter=" +
        			
        			Filter + "&startPos="+ startPos + "&endPos=" + endPos;
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
		long actEmpId = empid;
		if (session.getAttribute("delegatedId") != null) {
			employeeId = session.getAttribute("delegatedId").toString();
			empid = Long.parseLong(employeeId);
		}
		OtherDao ob = null;
		ob = OtherDao.getInstance();
		ScheduledEmpDao ScheduledDaoObj = new ScheduledEmpDao();
		ArrayList<ScheduledEmpDto> scheduledEmpList;
		ArrayList<ScheduledEmpDto> scheduledEmpListfirst;   
		ArrayList<ProjectDto> projectList = null;
		ProjectDao projectDaoObj = new ProjectDao();
		LogTimeDao logTimeDaoObj = new LogTimeDao();
		String site = "";
		site = request.getParameter("site");
		/*starting special section for pagination middle */
		int RECORD_SIZE = 50;
		String startPos = request.getParameter("startPos");
		String endPos = request.getParameter("endPos");
		String empFilter = request.getParameter("empFilter");
		String Filter = request.getParameter("Filter");
		empFilter = OtherFunctions.isEmpty(empFilter) == true ? "ALL"
				: empFilter;
		Filter = OtherFunctions.isEmpty(Filter) == true ? "ALL"
				: Filter;

		if (startPos == null && endPos == null) {
			startPos = "0";
			endPos = "" + RECORD_SIZE;
		}

		
	%>


	<div id="body">
		<div class="content">


			<br />
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
						
						
								<td>Filter</td>
							<td>
								<%
									try {
										EmployeeService empService = new EmployeeService();
										ArrayList<EmployeeDto> subordinateList = new ArrayList<EmployeeDto>();
										EmployeeDto empAllDto = new EmployeeDto();
										empAllDto.setEmployeeID("ALL");
										empAllDto.setDisplayName("--ALL--");
										empAllDto.setPersonnelNo("");
										EmployeeDto empSelfDto = new EmployeeDto();
										empSelfDto.setEmployeeID("SELF");
										empSelfDto.setDisplayName("--SELF--");
										empSelfDto.setPersonnelNo("");
										subordinateList.add(empAllDto);
										subordinateList.add(empSelfDto);
										System.out.println("Track....... " + empid);

										subordinateList.addAll(empService
												.getAllDirectSubordinate(String.valueOf(empid)));
										System.out.println("Size : " + subordinateList.size());

										if (subordinateList != null && subordinateList.size() > 0) {
								%> <select name="empFilter" id = "empFilter" onchange="return searchForm();">
									<%
										for (EmployeeDto edto : subordinateList) {
													String empSelect = "";
													if (OtherFunctions.isEmpty(empFilter) == false
															&& empFilter.equals(edto.getEmployeeID())) {
														empSelect = " selected ";
													}
									%>

									<option <%=empSelect%> value="<%=edto.getEmployeeID()%>"><%=edto.getDisplayName()%>&nbsp;&nbsp;--&nbsp;&nbsp;<%=edto.getPersonnelNo()%></option>
									<%
										}
									%>
							</select> 
							</td>
							<%
 	} %>
							
							<td id="Filter2"><select name="Filter" id="Filter" onchange="return searchForm();">
							
							
										
                <%String emp = request.getParameter("empFilter");
                EmployeeService empService2 = new EmployeeService();
				ArrayList<EmployeeDto> subordinateList2 = new ArrayList<EmployeeDto>();
				String select = "";
				System.out.println("filter value" + Filter );
				if (OtherFunctions.isEmpty(empFilter) == false 
						||(empFilter.equals("ALL"))) 
					{ 	select = "selected";%>
					
					<option <%=select %> value="ALL">ALL</option>
						<%}%>
						
						
			
						<%
						 if( !emp.equals("ALL")&& !emp.equals("SELF") )
				 {
				 
						subordinateList2.addAll(empService2
								.getAllDirectSubordinate(emp));
						System.out.println("Size of second list : " + subordinateList2.size());
					if (subordinateList2 != null && subordinateList2.size() > 0)
					{
					for(EmployeeDto edtos2 : subordinateList2)
					{   String empSelect = "";
						System.out.println("00"+edtos2.getEmployeeID());
						if (OtherFunctions.isEmpty(Filter) == false
								&& Filter.equals(edtos2.getEmployeeID())) 
						{
							empSelect = " selected ";
						}
					
					
				%><option  value="<%=edtos2.getEmployeeID()%>" > <%=edtos2.getDisplayName()%>&nbsp;&nbsp;--&nbsp;&nbsp;<%=edtos2.getPersonnelNo()%></option>
				
				<%}}} %>
				</select>
						</td>				

 <%	} catch (Exception e) {
 		System.out.println("Error : " + e);
 	}
 %>


				</tr>
				</table>
			</form>

			<%
			
			if(site!=null && !site.equals("") && !site.equals("") )
			{
				
				
				scheduledEmpList = new ArrayList<ScheduledEmpDto> ();
				scheduledEmpListfirst = ScheduledDaoObj.getScheduledEmp(actEmpId, empid,
						site);
				scheduledEmpList.addAll(scheduledEmpListfirst);
				String firstemp = request.getParameter("empFilter");
				String secondemp = request.getParameter("Filter");
				System.out.println("0"+ secondemp);
				
				
				System.out.println(firstemp + "----");
			if(firstemp.equalsIgnoreCase("SELF")&& secondemp.equalsIgnoreCase("ALL")){
				try{
				//if(firstemp.equalsIgnoreCase("SELF")){firstemp = employeeId;}
                 System.out.println("1");

 				ArrayList<ScheduledEmpDto> schedulesList = new ArrayList<ScheduledEmpDto> ();
 				   String flag = "false" ;
			//	firstemp = employeeId;
				for(ScheduledEmpDto empdto2 : scheduledEmpListfirst)
				{   
					if(empdto2.getEmployeeId().equals(employeeId) )
					{ 
						schedulesList.add(empdto2);
						
						if(schedulesList.size() > 0 )
						{
							
							scheduledEmpList.clear();
							scheduledEmpList.addAll(schedulesList);
							
							flag = "true";
							
						}
						
						
					}
				}	
				EmployeeService empService = new EmployeeService();
				ArrayList<EmployeeDto> subordinateList2 = new ArrayList<EmployeeDto>();
				subordinateList2.addAll(empService
						.getAllDirectSubordinate(String.valueOf(empid)));
				System.out.println("3--"+subordinateList2.size());
				ArrayList<ScheduledEmpDto> schedulesList2 = new ArrayList<ScheduledEmpDto> ();		
		if(subordinateList2.size()>0)
		{
			
			
			for(EmployeeDto subbemp : subordinateList2)
			{	 
			String subemployeeId = subbemp.getEmployeeID();
				for(ScheduledEmpDto empdto : scheduledEmpListfirst)
				{   
					if(empdto.getEmployeeId().equals(subemployeeId) )
					{     
						schedulesList2.add(empdto);
						
						if(schedulesList2.size() > 0 )
						{
							
							
							 if(flag.equalsIgnoreCase("false")) {
									
									
									scheduledEmpList.clear();
								   
								    }
							scheduledEmpList.add(empdto);
							
							flag = "true";
							
						}
						
						
					}
                  
						
				}
			}
		}
				
				 if(flag.equalsIgnoreCase("false")) {
						
						
						scheduledEmpList.clear();
					   
					    }
				}
				
				catch(Exception e){System.out.println("error occured"+ e);}
				}
			System.out.println("first emp" + firstemp );
			if(!(firstemp.equalsIgnoreCase("ALL")||firstemp.equalsIgnoreCase("SELF"))&& secondemp.equalsIgnoreCase("ALL"))
			{
				
				try{
			long firstemplong = 0;
			firstemplong = Long.parseLong(firstemp);
			ArrayList<ScheduledEmpDto> schedulesList3 = new ArrayList<ScheduledEmpDto> ();
			
			
			schedulesList3 = ScheduledDaoObj.getScheduledEmp(firstemplong,firstemplong,
					site);
			
			if(schedulesList3.size() > 0)
			{
				scheduledEmpList.clear();
				scheduledEmpList.addAll(schedulesList3);
			}
			
			else{
				scheduledEmpList.clear();
				
			}
				
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
				
			}	
				if(!firstemp.equalsIgnoreCase("ALL")&&!firstemp.equalsIgnoreCase("SELF") && !secondemp.equalsIgnoreCase("ALL"))
				{
					
					
					ArrayList<ScheduledEmpDto> schedulesList2 = new ArrayList<ScheduledEmpDto> ();
					String flag = "false";
					for(ScheduledEmpDto empdto2 : scheduledEmpListfirst)
					{   
						
						if(empdto2.getEmployeeId().equals(secondemp) )
						{
							schedulesList2.add(empdto2);
							
							if(schedulesList2.size() > 0)
							{
								scheduledEmpList.clear();
							scheduledEmpList.addAll(schedulesList2);
							flag = "true";
							}
							
							
						}
						
						
					}
					if(flag.equalsIgnoreCase("false"))
					{
						scheduledEmpList.clear();
					}
					
				
				}
				
			
				
				
			
			List<ScheduledEmpDto> scheduledEmpListIterated = new PaginationService()
					.getNext(scheduledEmpList, startPos, endPos);
			if (RECORD_SIZE > scheduledEmpList.size()) {
				endPos = "" + scheduledEmpList.size();
			}

			projectList = projectDaoObj.getProjects();
			int serialNo = 0;

				if (scheduledEmpList.size() > 0) {
			%>
			<h3>Your Team Schedule</h3>
			<hr />
			<br />

			<div></div>
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
			<br />
			<hr />

			<form name="schedule_emp" method="post" action="ScheduleModifyCancel"
				onsubmit="return Validate()">
				<table>
					<thead>
						<tr>
							<th>Emp Code</th>
							<th>Name</th>
							<th><%=SettingsConstant.PROJECT_TERM%></th>

							<th>From Date(dd/mm/yyyy)</th>
							<th>To Date(dd/mm/yyyy)</th>
							<th>Login</th>
							<th>Logout</th>
							<th>Scheduled By</th>
							<th>Updated on</th>
							<th></th>
							<th align="center">Audit Log</th>
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
							<td><%=schduledEmployeeDto.getEmployeePersonnelNo()%></td>
							<td><%=schduledEmployeeDto.getEmployeeName()%></td>
							<td><%=schduledEmployeeDto.getProjectDescription()%></td>

							<td><%=OtherFunctions
							.changeDateFromatToddmmyyyy(schduledEmployeeDto
									.getFrom_date())%></td>
							<td><%=OtherFunctions
							.changeDateFromatToddmmyyyy(schduledEmployeeDto
									.getTo_date())%></td>
							<%
								if (schduledEmployeeDto.getMultistatus() == 0) {
											out.write("<td>" + schduledEmployeeDto.getLoginTime()
													+ "</td>");
											out.write("<td>" + schduledEmployeeDto.getLogoutTime()
													+ "</td>");
										} else {
											out.write("<td>MULTIPLE</td>");
											out.write("<td>MULTIPLE</td>");
										}
							%>
							<td><%=schduledEmployeeDto.getScheduledBy()%></td>
							<td><%=schduledEmployeeDto.getStatusDate()%></td>
							<td><a
								href="schedule_modify.jsp?scheduleId=<%=schduledEmployeeDto.getScheduleId()%>">View</a></td>
							<td align="center"><input type="button" class="formbutton"
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
						out.println("<P>Employee not Scheduled");
					}
				}
			%>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
