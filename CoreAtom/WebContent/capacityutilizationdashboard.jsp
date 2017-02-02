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
	  var dates=new Array();
	  var d=new Date();
	  for(var i=1;i<=15;i++)
		  {
		  dates[i]=d.getDate()+"-"+monthname[d.getMonth()];
		  d=new Date();
		  d.setDate(d.getDate()-i);
		   }
	  var options = {
				title : 'Planned Vs Actuals',
				titleTextStyle : {
					color : 'red',
				         fontName: "Arial",
				         fontStyle: "normal",
				         fontSize: 25
				},
					width : 650,
					height : 350,
					colors: ['#3B72B3','#D55F5F','#5C96D6'],
					legend: {textStyle: {color: 'black'}},
					hAxis:{gridlines: {color: '#4A4849'}}
			};
	  var jsonData="";
		try{
		 jsonData = $.ajax({
	          url: "PlannedActualCapacity",
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
	          url: "DailyCapacityUtilizationServlet",
	          dataType:"json",
	          async: false
	          }).responseText;
		}catch (e)
		{
			 alert(" error " + e.message);
		}
		/* var jsonData3="";
			try{
			 jsonData3 = $.ajax({
		          url: "ShiftCapacityUtilizationServlet",
		          dataType:"json",
		          async: false
		          }).responseText;
			}catch (e)
			{
				 alert(" error " + e.message);
			}
		var datashift =	jQuery.parseJSON(jsonData3);*/
		var datadaily = jQuery.parseJSON(jsonData2);
		 var jsonData1="";
			try{
			 jsonData1 = $.ajax({
		          url: "ActualCapacityServlet",
		          dataType:"json",
		          async: false
		          }).responseText;
			}catch (e)
			{
				 alert(" error " + e.message);
			}
			var dataactual = jQuery.parseJSON(jsonData1);
			var dataplanned = jQuery.parseJSON(jsonData);
	  var data = google.visualization.arrayToDataTable([
		                                   				[ 'Month', 'Actual','Planned' ], [ monthname[date.getMonth()+1],dataactual.Current,dataplanned.Current],
		                                				 [ monthname[date.getMonth()],dataactual.pre1,dataplanned.pre1], [ monthname[date.getMonth()-1],dataactual.pre2,dataplanned.pre2],  [ monthname[date.getMonth()-2],dataactual.pre3,dataplanned.pre3] ,[ monthname[date.getMonth()-3],dataactual.pre4,dataplanned.pre4],[ monthname[date.getMonth()-4],dataactual.pre5,dataplanned.pre5]]);
	  var title1='Capacity Utilization By Shift For '+monthname[date.getMonth()+1];
	  var options1 = {
				title : title1,
				titleTextStyle : {
					color : 'red',
				         fontName: "Arial",
				         fontStyle: "normal",
				         fontSize: 25
				},
					width : 650,
					height : 350,
					colors: ['#F3B795','#D9853C'],
					legend: {textStyle: {color: 'black'}},
					hAxis:{gridlines: {color: '#4A4849'}}
			};
	  var data1 = google.visualization.arrayToDataTable([
		                                   				[ 'Shift', 'Cap Util Actual','Cap Util Planned' ],
		                                				 [ 'Shift1',0,0], [ 'Shift 4',0,0],  [ 'Shift 3',0,0] ,[ 'Shift 2',0,0],['Shift 1',0,0]]);
	  var options2 = {
				title : 'Daily Capacity-Planned Vs Actual',
				titleTextStyle : {
					color : 'red',
				         fontName: "Arial",
				         fontStyle: "normal",
				         fontSize: 25
				},
					width : 650,
					height : 350,
					colors: ['#565656','#B0B0B0'],
					legend: {textStyle: {color: 'black'}},
					hAxis:{direction:-1,
				        slantedText:true,
				        slantedTextAngle:90,gridlines: {color: '#4A4849'}}
			};
	  var data2=google.visualization.arrayToDataTable([['Day','Planned','Actual'],[dates[15],datadaily.pre14,datadaily.cpre14],[dates[14],datadaily.pre13,datadaily.cpre13],[dates[13],datadaily.pre12,datadaily.cpre12],[dates[12],datadaily.pre11,datadaily.cpre11],[dates[11],datadaily.pre10,datadaily.cpre10],[dates[10],datadaily.pre9,datadaily.cpre9],[dates[9],datadaily.pre8,datadaily.cpre8],[dates[8],datadaily.pre7,datadaily.cpre7],[dates[7],datadaily.pre6,datadaily.cpre6],[dates[6],datadaily.pre5,datadaily.cpre5],[dates[5],datadaily.pre4,datadaily.cpre4],[dates[4],datadaily.pre3,datadaily.cpre3],[dates[3],datadaily.pre2,datadaily.cpre2],[dates[2],datadaily.pre1,datadaily.cpre1],[dates[1],datadaily.Current,datadaily.cCurrent]]);
	  var chart = new google.visualization.BarChart(document
				.getElementById('chart_div'));
		chart.draw(data,options);
		 var chart1 = new google.visualization.BarChart(document
					.getElementById('chart_divs'));
			chart1.draw(data1,options1);
	var chart2=new google.visualization.LineChart(document.getElementById('chart_div1'));
	chart2.draw(data2,options2);
}
google.setOnLoadCallback(drawcharts);
google.load("visualization", "1", {
	packages : [ "corechart", "gauge" ]
});
</script>
<title>Capacity Utilization</title>
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

				<center>Capacity Utilization Dashboard</center>
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
				<tr align="center">
					<td align="center">
						<div id="chart_div1" align="center"></div>
						
					</td>
				</tr>
			</table>


			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>

</body>
</html>