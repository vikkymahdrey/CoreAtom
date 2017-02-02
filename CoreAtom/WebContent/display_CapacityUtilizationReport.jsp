



<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.reports.dto.CapacityUtilizationReportDto"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.reports.dto.ProjectWiseTripBillDto"%>
<%@page import="com.agiledge.atom.reports.TripSheetNoShowCountReportHelper"%>
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
<title>Capacity Utilization Report</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">

function resetFields()
{
	$("input[type=hidden]").val("");
}
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
</script>

<script type="text/javascript">        
            $(document).ready(function()
            {                                                                        
                $("#fromDate").datepick();
                $("#toDate").datepick();
            });     
        </script>
<script type="text/javascript">
            function validate()
            {
                  
                var fromdate=document.getElementById("fromDate").value;
                var todate=document.getElementById("toDate").value;
                if(fromdate.length<1)
                {
                    alert("Choose From Date");
                  //  date.focus();
                    return false;
                        
                }
                if(todate.length<1)
                {
                    alert("Choose To Date");
                  //  date.focus();
                    return false;
                        
                }
                 else
                {
                    return true;                            
                }
               
            }
        </script>

</head>
<body>	
	<div id="body">
		<div class="content">
			<%
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String siteID = request.getParameter("siteId");
		String logTimeIn = request.getParameter("shiftInTime");
		String logTimeOut = request.getParameter("shiftOutTime");
		String vehicleTypeId = request.getParameter("vehicleTypeId");
		
		String fname1=("CapacityReport :").concat(new Date().toString()).concat(".csv");
		String fname2=("CapacityReport :").concat(new Date().toString()).concat(".xls");
		String fname3=("CapacityReport :").concat(new Date().toString()).concat(".xml");		 
		TripSheetNoShowCountReportHelper reportHelper = new TripSheetNoShowCountReportHelper();
		List<CapacityUtilizationReportDto> emplist = reportHelper
				.getCapacityUtilizationReportAvailableVsPlanned(fromDate, toDate, siteID, logTimeIn,logTimeOut,vehicleTypeId);
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>

			<div>
		 
			</div>

			<hr />
			<hr />
			<br />
			<h2 align="center">Capacity Utilization Report</h2>
			<hr />
			<%
			TotalTableDecorator grandTotals = new TotalTableDecorator();

			grandTotals.setTotalLabel("Full Total");
			grandTotals.setSubtotalLabel("Days Summary");
			pageContext.getRequest().setAttribute("tableDecor", grandTotals);
		%>
			<display:table class="alternateColorInSub" name="<%=emplist%>"
				id="row" export="true" defaultsort="1" defaultorder="descending"
				pagesize="50" decorator="tableDecor">
				<display:column property="tripDate" title="Date" sortable="true"
					headerClass="sortable"  format="{0,date,dd/MM/yyyy}" />
				<display:column property="vehicleTypeName" title="Vehicle Type"
					sortable="true" headerClass="sortable" />
				<display:column property="tripTime" title="Trip Time"
					sortable="true" headerClass="sortable" />
				<display:column property="tripLog" title="Trip Log"
					sortable="true" headerClass="sortable" />

				<display:column property="avaiableCapacity" title="Available Capacity"
					sortable="true" format="{0,number,0}" headerClass="sortable"
					total="true" />
				<display:column property="plannedCapacity" title="Planned Capacity"
					sortable="true" format="{0,number,0}" headerClass="sortable"
					total="true" />
				<display:column property="actualCapcity" title="Actual Capacity"
					sortable="true" format="{0,number,0}" headerClass="sortable"
					total="true"  />
				<display:column property="avaliableVsPlanned" title="Planned vs Available"
					sortable="true" format="{0,number,0}%" headerClass="sortable"
					   />
				<display:column property="plannedVsActual" title="Actual vs Planned"
					sortable="true" format="{0,number,0}%" headerClass="sortable"
					  />
				<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
				<display:setProperty name="export.excel.filename"
					value="<%=fname2%>" />
				<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
			</display:table>
			<br />
			<p>
				<input type="Button" name="add" class="formbutton" value="Back"
					onclick="javascript:history.go(-1);" />
			</p>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
