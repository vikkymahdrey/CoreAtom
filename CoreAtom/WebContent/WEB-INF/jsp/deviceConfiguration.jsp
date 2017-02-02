 
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.service.SettingsService"%>
<%@page import="com.agiledge.atom.dto.SettingsDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="disp" uri="http://www.nfl.com" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";


 
 
 .label {
 	width:200px; display: inline-block; margin-top: 15px; text-align: right;  
 }
 
 .dtLabel {
 	width:68px; display: inline-block; margin-top: 15px; text-align: right; padding-left:3px;  
 }
 
 .propHldr {
 		width:80px; display: inline-block; 
 }
 
 input[type=text] {
 	width: 60px;
 	 
 }
 select {
 	width: 60px;
 	align:left;
 }
  
 /* ---- */
 
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script src="js/dateValidation.js"></script>
<script type="text/javascript">
var curId;
function openWindow(url) {
	window.open(url, 'Ratting',
			'width=400,height=350,left=150,top=200,toolbar=1,status=1,');

}
$(document).ready(function()
        {                                                                        
            $(".fromDate").datepick();
          
            
        });
        
    

   function postUpdate(property, element) {
	   try { 
		   
	   var fromDate = $("#fromdate_"+element).val();
		var today = new Date();
		var todayVar = today.getDate() + "/" + (today.getMonth()+1 ) + "/" + today.getFullYear();
		 if( $("#property_"+element).val().trim()=="") {
			 alert("Invalid Value");
		 } else if( fromDate.trim()=="") {
			 alert("Invalid Date");
		 }else if(CompareTwodiffDates(fromDate, todayVar)) {
			  	alert("Choose today or above.");
			 
		 } else { 
	
			  curId=element;
			   var lookup = {"property":property , "value":"\""+$("#property_"+element).val()+"\""  };
			   $("#loadingImage_"+element).show();
			    
			   alert($("#site").val());
			   ajaxPost("updateSettings.do?property=" + property + "&value=" + encodeURI( $("#property_"+element).val()) + "&fromDate=" + $("#fromdate_"+element).val()+ "&site=" +$("#site").val(), lookup, showResponse);
   } 
	 
    
	   }catch(e) {
		   alert(e);
	   }
   }
   
   function postUpdateWithoutSite(property, element) {
	   try { 
		   
	   var fromDate = $("#fromdate_"+element).val();
		var today = new Date();
		var todayVar = today.getDate() + "/" + (today.getMonth()+1 ) + "/" + today.getFullYear();
		 if( $("#property_"+element).val().trim()=="") {
			 alert("Invalid Value");
		 } else if( fromDate.trim()=="") {
			 alert("Invalid Date");
		 }else if(CompareTwodiffDates(fromDate, todayVar)) {
			  	alert("Choose today or above.");
			 
		 } else { 
	
			  curId=element;
			   var lookup = {"property":property , "value":"\""+$("#property_"+element).val()+"\""  };
			   $("#loadingImage_"+element).show();
			    
			 
			   ajaxPost("updateSettings.do?property=" + property + "&value=" + encodeURI( $("#property_"+element).val()) + "&fromDate=" + $("#fromdate_"+element).val() , lookup, showResponse);
   } 
	 
    
	   }catch(e) {
		   alert(e);
	   }
   }
   
   function showResponse(data) {
	  
	   alert (data );
	    
   }
   function ajaxPost( urlParam, lookup, retFunction) {
		//alert(urlParam   );
		$.ajax(
				{
					type:"POST",
					url: urlParam,
					data:lookup,
					async: false,
					 
					success: function (data) {
						try { 
						$("#loadingImage_"+curId).hide();
						}catch(e){alert(e);}
						 
						 retFunction( data);
					},
				 error:function (xhr, ajaxOptions, thrownError){
					 try{
					 $("#loadingImage_"+curId).hide();
					 }catch(e){alert(e);}
			            alert("in error" + xhr);
			        } 
				}		
			);
	}
   
   function alerti(data) {
	    
	  
	   $("#page").attr("src","showSettings.do?property=" + data + "&site=" + $("#site").val());
   }
   
   function showPage(data) {
	   $("#page").html(data);
   }

</script>
<title>Device Configuration Settings</title>
</head>
<body>
<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="../../Header.jsp"%>
	<%
	
		}
	
  	%>
	<div id="body">
		<div class="content">
<h3 align="center">Settings</h3>
<div>
 <form>
	 
				<div style="margin-left:3%; width:60%; float:left;  ">
							<div>  
							<label class="label">Site</label>
							<form:form commandName="showDeviceParamSettings" action="deviceSettings">
								 
								<form:select id="site" path="site" itemValue="id" itemLabel="name" items="${sites }" >
									<form:option value="">--Select the Site</form:option>
									
								</form:select>
							</form:form>
							 
							</div>
						     
 						     <label  class="label"  >AFFORDABLE ON TIME DELAY (Minutes)</label>  <div class="propHldr"><input id="property_0" type="text" class="number"   /></div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_0" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('AFFORDABLE ON TIME DELAY','0')" /> 
							  <label id="loader_0" ><img style="display:none;"  id="loadingImage_0" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('AFFORDABLE ON TIME DELAY')" >History</a> <br/> 
							
							 <label  class="label"  >PRE START DELAY FOR IN (Minutes)</label>  <div class="propHldr"><input id="property_1" type="text"  class="number"  /></div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_1" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('PRE START DELAY FOR IN','1')" /> 
							  <label id="loader_1" ><img style="display:none;"  id="loadingImage_1" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('PRE START DELAY FOR IN')" >History</a> <br/> 
							
							 <label  class="label"  >POST START DELAY FOR IN (Minutes)</label>  <div class="propHldr"><input id="property_2" type="text"  class="number"  /></div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_2" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('POST START DELAY FOR IN','2')" /> 
							  <label id="loader_2" ><img style="display:none;"  id="loadingImage_2" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('POST START DELAY FOR IN')" >History</a> <br/> 
							
							 <label  class="label"  >PRE STOP DELAY FOR OUT (Minutes)</label>  <div class="propHldr"><input id="property_3" type="text"  class="number" /></div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_3" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('PRE STOP DELAY FOR OUT','3')" /> 
							  <label id="loader_3" ><img style="display:none;"  id="loadingImage_3" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('PRE STOP DELAY FOR OUT')" >History</a> <br/> 
							
							 <label  class="label"  >POST STOP DELAY FOR OUT (Minutes)</label>  <div class="propHldr"><input id="property_4" type="text"  class="number"  /></div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_4" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('POST STOP DELAY FOR OUT','4')" /> 
							  <label id="loader_4" ><img style="display:none;"  id="loadingImage_4" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('POST STOP DELAY FOR OUT')" >History</a> <br/> 
							
							 <label  class="label"  >TRIP START VALIDATION (True/False)</label>  <div class="propHldr">
							 
							 <select id="property_5" >
							 <c:forEach var="item" items="${trueFalseLabel}">
							 <option value="${item }">${item } </option>
							  
							 </c:forEach>
							 </select>
							 </div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_5" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('TRIP START VALIDATION','5')" /> 
							  <label id="loader_5" ><img style="display:none;"  id="loadingImage_5" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('TRIP START VALIDATION')" >History</a> <br/> 
							
							 <label  class="label"  >TRIP STOP VALIDATION</label>  <div class="propHldr">
								
							 <select id="property_6" >
							 <c:forEach var="item" items="${trueFalseLabel}">
							 <option value="${item }">${item } </option>
							  
							 </c:forEach>
							 </select>
							</div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_6" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('TRIP STOP VALIDATION','6')" /> 
							  <label id="loader_6" ><img style="display:none;"  id="loadingImage_6" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('TRIP STOP VALIDATION')" >History</a> <br/> 
							
							 <label  class="label"  >PRE STOP DELAY FOR IN</label>  <div class="propHldr"><input id="property_7" type="text"  class="number"  /></div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_7" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('PRE STOP DELAY FOR IN','7')" /> 
							  <label id="loader_7" ><img style="display:none;"  id="loadingImage_7" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('PRE STOP DELAY FOR IN')" >History</a> <br/> 
							
							 <label  class="label"  >POST STOP DELAY FOR IN</label>  <div class="propHldr"><input id="property_8" type="text"  class="number"  /></div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_8" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('POST STOP DELAY FOR IN','8')" /> 
							  <label id="loader_8" ><img style="display:none;"  id="loadingImage_8" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('POST STOP DELAY FOR IN')" >History</a> <br/> 
							
							 <label  class="label"  >PRE START DELAY FOR OUT</label>  <div class="propHldr"><input id="property_9" type="text"  class="number"  /></div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_9" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('PRE START DELAY FOR OUT','9')" /> 
							  <label id="loader_9" ><img style="display:none;"  id="loadingImage_9" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('PRE START DELAY FOR OUT')" >History</a> <br/> 
							
							 <label  class="label"  >POST START DELAY FOR OUT</label>  <div class="propHldr"><input id="property_10" type="text"  class="number"  /></div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_10" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('POST START DELAY FOR OUT','10')" /> 
							  <label id="loader_10" ><img style="display:none;"  id="loadingImage_10" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('POST START DELAY FOR OUT')" >History</a> <br/> 
							
							 <label  class="label"  >DOUBLE AUTHENTICATION FOR EMPLOYEE PICKUP</label>  <div class="propHldr">
							
							 <select id="property_11" >
							 <c:forEach var="item" items="${trueFalseLabel}">
							 <option value="${item }">${item } </option>
							  
							 </c:forEach>
							 </select>
							</div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_11" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('DOUBLE AUTHENTICATION FOR EMPLOYEE PICKUP','11')" /> 
							  <label id="loader_11" ><img style="display:none;"  id="loadingImage_11" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('DOUBLE AUTHENTICATION FOR EMPLOYEE PICKUP')" >History</a> <br/> 
							
							 <label  class="label"  >DOUBLE AUTHENTICATION FOR EMPLOYEE DROP</label>  <div class="propHldr"> 
							 
							 <select id="property_12" >
							 <c:forEach var="item" items="${trueFalseLabel}">
							 <option value="${item }">${item } </option>
							  
							 </c:forEach>
							 </select>
							 </div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_12" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('DOUBLE AUTHENTICATION FOR EMPLOYEE DROP','12')" /> 
							  <label id="loader_12" ><img style="display:none;"  id="loadingImage_12" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('DOUBLE AUTHENTICATION FOR EMPLOYEE DROP')" >History</a> <br/> 
							
							 <label  class="label"  >DOUBLE AUTHENTICATION FOR ESCORT PICKUP</label>  <div class="propHldr"> 
							 
							 <select id="property_13" >
							 <c:forEach var="item" items="${trueFalseLabel}">
							 <option value="${item }">${item } </option>
							  
							 </c:forEach>
							 </select>
							 </div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_13" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('DOUBLE AUTHENTICATION FOR ESCORT PICKUP','13')" /> 
							  <label id="loader_13" ><img style="display:none;"  id="loadingImage_13" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('DOUBLE AUTHENTICATION FOR ESCORT PICKUP')" >History</a> <br/> 
							
							 <label  class="label"  >DOUBLE AUTHENTICATION FOR ESCORT DROP</label>  <div class="propHldr">
								
							 <select id="property_14" >
							 <c:forEach var="item" items="${trueFalseLabel}">
							 <option value="${item }">${item } </option>
							  
							 </c:forEach>
							 </select>
							</div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_14" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('DOUBLE AUTHENTICATION FOR ESCORT DROP','14')" /> 
							  <label id="loader_14" ><img style="display:none;"  id="loadingImage_14" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('DOUBLE AUTHENTICATION FOR ESCORT DROP')" >History</a> <br/> 
							
							 <label  class="label"  >FIRST ESCORT  AUTH BEFORE EMP AUTH PICKUP</label>  <div class="propHldr"> 
							 	
							 <select id="property_15" >
							 <c:forEach var="item" items="${trueFalseLabel}">
							 <option value="${item }">${item } </option>
							  
							 </c:forEach>
							 </select>
							 
							 </div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_15" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('FIRST ESCORT  AUTH BEFORE EMP AUTH PICKUP','15')" /> 
							  <label id="loader_15" ><img style="display:none;"  id="loadingImage_15" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('FIRST ESCORT  AUTH BEFORE EMP AUTH PICKUP')" >History</a> <br/> 
							
							 <label  class="label"  >SECOND ESCORT AUTH AFTER EMP AUTH PICKUP</label>  <div class="propHldr">
								
							 <select id="property_16" >
							 <c:forEach var="item" items="${trueFalseLabel}">
							 <option value="${item }">${item } </option>
							  
							 </c:forEach>
							 </select>
							 
							 </div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_16" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('SECOND ESCORT AUTH AFTER EMP AUTH PICKUP','16')" /> 
							  <label id="loader_16" ><img style="display:none;"  id="loadingImage_16" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('SECOND ESCORT AUTH AFTER EMP AUTH PICKUP')" >History</a> <br/> 
							
							 <label  class="label"  >FIRST ESCORTAUTHENTICATION BEFORE EMPLOYEE AUTHENTICATION DROP</label>  <div class="propHldr"> 
							 
							 <select id="property_17" >
							 <c:forEach var="item" items="${trueFalseLabel}">
							 <option value="${item }">${item } </option>
							  
							 </c:forEach>
							 </select>
							 </div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_17" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('FIRST ESCORTAUTHENTICATION BEFORE EMPLOYEE AUTHENTICATION DROP','17')" /> 
							  <label id="loader_17" ><img style="display:none;"  id="loadingImage_17" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('FIRST ESCORTAUTHENTICATION BEFORE EMPLOYEE AUTHENTICATION DROP')" >History</a> <br/> 
							
							 <label  class="label"  >SECOND ESCORTAUTHENTICATION AFTER EMPLOYEE AUTHENTICATION DROP</label>  <div class="propHldr">
							 <c:set var="count" value="0" scope="page" />
 

							 <select id="property_18" >
							 <c:forEach var="item" items="${trueFalseLabel}">
							 <option value="${item }">${item } </option>
							  
							 </c:forEach>
							 </select>
							 </div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_19" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdate('SECOND ESCORTAUTHENTICATION AFTER EMPLOYEE AUTHENTICATION DROP','18')" /> 
							  <label id="loader_19" ><img style="display:none;"  id="loadingImage_19" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('SECOND ESCORTAUTHENTICATION AFTER EMPLOYEE AUTHENTICATION DROP')" >History</a> <br/> 

							 <label  class="label"  >AUTOMATIC DEVICE VEHICLE MAPPING</label>  <div class="propHldr">
							 <c:set var="count" value="0" scope="page" />
 

							 <select id="property_20" >
							 <c:forEach var="item" items="${trueFalseLabel}">
							 <option value="${item }">${item } </option>
							  
							 </c:forEach>
							 </select>
							 </div> 
							 <label  class="dtLabel"  >From Date</label>   <input type="text" class="fromDate" id="fromdate_20" readonly="readonly" />   
							 <input type="button" value="Update" class="formbutton" onclick="postUpdateWithoutSite('AUTOMATIC DEVICE VEHICLE MAPPING','20')" /> 
							  <label id="loader_20" ><img style="display:none;"  id="loadingImage_20" src="images/ajax-loader.gif"/> </label> 
							 <a onclick="alerti('AUTOMATIC DEVICE VEHICLE MAPPING')" >History</a> <br/> 
							
							 						    
						    
				</div>
				<div style="float:left;width:35%;">
				  <iframe width="100%" id="page" ></iframe>
				</div>
</form>
<%@include file='../../Footer.jsp'%>
		</div>
		</div>
		</div>

</body>
</html>