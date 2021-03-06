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
<script>
	$(document).ready(function(){
		 $("input[name=truncate]").change( function() {
			 
			 if($(this).is(":checked")) {
				  
				if( confirm('Caution : You are going to delete existing routes')==false) {
					$(this).removeAttr("checked");
				}
				  
			 }
		 });
		
	})
</script>

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
 			 
 			<form:form method="post" enctype="multipart/form-data"  
   modelAttribute="uploadedFile" action="routeUpload.do">  
   <table>  
   
   <tr>
   	<td>Site</td>
   	<td>
   			<form:select path="site" items="${sites}" itemLabel="name" itemValue="id"
								class="route" id="site"></form:select>
   	</td>
   	<td></td>
   </tr>
    <tr>
   	<td>Type</td>
   	<td>
   			<form:select path="type" items="${types}" itemLabel="name" itemValue="id"
								class="type" id="type"></form:select>
		 
		<form:checkbox path="truncate"/>Clear Existing
	 
		 						
   	</td>
   	<td></td>
   </tr>
    
    <tr>  
     <td>Upload File: </td>  
     <td><input type="file" name="file" />  
     </td>  
     <td style="color: red; font-style: italic;">
     <form:errors path="file" />  
     </td>
        
    </tr>
    
     <tr>  
     <td>
  
      </td>  
     <td><input type="submit" value="Upload" />  
     </td>  
     <td> </td>
     <td>
     </td>  
    </tr>  
   </table>  
  </form:form>  	 
 		 
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
