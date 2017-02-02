<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Escalation Matrix</title>
</head>
<body>
<script type="text/javascript">
	function showPopup(url) {
		var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		var size = "height=450,width=700,top=200,left=300," + params;		
		newwindow = window.open(url, 'name', size);
		if (window.focus) {
			newwindow.focus();
		}
	}
</script>

<%
long empid = 0;
String employeeId = OtherFunctions.checkUser(session);
if (employeeId == null || employeeId.equals("null")) {
	String param = request.getServletPath().substring(1) + "___"
			+ request.getQueryString();
	response.sendRedirect("index.jsp?page=" + param);
} else {
	empid = Long.parseLong(employeeId);
}
%>
<%@include file="Header.jsp"%>
<hr>
<div id="body">
		<div class="content">
		<form name="escalationForm" action="EscalationMatrix" method="post">
<table>

<tr>
<th colspan="4">Level 1</th>
</tr>
<tr>
<td></td>
<td><b>Admin</b></td>
<!-- <td><b>Security</b></td> -->
<td><b>Vendor</b></td>

</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level1AID" id="level1AID"><input type='text'  readonly name="level1Aname" id="level1Aname" onclick="showPopup('escalationSearch.jsp?param=level1A')"></td>
<td><input type='hidden' name="Level1SID" id="level1SID"><input type='text'  readonly name="level1Sname" id="level1Sname" onclick="showPopup('escalationSearch.jsp?param=level1S')"></td>
 <td><input type='hidden' name="Level1VID" id="level1VID"><input type='text'  readonly name="level1Vname" id="level1Vname" onclick="showPopup('escalationSearch.jsp?param=level1V')"></td>
 
</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level1Atime" id="level1Atime"><%=OtherFunctions.FullminutesOptions()%></select></td>
<td><select name="Level1Stime" id="level1Stime"><%=OtherFunctions.FullminutesOptions()%></select></td> 
<td><select name="Level1Vtime" id="level1Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>

</tr>
<tr>
<th colspan="4">Level 2</th>
</tr>
<tr>
<td></td>
<td><b>Admin</b></td>
<!-- <td><b>Security</b></td> -->
<td><b>Vendor</b></td>
</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level2AID" id="level2AID"><input type='text'  readonly name="level2Aname" id="level2Aname" onclick="showPopup('escalationSearch.jsp?param=level2A')"></td>
<td><input type='hidden' name="Level2SID" id="level2SID"><input type='text'  readonly name="level2Sname" id="level2Sname" onclick="showPopup('escalationSearch.jsp?param=level2S')"></td>
<td><input type='hidden' name="Level2VID" id="level2VID"><input type='text'  readonly name="level2Vname" id="level2Vname" onclick="showPopup('escalationSearch.jsp?param=level2V')"></td>
</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level2Atime" id="level2Atime"><%=OtherFunctions.FullminutesOptions()%></select></td>
 <td><select name="Level2Stime" id="level2Stime"><%=OtherFunctions.FullminutesOptions()%></select></td>
<td><select name="Level2Vtime" id="level2Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>
</tr>
<tr>
<th colspan="4">Level 3</th>
</tr>
<tr>
<td></td>
<td><b>Admin</b></td>
<td><b>Security</b></td>
<td><b>Vendor</b></td>

</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level3AID" id="level3AID"><input type='text'  readonly name="level3Aname" id="level3Aname" onclick="showPopup('escalationSearch.jsp?param=level3A')"></td>
<td><input type='hidden' name="Level3SID" id="level3SID"><input type='text'  readonly name="level3Sname" id="level3Sname" onclick="showPopup('escalationSearch.jsp?param=level3S')"></td>
<td><input type='hidden' name="Level3VID" id="level3VID"><input type='text'  readonly name="level3Vname" id="level3Vname" onclick="showPopup('escalationSearch.jsp?param=level3V')"></td>

</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level3Atime" id="level3Atime"><%=OtherFunctions.FullminutesOptions()%></select></td>
<td><select name="Level3Stime" id="level3Stime"><%=OtherFunctions.FullminutesOptions()%></select></td>
<td><select name="Level3Vtime" id="level3Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>
</tr>
<tr><td></td><td colspan="2"><input type="submit" value="Add" class="formbutton">&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" value="Reset" class="formbutton"></td><td></td></tr>
</table>
</form>
</div>
</div>
<%@include file="Footer.jsp"%>
</body>

</html>