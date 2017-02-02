



<%@page import="java.util.concurrent.TimeUnit"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="com.agiledge.atom.reports.dto.ByVehicleTypeReportDto"%>
<%@page import="com.agiledge.atom.reports.ByVehicleTypeReportHelper"%>
<%@page import="com.agiledge.atom.reports.TripUtilisationReportHelper"%>
<%@page import="com.agiledge.atom.reports.dto.TripUtilisationReportDto"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.reports.dto.CapacityUtilizationReportDto"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.reports.dto.ProjectWiseTripBillDto"%>
<%@page import="com.agiledge.atom.reports.TripSheetNoShowCountReportHelper"%>
<%@page import="com.agiledge.atom.reports.dto.EmpSubscription"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.reports.EmpSubscriptionReportHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.nfl.com" prefix="disp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Trip By Vehicle Type Report</title>
<% request.setAttribute("contextPath", request.getContextPath()); %>
<link rel="stylesheet" type="text/css" href="${contextPath}/css/displaytag.css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">
function showReport(type,site, tripdate)
{
	var d=tripdate.split("-");
	tripdate=d[2]+"/"+d[1]+"/"+d[0];
	var params ="fromDate="+tripdate+"&toDate="+ tripdate + "&siteId=" + site+ "&vehicleTypeId=" + type;
	var url="display_vehicleKilometerReport.jsp?" + params;
	window.location=url;
}

function resetFields()
{
	$("input[type=hidden]").val("");
}
 </script>

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
	<div id="body">
		<div class="content">
			<%
			try{
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String siteID = request.getParameter("siteId");
		//String option = request.getParameter("option");
		
		String vendor = request.getParameter("vendor");
		vendor=vendor==null?"":vendor;
		
	/* 	String logTimeIn = request.getParameter("shiftInTime");
		String logTimeOut = request.getParameter("shiftOutTime");		 		
	 */	String fname1=("TripReport By Vehicle :").concat(new Date().toString()).concat(".csv");
		String fname2=("TripReport By Vehicle :").concat(new Date().toString()).concat(".xls");
		String fname3=("TripReport By Vehicle :").concat(new Date().toString()).concat(".xml");		 
		ByVehicleTypeReportHelper reportHelper = new ByVehicleTypeReportHelper();
		List<TripDetailsDto> dtolist = reportHelper.getVehicleKilometerReport(fromDate,toDate,siteID,vendor);
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		//System.out.println( employeeId);
		
			empid = Long.parseLong(employeeId);
		/* System.out.println("List size "+dtolist.size()); */
			/*  for(TripDetailsDto d : dtolist)
			{
				System.out.println(d.getTrip_date() + d.getTrip_code()+d.getTrip_code());
				System.out.println(d.getDistanceDouble() +  d.getDistanceCovered() +d.getTravelTime() + d.getTimeElapsed());
			}  */ 
	%>

<%@include file="Header.jsp"%>
			<div>
				<form name="form1" action="display_vehicleKilometerReport.jsp"
					method="POST" onsubmit="return validate()">
					<table>
						<tr>
							<td>Choose Site</td>
							<td><select name="siteId" id="siteId">
									<%
									String site=(request.getSession().getAttribute("site")==null||request.getSession().getAttribute("site").toString().trim().equals(""))?"" :request.getSession().getAttribute("site").toString().trim();
									 List<SiteDto> siteDtoList = new SiteDao().getSites();  
									 
									if(siteID!=null&&siteID.trim().equals("")==false)
									{
										site=siteID;
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

							<td>Vendor
							</td>
							<td>
							<% 
			ArrayList<VendorDto> masterVendorList = new VendorService()
			.getMasterVendorlist();
							%>
							<select id="vendor" name="vendor">
									<%
										for (VendorDto vendorDto : masterVendorList) {
											String vendorSelect="";
											if(vendorDto.getCompanyId().equals(vendor)) {
												vendorSelect="selected";
											}
									%>

									<option <%=vendorSelect %> value="<%=vendorDto.getCompanyId()%>"><%=vendorDto.getCompany()%></option>
									<%
										}
									%>
							</select>
							</td>
							<td align="right">From</td>
							<td align="left" colspan="2"><input name="fromDate"
								id="fromDate" type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=fromDate!=null&&fromDate.trim().equals("")==false?fromDate:"" %>" />



								To<input name="toDate" id="toDate" type="text" size="6"
								readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=toDate!=null&&toDate.trim().equals("")==false?toDate:"" %>" />


							</td>





							 
							<%-- <td>Option</td> 

							<td><select name="option" id="option">
									<%
									
									 String [] options={"ALL", "Time slot","Time of the day","Distance grid"};  
									 
									 
									for (String opt: options) {
									
									String optionSelect="";
									if(opt.equals(option))
									{
										optionSelect="selected";
									}
								 
								%>

									<option <%=optionSelect %> value="<%=opt%>"><%=opt%></option>
									<%  }%>
							</select></td> --%>
							<td><input type="submit" class="formbutton"
								value="Generate Report" /> <input type="reset"
								class="formbutton" value="Reset" onclick="resetFields();" /></td>
						</tr>
					</table>
				</form>
			</div>

			<hr />
			<hr />
			<br />
			<h2 align="center">Trip Based Kilometer Report</h2>
			<hr />
			<%
			TotalTableDecorator grandTotals = new TotalTableDecorator();

			grandTotals.setTotalLabel("Full Total");
	//		grandTotals
			//grandTotals.setSubtotalLabel("Days Summary");
			//grandTotals.setGrandTotalDescription("&nbsp;");
		//	grandTotals.setSubtotalLabel("&nbsp;");
			pageContext.getRequest().setAttribute("tableDecor", grandTotals);
		%>
		 <table >
		 <tr>
		 <th>Trip Date</th>
		 <th> Trip code </th>
		 <th> Trip Time </th>
		<th> Vehicle No </th>
		
		
		<th> Estimated Distance (Km) </th>
		<th> Estimated Time (Mins)</th>
		<th> Distance covered (Km)</th>
		<th> Travel Time (Mins)</th>
		</tr>
		<% float Totalestdist = 0.0f;
		float TotalTraveldist = 0.0f;
		
		for(TripDetailsDto d : dtolist)
		{
			/* System.out.println(d.getTrip_date() + d.getTrip_code()+d.getTrip_code());
			System.out.println(d.getDistanceDouble() +  d.getDistanceCovered() +d.getTravelTime() + d.getTimeElapsed());
 */		 float distancecovered = (float)d.getDistanceCovered();
 String timetaken= String.format("%d.%d", 
    TimeUnit.MILLISECONDS.toMinutes(d.getTimeElapsed()),
    TimeUnit.MILLISECONDS.toSeconds(d.getTimeElapsed()) - 
    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(d.getTimeElapsed()))
);
        Totalestdist += d.getDistanceDouble();
        TotalTraveldist += distancecovered;%>
		<tr>
		<td><%=d.getTrip_date() %></td>
		<td><%=d.getTrip_code()%></td>
		<td><%=d.getTrip_time()%></td>
		<td><%=d.getVehicleNo()%></td>
		
		<td> <%=d.getDistanceDouble()%> </td>
		<td> <%=d.getTravelTime() %> </td>
		<td> <%=distancecovered%> </td>
	
		<td> <%=timetaken%> </td></tr>
		 
		<%} %> 
		<tr>
		<td style="font-weight:bold">Total</td>
		<td></td>
		<td></td>
		<td></td>
		<td style="font-weight:bold"><%=Totalestdist%></td>
		<td></td>
		<td style="font-weight:bold"><%=TotalTraveldist%></td>
		<td></td>
		</table> 
	<%-- <disp:dipxTable  style="width:90%; margin-left:5%"  id="outer" list="<%=dtolist%>"  >
 	  		 
 	  		<disp:dipxColumn property="VehicleNo" title="Vehicle"  
					 sortable="true"  />
					<disp:dipxColumn property="Trip_code" title="Trip code"  
					sortable="true"  />
					<disp:dipxColumn property="Trip_date" title="Trip date"  
					sortable="true"  />
					<disp:dipxColumn property="Trip_time" title="Trip time"  
					sortable="true"  />
				
  				<disp:dipxColumn property="option" title="${row.optionTitle}"
					sortable="true"   />
  				<disp:dipxColumn property="DistanceDouble"    style="text-align:center;"  title="Estimated Distance"
					sortable="true" format="{0,number,0.00}&nbsp;Km."  />
				<disp:dipxColumn property="TravelTime"  style="text-align:center;" title="Estimated Time" format="{0,time,HH:mm:ss}"  sortable="true"    />
				<disp:dipxColumn property="DistanceCovered"    style="text-align:center;"  title="Distance Covered"
					sortable="true" format="{0,number,0.00}&nbsp;Km."  />
				<disp:dipxColumn property="TimeElapsed"  style="text-align:center;" title="Time Elapsed" format="{0,time,HH:mm:ss}"  sortable="true"    />
			
				<disp:dipxColumn property="maxCount"  style="text-align:center;" title="Maximum Trips Per Day"
					sortable="true" format="{0,number,0}" />
				<disp:dipxColumn property="minCount"   style="text-align:center;"  title="Mininum Trips Per Day"
					sortable="true" format="{0,number,0}" />
		
				<disp:dipxColumn property="count" title="# of Trips"   style="text-align:center;" 
					sortable="true" format="{0,number,0}" />
				<disp:dipxColumn property="totalDistance" title="Total Distance"   style="text-align:center;" 
					sortable="true" format="{0,number,0.00}&nbsp;Km." />
		
			 	<disp:DispxSummaryRow>
				 	<td><b>Total
				 	</b></td>
					
	  				<disp:dipxColumn   />
	  				<disp:dipxColumn   />
	  				<disp:dipxColumn   />
	  				<disp:dipxColumn property="DistanceDouble"  style="font-weight:bold; text-align:center;"   format="{0,time,HH:mm:ss}"  />
					<disp:dipxColumn property="TravelTime"  style="font-weight:bold; text-align:center;"   format="{0,number,0.00}&nbsp;Km."  />
					<disp:dipxColumn property="DistanceCovered"  style="font-weight:bold; text-align:center;"   format="{0,time,HH:mm:ss}"  />
					<disp:dipxColumn property="TravelElapsed"  style="font-weight:bold; text-align:center;"   format="{0,number,0.00}&nbsp;Km."  />
					<disp:dipxColumn property="maxCount"  style="font-weight:bold; text-align:center;"   format="{0,number,0}" />
					<disp:dipxColumn property="minCount"   style="font-weight:bold; text-align:center;"  format="{0,number,0}" />
			
					<disp:dipxColumn property="count"  style="font-weight:bold; text-align:center;"   />
					<disp:dipxColumn property="totalDistance"  style="font-weight:bold; text-align:center;"  format="{0,number,0.00}&nbsp;Km." />
			 	</disp:DispxSummaryRow>
		
 	</disp:dipxTable>
			 --%>
<%}catch(Exception e){System.out.println("error in display_vehiclekilometerReport " + e);
System.out.println(e.getLocalizedMessage());} %>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
