<%-- 
    Document   : vehicle_type
    Created on : Oct 19, 2012, 11:09:44 AM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
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
<title>Add Vehicle</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/validate.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<script src="js/JavaScriptUtil.js"></script>
<script src="js/Parsers.js"></script>
<script src="js/InputMask.js"></script>
<script type="text/javascript">

	$(document).ready(function() {
		maskVechicleNo();		
});
		function maskVechicleNo() {	
	$("#vehicleNo").each(
			function() {
				var name = $(this).attr("name");
				new InputMask([ fieldBuilder.upperLetters(2, 2),
								fieldBuilder.literal(" "),
								fieldBuilder.inputNumbers(1, 2),							
								fieldBuilder.literal(" "),
								fieldBuilder.literal(""),
								fieldBuilder.upperLetters(1, 3),
	 
								fieldBuilder.literal(" "),

						fieldBuilder.inputNumbers(1, 4) ], name);
			});
		}
            function validate()
            {  
                var vehicleType=$("input[id=vehicleType]").val();
                var vehicleNo=$("input[id=vehicleNo]").val();
                try{            
            if(vehicleType=="")
                        {
                        alert("Vehicle type should not be blank !");
                        return false;
                        }
            if(vehicleNo=="")
            {
            alert("select vehicle no");
            return false;
            }
               else
                  {
                  return true;
                  }
            return false;    
            }catch(e)
                {
                    alert(e)
                    return false;
                }
            }
        </script>
</head>

<body>
	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		ArrayList<VehicleTypeDto> vehicleTypeDtos=new VehicleTypeDao().getAllVehicleType();
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
			<h3>Add Vehicle</h3>
			<hr />
			<form name="vehicleadd" action="AddVehicle" method="post"
				onsubmit="return validate()">

				<table width="70%">
					<tr>
						<td align="center">VehicleNo</td>
						<td><input type="text" name="vehicleNo" id="vehicleNo" /></td>
					</tr>
					<tr>
						<td align="center">Vendor</td>
						<td><select name="vendor" id="vendor">
						<%
							ArrayList<VendorDto> vendorDtos = new VendorService().getMasterVendorlist();
						%>
								<option></option>
								
								<%
								if(vendorDtos!=null&&vendorDtos.size()>0)
								{
								for(VendorDto vendorDto:vendorDtos) {%>
								<option value="<%=vendorDto.getCompanyId()%>"><%=vendorDto.getCompany()%></option>
								<%} 
								
								}%>
						</select></td>
					</tr>
					<tr>
						<td align="center">Vehicle Type</td>
						<td><select name="vehicleType" id="vehicleType">
						<option></option>
								<%for(VehicleTypeDto vehicleTypeDto:vehicleTypeDtos) {%>
								<option value="<%=vehicleTypeDto.getId() %>" ><%=vehicleTypeDto.getType()%></option>
								<%} %>
						</select></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="submit" class="formbutton" name="submitbtn"
							value="Submit" /> <input type="button" class="formbutton"
							onclick="javascript:history.go(-1);" value="Back" /></td>
					</tr>
				</table>
			</form>

			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>

</html>
