



<%@page import="java.util.Date"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
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
<title>Trip Sheet No Show Report</title>
<script type="text/javascript">
	function showReport(month, site, logType) {

	//	alert(month + ' ' + site + ' ' + logType);
		var params ="month="+month+"&site="+ site + "&shift=" + logType;
		var url="display_OTA_OTD.jsp?" + params;
		window.location=url;
		//+"&cabType
	}
	
	function showReportWithCab(month, site, logType,cabType)
	{
		var params ="month="+month+"&site="+ site + "&shift=" + logType+"&cabType=" +cabType;
		var url="display_OTA_OTD.jsp?" + params;
		window.location=url;
	}
</script>

</head>
<body>
	<%
	String fname1=("OnshowTripReport :").concat(new Date().toString()).concat(".csv");
	String fname2=("OnshowTripReport :").concat(new Date().toString()).concat(".xls");
	String fname3=("OnshowTripReport :").concat(new Date().toString()).concat(".xml");
		String monthParam = request.getParameter("month");
		String yearParam = request.getParameter("year");
		String groupBy = request.getParameter("groupBy");

		TripSheetNoShowCountReportHelper reportHelper = new TripSheetNoShowCountReportHelper();
		List<OnTimeTripCountDto> dtoList = null;
		if (groupBy != null && !groupBy.equals("")) {

			System.out.println("groupby......." + groupBy);
			System.out.println("...month ..."+ monthParam);
			dtoList = reportHelper.getTripOnTimeCount(monthParam,
					yearParam, groupBy);

		} else {
			System.out.println("track.....");
			System.out.println("...month ..."+ monthParam);
			dtoList = reportHelper.getTripOnTimeCount(monthParam,
					yearParam, "site");
		}

		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<h2 align="center">OTA & OTD Report</h2>
			<div>
				<form action="display_OnShowTipCount.jsp">

					<table width="20%">
						<tr>
							<td>Month</td>
							<td>Year</td>
							<td>Group By</td>
							<td></td>
						</tr>
						<tr>
							<td><select name="month" id="month">
							<%
							String selectAll="";
								System.out.println("Month: " +request.getParameter("month"));
							if(request.getParameter("month")==null||request.getParameter("month").equals(""))
							{
								selectAll="selected";
							}
							%>
									<option <%=selectAll %> value="">All</option>
									<%
										int month = 0;
										Calendar cal = Calendar.getInstance();
										if (monthParam != null && !monthParam.equals("")) {
											month = Integer.parseInt(monthParam);
										} else {

											month = cal.get(cal.MONTH)+1;
										}
										int curYear = cal.get(cal.YEAR);
										int year = cal.get(cal.YEAR);
										if (yearParam != null && !yearParam.equals("")) {
											year = Integer.parseInt(yearParam);
										} else {
											year = cal.get(cal.YEAR);
										}
										String selected = "";
										String[] monthLabel = { "January", "February", "March", "April",
												"May", "June", "July", "August", "September", "October",
												"November", "December" };
										for (int i = 1; i <= 12; i++) {
											if (i == month) {
												if(selectAll.equals("selected")==false)
												selected = "selected";
											} else {
												selected = "";
											}
											out.print("<option " + selected + " value=" + i + ">"
													+ monthLabel[i - 1] + "</option>");
										}
									%>
							</select></td>
							<td><select name="year" id="year">
									<%
										int i = cal.get(cal.YEAR) - 5;
										for (; i <= cal.get(cal.YEAR); i++) {
											String yearSelect = "";
											if (i == year) {
												yearSelect = "selected";
											}
											out.print("<option  " + yearSelect + "  value=" + i + ">" + i
													+ "</option>");
										}
									%>

							</select></td>
							<td><select name="groupBy">
									<option value="Site">Site</option>
									<option value="Vehicle Type">Vehicle Type</option>
							</select></td>
							<td><input type="submit" class="formbutton" value="Generate" />
							</td>


						</tr>
					</table>
				</form>
			</div>
		</div>

		<hr />
		<%
			 TotalTableDecorator grandTotals=new TotalTableDecorator();
		
			pageContext.getRequest().setAttribute("tableDecor", grandTotals);
			
			%>

		<display:table class="alternateColor" name="<%=dtoList%>" id="row"
			export="true" defaultsort="1" defaultorder="descending"
			decorator="tableDecor">
			<display:column property="month" title="Month" sortable="true"
				headerClass="sortable" group="1" sortProperty="monthNo" />
			<display:column property="site" title="Site" sortable="true"
				headerClass="sortable" />

			<%
				if (groupBy != null && groupBy.equals("Vehicle Type")) {
			%>
			<display:column property="vehicleType" title="Vehicle Type"
				sortable="true" headerClass="sortable" />
			<%
				}
			%>
			<display:column property="logType" title="Log" sortable="true"
				headerClass="sortable" />

			<display:column property="onTimeCount" title="On Time "
				sortable="true" format="{0,number,0}" headerClass="sortable"
				total="true" />
			<display:column property="offTimeCount" title="Late" sortable="true"
				format="{0,number,0}" headerClass="sortable" total="true" />
			<display:column property="totalCount" title="Total" sortable="true"
				format="{0,number,0}" headerClass="sortable" total="true" />


			<display:column title="Details">
				<%
				if (groupBy != null && groupBy.equals("Vehicle Type")) {
			%>
				<a
					href="javascript:showReportWithCab('${row.month }','${row.site }','${row.logType }','${row.vehicleType }')">
					View </a>

				<%}else { %>
				<a
					href="javascript:showReport('${row.month }','${row.site }','${row.logType }')">
					View </a>
				<%} %>
			</display:column>
			
		<display:setProperty name="export.csv.filename" value="<%=fname1%>"/>
		<display:setProperty name="export.excel.filename" value="<%=fname2%>"/>					
        <display:setProperty name="export.xml.filename" value="<%=fname3%>"/>   
		</display:table>
		<%@include file="Footer.jsp"%>
	</div>
	</div>

</body>
</html>
