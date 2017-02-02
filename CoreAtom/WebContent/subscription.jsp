<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.dao.SettingsDoa"%>
<%@page import="com.agiledge.atom.dto.SettingsDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<script type="text/javascript">
		function showPopup(chkbx, tpttype) {
			if (tpttype != "adhoc") {
				chkbx.checked = false;
				url = "landmarkSearchForRegister.jsp?transport=" + tpttype + "";
				var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
				size = "height=450,width=600,top=200,left=300," + params;
				var site = document.getElementById("site").value;
				url += "&site=" + site;
				newwindow = window.open(url, 'name', size);
				if (window.focus) {
					newwindow.focus();
				}
			}
		}
	</script>
	<%
		long empid = 0;

		String employeeId = OtherFunctions.checkUser(session);

		empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<%
		OtherDao ob = null;
		ob = OtherDao.getInstance();
		out.println(ob.getEmployeeDet(empid));
		String empSite = (new EmployeeService()
				.getEmployeeAccurate(employeeId)).getSite();
		ArrayList<SettingsDTO> transportType = new SettingsDoa()
				.getSettingsStrings(SettingsConstant.TRANSPORT,
						SettingsConstant.TYPE);
	%>
	<%
		if (!ob.isRegisterd(employeeId)) {
	%>

	<form id="regietremployee" action="RegisterEmployee" method="post">


		<table>
			<input type="hidden" name="site" id="site" value="<%=empSite%>" />
			<%
				for (SettingsDTO dto : transportType) {
			%>

			<tr>
				<td>&nbsp;</td>
				<td class="checkboxtd"><input type="checkbox"
					value="<%=dto.getKeyValue()%>" name="<%=dto.getKeyValue()%>"
					id="<%=dto.getKeyValue()%>"
					onclick="showPopup(this,'<%=dto.getKeyValue()%>')" /><%=dto.getValue()%>
					<input type="hidden" id="landMarkID<%=dto.getKeyValue()%>"
					name="landMarkID<%=dto.getKeyValue()%>" /></td>
				<td id="apl<%=dto.getKeyValue()%>"></td>
			</tr>
			<%
				}
			%>

		</table>

		<p>
			<input type="submit" value="Register" class="formbutton" />
		</p>
	</form>
	<%
		} else {
	%>
	<h5>Already registered</h5>
	<%
		}
	%>
</body>
</html>