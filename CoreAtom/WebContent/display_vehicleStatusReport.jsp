



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
	var url="display_vehicleStatusReport.jsp?" + params;
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
            
            function printPageUnsaved() {
        		var fromDate = document.getElementById("fromDate").value;
        		var toDate = document.getElementById("toDate").value;
        		var siteId = document.getElementById("siteId").value;
        		var option = document.getElementById("option").value;
        		var vendor = document.getElementById("vendor").value;
        		window.location = "report_excel.jsp?siteId=" + siteId + "&fromDate="
        				+ fromDate + "&toDate=" + toDate + "&option=" + option
        				+ "&vendor="+vendor;
        	}
        </script>

</head>
<body>
	<div id="body">
		<div class="content">
			<%
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String siteID = request.getParameter("siteId");
		String option = request.getParameter("option");
		
		String vendor = request.getParameter("vendor");
		vendor=vendor==null?"":vendor;
		
	/* 	String logTimeIn = request.getParameter("shiftInTime");
		String logTimeOut = request.getParameter("shiftOutTime");		 		
	 */	String fname1=("TripReport By Vehicle :").concat(new Date().toString()).concat(".csv");
		String fname2=("TripReport By Vehicle :").concat(new Date().toString()).concat(".xls");
		String fname3=("TripReport By Vehicle :").concat(new Date().toString()).concat(".xml");		 
		ByVehicleTypeReportHelper reportHelper = new ByVehicleTypeReportHelper();
		List<ByVehicleTypeReportDto> dtolist = reportHelper.getVehicleStatusReport(fromDate,toDate,option,siteID,vendor);
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		
			empid = Long.parseLong(employeeId);
	%>

<%@include file="Header.jsp"%>
			<div>
				<form name="form1" action="display_vehicleStatusReport.jsp"
					method="POST" onsubmit="return validate()">
					<table>
						<tr>
							<td>Choose Site</td>
							<td><select name="siteId" id="siteId">
							<option value="ALL" >ALL</option>
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





							 
							<td>Option</td> 

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
							</select></td>
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
			<h2 align="center">Trip By Vehicle Type Report</h2>
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
	<disp:dipxTable  style="width:90%; margin-left:5%"  id="outer" list="<%=dtolist %>"  >
 	  		 
 	  		<disp:dipxColumn property="vehicleType" title="Vehicle"  
					sortable="true"  />
				
  				<disp:dipxColumn property="option" title="${row.optionTitle}"
					sortable="true"   />
  				<disp:dipxColumn property="averageDistance"    style="text-align:center;"  title="Average Distance"
					sortable="true" format="{0,number,0.00}&nbsp;Km."  />
				<disp:dipxColumn property="averageTime"  style="text-align:center;" title="Average Time" format="{0,time,HH:mm:ss}"  sortable="true"    />
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
	  				<disp:dipxColumn property="averageDistance"  style="font-weight:bold; text-align:center;"   format="{0,number,0.00}&nbsp;Km."  />
					<disp:dipxColumn    />
					<disp:dipxColumn property="maxCount"  style="font-weight:bold; text-align:center;"   format="{0,number,0}" />
					<disp:dipxColumn property="minCount"   style="font-weight:bold; text-align:center;"  format="{0,number,0}" />
			
					<disp:dipxColumn property="count"  style="font-weight:bold; text-align:center;"   />
					<disp:dipxColumn property="totalDistance"  style="font-weight:bold; text-align:center;"  format="{0,number,0.00}&nbsp;Km." />
			 	</disp:DispxSummaryRow>
		
 	</disp:dipxTable>
 	<% if(dtolist!=null) { %>
			<div>	<input type="button" class="formbutton" style="float:left"
					value="Export As Excel" onclick="printPageUnsaved()" /></div>
<%} %>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
