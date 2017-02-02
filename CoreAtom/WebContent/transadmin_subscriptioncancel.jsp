<%--
    Document   : emp_subscription
    Created on : Aug 28, 2012, 12:51:01 PM
    Author     : 123
--%>

<%@page import="com.agiledge.atom.service.SettingsService"%>
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
<title>Unsubscription</title>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/coin-slider.css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<link href="css/validate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script src="js/dateValidation.js"></script>

<!-- Beginning of compulsory code below -->


<script>
	$(document).ready(function() {

		setActiveMenuItem();
	});

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
	$(function() {

		$('#fromDate').datepick();

	});

	function goSubscriptionHistory() {
		location.href = "emp_subscriptionHistory.jsp";
		// window.open("emp_schedulingHistory.jsp");
	}
</script>

<script type="text/javascript">


	function dateCompare() {
		var flag = true;
		try{
			
		var currentDate = new Date();
		
		var currentDatevar = currentDate.getDate() + "/"
		+ (currentDate.getMonth()+1) + "/" + currentDate.getFullYear();
		var fromDate = $("input[name=fromDate]").val();	
		var fl=less_thanEqual(fromDate, currentDatevar);
		if ($("#fromDate").val().trim() == "") {

			alert("Unsubscription date can't be blank !");
			flag = false;
		} else if (fl) {
			alert("Unsubscription date could not be past Date!");
			flag = false;
		}
		if (flag == true) {
			if (confirm("You are trying to unsubscribe from "+fromDate+", all your active schedule will be cancelled after this date. Click 'OK' to unsubscribe, 'Cancel' to go back.")) {
				flag = true;
			} else {
				flag = false;
			}
		}
		}
		catch(e)
		{
			alert(e);
		}
		return flag;
		/*
		
		
		
		var acountingDate = setDates();
		
	//	else if (less_thanEqual(acountingDate, currentDatevar)) {
		//	if (fromdateyearMonth < currentDateyermonth) {
			//	var cutOffDate=acountingDate.split("/")[0];
			//	var monthnames=["Jan","Feb","March","April","May","June", "July", "Aug", "Sept", "Oct","Nov","Dec"];
			//	var effectFromDate= "01/" + monthnames[(parseInt( acountingDate.split("/")[1]) + 1)] +"/" +acountingDate.split("/")[2]; 
			//	alert("The cut off date to unsubscribe is "+ cutOffDate + "th. Your are allowed to submit un-subscription with effect from:" + effectFromDate  );
			//	flag = false;
			//}
		//}
		//  alert("Date 1 :" + date1 + "\n" + "Date 2 " + date2);		
		if (flag == true) {
			if (confirm("You are trying to unsubscribe from "+$("#fromDate").val()+", all your active schedule will be cancelled after this date. Click 'OK' to unsubscribe, 'Cancel' to go back.")) {
				flag = true;
			} else {
				flag = false;
			}
		}
		*/
		
		
		//alert("from date: "+ fromDate + "\n accounting date : "+ acountingDate + "\n Current date :" + currentDatevar);
		
		
	}

	/*
	function setDates() {
		var accountingDay = $("input[name=accountingDate]").val();
		var accountingdate = new Date();
		accountingdate.setDate(accountingDay);
		var weekday = accountingdate.getDay();
		if (weekday == 6) {
			accountingdate.setDate(accountingdate.getDate() - 1);
		}
		if (weekday == 0) {
			accountingdate.setDate(accountingdate.getDate() - 2);
		}

		var accountingdatevar = accountingdate.getDate() + "/"
				+ ( accountingdate.getMonth() + 1) + "/"
				+ accountingdate.getFullYear();
		
		return accountingdatevar;
	
	}
	*/
</script>

<script type="text/javascript">
	function loadEmployeeDetails()

	{		
		if (confirm1()) {
			
			try {				
				try
				{document.getElementById("subscriptiondetailsTable").innerHTML="";
				
				}
				catch(e)
				{
					
				}
				try{
					document.getElementById("detailsTable").innerHTML="";
				}
				catch(e)
				{
					
				}
				
				var xmlhttp;
				if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
					xmlhttp = new XMLHttpRequest();
				} else {// code for IE6, IE5
					xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
				}
				xmlhttp.onreadystatechange = function() {
					if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
						document.getElementById("EmployeeDetails").innerHTML = xmlhttp.responseText;
					}
				}
				xmlhttp.open("POST", "GetEmployeeDataFromSubscription?employeeID="
						+ document.getElementById("employeeID").value + "&firstName="
						+ document.getElementById("firstName").value + "&lastName="
						+ document.getElementById("lastName").value, true);
				xmlhttp.send();
			} catch (e) {
				alert(e.message);
			}

		} else {

			alert("Please type texts in search fields");
			//	alert("No records found !");

		}

	}

	function confirm1() {
		var flag = true;

		if ($("input[name=employeeID]").val().trim() == ""
				&& $("input[name=firstName]").val().trim() == ""
				&& $("input[name=lastName]").val().trim() == "") {
			flag = false;

		}

		return flag;

	}

	function onsubmitfn() {
		loadEmployeeDetails();
		return false;
	}
	function selectRow(id) {

			var name = document.getElementById("name-" + id).value;
			document.getElementById("EmployeeDetails").innerHTML="";
			window.location="transadmin_subscriptioncancel.jsp?employeeId="+id+"&name="+name;		
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
	<%

		String action = "";
		String buttonValue = "Unsubscribe";
		if (new EmployeeSubscriptionService()
				.isUnsubscriptionRequestMade(request.getSession()
						.getAttribute("user").toString())) {
			action = "UpdateUnsubscription";
			buttonValue = "Update";

		} else {
			action = "UnsubscriptionRequest";
		}
	%>



	<div id="body">
		<div class="content">
			<h3>Search Employee To Unsubscribe</h3>
			<form name="SearchForm" action="" onsubmit="return onsubmitfn()">
				<table border="0">
					<tr>
						<td>Employee ID</td>
						<td><input type="text" name="employeeID" id="employeeID" /></td>
						<td></td>
					</tr>
					<tr>
						<td>First Name</td>
						<td><input type="text" name="firstName" id="firstName" /></td>
						<td></td>
					</tr>
					<tr>
						<td>Last Name</td>
						<td><input type="text" name="lastName" id="lastName" /></td>
						<td></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="submit" class="formbutton" name="Search"
							value="Search" /></td>
						<td></td>
					</tr>


				</table>


			</form>
			<div id="EmployeeDetails"></div>


			<div>
				<%
					
					if(request.getParameter("employeeId")!=null)
					{
						employeeId=	request.getParameter("employeeId");
						String employeeName=request.getParameter("name");
						boolean subscribed;
						boolean unsubscribed;
						EmployeeSubscriptionService employeeSubscriptionService=new EmployeeSubscriptionService();
						boolean unsubscription= employeeSubscriptionService.isUnsubscribed(employeeId);
						unsubscribed = employeeSubscriptionService.isUnsubscriptionRequestMade(employeeId);
						subscribed = new EmployeeSubscriptionService().isSubscribed(employeeId);

						if (!(unsubscribed || !subscribed)) {
					%>
				<form name="unsubscribeRequestForm" action="UnsubscriptionRequest"
					onsubmit="return dateCompare()">
					<table id="detailsTable">

						<tr>
							<td>Employee Name</td>
							<td><%=employeeName%></td>
						</tr>

						<tr>
							<td><input type="hidden" name="employeeId"
								value="<%=employeeId%>" />From :</td>
							<td>
								<%
									SettingsService service = new SettingsService();
										service.getAccountingDate();
										String accountingDate = service.getAccountingDate();
								%> <input type="hidden" name="accountingDate"
								value="<%=accountingDate%>" /> <input type="hidden"
								name="compareDate" /> <input type="text" id="fromDate"
								name="fromDate"
								readOnly;
                                   class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2012,  1, 25)}" />
								<label for="fromDate" class="requiredLabel">*</label> <input
								type="submit" class="formbutton" id="unsubscribeRequest"
								value="<%=buttonValue%>" />

							</td>

						</tr>

					</table>
				</form>
				<%
						} else if (unsubscribed) {
							EmployeeSubscriptionDto dto = new EmployeeSubscriptionService()
									.getUnsubscriptionRequestDetails(employeeId);
					%>
				<br />
				<table style="width: 45%;">
					<tr>
						<td colspan="3"></td>
					</tr>
					<tr>
						<td>Employee Name</td>
						<td>:</td>
						<td><%=employeeName%></td>
					</tr>
					<tr>
						<td>Unsubscription Requested</td>
						<td>:</td>
						<td><%=OtherFunctions.changeDateFromatToddmmyyyy(dto.getUnsubscriptionDate())%></td>

					</tr>
					<tr>
						<td>Unsubscription Effective Date</td>
						<td>:</td>
						<td><%=OtherFunctions.changeDateFromatToddmmyyyy(dto.getUnsubscriptionFromDate())%></td>
					</tr>
				</table>
				<%
						} else if (unsubscription) {
							EmployeeSubscriptionDto dto = new EmployeeSubscriptionService()
									.getUnsubscriptionRequestDetails(employeeId);
					%>
				<br />
				<table style="width: 45%;">
					<tr>
						<td colspan="3"></td>
					</tr>
					<tr>
						<td>Employee Name</td>
						<td>:</td>
						<td><%=employeeName%></td>
					</tr>
					<tr>
						<td>Unsubscription Requested</td>
						<td>:</td>
						<td><%=OtherFunctions.changeDateFromatToddmmyyyy(dto.getUnsubscriptionDate())%></td>

					</tr>
					<tr>
						<td>Unsubscription Effective Date</td>
						<td>:</td>
						<td><%=OtherFunctions.changeDateFromatToddmmyyyy(dto.getUnsubscriptionFromDate())%></td>
					</tr>
					<tr>
						<td>Unsubscription Status</td>
						<td>:</td>
						<td><%=dto.getSubscriptionStatus()%></td>
					</tr>
				</table>



				<%

						} else {
					%>
				<center>Employee should have active subscription to
					unsubscribe</center>
				<br />
				<%
						}
					%>
			</div>


			<hr />
			<!-- Shows Subscription Information here
                        -->
			<%
					EmployeeSubscriptionDto dto = new EmployeeSubscriptionService()
							.getEmployeeSubscriptionDetails(employeeId);

					if (dto != null
							&& !dto.getSubscriptionStatus().equals("unsubscribed")) {

						// 						System.out.println(" subs status : "
						// 								+ dto.getSubscriptionStatus()
						// 								+ "\n compare : "
						// 								+ dto.getSubscriptionStatus().equals(
						// 										EmployeeSubscriptionDto.status.UNSUBSCRIBED));
						// 						System.out.println(" subs status : "
						// 								+ EmployeeSubscriptionDto.status.UNSUBSCRIBED);
				%>




			<table bgcolor="" id="subscriptiondetailsTable">
				<tr>
					<td colspan="3">
						<h4>Subscription Information</h4>
					</td>
				</tr>


				<tr>
					<td>Status</td>
					<td>:</td>
					<td><%=dto.getSubscriptionStatus()%></td>

				</tr>


				<tr>
					<td>Effective Date</td>
					<td>:</td>
					<td><%=OtherFunctions.changeDateFromatToddmmyyyy(dto
						.getSubscriptionFromDate())%></td>



				</tr>






				<tr>
					<td><input type="hidden" name="payment" /> Spoc</td>
					<td>:</td>
					<td>
						<%
								EmployeeDto employeeDto2 = new EmployeeService()
											.getEmployeeAccurate(dto.getSupervisor2());
									//			System.out.println("Supervisor 1 in jsp : "
									//				+ dto.getSupervisor2());
							%> <%=employeeDto2.getEmployeeFirstName() + " "
						+ employeeDto2.getEmployeeLastName()%>

					</td>

				</tr>


				<%
						APLDto aplDto = new APLService().getLandMarkAccurate(dto
									.getLandMark());
							String apl = aplDto.getArea() + "\n" + aplDto.getPlace()
									+ " -> " + aplDto.getLandMark();
					%>



				<tr>
					<td>Contact Number</td>
					<td>:</td>
					<td><%=dto.getContactNo()%></td>
					<td></td>
				</tr>
				<tr>
					<td>EmpType</td>
					<td>:</td>
					<td><%=dto.getEmpType()%></td>
					<td></td>
				</tr>

				<tr>
					<td>Landmark</td>
					<td>:</td>
					<td><%=apl%></td>

				</tr>

				<tr>
					<td colspan="3" align="center"></td>
				</tr>



			</table>

			<%
					} 
				%>
			<!--
                            end s subscription information
                        -->





			<%
					}
				%>
			<%@include file="Footer.jsp"%>


		</div>
	</div>

</body>
</html>
