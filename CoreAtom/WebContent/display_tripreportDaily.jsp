<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto" %>
<%@page import="com.agiledge.atom.billingprocess.report.dao.TripBasedBillingReportHelperDao"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Trip Report-Daily</title>
</head>
<body>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://www.nfl.com" prefix="disp" %>
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
		 padding-left:12px;
	}
		div#filterContainer >div > div:FIRST-CHILD {
		display: inline;
		 float: left;
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
        
        $("#trip_date").datepick();
	});
</script>

<script type="text/javascript">
 
function resetFields()
{
	$("input[type=input]").val("");
}
function validate()
{    
    var fromdate=document.getElementById("trip_date").value;
    if(fromdate.length<1)
    {
        alert("Choose From Date");
        return false;
            
    }
    if(todate.length<1)
    {
        alert("Choose To Date");
        return false;
            
    }
     else
    {
        return true;                            
    }
}
        </script>

</head>
<body  >
	<%@include file="Header.jsp"%>
		<%
		String fname1=("TripReport :").concat(new Date().toString()).concat(".csv");
		String fname2=("TripReport :").concat(new Date().toString()).concat(".xls");
		String fname3=("TripReport :").concat(new Date().toString()).concat(".xml");
		String Date = request.getParameter("Date");
		String siteID = request.getParameter("siteId");		
		String  url="display_tripreportDaily.jsp?siteId=" + siteID+"&Date="+Date;
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		
			empid = Long.parseLong(employeeId);
	%>
	<div>
	<form name="form" id="form" action="display_tripreportDaily.jsp"
					method="GET" onsubmit="return validate()" >
					<div id="filterContainer" >
					<div></div><div></div><div></div><div></div>
							<div><div>Choose Site</div>
							<div>
							
							<select name="siteId" id="siteId"  >
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
							<div></div><div></div><div></div><div></div>
							<div>
	 							<div>
	 								<label> Date</label>
	 							</div>
	 							<div>
	 								<label  >
									<input name="Date" id="Date" type="text" size="6" readonly="readonly" class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2008, 12 - 1, 25)}" value="<%=Date!=null&&Date.trim().equals("")==false?Date:"" %>" />
									</label>
								 		
	 								
	 							</div>
 							</div>
 							<div></div><div></div><div></div><div></div>
											<div>
						<div>		
						<br/>
							 		<input type="submit" class="formbutton" value="Generate Report" />
							 			</div>		
							</div>
						</div>
							</form>
							</div>
							<div id="reportDiv" >
			<hr />
			<hr />
			<br />
			<h2 align="center">Trip Report Daily - General Shift</h2>
			<hr />
<hr />
			<%
			ArrayList<TripDetailsDto> dtoList =null;
			try{
				dtoList = new TripBasedBillingReportHelperDao ().getTripsDaily(site, Date);	 
				
			}catch(Exception e) {
				System.out.println(" Error in jsp "+ e);
			} 
			TripDetailsDto dto = new TripDetailsDto();
		 	 
		if(dtoList!=null&&dtoList.size()>0) {
			TotalTableDecorator grandTotals = new TotalTableDecorator();
			pageContext.getRequest().setAttribute("tableDecor", grandTotals);
			
		%>
		 			<display:table class="alternateColorInSub" id="row"  style="width:60%; margin-left:20%" name="<%=dtoList %>" export="true" defaultsort="1" defaultorder="descending" decorator="tableDecor" pagesize="350" >
				    <display:column property="trip_date" sortable="true" title="Date" />
				 	<display:column property="trip_code" sortable="true" title="Trip Code" />
				 	<display:column property="vehicleNo"  sortable="true" title="Reg No"  />
				 	<display:column property="vehicle_type" sortable="true" title="Type"/>
				 	<display:column property="trip_time"  sortable="true" title="Time" />
				 	<display:column property="trip_log"  sortable="true" title="IN/OUT" />
				 	<display:column property="isSecurity"  sortable="true" title="Escort" />
				 	<display:column property="empCount"  sortable="true" title="Employee Count" />
				 	<display:column property="empInCount"  sortable="true" title="Boarded Count" />
				 	
				 <display:setProperty name="export.csv.filename" value="<%=fname1%>" />
				<display:setProperty name="export.excel.filename"
					value="<%=fname2%>" />
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