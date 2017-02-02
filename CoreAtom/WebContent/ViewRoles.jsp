<%@page import="com.agiledge.atom.usermanagement.dto.ViewManagementDto"%>
<%@page import="com.agiledge.atom.usermanagement.service.ViewManagementService"%>
<%@page import="com.agiledge.atom.usermanagement.service.UserManagementService"%>
<%@ page import="com.agiledge.atom.dto.UserManagementDTO" %>

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
<script type="text/javascript">
function showEditWindow(roleId,roleName,roleDescription){
	var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var size = "height=450,width=400,top=200,left=300," + params;
	var url="editRole.jsp?roleId="+roleId+"&roleName="+roleName+"&roleDescription="+roleDescription;	
    newwindow = window.open(url, 'Edit Role', size);
	if (window.focus) {
		newwindow.focus();
	}
}

function deleteRole(deleteid){
	
	 $("input[name=deleteId]").val(deleteid);
	 if(confirm("Do you really want to delete ?"))
		 { 
			$("form[name=deleteroleform]").submit();
			
		 }
  
  
}
</script>
<title>User Management Setup</title>
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
			<br /> 
<%
ViewManagementService service=new ViewManagementService();
ArrayList<ViewManagementDto> dtoList=service.getRoleList();
%>
<h3>User Management Setup</h3>
<hr />
<table>
<thead>
<tr>
<th align="center" width="10%">Id</th>
<th align="center" width="15%">Role Name</th>
<th align="center" width="20%">Description</th>
<th align="center" width="10%">Action</th>
</tr>
</thead>
<%
for(ViewManagementDto dto: dtoList)
{
%>
<tr>
<td align="center"><%=dto.getRoleId() %>
<input type="hidden" value="<%=dto.getRoleId() %>" id=roleId />
</td>
<td align="center"><%=dto.getRoleName()%>
<input type="hidden" value="<%=dto.getRoleName() %>" id=roleName />
</td>
<td align="center"><%=dto.getRoleDescription() %>
<input type="hidden" value="<%=dto.getRoleDescription()%>" id="name-<%=dto.getRoleDescription() %>" />
</td>
<td><table ><tr><td style="border-bottom: 0px solid #cE5; padding: 0px;" > <img src="images/edit.png" class="editButton" onclick="showEditWindow(<%=dto.getRoleId() %>,'<%=dto.getRoleName() %>','<%=dto.getRoleDescription() %>');" id="editimage"
									title="Edit" />
</td><td style="border-bottom: 0px solid #cE5; padding: 0px;"> 	<img src="images/delete.png" class="deleteButton" onclick="deleteRole(<%=dto.getRoleId() %>)" id="deleteimage"
									title="Delete" />
</td >
<td><input type="button" class="formbutton" value="Views" onClick="self.location='roleView.jsp?roleId=<%=dto.getRoleId() %>&roleName=<%=dto.getRoleName()%>'"/>
</td>
</tr></table></td>								
</tr>
<%
}
%>
</table>
<table>
<tr>
</tr>
<td align="center">
<input align="middle" type="button" class="formbutton" value="Add Role" onClick="self.location='AddRole.jsp'"/>
</td>
</table>
<form name="deleteroleform" action="DeleteRole" method="post">
<input type="hidden" name=deleteId id=deleteId/>
</form>
</head>
<body>
</body>
</html>