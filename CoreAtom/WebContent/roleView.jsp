<%@page import="com.agiledge.atom.usermanagement.dto.ViewManagementDto"%>
<%@page import="com.agiledge.atom.usermanagement.service.ViewManagementService"%>
<%@page import="com.agiledge.atom.usermanagement.service.UserManagementService"%>
<%@ page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.agiledge.atom.dto.UserManagementDTO" %>
 
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
function deleteView(roleId,actualId,viewId){
	
	 $("input[name=deleteroleId]").val(roleId);
	 $("input[name=deleteviewId]").val(viewId);
	 $("input[name=actualId]").val(actualId);
	 if(confirm("Do you really want to delete ?"))
		 { 
			$("form[name=deleteviewform]").submit();
			
		 }
 
 
}
</script>
<title>Views</title>		
</head>
<body>
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
<div id="body">
		<div class="content">
		<%
		int roleId=Integer.parseInt(request.getParameter("roleId"));
		String roleName=request.getParameter("roleName");
		ViewManagementService userser=new ViewManagementService();
		ArrayList<ViewManagementDto> dtoList=userser.getViewsbyRole(roleId);
		if(dtoList.isEmpty())
		{%>
			 <center style="color: red;">Sorry,No View Found For This Role</center>
		<%}
		else
		{
		%>
<h3>Views Under <%=roleName %></h3>
<table>
<thead>
<th align="center">Id</th>
<th align="center">View Name</th>
<th align="center">URL</th>
<th align="center">Show Order</th>
<th align="center" width="15%">Action</th>
</thead>
<%
try {
for(ViewManagementDto dto:dtoList)
{
	 
%>
<tr>
<td align="center"><%=dto.getViewId() %>
<input type="hidden" value="<%=dto.getViewId() %>" id=viewId /></td>
<td align="center"><%=dto.getViewName() %></td>
<td align="center"><%=dto.getViewURL() %></td>
<td align="center"><%=dto.getViewShowOrder() %></td>
<td align="center"><table align="center" ><tr><td align="center" "border-bottom: 0px solid #cE5; padding: 0px;"> 	<img src="images/delete.png" class="deleteButton" onclick="deleteView(<%=roleId%>,'<%=dto.getId() %>',<%=dto.getViewId() %>)" id="deleteImage"
									title="Delete" />
</td >
<td align="center"><input type="button" class="formbutton" value="Sub Views" onClick="self.location='ViewSubview.jsp?viewId=<%=dto.getViewId() %>&viewName=<%=dto.getViewName()%>'"/>
</td>
</tr></table></td>
</tr>
<%
}
}catch(Exception e) {
	System.out.println("Error : "+ e);
}
		}
%>
</table>
<table>
<tr align="center">
<td align="center">
<input type="button" class="formbutton" value="Add View" onClick="self.location='AddroleView.jsp?roleId=<%=roleId%>&roleName=<%=roleName%>'"/>
</td>
</tr>
</table>

<form name="deleteviewform" action="DeleteViewRole" method="post">
<input type="hidden" name=deleteroleId id=deleteroleId/>
<input type="hidden" name=deleteviewId id=deleteviewId/>
<input type="hidden" name=actualId id=actualId/>
<input type="hidden" name=deleteroleName id=deleteroleName value="<%=roleName%>"/>

</form>
</body>
</html>