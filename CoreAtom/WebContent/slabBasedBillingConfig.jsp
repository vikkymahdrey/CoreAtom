
<%@page import="com.agiledge.atom.billingtype.config.dto.BillingSlabDto"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.DistanceConstraintDto"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.AcConstraintDto"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.SlabBasedBillingConfigDto"%>
 
<%@page import="com.agiledge.atom.billingtype.config.service.SlabBasedBillingTypeConfigService"%>
<%@page import="com.agiledge.atom.dto.SettingsDTO"%>
<%@page import="com.agiledge.atom.service.SettingsService"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants"%>
<%@page import="com.agiledge.atom.billingtype.dto.BillingTypeConstant"%>

 <%@page import="com.agiledge.atom.billingtype.config.dto.SlabBasedBillingConfigDto"%>
<%@page import="com.agiledge.atom.billingtype.config.service.SlabBasedBillingTypeConfigService"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.BillingSlabDto"%>
<%@page import="com.agiledge.atom.dto.SettingsDTO"%>
<%@page import="com.agiledge.atom.service.SettingsService"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="com.agiledge.atom.billingtype.dto.BillingTypeDto"%>
<%@page import="com.agiledge.atom.billingtype.service.BillingTypeService"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="com.agiledge.atom.transporttype.service.TransportTypeService"%>
<%@page import="com.agiledge.atom.transporttype.dto.TransportTypeDto"%>
<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dto.BranchDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dto.CompanyDto"%>
<%@page import="com.agiledge.atom.service.CompanyBranchService"%>
 <%@page import="java.util.List"%>
 <%@page import="java.util.ArrayList"%>
 <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.constants.AuditLogConstants"%>
<%@ taglib uri="http://www.nfl.com" prefix="disp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script type="text/javascript" src="js/jquery-latest.js"></script> 
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<link rel="css/style.css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
<link href="css/second_menu.css" rel="stylesheet" type="text/css" />
 <style type="text/css">
@import "css/jquery.datepick.css";
label
{
  
 width:13em;
 height:0.5em;
 text-align:right;
 float:none;
 margin-right:0.5em;
}
  input[type="text"] 
{
 width: 10em;
 height:15px;
}
  .select1{
  width: 10em;
  }
 
p
{
 margin:1em 0;
}
</style>
 
<title>Billing Setup</title>

</head>
<body>
	<script type="text/javascript">
	$(document).ready(function() {
		 
		 $("select[name=escortRateType]").change(erChanged);
		$("#clearBtn").click( clearEntry);
		 $("#configureBtn").click(configureClicked); 
		$(".formPanel").draggable();
		
	});
	
	function fillEdit(vid) {
	  
	 clearEntry();
	  var lookup = {refId:$("#refId").val(), vehicleTypeId:vid};
		ajaxPost("GetSlabBasedBillingConfigTypes",lookup, fillSlabConfig);
	 
	    
	  
	 
	}
	
	function ajaxPost( urlParam, lookup, retFunction) {
		//alert(urlParam   );
		$.ajax(
 				{
 					method:"POST",
 					url: urlParam,
 					data:lookup,
 					dataType:"json",
 					success: function (data) {
 						 
 						 retFunction( data);
 					}
 				}		
 			);
	}
	
	function fillSlabConfig(data) {
		
		 try {
			alert(JSON.stringify(data.id));
			  
			if(data.result=="true") {
				

				$("select[name=vehicleType]").val(data.vehicleType);
				$("input[name=tripRate]").val(data.tripRate);
				$("#id").val(data.id);
				$("input[name=escortRate]").val(data.escortRate);
				$("select[name=escortRateType]").val(data.escortRateType);
			
				$("#selectedVehicleType").val($("#vehicleType").val());
				var slabs= data.slabList;
				for(var i = 0; i < slabs.length ; i ++) {
				 
					$("#slabRate_" + slabs[i].slabId).val(slabs[i].rate);
					$("#slabId_" + slabs[i].slabId).val(slabs[i].slabId);
				}
				
				$("form[name=fillForm]").attr("action", "UpdateSlabBasedBillingConfig");
				$("#entryDiv").show();
			}
			 
		 }catch(e) {
			 alert(e);
		 }
	}
	
	function deleteConf(vid) {
		if(confirm("Do you really want to delete ?")) {
			 
			$("form[name=fillForm]").attr("action", "DeleteSlabBasedBillingConfig");
			$("input[name=id]").val($("input[name=id_" + vid + "]").val());
			$("form[name=fillForm]").submit();
		}
	}
	
 	
	function configureClicked() {
		var exists=false;
		$(".selectedVehicleType").each(function(){
			if($(this).val()==$("#vehicleType").val()) {
				
				exists=true;
				return false;
			}
		});
		if(exists) {
			fillEdit($("#vehicleType").val());
		} else { 
			 $("form[name=fillForm]").attr("action", "AddSlabBasedBillingConfig");
			showEntryForm();
		}
	}
	function showEntryForm() {
		 try { 
			  
			 if($("#vehicleType").val()=="" || $("#vehicleType").val()==undefined ) {
				 alert("Select vehicle type to configure");
			 } else {
				 
				$("#selectedVehicleType").val($("#vehicleType").val());
				 
				$("#entryDiv").show();
			 }
		 }catch(e) {
			alert(e);	 
		 }
		 
		
		
		
	}
	
	
	function clearEntry() {
	 
			$("#selectedVehicleType").val("");
			$("input[name=tripRate]").val("");
			$(".slabRate").val("");
			$(".slabId").val("");
			$("input[name=escortRate]").val("");
			$("input[name=escortRateType]").val("");
			 $("form[name=fillForm]").attr("action", "AddSlabBasedBillingConfig");
			$("#entryDiv").hide();
			
			 
	}
	
	
	function validateEntryForm() {
		$("input[name=fromTime]").val($("input[name=fromHour]").val( ) + ":" + $("input[name=fromMinute]").val() );
		$("input[name=toTime]").val($("input[name=toHour]").val( ) + ":" + $("input[name=toMinute]").val() );
		flag=true;
		if($("input[name=tripRate]").val()=="") {
			alert ("Slab Amount is blank");
			flag=false;
			
		} else if($("select[name=dcYes]").val()=="") {
			alert("Distance constraint is not chosen");
			flag=false;
			
		}  
		$(".slabRate").each( function() {  
			if($(this).val()=="") {
				alert("Slab Rate is not chosen");
				flag=false;
				 
				 return false;
			}  
			}
			);
		
		if(flag && $("select[name=escortRateType]").val()=="") {
	 		alert("Escort Rate Type is not chosen");
	 		flag = false;
	 	} else if(flag && $("input[name=escortRate]").val()=="" ) {
	 		alert("Escort Rate is not chosen");
	 		
	 		flag = false;
	 	}
	  	
	 	return flag;
	}
	
	function erChanged() {
		if($("select[name=escortRateType]").val()=="Flat") {
			
			$("input[name=escortRate]").next("label").text("");
		} else {
			
			$("input[name=escortRate]").next("label").text("%");
		}
	}
	
	 
	  
 function deleteEntry() {
	
		var  id = $(this).attr("id").split("_")[1];
		var refId = $("#refId_"+id).val();
	 
		
		/* var lookup = {id:id};
		ajaxPost("DeleteTransportTypeMapping",lookup, deleteEntryAck); */
		if(confirm("Do you want to delete the rate ?")) {  
			window.location.href = "DeleteVehicleSlabRate?id=" + id+"&refId="+refId;
		}
		 
	}
	
	  </script>
	<%@include file="Header.jsp"%>
	<%
	try{%>
	<div id='body'>
		<div class='content'>
 	 
 	 <div class="div-menu-outer"  >
 	 	 
 	  <div class="div-menu-inner cur" >
 	 	<a href="billingtype_mapping.jsp">Billing Type Mapping</a>
 	 </div>
 	 	 
 	 
 
 	  </div>
 	  
 <hr/>
 	 <div style="padding-top:1%; background-color:#F0F0F0; padding-left:20px;" >
 	 <h3>Slab based billing configuration</h3>
 	 	
	
	
 	 <form name="TTForm" action="AddSlabBasedBillingConfig" >
 	 	<table style="width:240px; font: normal; position: relative; ">
 	 		<thead>
	 	 		<tr>
	 	 			<th>Vehicle Type *
	 	 			</th>
	 	 			 
	 	 		</tr>
 	 		</thead>
 	 		<tbody>
 	 			<tr>
 	 				<td>
 	 				<%System.out.println(".........................."); %>
						<select name="vehicleType" id = "vehicleType">
 	 						<option value="">-Select Vehicle Type-</option>
 	 										 
 	 						<%
 	 						
 	 						try {
 	 							System.out.println("****************");
 	 					BillingTypeService billingService = new BillingTypeService();
 	 					BillingTypeDto billingDto =  billingService.getBillingTypeMapping(request.getParameter("refId"));
 	 					ArrayList<VehicleTypeDto> vehicleTypeInSiteDtos = null;
 	 					vehicleTypeInSiteDtos = new VehicleTypeDao()
 						.getSiteVehicleType(billingDto.getSiteId());
 	 				 
 	 					if(vehicleTypeInSiteDtos!=null&&vehicleTypeInSiteDtos.size()>0) {
 	 							for(VehicleTypeDto dto : vehicleTypeInSiteDtos) {
 	 					%>		
 	 								<Option value="<%=dto.getId() %>"><%=dto.getType() %></Option>
 	 					<%
 	 							}
 	 						}
 	 						} catch(Exception e) {
 	 							System.out.println("ERROR 1 : " + e);
 	 						}
 	 						System.out.println("****************");
 	 					%>
 	 	
 	 					</select>
 	 				</td>
 	 				 
 	 				<td>
 	 					<input type="button" class="formbutton" id="configureBtn"   value="Configure" />
 	 				</td>
 	 				<td><input type="button" onclick="javascript:history.go(-1)" class="formbutton"  value="Back" />
 	 				</td>
 	 			</tr>
 	 		</tbody>
 	 	</table>
 	 	</form>
 	 </div>
 
 
 
 <div class="formPanel" id="entryDiv" style =" padding-top: 2px;   display: none; width: 75%; ">
 	<div class="formPanelTitle"  >
 		<p style="float:left; margin-top: 0px; margin-left: 30%;"><b>Fill Configuration</b></p>
 		<div style="float: right;" id="closeAddArea">
												<img id="closeAddArea_img" style="float: right; padding:3px; " src="images/close.png" onclick="clearEntry()" title="Close">
											</div>
 	</div>
 <form name="fillForm" action="AddSlabBasedBillingConfig">
 <input id="refId" name="refId" type="hidden" value ="<%=request.getParameter("refId") %>" />
 <input id="id" name="id" type="hidden"   />
 	<div id="configEntryDiv" style="padding:20px;   background-color:#F0F0F0;   ">
 	<p></p>
		  <div style="display:inline-block; " >
 		  	 <input type="hidden" name="selectedVehicleType" id="selectedVehicleType" />
 		  
 		  <table class="displaytag"> 
 		  	<thead> 
 		  		<tr><th colspan="4"></th></tr>
 		  	</thead>
 		  	<tbody>
 			 <tr>
 			 	<td   >Flat Amount</td> <td> <input type="text" name="tripRate" >   
 			 	</td>
 		  	</tr>
 		  	<tr>
 		  					 
 		  	<td>Escort Rate Type</td>
 		  	<td> 
 		   <select class="select1" name="escortRateType" >
						 		 		<%
						 		 		ArrayList<SettingsDTO> escortRateTypeList = new SettingsService().getSettingsStrings(BillingTypeConfigConstants.BILLING_SETUP_MODULE, BillingTypeConfigConstants.ESCORT_RATE_TYPE);
						 	 		 	if(escortRateTypeList != null && escortRateTypeList.size()>0) {
						 		 		for(SettingsDTO stDto: escortRateTypeList) { %>
						 		 		<option value="<%=stDto.getKeyValue()%>"><%=stDto.getValue()%></option>
						 		 		<%} 
						 		 		}%>
						 		 	 
			</select>
			</td>
			</tr>
			<tr>
 		 	<td>Escort Rate</td> 
 		  	<td><input name="escortRate" type="text"></td>
 		  	</tr>
 		  	</tbody>
 			</table>
 		 
 		 </div>  
 		<p></p>  
 		     <div id="timeSlabDiv" style="display:  block;  ">
 		   
 		     
 		   <div id="acEntryDiv_1" >
 		   		<!-- ......................................... -->
 		   					<%
 		   			try {
 		   				System.out.println("________________))))))))))))))))))))))))))))))__________");
 		   			 ArrayList<BillingSlabDto> timeSlabs = new SlabBasedBillingTypeConfigService().getTimeSlabs();
 		   			if(timeSlabs!=null&&timeSlabs.size()>0) {
 		   			%> 
 		  	 		<disp:dipxTable id="row"  list="<%=timeSlabs %>">
 		  				<disp:dipxColumn title="From (HH:mm)" property="startTime" />
 		  				<disp:dipxColumn title="From (HH:mm)" property="endTime" />
 		  				<disp:dipxColumn title="Rate" >
 		  					<input type="text" class="slabRate" name="slabRate_${row.id }" id="slabRate_${row.id }"/>
 		  					<input  type="hidden"  class="slabId" name="slabId" id="slabId_${row.id }" value="${row.id }"/>
 		  				</disp:dipxColumn>
 		  	           
 		  	
 		  			</disp:dipxTable>
 		  			<%
 		  			
 		  			}
 		   			}catch(Exception e) {
 		   				System.out.println("ERROR : " +e);
 		   			}
 		  			%>
 		  			   
 		  	
 		   		<!--  ........................................ -->
 		   
 		   	 </div>
  		  	 </div>
 		<p></p>  	 
 		 <div style="display:inline-block; " >
 		  
 		 	 
 		 </div>  
 		  <div>
 		  		<input style="margin-left:23em;" name="updateBtn" onclick="return validateEntryForm()" type="submit" value="Update" >
 		  		<input style="margin-left:0.5em;" id="clearBtn" type="button" value="Clear" >
 		  </div>
 		
 		    
 		
 	</div>
 </form>	
 </div>
 <div style="background-color: #F0F0F0; margin-top:10px; position: relative; height: 10%;    overflow: hidden;" > 
  				<%
  
  		SlabBasedBillingTypeConfigService service = new SlabBasedBillingTypeConfigService();
 		ArrayList<SlabBasedBillingConfigDto> dtoList =  service.getSlabBasedBillingConfig(request.getParameter("refId"));
 		if(dtoList!= null && dtoList.size()>0) {
 		for(SlabBasedBillingConfigDto dto : dtoList) {		
 		 
 %>
 			<div style="padding-top:5px; width:25em; float: left; " >
 				<table class="displaytag" style=" " >
 					<thead> <tr><th  colspan="2">
 									<div>
 										<div style="display:inline-block; width:60%;" > <label>Vehicle</label> <%=dto.getVehicleTypeDto().getType() %><br/>
 				
 					 						<input id="id" name="id_<%=dto.getVehicleType() %>" type="hidden" value="<%=dto.getId() %>"  />
 											<input type="hidden" value="<%=dto.getTripRate() %>" id="tripRate_<%=dto.getVehicleType() %>" />
											<input type="hidden" value="<%=dto.getVehicleType() %>" class="selectedVehicleType" id="selectedVehicleType_<%=dto.getVehicleType() %>" />
 										 	 
 											<%try { %>
 											
 											 <input type="hidden" value="<%=dto.getEscortRate()  %>" id="escortRate_<%=dto.getVehicleType() %>" />
 											<input type="hidden" value="<%=dto.getEscortRateType()  %>" id="escortRateType_<%=dto.getVehicleType() %>" />
 											 <%}catch(Exception e){
 												   System.out.println("Error : " + e);
 											 }%>
 				
 											
 										</div>
 										
 										<div style="display:inline-block; float:right; " >
 											
 											
 										 <a href="#" style="  font-weight: 100; " onclick="fillEdit('<%=dto.getVehicleType() %>')" >Edit </a>
 									<label   style=" width:2px;" > | </label>
 									<a href="#" style=" font-weight: 100; " onclick="deleteConf('<%=dto.getVehicleType() %>')" >Delete</a>
 											 </div> 
 									</div>
 									
 					
 								</th>
 								   
 							</tr> 
 							<tr> <td colspan="2">
 									
 									
 								</td>
 							</tr>
 							<tr >
 								<td style="border-right: 1px solid black; width: 50%;"> <label>Flat Rate</label> <%=dto.getTripRate() %> <br/>
 										 
 								</td>
 								<td>
 									<label>Escort Rate</label>  
 													<%=dto.getEscortRate() %>
	 												<%if(dto.getEscortRateType().equalsIgnoreCase(BillingTypeConfigConstants.VARIED )) {%> % of <%=dto.getTripRate() %>
	 												<%}%>
 								</td>
 							</tr> 					 
 					</thead>
 					 <tbody>
 				<% 	 		 
 				   if(dto.getSlabList() !=null && dto.getSlabList().size()>0) {
 		 				%>
 		 				 
 						<tr><td colspan="3" ><input type="hidden" value="<%=dto.getSlabList().size()  %>" id="slabListSize_<%=dto.getVehicleType() %>" />
 						
 									<table  style="padding-left: 3%; padding-right: 3%;" >
 									<thead>
 										<tr><th style="background-color: white;" colspan="3">Slab Rate Constraints</th></tr>
 										<tr><th>From</th><th>To</th><th>Rate</th></tr>
 									</thead>
 											<tbody>
 											<% int i=0;
 											for(BillingSlabDto slabDto : dto.getSlabList() ) { %>
 												 <tr><td><%=slabDto.getStartTime() %>
 												 	  
 												 </td>
 												 <td><%=slabDto.getEndTime()  %></td>
 												 <td><%=slabDto.getRate() %></td></tr>
 											 <% i++;
 											 } %>
 											</tbody>
 									
 									</table>
 						</td>
 						</tr>
 		 					<%
 		   
 							}
 		 					 %>
 					 		
 					 </tbody>
 				</table>
 				 
 				  
 				 </div>
 				<%
 				}
 		}
 		
 	 
 		%>
  					 
 	</div>		 
 
 </div>
 
   <div style="position: relative;">
			<%@include file="Footer.jsp"%>
			</div>
		</div>	 
	 
<%}catch(Exception e) {
	System.out.println("Errror"+e);	 
	 }%>

</body>
</html>
