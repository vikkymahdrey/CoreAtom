



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
<title>Trip Utilization Report</title>
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
                var mod=document.getElementById("tripMode").value;   
                var time=document.getElementById("tripTime").value;  
               
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
                        
                }  else if(mod=="ALL"&&time!="ALL")
                {
                    alert("Cannot choose time for both Shift");
                    //time.options[0].selected=true;   
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
          
 
        	
        
        
function getTripTypeTime()
{     
	var tripType=document.getElementById("tripType").checked;
	var logtype=document.getElementById("tripMode").value;
	var site=document.getElementById("siteId").value;
	if(tripType==true)
	{	
    if(logtype=="ALL")
    	{
    	var tripTimeId=document.getElementById("tripTimeId");
    	
    	tripTimeId.innerHTML='<select multiple="multiple" name="tripTime" id="tripTime"> <option value="ALL" >ALL</option></select>';
    	return;
    	}
    
    var url="GetLogTime?logtype="+logtype+"&type=disabled&site="+site;                                    
    xmlHttp=GetXmlHttpObject()
    if (xmlHttp==null)
    {
        alert ("Browser does not support HTTP Request");
        return
    }                    
    xmlHttp.onreadystatechange=setLogTime	
    xmlHttp.open("GET",url,true)                
    xmlHttp.send(null)
	}
	else
		{
		getTripTime();
		}
}
function getTripTime()
{                    
	var tripType=document.getElementById("tripType").checked;
	if(tripType==true)
	{	
		getTripTypeTime();	
	}
	else
		{
    var logtype=document.getElementById("tripMode").value;
    if(logtype=="ALL")
    	{
    	var tripTimeId=document.getElementById("tripTimeId");
    	tripTimeId.innerHTML='<select name="tripTime" id="tripTime"> <option value="ALL" >ALL</option></select>';
    	return;
    	}
    var site=document.getElementById("siteId").value;
    var url="GetLogTime?logtype="+logtype+"&site="+site;                                    
    xmlHttp=GetXmlHttpObject()
    if (xmlHttp==null)
    {
        alert ("Browser does not support HTTP Request");
        return
    }                    
    xmlHttp.onreadystatechange=setLogTime	
    xmlHttp.open("GET",url,true)                
    xmlHttp.send(null)
}
}
    
function GetXmlHttpObject()
{
    var xmlHttp=null;
    if (window.XMLHttpRequest) 
    {
        xmlHttp=new XMLHttpRequest();
    }                
    else if (window.ActiveXObject) 
    { 
        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
    }

    return xmlHttp;
}

function setLogTime() 
{                      
    if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
    { 
        var returnText=xmlHttp.responseText;
        var tripTimeId=document.getElementById("tripTimeId");
        tripTimeId.innerHTML='<select  multiple="multiple" name="tripTime" id="tripTime">  '+returnText+'</select>';                                             
    }
}

        </script>


</head>
<body>
	<div id="body">
		<div class="content">
			<%
				try {
					String fromDate = request.getParameter("fromDate");
					String toDate = request.getParameter("toDate");
					String siteID = request.getParameter("siteId");
					String tripMode = request.getParameter("tripMode");
					tripMode = tripMode == null ? "" : tripMode;
					String[] tripTimes = request.getParameterValues("tripTime");
					if (tripTimes == null) {
						String[] array = (String[]) request
								.getAttribute("tripTime");
						tripTimes = array;

					}
					System.out.println("...............");

					/* 	String logTimeIn = request.getParameter("shiftInTime");
						String logTimeOut = request.getParameter("shiftOutTime");	*/	 		
					String fname1 = ("TripReport :")
							.concat(new Date().toString()).concat(".csv");
					String fname2 = ("TripReport :").concat(new Date().toString())
							.concat(".xls");
					String fname3 = ("TripReport :").concat(new Date().toString())
							.concat(".xml");
					TripUtilisationReportHelper reportHelper = new TripUtilisationReportHelper();
					List<TripUtilisationReportDto> emplist = reportHelper

					.getTripUtilisationReport(siteID, fromDate, toDate, tripMode,
							tripTimes);
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
							<option value="ALL">ALL</option>
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
                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=toDate != null && toDate.trim().equals("") == false ? toDate
						: ""%>" />


							</td>





							<td align="right">Log In/Out</td>
							<td><select name="tripMode" id="tripMode"
								onchange="getTripTime()">
									<%
										String logs[] = { "ALL", "IN", "OUT" };
											for (int i = 0; i < logs.length; i++) {
												String tripModSelect = "";
												if (tripMode.equalsIgnoreCase(logs[i])) {
													tripModSelect = "selected";
												}
									%>
									<option <%=tripModSelect%> value="<%=logs[i]%>"><%=logs[i]%></option>
									<%
										}
									%>

							</select></td>
							<td align="right">Disabled Shift</td>
							<td><input type="checkbox" name="tripType" id="tripType"
								value="enable" onclick="getTripTypeTime()" /></td>
							<td align="right">Shift</td>
							<td id="tripTimeId">
								<%
									boolean error = false;
										String tripTimeMultiple = "";
										ArrayList<LogTimeDto> logTimeDtos = null;
										try {
											if (tripMode != null && tripMode.equalsIgnoreCase("IN")
													|| tripMode.equalsIgnoreCase("OUT")) {

												System.out.println("TripMODE: " + tripMode);
												logTimeDtos = new LogTimeDao().getAllLogtime(tripMode,
														site);

												tripTimeMultiple = "multiple";
											} else {
												error = true;
											}
										} catch (Exception e) {
											error = true;
										}
								%> <select <%=tripTimeMultiple%> name="tripTime" id="tripTime">
									<%
										if (error == true) {
									%>
									<option value="ALL">ALL</option>

									<%
										} else {
												int i = 0;
												for (LogTimeDto dto : logTimeDtos) {
													String tripTimeSelect = "";
													try {
														if (tripTimes != null && tripTimes.length > i
																&& dto.getLogTime().equals(tripTimes[i])) {
															tripTimeSelect = "selected";
															i++;
														}
													} catch (Exception e) {
														tripTimeSelect = "";
													}
									%>
									<option <%=tripTimeSelect%> value="<%=dto.getLogTime()%>"><%=dto.getLogTime()%></option>
									<%
										}
											}
									%>
							</select>
							</td>

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
			<h2 align="center">Trip Utilization Report</h2>
			<hr />
			<%
				TotalTableDecorator grandTotals = new TotalTableDecorator();

					grandTotals.setTotalLabel("Full Total");
					//		grandTotals
					//grandTotals.setSubtotalLabel("Days Summary");
					//grandTotals.setGrandTotalDescription("&nbsp;");
					//	grandTotals.setSubtotalLabel("&nbsp;");
					pageContext.getRequest()
							.setAttribute("tableDecor", grandTotals);
			%>

			<disp:dipxTable id="outer" style="width:60%; margin-left:20%"
				list="<%=emplist%>">
				<disp:dipxColumn group="1" style="text-align:center;"
					sortable="true" format="{0,date,dd/MM/yyy}" property="date"
					title="Date"></disp:dipxColumn>
				<disp:dipxColumn sortable="true" style="text-align:center;"
					property="totalCount" expandable="true" title="Total"></disp:dipxColumn>
				<disp:dipxColumn sortable="true" style="text-align:center;"
					property="downloadedCount" expandable="true" title="Downloaded"></disp:dipxColumn>
				<disp:dipxColumn sortable="true" style="text-align:center;"
					property="initialCount" expandable="true" title="Not Started"></disp:dipxColumn>
				<disp:dipxColumn sortable="true" style="text-align:center;"
					property="startedCount" title="Started" expandable="true"></disp:dipxColumn>
				<disp:dipxColumn sortable="true" style="text-align:center;"
					property="stoppedCount" title="Completed" expandable="true"></disp:dipxColumn>
				<disp:dipxColumn sortable="true" style="text-align:center;"
					property="pendingCount" title="Not Downloaded" expandable="true"></disp:dipxColumn>
					<disp:dipxColumn  style="text-align:center;"
					property="usage" title="%Usage" ></disp:dipxColumn>


				<disp:dipxTable styleClass="inner displaytag" id="inner-downloaded"
					property="downloadedTrips" type="inner"
					parentProperty="downloadedCount">
					<disp:dipxColumn sortable="true" style="text-align:center;"
						href="javascript:showTripReport('${row.id }');"
						property="trip_code" title="Trip Code"></disp:dipxColumn>
						<disp:dipxColumn sortable="true" style="text-align:center;"
						 
						property="vehicleNo" title="Vehicle#"></disp:dipxColumn>
					<disp:dipxColumn sortable="true" style="text-align:center;"
						 
						property="driverName" title="Driver"></disp:dipxColumn>	
					<disp:dipxColumn sortable="true" style="text-align:center;"
						property="trip_time" title="Trip Time"></disp:dipxColumn>
					<disp:dipxColumn sortable="true" style="text-align:right;"
						property="distanceDouble"  format="{0,number,0.00}&nbsp;Km."  title="Distance"></disp:dipxColumn>
					<disp:dipxColumn sortable="true" style="text-align:center;"
						property="travelTimeInDate" format="{0,time,HH:mm:ss }"
						title="Travel Time"></disp:dipxColumn>

				</disp:dipxTable>

				<disp:dipxTable styleClass="inner displaytag" id="inner-initial"
					property="initialTrips" type="inner"
					parentProperty="initialCount">
					<disp:dipxColumn sortable="true" style="text-align:center;"
						href="javascript:showTripReport('${row.id }');"
						property="trip_code" title="Trip Code"></disp:dipxColumn>
						<disp:dipxColumn sortable="true" style="text-align:center;"
						 
						property="vehicleNo" title="Vehicle#"></disp:dipxColumn>
					<disp:dipxColumn sortable="true" style="text-align:center;"
						 
						property="driverName" title="Driver"></disp:dipxColumn>	
					<disp:dipxColumn sortable="true" style="text-align:center;"
						property="trip_time" title="Trip Time"></disp:dipxColumn>
					<disp:dipxColumn sortable="true" style="text-align:right;"
						property="distanceDouble"  format="{0,number,0.00}&nbsp;Km."  title="Distance"></disp:dipxColumn>
					<disp:dipxColumn sortable="true" style="text-align:center;"
						property="travelTimeInDate" format="{0,time,HH:mm:ss }"
						title="Travel Time"></disp:dipxColumn>

				</disp:dipxTable>
				
				<disp:dipxTable id="inner-started" styleClass="inner displaytag"
					property="startedTrips" type="inner" parentProperty="startedCount">
					<disp:dipxColumn sortable="true" style="text-align:center;"
						href="javascript:showTripReport('${row.id }');"
						property="trip_code" title="Trip Code"></disp:dipxColumn>
				<disp:dipxColumn sortable="true" style="text-align:center;"
						 
						property="vehicleNo" title="Vehicle#"></disp:dipxColumn>
				<disp:dipxColumn sortable="true" style="text-align:center;"
						 
						property="driverName" title="Driver"></disp:dipxColumn>
					<disp:dipxColumn sortable="true" style="text-align:center;"
						property="trip_time" title="Trip Time"></disp:dipxColumn>
					<disp:dipxColumn sortable="true" style="text-align:right;"
						property="distanceDouble"  format="{0,number,0.00}&nbsp;Km."  title="Distance"  ></disp:dipxColumn>
					<disp:dipxColumn sortable="true" style="text-align:center;"
						property="travelTimeInDate" format="{0,time,HH:mm:ss }"
						title="Travel Time"></disp:dipxColumn>
				</disp:dipxTable>


				<disp:dipxTable id="inner-total" styleClass="inner displaytag"
					property="totalTrips" type="inner" parentProperty="totalCount">
					<disp:dipxColumn property="trip_code" href="javascript:showTripReport1('${row.id }');" style="text-align:center;"
						title="Trip Code"></disp:dipxColumn>
					<disp:dipxColumn sortable="true" style="text-align:center;"
						 
						property="vehicleNo" title="Vehicle#"></disp:dipxColumn>
					<disp:dipxColumn sortable="true" style="text-align:center;"
						 
						property="driverName" title="Driver"></disp:dipxColumn>
				
					<disp:dipxColumn property="trip_time" style="text-align:center;"
						title="Trip Time"></disp:dipxColumn>
					<disp:dipxColumn property="trip_log" style="text-align:center;"
						title="Shift"></disp:dipxColumn>


				</disp:dipxTable>
				<disp:dipxTable id="inner-notdownloaded"
					styleClass="inner displaytag" property="notDownloaded" type="inner"
					parentProperty="pendingCount">
					<disp:dipxColumn property="trip_code" style="text-align:center;"  href="javascript:showTripReport1('${row.id }');" 
						title="Trip Code"></disp:dipxColumn>
					<disp:dipxColumn sortable="true" style="text-align:center;"
						 
						property="vehicleNo" title="Vehicle#"></disp:dipxColumn>
					<disp:dipxColumn sortable="true" style="text-align:center;"
						 
						property="driverName" title="Driver"></disp:dipxColumn>
				
					<disp:dipxColumn property="trip_time" style="text-align:center;"
						title="Trip Time"></disp:dipxColumn>
					<disp:dipxColumn property="trip_log" style="text-align:center;"
						title="Shift"></disp:dipxColumn>


				</disp:dipxTable>

				<disp:dipxTable id="inner-stopped" styleClass="inner displaytag"
					property="stoppedTrips" type="inner" parentProperty="stoppedCount">
					<disp:dipxColumn property="trip_code" style="text-align:center;"
						href="javascript:showTripReport('${row.id }');" title="Trip Code"></disp:dipxColumn>
				<disp:dipxColumn sortable="true" style="text-align:center;"
						 
						property="vehicleNo" title="Vehicle#"></disp:dipxColumn>
					<disp:dipxColumn sortable="true" style="text-align:center;"
						 
						property="driverName" title="Driver"></disp:dipxColumn>
					<disp:dipxColumn property="trip_time" style="text-align:center;"
						title="Trip Time"></disp:dipxColumn>
					<disp:dipxColumn property="distanceDouble" style="text-align:right;"  format="{0,number,0.00}&nbsp;Km." 
						title="Distance"></disp:dipxColumn>
					<disp:dipxColumn property="travelTimeInDate"
						format="{0,time,HH:mm:ss }" style="text-align:center;"
						title="Travel Time"></disp:dipxColumn>

				</disp:dipxTable>
				<disp:DispxSummaryRow>
					<td><b>Total</b></td>
					<disp:dipxColumn property="totalCount"
						style="text-align:center; font-weight:bold;"></disp:dipxColumn>
					<disp:dipxColumn property="downloadedCount"
						style="text-align:center; font-weight:bold;"></disp:dipxColumn>
						<disp:dipxColumn property="initialCount"
						style="text-align:center; font-weight:bold;"></disp:dipxColumn>
					<disp:dipxColumn property="startedCount"
						style="text-align:center; font-weight:bold;"></disp:dipxColumn>
					<disp:dipxColumn property="stoppedCount"
						style="text-align:center; font-weight:bold;"></disp:dipxColumn>
					<disp:dipxColumn property="pendingCount"
						style="text-align:center; font-weight:bold;"></disp:dipxColumn>
				</disp:DispxSummaryRow>
			</disp:dipxTable>
			<%@include file="Footer.jsp"%>
			<%
				} catch (Exception e) {
					System.out.print("message :" + e);
				}
			%>
		</div>
	</div>

</body>
</html>
