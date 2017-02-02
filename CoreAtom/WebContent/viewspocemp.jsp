<%@page import="com.agiledge.atom.service.SpocService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto" %>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<link href="css/validate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.tablesorter.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	 $("#spoctable").tablesorter(); 
});
function removeEmployee(id,spocname,spocid)
{
	 var msg="Do you really want to remove ? ";
	 $("input[name=empid]").val(id);
	 $("input[name=namespoc]").val(spocname);
	 $("input[name=idspoc]").val(spocid);
	 if(confirm(msg))
	 { 
	 document.getElementById("Remove").submit(); 
	 }
}
</script>
<title>View Employees Under Spoc</title>
</head>
<body>
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
    <h3 align="center">View Employees Under <%=request.getParameter("spoc_name")%></h3> 
<%
try{
	ArrayList<EmployeeDto> list=new SpocService().getemployeesbyspoc(Long.parseLong(request.getParameter("spoc_id")));
	if(list.isEmpty())
	{ %>
	<h4 align="center">Sorry, No Employees Found!</h4>	
	<% }
	else
	{ %>
	<table id="spoctable" class="tablesorter">
    <thead>
<tr>
<th align="center"><a href=''>Serial No</a></th>
			<th align="center" ><a href=''>Personnel Number</a></th>
			<th align="center"><a href=''>First Name</a></th>
			<th align="center"><a href=''>Last Name</a></th>
			<th align="center"><a href=''>Gender</a></th>
			<th align="center"><a href=''>Email</a></th>
			<th align="center">Actions</th>
</tr>
</thead>
</tbody>
<%
int i=1;
for(EmployeeDto dto:list)
{%>
<tr>
<td align="center"><%=i++%></td>
<td align="center"><%=dto.getPersonnelNo() %></td>
<td align="center"><%=dto.getEmployeeFirstName()%></td>
<td align="center"><%=dto.getEmployeeLastName()%></td>
<td align="center"><%=dto.getGender()%></td>
<td align="center"><%=dto.getEmailAddress()%></td>
<td align="center"><input type="button" onClick="removeEmployee(<%=dto.getSpoc_id() %>,'<%=request.getParameter("spoc_name") %>','<%=request.getParameter("spoc_id") %>');" value="Remove" class="formbutton"/></td>
<%
}
	}
}catch(Exception e)
{
	System.out.println("Error in viewspocemp.jsp"+e);
}
%>
 </table>
 <table align="center">
 <tr align="center">
 <td align="center">
 <input type="button" value="Add Employee" onClick="window.location.href='addspocemp.jsp?spoc_id=<%=request.getParameter("spoc_id") %>&spoc_name=<%=request.getParameter("spoc_name") %>'" class="formbutton"></td>
 </tr>
 </table>
 <form name="Remove" id="Remove" Action="RemoveEmployee" method="post">
 <input type="hidden" id="empid" name="empid"/>
 <input type="hidden" id="namespoc" name="namespoc"/>
 <input type="hidden" id="idspoc" name="idspoc"/>
  </form>
 </div>
 </div>
 <%@include file='Footer.jsp'%>
</body>
</html>