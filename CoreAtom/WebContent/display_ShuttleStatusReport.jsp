<%@page import="java.util.*"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.*"%>
<%@page import="com.agiledge.atom.dao.*"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="org.displaytag.decorator.MultilevelTotalTableDecorator"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.reports.dto.TripSheetNoShowCountReportDto"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.nfl.com" prefix="disp"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Vehicle Based Reports</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<% request.setAttribute("contextPath", request.getContextPath()); %>
<link rel="stylesheet" type="text/css" href="${contextPath}/css/displaytag.css" />
<style type="text/css">
	div#filterContainer {
		height: auto;
		background-colore : red;
		width: 100%;
		height: 50px;
	}
	div#filterContainer > div {
		display: inline;
		 float: left;
		 padding-right:20px;
	}
		div#filterContainer >div > div:FIRST-CHILD {
		display: inline;
		 float: left;
		 padding-right:20px;
		 margin-bottom: 5px; 
		 margin-top: 10px;
	}
	
	div#reportDiv {
		margin-top:25px;
	}
</style>

<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript" src="js/dispx.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
        
        $("#fromDate").datepick();
        $("#toDate").datepick();

        
	});
</script>

<script type="text/javascript">

 
   function validate()
{
	var siteId=document.getElementById("siteId").value;   
   	var vehicle=document.getElementById("chosenregNo").value;  
    var fromdate=document.getElementById("fromDate").value;
    var todate=document.getElementById("toDate").value;

   
    
    if(siteId==0)
    {
        alert("Choose site");
        return false;
    }
    
    else if( vehicle=="" || vehicle==null)
    {
        alert("Choose Vehicle");
                return false;
    }
    

    else if(fromdate.length<1)
    {
        alert("Choose From Date");
      //  date.focus();
        return false;
    }    
    else if(todate.length<1)
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

       
            
/*  function refreshy()
 {
  	
  	var site=$("select[name=siteId]").val();
 	window.location.href ="display_ShuttleStatusReport.jsp?siteId="+site;
  	}   
 */
function getEmployees(tripId)
{
	try{ 
	
	var url="vehicle_employee.jsp?header=false&tripId=" +tripId;
	var size = "height=600,width=1200,top=100,left=60,toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var newwindow=window.open(url,size);
	if (window.focus) {
		newwindow.focus();
	}
	}catch(e) {
		alert(e);
	}
}
        </script>

</head>

<%
	String fname1=("VehicleBasedReport :").concat(new Date().toString()).concat(".csv");
	String fname2=("VehicleBasedReport :").concat(new Date().toString()).concat(".xls");
	String fname3=("VehicleBasedReport :").concat(new Date().toString()).concat(".xml");
	
	String siteID=request.getParameter("siteId");
	String fromDate = request.getParameter("fromDate");
	String toDate = request.getParameter("toDate");
	String vehicle = request.getParameter("chosenregNo");
		
	
    long empid = 0;
	String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
	 
	%>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content" align="center">
	   		<h2 align="center">Vehicle Based TripDetails</h2>
			<hr /><div>
			<form name="form1" method="POST" action="display_ShuttleStatusReport.jsp" id="vehiclereportform"
		onsubmit="return validate();" >
					<div id="filterContainer" style="margin-left: 20%" >
							<div  ><div>Choose Site</div>
							<div>
							
							<select name="siteId" id="siteId" onchange="refreshy();" >
								<option value="0">Select Site</option>
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
							</select></div></div>
							
							
 														<div>
							<div>Vehicle
							</div>
							<div id="regNo"><select  
							name="chosenregNo" id="chosenregNo" >
								<option value='all'>All</option>
							<%
							
						ArrayList<VehicleDto> vehicleInSiteDtos = null;
							try{
							vehicleInSiteDtos = new VehicleDao().getvehiclebyType("1");
							if(vehicleInSiteDtos!=null&&vehicleInSiteDtos.size()>0) {
								for (VehicleDto vdto : vehicleInSiteDtos) {
									String vehicleSelect="";
									try {
									
									if(request.getParameter("chosenregNo").equals(String.valueOf(vdto.getId()))) {
										vehicleSelect="selected";
														} }catch(Exception e) {
										vehicleSelect="";
									}
									 
								
							%>
								<option <%=vehicleSelect %> value='<%=vdto.getId() %>'><%=vdto.getVehicleNo()%></option>
							<%
								}
							}
							}catch(Exception e) {} %>
							</select>
 							</div>
 							</div>
 							<div>
	 							<div>
	 								<label> From</label>
	 							</div>
	 							<div>
	 								<input name="fromDate"
								id="fromDate" type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=fromDate!=null&&fromDate.trim().equals("")==false?fromDate:"" %>" />
								 		
	 								
	 							</div>
 							</div>
							<div  > 
							<div >		  
  									
									
									<label>To</label>
		 								
  							</div>
 							<div>
 																	 		
		 							<input name="toDate" id="toDate"
								type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=toDate!=null&&toDate.trim().equals("")==false?toDate:"" %>" />									
 								</div>
 		
						</div>
						<div style="margin-top:29px;">
							 
							 
							 	
							 		<input type="submit" class="formbutton" value="Generate" />
							</div></div>
							 
					
							

				</form>
			</div> 
	    <div id="reportDiv" >
			<hr />
		
	  	<%
	   fromDate = request.getParameter("fromDate");
		toDate = request.getParameter("toDate");
			ArrayList<VehicleBasedTripDto> dtoList =null;
			try{
				dtoList = new  VehicleBasedTripDao().getShuttleTripdetails( siteID,fromDate, toDate, vehicle);	
			
			}catch(Exception e) {
				System.out.println(" Error in jsp "+ e);
			} 
		 	 
		if(dtoList!=null&&dtoList.size()>0) {
			TotalTableDecorator grandTotals = new TotalTableDecorator();
			pageContext.getRequest().setAttribute("tableDecor", grandTotals);
			 
		%>
		<display:table class="alternateColor" name="<%=dtoList%>" id="row"  
			export="true" defaultsort="1" defaultorder="descending" pagesize="50">
				
				
				<display:column property="tripDate" title="TripDate"
				sortable="true" headerClass="sortable" />
				<display:column property="vehicle" title="Bus RegNo"
				sortable="true" headerClass="sortable" />
				<display:column property="route" title="Route Name"
				sortable="true" headerClass="sortable" />
				<display:column property="tripLog" title="TripLog"
				sortable="true" headerClass="sortable" />
				<display:column property="tripTime" title="TripTime" sortable="true"
				headerClass="sortable" />
				<display:column property="starttime" title="StartTime" sortable="true"
				headerClass="sortable" />
				<display:column property="stopTime" title="StopTime" sortable="true"
				headerClass="sortable" />
				<display:column property="actemp" title="Total Emp" sortable="true"
				headerClass="sortable" />
				<%-- <display:column property="inemp" title="Boarded Emp" sortable="true"
				headerClass="sortable" /> --%>
			<%-- 	<display:column title="Emp Details" headerClass="sortable"  >
			
				<a href="javascript:getEmployees('${row.tripId }')" >View </a>
				
			</display:column>
			    --%>
			   
			<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
			<display:setProperty name="export.excel.filename" value="<%=fname2%>" />
			<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
		</display:table>
<%} else { 
			System.out.println(" dto list is null ");
		}
		%>	</div>
<%@include file="Footer.jsp"%>	
</div>
	</div>
</body>
</html>
            