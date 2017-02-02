<%-- 
    Document   : newjsp
    Created on : Oct 22, 2012, 1:00:11 PM
    Author     : muhammad
--%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@page import="com.agiledge.atom.dao.DelegateRoleDao"%>
<%@page import="com.agiledge.atom.dto.DelegateRoleDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Routes</title>
<script type="text/javascript">
function showAuditLog(relatedId,moduleName){
	var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var size = "height=450,width=900,top=200,left=300," + params;
	var url="ShowAuditLog.jsp?relatedNodeId="+relatedId+"&moduleName="+moduleName;	
    newwindow = window.open(url, 'AuditLog', size);

	if (window.focus) {
		newwindow.focus();
	}
}
function validate()
{
	if(confirm("Are you sure you want to cancel this delegation"))
		{
		return true;
		}
	return false;
	}

</script>
</head>
<body>
	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>

	<br />
	<div class="content">
		<div class="content_resize">
			<%
				ArrayList<DelegateRoleDto> delegateRoleDtos = null;		
					delegateRoleDtos = new DelegateRoleDao()
							.getDelegatedRoles(empid);				
			%>
			<h3>Delegated Role</h3>
			<hr />
			<%
			if(delegateRoleDtos!=null && delegateRoleDtos.size()>0)
			{
			 %>
			<table>
				<thead>

					<tr>
						<th align="center">Employee Id</th>
						<th align="center">Employee Name</th>
						<th align="center">Delegated Employee Id</th>
						<th align="center">Delegated Employee Name</th>
						<th align="center">Delegated From</th>
						<th align="center">Delegated To</th>
						<th colspan="2">Action</th>
						<th></th>
					</tr>
				</thead>
				<%
					for (DelegateRoleDto delegateRoleDto : delegateRoleDtos) {
				%>

				<tr>
					<td align="center"><%=delegateRoleDto.getEmployeePersonnelNo()%></td>

					<td align="center"><%=delegateRoleDto.getEmployeeName()%></a></td>
					<td align="center"><%=delegateRoleDto.getDelegatedemployeePersonnelNo()%></td>
					<td align="center"><%=delegateRoleDto.getDelegatedemployeeName()%></td>
					<td align="center"><%=delegateRoleDto.getFromDate()%></td>
					<td align="center"><%=delegateRoleDto.getToDate()%></td>
					<td><a href="modifyDelegation.jsp?Id=<%=delegateRoleDto.getId()%>">
						<input type="button" value="Modify" name="modify" class="formbutton" />
						</a>
						
						</td>
						<td>
						<form name="ModifyDelegateRole" action="ModifyDelegateRole" method="post"
							onsubmit="return validate()">
							<input type="hidden" name="Id" value="<%=delegateRoleDto.getId()%>" /> <input 
								type="submit" value="Cancel" name="cancel" class="formbutton" />
						</form></td>
					<td align="center"><input type="button" class="formbutton"
						onclick="showAuditLog(<%=delegateRoleDto.getId()%>,'<%=AuditLogConstants.DELEGATE_MODULE%>');"
						value="Audit Log" /></td>
				</tr>
				<%
					}
				%>
			</table>
<%}else{out.print("<p>Display no delegation</p>");} %>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
