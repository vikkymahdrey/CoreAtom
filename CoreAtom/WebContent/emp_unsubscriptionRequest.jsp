<%--
    Document   : emp_subscription
    Created on : Aug 28, 2012, 12:51:01 PM
    Author     : 123
--%>

<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
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



<script src="js/dateValidation.js"></script>


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
	var date1 = new Date();
	var date2 = new Date();
	$(document).ready(function() {

	});

	function dateCompare() {
		var flag = true;
		
		var currentDate = new Date();
		var nextnextnextMonthDateSupporter= new Date();
		//nextnextnextMonthDateSupporter.setDate(2);
		nextnextnextMonthDateSupporter.setMonth(nextnextnextMonthDateSupporter.getMonth()+2);
		
		var nextnextnextMonthDate=nextnextnextMonthDateSupporter.getDate()+"/" +
			(nextnextnextMonthDateSupporter.getMonth()+1) +"/"+
			nextnextnextMonthDateSupporter.getFullYear();
		var currentDatevar = currentDate.getDate() + "/"
				+ (currentDate.getMonth()+1) + "/" + currentDate.getFullYear();
		var fromDate = $("input[name=fromDate]").val();
		var fromdateMonthSplit = fromDate.split("/");
		var fromdateMonth = fromdateMonthSplit[1];
		var fromdateyearMonth = parseInt(fromdateMonthSplit[2])
				+ (parseInt(fromdateMonth) / 12);
		var currentDateyermonth = (currentDate.getFullYear())
				+ ((currentDate.getMonth() + 3) / 12);
		var acountingDate = setDates();
		if ($("#fromDate").val().trim() == "") {

			alert("Unsubscription date can't be blank !");
			flag = false;
		} else if (less_thanEqual(fromDate, currentDatevar)) {
			alert("Unsubscription date could not be past Date!");
			flag = false;
		}

		/* else if (less_thanEqual(acountingDate, currentDatevar)) {
			if (fromdateyearMonth < currentDateyermonth) {
				var cutOffDate=acountingDate.split("/")[0];
				var monthnames=["Jan","Feb","March","April","May","June", "July", "Aug", "Sept", "Oct","Nov","Dec"];
				var effectFromDate= "01/" + monthnames[(parseInt( acountingDate.split("/")[1]) + 1)] +"/" +acountingDate.split("/")[2]; 
				alert("The cut off date to unsubscribe was "+ cutOffDate + "th. Your are allowed to unsubscription only effect " + effectFromDate  );
				flag = false;
			}
		} */
		else if(less_thanEqual(nextnextnextMonthDate,fromDate))
			{
				alert("Unsubscription date should not exceed 2 months ");
				flag=false;
				
			
			}
		//  alert("Date 1 :" + date1 + "\n" + "Date 2 " + date2);		
		if (flag == true) {
			if (confirm("You are trying to unsubscribe from "+$("#fromDate").val()+", all your active schedule will be cancelled from this date. Click 'OK' to unsubscribe, 'Cancel' to go back.")) {
				flag = true;
			} else {
				flag = false;
			}
		}
		return flag;
		//alert("from date: "+ fromDate + "\n accounting date : "+ acountingDate + "\n Current date :" + currentDatevar);
		
		
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

	function setDates() {
		var accountingDay = $("input[name=accountingDate]").val();
		var accountingdate = new Date();
		accountingdate.setDate(accountingDay);
		var weekday = accountingdate.getDay();
		if (weekday == 6) {
			//accountingdate.setDate(accountingdate.getDate() - 1);
		}
		if (weekday == 0) {
			accountingdate.setDate(accountingdate.getDate() - 1);
		}

		var accountingdatevar = accountingdate.getDate() + "/"
				+ ( accountingdate.getMonth() + 1) + "/"
				+ accountingdate.getFullYear();
		
		return accountingdatevar;
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
		
		OtherDao ob = null;
		ob = OtherDao.getInstance();
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




			<form name="unsubscribeRequestForm" action="<%=action%>"
				onsubmit="return dateCompare()">

				<h3>Unsubscription</h3>






				<div>
					<%
						boolean subscribed;
						boolean unsubscribed;
						EmployeeSubscriptionService employeeSubscriptionService=new EmployeeSubscriptionService();
						boolean unsubscription= employeeSubscriptionService.isUnsubscribed(employeeId);
						unsubscribed = employeeSubscriptionService.isUnsubscriptionRequestMade(Long.toString(empid));
						subscribed = new EmployeeSubscriptionService().isSubscribed(Long
								.toString(empid));

						if (!(unsubscribed || !subscribed)) {
					%>
					<table>
						<tr>
							<td>From :</td>
							<td>
								<%
									SettingsService service = new SettingsService();
																service.getAccountingDate();
																String accountingDate = service.getAccountingDate();
																Date d= new Date();
																String currentDate=OtherFunctions.changeDateFromatToSqlFormat(d);
																String accDate="";
																try{
																accDate=	OtherFunctions.getAccurateAccountingDate(Integer
													.parseInt(accountingDate));
										} catch (NumberFormatException e) {
											accDate = OtherFunctions.getAccurateAccountingDate(20);
										}

										System.out.println("Today " + currentDate);

										System.out.println("Accounting date : " + accDate);
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
						<tr>
							<td></td>
							<td>
								<% 
							String nextMonthFirstDate="";
							String nextMonth;
							Calendar	calNextMonth=Calendar.getInstance();
						    calNextMonth.setTime(OtherFunctions.sqlFormatToDate(currentDate));
								if(accDate.compareTo(currentDate)<=0)
								{								  			    
								    calNextMonth.set(Calendar.MONTH, calNextMonth.get(Calendar.MONTH)+2);
								    int nextMonthInt=calNextMonth.get(Calendar.MONTH)+1;
									   
									   nextMonthFirstDate="01/" + ((nextMonthInt +"").length()==1?"0"+nextMonthInt:nextMonthInt) + "/" +calNextMonth.get(Calendar.YEAR);
									    nextMonth= OtherFunctions.getMonthInString(nextMonthInt	);
									   String accountingDay=accDate.split("-")[2];
								%> 
							  <%
								    
								}else
								{						
								   calNextMonth.set(Calendar.MONTH, calNextMonth.get(Calendar.MONTH)+1);
								   int nextMonthInt=calNextMonth.get(Calendar.MONTH)+1;
								   
								   nextMonthFirstDate="01/" + ((nextMonthInt +"").length()==1?"0"+nextMonthInt:nextMonthInt) + "/" +calNextMonth.get(Calendar.YEAR);
								    nextMonth= OtherFunctions.getMonthInString(nextMonthInt	);
								    %> Note:<i> If you wish to unsubscribe from <%=nextMonth %>,
									select 1st of <%=nextMonth%>.
							</i> <%
								}
								 
									%>

							</td>
						</tr>

					</table>
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
							<td>Unsubscription Requested(dd/mm/yyyy)</td>
							<td>:</td>
							<td><%=OtherFunctions.changeDateFromatToddmmyyyy(dto.getUnsubscriptionDate())%></td>
							<td align="right"><input type="button" class="formbutton"
								onclick="showAuditLog(<%=dto.getSubscriptionID()%>,'<%=AuditLogConstants.UNSUBSCRIPTION_EMP_MODULE%>');"
								value="Audit Log" /></td>


						</tr>
						<tr>
							<td>Unsubscription Effective Date(dd/mm/yyyy)</td>
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
							<td>Unsubscription Requested(dd/mm/yyyy)</td>
							<td>:</td>
							<td><%=OtherFunctions.changeDateFromatToddmmyyyy(dto.getUnsubscriptionDate())%></td>

						</tr>
						<tr>
							<td>Unsubscription Effective Date(dd/mm/yyyy)</td>
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
					<center>You should have active subscription to unsubscribe</center>
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
							.getEmployeeSubscriptionDetails(request.getSession()
									.getAttribute("user").toString());

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


				<h4>Your Subscription Information</h4>


				<table bgcolor="">


					<tr>
						<td>Status</td>
						<td>:</td>
						<td><%=dto.getSubscriptionStatus()%></td>

					</tr>


					<tr>
						<td>Effective Date(dd/mm/yyyy)</td>
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
							String apl = aplDto.getArea() + "->" + aplDto.getPlace()
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
						<td colspan="3" align="center"><input type="button"
							class="formbutton" onclick="goSubscriptionHistory()"
							value=" Subscription History" /> <input type="button"
							onclick="history.go(-1);" class="formbutton" value="Back" /></td>
					</tr>



				</table>

				<%
					} 
				%>
				<!--
                            end s subscription information
                        -->




			</form>


			<%@include file="Footer.jsp"%>


		</div>
	</div>

</body>
</html>
