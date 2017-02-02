<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="com.agiledge.atom.constants.SettingsConstant" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="icon" href="images/agile.png" type="image/x-icon" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
function showPopup(url) {

	    var params="toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	    var size="height=450,width=520,top=200,left=300,"+params;
	    newwindow=window.open(url,'name',size);
	    

	     if (window.focus) {newwindow.focus();}
	    }
</script>
<title>Assign Project</title>
</head>
<body>
<h3 align="center">Assign Project</h3>
<div>
<form name="EditEmployee"  action="EditProject" method="post" >
<input type="hidden" name="eid" value="<%=request.getParameter("eid")%>"/> 
		
		<table style="border-style: outset; width: 100%;background-color:white;" >
		<tr>
		<td align="left">Personnel No:</td><td><%=request.getParameter("personnelno") %></td>
</tr>
		<tr>
		<td align="left">Display Name:</td><td><%=request.getParameter("dname") %></td>
		</tr>
		<tr>
		<td align="left">Project:</td><td align="left"><input type="text" name="projectdesc" id="projectdesc"
								readonly="readonly" value=<%=request.getParameter("project") %> onclick="showPopup('getproject.jsp' ); "/>
								<input type="hidden" name="project" id="project" value=<%=request.getParameter("pid") %>/>
								<input type="button" class="formbutton" value="..."
								onclick="showPopup('getproject.jsp' ); " /></td>
								</tr>
		<tr align="center">
		<td colspan="2" align="center">
		
		<input type="submit" value="Update" class="formbutton" onClick="updatebutton();"/>
		</td>
		</tr>
		
		</table>
		</form>
	</div>
</body>
</html>