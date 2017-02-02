



<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="org.displaytag.decorator.MultilevelTotalTableDecorator"%>
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
<script type="text/javascript" src="js/jquery-latest.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Trip Sheet No Show Report</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script>
$(document).ready(function(){
	$("table#row tr:even").css("background-color","#43f3");
	$("table#row  tr:odd").css("background-color","#0032");
	 
	
});
	function  showReport(month,label,id)
	
	{
		 
		window.location="drilldownReport.jsp?monthvalue=" + month + "&" + label +"=" + id + "&year=" + $("select[name=year]").val() 
				+ "&Site="+ $("select[name=siteId]").val();	
	}
</script>

</head>
<body>
	<%
	String fname1=("NoShowReport :").concat(new Date().toString()).concat(".csv");
	String fname2=("NoShowReport :").concat(new Date().toString()).concat(".xls");
	String fname3=("NoShowReport :").concat(new Date().toString()).concat(".xml");
	
	
	
	
	
	
		String monthParam = request.getParameter("month");
		String yearParam = request.getParameter("year");
		String groupBy=request.getParameter("groupBy");
		String siteId=request.getParameter("siteId");
		

		TripSheetNoShowCountReportHelper reportHelper = new TripSheetNoShowCountReportHelper();
		List<TripSheetNoShowCountReportDto> dtoList =null;
		if(groupBy!=null&&!groupBy.equals(""))
		{
			
		  
			  System.out.println("groupby......."+ groupBy);
			  System.out.println("month......."+ monthParam);
		
			  dtoList = reportHelper.getNoShowCountGroupBy(monthParam,yearParam,groupBy,siteId);
		 
		}else
		{
			  System.out.println("track.......");
			  System.out.println("month......."+ monthParam);
			  groupBy="Site";
			  dtoList = reportHelper.getNoShowCountGroupBy(monthParam,yearParam,"site", siteId);
		}
			 

		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<h2 align="center">Trip Sheet No Show Report</h2>
			<div>
				<form action="display_tripSheet_no_show_count_report.jsp">

					<table width="20%">
						<tr>
							<td>Site</td>
							<td>Month</td>
							<td>Year</td>
							<td>Group By</td>
							<td></td>
						</tr>
						<tr>
							<td><select name="siteId" id="siteId">
									<%
									String site=(request.getSession().getAttribute("site")==null||request.getSession().getAttribute("site").toString().trim().equals(""))?"" :request.getSession().getAttribute("site").toString().trim();
									 List<SiteDto> siteDtoList = new SiteDao().getSites();  
									 
									 site=request.getParameter("siteId")!=null&&request.getParameter("siteId").trim().equals("")==false?request.getParameter("siteId"):site;
									
										;
									 
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
                                        	String yearSelect="";
                                        	if(i==year)
                                        	{
                                        		yearSelect="selected";
                                        	}
                                            out.print("<option  "+yearSelect+"  value=" + i + ">" + i + "</option>");
                                        }
                                        
                                        String groupByLabel[]={"Project", "SPOC","Manager"};
                                        
                                    %>

							</select></td>
							<td><select name="groupBy">
									<%
								for(String v: groupByLabel)
								{
									String groupByselect="";
									if(v.equals(groupBy))
									{
										groupByselect="selected";
									}
							%>
									<option <%=groupByselect %> value="<%=v %>"><%=v %></option>
									<%} %>

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
			export="true" defaultsort="1" defaultorder="descending" pagesize="50"
			decorator="tableDecor">
			<display:column property="month" title="Month" sortable="true"
				headerClass="sortable" />
			<display:column property="groupBy" title="<%=groupBy%>"
				sortable="true" headerClass="sortable" />
			<display:column property="noShowCount" title="Number of No Shows "
				sortable="true" format="{0,number,0}" headerClass="sortable"
				total="true" />

			<display:column title="Details">
				<%
				if (groupBy != null && !groupBy.equals("")) {
			%>
				<a
					href="javascript:showReport('${row.month }','<%=groupBy %>','${row.groupById }')">
					View </a>
				<%} %>
			</display:column>

			<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
			<display:setProperty name="export.excel.filename" value="<%=fname2%>" />
			<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
		</display:table>
		<%@include file="Footer.jsp"%>
	</div>
	</div>

</body>
</html>
