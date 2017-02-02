<%-- 
    Document   : printTrips
    Created on : Nov 4, 2012, 5:19:23 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.service.TripDetailsService"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Print Trips</title>
<%

        String mimeType = "application/vnd.ms-excel";  
       	response.setContentType(mimeType);        
        String fname = "ManPowerList";
        response.setHeader("Content-Disposition", "attachment; filename = " + fname + ".xls");        
       	response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Pragma", "public");
        response.setHeader("Expires", "Mon, 1 Jan 1995 05:00:00 GMT");
        %>
</head>
<body>
	<%
            String tripDate = request.getParameter("tripDate");
            String siteId = request.getParameter("siteId");
            String tripMode = "" + request.getParameter("tripMode");
            String tripTime = "" + request.getParameter("tripTime");
            String notSaved = request.getParameter("notSaved");
            ArrayList<EmployeeDto> EmployeeDetails = null;
                     
      
                
            	EmployeeDetails=new EmployeeService().getemployeeProjectInfo();
          
            %>
            
      <table border="1" style="width: 1300px;">    
      <tr>
        <td align="left">Personnel Number</td>
		<td align="left">Display Name</td>
		<td align="left">Project Code </td>
		<td align="left">Project </td>
		<td align="left">Project Unit </td>
        
       </tr> 
        
             <% for (EmployeeDto employeeDto : EmployeeDetails) {%>
	
		<tr>
									
		<td align="left"><%=employeeDto.getPersonnelNo() %></td>
		<td align="left"><%=employeeDto.getDisplayName() %></td>
		<td align="left"><%=employeeDto.getProjectid() %></td>
		<td align="left"><%=employeeDto.getProject() %></td>
		<td align="left"><%=employeeDto.getProjectUnit() %></td>
		
		</tr>

		<%
                }%> 
		</tbody>
	</table>

</body>
</html>
