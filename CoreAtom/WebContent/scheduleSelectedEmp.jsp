<%@page import="com.agiledge.atom.dto.ScheduleAlterDto"%>
<%@page import="com.agiledge.atom.service.SchedulingService"%>
<%@page import="com.agiledge.atom.dao.ScheduledEmpDao"%>
<%@page import="com.agiledge.atom.dto.ScheduledEmpDto"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.service.SchedulingAlterService"%>
<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.dto.SchedulingDto"%>
<%@page import="com.agiledge.atom.dao.SchedulingDao"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>

<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Selected employees</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/validate.js"></script>

<script src="js/JavaScriptUtil.js"></script>
<script src="js/Parsers.js"></script>
<script src="js/InputMask.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/jquery-latest.js"></script>

<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<style>
 select {
   
 	width:80px;
 	height: 30px;
 	font-size: 15px;
 	 
 	
 } 
   select   option {
   
 	 font-size: 15px;
 }   
 
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script src="js/dateValidation.js"></script>

<script type="text/javascript">

$(document).ready(function() {
	
	 //alert("hai");
	 try{
	 var dateCols = $("#dateCols").val();
		$(".vcheckbox").change(fillLogTimesV);
		$(".hcheckbox").change(fillLogTimesH);
		 var empcount = $("#empcount").val();
		 var scheemp = $("#scheemp").val();
		 var totalcount = $("#employeeCount").val();
			//alert("total emp count" + totalcount );
		// alert(empcount + " empcount" + scheemp + " scheduled emp" );
		 var count = 1;
		 var listcount =1;
		 var value;
		 var iterator =1;
	
		 for(value  = empcount  ; value <= scheemp ; value++)
			 
			 {
			
		 var listsize = $("#listsize"+value).val();
			 
		
		  for(listcount = 1 ; listcount<= listsize ; listcount++)
			{
			 
			 var date = $("#date"+value+listcount+value).val();
			
			 var login = $("#login"+value+listcount+value).val();
			 var logout = $("#logout"+value+listcount+value).val();
				
			 var num1 = date.split("-")[0];
				var num2 = date.split("-")[1];
				var num3 = date.split("-")[2];
				
		//alert(date);
		for(count =1 ; count <= dateCols ; count++)
	      {
		
		var date2 = $("#dates2"+count).val();
		//alert(date2 + "date2");
		
		
		
		var n1 = date2.split("/")[0];
		var n2 = date2.split("/")[1];
		var n3 = date2.split("/")[2];
		//alert(num1 + " " + num2 + " " + num3);
		//alert(n1 + " " +n2 + " "+ n3 );
		
		if(num1 == n3 && num3 == n1 && num2 == n2)
			{
			
			
			if (login==""|| login == null)
				{
				
				login = "none";
				}
			else if(login=="weekly off")
				{
				login = "Weekly off";
				}
			
			if(logout =="" || logout == null)
				{				
				logout = "none";				
				}
			else if(logout=="weekly off")
			{
				logout = "Weekly off";
			}
			
			
		    if(!login==""&&!logout =="")
		    	{
		    	
		    $("#logintime"+count+value+count).val(login);	
		    
		    $("#logouttime"+count+value+count).val(logout);
		     document.getElementById('logouttime'+count+value+count).style.color = 'red';
		    document.getElementById('logintime'+count+value+count).style.color = 'red'; 
		    
			//$(".loginTimeV_" + value).val(login);
		    	}
			} 
		
	      
	      } 
			}
		
	      } 
		
	
		 for(iterator = 1 ; iterator<=totalcount ; iterator++)
			{
		 var subeffectivedate = $("#subscritiondat"+iterator).val();
		// alert(subeffectivedate + "effective date");
		 for(count =1 ; count <= dateCols ; count++)
	      {
			    //alert("in");
				var date3 = $("#dates2"+count).val();
				//alert(date3);
				 if(CompareTwoDatesyyyymmddddmmyyyy(date3, subeffectivedate))
					{
				//	alert(date3 +" "+ subeffectivedate +"working" );
					 $("#logintime"+count+iterator+count).show();
					$("#logouttime"+count+iterator+count).show(); 
					//document.getElementById('logintime'+value+count).show();
					}
					
				else
					{
					//alert(date3 +" "+ subeffectivedate +"same");
				//	$("#logintime"+iterator+count).hide();
				//$("#logouttime"+iterator+count).hide();
					$("#logintime"+count+iterator+count).attr("disabled", true);
					$("#logouttime"+count+iterator+count).attr("disabled", true);
				//	$("#logintime"+value+count).attr("disabled", true);
					/* var data = "hhdh"
					$("#logintime"+value+count).val(data); */
					document.getElementById('logouttime'+count+iterator+count).style.color = '#e4e4e4';
					document.getElementById('logintime'+count+iterator+count).style.color = '#e4e4e4';
					//document.getElementById('logouttime'+iterator+count).
					//document.getElementById('logintime'+iterator+count).attr("disabled", true);
				
					}
			 
	      }
		
			} 
		 
	 } catch(e){alert(e);
		 
	 }		
			
});

function fillLogTimesV() {
	if ($(".vcheckbox").is(":checked"))
		{
	 	var i = $(this).attr("id").split("_")[1];
	 	//alert( $(this).attr("id"));
	
	 	/* var j = $(this).attr("id"); */
	 	
 	var firtLoginTime = $(".loginTimeV_" + i).first().val();
	var firtLogoutTime = $(".logoutTimeV_" + i).first().val();
	$(".loginTimeV_" + i).val(firtLoginTime);
	$(".logoutTimeV_" + i).val(firtLogoutTime);
}
	 
}

function fillLogTimesH() {
	if ($(".hcheckbox").is(":checked"))
		{
 	var i = $(this).attr("id").split("_")[1];
 	//alert("i=" +i);
var firtLoginTime = $(".loginTimeH_" + i).first().val();
var firtLogoutTime = $(".logoutTimeH_" + i).first().val();
$(".loginTimeH_" + i).val(firtLoginTime);
$(".logoutTimeH_" + i).val(firtLogoutTime);
 
}}

function autofillscheduleddates()

{
	alert(here);
	var dateCols = $("#dateCols").val();
	 
	var count = 1;
	for(count ; count <= dateCols ; count++)
      {
	var dateCols = $("#date"+count).val();
	alert(dateCols);
      }
	
	}
	
	  function onSubmit()
	{
		 try{
		
		 var empcount = $("#empcount").val();
		 var scheemp = $("#scheemp").val();
		 var dateCols = $("#dateCols").val();
		 var value = 0;
		var count = 0;
		 for(value=empcount  ; value <= scheemp ; value++)
			 {
		for(count =1 ; count <= dateCols ; count++)
	      {
		if( $("#logouttime"+count+value+count).val() == "" || $("#logintime"+count+value+count).val() == "" )
			{
			alert("Please choose login and logout for all the fields");
			return false;
			}
	
	      }
			 }
		 return true;
		 }
		 catch(ee)
		 {
			 alert(ee);
		 }
	} 
 
</script>
</head>
<body>
<%@ include file="Header.jsp" %>
<div id="body">
		<div class="content">
			<h3>Schedule Your Team</h3>

<%

long empid = 0;

String employeeId = OtherFunctions.checkUser(session);
empid = Long.parseLong(employeeId);
%>

<%
long actEmpId=empid;
if (session.getAttribute("delegatedId") != null) {
	employeeId = session.getAttribute("delegatedId").toString();
	empid = Long.parseLong(employeeId);
}

int serialNo = 0;
int datecount = 0;
int count =0;
ArrayList<String> datelist = new ArrayList<String>();
List<Date> result = new ArrayList<Date>();
String[] selectedemp = request.getParameterValues("selectedemployees");
String fromdate = request.getParameter("from_date");
String todate = request.getParameter("to_date");
//String spocdata = request.getParameter("spocdata");
String site= request.getParameter("site");
String dualschedule =  "no";/* request.getParameter("dualschedule");  */

//long spoc= Long.parseLong(spocdata);

ArrayList<ScheduledEmpDto> scheduledEmpList;
scheduledEmpList = new ArrayList<ScheduledEmpDto> ();
//SchedulingAlterService schedulingAlterServiceObj = new SchedulingAlterService();
/* ArrayList<ScheduleAlterDto> scheduleDetailsList = new ArrayList<ScheduleAlterDto>();	
scheduleDetailsList = schedulingAlterServiceObj
		.getScheduleDetalsByDate(scheduleId); */
		ArrayList<LogTimeDto> masterLoginTimeList = null;		
		ArrayList<LogTimeDto> masterLogoutTimeList = null;
		ArrayList<LogTimeDto> loginTimeList = null;
		ArrayList<LogTimeDto> logoutTimeList = null;
		LogTimeService logTimeDaoObj = new LogTimeService();
		masterLoginTimeList = logTimeDaoObj.getAllGeneralLogtime("IN",site);
		masterLogoutTimeList = logTimeDaoObj
				.getAllGeneralLogtime("OUT",site);
		
		try{
			//String from = "24/09/2014";
			//String to = "29/09/2014";
			DateFormat formatter ; 
					formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date  startDate = (Date)formatter.parse(fromdate);
			Date  toDate = (Date)formatter.parse(todate);
		
	    Calendar start = Calendar.getInstance();
	    start.setTime(startDate);
	    Calendar end = Calendar.getInstance();
	    end.setTime(toDate);
	    end.add(Calendar.DAY_OF_YEAR, 1); //Add 1 day to endDate to make sure endDate is included into the final list
	    while (start.before(end)) {
	        result.add(start.getTime());
	        start.add(Calendar.DAY_OF_YEAR, 1);
	       
	        
	        
			
		}
	   
		
		}
		catch(Exception e)
		{
			System.out.println("error in dates" + e);
		}
		
%>

			<hr />
			<br />

			<div>
				<form name="empschedule" action= "SelectedEmpSchedule" method = "post"  onsubmit = "return onSubmit()">
					<table style="width:40%">
					
					
						<tr>
						
						    <th>Id</th>
							<th>Employees</th>
							<th></th>
							<!-- <th>Process</th>
							<th>Scheduled upto</th> --> 
							
					        
				<% for(Date d : result)
			    {
			    	
			    	  SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E : dd/MM/yyyy");
			          String date = DATE_FORMAT.format(d);
			          if(dualschedule.equalsIgnoreCase("yes"))
			          {
			        	 
			        	  
			        	  datecount = datecount + 2; 
			        	  if(date.contains("Sun")||date.contains("Sat")){%>
			        	  <th colspan = '2' style="background-color: #F2F5A9"; ><%=date%></th>
			        	  <th colspan = '2' style="background-color: #F2F5A9";><font color='red'><%=date%></font></th>
			        <%  }else{%> <th colspan = '2' ><%=date%></th>
			        	  <th colspan = '2' ><font color='red'><%=date%></font></th> <% } } 
			          else if(dualschedule.equalsIgnoreCase("no")) { 
			        	 
			        	  datecount++;
			        	  if(date.contains("Sun")||date.contains("Sat"))
			        	  { 
			          
			           
			          
			         
			          %>
			           <th colspan = '2' style="background-color: #F2F5A9";><%=date%></th>  
			         <%   } else  { %> <th colspan = '2' ><%=date%></th><% }
			         
			       
			   
					    
					   
					
					    
					    }  } %>
				<tr>
					<td></td><td></td><td>
					<input type="hidden" value="<%=result.size() %>" id="dateCols" />
					</td>
					<%
					int empcount = 0;
					for(int i=0; i< result.size(); i ++) {
					%>
						<td colspan="2" ><input type="checkbox" id="v_<%=i %>" class="vcheckbox"  /></td>
					<%} %>
				</tr>
					
						
					
					<%try{ 
						%><tr> <%
						ScheduledEmpDao ScheduledDaoObj = new ScheduledEmpDao();
						scheduledEmpList = ScheduledDaoObj.getScheduledEmp(actEmpId, empid,
								site);
						SchedulingDao SchedulingDaoObj = new SchedulingDao();
						ArrayList<SchedulingDto> schedulingEmpList,delgatedlist;
						schedulingEmpList = SchedulingDaoObj.getSubscribedEmployeeDetailsByspoc(empid, site);
						if(actEmpId!=empid){
						delgatedlist=SchedulingDaoObj.getSubscribedEmployeeDetailsByspoc(actEmpId, site);
						schedulingEmpList.addAll(delgatedlist);
						}
						
						for(String emp :  selectedemp)
					     
						{ 
						serialNo++;
						for(SchedulingDto sdto : schedulingEmpList)
						{
							
							
						if(emp.trim().equalsIgnoreCase(sdto.getEmployeeId().trim()))
						{
							
							
					//	SchedulingDao scheduledao= new SchedulingDao();
						EmployeeDao empdao = new EmployeeDao();
								EmployeeDto empdto	= empdao.getEmployeeAccurate(emp);
							
						//SchedulingDto empscheduledto = scheduledao.getDetailsForSchedule(emp);
							
							
							%>
							
						<input type = "hidden"  id = "subscritiondat<%=serialNo%>"value="<%= sdto.getSubscriptionDate()%>"/>
					<td name = "personnelNo" id = "personnelNo" > <%=sdto.getPersonnelNo() %> </td>
					
					<td name = "employeename" id = employeename >  <%=sdto.getEmployeeName() %> </td>
					<td>
						<input type="checkbox" class="hcheckbox"  id="h_<%=serialNo %>" />					</td>	
					<%-- <td name = projectdesc id= "projectdesc">  <%=sdto.getDescription()%> </td>
					<td name = "scheduledupto"><%=OtherFunctions
 								.changeDateFromatToddmmyyyy(sdto.
 										getSchedulingToDate())%></td> --%>
					
					
					<% if (sdto.getProject() != null
							&& !sdto.getProject().equals("")) {

						loginTimeList = sdto.getLoginTimeDto();
						logoutTimeList = sdto.getLogoutTimeDto();

					} else {
						loginTimeList = masterLoginTimeList;
						logoutTimeList = masterLogoutTimeList;
					}
					
					int status = 0;
					
					  String scheduledates;
					
				 	for( ScheduledEmpDto schduledemp: scheduledEmpList)
					{
						
					 if(schduledemp.getEmployeeId().trim().equalsIgnoreCase(sdto.getEmployeeId().trim()))
					 {
						 empcount++;
						 String scheduleid = schduledemp.getScheduleId();
						 long scheduledid = Long.parseLong(scheduleid);
						
						 /* ScheduledEmpDto schduledEmployeeDto = new SchedulingService().getSchedulingDetailBySchedule(scheduledid);
						 schduledEmployeeDto.getLoginTime(); */
						 SchedulingAlterService schedulingAlterObj = new SchedulingAlterService();
	                        ArrayList<ScheduleAlterDto> scheduleDetailsList = new ArrayList<ScheduleAlterDto>();
	                        scheduleDetailsList = schedulingAlterObj.getScheduleDetalsByDate(scheduledid);
	                        for(ScheduleAlterDto sadto :scheduleDetailsList)
	                        	
	                        {   
	                        	status++;
	                        	sadto.getDate();
	                        	sadto.getLoginTime();
	                        	sadto.getLogoutTime();
	                        	
	                  /*       	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
			                     Date schdate = DATE_FORMAT.parse(sadto.getDate());
		System.out.println( schdate);
		System.out.println(DATE_FORMAT.format( schdate)); */
		//System.out.println("verfify " +schduledemp.getEmployeePersonnelNo()+ " "+sadto.getDate() + " " + sadto.getLoginTime() + " " +sadto.getLogoutTime()  + " " +status + " " + serialNo);%>
			                      
			                      
	                        	<input type = "hidden" id = "date<%=serialNo%><%=status%><%=serialNo%>" class = "scheduledate" value = "<%=sadto.getDate()%>"/>
	                        	<input type = "hidden" id = "login<%=serialNo%><%=status%><%=serialNo%>" class = "schedulelogin" value = "<%=sadto.getLoginTime()%>"/>
	                        	<input type = "hidden" id = "logout<%=serialNo%><%=status%><%=serialNo%>" class = "schedulelogout" value = "<%=sadto.getLogoutTime()%>"/>
	                      <% } %>
	                        
					 
					<% }empcount = serialNo; }%> <input type = "hidden" id = "empcount" value = "<%=serialNo %>" />
	                        <input type = "hidden" id = "listsize<%=serialNo%>" value = "<%=status %>" />
	                        
					 
					
					
					   
					
					<%for(int i = 1 ; i<= datecount ; i++) {
						String currdate = request.getParameter("date"+i);
						%>
					     
					
						
 		                    <td id="td-login<%=sdto.getPersonnelNo()%><%=i%>">
 		                    <select  
							name="logintime<%=sdto.getPersonnelNo()%><%=i%>" class="loginTimeH_<%=serialNo %> loginTimeV_<%=i - 1 %>" 
							id="logintime<%=i%><%=serialNo%><%=i%>">

								<option value="">Login</option>
								<option value="none">None</option>
								<option value="Weekly off">Weekly off</option>
								<%
							 
									for (LogTimeDto logTimeDto : loginTimeList) {
								%>
								<option value="<%=logTimeDto.getLogTime()%>"><%=logTimeDto.getLogTime()%></option>
								<%
							 
									}
								%> 
						</select>
							<td id="td-logout<%=sdto.getPersonnelNo()%><%=i%>"><select  
							name="logouttime<%=sdto.getPersonnelNo()%><%=i%>"
							class="logoutTimeH_<%=serialNo %> logoutTimeV_<%=i - 1 %>"
							id="logouttime<%=i%><%=serialNo%><%=i%>">

								<option value="">Logout</option>
								<option value="none">None</option>
								<option value="Weekly off">Weekly off</option>
								 <%
								  
									for (LogTimeDto logTimeDto : logoutTimeList) {
								%>
								<option value="<%=logTimeDto.getLogTime()%>"><%=logTimeDto.getLogTime()%></option>
								<%
								 
									}
								%> 
						</select>
						
						</td>
							
						
						<%}   
						%> 
						
					 <td ><input type = "hidden" name = "subscriptionid<%=serialNo%>" id = "subscriptionid<%=serialNo%>"  value = "<%=sdto.getSubscriptionId() %>"  /></td>
					<% %>
					  <td ><input type = "hidden"  name = "project<%=serialNo%>" id = "project<%=serialNo%>" value ="<%=sdto.getDescription() %>"/>  </td>
					   <td ><input type = "hidden"  name = "personnelno<%=serialNo%>" id = "personnelno<%=serialNo%>" value ="<%=sdto.getPersonnelNo()%>"/>  </td>
					   <td ><input type = "hidden"  name = "eid<%=serialNo%>" id = "eid<%=serialNo%>" value ="<%=sdto.getEmployeeId()%>"/>  </td>
					  <td ><input type = "hidden"  name = "fromdate" id = "fromdate" value ="<%=fromdate %>"/>  </td>
					  <td ><input type = "hidden"  name = "todate" id = "todate" value ="<%=todate %>"/>  </td>
						</tr>
						<%}}}} catch(Exception e)
					   {System.out.println("error" + " "+ e);
					   }%>
					   
				
					  
					   <tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td colspan="5" align="center"><input type="submit"
							class="formbutton" name="submit" value="Schedule" style="width: 70px" />&nbsp;
							<input type="button" class="formbutton" value="Back"
							onclick="javascript:history.go(-1);" /> <input type="hidden"
							id="employeeCount" name="employeeCount" value="<%=serialNo%>" />
							<input type="hidden"
							id="site" name="site" value="<%=site%>" />
							<input type="hidden"
							id="datecount" name="datecount" value="<%=datecount%>" />
						</td>
					</tr>
						
				
					   	   <% for(Date d : result)
			    {
			    	
			    	  SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
			          String date = DATE_FORMAT.format(d);
			        //  count++;
			          if(dualschedule.equalsIgnoreCase("Yes"))
			          {
			        	//  count = count + 2;
			        	  datelist.add(date);
			        	  datelist.add(date);
			          }
			          else
			          {
			        	 // count++;
			        	  datelist.add(date);
			          }
			    }
					   	   for (String a : datelist)
					   	   {
			              count++; %> 
			      <td> <input type="hidden"
							id="dates2<%=count%>" name="date<%=count%>" value="<%=a%>" />
							</td> 
							<%} %> 
							<input type = "hidden" id = "scheemp" value = "<%=empcount%>" />
					    
					
						
						
 		                     
 		              
 		           
 		              
 		                   							
							</table>
							</form>
							</div>
							<%@include file="Footer.jsp"%>

</body>

</html>