<%@page import="com.agiledge.atom.usermanagement.dto.ViewManagementDto"%>
<%@page import="java.util.ArrayList"%>
<%@page
	import="com.agiledge.atom.usermanagement.service.ViewManagementService"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Edit Subview</title>

<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		if ($("#closeFlag").val() == "true") {
			window.close();
			opener.location.href = "AllView.jsp";
		}
	});
	function ajaxPost(urlParam, lookup, retFunction) {
		//alert(urlParam   );
		$.ajax({
			method : "POST",
			url : urlParam,
			data : lookup,
			dataType : "json",
			success : function(data) {

				retFunction(data);
			}
		});
	}

	function validation() {
		var parentid = document.getElementById("parentId").value;
		var subviewname = document.getElementById("subviewName").value;
		var subviewKey = document.getElementById("subviewKey").value;
		var url = document.getElementById("url").value;
		var showorder = document.getElementById("showorder").value;
		document.getElementById("pidvalid").innerHTML = "";
		document.getElementById("svnamevalid").innerHTML = "";
		document.getElementById("urlvalid").innerHTML = "";
		document.getElementById("showordervalid").innerHTML = "";
		if (parentid.length < 1) {
			document.getElementById("pidvalid").innerHTML = "ParentId is blank!";
			return false
		}

		if (subviewKey.length < 1) {
			document.getElementById("svkeyvalid").innerHTML = "Identifying name is blank !";
			return false
		}
		if (subviewname.length < 1) {
			document.getElementById("svnamevalid").innerHTML = "Subview Name is blank !";
			return false
		}
		if (url.length < 1) {
			document.getElementById("urlvalid").innerHTML = "URL is blank!";
			return false
		}
		if (showorder.length < 1) {
			document.getElementById("showordervalid").innerHTML = "Show Order is blank!";
			return false
		}
		//		window.close();
		//	opener.location.reload();
		var lookup = {
			subviewName : subviewname,
			subviewKey : subviewKey,
			url : url,
			subviewId : subviewid,
			showorder : showorder,
			parentId : parentid
		};
		ajaxPost("EditSubView", lookup, closeWindow);
		//window.close();
		//opener.location.href="AllView.jsp";

	}

	function closeWindow(data) {
		//alert('lddl');
		window.close();
		opener.location.href = "AllView.jsp";
	}
</script>
</head>
<body>
	<h3 align="center">Edit Subview Information</h3>
	<%
		int subviewId = Integer.parseInt(request.getParameter("subviewId"));
		int parentId = Integer.parseInt(request.getParameter("parentId"));
		String subviewName = request.getParameter("subviewName");
		String subviewKey = request.getParameter("subviewKey");
		String url = request.getParameter("url");
		System.out.println("URL : " + url);
		int showorder = Integer.parseInt(request.getParameter("showorder"));
	%>
	<form name="EditSubview" action="EditSubview" method="post"
		onsubmit="return validation()">
		<table align="center">
			<tr>
				<td>Subview Id</td>
				<td><input type="text" value="<%=subviewId%>" name="subviewId"
					id="subviewId" readonly /> <input type="hidden" id="closeFlag"
					value="${closeFlag}" /></td>
			</tr>
			<tr>
				<td>Parent Id</td>
				<td><input type="text" value="<%=parentId%>" name="parentId"
					id="parentId" /> <label id="pidvalid" style="color: red;"></label></td>
			</tr>
			<tr>
				<td>Subview Name</td>
				<td><input type="text" value="<%=subviewName%>"
					name="subviewName" id="subviewName" /> <label id="svnamevalid"
					style="color: red;"></label></td>
			</tr>
			<tr>
				<td>Identifying Name</td>
				<td><input type="text" value="<%=subviewKey%>"
					name="subviewKey" id="subviewKey" /> <label id="svkeyvalid"
					style="color: red;"></label></td>
			</tr>
			<tr>
				<td>URL</td>
				<td><select id="url" name="url">
						<option value="">SELECT URL</option>
						<%
							ViewManagementService service = new ViewManagementService();
							ArrayList<ViewManagementDto> dtoList = service.getPagesOnly();
							url = url == null ? "" : url;

							try {
								for (ViewManagementDto dto : dtoList) {
									String urlSelect = "";
									if (dto.getViewUrlId().equals(url)) {
										urlSelect = "selected";
									}
						%>
						<option value="<%=dto.getViewUrlId()%>" <%=urlSelect%>><%=dto.getViewURL()%></option>
						<%
							}
							} catch (Exception e) {
								System.out.println("Error : " + e);
							}
						%>
				</select> <label id="urlvalid" style="color: red;"></label></td>
			</tr>
			<tr>
				<td>Show Order</td>
				<td><input type="text" value="<%=showorder%>" name="showorder"
					id="showorder" /> <label id="showordervalid" style="color: red;"></label></td>
			</tr>
		</table>
		<table>
			<td align="center"><input type="submit" class="formbutton"
				value="Update" /></td>
		</table>
	</form>
</body>
</html>