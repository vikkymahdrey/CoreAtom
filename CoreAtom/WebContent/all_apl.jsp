<%-- 
    Document   : newjsp
    Created on : Oct 22, 2012, 1:00:11 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.APLService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>



<title>All APL</title>

</head>
<body>
	<div class="content">
		<div class="content_resize">
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
				OtherDao ob = null;
				ob = OtherDao.getInstance();
			%>
			<br />

			<%
				APLService APLServiceObj = new APLService();
				ArrayList<APLDto> APLDtoList = APLServiceObj.getAllAPL();
			%>

			<h3>APL List</h3>



			<hr />
			<iframe style="float: right; width: 48%" height="500px"
				src="marklandmark.jsp"></iframe>

			<table style="width: 50%">
				<thead>
					<tr>
						<th align="center">Area</th>
						<th align="center">Place</th>
						<th align="center">Landmark</th>
					</tr>
				</thead>
				<%
					for (APLDto aplDto : APLDtoList) {
				%>
				<tr>
					<td><%=aplDto.getArea()%></td>
					<td><%=aplDto.getPlace()%></td>
					<td><%=aplDto.getLandMark()%></td>
				</tr>
				<%
					}
				%>
			</table>

		</div>
	</div>
</body>
</html>
