<%@page import="com.agiledge.atom.service.TripDetailsService"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dao.TripDetailsDao"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.text.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Trip Sheet Modify</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
	function validateForm() {
		var total = 0;		
			/* if(isNaN(document.form1.tripidcheck.value))			
				{
				for ( var i = 0; i < document.form1.tripidcheck.length; i++) {
					if (document.form1.tripidcheck[i].checked) {
						total = total + 1;					
				}				
			}
				}
			else if(document.form1.tripidcheck.checked)
				{
				total=1;
				}
			 */
			   
			 $("input[name=tripidcheck]:checked").each(function () {
			 
				 total = total + 1;
			 });
			 /* $("input[name=tripidcheck]:checked").each(function () {
				 
			 }); */
			 
			// alert(total);
			 if(total>4)
				 {
				 alert(" select Maximum  of 4");
				 return false;
				 }
			if (total < 1) {
				alert(" select atleast One");
				return false;
			}
		//	 return false;
					
	}
	function saveTrip() {
		if (confirm("Are you sure you want to save this tripsheet?")) {
			var tripDate = document.getElementById("tripDate").value;
			var tripTime = document.getElementById("tripTime").value;
			var tripMode = document.getElementById("tripMode").value;
			var siteId = document.getElementById("siteId").value;
			window.location = "SaveTrip?siteId=" + siteId + "&tripDate="
					+ tripDate + "&tripTime=" + tripTime + "&tripMode="
					+ tripMode + "";

		}
	}
	function saveSelected() {
		var total = 0;
		var tripIds = new Array();
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		
			 $("input[name=tripidcheck]:checked").each(function () {
				 tripIds.push($(this).val());
				 total = total + 1;
				 
				 
			 });
			 if (total < 1) {
				 alert("No trips Are Selected")
			 }
			 else
				 {
				 if (confirm("Are you sure you want to save selected trips?")) {
			 window.location = "SaveTrip?siteId=" + siteId + "&tripDate="
				+ tripDate + "&tripTime=" + tripTime + "&tripMode="
				+ tripMode + "&tripids="+tripIds;
				 }
		}
					
	}
	function validate() {
		var total = 0;
		for ( var i = 0; i < document.form1.tripidcheck.length; i++) {
			if (document.form1.tripidcheck[i].checked) {
				total = total + 1;
			}
			if (total > 10) {
				document.form1.tripidcheck[2].checked = false;
				alert("Maximum select two");
				
				
				return false;
			}
		}
	}
</script>


</head>
<body>
	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);		
			empid = Long.parseLong(employeeId);
			%>
	<%@include file="Header.jsp"%>

	<div id="body">
		<div class="content">
			<hr />
			<%
						String tripDate = request.getParameter("tripDate");
						String tripTime = request.getParameter("tripTime");
						String tripMode = request.getParameter("tripMode");
						String siteId = request.getParameter("siteId");
						ArrayList<TripDetailsDto> tripSheetList = new TripDetailsService()
								.getTripSheetModified(tripDate, tripMode, siteId, tripTime);
						ArrayList<TripDetailsDto> tripSheetListActual = new TripDetailsService()
								.getTripSheetActual(tripDate, tripMode, siteId, tripTime);

						ArrayList<TripDetailsDto> tripSheetSaved = new TripDetailsService().getTripSheetSaved(tripDate, tripMode, siteId, tripTime);
					%>

			<hr />
			<h3>Modified Trip Sheet</h3>
			<form name="form1" action="modify_trip.jsp" method="POST"
				onsubmit="return validateForm()">
				<p>
					<input type="submit" class="formbutton" value="Modify" /> 				
					<input
						type="button" class="formbutton" value="Save" onclick="saveTrip()" />
						<input
						type="button" class="formbutton" value="Save Selected" onclick="saveSelected()" />
					<input type="button" class="formbutton" value="Back"
						onclick="javascript:history.go(-1);" />
				</p>
				<input type="hidden" name="tripDate" id="tripDate"
					value="<%=tripDate%>" /> <input type="hidden" name="siteId"
					id="siteId" value="<%=siteId%>" /> <input type="hidden"
					name="tripMode" id="tripMode" value="<%=tripMode%>" /> <input
					type="hidden" name="tripTime" id="tripTime" value="<%=tripTime%>" />

				<hr />
				<table border="1" width="80%">
				<%
							for (TripDetailsDto tripDetailsDtoObj : tripSheetList) {
						%>
				
					<tr>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>Vehicle type</td>
						<td colspan="3"><%=tripDetailsDtoObj.getVehicle_type()%></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td><input type="checkbox" name="tripidcheck"
							id="tripidcheck" value="<%=tripDetailsDtoObj.getId()%>"/></td>
					</tr>

					<tr>
						<td>&nbsp;</td>
						<td>Vehicle #</td>
						<td colspan="3">&nbsp;</td>
						<td>Date</td>
						<td><%=OtherFunctions
						.changeDateFromatToddmmyyyy(tripDetailsDtoObj
								.getTrip_date())%></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>Escort Trip</td>
						<td colspan="3"><%=tripDetailsDtoObj.getIsSecurity()%></td>
						<td>IN/OUT Time</td>
						<td><%=tripDetailsDtoObj.getTrip_log() + "  "
						+ tripDetailsDtoObj.getTrip_time()%></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>Escort Clock</td>
						<td colspan="3">&nbsp;</td>
						<td>Actual IN/OUT time</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>#</td>
						<td>Name</td>
						<td>Contact</td>
						<td>Area</td>
						<td>Place</td>
						<td>landmark</td>
						<td>Status remark</td>
						<td>Signature</td>
					</tr>
					<%
								int i = 1;
									for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
											.getTripDetailsChildDtoList()) {
							%>
					<tr>
						<td><%=i%></td>
						<td><%=tripDetailsChildDto.getEmployeeName()%></td>
						<td><%=tripDetailsChildDto.getContactNumber()%></td>
						<td><%=tripDetailsChildDto.getArea()%></td>
						<td><%=tripDetailsChildDto.getPlace()%></td>
						<td><%=tripDetailsChildDto.getLandmark()%></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
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
				<%if (tripSheetSaved != null && tripSheetSaved.size() > 0) { %>
<h3>Saved Trip Sheet</h3>

				<hr />
				<table border="1" width="80%">
				<%
							for (TripDetailsDto tripDetailsDtoObj : tripSheetSaved) {
						%>
				
					<tr>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>Vehicle type</td>
						<td colspan="3"><%=tripDetailsDtoObj.getVehicle_type()%></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td><input type="checkbox" name="tripidcheck"
							id="tripidcheck" value="<%=tripDetailsDtoObj.getId()%>"/></td>
					</tr>

					<tr>
						<td>&nbsp;</td>
						<td>Vehicle #</td>
						<td colspan="3">&nbsp;</td>
						<td>Date</td>
						<td><%=OtherFunctions
						.changeDateFromatToddmmyyyy(tripDetailsDtoObj
								.getTrip_date())%></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>Escort Trip</td>
						<td colspan="3"><%=tripDetailsDtoObj.getIsSecurity()%></td>
						<td>IN/OUT Time</td>
						<td><%=tripDetailsDtoObj.getTrip_log() + "  "
						+ tripDetailsDtoObj.getTrip_time()%></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>Escort Clock</td>
						<td colspan="3">&nbsp;</td>
						<td>Actual IN/OUT time</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>#</td>
						<td>Name</td>
						<td>Contact</td>
						<td>Area</td>
						<td>Place</td>
						<td>landmark</td>
						<td>Status remark</td>
						<td>Signature</td>
					</tr>
					<%
								int i = 1;
									for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
											.getTripDetailsChildDtoList()) {
							%>
					<tr>
						<td><%=i%></td>
						<td><%=tripDetailsChildDto.getEmployeeName()%></td>
						<td><%=tripDetailsChildDto.getContactNumber()%></td>
						<td><%=tripDetailsChildDto.getArea()%></td>
						<td><%=tripDetailsChildDto.getPlace()%></td>
						<td><%=tripDetailsChildDto.getLandmark()%></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<%
								i++;
									}
							%>
				

				<%
							}}
						%>
							</tbody>
				</table>

				<p>
					<input type="submit" class="formbutton" value="Modify" /> <input
						type="button" class="formbutton" value="Save" onclick="saveTrip()" />
					<input type="button" class="formbutton" value="Back"
						onclick="javascript:history.go(-1);" />
				</p>
			</form>

			<%
						if (tripSheetListActual != null && tripSheetListActual.size() > 0) {
					%>
			<h3>System Generated Trip Sheet</h3>
			<hr />
<table border="1" width="80%">
			<%
						for (TripDetailsDto tripDetailsDtoObj : tripSheetListActual) {
					%>
			
				<tr>
				</tr>
				<tr>
					<td>&nbsp</td>
					<td>Vehicle type</td>
					<td colspan="3"><%=tripDetailsDtoObj.getVehicle_type()%></td>
					<td>Trip ID</td>
					<td><%=tripDetailsDtoObj.getTrip_code()%></td>
				</tr>

				<tr>
					<td>&nbsp</td>
					<td>Vehicle #</td>
					<td colspan="3">&nbsp;</td>
					<td>Date</td>
					<td><%=OtherFunctions
							.changeDateFromatToddmmyyyy(tripDetailsDtoObj
									.getTrip_date())%></td>
				</tr>
				<tr>
					<td>&nbsp</td>
					<td>Escort Trip</td>
					<td colspan="3"><%=tripDetailsDtoObj.getIsSecurity()%></td>
					<td>IN/OUT Time</td>
					<td><%=tripDetailsDtoObj.getTrip_log() + "  "
							+ tripDetailsDtoObj.getTrip_time()%></td>
				</tr>
				<tr>
					<td>&nbsp</td>
					<td>Escort Clock</td>
					<td colspan="3">&nbsp;</td>
					<td>Actual IN/OUT time</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>#</td>
					<td>Name</td>
					<td>Area</td>
					<td>Place</td>
					<td>landmark</td>
					<td>Status remark</td>
					<td>Signature</td>
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
					<td>&nbsp;</td>
					<td>&nbsp;</td>
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
						}
					%>

			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>