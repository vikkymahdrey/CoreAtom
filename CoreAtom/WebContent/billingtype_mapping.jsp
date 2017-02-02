
<%@page import="com.agiledge.atom.billingtype.dto.BillingTypeDto"%>
<%@page import="com.agiledge.atom.billingtype.service.BillingTypeService"%>

 
<%@page import="com.agiledge.atom.dto.VendorDto"%>
<%@page import="com.agiledge.atom.service.VendorService"%>
<%@page import="com.agiledge.atom.billingtype.dto.BillingTypeDto"%>
<%@page import="com.agiledge.atom.billingtype.service.BillingTypeService"%>
<%@page import="com.agiledge.atom.service.SiteService"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>

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
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery-latest.js"></script> 
<script type="text/javascript" src="js/dateValidation.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<link rel="css/style.css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
<link href="css/second_menu.css" rel="stylesheet" type="text/css" />
 <style type="text/css">
@import "css/jquery.datepick.css";

 
</style>
 
<title>Billing Setup</title>

</head>
<body>
	<script type="text/javascript">
	 
	$(document).ready(function() {
	//	fillTransportTypes();
		$('#fromDate').datepick();
		//$("#site").change(fillTransportTypes);
		//$("#vendor").change(fillTransportTypes);
	});
	
	/* function fillTransportTypes() {

		 
		
		 
		 var lookup = {siteId:$("#site").val(), vendorId:$("#vendor").val()};
		ajaxPost("GetTransportTypes",lookup, fillTransportTypesAck); 
	}
	
 */	function fillTransportTypesAck(data) {
		 var  str = "<option value=\"\">-----Select Type-----</option>";
		 var typeVal =$("#trans_type").val();
		 if(data.result=="true") {
			  
			 var ttypes = data.collection;
			  
			 
			  var ttMap= new Object();
			 for(var i=0;i<ttypes.length; i++) {
				 
				 ttMap[ttypes[i].transportTypeId]=ttypes[i].transportType;
				 //str = str + "   " + ttMap[ttypes[i].transportTypeId];
				 
			 } 
			
			 for(key in ttMap) {
				 str= str + "<option  value=\""+key+"\" >" + ttMap[key] + "</option>";

			 }
			
			   
		 }  
		 $("#trans_type").html(str);
		 $("#trans_type").val(typeVal);
	}
	
	function setFromDate() {
		var dt= new Date();
		dt.setDate(dt.getDate() + 1);
		$("input[name=fromDate]").val(dt.getDate()+"/"+(dt.getMonth()+1)+"/" + dt.getFullYear());
	//	alert($("input[name=fromDate]").val());
	}
	
	function anyFutureDate() {
		dflag=false;
		
		$(".ffromdate-" + $("#site").val() +"-" + $("#vendor").val()  +"-"+ $("#trans_type").val() +"-"+ $("#type").val()).each(function() {
	 
			var s = $(this).val().split('-');
			var fdate = s[2] + '/' + s[1] + '/' + s[0];
			var today = new Date();
			var todayString = today.getDate()+ "/"+(today.getMonth())+"/" + today.getFullYear();
	 
			//var todayString = $("input[name=fromDate]").val();
			if(CompareTwoDatesddmmyyyy(todayString,fdate)) {
				
				dflag=true;
				
			}  
		});
		return dflag;
	}
	
	function validate(){
		
		 try {
	//		 setFromDate();
		var today = new Date();
		var todayString = today.getDate()+ "/"+(today.getMonth())+"/" + today.getFullYear();
		if($("#site").val()=="") {
		
			alert("Site not selected");
			return false;
		} else if($("#vendor").val()=="") {
			alert("Vendor not selected");
			return false;
		} else if ($("#trans_type").val()=="") {
			alert("Transport type not selected");
			return false;
		}  else if ($("#type").val()=="") {
			alert("Billing type not selected");
			return false;
		} else if($("input[name=fromDate]").val()=="") {
			alert("Effective date not selected");
			return false;
		} else if(CompareTwoDatesddmmyyyy( $("input[name=fromDate]").val(),todayString)) { 
			alert("Effective date should be any future date");
			return false;
		 } else if (anyFutureDate()) {
			 alert(" Please delete future entry");
			 return false;
		 } 
		
		else {
			return true;
		}
		 }catch(e) {
			 alert("Validation  Error " +e);
			 return false;
		 }
	}
	
	function deleteEntry(id) {
	
		/* var lookup = {id:id};
		ajaxPost("DeleteTransportTypeMapping",lookup, deleteEntryAck); */
		 
		window.location.href = "DeleteBillingTypeMapping?id=" + id;
		 
	}
	
	function deleteEntryAck(data) {
		alert(data);
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
 	 <div style="padding-top:1%; padding-left:20px;" >
 	 <h3>Billing Type</h3>
 	 <form name="TTForm" action="MapBillingType" >
 	 	<table style="width:240px; font: normal; position: relative;  ">
 	 		<thead>
	 	 		<tr>
	 	 			<th>Site *
	 	 			</th>
	 	 			<th>Vendor *
	 	 			</th>
	 	 			<th>Transport Type *
	 	 			</th>
	 	 			<th>Billing Type *
	 	 			</th>	 	 			
	 	 			<th>Effective On
	 	 			</th>
	 	 		</tr>
 	 		</thead>
 	 		<tbody>
 	 			<tr>
 	 				<td>
 	 				
 	 					<select name="site" id = "site">
 	 						<option value="">-Select Site-</option>
 	 					<%List<SiteDto> siteList = new SiteService().getSites();  
 	 						if(siteList!=null&&siteList.size()>0) {
 	 							String paramSite = request.getParameter("site");
 	 							paramSite= paramSite==null?"":paramSite;
 	 							for(SiteDto dto : siteList) {
 	 								String siteSelect="";
 	 								if(dto.getId().equals(paramSite)) {
 	 									siteSelect="selected";
 	 								}
 	 						
 	 					%>		
 	 								<Option <%=siteSelect %> value="<%=dto.getId() %>"><%=dto.getName() %></Option>
 	 					<%
 	 							}
 	 						}
 	 					%>
 	 					</select>
 	 				</td>
 	 				<td>
 	 						
 	 					<select name="vendor" id = "vendor">
 	 						<option value="">-Select Company-</option>
 	 					<%List<VendorDto> vendorList = new VendorService().getMasterVendorlist();  
 	 						if(vendorList !=null&&vendorList .size()>0) {
 	 							String paramVendor = request.getParameter("vendor");
 	 							paramVendor= paramVendor==null?"":paramVendor;
 	 							for(VendorDto dto : vendorList) {
 	 								String vendorSelect="";
 	 								if(dto.getCompanyId().equals(paramVendor)) {
 	 									vendorSelect="selected";
 	 								}
 	 								
 	 					%>		
 	 								<Option <%=vendorSelect %> value="<%=dto.getCompanyId() %>"><%=dto.getCompany() %></Option>
 	 					<%
 	 							}
 	 						}
 	 					%>
 	 					</select>
 	 				</td>
 	 				<td>
 	 						
 	 					<select   name="trans_type" id = "trans_type">
 	 						<option  value="">-----Select Type-----</option>
 	 					<%
 	 					System.out.println("........................................");
 	 					List<TransportTypeDto> ttList = new TransportTypeService().getTransportTypes();
 	 					
 	 						if(ttList!=null&&ttList.size()>0) {
 	 							String paramType = request.getParameter("trans_type");
 	 							paramType= paramType==null?"":paramType;
 	 							for(TransportTypeDto dto : ttList) {
 	 								String typeSelect="";
 	 							 	if(String.valueOf(dto.getId()).equals(  paramType )) {
 	 									typeSelect="selected";
 	 								} 
 	 					%>		
 	 								<Option <%=typeSelect %> value="<%=dto.getId() %>"><%=dto.getName() %></Option>
 	 					<%
 	 							}
 	 						}
 	 					%>
 	 					</select>
 	 					</td>
 	 				<td>
 	 						
 	 					<select   name="type" id = "type">
 	 						<option  value="">-----Select Type-----</option>
 	 					<%List<BillingTypeDto> btList = new BillingTypeService().getBillingTypes();  
 	 						if(btList!=null&&btList.size()>0) {
 	 							String paramType = request.getParameter("trans_type");
 	 							paramType= paramType==null?"":paramType;
 	 							for(BillingTypeDto dto : btList) {
 	 								String typeSelect="";
 	 							 	if(String.valueOf(dto.getId()).equals(  paramType )) {
 	 									typeSelect="selected";
 	 								} 
 	 					%>		
 	 								<Option <%=typeSelect %> value="<%=dto.getId() %>"><%=dto.getName() %></Option>
 	 					<%
 	 							}
 	 						}
 	 					%>
 	 					</select>
 	 					</td>
 	 					<td>
 	 					<input type="text" id = "fromDate" name="fromDate" value="" readonly="readonly"
 	 					class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2012,  1, 25)}" 
 	 					 />  	 				
 	 					</td>
 	 				<td>
 	 					<input type="submit" class="formbutton" onclick="return validate()" value="Submit" />
 	 					
 	 				</td>
 	 				<td><input type="button" onclick="javascript:history.go(-1)" class="formbutton"  value="Back" />
 	 				</td>
 	 			</tr>
 	 		</tbody>
 	 	</table>
 	 	</form>
 	 </div>
 
 
 
 <div>
 
 	<div style="padding:20px; width: 800px;  ">
 	
 		<% 
 		BillingTypeService service = new BillingTypeService();
 		ArrayList<BillingTypeDto> dtoList = service.getBillingTypeMappings("future");
 		for(BillingTypeDto dto : dtoList) {
 			String url = service.getCondtionUrl( Integer.parseInt( dto.getBillingTypeId())) + "?refId=" + dto.getId();
			 	dto.setConditionUrl( "<a href='" + url + "' >Show</a>");
			 
		}
 		%>
 	<h4> Future Settings</h4>	
 		<disp:dipxTable styleClass="displaytag"    id="row" list="<%=dtoList %>" style="width:100%;" >
			 
		
				<disp:dipxColumn group="1" property="site" title="Site" >
				<input type="hidden" value="${row.siteId}" />
				<input type="hidden" value="${row.id }" />
				</disp:dipxColumn>
				<disp:dipxColumn group="2" property="vendor" title="Vendor" >
				<input type="hidden" value="${row.vendorId}" />
				</disp:dipxColumn>
				 
				<disp:dipxColumn group="3" property="transportType" title="Tranport Type" >
				<input type="hidden" value="${row.transportTypeId}" />
				</disp:dipxColumn>
				<disp:dipxColumn group="4" property="billingType" title="Billing Type" >
				<input type="hidden" value="${row.billingTypeId}" />
				</disp:dipxColumn>
				<disp:dipxColumn   property="fromDate" title="Effective" format="{0,date,dd/MM/yyyy}" >
				<input type="hidden" id="fromDate${row.id}" class="ffromdate-${row.siteId}-${row.vendorId}-${row.transportTypeId}-${row.billingTypeId}" value="${row.fromDate}" />
				</disp:dipxColumn>
				<disp:dipxColumn  property="conditionUrl" title="Configuration" >
				</disp:dipxColumn>
				 <disp:dipxColumn title="">
					<input type="button" class = "formbutton"
					style="display: ${row.conditionId==-1? 'block' : 'none' };" 
					onclick="deleteEntry('${row.id}')" value="Delete" />
				</disp:dipxColumn>
			 
			</disp:dipxTable>
			
 		
 	</div>
 
	<div style="padding:20px;  width: 800px; ">
	 
 		<% 
 
 		ArrayList<BillingTypeDto> dtoActiveList = service.getBillingTypeMappings("present");
 		for(BillingTypeDto dto : dtoActiveList) {
 			String url = service.getCondtionUrl( Integer.parseInt( dto.getBillingTypeId())) + "?refId=" + dto.getId();
			 	dto.setConditionUrl( "<a href='" + url + "' >Show</a>" );
		 
		}
 		
 	 	%>
 	 	<h4> Active Settings</h4>
 		<disp:dipxTable styleClass="displaytag"    id="row" list="<%=dtoActiveList %>" style="width:100%;" >
				<disp:dipxColumn group="1" property="site" title="Site" />
				<disp:dipxColumn group="2" property="vendor" title="Vendor" />
				<disp:dipxColumn group="3" property="transportType" title="Transport Type" />
				<disp:dipxColumn group="4" property="billingType" title="Billing Type" />
				<disp:dipxColumn   property="fromDate" title="Effective" format="{0,date,dd/MM/yyyy}" />
				<disp:dipxColumn  property="conditionUrl" title="Configuration" />
		</disp:dipxTable>
			
 		
 	</div>
 
</div>

 	<div style="padding:20px; width: 800px;  display: inline-block;">
 	
 		<% 
 		 
 		ArrayList<BillingTypeDto> dtoPastList = service.getBillingTypeMappings("past");
 		for(BillingTypeDto dto : dtoPastList ) {
 			String url = service.getCondtionUrl( Integer.parseInt( dto.getBillingTypeId())) + "?refId=" + dto.getId();
			 
				
				dto.setConditionUrl( "<a href='" + url + "' >Show</a>");
	  }
 		 %>
 		 <h4> Past Settings</h4>
 		
 		<disp:dipxTable styleClass="displaytag"    id="row" list="<%=dtoPastList %>" style="width:100%;" >
				<disp:dipxColumn group="1" property="site" title="Site" />
				<disp:dipxColumn group="2" property="vendor" title="Vendor" />
				<disp:dipxColumn group="3" property="transportType" title="Transport Type" />
				<disp:dipxColumn group="4" property="billingType" title="Billing Type" />
				<disp:dipxColumn   property="fromDate" title="Effective" format="{0,date,dd/MM/yyyy}" />
				<disp:dipxColumn  property="conditionUrl" title="Configuration" />
		</disp:dipxTable>
			
 		
 	</div>
 


 			</div>
  			<div style="position: relative;">
			<%@include file="Footer.jsp"%>
			</div>
		</div>
	 


</body>
</html>
