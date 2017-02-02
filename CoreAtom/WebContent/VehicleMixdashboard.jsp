<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Vehicle Mix Dashboard</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
function drawcharts() {
	
	var jsonData1="";
	try{
	 jsonData1 = $.ajax({
          url: "VehicleMixServlet",
          dataType:"json",
          async: false
          }).responseText;
	}catch (e)
	{
		 alert(" error " + e.message);
	}
	var jsonData2="";
	try{
	 jsonData2 = $.ajax({
          url: "DailyVehicleMixServlet",
          dataType:"json",
          async: false
          }).responseText;
	}catch (e)
	{
		 alert(" error " + e.message);
	}
	var date=new Date();
	date.setMonth(date.getMonth()-1,date.getDate());
	var monthname=new Array("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
	  monthname[-1]="Dec";
	  monthname[-2]="Nov";
	  monthname[-3]="Oct";
	  monthname[-4]="Sep";
	  monthname[-5]="Aug";
	  var dates=new Array();
	  var d=new Date();
	  for(var i=1;i<=15;i++)
		  {
		  dates[i]=d.getDate()+"-"+monthname[d.getMonth()];
		  d=new Date();
		  d.setDate(d.getDate()-i);
		   }
	  
		var data1 = jQuery.parseJSON(jsonData1);
		var data2 = jQuery.parseJSON(jsonData2);
	var options = {
			title : 'Vehicle Mix',
			titleTextStyle : {
				color : 'red',
			         fontName: "Arial",
			         fontStyle: "normal",
			         fontSize: 25
			},
				width : 650,
				height : 350,
				colors: ['#417CCA','#CD3D3C'],
				legend: {textStyle: {color: 'black'}},
				hAxis:{gridlines: {color: '#4183D1'}},isStacked: true
		};
	var options2 = {
			title : 'Daily Vehicle Mix',
			titleTextStyle : {
				color : 'black',
			         fontName: "Arial",
			         fontStyle: "normal",
			         fontSize: 25
			},
				width : 650,
				height : 350,
				colors: ['#FCB38A','#DD8032'],
				legend: {textStyle: {color: 'black'}},
				hAxis:{direction:-1,
			        slantedText:true,
			        slantedTextAngle:90,gridlines: {color: 'black'}},isStacked: true
		};
	var data=google.visualization.arrayToDataTable([['Vehicle','Sumo','Indica'], [ monthname[date.getMonth()-4],data1.spre5,data1.pre5],
	                                				 [ monthname[date.getMonth()-3],data1.spre4,data1.pre4], [ monthname[date.getMonth()-2],data1.spre3,data1.pre3],  [ monthname[date.getMonth()-1],data1.spre2,data1.pre2] ,[ monthname[date.getMonth()],data1.spre1,data1.pre1],[ monthname[date.getMonth()+1],data1.scurrent,data1.current]]);
	var data3=google.visualization.arrayToDataTable([['Vehicle Mix','Indica','Sumo'],[dates[15],data2.pre14,data2.spre14],[dates[14],data2.pre13,data2.spre13],[dates[13],data2.pre12,data2.spre12],[dates[12],data2.pre11,data2.spre11],[dates[11],data2.pre10,data2.spre10],[dates[10],data2.pre9,data2.spre9],[dates[9],data2.pre8,data2.spre8],[dates[8],data2.pre7,data2.spre7],[dates[7],data2.pre6,data2.spre6],[dates[6],data2.pre5,data2.spre5],[dates[5],data2.pre4,data2.spre4],[dates[4],data2.pre3,data2.spre3],[dates[3],data2.pre2,data2.spre2],[dates[2],data2.pre1,data2.spre1],[dates[1],data2.current,data2.scurrent]]);
	var chart=new google.visualization.ColumnChart(document.getElementById('chart_div'));
	chart.draw(data,options);
	var chartA=new google.visualization.ColumnChart(document.getElementById('chart_divs'));
	chartA.draw(data3,options2);
}
google.setOnLoadCallback(drawcharts);
google.load("visualization", "1", {
	packages : [ "corechart", "gauge" ]
});
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
	%>
	<%@include file="Header.jsp"%>
	<%
		}
	%>
	<div id="body">
		<div class="content">
			<h2>

				<center>Vehicle Mix Dashboard</center>
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