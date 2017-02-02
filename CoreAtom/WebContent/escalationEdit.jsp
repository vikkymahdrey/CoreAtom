<%@page import="com.agiledge.atom.dao.EscalationMatrixDao"%>
<%@page import="com.agiledge.atom.dto.EscalationMatrixDto"%>
<%@page import="java.util.ArrayList"%>
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
		
		<%
		ArrayList<EscalationMatrixDto> matrixDtos=new EscalationMatrixDao().getEscalationMatrix();
		try{
		%>
		<form name="escalationForm" action="EscalationMatrix" method="post">
<table>
<%for(int i=0;i<matrixDtos.size();i++) {%>
<tr>
<th colspan="4"><%=matrixDtos.get(i).getLevel()%></th>
</tr>
<tr>
<td>&nbsp;</td>
<td><b><%=matrixDtos.get(i).getGroup() %></b></td>
<td><b><%=matrixDtos.get(i+1).getGroup() %></b></td>
<tr>
<td><b>Name</b></td>
<td><input type='hidden' name="<%=matrixDtos.get(i).getLevel()%>AID" id="<%=matrixDtos.get(i).getLevel()%>AID" value="<%=matrixDtos.get(i).getPersonId()%>">
<input type='text'  readonly name="<%=matrixDtos.get(i).getLevel()%>Aname" id="<%=matrixDtos.get(i).getLevel()%>Aname" value="<%=matrixDtos.get(i).getPersonName()%>" onclick="showPopup('escalationSearch.jsp?param=<%=matrixDtos.get(i).getLevel()%>A')"></td>
<td><input type='hidden' name="<%=matrixDtos.get(i).getLevel()%>VID" id="<%=matrixDtos.get(i).getLevel()%>VID" value="<%=matrixDtos.get(i+1).getPersonId()%>">
<input type='text'  readonly name="<%=matrixDtos.get(i).getLevel()%>Vname" id="<%=matrixDtos.get(i).getLevel()%>Vname" value="<%=matrixDtos.get(i+1).getPersonName()%>" onclick="showPopup('escalationSearch.jsp?param=<%=matrixDtos.get(i).getLevel()%>V')"></td>
</tr>
<tr>
<td><b>Time Gap(mns)</b></td>
<td><select name="<%=matrixDtos.get(i).getLevel()%>Atime" id="<%=matrixDtos.get(i).getLevel()%>Atime"><%=OtherFunctions.FullminutesOptions(matrixDtos.get(i).getTimeSlot(),null)%></select></td>
<td><select name="<%=matrixDtos.get(i).getLevel()%>Vtime" id="<%=matrixDtos.get(i).getLevel()%>Vtime"><%=OtherFunctions.FullminutesOptions(matrixDtos.get(i+1).getTimeSlot(),null)%></select></td>
</tr>
<tr>
<%
i=i+1;
} %>
<tr><td></td><td colspan="2"><input type="submit" value="Submit" class="formbutton">&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" value="Reset" class="formbutton"></td><td></td></tr>
</table>
</form>
<%}catch(Exception e) {
	System.out.println("aaaaaaaaaa"+e);
}%>
</div>
</div>
<%@include file="Footer.jsp"%>
</body>
</html>