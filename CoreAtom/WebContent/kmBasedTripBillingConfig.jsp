

<%@page import="com.agiledge.atom.billingtype.config.dto.KmBasedTemplateTripBillingConfigDto"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.KmTemplateDto"%>
<%@page import="com.agiledge.atom.billingtype.config.service.KmBasedTemplateBillingService"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.KmBasedBillingConfigConstants"%>
<%@page import="com.agiledge.atom.dao.SettingsDoa"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.KmBasedMapTripBillingConfigDto"%>
<%@page import="com.agiledge.atom.billingtype.config.service.KmBasedMapBillingTypeConfigService"%>
<%@page import="com.agiledge.atom.billingtype.config.service.KmBasedClassicBillingTypeConfigService"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.KmBasedClassicTripBillingConfigDto"%>
<%@page import="com.agiledge.atom.billingtype.config.service.KmBasedBillingTypeConfigService"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.KmBasedBillingConfigDto"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.DistanceConstraintDto"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.AcConstraintDto"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.TripBasedBillingConfigDto"%>
<%@page import="com.agiledge.atom.reports.TripBillingBreakUpsReportHelper"%>
<%@page import="com.agiledge.atom.billingtype.config.service.TripBasedBillingTypeConfigService"%>
<%@page import="com.agiledge.atom.dto.SettingsDTO"%>
<%@page import="com.agiledge.atom.service.SettingsService"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants"%>
<%@page import="com.agiledge.atom.billingtype.dto.BillingTypeConstant"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.billingtype.dto.BillingTypeDto"%>
<%@page import="com.agiledge.atom.billingtype.service.BillingTypeService"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="com.agiledge.atom.billingtype.config.service.KmBasedMapBillingTypeConfigService"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.KmBasedTemplateTripBillingConfigDto"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.KmBasedMapTripBillingConfigDto"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.AcConstraintDto"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.KmBasedClassicTripBillingConfigDto"%>
<%@page import="com.agiledge.atom.billingtype.config.service.KmBasedClassicBillingTypeConfigService"%>
<%@page import="com.agiledge.atom.dao.VehicleTypeDao"%>
<%@page import="com.agiledge.atom.dto.VehicleTypeDto"%>
<%@page import="com.agiledge.atom.billingtype.dto.BillingTypeDto"%>
<%@page import="com.agiledge.atom.billingtype.service.BillingTypeService"%>
<%@page import="com.agiledge.atom.billingtype.config.service.KmBasedTemplateBillingService"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.KmBasedBillingConfigConstants"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.KmTemplateDto"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants"%>
<%@page import="com.agiledge.atom.dto.SettingsDTO"%>
<%@page import="com.agiledge.atom.service.SettingsService"%>
<%@page import="com.agiledge.atom.billingtype.config.service.KmBasedBillingTypeConfigService"%>
<%@page import="com.agiledge.atom.billingtype.config.dto.KmBasedBillingConfigDto"%>
<%@page import="java.util.Date"%> 
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
<%@page import="com.agiledge.atom.transporttype.dto.TransportTypeDto"%> 
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
		try { 
		templateAcYesChanged();
		classicAcYesChanged();
		mapAcYesChanged();
		  //$("#templateUpdateForm").hide();
		  if($("#editTemplateBtn").html()==undefined){
			   
		  } else {
			  $("#templateUpdateForm").hide();
		  }
		   
		  $("select[name=templateEscortRateType]").change(templateErChanged);
		  $("select[name=mapEscortRateType]").change(mapErChanged);
		  $("select[name=classicEscortRateType]").change(classicErChanged);
		  $("select[name=templateSwingRateType]").change(templateSRTChanged);
		  $("select[name=calculationType]").change(mapSRTChanged);
			$("select[name=templateAcYes]").change(templateAcYesChanged);
			$("select[name=classicAcYes]").change(classicAcYesChanged);
			$("select[name=mapAcYes]").change(mapAcYesChanged);
		  $("#templateAcAddNewImg").click(moreTemplateAcConstraint);
		  $("#mapAcAddNewImg").click(moreMapAcConstraint);
		  $("#classicAcAddNewImg").click(moreClassicAcConstraint);
		 $("#clearClassicBtn").click( clearEntry_classic);
		$("#clearMapBtn").click( clearEntry_map);
		$("#clearTemplateBtn").click( clearEntry_template);
		 
		$("#configureBtn").click(configureClicked); 
		$(".formPanel").draggable();
		$("#editTemplateBtn").click(showTemplateUpdateForm);
		
		} catch(e) {
			alert('error : ' + e);
		}
	});
	
	function validateTemplateEntryForm() {
		
	 
		flag=true;
		
		if($("input[name=templateId]").val()=="") {
			alert ("Please upload template first.");
			flag=false;
			
		}
		if($("input[name=templateTripRate]").val()=="") {
			alert ("Trip / Km Amount is blank");
			flag=false;
			
		}  
	 	if(flag && $("select[name=templateAcYes]").val()=="") {
			alert("A/C constraint is not chosen");
			flag=false;
			
		} else if(flag && $("select[name=templateAcYes]").val()=="YES") {
			   if(flag &&$("input[name=templateAcRate]").val()=="") {
				alert("A/C Constraint : Rate is not chosen");
				flag = false;
			} 
		}
	 	
	 	if(flag && $("select[name=templateSwingRateType]").val()=="") {
	 		alert("Swing Addition is not chosen");
	 		flag = false;
	 	}
	 	
	 	 if(flag &&$("input[name=templateSwingRate]").val()=="") {
				alert("Swing value not chosen");
				flag = false;
			} 
	 	if(flag && $("select[name=templateEscortRateType]").val()=="") {
	 		alert("Escort Rate Type is not chosen");
	 		flag = false;
	 	} else if(flag && $("input[name=templateEscortRate]").val()=="" ) {
	 		alert("Escort Rate is not chosen");
	 		
	 		flag = false;
	 	}
	  	
	 	return flag;

 
	}
	

	function templateErChanged() {
		if($("select[name=templateEscortRateType]").val()=="Flat") {
			
			$("input[name=templateEscortRate]").next("label").text("");
		} else {
			
			$("input[name=templateEscortRate]").next("label").text("%");
		}
	}
	
	function classicErChanged() {
		if($("select[name=classicEscortRateType]").val()=="Flat") {
			
			$("input[name=classicEscortRate]").next("label").text("");
		} else {
			
			$("input[name=classicEscortRate]").next("label").text("%");
		}
	}

	function mapErChanged() {
		if($("select[name=mapEscortRateType]").val()=="Flat") {
			
			$("input[name=mapEscortRate]").next("label").text("");
		} else {
			
			$("input[name=mapEscortRate]").next("label").text("%");
		}
	}
	
	

	function mapSRTChanged () {
		if($("select[name=calculationType]").val()=="Flat") {
			
			$("input[name=swingDistance]").next("label").text("");
		} else {
			
			$("input[name=swingDistance]").next("label").text("%");
		}
	}
	
	function templateSRTChanged () {
		if($("select[name=templateSwingRateType]").val()=="Flat") {
			
			$("input[name=templateSwingRate]").next("label").text("");
		} else {
			
			$("input[name=templateSwingRate]").next("label").text("%");
		}
	}
	
	
	function moreTemplateAcConstraint() {
		var parentDiv = $(this).parent().clone();
		 
		 
		$(this).parent().parent().append($(parentDiv));
		  var idno=$(parentDiv).attr("id").split("_")[1];
		  idno=parseInt(idno) + 1;
		  $(parentDiv).attr("id","templateAcEntryDiv_"+idno);
		  $(parentDiv).children("input[type=text]").val("");
		 $(parentDiv).children( "#"+$(this).attr("id")).remove();
		 $(parentDiv).children( ".templateAcDeleteEntry").remove();
		$(parentDiv).append("<img alt='-' class='templateAcDeleteEntry' src='images/delete.png' title='Remove'    >");
		$(parentDiv).append($(this));
		$(".templateAcDeleteEntry").bind("click", deleteTemplateAcEntryRow);
	}

	function moreClassicAcConstraint() {
		var parentDiv = $(this).parent().clone();
		 
		 
		$(this).parent().parent().append($(parentDiv));
		  var idno=$(parentDiv).attr("id").split("_")[1];
		  idno=parseInt(idno) + 1;
		  $(parentDiv).attr("id","classicAcEntryDiv_"+idno);
		  $(parentDiv).children("input[type=text]").val("");
		 $(parentDiv).children( "#"+$(this).attr("id")).remove();
		 $(parentDiv).children( ".classicAcDeleteEntry").remove();
		$(parentDiv).append("<img alt='-' class='classicAcDeleteEntry' src='images/delete.png' title='Remove'    >");
		$(parentDiv).append($(this));
		$(".classicAcDeleteEntry").bind("click", deleteClassicAcEntryRow);
	}
	

	function moreMapAcConstraint() {
		var parentDiv = $(this).parent().clone();
		 
		 
		$(this).parent().parent().append($(parentDiv));
		  var idno=$(parentDiv).attr("id").split("_")[1];
		  idno=parseInt(idno) + 1;
		  $(parentDiv).attr("id","mapAcEntryDiv_"+idno);
		  $(parentDiv).children("input[type=text]").val("");
		 $(parentDiv).children( "#"+$(this).attr("id")).remove();
		 $(parentDiv).children( ".mapAcDeleteEntry").remove();
		$(parentDiv).append("<img alt='-' class='mapAcDeleteEntry' src='images/delete.png' title='Remove'    >");
		$(parentDiv).append($(this));
		$(".mapAcDeleteEntry").bind("click", deleteMapAcEntryRow);
	}
	


	function deleteTemplateAcEntryRow() {
		
		     
		  var imgElement = $("#templateAcAddNewImg"); 
		  $(this).parent().remove();
		   try { 
			 
			   $("#templateAcDiv").children().last().append(imgElement);
			   $(imgElement).unbind("click");
			   $(imgElement).bind("click", moreTemplateAcConstraint);
			   
		   }catch(e) {
			   alert(e);
		   }
		   
	}


	function deleteClassicAcEntryRow() {
		
		     
		  var imgElement = $("#classicAcAddNewImg"); 
		  $(this).parent().remove();
		   try { 
			 
			   $("#classicAcDiv").children().last().append(imgElement);
			   $(imgElement).unbind("click");
			   $(imgElement).bind("click", moreClassicAcConstraint);
			   
		   }catch(e) {
			   alert(e);
		   }
		   
	}


	function deleteMapAcEntryRow() {
		
		     
		  var imgElement = $("#mapAcAddNewImg"); 
		  $(this).parent().remove();
		   try { 
			 
			   $("#mapAcDiv").children().last().append(imgElement);
			   $(imgElement).unbind("click");
			   $(imgElement).bind("click", moreMapAcConstraint);
			   
		   }catch(e) {
			   alert(e);
		   }
		   
	}

	
	function showTemplateUpdateForm() {
		$("#showTemplateDiv").hide();
		  $("#templateUpdateForm").show();
	}
	function fillEdit(vid) {
	 //alert(" this is fill edit");
	 
	 
	 
		bt="#kmBillingTypeHidden";
		if($(bt).val()=="classic") {
			  fillEdit_classic(vid);
		} else if($(bt).val()=="template") {
			fillEdit_template(vid);
		} if($(bt).val()=="map") {
			fillEdit_map(vid);
		} 
	 
	  	  
	}
	

	function fillEdit_map(vid) {
		clearEntry_map();
		
		$("input[name=swingDistance]").val($("#swingDistance_"+vid+"").val());
		$("select[name=distanceType]").val( $("#distanceType_"+vid+"").val() );
		$("select[name=calculationType]").val( $("#calculationType_"+vid+"").val());
		$("#mapId").val( $("#mapId_"+vid+"").val( ));
		$("select[name=mapAcYes]").val( $("#mapAcYes_"+vid+"").val());
		$("input[name=mapEscortRate]").val( $("#mapEscortRate_"+vid+"").val());
		$("select[name=mapEscortRateType]").val( $("#mapEscortRateType_"+vid+"").val());
		$("input[name=mapTripRate]").val($("#mapTripRate_"+vid).val()); 
		// $("input[name=selectedVehicleType]").val($("#selectedMapVehicleType_"+vid).val());
		 
		$("#vehicleType").val($("#selectedMapVehicleType_"+vid).val() );
		
		$("form[name=mapBillingForm]").attr("action", "UpdateKmBasedMapBillingConfig");
		try{ 
		if($("select[name=mapAcYes]").val()=="YES" ) {
			var acno = parseInt($("#mapAcListSize_"+vid).val());
			if(acno>0) {
				$("select[name=mapFromHour]").val($("#mapFromHour_0_"+vid).val());
				$("select[name=mapFromMinute]").val($("#mapFromMinute_0_"+vid).val());
				$("select[name=mapToHour]").val($("#mapToHour_0_"+vid).val());
				$("select[name=mapToMinute]").val($("#mapToMinute_0_"+vid).val());
				$("input[name=mapAcRate]").val($("#mapAcRate_0_"+vid).val());
				 
				for(var i=1; i<acno; i++) {
					$("#mapAcAddNewImg").trigger("click");
					var parentDiv = $("#mapAcAddNewImg").parent();
					$(parentDiv).children("select[name=mapFromHour]").val($("#mapFromHour_"+i+"_"+vid).val());
					$(parentDiv).children("select[name=mapFromMinute]").val($("#mapFromMinute_"+i+"_"+vid).val());
					$(parentDiv).children("select[name=mapToHour]").val($("#mapToHour_"+i+"_"+vid).val());
					$(parentDiv).children("select[name=mapToMinute]").val($("#mapToMinute_"+i+"_"+vid).val());
					$(parentDiv).children("input[name=mapAcRate]").val($("#mapAcRate_"+i+"_"+vid).val());
				}
				
			}
		  }
		}catch(e) {
			alert(e);
		}
	  
		mapSRTChanged();
		mapErChanged();
		mapAcYesChanged();
		  showEntryForm();
	}
	
	
	function fillEdit_classic(vid) {
		clearEntry_classic();
		 $("input[name=classicTripRate]").val($("#classicTripRate_"+vid).val());
		 $("input[name=selectedVehicleType]").val($("#selectedClassicVehicleType_"+vid).val());
		 $("#vehicleType").val($("#selectedClassicVehicleType_"+vid).val());
		 $("input[name=classicId]").val($("#classicId_"+vid).val());
		 $("input[name=classicEscortRate]").val($("#classicEscortRate_"+vid).val());
		 $("select[name=classicEscortRateType]").val($("#classicEscortRateType_"+vid).val());
		 $("select[name=classicAcYes]").val($("#classicAcYes_"+vid).val());
		   
 
		 $("form[name=classicBillingForm]").attr("action", "UpdateKmBasedClassicBillingConfig");
		 try{ 
				if($("select[name=classicAcYes]").val()=="YES" ) {
					var acno = parseInt($("#classicAcListSize_"+vid).val());
					if(acno>0) {
						$("select[name=classicFromHour]").val($("#classicFromHour_0_"+vid).val());
						$("select[name=classicFromMinute]").val($("#classicFromMinute_0_"+vid).val());
						$("select[name=classicToHour]").val($("#classicToHour_0_"+vid).val());
						$("select[name=classicToMinute]").val($("#classicToMinute_0_"+vid).val());
						$("input[name=classicAcRate]").val($("#classicAcRate_0_"+vid).val());
						 
						for(var i=1; i<acno; i++) {
							$("#classicAcAddNewImg").trigger("click");
							var parentDiv = $("#classicAcAddNewImg").parent();
							$(parentDiv).children("select[name=classicFromHour]").val($("#classicFromHour_"+i+"_"+vid).val());
							$(parentDiv).children("select[name=classicFromMinute]").val($("#classicFromMinute_"+i+"_"+vid).val());
							$(parentDiv).children("select[name=classicToHour]").val($("#classicToHour_"+i+"_"+vid).val());
							$(parentDiv).children("select[name=classicToMinute]").val($("#classicToMinute_"+i+"_"+vid).val());
							$(parentDiv).children("input[name=classicAcRate]").val($("#classicAcRate_"+i+"_"+vid).val());
						}
						
					} 
				  }else {
						$("select[name=classicAcYes]").val("NO");
					}
				}catch(e) {
					alert(e);
				}
			  
		 classicAcYesChanged();
		 classicErChanged();
		  showEntryForm();
	}
	
	

	function fillEdit_template(vid) {
		clearEntry_template();
		
		$("input[name=templateSwingRate]").val($("#templateSwingRate_"+vid+"").val());
		$("input[name=templateSwingRateType]").val($("#templateSwingRateType_"+vid+"").val());
		$("input[name=templateId]").val( $("#templateId_"+vid+"").val( ));
		$("input[name=templateKeyId]").val( $("#templateKeyId_"+vid+"").val( ));
		 
		$("select[name=templateAcYes]").val( $("#templateAcYes_"+vid+"").val());
		 
		$("input[name=templateEscortRate]").val( $("#templateEscortRate_"+vid+"").val());
		$("select[name=templateEscortRateType]").val( $("#templateEscortRateType_"+vid+"").val());
		$("input[name=templateTripRate]").val($("#templateTripRate_"+vid).val()); 
		$("#vehicleType").val($("#selectedTemplateVehicleType_"+vid).val() );
		$("form[name=templateBillingForm]").attr("action", "UpdateKmBasedTemplateBillingConfig");
		
		try{ 
		if($("select[name=templateAcYes]").val()=="YES" ) {
			var acno = parseInt($("#templateAcListSize_"+vid).val());
			if(acno>0) {
				$("select[name=templateFromHour]").val($("#templateFromHour_0_"+vid).val());
				$("select[name=templateFromMinute]").val($("#templateFromMinute_0_"+vid).val());
				$("select[name=templateToHour]").val($("#templateToHour_0_"+vid).val());
				$("select[name=templateToMinute]").val($("#templateToMinute_0_"+vid).val());
				$("input[name=templateAcRate]").val($("#templateAcRate_0_"+vid).val());
				 
				for(var i=1; i<acno; i++) {
					$("#templateAcAddNewImg").trigger("click");
					var parentDiv = $("#templateAcAddNewImg").parent();
					$(parentDiv).children("select[name=templateFromHour]").val($("#templateFromHour_"+i+"_"+vid).val());
					$(parentDiv).children("select[name=templateFromMinute]").val($("#templateFromMinute_"+i+"_"+vid).val());
					$(parentDiv).children("select[name=templateToHour]").val($("#templateToHour_"+i+"_"+vid).val());
					$(parentDiv).children("select[name=templateToMinute]").val($("#templateToMinute_"+i+"_"+vid).val());
					$(parentDiv).children("input[name=templateAcRate]").val($("#templateAcRate_"+i+"_"+vid).val());
				}
				
			}
		  }
		}catch(e) {
			alert(e);
		}
	  
		templateSRTChanged();
		templateErChanged();
		templateAcYesChanged();
		  showEntryForm();
	}
	

	
	function  deleteConf_classic(id) {
		if(confirm("Do you really want to delete ?")) {
			 $("#classicId").val(id);
			$("form[name=classicBillingForm]").attr("action", "DeleteKmBasedClassicBillingConfig" );
			 
			$("form[name=classicBillingForm]").submit();
		}
	}
	

	function  deleteConf_template(id) {
		if(confirm("Do you really want to delete ?")) {
			 $("input[name=templateKeyId]").val(id);
			$("form[name=templateBillingForm]").attr("action", "DeleteKmBasedTemplateBillingConfig" );
			 
			$("form[name=templateBillingForm]").submit();
		}
	}
	

	function  deleteConf_map(id) {
		if(confirm("Do you really want to delete ?")) {
			 $("#mapId").val(id);
			$("form[name=mapBillingForm]").attr("action", "DeleteKmBasedMapBillingConfig" );
			 
			$("form[name=mapBillingForm]").submit();
		}
	}
	
/* 	function getForm(  ) {
		bt="#kmBillingTypeHidden";
		if($(bt).val()=="classic") {
			return "classicEntryDiv";
		} else if($(bt).val()=="template") {
			return "templateEntryDiv";
		} if($(bt).val()=="map") {
			return "mapEntryDiv";
		} 
	}
 */
	  function configureClicked() {
		 
		     
		if($("#kmBillingTypeHidden").val()!="") { 
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
				$("form[name=mapBillingForm]").attr("action", "AddKmBasedMapBillingConfig");
				$("form[name=classicBillingForm]").attr("action", "AddKmBasedClassicBillingConfig");
				$("form[name=templateBillingForm]").attr("action", "AddKmBasedTemplateBillingConfig");
				showEntryForm();
			}
		} else {  
			alert("Please update the billing type first");
		}
	}
	function showEntryForm() {
	
		 try { 
			  
			 if($("#vehicleType").val()=="" || $("#vehicleType").val()==undefined ) {
				 alert("Select vehicle type to configure");
			 } else {
				 
				 try {
				$("input[name=selectedVehicleType]").val($("#vehicleType").val());
			 }catch(e) {
				 alert(e);
			 }
				 
				$("#" + getForm() ).show();
			 }
		 }catch(e) {
			alert(e);	 
		 } 
	}
	
	function getForm(  ) {
		bt="#kmBillingTypeHidden";
		if($(bt).val()=="classic") {
			return "classicEntryDiv";
		} else if($(bt).val()=="template") {
			return "templateEntryDiv";
		} if($(bt).val()=="map") {
			 
			return "mapEntryDiv";
		} 
	}
	
	
	function clearEntry_classic() {
		
 			$("#selectedClassicVehicleType").val("");
			$("input[name=tripRate]").val("");
 		$("#classicEntryDiv").hide();
			
			 
	}
	

	function clearEntry_template() {
		
		$("select[name=templateFromHour]").val("");
		$("select[name=templateFromMinute]").val("");
		$("select[name=templateToHour]").val("");
		$("select[name=templateToMinute]").val("");
		$("#templateAcEntryDiv_1").append($("#templateAcAddNewImg"));
		 var cnt =2;
		 while($("#templateAcEntryDiv_"+cnt).length>0  ) {
			  $("#templateAcEntryDiv_"+cnt).remove();
			 
			 cnt++;
		 }

 			$("#selectedTemplateVehicleType").val("");
 			
 			$("input[name=templateEscortRateType]").val("");
 			$("input[name=templateEscortRate]").val("");
			$("input[name=swingRate]").val("");
			$("input[name=swingRateType]").val("");
 		$("#templateEntryDiv").hide();
			
			 
	}
	
	function clearEntry_map() {
		
		$("select[name=mapFromHour]").val("");
		$("select[name=mapFromMinute]").val("");
		$("select[name=mapToHour]").val("");
		$("select[name=mapToMinute]").val("");
		$("#mapAcEntryDiv_1").append($("#mapAcAddNewImg"));
		 var cnt =2;
		 while($("#mapAcEntryDiv_"+cnt).length>0  ) {
			  $("#mapAcEntryDiv_"+cnt).remove();
			 
			 cnt++;
		 }

		$("input[name=swingDistance]").val("");

		$("input[name=swingDistance]").val(""  );
		$("select[name=distanceType]").val(""  );
		$("select[name=calculationType]").val(""  );
		$("#mapId").val(""  );
		$("select[name=mapAcYes]").val(""  );
		$("input[name=mapEscortRate]").val(""  );
		$("select[name=mapEscortRateType]").val(""  );
		$("input[name=mapTripRate]").val(""  ); 
		// $("input[name=selectedVehicleType]").val($("#selectedMapVehicleType_"+vid).val());
		 
		$("#vehicleType").val(""  );
		
		$("#mapEntryDiv").hide();
		
		 
}

	
	
	function validateClassicEntryForm() {
	 
		flag=true;
		if($("input[name=tripRate]").val()=="") {
			alert ("Trip Amount is blank");
			flag=false;
			
		}    	
		if(flag && $("select[name=classicAcYes]").val()=="") {
			alert("A/C constraint is not chosen");
			flag=false;
			
		} else if(flag && $("select[name=classicAcYes]").val()=="YES") {
			   if(flag &&$("input[name=classicAcRate]").val()=="") {
				alert("A/C Constraint : Rate is not chosen");
				flag = false;
			} 
		}
		

	 	if(flag && $("select[name=classicEscortRateType]").val()=="") {
	 		alert("Escort Rate Type is not chosen");
	 		flag = false;
	 	} else if(flag && $("input[name=classicEscortRate]").val()=="" ) {
	 		alert("Escort Rate is not chosen");
	 		
	 		flag = false;
	 	}
	  	

		
	 	return flag;
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
 
 
 function templateAcYesChanged() {
		if($("select[name=templateAcYes]").val()=="YES") {
			$("#templateAcDiv").show();
		} else {
			$("#templateAcDiv").hide();
		}
	}

 function classicAcYesChanged() {
		if($("select[name=classicAcYes]").val()=="YES") {
			$("#classicAcDiv").show();
		} else {
			$("#classicAcDiv").hide();
		}
	}

 function mapAcYesChanged() {
		if($("select[name=mapAcYes]").val()=="YES") {
			$("#mapAcDiv").show();
		} else {
			$("#mapAcDiv").hide();
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
 	<div>
 		<form action="AddKmBasedBillingConfig">
 				<input type="hidden" name="refId" value="<%=request.getParameter("refId") %>" />
 				<%
 				KmBasedBillingConfigDto btdto = new KmBasedBillingTypeConfigService().getKmBasedBillingConfig( request.getParameter("refId"));
 				String btValue="";
 				if( btdto!=null && btdto.getKmBillingType()!=null ) {
 					btValue = btdto.getKmBillingType();
 					
 				}
 				%> 
 				<input type="hidden" id="kmBillingTypeHidden" name="kmBillingTypeHidden" value="<%=btValue%>"  />
 				<label>Billing Type</label> 
 				<select name="kmBillingType" id="kmBillingType" >
 				
 					 
 				<%
 				  
 				ArrayList<SettingsDTO> billingTypeList = new SettingsService().getSettingsStrings(BillingTypeConfigConstants.BILLING_SETUP_MODULE, BillingTypeConfigConstants.KM_BILLING_TYPE);
 	 		 	if(billingTypeList  != null && billingTypeList .size()>0) {
 	 		 		
 	 		 		for(SettingsDTO sdto: billingTypeList ){
 	 		 			String billingTypeSelect = "";
 	 		 		 
 	 		 			if( btdto!=null && btdto.getKmBillingType()!=null && sdto.getKeyValue().equalsIgnoreCase(btdto.getKmBillingType()) ) {
 	 		 				billingTypeSelect="selected";
 	 		 				
 	 		 			}
 	 		 		  %>
 	 		 		<option <%=billingTypeSelect %> value="<%=sdto.getKeyValue()%>"><%=sdto.getValue() %></option>	  
 	 		 	<%
 	 		 	} 
 	 		 	
 	 		 	}%>
 				</select>
 				<input type="submit" value="Update" />
 			
 		</form>
 	
 	</div>
 	<%
	KmTemplateDto kt=null;
 	if(btValue.equalsIgnoreCase(KmBasedBillingConfigConstants.TEMPLATE)) { 
 			KmBasedTemplateBillingService kbs = new KmBasedTemplateBillingService();
 			System.out.println("................................"+btdto.getId());
	     	  kt=kbs.getKmTemplateDto(btdto.getId());
	     	if(kt!=null) { 
	     %>
 	<div id="showTemplateDiv">
 	<p>
 	     <label style="margin-left:20%;" ><a href="viewKmTemplate.jsp?id=<%=btdto.getId()%>" >Template : <%=kt.getTemplateName() %></a> 
 	     	
 	     </label>
 	     <input type="button" value = "Edit" id="editTemplateBtn" />
 	  </p>
 	</div>
 	<%} %>
 	<div id="templateUpdateForm" >
 		<form action="UploadBillingTemplate" method="post"  enctype="multipart/form-data"  >
 		<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 		 	 Template url 
 		 	 </label><input  type="file" name="templateUrl" accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel" />
 		 	 <input type="hidden" name="refId" value = "<%=request.getParameter("refId") %>" />
 		 	 <input type="hidden" name="billingId" value="<%=btdto.getId() %>" />
 		 	 <label>Name</label>
 		 	 <input type="text" name="templateName" />
 		 	 
 		 	  <input type="submit" value="Update"  />
 	 	</form>
 		 	 
 	</div>
 	<% } %>
 	 <div style="padding-top:1%; background-color:#F0F0F0; padding-left:20px;" >
 	 <h3>Km based billing configuration</h3>
 	 <form name="TTForm" action="#" >
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
 
 
 
 <div class="formPanel" id="classicEntryDiv" style =" padding-top: 2px;   display: none; width: 75%; ">
 	<div class="formPanelTitle"  >
 		<p style="float:left; margin-top: 0px; margin-left: 30%;"><b>Fill Configuration - Classic billing setup</b></p>
 		<div style="float: right;" id="closeClassicEntryDiv">
												<img id="closeClassicEntryDiv_img" style="float: right; padding:3px; " src="images/close.png" onclick="clearEntry_classic()" title="Close">
											</div>
 	</div>
 <form name="classicBillingForm" action="AddKmBasedClassicBillingConfig" method="post" >
 <input id="billingTypeId_classic" name="billingTypeId_classic" type="hidden" value ="<%=btValue  %>" />
 	<div id="configEntryDiv_classic" style="padding:20px;   background-color:#F0F0F0;   ">
 	<p></p>
		  <div style="display:inline-block; " >
 		 	<label  >&nbsp;&nbsp;&nbsp;&nbsp;  
 		 	Amount Per Km</label>
 		 	 <input type="text" name="classicTripRate" >
 		 	 <input type="hidden" name="refId"  value="<%=request.getParameter("refId") %>" />
 		 	 <input type="hidden" name="selectedVehicleType" id="selectedClassicVehicleType" />
 		 	 <input type="hidden" id="kmBillingTypeHidden" name="kmBillingTypeHidden" value="<%=btdto==null?"":btdto.getId() %>"  />
 		 	 <input type="hidden" id="classicId" name="classicId" /> 
 		 </div> <p></p>
 		  		 <div style="display:inline-block; " >
 		 	<label  >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 		 	A/C Constraint</label>
 		 	
 		 	<select class="classicSelect1" name="classicAcYes">
 		 		<option value="NO" >NO</option>
 		 		<option value="YES" >YES</option>
 		 	</select>
 		 	   
 		 </div>  
 		 
 		   <div id="classicAcDiv" style="margin-left:20em;; display:  block;  ">
 		   
 		   <div>
 		   	<label style="margin-left:4.75em;">Hour</label>
 		   	<label style="margin-left:1.75em;">Minute</label>
 		   	<label style="margin-left:3.85em;">Hour</label>
 		   	<label style="margin-left:1.7em;">Minute</label>
 		   </div> 
 		   <div id="classicAcEntryDiv_1" >
 		   			 
 		  			<label >From *</label><input type="hidden" name="classicFromTime">
 		  			<select  name="classicFromHour">
 		  			<% int hour=0;
 		  				for (; hour <= 23; hour++) {
					 		String hourString = (hour < 10 ? "0" + hour : "" + hour); 	 
	%>
	 					<option value="<%=hourString%>"  ><%=hourString%></option>
 	<% 				} %>
				 	</select>:
				 	<select  name="classicFromMinute">
 		  			<% int minute=0;
 		  				for (; minute <= 59; minute++) {
					 		String minuteString = (minute < 10 ? "0" + minute : "" + minute); 	 
	%>
	 					<option value="<%=minuteString%>"  ><%=minuteString%></option> 
 	<% 				} %>
				 	</select>
 		  			<label >&nbsp;&nbsp;&nbsp;&nbsp;To</label><input type="hidden" name="classicToTime">
 		  					<select  name="classicToHour">
 		  			<%   hour=0;
 		  				for (; hour <= 23; hour++) {
					 		String hourString = (hour < 10 ? "0" + hour : "" + hour); 	 
	%>
	 					<option value="<%=hourString%>"  ><%=hourString%></option>
 	<% 				} %>
				 	</select>:
				 	<select  name="classicToMinute">
 		  			<%  minute=0;
 		  				for (; minute <= 59; minute++) {
					 		String minuteString = (minute < 10 ? "0" + minute : "" + minute); 	 
	%>
	 					<option value="<%=minuteString%>"  ><%=minuteString%></option> 
 	<% 				} %>
 					</select>
 		  			<label >&nbsp;&nbsp;&nbsp;Rate/Km *</label><input type="text" name="classicAcRate">
 		  			
 		  			<img style="height:23px;" alt="+" id="classicAcAddNewImg" src="images/add_new.png" title="More"   >
 		  			
 		  			 
 		  	 </div>
 
 	 		 
 			 
 		  	 </div>
 		<p></p>  	 
 		 	 
 		 	 <p></p>
 		 <div style="display:inline-block; " >
 		 	<label  >&nbsp;&nbsp;&nbsp;
 		 	Escort Rate Type</label>
 	 	 	<select class="select1" name="classicEscortRateType" >
 		 		<%
 		 		 
 		 		ArrayList<SettingsDTO> escortRateTypeList = new SettingsService().getSettingsStrings(BillingTypeConfigConstants.BILLING_SETUP_MODULE, BillingTypeConfigConstants.ESCORT_RATE_TYPE);
 	 		 
 		 	 		 		 
 	 		 	if(escortRateTypeList != null && escortRateTypeList.size()>0) {
 		 		for(SettingsDTO stDto: escortRateTypeList) { %>
 		 		<option value="<%=stDto.getKeyValue()%>"><%=stDto.getValue()%></option>
 		 		<%} 
 		 		}%>
 		 	 
 		 	</select>
 		 	  
 		 	<p>
 		 	<label id="classicEscortRateLabel" style=" margin-left:7.50em;">Rate </label><input name="classicEscortRate" type="text"><label></label>
 		 	</p>   
 		 	 
 		 </div>  
 		  
 		<p></p> 	
 		 <div>
 		  		<input style="margin-left:23em;" name="updateClassicBtn" onclick="return validateClassicEntryForm()" type="submit" value="Update" >
 		  		<input style="margin-left:0.5em;" id="clearClassicBtn" type="button" value="Clear" >
 		  </div>
 		
 		    
 		
 	</div>
 </form>	
 </div>
 
  <div class="formPanel" id="mapEntryDiv" style =" padding-top: 2px;   display: none; width: 80%; ">
 	<div class="formPanelTitle"  >
 		<p style="float:left; margin-top: 0px; margin-left: 30%;"><b>Fill Configuration - Map based billing setup</b></p>
 		<div style="float: right;" id="closeMapEntryDiv">
												<img id="closeMapEntryDiv_img" style="float: right; padding:3px; " src="images/close.png" onclick="clearEntry_map()" title="Close">
											</div>
 	</div>
 <form name="mapBillingForm" action="AddKmBasedMapBillingConfig">
 <input id="billingTypeId_map" name="billingTypeId_map" type="hidden" value ="<%=btValue  %>" />
 	<div id="configEntryDiv_map" style="padding:20px;   background-color:#F0F0F0;   ">
 	<p></p>
 	<label style="margin-left:6.8em;" >  
 		 	Amount Per Km</label>
 		 	 <input type="text" name="mapTripRate" >
 	<p></p>
		  <div style="display:inline-block; " >
 		  			<label  >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
 		 	Distance Type</label>
 		 	<%
 		 		ArrayList<SettingsDTO> sdtos = new SettingsService().getSettingsStrings(BillingTypeConfigConstants.BILLING_SETUP_MODULE  , BillingTypeConfigConstants.KM_DISTANCE_TYPE);
 		 	%>
 		 	  <select name="distanceType">
 		 	  <%
 		 	  for(SettingsDTO sdto: sdtos) {
 		 	  %>
 		 	  		<option value="<%= sdto.getKeyValue()%>"><%=sdto.getValue() %></option>
 		 	  	 
 		 	  <%
 		 	  }
 		 	  %>
 		 	  </select>
 		 	 <input type="hidden" name="refId"  value="<%=request.getParameter("refId") %>" />
 		 	 <input type="hidden" name="selectedVehicleType" id="selectedMapVehicleType" />
 		 	 <input type="hidden"   name="kmBillingTypeHidden" value="<%=btdto==null?"":btdto.getId() %>"  />
 		 	 <input type="hidden"  id="mapId" name="mapId"   />
 		 	 <p>
 		 	 </p>
 		 	 <label>Swing Addition Per Employee</label>
 		 	 <select name="calculationType" >
 		 	 		 
 		 	 		<%
 		 		ArrayList<SettingsDTO> calculTypeList = new SettingsService().getSettingsStrings(BillingTypeConfigConstants.BILLING_SETUP_MODULE, BillingTypeConfigConstants.ESCORT_RATE_TYPE);
 	 		 	if(calculTypeList != null && calculTypeList.size()>0) {
 		 		for(SettingsDTO stDto: calculTypeList) { %>
 		 		<option value="<%=stDto.getKeyValue()%>"><%=stDto.getValue()%></option>
 		 		<%} 
 		 		}%>
 		 	 </select>
 		 	 <p>
 		 	 </p>
 		 	 <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 		 	 Swing  Value
 		 	 </label>
 		 	 <input type="text" name="swingDistance" /><label></label>
 		 	 
 		 </div>
 		 <p></p>
 		  		 	  		  	 
 		 <div style="display:inline-block; " >
 		 	<label style="margin-left:7.3em;" > 
 		 	A/C Constraint</label>
 		 	
 		 	<select class="mapSelect1" name="mapAcYes">
 		 		<option value="NO" >NO</option>
 		 		<option value="YES" >YES</option>
 		 	</select>
 		 	   
 		 </div>  
 		 
 		   <div id="mapAcDiv" style="margin-left:20em;; display:  block;  ">
 		   
 		   <div>
 		   	<label style="margin-left:4.75em;">Hour</label>
 		   	<label style="margin-left:1.75em;">Minute</label>
 		   	<label style="margin-left:3.85em;">Hour</label>
 		   	<label style="margin-left:1.7em;">Minute</label>
 		   </div> 
 		   <div id="mapAcEntryDiv_1" >
 		   			 
 		  			<label >From *</label><input type="hidden" name="mapFromTime">
 		  			<select  name="mapFromHour">
 		  			<%  hour=0;
 		  				for (; hour <= 23; hour++) {
					 		String hourString = (hour < 10 ? "0" + hour : "" + hour); 	 
	%>
	 					<option value="<%=hourString%>"  ><%=hourString%></option>
 	<% 				} %>
				 	</select>:
				 	<select  name="mapFromMinute">
 		  			<%  minute=0;
 		  				for (; minute <= 59; minute++) {
					 		String minuteString = (minute < 10 ? "0" + minute : "" + minute); 	 
	%>
	 					<option value="<%=minuteString%>"  ><%=minuteString%></option> 
 	<% 				} %>
				 	</select>
 		  			<label >&nbsp;&nbsp;&nbsp;&nbsp;To</label><input type="hidden" name="mapToTime">
 		  					<select  name="mapToHour">
 		  			<%   hour=0;
 		  				for (; hour <= 23; hour++) {
					 		String hourString = (hour < 10 ? "0" + hour : "" + hour); 	 
	%>
	 					<option value="<%=hourString%>"  ><%=hourString%></option>
 	<% 				} %>
				 	</select>:
				 	<select  name="mapToMinute">
 		  			<%  minute=0;
 		  				for (; minute <= 59; minute++) {
					 		String minuteString = (minute < 10 ? "0" + minute : "" + minute); 	 
	%>
	 					<option value="<%=minuteString%>"  ><%=minuteString%></option> 
 	<% 				} %>
 					</select>
 		  			<label >&nbsp;&nbsp;&nbsp;Rate/Km *</label><input type="text" name="mapAcRate">
 		  			
 		  			<img style="height:23px;" alt="+" id="mapAcAddNewImg" src="images/add_new.png" title="More"   >
 		  			
 		  			 
 		  	 </div>
 
 	 		 
 			 
 		  	 </div>
 		<p></p>  	 
 		   
 		 <div style="display:inline-block; " >
 		 	<label style="margin-left:6.2em;" > 
 		 	Escort Rate Type</label>
 	 	 	<select class="select1" name="mapEscortRateType" >
 		 		<%
 		 		 
 	 		 	if(escortRateTypeList != null && escortRateTypeList.size()>0) {
 		 		for(SettingsDTO stDto: escortRateTypeList) { %>
 		 		<option value="<%=stDto.getKeyValue()%>"><%=stDto.getValue()%></option>
 		 		<%} 
 		 		}%>
 		 	 
 		 	</select>
 		 	  
 		 	<p>
 		 	<label id="mapEscortRateLabel" style=" margin-left:12.25em;">Rate </label><input name="mapEscortRate" type="text"><label></label>
 		 	</p>   
 		 	 
 		 </div>  
 		
 		   
 		<p></p> 	
 		 <div>
 		  		<input style="margin-left:23em;" name="updateMapBtn" onclick="return validateMapEntryForm()" type="submit" value="Update" >
 		  		<input style="margin-left:0.5em;" id="clearMapBtn" type="button" value="Clear" >
 		  </div>
 		
 		    
 		
 	</div>
 </form>	
 </div>
 
  <%
  	if(kt!=null) {
  %>
  <div class="formPanel" id="templateEntryDiv" style =" padding-top: 2px;   display: none; width: 70%; ">
 	<div class="formPanelTitle"  >
 		<p style="float:left; margin-top: 0px; margin-left: 30%;"><b>Fill Configuration - Template based billing setup</b></p>
 		<div style="float: right;" id="closeTemplateEntryDiv">
												<img id="closeTemplateEntryDiv_img" style="float: right; padding:3px; " src="images/close.png" onclick="clearEntry_template()" title="Close">
											</div>
 	</div>
 <form name="templateBillingForm" action="AddKmBasedTemplateBillingConfig"  enctype="multipart/form-data"  >
 <input id="billingTypeId_template" name="billingTypeId_template" type="hidden" value ="<%=btValue  %>" />
 	<div id="configEntryDiv_template" style="padding:20px;   background-color:#F0F0F0;   ">
 	<p></p>
		  <div style="display:inline-block; " >
		  <div>
 		  		 
 		 	   
 		 	 <input type="hidden" name="refId"  value="<%=request.getParameter("refId") %>" />
 		 	 <input type="hidden" name="templateId"  value="<%=kt.getTemplateId() %>" />
 		 	  
 		 	 <input type="hidden" name="templateKeyId" />
 		 	 <input type="hidden" name="selectedVehicleType" id="selectedTemplateVehicleType" />
 		 	 
 		 	 <input type="hidden"   name="kmBillingTypeHidden" value="<%=btdto==null?"":btdto.getId() %>"  />
 		 	   
 		 	 	<label  >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 		 	Rate / Km :</label><input type="text" name="templateTripRate" />
 		 	 
 		</div> 	  		  	 <p></p>
 		 	  		  	 
 		 <div style="display:inline-block; " >
 		 	<label  >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 		 	A/C Constraint</label>
 		 	
 		 	<select class="templateSelect1" name="templateAcYes">
 		 		<option value="NO" >NO</option>
 		 		<option value="YES" >YES</option>
 		 	</select>
 		 	   
 		 </div>  
 		 
 		   <div id="templateAcDiv" style="margin-left:20em; display:  block;  ">
 		   
 		   <div>
 		   	<label style="margin-left:4.75em;">Hour</label>
 		   	<label style="margin-left:1.75em;">Minute</label>
 		   	<label style="margin-left:3.85em;">Hour</label>
 		   	<label style="margin-left:1.7em;">Minute</label>
 		   </div> 
 		   <div id="templateAcEntryDiv_1" >
 		   			 
 		  			<label >From *</label><input type="hidden" name="templateFromTime">
 		  			<select  name="templateFromHour">
 		  			<%  hour=0;
 		  				for (; hour <= 23; hour++) {
					 		String hourString = (hour < 10 ? "0" + hour : "" + hour); 	 
	%>
	 					<option value="<%=hourString%>"  ><%=hourString%></option>
 	<% 				} %>
				 	</select>:
				 	<select  name="templateFromMinute">
 		  			<%  minute=0;
 		  				for (; minute <= 59; minute++) {
					 		String minuteString = (minute < 10 ? "0" + minute : "" + minute); 	 
	%>
	 					<option value="<%=minuteString%>"  ><%=minuteString%></option> 
 	<% 				} %>
				 	</select>
 		  			<label >&nbsp;&nbsp;&nbsp;&nbsp;To</label><input type="hidden" name="templateToTime">
 		  					<select  name="templateToHour">
 		  			<%   hour=0;
 		  				for (; hour <= 23; hour++) {
					 		String hourString = (hour < 10 ? "0" + hour : "" + hour); 	 
	%>
	 					<option value="<%=hourString%>"  ><%=hourString%></option>
 	<% 				} %>
				 	</select>:
				 	<select  name="templateToMinute">
 		  			<%  minute=0;
 		  				for (; minute <= 59; minute++) {
					 		String minuteString = (minute < 10 ? "0" + minute : "" + minute); 	 
	%>
	 					<option value="<%=minuteString%>"  ><%=minuteString%></option> 
 	<% 				} %>
 					</select>
 		  			<label >&nbsp;&nbsp;&nbsp;Rate/Km *</label><input type="text" name="templateAcRate">
 		  			
 		  			<img style="height:23px;" alt="+" id="templateAcAddNewImg" src="images/add_new.png" title="More"   >
 		  			
 		  			 
 		  	 </div>
 
 	 		 
 			 
 		  	 </div>
 		<p></p>  	 
 		 	 <div> 
 		 	 <label>Swing Addition Per Employee</label>
 		 	 <select name="templateSwingRateType" >
 		 	 		 
 		 	 		<%
 		 		ArrayList<SettingsDTO> escortRateTypeList1 = new SettingsService().getSettingsStrings(BillingTypeConfigConstants.BILLING_SETUP_MODULE, BillingTypeConfigConstants.ESCORT_RATE_TYPE);
 	 		 	if(escortRateTypeList1 != null && escortRateTypeList1.size()>0) {
 		 		for(SettingsDTO stDto: escortRateTypeList1) { %>
 		 		<option value="<%=stDto.getKeyValue()%>"><%=stDto.getValue()%></option>
 		 		<%} 
 		 		}%>
 		 	 </select>
 		 	   <p>
 		 	 </p><label> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 		 	 Swing  Value</label><input type="text" name="templateSwingRate"  /><label></label>
 		 	 
 		 	 </div>
 		 	 <p></p>
 		 <div style="display:inline-block; " >
 		 	<label  >&nbsp;&nbsp;&nbsp;
 		 	Escort Rate Type</label>
 	 	 	<select class="select1" name="templateEscortRateType" >
 		 		<%
 		 		 
 	 		 	if(escortRateTypeList != null && escortRateTypeList.size()>0) {
 		 		for(SettingsDTO stDto: escortRateTypeList) { %>
 		 		<option value="<%=stDto.getKeyValue()%>"><%=stDto.getValue()%></option>
 		 		<%} 
 		 		}%>
 		 	 
 		 	</select>
 		 	  
 		 	<p>
 		 	<label id="templateEscortRateLabel" style=" margin-left:8em;">Rate </label><input name="templateEscortRate" type="text"><label></label>
 		 	</p>   
 		 	 
 		 </div>  
 		
 		 	 
 		 </div>  
 		<p></p> 	
 		 <div>
 		  		<input style="margin-left:23em;" name="updateTemplateBtn" onclick="return validateTemplateEntryForm()" type="submit" value="Update" >
 		  		<input style="margin-left:0.5em;" id="clearTemplateBtn" type="button" value="Clear" >
 		  </div>
 		
 		    
 		
 	</div>
 </form>	
 </div>
 
 <%} %>
 <div style="background-color: #F0F0F0; margin-top:10px; position: relative; height: 10%;    overflow: hidden;" > 
 <%
 //System.out.println(" Params : " + btdto.getId() + "  "  +  btdto.getKmBillingType());
  		KmBasedClassicBillingTypeConfigService  service = new KmBasedClassicBillingTypeConfigService();
 		ArrayList< KmBasedClassicTripBillingConfigDto > dtoList =  service.getKmBasedClassicBillingConfig(request.getParameter("refId" ) );
 		if(dtoList!= null && dtoList.size()>0) {
 		for(KmBasedClassicTripBillingConfigDto dto : dtoList) {		
 		 
 %>
 			<div style="padding-top:5px; width:25em; float: left; " >
 				<table class="displaytag" style=" " >
 					<thead> <tr><th  colspan="2">
 									<div>
 										<div style="display:inline-block; width:60%;" > <label>Vehicle</label> <%=dto.getVehicleTypeDto().getType() %><br/>
 				
 					 
 											<input type="hidden" value="<%=dto.getRate() %>" id="classicTripRate_<%=dto.getVehicleTypeId() %>" />
											<input type="hidden" value="<%=dto.getVehicleTypeId() %>" class="selectedVehicleType" id="selectedClassicVehicleType_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getId() %>" id="classicId_<%=dto.getVehicleTypeId() %>"  name="classicId_<%=dto.getVehicleTypeId() %>" />
 											 
 											<input type="hidden" value="<%=dto.getId() %>" id="classicId_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getEscortRate() %>" id="classicEscortRate_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getEscortRateType() %>" id="classicEscortRateType_<%=dto.getVehicleTypeId() %>" />
 											 
 											<input type="hidden" value="<%=dto.getAcYes() %>" id="classicAcYes_<%=dto.getVehicleTypeId() %>" />
 											
 											
 										</div>
 										
 										<div style="display:inline-block; float:right; " >
 											
 											
 										 <a href="#" style="  font-weight: 100; " onclick="fillEdit_classic('<%=dto.getVehicleTypeId() %>')" >Edit </a>
 									<label   style=" width:2px;" > | </label>
 									<a href="#" style=" font-weight: 100; " onclick="deleteConf_classic('<%=dto.getId() %>')" >Delete</a>
 											 </div> 
 									</div>
 									
 					
 								</th>
 								   
 							</tr> 
 							<tr> <td colspan="2">
 									
 									
 								</td>
 							</tr>
 							<tr >
 								<td  style="width: 50%; text-align: center;"> <label>Rate / Km </label> <%=dto.getRate() %> <br/>
 										 
 								</td>
 							
 								<td  style="width: 50%; text-align: center;"> <label>Escort Rate </label> <%=dto.getEscortRate()%> <%=dto.getEscortRateType().equalsIgnoreCase(KmBasedBillingConfigConstants.VARIED)?"%":"" %> 
 								<br/>
 										 
 								</td>
 								 
 							</tr> 					 
 					</thead>
 					 <tbody>
 					 		 
							 					 <% 
 					 		  if(dto.getAcList()!=null && dto.getAcList().size()>0) {
 		 				%>
 		 				 
 						<tr><td colspan="3" ><input type="hidden" value="<%=dto.getAcList().size()  %>" id="classicAcListSize_<%=dto.getVehicleTypeId() %>" />
 										
 						
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
 												 	
 												 
 												 	
													<input type="hidden" value="<%=fromHour %>" id="classicFromHour_<%=i %>_<%=dto.getVehicleTypeId() %>" />
 												 	<input type="hidden" value="<%=fromMinute %>" id="classicFromMinute_<%=i %>_<%=dto.getVehicleTypeId() %>" />
	 												 <input type="hidden" value="<%=toHour %>" id="classicToHour_<%=i %>_<%=dto.getVehicleTypeId() %>" />
	 												 <input type="hidden" value="<%=toMinute %>" id="classicToMinute_<%=i %>_<%=dto.getVehicleTypeId() %>" />
	 												 <input type="hidden" value="<%=acDto.getRate() %>" id="classicAcRate_<%=i %>_<%=dto.getVehicleTypeId() %>" />
 												 	
 												 	
 												 </td>
 												 <td><%=acDto.getToTime()  %></td>
 												 <td><%=acDto.getRate() %></td></tr>
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
 	
 	 <div style="background-color: #F0F0F0; margin-top:10px; position: relative; height: 10%;    overflow: hidden;" > 
 <%
// System.out.println(" Params : " + btdto.getId() + "  "  +  btdto.getKmBillingType());
  		KmBasedMapBillingTypeConfigService  mapService = new KmBasedMapBillingTypeConfigService();
 		ArrayList< KmBasedMapTripBillingConfigDto > mapDtoList = mapService.getKmBasedMapBillingConfig(request.getParameter("refId" ) );
 		if(mapDtoList != null && mapDtoList .size()>0) {
 		for(KmBasedMapTripBillingConfigDto dto : mapDtoList) {		
 		 
 %>
 			<div style="padding-top:5px; width:25em; float: left; " >
 				<table class="displaytag" style=" " >
 					<thead> <tr><th  colspan="2">
 									<div>
 										<div style="display:inline-block; width:60%;" > <label>Vehicle</label> <%=dto.getVehicleTypeDto().getType() %><br/>
 				
 					 
 											<input type="hidden" value="<%=dto.getRate() %>" id="swingDistance_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getVehicleTypeId() %>" class="selectedVehicleType" id="selectedMapVehicleType_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getDistanceType()  %>" id="distanceType_<%=dto.getVehicleTypeId() %>" />
 											
 											<input type="hidden" value="<%=dto.getCalculationType()  %>" id="calculationType_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getId() %>" id="mapId_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getEscortRate() %>" id="mapEscortRate_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getEscortRateType() %>" id="mapEscortRateType_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getTripRate() %>" id="mapTripRate_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getAcYes() %>" id="mapAcYes_<%=dto.getVehicleTypeId() %>" />
 											 
 										</div>
 										
 										<div style="display:inline-block; float:right; " >
 											
 											
 										 <a href="#" style="  font-weight: 100; " onclick="fillEdit_map('<%=dto.getVehicleTypeId() %>')" >Edit </a>
 									<label   style=" width:2px;" > | </label>
 									<a href="#" style=" font-weight: 100; " onclick="deleteConf_map('<%=dto.getId() %>')" >Delete</a>
 											 </div> 
 									</div>
 									
 					
 								</th>
 								   
 							</tr> 
 							 
 							<tr   >
 								<td > <label  >Distance Type</label> 
 										 
 								</td>
 								<td> <%=dto.getDistanceType() %> 
 								</td>
 							
 							</tr>
 							<tr>
 									<td>
 										<label>Swing Addition Per Employee</label>  
 									</td>
 									<td><%=dto.getCalculationType() %>
 									</td>
 							</tr>
 							<tr >
 								<td   style=""> <label>Swing Value</label>  
 										 
 								</td>
 								<td> <%=dto.getRate() %> 
 								</td>
 								 
 							</tr>
 							 							<tr >
 								<td   style=""> <label>Escort Type</label>  
 										 
 								</td>
 								<td> <%=dto.getEscortRateType() %> 
 								</td>
 								 
 							</tr> 
 							 							<tr >
 								<td   style=""> <label>Escort Rate</label>  
 										 
 								</td>
 								<td> <%=dto.getEscortRate() %> <%=dto.getEscortRateType().equalsIgnoreCase(KmBasedBillingConfigConstants.VARIED)?"%":"" %>
 								</td>
 								 
 							</tr> 					 
 												 
 							 					 
 					</thead>
 					 <tbody>
 					 <% 
 					 		  if(dto.getAcList()!=null && dto.getAcList().size()>0) {
 		 				%>
 		 				 
 						<tr><td colspan="3" ><input type="hidden" value="<%=dto.getAcList().size()  %>" id="mapAcListSize_<%=dto.getVehicleTypeId() %>" />
 										
 						
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
 												 	
 												 
 												 	
													<input type="hidden" value="<%=fromHour %>" id="mapFromHour_<%=i %>_<%=dto.getVehicleTypeId() %>" />
 												 	<input type="hidden" value="<%=fromMinute %>" id="mapFromMinute_<%=i %>_<%=dto.getVehicleTypeId() %>" />
	 												 <input type="hidden" value="<%=toHour %>" id="mapToHour_<%=i %>_<%=dto.getVehicleTypeId() %>" />
	 												 <input type="hidden" value="<%=toMinute %>" id="mapToMinute_<%=i %>_<%=dto.getVehicleTypeId() %>" />
	 												 <input type="hidden" value="<%=acDto.getRate() %>" id="mapAcRate_<%=i %>_<%=dto.getVehicleTypeId() %>" />
 												 	
 												 	
 												 </td>
 												 <td><%=acDto.getToTime()  %></td>
 												 <td><%=acDto.getRate() %></td></tr>
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
 	
 	<!--  display template billing  -->
 	 <div style="background-color: #F0F0F0; margin-top:10px; position: relative; height: 10%;    overflow: hidden;" > 
 <%
// System.out.println(" Params : " + btdto.getId() + "  "  +  btdto.getKmBillingType());
  		KmBasedTemplateBillingService templateService = new KmBasedTemplateBillingService();
 		ArrayList< KmBasedTemplateTripBillingConfigDto> templateDtoList = templateService.getKmBasedTemplateBillingConfig(    request.getParameter("refId" ) );
 		if(templateDtoList != null && templateDtoList .size()>0) {
 			
 		for(KmBasedTemplateTripBillingConfigDto dto : templateDtoList) {		
 		 
 %>
 			<div style="padding-top:5px; width:25em; float: left; " >
 				<table class="displaytag" style=" " >
 					<thead> <tr><th  colspan="2">
 									<div>
 										<div style="display:inline-block; width:60%;" > <label>Vehicle</label> <%=dto.getVehicleTypeDto().getType() %><br/>
 				
 					 
 											<input type="hidden" value="<%=dto.getSwingRate() %>" id="templateSwingRate_<%=dto.getVehicleTypeId() %>" />
 											
 											<input type="hidden" value="<%=dto.getVehicleTypeId() %>" class="selectedVehicleType" id="selectedTemplateVehicleType_<%=dto.getVehicleTypeId() %>" />
 											 
 											
 											<input type="hidden" value="<%=dto.getSwingRateType()  %>" id="templateSwingRateType_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getId() %>" id="templateId_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden"  value="<%=dto.getId() %>"  id="templateKeyId_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getEscortRate() %>" id="templateEscortRate_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getEscortRateType() %>" id="templateEscortRateType_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getTripRate() %>" id="templateTripRate_<%=dto.getVehicleTypeId() %>" />
 											<input type="hidden" value="<%=dto.getAcYes() %>" id="templateAcYes_<%=dto.getVehicleTypeId() %>" />
 											 
 										</div>
 										
 										<div style="display:inline-block; float:right; " >
 											
 											
 										 <a href="#" style="  font-weight: 100; " onclick="fillEdit_template('<%=dto.getVehicleTypeId() %>')" >Edit </a>
 									<label   style=" width:2px;" > | </label>
 									<a href="#" style=" font-weight: 100; " onclick="deleteConf_template('<%=dto.getId() %>')" >Delete</a>
 											 </div> 
 									</div>
 									
 					
 								</th>
 								   
 							</tr> 
 							 
 							<tr   >
 								<td > <label  >Rate / Km</label> 
 										 
 								</td>
 								<td> <%=dto.getTripRate()%> 
 								</td>
 							
 							</tr>
 							<tr>
 									<td>
 										<label>Swing Addition Per Employee</label>  
 									</td>
 									<td><%=dto.getSwingRate() %> <%=dto.getSwingRateType().equalsIgnoreCase(KmBasedBillingConfigConstants.VARIED)?"%":"" %>
 									</td>
 							</tr>
 							<tr >
 								<td   style=""> <label>Escort Rate</label>  
 										 
 								</td>
 								<td> <%=dto.getEscortRate() %><%=dto.getEscortRateType().equalsIgnoreCase(KmBasedBillingConfigConstants.VARIED)?"%":"" %> 
 								</td>
 								 
 							</tr>
  							  					 
 												 
 							 					 
 					</thead>
 					 <tbody>
 					 <% 
 					 		  if(dto.getAcList()!=null && dto.getAcList().size()>0) {
 		 				%>
 		 				 
 						<tr><td colspan="3" ><input type="hidden" value="<%=dto.getAcList().size()  %>" id="templateAcListSize_<%=dto.getVehicleTypeId() %>" />
 										
 						
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
 												 	
 												 
 												 	
													<input type="hidden" value="<%=fromHour %>" id="templateFromHour_<%=i %>_<%=dto.getVehicleTypeId() %>" />
 												 	<input type="hidden" value="<%=fromMinute %>" id="templateFromMinute_<%=i %>_<%=dto.getVehicleTypeId() %>" />
	 												 <input type="hidden" value="<%=toHour %>" id="templateToHour_<%=i %>_<%=dto.getVehicleTypeId() %>" />
	 												 <input type="hidden" value="<%=toMinute %>" id="templateToMinute_<%=i %>_<%=dto.getVehicleTypeId() %>" />
	 												 <input type="hidden" value="<%=acDto.getRate() %>" id="templateAcRate_<%=i %>_<%=dto.getVehicleTypeId() %>" />
 												 	
 												 	
 												 </td>
 												 <td><%=acDto.getToTime()  %></td>
 												 <td><%=acDto.getRate() %></td></tr>
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
