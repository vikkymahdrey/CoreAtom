<%-- 
    Document   : vehicle_type_list
    Created on : Oct 19, 2012, 6:06:08 PM
    Author     : muhammad
--%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<Script>
function showAuditLog(relatedId,moduleName){
	var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var size = "height=450,width=900,top=200,left=300," + params;
	var url="ShowAuditLog.jsp?relatedNodeId="+relatedId+"&moduleName="+moduleName;	
    newwindow = window.open(url, 'AuditLog', size);

	if (window.focus) {
	newwindow.focus();
}
}
</Script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Vehicle Type List</title>
</head>
<body>
	<%

            ArrayList<VehicleTypeDto> vehicleTypeDtoList = new VehicleTypeDao().getAllVehicleType();

        %>
	<%
        long empid=0;
        String employeeId = OtherFunctions.checkUser(session);
       
            empid = Long.parseLong(employeeId);
            %>
	<%@include file="Header.jsp"%>

	<div id="body">
		<div class="content">
			<h3>Vehicle List</h3>
			<hr />
			<table width="60%">
				<thead>
					<th>Vehicle</th>
					<th>Seat</th>
					<th>Sitting Capacity</th>
					<th></th>
					<th align="center">Audit Log</th>
				</thead>
				<tbody>
					<tr>
						<%

                    for (VehicleTypeDto vehicleTypeDto : vehicleTypeDtoList) {
                   //     System.out.println("ID"+vehicleTypeDto.getId());
                %>
						<td align="center"><%=vehicleTypeDto.getType()%></td>
						<td align="center"><%=vehicleTypeDto.getSeat()%></td>
						<td align="center"><%=vehicleTypeDto.getSittingCopacity()%></td>
						<td>
							<form>
								<input type="button" class="formbutton" value="Modify"
									onclick="javascript: window.location='vehicle_type_modify.jsp?id=<%=vehicleTypeDto.getId()%>';"
									value="Modify" />
							</form>
						</td>
						<td align="center"><input type="button" class="formbutton"
							onclick="showAuditLog(<%=vehicleTypeDto.getId() %>,'<%=AuditLogConstants.VEHICLE_MODULE%>');"
							value="Audit Log" /></td>

					</tr>
					<%}
            %>
					<tr>
						<td align="center" colspan="4">
							<form>
								<input type="button" class="formbutton"
									onclick="javascript:history.go(-1);" value="Back" />
									&nbsp;
									<input type="button" class="formbutton"
									onclick="location.href='vehicle_type.jsp'" value="New" />
							</form>
						</td>
					</tr>
				</tbody>
			</table>

			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
