<%-- 
    Document   : vehicle_type
    Created on : Oct 19, 2012, 11:09:44 AM
    Author     : muhammad
--%>
 
<%@page import="com.agiledge.atom.usermanagement.dto.ViewManagementDto"%>
<%@page import="com.agiledge.atom.usermanagement.service.ViewManagementService"%>
<%@page import="com.agiledge.atom.usermanagement.service.UserManagementService"%>
<%@page import="com.agiledge.atom.usermanagement.dto.UserManagementDto"%>
<%@page import="java.io.File"%>
 
 
<%@page import="com.itextpdf.text.log.SysoLogger"%>
 
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.nfl.com" prefix="disp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Add Vehicle</title>
<% request.setAttribute("contextPath", request.getContextPath()); %>
<link rel="stylesheet" type="text/css" href="${contextPath}/css/displaytag.css" />
<script type="text/javascript" src="js/dispx.js"></script>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="js/validate.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<script src="js/JavaScriptUtil.js"></script>

<script type="text/javascript">
 	$(document).ready(readyFunction);
 	function readyFunction() {
 		$("#pageDiv").hide();
 		$("#userType").keyup(function() {
 			checkUserTypeExists('userType');
 			
 		});
 		
 		$("#userType").keyup(function() {
 			checkUserTypeExists('userType');
 			
 		});
 		
 		$("#setHomePage").click(setHomePage);
 		$("#cancelSetHomePage").click(clearSetHomePage);
 	
 	}
 	
 	function showPageDiv(role) {
 		$("#selectedRole").val(role);
 		$("#pageDiv").show();
 		
 		 
 	}
 	
 	function clearSetHomePage() {
 		$("#pageDrp").val("");
 		$("#selectedRole").val("");
 		$("#pageDiv").hide();
 		
 	}
 	
 	/*
 	 function to set home page for roles (users)
 	*/
 	function setHomePage() {
 		 try {
 		 homePageId = $("#pageDrp").val();
 		 if(  homePageId=="") {
 			 alert('Home page is not set');
 		 } else {
 			var role = $("#selectedRole").val();
 			var lookup = {userType:role, urlId:homePageId};
 			 
 			ajaxPost("SetRolesHomePage",lookup, setHomePageAck);
 		 }
 		 }catch (e) {

			alert(e);
		}
 	 }
 	
 	function setHomePageAck(data) {
 		alert(data.message);
 		clearSetHomePage();
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
		
	
 	/*
 	* function to show views associated with role
 	*/
 	function showRoleView(roleName,roleId ) {
 		 
 		self.location = "roleView.jsp?roleId=" + encodeURIComponent(roleId) + "&roleName=" + encodeURIComponent(roleName);
 		 
 	}
	function validate() {
		var name = $("input[id=name]").val();
//		var escortId = $("input[id=escortId]").val();
		var escortClock = $("input[id=escortClock]").val();
//		var address = $("input[id=address]").val();
		var site = $("input[id=site]").val();
		var phone = $("input[id=phone]").val();
		 
		 
		
		 
		try {
			if (name == "") {
				alert("Name should not be blank !");
				return false;
			}
			if (escortClock == "") {
				alert("Escort Id should not be blank !");
				return false;
			} 
	
			if (phone == "") {
				alert("Contact No  should not be blank !");
				return false;
			}  
			

			if (site== "") {
				alert("Site should not be blank !");
				return false;
			}  
			
			  
			else {
				return true;
				 
			}
			return false;
		} catch (e) {
			alert(e)
			return false;
		}
	}
	function keyPressed() { alert('key pressed')}
	function checkUserNameExists(source ) {
		// alert('');
		
		var lookup =  { name: $("#name").val(), userType: $("#userType").val()};
		try { 
		/* var jqxhr = $.ajax({
			method:"POST",
			url:"CheckExists", 
			data:  lookup,
			success: function(data) {
		
				if(data.result=="true") {
					alert(data.message);
				}
			},
			dataType: "json"
	
		}
		
		); */
		$.post("CheckUserNameExists",lookup,function(data,status) {
			var jsonData= JSON.parse(data)
			//alert(jsonData.result);
			if(jsonData.result=="true") {
				alert(jsonData.message);
			}
		}
				
		);
		}catch(e) {
			alert (e);
			
		}
	}
	
	function checkUserTypeExists(source ) {
		// alert('');
		
		var lookup =  { name: $("#name").val(), userType: $("#userType").val()};
		try { 
		/* var jqxhr = $.ajax({
			method:"POST",
			url:"CheckExists", 
			data:  lookup,
			success: function(data) {
		
				if(data.result=="true") {
					alert(data.message);
				}
			},
			dataType: "json"
	
		}
		
		); */
		$.post("CheckUserTypeExists",lookup,function(data,status) {
			var jsonData= JSON.parse(data)
			//alert(jsonData.result);
			if(jsonData.result=="true") {
				alert(jsonData.message);
			}
		}
				
		);
		}catch(e) {
			alert (e);
			
		}
	}
	
	
	function fillFields(id) {
		 
		
		$("#name").val($("#name-"+id).val());
		
		$("#description").val($("#description-"+id).val());
		$("#type").val($("#type-"+id).val());
		$("#id").val($("#id-"+id).val());
		$("#userType").val($("#userType-"+id).val());
		$("#userType").attr("readonly","readonly");
		 
		$("form[name='UserRoleSetUpForm']").attr("action","UpdateUserRole");
		$("input[name='submitbtn']").val("Update");
	}
	

	function clearFields() {
		 

		$("#name").val("");
		
		$("#description").val("");
		$("#type").val("");
		$("#id").val("");
		$("#userType").removeAttr("readonly");
		$("#userType").val("");
		$("form[name='UserRoleSetUpForm']").attr("action","AddUserRole");
		$("input[name='submitbtn']").val("Submit");
	}
	
	
	
</script>
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
	%><%@include file="Header.jsp"%>
	<%
		}
		ArrayList<UserManagementDto> dtos= new UserManagementService().getSystemUsers();
		System.out.println(" APP " + application.getRealPath("/"));
			%>

 


	<div id="body">
		<div class="content">


			<h3>User Role Setup</h3>
			<hr />
			<form name="UserRoleSetUpForm" action="AddUserRole" method="post"
				onsubmit="return validate()">

				<table width="70%">
					<tr>
						<td align="center">Name</td>
						<td><input type="text" name="name" id="name" onchange="checkExists('name')" />
						<input type="hidden" name="id" id="id" />
						<input type="hidden" name="type" id="type" />
						
						
						</td>
					</tr>
					<tr>
						<td align="center">Description</td>
						<td><input type="text" name="description" id="description" />
						
						
						</td>
						 
		
					</tr>
					<tr>
						<td align="center">Key</td>
						<td><input type="text" name="userType" onchange="checkExists('usertype')" id="userType" />
						
						
						</td>
						 
		
					</tr>
					
					 
		 			 		<tr>
						<td></td>
						<td><input type="submit" class="formbutton" name="submitbtn"
							value="Submit" /> <input type="button" class="formbutton"
							onclick="javascript:history.go(-1);" value="Back" />
							<input type="button" class="formbutton"
							onclick="clearFields()" value="Clear" />
							</td>
					</tr>
				</table>
							</form>
			<p>Driver</p>
			<div style = "display: inline-block; width: 60%;" > 
			<disp:dipxTable  id="row" list="<%=dtos %>" style="width:100%;" >
			 
				<disp:dipxColumn  title="Name" sortable="true" property="name" >
			 
				<input type="hidden" id="id-${rowCount+1}" value="${row.id }"/>
				<input type="hidden" id="name-${rowCount+1}" value="${row.name}"/>
				<input type="hidden" id="description-${rowCount+1}" value="${row.description}"/>
				<input type="hidden" id="userType-${rowCount+1}" value="${row.userType }"/>
				<input type="hidden" id="type-${rowCount+1}" value="${row.type }"/>
				<input type="hidden" id="userType-${rowCount+1}" value="${row.userType }"/>
				 
				
				 
				 
				</disp:dipxColumn>
				 
				<disp:dipxColumn  title="Name" sortable="true" property="name" ></disp:dipxColumn>
				<disp:dipxColumn  title="Description" sortable="true" property="description" ></disp:dipxColumn>
				<disp:dipxColumn  title="Type" sortable="true" property="type" ></disp:dipxColumn>
				<disp:dipxColumn  title="Action"   ><a onclick="fillFields('${rowCount+1}')">Edit</a>|
				<a href="#"   onClick="showRoleView('${row.name}', '${row.id}')" >Views &gt;&gt; </a>|
				<a href="#"   onClick="showPageDiv('${row.userType}' )" >Assign Home Page&gt;&gt; </a>	
				</disp:dipxColumn>
			</disp:dipxTable>
			</div>
			<div id = "pageDiv"  style = " display: fixed; float: right; padding: 10px; height: 20px;">
					
					 
				<%
					ViewManagementService   service = new ViewManagementService();
					ArrayList<ViewManagementDto> pageList = service.getAllPages();
 
					
					
				%>
					<select   name="pageDrp" id="pageDrp"   >
						<option value="">Select Page </option>
						<% if(pageList!=null&&pageList.size()>0) {
								for(ViewManagementDto dto : pageList) {
									
								
							%>
								<option value="<%=dto.getViewUrlId()%>"><%=dto.getViewURL()%></option>
						<%
								}
						}%> 
					</select>
					<br/>
					<input id="selectedRole" type="hidden" value=""; />
					<input type="button" id= "setHomePage" value="Set Home Page" />
					<input type="button" id = "cancelSetHomePage" value = "Cancel" />
					<br/>
					<br/>
					<br/>
					<br/>
					<br/>
					<br/>
					<br/>
					<br/>
					<br/>
					<br/>
					<br/>
					<br/>
					
				</div>
			
		 
			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>

</html>
