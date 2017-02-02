<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>No Shows</title>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
function drawcharts() {
	var date=new Date();
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
	  var jsonData2="";
		try{
		 jsonData2 = $.ajax({
	          url: "SiteNoShowServlet",
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
	          url: "DailyNoShowServlet",
	          dataType:"json",
	          async: false
	          }).responseText;
		}catch (e)
		{
			 alert(" error " + e.message);
		}
		var jsonData4="";
		try{
		 jsonData4 = $.ajax({
	          url: "ShiftNoShowServlet",
	          dataType:"json",
	          async: false
	          }).responseText;
		}catch (e)
		{
			 alert(" error " + e.message);
		}
		 var datasite = jQuery.parseJSON(jsonData2);
		 var datadaily=jQuery.parseJSON(jsonData3);
		 var datashift=jQuery.parseJSON(jsonData4);
		 var options1 = {
					title : 'Overall No Show Trend',
					titleTextStyle : {
						color : 'red',
					         fontName: "Arial",
					         fontStyle: "normal",
					         fontSize: 25
					},
					width : 650,
					height : 350,
						colors: ['#6D86AE','red'],
						legend: {textStyle: {color: 'black'}},
						hAxis:{gridlines: {color: 'black'}}
				};	
	  var options2 = {
				title : 'No Show By Site',
				titleTextStyle : {
					color : 'red',
				         fontName: "Arial",
				         fontStyle: "normal",
				         fontSize: 25
				},
				width : 650,
				height : 350,
					colors: ['#6D86AE','red'],
					legend: {textStyle: {color: 'black'}},
					hAxis:{gridlines: {color: 'black'}}
			};
	  var options3 = {
				title : 'Daily No Show % For Technopolis',
				titleTextStyle : {
					color : 'black',
				         fontName: "Arial",
				         fontStyle: "normal",
				         fontSize: 25
				},
				width : 650,
				height : 350,
					colors: ['violet'],
					legend: {textStyle: {color: 'black'}},
					hAxis:{direction:-1,
				        slantedText:true,
				        slantedTextAngle:90,gridlines: {color: 'black'}}
			};
	  var options4 = {
				title : 'No Show By Shift',
				titleTextStyle : {
					color : 'black',
				         fontName: "Arial",
				         fontStyle: "normal",
				         fontSize: 25
				},
				width : 650,
				height : 350,
					colors: ['red','blue'],
					legend: {textStyle: {color: 'black'}},
					hAxis:{direction:-1,
				        slantedText:true,
				        slantedTextAngle:90,gridlines: {color: 'black'}}
			};
	   var data1= google.visualization.arrayToDataTable([
	                                   				[ 'Month', 'No Show' ], [ monthname[date.getMonth()-5],datasite.pre5+datasite.spre5],
	                                				 [ monthname[date.getMonth()-4],datasite.pre4+datasite.spre4], [ monthname[date.getMonth()-3],datasite.pre3+datasite.spre3],  [ monthname[date.getMonth()-2],datasite.pre2+datasite.spre2] ,[ monthname[date.getMonth()-1],datasite.pre1+datasite.spre1 ],[ monthname[date.getMonth()], datasite.Current+datasite.sCurrent]]);
		var data2 = google.visualization.arrayToDataTable([
				                                   				[ 'Month', 'Technopolis','Bagmane' ], [ monthname[date.getMonth()-5], datasite.pre5,datasite.spre5 ],
				                                				 [ monthname[date.getMonth()-4], datasite.pre4,datasite.spre4], [ monthname[date.getMonth()-3], datasite.pre3,datasite.spre3 ],  [ monthname[date.getMonth()-2], datasite.pre2,datasite.spre2] ,[ monthname[date.getMonth()-1], datasite.pre1,datasite.spre1 ],[ monthname[date.getMonth()], datasite.Current,datasite.sCurrent ]]);
		var data3=google.visualization.arrayToDataTable([['Day','No Show'],[dates[15],datadaily.pre14],[dates[14],datadaily.pre13],[dates[13],datadaily.pre12],[dates[12],datadaily.pre11],[dates[11],datadaily.pre10],[dates[10],datadaily.pre9],[dates[9],datadaily.pre8],[dates[8],datadaily.pre7],[dates[7],datadaily.pre6],[dates[6],datadaily.pre5],[dates[5],datadaily.pre4],[dates[4],datadaily.pre3],[dates[3],datadaily.pre2],[dates[2],datadaily.pre1],[dates[1],datadaily.Current]]);
		var data4=google.visualization.arrayToDataTable([['Day','Technopolis','Bagmane'],[dates[15],datashift.pre14,datashift.cpre14],[dates[14],datashift.pre13,datashift.cpre13],[dates[13],datashift.pre12,datashift.cpre12],[dates[12],datashift.pre11,datashift.cpre11],[dates[11],datashift.pre10,datashift.cpre10],[dates[10],datashift.pre9,datashift.cpre9],[dates[9],datashift.pre8,datashift.cpre8],[dates[8],datashift.pre7,datashift.cpre7],[dates[7],datashift.pre6,datashift.cpre6],[dates[6],datashift.pre5,datashift.cpre5],[dates[5],datashift.pre4,datashift.cpre4],[dates[4],datashift.pre3,datashift.cpre3],[dates[3],datashift.pre2,datashift.cpre2],[dates[2],datashift.pre1,datashift.cpre1],[dates[1],datashift.Current,datashift.cCurrent]]);
		var chartA = new google.visualization.LineChart(document.getElementById('chart_div'));
        chartA.draw(data1, options1);
		   var chartB = new google.visualization.LineChart(document.getElementById('chart_divs'));
           chartB.draw(data2, options2);
           var chartC = new google.visualization.LineChart(document.getElementById('chart_div1'));
           chartC.draw(data3, options3);
           var chartD = new google.visualization.LineChart(document.getElementById('chart_div2'));
           chartD.draw(data4, options4);
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

		<center>No Show Dashboard</center>
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