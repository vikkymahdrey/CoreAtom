<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="java.util.*"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="com.agiledge.atom.dao.*"%>
<%@page import="com.agiledge.atom.dto.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Panic Report</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<link rel="stylesheet" type="text/css" style="center "
	href="css/displaytag.css" />
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
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
            	var siteId = document.getElementById("siteId").value;
            	if (siteId =="ALL") {
            		alert("Please select site!");
        			return false;
               
            	}
            	else{
            		return true;
            	}
                
              
                
            }
        </script>




</head>
<body>
	<div id="body" align="justify">
		<div class="content" align="justify">
			<%
				try {

				
					
					String siteID = request.getParameter("siteId");
					String fromDate = request.getParameter("fromDate");
					String toDate = request.getParameter("toDate");
					
							 		
					String fname1 = ("PanicReport :")
							.concat(new Date().toString()).concat(".csv");
					String fname2 = ("PanicReport :").concat(new Date().toString())
							.concat(".xls");
					String fname3 = ("PanicReport:").concat(new Date().toString())		
							.concat(".xml");
					long empid = 0;
					String employeeId = OtherFunctions.checkUser(session);

					empid = Long.parseLong(employeeId);
			%>

			<%@include file="Header.jsp"%>
			<div>
		
				<form name="form1" action="PanicTripReport.jsp"
					method="POST" onsubmit="return validate();">
					<table>
		
						<tr>
							<td align="center" >Choose Site &nbsp;<select name="siteId" id="siteId" >
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
									<option  value="<%=siteDto.getId()%>"><%=siteDto.getName()%></option>
									<%
										}
									%>
							</select>

                          </td>
                          
                           <td>From Date
                          <input name="fromDate"
								id="fromDate" type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=fromDate!=null&&fromDate.trim().equals("")==false?fromDate:"" %>" />
                           </td>
                          
                          <td>To Date
                          <input name="toDate" id="toDate"
								type="text" size="6" readonly="readonly"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=toDate!=null&&toDate.trim().equals("")==false?toDate:"" %>" />		
                          
                          </td>
                          
                          
                          
							<td><input type="submit" class="formbutton" value="Generate Report" /></td>
						
						</tr>
					</table>
				</form>
			</div>
		
	<% if(siteID!=null){ %>	
			
			
				<%
	 
			ArrayList<PanicDto> dtoList =null;
			try{
			
				dtoList = new  PanicDao().GetPanicTrips(site,fromDate,toDate);
			
			}catch(Exception e) {
				System.out.println(" Error in jsp "+ e);
			} 
			
		if(dtoList!=null&&dtoList.size()>0) {
			TotalTableDecorator grandTotals = new TotalTableDecorator();
			pageContext.getRequest().setAttribute("tableDecor", grandTotals);
			 
		%>
		<h2 align="center">PANIC REPORT</h2>
	

		<display:table class="alternateColor" name="<%=dtoList%>" id="row"   style="text-align:center ;"
			export="true" defaultsort="1" defaultorder="descending" pagesize="50"  >
				<display:column property="tripId" title="TripID"
				sortable="true" headerClass="sortable" style="text-align:center"/>
				<display:column property="vehicle" title="Vehicle NO"
				sortable="true" headerClass="sortable" style="text-align:center"/>
				<display:column property="activatedby" title="ActivatedBy"
				sortable="true" headerClass="sortable" /> 
				<display:column property="actiontime" title="Activated Time"
				sortable="true" headerClass="sortable" />
				<display:column property="alarmCause" title="AlarmCause" sortable="true"
				headerClass="sortable" />
				<display:column property="primaryActiontakenByName" title="PrimaryActiontakenBy" sortable="true"
				headerClass="sortable" />	
				<display:column property="primaryAction" title="PrimaryAction" sortable="true"
				headerClass="sortable" />	
				<display:column property="curStatus" title="CurentStatus" sortable="true"
				headerClass="sortable" />
			    <display:column property="approvedBy" title="ApprovedBy" sortable="true"
				headerClass="sortable" />
			
			<display:setProperty  name="export.csv.filename" value="<%=fname1%>" />
			<display:setProperty name="export.excel.filename" value="<%=fname2%>" />
			<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
			
			
		</display:table>
      <%}
		else { 
				out.print("PANIC NOT ACTIVATED");
			System.out.println(" dto list is null ");
		}
	}
		%>	</div>
			
			<%@include file="Footer.jsp"%>
			<%
				} catch (Exception e) {
					System.out.print("message :" + e);
				}
				
			%>
		</div>


</body>
</html>
