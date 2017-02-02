<%-- 
    Document   : modify_trip
    Created on : Oct 31, 2012, 6:58:21 PM
    Author     : muhammad
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="com.agiledge.atom.service.TripDetailsService"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="com.agiledge.atom.dao.TripDetailsDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Assign Vendor</title>

<script type="text/javascript">
	function submitForm(form) {
		try {
			var selectedtrip = document.getElementById("selectedtrip");
			var selectedtripLength = document.getElementById("selectedtrip").options.length;
			
				for ( var i = 0; i < selectedtripLength; i++) {
				
					selectedtrip.options[i].selected = true;
				}
				form.submit();			
		} catch (e) {
			aert(e);
		}
	}

	function listMoveRight() {
		var trip = document.getElementById("trip");
		var selectedtrip = document.getElementById("selectedtrip");
		var totLen=trip.options.length;
		for(var count=0; count < totLen; count++) {
			
			if(trip.options[count].selected == true) {
		var optionNew = document.createElement('option');
		optionNew.value = trip.options[count].value;
		optionNew.text = trip.options[count].innerHTML;
		try {
			selectedtrip.add(optionNew, null);
		} catch (e) {
			selectedtrip.add(optionNew);
		}
		trip.options[count] = null;
		count--;
			}
		}
	}
	function listMoveLeft() {
		var selectedtrip = document.getElementById("selectedtrip");
		var trip = document.getElementById("trip");
		var optionSelected = selectedtrip.selectedIndex;
		var optionNew = document.createElement('option');
		optionNew.value = selectedtrip.options[optionSelected].value;
		optionNew.text = selectedtrip.options[optionSelected].innerHTML;
		try {
			trip.add(optionNew, null);
		} catch (e) {
			trip.add(optionNew);
		}
		selectedtrip.options[optionSelected] = null;
	}
</script>
</head>
<body>
	<div id="body">
		<div class="content">
			<%
				long empid = 0;
				String employeeId = OtherFunctions.checkUser(session);

				empid = Long.parseLong(employeeId);
			%>
			<%@include file="Header.jsp"%>




			<%
				String tripDate = OtherFunctions.changeDateFromatToIso(request
						.getParameter("tripDate"));
				String siteId = request.getParameter("siteId");
				String tripMode = "" + request.getParameter("tripMode");
				String tripTime = "" + request.getParameter("tripTime");
				ArrayList<TripDetailsDto> vendorAssignTrip = new TripDetailsService()
						.getTripToAssignVendor(tripDate, tripMode, siteId, tripTime);
				ArrayList<VendorDto> masterVendorList = new VendorService()
						.getMasterVendorlist();
			%>

			<h3>Assign Vendor</h3>

			<form action="AssignVendor" name="assignvendorform" id="assignvendorform"
				method="POST">
				<table border="0" width="100%">
					<tbody>
						<tr>
						
							<td><input type="hidden" name="tripDate" id="tripDate"
				value="<%=tripDate%>" /> <input type="hidden" name="siteId"
				id="siteId" value="<%=siteId%>" /> <input type="hidden"
				name="tripMode" id="tripMode" value="<%=tripMode%>" /> <input
				type="hidden" name="tripTime" id="tripTime" value="<%=tripTime%>" />
							Trip Details</td>
							<td>&nbsp;</td>
							<td>Selected Trips</td>
						</tr>
						<tr>
							<td rowspan="5" width="40%"><select name="trip" id="trip"
								multiple="multiple">
									<%
										for (TripDetailsDto detailsDto : vendorAssignTrip) {
									%>
									<option value="<%=detailsDto.getId()%>">
									<%=detailsDto.getTrip_code()+":"+detailsDto.getTripDetailsChildDtoObj().getArea()+"-"+detailsDto.getTripDetailsChildDtoObj().getPlace()+"-"+detailsDto.getTripDetailsChildDtoObj().getLandmark()%>								
									</option>
									<%
										}
									%>
							</select></td>
							<td width="10%" rowspan="5">
								<p>
									<input type="button" class="formbutton" name="right"
										value="&rArr;" onclick="listMoveRight()" />
								</p>
								<p>
									<input type="button" class="formbutton" name="left"
										value="&lArr;" onclick="listMoveLeft()" />
								</p>
							</td>
							<td rowspan="5"><select name="selectedtrip" id="selectedtrip"
								multiple="multiple">

							</select></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>						
						<tr>
							<td align="center">Choose Vendor</td>
							<td align="left"><select id="vendor" name="vendor">
									<%
										for (VendorDto vendorDto : masterVendorList) {
									%>

									<option value="<%=vendorDto.getCompanyId()%>"><%=vendorDto.getCompany()%></option>
									<%
										}
									%>
							</select></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td colspan="2"><input type="button" id="submitbtn"
								name="submitbtn" class="formbutton" value="Submit"
								onclick="submitForm(this.form)"></td>
							<td>&nbsp;</td>
						</tr>
					</tbody>
				</table>
			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
