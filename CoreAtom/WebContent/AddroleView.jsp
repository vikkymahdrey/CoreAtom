<%@page import="com.agiledge.atom.usermanagement.service.ViewManagementService"%>
<%@page import="com.agiledge.atom.usermanagement.dto.ViewManagementDto"%>
<%@ page import="com.agiledge.atom.dao.OtherDao" %>
<%@ page import="com.agiledge.atom.dto.UserManagementDTO" %>
<%@ page import="java.util.ArrayList" %>
 
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<title>Add View</title>
<script type="text/javascript">
$(document).ready(function() {
		$("#views").keydown(function(e){
			 
			if(e.keyCode==39) {
				listMoveRight();
			}
		});
		
		$("#choosenviews").keydown(function(e){
			 //evt.which == 17 : ctrl button  pressed
			// alert(e.keyCode);
			if(e.keyCode==37) {
				listMoveLeft();
			}
			 
		});
		 
});

function choosenviewsKeyUp() {
	
	try { 
	
	var selectedIndex = $("#choosenviews option:selected").index();
	if(selectedIndex >0) {
		//alert($($("#choosenviews option")[selectedIndex]).val() );
		$("#choosenviews option:selected").insertBefore($("#choosenviews option:selected").prev());
		//alert($("#choosenviews option").length);
	}
	}catch(e) {
		;	
	}
}


function choosenviewsKeyDown() {
	try { 
		
		var selectedIndex = $("#choosenviews option:selected").index();
		if(selectedIndex >0) {
			//alert($($("#choosenviews option")[selectedIndex]).val() );
			$("#choosenviews option:selected").insertBefore($("#choosenviews option:selected").next().next());
			//alert($("#choosenviews option").length);
		}
		}catch(e) {
			;	
		} 
	 
	}

function listMoveRight() {
 
	try { 
		
	$("#choosenviews").append($("#views option:selected"));
	 
	}catch(e) {
		alert(e);
	}
	
}
function listMoveLeft() {
 
	try { 
		
		$("#views").append($("#choosenviews option:selected"));
		 
		}catch(e) {
			alert(e);
		}
		
 }
	function submitForm() {
		var views = document.getElementById("choosenviews");
		var viewsLength = document.getElementById("choosenviews").options.length;
		for ( var i = 0; i < viewsLength; i++) {
			views.options[i].selected = true;
		}
		document.AddViewRole.submit();
	}

</script>
</head>
<body>
<%
          long empid=0;
        String employeeId = OtherFunctions.checkUser(session);
        if (employeeId == null||employeeId.equals("null") ) {
            String param = request.getServletPath().substring(1) + "___"+ request.getQueryString(); 	response.sendRedirect("index.jsp?page=" + param);
        } else {
            empid = Long.parseLong(employeeId);
            %>
			<%@include file="Header.jsp"%>
			<%
        }
        OtherDao ob = null;
        ob = OtherDao.getInstance();
    %>
<div id="body">
		<div class="content">
		<% 
		int roleId=Integer.parseInt(request.getParameter("roleId"));
		String roleName=request.getParameter("roleName");
		
		%>
	<h3 align="center">&nbsp;&nbsp;&nbsp;&nbsp;Add Views To Role</h3>
			<p></p>
			<div></div>
			<form name="AddViewRole" action="AddViewRole" method="Post">
			<input type="hidden" id="roleId" name="roleId" value="<%=roleId %>">
			<input type="hidden" id="roleName" name="roleName" value="<%=roleName %>">
			<table  border="0">
			<tr><td></td><td></td>
						<td width="10%;"><h4 align="left">Views Available</h4>
						<select name="views" id="views" multiple="multiple" size="10" >
						<%
						ArrayList<ViewManagementDto> viewList= new ViewManagementService().roleViewExisting(roleId);
						if(viewList!=null && viewList.size()>0) {
							for(ViewManagementDto dto: viewList)
			                {
							%>
								<option value="<%=dto.getViewId()%>" ><%=dto.getViewKey() %></option>
							<%
			                }
						}
						%>
						</select></td>
						<td width="10%;" align="center"><p><input type="button" class="formbutton" name="right"
										value="&rArr;" onclick="listMoveRight()" /></p>
										<p><input type="button" class="formbutton" name="left"
										value="&lArr;" onclick="listMoveLeft()" /></p>
						</td>
						<td width="10%;" ><h4 align="left">Currently Added Views</h4>
						<select name="choosenviews" id="choosenviews" size="10" multiple>
								<%
						ArrayList<ViewManagementDto> activeList= new ViewManagementService().getViewsbyRole(roleId);
						if(viewList!=null && viewList.size()>0) {
							for(ViewManagementDto dto: activeList)
			                {
							%>
								<option value="<%=dto.getViewId()%>" ><%=dto.getViewKey() %></option>
							<%
			                }
						}
						%>
						</select>
							</td>
							<td  >
									<div style=" float:left;" ><input type="button" value="Up" onclick="choosenviewsKeyUp()" /><br/>
										<input type="button" value="Down" onclick="choosenviewsKeyDown()" />
									</div>
							</td>
							</tr>
						
							<tr>
							<tr><td></td><td></td><td></td><td></td>
							<td><input type="button" class="formbutton" value="Update" onclick="submitForm()" /></td>
							<td><a href="AllView.jsp">Add New View</a></td>
							</tr>
							</table>
					</tr>
			</table>
			</form>
		
</body>
</html>