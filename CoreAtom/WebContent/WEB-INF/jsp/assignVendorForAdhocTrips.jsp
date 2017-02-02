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
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
		var optionSelected = trip.selectedIndex;
		var optionNew = document.createElement('option');
		optionNew.value = trip.options[optionSelected].value;
		optionNew.text = trip.options[optionSelected].innerHTML;
		try {
			selectedtrip.add(optionNew, null);
		} catch (e) {
			selectedtrip.add(optionNew);
		}
		trip.options[optionSelected] = null;
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
			<%@include file="../../Header.jsp"%>




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

			<h3>Add Route</h3>

		 
				<form:form action="addAdhocVendorTrip.do" method="POST" modelAttribute="tripVendor" name="assignvendorform" id="assignvendorform" >
				<table border="0" width="100%">
					<tbody>
						<tr>
						
							<td>
							<form:hidden path="tripDate" id="tripDate"  />
							<form:hidden path="siteId" id="siteId"  />
							<form:hidden path="tripMode" id="tripMode" />
							<form:hidden path="tripTime" id="tripTime" />
							     
							Trip Details</td>
							<td>&nbsp;</td>
							<td>Selected Trips</td>
						</tr>
						<tr>
							<td rowspan="5" width="40%">
							
						 		<form:select path="trip" id="trip" items="${vendorAssignTrip}"  itemLabel="label" itemValue="id"
								class="type" multiple="true"  ></form:select>
							 
							 </td>
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
							<td rowspan="5">
							
 
							<form:select items="${vendorAssignedTrip}" itemLabel="label" itemValue="id" multiple="true" path="selectedtrip"></form:select>
							
							</td>
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
							<td align="left">
							
								<form:select path="vendor" items="${masterVendorList}" itemValue="companyId" itemLabel="company" id="vendor"></form:select>
							 </td>
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
				</form:form>
			 
			<%@include file="../../Footer.jsp"%>
		</div>
	</div>

</body>
</html>
