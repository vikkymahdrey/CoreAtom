<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
function drawcharts() {
	var date=new Date();
	date.setMonth(date.getMonth()-1,date.getDate());
	var monthname=new Array("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
	  monthname[-1]="Dec";
	  monthname[-2]="Nov";
	  monthname[-3]="Oct";
	  monthname[-4]="Sep";
	  monthname[-5]="Aug";
	var options = {
			title : 'OTA/OTD %',
			titleTextStyle : {
				color : 'White',
			         fontName: "Arial",
			         fontStyle: "normal",
			         fontSize: 25
			},legend: {textStyle: {color: 'white'}},
				width : 800,
				height : 450,
				backgroundColor: '#080003','chartArea': {
				    'backgroundColor': {
				        'fill': '#4A4849'

				     }},hAxis: {textStyle: {color: 'FBFAFF' , fontSize: 11}},
				     vAxis:{title : 'Percentage(%)',titleTextStyle : {color : 'White'},textStyle: {color: 'FBFAFF' , fontSize: 11},gridlines: {color: '#4A4849'}},series: {0: {color: '#E07A26', lineWidth:6,pointSize:5},1: {color: '#EBBCA0', lineWidth:6,pointSize:5}},is3D:true
		};
	 var jsonData="";
		try{
		 jsonData = $.ajax({
	          url: "OTAServlet",
	          dataType:"json",
	          async: false
	          }).responseText;
		}catch (e)
		{
			 alert(" error " + e.message);
		}
	var datapercent = jQuery.parseJSON(jsonData);
	var jsonData1="";
	try{
	 jsonData1 = $.ajax({
          url: "OTDServlet",
          dataType:"json",
          async: false
          }).responseText;
	}catch (e)
	{
		 alert(" error " + e.message);
	}
var datapercent1 = jQuery.parseJSON(jsonData1);
	var data = google.visualization.arrayToDataTable([
		                                   				[ 'Month', 'OTA','OTD' ], [ monthname[date.getMonth()-4], datapercent.pre5,datapercent1.pre5],
		                                				 [ monthname[date.getMonth()-3], datapercent.pre4,datapercent1.pre4], [ monthname[date.getMonth()-2], datapercent.pre3,datapercent1.pre3],  [ monthname[date.getMonth()-1], datapercent.pre2,datapercent1.pre2] ,[ monthname[date.getMonth()], datapercent.pre1,datapercent1.pre1 ],[ monthname[date.getMonth()+1], datapercent.Current,datapercent1.Current]]);
	var chart = new google.visualization.LineChart(document
			.getElementById('chart_div'));
	chart.draw(data,options, { backgroundColor: { fill: "#736D6F" } });
	
}
google.setOnLoadCallback(drawcharts);
google.load("visualization", "1", {
	packages : [ "corechart", "gauge" ]
});
</script>
<%
		long empid = 0;
		String employeeId = OtherFunctions.checkUser(session);
		if (employeeId == null || employeeId.equals("null")) {
			String param = request.getServletPath().substring(1) + "___"
					+ request.getQueryString();
			response.sendRedirect("index.jsp?page=" + param);
		} else {
			empid = Long.parseLong(employeeId);
	%>
	<%@include file="Header.jsp"%>
	<%
		}
	%>
	<div id="body">
		<div class="content">
			<h2>

				<center>OTA,OTD Dashboard</center>
			</h2>
			<hr />
			<table width="100%" border=1>
				<tr>
					<td align="center">
						<div id="chart_div" align="center" ></div>
					</td>
				</tr>
			</table>


			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</head>
<body>

</body>
</html>