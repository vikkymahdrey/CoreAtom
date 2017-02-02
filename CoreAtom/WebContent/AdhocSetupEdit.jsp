
<%@page import="com.agiledge.atom.usermanagement.dto.UserManagementDto"%>
<%@page import="com.agiledge.atom.service.AdhocService"%>
<%@page import="com.agiledge.atom.dto.ProjectDto"%>
<%@page import="com.agiledge.atom.service.ProjectService"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.service.SettingsService"%>
<%@page import="com.agiledge.atom.dto.AdhocDto"%>
<%@page import="com.agiledge.atom.dto.SettingsDTO"%>
<%@page import="java.util.List"%>

<%@page import="java.util.ArrayList"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Adhoc Setup Edit</title>
</head>
<body>

	<script type="text/javascript">
		$(document).ready(
				function() {

					if ($("#approval").val() == "no") {
						$("#maxpendingrequetstr").hide();
						$("#approverwhotr").hide();
						$("#maxpendingrequetstrdrop").hide();
					} 
					/*
					$("#approvereqtr").hide();
					$("#requestcutofftr").hide();
					$("#cancelcutofftr").hide();
					$("#cancelModetr").hide();
					$("#maxrequetstr").hide();
					$("#whoalltr").hide();
					$("#approverwhotr").hide();
					$("#submittr").hide();
					 */
					
					$("#adhoctype").change(
							function() {
								if ($("#siteId").val() == null
										|| $("#siteId").val() == "") {
									$("#adhoctype").val("");
									alert("Please Select Site");
								} else if ($("#adhoctype").val() == null
										|| $("#adhoctype").val() == "") {
									$("#adhoctype").val("");
									alert("Please select Adhoc type");
								} else {
									$("#adhocIdSet").attr("action",
											"AdhocSetupEdit.jsp");
									$('#adhocIdSet').attr('onsubmit',
											'return 1');
									$("#adhocIdSet").submit();
								}
							});
						$("#siteId").change(function() {
							$("#adhoctype").val("");
							$("#approvereqtr").hide();
							$("#requestcutofftr").hide();
							$("#cancelcutofftr").hide();
							$("#cancelModetr").hide();
							$("#maxrequetstr").hide();
							$("#maxpendingrequetstr").hide();
							$("#requestertr").hide();
							$("#approverwhotr").hide();
							$("#pickupDrop").hide();
							$("#submittr").hide();
							$("#pickuptr").hide();
							$("#droptr").hide();
							$("#requestcutofftrdrop").hide();
							
							$("#cancelcutofftrdrop").hide();
							$("#cancelModetrdrop").hide();
							$("#maxrequetstrdrop").hide();
							$("#maxpendingrequetstrdrop").hide();
						});
					/*
							$("#approval").val("no");
							$("#approvereqtr").hide();
							$("#requestcutofftr").hide();
							$("#cancelcutofftr").hide();
							$("#cancelModetr").hide();
							$("#maxrequetstr").hide();
							$("#approverwhotr").hide();
							$("#whoalltr").hide();
							$("#submittr").hide();

							if ($("#adhoctype").val() == "shiftExtension") {
								$("#approvereqtr").show();
								$("#requestcutofftr").show();
								$("#cancelcutofftr").show();
								$("#cancelModetr").show();
								$("#maxrequetstr").show();
								$("#whoalltr").show();
								$("#approverwhotr").hide();
								$("#submittr").show();
							} else if ($("#adhoctype").val() == "hotelAirport") {
								$("#approvereqtr").show();
								$("#requestcutofftr").show();
								$("#cancelcutofftr").show();
								$("#whoalltr").show();
								$("#submittr").show();
							} else if ($("#adhoctype").val() == "atDisposal") {
								$("#approvereqtr").show();
								$("#requestcutofftr").show();
								$("#cancelcutofftr").show();
								$("#whoalltr").show();
								$("#submittr").show();
							}
						});
					 */
					$("#approval").change(function() {
						if ($("#approval").val() == "yes") {
							$("#approverwhotr").show();
							$("#maxpendingrequetstr").show();
						} else {
							$("#approverwhotr").hide();
							$("#maxpendingrequetstr").hide();

						}
					});

				});
				function validateForm() {
					var regexp = /([01][0-9]|[02][0-4]):[0-5][0-9]/;
					var numregexp = /[0-9]+$/;
					if ($("#requesters").val() == null || $("#requesters").val() == "") {
						alert("Select Requester");
						return false;
					} else if ($("#approval").val() == "yes"
							&& ($("#approvers").val() == null || $("#approvers").val() == "")) {
						alert("Select Approvar");
						return false;
					} else if ($("#bookCutoff").val() == "" && $("#bookCutoffdrop").val == "") {
						alert("Enter Cut off time for Booking");
						return false;

					}

					else if ((!regexp.test($("#bookCutoff").val()) && (!regexp.test($("#bookCutoffdrop").val())))) {
						alert("Please enter book cut off time in correct format");
						return false;

					} else if ($("#cancelCutoff").val() == "" && $("#cancelCutoffdrop").val() == "" ) {
						alert("Enter Cut off time for Booking cancel");
						return false;

					}
					/*else if ($("#adhoctype").val() == "shiftExtension" && $("#cancelMode").val() == "") {
						alert("Existing Schedule Cancel Mode");				
					}*/
					else if ((!regexp.test($("#cancelCutoff").val())) && (!regexp.test($("#cancelCutoffdrop").val()))) {
						alert("Please enter cancellation cut off time in correct format");
						return false;

					} else if ($("#adhoctype").val() == "shiftExtension"
							&& ($("#existingCancel").val() == "" && $("#existingCanceldrop").val() == "" )) {
						alert("Enter existing  booking cancel Time");
						return false;
					} else if ($("#adhoctype").val() == "shiftExtension"
							&& ((!regexp.test($("#existingCancel").val())) && (!regexp.test($("#existingCanceldrop").val()))) ){
						alert("Please enter existing booking cancellation time in correct format");
						return false;

					} else if ($("#adhoctype").val() == "shiftExtension"
							&& (($("#maxrequest").val() == "") && ($("#maxrequestdrop").val() == ""))){
						alert("Enter maximum number of request per day for pick up/drop");
						return false;

					}
					/* else if((!numregexp.test($("#maxrequest").val()))&& (!numregexp.test($("#maxrequestdrop").val()))){
						alert("Enter valid maximum number of request per day for pickup/drop 1");
						return false
					} */
					else if (($("#adhoctype").val() == "shiftExtension")
							&& ( (($("#maxrequest").val() == "0") || ($("#maxrequestdrop").val() == "0" )) || ((!numregexp.test($("#maxrequest").val())) && (!numregexp.test($("#maxrequestdrop").val()))))) {

						alert("Enter valid maximum number of request per day for pickup/drop");
						return false;

					}
				
					
					
					

					else if (($("#adhoctype").val() == "shiftExtension"&& $("#approval").val() == "yes")
						&& (($("#maxpendingrequest").val() == "") && ($("#maxpendingrequestdrop").val() == ""))){
					alert("Enter maximum number of pending request per day for pick up/drop");
					return false;

				}
					else if (($("#adhoctype").val() == "shiftExtension" && $("#approval").val() == "yes")
							&& ( (($("#maxpendingrequest").val() == "0") || ($("#maxpendingrequestdrop").val() == "0" )) || ((!numregexp.test($("#maxpendingrequest").val())) && (!numregexp.test($("#maxpendingrequestdrop").val()))))) {

						alert("Enter valid maximum number of pending request per day for pickup/drop");
						return false;

					}
				
				else if ((!($("#adhoctype").val() == "shiftExtension") && ($("#approval").val() == "yes"))
							&& (($("#maxpendingrequest").val() == "") || (($(
									"#maxpendingrequest").val() == "0") || (!numregexp.test($("#maxpendingrequest").val()))))) {
						alert("Enter valid maximum number of pending request");
						return false;
					} else {
						return true;
					}
				}
		/* function validateForm() {
			var regexp = /([01][0-9]|[02][0-4]):[0-5][0-9]/;
			var numregexp = /^[0-9]+$/;
			var adhoct = $.trim($("#adhoctype").val());
			if ($("#requesters").val() == null || $("#requesters").val() == "") {
				alert("Select Requester");
				return false;
			} else if ($("#approval").val() == "yes"
					&& ($("#approvers").val() == null || $("#approvers").val() == "")) {
				alert("Select Approvar");
				return false;
			} else if ($("#bookCutoff").val() == "") {
				alert("Enter Cut off time for Booking");
				return false;

			} else if (!regexp.test($("#bookCutoff").val())) {
				alert("Please enter booking cut off time in correct format");
				return false;

			} else if ($("#cancelCutoff").val() == "") {
				alert("Enter Cut off time for Booking cancel");
				return false;

			} else if (!regexp.test($("#cancelCutoff").val())) {
				alert("Please enter cancellation cut off time in correct format");
				return false;

			}
			else if ($("#adhoctype").val() == "shiftExtension"
					&& $("#existingCancel").val() == "") {
				alert("Enter existing  booking cancel Time");
				return false;
			} else if ($("#adhoctype").val() == "shiftExtension"
					&& (!regexp.test($("#existingCancel").val()))) {
				alert("Please enter existing booking cancellation time in correct format");
				return false;

			} else if ($("#adhoctype").val() == "shiftExtension"
					&& $("#maxrequest").val() == "") {
				alert("Enter maximum number of request per day");
				return false;
			}

			else if (adhoct == "shiftExtension"
					&& ($("#maxrequest").val() == ""
							|| $("#maxrequest").val() == "0" || (!numregexp
							.test($("#maxrequest").val())))) {

				alert("Enter valid maximum number of request per day ");
				return false;

			} else if ($("#approval").val() == "yes"
					&& ($("#maxpendingrequest").val() == "" || $(
							"#maxpendingrequest").val() == "0")) {
				alert("Enter valid maximum number of pending request");
				return false;
			} else {
				return true;
			}
		} */
	</script>

	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<h3>Modify Adhoc Setup</h3>

			<%
				long empid = 0;
				String employeeId = OtherFunctions.checkUser(session);
				empid = Long.parseLong(employeeId);
				ArrayList<SettingsDTO> adhoctypeList = null;
				ArrayList<AdhocDto> adhocdetailslist = null;
				String adhoctype = request.getParameter("adhoctype");
				String siteId = request.getParameter("siteId");
				String projectUnit = request.getParameter("projectUnit");
               // String pickDrop = request.getParameter("pickDrop");
				//AdhocDto adhocDto = null;
				adhoctypeList = new SettingsService().getSettingsStrings(
						SettingsConstant.ADHOC, SettingsConstant.TYPE, siteId,
						projectUnit);
				
				List<SiteDto> siteDtos = new SiteDao().getSites();
				ArrayList<ProjectDto> projects = new ProjectService()
						.getProjectUnit();
				if (adhoctype != null && adhoctype != "") {
					adhocdetailslist = new AdhocService().getSetupDetails(adhoctype,
							siteId, projectUnit);
				
					
					//System.out.println(adhocDto.getAdhocType() + adhocDto.getMaxPendingRequest());
					 if (adhocdetailslist.size()==0) {
						response.sendRedirect("adhocSetup.jsp?adhoctype="
								+ adhoctype + "&siteId=" + siteId + "&projectUnit="
								+ projectUnit);
					} //these section has to be checked and un commented
				} 
			%>
			<form id="adhocIdSet" action="AdhocSetup" method="post"
				onsubmit="return validateForm()">
				<table>
					<tr>
						<td align="center">Site</td>
						<td>&nbsp;</td>
						<td align="left"><select name="siteId" id="siteId">
								<option value="">Select</option>
								<%
									for (SiteDto sitedto : siteDtos) {
										if (sitedto.getId().equalsIgnoreCase(siteId)) {
								%>
								<option selected="selected" value=<%=sitedto.getId()%>><%=sitedto.getName()%></option>
								<%
									} else {
								%>
								<option value=<%=sitedto.getId()%>><%=sitedto.getName()%></option>
								<%
									}
									}
								%>
						</select></td>
					</tr>
					<tr>
						<td align="center"><%=SettingsConstant.PROJECT_UNIT_TERM%></td>
						<td>&nbsp;</td>
						<td align="left" ><select name="projectUnit" id="projectUnit">
								<option value="all">All</option>
								<%
									for (ProjectDto projectDto : projects) {
										if (projectDto.getProjectUnit().equalsIgnoreCase(projectUnit)) {
								%>
								<option selected="selected"
									value="<%=projectDto.getProjectUnit()%>" ><%=projectDto.getProjectUnit()%></option>
								<%
									} else {
								%>
								<option value="<%=projectDto.getProjectUnit()%>"><%=projectDto.getProjectUnit()%></option>
								<%
									}
									}
								%>
						</select></td>
					</tr>
					<tr>
						<td align="center">Adhoc Type :</td>
						<td>&nbsp;</td>
						<td align="left"><select name="adhoctype" id="adhoctype">
								<option value="">Select</option>
								<%
									String isSetVal = "";
								    long flag = 0;
									for (SettingsDTO dto : adhoctypeList) {
									
										
										if (flag == dto.getId())
										{
											continue;
										}
										if (dto.getKeyValue().equalsIgnoreCase(adhoctype) ) {
											
									
								%>
								
								 <option selected="selected" value="<%=dto.getKeyValue()%>"><%=dto.getValue()%></option>

								<%
									} else if
									(!dto.getKeyValue().equalsIgnoreCase(adhoctype)) {
										
								%>
								 <option value="<%=dto.getKeyValue()%>"><%=dto.getValue()%></option> 
								<%
									 } 
										flag = dto.getId();
									}
								%>
						</select></td>
					</tr>
				</table>
				<table>
				<%
				if(adhocdetailslist != null)
				{
				for(AdhocDto adhocDto : adhocdetailslist)
				{
					%>
					<% if (adhocDto != null && adhocDto.getAdhocType().equalsIgnoreCase(SettingsConstant.SHIFT_EXTENSTION)) {
						
				%>
				<%if (adhocDto.getPickupDrop().equalsIgnoreCase("pick up")) {%>
			
					<tr id="requestertr">
						<td><input type="hidden" name="adhoctype"
							value="<%=adhocDto.getAdhocType()%>" /> <input type="hidden"
							id="adhocTypeId" name="adhocTypeId" value="<%=adhocDto.getId()%>" />
							Who raise the request</td>
						<td><select name="requesters" id="requesters"
							multiple="multiple">
								<%
									for (UserManagementDto dto : adhocDto.getRequesterRoles()) {
								%>
								<option <%=dto.getSelectionStatus()%> value="<%=dto.getId()%>"><%=dto.getName()%></option>
								<%
									}
								%>

						</select></td>
					</tr>
					<tr id="approvereqtr">
						<td>Approval required or Not</td>
						<td colspan="2"><select name="approval" id="approval">
								<%
									if (adhocDto.getApproval().equalsIgnoreCase("yes")) {
								%>
								<option value="yes" selected>YES</option>
								<option value="no">NO</option>
								<%
									} else {
								%>
								<option value="yes">YES</option>
								<option value="no" selected>NO</option>
								<%
									}
								%>

						</select></td>
					</tr>

					<tr id="approverwhotr">
						<td>Who All</td>
						<td colspan="2"><select multiple="multiple" name="approvers"
							id="approvers">

								<%
									for (UserManagementDto dto : adhocDto.getApproverRoles()) {
								%>
								<option <%=dto.getSelectionStatus()%> value="<%=dto.getId()%>"><%=dto.getName()%></option>
								<%
									}
								%>
						</select></td>
					</tr>
                <!--   <tr id = "pickupDrop"><td>Pick Up/Drop</td> 
                     <td colspan = "2"><select name="pickDrop" id = "pickDrop">
                     <option>select</option>
                     <option>Pick Up</option>
                     <option>Drop</option>
                     </select></td></tr> -->
                     
                     
                     <tr id = "pickuptr"><td><h2>Pick Up</h2></td></tr>
                   
					<tr id="requestcutofftr">
						<td>Cut off time for raising request</td>
						<td colspan="2"><input type="text" name="bookCutoff"
							id="bookCutoff" value="<%=adhocDto.getRequestCutoff()%>" />hh:mm</td>
					</tr>
					<tr id="cancelcutofftr">
						<td>Cut off time for canceling the booking</td>
						<td colspan="2"><input type="text" name="cancelCutoff"
							id="cancelCutoff" value="<%=adhocDto.getCancelCutoff()%>" />hh:mm</td>
					</tr>
					<%
						if (adhocDto.getAdhocType().equalsIgnoreCase(
									SettingsConstant.SHIFT_EXTENSTION)) {
					%>
					<tr id="cancelModetr">
						<td>Cancellation mode of existing booking</td>
						<td><select name="cancelMode" id="cancelMode">
								<%
									if (adhocDto.getCancelMode().equalsIgnoreCase("automatic")) {
								%>
								<option selected="selected" value="automatic">Automatic</option>
								<option value="manual">Manual</option>
								<%
									} else {
								%>
								<option value="automatic">Automatic</option>
								<option selected="selected" value="manual">Manual</option>
								<%
									}
								%>
						</select></td>
						<td>Cut off Time to Cancel<input type="text"
							name="existingCancel" id="existingCancel"
							value="<%=adhocDto.getExistingCancelTime()%>" />hh:mm
						</td>
					</tr>
					<tr id="maxrequetstr">
						<td>Maximum number of request per day</td>
						<td><input type="text" name="maxrequest" id="maxrequest"
							value="<%=adhocDto.getMaxRequest()%>" /></td>
					</tr>
					<%
						}
					%>
					<tr id="maxpendingrequetstr">
						<td>Maximum number of pending request</td>
						<td><input type="text" name="maxpendingrequest"
							id="maxpendingrequest"
							value="<%=adhocDto.getMaxPendingRequest()%>" /></td>
					</tr>
					<%} %>
					<%if(adhocDto.getPickupDrop().equalsIgnoreCase("drop")) {%>
					<tr id ="droptr"><td><h2>Drop</h2></td></tr>
					<tr id="requestcutofftrdrop">
						<td>Cut off time for raising request</td>
						<td colspan="2"><input type="text" name="bookCutoffdrop"
							id="bookCutoffdrop" value="<%=adhocDto.getRequestCutoff()%>" />hh:mm</td>
					</tr>
					<tr id="cancelcutofftrdrop">
						<td>Cut off time for canceling the booking</td>
						<td colspan="2"><input type="text" name="cancelCutoffdrop"
							id="cancelCutoffdrop" value="<%=adhocDto.getCancelCutoff()%>" />hh:mm</td>
					</tr>
					<%
						if (adhocDto.getAdhocType().equalsIgnoreCase(
									SettingsConstant.SHIFT_EXTENSTION)) {
					%>
					<tr id="cancelModetrdrop">
						<td>Cancellation mode of existing booking</td>
						<td><select name="cancelModedrop" id="cancelModedrop">
								<%
									if (adhocDto.getCancelMode().equalsIgnoreCase("automatic")) {
								%>
								<option selected="selected" value="automatic">Automatic</option>
								<option value="manual">Manual</option>
								<%
									} else {
								%>
								<option value="automatic">Automatic</option>
								<option selected="selected" value="manual">Manual</option>
								<%
									}
								%>
						</select></td>
						<td>Cut off Time to Cancel<input type="text"
							name="existingCanceldrop" id="existingCanceldrop"
							value="<%=adhocDto.getExistingCancelTime()%>" />hh:mm
						</td>
					</tr>
					<tr id="maxrequetstrdrop">
						<td>Maximum number of request per day</td>
						<td><input type="text" name="maxrequestdrop" id="maxrequestdrop"
							value="<%=adhocDto.getMaxRequest()%>" /></td>
					</tr>
					<%
						}
					%>
					<tr id="maxpendingrequetstrdrop">
						<td>Maximum number of pending request</td>
						<td><input type="text" name="maxpendingrequestdrop"
							id="maxpendingrequestdrop"
							value="<%=adhocDto.getMaxPendingRequest()%>" /></td>
					</tr>
					<tr id="submittr">
						<td></td>
						<td><input type="submit" value="Update" class="formbutton" /></td>
					</tr>
					<%}}
					else{
					%>
					
					
					
					
					
					<tr id="requestertr">
					<td><input type="hidden" name="adhoctype"
					value="<%=adhocDto.getAdhocType()%>" /> <input type="hidden"
					id="adhocTypeId" name="adhocTypeId" value="<%=adhocDto.getId()%>" />
					Who raise the request</td>
				<td><select name="requesters" id="requesters"
					multiple="multiple">
						<%
							for (UserManagementDto dto : adhocDto.getRequesterRoles()) {
						%>
						<option <%=dto.getSelectionStatus()%> value="<%=dto.getId()%>"><%=dto.getName()%></option>
						<%
							}
						%>

				</select></td>
			</tr>
			<tr id="approvereqtr">
				<td>Approval required or Not</td>
				<td colspan="2"><select name="approval" id="approval">
						<%
							if (adhocDto.getApproval().equalsIgnoreCase("yes")) {
						%>
						<option value="yes" selected>YES</option>
						<option value="no">NO</option>
						<%
							} else {
						%>
						<option value="yes">YES</option>
						<option value="no" selected>NO</option>
						<%
							}
						%>

				</select></td>
			</tr>

			<tr id="approverwhotr">
				<td>Who All</td>
				<td colspan="2"><select multiple="multiple" name="approvers"
					id="approvers">

						<%
							for (UserManagementDto dto : adhocDto.getApproverRoles()) {
						%>
						<option <%=dto.getSelectionStatus()%> value="<%=dto.getId()%>"><%=dto.getName()%></option>
						<%
							}
						%>
				</select></td>
			</tr>
     
           
			<tr id="requestcutofftr">
				<td>Cut off time for raising request</td>
				<td colspan="2"><input type="text" name="bookCutoff"
					id="bookCutoff" value="<%=adhocDto.getRequestCutoff()%>" />hh:mm</td>
			</tr>
			<tr id="cancelcutofftr">
				<td>Cut off time for canceling the booking</td>
				<td colspan="2"><input type="text" name="cancelCutoff"
					id="cancelCutoff" value="<%=adhocDto.getCancelCutoff()%>" />hh:mm</td>
			</tr>
			<%
				if (adhocDto.getAdhocType().equalsIgnoreCase(
							SettingsConstant.SHIFT_EXTENSTION)) {
			%>
			<tr id="cancelModetr">
				<td>Cancellation mode of existing booking</td>
				<td><select name="cancelMode" id="cancelMode">
						<%
							if (adhocDto.getCancelMode().equalsIgnoreCase("automatic")) {
						%>
						<option selected="selected" value="automatic">Automatic</option>
						<option value="manual">Manual</option>
						<%
							} else {
						%>
						<option value="automatic">Automatic</option>
						<option selected="selected" value="manual">Manual</option>
						<%
							}
						%>
				</select></td>
				<td>Cut off Time to Cancel<input type="text"
					name="existingCancel" id="existingCancel"
					value="<%=adhocDto.getExistingCancelTime()%>" />hh:mm
				</td>
			</tr>
			<tr id="maxrequetstr">
				<td>Maximum number of request per day</td>
				<td><input type="text" name="maxrequest" id="maxrequest"
					value="<%=adhocDto.getMaxRequest()%>" /></td>
			</tr>
			<%
				}
			%>
			<tr id="maxpendingrequetstr">
				<td>Maximum number of pending request</td>
				<td><input type="text" name="maxpendingrequest"
					id="maxpendingrequest"
					value="<%=adhocDto.getMaxPendingRequest()%>" /></td>
			</tr>
			<tr id="submittr">
						<td></td>
						<td><input type="submit" value="Update" class="formbutton" /></td>
					</tr>
			<%} %>
			<%
					}} %>
					<!-- <tr id="submittr">
						<td></td>
						<td><input type="submit" value="Update" class="formbutton" /></td>
					</tr>
 -->
				</table>
				<%
					
				
				/* else
				{
					System.out.println("error");
				} */
				%>
			</form>

			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>