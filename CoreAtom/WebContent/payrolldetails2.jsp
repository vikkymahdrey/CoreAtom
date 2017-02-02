<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dto.PayrollDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.service.PayrollService"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="error.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Payroll Details</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />



<!-- Beginning of compulsory code below -->

<script type="text/javascript">
	$(document).ready(function() {
		$("#Search").click(getPayrollDetails);

	});

	function getPayrollDetails() {

		var url1 = "GetPayrollDetails?month=" + $("select[name=month]").val()
				+ "&year=" + $("select[name=year]").val()+"&site="+$("select[name=siteId]").val();
		$("#loadImageOfSupervisor2").html(
				"<img   id=\"loadingImage\" src=\"images/ajax-loader.gif\"/>");
		$.ajax({

			type : "POST",
			url : url1,
			success : function(data) {
				$("#loadImageOfSupervisor2").html(" ");
				$("#PayrollDiv").html(data);
			}

		});

	}
</script>
</head>
<body>
	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>



	<div id="body">
		<div class="content">

			<h2 align="center">Payroll Report</h2>

			<form id="frmGetPayrollDetails" name="frmGetPayrollDetails"
				method="post" action="payrolldetails2.jsp">
				<table width="529">
					<tr>
						<td>Choose Site</td>
						<td><select name="siteId" id="siteId">
								<%
									String siteID = request.getParameter("siteId");
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
					</tr>
					<tr>
						<td width="20%">Month</td>
						<td>Year</td>

					</tr>

					<tr>

						<td width="20%"><select name="month" id="month">
								<%  
									String monthParam = request.getParameter("month") == null ? ""
											: request.getParameter("month");
									String yearParam = request.getParameter("year") == null ? ""
											: request.getParameter("year");
									int month = 0;
									Calendar cal = Calendar.getInstance();
									if (monthParam != null && !monthParam.equals("")) {
										month = Integer.parseInt(monthParam);
									} else {

										month = cal.get(cal.MONTH) + 1;
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

											selected = "selected";
										} else {
											selected = "";
										}
										out.print("<option " + selected + " value=" + i + ">"
												+ monthLabel[i - 1] + "</option>");
									}
								%>
						</select></td>
						<td align="left"><select name="year" id="year">
								<%
									int i = year - 5;
									for (; i < year; i++) {

										out.print("<option    value=" + i + ">" + i + "</option>");
									}
									out.print("<option  selected  value=" + i + ">" + i + "</option>");
								%>

						</select></td>

					</tr>
					<tr>

						<td width="20%" align="right">
							<div id="loadImageOfSupervisor2"></div>
						</td>
						<td><input type="button" class="formbutton" name="Search"
							id="Search" value="Submit" /> <input type="button"
							class="formbutton" onclick="javascript:history.go(-1);"
							value="Back" /></td>

					</tr>
				</table>
			</form>
			<div id="PayrollDiv"></div>







			<table style="height: 250px">
				<tr>
					<td width="310"></td>
					<td width="250"></td>
				</tr>

			</table>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
