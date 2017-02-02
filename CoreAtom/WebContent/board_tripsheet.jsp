<%-- 
    Document   : printTrips
    Created on : Nov 4, 2012, 5:19:23 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.service.TripDetailsService"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="com.agiledge.atom.dao.TripDetailsDao"%>
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
            String tripDate = request.getParameter("tripDate");
            String siteId = request.getParameter("siteId");
            String tripMode = "" + request.getParameter("tripMode");
            String tripTime = "" + request.getParameter("tripTime");
            ArrayList<TripDetailsDto> tripSheetSaved = new TripDetailsService().getTripSheetSaved(tripDate, tripMode, siteId, tripTime);
            %>
            <table border="1" style="width: 600px;">
            <%

            for (TripDetailsDto tripDetailsDtoObj : tripSheetSaved) {%>	
		<tr>
		</tr>
		<tr>
			<td>&nbsp</td>		
			<td>Trip ID</td>
			<td><%=tripDetailsDtoObj.getTrip_code()%></td>
			<td>&nbsp</td>				
			<td>Time</td>
			<td><%=tripDetailsDtoObj.getTrip_time()+"   "+tripDetailsDtoObj.getTrip_log()%></td>
		</tr>
		<thead>
			<tr>
				<th style="background-color: yellow;">#</th>
				<th style="background-color: yellow;">Name</th>
				<th style="background-color: yellow;">Area</th>
				<th style="background-color: yellow;">Place</th>
				<th style="background-color: yellow;">landmark</th>
				<th style="background-color: yellow;">Vehicle Details</th>
			</tr>
		</thead>
		<%
                int i = 1;
                for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj.getTripDetailsChildDtoList()) {
            %>
		<tr>
			<td><%=i%></td>
			<td><%=tripDetailsChildDto.getEmployeeName()%></td>
			<td><%=tripDetailsChildDto.getArea()%></td>
			<td><%=tripDetailsChildDto.getPlace()%></td>
			<td><%=tripDetailsChildDto.getLandmark()%></td>		
			<td>&nbsp;</td>
		</tr>

		<%
                    i++;
                }
                while(i<7)
                {%>
		<tr>
			<td><%=i%></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<%i++;}%>

	<%
            }%>
           
		</tbody>
	</table>
	<br>&nbsp;
	</br> 
</body>
</html>