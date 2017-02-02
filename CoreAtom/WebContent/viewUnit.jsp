<%@page import="com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.UnitService"%>
<%@page import="com.agiledge.atom.dto.UnitMasterDTO" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.tablesorter.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	 $("form[name=EditUnit]").hide();
	 $("form[name=AddUnit]").hide();
	 $("#addclose_img").click(closeAddArea);
	 $("#close_img").click(closeEditArea);
	 $("#unitTable").tablesorter(); 
});
function closeEditArea()
{
	  $("form[name=EditUnit]").hide();
	  $("form[name=EditUnit]").attr("action","#");
}
function showEditArea(id,uncode,unname,locid,sno)
{
	 $("form[name=EditUnit]").show();
	 $("input[name=unid]").val(id);
	 $("input[name=ucode]").val(uncode);
	 $("input[name=serialno]").val(sno);
	 $("textarea[name=uname]").val(unname);
	 $("input[name=lid]").val(locid);
}
function editvalidate()
{
	var id=$("input[name=unid]").val();
	var unid=$("input[name=ucode]").val();
	var uname=$("textarea[name=uname]").val();
	var serial=$("input[name=serialno]").val();
	var flag=true;
	if(unid=="")
		{
		flag=false;
		alert("Please Specify Unit Code!");
		}else if(uname=="")
		{
		flag=false;
		alert("Please Specify Unit Description");
		}else if(unid.length>0)
		{
        var oTable = document.getElementById("unitTable");
		var i;
		var rowLength = oTable.rows.length;
		for (i = 2; i < rowLength; i++) {
		var oCells = oTable.rows.item(i).cells;
		if (unid==oCells[1].firstChild.data) {
		if(serial!=oCells[0].firstChild.data)
		{
		alert("Unit Code You Have Entered Already Exists!");
		flag= false;
		break;
		}
		}

		}
		}
	return flag;
	}
function updateStatus(id,curstatus)
{
	 var msg="Do you really want to deactivate ? ";
	 $("input[name=updid]").val(id);
	 $("input[name=upstatus]").val(curstatus);
	 if(curstatus=="Cancel")
		 msg="Do you really want to activate ? ";
	 if(confirm(msg))
	 { 
	 document.getElementById("statusform").submit(); 
	 }
}
function closeAddArea()
{
	$("form[name=AddUnit]").hide();
    $("form[name=AddUnit]").attr("action","#");
	
}
function showAddArea()
{
	 $("form[name=AddUnit]").show();
}
function addvalidate()
{
	var unid=$("input[name=adducode]").val();
	var uname=$("textarea[name=adduname]").val();
	var flag=true;
	if(unid=="")
	{
	flag=false;
	alert("Please Specify Unit Code!");
	}else if(uname=="")
	{
	flag=false;
	alert("Please Specify Unit Description");
	}else if(unid.length>0)
	{
    var oTable = document.getElementById("unitTable");
	var i;
	var rowLength = oTable.rows.length;
	for (i = 2; i < rowLength; i++) {
	var oCells = oTable.rows.item(i).cells;
	if (unid==oCells[1].firstChild.data) {
	alert("Unit Code You Have Entered Already Exists!");
	flag= false;
	break;
	}

	}
	}
	return flag;
	}
function showuploadform(){
		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		var size = "height=250,width=500,top=200,left=300," + params;
		var url="UnituploadExcel.jsp";	
	    newwindow = window.open(url, 'Upload', size);

		if (window.focus) {
			newwindow.focus();
		}
	}
</script>
<title>View Unit Data</title>
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
	%>
	<%@include file="Header.jsp"%>
	<%
		}
	%>
<h3 align="center">View All Unit Data</h3>
<table id="unitTable" class="tablesorter">
<thead> 
<tr>
		<td colspan="7" align="right">
		<a href='<%=request.getContextPath() %>/UnitDataTemplate.xlsx'>Download Template</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="formbutton" value="New" onClick="showAddArea();"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="formbutton" value="Upload" onClick="showuploadform();"/>
		<td>
		<tr>
		    <th align="center"><a href=''>Serial No.</a></th>
			<th align="center" ><a href=''>Unit Code</a></th>
			<th align="center"><a href=''>Unit Description</a></th>
			<th align="center"><a href=''>Location Id</a></th>
			<th align="center"><a href=''>Status</a></th>
			<th align="center">Actions</th>
		</tr>
		</thead>
		</tbody>
		<%
		String status="",value="";
		int i=1;
		ArrayList<UnitMasterDTO> list=new UnitService().getallUnits();
		for(UnitMasterDTO dto:list)
		{
			if(dto.getStatus().equalsIgnoreCase("A"))
			{
				status="Active";
				value="Deactivate ";
			}else
			{
				status="Cancel";
				value="Activate";
			}
		%>
		<tr>
		<td align="center"><%=i++%></td>
		<td align="center"><%=dto.getUnitcode() %></td>
		<td align="center"><%=dto.getUnitname() %></td>
		<td align="center"><%=dto.getLocation_id() %></td>
		<td align="center"><%=status %></td>
		<td align="center"><img align="left" src="images/edit.png" class="editButton" title="Edit" onClick="showEditArea(<%=dto.getUnitid()%>,'<%=dto.getUnitcode() %>','<%=dto.getUnitname()%>','<%=dto.getLocation_id()%>',<%=i-1%>)"/>&nbsp;&nbsp;&nbsp;&nbsp;<input align="left" name="statusbutton" type="button" class="formbutton" value="<%=value %>" onClick="updateStatus('<%=dto.getUnitid() %>','<%=status %>')" /></td>
		</tr>
		<%
		}
		%>
		
		</table>
		<table>
		<tr>
		<td colspan="7" align="right">
		<a href='<%=request.getContextPath() %>/UnitDataTemplate.xlsx'>Download Template</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="formbutton" value="New" onClick="showAddArea();"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="formbutton" value="Upload" onClick="showuploadform();"/>
		<td>
		</tr>
		</table>
		<div align="center" style="position:fixed; top:20%; left:35%;">
		<form name="EditUnit" action="EditUnit" method="post" onSubmit="return editvalidate()">
		<table style="border-style: outset; width: 130%; background-color:#ddd;" >
		<thead>
		<tr>
	    <th colspan="2" align="center"><label id="windowTitle">Edit Unit Data</label>
		<div style="float: right;" id="closeAddArea">
		<img id="close_img" style="float: right;" id="close" src="images/close.png" title="Close" />
		</div></th>
		</tr>
		</thead>
		<tr>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Unit Code</td><td align="left"><input type="hidden" id="serialno" name="serialno"/><input type="hidden" id="unid" name="unid"/><input type="text" id="ucode" name="ucode"/></td>
		</tr>
		<tr>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Unit Description</td><td align="left"><textarea rows="" cols="" id="uname" name="uname"></textarea></td>
		</tr>
		<tr>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Location Id</td><td align="left"><input type="text" id="lid" name="lid"/></td>
		</tr>
		<tr>
		</tr>
		<tr align="center">
		<td colspan="2" align="center">
		<input type="submit" value="Update" class="formbutton"/>
		</td>
		</tr>
		</table>
		</form>
		</div>
		<form name="statusform" id="statusform" action="Updateunit" method="post">
		<input type="hidden" id="updid" name="updid"/>
		<input type="hidden" id="upstatus" name="upstatus"/>
 		</form>
 		<div align="center" style="position:fixed; top:20%; left:35%;">
		<form name="AddUnit" action="AddUnit" method="post" onSubmit="return addvalidate()">
		<table style="border-style: outset; width: 130%; background-color:#ddd;" >
		<thead>
		<tr>
	    <th colspan="2" align="center"><label id="windowTitle">Add Unit Data</label>
		<div style="float: right;" id="closeAdd">
		<img id="addclose_img" style="float: right;" src="images/close.png" title="Close" />
		</div></th>
		</tr>
		</thead>
        <tr>
        <td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Unit Code</td><td align="left"><input id="adducode" name="adducode"/></td>
        </tr>
        <tr>
        <td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Unit Description</td><td align="left"><textarea id="adduname" name="adduname"></textarea></td>
        </tr>
        <tr>
        <td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Location Id</td><td align="left"><input id="addlocid" name="addlocid"/></td>
        </tr>
        <tr>
        <td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Status</td><td align="left"><select id="addstatus" name="addstatus"><option value="A">Active</option><option value="C">Cancel</option></select></td>
        </tr>
        <tr>
        </tr>
        <tr align="center">
		<td colspan="2" align="center">
		<input type="submit" value="Add" class="formbutton"/>
		&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" value="Reset" class="formbutton"/>
		</td>
		</tr>
        </table>
        </form>
        </div>
		<%@ include file='Footer.jsp' %>
</body>
</html>