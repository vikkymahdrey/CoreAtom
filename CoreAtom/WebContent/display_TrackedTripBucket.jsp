<%-- 
    Document   : routeSample
    Created on : Oct 25, 2012, 1:05:50 PM
    Author     : muhammad
--%>

<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.reports.TripBucketReportHelper"%>
<%@page import="com.agiledge.atom.reports.dto.TripBucketDto"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Trip Bucket</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">        
            $(document).ready(function()
            {       getTripTime();                                                                 
                $("#fromDate").datepick();  
                $("#toDate").datepick();  
                
            });     
        </script>
<script type="text/javascript">
            function validate()
            {
                var mod=document.getElementById("tripMode").value;   
                var time=document.getElementById("tripTime").value;  
                var fromDate=document.getElementById("fromDate").value;  
                var toDate=document.getElementById("toDate").value;  
                if(fromDate.length<1)
                {
                    alert("Choose From Date");
                  //  date.focus();
                    return false;
                        
                }else if(toDate.length<1)
                {
                    alert("Choose To Date");
                  //  date.focus();
                    return false;
                        
                }
                else if(mod=="ALL"&&time!="ALL")
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
                	
                	tripTimeId.innerHTML='<select name="tripTime" id="tripTime"> <option value="ALL" >ALL</option></select>';
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
                    tripTimeId.innerHTML='<select name="tripTime" id="tripTime"> <option value="ALL" >ALL</option>'+returnText+'</select>';                                             
                }
            }
            
            function load(siteId,tripDate,tripTime,tripMode,approvalStatus,value)
        	{
        	if(value>0)
        		{
        		$("#hiddenForm-siteId").val(siteId);
        		$("#hiddenForm-tripDate").val(tripDate);
        	 
        		$("#hiddenForm-tripTime").val(tripTime);
        		$("#hiddenForm-tripMode").val(tripMode);
        		$("#hiddenForm-approvalStatusParam").val(approvalStatus);
        		
        		  document.hiddenForm.action = "transadmin_trackedTrips.jsp";
          		document.hiddenForm.submit();
        		}
          		 
          		return false;  
        	 
        		
        		 
        	}
        </script>

</head>
<body>
	<%
		List<SiteDto> siteDtoList = new SiteDao().getSites();
		//ArrayList<LogTimeDto> logtimeDtoList = new LogTimeDao().getAllGeneralLogtime();
	%>
	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
		
		String fname1=("Trip Bucket :").concat(new Date().toString()).concat(".csv");
		String fname2=("Trip Bucket :").concat(new Date().toString()).concat(".xls");
		String fname3=("Trip Bucket :").concat(new Date().toString()).concat(".xml");
		
		String tripMode= request.getParameter("tripMode");
		String fromDate= request.getParameter("fromDate");
		String toDate= request.getParameter("toDate");
		String siteId= request.getParameter("siteId");
		String tripTime= request.getParameter("tripTime");
		siteId=siteId==null?"":siteId;
		fromDate=fromDate==null?"":fromDate;
		toDate=toDate==null?"":toDate;
		tripTime=tripTime==null?"":tripTime;
		tripMode=tripMode==null?"":tripMode;
		
		
		 List <TripBucketDto> dtoList= new TripBucketReportHelper().getTripBucket(fromDate, toDate, siteId, tripTime, tripMode  );
	%>
	<%@include file="Header.jsp"%>
	<hr />
	<div id="body">
		<div class="content">
			<h3>Trip Bucket</h3>
			<form name="form1" action="display_TrackedTripBucket.jsp" method="POST"
				onsubmit="return validate()">
				<table>
					<tr>
						<td align="right">Choose Site</td>
						<td><select name="siteId" id="siteId"  onchange="getTripTime()">
								<%
									for (SiteDto siteDto : siteDtoList) {
										String site = (request.getSession().getAttribute("site") == null || request
												.getSession().getAttribute("site").toString().trim()
												.equals("")) ? "" : request.getSession()
												.getAttribute("site").toString().trim();
										site=siteId==""?site:siteId;
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


						<td align="right">From Date</td>
						<td><input name="fromDate" id="fromDate" type="text" size="6" value="<%=fromDate %>"
						readonly="readonly"	class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}" /></td>

					<td align="right">To Date</td>
						<td><input name="toDate" id="toDate" type="text" size="6"  value="<%=toDate %>"
						readonly="readonly"	class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}" /></td>


						<td align="right">Log In/Out</td>
						<td><select name="tripMode" id="tripMode"
							onchange="getTripTime()">
							<%
								
							   String timeModes[]={"ALL","IN","OUT"};
							   for(String mod: timeModes)
							   {
								   String logSelect="";
								   if(tripMode.equals(mod))
								   {
									   logSelect="selected";
								   }
							%>
								<option <%=logSelect%> value="<%=mod%>"><%=mod%></option>
							<%} %>
								 
						</select></td>
						<td align="right">Disabled Shift</td>
						<td><input type="checkbox" name="tripType" id="tripType"
							value="enable" onclick="getTripTypeTime()"></td>

						<td align="right">Shift</td>
						<td id="tripTimeId"><select name="tripTime" id="tripTime">
								<option value="ALL">ALL</option>
						</select>
						<input type="hidden" id="tripTimeSelected" name="tripTimeSelected" value="<%=tripTime %>" />
						</td>

						<td>&nbsp;</td>
						<td><input type="submit" class="formbutton" value="Show" /></td>
					</tr>
				</table>
				<br/>
				<hr/>
				<display:table class="alternateColor" name="<%=dtoList%>" id="row"
				export="true" defaultsort="1" defaultorder="descending"
				pagesize="50">
				<display:column property="tripDate" title="Date" 
					format="{0,date,dd/MM/yyyy}" sortable="true" headerClass="sortable"  sortProperty="tripDate" />
				 
				<display:column property="tripLog" title="Log"
					sortable="true" headerClass="sortable" />
			
				 
				<display:column property="tripTime" title="Time" sortable="true"
					headerClass="sortable" />
				 
					 
      			<display:column style="text-align:left" title="Open" sortable="true" >
      				<a onclick="load('${row.siteId }','${row.tripDate }','${row.tripTime }','${row.tripLog }','Open',${row.openCount })" > ${row.openCount }</a>
      			</display:column>
 
 				
 
				 <display:column title="Sent For TC" sortable="true" >
      				<a onclick="load('${row.siteId }','${row.tripDate }','${row.tripTime }','${row.tripLog }','Sent for TC approval',${row.stcaCount })" > ${row.stcaCount }</a>
      			</display:column>
      			<display:column title="Rejected By TC" sortable="true" >
      				  <a onclick="load('${row.siteId }','${row.tripDate }','${row.tripTime }','${row.tripLog }','Rejected by Transport Co-ordinator',${row.rtcCount })" > ${row.rtcCount }</a>
      			</display:column>
				
			    <display:column title="Sent For TM" sortable="true" >
      				<a onclick="load('${row.siteId }','${row.tripDate }','${row.tripTime }','${row.tripLog }','Sent for TM approval',${row.stmaCount })" > ${row.stmaCount }</a>
      			</display:column>
      			 <display:column title="Rejected By TM" sortable="true" >
      				<a onclick="load('${row.siteId }','${row.tripDate }','${row.tripTime }','${row.tripLog }','Rejected by Transport Manager',${row.rtmCount })" > ${row.rtmCount }</a>
      			</display:column> 
			 
			  <display:column title="Approved By TM" sortable="true" >
      				<a onclick="load('${row.siteId }','${row.tripDate }','${row.tripTime }','${row.tripLog }','Approved by Transport Manager',${row.atmCount })" >${row.atmCount }</a>
      			</display:column>
			 	 
		 
				<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
				<display:setProperty name="export.excel.filename"
					value="<%=fname2%>" />
				<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
			</display:table>
				
			</form>
			<form name="hiddenForm" id="hiddenForm">
				<input type="hidden" name="siteId" id="hiddenForm-siteId" />
				
				<input type="hidden" name="tripDate" id="hiddenForm-tripDate" />
				<input type="hidden" name="tripTime" id="hiddenForm-tripTime" />
				<input type="hidden" name="tripMode" id="hiddenForm-tripMode" />
				<input type="hidden" name="approvalStatusParam" id="hiddenForm-approvalStatusParam" />
				
				
			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
