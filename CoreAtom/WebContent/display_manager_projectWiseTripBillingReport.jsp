



<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="java.util.Date"%>
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
<title>Project Wise Billing Report</title>
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
	<%
	String fname1=("TripBillPrReport :").concat(new Date().toString()).concat(".csv");
	String fname2=("TripBillPrReport :").concat(new Date().toString()).concat(".xls");
	String fname3=("TripBillPrReport :").concat(new Date().toString()).concat(".xml");
	
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String siteID = request.getParameter("siteId");
	 

		
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<%
		}
		TripSheetNoShowCountReportHelper reportHelper = new TripSheetNoShowCountReportHelper();
		List<ProjectWiseTripBillDto> emplist = reportHelper
				.getProjectWiseBillingReportForManager(fromDate, toDate,siteID,""+empid);

	%>
	<div id="body">
		<div class="content">

			<div>
				<form name="form1" action="display_projectWiseTripBillingReport.jsp"
					method="POST" onsubmit="return validate()">
					<table style="margin-left: 24%; width: 25%;">
						<tr>
							<td>Choose Site</td>
							<td><select name="siteId" id="siteId">
									<%
									String site=(request.getSession().getAttribute("site")==null||request.getSession().getAttribute("site").toString().trim().equals(""))?"" :request.getSession().getAttribute("site").toString().trim();
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

						</tr>
						<tr>
							<td align="right">From</td>
							<td align="left" colspan="2"><input name="fromDate"
								id="fromDate" type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=fromDate!=null&&fromDate.trim().equals("")==false?fromDate:"" %>" />



								To<input name="toDate" id="toDate" type="text" size="6"
								readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=toDate!=null&&toDate.trim().equals("")==false?toDate:"" %>" />


							</td>




						</tr>



						<tr>
							<td>&nbsp;</td>
							<td><input type="submit" class="formbutton"
								value="Generate Report" /> <input type="reset"
								class="formbutton" value="Reset" onclick="resetFields();" /></td>
						</tr>
					</table>
				</form>
			</div>

			<hr />
			<hr />
			<br />
			<h2 align="center"><%=SettingsConstant.PROJECT_TERM%> Wise Billing Report</h2>
			<hr />
			<%
			TotalTableDecorator grandTotals = new TotalTableDecorator();

			//grandTotals.setGrandTotalDescription("&nbsp;");
			//grandTotals.setSubtotalLabel("&nbsp;",null);
			pageContext.getRequest().setAttribute("tableDecor", grandTotals);
		%>
			<display:table class="alternateColorInSub" name="<%=emplist%>"
				id="row" export="true" defaultsort="1" defaultorder="descending"
				pagesize="50" decorator="tableDecor">
				<display:column property="projectcode" title="Project Code"
					sortable="true" headerClass="sortable" group="1" />
				<display:column property="project" title="<%=SettingsConstant.PROJECT_TERM%>" sortable="true"
					headerClass="sortable" group="1" />
				<display:column property="tripDate" title="Date" sortable="true"
					headerClass="sortable" />



				<display:column property="totalEmployees" title="Total Employees"
					sortable="true" format="{0,number,0}" headerClass="sortable"
					total="true" />
				<display:column property="plannedTotalcost"
					title="Planned Total Cost" sortable="true" format="{0,number,0}"
					headerClass="sortable" total="true" />
				<display:column property="plannedPerInstanceCost"
					title="Planned Per Instance Cost" sortable="true"
					format="{0,number,0}" headerClass="sortable" />
				<display:column property="plannedCostPerEmployees"
					title="Planned Cost Per Emp (2 ways)" sortable="true"
					format="{0,number,0}" headerClass="sortable" />
				<display:column property="actualTotalCost" title="Actual Total Cost"
					sortable="true" format="{0,number,0}" headerClass="sortable"
					total="true" />
				<display:column property="actualPerInstanceCost"
					title="Actual Per Instance Cost" sortable="true"
					format="{0,number,0}" headerClass="sortable" />
				<display:column property="actualCostPerEmployees"
					title="Actual Cost Per Emp (2 ways)" sortable="true"
					format="{0,number,0}" headerClass="sortable" />

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
