<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.service.VehicleService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Trip Cost Setup</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script type="text/javascript">        
            $(document).ready(function()
            {                                                                        
                $("#fromDate").datepick();
                $("#siteId").change(getVehicleTypes);
               
            });     
            
            function getVehicleTypes()
            {
            	
            	$.ajax({url:"GetVehicleNotInSite?siteId="+ $("#siteId").val(), type:"POST",
            			success: function(result){
            				
            				var fullvehicle =result;
            				 
    						fullvehicle = fullvehicle.split("$");
    						 
            				$("#vehicleContainter").html("<select name='vehicleTypeId' id='vehicleTypeId' > "+	
            				" <option value=''>Select Vehicle Type</option> "+fullvehicle[1]+" </select>");
            			}	
            	});
            };
            
            function validate()
            {
            	 
            	var flag=true;
            	if($("#site").val()=="")
            		{
            		 alert("Choose Site");
            		 flag=false;
             		}  else
             	if($("#vehicleTypeId").val()=="")
             		{
             		 alert("Choose Vehicle Type");
            		 flag=false;
             		}else
             	if($("#fromDate").val()=="")
             		{
             		 alert("Choose Effective Date");
            		 flag=false;
             		}  else
             	if($("#rate").val()=="")
             		{
             		 alert("Choose Rate");
            		 flag=false;
             		}
            	
            	return flag;
            }
        </script>
</head>
<body>
	
	
	<div id='body'>
		<div class='content'>
		 <%
	
		 				long empid = 0;
		 				
		 					String employeeId = OtherFunctions.checkUser(session);

		 					empid = Long.parseLong(employeeId);
		 					 
		 					 
				ArrayList<VehicleTypeDto> vehicleRateDtoList = new VehicleService().getVehicleTypeRateHistory();		 						
		 				 
			%>
			<%@include file="Header.jsp"%>
			<h3>
				Trip Cost Set Up</h3>

<div>
				<form name="form1" action="AddTripRate" method="post">



					<table>
						<tr>
							<th> Site</th>
							<th>Vehicle Type</th>
							<th>Effective Date</th>
							<th>Rate Per Trip</th>
							<th> </th>
							<th></th>
						</tr>
						<tr>

							<td>   
							
				<select name="siteId" id="siteId">
								<%
								String siteId=request.getParameter("siteId")==null?"":request.getParameter("siteId");
								System.out.println("site :" + siteId);
									String site=(request.getSession().getAttribute("site")==null||request.getSession().getAttribute("site").toString().trim().equals(""))?"" :request.getSession().getAttribute("site").toString().trim();
									 List<SiteDto> vrDtoList = new SiteDao().getSites();  
									 
									if(siteId!=null&&siteId.trim().equals("")==false)
									{
										site=siteId;
									} 
									
									for (SiteDto vrDto : vrDtoList) {
									
									String siteSelect="";
									if(site.equals(vrDto.getId()))
									{
										siteSelect="selected";
									}
								
								%>

								<option <%=siteSelect %> value="<%=vrDto.getId()%>"><%=vrDto.getName()%></option>
								<%  }%>
						</select>
							
							</td>
							<td id="vehicleContainter">
									<select name="vehicleTypeId" id="vehicleTypeId" >
										<option value="">Select Vehicle Type</option>
									<%
									 
										String vehicleTypeId= request.getParameter("vehicleTypeId");
									String vehicleSelect="";
										vehicleTypeId=vehicleTypeId==null?"":vehicleTypeId;
										ArrayList<VehicleTypeDto> vList= new VehicleService().getAllVehicleTypeBySite(site);
										
										if(vList!=null&&vList.size()>0)
										{
										for(VehicleTypeDto vdto: vList)
										{
											
											if((""+vdto.getId()).equals(vehicleTypeId))
											{
												vehicleSelect="selected";
											}else
											{
												vehicleSelect="";
											}
									%>
											<option <%=vehicleSelect %> value="<%=vdto.getId() %>" ><%=vdto.getType() %></option>
											<% 
										}
										}
										
										
									%>
										 
									</select>
							</td>
							<td> 
							
								 <input name="fromDate" readonly="readonly"
								id="fromDate" type="text" 
								class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd',
	                                                 minDate: new Date(2008, 12 - 1, 25)}" />

							</td>
							<td>&#8377;<input  type="text" id="rate" name="rate" /> 
								
							</td>
							<td><input type="submit" 
								value="Add" class="formbutton" onclick="return validate()" />
							</td>
							<td>
								<input type="reset" class="formbutton" />
							</td>
						</tr>
					</table>
					<br /> <input type="Button" class="formbutton" value="Back" 
						onclick="javascript:history.go(-1);" /> <br />
				</form>

			</div>
			
			<hr/>
			
				<%
					int serial = 1;
							if (vehicleRateDtoList != null && vehicleRateDtoList.size() > 0) {
				%>
				<table>
				<tr>
					<th>Site</th>
					<th>Vehicle Type</th>
					<th>From(dd/mm/yyyy)</th>
					<th>To(dd/mm/yyyy)</th>
					<th>Rate Per Trip</th>
					<th>Status</th>

				</tr>
				<%
					for (VehicleTypeDto vrDto : vehicleRateDtoList) {
				%>

				<tr>

					<td width='13%'><%=vrDto.getSiteName()%> 
					</td>
					<td><%=vrDto.getType()%>  
					</td>
					<td>
						<%=OtherFunctions.changeDateFromatToddmmyyyy( vrDto.getFromDate() )%>
					</td>
					<td>
						<%=OtherFunctions.changeDateFromatToddmmyyyy( vrDto.getToDate() )%>
					</td>
					<td>
						<%=vrDto.getRatePerTrip() %>
					</td>
					<td  >
						<%=vrDto.getStatus() %>
					</td>
				</tr>

				<%
					serial++;
								}
							 
				%>
			</table>
			<%
							}
				
			%>





			<h3></h3>
			<hr>
			
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>