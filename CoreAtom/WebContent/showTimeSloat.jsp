<%-- 
    Document   : newjsp
    Created on : Oct 22, 2012, 1:00:11 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.dto.TimeSloatDto"%>
<%@page import="com.agiledge.atom.dao.TimeSloatDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Time Slots</title>
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
	<br />
	<div class="content">
		<div class="content_resize">
			<%
			
      ArrayList<TimeSloatDto> timeSloatDtos=new TimeSloatDao().getTimeSloats();

%>
			<h3>Time Slot</h3>
			<hr />
			<div style="float:left; margin-left: 20px; padding: 5px;  "><button class="formbutton" onclick="location.href='timeSloat.jsp'"  >Modify Slots</button></div>
			<table>
				<thead>

					<tr>
						<th align="center">Traffic</th>
						<th align="center">Start Time</th>
						<th align="center">End Time</th>
						<th align="center">Speed</th>
					</tr>
				</thead>
				<%
for(TimeSloatDto dto:timeSloatDtos)
{
%>

				<tr>
					<td align="center"><%=dto.getTraffic()%></td>

					<td align="center"><%=dto.getTimeStart()%></td>
					<td align="center"><%=dto.getTimeEnd()%></td>
					<td align="center"><%=dto.getSpeed()%></td>					
				</tr>
				<%
}        %>
			</table>

		</div>
	</div>
</body>
</html>
