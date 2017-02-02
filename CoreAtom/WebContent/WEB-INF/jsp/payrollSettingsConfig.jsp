<%--
    Document   : emp_subscription
    Created on : Aug 28, 2012, 12:51:01 PM
    Author     : 123
--%>

<%@page import="com.agiledge.atom.service.APLService"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@page import="com.agiledge.atom.dto.EmployeeSubscriptionDto"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.lang.Exception"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="com.agiledge.atom.dao.APLDao"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"
	errorPage="error.jsp"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.nfl.com" prefix="disp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<link href="css/jquery.datepick.css" rel="stylesheet" type="text/css" />

<script src="js/dateValidation.js"></script>
<style>
	#formDiv {
			width: 20%;
			border: thin;
			border-style: outset;
			margin-left: 20%;
	}
	 
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Subscription</title>


 </head>
<body onload="displayaddress()">

	<%
		try {
			long empid = 0;
			String employeeId = OtherFunctions.checkUser(session);
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="../../Header.jsp"%>
	<%
		OtherDao ob = null;
			ob = OtherDao.getInstance();
			 
	%>
	<div id="body">
		<div class="content">
		<div id="formDiv" >
 			<form:form action="addPayrollConfig.do"
				commandName="payrollEntry" method="POST"
			 >
				
	 
	
				<table align="center"  >

					<tr>

						<td align="right"  >Site</td>
						<td>
								<form:select path="site" itemValue="id" itemLabel="name" items="${sites }" >
									<form:option value="">--Select the Site</form:option>
									
								</form:select>
								
						</td>
						<td  ></td>
					</tr>
					<tr>

						<td align="right">Transport Types</td>
						<td><form:select path="transportType" itemValue="id" itemLabel="name"   items="${transportTypes}"
							 ></form:select></td>
						<td ></td>

					</tr>
					<tr>

						<td align="right">Payroll Type</td>
						<td><form:select path="payrollType" items="${payrollTypes}"
							 ></form:select></td>
						<td></td>

					</tr>
					<tr>

						<td align="right"  >Flat Rate</td>
						<td><form:input path="flatRate"/> </td>
						<td  ></td>
					</tr>
					<tr>

						<td align="right"  ></td>
						<td><input type="submit"   value="Submit" /></td>
						<td  ></td>
					</tr>
					
		 </table>
		 
			</form:form>
			<div>
				<disp:dipxTable styleClass="displaytag"    id="row" list="${list}" style="width:100%;" >
			 
		
				<disp:dipxColumn  property="site" title="Site"  >
				 </disp:dipxColumn>
 				<disp:dipxColumn  property="payrollType" title="proRate" sortable="true" >
				 </disp:dipxColumn>
				 
 				<disp:dipxColumn  property="transportType" title="transportType" >
				 </disp:dipxColumn>
				<disp:dipxColumn  property="flatRate" title="flatRate" >
				 </disp:dipxColumn>
				
				</disp:dipxTable>
		
			</div>
			</div>
			<%@include file="../../Footer.jsp"%>
			<%
				} catch (Exception e) {
					System.out.println("Error" + e);
				}
			%>
		</div>
	</div>

</body>
</html>
