



<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="org.displaytag.decorator.MultilevelTotalTableDecorator"%>
<%@page import="com.agiledge.atom.reports.dto.TripBillDto"%>
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
<title>Trip Billing Report</title>
<script type="text/javascript">
	function showReport(month, site, logType) {

		//	alert(month + ' ' + site + ' ' + logType);
		var params = "month=" + month + "&site=" + site + "&shift=" + logType;
		var url = "display_OTA_OTD.jsp?" + params;
		window.location = url;
		//+"&cabType
	}

	function showReportWithCab(month, site, logType, cabType) {
		var params = "month=" + month + "&site=" + site + "&shift=" + logType
				+ "&cabType=" + cabType;
		var url = "display_OTA_OTD.jsp?" + params;
		window.location = url;
	}
</script>

</head>
<body>
	<%
	String fname1=("TripBillReport :").concat(new Date().toString()).concat(".csv");
	String fname2=("TripBillReport :").concat(new Date().toString()).concat(".xls");
	String fname3=("tripBillReport :").concat(new Date().toString()).concat(".xml");
	
		String monthParam = request.getParameter("month");
		String yearParam = request.getParameter("year");
		String siteParam = request.getParameter("siteId");

		TripSheetNoShowCountReportHelper reportHelper = new TripSheetNoShowCountReportHelper();
		List<TripBillDto> dtoList = null;
		if (monthParam != null && yearParam != null &&siteParam!=null
				&& !yearParam.equals("") && !monthParam.equals("")
				&& !siteParam.equals("")  ) {

			dtoList = reportHelper.getBillingReportDetails(monthParam,
					yearParam, siteParam);

		} else {
			Calendar cal1 = Calendar.getInstance();
			yearParam = "" + cal1.get(cal1.YEAR);
			dtoList = reportHelper.getBillingReportDetails("", yearParam,siteParam);

		}

		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<h2 align="center">Trip Billing Report</h2>
			<div>
				<form action="display_TripBillingSummary.jsp">

					<table width="20%">
						<tr>
							<td>Site</td>
							<td>Month</td>
							<td>Year</td>
							<td></td>

						</tr>
						<tr>
							<td><select name="siteId" id="siteId">
									<%
									
								List<SiteDto> siteDtoList = new SiteDao().getSites();
									String site=(request.getSession().getAttribute("site")==null||request.getSession().getAttribute("site").toString().trim().equals(""))?"" :request.getSession().getAttribute("site").toString().trim();
									if(siteParam!=null&&siteParam.equals("")==false)
									{
										site=siteParam;
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
			export="true"   defaultorder="descending"
			decorator="tableDecor">

			<display:column property="month" title="Month" sortable="true"
				headerClass="sortable" group="1" sortProperty="monthNo" />
			<display:column property="site" title="Site" sortable="true"
				headerClass="sortable" group="2" />
			<display:column property="type" title="Vehicle Type" sortable="true"
				headerClass="sortable" />


			<display:column property="actualTrips" title="Actual Trips"
				headerClass="r" class="r" format="{0,number,0}" total="true" />

			<display:column property="submittedTrips" title="Submitted Trips"
				headerClass="r" class="r" format="{0,number,0}" total="true" />

			<display:column property="approvedTrips" title="Approved Trips"
				headerClass="r" class="r" format="{0,number,0}" total="true" />
				
		
			<display:column property="totalCost" title="Total Cost"
				headerClass="r" class="r" format="&#8377; {0,number,0}" total="true" />
			<display:column property="perTripCost" title="Per Trip Cost"
				headerClass="r" class="r" format="&#8377; {0,number,0}" />



			<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
			<display:setProperty name="export.excel.filename" value="<%=fname2%>" />
			<display:setProperty name="export.xml.filename" value="<%=fname3%>" />

		</display:table>

		<%@include file="Footer.jsp"%>
	</div>
	</div>

</body>
</html>
