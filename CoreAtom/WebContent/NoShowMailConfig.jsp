<%@ page import="com.agiledge.atom.service.EmployeeService"%>
<%@ page import="com.agiledge.atom.dto.EmployeeDto" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	if($("input[name=seltype]").val()!="null")
		{
	 $("select[name=type]").val($("input[name=seltype]").val()); 
		}
});
function validate()
{
	var count=document.getElementById("count").value;
	var days=document.getElementById("days").value;
	var flag=true;
	var type=document.getElementById("type").value;
	if(type=="select")
		{
		flag=false;
		alert("Please Select Type!");
		}
	else if(count=="" || isNaN(count))
		{
		flag=false;
		alert("Please Enter Count In Correct Format!");
		}
	else if(days=="" || isNaN(days))
		{
		flag=false;
		alert("Please Enter Days In Correct Format!");
		}
	return flag;
	}
	function reload()
	{
		var type=document.getElementById("type").value;
		if(type!="select")
			{

			window.location.href="NoShowMailConfig.jsp?type="+type;
			}
	}
</script>
<title>No Show Mail Notification Config</title>
</head>
<body>
<%
		long empid = 0;
         String status="",value="";
		String employeeId = OtherFunctions.checkUser(session);
		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
			
			
	%>
	<%@include file="Header.jsp"%>
<%} %>
<h3 align="center">No Show Alert Configuration</h3>
<div>
<%
String type=request.getParameter("type");
String count="",days="";
if(type!=null&&!type.equalsIgnoreCase(""))
{
	EmployeeDto dto=new EmployeeService().getNoShowConfig(type);
	count=dto.getNoshowcount();
	days=dto.getNoshowdays();
}
%>
<form  method="post" action="NoShowEmail" onsubmit="return validate()">
<input type="hidden" name="seltype" value="<%=type%>" />
<table>
<tr><td>Type Of Alert</td><td><select name="type" id="type" onChange="reload();"><option value="select">Select</option><option value="email">E-mail</option><option value="sms">SMS</option></select></td></tr>
<tr><td>No Show Count</td><td align="left"><input type="text" name="count" id="count" value=<%=count %>></td></tr>
<tr><td>Days (Range Of Capturing No Show)</td><td align="left"> <input type="text" name="days" id="days" value=<%=days %>></td></tr>
</table>
<table align="center"><tr align="center" ><td align="center"><input type="submit" class="formbutton" value="Save"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" class="formbutton"/></td></tr></table>
</form>
</div>
<%@include file='Footer.jsp'%>
</body>
</html>