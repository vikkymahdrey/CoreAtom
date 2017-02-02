



<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.reports.dto.OtaOtdDto"%>
<%@page import="com.agiledge.atom.reports.dto.OnTimeTripCountDto"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.reports.dto.TripSheetNoShowCountReportDto"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.agiledge.atom.reports.TripSheetNoShowCountReportHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>OTA & OTD Report</title>

<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">

function resetFields()
{
	$("input[type=hidden]").val("");
}
        function showPopup(url) {

        var params="toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
        size="height=450,width=520,top=200,left=300,"+params;
        if(url=="LandMarkSearch.jsp")
            {
                size="height=450,width=600,top=200,left=300," + params;
            }
        else if(url=="SupervisorSearch1.jsp" || url=="SupervisorSearch2.jsp"  )
               {
                   size="height=450,width=700,top=200,left=300,"+params;
               }
        else if(url=="termsAndConditions.html")
            {
                   size="height=450,width=520,top=200,left=300,"+params;
            }

        newwindow=window.open(url,'name',size);
        

         if (window.focus) {newwindow.focus();}
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
        </script>



</head>
<body>
	<%
	String fname1=("OTAOTDReport :").concat(new Date().toString()).concat(".csv");
	String fname2=("OTAOTDReport :").concat(new Date().toString()).concat(".xls");
	String fname3=("OTAOTDReport :").concat(new Date().toString()).concat(".xml");
		String monthParam = request.getParameter("month");
		String siteParam = request.getParameter("site");
		String shiftParam = request.getParameter("shift");
		String onTimeStatus = request.getParameter("onTimeStatus");
		String fromDate = request.getParameter("fromDate");
		String toDate= request.getParameter("toDate");
		onTimeStatus=onTimeStatus==null?"":onTimeStatus;
		String cabTypeParam = "";
		if (request.getParameterMap().containsKey("cabType")) {
			cabTypeParam = request.getParameter("cabType");
		}

		TripSheetNoShowCountReportHelper reportHelper = new TripSheetNoShowCountReportHelper();
		List<OtaOtdDto> dtoList = null;
		if (monthParam != null && siteParam != null && shiftParam != null
				&& !monthParam.equals("") && !siteParam.equals("")
				&& !shiftParam.equals("")) {

			dtoList = reportHelper.getOtaOtdReportDetails(monthParam,
					siteParam, shiftParam, cabTypeParam,onTimeStatus, fromDate,toDate);

		}

		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);		
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">


			<form name="form" action="display_OTA_OTD.jsp">




				<table>
					<tr>
						<td align="right"><input type="hidden" name="month"
							value="<%=request.getParameter("month")==null?"":request.getParameter("month") %>" />
							<input type="hidden" name="site"
							value="<%=request.getParameter("site")==null?"":request.getParameter("site") %>" />
							<input type="hidden" name="shift"
							value="<%=request.getParameter("shift")==null?"":request.getParameter("shift") %>" />
							<input type="hidden" name="onTimeStatus"
							value="<%=request.getParameter("onTimeStatus")==null?"":request.getParameter("onTimeStatus") %>" />


							From</td>
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




					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" class="formbutton"
							value="Generate Report" /> <input type="reset"
							class="formbutton" value="Reset" onclick="resetFields();" /></td>
					</tr>
				</table>

			</form>

			<h2 align="center">OTA & OTD Report Details</h2>
			<hr />
			<%
			if(shiftParam.equals("IN")) {
			pageContext.setAttribute("timeTitle", "Reporting Time");
			} else {
				pageContext.setAttribute("timeTitle", "Departure Time");
			}
			%>

			<display:table class="alternateColor" name="<%=dtoList%>" id="row"
				export="true" defaultsort="1" defaultorder="descending"
				pagesize="25">
				<display:column property="trip_date" title="Date" sortable="true"
					format="{0,date,dd-MM-yyyy}" headerClass="sortable" />
				<display:column property="tripCode" title="Trip Code"
					sortable="true" headerClass="sortable" />


				<display:column property="shift" title="Log Type" sortable="true"
					headerClass="sortable" />

				<display:column property="cabNo" title="Cab#" sortable="true"
					headerClass="sortable" />

				<display:column property="cabType" title="Cab Type" sortable="true"
					headerClass="sortable" total="true" />

				<display:column property="plannedTime" title="${timeTitle}"
					sortable="true" headerClass="sortable" total="true" />
				<display:column property="actualTime" title="Actual Reporting"
					sortable="true" headerClass="sortable" total="true" />

				<display:column property="onTimeStatus" title="Status"
					sortable="true" headerClass="sortable" total="true" />

				<td><a href="view_tracked_trip.jsp?tripId=">tripId>View</a></td>
				<display:column title="Details">
					<a href="view_tracked_trip.jsp?tripId=${row.tripId }"> View </a>

				</display:column>




				<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
				<display:setProperty name="export.excel.filename"
					value="<%=fname2%>" />
				<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
			</display:table>
			<br />
			<p>
				<input type="Button" name="add" class="formbutton" value="Back"
					onclick="javascript:history.go(-1);" />
			</p>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
