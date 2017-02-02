<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="css/style.css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Add Site</title>
<script type="text/javascript">
        
        
        function showPopup(url) {

         
        var params="toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
         var size="height=124,width=300,top=200,left=300," + params;
        if(url=="LandMarkSearch.jsp")
            {
                size="height=450,width=520,top=200,left=300," + params;
            }
       

        newwindow=window.open(url,'name',size);

         if (window.focus) {newwindow.focus()}
        }
</script>
</head>
<body>

	<div id='body'>
		<div class='content'>
			<%
            long empid = 0;
            String employeeId = OtherFunctions.checkUser(session);
            if (employeeId == null||employeeId.equals("null") ) {
                String param = request.getServletPath().substring(1) + "___"+ request.getQueryString(); 	response.sendRedirect("index.jsp?page=" + param);
            } else {
                empid = Long.parseLong(employeeId);
                %>
			<%@include file="Header.jsp"%>
			<% 
            }
           
            
            
            
            OtherDao ob = null;
            ob = OtherDao.getInstance();
        %>



			<h3>Add New Site</h3>
			<form name="SiteFormForm" method="post" action="AddSite">

				<div>
					<table>
						<tr>
							<td width='10%'>Name</td>
							<td width='13%'><input type="text" name="name" /></td>

						</tr>

						<tr>
							<td>Area</td>
							<td><input type="text" value=""
								onclick="showPopup('LandMarkSearch.jsp') " readonly name="area"
								id="area" /> <input type="hidden" id="landMarkID"
								name="landMarkID" /> <label for="area" class="requiredLabel">*</label>

							</td>

						</tr>
						<tr>
							<td>Place</td>
							<td><input type="text" value=""
								onclick="showPopup('LandMarkSearch.jsp') " readonly name="place"
								id="place" /> <label for="place" class="requiredLabel">*</label>
							</td>

						</tr>
						<tr>
							<td>Landmark</td>
							<td><input type="text"
								onclick="showPopup('LandMarkSearch.jsp') " readonly
								name="landMark" id="landmark" /> <label for="landMark"
								class="requiredLabel">*</label> <input type="button"
								onclick="showPopup('LandMarkSearch.jsp') " value="..."
								class="formbutton" /></td>

						</tr>
						<tr>
							<td width='10%'>Night Shift Starts</td>
							<td width='13%'><input type="text" name="nightShiftStarts" />
								(hh:mm)</td>

						</tr>
						<tr>
							<td width='10%'>Night Shift Ends</td>
							<td width='13%'><input type="text" name="nightShiftEnds" />
								(hh:mm)</td>

						</tr>

						<tr>
							<td width='10%'>Lady Security</td>
							<td width='13%'><select name="ladySecurity">
									<option value="1">Yes</option>
									<option value="0">No</option>
							</select></td>

						</tr>
						<tr>
							<td width='10%'></td>
							<td width='13%'><input type="Submit" name="add"
								class="formbutton" value="Save" /> <input type="Button"
								name="add" class="formbutton" value="Back"
								onclick="javascript:history.go(-1);" /></td>

						</tr>
					</table>
				</div>
			</form>

			<%@include file="Footer.jsp"%>
		</div>
	</div>


</body>
</html>