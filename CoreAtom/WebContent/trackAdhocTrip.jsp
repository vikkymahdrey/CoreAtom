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

<script src="js/JavaScriptUtil.js"></script>
<script src="js/Parsers.js"></script>
<script src="js/InputMask.js"></script>


<script type="text/javascript">
	$(document).ready(function() {
		$(".escortIds").hide();
		$("#date").datepick();
		//getLogTime();                                
		$(".isEscort").change(function() {
			if ($(this).val() == "YES") {
				$(this).parent().next().children().show();
			} else {
				$(this).parent().next().children().hide();
			}
		});

	});
</script>
<script type="text/javascript">
	function validate() {
		//var siteId=document.getElementById("siteId").value;
		var date = document.getElementById("date").value;
		if (date.length < 1) {
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
				date = date==null ? "" : date;
				ArrayList<TripDetailsDto> tripList = new AdhocRoutingService()
						.getAdhocRouting(siteId, date);
				String selected = "";
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
						<td><select name="siteId" id="siteId" onchange="getLogTime()">
								<%
									for (SiteDto siteDto : siteDtoList) {
										String siteSelect = "";
										if(siteId!=null && siteId.equalsIgnoreCase(siteDto.getId())) {
											siteSelect = "selected";
										}
								%>

								<option <%=siteSelect %> value="<%=siteDto.getId()%>"><%=siteDto.getName()%></option>
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
			<br />

			<%
				int savedCount = 0;
				if (tripList != null && tripList.size() > 0) {
			%>
			<form name="form1" action="AdhocTripUpdate" method="post">
				<input type="hidden" name="siteId" value="<%=siteId %>" />
				<input type="hidden" name="date" value="<%=date %>" /> 
				<table border="1" width="80%">
					<%
						for (TripDetailsDto tripDetailsDtoObj : tripList) {
					%>

					<tr>
						<th>&nbsp;</th>
						<th>Vehicle type</th>
						<th><%=tripDetailsDtoObj.getVehicle_type()%></th>

						<th>Date</th>
						<th><%=OtherFunctions
							.changeDateFromatToddmmyyyy(tripDetailsDtoObj
									.getTrip_date())%></th>
						<th>&nbsp;</th>
						<th>Status</th>
						<th><%=tripDetailsDtoObj.getStatus()%></th>
						<%
							if (tripDetailsDtoObj.getStatus().equalsIgnoreCase("saved")) {
										savedCount++;
						%>
						<th><input type="checkbox" name="tripids" id="tripidcheck"
							value="<%=tripDetailsDtoObj.getId()%>" /> <%
 	}
 %></th>

					</tr>

					<tr>
						<th>&nbsp;</th>
						<th>Vehicle #</th>
						<th><input type="text"
							name="vehicleNo<%=tripDetailsDtoObj.getId()%>" value="<%=tripDetailsDtoObj.getVehicleNo() %>" id="vehicleNo" /></th>
						<th>Source</th>
						<th><%=tripDetailsDtoObj.getStartPlace()%></th>
						<th>Destination</th>
						<th><%=tripDetailsDtoObj.getEndPlace()%></th>
						<th>Distance: <input type="text"
							name="distance<%=tripDetailsDtoObj.getId()%>"
							value="<%=tripDetailsDtoObj.getDistance()%>" /></th>

					</tr>
					<tr>
						<th>&nbsp;</th>
						<th>Start Time</th>
						<th><%=tripDetailsDtoObj.getStartTime()%></th>
						<th>End Time</th>
						<th><%=tripDetailsDtoObj.getStopTime()%></th>
						<th>IS Escort</th>
						<th><select
							name="escortApprove<%=tripDetailsDtoObj.getId()%>"
							class="isEscort">
								<option value="NO">NO</option>
								<%
									selected = "";
											if (tripDetailsDtoObj.getEscort().equalsIgnoreCase(
													"YES")) {
												selected = "selected='selected'";
											}
								%>
								<option value="YES" <%=selected%>>YES</option>
						</select></th>
						<th><input type="text"
							name="escortId<%=tripDetailsDtoObj.getId()%>" class="escortIds" />
						</th>



					</tr>

					<tr>
						<th>#</th>
						<th>Name</th>
						<th colspan="6"></th>

					</tr>
					<%
						int i = 1;
								for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
										.getTripDetailsChildDtoList()) {
					%>
					<tr>
						<th><%=i%></th>
						<td><%=tripDetailsChildDto.getEmployeeName()%></td>
						<td colspan="6"></td>
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
				<%
					 if(savedCount>0) {
				%>
				<p>
					<input type="submit" name="Approve" class="formbutton"
						value="Approve" /> &nbsp;&nbsp;&nbsp;&nbsp; <input type="submit"
						name="Reject" class="formbutton" value="Reject" />
				</p>
				<%
					 }
				%>
			</form>
			<%
				}
			%>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
