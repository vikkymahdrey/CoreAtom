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
	 
     
     ArrayList<TripDetailsDto> tripSheetList=new TripDetailsService().viewTrackedTrip(tripId);
    ArrayList<TripDetailsDto> tripSheetSaved = tripSheetList;
%>
	<%
            long empid = 0;
            String employeeId = OtherFunctions.checkUser(session);
            if (employeeId == null||employeeId.equals("null") ) {
            	String queryString=request.getQueryString();
            	queryString=queryString.replaceAll("&","___");
             
                String param = request.getServletPath().substring(1) + "___"+queryString; 	response.sendRedirect("index.jsp?page=" + param);
            } else {
                empid = Long.parseLong(employeeId);
                %>
	<%@include file="Header.jsp"%>
	<%
            }
            OtherDao ob = null;
            ob = OtherDao.getInstance();
            
            boolean flag=false;
        %>

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
									<td width="20%">
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
									<td>&nbsp;</td>
									<td>IN/OUT Time</td>
									<td><%=tripDetailsDtoObj.getTrip_log() + "  " + tripDetailsDtoObj.getTrip_time()%></td>
									<td>Actual IN/OUT time</td>
									<td>
										<%
										String actualLogTime="";
										
										if(tripDetailsDtoObj.getActualLogTime()!=null&&!tripDetailsDtoObj.getActualLogTime().equals(""))
										{
											actualLogTime=tripDetailsDtoObj.getActualLogTime();
										}
										String vehicleNo="";
 
										
										if(tripDetailsDtoObj.getVehicleNo()!=null&&!tripDetailsDtoObj.getVehicleNo().equals(""))
										{
											vehicleNo= tripDetailsDtoObj.getVehicleNo();
										}
										 
										 
										
										
									
										%> <input type="text" readOnly value="<%=actualLogTime %>"
										name="actualLogTime-<%=tripDetailsDtoObj.getId() %>" /> <input
										type="hidden"
										name="actualTripLog-<%=tripDetailsDtoObj.getId() %>"
										value="<%=tripDetailsDtoObj.getTrip_log() %>" />

									</td>
								</tr>
								<tr>
									<td width="20%"><i><%=tripDetailsDtoObj.getApprovalStatus()%></i>
									</td>
									<td width="20%"></td>
									<td width="20%"></td>
									<td width="20%">Vehicle No</td>
									<td width="20%"><input type="text" readOnly
										value="<%=vehicleNo %>"
										name="vehiclNo-<%=tripDetailsDtoObj.getId() %>" /></td>
								</tr>


								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td>Escort Trip :</td>
									<td><select class="escort" disabled="disabled"
										id="escort-<%=tripDetailsDtoObj.getId()%>"
										name="escort-<%=tripDetailsDtoObj.getId()%>">
											<%
									
									String escort[]={"YES","NO"};
									String escortSelect="";
									for(String es: escort)
									{
										if(tripDetailsDtoObj.getEscort()!=null)
										{
											escortSelect="";
											
											if(es.equals(tripDetailsDtoObj.getEscort()))
											{
												escortSelect="selected";
											}
											 
												
											 
										}
										
										%>
											<option <%=escortSelect %> value="<%=es %>"><%=es %></option>
											<%
									}
									%>


									</select></td>
								</tr>
								<%
								String escortRowStyle="  "; 
								if(tripDetailsDtoObj.getEscort()!=null&&tripDetailsDtoObj.getEscort().equals("NO"))
								{
									
									
									escortRowStyle=" style='display:none;'";
								}
								%>
								<tr id="escortRow-<%=tripDetailsDtoObj.getId() %>"
									<%=escortRowStyle %>>
									<td></td>
									<td></td>
									<td></td>
									<td>Escort Clock :</td>
									<td>
										<%
										if(tripDetailsDtoObj.getEscortNo() ==null)
										{
											tripDetailsDtoObj.setEscortNo(""); 
										}
									%> <input type="text" class="escortClock" readonly="readonly"
										name="escortClock-<%=tripDetailsDtoObj.getId()%>"
										id="escortClock-<%=tripDetailsDtoObj.getId()%>"
										value="<%=tripDetailsDtoObj.getEscortNo() %>" /> <img
										id="escortClock-<%=tripDetailsDtoObj.getId()%>_errorImage"
										src="images/error.png" style="display: none;" />
									</td>
								</tr>

							</table>
						</td>
					</tr>
					<tr>
						<th width="4%">#</th>
						<th width="20%">Name</th>
						<th width="20%">Area</th>
						<th width="20%">Place</th>
						<th width="20%">landmark</th>
						<th width="10%">Contact No</th>
						<th width="6%">Present</th>
						<th></th>

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
						<td>
							<%
						String checked="";
						
						if(tripDetailsChildDto.getShowStatus()!=null&&tripDetailsChildDto.getShowStatus().equals("Show"))
						{
							checked="checked";
							disapproved="";
						}
						String type="";
						if(tripDetailsDtoObj.getTrip_code().contains("P"))
						{
							type="P";
						}else
						{
							type="D";
						}
						
							 %> <input type="checkbox" disabled <%=checked %> name="approve"
							value="<%=tripDetailsChildDto.getEmployeeId() + "-" + type %>" />
						</td>

						<td><%=tripDetailsChildDto.getReason() == null ? " "
									: tripDetailsChildDto.getReason()%></td>

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






				<br /> <input style="margin-left: 50%" type="Button" name="add"
					class="formbutton" value="Back"
					onclick="javascript:history.go(-1);" /> <br /> <br /> <br />
				<hr />

			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>

