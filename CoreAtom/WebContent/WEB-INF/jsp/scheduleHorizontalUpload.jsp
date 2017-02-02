<%--
    Document   : emp_Schedule
    Created on : Aug 28, 2012, 12:51:01 PM
    Author     : 123
--%>

 
<%@page import="com.agiledge.atom.dao.OtherDao"%>
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
		$('input[name=fromDate]').datepick();
		$('input[name=toDate]').datepick();
	});
	
	function download() {
		var fromDate = $("input[name=fromDate]").val();
		var toDate = $("input[name=toDate]").val();
		var site = $("select[name=site]").val();
		var action = "downloadHXLS.do";
		window.location.href=action+"?"+ encodeURI( "fromDate="+ fromDate + "&toDate="+ toDate + "&site=" + site );
		
	}
</script>

<style>
	#formDiv {
			width: 20%;
			border: thin;
			border-style: outset;
			margin-left: 1%;
	}
	 
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Schedule Upload</title>


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
	<div id="body" style="width:100%;">
		<div class="content" style="width:100%;">
		<div id="formDiv" style="width:100%;" >
 			 
 			<form:form method="post" enctype="multipart/form-data"  
   modelAttribute="uploadedFile" action="uploadHXLSchedule.do">  
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
   	<td>From Date
   	</td>
   	<td>	<input type="text" name="fromDate" />
   	</td>
   </tr>
   <tr>
   	<td>To Date
   	</td>
   	<td>	<input type="text" name = "toDate" />
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
  		<input type="button" value="Download" onclick="download()" />
      </td>  
     <td><input type="submit" value="Upload" />  
     </td>  
     <td> </td>
     <td>
     </td>  
    </tr>  
   </table>
    
  </form:form>
  <div style="width: 80%;">
  ${statusReport }  	 
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
