<%@page import="com.agiledge.atom.dto.BranchDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dto.CompanyDto"%>
<%@page import="com.agiledge.atom.service.CompanyBranchService"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="css/style.css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Add Location</title>
</head>
<body>
	<script type="text/javascript">
		function displaytxtDiv(serial)

		{
			document.getElementById("txtdiv"+serial).style.display = 'block';
			document.getElementById("btndiv"+serial).style.display = 'none';
		}
		function showSites(companyId,branchId)
		{
			window.location="site.jsp?companyId="+companyId+"&branchId="+branchId+"";
		}
		function showAuditLog(relatedId,moduleName){
			var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
			var size = "height=450,width=900,top=200,left=300," + params;
			var url="ShowAuditLog.jsp?relatedNodeId="+relatedId+"&moduleName="+moduleName;	
		    newwindow = window.open(url, 'AuditLog', size);

			if (window.focus) {
			newwindow.focus();
		}
		}
		
		function validateForm(val)
		{
			try{
	if(val=="add")
		{
		var location=document.getElementById("branchLocation").value;			
		}
	else 
		{
		var location=document.getElementById("mbranchLocation"+val).value;
		}				
		if(location!=null&&location.length>1)
			{
			return true;
			}
		else
			{
			alert("Please enter Location");			
			}	
			}
			catch(e){alert(e);}
		return false;
		}
	</script>
	<div id='body'>
		<div class='content'>
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
			String companyId=request.getParameter("companyId");
					ArrayList<BranchDto> branchDtos = new CompanyBranchService().getBranches(companyId);
					if(branchDtos!=null && branchDtos.size()>0){
			%>
			<h3>
				<%=branchDtos.get(0).getCompanyName()%>
				Locations
			</h3>
			<table>
				<tr>
					<th>Id</th>
					<th width='20%'>Location</th>
					<th colspan="2"></th>
				</tr>
				<%
					int serial = 1;
						for (BranchDto branchDto : branchDtos) {
				%>
				<tr>
					<td><%=branchDto.getId()%></td>
					<td width='13%'><%=branchDto.getLocation()%></td>
					<td id="btndiv<%=serial%>" colspan="3"><input type="Button"
						name="showsites" class="formbutton" value="Show Sites"
						onclick="showSites('<%=companyId%>','<%=branchDto.getId()%>')" />
						<input type="Button" name="modify" class="formbutton"
						value="Modify" onclick="displaytxtDiv('<%=serial%>')" /> <input
						type="button" class="formbutton"
						onclick="showAuditLog(<%=branchDto.getId()%>,'<%=AuditLogConstants.COMPANY_BRANCH_MODULE%>');"
						value="Audit Log" /></td>
					<td id="txtdiv<%=serial%>" colspan="3" style="display: none;">
						<form name="modifybranch" action="Branch" method="post"
							onsubmit="return validateForm(<%=serial%>)">
							<input type="hidden" name="id" value="<%=branchDto.getId()%>" />
							<input type="hidden" name="companyId" value="<%=companyId%>" />
							<input type="text" name="branchLocation"
								id="mbranchLocation<%=serial%>"
								value="<%=branchDto.getLocation()%>" /> <input type="submit"
								value="Update" class="formbutton" />
						</form>
					</td>
				</tr>

				<%
					serial++;
						}
				%>
			</table>
			<%
				}
			%>





			<h3>Add Location</h3>
			<hr>
			<div>
				<form name="AddBranchForm" action="Branch" method="post"
					onsubmit="return validateForm('add')">


					<table>
						<tr>

							<td width='13%'><input type="text" name="branchLocation"
								id="branchLocation" /></td>
							<td><input type="hidden" name="companyId"
								value="<%=companyId%>" /></td>
						</tr>
						<tr>
							<td width='10%'></td>
							<td width='13%'><input type="Submit" name="add"
								class="formbutton" value="Add" /> <input type="Button"
								name="add" class="formbutton" value="Back"
								onclick="javascript:history.go(-1);" /></td>
							<td></td>
						</tr>
					</table>

				</form>
			</div>
			<%@include file="Footer.jsp"%>
		</div>
	</div>


</body>
</html>
