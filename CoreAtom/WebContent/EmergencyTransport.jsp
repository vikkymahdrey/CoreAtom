<%@page import="java.util.Date"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="org.displaytag.decorator.MultilevelTotalTableDecorator"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.servlets.EmergencyDao"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="java.lang.Exception"%>
<%@page import="com.agiledge.atom.dao.APLDao"%>
<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Emergency Transportation services</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script src="js/dateValidation.js"></script>
<script type="text/javascript">
	function showPopup3(url) {
		try {

			var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
			var size = "height=124,width=300,top=200,left=300," + params;

			var site = document.getElementById("siteId").value;

			if (url == "LandMarkSearchInEmergency.jsp") {

				size = "height=450,width=600,top=200,left=300," + params;
				if (site.length < 1) {
					alert("Choose Site");
					return false;
				}
				url += "?siteId=" + site;
			}
			newwindow = window.open(url, 'name', size);

			if (window.focus) {
				newwindow.focus();
			}
		} catch (e) {
			alert(e);
		}
	}
</script>
<script type="text/javascript">
	var validationStatus = 0;
	$(document).ready(function() {
		$("#travelDate").datepick();
		$("#travelDate1").datepick();
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

	});

	function validate() {

		var siteId = document.getElementById("siteId").value;
		var type = document.getElementById("chosenVehicleType").value;
		var vehicle = document.getElementById("selectVehicle").value;
		var bookingFor = document.getElementById("employeeName").value;
		var bookingBy = document.getElementById("empName").value;
		var travelDate = document.getElementById("travelDate").value;
		var travelDate1 = document.getElementById("travelDate1").value;
		var startTime = document.getElementById("startTime").value;
		var area = document.getElementById("area").value;
		var place = document.getElementById("place").value;
		var landmark = document.getElementById("landmark").value;
		var reason = document.getElementById("reason").value;
		var currentDate = new Date();
		var currentDatevar = currentDate.getDate() + "/"
				+ currentDate.getMonth() + "/" + currentDate.getFullYear();
		var daybeforeDatevar = currentDate.getDate()-1 + "/"
		+ currentDate.getMonth() + "/" + currentDate.getFullYear();
		var monthP =currentDate.getMonth() +1;
		var dateT = currentDate.getDate()+1
		if(dateT < 10){
			dateT = "0"+dateT;
		}
		if(monthP < 10){
			monthP = "0"+monthP;
		}
		var nxtDatevar = dateT+ "/"
		+ monthP+ "/" + currentDate.getFullYear();
		
		
		var currentTime = currentDate.getHours()*60+currentDate.getMinutes();
		
		var cutoffTime = currentTime+30;
		var startTimesplit = startTime.split(":");
		var startTimeMIn = parseInt(startTimesplit[0]*60 )+parseInt(startTimesplit[1]);
		if (siteId == 0) {
			alert("Choose site");
			return false;
		} else if (bookingFor == null || bookingFor == "") {
			alert("Choose BookingFor");
			return false;
		} else if (bookingBy == null || bookingBy == "") {
			alert("Choose BookingBy");
			return false;
		} else if (process == null || process == "") {
			alert("Choose starttime");
			//  date.focus();
			return false;

		} else if (travelDate.length < 1) {
			alert("Choose From Date");
			//  date.focus();
			return false;

		} else if (travelDate1.length < 1) {
			alert("Choose To Date");
			//  date.focus();
			return false;

		
		} else if (travelDate1<travelDate) {
			alert("FromDate should be less than ToDate");
			//  date.focus();
			return false;

		} else if (startTime == null || startTime == "") {
			alert("Choose starttime");
			//  date.focus();
			return false;
		}else if(travelDate < nxtDatevar){
			if( startTimeMIn < cutoffTime  ) {
			alert("!booking request is available only before 30 min...");
			//  date.focus();
			return false;}
		}  else if(type=="ALL" || type==null || type=="")
	    {
	        alert("Choose VehicleType ");
	         return false;
	    }
	    else if(vehicle=="ALL" || vehicle=="" || vehicle==null)
	    {
	        alert("Choose Vehicle");
	                return false;
	    }
	    
	    else if(area==null || area=="" || area== "null")
	    {
	        alert("Choose area");
	      //  date.focus();
	        return false;
	            
	    }
	    else if(place==null || place=="" || place== "null")
	    {
	        alert("Choose place");
	      //  date.focus();
	        return false;
	            
	    }
	    else if(landmark==null || landmark=="" || landmark== "null")
	    {
	        alert("Choose landmark");
	                return false;
	    }
	    else if(reason==null || reason=="")
	    {
	        alert("Choose reason");
	      //  date.focus();
	        return false;
	            
	    }
	        
	    else
	    	{
	    	return true;
	    	}
	   
	}
</script>
<script type="text/javascript">
	/* function formvalue(){
	
	 var x=document.forms["frm1"];
	 var text="";
	 var i;
	 for(i=0; i<x.length; i++)
	 {
	 text+=x.elements[i].value+"<br>";
	 }
	 alert(text);
	 } */

	function showPopup(url) {
		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		size = "height=450,width=520,top=200,left=300," + params;

		newwindow = window.open(url, 'name', size);

		if (window.focus) {
			newwindow.focus();
		}
	}

	function showPopup1(url) {
		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		size = "height=450,width=520,top=200,left=300," + params;

		newwindow = window.open(url, 'name', size);

	}

	function getVehicles(chosenSite) {
		if (chosenSite.length > 0) {
			try {
				var xmlhttp;
				if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
					xmlhttp = new XMLHttpRequest();
				} else {// code for IE6, IE5
					xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
				}
				xmlhttp.onreadystatechange = function() {
					if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
						var fullvehicle = xmlhttp.responseText;
						//alert(fullvehicle);
						fullvehicle = fullvehicle.split("$");
						//document.getElementById("vehicleTypelistd").innerHTML = "<select name='vehicleType' id='vehicleType' multiple=''>"+fullvehicle[0]+"</select>";
						document.getElementById("vehicleTypeAdded").innerHTML = "<select name='chosenVehicleType' id='chosenVehicleType' onChange='getAllVehicles();'  ><option value=''>All</option>"
								+ fullvehicle[1] + "</select>";

					}
				}
				xmlhttp.open("POST", "GetVehicleNotInSiteEmergency?siteId="
						+ chosenSite, true);
				xmlhttp.send();
			} catch (e) {

				alert(e);
			}
		}

	}

	function getAllVehicles() {
		var chosenSite = $("#siteId").val();
		var type = $("#chosenVehicleType").val();
		if (chosenSite.length > 0) {
			try {
				var xmlhttp;
				if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
					xmlhttp = new XMLHttpRequest();
				} else {// code for IE6, IE5
					xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
				}
				xmlhttp.onreadystatechange = function() {
					if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
						var fullvehicle = xmlhttp.responseText;
						fullvehicle = fullvehicle.split("#");
						//document.getElementById("vehicleTypelistd").innerHTML = "<select name='vehicleType' id='vehicleType' multiple=''>"+fullvehicle[0]+"</select>";
						//document.getElementById("vehicleTypeAdded").innerHTML = "<select name='chosenVehicleType' id='chosenVehicleType'  ><option value=''>All</option>"+fullvehicle[1]+"</select>";
						document.getElementById("selectVehicle").innerHTML = "<select name='selectVehicle' id='selectVehicle'  ><option value=''>All</option>"
								+ fullvehicle[1] + "</select>";

					}
				}
				xmlhttp.open("POST", "GetVehicleNotInSiteAndType?siteId="
						+ chosenSite + "&typeId=" + type, true);
				xmlhttp.send();
			} catch (e) {

				alert(e);
			}
		}

	}

	function APLcall() {
		var employeeId = $("#employeeID").val();
		try {
			
			var xmlhttp;
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					var APLfetched = xmlhttp.responseText;
					
					APLfetched = APLfetched.split("$");
					var a = APLfetched[0];
					var p = APLfetched[1];
					var l = APLfetched[2];
					var pc = APLfetched[3];
					var lid = APLfetched[4];
					if(pc== null ||pc == "null")
						{
						pc= pc+" !not subscribed";
						}
					document.getElementById("process").value=pc;
					document.getElementById("area").value=a;
					document.getElementById("place").value=p;	
					document.getElementById("landmark").value=l;
					document.getElementById("landMarkID").value=lid;
			
				}
			}
			xmlhttp.open("POST", "EmergencyAPLFetcher?eid=" + employeeId, true);
			xmlhttp.send();
		} catch (e) {

			alert("APL call alert");
		}

	}
	
</script>
</head>
<body>


	<%
		long empid = 0;
		String siteId = request.getParameter("siteId");
		String type = request.getParameter("chosenVehicleType");
		String vehicle = request.getParameter("selectVehicle");
		String bookingFor = request.getParameter("employeeName");
		String reason = request.getParameter("reason");
		String bookingBy = request.getParameter("empName");
		String travelDate = request.getParameter("travelDate");
		String travelDate1 = request.getParameter("travelDate1");
		String startTime = request.getParameter("startTime");
		String area = request.getParameter("area");
		String place = request.getParameter("place");
		String landmark = request.getParameter("landmark");
		String landmarkID = request.getParameter("landmarkID");
		System.out.println("siteIDDDDDDDDDDDDDDDDDDDD" + siteId);

		String employeeId = OtherFunctions.checkUser(session);

		empid = Long.parseLong(employeeId);
		String pickDrop = request.getParameter("pickdrop");
	%>
	<%@include file="Header.jsp"%>
	<%
		OtherDao ob = null;
		ob = OtherDao.getInstance();
		String empRole = session.getAttribute("role").toString();
	%>


	<br />
	<h3 style="margin-left: 100px">Emergency Transportation services</h3>
	<form id="frm1" method="POST" name="form1"
		action="EmergencyTransportationSubmission.jsp"
		id="emergencyBookingform" onsubmit="return validate()">

		<input type="hidden" value="<%=siteId%>" name="getsite" id="getsite" />
		<input type="hidden" value="<%=type%>" name="gettype" id="gettype" />
		<table>
			<tr>
				<td align="right">Site</td>
				<td>
					<%
						List<SiteDto> siteList = new SiteService().getSites();
					%> <select name="siteId" id="siteId"
					onchange="getVehicles(this.value)">
						<option value="0">---------- Select -----------</option>
						<%
							for (SiteDto dto : siteList) {
						%>


						<option value="<%=dto.getId()%>">
							<%=dto.getName()%>
						</option>


						<%
							}
						%>
				</select> <label for="site" class="requiredLabel">*</label>


				</td>

			</tr>
			<tr>
				<td align="right">Booking For</td>

				<td><input type="hidden" name="employeeID" id="employeeID" /> <input
					type="text" name="employeeName" id="employeeName"
					onclick="showPopup('EmployeeSearch.jsp')" /> <label
					for="employeeID" class="requiredLabel">*</label> <input
					class="formbutton" type="button" value="..."
					onclick="showPopup('EmployeeSearch.jsp')"  /></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>

			<tr>
				<td align="right">Booking By</td>

				<td><input type="hidden" name="empID" id="empID" /> <input
					type="text" name="empName" id="empName"
					onclick="showPopup1('EmployeeSearchBy.jsp')"  /> <label for="empID"
					class="requiredLabel">*</label> <input class="formbutton"
					type="button" value="..."
					onclick="showPopup1('EmployeeSearchBy.jsp')" /></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td width="20%" align="right">Process</td>
				<td><input type="text" id="process" name="process" value=""/><input  type="button"
					class ="formbutton" value="Select process and APL" id="APLselection" onclick="APLcall();" /></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
					
				
			</tr>
			
			<tr>

				<td width="20%" align="right">From Date</td>
				<td><input type="text" name="travelDate" id="travelDate"> <%-- <input type="hidden" id="bookCutoff"
					value="<%=dto.getRequestCutoff()%>"> --%></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>

				<td width="20%" align="right">To Date</td>
				<td><input type="text" name="travelDate1" id="travelDate1" />
					<%-- <input type="hidden" id="bookCutoff"
					value="<%=dto.getRequestCutoff()%>"> --%></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td width="20%" align="right">Start Time</td>
				<td><select name="startTime" id="startTime">
						<%=OtherFunctions.FullTimeInIntervalOptions()%>
				</select><br>
				<label for="">!booking request is available only before 30 min...</label></td>
			</tr>
			<tr>
				<td align="right">Vehicle Type</td>
				<td id="vehicleTypeAdded"><select name="chosenVehicleType"
					id="chosenVehicleType" onchange="getAllVehicles();" height="4">
						<option value="ALL">All</option>

				</select></td>
			</tr>
			<tr>
				<td align="right">Vehicles</td>
				<td id="vehicles"><select name="selectVehicle"
					id="selectVehicle" height="4">
						<option value="ALL">All</option>

				</select></td>
			</tr>		
			<tr>
				<td align="right">Area</td>
				<td><input type="text" name="area" id="area" value=""/><input type="button" value="Alter APL" class="formbutton"
							onclick="showPopup3('LandMarkSearchInEmergency.jsp') " /></td>

			</tr>
			<tr>
				<td align="right">Place</td>
				<td><input type="text" name="place" id="place" value=""/></td>

			</tr>
			<tr>
				<td align="right">Landmark</td>
				<td><input type="text" name="landMark" id="landmark" value=""/><input type="hidden" id="landMarkID" name="landMarkID" /></td>

			</tr>


			<tr>
				<td align="right">Reason</td>
				<td><input type="text" name="reason" id="reason"  /></td>

			</tr>

			<tr>
				<td></td>
				<td><input type="submit" value="Book" class="formbutton" /> <!-- <td><input type="submit" value="Book" class="formbutton" onclick="formvalue()" /> -->
			</tr>


		</table>
	</form>

	<%@include file="Footer.jsp"%>


</body>
</html>