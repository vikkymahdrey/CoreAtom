<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
</head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<body>
	<%
 
String date=request.getParameter("date");

        String mimeType = "application/vnd.ms-excel";        
        response.setContentType(mimeType);        
        String fname = new Date().toString();
        response.setHeader("Content-Disposition", "inline; filename = " + fname + ".xls");        
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Pragma", "public");
        response.setHeader("Expires", "Mon, 1 Jan 1995 05:00:00 GMT"); 
	 OtherDao ob1= OtherDao.getInstance();
%>
	<%=ob1.getEmployeesNotSccheduledforTomorrow(OtherFunctions.changeDateFromatToIso(date))%>
</body>
</html>
