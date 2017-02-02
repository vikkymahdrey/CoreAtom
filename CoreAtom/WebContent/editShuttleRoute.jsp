<%-- 
    Document   : modify_trip
    Created on : Oct 31, 2012, 6:58:21 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.service.RouteService"%>
<%@page import="com.agiledge.atom.dao.SettingsDoa"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="com.agiledge.atom.service.APLService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Modify Route</title>

<script type="text/javascript">
	
</script>
</head>
<body>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<%
				long empid = 0;
				String employeeId = OtherFunctions.checkUser(session);				
					empid = Long.parseLong(employeeId);						
				int routeId = Integer.parseInt(request.getParameter("routeId"));
				RouteService routeService = new RouteService();
				RouteDto routeDto = routeService.getShuttleMasterRouteDetails(routeId);				
			%>

			<h3>Modify Route Info</h3>
			<form action="ModifyMasterRoute" name="modifymasterRoute"
				id="modifymasterRoute" method="POST">
				<table border="0" width="100%">
					<tbody>
						<tr>
							<td align="center">Route Name</td>
							<td><input type="text" name="routeName"
								value="<%=routeDto.getRouteName()%>" /></td>

						</tr>
						
						<tr>
							<td>&nbsp; <input type="hidden" name="routeId"
								value="<%=routeId%>" />
							</td>
							<td colspan="2"><input type="submit" id="submitbtn"
								name="submitbtn" class="formbutton" value="Submit"></td>
							<td>&nbsp;</td>
						</tr>
					</tbody>
				</table>
			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
