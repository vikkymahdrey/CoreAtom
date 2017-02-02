<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dashboard</title>

<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	function drawcharts() {
		var jsonData1="";
		try{
		 jsonData1 = $.ajax({
	          url: "SiteCostSummaryServlet",
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
	          url: "PerPersonCostServlet",
	          dataType:"json",
	          async: false
	          }).responseText;
		}catch (e)
		{
			 alert(" error " + e.message);
		}
		var jsonData3="";
		try{
		 jsonData3 = $.ajax({
	          url: "VendorCostServlet",
	          dataType:"json",
	          async: false
	          }).responseText;
		}catch (e)
		{
			 alert(" error " + e.message);
		}
		var date=new Date();
		date.setMonth(date.getMonth()-1,date.getDate());
		var data1;
		var monthname=new Array("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
		  monthname[-1]="Dec";
		  monthname[-2]="Nov";
		  monthname[-3]="Oct";
		  monthname[-4]="Sep";
		  monthname[-5]="Aug";
		  var datacost = jQuery.parseJSON(jsonData1);
		  data1= google.visualization.arrayToDataTable([
				                                   				[ 'Month', 'Cost' ], [ monthname[date.getMonth()-4],datacost.pre5+datacost.spre5],
				                                				 [ monthname[date.getMonth()-3],datacost.pre4+datacost.spre4], [ monthname[date.getMonth()-2],datacost.pre3+datacost.spre3],  [ monthname[date.getMonth()-1],datacost.pre2+datacost.spre2] ,[ monthname[date.getMonth()],datacost.pre1+datacost.spre1 ],[ monthname[date.getMonth()+1], datacost.Current+datacost.sCurrent]]);
		var options1 = {
			title : 'Monthly Transport Cost Summary',
			titleTextStyle : {
				color : 'White',
			         fontName: "Arial",
			         fontSize: 25
			},legend: {textStyle: {color: 'white'}},
			width : 650,
			height : 350,
			backgroundColor: '#080003','chartArea': {
			    'backgroundColor': {
			        'fill': '#4A4849'
               }},
               hAxis: { textStyle: {color: 'FBFAFF' , fontSize: 11} },
               vAxis: {title : '(INR)',titleTextStyle : {color : 'White'},textStyle: {color: 'FBFAFF' , fontSize: 11} ,gridlines: {color: '#4A4849'}}, series: {0: {color: '#9EB66A', lineWidth:6,pointSize:5
		    	 },1: {color: '#DBBEB0', lineWidth:6,pointSize:5}}
			
		};
		var data2 = google.visualization.arrayToDataTable([
			                                   				[ 'Month', 'Technopolis','Bagmane' ], [ monthname[date.getMonth()-4], datacost.pre5,datacost.spre5 ],
			                                				 [ monthname[date.getMonth()-3], datacost.pre4,datacost.spre4], [ monthname[date.getMonth()-2], datacost.pre3,datacost.spre3 ],  [ monthname[date.getMonth()-1], datacost.pre2,datacost.spre2] ,[ monthname[date.getMonth()], datacost.pre1,datacost.spre1 ],[ monthname[date.getMonth()+1], datacost.Current,datacost.sCurrent ]]);
		var options2 = {
			title : 'Monthly Cost Summary By Site ',
			titleTextStyle : {
				align : 'right',
				color : 'White',
			         fontName: "Arial",
			         fontStyle: "normal",
			         fontSize: 25
			},legend: {textStyle: {color: 'white'}},
			width : 650,
			height : 350,
			backgroundColor: '#080003','chartArea': {
			    'backgroundColor': {
			        'fill': '#4A4849'

			     }},
			     hAxis: {textStyle: {color: 'FBFAFF' , fontSize: 11} },
			     vAxis:{title : '(INR)',titleTextStyle : {color : 'White'},textStyle: {color: 'FBFAFF' , fontSize: 11},gridlines: {color: '#4A4849'}}, series: {0: {color: '#AE7B50', lineWidth: 6,pointSize:5
			    	 }}

		};
		var datacost3 = jQuery.parseJSON(jsonData3);
		var data = google.visualization.arrayToDataTable([
				['Vendor A', monthname[date.getMonth()-1], monthname[date.getMonth()], monthname[date.getMonth()+1]], [ 'Vend A',datacost3.pre2,datacost3.pre1,datacost3.Current]]);

		var options = {
			title : 'Cost Summary By Vendor',
			titleTextStyle : {
				color : 'White',
			         fontName: "Arial",
			         fontStyle: "normal",
			         fontSize: 25
			},legend: {textStyle: {color: 'white'}},
			colors: ['#3B72B3','#CC4041','#91B74A'],
				width : 650,
				height : 350,
				backgroundColor: '#080003','chartArea': {
				    'backgroundColor': {
				        'fill': '#4A4849'

				     }},hAxis: {textStyle: {color: 'FBFAFF' , fontSize: 11}},
				     vAxis:{title : '(INR)',titleTextStyle : {color : 'White'},textStyle: {color: 'FBFAFF' , fontSize: 11},gridlines: {color: '#4A4849'}},is3D:true
		};
		var datacost2 = jQuery.parseJSON(jsonData2);

		var datab = google.visualization.arrayToDataTable([
			                                   				[ 'Month', 'Cost' ], [ monthname[date.getMonth()-4], datacost2.pre5 ],
			                                				 [ monthname[date.getMonth()-3], datacost2.pre4], [ monthname[date.getMonth()-2], datacost2.pre3 ],  [ monthname[date.getMonth()-1], datacost2.pre2] ,[ monthname[date.getMonth()], datacost2.pre1 ],[ monthname[date.getMonth()+1], datacost2.Current ]]);
		
		var optionsb = {
			title : 'Per Person Cost(INR)',
			titleTextStyle : {
				color : 'White',
			         fontName: "Arial",
			         fontStyle: "normal",
			         fontSize: 25
			},legend: {textStyle: {color: 'white'}},
			width : 650,
			height : 350,
			backgroundColor: '#080003','chartArea': {
			    'backgroundColor': {
			        'fill': '#4A4849'

			     }},
			     hAxis: {textStyle: {color: 'FBFAFF' , fontSize: 11} },
	               vAxis: {title : '(INR)',titleTextStyle : {color : 'White'},textStyle: {color: 'FBFAFF' , fontSize: 11} ,gridlines: {color: '#4A4849'}}, series: {0: {color: '#BA4C4B', lineWidth: 6,pointSize:5}}
		};

		var chartA = new google.visualization.LineChart(document
				.getElementById('chart_div'));
		chartA.draw(data1,options1, { backgroundColor: { fill: "#736D6F" } });

		var chartB = new google.visualization.LineChart(document
				.getElementById('chart_divs'));

		chartB.draw(data2, options2);
 		var chart = new google.visualization.ColumnChart(document
 				.getElementById('chart_div1'));
		chart.draw(data, options);

		var chartD = new google.visualization.LineChart(document
				.getElementById('chart_div2'));
		chartD.draw(datab, optionsb);

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

				<center>Dashboard</center>
			</h2>
			<hr />
			<table width='100%' border=1>
				<tr>
					<td>
						<div id="chart_div" style="width: 450px; height: 350px;" align="center"></div>
						
					</td>
					<td>
						

						<div id="chart_divs" style="width: 450px; height: 350px;" align="center"></div>
					</td>
				</tr>
				<tr>
					<td>
						<div id="chart_div1" style="width: 450px; height: 350px;" align="center"></div>
						
					</td>
					<td>
						
						<div id="chart_div2" style="width: 450px; height: 350px;" align="center"></div>
					</td>
				</tr>
			</table>


			<%@include file="Footer.jsp"%>
		</div>
	</div>

</body>
</html>