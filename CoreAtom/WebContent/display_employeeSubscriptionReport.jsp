<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="java.util.Date"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.reports.dto.SubscriptionReportDto"%>
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

<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">

function resetFields()
{
	$("input[type=hidden]").val("");
	$("input[type='text']").val("");
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
        else if(url=="EmployeeSearch.jsp")
        	{
            size="height=450,width=700,top=200,left=300,"+params;
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
	String fname1=("SubscriptionReport :").concat(new Date().toString()).concat(".csv");
	String fname2=("SubscriptionReport :").concat(new Date().toString()).concat(".xls");
	String fname3=("SubscriptionReport :").concat(new Date().toString()).concat(".xml");
	
	
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String siteID = request.getParameter("siteId");
	 
		String spoc = request.getParameter("supervisorID2");
		String empId = request.getParameter("employeeId");

		EmpSubscriptionReportHelper reportHelper = new EmpSubscriptionReportHelper();
		
		List<SubscriptionReportDto> emplist = null;
		if(fromDate!=null&&toDate!=null)
		{
		emplist=reportHelper.getSubscripionReport(fromDate, toDate,   spoc,siteID,empId);
		}

		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<div>
				<form name="form1" action="display_employeeSubscriptionReport.jsp"
					method="POST" onsubmit="return validate()">
					<table>
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



							<td align="right">From</td>
							<td align="left" colspan="2"><input name="fromDate"
								id="fromDate" type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=fromDate!=null&&fromDate.trim().equals("")==false?fromDate:"" %>" />

							</td>
							<td align="right">To</td>
							<td align="left"><input name="toDate" id="toDate"
								type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=toDate!=null&&toDate.trim().equals("")==false?toDate:"" %>" />


							</td>







							<td align="right">Reporting Officer</td>
							<td><input type="hidden" name="supervisorID2"
								id="supervisorID2"
								value="<%=request.getParameter("supervisorID2")!=null&&request.getParameter("supervisorID2").trim().equals("")==false?request.getParameter("supervisorID2"):"" %>" />
								<input type="text" readonly name="supervisorName2"
								id="supervisorName2"
								onclick="showPopup('SupervisorSearch2.jsp')"
								value="<%=request.getParameter("supervisorName2")!=null&&request.getParameter("supervisorName2").trim().equals("")==false?request.getParameter("supervisorName2"):"" %>" />
								<label for="supervisorID2" class="requiredLabel"></label> <input
								class="formbutton" type="button" value="..."
								onclick="showPopup('SupervisorSearch2.jsp')" /></td>
							<td align="right">Employee</td>

							<td><input type="hidden" name="employeeId" id="employeeID"
								value="<%=request.getParameter("employeeId")!=null&&request.getParameter("employeeId").trim().equals("")==false?request.getParameter("employeeId"):"" %>" />
								<input type="text" readonly name="employeeName"
								id="employeeName" onclick="showPopup('EmployeeSearch.jsp')"
								value="<%=request.getParameter("employeeName")!=null&&request.getParameter("employeeName").trim().equals("")==false?request.getParameter("employeeName"):"" %>" />
								<label for="employee" class="requiredLabel"></label> <input
								class="formbutton" type="button" value="..."
								onclick="showPopup('EmployeeSearch.jsp')" /></td>


							<td>&nbsp;</td>
							<td><input type="submit" class="formbutton"
								value="Generate Report" /> <input type="button"
								class="formbutton" value="Reset" onclick="resetFields();" /></td>
						</tr>
					</table>
				</form>
			</div>


			<br />
			<h2 align="center">Employee Subscription Report</h2>
			<hr />

			<display:table class="alternateColorInSub" name="<%=emplist%>"
				id="row" export="true" defaultsort="1" defaultorder="descending"
				pagesize="50">

				<display:column property="siteName" title="Site" sortable="true"
					headerClass="sortable" />
				<display:column property="effectiveDate" title="Effective Date"
					sortable="true" headerClass="sortable" />
				<display:column property="subscriptionDate" title="subscribed Date"
					sortable="true" headerClass="sortable" />
				<display:column property="staffCode" title="Staff#" sortable="true"
					headerClass="sortable" />
				<display:column property="staff" title="Staff Name" sortable="true"
					headerClass="sortable" />
				<display:column property="gender" title="Gender" sortable="true"
					headerClass="sortable" />
				<display:column property="contactNo" title="Contact#"
					sortable="true" headerClass="sortable" />
				<display:column property="roll" title="Roll" sortable="true"
					headerClass="sortable" />

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
				<display:column property="project" title="<%=SettingsConstant.PROJECT_TERM%>" sortable="true"
					headerClass="sortable" />
				<display:column property="projectCode" title="Code" sortable="true"
					headerClass="sortable" />
				 
				 
				<display:column property="subscriptionStatus" title="Status"
					sortable="true" headerClass="sortable" />
				<display:column property="scheduledUpTo" title="Scheduled Up To"
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
