<%--
    Document   : emp_subscription
    Created on : Aug 28, 2012, 12:51:01 PM
    Author     : 123
--%>

<%@page import="com.agiledge.atom.service.APLService"%>
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
<title>Book Adhock</title>

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
		$('#effectiveDate').datepick();
		$("#sameToBelow").click(sameToBelowClicked);
		setActiveMenuItem();
		
	});

	// function to be same supervisor as spoc
 

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
	alert (url);
    var params="toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
    size="height=450,width=520,top=200,left=300,"+params;        
    
   if(url=="termsAndConditions.html")
        {
               size="height=450,width=520,top=200,left=300,"+params;
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
    newwindow=window.open(url,'name',size);
    

     if (window.focus) {newwindow.focus();}
    }

 
</script>
 
<script type="text/javascript">
	//  validate to prompt user about the payment
	function confirmValidate() {		
		var flag=true;
		var currentDate = new Date();
		 
		var nextnextnextMonthDateSupporter= new Date();
		//nextnextnextMonthDateSupporter.setDate(2);
		nextnextnextMonthDateSupporter.setMonth(nextnextnextMonthDateSupporter.getMonth()+2);		
		var nextnextnextMonthDate=nextnextnextMonthDateSupporter.getDate()+"/" +
			(nextnextnextMonthDateSupporter.getMonth()+1) +"/"+
			nextnextnextMonthDateSupporter.getFullYear();
		var currentDatevar = currentDate.getDate() + "/"
				+ currentDate.getMonth() + "/" + currentDate.getFullYear();
		var effectiveDate = $("input[name=effectiveDate]").val();
		var site = $("select[name=site]").val();
		if(effectiveDate=="")
			{
			flag=false;
			alert("Please Specify Effective Date!");
			}
		else if (!$("input[name=termsAndConditions]").is(":checked")) {
			alert("Please accept terms and conditions");
			flag = false;
		} else if (CompareTwoDatesddmmyyyy(effectiveDate, currentDatevar)) {
			alert("Effective date must be after current date !");
			flag = false;
		} else if(less_thanEqual(nextnextnextMonthDate,effectiveDate))
		{
			alert("Effective date should not exceed 2 months ");
			flag=false;					
		}else if ($("input[name=landMark]").val() == "") {
			alert("Please specify Landmark");
			flag = false;
		}  else if (isNaN($("input[name=contactNo]").val())
				|| ($("input[name=contactNo]").val()).length != 10) {
			alert("Please specify 10 digit contact number");
			flag = false;
		}		

		if (flag == true) {
			if (!confirm(" Please verify your adhock booking details before submitting\n Click OK to proceed Cancel to review")) {
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
				<h3>Book Adhock</h3>





				<table align="center">

					<tr>

						<td align="right">Effective Date</td>
						<td><input type="text" name="effectiveDate" id="effectiveDate"
							class="required" readOnly
							class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2012,  1, 25)}" />
							<label for="effectiveDate" class="requiredLabel">*</label></td>

					</tr>
				 
				 	<tr>

						<td align="right"> Origination 
						</td>
						<td   ><input  
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
						<td><input type="text" name="contactNo" id="contactNo"
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
							class="requiredLabel">#</label> Manager is your transport
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
