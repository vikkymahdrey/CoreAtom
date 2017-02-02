<%@page import="com.agiledge.atom.dto.TripCommentDto"%>
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
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Trip Sheet</title>
	<%
		String tripDate = OtherFunctions.changeDateFromatToIso(request
				.getParameter("tripDate"));
		String siteId = request.getParameter("siteId");
		String tripMode = "" + request.getParameter("tripMode");
	 
		String employeePersonnelNo = request.getParameter("personnelNo");
		String vendor = request.getParameter("vendor");
		String [] tripTimes= request.getParameterValues("tripTime");
		String approvalStatusParam=request.getParameter("approvalStatusParam");
		approvalStatusParam=approvalStatusParam==null||approvalStatusParam.equals("")?"All":approvalStatusParam;
		
		for(String triptime: tripTimes)
		{
		System.out.println("Trip time :"+ triptime);
		}
		 String mimeType = "application/vnd.ms-excel";        
	        response.setContentType(mimeType);        
	        String fname = new Date().toString();
	        response.setHeader("Content-Disposition", "inline; filename = TrackedTrip" + tripDate +"( "+approvalStatusParam+").xls");        
	        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
	        response.setHeader("Cache-Control", "post-check=0, pre-check=0");
	        response.setHeader("Pragma", "no-cache");
	        response.setHeader("Pragma", "public");
	        response.setHeader("Expires", "Mon, 1 Jan 1995 05:00:00 GMT");
        %>


</head>
<body>

	<%
	boolean flag = false;
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
					empid = Long.parseLong(employeeId);
	%>
	 

 
		 


		 
			 
					 	 
							
									
								<%
										String [] status= new String[1];
												status[0]=approvalStatusParam;
												System.out.println("Status : "+approvalStatusParam);
												if(approvalStatusParam.trim().equals("All"))
												{
													
													status= new String[6]; 
												    status[0]="";
													status[1]="Rejected by Transport Co-ordinator";
													status[2]="Sent for TC approval";
													status[3]="Rejected by Transport Manager";
													status[4]="Sent for TM approval";
													status[5]="Approved by Transport Manager";
												}
										ArrayList<TripDetailsDto> tripSheetList = new TripDetailsService()
										.getTrackedTripSheet(tripDate, tripMode, siteId, tripTimes,
												employeePersonnelNo, vendor, status);
										 System.out.println("Size : "+tripSheetList.size());
								ArrayList<TripDetailsDto> tripSheetSaved = tripSheetList;
									%>
							 
								
						 
					 	
						 
			 
 

			
					<% 
			 
					if (tripSheetSaved != null && tripSheetSaved.size() > 0) {

						flag = true;
				%>
				<!-- From here modified trip sheet starts -->

				<h3>Trip Sheet</h3>
		<%
					for (TripDetailsDto tripDetailsDtoObj : tripSheetSaved) {
						
				%>
						<table border="1" style="width: 85%;">
					<tr>
						<td colspan="8">
		
							<table style="background-color: #E8E8E6; width: 100%">
								<tr>
									<td width="20%">
									  
									</td>
									<td width="20%">Vehicle type</td>
									<td width="20%"><%=tripDetailsDtoObj.getVehicle_type()%></td>
									<td width="20%">Trip ID</td>
									<td width="20%"><%=tripDetailsDtoObj.getTrip_code()%></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>IN/OUT Time</td>
									<td><%=tripDetailsDtoObj.getTrip_log() + "  "
							+ tripDetailsDtoObj.getTrip_time()%></td>
									<td>Actual IN/OUT time</td>
									<td align="left">
										 <%if( tripDetailsDtoObj.getTrip_time()!=null){ %>
											<%= tripDetailsDtoObj.getTrip_time() %>
											<%} %>
									</td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td>Escort Trip :</td>
									<td> 
											<%if(tripDetailsDtoObj .getEscort()!=null) { %>
												<%=tripDetailsDtoObj .getEscort() %>
											<%} %>
									</td>
								</tr>
								
								<%
								String escortRowStyle="  "; 
								if((tripDetailsDtoObj.getEscort()==null||tripDetailsDtoObj.getEscort().equals("NO"))==false)
								{
									
									
									 
							%>
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td>Escort Clock :</td>
									<td>
										 
											<%=tripDetailsDtoObj.getEscortNo() %> 
										 
									  
									</td>
								</tr>
							<%} %>
								
								<tr>
									<td width="20%" colspan="2" align="left"><i><%=tripDetailsDtoObj.getApprovalStatus()%></i></td>

									<td width="20%">
										 
									</td>
									<td width="20%">Vehicle No</td>
									<td width="20%">   
										<%=tripDetailsDtoObj.getVehicleNo()==null?"":tripDetailsDtoObj.getVehicleNo() %>
								 </td>
								</tr>

								 

							</table>
						</td>
						
					</tr>
					<tr  >
						<th width="4%">#</th>
						<th width="12%">Name</th>
						<th width="20%">Area</th>
						<th width="20%">Place</th>
						<th width="20%">landmark</th>
						<th width="10%">Contact No</th>
						<th width="6%">Present</th>
						<th width="6%">Reason For No-Show</th>
						 

					</tr>
					<%
						int i = 1;
								for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
										.getTripDetailsChildDtoList()) {
									 %>
					<tr>
						<td><%=i%></td>
						<td><%=tripDetailsChildDto.getEmployeeName()%></td>
						<td><%=tripDetailsChildDto.getArea()%></td>
						<td><%=tripDetailsChildDto.getPlace()%></td>
						<td><%=tripDetailsChildDto.getLandmark()%></td>
						<td><%=tripDetailsChildDto.getContactNumber()%></td>
						<td>
						<% if(tripDetailsChildDto.getShowStatus()!=null)
							{
							
							%>
							<%=tripDetailsChildDto.getShowStatus()%>
							<%} %>
						</td>
						<td>
							<% if(tripDetailsChildDto.getReason()!=null)
							 {
								
								%>
								<%=tripDetailsChildDto.getReason() %>
								<%
							 }
							%>
							 
						</td>
                       </tr>
                      
					<%
						i++;
								}
								%>
								 <tr> 
                        	 <td colspan="8" >
								<%
						
						if(tripDetailsDtoObj.getComments()!=null&&tripDetailsDtoObj.getComments().size()>0){ %>
					 
						<div style="background-color: #2CEFF2" >
						<b>	 Comments</b> 
						</div>

						<% 
						for(TripCommentDto tripCommentDto: tripDetailsDtoObj.getComments())
						{
							
						%>
						
						 
					     <div style="background-color: #C2C2C2"    >
						
					  <%=tripCommentDto.getCommentedByName() %>  &nbsp;On&nbsp;
					 <%=tripCommentDto.getCommentedDate() %> 
						:  &nbsp;&nbsp;&nbsp;&nbsp;
						
						<%=tripCommentDto.getComment() %>
						 
						</div>
						<%} %>
						
						 
						<%} %>
                        	 </td>
                      


					</tr>
					<tr>
						<td colspan="8">
						</td>
					</tr>
								<%
					}
					%>


					</tbody>
				</table>
				<br /> <br /> <br />


			


				<%}
					if (!flag) {
				%>
				<p>No trips to display</p>


				<%
					}
				%>
			 
			 
			 
			 
				&nbsp; <br /> <br />
				<hr />

	 
</body>
</html>

