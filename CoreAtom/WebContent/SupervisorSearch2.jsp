<%-- 
    Document   : ManagerSearch
    Created on : Oct 17, 2012, 2:28:15 PM
    Author     : 123
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<script type="text/javascript">
        
           function loadEmployeeDetails()
           
{

        	   if(confirm())
	{
try{
    var xmlhttp;
if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xmlhttp.onreadystatechange=function()
  {
  if (xmlhttp.readyState==4 && xmlhttp.status==200)
    {
    document.getElementById("EmployeeDetails").innerHTML=xmlhttp.responseText;
    }
  };
  xmlhttp.open("POST","GetEmployeeData?employeeID=" + document.SearchForm.employeeID.value + "&firstName=" + document.SearchForm.firstName.value + "&lastName=" + document.SearchForm.lastName.value,true);
xmlhttp.send();
}catch (e) {
	 alert(e.message);
}
	}else
		{
		alert("Please type texts in search fields");
    	//	alert("No records found !");
		}

}
           
           function confirm()
           {
          	   var flag=true;
          	 
           
          	 if($("input[name=employeeID]").val().trim()=="" && $("input[name=firstName]").val().trim()=="" &&	$("input[name=lastName]").val().trim()=="" )
          		 {
          		 flag=false;
          		 	 
          		 }
          	 
          	 return flag;
          	 
          	 
 
  
          	  
           }
           
           function onsubmitfn()
           {        	   
           	loadEmployeeDetails();
           	return false;	
           }

function selectRow(id)
{
	var name = document.getElementById("employeeNameCell-" + id).innerHTML;
	var userType=  document.getElementById("userType-" + id).value;
	try {			
		// var  id=document.getElementById("personnelNoCell-" + id).innerHTML;
		opener.subscriptionForm.supervisorID2.value = id;
		opener.subscriptionForm.supervisorName2.value = name;
		
		opener.document.getElementById("userType2").value = userType;
		this.close();
	} catch (e) {
		opener.document.getElementById("supervisorID2").value = id;
		opener.document.getElementById("supervisorName2").value = name;
		this.close();
	}
}

        </script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search Manager</title>
</head>
<body>
	<form name="SearchForm" onsubmit="return onsubmitfn();">


		<table border="0">

			<tr>
				<td>Employee ID</td>
				<td><input type="text" name="employeeID" /></td>
				<td></td>
			</tr>
			<tr>
				<td>First Name</td>
				<td><input type="text" name="firstName" /></td>
				<td></td>
			</tr>
			<tr>
				<td>Last Name</td>
				<td><input type="text" name="lastName" /></td>
				<td></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" class="formbutton" name="Search"
					value="Search" onclick="loadEmployeeDetails()" /></td>
				<td></td>
			</tr>


		</table>
		<div id="EmployeeDetails"></div>

	</form>

</body>
</html>




