<%@page import="com.agiledge.atom.dao.VehicleDao"%>
<%@page import="com.agiledge.atom.dto.VehicleDto"%>
<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="com.agiledge.atom.dao.VendorDao" %>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add vehicle</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-2.1.3.js"></script>
<script type="text/javascript" src="js/validate.js"></script>
<script type="text/javascript" src="http://code.jquery-latest.js"></script>

<script src="js/JavaScriptUtil.js"></script>
<script src="js/Parsers.js"></script>
<script src="js/InputMask.js"></script>

<link href="css/select2.css" rel="stylesheet"/>
<script src="js/select2.js"></script>
<script src="js/dataValidation1.js"></script>
<script src="js/tablefilter.js"></script>



<script type="text/javascript">

	$(document).ready(function() {
	  $(".vehicle").select2();
		$("#vehicle").change(function() {
			var vehicle = $("#vehicle").val();
			$("#vehicleDriver option").remove();
			$("#drivers option").remove();
			$.ajax({
				type : "POST",
				url : "DriverVehicle",
				data : {
					vehicle : vehicle
				}}).done(function(result) {
					var retdata= result.split("|");
					var tableData=retdata[1];
					var vehicleDriver=retdata[2];
					var drivers=retdata[3];
					$("div#datapart").html(tableData)
					$("#vehicleDriver").append(vehicleDriver);
					$("#drivers").append(drivers);
				});
			
		});
		
		 $('#remove').click(function() {  
			  $('#vehicleDriver option:selected').remove().appendTo('#drivers');  
			 });  
			 $('#add').click(function() {  
			  $('#drivers option:selected').remove().appendTo('#vehicleDriver');  
			 });
			 
			 $('#submitbtn').click(function() {
				 try{	
			 $('#vehicleDriver option').prop('selected', 'seleted');	 
			 $("#driverVehicleForm").submit();
				 }catch(e){alert(e);}
			 });
	});
	function getVehicles(Vendor)
	{
		try {
				
				var xmlhttp;
				if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
					xmlhttp = new XMLHttpRequest();
				} else {// code for IE6, IE5
					xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
				}
				xmlhttp.onreadystatechange = function() {
					if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
						var vehicles = xmlhttp.responseText;
						document.getElementById("vehicle").innerHTML = "<select name='vehicle' id='vehicle'><option value='select' selected>-select Vehicle-</option>"
							+ vehicles + "</select>";
							}
				}
				xmlhttp.open("POST", "vendorBaseVehicle?vendor="+Vendor, true);
				xmlhttp.send();
			} catch (e) {

				alert("Cant fetch Vehicles");
			}

		
	}
</script>
</head>
<body>
<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		
		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
	%><%@include file="Header.jsp"%>
	<%
		}
	%>

	<div id="body">
		<div class="content">

	<h2>Add New Vehicle</h2>
	<%
		ArrayList<VendorDto> vendorDtos = null;
	%>
	<form id="driverVehicleForm" name="AddVehicleForm" action="DriverVehicle" method="post">
		<table>
			<%
				vendorDtos= new VendorService().getMasterVendorlist();
			%>
			
			<tr>
			<td>Select Vendor
			<select name="vendor" id="vendor" onchange="getVehicles(this.value);">
			<option value="">-SELECT-</option>
				<%
					for(VendorDto vendorDto : vendorDtos){
						%>
							<option value="<%=vendorDto.getCompanyId()%>"><%=vendorDto.getCompany()%></option>
						<% 
					}
				%>
				</select>
			</td>
			
				<td>Vehicle
				<select class="vehicle" name="vehicle" id="vehicle">
						<option> SELECT VEHICLE </option>
						
						
				</select></td>
			</tr>
			<tr>
			<td><select name="drivers" id="drivers" multiple="multiple"></select></td>
			
			<td><input type="button" id="add" value=">>" class="formbutton"/></td>
			<td><input type="button" id="remove" class="formbutton" value='<<'/></td>
			<td><select name="vehicleDrivers" id="vehicleDriver" multiple="multiple"></select></td>			
			</tr>
			<tr><td></td><td></td><td></td><td><input type="hidden" name="source" value="formsubmit"/><input id="submitbtn"  type="button" name="submitbtn" value="Submit" class="formbutton"/></td></tr>
			</table>
			
			<div id="datapart"></div>
			
	</form>
	<%@include file="Footer.jsp"%>
	</div>
	</div>
</body>
</html>