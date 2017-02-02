<%--
    Document   : emp_subscription
    Created on : Aug 28, 2012, 12:51:01 PM
    Author     : 123
--%>

<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@page import="com.agiledge.atom.service.SchedulingService"%>
<%@page import="com.agiledge.atom.dto.ScheduledEmpDto"%>
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
<title>Subscription History</title>

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


			<form action="SubscriptionHistory" name="subscriptionHistoryForm">

				<%
                
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
				<h3>Subscription History</h3>
				<%

                      List<  EmployeeSubscriptionDto > subscriptionList = new EmployeeSubscriptionService().getEmployeeSubscriptionsList( employeeId);

                        if (subscriptionList.size()>0) {
                    %>




				<table class="dataTable">

					<thead>
						<tr>

							<th>Subscription ID</th>
							<th>Subscription Date(dd/mm/yyyy)</th>
							<th>Effective Date(dd/mm/yyyy)</th>
							<th>Supervisor</th>
							<th>SPOC</th>
							<th>Area</th>
							<th>Place</th>
							<th>Landmark</th>
							<th>Status</th>
							<th>Un Subscription Date(dd/mm/yyyy)</th>
							<th>Action</th>
							 
						</tr>
					</thead>
					<tbody>
						<%
                            for(EmployeeSubscriptionDto subDto: subscriptionList)
                                {

                            %>

						<tr>

							<td><%=subDto.getSubscriptionID() %></td>

							<td><%=OtherFunctions.changeDateFromatToddmmyyyy(subDto.getSubscriptionDate())%></td>
							<td><%=OtherFunctions.changeDateFromatToddmmyyyy(subDto.getSubscriptionFromDate())%></td>
							<td><%=subDto.getSupervisor().getDisplayName()%></td>
							<td><%= subDto.getSpoc().getDisplayName() %></td>
							<td><%= subDto.getApl().getArea() %></td>
							<td><%= subDto.getApl().getPlace() %></td>
							<td><%= subDto.getApl().getLandMark() %></td>
							<td><%=subDto.getSubscriptionStatus()%></td>
							<td>
								<%
                                String status="" + EmployeeSubscriptionDto.status.UNSUBSCRIBED;
                                 boolean unsubscriptionrequestmade= new EmployeeSubscriptionService().isUnsubscriptionRequestMade(Long.toString(empid));

                                    if(subDto.getSubscriptionStatus().equals(status )|| unsubscriptionrequestmade)
                                        {
                                 %> <%=OtherFunctions.changeDateFromatToddmmyyyy(subDto.getUnsubscriptionDate())%>
								<%
                                        }else
                                            {
                                    %> &nbsp; <%
                                    }
                                    %>
							</td>
							<td>
								<%
                                    if(new SchedulingService().isScheduled(subDto.getSubscriptionID()))
                                        {
                                %> <a
								href="emp_schedulingHistory.jsp?subid=<%=subDto.getSubscriptionID() %>">View
									Schedule</a> <%
                                    }else
                                    {
                                %> No Schedule <%   } %>
							</td>
							<td>											
								
							</td>
						</tr>
						<%
                            }
                        %>
					</tbody>
				</table>
				<%

                        }
                    %>
				<div align="center">
					<input type="button" class="formbutton"
						onclick="javascript:history.go(-1);" value="Back" />

				</div>
				<hr />
			</form>



			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
