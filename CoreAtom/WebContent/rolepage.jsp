<%-- 
    Document   : modify_trip
    Created on : Oct 31, 2012, 6:58:21 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.usermanagement.dto.ViewManagementStatus"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Role Page</title>

<script type="text/javascript">
	function submitForm(form) {
		var selectedshift = document.getElementById("selectedpage");		
			var selectedshiftLength = document.getElementById("selectedpage").options.length;
			for ( var i = 0; i < selectedshiftLength; i++) {
				selectedshift.options[i].selected = true;
			}
			form.submit();
		}	

	function listMoveRight() {
		var page = document.getElementById("page");
		var selectedpage = document.getElementById("selectedpage");
		var optionSelected = page.selectedIndex;
		var optionNew = document.createElement('option');
		optionNew.value = page.options[optionSelected].value;
		optionNew.text = page.options[optionSelected].innerHTML;
		try {
			selectedpage.add(optionNew, null);
		} catch (e) {
			selectedpage.add(optionNew);
		}
		page.options[optionSelected] = null;
	}
	function listMoveLeft() {
		var selectedpage = document.getElementById("selectedpage");
		var page = document.getElementById("page");
		var optionSelected = selectedpage.selectedIndex;
		var optionNew = document.createElement('option');
		optionNew.value = selectedpage.options[optionSelected].value;
		optionNew.text = selectedpage.options[optionSelected].innerHTML;
		try {
			page.add(optionNew, null);
		} catch (e) {
			page.add(optionNew);
		}
		selectedpage.options[optionSelected] = null;
	}
	function getPages()
	{
	var usertype=document.getElementById("usertype").value;
	var url="SetGetRolePage?usertype="+usertype;
    xmlHttp=GetXmlHttpObject();
    if(xmlHttp==null)
    	{
    	   alert ("Browser does not support HTTP Request");
           return
       }                    
       xmlHttp.onreadystatechange=setPage;	
       xmlHttp.open("POST",url,true);                
       xmlHttp.send(null);       
	}
	function setPage()
	{
		 if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
         {			 
		var returnText=xmlHttp.responseText;
		var splittedData=returnText.split("|");
        document.getElementById("pagetd").innerHTML='<select name="page" id="page" multiple="multiple">'+splittedData[0]+'</select>';
		document.getElementById("selectedpagetd").innerHTML='<select	name="selectedpage" id="selectedpage" multiple="multiple">'+splittedData[1]+'</select>';
         }
	}
	function GetXmlHttpObject()
    {
        var xmlHttp=null;
        if (window.XMLHttpRequest) 
        {
            xmlHttp=new XMLHttpRequest();
        }                
        else if (window.ActiveXObject) 
        { 
            xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
        return xmlHttp;
    }
</script>
</head>
<body>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">
			<%
				long empid = 0;
				String employeeId = OtherFunctions.checkUser(session);
				empid = Long.parseLong(employeeId);
			%>
			<%
			try {
				OtherDao ob = OtherDao.getInstance();
				ArrayList<String> pages = new ArrayList<String>();
			System.out.println(" APP" + application.getRealPath("/"));
					File folder = new File(application.getRealPath("/"));
					File[] listOfFiles = folder.listFiles();

					for (int i = 0; i < listOfFiles.length; i++) {
						if (listOfFiles[i].isFile())
							pages.add(listOfFiles[i].getName());
					}
					ob.insertPageToDatabase(pages, ViewManagementStatus.PAGE);
				} catch (Exception e) {

					System.out.println("error" + e);
				}
			%>
			<p></p>
			<h3>Role Page</h3>
			<hr>
			<form action="SetGetRolePage" name="setSiteShiftform"
				id="setRouteRuleform" method="POST">
				<table border="0" width="100%">
					<tbody>
						<tr>
							<td align="center">User</td>
							<td>&nbsp;</td>
							<td align="left"><select name="usertype" id="usertype"
								onchange="getPages()">
									<option>Select</option>
									<option value="emp">Employee</option>
									<option value="hrm"><%=SettingsConstant.hrm %></option>
									<option value="tm">Transport Co-ordinator</option>
									<option value="ta">Transport Administrator</option>
									<option value="v">Vendor</option>
							</select></td>
						</tr>
						<tr>
							<td align="center"><b>All Role</b></td>
							<td>&nbsp;</td>
							<td><b>User Role</b></td>
						</tr>
						<tr>
							<td rowspan="5" width="40%" align="center" id="pagetd"><select
								name="page" id="page" multiple="multiple">
							</select></td>
							<td width="10%" rowspan="5">
								<p>
									<input type="button" class="formbutton" name="right"
										value="&rArr;" onclick="listMoveRight()" />
								</p>
								<p>
									<input type="button" class="formbutton" name="left"
										value="&lArr;" onclick="listMoveLeft()" />
								</p>
							</td>
							<td rowspan="5" id="selectedpagetd"><select
								name="selectedpage" id="selectedpage" multiple="multiple">
							</select></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td colspan="2"><input type="button" id="submitbtn"
								name="submitbtn" class="formbutton" value="Submit"
								onclick="submitForm(this.form)"></td>
							<td>&nbsp;</td>
						</tr>
					</tbody>
				</table>
			</form>

			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>
