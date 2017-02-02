<%-- 
    Document   : SupervisorSearch
    Created on : Oct 17, 2012, 2:28:15 PM
    Author     : 123
--%>
<%@page import="com.agiledge.atom.usermanagement.dto.ViewManagementDto"%>
<%@page import="com.agiledge.atom.usermanagement.service.ViewManagementService"%>
<%@page import="com.agiledge.atom.usermanagement.service.PageService"%>

 <%@page import="com.itextpdf.text.log.SysoLogger"%>
 <%@page import="java.io.File"%>
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
		$("#pageSubmit").click(pageSubmit);
		$("#servletSubmit").click(servletSubmit);
		
		$("#pageClear").click(function () {
			
			 
			$("#pageSubmit").val("Submit");
			$("#page").val("");
			$("#pageId").val("");
			
		});
		$("#servletClear").click(function () {
			$("#servletSubmit").val("Submit");
			$("#servlet").val("");
			$("#servletId").val("");
		});
		$("#pageEditButton").click(function() {
			try { 
				
			if($("#pageDrp option:selected").toArray().length>1) {
				alert("Please select only one item.");
			} else {
				
				 
			$("#pageSubmit").val("Update");
			$("#pageId").val($("#pageDrp option:selected").val());
			$("#page").val($("#pageDrp option:selected").text());
			}
			}catch (e) {
				// TODO: handle exception
				alert(e);
			}
		});
		
		$("#servletEditButton").click(function() {
			try { 
				
			if($("#servletDrp option:selected").toArray().length>1) {
				alert("Please select only one item.");
			} else {
				
				 
			$("#servletSubmit").val("Update");
			$("#servletId").val($("#servletDrp option:selected").val());
			$("#servlet").val($("#servletDrp option:selected").text());
			}
			}catch (e) {
				// TODO: handle exception
				alert(e);
			}
		});

	});

	function pageSubmit( ) {
		$("#pageLoadingImage").show();
		var lookup ={page:$("#page").val(), pageType:"PAGE"};
		var urlParam = "AddPage";
		if($("#pageSubmit").val()=="Update") {
			urlParam = "EditPage";
			lookup = {page:$("#page").val(), pageType:"PAGE", id:$("#pageId").val()};
			 $.ajax({
				 method : "POST",
				 url : urlParam,
				 data : lookup,
			 	 dataType: "json",
			 	 success: function (data) {
			 		$("#pageLoadingImage").hide();
			 		 alert(data.message
			 				 );
			 		 if(data.result=="true") { 
			 	 
			 		 $("#pageDrp option[value="+data.id+"]").text($("#page").val());
			 		 }
			 	 }
				 
				 
			 });
		
		} else {
		 
		 $.ajax({
			 method : "POST",
			 url : urlParam,
			 data : lookup,
		 	 dataType: "json",
		 	 success: function (data) {
		 		$("#pageLoadingImage").hide();
		 		 alert(data.message
		 				 );
		 		 if(data.result=="true") { 
		 		 	$("#pageDrp").append("<Option selected value="+data.id+" >"+$("#page").val()+"</Option>");
		 		 }
		 	 }
			 
			 
		 });
		}
	}
	
	function servletSubmit( ) {
		$("#servletLoadingImage").show();
		var lookup ={page:$("#servlet").val(), pageType:"SERVLET"}
		var urlParam = "AddPage";
		if($("#servletSubmit").val()=="Update") {
			urlParam = "EditPage";
			lookup = {page:$("#servlet").val(), pageType:"SERVLET", id:$("#servletId").val()};
		
		 $.ajax({
			 method : "POST",
			 url : urlParam,
			 data : lookup,
		 	 dataType: "json",
		 	 success: function (data) {
		 		$("#servletLoadingImage").hide();
		 		 alert(data.message
		 				 );
		 		 if(data.result=="true") { 
		 			$("#servletDrp option[value="+data.id+"]").text($("#servlet").val());
		 		 }
		 		 
		 	 }
			 
			 
		 });
		} else {
			 $.ajax({
				 method : "POST",
				 url : urlParam,
				 data : lookup,
			 	 dataType: "json",
			 	 success: function (data) {
			 		$("#servletLoadingImage").hide();
			 		 alert(data.message
			 				 );
			 		$("#servletDrp").append("<Option selected value="+data.id+" >"+$("#servlet").val()+"</Option>");
			 	 }
				 
				 
			 });
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
					
   				
   				new PageService().loadPagesToDb(application);
			%>


			<h3>Url (Pages & Servlets) Setup </h3>
			 
			 
			<form name="UpdateEmployeesRoleForm" action="#">
			<div style="width:50%; display: inline;">
				<div id="pageDiv"  style="position: absolute; margin-left: 2%; display:inline;">
					 
					
				<div  style="position: relative; position: (0,0);">
					Pages <input type = "text" name = "page" id = "page" />
					<input type = "hidden" name = "pageId" id = "pageId" />
					<input type = "button" id =  "pageSubmit" value="Submit"  />
					<input type = "button" id =  "pageClear" value="Clear"  />
					<img alt="sd" src="images/ajax-loader.gif" style="display:none;" id="pageLoadingImage" />
					<br/>
				<div id ="servletLabel" style="margin-left: 18%;">EROROROORiiiiiiiiiiiiiiiO</div>
				
				</div>
							<div style = "margin-left: 43px;padding-top:10px; " >
						<input type="button" id="pageEditButton" class="formbutton" value="Edit" />
						 
						<input type="button" id="pageSelectButton" class="formbutton" value="Assign" />
						
						<input type="button" id="pageDeleteButton" class="formbutton" value="Select" />
						<br/> 
					</div>
		
				<div style = "padding-top:10px; margin-left: 43px;" >
				<%
					ViewManagementService  service = new ViewManagementService();
					ArrayList<ViewManagementDto> pageList = service.getPagesOnly();
					 
					ArrayList<ViewManagementDto> servletList = service.getServletsOnly();
					
					
				%>
					<select multiple="multiple" name="pageDrp" id="pageDrp"   >
						<% if(pageList!=null&&pageList.size()>0) {
								for(ViewManagementDto dto : pageList) {
									
								
							%>
								<option value="<%=dto.getViewUrlId()%>"><%=dto.getViewURL()%></option>
						<%
								}
						}%> 
					</select>
				</div>
				</div>
				
				
				<div id="servletDiv"  style="position: relative; margin-left: 50%; display:inline-block; ">
				Servlet <input type = "text" name = "servlet" id = "servlet" />
				<input type = "hidden" name = "servletId" id="servletId" />
				<input type = "button" id =  "servletSubmit" value="Submit"  />
				<input type = "button" id =  "servletClear" value="Clear"  />
				<img alt="sd" src="images/ajax-loader.gif" style="display:none;" id="servletLoadingImage" />
				<div id ="servletLabel" style="margin-left: 20%;">EROROROORO</div>
					<div style = "margin-left: 20%;padding-top:10px; " >
						<input type="button" id="servletEditButton" class="formbutton" value="Edit" />
						 
						<input type="button" id="servletSelectButton" class="formbutton" value="Assign" />
						
						<input type="button" id="servletDeleteButton" class="formbutton" value="Select" />
						<br/> 
					</div>
				<div style = "padding-top:20px; margin-left: 43px;"   >
				
					<select multiple="multiple" name="servletDrp" id="servletDrp"   >
						 <% if(servletList!=null&&servletList.size()>0) {
								for(ViewManagementDto dto : servletList) {
									
								
							%>
								<option value="<%=dto.getViewUrlId()%>"><%=dto.getViewURL()%></option>
						<%
								}
						}%>
					</select>
				</div>
				</div>
		</div>
			</form>
			
			<div id="assignDiv" style="padding: 5%;">
				  
			</div>
			<%@include file='Footer.jsp'%>
		</div>
	</div>
</body>
</html>




