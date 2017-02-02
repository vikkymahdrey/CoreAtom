
<%@page import="com.agiledge.atom.service.TripDetailsService"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.agiledge.atom.dao.TripDetailsDao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<title>Vehicle Status Report</title>
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/validate.js"></script>

<script type="text/javascript" src="js/dateValidation.js"></script>
<script src="js/JavaScriptUtil.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>
<script type="text/javascript" src="js/jquery.datepick.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$("#tripDate").datepick();

		$(".hideclass").hide();

		$(".showhideclass").click(function() {
			var id = $(this).attr("id")
			if ($(".empDetails" + id).is(":hidden")) {
				$(".empDetails" + id).show();
				$(this).attr("src", "images/minus.png");
				var p = $(".empDetails" + id + ":last");
				var position = p.position();

				showMap("tripId" + id, position.top + 30);
			} else {
				$(".empDetails" + id).hide();
				$(".empDetails" + id).slideUp("slow");
				$(this).attr("src", "images/plus.png");
				document.getElementById("map").style.display = "none";
			}
		});
	});
</script>




<script type="text/javascript">
	function showTrips(tripDate, tripLog, tripTime) {

		var url = "TripWiseDetails?tripDate=" + tripDate + "&tripLog="
				+ tripLog + "&tripTime=" + tripTime;
		xmlHttp = GetXmlHttpObject();

		if (xmlHttp == null) {
			alert("browser not supprt");
		}

		xmlHttp.onreadystatechange = showTripWise;

		xmlHttp.open("GET", url, true);
		xmlHttp.send(null);

	}
	function showTripWise() {
		if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete") {
			document.getElementById("tab2").innerHTML = "<table style='border: thick;'><tr><th>tripCode</th><th>Date</th></th><th>Time</th><th>Count</th><th>Boared</th><th>Time</th><th>ExpectedTime</th></tr>"
					+ xmlHttp.responseText + "</table>";
			document.getElementById("tab3").innerHTML = "";
		}

	}

	function GetXmlHttpObject() {
		var xmlHttp = null;
		if (window.XMLHttpRequest) {
			xmlHttp = new XMLHttpRequest();
		}
		//catch (e)
		else if (window.ActiveXObject) {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		return xmlHttp;
	}
	function showTripEmp(tripId) {
		var url = "TripWiseDetails?tripId=" + tripId;
		xmlHttp = GetXmlHttpObject();

		if (xmlHttp == null) {
			alert("browser not supprt");
		}
		xmlHttp.onreadystatechange = showEmpWise;
		xmlHttp.open("GET", url, true);
		xmlHttp.send(null);

	}
	function showEmpWise() {
		if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete") {
			document.getElementById("tab3").innerHTML = "<table style='border: thick;'><tr><th>Name</th><th>APL</th><th>Show</th><th>Now</th></tr>"
					+ xmlHttp.responseText + "</table>";
			//document.getElementById("tab3").innerHTML = "";
		}

	}
</script>










</head>

<body>
	<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);

		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
	%><%@include file="Header.jsp"%>
	<%
		}
		OtherDao ob = null;
		ob = OtherDao.getInstance();
		String site =""; 
	     try {
	    	 site = session.getAttribute("site").toString();	 
	     }catch(Exception ignor){}
	    
		String[] city = ob.getCity(site);
	%>

	<%
		String tripDate = request.getParameter("tripDate") == null ? OtherFunctions
				.formatDateToOrdinaryFormat(new Date()) : request
				.getParameter("tripDate");
				String projectId = request.getParameter("projectId");
		ArrayList<TripDetailsDto> list = new TripDetailsService()
				.liveTripStatus(tripDate,projectId);
	%>
	<div id="body">
		<div class="content">
			<h3>Live Vehicle Status</h3>

			<form>

				<table>
					<tr>

						<td>Date</td>
						<td><input name="tripDate" id="tripDate" type="text" size="6"
							value="<%=tripDate%>" /></td>
						<td></td>
						<td><input type="submit" value="submit" class="formbutton"></td>
					</tr>

				</table>

			</form>

			<form>
				<table width="100%">
					<TR>
						<TD width="30%">
							<table style='border: thick;'>
								<tr>

									<th>Date</th>
									<th>Time</th>
									<th>#trip</th>
									<th>#Emp</th>
									<th>#Board</th>
								</tr>
								<%
									for (TripDetailsDto detailsDto : list) {
								%>
								<tr>

									<td><%=detailsDto.getTrip_date()%></td>
									<td><%=detailsDto.getTrip_time() + "-"
						+ detailsDto.getTrip_log()%></td>
									<td><a href="#"
										onclick="showTrips('<%=detailsDto.getTrip_date()%>','<%=detailsDto.getTrip_log()%>','<%=detailsDto.getTrip_time()%>')"><%=detailsDto.getTripCount()%></a></td>
									<td><%=detailsDto.getEmpInCount()%></td>
									<td><%=detailsDto.getReachedempCount()%></td>
								</tr>
								<%
									}
								%>
							</table>
						</TD>
						<TD width="40%" id="tab2"></TD>
						<TD width="40%" id="tab3"></TD>
					</TR>
				</table>
			</form>
		</div>
	</div>
	<div style="height: 750px"></div>
	<%@include file="Footer.jsp"%>
</body>

</html>
