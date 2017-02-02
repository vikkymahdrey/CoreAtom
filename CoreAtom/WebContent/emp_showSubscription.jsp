
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
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
<title>View Subscription Details</title>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/coin-slider.css" />
<script type="text/javascript" src="js/cufon-yui.js"></script>
<script type="text/javascript" src="js/cufon-titillium-250.js"></script>
<script type="text/javascript" src="js/coin-slider.min.js"></script>


<link href="css/validate.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script src="js/dateValidation.js"></script>

<!-- Beginning of compulsory code below -->
<link href="css/dropdown/dropdown.css" media="screen" rel="stylesheet"
	type="text/css" />

<link href="css/dropdown/dropdown.vertical.css" media="screen"
	rel="stylesheet" type="text/css" />
<link href="css/dropdown/themes/default/default.advanced.css"
	media="screen" rel="stylesheet" type="text/css" />


<!--[if lte IE 7]>
        <script type="text/javascript" src="js/jquery/jquery.js"></script>
        <script type="text/javascript" src="js/jquery/jquery.dropdown.js"></script>
        <![endif]-->


<script src="js/dateValidation.js"></script>

<script type="text/javascript" src="js/jquery-latest.js"></script>
<script>   
    $(document).ready(function(){
    		 
    	setActiveMenuItem();
    });
    
    function setActiveMenuItem()
    {
    	var url=window.location.pathname;
    	var filename=url.substring(url.lastIndexOf('/') + 1);
   //  $("li[class=active']").removeAttr("active");
  		 
  		
  		 
  		 
   		
    	 $("a[href='"+ filename + "']").parent().attr("class", "active");
    		 $("a[href='"+ filename + "']").parent().parent().parent('li').attr("class", "active") ;
    		 
    	 
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
<script type="text/javascript">
function  goSubscriptionHistory()
{
	  location.href="emp_subscriptionHistory.jsp";
	 // window.open("emp_schedulingHistory.jsp");
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
%>





	<div id="body">
		<div id="content">



			<form action="UpdateSubscription" name="subscriptionForm">



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
				<br /> <br />


				<%
					EmployeeSubscriptionDto dto = new EmployeeSubscriptionService()
							.getEmployeeSubscriptionDetails(request.getSession()
									.getAttribute("user").toString());

					if (dto != null) {
				%>


				<h3>Your Subscription Information</h3>
				<hr />

				<table bgcolor="">
					<tr>
						<td>Effective Date(dd/mm/yyyy)</td>
						<td>:</td>
						<td><%=OtherFunctions.changeDateFromatToddmmyyyy(dto.getSubscriptionFromDate())%></td>
						<td></td>
						<td></td>
					</tr>




					<tr>
						<td><%=SettingsConstant.hrm%></td>
						<td>:</td>
						<td>
							<%
								EmployeeDto employeeDto1 = new EmployeeService()
											.getEmployeeAccurate(dto.getSupervisor1());
						//			System.out.println("Supervisor 1 in jsp : "
							//				+ dto.getSupervisor2());
							%> <%=employeeDto1.getEmployeeFirstName() + " "
						+ employeeDto1.getEmployeeLastName()%>

						</td>
						<td></td>
					</tr>
					<tr>
						<td>Spoc</td>
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
						<td></td>
					</tr>

					<tr>
						<td>Schedule Status</td>
						<td>:</td>
						<td><%=
								dto.getScheduleStatus()
						%></td>
						<td></td>
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
						<td></td>
					</tr>
					<tr>
						<td colspan="4" align="center"><input type="button"
							class="formbutton" onclick="goSubscriptionHistory();"
							value="Subscription History " /> <input type="button"
							class="formbutton" onclick="javascript:history.go(-1);"
							value="Back" /> <input type="button" value="Edit "
							class="formbutton"
							onclick="javascript: location.href=' emp_modifySubscription.jsp';"
							class="formbutton" /> <input type="button" class="formbutton"
							onclick="showAuditLog(<%=dto.getSubscriptionID()%>,'<%=AuditLogConstants.SUBCRIPTION_EMP_MODULE%>');"
							value="Audit Log" />
							<%
								/*
								try {
										if (new EmployeeSubscriptionService()
												.isSubscriptionRequestMade(request.getSession()
														.getAttribute("user").toString())
												|| new EmployeeService()
														.hasPrivilegeToChangeAddress(request
																.getSession().getAttribute("user")
																.toString())) {
	 
							
							<input type="button" value="Edit " class="formbutton"
							onclick="javascript: location.href=' emp_modifySubscription.jsp';"
							class="formbutton" /> <%
 	}
 		} catch (Exception e) {
 			System.out.println(" Error " + e);
 		}
							
							*/
 %></td>
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
