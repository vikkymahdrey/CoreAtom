



<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="java.util.Date"%>
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
<title>Employee Subscription Report</title>
<style>
div.framed-outer.guttered #exportTypes {
	*top: -10px;
}
</style>

</head>
<body>
	<%
	String fname1=("SubscriptionReport :").concat(new Date().toString()).concat(".csv");
	String fname2=("SubscriptionReport :").concat(new Date().toString()).concat(".xls");
	String fname3=("SubscriptionReport :").concat(new Date().toString()).concat(".xml");
	
	
	
	
	
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String siteID = request.getParameter("siteId");
		String manager = request.getParameter("supervisorID1");
		String spoc = request.getParameter("supervisorID2");

		
		
		
		EmpSubscriptionReportHelper reportHelper = new EmpSubscriptionReportHelper();
		List<EmpSubscription> emplist = reportHelper
				.getAllSubscriptionDetails(fromDate, toDate, manager, spoc);

		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<h2>
				<center>Employee Subscription Report</center>
			</h2>
			<hr />

			<display:table name="<%=emplist%>" id="row" export="true"
				defaultsort="1" defaultorder="descending" pagesize="50">
				<display:column property="empID" title="Employee ID" sortable="true"
					headerClass="sortable" />
				<display:column property="empName" title="Employee Name"
					sortable="true" headerClass="sortable" />
				<display:column property="subscriptionDate"
					title="Subscription Date" format="{0,date,dd-MM-yyyy}"
					sortable="true" headerClass="sortable" />
				<display:column property="effectiveDate" title="Effective Date"
					format="{0,date,dd-MM-yyyy}" sortable="true" headerClass="sortable" />
				<display:column property="manager" title="<%=SettingsConstant.hrm%>" sortable="true"
					headerClass="sortable" />
				<display:column property="spoc" title="SPOC" sortable="true"
					headerClass="sortable" />
				<display:column property="area" title="Area" sortable="true"
					headerClass="sortable" />
				<display:column property="place" title="Place" sortable="true"
					headerClass="sortable" />
				<display:column property="landmark" title="Landmark" sortable="true"
					headerClass="sortable" />

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
