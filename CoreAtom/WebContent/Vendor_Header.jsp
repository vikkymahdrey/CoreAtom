

<%@page import="com.agiledge.atom.usermanagement.service.UserManagementService"%>
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.UserManagementDAO" %>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />

<div class="container">
	<div class="header">
		<table width="100%">
			<tr>
				<td width="10%"><a href="http://www.agiledgesolutions.com"><img
						src="images/agile.png" /></a></td>

				<td width="90%">
					<div style="float: left">
						<h1 style="color: #FF4000;">ATOm</h1>

						<h4>Agiledge transport optimization manager</h4>
					</div>
					<div id="#header_user_details"
						style="float: right; height: 100%; vertical-align: bottom; font-size: 9;">

						Welcome
						<% 
						String role=session.getAttribute("role").toString();
						try{
							 
						 	//EmployeeDto empDtoInHeader= new EmployeeService().getEmployeeAccurate( session.getAttribute("user").toString()); 
						%>
						<%-- <%=empDtoInHeader.getEmployeeFirstName() + " "+empDtoInHeader.getEmployeeLastName() %>
						(
						<%=OtherFunctions.getRoleName(session.getAttribute("role").toString()) %>
						) --%>


					</div>
				</td>
		</table>
		<div class="clear"></div>
	</div>


	<div class="clr"></div>



	<div id="cssmenu">

		<ul>
 
			<%=OtherFunctions.getLinks(new UserManagementService().getRoleId(role),application )%>
 
		</ul>

	</div>






	<%
		String message = "";

		try {
			message = session.getAttribute("status").toString();

		} catch (Exception e) {
			message = "";
		}
		session.setAttribute("status", "");
		if (message != null && !message.equals("")) {
	%>
	<div id="statusMessage">

		<%=message%>

	</div>
	<%
		}
	%>
</div>
<%
						}catch(Exception e)
						{
						//throw (e);
						}%>
