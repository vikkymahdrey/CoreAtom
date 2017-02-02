<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.dto.SchedulingDto"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dao.SchedulingDao"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dao.VehicleDao"%>
<%@page import="com.agiledge.atom.dto.VehicleDto"%>
<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
	<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="error.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 

<title>Select employees</title>
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
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script src="js/dateValidation1.js"></script>



<script type="text/javascript">


	$(document).ready(function() {
		
		 $("#from_date").datepick(); 
		 $("#to_date").datepick(); 
		 
		 
		
		
		  var fromdate = $("#from_date").val();
		 var todate = $("#to_date").val();  
		
		  $("#from_date").change(function(){
			
			 fromdate = $("#from_date").val();
			
		 });
		 
		 $("#to_date").change(function(){
			 
			 todate = $("#to_date").val(); 
		
			   
			   
		  }); 
		 
		
		
		   $("#site").change(function() {
			
			   /* var site = $("#site").val(); */  
			
			$("#selectedemployees option").remove();
			$("#employees option").remove();
			
			
			
		});  
		  
		 $('#remove').click(function() {  
			 
			 
			
			
			  $('#selectedemployees option:selected').remove().appendTo('#employees');  
			 }); 
		 try
			{
			 $('#add').click(function() {  
				
			  $('#employees option:selected').remove().appendTo('#selectedemployees');  
			 });
		 }
		 catch(e){alert(e);}
			 $('#submitbtn').click(function() {
				 try{
					  
			 $('#selectedemployees option').prop('selected', 'seleted');
			var flag = datevalidation(todate,fromdate);
			//alert(flag + "flag");
		      if(todate == "")
			    	alert("Please choose fromdate");
			 else if(fromdate == "")
			    	alert("Please choose todate");
			
			 else if(document.getElementById("selectedemployees").value == "")
				  alert("Please choose employees");
			
				 
				
			 else	if(!flag == 1) 
			 $("#employeeform").submit();
				 }catch(e){alert(e);}
			 });
	});
	
	function searchForm() {
		
		document.siteSearchForm.action = "empSelectForSchedule.jsp";
		document.siteSearchForm.submit();
		return false;
	}
	function datevalidation(to_date, from_date)
	{ 
	
		var flag = 0;
		var cur_date = new Date();
		var today = new Date();
	//	var todayevar = today.getDate() + "/" + today.getMonth() + "/"
	//			+ today.getFullYear();
		var todayevar = $("#curDate").val();
		var current_hour = cur_date.getHours();
		var next_day = new Date(cur_date
				.setDate(cur_date.getDate() + 1));
		var next_dayvar = next_day.getDate() + "/"
				+ next_day.getMonth() + "/" + next_day.getFullYear();
		var mon1 = parseInt(from_date.substring(3, 5), 10) - 1;
		var dt1 = parseInt(from_date.substring(0, 2), 10);
		var yr1 = parseInt(from_date.substring(6, 10), 10);
		var date1 = new Date(yr1, mon1, dt1);
		var advFifteen_day = new Date(date1.setDate(date1.getDate() + 30));

		var advFifteen_dayvar = advFifteen_day.getDate() + "/"
				+ advFifteen_day.getMonth() + "/"
				+ advFifteen_day.getFullYear();
		
		if (CompareTwodiffDates(to_date, from_date)) {
			flag = 1;
			alert("Invalid Date Range!\nFrom Date cannot be after To Date!");
		}
		if (CompareTwoDatesddmmyyyy_1(from_date, todayevar)) {
			flag = 1;
			alert("Invalid Date Range!\n From date Should  be after " + todayevar + " !");
		} 
		if (CompareTwoDatesddmmyyyy(from_date, next_dayvar)
				&& current_hour >= 23){
			flag = 1;
			alert("Invalid date. Cut off time is 11 pm to schedule for next day.");
		}
		if((to_date != "")&&(CompareTwoDatesddmmyyyy(to_date,
				advFifteen_dayvar)) == false) {
			flag =1;		
		   alert("Invalid Date Range!\n Date range cannot be more than 15 Days!");
		}
		
		return flag;
			
	}

	</script>
	<link href="css/style.css" rel="stylesheet" type="text/css" />
	<link href="css/menu.css" rel="stylesheet" type="text/css" />
	
</head>
<body>
<%    


try {


       String site  = request.getParameter("site");
     
		long empid = 0,empid1=0;
		int serialNo = 0;
		String employeeId = OtherFunctions.checkUser(session);
		String employeeId1="";
		if (session.getAttribute("delegatedId") != null) {
			employeeId1 = session.getAttribute("delegatedId").toString();
			empid1 = Long.parseLong(employeeId1);
		}
		
		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
			
			
			EmployeeDto spoc=new EmployeeDao().getEmployeeAccurate(employeeId);
			
			
	%><%@include file="Header.jsp"%>
	<%
		}

	%>

	<div id="body">
		<div class="content">
		<form name="siteSearchForm">
					<table style="width: 40%; border: 0px none;">
						<tr>
							<td><h2>Site</h2></td>
							<td>
								<input type="hidden" id="curDate" value="<%=SettingsConstant.getCurdate() %>" />
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
							

	<h2>Select employees</h2>
	<%SchedulingDao SchedulingDaoObj = new SchedulingDao();
	ArrayList<SchedulingDto> schedulingEmpList,delegatedlist;
	schedulingEmpList = SchedulingDaoObj.getSubscribedEmployeeDetailsByspoc(empid, site);
	if(empid1!=0){
		delegatedlist=SchedulingDaoObj.getSubscribedEmployeeDetailsByspoc(empid1, site);
		schedulingEmpList.addAll(delegatedlist);
	}
    /* if(schedulingEmpList != null)
	{
	for (SchedulingDto d : schedulingEmpList)
	{
		System.out.println(d.employeeId);
	}} else
	{
		out.println("No employees to schedule");
	}  */%>
	<%-- <%
		ArrayList<VehicleDto> vehicleDtos = null;
	%> --%>
	<form id="employeeform" name="selectemployeeform" action="scheduleSelectedEmp.jsp" method="post">
		<table>
			
			<tr>
			
			<td><select name="employees" id="employees" multiple="multiple" size = "20">
			<%if(schedulingEmpList != null)
			{
				for (SchedulingDto d : schedulingEmpList)
				{				
					 %>
				 
			
			
			<option value= " <%=d.getEmployeeId()%> ">  <%=d.getPersonnelNo()%> : <%=d.getEmployeeName() %>  : <%=d.getDescription() %></option>
			
			
			<% }} else
				{
					out.println("No employees to schedule");
				}  
				

				%>
			</select>
			</td>
			<td><input type="button" id="add" value=">>" class="formbutton"/></td>
			<td><input type="button" id="remove" class="formbutton" value='<<'/></td>
			<td><select name="selectedemployees" id="selectedemployees" multiple="multiple" size = "20"></select></td>			
			</tr>
				</table>
				<table>
			<tr>
			<th>From date </th> <th>To date</th> <!-- <th>Dual schedule</th> --></tr>
			<tr>
			<td class="td-schedulingFromDate"><input readonly="readonly"
							name="from_date"
							id="from_date" type="text" size="7"
							class="fromDate {showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                        minDate: new Date(2008, 12 - 1, 25)} " />
							<!-- <img src="images/error.png" class="errorImage"
							id="from_date_img" /> --></td>

						<td><input class="toDate" readonly="readonly"
							name="to_date"
							id="to_date" type="text" size="7"
							class="{showOtherMonths: true, firstDay: 1, dateFormat: 'dd/mm/yyyy',
                                        minDate: new Date(2008, 12 - 1, 25)}" />
							<!-- <img src="images/error.png" class="errorImage"
							id="to_date_img" /> --></td>
						<!-- 	 <td><select  name = "dualschedule" id = "dualschedule"><option >Yes</option><option selected = 'selected'>No</option></select></select></td> -->
			</tr>
			<tr><td></td><td></td><td></td><td><input type="hidden" name="source" value="formsubmit"/>
			<input id="submitbtn"  type="button" name="submitbtn" value="Submit" class="formbutton"/></td></tr>
			
			<input type="hidden" id="site" name="site" value="<%=site%>" />
			</table>
			
			<div id="datapart"></div>
			
	</form>
	<%
	}catch(Exception e) {
		System.out.println("Error in empSelectForSchedule.jsp :  " + e);
	}
	%>
	<%@include file="Footer.jsp"%>
	</div>
	</div>
</body>
</html>