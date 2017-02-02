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
	 var jsonData="";
		try{
		 jsonData = $.ajax({
	          url: "ProjectwiseNoShowServlet",
	          dataType:"json",
	          async: false
	          }).responseText;
		}catch (e)
		{
			 alert(" error " + e.message);
		}
		var jsonData1="";
		try{
		 jsonData1 = $.ajax({
	          url: "NoShowbyProjectsServlet",
	          dataType:"json",
	          async: false
	          }).responseText;
		}catch (e)
		{
			 alert(" error " + e.message);
		}
    var dataproject = jQuery.parseJSON(jsonData);
    var dataproject1 = jQuery.parseJSON(jsonData1);
	var options1 = {
				title : 'Top 5 Projects For No Shows',
					width : 650,
					height : 350,
					slices: [{color: '#95A7CB'}, {color: '#476EA5'}, {color: '#9F4A47'}, {color: '#7C9341'}, {color: 'violet'}]
			};
	var options2 = {
			title : 'No Show By Projects',
			titleTextStyle : {
				color : 'red',
			         fontName: "Arial",
			         fontStyle: "normal",
			         fontSize: 25
			},
			width : 650,
			height : 350,
				colors: ['red','green','green','blue','red','green','blue','blue','green','red'],
				legend: {textStyle: {color: 'black'}},gridlines: {color: 'black'}
		};
	 var data1= google.visualization.arrayToDataTable([['Project','Value'],['Project1',dataproject.current],['Project2',dataproject.acurrent],['Project3',dataproject.bcurrent],['Project4',dataproject.ccurrent],['Project5',dataproject.dcurrent]]);
	 var data2= google.visualization.arrayToDataTable([['Project','Value'],['Proj 1',dataproject1.current],['Proj 2',dataproject1.acurrent],['Proj 3',dataproject1.bcurrent],['Proj 4',dataproject1.ccurrent],['Proj 5',dataproject1.dcurrent],['Proj 6',dataproject1.ecurrent],['Proj 7',dataproject1.fcurrent],['Proj 8',dataproject1.gcurrent],['Proj 9',dataproject1.hcurrent],['Proj 10',dataproject1.icurrent]]);
		 
	 var chart = new google.visualization.PieChart(document
				.getElementById('chart_divs'));
		chart.draw(data1,options1);
		 var chart = new google.visualization.ColumnChart(document
					.getElementById('chart_div'));
			chart.draw(data2,options2);
		}
google.setOnLoadCallback(drawcharts);
google.load("visualization", "1", {
	packages : [ "corechart", "gauge" ]
});
</script>
<title>No Shows - Projectwise</title>
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
	%>
	<%@include file="Header.jsp"%>
	<%
		}
	%>
	<div id="body">
		<div class="content">
			<h2>

				<center>Projectwise No Show Dashboard</center>
			</h2>
			<hr />
			<table width='100%' border=1>
				<tr>
					<td>
						<div id="chart_div" style="width: 650px; height: 350px;" align="center"></div>
						
					</td>
					<td>
						

						<div id="chart_divs" style="width: 650px; height: 350px;" align="center"></div>
					</td>
				</tr>
			</table>


			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>