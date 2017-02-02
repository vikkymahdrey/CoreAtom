<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dao.BranchDao"%>
<%@page import="com.agiledge.atom.dto.BranchDto"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.service.CompanyBranchService"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="css/style.css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<title>Add Company</title>
</head>
<body>
	<script type="text/javascript">
	
			$(document).ready(function(){
				$(".modify").click(setUpdateValues);
				$("#addNew").click(clearUpdateValues);
				$("select[name=night_shift_start_hour]").click(function(){
					concateNightShiftStartHour();
				});
				$("select[name=night_shift_start_minute]").click(function(){
					concateNightShiftStartMinute();
				});
				
				$("select[name=night_shift_end_hour]").click(function(){
					concateNightShiftEndHour();
				});
				$("select[name=night_shift_end_minute]").click(function(){
					concateNightShiftEndMinute();
				});
			});
		 
		function displaytxtDiv(serial)

		{
			document.getElementById("txtdiv"+serial).style.display = 'block';
			document.getElementById("btndiv"+serial).style.display = 'none';
		}
		function showSites(companyId,branchId)
		{
			window.location="site.jsp?companyId="+companyId+"&branchId="+branchId+"";
		}	
		
		  function showPopup(url) {

		         var branchId=document.getElementById("branchId").value;
		        var params="toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		         var size="height=124,width=300,top=200,left=300," + params;
		        if(url=="LandMarkSearch.jsp")
		            {
		                size="height=450,width=520,top=200,left=300," + params;
		            }
		       

		        newwindow=window.open(url+"?branchId="+branchId,'name',size);

		         if (window.focus) {newwindow.focus()}
		  }
		
		function setUpdateValues()
		{
			var s=$(this).attr("name").split("-");
			var index=s[1];
			
			$("input[name=branchId]").val($("input[name=branchId-" + index + "]").val());
			$("input[name=id]").val($("input[name=id-" + index + "]").val());
			$("input[name=siteName]").val($("input[name=siteName-" + index + "]").val());
			$("input[name=landMarkID]").val($("input[name=landmarkId-" + index + "]").val());
			$("input[name=landMark]").val($("input[name=landmark-" + index + "]").val());
			
			$("input[name=night_shift_start]").val($("input[name=night_shift_start-" + index + "]").val());
		
			$("input[name=night_shift_end]").val($("input[name=night_shift_end-" + index + "]").val());
			$("select[name=lady_security]").val($("input[name=lady_security-" + index + "]").val());
			 
			setHoursAndMinute();
			$("form[name=form1]").attr("action","UpdateSite");
			$("#actionButton").val("Update");
			
		}
		function concateNightShiftStartMinute()
		{
			var nightShiftStart=$("input[name=night_shift_start]").val().split(":");
			
			$("input[name=night_shift_start]").val(nightShiftStart[0]+":"+$("select[name=night_shift_start_minute]").val());
		}
		function concateNightShiftStartHour()
		{
			var nightShiftStart=$("input[name=night_shift_start]").val().split(":");
			
			$("input[name=night_shift_start]").val($("select[name=night_shift_start_hour]").val()+":"+nightShiftStart[1]);
		}
		
		function concateNightShiftEndMinute()
		{
			var nightShift=$("input[name=night_shift_end]").val().split(":");
			
			$("input[name=night_shift_end]").val(nightShift[0]+":"+$("select[name=night_shift_end_minute]").val());
		}
		function concateNightShiftEndHour()
		{
			var nightShift=$("input[name=night_shift_end]").val().split(":");
			
			$("input[name=night_shift_end]").val($("select[name=night_shift_end_hour]").val()+":"+nightShift[1]);
		}
		
		function setHoursAndMinute()
		{
			 
			var nightShiftStart=$("input[name=night_shift_start]").val().split(":");
			$("select[name=night_shift_start_hour]").val(nightShiftStart[0]);
			$("select[name=night_shift_start_minute]").val(nightShiftStart[1]);
			 
			var nightShiftEnd=$("input[name=night_shift_end]").val().split(":");
			$("select[name=night_shift_end_hour]").val(nightShiftEnd[0]);
			$("select[name=night_shift_end_minute]").val(nightShiftEnd[1]);
			
			 
			
		}
		
		function clearUpdateValues()
		{
			
			$("input[name=id]").val("");
			$("input[name=siteName]").val("");
			$("input[name=landMarkID]").val("");
			$("input[name=landMark]").val("");
			$("#actionButton").val("Add");
			$("form[name=form1]").attr("action","AddSite");
		}
		
		function validateFields()
		{
			var flag=true;
			if($("input[name=siteName]").val().trim()=="")
				{
					alert("Site Name is empty");
					flag=false;
					
				}
			else if($("input[name=landMark]").val().trim()=="")
				{
				alert("Land mark is empty");
					flag=false;
				}
				return flag;
		}
		function showAuditLog(relatedId,moduleName){
			var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
			var size = "height=450,width=900,top=200,left=300," + params;
			var url="ShowAuditLog.jsp?relatedNodeId="+relatedId+"&moduleName="+moduleName;	
		    newwindow = window.open(url, 'AuditLog', size);

			if (window.focus) {
			newwindow.focus();
		}
		}
		
	</script>
<%
				long empid = 0;
				String employeeId = OtherFunctions.checkUser(session);

				empid = Long.parseLong(employeeId);
%>
	<%@include file="Header.jsp"%>
	<div id='body'>
		<div class='content'>
			<%				
				String companyId = request.getParameter("companyId");
				String branchId = request.getParameter("branchId");
				if (branchId != null && !branchId.equals("")) {

					ArrayList<SiteDto> siteDtos = new SiteService()
							.getSites(branchId);

					if (siteDtos != null && siteDtos.size() > 0) {
						System.out.println(" track...........");
			%>
			<h3>
				Sites of
				<%=new BranchDao().getBranchLocation(branchId)%></h3>

			<table>
				<%
					int serial = 1;
							if (siteDtos != null && siteDtos.size() > 0) {
				%>
				<tr>
					<th>Site</th>
					<th>Landmark</th>
					<th>Night shift start</th>
					<th>Night shift end</th>
					<th>Lady Security</th>
					<th>Actions</th>

				</tr>
				<%
					for (SiteDto siteDto : siteDtos) {
				%>

				<tr>

					<td width='13%'><%=siteDto.getName()%> <input type="hidden"
						name="siteName-<%=siteDto.getId()%>"
						value="<%=siteDto.getName()%>" /> <input type="hidden"
						name="id-<%=siteDto.getId()%>" value="<%=siteDto.getId()%>" /> <input
						type="hidden" name="branchId-<%=siteDto.getId()%>"
						value="<%=siteDto.getBranch()%>" /></td>
					<td><%=siteDto.getLandmarkName()%> <input type="hidden"
						name="landmark-<%=siteDto.getId()%>"
						value="<%=siteDto.getLandmarkName()%>" /> <input type="hidden"
						name="landmarkId-<%=siteDto.getId()%>"
						value="<%=siteDto.getLandmark()%>" /></td>
					<td><%=siteDto.getNight_shift_start()%> <input type="hidden"
						name="night_shift_start-<%=siteDto.getId()%>"
						value="<%=siteDto.getNight_shift_start()%>" /></td>
					<td><%=siteDto.getNight_shift_end()%> <input type="hidden"
						name="night_shift_end-<%=siteDto.getId()%>"
						value="<%=siteDto.getNight_shift_end()%>" /></td>
					<td><%=siteDto.getHasLadySecurity()%> <input type="hidden"
						name="lady_security-<%=siteDto.getId()%>"
						value="<%=siteDto.getLady_securiy()%>" /></td>
					<td id="btndiv<%=serial%>"><input type="Button"
						name="modify-<%=siteDto.getId()%>" class="modify formbutton"
						value="Modify" /> <input type="Button"
						name="addVendors-<%=siteDto.getId()%>"
						class="addVendors formbutton" value="Add Vendors"
						onclick="javascript: document.location='vendor_site.jsp?siteId=<%=siteDto.getId()%>'" />

						<input type="button" class="formbutton"
						onclick="showAuditLog(<%=siteDto.getId()%>,'<%=AuditLogConstants.SITE_MODULE%>');"
						value="Audit Log" /></td>
				</tr>

				<%
					serial++;
								}
							}
				%>
			</table>
			<%
				}
				}
			%>





			<h3></h3>
			<hr>
			<div>
				<form name="form1" action="" method="post">



					<table>
						<tr>
							<th><input type="hidden" name="companyId"
								value="<%=companyId%>" /> Site</th>
							<th>Landmark</th>
							<th>Night shift start</th>
							<th>Night shift end</th>
							<th>Lady Security</th>
							<th></th>
						</tr>
						<tr>

							<td><input type="hidden" name="id" /> <input type="hidden"
								name="branchId" id="branchId" value="<%=branchId%>" /> <input type="text"
								name="siteName" /></td>
							<td><input type="text" name="landMark" id="landmark"
								onclick="showPopup('LandMarkSearch.jsp') " value="" readOnly />
								<input type="hidden" name="landMarkID" id="landMarkID" /> <input
								type="hidden" readonly name="area" id="area" /> <input
								type="hidden" readonly name="area" id="place" /></td>
							<td>
								<%
		
								%> <input type="hidden" name="night_shift_start"
								id="night_shift_start" value="00:00" /> <select
								name="night_shift_start_hour" id="night_shift_start_hour">
									<%
										for (int hour = 0; hour < 24; hour++) {
											String hourString = (hour < 10 ? "0" + hour : "" + hour);
									%>
									<option value="<%=hourString%>"><%=hourString%></option>
									<%
										}
									%>
							</select> <select name="night_shift_start_minute"
								id="night_shift_start_minute">
									<%
										for (int minute = 0; minute < 60; minute++) {
											String minuteString = (minute < 10 ? "0" + minute : "" + minute);
									%>
									<option value="<%=minuteString%>"><%=minuteString%></option>
									<%
										}
									%>
							</select>

							</td>
							<td>
								<%
								%> <input type="hidden" name="night_shift_end"
								id="night_shift_end" value="00:00" /> <select
								name="night_shift_end_hour" id="night_shift_end_hour">
									<%
										for (int hour = 0; hour < 24; hour++) {
											String hourString = (hour < 10 ? "0" + hour : "" + hour);
									%>
									<option value="<%=hourString%>"><%=hourString%></option>
									<%
										}
									%>
							</select> <select name="night_shift_end_minute"
								id="night_shift_end_minute">
									<%
										for (int minute = 0; minute < 60; minute++) {
											String minuteString = (minute < 10 ? "0" + minute : "" + minute);
									%>
									<option value="<%=minuteString%>"><%=minuteString%></option>
									<%
										}
									%>
							</select>


							</td>
							<td><select name="lady_security">
									<option value="1">Yes</option>
									<option value="0">No</option>
							</select></td>
							<td><input type="submit" value="Update" id="actionButton"
								class="formbutton" /> <input id="addNew" type="button"
								value="Add New" class="formbutton" /></td>
							<td></td>
						</tr>
					</table>
					<br /> <input type="Button" class="formbutton" value="Back"
						onclick="javascript:history.go(-1);" /> <br />
				</form>

			</div>
			<%@include file="Footer.jsp"%>
		</div>
	</div>


</body>
</html>
