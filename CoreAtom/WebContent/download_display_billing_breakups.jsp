<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.reports.TripBillingBreakUpsReportHelper"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dto.PayrollDto"%>
<%@page import="com.agiledge.atom.service.PayrollService"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="error.jsp"%>
	<%@ taglib uri="http://www.nfl.com" prefix="disp"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Billing Break Ups</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
  <%

        String mimeType = "application/vnd.ms-excel";        
        response.setContentType(mimeType);        
        String fname = new Date().toString();
        response.setHeader("Content-Disposition", "inline; filename = APL" + fname + ".xls");        
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Pragma", "public");
        response.setHeader("Expires", "Mon, 1 Jan 1995 05:00:00 GMT");
        %>
<!-- Beginning of compulsory code below -->
 
</head>
<body>
	<%
        long empid = 0;
        String employeeId = OtherFunctions.checkUser(session);        
            empid = Long.parseLong(employeeId);
            %>
            
            
									<%
									String monthParam=request.getParameter("month")==null?"":request.getParameter("month");
									String yearParam=request.getParameter("year")==null?"":request.getParameter("year");
									String siteID=request.getParameter("siteId");
									siteID=siteID==null?"":siteID;
									%>
	 

<% 

	 			try {
				TripBillingBreakUpsReportHelper helper = new TripBillingBreakUpsReportHelper();
				System.out.println("month : "+ monthParam +  "\n Year : "+ yearParam + " site : " + siteID);
		ArrayList<TripDetailsDto> dtoList=	helper.getBillBreakUpsReport(Integer.parseInt(monthParam), Integer.parseInt( yearParam), Integer.parseInt( siteID));
		System.out.println("size :  " + dtoList.size());
			%>
			 		<br/>
				<disp:dipxTable id="row1" styleClass="alternateColor"  list="<%=dtoList %>">
						<disp:dipxColumn title="Date" 
					  sortable="true" headerClass="sortable"  property="trip_date"  />
					  					  <disp:dipxColumn title="Trip#" 
					  sortable="true" headerClass="sortable"  property="trip_code"  />
					  <disp:dipxColumn title="Vehicle" 
					  sortable="true" headerClass="sortable"  property="vehicle_type"  />
					  <disp:dipxColumn title="Vehicle#" 
					  sortable="true" headerClass="sortable"  property="vehicleNo"  />
							  <disp:dipxColumn title="Escort#" 
					  sortable="true" headerClass="sortable"  property="escortNo"  />
					    <disp:dipxColumn title="Escort" 
					  sortable="true" headerClass="sortable"  property="escort"  />
					    <disp:dipxColumn title="Shift" 
					  sortable="true" headerClass="sortable"  property="trip_time"  />
					    <disp:dipxColumn title="Reporting Time" 
					  sortable="true" headerClass="sortable"  property="actualLogTime"  />
					    <disp:dipxColumn title="Reporting Time" 
					  sortable="true" headerClass="sortable"  property="actualLogTime"  />
					   <disp:dipxColumn title="Status" 
					  sortable="true" headerClass="sortable"  property="onTimeStatus"  />
					    <disp:dipxColumn title="Approval Status" 
					  sortable="true" headerClass="sortable"  property="approvalStatus"  />
					   <disp:dipxColumn title="Seats" 
					  sortable="true" headerClass="sortable"  property="sittingCapasity"  />
					   <disp:dipxColumn title="Boarded" 
					  sortable="true" headerClass="sortable"  property="showCount"  />
				 <disp:dipxColumn title="No Shows" 
					  sortable="true" headerClass="sortable"  property="noShowCount"  />
					  
				</disp:dipxTable>
			<%
			}catch(Exception e) {
				System.out.print("error in display_billing_breakups :" +e);
			}
			 %>
			
	 






 	 
 
</body>
</html>
