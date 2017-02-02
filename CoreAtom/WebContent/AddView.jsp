<%@page import="com.agiledge.atom.usermanagement.service.ViewManagementService"%>
<%@page import="com.agiledge.atom.usermanagement.dto.ViewManagementDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<title>Add View</title>
<script type="text/javascript">
	function validate() {
		var viewName = document.getElementById("viewName").value;
		var viewKey = document.getElementById("viewKey").value;
		var url = document.getElementById("url").value;
		var showorder = document.getElementById("showorder").value;
		document.getElementById("showordervalid").innerHTML="";
		document.getElementById("namevalid").innerHTML = "";
		document.getElementById("urlvalid").innerHTML = "";
		if (viewName.length < 1) {
			document.getElementById("namevalid").innerHTML = "View Name can't be blank !";
			return false
		}
		if (viewKey.length < 1) {
			document.getElementById("viewKeyValid").innerHTML = "Identifying Name can't be blank !";
			return false
		}
		if (url.length < 1) {
			document.getElementById("urlvalid").innerHTML = "URL can't be blank !";
			return false
		}
		if(showorder.length < 1) {
			document.getElementById("showordervalid").innerHTML = "Show Order can't be blank !";
			return false
		}
	}
	 
</script>
<div class="content">
<div class="content_resize">
		<%
          long empid=0;
        String employeeId = OtherFunctions.checkUser(session);
        if (employeeId == null||employeeId.equals("null") ) {
            String param = request.getServletPath().substring(1) + "___"+ request.getQueryString(); 	response.sendRedirect("index.jsp?page=" + param);
        } else {
            empid = Long.parseLong(employeeId);
            %>
			<%@include file="Header.jsp"%>
			<%
        }
        OtherDao ob = null;
        ob = OtherDao.getInstance();
         
        
        
    %>
    <h3>Add View</h3>
    <form name="AddView" action="AddView" method="post" onsubmit="return validate()">
    <table align="center">
    <tr>
    <td>View Name</td>
    <td><input type="text" id="viewName" name="viewName"/>
    <label id="namevalid" style="color: red;"></label></td> 
    </tr>
    <tr>
    <td>Identifying Name</td>
    <td><input type="text" id="viewKey" name="viewKey"/>
    <label id="viewKeyValid" style="color: red;"></label></td> 
    </tr>
    <tr>
    <td>URL</td>
    <td> 
    	<select name="url">
    		<option value="" >SELECT URL</option>
    		<%
    		ViewManagementService service = new ViewManagementService();
            ArrayList<ViewManagementDto> dtoList = service.getPagesOnly();
    		for(ViewManagementDto dto : dtoList) {
    		%>
    		<option value="<%=dto.getViewUrlId() %>" ><%=dto.getViewURL() %></option>
    		<%} %>
    	</select>
     	
    <label id="urlvalid" style="color: red;"></label></td>
    </tr>
    <tr>
    <td>Show Order</td>
    <td><input type="text" id="showorder" name="showorder"/>
    <label id="showordervalid" style="color: red;"></label></td>
    </tr>
    <tr>
<td align="center"><input type="submit" class="formbutton"/></td>
</tr>
    </table>
    </form>
</head>
<body>

</body>
</html>