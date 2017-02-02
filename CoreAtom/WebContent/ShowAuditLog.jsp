<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dto.AuditLogDTO"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.AuditLogDAO"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="icon" href="images/agile.png" type="image/x-icon" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<title>AuditLog</title>
</head>
<body>
	<h3 align="center">Audit Log Information</h3>
	<hr />
	<%
		String relatedNodeId = request.getParameter("relatedNodeId");
		String moduleName = request.getParameter("moduleName");
		String current=request.getParameter("current");
		String employeename=null;
		
        List<AuditLogDTO> auditEntries = new AuditLogDAO()
				.getAllAuditLogEntries(Integer.parseInt(relatedNodeId),
						moduleName,current);
        System.out.println("current :" + current);
        System.out.println("AuditLog null :" + (auditEntries ==null));
         
        
        if(auditEntries!=null &&auditEntries.isEmpty())
        {%>
	<center>Sorry,No Records Found For This Request</center>
	<%
       }
        else
        {
        %>
	<table>
		<tr>
			<th align="center">Id</th>
			<th align="center">Related Id</th>
			<th align="center">Module Name</th>
			<th align="center">Date Changed</th>
			<th align="center">Changed By</th>
			<th align="center">Previous State</th>
			<th align="center">Current State</th>
			<th align="center">Action</th>
		</tr>
		<%for(AuditLogDTO dto:auditEntries)
		{ %>

			<tr>
			<td align="center"><%=dto.getId()%></td>
			<td align="center"><%=dto.getRelatedNodeId()%></td>
			<td align="center"><%=dto.getModuleName() %></td>
			<td align="center">
			<%=OtherFunctions.changeDateFromat(dto.getDateChanged()) %>
			</td>
			<%
			if(dto.getChangedBy()==0)
			{
				employeename="System";
			}
			else
			{
			AuditLogDAO auditob= new AuditLogDAO();
			employeename=auditob.getemployeename(dto.getChangedBy());
			}
			%>
            <td align="center"><%=employeename %></td>
			<td align="center"><%=dto.getPreviousState()%></td>
			<td align="center"><%=dto.getCurrentState()%></td>
			<td align="center"><%=dto.getAction()%></td>
			</tr>
<%}
        }
%>
	</table>
	<table>
		<td align="center"><input type="button" class="formbutton"
			value="Close" onclick="javascript:window.close();" /></td>
	</table>




</body>
</html>