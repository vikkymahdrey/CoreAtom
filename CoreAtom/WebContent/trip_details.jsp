<%@page import="com.agiledge.atom.service.TripDetailsService"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
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
<title>Trip Sheet</title>
<style>
.hd {
	background-color: #F4F0F5;
}
</style>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
	function saveTrip(reflag) {
		var flag="true";
		if(reflag){
        var total=document.getElementById("stotaltrip").value;
        for(var i=1;i<=total;i++){
        	var status=document.getElementById("saveflag"+i).value;
        	if(status=="false"){
        		flag="false";
        		break;
        	}
        }
		}
        if(flag=="true"){
		if (confirm("Are you sure you want to save this tripsheet?") == true) {
			var tripDate = document.getElementById("tripDate").value;
			var tripTime = document.getElementById("tripTime").value;
			var tripMode = document.getElementById("tripMode").value;
			var siteId = document.getElementById("siteId").value;
			window.location = "SaveTrip?siteId=" + siteId + "&tripDate="
					+ tripDate + "&tripTime=" + tripTime + "&tripMode="
					+ tripMode + "";
		}
        }
        else{
        	alert("Escort needed for some trips!");
        }
       
	}
	function checkTime(){
		var tripTime = document.getElementById("tripTime").value;
		var site=document.getElementById("siteId").value;
		var flag=false;
		try {
			var xmlhttp;
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					tripTime=tripTime.replace(":",".");
					var i = xmlhttp.responseText;
					if(tripTime<(i.split("#")[1]).replace(":",".") ||tripTime>(i.split("#")[0]).replace(":",".")){
						flag=true;
					}
					saveTrip(flag);
						}
			}
			xmlhttp.open("POST", "GetNightShiftTime?site="
					+ site, true);
			xmlhttp.send();
		} catch (e) {

			alert(e);
		}
	}
	function printPage() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		window.location = "print_trips.jsp?siteId=" + siteId + "&tripDate="
				+ tripDate + "&tripTime=" + tripTime + "&tripMode=" + tripMode
				+ "";
	}
	function printPagenew() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		window.location = "print_tripsnew.jsp?siteId=" + siteId + "&tripDate="
				+ tripDate + "&tripTime=" + tripTime + "&tripMode=" + tripMode
				+ "";
	}
	function printPageUnsaved() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		window.location = "print_trips.jsp?siteId=" + siteId + "&tripDate="
				+ tripDate + "&tripTime=" + tripTime + "&tripMode=" + tripMode
				+ "&notSaved=something";
	}
	function printBoardPage() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		window.location = "assignVendor.jsp?siteId=" + siteId + "&tripDate="
				+ tripDate + "&tripTime=" + tripTime + "&tripMode=" + tripMode
				+ "";
	}
	function comparePage() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		window.location = "compare_trips.jsp?siteId=" + siteId + "&tripDate="
				+ tripDate + "&tripTime=" + tripTime + "&tripMode=" + tripMode
				+ "";
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
	function exportPage() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		var newwindow = window.open("export2PDF?siteId=" + siteId
				+ "&tripDate=" + tripDate + "&tripTime=" + tripTime
				+ "&tripMode=" + tripMode + "", 'name', 'location=no');
		if (window.focus) {
			newwindow.focus()
		}
	}
	function showAuditLog(relatedId,moduleName){
  		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
  		var size = "height=450,width=900,top=200,left=300," + params;
  		var url="ShowAuditLog.jsp?relatedNodeId="+relatedId+"&moduleName="+moduleName;	
  	    newwindow = window.open(url, 'AuditLog', size);

  		if (window.focus) {
  			newwindow.focus();
  		}
  	}
	function alterEscort(relatedId,key)
	{
		var Status=document.getElementById("escortchbutton"+relatedId).value;
		var chstatus="NO";
		if(Status=="Add Escort")
			chstatus="YES";
			try {
				var xmlhttp;
				if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
					xmlhttp = new XMLHttpRequest();
				} else {// code for IE6, IE5
					xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
				}
				xmlhttp.onreadystatechange = function() {
					if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
						var i = xmlhttp.responseText;
						if(i>0)
							{
							alert("Escort Status Changed Succesfully!");
							document.getElementById("escortstatus"+relatedId).innerHTML=chstatus;
							if(Status=="Cancel Escort"){
								document.getElementById("escortchbutton"+relatedId).value="Add Escort";
							}else{
								document.getElementById("escortchbutton"+relatedId).value="Cancel Escort";
								document.getElementById("saveflag"+key).value="true";
							}}
						else
							{
							alert("Failed to Change Escort Status");
							}
					}
				}
				xmlhttp.open("POST", "EscortStatusUpdate?TripId="
						+ relatedId + "&Status=" + Status, true);
				xmlhttp.send();
			} catch (e) {

				alert(e);
			}
	}
	function modify(){
		 document.getElementById("tripmodifyform1").submit();
	}
</script>
</head>
<body>
	<%
		String tripDate = OtherFunctions.changeDateFromatToIso(request
				.getParameter("tripDate"));
		String siteId = request.getParameter("siteId");
		String tripMode = "" + request.getParameter("tripMode");
		String tripTime = "" + request.getParameter("tripTime");
		int j=1;
		// System.out.println("tripDate" + tripDate + "siteId" + siteId + "tripMode" + tripMode);
		//ArrayList<TripDetailsDto> tripSheetListActual = new TripDetailsDao().getTripSheetActual(tripDate, tripMode, siteId, tripTime);
		ArrayList<TripDetailsDto> tripSheetList = new TripDetailsService().getTripSheetModified(tripDate, tripMode, siteId, tripTime);
		ArrayList<TripDetailsDto> tripSheetSaved = new TripDetailsService().getTripSheetSaved(tripDate, tripMode, siteId, tripTime);
	%>
	<%
		long empid = 0;
		boolean flag = false;
		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>

	<div id="body">
		<div class="content">
			<hr />
			<input type="hidden" id="stotaltrip" value=<%=tripSheetList.size() %> />
			<input type="hidden" name="tripDate" id="tripDate"
				value="<%=tripDate%>" /> <input type="hidden" name="siteId"
				id="siteId" value="<%=siteId%>" /> <input type="hidden"
				name="tripMode" id="tripMode" value="<%=tripMode%>" /> <input
				type="hidden" name="tripTime" id="tripTime" value="<%=tripTime%>" />
			<%
				if (tripSheetList != null && tripSheetList.size() > 0) {
					flag = true;
			%>
			<br /> <input style="margin-left: 63%" class="formbutton"
				type="button" value="Back" onclick="javascript:history.go(-1);" />

			<form action="trip_details_modify.jsp" method="POST">
				<h3>Trip Sheet</h3>
				<hr />
				<p id="top"></p>
				<a href="#top" style="position: fixed;">Top</a> &nbsp;
				<input type="hidden" name="tripDate" value="<%=tripDate%>" /> <input
					type="hidden" name="siteId" value="<%=siteId%>" /> <input
					type="hidden" name="tripMode" value="<%=tripMode%>" /> <input
					type="hidden" name="tripTime" value="<%=tripTime%>" />
				<div>	<input type="button" class="formbutton" style="float:left"
					value="Export As Excel" onclick="printPageUnsaved()" />  
				<%
					if (!(tripTime.equalsIgnoreCase("all"))) {
				%>
				<p>
					<input type="submit" class="formbutton" style="float:left" value="Modify" /> <input
						type="button" class="formbutton" value="Save" onclick="checkTime()"  style="float:left" />
						<input
						type="button" class="formbutton" value="Save Selected" onclick="saveSelected()" />
						
				</p>
				

				<%
					}
				%>
				</div>
				<table border="1" width="80%">
					<%
					
						for (TripDetailsDto tripDetailsDtoObj : tripSheetList) {
							String escbuttoncvalue="Add Escort";
							if(tripDetailsDtoObj.getIsSecurity().equalsIgnoreCase("Yes"))
								escbuttoncvalue="Cancel Escort";
							String savestatus="true";
					%>

					<tr class="hd" >
					</tr>
					<tr class="hd">
						<td> &nbsp </td>
						<td>Vehicle type</td>
						<td colspan="3"><%=tripDetailsDtoObj.getVehicle_type()%></td>
						<td colspan="3">Trip ID</td>
						<td>
						<input type="checkbox" name="tripidcheck"
							id="tripidcheck" value="<%=tripDetailsDtoObj.getId()%>"
							 />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a href="showTripRoute.jsp?tripId=<%=tripDetailsDtoObj.getId()%>" target="_blank"><%=tripDetailsDtoObj.getTrip_code()%></a>
						</td>
					</tr>

					<tr class="hd">
						<td>&nbsp</td>
						<td>Vehicle #</td>
						<td colspan="3">&nbsp;</td>
						<td colspan="3">Date</td>
						<td><%=OtherFunctions
							.changeDateFromatToddmmyyyy(tripDetailsDtoObj
									.getTrip_date())%></td>
					</tr>
					<tr class="hd">
						<td>&nbsp</td>
						<td>Escort Trip</td>
						<td colspan="3" id="escortstatus<%=tripDetailsDtoObj.getId() %>"><%=tripDetailsDtoObj.getIsSecurity()%></td>
						<td colspan="3">IN/OUT Time</td>
						<td><%=tripDetailsDtoObj.getTrip_log() + "  "
							+ tripDetailsDtoObj.getTrip_time()%></td>
					</tr>
					<tr class="hd">
						<td>&nbsp</td>
						<td>Travel Time</td>
						<td colspan="3"><%=tripDetailsDtoObj.getTravelTime()%></td>
						<td colspan="3">TotalDistance</td>
						<td><%=tripDetailsDtoObj.getDistance()%></td>
					</tr>
					<tr class="hd">
						<td>&nbsp</td>
						<td>Escort Clock</td>
						<td colspan="3">&nbsp;</td>
						<td colspan="3" >Actual IN/OUT time</td>
						<td>&nbsp;</td>
					</tr>
					<tr class="hd" >
						<td>#</td>
						<td>Name</td>
						<td>Contact</td>
						<td>Gender</td>
						<td>Area</td>
						<td>Place</td>
						<td>landmark</td>
						<td>Dist</td>
						<td>Time</td>
					</tr>
					<%
						int i = 1;
								for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
										.getTripDetailsChildDtoList()) {
									if(i==1&&tripDetailsDtoObj.getIsSecurity().equalsIgnoreCase("NO")&&tripDetailsChildDto.getGender().equalsIgnoreCase("F")&&tripDetailsDtoObj.getTrip_log().equalsIgnoreCase("IN")){
										savestatus="false";
									}
									if(i==tripDetailsDtoObj.getTripDetailsChildDtoList().size()&&tripDetailsDtoObj.getIsSecurity().equalsIgnoreCase("NO")&&tripDetailsChildDto.getGender().equalsIgnoreCase("F")&&tripDetailsDtoObj.getTrip_log().equalsIgnoreCase("OUT")){
										savestatus="false";
									}

					%>
					<tr>
						<td><%=i%></td>
						<td><%=tripDetailsChildDto.getEmployeeName()%>						
						<%if(tripDetailsChildDto.getTransportType()!=null&&tripDetailsChildDto.getTransportType().equalsIgnoreCase("adhoc"))
						{%>
						--A
						<%}%></td>
						<td><%=tripDetailsChildDto.getContactNumber() %></td>
						<td><%=tripDetailsChildDto.getGender()%></td>
						
						<td><%=tripDetailsChildDto.getArea()%></td>
						<td><%=tripDetailsChildDto.getPlace()%></td>
						<td><%=tripDetailsChildDto.getLandmark()%></td>
						<td><%=tripDetailsChildDto.getDistance()%></td>
						<td><%=tripDetailsChildDto.getTime()%></td>				
					</tr>
					<%
						i++;
								}
					%>
					<tr>

						<td colspan= 9 align="right">
						
						<input type="button" id="escortchbutton<%=tripDetailsDtoObj.getId() %>" class="formbutton" value="<%=escbuttoncvalue %>" onclick="alterEscort(<%=tripDetailsDtoObj.getId()%>,<%=j %>);" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="hidden"  value="<%=savestatus %>" name="savestatus<%=tripDetailsDtoObj.getId() %>" id="saveflag<%=j %>" />
						<% j++;
						%>
						<input type="button"
							value="Audit Log" class="formbutton"
							onclick="showAuditLog(<%=tripDetailsDtoObj.getId()%>,'<%=AuditLogConstants.TRIPSHEET_MODULE%>');" /></td>

					
					</tr>
					<%
						}
					%>

					</tbody>
				</table>
				<%
					if (!(tripTime.equalsIgnoreCase("all"))) {
				%>
				<p>
					<input type="submit" class="formbutton" value="Modify" /> <input
						type="button" class="formbutton" value="Save" onclick="checkTime()" />
						
				</p>
				<%
					}
				%>
			</form>
			<%
				}
			%>
			<%
				if (tripSheetSaved != null && tripSheetSaved.size() > 0) {
					flag = true;
			%>
			<hr />
			<h3>Trip Sheet</h3>
			<br /> <input style="margin-left: 63%" class="formbutton"
				type="button" value="Back" onclick="javascript:history.go(-1);" />


			<p>
				<input type="button" class="formbutton" value="Show Compare"
					onclick="comparePage()" /> &nbsp;&nbsp; <input type="button"
					class="formbutton" value="Export As PDF" onclick="exportPage()" />
				&nbsp;&nbsp; <input type="button" class="formbutton"
					value="Export As Excel" onclick="printPage()" /> &nbsp;&nbsp; <input
					type="button" class="formbutton" value="Assign Vendor"
					onclick="printBoardPage()" />&nbsp;&nbsp;
					<input type="button" class="formbutton" value="Modify" onclick="modify();" />

			</p>

			<table border="1" width="80%">
				<%
					for (TripDetailsDto tripDetailsDtoObj : tripSheetSaved) {
				%>

				<tr>
				</tr>
				<tr class="hd">
					<td>&nbsp</td>
					<td>Vehicle</td>
					<td colspan="3"><%=tripDetailsDtoObj.getVehicle_type()%></td>
					<td colspan="3">Trip ID</td>
					<td><a href="showTripRoute.jsp?tripId=<%=tripDetailsDtoObj.getId()%>" target="_blank"><%=tripDetailsDtoObj.getTrip_code()%></a></td>
				</tr>
				<tr class="hd">
					<td>&nbsp</td>
					<td>Vehicle #</td>
					<td><%=tripDetailsDtoObj.getVehicleNo() %></td>
					<td>Driver</td>
					<td><%=tripDetailsDtoObj.getDriverName()%></td>
				
					<td colspan="3">Date</td>
					<td><%=OtherFunctions
							.changeDateFromatToddmmyyyy(tripDetailsDtoObj
									.getTrip_date())%></td>
				</tr>
				<tr class="hd">
					<td>&nbsp</td>
					<td>Escort Trip</td>
					<td ><%=tripDetailsDtoObj.getIsSecurity()%></td>
						<td>Driver Contact</td>
					<td><%=tripDetailsDtoObj.getDriverContact()%></td>
					<td colspan="3">IN/OUT Time</td>
					<td><%=tripDetailsDtoObj.getTrip_log() + "  "
							+ tripDetailsDtoObj.getTrip_time()%></td>
				</tr>
				<tr class="hd">
					<td>&nbsp</td>
					<td>Travel Time</td>
					<td colspan="3"><%=tripDetailsDtoObj.getTravelTime()%></td>
					<td colspan="3">TotalDistance</td>
					<td><%=tripDetailsDtoObj.getDistance()%></td>
				</tr>
				<tr class="hd">
					<td>&nbsp</td>
					<td>Escort Clock</td>
					<td colspan="3"><%=tripDetailsDtoObj.getEscort() %></td>
					<td colspan="3">Actual IN/OUT time</td>
					<td>&nbsp;</td>
				</tr>

				<tr class="hd">
					<td>#</td>
					<td>Name</td>
					<td>Contact</td>
					<td>Gender</td>
					
					<td>Area</td>
					<td>Place</td>
					<td>landmark</td>
					<td>Dist</td>
					<td>Time</td>
				</tr>
				<%
					int i = 1;
							for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
									.getTripDetailsChildDtoList()) {
				%>
				<tr>
					<td><%=i%></td>
					<td><%=tripDetailsChildDto.getEmployeeName()%><%if(tripDetailsChildDto.getTransportType()!=null&&tripDetailsChildDto.getTransportType().equalsIgnoreCase("adhoc")){%>--A<%}%></td>
					<td><%=tripDetailsChildDto.getContactNumber() %></td>
					<td><%=tripDetailsChildDto.getGender()%></td>
					
					<td><%=tripDetailsChildDto.getArea()%></td>
					<td><%=tripDetailsChildDto.getPlace()%></td>
					<td><%=tripDetailsChildDto.getLandmark()%></td>
					<td><%=tripDetailsChildDto.getDistance()%></td>
					<td><%=tripDetailsChildDto.getTime()%></td>
				</tr>
				<%
					i++;
							}
				%>
				<tr>
					<td height='60px' colspan="4">Security Seal</td>
					<td colspan="3" align="right">Admin Seal</td>
				</tr>
				<tr>

					<td colspan=9 align="right"><input type="button"
						value="Audit Log" class="formbutton" onclick="showAuditLog(<%=tripDetailsDtoObj.getId()%>,'<%=AuditLogConstants.TRIPSHEET_MODULE%>');" /></td>

				</tr>

				<%
					}
				%>
				</tbody>
			</table>
			<br /> <input type="button" class="formbutton" value="Show Compare"
				onclick="comparePage()" /> &nbsp;&nbsp; <input type="button"
				class="formbutton" value="Export As PDF" onclick="exportPage()" />
			&nbsp;&nbsp; <input type="button" class="formbutton"
				value="Export As Excel" onclick="printPage()" /> &nbsp;&nbsp; <input type="button" class="formbutton"
				value="Export Excel New" onclick="printPagenew()" />&nbsp;&nbsp; <input
				type="button" class="formbutton" value="Assign Vendor"
				onclick="printBoardPage()" />

			<%
				}
				if (!flag) {
			%>
			<p>No trip Is is to display</p>
			<%
				}
			%>

			<p>
				<input style="margin-left: 63%" class="formbutton" type="button"
					value="Back"
					onclick="javascript:window.location = 'view_routing.jsp';" />
			</p>
			<br /> <br />
			<hr />
			<form id="tripmodifyform1" action="trip_details_modify.jsp" method="POST">
			<input type="hidden" name="tripDate" value="<%=tripDate%>" /> <input
					type="hidden" name="siteId" value="<%=siteId%>" /> <input
					type="hidden" name="tripMode" value="<%=tripMode%>" /> <input
					type="hidden" name="tripTime" value="<%=tripTime%>" />
			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
