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
            String notSaved = request.getParameter("notSaved");
            ArrayList<TripDetailsDto> tripSheetSaved = null;
            if(OtherFunctions.isEmpty(notSaved)) {
            	tripSheetSaved = new TripDetailsService().getTripSheetSaved(tripDate, tripMode, siteId, tripTime);
            } else {
            	tripSheetSaved = new TripDetailsService().getTripSheetModified(tripDate, tripMode, siteId, tripTime);
            }
            for (TripDetailsDto tripDetailsDtoObj : tripSheetSaved) {%>
	<table border="1" style="width: 1300px;">
		<tr>
		<td>&nbsp;</td>
		<td colspan="2">Date</td>
		<td align="left"><%=OtherFunctions.changeDateFromatToddmmyyyy(tripDetailsDtoObj.getTrip_date())%></td>								
		<td align="left"><%=tripDetailsDtoObj.getTrip_code() %></td>
		<td align="left"><%=tripDetailsDtoObj.getVehicle_type() %></td>
		<td align="left"><%=tripDetailsDtoObj.getVehicleNo() %></td>
		<td align="left"><%=tripDetailsDtoObj.getDistance() %></td>
		</tr>

		<%
                }%> 
		</tbody>
	</table>

</body>
</html>
