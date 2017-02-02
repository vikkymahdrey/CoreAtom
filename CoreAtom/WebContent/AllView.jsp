<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.agiledge.atom.usermanagement.dto.ViewManagementDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.usermanagement.service.ViewManagementService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<script type="text/javascript">
function showEditWindow(viewId,viewName, viewKey,urlPath,showorder){
	var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var size = "height=450,width=400,top=200,left=300," + params;
	var url="editView.jsp?viewId="+ encodeURI( viewId)+"&viewName="+encodeURI( viewName)+"&viewKey="+encodeURI( viewKey)+"&showorder="+encodeURI( showorder)+"&url=" + encodeURI( urlPath);
	 
    newwindow = window.open(url, 'Edit View', size);
	if (window.focus) {
		newwindow.focus();
	}
}

function showSubView(viewId,viewName,urlPath ){
	 
	var url = "ViewSubview.jsp?viewId="+ encodeURI( viewId)+"&viewName="+encodeURI( viewName)+"&url=" + encodeURI( urlPath) + "&type=child"; 
	 
	window.location.href = url;
     
}


function deleteRole(deleteid){
	
	 $("input[name=deleteId]").val(deleteid);
	 if(confirm("Do you really want to delete ?"))
		 { 
			$("form[name=deleteroleform]").submit();
			
		 }
  
  
}
</script>
<title>All Views</title>
<div class="content">
<div class="content_resize">
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
			<br />
			<%
			String parentName = request.getParameter("viewName");
			String parentId = request.getParameter("viewId");
			String type = request.getParameter("type");
			parentName= parentName==null?"": parentName;
			parentId =  parentId==null ? "": parentId;
			type = type == null ? "" : type;
			
ViewManagementService service=new ViewManagementService();
ArrayList<ViewManagementDto> dtoList=null;
 if(type.equalsIgnoreCase("CHILD")) 
 {
	 
	 dtoList=service.getSubviewsbyView(Integer.parseInt(parentId));
 } else {
	 dtoList=service.getAllRootViewList(); 
 }
%>
<h3>Views</h3>
<table>
<thead>
<tr>
<th align="center">Id</th>
<th align="center">Identifying Name</th>
<th align="center">View Name</th>
<th align="center">URL</th>
<th align="center">Show Order</th>
<th align="center" width="10%">Action</th>
</tr>
</thead>
<%

for(ViewManagementDto dto: dtoList)
{
%>
<tr>
<td align="center"><%=dto.getViewId() %>
<input type="hidden" value="<%=dto.getViewId() %>" id=viewId />
</td>
<td align="center">
 
<%=dto.getViewKey() %>
<input type="hidden" value="<%=dto.getViewKey()%>" id=viewKey />
</td>
<td align="center"><%=dto.getViewName()%>
<input type="hidden" value="<%=dto.getViewName() %>" id=viewName />
</td>
<td align="center"><%=dto.getViewURL() %>
<input type="hidden" value="<%=dto.getViewURL()%>" id="url" />
</td>
<td align="center"><%=dto.getViewShowOrder()%>
<input type="hidden" value="<%=dto.getViewShowOrder()%>" id="showorder" />
</td>
<td><table ><tr><td style="border-bottom: 0px solid #cE5; padding: 0px;" > <img src="images/edit1.png" class="editButton" onclick="showEditWindow(<%=dto.getViewId() %>,'<%=dto.getViewName() %>','<%=dto.getViewKey() %>','<%=dto.getViewUrlId()%>',<%=dto.getViewShowOrder() %>);" id="editimage"
									title="Edit" />
</td><td style="border-bottom: 0px solid #cE5; padding: 0px;"> 	<img src="images/delete1.png" class="deleteButton" onclick="deleteRole(<%=dto.getViewId()%>)" id="deleteimage"
									title="Delete" /></td>
<td align="center"><input type="button" class="formbutton" value="Sub Views" onClick="showSubView(<%= dto.getViewId() %>,'<%=dto.getViewName()%>','<%=dto.getViewUrlId()%>' )"/>
</td></tr></table></td></tr>
<%
 
}
%>
</table>
<table>
<td align="center">
<input type="button" class="formbutton" value="Add View" onClick="self.location='AddView.jsp'"/>
</td>
</table>
<form name="deleteroleform" action="DeleteView" method="post">
<input type="hidden" name=deleteId id=deleteId/>
</form>
<hr />
</head>
<body>

</body>
</html>