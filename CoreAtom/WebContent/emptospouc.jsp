<%@page import="com.agiledge.atom.service.SpocService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
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
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<script src="js/dateValidation.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script src="js/dateValidation.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	 $("#emptable").tablesorter(); 
	 $("#lblmfrom").hide();
	 $("#lblmto").hide();
	 $("#searchemp").hide();
	 $("#multifrom_date").hide();
	 $("#multito_date").hide();
	 $('#multifrom_date').datepick();
	 $('#multito_date').datepick();
	 $("#lblsfrom").hide();
	 $("#lblsto").hide();
	 $("#selfrom_date").hide();
	 $("#selto_date").hide();
	 $('#selfrom_date').datepick();
	 $('#selto_date').datepick();
});
</script>
<title>Make Employee As Spoc</title>
<script type="text/javascript">
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
	function validate() {
		var currentDate = new Date();
		var flag=true;
		var currentDatevar = currentDate.getDate() + "/"
				+ parseInt(parseInt(currentDate.getMonth())+1) + "/" + currentDate.getFullYear();
	 var checked = $("#multispoc input:checked").length > 0;
		 var fromval=document.getElementById("multifrom_date").value;
		 var toval=document.getElementById("multito_date").value;
		 alert(currentDatevar+"   "+fromval);
				    if (!checked){
		        alert("Please check at least one checkbox");
		        flag=false;
		    }
		    else 
		    	{
		    	if($("#multicheck").attr('checked'))
		    	{
		    		if(fromval=="")
		    			{
		    			alert("Please Specify From Date!");
		    			flag=false;
		    			}
		    		else if(toval=="")
	    			{
	    			alert("Please Specify To Date!");
	    			flag=false;
	    			}
		    		 else if (!less_thanEqual(currentDatevar,fromval)) {
		    			alert("From date must be same or after current date !");
		    			flag=false;
		    		}else if (!less_thanEqual(fromval,toval)) {
		    			alert("To date must be same or after From date !");
		    			flag=false;
		    		}
		    		}
		    	}
		    	return flag;
		    	}
	function showPopup(url) {
		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		size = "height=450,width=520,top=200,left=300," + params;
		newwindow = window.open(url, 'name', size);

		if (window.focus) {
			newwindow.focus();
		}
	}
	function multitime(par)
	{
		if(par=="multi")
			{
			$("#multifrom_date").val("");
			 $("#multito_date").val("");
		 if($("#multicheck").attr('checked'))
			 {
			 $("#lblmfrom").show();
			 $("#lblmto").show();
			 $("#multifrom_date").show();
			 $("#multito_date").show();
			 }
		 else 
			 {
			 $("#lblmfrom").hide();
			 $("#lblmto").hide();
			 $("#multifrom_date").hide();
			 $("#multito_date").hide();
			 }
			}
		else
			{
			$("#selfrom_date").val("");
			 $("#selto_date").val("");
			 if($("#selcheck").attr('checked'))
			 {
			 $("#lblsfrom").show();
			 $("#lblsto").show();
			 $("#selfrom_date").show();
			 $("#selto_date").show();
			 }
		 else 
			 {
			 $("#lblsfrom").hide();
			 $("#lblsto").hide();
			 $("#selfrom_date").hide();
			 $("#selto_date").hide();
			 }
			}
	}
	function creset()
	{
		 $("#lblmfrom").hide();
		 $("#lblmto").hide();
         $("#multifrom_date").hide();
		 $("#multito_date").hide();
	}
	function removeOption()
	{
	var x=document.getElementById("views");
	x.remove(x.selectedIndex);
	}
	function submitForm() {
		var currentDate = new Date();
		var currentDatevar = currentDate.getDate() + "/"
				+ currentDate.getMonth()+1 + "/" + currentDate.getFullYear();
		var flag=true;
		var views = document.getElementById("views");
		var viewsLength = document.getElementById("views").options.length;
		var fromval=document.getElementById("selfrom_date").value;
		 var toval=document.getElementById("selto_date").value;
		if(viewsLength<1)
			{
			alert("Select Atleast One Employee!");
			flag=false;
			}
		else if($("#selcheck").attr('checked'))
			{
			if(fromval=="")
				{
				alert("Please Specify From Date!");
				flag=false;
				}
			else if(toval=="")
				{
				alert("Please Specify To Date!");
				flag=false;
				}
			else if (!less_thanEqual(currentDatevar,fromval)) {
    			alert("From date must be same or after current date !");
    			flag=false;
    		}else if (!less_thanEqual(fromval,toval)) {
    			alert("To date must be same or after From date !");
    			flag=false;
    		}
			}
			var jsonData="";
			var jsonres=""; 
			for ( var i = 0; i < viewsLength; i++) {
				jsonData="";
				jsonres="";
				try{
					 jsonData = $.ajax({
				          url: "CheckSpoc?empid="+views.options[i].value+"&from_date="+fromval+"&to_date="+toval,
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
					var alrtmsg="Selected Employee "+views.options[i].text+" Is Already Assigned As Spoc!..Try With Different Time Constraint!";
					alert(alrtmsg);
					}
			}
		for ( var i = 0; i < viewsLength; i++) {
			views.options[i].selected = true;
		}
		return flag;
	}
</script>
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
    <h3 align="center">Assign Employee As Spoc</h3>
    <br>
    <br>
    <br>
    <select id="menuselect" onChange="selectval();" ><option value="all">All</option><option value="select">Search Employee</option></select>
     <br>
    <br>
    <br>
    <form name="multispoc" id="multispoc" method="post" onsubmit="return validate()" action="AddMultiSpoc">
      <%
  int i=1;
  String type="";
  int mid=Integer.parseInt(employeeId);
  try{		
		ArrayList<EmployeeDto> list =new SpocService().getemployeesbymanagerforspoc(empid);
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
			type="Employee";
			if(dto.getUserType().equals("v"))
			type="Vendor";
			else if(dto.getUserType().equals("tm"))
			type="Transport Manager";
			else if(dto.getUserType().equals("hrm"))
			type="Manager";
			else if(dto.getUserType().equals("tc"))	
			type="Transport Co-Ordinator";
			else if(dto.getUserType().equals("ta"))	
			type="Transport Administrator";
		%>
		<tr>
		<td align="center"><input type="checkbox" id="employeeIdCheckBox" name="employeeIdCheckBox" value="<%=dto.getEmployeeID()%>"/></td>
		<td align="center"><%=dto.getPersonnelNo() %></td>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=dto.getEmployeeFirstName() %></td>
		<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=dto.getEmployeeLastName() %></td>
		<td align="center"><%=dto.getGender() %></td>
		<td align="center"><%=dto.getLoginId() %></td>
		<td align="center"><%=type %></td>
		<td align="center"><%=dto.getEmailAddress() %></td>
		</tr>
			
	<%	}%>
	</table>
	<br><br><table>
	<tr>
	<td align="center"><input type="checkbox" id="multicheck" onchange="multitime('multi');" /><label>Time Constraint</label></td>
	<td><label id="lblmfrom">From Date</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="multifrom_date" id="multifrom_date"  readOnly class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2012,  1, 25)}" />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label id="lblmto">To Date</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="multito_date" id="multito_date" readOnly class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2012,  1, 25)}" />
	</td></tr><tr>
	<td align="center" colspan="4"><br><br><input type="submit" class="formbutton" value="Submit"/>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" class="formbutton" value="Reset" onClick=creset(); /></td>
	</tr>
	</table>
	 <input type="hidden" id="mainselector" name="mainselector" value="all" />
   <input type="hidden" id="man_id" name="man_id" value="<%=empid %>"/>
   <% }
  }catch(Exception e)
	{
		System.out.println("Error in emptospoc.jsp"+e);
	}
	%>
   </form>
   <form id="searchemp" name="searchemp" action="AddMultiSpoc" method="post" onSubmit="return submitForm();">
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
   <tr>
	<td align="left"><input type="checkbox" id="selcheck" onchange="multitime('sel');" /><label>Time Constraint</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label id="lblsfrom">From Date</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="selfrom_date" id="selfrom_date"  readOnly class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2012,  1, 25)}" />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label id="lblsto">To Date</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="selto_date" id="selto_date" readOnly class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2012,  1, 25)}" />
	</td></tr>
   <tr align="left">
   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="Submit" class="formbutton"/></td>
   </tr>
   </table>
   <input type="hidden" id="man_id" name="man_id" value="<%=empid %>"/>
   </form>
<%@include file='Footer.jsp'%>
		</div>
		</div>
</body>
</html>