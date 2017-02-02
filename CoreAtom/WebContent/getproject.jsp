<%-- 
    Document   : newjsp
    Created on : Oct 17, 2012, 2:57:07 PM
    Author     : muhammad
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<script type="text/javascript">
	function getProjets() {

		var projectCode = document.getElementById("projectCode").value;
		var projectName = document.getElementById("projectName").value;
		var type = $("select[name=selector]").val();
			if (projectCode.length >= 1) {
				var url = "GetProject?projectCode=" + projectCode+"&type="+type;
			} else if (projectName.length >= 1) {

				var url = "GetProject?projectName=" + projectName+"&type="+type;
			} else {
				alert("Please enter Project Code or Project Name");
				return false;
			}
			xmlHttp = GetXmlHttpObject();
			if (xmlHttp == null) {
				alert("Browser does not support HTTP Request");
				return				
			}
			xmlHttp.onreadystatechange = setProject;
			xmlHttp.open("GET", url, true);
			xmlHttp.send(null);
			return false;
		
	}

	function GetXmlHttpObject() {
		var xmlHttp = null;
		if (window.XMLHttpRequest) {
			xmlHttp = new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}

		return xmlHttp;
	}

	function setProject() {
		if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete") {
			var type = $("select[name=selector]").val();
			var returnText = xmlHttp.responseText;
			var projectList = document.getElementById("projectstable");
			projectList.innerHTML = "<table><thead><tr><th>Project/Business Code</th><th>Description</th><th></th></tr>"+returnText+"</table>"
			returnText;
			if (returnText == "") {
				if (type == "project") {
					alert("No projects found");
				} else {
					alert("No Units found");
				}
			}

		}
	}
	function closeWindow(project_Unit_Id,project)
	{       
	    try{
		var success=0;
	    if(document.getElementById("serialNo")==null)
	        {
	      opener.document.getElementById("project").value =project_Unit_Id; 	    	   	    
	    opener.document.getElementById("projectdesc").value =project;	
	    success=1;
	        }
	    else
	        {        
	    var serialNo=document.getElementById("serialNo").value;
	    var site=opener.document.getElementById("site").value;
	    opener.document.getElementById("projectdesc"+serialNo).value =project;
	    opener.document.getElementById("project"+serialNo).value =project_Unit_Id; 
		var projectSpecific= document.getElementById("projectSpecific").value;
		
		if(projectSpecific=="true")
			{
	    setProjectSpecificLogTime(project_Unit_Id, serialNo,site);
			}
		else{
			success=1;
		}	   		    
	        }
	    if(success==1)
	    	{
	    	window.close();
	    	}
	    	               
	                                          
	    }catch(e)
	    {
	    	
	    	alert(e+"Project Not Selected");
	    	}
	}
	
	
	function setProjectSpecificLogTime(id, serialNo,site)
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
				//	alert('tr'+xmlhttp.responseText);
					var data = xmlhttp.responseText;
				 	try{        						 
					var Times= data.split("_");
					var inTime=Times[0].split("&");
					var outTime=Times[1].split("&");
					var loginTimeSelect = opener.document.getElementById("logintime"+serialNo);
					
					loginTimeSelect.options.length=2;	
				    var logoutTimeSelect = opener.document.getElementById("logouttime"+serialNo);
				    logoutTimeSelect.options.length=2;    
				  loginTimeSelect.options[0].selected = true;
				logoutTimeSelect.options[0].selected = true;
					var optionNew = opener.document.createElement('option');
					  for(var i=1;i<inTime.length;i++)
						  {
						  optionNew = opener.document.createElement('option'); 
						optionNew.value =inTime[i];
						optionNew.text = inTime[i];
						try {
							loginTimeSelect.add(optionNew, null);
						} catch (e) {
							loginTimeSelect.add(optionNew);
						}
						  }        					  
					  var optionNew1 = opener.document.createElement('option');        					
					  for(var i=1;i<outTime.length;i++)
						  {
						  optionNew1 = opener.document.createElement('option'); 
						optionNew1.value =outTime[i];
						optionNew1.text = outTime[i];
						try {
							logoutTimeSelect.add(optionNew1, null);
						} catch (e) {
							logoutTimeSelect.add(optionNew1);
						}
						
						  }
					  	window.close();
				 	}catch(e)
				 	{
				 		
				 		//alert("Error in logtimes :" + e);
				 		window.close();
				 	}
					
				}
			
	
				
			};
			
			xmlhttp.open("POST", "GetProjectSpecificShiftTime_Ajax?projectId="
					+ id+"&site="+site , true);
			xmlhttp.send();
			
		} catch (e) {
			alert(e.message);
		}
	}
	 
	function updatefunctions() {
		var selected = $("select[name=selector]").val();
		if (selected == "unit") {
			$("td[id=projectnametext]").html("Unit Description");
			$("td[id=projectcodetext]").html("Unit Code");
		} else {
			$("td[id=projectnametext]").html("Project Name");
			$("td[id=projectcodetext]").html("Project Code");
		}
	}
</script>
</head>
<body>

	<%
		String serialNo = request.getParameter("srno");
		String projectSpecific = request.getParameter("projectSpecific");
	%>
	<form name="getProject" onsubmit="return getProjets()">

		<table>
			<tr>
				<td>Type</td>
				<td><select name="selector"><option value="project">Billable</option>
						<option value="unit">Non-Billable</option></select></td>
			</tr>
			<tr>
				<%
					if (serialNo != null) {
				%>
				<input type="hidden" value="<%=serialNo%>" id="serialNo" />
				<input type="hidden" value="<%=projectSpecific%>"
					id="projectSpecific" />
				<%
					}
				%>
				<td id="projectcodetext">Project Code</td>
				<td><input type="text" name="projectCode" id="projectCode" /></td>
			</tr>
			<tr>
				<td id="projectnametext">Project Name</td>
				<td><input type="text" name="projectName" id="projectName" /></td>
			</tr>
			<tr>
				<td></td>
				<td align="center"><input type="submit" class="formbutton"
					value="Search" /></td>
			</tr>
		</table>
		<div id="projectstable"></div>
	</form>
</body>
</html>
