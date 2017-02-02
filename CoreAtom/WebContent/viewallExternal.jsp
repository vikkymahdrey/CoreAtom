<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.agiledge.atom.usermanagement.service.UserManagementService"%>
<%@page import="com.agiledge.atom.dto.UserManagementDTO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.tablesorter.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	 $("form[name=EditEmployee]").hide();
	 $("#close_img").click(closeEditArea);
	 $("#extTable").tablesorter();	 
});
function showuploadform(){
      		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
      		var size = "height=250,width=500,top=200,left=300," + params;
      		var url="uploadExcel.jsp";	
      	    newwindow = window.open(url, 'Upload', size);
      		if (window.focus) {
      			newwindow.focus();
      		}
      	}
function showuploadform1(){
		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		var size = "height=250,width=500,top=200,left=300," + params;
		var url="uploadManpowerList.jsp";	
	    newwindow = window.open(url, 'Upload', size);
		if (window.focus) {
			newwindow.focus();
		}
	}
function updateStatus(empid,curstatus)
{
	 var msg="Do you really want to disable ? ";
	 $("input[name=empid]").val(empid);
	 $("input[name=active]").val(curstatus);
	 if(curstatus==0)
		 msg="Do you really want to enable ? ";
	 if(confirm(msg))
	 { 
	 document.getElementById("statusform").submit(); 
	 }
}
 function resetPassword(empid)
 {
	 document.getElementById("pwdempid").value=empid;
	 if(confirm("Do you really want to reset password for this user?"))
		 {
		 document.getElementById("pwdreset").submit();
		 }
 }
 
 function showEditArea(personnelNo,pno,eid,gen,atyp,utyp,project,linemanager,pid)
 {
	 $("form[name=EditEmployee]").show();
	 $("#editExemp").draggable();
	
$("input[name=personnelNo]").val(personnelNo);
	 $("input[name=fname]").val($("input[name=fisname"+personnelNo+"]").val());
	 $("input[name=mname]").val($("input[name=midname"+personnelNo+"]").val());
	 $("input[name=lname]").val($("input[name=lasname"+personnelNo+"]").val());
	 $("input[name=dname]").val($("input[name=dsname"+personnelNo+"]").val());
	 $("input[name=phno]").val(pno);
	 $("input[name=eid]").val(eid);
	 $("select[name=gender]").val(gen);
	 $("select[name=authtype]").val(atyp);
	 $("select[name=usertype]").val(utyp);
     $("textarea[name=address]").val($("input[name=address"+personnelNo+"]").val());
     $("input[name=projectdesc]").val(project);
     $("input[name=supervisorName1]").val($("input[name=linename"+personnelNo+"]").val());
     $("input[name=supervisorID1]").val(linemanager);
     $("input[name=project]").val(pid);
     
 }
 function closeEditArea()
 {
	  $("form[name=EditEmployee]").hide();
	  $("form[name=EditEmployee]").attr("action","#");
 }
 function editvalidate()
 {
	 
	var fname = $("input[name=fname]").val();
	var lname = $("input[name=lname]").val();
	var dname = $("input[name=dname]").val();
	var eid = $("input[name=eid]").val();
	var address =  $("textarea[name=address]").val();
	var supervisorName=$("#supervisorName1").val();	
	var flag=true;
	if(fname=="")
	{
	alert("Please Specify First Name");
	flag=false;
	}else if(lname=="")
	{
		alert("Please Specify Last Name");
		flag=false;
		}else if(dname=="")
		{
			alert("Please Specify Display Name");
			flag=false;
			}else if(eid=="")
				{
					alert("Please Specify Email Id");
					flag=false;
					}
			else if(address=="")
			{
				alert("Please Specify the address");
				flag=false;
				}
			else if(supervisorName=="")
			{
			alert("Please Specify Reporting Officer");
			flag=false;
			}
	
	return flag;
 }
 function showPopup(url) {

	    var params="toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	    size="height=450,width=520,top=200,left=300,"+params;
	    newwindow=window.open(url,'name',size);
	    

	     if (window.focus) {newwindow.focus();}
	    }
 function submitform()
 {
	 $("form[name=EditEmployee]").submit();
	 
	 
 }
 
 function showproject(eid,personnelno,dname,project,pid)
 {
	 var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		var size = "height=250,width=300,top=200,left=300," + params;
		var url="EditEmployee.jsp?eid="+eid+"&personnelno="+personnelno+"&project="+project+"&dname="+dname+"&pid="+pid;	
	    newwindow = window.open(url, 'Assign Project', size);
		if (window.focus) {
			newwindow.focus();
		}
 }
  
 </script>
<title>View All External Employees</title>

</head>
<body>
<%
		long empid = 0;
         String status="",value="";
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
		
		String showAll = request.getParameter("showAll");
		showAll = showAll==null?"":showAll;
		System.out.println(" SHOW ALL : " + showAll);
		 
		if(showAll.equals("")==false && showAll.equals("Show All")) {
			 
			 showAll = "Show Active Employees Only";
			 
		} else {
			showAll="Show All";
		}
	%>
<h3 align="center">View All External Employees</h3>
<div>
<form id="searchForm" action="viewallExternal.jsp">
<label>
	<input type="submit"  id="showAll"  name="showAll" value="<%=showAll %>"  />  
</label> 
</form>
</div>
<table id="extTable" class="tablesorter">
<thead> 
<tr>
		<td colspan="9" align="right">
		<a href='<%=request.getContextPath() %>/ExternalEmpTemplate.xlsx'>Download Template</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="formbutton" value="New" onClick="window.location.href='addExternalemp.jsp'"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="formbutton" value="Upload" onClick="showuploadform();"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="formbutton" value="Download ManPowerList" onclick="location.href = 'AlterEmpDetails.jsp'" />&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="formbutton" value="Upload ManPowerList" onClick="showuploadform1();"/>
		<td>
		<tr>
		<tr>
			<th align="center">Personnel Number</th>
			<th align="center">First Name</th>
			<th align="center">Last Name</th>
			<th align="center">Gender</th>
			<th align="center">Phone Number</th>
			<th align="center">Email Id</th>
			<th align="center">Status</th>
			<th align = "center"> Actions </th>
		</tr>
		</thead>
		</tbody>
		<%
		
		ArrayList<EmployeeDto> list=null;
		if(showAll.equalsIgnoreCase("Show All")) 
		{
			list = new EmployeeService().getEnabledExternalemployees();
		} else 
		{
	 		list = new EmployeeService().getAllExternalemployees();
		} 
		for(EmployeeDto dto:list)
		{ 
			String emp2 = dto.getEmployeeID();

			if(dto.getActive()==null || dto.getActive()=="")
			{	
				System.out.println(dto.getActive());
				status="Invalid";
				value="Invalid";
			}
			else
			{
				if(Integer.parseInt(dto.getActive())==0)
				{
					status="Disabled";
					value="Enable ";
				}else
				{
					status="Enabled";
					value="Disable";
				}
			}
			
			
		%>
		<tr>
		<td align="center"><%=dto.getPersonnelNo() %></td>
		<td align="center"><%=dto.getEmployeeFirstName() %></td>
		<td align="center"><%=dto.getEmployeeLastName() %></td>
		<td align="center"><%=dto.getGender() %></td>
		<td align="center"><%=dto.getContactNo() %></td>
		<td align="center"><%=dto.getEmailAddress() %>
		<input type="hidden" name="address<%=dto.getPersonnelNo()%>" value="<%=dto.getAddress() %>"/>
		<input type="hidden" name="linename<%=dto.getPersonnelNo()%>" value="<%=dto.getManagerName()%>"/>
		<input type="hidden" name="fisname<%=dto.getPersonnelNo()%>" value="<%=dto.getEmployeeFirstName()%>"/>
		<input type="hidden" name="midname<%=dto.getPersonnelNo()%>" value="<%=dto.getEmployeeMiddleName()%>"/>
		<input type="hidden" name="lasname<%=dto.getPersonnelNo()%>" value="<%=dto.getEmployeeLastName()%>"/>
		<input type="hidden" name="dsname<%=dto.getPersonnelNo()%>" value="<%=dto.getDisplayName()%>"/></td>
		
		
		<td align="center"><%=status%>&nbsp;&nbsp;&nbsp;&nbsp;<input name="statusbutton" type="button" class="formbutton" value="<%=value %>" onClick="updateStatus(<%=dto.getEmployeeID() %>,<%=dto.getActive() %>)" /></td>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/edit1.png" class="editButton" title="Edit" onClick='showEditArea("<%=dto.getPersonnelNo()%>","<%=dto.getContactNo() %>","<%=dto.getEmailAddress() %>","<%=dto.getGender()%>","<%=dto.getAuthtype() %>","<%=dto.getUserType()%>","<%=dto.getProject()%>","<%=dto.getLineManager()%>","<%=dto.getProjectid() %>")'/>
		&nbsp;&nbsp;&nbsp;&nbsp;<img src="images/project.png" title="Assign Project" onClick='showproject("<%=dto.getEmployeeID() %>","<%=dto.getPersonnelNo() %>","<%=dto.getDisplayName() %>","<%=dto.getProject() %>","<%=dto.getProjectid() %>")'/>
		<%
		
		if(dto.getAuthtype().equalsIgnoreCase("l"))
		{
		%>
		&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"  id="pwdresetbutton" class="formbutton" name="pwdresetbutton" value="Password Reset" onClick="resetPassword(<%=dto.getEmployeeID()%>)"/>
		<%
		}
		%>
		</td>
		</tr>
		<%
		}
		%>
		</table>
		 		<div id="editExemp" align="center" style="position:fixed; top:12%; left:35%;">
 		<br/>
		<form name="EditEmployee"  action="EditEmployee" method="post" onSubmit="return editvalidate()">
		
		<table style="border-style: outset; width: 100%;background-color:white;" >
		<thead>
		<tr>
	    <th colspan="2" align="center"><label id="windowTitle">Edit Employee</label>
		<div style="float: right;" id="closeAddArea">
		<img id="close_img" style="float: right;" id="close" src="images/close.png" title="Close" />
		</div></th>
		</tr>
		</thead>
		<tr>
		<input type="hidden" id="personnelNo" name="personnelNo" value=""/>
		
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;First Name</td><td align="left"><input type="text" id="fname" name="fname"/></td>
		</tr>
		<tr>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Middle Name</td><td align="left"><input type="text" id="mname" name="mname"/></td>
		</tr>
		<tr>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Last Name</td><td align="left"><input type="text" id="lname" name="lname"/></td>
		</tr>
		<tr>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Gender</td><td align="left"><select name="gender" id="gender"><option value="M">Male</option><option value="F">Female</option></select></td>
		</tr>
		<tr>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Display Name</td><td align="left"><input type="text" id="dname" name="dname"/></td>
		</tr>
		<tr>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;User Type</td><td align="left"><select name="usertype" id="usertype"> <option value="emp">O & I EMP</option><option value="spoc">EMPLOYEE</option><option value="hrm">Manager</option><option value="v">Vendor</option><option value="se">SECURITY VENDOR</option><option value="tc">Transport Coordinator</option></select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<tr>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Authentication Type</td><td align="left"><select name="authtype" id="authtype"><option value="l">Local</option><option value="w">Windows</option></select></td>
		</tr>
		<tr>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Phone Number</td><td align="left"><input type="text" id="phno" name="phno"/></td>
		</tr>
		<tr>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Email Id</td><td align="left"><input type="text" id="eid" name="eid"/></td>
		</tr>
		<tr>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Address</td><td align="left"><textarea rows="3" cols="30" name="address" id="address"/></textarea></td>
		</tr><tr>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;Project</td><td align="left"><input type="text" name="projectdesc" id="projectdesc"
								readonly="readonly" onclick="showPopup('getproject.jsp' ); "/>
								<input type="hidden" name="project" id="project"/>
								<input type="button" class="formbutton" value="..."
								onclick="showPopup('getproject.jsp' ); " /></td>
								</tr>
								<tr><td align="left"><%=SettingsConstant.hrm %></td><td align="left"><input type="hidden" name="supervisorID1"
							id="supervisorID1" /> <input type="text" readonly
							name="supervisorName1" id="supervisorName1"
							onclick="showPopup('SupervisorSearch1.jsp')" /> <label
							for="supervisorID1" class="requiredLabel">#</label> <input
							class="formbutton" type="button" value="..."
							onclick="showPopup('SupervisorSearch1.jsp')" /></td>
</tr>
		<tr>
		</tr>
		<tr align="center">
		<td colspan="2" align="center">
		
		<input type="button" value="Update" class="formbutton" onClick='submitform();'/>
		</td>
		</tr>
		
		</table>
		</form>
			</div>
			<form name="statusform" id="statusform" action="UpdateStatus" method="post">
		<input type="hidden" id="empid" name="empid"/>
		<input type="hidden" id="active" name="active"/>
 		</form>
 		<form name="pwdreset" id="pwdreset" action="ResetPassword" method="post">
 		<input type="hidden" id="pwdempid" name="pwdempid" value=""/>
 		</form>
		<%@include file='Footer.jsp'%>
</body>
</html>