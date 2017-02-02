<%@page import="com.agiledge.atom.usermanagement.dto.ViewManagementDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.usermanagement.service.ViewManagementService"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Edit View</title>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	if ($("#closeFlag").val() == "true") {
		window.close();
		opener.location.href = "AllView.jsp";
	}
});
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

	function validation() {
		var subviewname = document.getElementById("viewName").value;
		var viewKey = document.getElementById("viewKey").value;
		var url=document.getElementById("url").value;
		var viewid=  document.getElementById("viewId").value;
		var showorder=document.getElementById("showorder").value;
		document.getElementById("vnamevalid").innerHTML = "";
		document.getElementById("urlvalid").innerHTML="";
		document.getElementById("showordervalid").innerHTML="";
		if (subviewname.length < 1) {
			document.getElementById("vnamevalid").innerHTML = "View Name is blank !";
			return false;
		}
		if (viewKey.length < 1) {
			document.getElementById("viewKeyValid").innerHTML = "Identifying Name is blank !";
			return false;
		}
		if (url.length < 1) {
			document.getElementById("urlvalid").innerHTML = "URL is blank!";
			return false
		}
		if (showorder.length < 1) {
			document.getElementById("showordervalid").innerHTML = "Show Order is blank!";
			return false
		}
		
		 var lookup = {viewName: subviewname, viewKey:viewKey, url:url,viewId:viewid, showorder:showorder };
			ajaxPost("EditView",lookup, closeWindow);
		//window.close();
		//opener.location.href="AllView.jsp";
					
	}
	
	function closeWindow(data) {
		//alert('lddl');
		window.close();
		opener.location.href="AllView.jsp";
	}
	 
</script>
</head>
<body>
<h3 align="center">Edit View Information</h3>
<%
int viewId=Integer.parseInt(request.getParameter("viewId"));
String viewName=request.getParameter("viewName");
String viewKey=request.getParameter("viewKey");
String url=request.getParameter("url");
int showorder=Integer.parseInt(request.getParameter("showorder"));
%>
<form name="EditView"  >
<table align="center">
<tr><td>View Id</td>
<td><input type="text" value="<%=viewId %>" name="viewId" id="viewId" readonly/></td></tr>
<tr><td>View Name</td>
<td><input type="text" value="<%=viewName %>" name="viewName" id="viewName"/>
<label id="vnamevalid" style="color: red;"></label></td></tr>
<tr><td>Identifying Name</td>
<td><input type="text" value="<%=viewKey %>" name="viewKey" id="viewKey"/>
<label id="viewKeyValid" style="color: red;"></label></td></tr>
<tr><td>URL</td>
<td>

 
<select id="url" name="url">
    		<option value="" >SELECT URL</option>
    		<%
    		ViewManagementService service = new ViewManagementService();
            ArrayList<ViewManagementDto> dtoList = service.getPagesOnly();
            url=url==null?"":url;
            
            try {
    		for(ViewManagementDto dto : dtoList) {
    			String urlSelect="";
    			if(dto.getViewUrlId().equals(url)) {
    				urlSelect = "selected";
    			}
    		%>
    		<option value="<%=dto.getViewUrlId() %>" <%=urlSelect %> ><%=dto.getViewURL() %></option>
    		<%} 
            } catch( Exception e) {
            	System.out.println("Error : " + e );
            }
            %>
    	</select>
<label id="urlvalid" style="color: red;"></label></td></tr>
<tr><td>Show Order</td>
<td><input type="text" value="<%=showorder %>" name="showorder" id="showorder"/>
<input type="hidden" id="closeFlag" value="${closeFlag}" />
<label id="showordervalid" style="color: red;"></label></td></tr>
</table>
<table>
<tr>
<td align="center"><input type="button" class="formbutton" value="Update" onclick="validation()"/></td>
</tr>
</table>
</form>
</body>
</html>