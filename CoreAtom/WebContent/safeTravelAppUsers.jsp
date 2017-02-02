<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<%@ taglib uri="http://www.nfl.com" prefix="disp"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<%
	request.setAttribute("contextPath", request.getContextPath());
%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>EmployeeAppUsers</title>

<link rel="stylesheet" type="text/css"
	href="${contextPath}/css/displaytag.css" />
	<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dispx.js"></script>
</head>
<body>
	<div id="body">
		<div class="content" align="center" >
			<%
			String fname1 = ("EmployeeAppUsersReport :")
			.concat(new Date().toString()).concat(".csv");
	String fname2 = ("EmployeeAppUsersReport :").concat(new Date().toString())
			.concat(".xls");
	String fname3 = ("EmployeeAppUsersReport:").concat(new Date().toString()).concat(".xml");

					long empid = 0;
					String employeeId = OtherFunctions.checkUser(session);

					empid = Long.parseLong(employeeId);
			%>

			  <%@include file="Header.jsp"%>  
		

			<h2 align="center" style="color:teal;">Employee App Users</h2>
			
				<%
				ArrayList<EmployeeDto> dtoList =null;
				try{
				
					dtoList = new  EmployeeService().getSafeTravelAppUsers();
				
				}catch(Exception e) {
					System.out.println(" Error in jsp "+ e);
				} 
				 
			if(dtoList!=null&&dtoList.size()>0) {
				TotalTableDecorator grandTotals = new TotalTableDecorator();
				pageContext.getRequest().setAttribute("tableDecor", grandTotals);
				 
			
		%>
		<display:table class="alternateColor"    name="<%=dtoList%>" id="row"   
			export="true"  pagesize="50" >
				
				<display:column property="displayName" title="Employee Name"
				sortable="true" headerClass="sortable" /> 
				<display:column property="personnelNo" title="Personnel No"
				sortable="true" headerClass="sortable" />
				<display:column property="contactNo" title="Contact Number"
				sortable="true" headerClass="sortable" />
         		<display:column property="emailAddress" title="EmailAddress"
				sortable="true" headerClass="sortable" />
				<display:column property="date" title="Downloaded Date"
				sortable="true" headerClass="sortable" />
			<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
			<display:setProperty name="export.excel.filename" value="<%=fname2%>" />
			<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
		</display:table>
<%} else { 
			System.out.println(" dto list is null ");
		}
		%>	</div>
			
			 <%@include file="Footer.jsp"%>
		
		</div>
	

</body>
</html>
