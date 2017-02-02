
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
<title>Subscription</title>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script src="js/dateValidation.js"></script>

<script>
	$(document).ready(function() {
		$("#sameToBelow").click(sameToBelowClicked);
		setActiveMenuItem();
		
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
try{

		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		var size = "height=124,width=300,top=200,left=300," + params;
		
	 if (url == "SupervisorSearch1.jsp"
				|| url == "SupervisorSearch2.jsp") {
			size = "height=450,width=700,top=200,left=300," + params;
		} else if (url == "termsAndConditions.html") {
			size = "height=450,width=520,top=200,left=300," + params;
		}
		var site=document.getElementById("site").value;
		if (url == "LandMarkSearch.jsp") {
			size = "height=450,width=600,top=200,left=300," + params;
			if(site.length<1)
			{
			alert("Choose Site");
			return false;
			}
			url+="?site="+site;
		}			
		newwindow=window.open(url,'name',size);

		if (window.focus) {
			newwindow.focus();
		}
}catch(e)
{alert(e);}
	}
</script>
<script type="text/javascript">
	$(function() {
		$('#fromDate').datepick();

	});
</script>
<script type="text/javascript">
	//  validate to prompt user about the payment
	function confirmValidate() {		
		var flag=true;
		//var currentDate = new Date();
		var currentDate=new Date();//new Date().getTime() + 24 * 60 * 60 * 1000);
		
		var nextnextnextMonthDateSupporter= new Date();
		//nextnextnextMonthDateSupporter.setDate(2);
		nextnextnextMonthDateSupporter.setMonth(nextnextnextMonthDateSupporter.getMonth()+2);		
		var nextnextnextMonthDate=nextnextnextMonthDateSupporter.getDate()+"/" +
			(nextnextnextMonthDateSupporter.getMonth()+1) +"/"+
			nextnextnextMonthDateSupporter.getFullYear();
		var currentDatevar = currentDate.getDate() + "/"
				+ currentDate.getMonth() + "/" + currentDate.getFullYear();
	
		var fromDate = $("input[name=fromDate]").val();
		var tomorrow = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
		var tomorrowVar = tomorrow.getDate() + "/" + tomorrow.getMonth() + "/" + tomorrow.getFullYear();
		var curtime = new Date();
		var curtimeCuttOff = new Date();
		curtimeCuttOff.setHours( $("#cutofftimefortomorrow").val().split(":")[0],$("#cutofftimefortomorrow").val().split(":")[1], 0,0 );
		var cutHour=parseInt( $("#cutofftimefortomorrow").val().split(":")[0]);
		var cutMinute = parseInt ($("#cutofftimefortomorrow").val().split(":")[1]);		
	 	var site = $("select[name=site]").val();
		if(fromDate=="")
			{
			flag=false;
			alert("Please Specify Effective Date!");
			}
		else if (!$("input[name=termsAndConditions]").is(":checked")) {
			alert("Please accept terms and conditions");
			flag = false;
		} else if (CompareTwoDatesddmmyyyy(fromDate, currentDatevar)) {
			alert("Effective date must be after current date !");
			flag = false;				
		} else if (CompareTwoDatesddmmyyyy(fromDate, tomorrowVar) && curtime.getTime() >= curtimeCuttOff.getTime()) {
			alert("Cut off time for tomorrow is   " + ( (cutHour%13 +1 )<=9? ('0'+(cutHour%13 + 1).toString()) : 'DDd' + (cutHour%13 + 1).toString() ) +":"+ ( cutMinute<=9? ('0'+cutMinute.toString()) :   cutMinute.toString() ) + " " + ( cutHour>12?"PM":"AM"  )); 
			flag = false;
		}
		else if(less_thanEqual(nextnextnextMonthDate,fromDate))		
		{
			alert("Effective date should not exceed 2 months ");
			flag=false;					
		}else if ($("input[name=supervisorID1]").val() == "") {
			alert("Please specify Reporting Officer");
			flag = false;
		} else if ($("input[name=supervisorID2]").val() == "") {
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

	<%
	long empid = 0;
	String employeeId = OtherFunctions.checkUser(session);
	EmployeeDto empDto=(EmployeeDto)session.getAttribute("userDto");
		empid = Long.parseLong(employeeId);
		%>
	<%@include file="Header.jsp"%>
	<%
	OtherDao ob = null;
	ob = OtherDao.getInstance();
%>
	<div id="body">
		<div class="content">



			<form action="Subscribe" name="subscriptionForm"
				onsubmit="return confirmValidate();">

			<input type="hidden" name="cutofftimefortomorrow" id="cutofftimefortomorrow" value="23:00" />
				<%
					// code to show employee information in page
					String code = "";
					try {
						code = OtherDao.getInstance().getEmployeeDet(empid);
					} catch (Exception e) {
						System.out.println("Exception  " + e);
					}
				%>
				<%=code%>
				<hr />
				<h3>Add Subscription</h3>





				<table align="center">

					<tr>

						<td align="right">Effective Date</td>
						<td><input type="text" name="fromDate" id="fromDate"
							class="required" readOnly
							class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2012,  1, 25)}" />
							<label for="fromDate" class="requiredLabel">*</label></td>

					</tr>
					<tr>
						<td align="right">Site</td>
						<!-- 
						
					 
						<td>
						
						</td>

					 
						 -->
						<td>
							<%
								List<SiteDto> siteList = new SiteService().getSites();
							%> <select name="site" id="site">
								<option value="">---------- Select -----------</option>
								<%
									for (SiteDto dto : siteList) {
								 
									String site=(request.getSession().getAttribute("site")==null||request.getSession().getAttribute("site").toString().trim().equals(""))?"" :request.getSession().getAttribute("site").toString().trim();
									String siteSelect="";
									if(site.equals(dto.getId()))
									{
										siteSelect="selected";
									}
											
								%>

								<option <%=siteSelect %> value="<%=dto.getId()%>"><%=dto.getName()%></option>
								<%  }%>
						</select> <label for="site" class="requiredLabel">*</label>


						</td>

					</tr>
					<tr>
						<td align="right"><%=SettingsConstant.hrm %></td>
						<td><input type="hidden" name="userType1" id="userType1" />

							<input type="hidden" name="supervisorID1" id="supervisorID1" value="<%=empDto.getLineManager()==null?"":empDto.getLineManager()%>" />
							<input type="text" readonly name="supervisorName1"
							id="supervisorName1" value="<%=empDto.getManagerName()==null?"":empDto.getManagerName()%>"/>
							<label for="supervisorID1" class="requiredLabel">#</label></td>

					</tr>


					<tr>
						<td align="right">SPOC</td>
						<td><input type="hidden" id="userType2" name="userType2" />
							<input type="hidden" name="supervisorID2" id="supervisorID2" value="<%=empDto.getSpoc_id()==null?"":empDto.getSpoc_id()%>" />
							<input type="text" readonly name="supervisorName2" 
							id="supervisorName2" onclick="showPopup('SupervisorSearch2.jsp')" value="<%=empDto.getSpocName()==null?"":empDto.getSpocName()%>" />
							<label for="supervisorID2" class="requiredLabel">#</label> <input
							class="formbutton" type="button" value="..."
							onclick="showPopup('SupervisorSearch2.jsp')" /> <input
							type="checkbox" id="sameToBelow" />Same as <%=SettingsConstant.hrm %></td>

					</tr>
					
					<tr>


						<td colspan="2"><input style="margin-left: 36%;"
							type="button" value="Select APL" class="formbutton"
							onclick="showPopup('LandMarkSearch.jsp') " /></td>

					</tr>
					<tr>
						<td align="right">Area</td>
						<td><input type="text" value=""
							onclick="showPopup('LandMarkSearch.jsp') " readonly name="area"
							id="area" /> <input type="hidden" id="landMarkID"
							name="landMarkID" /> <label for="area" class="requiredLabel">*</label>

						</td>

					</tr>
					<tr>
						<td align="right">Place</td>
						<td><input type="text" value=""
							onclick="showPopup('LandMarkSearch.jsp') " readonly name="place"
							id="place" /> <label for="place" class="requiredLabel">*</label>
						</td>

					</tr>
					<tr>
						<td align="right">Landmark</td>
						<td><input type="text"
							onclick="showPopup('LandMarkSearch.jsp') " readonly
							name="landMark" id="landmark" /> <label for="landMark"
							class="requiredLabel">*</label></td>

					</tr>




					<tr>
						<td align="right">Contact No</td>
						<td><input type="text" name="contactNo" id="contactNo" value="<%=OtherFunctions.isEmpty(empDto.getContactNo())?"":empDto.getContactNo() %>"
							maxlength="10" /> <label for="contactNo" class="requiredLabel">*</label>



						</td>

					</tr>


					<tr>
						<td align="right" rowspan="4" valign="middle">Address</td>
						<td rowspan="4" id='addresstd'></td>

					</tr>
					<tr></tr>
					<tr></tr>
					<tr></tr>
					<tr>
						<td></td>
						<td><input name="termsAndConditions" type="checkbox" />
							Accept <a href="#" onclick="showPopup('termsAndConditions.html')">
								Terms And Conditions </a></td>


					</tr>
					<tr>
						<td></td>
						<td><input class="formbutton" type="submit" name="subscribe"
							value="Submit" /> <input type="button" class="formbutton"
							onclick="javascript:history.go(-1);" value="Back" /></td>



					</tr>
					<tr>


						<td colspan="2"><label style="margin-left: 30%" for="site"
							class="requiredLabel">*</label> Indicates mandatory field</td>
					</tr>
					<tr>
						<td colspan="2"><label style="margin-left: 30%" for="site"
							class="requiredLabel">#</label> <%=SettingsConstant.hrm %> is your transport
							approving authority.
					</tr>
					<tr>
						<td colspan="2"><label style="margin-left: 30%" for="site"
							class="requiredLabel">#</label> Spoc is your transport scheduling
							authority.
					</tr>

				</table>
			</form>



			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
