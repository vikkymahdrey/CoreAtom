<%-- 
    Document   : newjsp
    Created on : Oct 22, 2012, 1:00:11 PM
    Author     : muhammad
--%>

<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dao.RouteDao"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dao.BranchDao"%>
<%@page import="com.agiledge.atom.dto.BranchDto"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="com.agiledge.atom.service.RouteService"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.APLService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<Script>
function showAuditLog(relatedId,moduleName){
	var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var size = "height=450,width=900,top=200,left=300," + params;
	var url="ShowAuditLog.jsp?relatedNodeId="+relatedId+"&moduleName="+moduleName;	
    newwindow = window.open(url, 'AuditLog', size);

	if (window.focus) {
	newwindow.focus();
}
}
function submitSiteForm() {
	try{
	document.getElementById("siteForm").submit();
	}catch(e)
	{alaert(e);}
	
}
</Script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Routes</title>
</head>
<body>
	<%
          long empid=0;
        String employeeId = OtherFunctions.checkUser(session);
            empid = Long.parseLong(employeeId);
            String site = request.getParameter("siteId");
            String orderTheRoute = request.getParameter("orderTheRoute");
			List<SiteDto> dtos= new SiteDao().getSites();
            %>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<br />
			<form action="route_list.jsp" name="siteForm" id="siteForm">
				<table>
					<tr>
						<td>Site</td>
						<td><select name="siteId" onchange="submitSiteForm()">
								<option>Select</option>
								<%
									for (SiteDto dto : dtos) {
										if (dto.getId().equals(site)) {
								%>
								<option value="<%=dto.getId()%>" selected="selected"><%=dto.getName()%></option>
								<%
									} else {
								%>
								<option value="<%=dto.getId()%>"><%=dto.getName()%></option>
								<%
									}
									}
								%>
						</select></td>
					</tr>
				</table>
			
			<%
      ArrayList<RouteDto> routeDtoList=new RouteService().getAllRoutes(site);

%>
			<h3>Route List</h3>
			
			<input type="submit" name="orderTheRoute" class="formbutton" onclick="submitSiteForm()" value="Order The Route" >
			
			<%
			if(orderTheRoute!=null &&!orderTheRoute.equals("")&&!orderTheRoute.equals("null") )
			{
				System.out.println("orderTheRoute IN   "+orderTheRoute);
				new RouteService().orderTheRoute(site);
			}			
			%>
			</form>	
			<hr />
			<p align="center"> <a href="downloadroute.jsp?site=<%=site%>">Download all routes</a></p>
			<table>
			
				<thead>

					<tr>
						<th align="center">Id</th>
						<th align="center">Route</th>
						<th align="center">Type</th>
						<th align="center">Site</th>
						<th align="center">Path</th>
						<th align="center">Audit Log</th>
					</tr>
				</thead>
				<%
for(RouteDto routeDto:routeDtoList)
{
%>

				<tr>
					<td align="center"><%=routeDto.getRouteId()%></td>

					<td align="center"><a
						href="route_modify.jsp?routeId=<%=routeDto.getRouteId()%>"><%=routeDto.getRouteName()%></a></td>
					<td align="center"><%=routeDto.getRouteTypeDesc()%></td>
					<td align="center"><%=routeDto.getSiteName()%></td>
					<td><a
						href="edit_route.jsp?routeId=<%=routeDto.getRouteId()%>">Edit</a></td>
					<td align="center"><input type="button" class="formbutton"
						onclick="showAuditLog(<%=routeDto.getRouteId()%>,'<%=AuditLogConstants.ROUTE_MODULE%>');"
						value="Audit Log" /></td>
				</tr>
				<%
}        %>
			</table>

		</div>
	</div>
</body>
</html>
