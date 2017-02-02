<%--
    Document   : emp_subscription
    Created on : Aug 28, 2012, 12:51:01 PM
    Author     : 123
--%>

<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@page import="com.agiledge.atom.dto.EmployeeSubscriptionDto"%>
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.service.APLService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.lang.Exception"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="com.agiledge.atom.dao.APLDao"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"
	errorPage="error.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Modify Subscription</title>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<link href="css/validate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script src="js/dateValidation.js"></script>



<script src="js/dateValidation.js"></script>
<script type="text/javascript">
	$(function() {

		/* if(!$("#fromDate").is("readOnly"))
		    {
		             $('#fromDate').datepick();

		    }
		 */

	});
</script>
<script>
	$(document).ready(function() {

		setActiveMenuItem();
		$("#sameToBelow").click(sameToBelowClicked);
	});

	// function to be same supervisor as spoc
	function sameToBelowClicked() {
		if ($("#sameToBelow").is(":checked")) {
			$("input[name=supervisorID2]").val(
					$("input[name=supervisorID1]").val());
			$("input[name=supervisorName2]").val(
					$("input[name=supervisorName1]").val());
			$("input[name=userType2]").val(
					$("input[name=userType1]").val());
		} else {
			$("input[name=supervisorID2]").val("");
			$("input[name=supervisorName2]").val("");
			$("input[name=userType2]").val("");
		}
	}

	function setActiveMenuItem() {
		var url = window.location.pathname;
		var filename = url.substring(url.lastIndexOf('/') + 1);
		//  $("li[class=active']").removeAttr("active");

		$("a[href='" + filename + "']").parent().attr("class", "active");
		$("a[href='" + filename + "']").parent().parent().parent('li').attr(
				"class", "active");

	}
</script>
<script type="text/javascript">
	function showPopup(url) {

		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		var size = "height=124,width=300,top=200,left=300," + params;
		if (url == "LandMarkSearch.jsp") {
			size = "height=450,width=520,top=200,left=300," + params;
		} else if (url == "SupervisorSearch1.jsp"
				|| url == "SupervisorSearch2.jsp") {
			size = "height=450,width=580,top=200,left=300," + params;
		} else if (url == "termsAndConditions.html") {
			size = "height=450,width=520,top=200,left=300," + params;
		}

		var site=document.getElementById("site").value;
		if (url == "LandMarkSearch.jsp") {
			if(site.length<1)
			{
			alert("Choose Site");
			return false;
			}
			url+="?site="+site;
		}
		newwindow = window.open(url, 'name', size);

		if (window.focus) {
			newwindow.focus()
		}
	}

	//  validate to prompt user about the payment
	function confirmValidate() {
		var flag = true;
		var currentDate = new Date();
		var currentDatevar = currentDate.getDate() + "/"
				+ currentDate.getMonth() + "/" + currentDate.getFullYear();
		var fromDate = $("input[name=fromDate]").val();
		var site = $("select[name=site]").val();
		if(fromDate=="")
		{
		flag=false;
		alert("Please Specify Effective Date!");
		}
		if ($("input[name=supervisorName1]").val() == "") {
			alert("Please specify Supervisor");
			flag = false;
		} else if ($("input[name=supervisorName2]").val() == "") {
			alert("Please specify SPOC");
			flag = false;
		} else if ($("input[name=landMark]").val() == "") {
			alert("Please specify Landmark");
			flag = false;
		} else if (site == "") {
			alert("Please choose your site");
			flag = false;
		} else if (isNaN($("input[name=contactNo]").val())
				|| ($("input[name=contactNo]").val()).length != 10) {
			alert("Please specify 10 digit contact number");
			flag = false;
		}

		else if (($("#editPrivilage").val() == "true")
				&& ($("#subscribedStatus").val() == "false")) {
			if (CompareTwoDatesddmmyyyy(fromDate, currentDatevar)) {
				alert("Effective date must be after current date !");
				flag = false;
			}
		}
		if (flag == true) {
			if (!confirm(" Please verify your subscription details before submitting\n Click OK to proceed Cancel to review")) {
				flag = false;
			}
		}

		return flag;

	}
</script>


<script type="text/javascript">
	function displayaddress() {
		document.getElementById("addresstd").innerHTML = document
				.getElementById("address").innerHTML;
	}
</script>



</head>
<body onload="displayaddress()">	
	<div id="body">
		<div class="content">
			<%
		long empid = 0;
	            String employeeId = OtherFunctions.checkUser(session);
	            empid = Long.parseLong(employeeId);	
	            OtherDao ob = null;
	            ob = OtherDao.getInstance();
	%>
<%@include file="Header.jsp"%>






			<form action="UpdateSubscription" name="subscriptionForm">


				<%
					// code to show employee information in page
						                        String code="";
						                        try{
						                         code= OtherDao.getInstance().getEmployeeDet(empid);
						                        }catch(Exception e)
						                                {
						                                System.out.println("Exception  " + e);
						                            }
				%>
				<%=code%>
				<hr />
				<h3>Edit Subscription</h3>
				<%
					boolean aplChangePrivilege = false;
						                        boolean editPrivilage=false;
						                        boolean subscribedStatus=true;
						                        String readOnly="readOnly";
						                        if (new EmployeeService().hasPrivilegeToChangeAddress(request.getSession().getAttribute("user").toString())) {
						                            aplChangePrivilege = true;
						                            
						                           // System.out.println("ApL chage privilage is here");
						                        }else
						                           {
						                            //System.out.println("No ApL chage privilage is here");

						                            }
						                        if (new EmployeeSubscriptionService().isSubscriptionRequestMade(request.getSession().getAttribute("user").toString())) {
						                          editPrivilage=true;
						                          subscribedStatus=false;
						                          readOnly="";

						                        }
						                        EmployeeSubscriptionDto dto = new EmployeeSubscriptionService().getEmployeeSubscriptionDetails(request.getSession().getAttribute("user").toString());
System.out.println("Edit Previlage"+editPrivilage+"   apl change previlage"+aplChangePrivilege);
						                        if (dto != null) {
						                        	//System.out.println(dto.getSubscriptionFromDate()+"KL"+OtherFunctions.changeDateFromatToddmmyyyy(dto.getSubscriptionFromDate()));
				%>
				<table>

					<tr>
						<td align="right"><input type="hidden" name="subscriptionId"
							value="<%=dto.getSubscriptionID()%>" /> <input type="hidden"
							id="editPrivilage" value="<%=editPrivilage%>" /> Effective Date</td>
						<td><input type="hidden" id="subscribedStatus"
							value="<%=subscribedStatus%>" /> <input type="text"
							name="fromDate" id="fromDate" readonly
							value="<%=OtherFunctions.changeDateFromatToddmmyyyy(dto.getSubscriptionFromDate())%>"
							class="required"
							class="{showOtherMonths: true, firstDay: 1, dateFormat: 'MM-DD-YYY', minDate: new Date(2012,  1, 25)}" /><label
							for="fromDate" class="requiredLabel">*</label> <%
  
 %></td>

					</tr>
					<tr>
						<td align="right">Site</td>
						<td>
							<%
								List<SiteDto> siteList = new SiteService().getSites();
									String disabled = "disabled";
									if (editPrivilage) {
										disabled = "";
									}
							%> <select name="site" id="site">

								<%
									for (SiteDto sdto : siteList) {
											String selected = "";
											if (sdto.getId().equals(dto.getSite())) {
												selected = "selected";
											}
								%>


								<option <%=selected%> value="<%=sdto.getId()%>">
									<%=sdto.getName()%>
								</option>


								<%
									}
								%>
						</select> <label for="site" class="requiredLabel">*</label>


						</td>

					</tr>

					<tr>
						<td align="right"><%=SettingsConstant.hrm %> <input type="hidden" name="payment" />

						</td>
						<td>
							<%
								EmployeeDto employeeDto = new EmployeeService()
											.getEmployeeAccurate(dto.getSupervisor1());
									//	System.out.println("Supervisor 1 in jsp : "
									//		+ dto.getSupervisor1());
							%> <input name="supervisorName1" readOnly id="supervisorName1"
							value='<%=employeeDto.getEmployeeFirstName() + " "
						+ employeeDto.getEmployeeLastName()%>' />
							<input type="hidden" name="userType1" id="userType1" /> <input
							type="hidden" value="<%=dto.getSupervisor1()%>"
							name="supervisorID1" id="supervisorID1" />  <label
							for="supervisorName1" class="requiredLabel">*</label>
						</td>

					</tr>


					<tr>
						<td align="right">SPOC</td>
						<td>
							<%
								EmployeeDto employeeDto2 = new EmployeeService()
											.getEmployeeAccurate(dto.getSupervisor2());
									//	System.out.println("Supervisor 1 in jsp : "
									//			+ dto.getSupervisor2());
							%> <input name="supervisorName2" readonly id="supervisorName2"
							value='<%=employeeDto2.getEmployeeFirstName() + " "
						+ employeeDto2.getEmployeeLastName()%>' />

							<input type="hidden" name="userType2" id="userType2" /> <input
							type="hidden" value="<%=dto.getSupervisor2()%>"
							name="supervisorID2" id="supervisorID2" /> <input type="button"
							class="formbutton" value="..."
							onclick="showPopup('SupervisorSearch2.jsp')" /> <label
							for="fromDate" class="requiredLabel">*</label>
						</td>
					</tr>



					<%
						APLDto aplDto = new APLService().getLandMarkAccurate(dto
									.getLandMark());
							String apl = aplDto.getArea() + "\n" + aplDto.getPlace()
									+ " -> " + aplDto.getLandMark();
							//			System.out.println("apl " + apl);
					%>



					<tr>

						<td colspan="2"><label style="margin-left: 32%;"> <%
							
								String aplLastEditedDate = "";
									if (editPrivilage || aplChangePrivilege) {
							%> <input type="button" value="Edit APL" class="formbutton"
								onclick="showPopup('LandMarkSearch.jsp') " /> <%
 	}
									if (dto.getEmpType().equals("contract")) {
							 			out.print("APL last Updated on : "
							 					+ dto.getEmployee().getAddressChangeDate()
							 					+ "<br><i style='margin-left: 32%'> Note:<i> APL only can change once in 30 days</i>");
							 		}
									
 	
 %>
						</label></td>


					</tr>
					<tr>
						<td align="right">Area</td>
						<td><input type="text" value="<%=aplDto.getArea()%>" readonly
							id="area" name="area" /> <input type="hidden" id="landMarkID"
							name="landMarkID" value="<%=dto.getLandMark()%>" /> <label
							for="area" class="requiredLabel">*</label></td>

					</tr>
					<tr>
						<td align="right">Place</td>
						<td><input type="text" value="<%=aplDto.getPlace()%>"
							readonly id="place" name="place" /> <label for="place"
							class="requiredLabel">*</label></td>

					</tr>
					<tr>
						<td align="right">Landmark</td>
						<td><input type="text" value="<%=aplDto.getLandMark()%>"
							readonly id="landmark" name="landMark" /> <label for="landMark"
							class="requiredLabel">*</label></td>

					</tr>


					<tr>
						<td align="right">Emp Type</td>
						<td><%=dto.getEmpType()%></td>
					</tr>
					<tr>
						<td align="right">Contact No</td>
						<td><input type="text" name="contactNo" id="contactNo"
							maxlength="10" value="<%=dto.getContactNo()%>" /> <label
							for="contactNo" class="requiredLabel">*</label></td>

					</tr>
					<tr>
						<td align="right" rowspan="4" valign="middle">Address</td>
						<td rowspan="4" id='addresstd'></td>

					</tr>
					<tr></tr>
					<tr></tr>
					<tr></tr>
					<tr>

						<td colspan="2"></td>

					</tr>

					<tr>



						<td></td>
						<td><input type="submit" name="subscribe" value="Update"
							onclick="return confirmValidate();" class="formbutton" /> <input
							type="button" class="formbutton"
							onclick="javascript:history.go(-1);" value="Back" /></td>

					</tr>

					<tr>


						<td colspan="2"><label style="margin-left: 30%" for="site"
							class="requiredLabel">*</label> Indicates mandatory field</td>
					</tr>
				</table>
				<%
					}
				%>
			</form>


			<%@include file="Footer.jsp"%>

		</div>
	</div>


</body>
</html>
