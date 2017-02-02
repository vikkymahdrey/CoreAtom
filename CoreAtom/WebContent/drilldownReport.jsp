




<%@page import="com.agiledge.atom.reports.dto.TripSheetNoShowCountReportDto"%>
<%@page import="com.agiledge.atom.reports.TripSheetNoShowCountReportHelper"%>
<%@page import="java.util.Date"%>
 
<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
 
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Trip Sheet No Show Report</title>


</head>
<body>
	<%
	String fname1=("DrillDownReport :").concat(new Date().toString()).concat(".csv");
	String fname2=("DrilDownReport :").concat(new Date().toString()).concat(".xls");
	String fname3=("DrillDownReport :").concat(new Date().toString()).concat(".xml");
	
	
	
	
		//String monthParam = request.getParameter("month");
		String monthParam = request.getParameter("monthvalue");
		String yearParam = request.getParameter("year");
		String Site=OtherFunctions.getEmptyIfNull(request.getParameter("Site"));
		String User=OtherFunctions.getEmptyIfNull(request.getParameter("User"));
		String Project=OtherFunctions.getEmptyIfNull(request.getParameter("Project"));
		String SPOC=OtherFunctions.getEmptyIfNull(request.getParameter("SPOC"));
		String Manager=OtherFunctions.getEmptyIfNull(request.getParameter("Manager"));
		 
		 


		TripSheetNoShowCountReportHelper reportHelper = new TripSheetNoShowCountReportHelper();
		 List<TripSheetNoShowCountReportDto> dtoList = reportHelper.getNoShowCountEmployees(monthParam,yearParam,Site,Project,SPOC, Manager, User);
			 

		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<div id="body">

		<div class="content"></div>
		<h2 align="center">No Show Report</h2>
		<hr />

		<display:table class="alternateColor" name="<%=dtoList%>" id="row"
			export="true" defaultsort="1" defaultorder="descending" pagesize="50">
			<display:column property="employeeId" title="Employee#"
				sortable="true" headerClass="sortable" />
			<display:column property="employeeName" title="Employee Name"
				sortable="true" headerClass="sortable" />
			<display:column property="project" title="Project/Unit" sortable="true"
				headerClass="sortable" />
			<display:column property="scheduledBy" title="Scheduled By"
				sortable="true" headerClass="sortable" />

			<display:column property="tripDate" title="Trip Date" sortable="true"
				headerClass="sortable" />
			<display:column property="shiftTime" title="Shift Time"
				sortable="true" headerClass="sortable" />
			<display:column property="tripLog" title="Type" sortable="true"
				headerClass="sortable" />
			<display:column property="reason" title="Reason" sortable="true"
				headerClass="sortable" />

			<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
			<display:setProperty name="export.excel.filename" value="<%=fname2%>" />
			<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
		</display:table>
		<br /> <br /> <input type="button" value="Back" class="formbutton"
			onclick="javascript:history.go(-1);" /> <br /> <br />

		<%@include file="Footer.jsp"%>
	</div>

	</div>

</body>
</html>
