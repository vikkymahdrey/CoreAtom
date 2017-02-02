


 
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.usermanagement.service.PageService"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />

<div class="container">
	<div class="header">
	<div style="float: right; font-size: 9;">
								<%

						EmployeeDto empDtoInHeader=null; 
						try{
							
								String role=session.getAttribute("role").toString();
							try {
						 		System.out.println("BEFORE SEsion");
								empDtoInHeader = (EmployeeDto)session.getAttribute("userDto");
								System.out.println("AFTER SEsion");
								if(empDtoInHeader==null) {
									empDtoInHeader = new EmployeeService().getEmployeeAccurate(session.getAttribute("user").toString());
								}
								
							}catch(NullPointerException ne) {
						 
								empDtoInHeader = new EmployeeService().getEmployeeAccurate(session.getAttribute("user").toString());
							}
						 
						 
						%>
						
						

	</div>
						<div style="float:right; ">
					<div style="display:inline;"> <a href="<%=new PageService().getHomePageUrl(empDtoInHeader).getUrl()%>">Home</a>  &nbsp;&nbsp;|&nbsp;&nbsp;<a href="Logout" >Logout</a> </div>
						

						Welcome
						<%if(SettingsConstant.showDesigOrRole.equalsIgnoreCase("designation")){ %>
		<%=empDtoInHeader.getDisplayName()+"("+empDtoInHeader.getDesignationName()+")"%>				
		<%}else{ %>
						<%=empDtoInHeader.getDisplayName()+"("+OtherFunctions.getRoleNamebyId(empDtoInHeader.getRoleId()) +")"%>
<%} %>
					
					
					</div>
	
		<table>
			<tr>
				<td><a href="http://www.agiledgesolutions.com"><img
						src="images/agile.png" /></a></td>

				<td>
					<div style="float: left">
						<h1 style="color: #FF4000;">ATOm</h1>						
						<h4>Agiledge Transport Optimization manager</h4>
						
					</div>					
					</td>

					<td style="float: right;">									
					<%if(SettingsConstant.comp.equalsIgnoreCase("cd")||SettingsConstant.comp.equalsIgnoreCase("cd1")) {%>
<img style="float:right: ;" alt="" src="CROSSDOMAIN_logosmall.jpg">
<%} else{%>
<img style="float:right: ;" alt="">
<%} %>
					
				</td>
				</tr>
		</table>
		<div class="clear"></div>
	</div>


	<div class="clr"></div>



	<div id="cssmenu">

		<ul>	
					<%
				String menu = OtherFunctions.getLinks (empDtoInHeader.getRoleId(), application);
				out.write(menu);%> 
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
						System.out.println("Exception in Header.jsp " + e);
						}%>
