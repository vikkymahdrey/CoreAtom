<%@page import="com.agiledge.atom.dto.VehicleDto"%>
<%@page import="com.agiledge.atom.service.RouteService"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.VehicleDao"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script>
function searchForm() {
	document.siteSearchForm.action = "vehicleShiftMap.jsp";
	document.siteSearchForm.submit();
	return false;
}
function validate(){
	var vehicle=document.getElementById("vehicle").value;
	var routeIn=document.getElementById("routeIn").value;
	var routeOut=document.getElementById("routeOut").value;
	var inT=document.getElementById("in").value;
	var outT=document.getElementById("out").value;
	if(vehicle=="NO" || vehicle==null){
		alert("choose Vehicle");
		return false;
	}else if(inT==null){
		alert("Choose atlest 1 In time");
		return false;
	}else if(outT==null){
		alert("Choose atlest 1 Out time");
		return false;
	}else if(routeIn==null || routeIn=="NO" || routeOut=="NO" || routeOut==null){
		alert("Choose route");
		return false;
	}else
		return true;
}

</script>
<title>Vehicle Mapping</title>
</head>
<body>
			<%
			   long empid=0;
	        String employeeId = OtherFunctions.checkUser(session);
	        if (employeeId == null||employeeId.equals("null") ) {
	            String param = request.getServletPath().substring(1) + "___"+ request.getQueryString(); 	response.sendRedirect("index.jsp?page=" + param);
	        } else {
	            empid = Long.parseLong(employeeId);}
	            %>
			<%@include file="Header.jsp"%>
		

	<% 	String site="";
	boolean flag=true;
if(request.getParameter("site1")!=null){
		site=request.getParameter("site1");
		flag=false;
	}
		List<SiteDto> siteList = new SiteService().getSites();
		ArrayList<VehicleDto> vehicles1=null;
				
		ArrayList<LogTimeDto> inLog= new LogTimeService().getAllLogtime("IN",site);
		ArrayList<LogTimeDto> outLog= new LogTimeService().getAllLogtime("OUT",site);
		ArrayList<RouteDto> routes = new RouteService().getAllRoutes(site);
		try{
			vehicles1=new VehicleDao().getAllVehicle();
	}catch(Exception e){e.printStackTrace();}
		
	%>	<div id="body">
		<div class="content">
			<div><%if(flag){ %>
					<form name="siteSearchForm">
					<table style="width: 40%; border: 0px none;">
						<tr>
							<td width="20%">&nbsp;&nbsp;Site</td><td>
							
				<select name="site1" id="site1" onchange="return searchForm();">
									<option value="">--select--</option>
									<%
										for (SiteDto dto : siteList) {
											%>

											<option  value="<%=dto.getId()%>">
												<%=dto.getName()%>
											</option>


											<%
												}
											%>
									</select></td>
</tr></table></form>
<%
			}else{
%>
			<form name="vehicleMap" method="post" action="VehicleShiftMap"
				onsubmit="return Validate()">
				<table style="width: 100%; border: 0px none;  padding-right: 20%; padding-left: 20%;" align="center">
				<tr>
				<td ><strong>select site</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<select id="site" name="site"><option value="NO">select</option>
				<%
										for (SiteDto dto : siteList) {
											if(dto.getId().equalsIgnoreCase(site)){
											%>
										<option  value="<%=dto.getId()%>" selected>
												<%=dto.getName()%>
											</option>
											<%}else{ %>
											<option  value="<%=dto.getId()%>">
												<%=dto.getName()%>
											</option>


											<%
												}}
											%>
				</select></td>
				<td><strong>choose vehicle</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<select id="vehicle" name="vehicle"><option value="NO">select</option>
				<%for(VehicleDto vdto : vehicles1){ %>
					<option  value="<%=vdto.getId()%>">
												<%=vdto.getVehicleNo()%>
				<%} %>
				</select></td>
				</tr>
				<tr>
				<td><strong>IN </strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select id="in" name="in"  multiple="multiple">
				<%for(LogTimeDto in : inLog){
					%>
				<option value="<%=in.getId()%>"><%=in.getLogTime()%></option>
				<%
				}
			 %> 
				</select></td>
				<td><strong>OUT</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select id="out" name="out"  multiple="multiple">
				<%for(LogTimeDto Out : outLog){
					%>
				<option value="<%=Out.getLogTime()%>" ><%=Out.getLogTime()%></option>
				<%
				}
			 %> 
				</select></td>
				</tr>
				<tr>
				<td><strong>Route</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select id="routeIn" name="routeIn"><option value="NO">select</option>
				<% for(RouteDto rdto : routes){
										
										%>
									<option value="<%=rdto.getRouteId()%>"><%=rdto.getRouteName()%>
									</option>
									<%
									}
								 %>
				</select></td>
				<td><strong>Route</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select id="routeOut" name="routeOut"><option value="NO">select</option>
				<% for(RouteDto rdto : routes){
										
										%>
									<option value="<%=rdto.getRouteId()%>"><%=rdto.getRouteName()%>
									</option>
									<%
									}
								 %>
				</select></td>
				</tr>
				<tr>
				<td align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" class="formbutton" value="save" onclick="return validate();"/>&nbsp;&nbsp;
				<input type="reset" class="formbutton" value="Reset"></td>
				</tr>
					<tbody>
					</tbody>
				</table>
			</form>
			<%} %>
			</div>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>