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
		$('#fromDate').datepick();
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
try{

		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		var size = "height=124,width=300,top=200,left=300," + params;
		
		  if (url == "termsAndConditions.html") {
			size = "height=450,width=520,top=200,left=300," + params;
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
		var fromDate = $("input[name=fromDate]").val();
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
		} else if(less_thanEqual(nextnextnextMonthDate,fromDate))
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
		empid = Long.parseLong(employeeId);
		%>
	<%@include file="Header.jsp"%>
	<%
	OtherDao ob = null;
	ob = OtherDao.getInstance();
%>
	<div id="body">
		<div class="content">
			<%
					// code to show employee information in page
					String code = "";
					try {
						code = ob.getEmployeeDet(empid);
					} catch (Exception e) {
						System.out.println("Exception  " + e);
					}
				%>
				<%=code%>


			<form action="Subscribe" name="subscriptionForm"
				onsubmit="return confirmValidate();">


	
				<hr />
				<h3> Subscription</h3>




				<table align="center">

					<tr>

						<td align="right">Effective Date</td>
						<td><input type="text" name="fromDate" id="fromDate"
							class="required" readOnly
							class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2012,  1, 25)}" />
							<label for="fromDate" class="requiredLabel">*</label></td>

					</tr>
										<tr>

						<td align="right">Routes</td>
						<td></td>

					</tr>					

				</table>
			</form>

			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
