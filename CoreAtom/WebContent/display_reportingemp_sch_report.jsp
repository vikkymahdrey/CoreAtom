
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="com.agiledge.atom.reports.dto.EmpSchedule"%>
<%@page import="com.agiledge.atom.reports.EmpScheduleReportHelper"%>
<%@page import="com.agiledge.atom.reports.dto.EmpSubscription"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.reports.EmpSubscriptionReportHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Schedule Report</title>

<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">
	function showPopup(url) {

		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		size = "height=450,width=520,top=200,left=300," + params;
		if (url == "LandMarkSearch.jsp") {
			size = "height=450,width=600,top=200,left=300," + params;
		} else if (url == "SupervisorSearch1.jsp"
				|| url == "SupervisorSearch2.jsp") {
			size = "height=450,width=700,top=200,left=300," + params;
		} else if (url == "termsAndConditions.html") {
			size = "height=450,width=520,top=200,left=300," + params;
		}

		newwindow = window.open(url, 'name', size);

		if (window.focus) {
			newwindow.focus();
		}
	}

	function resetFields()
	{
		$("select[name='shiftInTime']").children().removeAttr("selected");
		$("select[name='shiftOutTime']").children().removeAttr("selected");
		$("input[type='text']").val("");
		//alert('..');
		$("input[type='hidden']").val("");
		//alert('..');
	}
	function openWinodw(url) {
		window.open(url, 'Ratting',
				'width=400,height=250,left=150,top=200,toolbar=1,status=1,');

	}
	function closeWindow() {
		// opener.document.getElementById("").value =       
		try {
			var selTag = document.getElementById("ProjectList");
			var selectedText = selTag.options[selTag.selectedIndex].text
			var selectedTextValue = selTag.value;
			var selectedTextContent = selectedText.split("-");

			opener.document.getElementById("projectdesc").value = selectedTextContent[1];
			opener.document.getElementById("project").value = selectedTextValue;
			//`self.close();

		} catch (e) {
			alert(e);
			alert("Process Not Selected")
		}
	}
</script>

<script type="text/javascript">
	$(document).ready(function() {
		$("#fromDate").datepick();
		$("#toDate").datepick();
	});
</script>
<script type="text/javascript">

function showPopup(url) {

    var params="toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
    size="height=450,width=520,top=200,left=300,"+params;
    if(url=="LandMarkSearch.jsp")
        {
            size="height=450,width=600,top=200,left=300," + params;
        }
    else if(url=="SupervisorSearch1.jsp" || url=="SupervisorSearch2.jsp"  )
           {
               size="height=450,width=700,top=200,left=300,"+params;
           }
    else if(url=="termsAndConditions.html")
        {
               size="height=450,width=520,top=200,left=300,"+params;
        }

    newwindow=window.open(url,'name',size);
    

     if (window.focus) {newwindow.focus();}
    }
	function validate() {

		var fromdate = document.getElementById("fromDate").value;
		var todate = document.getElementById("toDate").value;
		if (fromdate.length < 1) {
			alert("Choose From Date");
			//  date.focus();
			return false;

		}
		if (todate.length < 1) {
			alert("Choose To Date");
			//  date.focus();
			return false;

		} else {
			return true;
		}

	}
	
	
	
	
	 function getLogTime()
     {                    
         var site=document.getElementById("siteId").value;       
         var url="GetLogTime?site="+site;                                    
         xmlHttp=GetXmlHttpObject()
         if (xmlHttp==null)
         {
             alert ("Browser does not support HTTP Request");
             return
         }                    
         xmlHttp.onreadystatechange=setLogTime	
         xmlHttp.open("GET",url,true)                
         xmlHttp.send(null)                  	
     }
         
     function GetXmlHttpObject()
     {
         var xmlHttp=null;
         if (window.XMLHttpRequest) 
         {
             xmlHttp=new XMLHttpRequest();
         }                
         else if (window.ActiveXObject) 
         { 
             xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
         }

         return xmlHttp;
     }
 
     function setLogTime() 
     {                      
         if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
         { 
             var returnText=xmlHttp.responseText;
             var logtime=returnText.split("|");
             var logtimetd=document.getElementById("logtimetd");
             logtimetd.innerHTML='<select  name="shiftInTime"><Option value="">All</Option>'+logtime[0]+'</select>';
             logtimetd.innerHTML+='OUT <select  "shiftOutTime" style="margin-left: 3%"><Option value="">All</Option>'+logtime[1]+'</select>';
         }
     }
     

	
	
	
	
	
	
	
	
</script>


</head>
<body>
	<%

	long empid = 0;
	String employeeId = OtherFunctions.checkUser(session);
	
	
	String fname1=("ScheduleReport :").concat(new Date().toString()).concat(".csv");
	String fname2=("ScheduleReport :").concat(new Date().toString()).concat(".xls");
	String fname3=("ScheduleReport :").concat(new Date().toString()).concat(".xml");

		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String siteID = request.getParameter("siteId");
	//	String manager = request.getParameter("supervisorID1");
	    String empId = request.getParameter("employeeId");
		String spoc = employeeId;
		String project = request.getParameter("project");
		String shiftInTime=request.getParameter(("shiftInTime"));
		String shiftOutTime=request.getParameter(("shiftOutTime"));
		
		EmpScheduleReportHelper reportHelper = new EmpScheduleReportHelper();
		List<EmpSchedule> emplist=null;
	
		if(fromDate!=null&&toDate!=null)
		{
		 emplist = reportHelper.getActiveScheduleDetails(fromDate, toDate, siteID, spoc, project, shiftInTime, shiftOutTime, empId);
		 
		}
		
        
	%>		
		
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<div>
		
				<form name="form1" action="display_reportingemp_sch_report.jsp" method="POST"
					onsubmit="return validate()">
					<table>
						<tr>
							<td align="right">Choose Site</td>
							<td><select name="siteId" id="siteId" onchange="getLogTime()">
							<%
							String site=(request.getSession().getAttribute("site")==null||request.getSession().getAttribute("site").toString().trim().equals(""))?"" :request.getSession().getAttribute("site").toString().trim();
																if(site==null||site.equals("0"))
									{%>
									<option>Select</option>	
									<%}								
									
									 List<SiteDto> siteDtoList = new SiteDao().getSites();  
									 
									if(siteID!=null&&siteID.trim().equals("")==false)
									{
										site=siteID;
									} 
									for (SiteDto siteDto : siteDtoList) {
									
									String siteSelect="";
									if(site.equals(siteDto.getId()))
									{
										siteSelect="selected";
									}
								 
								%>

									<option <%=siteSelect %> value="<%=siteDto.getId()%>"><%=siteDto.getName()%></option>
									<%  }%>
							</select></td>


							<td align="right">From</td>
							<td align="left"><input name="fromDate" id="fromDate"
								type="text" size="6"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=fromDate!=null&&fromDate.trim().equals("")==false?fromDate:"" %>" />




								To <input name="toDate" id="toDate" type="text" size="6"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=toDate!=null&&toDate.trim().equals("")==false?toDate:"" %>" />



							</td>



							<td align="right">IN</td>

							<td align="left" colspan="3" id="logtimetd"><select name="shiftInTime"><option
										value="">--All--</option>
									<%
								  String shiftInValue=request.getParameter("shiftInTime")!=null&&request.getParameter("shiftInTime").trim().equals("")==false?request.getParameter("shiftInTime"):"";
								 String shiftOutValue=request.getParameter("shiftOutTime")!=null&&request.getParameter("shiftOutTime").trim().equals("")==false?request.getParameter("shiftOutTime"):"";
							 ArrayList<LogTimeDto> timeList=new LogTimeService().getAllLogtime("IN",site);
							 if(timeList!=null&&timeList.size()>0)
							 {
								 
							 	for(LogTimeDto timeDto: timeList)
							 	{
							 		String selectShiftIn="";
							 		if(shiftInValue.equals(timeDto.getLogTime()))
							 			selectShiftIn="selected";
							 %>
									<option <%=selectShiftIn %> value="<%=timeDto.getLogTime() %>"><%=timeDto.getLogTime() %></option>
									<%}
							 	}%>
							</select> OUT<select name="shiftOutTime" style="margin-left: 3%"><option
										value="">--All--</option>
									<%
							   timeList=new LogTimeService().getAllLogtime("OUT",site);
							 if(timeList!=null&&timeList.size()>0)
							 {
								 
							 	for(LogTimeDto timeDto: timeList)
							 	{
									String selectShiftOut="";
							 		if(shiftOutValue.equals(timeDto.getLogTime()))
							 			selectShiftOut="selected";
							 %>
									<option <%=selectShiftOut %> value="<%=timeDto.getLogTime() %>"><%=timeDto.getLogTime() %></option>
									<%}
							 	}%>
							</select></td>



						</tr>
						<tr>
							<td align="right">Employee</td>

							<td><input type="hidden" name="employeeId" id="employeeID"
								value="<%=request.getParameter("employeeId")!=null&&request.getParameter("employeeId").trim().equals("")==false?request.getParameter("employeeId"):"" %>" />
								<input type="text" readonly name="employeeName"
								id="employeeName" onclick="showPopup('EmployeeSearch.jsp')"
								value="<%=request.getParameter("employeeName")!=null&&request.getParameter("employeeName").trim().equals("")==false?request.getParameter("employeeName"):"" %>" />
								<label for="employee" class="requiredLabel"></label> <input
								class="formbutton" type="button" value="..."
								onclick="showPopup('EmployeeSearch.jsp')" /></td>
							<!--
						<td align="right"> Supervisor </td>
						<td><%-- <input type="hidden" name="supervisorID1"
							id="supervisorID1"
						 value="<%=request.getParameter("supervisorID1")!=null&&request.getParameter("supervisorID1").trim().equals("")==false?request.getParameter("supervisorID1"):""%>"
							 /> <input type="text" readonly
							name="supervisorName1" id="supervisorName1"
							onclick="showPopup('SupervisorSearch1.jsp')"
								 value="<%=request.getParameter("supervisorName1")!=null&&request.getParameter("supervisorName1").trim().equals("")==false?request.getParameter("supervisorName1"):""%>"
							/> <label
							for="supervisorID1" class="requiredLabel"></label> <input
							class="formbutton" type="button" value="..."
							onclick="showPopup('SupervisorSearch1.jsp')" /> --%></td>
-->

							

							<td align="right"><%=SettingsConstant.PROJECT_TERM%></td>
							<td><input type="text" name="projectdesc" id="projectdesc"
								readonly="readonly"
								value="<%=request.getParameter("projectdesc")!=null&&request.getParameter("projectdesc").trim().equals("")==false?request.getParameter("projectdesc"):""%>" />
								<input type="hidden" name="project" id="project"
								value="<%=request.getParameter("project")!=null&&request.getParameter("project").trim().equals("")==false?request.getParameter("project"):""%>" />
								<input type="button" class="formbutton" value="..."
								onclick="openWinodw('getproject.jsp' ); " /></td>
							<td>&nbsp;</td>
							<td><input type="submit" class="formbutton"
								value="Generate Report" /> <input type="button"
								class="formbutton" value="Reset" onclick="resetFields();"   /></td>
						</tr>
					</table>
				</form>
				<hr />
				<br />
			</div>
			<h2 align="center">Schedule Report</h2>
			<hr />
			<display:table class="alternateColor" name="<%=emplist%>" id="row"
				export="true" defaultsort="1" defaultorder="descending"
				pagesize="50">
				<display:column property="date" title="Date" 
					format="{0,date,dd/MM/yyyy}" sortable="true" headerClass="sortable"  sortProperty="date" />
				<display:column property="empID" title="Employee ID" sortable="true"
					headerClass="sortable" />
				<display:column property="empName" title="Employee Name"
					sortable="true" headerClass="sortable" />
			
				 
				<display:column property="manager" title="<%=SettingsConstant.hrm%>" sortable="true"
					headerClass="sortable" />
				<display:column property="spoc" title="SPOC" sortable="true"
					headerClass="sortable" />
				<display:column property="loginTime" title="Login Time"
					sortable="true" headerClass="sortable" />
				<display:column property="logoutTime" title="Logout Time"
					sortable="true" headerClass="sortable" />
				<display:column property="projectID" title="<%=SettingsConstant.PROJECT_TERM%>" sortable="true"
					headerClass="sortable" />
				<display:column property="projectName" title="Project Name"
					sortable="true" headerClass="sortable" />

				<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
				<display:setProperty name="export.excel.filename"
					value="<%=fname2%>" />
				<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
			</display:table>
		
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
