



<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.reports.dto.ExceptionReportDto"%>
<%@page import="com.agiledge.atom.reports.SchedulingReport"%>
<%@page import="com.agiledge.atom.reports.dto.EmpSubscription"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.reports.EmpSubscriptionReportHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Exception Report</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script type="text/javascript">
	$(function() {
		$('#date').datepick();

	});
</script>

</head>
<body>
	<%
	String fname1=("ExceptionReport :").concat(new Date().toString()).concat(".csv");
	String fname2=("ExceptionReport :").concat(new Date().toString()).concat(".xls");
	String fname3=("ExceptionReport :").concat(new Date().toString()).concat(".xml");
	
		String siteId=request.getParameter("siteId");
		String date = request.getParameter("date");
		String log = request.getParameter("log");
		//String site = request.getParameter("site");
		SchedulingReport reportHelper = new SchedulingReport();
		List<ExceptionReportDto> emplist=null;
		if(date!=null&&!date.equals(""))
		{
			 emplist = reportHelper.getExceptionReport(date,siteId,log);
		}else
		{
			date="";
		}
						
		
		
		
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<h2 align="center">Exception Report</h2>
			<form action="display_exceptionReport.jsp">
				<table>
					<tr>
						<td>Date <input type="text" name="date" value="<%=date %>"
							readOnly id="date" />

						</td>
						<td><select name="siteId" id="siteId">
								<%
									String site=(request.getSession().getAttribute("site")==null||request.getSession().getAttribute("site").toString().trim().equals(""))?"" :request.getSession().getAttribute("site").toString().trim();
									 List<SiteDto> siteDtoList = new SiteDao().getSites();  
									 
									if(siteId!=null&&siteId.trim().equals("")==false)
									{
										site=siteId;
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
							<td>IN/OUT <select name="log" ><option value="IN">IN</option><option value="OUT">OUT</option></select>

						</td>
						<td><input type="submit" class="formbutton" value="Generate" />
						</td>
					</tr>
				</table>


			</form>
			<hr />

			<display:table class="alternateColor" name="<%=emplist%>" id="row"
				export="true" defaultsort="1" defaultorder="descending"
				pagesize="50">
				<display:column property="personnelNo" title="Staff#"
					sortable="true" headerClass="sortable" />
				<display:column property="staff" title="Staff" sortable="true"
					headerClass="sortable" />
				<display:column property="status" title="Status" sortable="true"
					headerClass="sortable" />
				<display:column property="date" title="Date (dd/mm/yyyy)"
					sortable="true" headerClass="sortable" />

				<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
				<display:setProperty name="export.excel.filename"
					value="<%=fname2%>" />
				<display:setProperty name="export.xml.filename" value="<%=fname3%>" />



			</display:table>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
