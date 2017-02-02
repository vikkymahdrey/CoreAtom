<%@page import="com.agiledge.atom.dto.ProjectDto"%>
<%@page import="com.agiledge.atom.service.ProjectService"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.service.SettingsService"%>
<%@page import="com.agiledge.atom.dto.SettingsDTO"%>
<%@page import="com.agiledge.atom.usermanagement.dto.ViewManagementDto"%>
<%@page
	import="com.agiledge.atom.usermanagement.service.ViewManagementService"%>
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
<title>Adhoc Setup</title>
</head>
<body>

	<script type="text/javascript">
		$(document).ready(
				function() {
					$("#approvereqtr").hide();
					$("#requestcutofftr").hide();
					
					$("#cancelcutofftr").hide();
					$("#cancelModetr").hide();
					$("#maxrequetstr").hide();
					$("#maxpendingrequetstr").hide();
					$("#requestertr").hide();
					$("#approverwhotr").hide();
					$("#pickuptr").hide();
					$("#droptr").hide();
					$("#requestcutofftrdrop").hide();
					
					$("#cancelcutofftrdrop").hide();
					$("#cancelModetrdrop").hide();
					$("#maxrequetstrdrop").hide();
					$("#maxpendingrequetstrdrop").hide();
					
					$("#submittr").hide();
				//	$("#pickupDrop").hide();
				   
					if ($("#adhoctype").val() == "shiftExtension") {
						$("#pickuptr").show();
						$("#droptr").show();
						$("#approvereqtr").show();
						$("#pickupDrop").show();
						$("#requestcutofftr").show();
						$("#cancelcutofftr").show();
						$("#cancelModetr").show();
						$("#maxrequetstr").show();
						$("#requestertr").show();
						$("#requestcutofftrdrop").show();
						
						$("#cancelcutofftrdrop").show();
						$("#cancelModetrdrop").show();
						$("#maxrequetstrdrop").show();
						$("#maxpendingrequetstrdrop").hide();
						//	$("#maxpendingrequetstr").show();
						$("#approverwhotr").hide();
						$("#submittr").show();
					} else if ($("#adhoctype").val() == "hotelAirport") {
						$("#approvereqtr").show();
						$("#requestcutofftr").show();
						//	$("#maxpendingrequetstr").show();
						$("#cancelcutofftr").show();
						$("#requestertr").show();
						$("#submittr").show();
					} else if ($("#adhoctype").val() == "atDisposal" || $("#adhoctype").val() == "pointToPoint"
							|| $("#adhoctype").val() == "intervalOrPeriod" || $("#adhoctype").val() == "airportTransfer" ) {
						$("#approvereqtr").show();
						$("#requestcutofftr").show();
						$("#cancelcutofftr").show();
						//	$("#maxpendingrequetstr").show();
						$("#requestertr").show();
						$("#submittr").show();
					}
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
											"adhocSetup.jsp");
									$('#adhocIdSet').attr('onsubmit',
											'return 1');
									$("#adhocIdSet").submit();
								}
							});
					/* $("#pickDrop").change(function(){
						$("#requestcutofftr").show();
						$("#cancelcutofftr").show();
						$("#cancelModetr").show();
						$("#maxrequetstr").show();
						$("#requestertr").show();
						
					}); */

					$("#approval").change(function() {
						if ($("#approval").val() == "yes") {
							$("#approverwhotr").show();
							$("#maxpendingrequetstr").show();
							if($("#adhoctype").val() == "shiftExtension"){$("#maxpendingrequetstrdrop").show();}
							$('#approvers').children().removeAttr('selected');
						} else {
							$("#approverwhotr").hide();
							$("#maxpendingrequetstr").val("");
							$("#maxpendingrequetstr").hide();
							$("#maxpendingrequetstrdrop").hide();
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
	</script>

	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<h3>Set Up Adhoc</h3>

			<%
				long empid = 0;
				String employeeId = OtherFunctions.checkUser(session);

				empid = Long.parseLong(employeeId);
				ArrayList<ViewManagementDto> roleList = new ViewManagementService()
						.getRoleList();

				String adhoctype = request.getParameter("adhoctype");
				String siteId = request.getParameter("siteId");
				String projectUnit = request.getParameter("projectUnit");
				ArrayList<SettingsDTO> adhoctypeList = new SettingsService()
						.getSettingsStrings(SettingsConstant.ADHOC,
								SettingsConstant.TYPE, siteId, projectUnit);
				//System.out.println("adhoctype="+adhoctype+"siteId= "+siteId+"projectUnit= "+projectUnit);

				List<SiteDto> siteDtos = new SiteDao().getSites();
				ArrayList<ProjectDto> projects = new ProjectService()
						.getProjectUnit();
				for (SettingsDTO settingsDTO : adhoctypeList) {
					
					System.out.println(settingsDTO.getKeyValue() +" " + settingsDTO.getModuleId());
					//these section should be checked and un commented
					/* if(settingsDTO.getKeyValue().equalsIgnoreCase("shiftExtension"))
					{
					if (settingsDTO.getKeyValue().equalsIgnoreCase(adhoctype)
							&& settingsDTO.getModuleId() != null && settingsDTO.getPickDrop() !=null){
						response.sendRedirect("AdhocSetupEdit.jsp?adhoctype="
								+ adhoctype + "&siteId=" + siteId + "&projectUnit="
								+ projectUnit);
					}
					else
					{
						if (settingsDTO.getKeyValue().equalsIgnoreCase(adhoctype)
								&& settingsDTO.getModuleId() != null){
							response.sendRedirect("AdhocSetupEdit.jsp?adhoctype="
									+ adhoctype + "&siteId=" + siteId + "&projectUnit="
									+ projectUnit);
						}
						
					}
					}  */
					if (settingsDTO.getKeyValue().equalsIgnoreCase(adhoctype)
							&& settingsDTO.getModuleId() != null && !response.isCommitted()){
						response.sendRedirect("AdhocSetupEdit.jsp?adhoctype="
								+ adhoctype + "&siteId=" + siteId + "&projectUnit="
								+ projectUnit);
						
					}
					/* else
					{
						System.out.println("error in forwarding");
					} */
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
						<td align="left"><select name="projectUnit" id="projectUnit">
								<option value="all">All</option>
								<%
									for (ProjectDto projectDto : projects) {
										if (projectDto.getProjectUnit().equalsIgnoreCase(projectUnit)) {
								%>
								<option selected="selected"
									value="<%=projectDto.getProjectUnit()%>"><%=projectDto.getProjectUnit()%></option>
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
									for (SettingsDTO dto : adhoctypeList) {
										isSetVal = "";
										if (dto.getModuleId() != null) {
											//	isSetVal = "-1";
										}
										if (dto.getKeyValue().equalsIgnoreCase(adhoctype)) {
								%>
								<option selected="selected"
									value="<%=dto.getKeyValue() + isSetVal%>"><%=dto.getValue()%></option>

								<%
									} else {
								%>
								<option value="<%=dto.getKeyValue() + isSetVal%>"><%=dto.getValue()%></option>
								<%
									}
									}
								%>
						</select></td>
					</tr>
				</table>
				<table>

					<tr id="requestertr">
						<td>Who raise the request</td>
						<td><select name="requesters" id="requesters"
							multiple="multiple">
								<%
									for (ViewManagementDto dto : roleList) {
								%>
								<option value="<%=dto.getRoleId()%>"><%=dto.getRoleName()%></option>
								<%
									}
								%>


						</select></td>
					</tr>
					<tr id="approvereqtr">
						<td>Approval required or Not</td>
						<td colspan="2"><select name="approval" id="approval">
								<option value="no">NO</option>
								<option value="yes">YES</option>

						</select></td>
					</tr>
					<tr id="approverwhotr">
						<td>Who All</td>
						<td colspan="2"><select multiple="multiple" name="approvers"
							id="approvers">
								<%
									for (ViewManagementDto dto : roleList) {
								%>
								<option value="<%=dto.getRoleId()%>"><%=dto.getRoleName()%></option>
								<%
									}
								%>
						</select></td>
					</tr>
					
						</tr>
                   <!--   <tr id = "pickupDrop"><td>Pick Up/Drop</td> 
                     <td colspan = "2"><select name="pickDrop" id = "pickDrop">
                     <option>select</option>op
                     <option>Pick Up</option>
                     <option>Drop</option>
                     </select></td></tr> -->
                     <tr id = "pickuptr"><td><h2>Pick Up</h2></td></tr>
                     
					<tr id="requestcutofftr">
						<td>Cut off time for raising request</td>
						<td colspan="2"><input type="text" name="bookCutoff"
							id="bookCutoff" />hh:mm</td>
					</tr>
					<tr id="cancelcutofftr">
						<td>Cut off time for canceling the booking</td>
						<td colspan="2"><input type="text" name="cancelCutoff"
							id="cancelCutoff" />hh:mm</td>
					</tr>

					<tr id="cancelModetr">
						<td>Cancellation mode of existing booking</td>
						<td><select name="cancelMode" id="cancelMode">
								<option value="automatic">Automatic</option>
								<option value="manual">Manual</option>
						</select></td>
						<td>Cut off Time to Cancel<input type="text"
							name="existingCancel" id="existingCancel" />hh:mm
						</td>
					</tr>
					<tr id="maxrequetstr">
						<td>Maximum number of request per day</td>
						<td><input type="text" name="maxrequest" id="maxrequest" /></td>
					</tr>
					<tr id="maxpendingrequetstr">
						<td>Maximum number of Pending request</td>
						<td><input type="text" name="maxpendingrequest"
							id="maxpendingrequest" /></td>
					</tr>

                  <tr id ="droptr"><td><h2>Drop</h2></td></tr>
                     
					<tr id="requestcutofftrdrop">
						<td>Cut off time for raising request</td>
						<td colspan="2"><input type="text" name="bookCutoffdrop"
							id="bookCutoffdrop" />hh:mm</td>
					</tr>
					<tr id="cancelcutofftrdrop">
						<td>Cut off time for canceling the booking</td>
						<td colspan="2"><input type="text" name="cancelCutoffdrop"
							id="cancelCutoffdrop" />hh:mm</td>
					</tr>

					<tr id="cancelModetrdrop">
						<td>Cancellation mode of existing booking</td>
						<td><select name="cancelModedrop" id="cancelModedrop">
								<option value="automatic">Automatic</option>
								<option value="manual">Manual</option>
						</select></td>
						<td>Cut off Time to Cancel<input type="text"
							name="existingCanceldrop" id="existingCanceldrop" />hh:mm
						</td>
					</tr>
					<tr id="maxrequetstrdrop">
						<td>Maximum number of request per day</td>
						<td><input type="text" name="maxrequestdrop" id="maxrequestdrop" /></td>
					</tr>
					<tr id="maxpendingrequetstrdrop">
						<td>Maximum number of Pending request</td>
						<td><input type="text" name="maxpendingrequestdrop"
							id="maxpendingrequestdrop" /></td>
					</tr>

					<tr id="submittr">
						<td></td>
						<td><input type="submit" value="Submit" class="formbutton" /></td>
					</tr>
				</table>
			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>