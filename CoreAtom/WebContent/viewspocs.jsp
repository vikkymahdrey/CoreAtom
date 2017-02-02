<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dto.SpocDto" %>
<%@page import="com.agiledge.atom.service.SpocService" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.tablesorter.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	 $("#spoctable").tablesorter(); 
});
function updateStatus(spocid,curstatus)
{
	 var msg="Do you really want to cancel ? ";
	 $("input[name=spocid]").val(spocid);
	 $("input[name=status]").val(curstatus);
	 if(curstatus=="c")
		 msg="Do you really want to activate ? ";
	 if(confirm(msg))
	 { 
	 document.getElementById("statusform").submit(); 
	 }
}
</script>
<title>View Spocs</title>
</head>
<body>
<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<%
		}
	%>
<h3 align="center">View All Assigned Spocs</h3>
<table id="spoctable" class="tablesorter">
<thead> 
<%
try{
	ArrayList<SpocDto> list=new SpocService().getspocsbymanagerid(empid);
	System.out.println("List Size :");
	System.out.println("  " + list.size());
	String fd=" ",td=" ",status="Active",buttonvalue="Cancel";
	if(list.isEmpty())
	{%>
	<h4 align="center">Sorry, No Spocs Found!</h4>	
	<% }
	else
	{
%>
<tr>
			<th align="center" ><a href=''>Personnel Number</a></th>
			<th align="center"><a href=''>Name</a></th>
			<th align="center"><a href=''>From Date</a></th>
			<th align="center"><a href=''>To Date</a></th>
			<th align="center"><a href=''>Status</a></th>
			<th align="center">Actions</th>
		</tr>

		</thead>
		</tbody>
<%
for(SpocDto dto:list)
{
	if(dto.getFrom_date()!=null)
	{
		fd=dto.getFrom_date();
		td=dto.getTo_date();
	}
	if(dto.getStatus().equalsIgnoreCase("c"))
	{
		status="Cancelled";
		buttonvalue="Activate";
	}
	else
	{
		status="Active";
		buttonvalue="Cancel";
	}
%>
<tr>
<td align="center"><%=dto.getPers_no() %></td>
<td align="center"><%=dto.getSpocName() %></td>
<td align="center"><%=fd%></td>
<td align="center"><%=td%></td>
<td align="center"><%=status%></td>
<td align="center"><input type="button" onClick="window.location.href='viewspocemp.jsp?spoc_id=<%=dto.getSpoc_id() %>&spoc_name=<%=dto.getSpocName()%>'" value="View Employees" class="formbutton"/> <input type="button" value="<%=buttonvalue %>" class="formbutton" onClick="updateStatus('<%=dto.getSpoc_id()%>','<%=dto.getStatus()%>');"/></td>
<%
}
}
}catch(Exception e)
{
	System.out.println("Error in viewspocs.jsp"+e);
}
%>
</table>
<table align="center">
<tr align="center">
<td align="center">
<input type="button" value="Assign Spoc" class="formbutton" onClick="window.location.href='emptospouc.jsp'"/>
</td>
</tr>
</table>
<form name="statusform" id="statusform" action="SpocStatus" method="post">
		<input type="hidden" id="spocid" name="spocid"/>
		<input type="hidden" id="status" name="status"/>
 		</form>
		<%@include file='Footer.jsp'%>
</body>
</html>