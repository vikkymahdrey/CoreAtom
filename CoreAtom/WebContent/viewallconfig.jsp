<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.GeneralShiftService"%>
<%@page import="com.agiledge.atom.dto.GeneralShiftDTO" %>
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
	 $("#configtbl").tablesorter(); 
	 $("form[name=deleteform]").hide();
});

function deleteconfig(id)
{
	var msg="Do you really want to delete ? ";
	$("input[name=deleteid]").val(id);
	if(confirm(msg))
	 { 
	 document.getElementById("deleteform").submit(); 
	 }
}
</script>
<title>View All Configurations</title>
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
<h3 align="center">View All Configurations</h3>
<%
try{
ArrayList<GeneralShiftDTO> list=new GeneralShiftService().getallConfigurations();
String app_req="",amt_ded="",wait_reconf="";
if(list==null||list.size()<1)
{
response.sendRedirect("addgenconfig.jsp");
}
%>
<table id="configtbl" class="tablesorter">
<thead> 
<tr>
		    <th align="center" width="10%"><a href=''>Site</a></th>
			<th align="center" width="3%"><a href=''>Approval</a></th>
			<th align="center" width="10%"><a href=''>Approval By</a></th>
			<th align="center"><a href=''>From Date</a></th>
			<th align="center" width="3%"><a href=''>Cut-Off Time For Subscribing</a></th>
			<th align="center" width="3%"><a href=''>Cut-Off Time For Cancelling</a></th>
			<th align="center" width="3%"><a href=''>Deduction</a></th>
			<th align="center"><a href=''>Type</a></th>
			<th align="center" width="3%"><a href=''>Amount(Rs)</a></th>
			<th align="center" width="3%"><a href=''>Waiting List Re-Confirmation</a></th>
			<th align="center" width="3%"><a href=''>Re-confirmation Days</a></th>
			<th align="center" width="20%">Actions</th>
		</tr>
		</thead>
		</tbody>
		<%
		for(GeneralShiftDTO dto:list)
			{
			app_req="Yes";
			amt_ded="Yes";
			wait_reconf="Yes";
			if(dto.getApproval_req().equals("n"))
			{
				app_req="No";
			}
			if(dto.getDeduction().equals("n"))
			{
				amt_ded="No";
			}
			if(dto.getWaitlist_reconf().equals("n"))
			{
				wait_reconf="No";
			}
			if(dto.getApproval_req().equals("n"))
			{
				dto.setApproved_by("");
			}
			if(dto.getDeduction().equals("n"))
			{
				dto.setDeduction_amt("");
				dto.setDeductiontype("");
			}
			if(dto.getWaitlist_reconf().equals("n"))
					{
				dto.setWaitlist_cutoffdays("");
					}
			%>
		<tr>
		<td align="center"><%=dto.getSite_id()%></td>
		<td align="center"><%=app_req%></td>
		<td align="center"><%=dto.getApproved_by()%></td>
		<td align="center"><%=dto.getFrom_date()%></td>
		<td align="center"><%=dto.getCutoffdays()%></td>
		<td align="center"><%=dto.getCancelcutoff() %></td>
		<td align="center"><%=amt_ded%></td>
		<td align="center"><%=dto.getDeductiontype() %></td>
		<td align="center"><%=dto.getDeduction_amt() %></td>
		<td align="center"><%=wait_reconf%></td>
		<td align="center"><%=dto.getWaitlist_cutoffdays()%></td>
		<td align="center"><input type="button" class="formbutton" value="Assigned LogTime" onClick="window.location.href='viewgenshift.jsp?id=<%=dto.getConf_id()%>'"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<%
		if(dto.getEditable().equals("yes"))
		{
		%>
		<input type="button" value="Delete" class="formbutton" onClick="deleteconfig('<%=dto.getConf_id()%>')"/>
		<%
		}
		%>
		</td>
		</tr>
		<%
		}
}catch(Exception e)
{
	System.out.println("Error in viewallconfig.jsp"+e);
}
		%>
		</table>
		<form id="deleteform" name="deleteform" action="DeleteConfig" method="post">
		<input type="hidden" id="deleteid" name="deleteid"/>
		</form>
	<%@include file='Footer.jsp'%>
</body>
</html>