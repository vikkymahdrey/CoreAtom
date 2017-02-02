<%@page import="com.agiledge.atom.service.TripDetailsService"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dao.TripDetailsDao"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="java.sql.*"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.text.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Tracked Trip Sheet</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>

<script type="text/javascript">

 
	 
	 </script>
</head>
<body>
	<%
    String tripDate = OtherFunctions.changeDateFromatToIso(request.getParameter("tripDate"));
	String tripId= request.getParameter("tripId");
	 
     
      TripDetailsDto tripSheetDto=new TripDetailsService().getTripSheetByTrip(tripId);
    ArrayList<TripDetailsDto> tripSheetSaved = new ArrayList<TripDetailsDto> ();
    tripSheetSaved.add( tripSheetDto);
%>
	<%
            long empid = 0;
            String employeeId = OtherFunctions.checkUser(session);
                empid = Long.parseLong(employeeId);
                
                
                %>
                
                <% String header=request.getParameter("header");
                if(header!=null&&header.equalsIgnoreCase("false")) {
                	%>
                	<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
                	<% 
                } else {
                %>
                
	<%@include file="Header.jsp"%>
			<% } %>
	<div id="body">
		<div class="content">
			<hr />


			<form method="POST" name="vendorTripSheetForm" action="ApproveTrips">




				<h3 align="center">Trip Sheet</h3>


				<%
				if(tripSheetSaved!=null&&tripSheetSaved.size()>0)
				{
				for (TripDetailsDto tripDetailsDtoObj : tripSheetSaved) {
				
					   
				 
			
				%>
				<table class="alternateColor" border="1" style="width: 80%;">
					<tr>
					</tr>
					<tr>
						<td colspan="8">
							<table style="background-color: #E8E8E6;">
								<tr>
									<td width="5%">
										<%
											if(tripDetailsDtoObj.isCanTravel())
					{ %> <input type="hidden" name="tripId"
										value="<%=tripDetailsDtoObj.getId() %>" /> <%} %>
									</td>
									<td width="20%">Vehicle type</td>
									<td width="20%"><%=tripDetailsDtoObj.getVehicle_type()%></td>
									<td width="20%">Trip ID</td>
									<td width="20%"><%=tripDetailsDtoObj.getTrip_code()%></td>
								</tr>

 					<tr>
						<th >#</th>
						<th width="20%">Name</th>
						<th width="20%">Area</th>
						<th width="20%">Place</th>
						<th width="20%">landmark</th>
						<th width="20%">Contact No</th>
			 
						 

					</tr>
					<%
				      int i = 1;
	        for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj.getTripDetailsChildDtoList()) {
	        	
	        	String approved="";
				String disapproved="checked";
			 
	    %>
					<tr>
						<td><%=i%></td>
						<td><%=tripDetailsChildDto.getEmployeeName()%></td>
						<td><%=tripDetailsChildDto.getArea()%></td>
						<td><%=tripDetailsChildDto.getPlace()%></td>
						<td><%=tripDetailsChildDto.getLandmark()%></td>
						<td><%=tripDetailsChildDto.getContactNumber()%></td>
						
					 
					</tr>
					<%
	            i++;
				 
	        
	        }%>


					</tbody>
				</table>

				<%
	    	 
				}
				}else
				{
					%><p>No trip Is is to display</p>

				<%
				}
				    
        %>






				<br /> 
				<% if(header!=null&&header.equalsIgnoreCase("false")) { %>
			
					<input style="margin-left: 50%" type="Button" name="add"
					class="formbutton" value="Close"
					onclick="javascript:window.close()" />
					<% } else { %>
						<input style="margin-left: 50%" type="Button" name="add"
					class="formbutton" value="Back"
					onclick="javascript:history.go(-1);" /> 
					<%} %>
					<br /> <br /> <br />
				<hr />

			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>

