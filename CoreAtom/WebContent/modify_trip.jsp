<%-- 
    Document   : modify_trip
    Created on : Oct 31, 2012, 6:58:21 PM
    Author     : muhammad
--%>

<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dao.ModifyTripDao"%>
<%@page import="com.agiledge.atom.dto.ModifyTripDto"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="javax.swing.text.html.Option"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="com.agiledge.atom.dao.TripDetailsDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Modify Trip</title>

<script type="text/javascript">	
	function selectTrip() {
		var emplist;
		var isSecNeeded=document.getElementById("isSecNeeded").value;
		var tripMode=document.getElementById("tripMode").value;
		
		for (var j=0;j<4;j++) {
			emplist=document.getElementById("trip"+j);
			var vehicleType=document.getElementById("vehicleType"+j).value;
			var escort=document.getElementById("escort"+j).value;
			var vehicleSeat=vehicleType.split(":")[1];
			if((vehicleSeat<emplist.options.length)||(vehicleSeat==emplist.options.length&&escort=="YES"))
				{
				alert("No:"+parseInt(j+1)+" Trip Have Not enough Seat");
				return false;
				}
			var emplistLength = emplist.options.length;
			
			if(emplist!=null&&emplistLength>0)
				{
				var lastDropEmpgender=emplist.options[emplistLength-1].value.split(":")[3];
				var firstPickupEmpgender=emplist.options[0].value.split(":")[3];	
				if(escort=="NO"&&isSecNeeded=="1"&&((tripMode=="IN"&&firstPickupEmpgender=="F")||(tripMode=="OUT"&&lastDropEmpgender=="F")))
						{
					alert("No:"+parseInt(j+1)+" Trip Escort Required");
					return false;	
						}
				
			for ( var i = 0; i < emplistLength; i++) {
				
				emplist.options[i].selected = true;
			}
			
				}
			
		}	
		return true;
	}
	function selectNewTrip() {
		var newEmplist;
		var newEmplistLength;
		newEmplist = document.getElementById("empids");
		newEmplistLength = document.getElementById("empids").options.length;

		for ( var i = 0; i < newEmplistLength; i++) {
			newEmplist.options[i].selected = true;
		}
	}

	function saveTrip() {
		try {			
			var notIncludeEmps = document.getElementById("empids");
			if(notIncludeEmps!=null&&notIncludeEmps.options.length>0)
				{
				alert("Some employees are not Included in any trip");				
				}
			else
				{
			if(selectTrip())
				{
				selectNewTrip();
				document.tripmodifyform.submit();
				}
				}		
		} catch (e) {
			alert(e)
		}
	}

	
	function listMoveDown(iVal) {
		
		try {
			var listEmps = document.getElementById("trip"+iVal);	
			if (listEmps.selectedIndex >= 0) {
				var optionSelected = listEmps.selectedIndex;
				var optionNewValue = listEmps.options[optionSelected+1].value;
				var optionNewText = listEmps.options[optionSelected+1].innerHTML;											
				listEmps.options[optionSelected+1].value = listEmps.options[optionSelected].value;
				listEmps.options[optionSelected+1].text = listEmps.options[optionSelected].innerHTML;
				listEmps.options[optionSelected].value =optionNewValue;
				listEmps.options[optionSelected].text=optionNewText;
				listEmps.selectedIndex=listEmps.selectedIndex+1;				
			}
		} catch (e) {
			//alert(e);
		}

	}
	
	function listMoveRight(iVal) {	
		try{
		var empList = document.getElementById("empids");
		var trip = document.getElementById("trip" + iVal);
		
		var optionSelected = empList.selectedIndex;	
				var optionNew = document.createElement('option');
				optionNew.value = empList.options[optionSelected].value;
				optionNew.text = empList.options[optionSelected].innerHTML;
				try {
					trip.add(optionNew, null);
				} catch (e) {
					trip.add(optionNew);
				}		
				empList.options[optionSelected] = null;
		}catch(e){alert(e);}
			
	}
	function listMoveLeft(iVal) {
		try{
			var empList = document.getElementById("empids");
			var trip = document.getElementById("trip" + iVal);
			
			var optionSelected = trip.selectedIndex;	
					var optionNew = document.createElement('option');
					optionNew.value = trip.options[optionSelected].value;
					optionNew.text = trip.options[optionSelected].innerHTML;
					try {
						empList.add(optionNew, null);
					} catch (e) {
						empList.add(optionNew);
					}		
					trip.options[optionSelected] = null;
			}catch(e){alert(e);}
	}
	function deleteEmp()
	{
		var optionSelected = empids.selectedIndex;
		var optionNew = document.createElement('option');
		optionNew.value = empids.options[optionSelected].value;
		optionNew.text = empids.options[optionSelected].innerHTML;
		empids.remove(optionSelected);
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
		TripDetailsDto tripDetailsDtoObj = null;
		String siteId = request.getParameter("siteId");

		String tripDate = request.getParameter("tripDate");
		String tripMode = "" + request.getParameter("tripMode");
		String tripTime = "" + request.getParameter("tripTime");
		String tripIds[] = request.getParameterValues("tripidcheck");
		int isSecNeeded=new TripDetailsDao().checkSecurity(siteId,tripTime);
		VehicleTypeDao vehicleTypeDao = new VehicleTypeDao();
		ArrayList<TripDetailsChildDto> tripDetailsChildDtoList = new ArrayList<TripDetailsChildDto>();
		ArrayList <VehicleTypeDto> vehicleTypeDtos=null;
		ArrayList<TripDetailsChildDto> removedEmployee=null;
		vehicleTypeDtos = vehicleTypeDao.getAllVehicleTypeBySite(Integer.parseInt(siteId));
		try
		{
		
		removedEmployee = new TripDetailsDao().getTripSheetRemoved(tripDate,siteId, tripMode,tripTime);
		}catch(Exception e)
		{
		System.out.println("error "+e);	
		}
	%>

	<div id="body">
		<div class="content">
			<form action="UpdateTrip" name="tripmodifyform" id="tripmodifyform"
				method="POST">
				<h3>Edit Trip Sheet</h3>
				<p>
					<input type="button" class="formbutton" value="Back"
						onclick="javascript:history.go(-1);" />
						<input type="hidden" name="isSecNeeded" id="isSecNeeded" value="<%=isSecNeeded%>"/>
				</p>
				<input type="hidden" name="tripDate" value="<%=tripDate%>" /> <input
					type="hidden" name="siteId" value="<%=siteId%>" /> <input
					type="hidden" name="tripMode" id="tripMode" value="<%=tripMode%>" /> <input
					type="hidden" name="tripTime" value="<%=tripTime%>" /> 
				<input type="hidden" name="src" id="src" />
				<table border="0" >
					<tbody>
						<tr>
							<td valign="top" rowspan="40" width="40%">
									<%for(String tripId:tripIds)
									{
									%>
							<input	type="hidden" value=<%=tripId %> name="tripIds" id="tripIds" />
							<%} %>
							
							
							
							<select name="empids" id="empids" multiple="" size="25">
						<%		if(removedEmployee!=null&&removedEmployee.size()>0)
									{										
										for (TripDetailsChildDto tripDetailsChildDto : removedEmployee) {									
										%>
										<option
										value="<%=tripDetailsChildDto.getEmployeeId() + ":"
							+ tripDetailsChildDto.getLandmarkId() +":"+tripDetailsChildDto.getScheduleId()%>">
										<%=tripDetailsChildDto.getEmployeeName() + "--"
							+ tripDetailsChildDto.getArea() + "--"
							+ tripDetailsChildDto.getPlace() + "--"
							+ tripDetailsChildDto.getLandmark()%></option>
								<%}
								}
						 %>
								</select>
								<br/>
							<input type="button" class="formbutton" value="Add Employee"
								onclick="window.open('getemployee.jsp','Ratting','width=500,height=400,left=150,top=200,toolbar=1,status=1,');" />														
						
					     	<input type="button" class="formbutton" value="Delete Employee"
								onclick="deleteEmp()" />														
				     	   
							</td>
							</tr>		
																	
							<%
							 
								
								for(int i=0;i<4;i++)
									{
									%>
									<tr>
									<td width="5%" rowspan="2">							
								<p>
									<input type="button" class="formbutton" name="right"
										value="&rArr;" onclick="listMoveRight(<%=i %>)" />
								</p>													
								<p>
									<input type="button" class="formbutton" name="left"
										value="&lArr;" onclick="listMoveLeft(<%=i %>)" />
								</p>
								
							</td>
							<td>	
								<select id="trip<%=i%>"
								name="trip<%=i%>" multiple="" size="3" style="width: 300px">
									
									
									<%
									if(i<tripIds.length)
									{	
								String tripId = tripIds[i];
									tripDetailsDtoObj = new TripDetailsDao()
											.getTripSheetByTrip(tripId);
									tripDetailsChildDtoList = tripDetailsDtoObj
											.getTripDetailsChildDtoList();								
									
									
							%>																					
									<%
										for (TripDetailsChildDto tripDetailsChildDto : tripDetailsChildDtoList) {
									%>
									<option
										value="<%=tripDetailsChildDto.getEmployeeId() + ":"
							+ tripDetailsChildDto.getLandmarkId() +":"+tripDetailsChildDto.getScheduleId()+":"+tripDetailsChildDto.getGender()%>">
										<%=tripDetailsChildDto.getEmployeeName() + "--"
							+ tripDetailsChildDto.getArea() + "--"
							+ tripDetailsChildDto.getPlace() + "--"
							+ tripDetailsChildDto.getLandmark()%></option>
									<%
										}
								
								}
									%>
							
						
								</select>
															
									<input type="button" class="formbutton" name="down"
										value="&darr;" onclick="listMoveDown(<%=i %>)" />														
								</td>
								<td>Escort
								<select name="escort<%=i%>" id="escort<%=i%>"><option value="NO">NO</option><option value="YES">YES</option></select></td>
								<td>Vehicle Type<select name="vehicleType<%=i%>" id="vehicleType<%=i%>">
								<%for(VehicleTypeDto vehicleTypeDto:vehicleTypeDtos) {%>
								<option value="<%=vehicleTypeDto.getId()+":"+vehicleTypeDto.getSittingCopacity()%>"><%=vehicleTypeDto.getType() %></option>
								<%} %>
								</select></td>	
																		
						</tr>
						<tr>		
							<td>&nbsp;</td>	
							</tr>
						<%} %>												
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td align="center"><input type="button"
								class="formbutton" value="Save" name="savebtn" id="savebtn"
								onclick="saveTrip()" /></td>													
						</tr>
						<tr>

							<td colspan="4" align="center"></td>
						</tr>						
					</tbody>
				</table>
			</form>
			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
