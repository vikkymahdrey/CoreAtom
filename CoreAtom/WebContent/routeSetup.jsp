<%-- 
    Document   : modify_trip
    Created on : Oct 31, 2012, 6:58:21 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dao.LogTimeDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Combained Route</title>

<script type="text/javascript">
	function submitForm(form) {
		var selectedshift = document.getElementById("selectedshift");		
			var selectedshiftLength = document.getElementById("selectedshift").options.length;
			for ( var i = 0; i < selectedshiftLength; i++) {
				selectedshift.options[i].selected = true;
			}
			form.submit();
		}	

	function listMoveRight() {
		var shift = document.getElementById("shift");
		var selectedshift = document.getElementById("selectedshift");
		var optionSelected = shift.selectedIndex;
		var optionNew = document.createElement('option');
		optionNew.value = shift.options[optionSelected].value;
		optionNew.text = shift.options[optionSelected].innerHTML;
		try {
			selectedshift.add(optionNew, null);
		} catch (e) {
			selectedshift.add(optionNew);
		}
		shift.options[optionSelected] = null;
	}
	function listMoveLeft() {
		var selectedshift = document.getElementById("selectedshift");
		var shift = document.getElementById("shift");
		var optionSelected = selectedshift.selectedIndex;
		var optionNew = document.createElement('option');
		optionNew.value = selectedshift.options[optionSelected].value;
		optionNew.text = selectedshift.options[optionSelected].innerHTML;
		try {
			shift.add(optionNew, null);
		} catch (e) {
			shift.add(optionNew);
		}
		selectedshift.options[optionSelected] = null;
	}
	function setShifts()
	{
	var siteId=document.getElementById("siteId").value;
	var url="GetSiteShift?siteId="+siteId;	
    xmlHttp=GetXmlHttpObject();
    if(xmlHttp==null)
    	{
    	   alert ("Browser does not support HTTP Request");
           return
       }                    
       xmlHttp.onreadystatechange=setShiftTime;	
       xmlHttp.open("GET",url,true);                
       xmlHttp.send(null);       
	}
	function setShiftTime()
	{
		 if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
         {
			 
		var returnText=xmlHttp.responseText;
		var splittedData=returnText.split("|");
        document.getElementById("shifttd").innerHTML=splittedData[1];		
		document.getElementById("selectshifttd").innerHTML=splittedData[2];
		if(splittedData[3]==1)
			{
			document.getElementById("weekoffstatus").checked=true;
			}
		else
			{
			document.getElementById("weekoffstatus").checked=false;
			}
		
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
	
	<div id="body">
		<div class="content">
			<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
			empid = Long.parseLong(employeeId);						
	//	ArrayList<LogTimeDto> primaryRouteLogTimeDtos = new LogTimeDao().getAllPrimaryRouteLogtime();
	//	ArrayList<LogTimeDto> combainedRouteLogTimeDtos = new LogTimeDao().getAllCombainedRouteLogtime(); 
		List<SiteDto> siteDtos = new SiteDao().getSites();
	%>
<%@include file="Header.jsp"%>
			<p></p>
			<h3>Combined Route Shift</h3>
			<hr>
			<form action="SetRouteRule" name="setRouteRuleform"
				id="setRouteRuleform" method="POST">
				<table border="0" width="100%">
					<tbody>
						<tr>
							<td align="center">Site</td>
							<td>&nbsp;</td>
							<td align="left"><select name="siteId" id="siteId" onchange="setShifts()">
							<option>Select</option>
									<%for(SiteDto sitedto: siteDtos) {%>
									<option value=<%=sitedto.getId()%>><%=sitedto.getName()%></option>
									<%}%>
							</select></td>
						</tr>
						<tr>
							<td align="center">Set all shift for week off days</td>
							<td>&nbsp;</td>
							<td><input type="checkbox" id="weekoffstatus"
								name="weekoffstatus"></td>
						</tr>
						<tr>
							<td align="center"><b>General Shift</b></td>
							<td>&nbsp;</td>
							<td><b>Combined Route using Shift</b></td>
						</tr>
						<tr>

							<td rowspan="5" width="40%" align="center" id="shifttd"><select
								name="shift" id="shift" multiple="multiple">
									
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
							<td rowspan="5" id="selectshifttd"><select name="selectedshift"
								id="selectedshift" multiple="multiple">
									
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
