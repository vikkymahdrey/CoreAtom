<%-- 
    Document   : SupervisorSearch
    Created on : Oct 17, 2012, 2:28:15 PM
    Author     : 123
--%>
 
<%@page import="com.agiledge.atom.usermanagement.dto.ViewManagementDto"%>
<%@page import="com.agiledge.atom.usermanagement.service.ViewManagementService"%>
<%@page import="java.util.ArrayList"%>
 
  
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.9.2/jquery-ui.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Change Role</title>
 
 	<script type="text/javascript">
 		$(document).ready(function() {
 			fillAssignedPageDrp();
 				$("#moveRight").click(moveRight);
 				$("#pageDrp").keydown(function(e) {
 					if(e.keyCode==39) {
 						moveRight();
 					}
 				});
 				$("#delete").click(deleteAssignedItem);
 				 
 				$("#mainPageDrp").change(fillAssignedPageDrp);
 				$("#submitButton").click(submitGroupingAssignement);
 		
 		});
 		
 		function submitGroupingAssignement() {
 		 
 		  var array= new Array();
 		  var i = 0;
 		  $("#assignedPageDrp option").each(function () {
 			  array.push( $(this).val());
 			   
 			  i++;
 		  });
 		  var data = JSON.stringify(array);
 		   
 		  var lookup = {mainPageDrp:$("#mainPageDrp").val(), assignedPageDrp:data};
 		  ajaxPost("GroupUrls", lookup, assignSuccess);
 		}
 		
 		function assignSuccess(data) {
 			 
 			alert( data.message);
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
 		
 		function deleteAssignedItem() {
 			$("#assignedPageDrp option:selected").remove();
 		}
 		
 		function moveRight() {
 			 
 			//alert($("#pageDrp option:selected").clone());
 	 	$("#assignedPageDrp").append( $("#pageDrp option:selected").clone());
 	 	$("#assignedPageDrp option").each(function(){
 	 		 
 	 		try {  
 	 		if($("#assignedPageDrp option[value=" + $(this).val() + "]").toArray().length>1) {
 	 			$(this).remove();
 	 		}
 	 		}catch(e) {
 	 			
 	 			alert(e);
 	 		
 	 		}
 	 	});
 	 	 
 		}
 		
 		function fillAssignedPageDrp() {
 		 //alert('');
 			var lookup = {id:$("#mainPageDrp").val()};
 			var urlParam = "GetAssignedUrls";
 			ajaxPost(urlParam, lookup, fillItemsToAssigned);
 			/* 
 			$.ajax(
 				{
 					method:"POST",
 					url: urlParam,
 					data:lookup,
 					dataType:"json",
 					success: function (data) {
 						var item = data.item;
						$("#assignedPageDrp option").remove();
						for(var i=0; i < item.length; i++) {
							$("#assignedPageDrp").append("<option value='" + item[i].id + "'>" + item[i].url+ "</option>");
						}
			
 					}
 				}		
 			); */
 		}
 		
 		function fillItemsToAssigned(data) {
 			 
 		 if(data.result=="true") { 
			var item = data.item;
			//alert(data.item);
			$("#assignedPageDrp option").remove();
			for(var i=0; i < item.length; i++) {
				$("#assignedPageDrp").append("<option value='" + item[i].id + "'>" + item[i].url+ "</option>");
			}
 		 } else {
 			 $("#assignedPageDrp option").remove();
 		 }
 		}
 		
 	</script>
	 </head>
<body>
	<%@include file='Header.jsp'%>
	<div id="body">
		<div class="content">
			<%
				long empid = 0;
				String employeeId = OtherFunctions.checkUser(session);
					empid = Long.parseLong(employeeId);
			%>


			<h3>Url (Pages & Servlets) Grouping </h3>
			 
			 
			<form name="GroupUrlForm" action="GroupUrl">
			 		 
					
			 	<div style="padding-left: 50px; display: inline-block;  " >
			 		<div>
			 		 URLS
			 		</div>
			 		<br/>
			 		<div> 
			 		<div style = "display:inline-block; text-align: center;">
			 							   
					 
				<%
					ViewManagementService   service = new ViewManagementService();
					ArrayList<ViewManagementDto> pageList = service.getAllPages();
 
					
					
				%>
					<select multiple="multiple"  size="10"  name="pageDrp" id="pageDrp"   >
						<% if(pageList!=null&&pageList.size()>0) {
								for(ViewManagementDto dto : pageList) {
									
								
							%>
								<option value="<%=dto.getViewUrlId()%>"><%=dto.getViewURL()%></option>
						<%
								}
						}%> 
					</select>
					
			 		</div>
			 		<div style = "display:inline-block; position:relative; text-align: center; ">
			 				<input type="button" value=">>" name="moveRight" id="moveRight" />
			 				<div style="padding-bottom:15px;" >
			 				 <br/>
			 			
			 				</div>
			 		</div>
			 		</div>
			 		
			 	
			 	</div>					 
		
		<!--   next div -->
		
					
			 	<div style="padding-left: 50px; display: inline-block; border-color: gray;   " >
			 		<div>
			 		 	<select  name="mainPageDrp" id="mainPageDrp"   >
						<% 
						pageList = service.getViewPages();
						if(pageList!=null&&pageList.size()>0) {
								for(ViewManagementDto dto : pageList) {
									
								
							%>
								<option value="<%=dto.getViewUrlId()%>"><%=dto.getViewURL()%></option>
						<%
								}
						}%> 
					</select>
		
			 		</div>
			 		<br/>
			 		<div>
			 					<select multiple="multiple" size="10"   name="assignedPageDrp" id="assignedPageDrp" >
			 							 
			 					</select>
			 					<input type="button" value="Delete" id="delete" />		   
					 
				 							
			 		</div>
			 		
			 	
			 	</div>					 
		
				<br/>
				<div>
					<br/>
					<input type="button" id="submitButton" value="Submit" /> 
				</div>		
		
				
				 
			 
			</form>
			
			<div id="assignDiv" style="padding: 5%;">
				  
			</div>
			<%@include file='Footer.jsp'%>
		</div>
	</div>
</body>
</html>




