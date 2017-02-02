<%-- 
    Document   : newjsp
    Created on : Oct 22, 2012, 1:00:11 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="com.agiledge.atom.service.RouteService"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.APLService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Print Trips</title>
<%

        String mimeType = "application/vnd.ms-excel";        
        response.setContentType(mimeType);        
        String fname = new Date().toString();
        response.setHeader("Content-Disposition", "inline; filename = Route " + fname + ".xls");        
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Pragma", "public");
        response.setHeader("Expires", "Mon, 1 Jan 1995 05:00:00 GMT");
        %>
</head>
<body>

			<%
				RouteService routeService=new RouteService();
			String site=request.getParameter("site");
				ArrayList<RouteDto> routeDtoList = routeService.getRoutes(site);
			%>
			<table style="width: 60%" border="1">
			<!-- 	<thead>
					<tr>
						 
						<th align="center">Route Name</th>
						<th align="center">Area</th>
						<th align="center">Place</th>
						<th align="center">Landmark</th>
						<th align="center">Type</th>
					</tr>
				</thead>
				
			 -->	
				<%
				RouteDto preRouteDto = new RouteDto();
				preRouteDto.setRouteId(-1);
					for (RouteDto routeDto: routeDtoList) {
					
				%>
				<%
				if(routeDto.getRouteId()!=preRouteDto.getRouteId()) {
					
					%>
					<tr>
						 
						<th align="center">Route Name</th>
						<th align="center">Area</th>
						<th align="center">Place</th>
						<th align="center">Landmark</th>
						<th align="center">Type</th>
					</tr> 
					<% 
				}
				preRouteDto.setRouteId(routeDto.getRouteId());
				%>
				<tr>
				
					 
					<td><%=routeDto.getRouteName()%></td>
					<td><%=routeDto.getArea()%></td>
					<td><%=routeDto.getPlace()%></td>
					<td><%=routeDto.getLandmark()%></td>
					<td><%=routeDto.getRouteTypeDesc()%></td>
				</tr>
				<%
					}
				%>
			</table>
</body>
</html>
