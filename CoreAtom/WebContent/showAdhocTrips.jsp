<%-- 
    Document   : routeSample
    Created on : Oct 25, 2012, 1:05:50 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="com.agiledge.atom.service.AdhocRoutingService"%>
<%@page import="com.agiledge.atom.dto.AdhocDto"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Adhoc Routing</title>

<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script type="text/javascript">        
            $(document).ready(function()
            {                                                                        
                $("#date").datepick();  
                getLogTime();       
            });     
        </script>
<script type="text/javascript">
			function assignVendor() {
				try { 
				var siteId= $("#siteId").val();
				var date = $("#date").val();
				 
				var goUrl=encodeURI(  "adhocVendorAssign.do?siteId="+siteId+"&tripDate="+ date);
			 
			location.href=goUrl;
				}catch (e) {
					// TODO: handle exception
					alert(e);
				}
				 
				 
				 
			}
            function validate()
            {
            	//var siteId=document.getElementById("siteId").value;
                var date=document.getElementById("date").value;   
                if(date.length<1)
                {
                    alert("Choose Date");                  
                    return false;
                }
            }
            
        </script>


</head>
<body>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<%
				List<SiteDto> siteDtoList = new SiteDao().getSites();
				String siteId = request.getParameter("siteId");
				String date = request.getParameter("date");
				ArrayList<TripDetailsDto> tripList = new AdhocRoutingService()
						.getAdhocRouting(siteId, date);
			%>
			<%
				long empid = 0;
				String employeeId = OtherFunctions.checkUser(session);
			%>
			<h3>Adhoc Trip Sheet</h3>
			<hr />
			<form method="post">
				<table>
					<tr>
						<td align="right">Choose Site</td>
						<td><select name="siteId" id="siteId">
								<%
									for (SiteDto siteDto : siteDtoList) {
								%>

								<option value="<%=siteDto.getId()%>"><%=siteDto.getName()%></option>
								<%
									}
								%>
						</select></td>



						<td align="right">Date</td>
						<td><input name="date" id="date" type="text" size="6" value="<%=date %>"
							class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}" /></td>



						<td><input type="submit" class="formbutton" value="Submit" /></td>
					</tr>


				</table>
			</form>
			<hr />
			<br/>

			<%
			int notSavedCount=0;
			int savedCount=0;
				if (tripList != null && tripList.size() > 0) {
			%>
			<form name="form1" action="AdhocTripUpdate" method="post">
				<table border="1" width="80%">
					<%
						for (TripDetailsDto tripDetailsDtoObj : tripList) {
					%>

					<tr>
						<th>&nbsp;</th>
						<th>Vehicle type</th>
						<th><%=tripDetailsDtoObj.getVehicle_type()%> <input type="hidden" value="<%=tripDetailsDtoObj.getVehicleTypeId()%>" name="vehicleType<%=tripDetailsDtoObj.getId()%>"/></th>
						
						<th>Date</th>
						<th><%=OtherFunctions
							.changeDateFromatToddmmyyyy(tripDetailsDtoObj
									.getTrip_date())%></th>						
							<th>Escort Trip</th>
						<th colspan="3"><%=tripDetailsDtoObj.getIsSecurity()%></th>
						<th>
						<%if(tripDetailsDtoObj.getStatus().equalsIgnoreCase("routed")) {
							 
							notSavedCount++;
						%>					
						<input type="checkbox" name="tripids"
							id="tripidcheck" value="<%=tripDetailsDtoObj.getId()%>"/>
							<%} else if(tripDetailsDtoObj.getStatus().equalsIgnoreCase("saved")) {
								 
								savedCount++;
								} 
						 
								%>
							
							</th>
							
					</tr>

					<tr>
						<th>&nbsp;</th>
						<th>Vehicle #</th>
						<th>&nbsp;</th>
						<th>Source</th>
						<th><%=tripDetailsDtoObj.getStartPlace() %></th>
						<th>Destination</th>
						<th><%=tripDetailsDtoObj.getEndPlace() %></th>
						
					</tr>
						<tr>
						<th>&nbsp;</th>
						<th>Status</th>
						<th><%=tripDetailsDtoObj.getStatus() %></th>
						<th>Start Time</th>
						<th><%=tripDetailsDtoObj.getStartTime() %></th>
						<th>End Time</th>
						<th><%=tripDetailsDtoObj.getStopTime() %></th>						
					</tr>
				
					<tr>
						<th>#</th>
						<th>Name</th>
						<th colspan="5"></th>

					</tr>
					<%
						int i = 1;
								for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
										.getTripDetailsChildDtoList()) {
					%>
					<tr>
						<th><%=i%></th>
						<td><%=tripDetailsChildDto.getEmployeeName()%></td>
						<td colspan="5"></td>						
					</tr>
					<%
						i++;
								}
					%>


					<%
						}
					%>
					</tbody>
				</table>
				<%if(notSavedCount>0) {%>		
				<p>
					<input type="submit" class="formbutton" value="Save" />
				</p>
				<%} %>
				<%
				 
				if(savedCount>0) {%>		
				<p>
					<input type="button" class="formbutton" value="AssignVenor" onclick="assignVendor()" />
				</p>
				<%} %>
			</form>
			<%
				}
			%>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
