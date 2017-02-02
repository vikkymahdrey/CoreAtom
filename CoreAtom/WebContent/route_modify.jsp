<%-- 
    Document   : modify_trip
    Created on : Oct 31, 2012, 6:58:21 PM
    Author     : muhammad
--%>

<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="com.agiledge.atom.service.APLService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="com.agiledge.atom.service.RouteService"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Modify Route</title>

<script type="text/javascript">
	function submitForm(form) {
		var selectedAPL = document.getElementById("routeApl");

		var selectedAPLLength = document.getElementById("routeApl").options.length;
		for ( var i = 0; i < selectedAPLLength; i++) {
			selectedAPL.options[i].selected = true;
		}
		form.submit();
	}
	function moveDown() {
		var routeApl = document.getElementById("routeApl");
		try {
			if (routeApl.selectedIndex >= 0) {
				var optionNew = document.createElement('option');
				var optionSelected = routeApl.options[routeApl.selectedIndex];
				optionNew.text = "";
				try {
					routeApl.add(optionNew, optionSelected);
				} catch (e) {
					routeApl.add(optionNew, routeApl.selectedIndex);
				}
			}
		} catch (e) {
			alert(e);
		}

	}
	function changeDown() {
		var routeApl = document.getElementById("routeApl");
		try {
			if (routeApl.selectedIndex >= 0) {
				var optionSelected = routeApl.selectedIndex;
				var optionNewValue = routeApl.options[optionSelected+1].value;
				var optionNewText = routeApl.options[optionSelected+1].innerHTML;											
				routeApl.options[optionSelected+1].value = routeApl.options[optionSelected].value;
				routeApl.options[optionSelected+1].text = routeApl.options[optionSelected].innerHTML;
				routeApl.options[optionSelected].value =optionNewValue;
				routeApl.options[optionSelected].text=optionNewText;
				routeApl.selectedIndex=routeApl.selectedIndex+1;				
			}
		} catch (e) {
			//alert(e);
		}

	}function changeUp() {
		var routeApl = document.getElementById("routeApl");
		try {
			if (routeApl.selectedIndex > 0) {
				var optionSelected = routeApl.selectedIndex;
				var optionNewValue = routeApl.options[optionSelected-1].value;
				var optionNewText = routeApl.options[optionSelected-1].innerHTML;											
				routeApl.options[optionSelected-1].value = routeApl.options[optionSelected].value;
				routeApl.options[optionSelected-1].text = routeApl.options[optionSelected].innerHTML;
				routeApl.options[optionSelected].value =optionNewValue;
				routeApl.options[optionSelected].text=optionNewText;
				routeApl.selectedIndex=routeApl.selectedIndex-1;				
			}
		} catch (e) {
			//alert(e);
		}
	}

	function hideModifyBlock() {
		try {
			document.getElementById("modifyblock").style.display = 'none';
		} catch (e) {
			alert(e);
		}

	}
	function showModifyBlock() {
		try {
			document.getElementById("modifyblock").style.display = 'block';
		} catch (e) {
			alert(e);
		}

	}
	function listMoveRight() {
		var apl = document.getElementById("otherApl");
		var selectedAPL = document.getElementById("routeApl");
		var selectedAPLIterate;
		var optionSelected = apl.selectedIndex;
		var selectedAPLLength = selectedAPL.options.length;
		var flag = false;
		for ( var i = 0; i < selectedAPLLength; i++) {
			selectedAPLIterate = selectedAPL.options[i];
			if (selectedAPLIterate.value == null
					|| selectedAPLIterate.value == "") {
				selectedAPL.options[i].value = apl.options[optionSelected].value;
				selectedAPL.options[i].text = apl.options[optionSelected].innerHTML;
				flag = true;
				break;
			}
		}
		if (flag == false) {
			var optionNew = document.createElement('option');
			optionNew.value = apl.options[optionSelected].value;
			optionNew.text = apl.options[optionSelected].innerHTML;
			try {
				selectedAPL.add(optionNew, null);
			} catch (e) {
				selectedAPL.add(optionNew);
			}
		}
		apl.options[optionSelected] = null;
	}
	function listMoveLeft() {
		var selectedAPL = document.getElementById("routeApl");
		var apl = document.getElementById("otherApl");
		var optionSelected = selectedAPL.selectedIndex;
		var optionNew = document.createElement('option');
		optionNew.value = selectedAPL.options[optionSelected].value;
		optionNew.text = selectedAPL.options[optionSelected].innerHTML;
		try {
			apl.add(optionNew, null);
		} catch (e) {
			apl.add(optionNew);
		}
		selectedAPL.options[optionSelected] = null;
	}
</script>
</head>
<body onload="hideModifyBlock()">

	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);	
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<%	
		int routeId = Integer.parseInt(request.getParameter("routeId"));
		RouteService routeService = new RouteService();
		ArrayList<APLDto> aplNotInRoute = routeService
				.getAllAPLNotInRoute(routeId);
		ArrayList<RouteDto> routeAPL = routeService
				.getAllAPLInRoute(routeId);
	%>
	<div id="body">
		<div class="content">
			<h3>Route Details</h3>
			<div id="modifyblock">

				<form action="ModifyRoute" name="modifyRouteform"
					id="modifyRouteform" method="POST">
					<table border="0" width="100%">
						<tbody>
							<input type="hidden" value="<%=routeId%>" name="routeId">
							<tr>
								<td rowspan="5" width="40%"><select name="otherApl"
									id="otherApl" multiple="multiple" size="40">
										<%
											for (APLDto aplDto : aplNotInRoute) {
										%>
										<option value="<%=aplDto.getLandMarkID()%>">
											<%=aplDto.getArea() + ":" + aplDto.getPlace() + ":"
						+ aplDto.getLandMark()%>
										</option>
										<%
											}
										%>
								</select></td>
								<td width="10%" rowspan="5">
									<p>
										<input type="button" class="formbutton" name="right"
											value="&rArr;" onclick="listMoveRight()" />
									</p>
									<p>
										<input type="button" class="formbutton" name="left"
											value="&lArr;" onclick="listMoveLeft()" /> <input
											type="button" class="formbutton" name="down" value="&dArr;"
											onclick="moveDown()" />
									</p>

								</td>
								<td rowspan="5" valign="middle" width="40%" align="right"><select
									name="routeApl" id="routeApl" multiple="multiple" size="20">
										<%
											for (RouteDto routeDto : routeAPL) {
										%>
										<option value="<%=routeDto.getLandmarkId()%>">
											<%=routeDto.getArea() + ":" + routeDto.getPlace() + ":"
						+ routeDto.getLandmark()%>
										</option>
										<%
											}
										%>
								</select></td>
								<td rowspan="5" width="10%" align="left">
									<p>
										<input type="button" class="formbutton" name="right"
											value="&uarr;" onclick="changeUp()" />
									</p>
									<p>
										<input type="button" class="formbutton" name="left"
											value="&darr;" onclick="changeDown()" />
									</p>
								</td>

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
									name="submitbtn" class="formbutton" value="Update"
									onclick="submitForm(this.form)"></td>
								<td>&nbsp;</td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>


			<hr />
			<div align="center">
				<input type="button" class="formbutton" value="Modify"
					onclick="showModifyBlock()">&nbsp;&nbsp;&nbsp; <input
					type="button" class="formbutton" value="Back"
					onclick="javascript:history.go(-1);" />
			</div>
<iframe  style="float: right; width: 46%" height="500px" src="showRoute.jsp?routeId=<%=routeId%>" ></iframe>  
			<table>
				<thead>
					<tr>
						<th align="center">Area</th>
						<th align="center">Place</th>
						<th align="center">Landmark</th>
					</tr>
				</thead>
				<%
					for (RouteDto routeDto : routeAPL) {
				%>
				<tr>
					<td align="center"><%=routeDto.getArea()%></td>
					<td align="center"><%=routeDto.getPlace()%></td>
					<td align="center"><%=routeDto.getLandmark()%></td>
				</tr>
				<%
					}
				%>
			</table>


			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>
