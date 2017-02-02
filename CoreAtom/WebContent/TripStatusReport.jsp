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
<%@page import="com.agiledge.atom.dao.*"%>
<%@page import="com.agiledge.atom.dto.*"%>
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
<title>Trip Status Report</title>

<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dispx.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">
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
    xmlHttp=GetXmlHttpObject();
    if (xmlHttp==null)
    {
        alert ("Browser does not support HTTP Request");
        return
    }                    
    xmlHttp.onreadystatechange=setLogTime	;
    xmlHttp.open("GET",url,true)             ;   
    xmlHttp.send(null);
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
    xmlHttp=GetXmlHttpObject();
    if (xmlHttp==null)
    {
        alert ("Browser does not support HTTP Request");
        return
    }                    
    xmlHttp.onreadystatechange=setLogTime	;
    xmlHttp.open("GET",url,true) ;               
    xmlHttp.send(null);
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
		
			<%
				try {

				
					String fromDate = request.getParameter("fromDate");
					String toDate = request.getParameter("toDate");
					String siteID = request.getParameter("siteId");
					String tripMode = request.getParameter("tripMode");
					String tripstatus  = request.getParameter("tripstatus");
					String tripTime= request.getParameter("tripTime");
					tripMode = tripMode == null ? "ALL" : tripMode;
					tripTime= tripTime == null ? "ALL" : tripTime;
					
					
					String[] tripTimes = request.getParameterValues("tripTime");
					if (tripTimes == null) {
						String[] array = (String[]) request
								.getAttribute("tripTime");
						tripTimes = array;

					}
					System.out.println("...............");

					/* 	String logTimeIn = request.getParameter("shiftInTime");
						String logTimeOut = request.getParameter("shiftOutTime");	*/	 		
					String fname1 = ("TripStatusReport :")
							.concat(new Date().toString()).concat(".csv");
					String fname2 = ("TripStatusReport :").concat(new Date().toString())
							.concat(".xls");
					String fname3 = ("TripStatusReport:").concat(new Date().toString())
				
							
							.concat(".xml");
					long empid = 0;
					String employeeId = OtherFunctions.checkUser(session);

					empid = Long.parseLong(employeeId);
			%>

			<%@include file="Header.jsp"%>
			<div>
				<form name="form1" action="TripStatusReport.jsp"
					method="POST" onsubmit="return validate()">
					<table align="left">
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
							<td align="right">Status</td>
							<td><select name="tripstatus" id="tripstatus">
									<%						
									
									/* Open
									Sent for TC approval
									Sent for TM approval
									Approved by Transport Manager
									Rejected by Transport Co-ordinator
									Rejected by Vendor */
									
									String type[] = { "ALL","Open","Rejected by Vendor", "Sent for TC approval", "Rejected by Transport Co-ordinator" ,"Sent for TM approval",  "Approved by Transport Manager" };
											for (int i = 0; i < type.length; i++) {
												String typestatusSelect = "";
												if (tripMode.equalsIgnoreCase(type[i])) {
													typestatusSelect = "selected";
												}
									%>
									<option <%=typestatusSelect%> value="<%=type[i]%>"><%=type[i]%></option>
									<%
										}
									%>

							</select></td>

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
			<h2 align="center">Trip Status Report</h2>
			<hr />
			
			
				<%
	   fromDate = request.getParameter("fromDate");
		toDate = request.getParameter("toDate");
			ArrayList<VehicleBasedTripDto> dtoList =null;
			try{
			
				dtoList = new  VehicleBasedTripDao().getTripStatus( siteID,fromDate, toDate, tripMode,tripTime,tripstatus);
			
			}catch(Exception e) {
				System.out.println(" Error in jsp "+ e);
			} 
			VehicleBasedTripDto dto = new VehicleBasedTripDto();
		 	 
		if(dtoList!=null&&dtoList.size()>0) {
			TotalTableDecorator grandTotals = new TotalTableDecorator();
			pageContext.getRequest().setAttribute("tableDecor", grandTotals);
			 
		%>
		<p align="center" >
		<display:table class="alternateColor" name="<%=dtoList%>" id="row"  style="text-align:center  ;width:90%;"
			export="true" defaultsort="1" defaultorder="descending" pagesize="50" >
				<display:column style="align:center" property="tripDate" title="TripDate"
				sortable="true" headerClass="sortable" />
				<display:column property="tripCode" title="TripCode"
				sortable="true" headerClass="sortable" /> 
				<display:column property="tripLog" title="TripLog"
				sortable="true" headerClass="sortable" />
				<display:column property="tripTime" title="TripTime" sortable="true"
				headerClass="sortable" />
				<display:column property="type" title="VehicleTypeAsPerTool" sortable="true"
				headerClass="sortable" />
				<display:column property="vehicle" title="Vehicle Assigned" sortable="true"
				headerClass="sortable" />
				<display:column property="tripAPL" title="LOCATION" sortable="true"
				headerClass="sortable" />
				<display:column  property="approvalStatus"  title="CurrentStatus"   sortable="true"
				headerClass="sortable"  />
				<!-- Among vendors only Ambrish should able to see the bill Amount -->
				<%if(session.getAttribute("user").toString().equalsIgnoreCase("3583")   ||   session.getAttribute("roleId").toString().equalsIgnoreCase("2") ||  session.getAttribute("roleId").toString().equalsIgnoreCase("5")){ %>>
				<display:column  property="tripRate"  title="TripAmount"   sortable="true"
				headerClass="sortable"  />
				<display:column  property="escortRate"  title="EscortAmount"   sortable="true"
				headerClass="sortable"  />
				
				<% }%>
			   
			<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
			<display:setProperty name="export.excel.filename" value="<%=fname2%>" />
			<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
		</display:table>
		
<%} else { 
			System.out.println(" dto list is null ");
		}
		%>				</p>
			<%@include file="Footer.jsp"%>
			<%
				} catch (Exception e) {
					System.out.print("message :" + e);
				}
			%>
		</div>
	

</body>
</html>
