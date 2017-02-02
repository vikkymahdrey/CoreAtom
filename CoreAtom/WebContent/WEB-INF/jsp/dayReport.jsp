



<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
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
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Day Report</title>
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
        </script>

<script type="text/javascript">        
            $(document).ready(function()
            {                                                                        
                $("#date").datepick();
            });     
        </script>
<script type="text/javascript">
            function validate()
            {
                  
                var date=document.getElementById("date").value;
                
                if(date.length<1)
                {
                    alert("Choose Date");
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
	
			long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="../../Header.jsp"%>
	<div id="body">
		<div class="content">
			<div>
				<form:form method="POST" commandName="reportDto" action="dayReport.do" onsubmit="return validate()">
					<table>
						<tr>
							<td>Choose Site</td>
							<td>
							<form:select path="site" items="${sites}" itemLabel="name" itemValue="id"
								class="route" id="site"></form:select>
								</td>
							<td align="right">Date</td>
							<td align="left" ><form:input path="date"
								id="date" type="text" size="10" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								 />
							</td>							
							<td><input type="submit" class="formbutton" 
								value="Generate Report" /> <input type="button"
								class="formbutton" value="Reset" onclick="resetFields();" /></td>
						</tr>
					</table>
				</form:form>			
				<display:table requestURI="/dayReport.do" class="alternateColor" name="${empList}" id="row"
				export="true" defaultsort="1" defaultorder="descending"
				pagesize="50">
				<display:column property="employeePersonnelNo" title="Employee ID" sortable="true"
					headerClass="sortable" />
				<display:column property="employeeName" title="Employee Name"
					sortable="true" headerClass="sortable" />
			
				 
				
				<display:column property="loginTime" title="Login Time"
					sortable="true" headerClass="sortable" />
				<display:column property="logoutTime" title="Logout Time"
					sortable="true" headerClass="sortable" />				
				<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
				<display:setProperty name="export.excel.filename"
					value="<%=fname2%>" />
				<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
			</display:table>
				
			</div>
			<%@include file="../../Footer.jsp"%>
		</div>
	</div>

</body>
</html>
