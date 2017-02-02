<%@page contentType="text/html" pageEncoding="UTF-8"
	errorPage="error.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Unsubscription</title>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/coin-slider.css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<link href="css/validate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script src="js/dateValidation.js"></script>

<script>
	$(document).ready(function() {
		$('#fromDate').datepick();		
	});
</script>



</head>
<body>
	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		empid = Long.parseLong(employeeId);
		String bookingId=request.getParameter("bookingId");
	%>
	<%@include file="Header.jsp"%>
	<div id="body">
		<div class="content">




			<form name="unsubscribeRequestForm" action="ShuttleCancelConfirm" method="post">

				<h3>Cancel Shuttle</h3>

					<table>
						<tr>
							<td>
							<input type="hidden" value="<%=bookingId %>" name="bookingId"/>
							From :</td>
							<td><input type="text" id="fromDate" name="fromDate" readonly="readonly"/>
								<label for="fromDate" class="requiredLabel">*</label> <input
								type="submit" class="formbutton" id="unsubscribeRequest"
								value="Unsubscribe" /></td>

						</tr>

					</table>
			</form>


			<%@include file="Footer.jsp"%>


		</div>
	</div>

</body>
</html>
