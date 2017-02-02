
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="java.util.Date"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dto.EmergencyDto"%>
<%@page import="com.agiledge.atom.reports.EmergencyTransportReport"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Emergency Transportation Booked Report</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>

<script type="text/javascript">        
            $(document).ready(function()
            {                                                                        
                $("#fromDate").datepick();
                $("#toDate").datepick();
            });     
        </script>
<script type="text/javascript">
            function validate()
            {
                  
                var fromdate=document.getElementById("fromDate").value;
                var todate=document.getElementById("toDate").value;
                if(fromdate.length<1)
                {
                    alert("Choose From Date");
                  //  date.focus();
                    return false;
                        
                }
                if(todate.length<1)
                {
                    alert("Choose To Date");
                  //  date.focus();
                    return false;
                        
                }
                 else
                {
                    return true;                            
                }
               
            }
        </script>

</head>



<body>
	<%
	String fname1=("EmergencyBookedReport :").concat(new Date().toString()).concat(".csv");
	String fname2=("EmergencyBookedReport :").concat(new Date().toString()).concat(".xls");
	String fname3=("EmergencyBookedReport :").concat(new Date().toString()).concat(".xml");
	
	
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String siteId = request.getParameter("siteId");
	 
		

		EmergencyTransportReport reportEmer = new EmergencyTransportReport();
		
		List<EmergencyDto> emerReportList = null;
		if(fromDate!=null&&toDate!=null)
		{
			emerReportList=reportEmer.getAllEmergencyDetails(fromDate, toDate, siteId);
		}

		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<div>
				<form name="form1" action="EmergencyBookingReport.jsp"
					method="POST" onsubmit="return validate()">
					<table>
						<tr>
							<td>Choose Site</td>
							<td><select name="siteId" id="siteId">
									<%
									String site=(request.getSession().getAttribute("site")==null||request.getSession().getAttribute("site").toString().trim().equals(""))?"" :request.getSession().getAttribute("site").toString().trim();
									 List<SiteDto> siteDtoList = new SiteDao().getSites();  
									 
									if(siteId!=null&&siteId.trim().equals("")==false)
									{
										site=siteId;
									} 
									for (SiteDto siteDto : siteDtoList) {
									
									String siteSelect="";
									if(site.equals(siteDto.getId()))
									{
										siteSelect="selected";
									}
								 
								%>

									<option <%=siteSelect %> value="<%=siteDto.getId()%>"><%=siteDto.getName()%></option>
									<%  }%>
							</select></td>



							<td align="right">From</td>
							<td align="left" colspan="2"><input name="fromDate"
								id="fromDate" type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=fromDate!=null&&fromDate.trim().equals("")==false?fromDate:"" %>" />

							</td>
							<td align="right">To</td>
							<td align="left"><input name="toDate" id="toDate"
								type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=toDate!=null&&toDate.trim().equals("")==false?toDate:"" %>" />


							</td>

	

							<td>&nbsp;</td>
							<td><input type="submit" class="formbutton"
								value="Booked Report" /> </td>
						</tr>
					</table>
				</form>
			</div>


			<br />
			<h2 align="center">Emergency Transportation Booked Report</h2>
			<hr />

			<display:table class="alternateColorInSub" name="<%=emerReportList%>"
				id="row" export="true" defaultsort="1" defaultorder="descending"
				pagesize="500">

				<display:column property="siteId" title="Site" sortable="true"
					headerClass="sortable" />
				<display:column property="bookingFor" title="Booking For"
					sortable="true" headerClass="sortable" />
				<display:column property="travelFromDate" title="From Date"
					sortable="true" headerClass="sortable" />
				<display:column property="travelToDate" title="To Date"
					sortable="true" headerClass="sortable" />
				<display:column property="startTime" title="Start Time"
					sortable="true" headerClass="sortable" />
				<display:column property="bookingBy" title="Booking By"
					sortable="true" headerClass="sortable" />
								
				<display:column property="vehicle" title="Vehicle" sortable="true"
					headerClass="sortable" />
				<display:column property="area" title="Area"
					sortable="true" headerClass="sortable" />
				<display:column property="place" title="Place"
					sortable="true" headerClass="sortable" />
				<display:column property="landmark" title="landmark"
					sortable="true" headerClass="sortable" />
				<display:column property="reason" title="Reason" sortable="true"
					headerClass="sortable" />
				<display:column property="booking_date_status" title="Booking Date status" sortable="true"
					headerClass="sortable" />
				<display:column property="booking_time_status" title="Booking Time status"
					sortable="true" headerClass="sortable" />
				

				<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
				<display:setProperty name="export.excel.filename"
					value="<%=fname2%>" />
				<display:setProperty name="export.xml.filename" value="<%=fname3%>" />


			</display:table>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
