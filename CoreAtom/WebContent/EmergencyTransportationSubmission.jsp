
<%@page import="java.util.Date"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="org.displaytag.decorator.MultilevelTotalTableDecorator"%>
<%@page import="com.agiledge.atom.servlets.EmergencyDao"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>



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

</head>
<body >
	<%
		long empid = 0;
	
	
		String siteId = request.getParameter("siteId");
		String type = request.getParameter("chosenVehicleType");
		String vehicle = request.getParameter("selectVehicle");
		String bookingFor = request.getParameter("employeeID");
		String bookingBy = request.getParameter("empID");
		String reason = request.getParameter("reason");
		String travelDate = request.getParameter("travelDate");
		String traveltoDate = request.getParameter("travelDate1");
		String startTime = request.getParameter("startTime");
		String area = request.getParameter("area");
		String place = request.getParameter("place");
		String landmark = request.getParameter("landMark");
		String landmarkId = request.getParameter("landMarkID");
		
		
		
		String employeeId = OtherFunctions.checkUser(session);

		empid = Long.parseLong(employeeId);
		
	%>
	<%@include file="Header.jsp"%>
	<%
	
		OtherDao ob = null;
		ob = OtherDao.getInstance();
		String empRole = session.getAttribute("role").toString();
		
	%>
	<table><tr>
    </br></br></br></br>
			
	<%
		
	int i=new EmergencyDao().insertEmergencyDetails(siteId, bookingFor, travelDate,traveltoDate,startTime, bookingBy, type, vehicle, area, place, landmark,landmarkId, reason);
	if(i>0)
	{ %>
	
	<td><p style="color:black" style="margin-left: 30px">Emergency transportation service booked successfully......... </p>
	</td>
	<%}
	else {%>
			<td><p style="color:red" style="margin-left: 30px">Emergency transportation service booking  Unsuccessful......... </p></td>
	<%}	
	 %>
	</br></br></br></br>
	
	
	<td>
	<button onclick="window.history.back()">Back</button></td>
	</tr>
	
	</table>
	<%@include file="Footer.jsp"%>
</body>
</html>