<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.dao.RouteUpload"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" >
 $(document).ready( function () {
	 	alert(' ready');
	 
 }); 

</script>
<title>Insert title here</title>
</head>
<body>
	<%
String buttonStatus=""+request.getParameter("submitbtn");
if(buttonStatus.equals("Update"))
{
String SheetName=""+request.getParameter("sheetName");
String sourceName=""+request.getParameter("sourceName");
int status=Integer.parseInt(""+request.getParameter("status"));
int siteId=1;

RouteUpload routeUpload=new RouteUpload();
ArrayList<RouteDto> dtos=routeUpload.getRoutesFromExcel(SheetName,sourceName,status,siteId);

routeUpload.printAll();

//new InsertToDB().insertRoutesToDB(dtos,routeUpload.count);
/*routeUpload.insertRoutesToDB();
if(returnVal==1)
{
	out.println("<b>Success</b>");
	}
else
{
	out.println("<b>Failed</b>");
}*/
}
%>
	<form name="form1" action="" onsubmit="return Validate()">
		<table>
			<tr>
				<td>Data Source Name</td>
				<td><input type="text" value="" name="sourceName" /></td>
			</tr>
			<tr>
				<td>Sheet Name</td>
				<td><input type="text" value="" name="sheetName" /></td>
			</tr>
			<tr>
				<td>Save/Update</td>
				<td><select name="status" id="status">
						<option value="0">Save</option>
						<option value="1">Update</option>
				</select>
				<input name="truncate"  type="checkbox" /> Truncate
				</td>

			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Update" name="submitbtn" /></td>
			</tr>
			<tr>
		</table>
	</form>
</body>
</html>