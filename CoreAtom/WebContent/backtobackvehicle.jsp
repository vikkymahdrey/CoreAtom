<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.usermanagement.service.UserManagementService"%>
<%@page import="com.agiledge.atom.dto.UserManagementDTO"%>
<%@page import="com.agiledge.atom.service.BackToBackRoutingService" %>
<%@page import="com.agiledge.atom.dto.BackToBackDto" %>

<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="${contextPath}/css/displaytag.css" />
<style type="text/css">
	div#filterContainer {
		height: auto;
		background-colore : red;
		width: 100%;
		height: 50px;
	}
	div#filterContainer > div {
		display: inline;
		 float: left;
		 padding-left:12px;
	}
		div#filterContainer >div > div:FIRST-CHILD {
		display: inline;
		 float: left;
		 margin-bottom: 5px; 
		 margin-top: 10px;
	}
	
	div#reportDiv {
		margin-top:25px;
	}
</style>
<title>View Back To Back Trips</title>

</head>
<body>
<%
		long empid = 0;
         String status="",value="";
		String employeeId = OtherFunctions.checkUser(session);
		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
			
			
	%>
	<%@include file="Header.jsp"%>
	
	<%
		}
		
		String tripDate = OtherFunctions.changeDateFromatToIso(request
				.getParameter("tripDate"));
		String todate=OtherFunctions.changeDateFromatToIso(request
				.getParameter("toDate"));
		String siteId = request.getParameter("siteId");
		String fname1=("Back2BackReport :").concat(new Date().toString()).concat(".csv");
		String fname2=("Back2BackReport :").concat(new Date().toString()).concat(".xls");
		String fname3=("Back2BackReport :").concat(new Date().toString()).concat(".xml");
		ArrayList<BackToBackDto> list=null;
		if(tripDate!=null&&siteId!=null)
		{
			list=new BackToBackRoutingService().getAllBackToBackTrips(siteId, tripDate,todate);
		}
		if(list!=null&&list.size()>0) {
			TotalTableDecorator grandTotals = new TotalTableDecorator();
			pageContext.getRequest().setAttribute("tableDecor", grandTotals);
			pageContext.setAttribute("siteID", siteId);
			pageContext.setAttribute("fromDate", tripDate);
	%>
<div id="reportDiv" >
			<hr />
			<hr />
			<br />
			<h3 align="center">View Back To Back Trips</h3>
			<display:table class="alternateColorInSub"  id="row"  style="width:80%; margin-left:5%" name="<%=list %>" export="true" defaultsort="1" defaultorder="descending" decorator="tableDecor" pagesize="100" >
				    <display:column property="tripid1" sortable="true" title="Id" />
				    <display:column property="tripdate1" sortable="true" title="Date" paramId="Date"   />
				    <display:column property="triptime1" sortable="true" title="Time"   />
				    <display:column property="triplog1" sortable="true" title="Log"   />
				    <display:column property="security1" sortable="true" title="Escort"   />
				    <display:column property="distance1" sortable="true" title="Distance"   />
				    <display:column property="traveltime1" sortable="true" title="Travel Time"   />
				    <display:column property="vehicletype1" sortable="true" title="Vehicle"   />
				 	 <display:column property="tripid2" sortable="true" title="Back id" />
				    <display:column property="tripdate2" sortable="true" title="Back Date"  paramId="Date"  />
				    <display:column property="triptime2" sortable="true" title="Back  Time"   />
				    <display:column property="triplog2" sortable="true" title="Back Log"   />
				    <display:column property="security2" sortable="true" title="Escort"   />
				    <display:column property="distance2" sortable="true" title="Distance"   />
				    <display:column property="traveltime2" sortable="true" title="Travel Time"   />
				    <display:column property="vehicletype2" sortable="true" title="Vehicle"   />
				 <display:setProperty name="export.csv.filename" value="<%=fname1%>" />
				<display:setProperty name="export.excel.filename"
					value="<%=fname2%>" />
				<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
				 </display:table>	

</div>
<%} %>
		<%@include file='Footer.jsp'%>
</body>
</html>