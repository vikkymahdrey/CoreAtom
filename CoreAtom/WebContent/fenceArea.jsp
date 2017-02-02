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
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<title>Time Slot</title>
</head>
<body>
	<script type="text/javascript">
		var rowNumber = 1;
		$(document).ready(
				function() {
					$('#AddRow').click(
							function() {
								if (rowNumber == 1)
									var rowno = "onerow";
								else
									var rowno = "onerow" + (rowNumber - 1);
								endLat = $('#' + rowno + '').children().first().next()
										.children().first();
								endLon = $('#' + rowno + '').children().first().next()
										.children().first().next();
								$('#myTable tbody')
										.append(
												'<tr id=onerow'+rowNumber+'>'
														+ $('#onerow').html()
														+ '</tr>');

							});

				});
		function validate() {
			var flag = true;
			var rowNumbertemp = rowNumber - 1;
			if ($('#onerow' + rowNumbertemp + '').children().first().next()
					.next().children().first().val() != "24"
					|| $('#onerow' + rowNumbertemp + '').children().first()
							.next().next().children().first().next().val() != "00") {
				alert("Last time should 24:00");
				flag = false;
			}
			/* else if ($('#onerow' + 1 + '').children().first().next().children().first().val() != "00" 
				|| 
				$('#onerow' + 1 + '').children().first().next().children().first().next().val() != "00") 
			{
				alert("First time should 00:00");
				flag=false;
			}		
			
			 */
			return flag;

		}
	</script>


	<div id='body'>
		<div class='content'>
			<%
				long empid = 0;
				String employeeId = OtherFunctions.checkUser(session);
				if (employeeId == null || employeeId.equals("null")) {
					response.sendRedirect("index.jsp");
				} else {
					empid = Long.parseLong(employeeId);
			%>
			<%@include file="Header.jsp"%>
			<%
				}
			%>
			<h3>Time Slot Set Up</h3>
			<form name="form1" method="post" action="TimeSloat"
				onsubmit="return validate()">
				<p>
					Fence Type
						<input type="text" name="fenceType" />
					</p>
				<table id="myTable">
					


					<tr>
						<th>Start Lat Lon</th>
						<th>End Lat Lon</th>
						<th>Order</th>

					</tr>

					<tbody>

						<tr id="onerow">
							<td><input type="text" name="startlat" /><input type="text"
								name="startLong" /></td>
							<td><input type="text" name="endlat" /><input type="text"
								name="endLong" /></td>
							<td><input type="text" name="order" /></td>
						</tr>
					</tbody>

				</table>
				<p align="center">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="#" id="AddRow">Add</a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="submit"
						name="submit" value="Submit" class="formbutton" />
				</p>
			</form>


		</div>
		<%@include file="Footer.jsp"%>
	</div>



</body>
</html>
