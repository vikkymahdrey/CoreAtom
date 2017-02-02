<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.dao.StsDao"%>
<%@page import="com.agiledge.atom.dto.EmergencyDto"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<!DOCTYPE html >
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>Sts Requiest</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/custom_siemens.css" rel="stylesheet">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<%
		long empid = 0;
	
	
	String siteId = request.getParameter("siteId");
	String Reason = request.getParameter("Reason");
	String startTime = request.getParameter("startTime"); ;
	String travelDate = request.getParameter("travelDate");
	String Travellers = request.getParameter("Travellers");
	String vehicletype = request.getParameter("chosenVehicleType");
	String Pickup = request.getParameter("Pickup");
	String Drop = request.getParameter("Drop");
	
		
		
		String employeeId = OtherFunctions.checkUser(session);

		empid = Long.parseLong(employeeId);

		EmergencyDto dto = new EmergencyDto();
	dto.setBookingBy(employeeId);
	dto.setSiteId(siteId);
	dto.setStartTime(startTime);
	dto.setReason(Reason);
	dto.setTravelDate(travelDate);
	dto.setVehicleType(vehicletype);
	dto.setLandmark(Pickup);
	dto.setDrop(Drop);
	dto.setEmpCount(Travellers);
	int i= new StsDao().insertSpecialTransportDetails(dto); 
		
	%>
<%@include file="Header.jsp"%>
<%
	
		OtherDao ob = null;
		ob = OtherDao.getInstance();
		String empRole = session.getAttribute("role").toString();
		
	%>
	<div class="wrapper">

		<div class="main-page-container">
			<div class="container">
				<div class="row">
					<div class="col-sm-12">

						<div class="content-wrap">

							<%
								if (session.getAttribute("status") != null) {
							%>
							<div class="row mar-top-40">
								<div class="col-sm-12">
									<div class="alert alert-success"><%=session.getAttribute("status")%></div>
								</div>
							</div>
							<%
								}
							%>


							<div class="section-heading">
								<div class="row">
									<div class="col-sm-12">Special Transportation Service</div>
								</div>
							</div>
							<div class="row mar-top-20">
								<div class="col-sm-12">
									<div class="alert alert-danger san" hidden="hidden"
										style="color: red">
										<p id="errortag"></p>
									</div>
								</div>
							</div>
							<div class="push-15 login-input-wrap">
							<div class="row">
									<% if(i>0) {%>
										
	<div class="col-sm-12" style="color: blue;">Special transportation service booked successfully.........
	 </div>
	<% }else{%><div class="col-sm-12" style="color: red;">Special transportation service booking  Unsuccessful......... </div>
			<% }%>					
								</div>
							</div>

						</div>

						<%@include file="Footer.jsp"%>

					</div>
				</div>

			</div>
		</div>
	</div>

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="js/bootstrap.min.js"></script>

</body>


</html>