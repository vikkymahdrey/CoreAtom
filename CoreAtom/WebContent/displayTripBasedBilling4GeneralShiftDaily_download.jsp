 
<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.billingprocess.report.dao.KmBasedClassicBillingReportHelperDao"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants"%>
<%@page import="java.util.Calendar"%> 
<%@page import="java.util.List"%> 
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.billingprocess.report.dto.TripBasedBillingReportHelperDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.billingprocess.report.dao.TripBasedBillingReportHelperDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
 
<%@ taglib uri="http://www.nfl.com" prefix="disp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
 
<% request.setAttribute("contextPath", request.getContextPath());

String date=request.getParameter("date");

        String mimeType = "application/vnd.ms-excel";        
        response.setContentType(mimeType);        
        String fname = new Date().toString();
        response.setHeader("Content-Disposition", "inline; filename = " + fname + ".xls");        
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Pragma", "public");
        response.setHeader("Expires", "Mon, 1 Jan 1995 05:00:00 GMT"); 
	  
 %>
 
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
 
</head>
<body>
	 
			<%
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String siteID = request.getParameter("siteId");
		String option = request.getParameter("option");
		String vehicleType= request.getParameter("chosenVehicleType");
		String year= request.getParameter("year");
		String month= request.getParameter("month");
		String escort = request.getParameter("escort");
		String constraint = request.getParameter("constraint");
		year = year == null ? "":year;
		month = month == null ? "":month;

		
		String vendor="1";
		try{
			System.out.println(")))))))))))))))))))))");
		vendor= request.getParameter("vendor");
		vendor=vendor==null?"":vendor;
		 } catch(Exception e ) {
			 System.out.print("vendor : "+vendor);
			System.out.println("Error in jsp : " + e );
		}
		String  url="displayTripBasedBilling4GeneralShiftDaily.jsp?siteId=" + siteID+"&fromDate="+fromDate+"&toDate="+toDate+"&vendor="+vendor;
		//vendor="1";
	/* 	String logTimeIn = request.getParameter("shiftInTime");
		String logTimeOut = request.getParameter("shiftOutTime");		 		
	 */ 
		
		
		//System.out.println("List size : "+ dtolist.size());
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		
			empid = Long.parseLong(employeeId);
			 
			ArrayList<VendorDto> masterVendorList = new VendorService().getMasterVendorlist ();
			 
			
			
	%>

 
 			<%
			ArrayList<TripBasedBillingReportHelperDto> dtoList =null;
			try{
				dtoList = new TripBasedBillingReportHelperDao ().getTripBasedBillingTripCostReport_daily (siteID, vendor, vehicleType, fromDate, toDate,month,year, escort, constraint);
						 
				
			}catch(Exception e) {
				System.out.println(" Error in jsp "+ e);
			} 
	 	TripBasedBillingReportHelperDto dto = new TripBasedBillingReportHelperDto();
		 	 
		if(dtoList!=null&&dtoList.size()>0) {
			System.out.println("SIze "+ dtoList.size());
		 pageContext.setAttribute("pageUrl",url);
		//	pageContext.setAttribute("siteID", siteID);
			 
		%>
		 
		 			<disp:dipxTable id="outer"  style="width:60%; margin-left:20%" list="<%=dtoList %>"  >
			
				 <disp:dipxColumn  format="{0,date,dd/MM/yyy}" property="date" title="Date"/>
				 <disp:dipxColumn  property="distanceRange" title="Distance"/>
				 <disp:dipxColumn     property="vehicleType" title="Cab"/>
				 <disp:dipxColumn   property="totalTrips"  title="#.Trips"/>
				 <disp:dipxColumn property="rate" title="Trip Cost"  />
				 <disp:dipxColumn  property="escortRate"  format="{0,number,0.00}"  title="Escort Cost"  />
				 <disp:dipxTable id="inner" type="inner" property="trips" parentProperty="totalTrips" >
				 	<disp:dipxColumn property="trip_code" title="Trip Code" />
				 	<disp:dipxColumn property="tripDate"  title="Trip Date" />
				 	<disp:dipxColumn property="trip_time"  title="Time" />
				 	<disp:dipxColumn property="trip_log"  title="Shift" />
				 	<disp:dipxColumn property="tripRate"  title="Trip Rate" />
				 	<disp:dipxColumn property="escortRate"  title="Escort Cost" />
				 	
				 </disp:dipxTable>
				 <disp:DispxGroupSummaryRow groupProperty="date" id="grp">
				 	<td></td>
				 	<td></td>
				 	<td></td>
				 	 <disp:dipxColumn style="text-align:center; font-weight:bold;"  property="totalTrips" />
				 	 <disp:dipxColumn style="text-align:center; font-weight:bold;"  property="rate"  />
				 	 <disp:dipxColumn style="text-align:center; font-weight:bold;"  property="escortRate" format="{0,number,0.00}" />
				 </disp:DispxGroupSummaryRow>
				 <disp:DispxSummaryRow style="background-color:#fff;" >
				  
				 	<td colspan="3"><center>Grand Total </center></td>
				 	  
				 	 <disp:dipxColumn style="text-align:center; font-weight:bold;"  property="totalTrips" />
				 	 <disp:dipxColumn style="text-align:center; font-weight:bold;"  property="rate"  />
				 	 <disp:dipxColumn style="text-align:center; font-weight:bold;"  property="escortRate"  format="{0,number,0.00}"   />
				 	  
				 </disp:DispxSummaryRow>
				  
 		</disp:dipxTable> 	  
		 
		<%} else { 
			System.out.println(" dto list is null ");
		}
		%>	 

</body>
</html>
