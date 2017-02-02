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
 <form action="UnitUploadExcel" method="post" enctype="multipart/form-data" name="form1" id="form1"> 

    Upload File:
   <input name="file" type="file" id="file" class="formbutton" accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"><br><br>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" class="formbutton" name="Submit" value="Submit"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <input type="reset" class="formbutton"name="Reset" value="Reset"/>   
   </form>
   </center>
   <%@include file='Footer.jsp'%>
</body>
</html>