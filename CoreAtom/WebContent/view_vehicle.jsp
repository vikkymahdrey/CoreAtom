<%-- 
    Document   : vehicle_type
    Created on : Oct 19, 2012, 11:09:44 AM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.dao.VehicleDao"%>
<%@page import="com.agiledge.atom.dto.VehicleDto"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
function updateStatus(id,curstatus)
{
	 var msg="Do you really want to disable ? ";
	 $("input[name=vehid]").val(id);
	 $("input[name=active]").val(curstatus);
	 if(curstatus=='c')
		 msg="Do you really want to enable ? ";
	 if(confirm(msg))
	 { 
	 document.getElementById("statusform").submit(); 
	 }
}
</script>
<title>View Vehilce</title>
</head>

<body>
	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		ArrayList<VehicleDto> vehicleDtos=new VehicleDao().getAllVehicle();
		String status="ENABLED",btnvalue="DISABLE";
		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
	%><%@include file="Header.jsp"%>
	<%
		}
	%>

	<div id="body">
		<div class="content">
			<h3>View  Vehicle</h3>
			<hr />
			<div> <input type="button" class="formbutton" onclick="location.href='addvehicle.jsp'" value="New"> </div>
			<form name="statusform" id="statusform" action="UpdateVehicleStatus" method="post">
		<input type="hidden" id="vehid" name="vehid"/>
		<input type="hidden" id="active" name="active"/>
 		</form>
 
				<table width="70%">
				<tr>
				<th>VehicleNo</th>
				<th >Vehicle Type</th>
				<th>Actions</th>
				</tr>
				<% for(VehicleDto vehicleDto: vehicleDtos){%>															
					<tr>
						<td><%=vehicleDto.getVehicleNo() %></td>
						<td><%=vehicleDto.getVehicleType()%></td>
						<%
						if(vehicleDto.getStatus().equalsIgnoreCase("a"))
						{
							status="ENABLED";
							btnvalue="DISABLE";
						}
						else
						{
							status="DISABLED";
							btnvalue="ENABLE";
						}
						%>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=status%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="formbutton" value="<%=btnvalue%>" onClick="updateStatus('<%=vehicleDto.getId() %>','<%=vehicleDto.getStatus() %>')" /></td>
					</tr>
					<%} %>					
				</table>	
						

			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>

</html>
