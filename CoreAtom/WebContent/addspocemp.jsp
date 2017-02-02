<%@page import="com.agiledge.atom.service.SpocService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<link href="css/validate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.tablesorter.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	 $("#emptable").tablesorter();
	 $("#searchemp").hide();
});
function selectval()
{
	var val=document.getElementById("menuselect").value;
	if(val=="select")
		{
		 $("#multispoc").hide();
		 $("#searchemp").show();
		}
	else
		{
		$("#searchemp").hide();
		$("#multispoc").show();
		}
	}
function showPopup(url) {
	var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	size = "height=450,width=520,top=200,left=300," + params;
	newwindow = window.open(url, 'name', size);

	if (window.focus) {
		newwindow.focus();
	}
}
function removeOption()
{
var x=document.getElementById("views");
x.remove(x.selectedIndex);
}
function submitForm() {
	var flag=true;
	var views = document.getElementById("views");
	var viewsLength = document.getElementById("views").options.length;
	if(viewsLength<1)
		{
		alert("Select Atleast One Employee!");
		flag=false;
		}
	else
		{
		for ( var i = 0; i < viewsLength; i++) {
			jsonData="";
			jsonres="";
			try{
				 jsonData = $.ajax({
			          url: "CheckAssignEmp?empid="+views.options[i].value,
			          dataType:"json",
			          async: false
			          }).responseText;
				}catch (e)
				{
					 alert(" error " + e.message);
				}
				jsonres= jQuery.parseJSON(jsonData);
				if(jsonres.result=="true"){
				flag=false;
				var alrtmsg="Selected Employee "+views.options[i].text+" Is Already Assigned Under A Spoc!";
				alert(alrtmsg);
				}
		}
	for ( var i = 0; i < viewsLength; i++) {
		views.options[i].selected = true;
	}
	return flag;
}
}
function validate() {
 var checked = $("#multispoc input:checked").length > 0;
			    if (!checked){
	        alert("Please check at least one checkbox");
	        return false;
	    }
			    else
			    	{
			    	return true;
			    	}
}
</script>
<title>Assign Employee Under Spoc</title>
</head>
<body>
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
    <h3 align="center">Assign Employee Under <%=request.getParameter("spoc_name") %></h3>
    <br>
    <br>
    <br>
    <select id="menuselect" onChange=selectval(); ><option value="all">All</option><option value="select">Search Employee</option></select>
     <br>
    <br>
    <br>
    <form name="multispoc" id="multispoc" method="post" onsubmit="return validate()" action="AddEmployeeSpoc">
      <%
  int i=1;
  String type="";
  int mid=Integer.parseInt(employeeId);
  try{		
		ArrayList<EmployeeDto> list =new SpocService().getemployeesbymanagerspocid(empid);
		if(list.isEmpty())
		{%>
			<h4 align="center">Sorry, No Employees Found!</h4>
		<%}
		else
		{
		%>
   <table id="emptable" class="tablesorter">
  <thead>
  <tr> 
  <th align="center" width="4%"></th>
  <th align="center" ><a href=''>Personnel Number</a></th>
  <th align="center"><a href=''>First Name</a></th>
  <th align="center"><a href=''>Last Name</a></th>
  <th align="center"><a href=''>Gender</a></th>
  <th align="center"><a href=''>Login Id</a></th>
  <th align="center"><a href=''>User Type</a></th>
  <th align="center"><a href=''>Email</a></th>
  </tr>
  </thead>
  </tbody>
		<% 
		for(EmployeeDto dto:list)
		{		
		%>
		<tr>
		<td align="center"><input type="checkbox" id="employeeIdCheckBox" name="employeeIdCheckBox" value="<%=dto.getEmployeeID()%>"/></td>
		<td align="center"><%=dto.getPersonnelNo() %></td>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=dto.getEmployeeFirstName() %></td>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=dto.getEmployeeLastName() %></td>
		<td align="center"><%=dto.getGender() %></td>
		<td align="center"><%=dto.getLoginId() %></td>
		<td align="center"><%=dto.getUserType() %></td>
		<td align="center"><%=dto.getEmailAddress() %></td>
		</tr>
			
	<% }
		}
	%>
	</table>
	<br><br><table>
	<tr>
	<td align="center" colspan="4"><br><br><input type="submit" class="formbutton" value="Submit"/>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" class="formbutton" value="Reset"/></td>
	</tr>
	</table>
<% 	 }catch(Exception e)
	{
		System.out.println("Error in emptospoc.jsp"+e);
	}
	%>
	<input type="hidden" id="spocid" name="spocid" value="<%=request.getParameter("spoc_id")%>" />
	<input type="hidden" id="spocname" name="spocname" value="<%=request.getParameter("spoc_name")%>" />
	<input type="hidden" id="mainselector" name="mainselector" value="all" />
   </form>
   <form id="searchemp" name="searchemp" action="AddEmployeeSpoc" method="post" onSubmit="return submitForm()">
   <br>
   <br>
   <br>
    <input type="hidden" id="mainselector" name="mainselector" value="select" />
   <label>Select Employee</label>
   <table align="left">
   <tr align="left">
   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select name="views" id="views" size="10" multiple></select>
   <input class="formbutton" type="button" value="..." onclick="showPopup('spsearch.jsp')" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="formbutton" onclick="removeOption()" value="Remove"></td>						
   </tr>
   <tr align="left">
   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="Submit" class="formbutton"/></td>
   </tr>
   </table>
   <input type="hidden" id="spocid" name="spocid" value="<%=request.getParameter("spoc_id") %>" />
	<input type="hidden" id="spocname" name="spocname" value="<%=request.getParameter("spoc_name")%>" />
   </form>
<%@include file='Footer.jsp'%>
		</div>
		</div>
</body>
</html>