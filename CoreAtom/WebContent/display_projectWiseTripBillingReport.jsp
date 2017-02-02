



<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dao.ProjectDao"%>
<%@page import="com.agiledge.atom.dto.ProjectDto"%>
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
	 
	$("input[type=text]").val("");
	
} 
function openWinodw(url) {
	window.open(url, 'Ratting',
			'width=400,height=250,left=150,top=200,toolbar=1,status=1,');

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
		String projectParam=(request.getParameter("project")==null?"" :request.getParameter("project").toString().trim());
	 

		TripSheetNoShowCountReportHelper reportHelper = new TripSheetNoShowCountReportHelper();
		List<ProjectWiseTripBillDto> emplist = reportHelper
				.getProjectWiseBillingReport(fromDate, toDate,siteID,projectParam);

		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);		
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
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
							 <td align="right">Project</td>
							<td><input type="text" name="projectdesc" id="projectdesc"
								readonly="readonly"
								value="<%=request.getParameter("projectdesc")!=null&&request.getParameter("projectdesc").trim().equals("")==false?request.getParameter("projectdesc"):""%>" />
								<input type="hidden" name="project" id="project"
								value="<%=request.getParameter("project")!=null&&request.getParameter("project").trim().equals("")==false?request.getParameter("project"):""%>" />
								<input type="button" class="formbutton" value="..."
								onclick="openWinodw('getproject.jsp' ); " /></td>
							<td>
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
								value="Generate Report" /> <input type="button"
								class="formbutton" value="Reset" onclick=" resetFields()" /></td>
						</tr>
					</table>
				</form>
			</div>

			<hr />
			<hr />
			<br />
			<h2 align="center">Project Wise Billing Report</h2>
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
					sortable="true" headerClass="sortable" group="1"  />
				<display:column property="project" title="Project" sortable="true"
					headerClass="sortable" group="1" />
				<display:column property="tripDate" title="Date" sortable="true"
					headerClass="sortable" />
				<display:column property="tripTime" title="Trip Time" sortable="true"
					headerClass="sortable" />
				<display:column property="tripLog"  title="Trip Type" sortable="true"
					headerClass="sortable" />


				<display:column property="totalEmployees" title="No. Of Instances Travelled"
					sortable="true" format="{0,number,0}" headerClass="sortable"
					total="true" />
				
				<display:column property="plannedPerInstanceCost"
					title="Per Instance Cost" sortable="true"
					format="&#8377;{0,number,0.00}" headerClass="sortable"   />
				<display:column property="plannedTotalcost"  
					title="Total Per Day Cost" sortable="true" format="&#8377;{0,number,0.00}"
					headerClass="sortable" total="true"  />
			 
				 

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
