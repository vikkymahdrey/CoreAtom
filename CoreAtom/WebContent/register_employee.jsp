<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dao.EmployeeDao"%>
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.dao.SettingsDoa"%>
<%@page import="com.agiledge.atom.dto.SettingsDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Insert title here</title>
</head>
<body>
<script type="text/javascript">
		function goHome(){
			window.location="employee_home.jsp";
		}
		
	</script>
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

						EmployeeDto empDtoInHeader=null; 
						try{
							
								String role=session.getAttribute("role").toString();
								if(empDtoInHeader==null) {
									empDtoInHeader = new EmployeeService().getEmployeeAccurate(session.getAttribute("user").toString());
								}
								
							}catch(NullPointerException ne) {
						 
								empDtoInHeader = new EmployeeService().getEmployeeAccurate(session.getAttribute("user").toString());
							}
						session.setAttribute("userDto",empDtoInHeader);
						 
						%>
						<%=empDtoInHeader.getDisplayName() %>
						(
						<%=OtherFunctions.getRoleNamebyId(empDtoInHeader.getRoleId()) %>
						)


					</div>
				</td>
		</table>
		<div class="clear"></div>
	</div>
	<%
		long empid = 0;

		String employeeId = OtherFunctions.checkUser(session);

		empid = Long.parseLong(employeeId);
		String loginId=request.getParameter("loginId");
		EmployeeDto dto=new EmployeeDao().getEmployeeFullInfoByEmpId(employeeId);
		
	%>

	<%
	/*	OtherDao ob = null;
		ob = OtherDao.getInstance();
		out.println(ob.getEmployeeDet(empid));
		String empSite = (new EmployeeService()
				.getEmployeeAccurate(employeeId)).getSite();
		ArrayList<SettingsDTO> transportType = new SettingsDoa()
				.getSettingsStrings(SettingsConstant.TRANSPORT,
						SettingsConstant.TYPE);
		*/
	%>
<h3 align="center"> Register in ATOm</h3>
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
	<form id="regietremployee" action="RegisterEmployee" method="post">
		<table style="width: 40%;margin-left: auto;margin-right: auto;border: outset;">
		<tr><th colspan="3" align="center">To Use ATOm please Register 
		<input type="hidden" name="loginId" value="<%=loginId %>"/>
		</th></tr>
		<%try{ 	
		%>
		<%System.out.println("aaaaaaaaaaaaaaa"); %>
		<tr><td align="right">Employee Code</td><td>&nbsp;</td><td><%=dto.getPersonnelNo()%></td></tr>
		
		<tr><td align="right">Name </td><td>&nbsp;</td><td><%=dto.getDisplayName() %></td></tr>
		<tr><td align="right">Reporting Officer</td><td>&nbsp;</td><td><%=dto.getLineManager()%></td></tr>
		<tr><td align="right"><%=SettingsConstant.PROJECT_TERM%> Name</td><td>&nbsp;</td><td><%=dto.getProjectid()%></td></tr>		
		<tr><td align="right"></td><td>&nbsp;</td><td><% %></td></tr>
		<tr><td align="right"></td><td>&nbsp;</td><td><% %></td></tr>
		<tr><td align="right"></td><td>&nbsp;</td><td><% %></td></tr>
		<tr><td align="right"></td><td>&nbsp;</td><td><% %></td></tr>
		<tr><td align="right"></td><td>&nbsp;</td><td><% %></td></tr>
		<%}catch(Exception e) {System.out.println("Error"+e);}%>
		<tr><td></td><td>&nbsp;</td><td>
		<%if(empDtoInHeader.getRegisterStatus()!=null && empDtoInHeader.getRegisterStatus().equalsIgnoreCase("a")) {%>
		<input type="button" value="Go Home" name="home" class="formbutton" onclick="goHome()"/>
		<%} else {%>
		<input type="submit" value="Register" name="register" class="formbutton"/>
		<%} %>		
		</td></tr>
		</table>
		</form>
</body>
</html>