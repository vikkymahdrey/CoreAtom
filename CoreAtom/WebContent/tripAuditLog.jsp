



<%@page import="com.agiledge.atom.dao.AuditLogDAO"%>
<%@page import="com.agiledge.atom.dto.AuditLogDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.reports.dto.EmpUnsubscription"%>
<%@page import="com.agiledge.atom.reports.EmpUnSubscriptionReportHelper"%>
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
<title>Trip Audit Log</title>

<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">
	function resetFields() {
		$("input[type=hidden]").val("");
	}
	function showPopup(url) {

		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		size = "height=450,width=520,top=200,left=300," + params;
		if (url == "LandMarkSearch.jsp") {
			size = "height=450,width=600,top=200,left=300," + params;
		} else if (url == "SupervisorSearch1.jsp"
				|| url == "SupervisorSearch2.jsp") {
			size = "height=450,width=700,top=200,left=300," + params;
		} else if (url == "termsAndConditions.html") {
			size = "height=450,width=520,top=200,left=300," + params;
		} else if (url == "EmployeeSearch.jsp") {
			size = "height=450,width=520,top=200,left=300," + params;
		}

		newwindow = window.open(url, 'name', size);

		if (window.focus) {
			newwindow.focus();
		}
	}
</script>

<script type="text/javascript">
	$(document).ready(function() {
		$("#fromDate").datepick();
		$("#toDate").datepick();
	});
</script>
<script type="text/javascript">
	function validate() {
		return true;
	}
</script>


</head>
<body>
	<%
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String siteId = request.getParameter("siteId");
		ArrayList<AuditLogDTO> auditDtos = null;
		if (fromDate != null) {
			auditDtos = new AuditLogDAO().getTripSheetAuditLog(siteId,
					fromDate, toDate);

		}

		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
	%>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<div>
				<form name="form1" action="tripAuditLog.jsp" method="POST"
					onsubmit="return validate()">
					<table style="width: 50%">
						<tr>
							<td>Choose Site</td>
							<td><select name="siteId" id="siteId">
									<%
										String site = (request.getSession().getAttribute("site") == null || request
												.getSession().getAttribute("site").toString().trim()
												.equals("")) ? "" : request.getSession()
												.getAttribute("site").toString().trim();
										List<SiteDto> siteDtoList = new SiteDao().getSites();

										if (siteId != null && siteId.trim().equals("") == false) {
											site = siteId;
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


							<td align="right">From</td>
							<td align="left" colspan="2"><input name="fromDate"
								id="fromDate" type="text" size="6"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=fromDate != null && fromDate.trim().equals("") == false ? fromDate
					: ""%>" />

								To<input name="toDate" id="toDate" type="text" size="6"
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
                                                 minDate: new Date(2008, 12 - 1, 25)}"
								value="<%=toDate != null && toDate.trim().equals("") == false ? toDate
					: ""%>" />
							</td>
							<td><input type="submit" value="Submit" class="formbutton" /></td>
						</tr>
					</table>
				</form>
			</div>
			<hr />
			<br />
			<h2 align="center">Trip Sheet AuditLog</h2>
			<hr />
			java.lang.Object row = (java.lang.Object) pageContext
						.getAttribute("row");
						<display:table class="alternateColorInSub" name=" "
				id="row" export="true" defaultsort="1" defaultorder="descending"
				pagesize="50" decorator="tableDecor">
			
				<display:column property="tripCode" title="Code" sortable="true"
					headerClass="sortable" />
				<display:column property="tripDate" title="Trip Date"
					sortable="true" headerClass="sortable" />
				<display:column property="tripMode" title="IN/OUT" sortable="true"
					headerClass="sortable" />
				<display:column property="tripTime" title="Trip Time"
					sortable="true" headerClass="sortable" />
				<display:column property="displayDate" title="Changed Date"
					sortable="true" headerClass="sortable" />
				<display:column property="changedByName" title="Changed By"
					sortable="true" headerClass="sortable" />
				<display:column property="previousState" title="previousState"
					sortable="true" headerClass="sortable" />
				<display:column property="currentState" title="currentState"
					sortable="true" headerClass="sortable" />
				<display:column property="action" title="action" sortable="true"
					headerClass="sortable" />
			</display:table>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
