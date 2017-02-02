<%@page import="com.agiledge.atom.dao.SiteDao"%>
<%@page import="com.agiledge.atom.dto.SiteDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<title>Upload Excel File</title>
</head>
<body>
<h3 align="center">Upload Excel File</h3>
<center>
 <form action="UploadExcel" method="post" enctype="multipart/form-data" name="form1" id="form1"> 
	Site :&nsbsp;
	<select name="site" id="site"  >
								<option>--Select Site--</option>
								<%
						 
								List<SiteDto> siteDtoList = new SiteDao().getSites();
						 
									for (SiteDto siteDto : siteDtoList) {
										String site = (request.getSession().getAttribute("site") == null || request
												.getSession().getAttribute("site").toString().trim()
												.equals("")) ? "" : request.getSession()
												.getAttribute("site").toString().trim();
										site = request.getParameter("siteId") != null
												&& request.getParameter("siteId").trim().equals("") == false ? request
												.getParameter("siteId") : site;
										String siteSelect = "";
										if (site.equals(siteDto.getId())) {
											siteSelect = "selected";
										}
								%>

								<option <%=siteSelect%> value="<%=siteDto.getId()%>"><%=siteDto.getName()%></option>
								
								<%
									}
								%>
						</select>
						<br/>
						<label><input type="checkbox" name="sendMail" /> Send mail </label><br/>
    Upload File : 
   <input name="file" type="file" id="file" class="formbutton" accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"><br><br>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" class="formbutton" name="Submit" value="Submit"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <input type="reset" class="formbutton"name="Reset" value="Reset"/>   
   </form>
   </center>
   <%@include file='Footer.jsp'%>
</body>
</html>