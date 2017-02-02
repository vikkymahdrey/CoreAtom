<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.usermanagement.dto.ViewManagementDto"%>
<%@page import="com.agiledge.atom.usermanagement.service.ViewManagementService"%>
<%@ page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.agiledge.atom.dto.UserManagementDTO" %>
  
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%
    
    System.out.println("................ first  : "  );
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<script type="text/javascript">
function deleteSubview(deleteid,viewId,viewName){
	
	 $("input[name=deleteId]").val(deleteid);
	 $("input[name=deleteviewid]").val(viewId);
	 $("input[name=deleteviewname]").val(viewName);
	 if(confirm("Do you really want to delete ?"))
		 { 
			$("form[name=deletesubviewform]").submit();
			
		 }
 
 
}

function  showAddSubview(viewId, viewName ) {
	
	var url = "AddSubview.jsp?viewId=" + encodeURI(viewId) + "&viewName=" + encodeURI(viewName); 
	window.location.href = url;
}

function showEditWindow(subviewId,parentId,subviewName, subviewKey,url,showorder){
	var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	var size = "height=450,width=400,top=200,left=300," + params;
	var url="editSubview.jsp?subviewId="+encodeURIComponent(subviewId)+"&parentId="+encodeURIComponent(parentId)+"&subviewName="+encodeURIComponent(subviewName)+"&subviewKey="+encodeURIComponent(subviewKey)+"&showorder="+encodeURIComponent(showorder)+"&url="+encodeURIComponent(url);
	  
    newwindow = window.open(url, 'Edit Subview', size);
	if (window.focus) {
		newwindow.focus();
	}
}
</script>
<title>Sub Views</title>
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
int viewId=Integer.parseInt(request.getParameter("viewId"));
String viewName=request.getParameter("viewName");
ViewManagementService service =new ViewManagementService();
ArrayList<ViewManagementDto> dtoList=service.getSubviewsbyView(viewId);
if(dtoList.isEmpty())
{%>
	 <center style="color: red;">Sorry,No Subview Found For This View</center>
<%}
else
{
%>
<h3>Sub Views Under <%=viewName %></h3>
<table>
<thead>
<th align="center">Id</th>
<th align="center">Sub-View Key</th>
<th align="center">Sub-View Name</th>
<th align="center">URL</th>
<th align="center">Show Order</th>
<th align="center">Action</th>
</thead>
<%
for(ViewManagementDto dto:dtoList)
{
%>
<tr>
<td align="center"><%=dto.getSubViewId() %></td>
<td align="center"><%=dto.getSubViewKey() %></td>
<td align="center"><%=dto.getSubViewName() %></td>
<td align="center"><%=dto.getSubViewURL() %></td>
<td align="center"><%=dto.getSubViewShowOrder() %></td>
<td><table ><tr><td style="border-bottom: 0px solid #cE5; padding: 0px;" > <img src="images/edit1.png" class="editButton" id="editImage" onclick="showEditWindow(<%=dto.getSubViewId() %>,<%=dto.getParentId() %>,'<%=dto.getSubViewName() %>','<%=dto.getSubViewKey()%>','<%=dto.getSubViewURLId() %>',<%=dto.getSubViewShowOrder() %>)"
									title="Edit" />
</td><td style="border-bottom: 0px solid #cE5; padding: 0px;"> 	<img src="images/delete1.png" class="deleteButton"  id="deleteImage" onclick="deleteSubview(<%=dto.getSubViewId() %>,<%=viewId %>,'<%=viewName %>')"
									title="Delete" /> 
</td ></tr></table></td>
</tr>
<%
}
  }

%>

</table>
<table>
<tr>
</tr>
<td align="center">
<input align="middle" type="button" class="formbutton" value="Add Subview" onClick="showAddSubview(<%=viewId%>, '<%=viewName%>' )"/>
</td>
</table>
<form name="deletesubviewform" action="DeleteSubview" method="post">
<input type="hidden" name=deleteId id=deleteId/>
<input type="hidden" name=deleteviewid id=deleteviewid/>
<input type="hidden" name=deleteviewname id=deleteviewname/>
</form>
</body>
</html>