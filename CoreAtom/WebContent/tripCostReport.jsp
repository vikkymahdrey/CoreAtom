



<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.reports.TripUtilisationReportHelper"%>
<%@page import="com.agiledge.atom.reports.dto.TripUtilisationReportDto"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page
	import="com.agiledge.atom.reports.dto.CapacityUtilizationReportDto"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.reports.dto.ProjectWiseTripBillDto"%>
<%@page
	import="com.agiledge.atom.reports.TripSheetNoShowCountReportHelper"%>
<%@page import="com.agiledge.atom.reports.dto.EmpSubscription"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.reports.EmpSubscriptionReportHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://www.nfl.com" prefix="disp"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<%
	request.setAttribute("contextPath", request.getContextPath());
%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Trip Cost Report</title>
<link rel="stylesheet" type="text/css"
	href="${contextPath}/css/displaytag.css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dispx.js"></script>
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
	var url="display_CapacityUtilizationReport.jsp?" + params;
	window.location=url;
}
function openWinodw(url) {
	window.open(url, 'Ratting',
			'width=400,height=250,left=150,top=200,toolbar=1,status=1,');

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
            //	getTripTime();
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

<script language="javascript">
        

        function showTripReport(tripId)
        {
        	try{ 
        	
        	 
        	
  
 
        	
        	var url="view_tracked_trip.jsp?header=false&tripId=" +tripId;
        	var size = "height=600,width=1200,top=100,left=60,toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
        	var newwindow=window.open(url,'name',size);

        	if (window.focus) {
        		newwindow.focus();
        	}
        	}catch(e) {
        		alert(e);
        	}
     }


        function showTripReport1(tripId)
        {
        	try{ 
        	
        	 
        	
  
 
        	
        	var url="view_saved_trip.jsp?header=false&tripId=" +tripId;
        	var size = "height=600,width=1200,top=100,left=60,toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
        	var newwindow=window.open(url,'name',size);

        	if (window.focus) {
        		newwindow.focus();
        	}
        	}catch(e) {
        		alert(e);
        	}
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

					/* 	String logTimeIn = request.getParameter("shiftInTime");
						String logTimeOut = request.getParameter("shiftOutTime");		 		
					 String fname1 = ("TripReport :")
							.concat(new Date().toString()).concat(".csv");
					String fname2 = ("TripReport :").concat(new Date().toString())
							.concat(".xls");
					String fname3 = ("TripReport :").concat(new Date().toString())
							.concat(".xml");
					*/
//					List<TripUtilisationReportDto> tripCost = reportHelper

					long empid = 0;
					String employeeId = OtherFunctions.checkUser(session);

					empid = Long.parseLong(employeeId);
			%>

			<%@include file="Header.jsp"%>
			<div>
				<form name="form1" action="display_tripUsageReport.jsp"
					method="POST" onsubmit="return validate()">
					<table>
						<tr>
							<td>Choose Site</td>
							<td><select name="siteId" id="siteId">
									<%
										String site = (request.getSession().getAttribute("site") == null || request
													.getSession().getAttribute("site").toString().trim()
													.equals("")) ? "" : request.getSession()
													.getAttribute("site").toString().trim();
											List<SiteDto> siteDtoList = new SiteDao().getSites();

											if (siteID != null && siteID.trim().equals("") == false) {
												site = siteID;
											}
											for (SiteDto siteDto : siteDtoList) {

												String siteSelect = "";
												if (site.equals(siteDto.getId())) {
													siteSelect = "selected";
												}
									%>

									<option <%=siteSelect%> value="<%=siteDto.getId()%>"><%=siteDto.getName()%></option>
									<%
										}
									%>
							</select></td>


							<td align="right">From</td>
							<td align="left" colspan="2"><input name="fromDate"
								id="fromDate" type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=fromDate != null
						&& fromDate.trim().equals("") == false ? fromDate : ""%>" />



								To<input name="toDate" id="toDate" type="text" size="6"
								readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}" value="<%=toDate != null && toDate.trim().equals("") == false ? toDate
						: ""%>" />


							</td>
							 <td align="right"><%=SettingsConstant.PROJECT_TERM%></td>
							<td><input type="text" name="projectdesc" id="projectdesc"
								readonly="readonly"
								value="<%=request.getParameter("projectdesc")!=null&&request.getParameter("projectdesc").trim().equals("")==false?request.getParameter("projectdesc"):""%>" />
								<input type="hidden" name="project" id="project"
								value="" />
								
								<%
								/*=request.getParameter("project")!=null&&request.getParameter("project").trim().equals("")==false?request.getParameter("project"):""*/
								%> 
								<input type="button" class="formbutton" value="..."
								onclick="openWinodw('getproject.jsp' ); " /></td>
							<td>
							<td>&nbsp;</td>
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
			<h2 align="center">Trip Cost Report</h2>
			<hr />
	<table>
	<tr><td></td></tr>
	</table>
		</div>
	</div>
<%@include file="Footer.jsp"%>
</body>
</html>
