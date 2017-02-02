<%@page import="com.agiledge.atom.service.TripDetailsService"%>
<%@page import="com.agiledge.atom.dto.TripDetailsChildDto"%>
<%@page import="com.agiledge.atom.dao.TripDetailsDao"%>
<%@page import="com.agiledge.atom.dto.TripDetailsDto"%>
<%@page import="java.sql.*"%>
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Date"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="java.text.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/dateValidation.js"></script>
<title>Compare Sheet</title>
<script type="text/javascript">
function validateForm()
{
	  var total=0;
      for(var i=0; i < document.form1.tripidcheck.length; i++){
          if(document.form1.tripidcheck[i].checked){
              total =total +1;}
      }
          if(total < 1){
              alert(" select two atleast One"); 
              document.form1.tripidcheck[3].checked = false ;
              return false;         
      }          	
}
    function saveTrip()
    {
        var tripDate = document.getElementById("tripDate").value;
        var tripTime = document.getElementById("tripTime").value;
        var tripMode = document.getElementById("tripMode").value;
        var siteId = document.getElementById("siteId").value;     
        window.location="SaveTrip?siteId=" + siteId + "&tripDate=" + tripDate +"&tripTime=" + tripTime + "&tripMode=" + tripMode+"";
    }
    function validate()
    {
        var total=0;
        for(var i=0; i < document.form1.tripidcheck.length; i++){
            if(document.form1.tripidcheck[i].checked){
                total =total +1;}
            if(total > 2){
                alert("Maximum select two");
                document.form1.tripidcheck[3].checked = false ;
                return false;
            }
        }                   
    }
    </script>
</head>
<body>
	<div id="body">
		<div class="content">
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
    String tripDate = request.getParameter("tripDate");
    String tripTime = request.getParameter("tripTime");
    String tripMode = request.getParameter("tripMode");
    String siteId = request.getParameter("siteId");
    ArrayList<TripDetailsDto> tripSheetList = new TripDetailsService().getTripSheetSaved(tripDate, tripMode, siteId, tripTime);
    ArrayList<TripDetailsDto> tripSheetListActual = new TripDetailsService().getTripSheetActual(tripDate, tripMode, siteId, tripTime);
%>



			<hr />
			<p>
				<input type="button" class="formbutton" value="Back"
					onclick="javascript:history.go(-1);" />
			</p>
			<h3>Modified Trip Sheet</h3>


			<hr />
			<%
        for (TripDetailsDto tripDetailsDtoObj : tripSheetList) {%>
			<table border="1" width="80%">
				<tr>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>Vehicle type</td>
					<td colspan="3"><%=tripDetailsDtoObj.getVehicle_type()%></td>
					<td>Trip ID</td>
					<td><%=tripDetailsDtoObj.getTrip_code()%></td>
				</tr>

				<tr>
					<td>&nbsp;</td>
					<td>Vehicle #</td>
					<td colspan="3">&nbsp;</td>
					<td>Date</td>
					<td><%=OtherFunctions.changeDateFromatToddmmyyyy(tripDetailsDtoObj.getTrip_date())%></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>Escort Trip</td>
					<td colspan="3"><%=tripDetailsDtoObj.getIsSecurity()%></td>
					<td>IN/OUT Time</td>
					<td><%=tripDetailsDtoObj.getTrip_log() + "  " + tripDetailsDtoObj.getTrip_time()%></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>Escort Clock</td>
					<td colspan="3">&nbsp;</td>
					<td>Actual IN/OUT time</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>#</td>
					<td>Name</td>
					<td>Area</td>
					<td>Place</td>
					<td>landmark</td>
					<td>Status remark</td>
					<td>Signature</td>
				</tr>
				<%
            int i = 1;
            for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj.getTripDetailsChildDtoList()) {
        %>
				<tr>
					<td><%=i%></td>
					<td><%=tripDetailsChildDto.getEmployeeName()%></td>
					<td><%=tripDetailsChildDto.getArea()%></td>
					<td><%=tripDetailsChildDto.getPlace()%></td>
					<td><%=tripDetailsChildDto.getLandmark()%></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<%
                i++;
            }%>
				</tbody>
			</table>

			<%
        }
    %>


			<%
        if (tripSheetListActual != null && tripSheetListActual.size() > 0) {
    %>
			<h3>System Generated Trip Sheet</h3>
			<hr />

			<%
        for (TripDetailsDto tripDetailsDtoObj : tripSheetListActual) {%>
			<table border="1" width="80%">
				<tr>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>Vehicle type</td>
					<td colspan="3"><%=tripDetailsDtoObj.getVehicle_type()%></td>
					<td>Trip ID</td>
					<td><%=tripDetailsDtoObj.getTrip_code()%></td>
				</tr>

				<tr>
					<td>&nbsp;</td>
					<td>Vehicle #</td>
					<td colspan="3">&nbsp;</td>
					<td>Date</td>
					<td><%=OtherFunctions.changeDateFromatToddmmyyyy(tripDetailsDtoObj.getTrip_date())%></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>Escort Trip</td>
					<td colspan="3"><%=tripDetailsDtoObj.getIsSecurity()%></td>
					<td>IN/OUT Time</td>
					<td><%=tripDetailsDtoObj.getTrip_log() + "  " + tripDetailsDtoObj.getTrip_time()%></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>Escort Clock</td>
					<td colspan="3">&nbsp;</td>
					<td>Actual IN/OUT time</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>#</td>
					<td>Name</td>
					<td>Area</td>
					<td>Place</td>
					<td>landmark</td>
					<td>Status remark</td>
					<td>Signature</td>
				</tr>
				<%
            int i = 1;
            for (TripDetailsChildDto tripDetailsChildDto : tripDetailsDtoObj.getTripDetailsChildDtoList()) {
        %>
				<tr>
					<td><%=i%></td>
					<td><%=tripDetailsChildDto.getEmployeeName()%></td>
					<td><%=tripDetailsChildDto.getArea()%></td>
					<td><%=tripDetailsChildDto.getPlace()%></td>
					<td><%=tripDetailsChildDto.getLandmark()%></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<%
                i++;
            }%>
				</tbody>
			</table>

			<%
    }
%>
			<%}%>

			<%@include file="Footer.jsp"%>
		</div>
	</div>
</body>
</html>