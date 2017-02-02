<%-- 
    Document   : modify_trip
    Created on : Oct 31, 2012, 6:58:21 PM
    Author     : muhammad
--%>


<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="com.agiledge.atom.dto.APLDto"%>
<%@page import="com.agiledge.atom.service.APLService"%>
<%@page import="com.agiledge.atom.dao.BranchDao"%>
<%@page import="com.agiledge.atom.dto.BranchDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Add Route</title>

<script type="text/javascript">
	function submitForm(form) {
		try{
		var selectedAPL = document.getElementById("selectedapl");
		var routeName = document.getElementById("routeName");
		var routeSite = document.getElementById("routeSite");
		var selectedAPLLength = document.getElementById("selectedapl").options.length;
		if (routeName.value.length < 1) {
			alert("Route Name Should Not Blank");
		} else if (routeSite.value.length < 1) {
			alert("Site Should Not Blank");
		} else if (selectedAPLLength < 1) {
			alert("No  APL is selected");
		} else {
			for ( var i = 0; i < selectedAPLLength; i++) {
				selectedAPL.options[i].selected = true;
			}
			form.submit();
		}
		}catch(e)
		{aert(e);}
	}

	function listMoveRight() {
		var apl = document.getElementById("apl");
		var selectedAPL = document.getElementById("selectedapl");
		var optionSelected = apl.selectedIndex;
		var optionNew = document.createElement('option');
		optionNew.value = apl.options[optionSelected].value;
		optionNew.text = apl.options[optionSelected].innerHTML;
		try {
			selectedAPL.add(optionNew, null);
		} catch (e) {
			selectedAPL.add(optionNew);
		}
		apl.options[optionSelected] = null;
	}
	function listMoveLeft() {
		var selectedAPL = document.getElementById("selectedapl");
		var apl = document.getElementById("apl");
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
	function submitBranchForm() {
		document.getElementById("branchForm").submit();
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
				String location = request.getParameter("branchId");
				ArrayList<BranchDto> branchDtos = new BranchDao().getLocations();
			%>
			<form action="addShuttleRoute.jsp" name="branchForm" id="branchForm">
				<table>
					<tr>
						<td>Location</td>
						<td><select name="branchId" onchange="submitBranchForm()">
								<option>Select</option>
								<%
									for (BranchDto dto : branchDtos) {
										if (dto.getId().equals(location)) {
								%>
								<option value="<%=dto.getId()%>" selected="selected"><%=dto.getLocation()%></option>
								<%
									} else {
								%>
								<option value="<%=dto.getId()%>"><%=dto.getLocation()%></option>
								<%
									}
									}
								%>
						</select></td>
					</tr>
				</table>
			</form>
			<%
			
			ArrayList<APLDto> aplDtos = new APLService().getAllShuttleAPL(location);
				List<SiteDto> siteDtos = new SiteDao().getSites(location);
				//ArrayList<RouteDto> routeTypedtos = new RouteService()
					//	.getRoutetypes();
			%>

			<h3>Add Route</h3>

			<form action="AddRoute" name="addrouteform" id="addrouteform"
				method="POST">
				<table border="0" width="100%">
					<tbody>
						<tr>
							<td>Existing APL</td>
							<td>&nbsp;</td>
							<td>APL added to Route</td>
						</tr>
						<tr>
							<td rowspan="5" width="40%"><select name="apl" id="apl"
								multiple="multiple">
									<%
										for (APLDto aplDto : aplDtos) {
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
										value="&lArr;" onclick="listMoveLeft()" />
								</p>
							</td>
							<td rowspan="5"><select name="selectedapl" id="selectedapl"
								multiple="multiple">

							</select></td>
						</tr>
						<tr>							
						</tr>
						<tr>							
						</tr>
						<tr>						
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						
						<tr>
							<td align="center">Route Name</td>
							<td align="left"><input type="text" id="routeName"
								name="routeName"></td>
						</tr>
						<tr>
							<td align="center">In/Out</td>
							<td align="left"  colspan="2"><select id="inOut" name="inOut">									
									<option value="BOTH">BOTH</option>
									<option value="IN">IN</option>
									<option value="OUT">OUT</option>									
							</select>
							First point will consider as source for OUT.Last Point will consider as source for IN							
							</td>
						</tr>	
						<tr>
							<td align="center">Site</td>
							<td align="left"><select id="routeSite" name="routeSite">
									<%
										for (SiteDto siteDto : siteDtos) {
									%>

									<option value="<%=siteDto.getId()%>"><%=siteDto.getName()%></option>
									<%
										}
									%>
							</select></td>
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
