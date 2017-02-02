 <%@page import="javassist.bytecode.stackmap.BasicBlock.Catch"%>
<%@page import="com.agiledge.atom.dto.EscortDto"%>
<%@page import="com.agiledge.atom.escort.service.EscortService"%>
<%@page import="com.agiledge.atom.service.VehicleService"%>
<%@page import="com.agiledge.atom.dto.DriverVehicleDto"%>
<%@page import="com.agiledge.atom.dto.DriverDto"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dto.VehicleDto"%>
<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dao.TripDetailsDao"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="java.sql.*"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.text.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Trip Sheet</title>





<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/validate.js"></script>

<script type="text/javascript">
			$(document).ready(function() {
				$(".vehicleclass").change(function() {
					
					var vehicleid=$(this).attr("id");
					var driverId="driver_"+vehicleid.split("_")[1];
					$("#"+driverId+" option").remove();		
					
					var vehicle = $("#"+vehicleid+"").val();
					var countof=$(" select[value="+vehicle+"][class=vehicleclass] option:selected").toArray();
					if(vehicle!=null && vehicle!="" && countof.length>1)
						{
						$(this).val("");
						alert("The vehicle is already seleced");												
						}
					else
						{										
					$.ajax({
						type : "POST",
						url : "GetVehicleDriver",
						data : {
							vehicle : vehicle,
							
						},
						dataType: "text"
					}
					).done(function(result) {						
							$("#"+driverId).append(result);
						});
						}
				});
				
				
			});
			
			function sendTripSms(tripId1) {
				 var driver=$("[name=driver"+tripId1+"]").val();
				 var vehicle=$("[name=vehicle"+tripId1+"]").val();
				 var isescort=$("[name=isSecurity"+tripId1+"]").val();
				 var escort="";
				 try{
				 escort=$("[name=escort"+tripId1+"]").val();
				 }catch(e)
				 {
					 alert(e);
				 }
				 
				 if(driver==null||driver=="" ||driver=="null"||vehicle==null||vehicle=="" ||vehicle=="null")
					 {
					 alert("driver or vehicle not set");
					 }
				 else if(driver!=null&&isescort=="YES"&&(escort==null||escort=="" ||escort=="null"))
					 {					 					 
					alert("Escort Not Set");	 					 
					 }
				 else
					 {
					
				  $.ajax(
					 {
					         
					        type: "POST",
					        url: "SendSmsToDriver",
					        processData: true,
					         data:{trip:""+tripId1,vehicleid:""+vehicle,driverid:""+driver,escortid:""+escort}
					         
					 
					} 		
				).done(function(msg) {
					alert(msg);
				});	
					 }				 
			}
			
			function sendTripSmsToEscort(tripId1) {
				//	 alert(tripId1);
					  $.ajax(
						 {
						         
						        type: "POST",
						        url: "SendSmsToEscort",
						        processData: true,
						         data:{trip:""+tripId1}
						         
						 
						} 		
					).done(function(msg) {
						alert(msg);
					});	
				}
			
			
		// Validate on change of escort
		function validateEscortChange(id) {
			var escortClass=$('#' + id ).attr("class");
			//alert(escortClass);
			var escortValue = $('#' + id).attr("value");
			
			 if(escortValue!="") { 
		  
				if($("."+ escortClass + "[value="+escortValue+"]").length>1) {
					$('#' + id).attr("value","");
					alert("This escort has been selected already for the same time");
					showErrorMessage($('#' + id),"The field is required");
				} else {
					 removeErrorMessage(	$('#' + id));  
				}
		 
			 } else {
				 showErrorMessage($('#' + id),"The field is required");
			 }
			
			
		}

				
</script>



<script type="text/javascript">
	function saveTrip() {

		if (confirm("Are you sure you want to save this tripsheet?") == true) {
			var tripDate = document.getElementById("tripDate").value;
			var tripTime = document.getElementById("tripTime").value;
			var tripMode = document.getElementById("tripMode").value;
			var siteId = document.getElementById("siteId").value;
			window.location = "SaveTrip?siteId=" + siteId + "&tripDate="
					+ tripDate + "&tripTime=" + tripTime + "&tripMode="
					+ tripMode + "";
		}
	}
	function printPage() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		window.location = "print_trips.jsp?siteId=" + siteId + "&tripDate="
				+ tripDate + "&tripTime=" + tripTime + "&tripMode=" + tripMode
				+ "";
	}
	function printBoardPage() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		window.location = "assignVendor.jsp?siteId=" + siteId + "&tripDate="
				+ tripDate + "&tripTime=" + tripTime + "&tripMode=" + tripMode
				+ "";
	}
	function comparePage() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		window.location = "compare_trips.jsp?siteId=" + siteId + "&tripDate="
				+ tripDate + "&tripTime=" + tripTime + "&tripMode=" + tripMode
				+ "";
	}

	function exportPage() {
		var tripDate = document.getElementById("tripDate").value;
		var tripTime = document.getElementById("tripTime").value;
		var tripMode = document.getElementById("tripMode").value;
		var siteId = document.getElementById("siteId").value;
		var newwindow = window.open("export2PDF?siteId=" + siteId
				+ "&tripDate=" + tripDate + "&tripTime=" + tripTime
				+ "&tripMode=" + tripMode + "", 'name', 'location=no');
		if (window.focus) {
			newwindow.focus()
		}
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
	
	function showErrorMessage(element, message) {
		removeErrorMessage(element); 
		 $(element).parent().append( 
				 "<img id='" + $(element).attr("id") + "_errorImage' src='images/error.png' title='" + message + "'   />");

	}
	
	function removeErrorMessage(element) {
		try { 
			if($("#"+$(element).attr("id") + "_errorImage" )!=undefined) {
				$("#"+$(element).attr("id") + "_errorImage" ).remove();
			}
		} catch(e ) {}
	}
	
	function requiredValidate(element, message) {
		var retFlag=true;
		//alert('...........((()))');
		try { 
		//var isToValidate =$("#driver_" + $(element).attr("id").split("_")[1]).val()!="" && $("#driver_" + $(element).attr("id").split("_")[1]).val()!=null;
		//alert( "IIII: " +(isToValidate) + "  " + $("#driver_" + $(element).attr("id").split("_")[1]).val() +  " isnull " + ($("#driver_" + $(element).attr("id").split("_")[1]).val()==null));
		//if(isToValidate) {
		//	alert("Not empty..");
		//}
		  
		 if($(element).val()==""  ) {
			 retFlag=false;
			 showErrorMessage(element,message);
		 }
		}catch (e) {
			// TODO: handle exception
			alert(e);
		}
		 return retFlag;
	}
	
	function validateForm() {
	 	try {
			flag = true;
		 $("select[class*=escortClass]").each(function() {
			 //alert($(this).val() + " - " + $(this).attr("class"));
			 if($(this).val()=="") { 
			  
				 flag= requiredValidate(this,"The field is required");
			 }
		 });
		 if(flag==false) {
			 alert("Validation Error");
		 }
		} catch(e) {
			alert(e);
		}
		return flag;
 
 return true;
	}
	
</script>



</head>
<body>
	<%

		String tripDate = OtherFunctions.changeDateFromatToIso(request
				.getParameter("tripDate"));
		String siteId = request.getParameter("siteId");
		String tripMode = "" + request.getParameter("tripMode");
		String tripTime[] =   request.getParameterValues("tripTime");
		 
		
		
		// System.out.println("tripDate" + tripDate + "siteId" + siteId + "tripMode" + tripMode);
		//ArrayList<TripDetailsDto> tripSheetListActual = new TripDetailsDao().getTripSheetActual(tripDate, tripMode, siteId, tripTime);
		
		//String vendorCompany = session.getAttribute("vendorCompany").toString();
		//System.out.println("vendcom" + vendorCompany);

		ArrayList<TripDetailsDto> tripSheetSaved = new VendorService().getAssignedTrip1(siteId, tripDate, tripTime,tripMode, "assign");

		VendorService vendorService = new VendorService();
		ArrayList<VehicleDto> vehicleDtos = null;
		
		try{
			vendorService.loadVehicles();
		} catch (Exception e) {
			System.out.println("error " + e);
		}
		
	%>
	<%
		long empid = 0;
		boolean flag = false;
		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>

	<div id="body">
		<div class="content">
			<hr />
			<form name="assignVehicle" action="TripAssignVehicle" method="post" onsubmit="return validateForm()" >
 
			<input type="hidden" name="tripDate" id="tripDate"
				value="<%=tripDate%>" /> <input type="hidden" name="siteId"
				id="siteId" value="<%=siteId%>" /> <input type="hidden"
				name="tripMode" id="tripMode" value="<%=tripMode%>" /> <input
				type="hidden" name="tripTime" id="tripTime" value="<%=tripTime%>" />
									
			<%
				if (tripSheetSaved != null && tripSheetSaved.size() > 0) {
					flag = true;
			%>
			<hr />
			<h3>Track Trip Sheet</h3>


			<table border="1" style=" width:85%; ">
				<%
			
				try{
					int serialNo=1;
					for (TripDetailsDto tripDetailsDtoObj : tripSheetSaved) {
						vehicleDtos=vendorService.getVendorVehicles(tripDetailsDtoObj.getVehicle());
						 
						   
				%>


				<tr>
				</tr>
				<tr>
					<td colspan="7"  style="background-color: #ddd;" >
					<input type="hidden" name="tripId" value="<%=tripDetailsDtoObj.getId() %>">					
				 <div style="display: inline; margin-left: 15%; font-size: 15px; font-style: italic; ">
					 Trip ID 
					 <%=tripDetailsDtoObj.getTrip_code()%>
				</div>
				<div  style="display: inline; margin-left: 15%;  font-size: 15px; font-style: italic; ">
					Trip Date :
					 <%=OtherFunctions
							.changeDateFromatToddmmyyyy(tripDetailsDtoObj
									.getTrip_date())%>
				</div>
				<div  style="display: inline; margin-left: 15%;  font-size: 15px; font-style: italic; ">
					Time :
					<%=tripDetailsDtoObj.getTrip_log() + "  "
							+ tripDetailsDtoObj.getTrip_time()%>
				</div>
					</td> 
				</tr>
			 
			 
				<tr>
					<td colspan="4">
						<div>
							<table>
								<tr>
									<td>Vehicle</td>
									<td  ><%=tripDetailsDtoObj.getVehicle_type()%></td>
								</tr>
								<tr>
									<td>Escort Trip</td>
									<td><%=tripDetailsDtoObj.getIsSecurity()%></td>
									
								</tr>
								<tr>
									<td>Travel Time</td>
									<td><%=tripDetailsDtoObj.getTravelTime()%></td>
								</tr>
								<tr>
									<td>TotalDistance</td>
									<td><%=tripDetailsDtoObj.getDistance()%></td>				
								</tr>
							</table>
						
						</div>
					</td >
					<td colspan="3">
						<div>
							<table>
								<tr>
									
							<td>Vehicle Number</td>
							<td>
							<select class="vehicleclass" name="vehicle<%=tripDetailsDtoObj.getId()%>" id="vehicle_<%=serialNo%>">
									<option value="">Select</option>
		 
							<%
							 try{
								 			
							for(VehicleDto dto: vehicleDtos) {
								String selected="";									
								if(tripDetailsDtoObj.getVehicleId()!=null && tripDetailsDtoObj.getVehicleId().equals(dto.getId())) {
									selected="selected";
								}
								
							%>
					
							<option <%=selected %> value="<%=dto.getId()%>"><%=dto.getVehicleNo()%></option>
		 					<%}
								
							 }catch(Exception e) {
									System.out.println("ERRRRRRRRRRRRRRRR"+e);
							 } 
								 %>
							</select>
							
							<select name="driver<%=tripDetailsDtoObj.getId()%>" id="driver_<%=serialNo%>">
							<%
							
							
							ArrayList<DriverVehicleDto> dtos = new VehicleService()
									.getVehicleDetails(tripDetailsDtoObj.getVehicleId());
							 
							 if(dtos!=null&&dtos.size()>0) {
							
							for (DriverVehicleDto dto : dtos) {
								DriverDto ddto = dto.getDriverDto();
								String selected="";						
								if(tripDetailsDtoObj.getDriverId().equals(ddto.getDriverId())) {
									selected="selected";
								}
				
							%>
								<option <%=selected %> value="<%=ddto.getDriverId() %>" ><%=ddto.getDriverName() %></option>
							<%} 
							
							}%>
							</select>
					</td>
					<td>
					  
					 	
					 	<input type="button" class="formbutton" value="Set Driver & SendSMS" onclick="sendTripSms('<%=tripDetailsDtoObj.getId() %>')" />					 	
					 
					 
						
					</td>
													
								</tr>
					<tr>
							<td>
								Escort
							</td>
							<td>
							 	 <input type="hidden" value="<%=tripDetailsDtoObj.getIsSecurity() %>" name="isSecurity<%=tripDetailsDtoObj.getId() %>" />
								 <%
								 if(tripDetailsDtoObj.getIsSecurity().equalsIgnoreCase("YES")) {
									try {
										 EscortService escortService = new EscortService();
												List<EscortDto> escortList = escortService.getAllEscorts(siteId);
												String escortClass = "";
												escortClass = "escortClass" + tripDetailsDtoObj.getTrip_time().replace(":", "_");
												//System.out.println("TRIP LOG : "+ tripDetailsDtoObj.getTrip_log());
														   escortClass = escortClass + tripDetailsDtoObj.getTrip_log(); 
														
								%>
								<select class="<%=escortClass %>"  name="escort<%=tripDetailsDtoObj.getId()%>" id="escort_<%=serialNo%>" onchange="validateEscortChange('escort_<%=serialNo%>' )" >
									    <option value=""> Select Escort</option>
								    <%for(EscortDto dto : escortList) { 
								    	pageContext.setAttribute("escort", dto);
								    	String escortSelect = "";
								    	 
								    	if(dto.getId()!=null&&dto.getId().equals(tripDetailsDtoObj.getEscortId())) {
								    		escortSelect="selected";
								    	}
								    	
								    %>
								    
								       <option <%=escortSelect %> value="${escort.id}">${escort.name} - ${escort.escortClock} </option>
								   <% } %>
								</select>
								
								<%}catch(Exception e) { 
								 	System.out.println("Error "+ e);
								}%>
								
								
		
		  
							</td>
							<td> 	<%
							
					 	 
					 		if(tripDetailsDtoObj.getEscortId()!=null&&tripDetailsDtoObj.getEscortId().trim().equals("")==false) {
					 	%>
					 	<%
					 		}
									
						}
					 	%>
							</td>
					</tr>			
					
							</table>	
						</div>
					</td>
					
					 
				</tr>
				 

				<tr style="font-weight: bold; font-size: 26px; background-color: #E8E6DC">
					<td>#</td>
					<td>Name</td>
					<td>Area</td>
					<td>Place</td>
					<td>Landmark</td>
					<td>Dist</td>
					<td>Time</td>
				</tr>
				<%
					int i = 1;
				
							for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj
									.getTripDetailsChildDtoList()) {
				%>
				<tr>
					<td><%=i%></td>
					<td><%=tripDetailsChildDto.getEmployeeName()%></td>
					<td><%=tripDetailsChildDto.getArea()%></td>
					<td><%=tripDetailsChildDto.getPlace()%></td>
					<td><%=tripDetailsChildDto.getLandmark()%></td>
					<td><%=tripDetailsChildDto.getDistance()%></td>
					<td><%=tripDetailsChildDto.getTime()%></td>
				</tr>
				<%
					i++;
							}
				%>
				
				<tr>

					<td colspan=7 align="right"><input type="button"
						value="Audit Log" class="formbutton"
						onclick="showAuditLog(<%=tripDetailsDtoObj.getId()%>,'<%=AuditLogConstants.TRIPSHEET_MODULE%>');" /></td>

				</tr>
				<tr>
					<td height='60px' colspan="7"> </td>
					 
				</tr>

				<%
				serialNo++;
					}
				}catch(Exception e)
				{System.out.println("error"+e);}
				%>
				<tr>
					<td colspan="7"> 
					<input style="margin-left: 3%" class="formbutton" type="button"
					value="Back"
					onclick="javascript:window.location = 'vendor_viewTripSheet.jsp';" />
					<input type="submit" style="margin-left: 60%"  class="formbutton" value="submit">
					</td>
				</tr> 
				</tbody>
			</table>
			
						</form>
			<br />


			<%
				}
				if (!flag) {
			%>
			<p>No trip Is is to display</p>
			<%
				}
	 
			%>

			<p>
			</p>
			<br /> <br />
			<hr />
			<%@include file="Footer.jsp" %>
		</div>
	</div>
</body>
</html>
