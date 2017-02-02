<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dto.AdhocDto" %>
<%@page import="com.agiledge.atom.service.AdhocService" %>
<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.tablesorter.js"></script>
<title>View Adhoc Bookings</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
</head>
<script type="text/javascript">
$(document).ready(function(){
	 $("#adhoctbl").tablesorter();	 
});
</script>
<script type="text/javascript">
function openWindow(bookingid) {
	url='viewadhocpassengers.jsp?bookingId='+bookingid;
	window.open(url, 'Ratting','width=400,height=350,left=150,top=200,toolbar=1,status=1,');
}
function onchangesite(site) {
	window.location.href="viewadhocTrips.jsp?siteId="+site;
}

</script>
<body>
<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
		String site = request.getParameter("siteId");
		List<SiteDto> dtos= new SiteDao().getSites();
	%>
	<%@include file="Header.jsp"%>
	<%
		OtherDao ob = null;
		ob = OtherDao.getInstance();
	%>
<br />
	<h3 style="margin-left: 100px" align="center">View Adhoc Booking</h3>
					<table>
					<tr>
						<td>Site &nbsp;&nbsp;&nbsp;&nbsp;<select name="siteId" onchange="onchangesite(this.value);">
								<option>Select</option>
								<%
									for (SiteDto dto : dtos) {
										if (dto.getId().equals(site)) {
								%>
								<option value="<%=dto.getId()%>" selected="selected"><%=dto.getName()%></option>
								<%
									} else {
								%>
								<option value="<%=dto.getId()%>"><%=dto.getName()%></option>
								<%
									}
									}
								ArrayList<AdhocDto> list= new AdhocService().getAdhocTrips(site);
								ArrayList<VendorDto> masterVendorList = new VendorService().getMasterVendorlist();
								%>
						</select></td>
					</tr>
				</table>
	<form action="AdhocVendorAssign" >
	<table id="adhoctbl" class="tablesorter">
<thead> 
<tr>
			<th width="1%"></th>
			<th align="center">Booking Id</th>
			<th align="center">Type</th>
			<th align="center">Travel Date</th>
			<th align="center">Booked Date</th>
			<th align="center">Booked By</th>
			<th align="center">Start Time</th>
			<th align="center">End Time</th>
			<th align="center">Origin</th>
			<th align="center">Destination</th>
			<th align="center">Reason</th>
			<th align="center">Comments</th>
			<th align="center">Status</th>
			<th align="center">Actions</th>
			</tr>
		</thead>
		</tbody>
			<%
			for(AdhocDto dto:list)
			{
				if(dto.getEndTime()==null)
				{
				dto.setEndTime("");
				}
			%>
			<tr>
		<td align="center"><input type="checkbox" name="bookingIdCheckBox" value="<%=dto.getBookingId()%>"/></td>
		<td align="center"><%=dto.getBookingId() %></td>
		<td align="center"><%=dto.getAdhocType()%></td>
		<td align="center"><%=dto.getTravelDate()%></td>
		<td align="center"><%=dto.getBookedDate()%></td>
		<td align="center"><%=dto.getBookedByname()%></td>
		<td align="center"><%=dto.getStartTime() %></td>
		<td align="center"><%=dto.getEndTime() %></td>
		<td align="center"><%=dto.getOrgination() %></td>
		<td align="center"><%=dto.getDestination() %></td>
		<td align="center"><%=dto.getReason() %></td>
		<td align="center"><%=dto.getComment() %></td>
		<td align="center"><%=dto.getStatus() %></td>
		<td align="center"><input type="button" class="formbutton" value="Passenger Details" onClick="openWindow('<%=dto.getBookingId() %>');" /></td>
		</tr>
		<% 
		} 
		%>
		</table>
		<table align="center"> 
		<tr align="center"><td align="center">Select Vendor&nbsp;&nbsp;&nbsp;&nbsp;<select id="vendorselect" name="vendorselect">
		<%
		for(VendorDto vdto:masterVendorList)
		{
			%>
		<option value="<%=vdto.getCompanyId()%>"><%=vdto.getCompany()%></option>
		<%} %>
		</select></td>
		</tr>
		<tr align="center"><td align="center">&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="Assign Vendor" class="formbutton"/></td>
		<input type="hidden" value="<%=site %>" name="siteId" />
		</table>
		</form>
<%@include file="Footer.jsp"%>
</body>
</html>