<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="java.sql.*"%>
<%@page import="com.agiledge.atom.dto.*"%>
<%@page import="com.agiledge.atom.dao.*"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html>
<html>
<head>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="icon" href="images/agile.png" type="image/x-icon" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<title>Employee Details</title>
</head>
</head>
<body>
	<%
    int tripId= Integer.parseInt(request.getParameter("tripId"));
	
	VehicleBasedTripDao  report =new VehicleBasedTripDao();
	ArrayList<VehicleBasedTripDto> dtolist = new ArrayList<VehicleBasedTripDto>();

	 dtolist = report.getEmpDetails(tripId);     
	%>
                
                	
                
	
	<div id="body">
		<div class="content">
			<hr />
		  <display:table class="alternateColor" name="<%=dtolist%>" id="row"
			export="false" defaultsort="1" defaultorder="descending" >
			<display:column property="empname" title="DisplayName" sortable="true"
				headerClass="sortable" />
			<display:column property="personalno" title="PersonnelNo" sortable="true"
				headerClass="sortable" />
			<display:column property="estat" title="Emp Status" sortable="true"
				headerClass="sortable" />
			



</display:table>

<br /> 
				
			
					<input style="margin-left: 50%" type="Button" name="add"
					class="formbutton" value="Close"
					onclick="javascript:window.close()" />
					
					<br /> <br /> <br />
				<hr />


		</div>
	</div>
</body>
</html>

