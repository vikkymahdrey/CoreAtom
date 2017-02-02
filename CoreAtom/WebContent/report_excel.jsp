<%-- 
    Document   : printTrips
    Created on : Nov 4, 2012, 5:19:23 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.reports.dto.ByVehicleTypeReportDto"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.reports.ByVehicleTypeReportHelper"%>
<%@page import="java.util.List"%>
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
        String fname = new Date().toString();
        response.setHeader("Content-Disposition", "inline; filename = " + fname + ".xls");        
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Pragma", "public");
        response.setHeader("Expires", "Mon, 1 Jan 1995 05:00:00 GMT");
        %>
</head>
<body>
	<%
	String fromDate = request.getParameter("fromDate");
	String toDate = request.getParameter("toDate");
	String siteID = request.getParameter("siteId");
	String option = request.getParameter("option");
	
	String vendor = request.getParameter("vendor");
	vendor=vendor==null?"":vendor;
	ByVehicleTypeReportHelper reportHelper = new ByVehicleTypeReportHelper();
	List<ByVehicleTypeReportDto> dtolist = reportHelper.getVehicleStatusReport(fromDate,toDate,option,siteID,vendor);
	%><table border="1" style="width: 1300px;">
	<tr>
	<td>Vehicle</td>
	<td>Average Distance</td>
	<td>Average Time</td>
	<td>Max Trip per Day</td>
	<td>Min Trip per Day</td>
	<td>No Of Trips</td>
	<td>Total Distance</td>
	</tr>
            
      <%       for (ByVehicleTypeReportDto dto : dtolist) {%>
	<tr>
		<td align="left"><%=dto.getVehicleType()%></td>	
		
		<td><%=dto.getAverageDistance()%></td>								
		
		<td><%=dto.getAverageTime()%></td>				
		
		<td><%=dto.getMaxCount()%></td>
		
		<td><%=dto.getMinCount()%></td>
		
		<td><%=dto.getCount() %></td>
		
		<td><%=dto.getTotalDistance() %></td>
		</tr>
		
	<%
            }%>
            
	</table>
</body>
</html>
