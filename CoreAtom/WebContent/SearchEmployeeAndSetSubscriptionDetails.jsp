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
		if (confirm()) {

			try {

				var xmlhttp;
				if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
					xmlhttp = new XMLHttpRequest();
				} else {// code for IE6, IE5
					xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
				}
				xmlhttp.onreadystatechange = function() {
					if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
						document.getElementById("EmployeeDetails").innerHTML = xmlhttp.responseText;
					}
				}
				xmlhttp.open("POST", "GetEmployeeDataFromSubscription?employeeID="
						+ document.SearchForm.employeeID.value + "&firstName="
						+ document.SearchForm.firstName.value + "&lastName="
						+ document.SearchForm.lastName.value, true);
				xmlhttp.send();
			} catch (e) {
				alert(e.message);
			}

		} else {

			alert("Please type texts in search fields");
			//	alert("No records found !");

		}

	}

	function confirm() {
		var flag = true;

		if ($("input[name=employeeID]").val().trim() == ""
				&& $("input[name=firstName]").val().trim() == ""
				&& $("input[name=lastName]").val().trim() == "") {
			flag = false;

		}

		return flag;

	}

	function onsubmitfn() {			
		loadEmployeeDetails();
		return false;
	}
	function selectRow(id) {
		var source=document.getElementById("source").value;
		if(source=="cancel")
			{
			var name = document.getElementById("name-" + id).value;
			var url="transadmin_subscriptioncancel.jsp?employeeId="+id+"&name="+name;
	        var params="scrollbars=yes,resizable=yes";
	        size="height=100%,width=100%,"+params;
	        opener.window.close();
	        newwindow=window.open(url,'name',size);
	        //self.close();
	        // if (window.focus) {newwindow.focus();}				
			}
		else
	{		
		var name = document.getElementById("name-" + id).value;
		opener.document.getElementById("employeeID").value = id;
		opener.document.getElementById("employeeName").value = name;
		try{
			
			opener.document.getElementById("addresstd").innerHTML=document.getElementById("address-" + id).value;
			 
			setSubscriptionDetails(id);
		}catch(e)
		{
			alert(e);
		}
	}
		
	}
		
	
	
	function setSubscriptionDetails(id)
	{
		try {
			
	
			var xmlhttp;
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					var data = xmlhttp.responseText;
				 //	alert('' + data);
							
					var dataControls = data.split("|");
					
					var fromDateValue=dataControls[0].split("-");
				opener.document.getElementById("fromDate").value=fromDateValue[2]+ "/"+ fromDateValue[1] + "/" + fromDateValue[0];
		    	opener.document.getElementById("supervisorName1").value=dataControls[1];
				opener.document.getElementById("supervisorID1").value=dataControls[2];				
			        opener.document.getElementById("supervisorName2").value=dataControls[3];
					opener.document.getElementById("supervisorID2").value=dataControls[4];
					opener.document.getElementById("area").value=dataControls[5];
					opener.document.getElementById("place").value=dataControls[6];
					opener.document.getElementById("landmark").value=dataControls[7];
					opener.document.getElementById("landMarkID").value=dataControls[8];
					opener.document.getElementById("site").value=dataControls[9];
					opener.document.getElementById("contactNo").value=dataControls[10];
					opener.document.getElementById("subscriptionId").value=dataControls[11];
					
					window.close();
					//alert(data);
				}
			}
			
			xmlhttp.open("POST", "GetLastSubscriptionDetails_Ajax?employeeID="
					+ id , true);
			xmlhttp.send();
		} catch (e) {
			alert(e.message);
		}
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search Manager</title>
</head>
<body>
	<%
String source=request.getParameter("source");
%>
	<form name="SearchForm" action="" onsubmit="return onsubmitfn()">


		<table border="0">
			<%
if(source!=null)
{%>
			<input type="hidden" name="source" id="source" value="cancel" />
			<%
}
else
{%>
			<input type="hidden" name="source" id="source" value="modify" />
			<%}%>
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
					value="Search" /></td>
				<td></td>
			</tr>


		</table>
		<div id="EmployeeDetails"></div>

	</form>

</body>
</html>




