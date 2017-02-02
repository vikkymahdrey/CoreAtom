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
				$('#AddResults').click(
						function() {
							if (rowNumber == 1)
								var rowno = "onerow";
							else
								var rowno = "onerow" + (rowNumber - 1);
							hour = $('#' + rowno + '').children().first()
									.next().next().children().first();
							minute = $('#' + rowno + '').children().first()
									.next().next().children().first().next();
							if (hour.val() == "00" && minute.val() == "00") {
								alert("Select end time and click add");
							} else {
								$('#myTable tbody').append('<tr id=onerow'+rowNumber+'>'+ $('#onerow').html()+ '</tr>');
								var hour;
								var minute;
								if (rowNumber == 1)
									var rowno = "onerow";
								else
									var rowno = "onerow" + (rowNumber - 1);
								hour = $('#' + rowno + '').children().first()
										.next().next().children().first();
								minute = $('#' + rowno + '').children().first()
										.next().next().children().first()
										.next();

								$('#' + rowno + '').next().children().first()
										.next().children().first().val(
												$(hour).val());
								$('#' + rowno + '').next().children().first()
										.next().children().first().next().val(
												$(minute).val());
								rowNumber++;
							}
						});

			});
	function validate() {
		var flag = true;
		var rowNumbertemp = rowNumber - 1;
		if ($('#onerow' + rowNumbertemp + '').children().first().next().next()
				.children().first().val() != "24"
				|| $('#onerow' + rowNumbertemp + '').children().first().next()
						.next().children().first().next().val() != "00") {
			alert("Last time should 24:00");
			flag = false;
		}
		 else if ($('#onerow').children().first().next().children().first().val() != "00" ) 
		{
			alert("First time should 00:00");
			flag=false;
		}		
		$('.required').each(function() {
				if($(this).val()==null||$(this).val()=="")
					{
					alert("Fill all the Traffic Fields");
					flag=false;
					}
		});
		$('.numberrequired').each(function() {			
			if(isNaN($(this).val()))
				{
				alert("All the speed should be number");
				flag=false;
				}
	});
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
			<form name="form1" method="post" action="TimeSloat" onsubmit="return validate()">
		<table id="myTable">

			<thead>

				<tr>
					<th>Traffic</th>
					<th>Time Start</th>
					<th>Time end</th>
					<th>Speed</th>
				</tr>

			</thead>

			<tbody>
				<tr id="onerow">
					<td><input type="text" name="traffic" class="required" /></td>
					<td><select name="time_start_hr" class="starttime"
						id="time_start_hr" >
							<%
								for (int hour = 0; hour <= 24; hour++) {
									String hourString = (hour < 10 ? "0" + hour : "" + hour);
							%>
							<option value="<%=hourString%>"><%=hourString%></option>
							<%
								}
							%>
					</select> <select name="time_start_mnt" class="starttime"
						id="time_start_mnt">
							<%
								for (int minute = 0; minute < 60; minute++) {
									String minuteString = (minute < 10 ? "0" + minute : "" + minute);
							%>
							<option value="<%=minuteString%>"><%=minuteString%></option>
							<%
								}
							%>
					</select></td>
					<td><select class="endtime" name="time_end_hr"
						id="time_end_hr">
							<%
								for (int hour = 0; hour <= 24; hour++) {
									String hourString = (hour < 10 ? "0" + hour : "" + hour);
							%>
							<option value="<%=hourString%>"><%=hourString%></option>
							<%
								}
							%>
					</select> <select class="endtime" name="time_end_mnt" id="time_end_mnt">
							<%
								for (int minute = 0; minute < 60; minute++) {
									String minuteString = (minute < 10 ? "0" + minute : "" + minute);
							%>
							<option value="<%=minuteString%>"><%=minuteString%></option>
							<%
								}
							%>
					</select></td>
					<td><input type="text" name="speed" class="numberrequired"/></td>
				</tr>
			</tbody>

		</table>
		<p align="center">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" id="AddResults">Add</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="submit" name="submit" value="Submit" class="formbutton" />
		</p>
	</form>


			</div>
			<%@include file="Footer.jsp"%>
		</div>



</body>
</html>
