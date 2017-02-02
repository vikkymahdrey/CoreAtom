
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
<style>
	#fillMeAnchor {
	
		font-size: x-small;
		font-style: italic;
	}
	
	#selectMate {
		text-align: left;
	}
	
	#employeeID{
		text-align: center;
	}
	
	#passengerTable {
		text-align: left;
		margin-left: 20%;
		width: 60%;
	}
	
</style>

<script src="js/dateValidation.js"></script>
</head>
<body>

	<script type="text/javascript">
		var validationStatus = 0;
		$(document).ready(
				function() {
					
					$("#fillMeAnchor").click(fillMeFn);
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
					
					$(".moreImg").click(showOneMoreRow);
				});
		
		function showOneMoreRow() {
			try {
					var newRow = $(".moreImg").last().parent().parent().clone();
					$(newRow).children().first().children("#fillMeAnchor").remove();
					$(this).html($(this).parent().html() + " " );
				 	if($(newRow).children().last().children(".imgRemoveRow").length==0) {
						
					 
						$(newRow).children().last().append(" <img class='imgRemoveRow' alt='-' src=''/>");
						$(newRow).children().last().children(".imgRemoveRow").click(removeRow);
						
					} else {
						
						try {
						$(this).next().remove();
						$(newRow).children().last().children(".imgRemoveRow").click(removeRow);
						}catch(e) {
							alert("error 2" + e);
						}
						
					}
					removeFieldsReadOnly.call($(newRow).children().last().children(".imgRemoveRow"));
					clearRow.call($(newRow).children().last().children(".imgRemoveRow"));
					$(this).parent().parent().parent().append(newRow);
					$(".moreImg").click(showOneMoreRow);
					
					$(this).remove();
					
					 
			
			}catch(e) {
				alert(e);
			}
		}
		
		function removeRow() {
			try {
				var newRow = this;
				$($(newRow).parent().parent().prev()).children().last().html($(this).parent().html());
				$(".imgRemoveRow").click(removeRow);
				$(".moreImg").click(showOneMoreRow);
				$(this).parent().parent("tr").remove(); 
			}catch(e) {
				alert(e);
			}
		}
		function onFillMeClick() {
		}
		
		function reloadForm(adhoctype) {
			window.location.href = "adhocBooking2.jsp?adhoctype=" + adhoctype;
			if ($("#adhocType" ).val().toUpperCase()=="SHIFTEXTENSION")
			{
				window.location.href="adhocBooking.jsp?adhoctype=shiftExtension";
			}
		}

		function validateForm() {
			if ($("#bookingFor").val() == "other"&&($("#employeeID").val()==null||$("#employeeID").val()=="")) {
				alert("Please Choose Employee!");
				return false;
			}

			if($("#adhocType").val() == "shiftExtension")
				{
				if($("#shiftTime").val()==null||$("#shiftTime").val()==""||$("#shiftTime").val()=="Select")
				{
				alert("Please Select Time!");
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
					alert("Please Select Time!");
					return false;
				}
			validateTime($("#startTime").val());
				}
			
			
			if (validationStatus == 0) {
				if ($("#adhocType").val() != "shiftExtension") {
					if ($("#orgination").val() == null
							|| $("#orgination").val() == "") {
						alert("Please Specify Origin!");
						return false;
					}
					if ($("#destination").val() == null
							|| $("#destination").val() == "") {
						alert("Please Specify Destination!");
						return false;
					}
					if ($("#reason").val() == null
							|| $("#reason").val() == "") {
						alert("Please Specify Reason!");
						return false;
					}
					else {
						return true;
					}
				}
			}
			if (validationStatus == 1) {
				alert("Past Date chosen");
				return false;
			} if(validationStatus !=0){
				alert("Invalid Date/Time Chosen!");
				return false;
			}
		}

		function validateTime(selectedTime) {
			
			validationStatus = 0;
			var currentDate = new Date();
			var currentDatevar = currentDate.getDate() + "/"
					+ currentDate.getMonth() + "/" + currentDate.getFullYear();
			var dateSelectedVal = CheckDateEqualOrGreaterDatesddmmyyyy($(
					"#travelDate").val(), currentDatevar);
			if (dateSelectedVal == 0) {
				var bookingCutOff = $("#bookCutoff").val();
				var bookingCutOffSplit = bookingCutOff.split(":");
				var currentTime = (parseInt(currentDate.getHours()) + parseInt(bookingCutOffSplit[0])) * 60;
				currentTime += parseInt(currentDate.getMinutes())
						+ parseInt(bookingCutOffSplit[1]);
				var selectedTimeMnt = parseInt(selectedTime.split(":")[0]) * 60;
				selectedTimeMnt += parseInt(selectedTime.split(":")[1]);
				if (selectedTimeMnt < currentTime) {
					validationStatus = 2;
					alert("Please Choose Correct Date!");

				}

			} else if (dateSelectedVal == -1) {
				validationStatus = 1;
			} else {
			}
		}

		function showPopup(url) {
			var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
			size = "height=450,width=520,top=200,left=300," + params;
			newwindow = window.open(url, 'name', size);

			if (window.focus) {
				newwindow.focus();
			}
		}
		
		function fillMeFn() {
			 
			try { 
				
				if(  $("#fillMeAnchor").text().trim()!="Clear") {
					
					//alert($(this).parent().parent().find(".employeeName").val());
					  $(this).parent().parent().find(".employeeID").val( $("#myEmployeeId").val( ));
					  $(this).parent().parent().find(".bookingFor").val( $("#myEmployeeId").val( ));
					  $(this).parent().parent().find(".employeeName").val( $("#myDisplayName").val());
					  $(this).parent().parent().find(".passengerType").val("local");
					  $(this).parent().parent().find(".passengerContactNo").val( $("#myContactNumber").val() );
					  $(this).parent().parent().find(".passengerEmailId").val( $("#myEmailId").val() );
					  if( $("#myGender").val()=="M"||$("#myGender").val()=="m")
						  {
					  $(this).parent().parent().find(".passengerGender").val("m");
						  }
					  else{
						  $(this).parent().parent().find(".passengerGender").val("f");
					  }
					  $("#fillMeAnchor").text("Clear");
					  //this.setFieldsReadOnly();
					  setFieldsReadOnly.call(this);
					  
				} else {
					clearRow.call(this);
				}
			}catch (e) {
				// TODO: handle exception
				alert(e);
			} 
			
		  
			
		}
		

		function setFieldsReadOnly() {
			 
			try { 
				 
			//	alert($(this).parent().parent().find(".employeeName").val());
				 
				   
				  $(this).parent().parent().find(".employeeName").attr("readonly", "readonly");
				  
				  $(this).parent().parent().find(".passengerContactNo").attr("readonly", "readonly");
				  $(this).parent().parent().find(".passengerEmailId").attr("readonly", "readonly");
				  $(this).parent().parent().find(".passengerGender").attr("readonly", "readonly");
				   
			}catch (e) {
				// TODO: handle exception
				alert(e);
			} 
			
		  
			
		}

		function removeFieldsReadOnly() {
			 
			try { 
				//alert($(this).parent().parent().find(".employeeName").val());
				 
				   
				  $(this).parent().parent().find(".employeeName").removeAttr("readonly" );
				  $(this).parent().parent().find(".passengerContactNo").removeAttr("readonly" );
				  $(this).parent().parent().find(".passengerEmailId").removeAttr("readonly" );
				  $(this).parent().parent().find(".passengerGender").removeAttr("readonly" );
				   
			}catch (e) {
				// TODO: handle exception
				alert(e);
			} 
			
		  
			
		}


		
		function clearRow() {
			try { 
				//alert($(this).parent().parent().find(".employeeName").val());
				$(this).parent().parent().find(".employeeID").val("");
				  $(this).parent().parent().find(".bookingFor").val( "");
				  $(this).parent().parent().find(".employeeName").val("");
				  $(this).parent().parent().find(".passengerType").val("");
				  $(this).parent().parent().find(".passengerContactNo").val( "" );
				  $(this).parent().parent().find(".passengerEmailId").val( "" );
				  $(this).parent().parent().find(".passengerGender").val( "");
				  $("#fillMeAnchor").text("Fill Me");
				  removeFieldsReadOnly.call(this);
			}catch (e) {
				// TODO: handle exception
				alert(e);
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
	<h3 style="margin-left: 100px">Adhoc Booking</h3>
	<form method="POST" action="AdhocBooking2" id="adhocbbokingform"
		onsubmit="return validateForm()">
		<table>
			<tr>
				<td width="20%">Select Adhoc Type</td>
				<td>
				
					<select name="adhocType" id="adhocType"
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
		<input type="hidden" name="myEmployeeId" id="myEmployeeId"  value="<%=empDto.getEmployeeID() %>"  />
		<input type="hidden" name="myDisplayName" id="myDisplayName"  value="<%=empDto.getDisplayName() %>" />
		<input type="hidden" name="myContactNumber"  id="myContactNumber"  value="<%=empDto.getContactNo() %>" />
		<input type="hidden" name="myEmailId" id="myEmailId"  value="<%=empDto.getEmailAddress() %>" />
		<input type="hidden" name="myGender" id="myGender"  value="<%=empDto.getGender() %>" />
		
		<table>

			<tr>
				<td colspan="4">
						<table id="passengerTable"  >
							<thead>
								<tr>
									<th colspan ="4" >
										Passengers Details
									</th>
								</tr>
								<tr>
									<th>
										Name
									</th>
									<th>
										Phone No
									</th>
									<th>
										Email Id
									</th>
									<th>
										Gender
									</th>
									 
									
								</tr>
							</thead>
							<tbody>
								<tr>
								 
									 
	
					<td>
					
					 
						 
						  
						<input type="hidden" class="index" value="0" />
						
						<input type="hidden" class="bookingFor" name="bookingFor" id="bookingFor_0" />
						
						<input type="text" name="employeeName" class="employeeName" id="employeeName_0" />
						 
						<input type="hidden" name="passengerType" class="passengerType"  id="passengerType_0"  />
						<input type="hidden" id="employeeID_0" class="employeeID"  name="employeeID" />
						 
						<img  title="Choose Team Mate" src="images/selectEmployee.png" id="selectMate" 
						 onclick="showPopup('EmployeeSearchUnderManager.jsp')"  />
						<a href="#" id="fillMeAnchor" >fill me</a>  
						
	 				</td>
	 				<td>
	 					<input type="text" name="passengerContactNo" class="passengerContactNo"  id="passengerContactNo_0" />
	 					
	 				</td>
					<td>
						<input type="text" name="passengerEmailId" class="passengerEmailId" id="passengerEmailId_0" />
					</td>
					<td>
						<select id="passengerGender_0" name="passengerGender" class="passengerGender" >
							<option value="m" >MALE</option>
							<option value="f" >FEMALE</option>
						</select>
					</td>
					
					<td>
						<img class="moreImg" alt="+" src="">
					</td>
					 
					  
					 
				</tr>
				</tbody>
				
				</table>
				
			</td>
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
			%>
			<tr>
				<td width="20%">Start Time</td>
				<td><select name="startTime" id="startTime">
						<%=OtherFunctions
									.FullTimeInIntervalOptions()%>
				</select></td>
				<%if(selectedAdhocType.equalsIgnoreCase(SettingsConstant.AT_DISPOSAL) ||
						selectedAdhocType.equalsIgnoreCase(SettingsConstant.intervalOrPeriod) 
						){
					//response.sendRedirect("adhocBooking.jsp?adhoctype=" + selectedAdhocType);
				%>
				<td>End Time</td>
				<td><select name="endTime" id="endTime">
						<%=OtherFunctions
									.FullTimeInIntervalOptions()%>
				</select></td>
				<%}else{ 
				
					
				%>
					<td></td>
				<td><input type="text" name="endTime" value=""></td>
				
				<%} %>
			</tr>
			<tr>
				<td width="20%">Orgination</td>
				<td><input type="text" name="orgination" id="orgination" /></td>
				<td>Destination</td>
				<td><input type="text" name="destination" id="destination" /></td>
			</tr>
			
			<tr>
				<td>Reason</td>
				<td><input type="text" name="reason" id="reason" /></td>
				<td>Comment</td>
				<td><textarea name="comment" id="comment"></textarea></td>
			</tr>
			
			<%
				} else {
			%>
			
			<tr>
				<td width="20%">Pick Up/Drop</td>
				<td><select name="pickdrop" id="pickdrop">
						<option>Select</option>
						<% for(AdhocDto adto : adhocdetails){
							if(adto.getAdhocType().equalsIgnoreCase(SettingsConstant.SHIFT_EXTENSTION) && adto.getPickupDrop().equalsIgnoreCase("pick up")&& !(adto.getRequestCutoff().equalsIgnoreCase("")) && !(adto.getCancelCutoff().equalsIgnoreCase(""))){
							System.out.println(adto.getRequestCutoff() + adto.getCancelCutoff() + " Check this");	
							%><option value="IN">pickUp</option> <% 	
								
							}
							
							else if(adto.getAdhocType().equalsIgnoreCase(SettingsConstant.SHIFT_EXTENSTION) && adto.getPickupDrop().equalsIgnoreCase("drop")&& !(adto.getRequestCutoff().equalsIgnoreCase("")) && !(adto.getCancelCutoff().equalsIgnoreCase(""))){
							%> <option value="OUT">Drop</option>  <% 
							
							}}%>
						
						<!-- <option value="IN">pickUp</option> 
						  <option value="OUT">Drop</option>  -->
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
				<td><input type="hidden" name="site"
					value="<%=empDto.getSite()%>" /> <input type="hidden"
					name="projectUnit" value="<%=empDto.getProjectUnit()%>" /></td>
				<td><input type="submit" value="Book" class="formbutton" /></td>
			</tr>
				<%if(adhocdetails != null)
				{ 
					String flag ="";
					if(pickDrop!=null && pickDrop.equalsIgnoreCase("OUT"))
					{
						flag = "drop";
					}
					else
					{
						flag = "pick up";
					}
				for(AdhocDto adhocdto : adhocdetails){
					if (adhocdto.getAdhocType().equalsIgnoreCase(SettingsConstant.SHIFT_EXTENSTION)&& flag.equalsIgnoreCase(adhocdto.getPickupDrop())){%>
					<tr><td><input				type="hidden" id="bookCutoff" value="<%=adhocdto.getRequestCutoff()%>">
				<% }
					else{%>
					<input type="hidden" id="bookCutoff" value="<%=adhocdto.getRequestCutoff()%>"></td></tr>
					<% }
					}}
				else{
					System.out.println("error it is null");}%>
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