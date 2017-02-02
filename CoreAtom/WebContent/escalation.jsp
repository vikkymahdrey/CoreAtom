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
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>

</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level1AID" id="level1AID"><input type='text'  readonly name="level1Aname" id="level1Aname" onclick="showPopup('escalationSearch.jsp?param=level1A')"></td>
 <td><input type='hidden' name="Level1VID" id="level1VID"><input type='text'  readonly name="level1Vname" id="level1Vname" onclick="showPopup('escalationSearch.jsp?param=level1V')"></td>
 
</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level1Atime" id="level1Atime"><%=OtherFunctions.FullminutesOptions()%></select></td> 
<td><select name="Level1Vtime" id="level1Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>

</tr>
<tr>
<th colspan="4">Level 1</th>
</tr>
<tr>
<td></td>
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>
</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level2AID" id="level2AID"><input type='text'  readonly name="level2Aname" id="level2Aname" onclick="showPopup('escalationSearch.jsp?param=level2A')"></td>
<td><input type='hidden' name="Level2VID" id="level2VID"><input type='text'  readonly name="level2Vname" id="level2Vname" onclick="showPopup('escalationSearch.jsp?param=level2V')"></td>
</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level2Atime" id="level2Atime"><%=OtherFunctions.FullminutesOptions()%></select></td>
<td><select name="Level2Vtime" id="level2Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>
</tr>
<tr>
<th colspan="4">Level 1</th>
</tr>
<tr>
<td></td>
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>
</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level3AID" id="level3AID"><input type='text'  readonly name="level3Aname" id="level3Aname" onclick="showPopup('escalationSearch.jsp?param=level3A')"></td>
<td><input type='hidden' name="Level3VID" id="level3VID"><input type='text'  readonly name="level3Vname" id="level3Vname" onclick="showPopup('escalationSearch.jsp?param=level3V')"></td>

</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level3Atime" id="level3Atime"><%=OtherFunctions.FullminutesOptions()%></select></td>
<td><select name="Level3Vtime" id="level3Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>
</tr>
<tr>
<th colspan="4">Level 1</th>
</tr>
<tr>
<td></td>
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>

</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level4AID" id="level4AID"><input type='text'  readonly name="level4Aname" id="level4Aname" onclick="showPopup('escalationSearch.jsp?param=level4A')"></td>
 <td><input type='hidden' name="Level4VID" id="level4VID"><input type='text'  readonly name="level4Vname" id="level4Vname" onclick="showPopup('escalationSearch.jsp?param=level4V')"></td>
 
</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level4Atime" id="level4Atime"><%=OtherFunctions.FullminutesOptions()%></select></td> 
<td><select name="Level4Vtime" id="level4Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>

</tr>
<tr>
<th colspan="4">Level 1</th>
</tr>
<tr>
<td></td>
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>
</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level5AID" id="level5AID"><input type='text'  readonly name="level5Aname" id="level5Aname" onclick="showPopup('escalationSearch.jsp?param=level5A')"></td>
<td><input type='hidden' name="Level5VID" id="level5VID"><input type='text'  readonly name="level5Vname" id="level5Vname" onclick="showPopup('escalationSearch.jsp?param=level5V')"></td>
</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level5Atime" id="level5Atime"><%=OtherFunctions.FullminutesOptions()%></select></td>
<td><select name="Level5Vtime" id="level5Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>
</tr>
<tr>
<th colspan="4">Level 2</th>
</tr>
<tr>
<td></td>
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>
</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level6AID" id="level6AID"><input type='text'  readonly name="level6Aname" id="level6Aname" onclick="showPopup('escalationSearch.jsp?param=level6A')"></td>
<td><input type='hidden' name="Level6VID" id="level6VID"><input type='text'  readonly name="level6Vname" id="level6Vname" onclick="showPopup('escalationSearch.jsp?param=level6V')"></td>

</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level6Atime" id="level6Atime"><%=OtherFunctions.FullminutesOptions()%></select></td>
<td><select name="Level6Vtime" id="level6Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>
</tr>
<tr>
<th colspan="4">Level 2</th>
</tr>
<tr>
<td></td>
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>

</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level7AID" id="level7AID"><input type='text'  readonly name="level7Aname" id="level7Aname" onclick="showPopup('escalationSearch.jsp?param=level7A')"></td>
 <td><input type='hidden' name="Level7VID" id="level7VID"><input type='text'  readonly name="level7Vname" id="level7Vname" onclick="showPopup('escalationSearch.jsp?param=level7V')"></td>
 
</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level7Atime" id="level7Atime"><%=OtherFunctions.FullminutesOptions()%></select></td> 
<td><select name="Level7Vtime" id="level7Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>

</tr>
<tr>
<th colspan="4">Level 2</th>
</tr>
<tr>
<td></td>
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>
</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level8AID" id="level8AID"><input type='text'  readonly name="level8Aname" id="level8Aname" onclick="showPopup('escalationSearch.jsp?param=level8A')"></td>
<td><input type='hidden' name="Level8VID" id="level8VID"><input type='text'  readonly name="level8Vname" id="level8Vname" onclick="showPopup('escalationSearch.jsp?param=level8V')"></td>
</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level8Atime" id="level8Atime"><%=OtherFunctions.FullminutesOptions()%></select></td>
<td><select name="Level8Vtime" id="level8Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>
</tr>
<tr>
<th colspan="4">Level 2</th>
</tr>
<tr>
<td></td>
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>
</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level9AID" id="level9AID"><input type='text'  readonly name="level9Aname" id="level9Aname" onclick="showPopup('escalationSearch.jsp?param=level9A')"></td>
<td><input type='hidden' name="Level9VID" id="level9VID"><input type='text'  readonly name="level9Vname" id="level9Vname" onclick="showPopup('escalationSearch.jsp?param=level9V')"></td>

</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level9Atime" id="level9Atime"><%=OtherFunctions.FullminutesOptions()%></select></td>
<td><select name="Level9Vtime" id="level9Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>
</tr>
<tr>
<th colspan="4">Level 2</th>
</tr>
<tr>
<td></td>
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>

</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level10AID" id="level10AID"><input type='text'  readonly name="level10Aname" id="level10Aname" onclick="showPopup('escalationSearch.jsp?param=level10A')"></td>
 <td><input type='hidden' name="Level10VID" id="level10VID"><input type='text'  readonly name="level10Vname" id="level10Vname" onclick="showPopup('escalationSearch.jsp?param=level10V')"></td>
 
</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level10Atime" id="level10Atime"><%=OtherFunctions.FullminutesOptions()%></select></td> 
<td><select name="Level10Vtime" id="level10Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>

</tr>
<tr>
<th colspan="4">Level 3</th>
</tr>
<tr>
<td></td>
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>
</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level11AID" id="level11AID"><input type='text'  readonly name="level11Aname" id="level11Aname" onclick="showPopup('escalationSearch.jsp?param=level11A')"></td>
<td><input type='hidden' name="Level11VID" id="level11VID"><input type='text'  readonly name="level11Vname" id="level11Vname" onclick="showPopup('escalationSearch.jsp?param=level11V')"></td>

</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level11Atime" id="level11Atime"><%=OtherFunctions.FullminutesOptions()%></select></td>
<td><select name="Level11Vtime" id="level11Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>
</tr>
<tr>
<th colspan="4">Level 3</th>
</tr>
<tr>
<td></td>
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>

</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level12AID" id="level12AID"><input type='text'  readonly name="level12Aname" id="level12Aname" onclick="showPopup('escalationSearch.jsp?param=level12A')"></td>
 <td><input type='hidden' name="Level12VID" id="level12VID"><input type='text'  readonly name="level12Vname" id="level12Vname" onclick="showPopup('escalationSearch.jsp?param=level12V')"></td> 
</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level12Atime" id="level12Atime"><%=OtherFunctions.FullminutesOptions()%></select></td> 
<td><select name="Level12Vtime" id="level12Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>

</tr>
<tr>
<th colspan="4">Level 3</th>
</tr>
<tr>
<td></td>
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>
</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level13AID" id="level13AID"><input type='text'  readonly name="level13Aname" id="level13Aname" onclick="showPopup('escalationSearch.jsp?param=level13A')"></td>
<td><input type='hidden' name="Level13VID" id="level13VID"><input type='text'  readonly name="level13Vname" id="level13Vname" onclick="showPopup('escalationSearch.jsp?param=level13V')"></td>
</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level13Atime" id="level13Atime"><%=OtherFunctions.FullminutesOptions()%></select></td>
<td><select name="Level13Vtime" id="level13Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>
</tr>
<tr>
<th colspan="4">Level 3</th>
</tr>
<tr>
<td></td>
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>
</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level14AID" id="level14AID"><input type='text'  readonly name="level14Aname" id="level14Aname" onclick="showPopup('escalationSearch.jsp?param=level14A')"></td>
<td><input type='hidden' name="Level14VID" id="level14VID"><input type='text'  readonly name="level14Vname" id="level14Vname" onclick="showPopup('escalationSearch.jsp?param=level14V')"></td>

</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level14Atime" id="level14Atime"><%=OtherFunctions.FullminutesOptions()%></select></td>
<td><select name="Level14Vtime" id="level14Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>
</tr>
<tr>
<th colspan="4">Level 3</th>
</tr>
<tr>
<td></td>
<td><b>1st Person</b></td>
<td><b>2nd person</b></td>
</tr>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="Level15AID" id="level15AID"><input type='text'  readonly name="level15Aname" id="level15Aname" onclick="showPopup('escalationSearch.jsp?param=level15A')"></td>
<td><input type='hidden' name="Level15VID" id="level15VID"><input type='text'  readonly name="level15Vname" id="level15Vname" onclick="showPopup('escalationSearch.jsp?param=level15V')"></td>

</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="Level15Atime" id="level15Atime"><%=OtherFunctions.FullminutesOptions()%></select></td>
<td><select name="Level15Vtime" id="level15Vtime"><%=OtherFunctions.FullminutesOptions()%></select></td>
</tr>
<tr><td></td><td colspan="2"><input type="submit" value="Add" class="formbutton">&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" value="Reset" class="formbutton"></td><td></td></tr>
</table>
</form>
</div>
</div>
<%@include file="Footer.jsp"%>
</body>

</html>