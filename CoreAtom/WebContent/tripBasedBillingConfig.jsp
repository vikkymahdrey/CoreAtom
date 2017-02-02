 
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.service.SettingsService"%>
<%@page import="com.agiledge.atom.dto.SettingsDTO"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.DistanceConstraintDto"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.AcConstraintDto"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.TripBasedBillingConfigDto"%> 
<%@page import="com.agiledge.atom.billingtype.config.service.TripBasedBillingTypeConfigService"%> 
<%@page import="com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants"%>
<%@page import="com.agiledge.atom.billingtype.dto.BillingTypeConstant"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.billingtype.dto.BillingTypeDto"%>
<%@page import="com.agiledge.atom.billingtype.service.BillingTypeService"%> 
<%@page import="java.util.List"%> 
<%@page import="java.util.ArrayList"%> 
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%> 
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
 width: 3em;
 height:15px;
}
  .select1{
  width: 6em;
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
		 
		dcYesChanged();
		acYesChanged();
		$("select[name=dcYes]").change(dcYesChanged);
		$("select[name=acYes]").change(acYesChanged);
		$("select[name=escortRateType]").change(erChanged);
		$("#clearBtn").click( clearEntry);
		$("#acAddNewImg").click(moreAcConstraint);
		$("#dcAddNewImg").click(moreDcConstraint);
		$("#configureBtn").click(configureClicked); 
		$(".formPanel").draggable();
		
	});
	
	function fillEdit(vid) {
	 //alert(" this is fill edit");
	 clearEntry();
	 $("input[name=id]").val($("input[name=id_"+ vid + "]").val());
	 $("input[name=tripRate]").val($("#tripRate_"+vid).val());
	 $("input[name=tripRate]").val($("#tripRate_"+vid).val());
	 $("select[name=dcYes]").val($("#dcYes_"+vid).val());
	 $("select[name=acYes]").val($("#acYes_"+vid).val());
	 $("select[name=escortRateType]").val($("#escortRateType_"+vid).val());
	 $("input[name=escortRate]").val($("#escortRate_"+vid).val());
	 $("input[name=selectedVehicleType]").val($("#selectedVehicleType_"+vid).val());
	 $("#vehicleType").val($("#selectedVehicleType_"+vid).val());
	  if($("select[name=dcYes]").val()=="YES" ) {
		var acno = parseInt($("#dcListSize_"+vid).val());
		if(acno>0) {
			$("input[name=fromKm]").val($("#fromKm_0_"+vid).val());
			$("input[name=toKm]").val($("#toKm_0_"+vid).val());
			$("input[name=dcRate]").val($("#dcRate_0_"+vid).val());
			$("input[name=dcAcRate]").val($("#dcAcRate_0_"+vid).val());
			
			 
			for(var i=1; i<acno; i++) {
				$("#dcAddNewImg").trigger("click");
				var parentDiv = $("#dcAddNewImg").parent();
				$(parentDiv).children(".fromKm").val($("#fromKm_"+i+"_"+vid).val());
				$(parentDiv).children(".toKm").val($("#toKm_"+i+"_"+vid).val());
				$(parentDiv).children(".dcRate").val($("#dcRate_"+i+"_"+vid).val());
				$(parentDiv).children(".dcAcRate").val($("#dcAcRate_"+i+"_"+vid).val());
				$(".dcAcRateSpan").hide();
			}
			
		}
	  }
	  
	  if($("select[name=acYes]").val()=="YES" ) {
		   
		  $(".dcAcRateSpan").show();
			var acno = parseInt($("#acListSize_"+vid).val());
			if(acno>0) {
				$("select[name=fromHour]").val($("#fromHour_0_"+vid).val());
				$("select[name=fromMinute]").val($("#fromMinute_0_"+vid).val());
				$("select[name=toHour]").val($("#toHour_0_"+vid).val());
				$("select[name=toMinute]").val($("#toMinute_0_"+vid).val());
				$("input[name=acRate]").val($("#acRate_0_"+vid).val());
				 
				for(var i=1; i<acno; i++) {
					$("#acAddNewImg").trigger("click");
					var parentDiv = $("#acAddNewImg").parent();
					$(parentDiv).children("select[name=fromHour]").val($("#fromHour_"+i+"_"+vid).val());
					$(parentDiv).children("select[name=fromMinute]").val($("#fromMinute_"+i+"_"+vid).val());
					$(parentDiv).children("select[name=toHour]").val($("#toHour_"+i+"_"+vid).val());
					$(parentDiv).children("select[name=toMinute]").val($("#toMinute_"+i+"_"+vid).val());
					$(parentDiv).children("input[name=acRate]").val($("#acRate_"+i+"_"+vid).val());
				}
				
			}
		  }
	 
	 showEntryForm();
	 dcYesChanged();
	 acYesChanged();
	 $("form[name=fillForm]").attr("action", "UpdateTripBasedBillingConfig");
	 
	}
	
	function deleteConf(vid) {
		if(confirm("Do you really want to delete ?")) {
			 
			$("form[name=fillForm]").attr("action", "DeleteTripBasedBillingConfig");
			$("input[name=id]").val($("input[name=id_" + vid + "]").val());
			$("form[name=fillForm]").submit();
		}
	}
	
	function deleteAcEntryRow() {
		
		     
		  var imgElement = $("#acAddNewImg"); 
		  $(this).parent().remove();
		   try { 
			 
			   $("#acDiv").children().last().append(imgElement);
			   $(imgElement).unbind("click");
			   $(imgElement).bind("click", moreAcConstraint);
			   
		   }catch(e) {
			   alert(e);
		   }
		   
	}
	function deleteDcEntryRow() {

	     
		  var imgElement = $("#dcAddNewImg"); 
	  $(this).parent().remove();
	  try { 
	  		 
	  		$("#dcDiv").children().last().append(imgElement);
	  		$(imgElement).unbind("click");
	  		$(imgElement).bind("click", moreDcConstraint);
	  }catch(e) {
		   alert(e);
	   }
	}
	

	

	function moreDcConstraint() {
		var parentDiv = $(this).parent().clone();
		 
		 
		$(this).parent().parent().append($(parentDiv));
		  var idno=$(parentDiv).attr("id").split("_")[1];
		  idno=parseInt(idno) + 1;
		  $(parentDiv).attr("id","dcEntryDiv_"+idno);
		  $(parentDiv).children("input[type=text]").val("");
		 $(parentDiv).children( "#"+$(this).attr("id")).remove();
		 $(parentDiv).children( ".dcDeleteEntry").remove();
		$(parentDiv).append("<img alt='-' class='dcDeleteEntry' src='images/delete.png' title='Remove'    >");
		$(parentDiv).append($(this));
		$(".dcDeleteEntry").bind("click", deleteDcEntryRow);
	}
	
	function moreAcConstraint() {
		var parentDiv = $(this).parent().clone();
		 
		 
		$(this).parent().parent().append($(parentDiv));
		  var idno=$(parentDiv).attr("id").split("_")[1];
		  idno=parseInt(idno) + 1;
		  $(parentDiv).attr("id","acEntryDiv_"+idno);
		  $(parentDiv).children("input[type=text]").val("");
		 $(parentDiv).children( "#"+$(this).attr("id")).remove();
		 $(parentDiv).children( ".acDeleteEntry").remove();
		$(parentDiv).append("<img alt='-' class='acDeleteEntry' src='images/delete.png' title='Remove'    >");
		$(parentDiv).append($(this));
		$(".acDeleteEntry").bind("click", deleteAcEntryRow);
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
			 $("form[name=fillForm]").attr("action", "AddTripBasedBillingConfig");
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
		
			var cnt =2;
			$("#dcEntryDiv_1").append($("#dcAddNewImg"));
			 while($("#dcEntryDiv_"+cnt).length>0  ) {
				  $("#dcEntryDiv_"+cnt).remove();
				 
				 cnt++;
			 }
			 $("#acEntryDiv_1").append($("#acAddNewImg"));
			 var cnt =2;
			 while($("#acEntryDiv_"+cnt).length>0  ) {
				  $("#acEntryDiv_"+cnt).remove();
				 
				 cnt++;
			 }
	
			$("#selectedVehicleType").val("");
			$("input[name=tripRate]").val("");
			$("select[name=dcYes]").val("");
			$("select[name=acYes]").val("");
			$("input[name=fromKm]").val("");
			$("input[name=toKm]").val("");
			$("input[name=dcRate]").val("");
			$("select[name=fromHour]").val("");
			$("select[name=fromMinute]").val("");
			$("select[name=toHour]").val("");
			$("select[name=toMinute]").val("");
			$("input[name=dcRate]").val("");
			$("input[name=fromTime]").val("");
			$("input[name=toTime]").val("");
			$("input[name=acRate]").val("");
			$("input[name=escortRate]").val("");
			$("select[name=escortRateType]").val("");
			$("#dcDiv").hide();
			$("#acDiv").hide(); 
			
			$("#entryDiv").hide();
			
			 
	}
	
	
	function validateEntryForm() {
		$("input[name=fromTime]").val($("input[name=fromHour]").val( ) + ":" + $("input[name=fromMinute]").val() );
		$("input[name=toTime]").val($("input[name=toHour]").val( ) + ":" + $("input[name=toMinute]").val() );
		flag=true;
		if($("input[name=tripRate]").val()=="") {
			alert ("Trip amount is blank");
			flag=false;
			
		} else if(/^(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test($("input[name=tripRate]").val())==false) {
			alert("Trip amount is invalid");
			flag=false;
			return false;

		}
		else if($("select[name=dcYes]").val()=="") {
			alert("Distance constraint is not chosen");
			flag=false;
			
		} else if( $("select[name=dcYes]").val()=="YES") {
			 
			$(".fromKm").each( function() {  
			if($(this).val()=="") {
				alert("From distance is not chosen");
				flag=false;
				 
				 return false;
			}
			else if(/^(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test($(this).val())==false) {
				alert("From distance is invalid");
				flag=false;
				return false;

			} 			}
			);
			 if(flag && $("select[name=acYes]").val()=="YES") {
				 $("input[name=dcAcRate]").each( function() {
						 
						if($(this).val()=="") {
							alert("Distance constraint: A/c rate is invalid");
							flag=false;
							return false;
						} else if (/^(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test($(this).val())==false) {
							alert("Distance constraint: A/c rate is invalid");
							flag=false;
							return false;
		
						}
				 });
 
			} 
			 if(flag) {
				$(".toKm").each( function() {  
					if($(this).val()=="") {
						alert("To distance is not chosen");
						flag=false;
						 
						 return false;
					}
					else if(/^(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test($(this).val())==false) {
						alert("To distance is invalid");
						flag=false;
						return false;

					}
					}
					);
				var fromKmElement =$(".fromKm").toArray();
				 var toKmElement =$(".toKm").toArray();
				 try { 
				 for(var i=0; i<fromKmElement.length; i++) {
					 //alert($(fromKmElement[i]).val() + " "+ $(toKmElement[i]).val());
					 var fromNo= parseInt($(fromKmElement[i]).val());
					 var toNo =  parseInt($(toKmElement[i]).val());
					 if(fromNo>toNo) {
						 alert("From distance :" + fromNo + " is greater than to distance " + toNo);
						 flag=false;
						return false;
					 }
				 }
				 } catch(e) {
					 alert(e);
					 flag=false;
					 return flag;
				 }
					
			}
			if(flag) { 
				$("input[name=dcRate]").each( function() {
			if($(this).val()=="") {
				alert("Distance constraint :Rate is not chosen");
				flag = false;
				return false;
			}
				}
				);
			}
		}
		
	 	if(flag && $("select[name=acYes]").val()=="") {
			alert("A/C constraint is not chosen");
			flag=false;
			
		} else if(flag && $("select[name=acYes]").val()=="YES") {
			var fromHrElement =$("select[name=fromHour]").toArray();
			 var toHrElement =$("select[name=toHour]").toArray();
			 var fromMtElement =$("select[name=fromMinute]").toArray();
			 var toMtElement =$("select[name=toMinute]").toArray();
			 
				
			if(flag &&$("input[name=fromTime]").val()=="") {
				alert("From time is not chosen");
				flag=false;
			}else if(flag &&$("input[name=toTime]").val()=="") {
				alert("To time is not chosen");
				flag=false;
			} else if(flag &&$("input[name=acRate]").val()=="") {
				alert("A/C constraint : Rate is not chosen");
				flag = false;
			} else if(/^(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test($("input[name=acRate]").val())==false) {
				alert("A/C constraint : Rate is invalid");
				flag=false;
				return false;

			} else if(flag) {
				try { 
					 for(var i=0; i<fromHrElement.length; i++) {
						 //alert($(fromKmElement[i]).val() + " "+ $(toKmElement[i]).val());
						 var fromHr= $(fromHrElement[i]).val();
						 var toHr =   $(toHrElement[i]).val();
						 var fromMt=  $(fromMtElement[i]).val();
						 var toMt =   $(toMtElement[i]).val();
						 var fromTime1 = fromHr+":" + fromMt;
						 var toTime1 = toHr + ":" + toMt;
						  
						 if(fromTime1>toTime1) {
 							 alert("From time :" + fromHr + ":"+ fromMt +" is greater than to time " + toHr + ":" + toMt);
							 flag=false;
							return false;
						 }
					 }
					 } catch(e) {
						 alert(e);
						 flag=false;
						 return flag;
					 }
				
			} 
		}
	 	
	 	if(flag && $("select[name=escortRateType]").val()=="") {
	 		alert("Escort rate type is not chosen");
	 		flag = false;
	 	} else if(flag && $("input[name=escortRate]").val()=="" ) {
	 		alert("Escort rate is not chosen");
	 		
	 		flag = false;
	 	}else if(flag && /^(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test($("input[name=escortRate]").val())==false) {
			alert("Esocrt rate is invalid");
			flag=false;
			return false;

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
	
	function dcYesChanged() {
		if($("select[name=dcYes]").val()=="YES") {
			$("#dcDiv").show();
		} else {
			$("#dcDiv").hide();
		}
	}
	
	function acYesChanged() {
		if($("select[name=acYes]").val()=="YES") {
			$("#acDiv").show();
			if($("select[name=dcYes]").val()=="YES") {
				$(".dcAcRateSpan").show();
			}
		} else {
			$("#acDiv").hide();
			$(".dcAcRateSpan").hide();
		}
	}
	
	  
 function deleteEntry() {
	
		var  id = $(this).attr("id").split("_")[1];
		var refId = $("#refId_"+id).val();
	 
		
		/* var lookup = {id:id};
		ajaxPost("DeleteTransportTypeMapping",lookup, deleteEntryAck); */
		if(confirm("Do you want to delete the rate ?")) {  
			window.location.href = "DeleteVehicleKmRate?id=" + id+"&refId="+refId;
		}
		 
	}
	
	  </script>
	<%@include file="Header.jsp"%>
	<div id='body'>
		<div class='content'>
 	 
 	 <div class="div-menu-outer"  >
 	 	 
 	  <div class="div-menu-inner cur" >
 	 	<a href="billingtype_mapping.jsp">Billing Type Mapping</a>
 	 </div>
 	 	 
 	 
 
 	  </div>
 	  
 <hr/>
 	 <div style="padding-top:1%; background-color:#F0F0F0; padding-left:20px;" >
 	 <h3>Trip based billing configuration</h3>
 	 <form name="TTForm" action="AddTripBasedBillingConfig" >
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
						<select name="vehicleType" id = "vehicleType">
 	 						<option value="">-Select Vehicle Type-</option>
 	 					 
 	 						<%
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
 <form name="fillForm" action="AddTripBasedBillingConfig">
 <input id="refId" name="refId" type="hidden" value ="<%=request.getParameter("refId") %>" />
 <input id="id" name="id" type="hidden"   />
 	<div id="configEntryDiv" style="padding:20px;   background-color:#F0F0F0;   ">
 	<p></p>
		  <div style="display:inline-block; " >
 		 	<label  >&nbsp;&nbsp;&nbsp;&nbsp;  
 		 	Amount Per Trip</label>
 		 	 &#8377;<input type="text" name="tripRate" >
 		 	 <input type="hidden" name="selectedVehicleType" id="selectedVehicleType" /> 
 		 </div>  
 		<p></p> 	
 		  <div style="display:inline-block;  " >
 		 	<label  >Distance Constraint</label>
 		 	<select class="select1" name="dcYes" >
 		 		<option value="<%=BillingTypeConfigConstants.AC_NO%>" >NO</option>
 		 		<option value="<%=BillingTypeConfigConstants.AC_YES%>" >YES</option>
 		 	</select>
 		 	   
 		 </div>  
 		 
 		   <div id="dcDiv" style="margin-left: 20em; display:  block;  ">
 		   		 
 		   		<div id="dcEntryDiv_1" style="background-color: #ddd;" >
 		  			<label >From *</label><input class="fromKm" name="fromKm" type="text"  >
 		  			<label >To</label><input  class="toKm" name="toKm" type="text"  >
 		  			<label >Rate * &#8377;</label><input class="dcRate amount"  name="dcRate" type="text">
 		  			 
 		  			<span class="dcAcRateSpan"><label >A/C Rate * &#8377;</label><input class="dcAcRate amount"  name="dcAcRate" type="text"></span>
 		  			 
 		  			<img style="height:23px;" alt="+" id="dcAddNewImg" src="images/add_new.png" title="More"   > 
 		  	 	</div>
 		  			 
 		  	 </div>
 		  	 <p></p>
 		 <div style="display:inline-block; " >
 		 	<label  >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 		 	A/C Constraint</label>
 		 	
 		 	<select class="select1" name="acYes">
 		 		<option value="NO" >NO</option>
 		 		<option value="YES" >YES</option>
 		 	</select>
 		 	   
 		 </div>  
 		 
 		   <div id="acDiv" style="margin-left:20em;; display:  block;  ">
 		   
 		   <div>
 		   	<label style="margin-left:4.75em;">Hour</label>
 		   	<label style="margin-left:1.75em;">Minute</label>
 		   	<label style="margin-left:3.70em;">Hour</label>
 		   	<label style="margin-left:1.62em;">Minute</label>
 		   </div> 
 		   <div id="acEntryDiv_1" >
 		   			 
 		  			<label >From *</label><input type="hidden" name="fromTime">
 		  			<select  name="fromHour">
 		  			<% int hour=0;
 		  				for (; hour <= 23; hour++) {
					 		String hourString = (hour < 10 ? "0" + hour : "" + hour); 	 
	%>
	 					<option value="<%=hourString%>"  ><%=hourString%></option>
 	<% 				} %>
				 	</select>:
				 	<select  name="fromMinute">
 		  			<% int minute=0;
 		  				for (; minute <= 59; minute++) {
					 		String minuteString = (minute < 10 ? "0" + minute : "" + minute); 	 
	%>
	 					<option value="<%=minuteString%>"  ><%=minuteString%></option> 
 	<% 				} %>
				 	</select>
 		  			<label >&nbsp;&nbsp;&nbsp;&nbsp;To</label><input type="hidden" name="toTime">
 		  					<select  name="toHour">
 		  			<%   hour=0;
 		  				for (; hour <= 23; hour++) {
					 		String hourString = (hour < 10 ? "0" + hour : "" + hour); 	 
	%>
	 					<option value="<%=hourString%>"  ><%=hourString%></option>
 	<% 				} %>
				 	</select>:
				 	<select  name="toMinute">
 		  			<%  minute=0;
 		  				for (; minute <= 59; minute++) {
					 		String minuteString = (minute < 10 ? "0" + minute : "" + minute); 	 
	%>
	 					<option value="<%=minuteString%>"  ><%=minuteString%></option> 
 	<% 				} %>
 					</select>
 		  			<label >&nbsp;&nbsp;&nbsp;Rate * &#8377;</label><input type="text" name="acRate">
 		  			
 		  			<img style="height:23px;" alt="+" id="acAddNewImg" src="images/add_new.png" title="More"   >
 		  			
 		  			 
 		  	 </div>
 
 	 		 
 			 
 		  	 </div>
 		<p></p>  	 
 		 <div style="display:inline-block; " >
 		 	<label  >&nbsp;&nbsp;&nbsp;
 		 	Escort Rate Type</label>
 	 	 	<select class="select1" name="escortRateType" >
 		 		<%
 		 		ArrayList<SettingsDTO> escortRateTypeList = new SettingsService().getSettingsStrings(BillingTypeConfigConstants.BILLING_SETUP_MODULE, BillingTypeConfigConstants.ESCORT_RATE_TYPE);
 	 		 	if(escortRateTypeList != null && escortRateTypeList.size()>0) {
 		 		for(SettingsDTO stDto: escortRateTypeList) { %>
 		 		<option value="<%=stDto.getKeyValue()%>"><%=stDto.getValue()%></option>
 		 		<%} 
 		 		}%>
 		 	 
 		 	</select>
 		 	  
 		 	<p>
 		 	<label id="escortRateLabel" style=" margin-left:8em;">Rate </label><input name="escortRate" type="text"><label></label>
 		 	</p>   
 		 	 
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
  		TripBasedBillingTypeConfigService service = new TripBasedBillingTypeConfigService();
 System.out.println("Refid : "+ request.getParameter("refId"));
 		ArrayList<TripBasedBillingConfigDto> dtoList =  service . getTripBasedBillingConfig(request.getParameter("refId"));
 		if(dtoList!= null && dtoList.size()>0) {
 			System.out.println(" list is there ");
 		for(TripBasedBillingConfigDto dto : dtoList) {		
 		 
 %>
 			<div style="padding-top:5px; width:25em; float: left; " >
 				<table class="displaytag" style=" " >
 					<thead> <tr><th  colspan="2">
 									<div>
 										<div style="display:inline-block; width:60%;" > <label>Vehicle</label> <%=dto.getVehicleTypeDto().getType() %><br/>
 				
 					 						<input id="id" name="id_<%=dto.getVehicleTypeId() %>" type="hidden" value="<%=dto.getId() %>"  />
 											<input type="hidden" value="<%=dto.getFlatTripRate() %>" id="tripRate_<%=dto.getVehicleTypeId() %>" />
											<input type="hidden" value="<%=dto.getVehicleTypeId() %>" class="selectedVehicleType" id="selectedVehicleType_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getDcYes()  %>" id="dcYes_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getAcYes()  %>" id="acYes_<%=dto.getVehicleTypeId() %>" />
 											 
 											<%try { %>
 											
 											 <input type="hidden" value="<%=dto.getEscortRate()  %>" id="escortRate_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getEscortRateType()  %>" id="escortRateType_<%=dto.getVehicleTypeId() %>" />
 											 <%}catch(Exception e){
 												   System.out.println("Error : " + e);
 											 }%>
 				
 											
 										</div>
 										
 										<div style="display:inline-block; float:right; " >
 											
 											
 										 <a href="#" style="  font-weight: 100; " onclick="fillEdit('<%=dto.getVehicleTypeId() %>')" >Edit </a>
 									<label   style=" width:2px;" > | </label>
 									<a href="#" style=" font-weight: 100; " onclick="deleteConf('<%=dto.getVehicleTypeId() %>')" >Delete</a>
 											 </div> 
 									</div>
 									
 					
 								</th>
 								   
 							</tr> 
 							<tr> <td colspan="2">
 									
 									
 								</td>
 							</tr>
 							<tr >
 								<td style="border-right: 1px solid black; width: 50%;"> <label>Flat Rate</label> &#8377;<%=dto.getFlatTripRate() %> <br/>
 										 
 								</td>
 								<td>
 									<label>Escort Rate</label>  
 													<%=dto.getEscortRate() %>
	 												<%if(dto.getEscortRateType().equalsIgnoreCase(BillingTypeConfigConstants.VARIED )) {%> % of &#8377;<%=dto.getFlatTripRate() %>
	 												<%}%>
 								</td>
 							</tr> 					 
 					</thead>
 					 <tbody>
 					 		 
 				<% if(dto.getDcList()!=null && dto.getDcList().size()>0) {
 						int dcColspan=3;
 						if ( !OtherFunctions.isEmpty(dto.getAcYes()) && dto.getAcYes().equalsIgnoreCase(BillingTypeConfigConstants.AC_YES)) {
 							dcColspan = 4;
 						}
 					
 				  %>
 
 						<tr><td colspan="2" > 
 					 
								<input type="hidden" value="<%=dto.getDcList().size()  %>" id="dcListSize_<%=dto.getVehicleTypeId() %>" />
 									<table style="padding-left: 3%; padding-right: 3%;" ><thead>
 									<tr><th style="background-color: white;" colspan="<%=dcColspan%>">Distance  Constraints</th></tr>
 									<tr><th>From</th><th>To</th><th>Rate</th>
 									<%
 										if ( !OtherFunctions.isEmpty(dto.getAcYes()) && dto.getAcYes().equalsIgnoreCase(BillingTypeConfigConstants.AC_YES)) {
 									%>
 									<th>A/C Rate</th>
 									<%
 									}
 									%>
 									</tr></thead>
 											<tbody>
 											<% int i=0;
 											for(DistanceConstraintDto dcDto : dto.getDcList()) { %>
 												 <tr><td><%=dcDto.getFromKm() %>
 	 
 														 <input type="hidden" value="<%=dcDto.getFromKm() %>" id="fromKm_<%=i %>_<%=dto.getVehicleTypeId() %>" />
 														 <input type="hidden" value="<%=dcDto.getToKm() %>" id="toKm_<%=i %>_<%=dto.getVehicleTypeId() %>" />
 														 <input type="hidden" value="<%=dcDto.getRate() %>" id="dcRate_<%=i %>_<%=dto.getVehicleTypeId() %>" />
 														 <input type="hidden" value="<%=dcDto.getDcAcRate() %>" id="dcAcRate_<%=i %>_<%=dto.getVehicleTypeId() %>" />
 		
 																		
 												 </td>
 												 <td><%=dcDto.getToKm()  %></td>
 												 <td>&#8377;<%=dcDto.getRate() %></td>
 												 	<%
 										if ( !OtherFunctions.isEmpty(dto.getAcYes()) && dto.getAcYes().equalsIgnoreCase(BillingTypeConfigConstants.AC_YES)) {
 									%>
 									<td>&#8377;<%=dcDto.getDcAcRate() %></td>
 									<%
 									}
 									%>
 												 </tr>
 											 <%
 												i++; 
 											} %>
 											</tbody>
 									
 									</table>
 						</td>
 						</tr>
 						
 					<%
 				}  if(dto.getAcList()!=null && dto.getAcList().size()>0) {
 		 				%>
 		 				 
 						<tr><td colspan="3" ><input type="hidden" value="<%=dto.getAcList().size()  %>" id="acListSize_<%=dto.getVehicleTypeId() %>" />
 						
 									<table  style="padding-left: 3%; padding-right: 3%;" >
 									<thead>
 										<tr><th style="background-color: white;" colspan="3">A/c Rate  Constraints</th></tr>
 										<tr><th>From</th><th>To</th><th>Rate</th></tr>
 									</thead>
 											<tbody>
 											<% int i=0;
 											for(AcConstraintDto acDto : dto.getAcList()) { %>
 												 <tr><td><%=acDto.getFromTime() %>
 												 	<% 
 												 	String []fromTime = acDto.getFromTime().split(":");
 												 	String []toTime = acDto.getToTime().split(":");
 												 	String fromHour = fromTime[0];
 												 	String fromMinute = fromTime[1];
 												 	String toHour = toTime[0];
 												 	String toMinute =  toTime[1];
 												 	%>
 												 	
 												 
 												 	
													<input type="hidden" value="<%=fromHour %>" id="fromHour_<%=i %>_<%=dto.getVehicleTypeId() %>" />
 												 	<input type="hidden" value="<%=fromMinute %>" id="fromMinute_<%=i %>_<%=dto.getVehicleTypeId() %>" />
	 												 <input type="hidden" value="<%=toHour %>" id="toHour_<%=i %>_<%=dto.getVehicleTypeId() %>" />
	 												 <input type="hidden" value="<%=toMinute %>" id="toMinute_<%=i %>_<%=dto.getVehicleTypeId() %>" />
	 												 <input type="hidden" value="<%=acDto.getRate() %>" id="acRate_<%=i %>_<%=dto.getVehicleTypeId() %>" />
 												 	
 												 	
 												 </td>
 												 <td><%=acDto.getToTime()  %></td>
 												 <td>&#8377;<%=acDto.getRate() %></td></tr>
 											 <% i++;
 											 } %>
 											</tbody>
 									
 									</table>
 						</td>
 						</tr>
 		 					<%} 
 		 					 %>
 					 		
 					 </tbody>
 				</table>
 				 
 				  
 				 </div>
 				<%}
 		}%>
 					 
 	</div>		 
 
 </div>
   <div style="position: relative;">
			<%@include file="Footer.jsp"%>
			</div>
		</div>
	 


</body>
</html>
