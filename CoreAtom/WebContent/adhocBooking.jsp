
<%@page import="java.util.Date"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.dto.AdhocDto"%>
<%@page import="com.agiledge.atom.service.AdhocService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="java.util.ArrayList"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Adhoc Booking</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script src="js/dateValidation1.js"></script>
</head>
<body>

	<script type="text/javascript">
		var validationStatus = 0;
		$(document).ready(
				function() {
					$("#empSet").hide();
					$("#travelDate").datepick();
					$("#startTime").change(function() {
						validateTime($("#startTime").val());
					});
					$("#shiftTime").change(function() {
						validateTime($("#shiftTime").val());
					});					
					$("#bookingFor").change(function() {
						if ($("#bookingFor").val() == "other") {
							$("#empSet").show();
						}
						if ($("#bookingFor").val() == "self") {
							$("#empSet").hide();
						}
					});

					$("#pickdrop").change(
							function() {
								$.post("GetExtensionShift?pickdrop="
										+ $("#pickdrop").val(), function(data,
										status) {
									$("#shiftTime").html(data);
								});
							});
				});
		function reloadForm(adhoctype) {
			window.location.href = "adhocBooking.jsp?adhoctype=" + adhoctype;
		}

		function validateForm() {
			if ($("#bookingFor").val() == "other"&&($("#employeeID").val()==null||$("#employeeID").val()=="")) {
				alert("Choose Employee");
				return false;
			}
			if($("#adhocType").val() == "shiftExtension")
				{
				if($("#shiftTime").val()==null||$("#shiftTime").val()==""||$("#shiftTime").val()=="Select")
				{
				alert("Select Time");
				return false;
				}
			else{
				validateTime($("#shiftTime").val());
			}
				}
			else
				{
				if($("#startTime").val()==null||$("#startTime").val()=="")
				{
					alert("Select Time");
					return false;
				}
			validateTime($("#startTime").val());
				}
			
			
			if (validationStatus == 0) {
				if ($("#adhocType").val() != "shiftExtension") {
					if ($("#orgination").val() == null
							|| $("#orgination").val() == "") {
						alert("Null value");
						return false;
					} else {
						return true;
					}
				}
			}
			if (validationStatus == 1) {
				alert("Past Date chosen");
				return false;
			} if(validationStatus !=0){
				alert("Booking for That time has closed");
				return false;
			}
		}

		function validateTime(selectedTime) {
			
			validationStatus = 0;
			var currentDateparted=document.getElementById("curdate").value.split(" ");
			var currentDatevar=currentDateparted[0].split("/")[0]+"/"+currentDateparted[0].split("/")[1]+"/"+currentDateparted[0].split("/")[2];
			var dateSelectedVal = CheckDateEqualOrGreaterDatesddmmyyyy($(
					"#travelDate").val(), currentDatevar);
			
			if (dateSelectedVal == 0) {
				var pickdrop = $("#pickdrop").val();
				
				var bookingCutOff;
				if(pickdrop=="IN")
					{
				bookingCutOff = $("#pickcutoff").val();
					}
				else
					{
				bookingCutOff = $("#dropcutoff").val();
					}
				var bookingCutOffSplit = bookingCutOff.split(":");
				var currentTime = (parseInt(currentDateparted[1].split(":")[0]) + parseInt(bookingCutOffSplit[0])) * 60;
				currentTime += parseInt(currentDateparted[1].split(":")[1])
						+ parseInt(bookingCutOffSplit[1]);
				var selectedTimeMnt = parseInt(selectedTime.split(":")[0]) * 60;
				selectedTimeMnt += parseInt(selectedTime.split(":")[1]);
				if (selectedTimeMnt < currentTime) {
					validationStatus = 2;
					alert("Booking for That time has closed");

				}

			} else if (dateSelectedVal == -1) {
				validationStatus = 1;
			} else {
			}
		}

		function showPopup(url) {
			var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
			size = "height=450,width=520,top=200,left=300," + params;
			/*
			if (url == "LandMarkSearch.jsp") {
				size = "height=450,width=600,top=200,left=300," + params;
			} else if (url == "SupervisorSearch1.jsp"
					|| url == "SupervisorSearch2.jsp") {
				size = "height=450,width=700,top=200,left=300," + params;
			} else if (url == "termsAndConditions.html") {
				size = "height=450,width=520,top=200,left=300," + params;
			}
			var site = document.getElementById("site").value;
			if (url == "LandMarkSearch.jsp") {
				if (site.length < 1) {
					alert("Choose Site");
					return false;
				}
				url += "?site=" + site;
			}
			 */

			newwindow = window.open(url, 'name', size);

			if (window.focus) {
				newwindow.focus();
			}
		}
	</script>

	<%
		long empid = 0;

		String employeeId = OtherFunctions.checkUser(session);

		empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<%
		OtherDao ob = null;
		ob = OtherDao.getInstance();
	%>

	<%
		try {
			AdhocService service = new AdhocService();
			EmployeeDto empDto = (EmployeeDto) session
					.getAttribute("userDto");
			String roleId=session.getAttribute("roleId").toString();
			String pickDrop = request.getParameter("pickdrop");
			ArrayList<AdhocDto> adhoctypes = service.getAdhocTypes(empDto.getSite());
			String selectedAdhocType = ""
					+ request.getParameter("adhoctype");
			System.out.println(selectedAdhocType + "selected adhoc");
			String bookingFor = "" + request.getParameter("bookingFor");
			AdhocDto dto = new AdhocDto();
			ArrayList<AdhocDto> adhocdetails = new ArrayList<AdhocDto>();

			String rightToBook = service.isAdhocBookingright(employeeId,
					selectedAdhocType, bookingFor, empDto.getSite(),
					empDto.getProjectUnit(),roleId);
	%>
	<br />
	<h3 style="margin-left: 100px">Adhoc</h3>
	<form method="POST" action="AdhocBooking" id="adhocbbokingform"
		onsubmit="return validateForm()">
		<table>
			<tr>
				<td width="20%">Select Adhoc Type</td>
				<td><select name="adhocType" id="adhocType"
					onchange="reloadForm(this.value)">
						<option value="">Select</option>
						<%
						  String Id="";
							for (AdhocDto adhocType : adhoctypes) {
								if (Id.equalsIgnoreCase(adhocType.getAdhocType()))
								{
									continue;
								}  
									if (selectedAdhocType.equals(adhocType.getAdhocType())) {
						%>
						<option selected="selected" value="<%=adhocType.getAdhocType()%>"><%=adhocType.getAdhocTypeString()%></option>
						<%
							} else {
						%>
						<option value="<%=adhocType.getAdhocType()%>">
							<%=adhocType.getAdhocTypeString()%></option>
						<%
							} 
									Id = adhocType.getAdhocType();
									
								}
						%>
				</select></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</table>
		<%
		 
			if (!(selectedAdhocType.equals("") || selectedAdhocType
						.equals("null"))) {

					if (!(rightToBook.equalsIgnoreCase("self") || rightToBook
							.equals("all"))) {
		%>
		<p>You do not have the right to book this Adhoc</p>
		<%
			} else {
				adhocdetails = service.getSetupDetails(selectedAdhocType,
								empDto.getSite(), empDto.getProjectUnit());
		%>
		<table>

			<tr>
				<td width="20%">Booking For</td>

				<td><select name="bookingFor" id="bookingFor">
						<%
							if (rightToBook.equalsIgnoreCase("all")) {
						%>
						<option value="self">Self</option>
						<option value="other">For Other</option>
						<%
							}
						%>
						<%
							if (rightToBook.equalsIgnoreCase("self")) {
						%>
						<option value="self">Self</option>
						<%
							}
						%>
				</select></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr id="empSet">

				<td>Employee</td>

				<td><input type="hidden" name="employeeID" id="employeeID" />
					<input type="text" readonly name="employeeName" id="employeeName"
					onclick="showPopup('EmployeeSearchUnderManager.jsp')" /> <label
					for="employeeID" class="requiredLabel">*</label> <input
					class="formbutton" type="button" value="..."
					onclick="showPopup('EmployeeSearchUnderManager.jsp')" /></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td width="20%">Date</td>
				<td><input type="text" name="travelDate" id="travelDate" /> 
				
				<%-- <input				type="hidden" id="bookCutoff" value="<%=dto.getRequestCutoff()%>"> --%>
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<%
				if (!selectedAdhocType
									.equalsIgnoreCase(SettingsConstant.SHIFT_EXTENSTION)) {
							
					response.sendRedirect("adhocBooking2.jsp?adhoctype=" + selectedAdhocType);					
				} else {
			%>
			<tr>
				<td width="20%">Pick Up/Drop
				
						<% for(AdhocDto adto : adhocdetails){
							if(adto.getAdhocType().equalsIgnoreCase(SettingsConstant.SHIFT_EXTENSTION) && adto.getPickupDrop().equalsIgnoreCase("pick up")){
								
							%><input type="hidden" id="pickcutoff" value="<%=adto.getRequestCutoff()%>"/><% 	
								
							}
							
							else if(adto.getAdhocType().equalsIgnoreCase(SettingsConstant.SHIFT_EXTENSTION) && adto.getPickupDrop().equalsIgnoreCase("drop")){
							%><input type="hidden" id="dropcutoff" value="<%=adto.getRequestCutoff()%>"/><% 
							
							}}%>
							</td>
				<td><select name="pickdrop" id="pickdrop">
						<option>Select</option>		
						<option value="IN">pickUp</option> 
						  <option value="OUT">Drop</option>
				</select>
			
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td width="20%">Time</td>
				<td><select name="shiftTime" id="shiftTime">
				</select>
				<td></td>
				<td></td>
			</tr>
			<%
				}
			%>
			<tr>
				<td>
				<input type="hidden" id="curdate"
					value="<%=OtherFunctions.getTodaysDate()%>" />
				<input type="hidden" name="site"
					value="<%=empDto.getSite()%>" /> <input type="hidden"
					name="projectUnit" value="<%=empDto.getProjectUnit()%>" /></td>
				<td><input type="submit" value="Book" class="formbutton" /></td>
			</tr>				
		</table>
		<%
			}
				}
			} catch (Exception e) {
				System.out.println("eroorr" + e);
			}
		%>
	</form>
	<%@include file="Footer.jsp"%>
</body>
</html>