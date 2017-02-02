

<%@page import="com.agiledge.atom.dto.GeoTagDto"%>
<%@page import="java.util.List"%>
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.dao.ShuttleSocketDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.dto.RouteDto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.RouteService"%>
<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@page import="com.agiledge.atom.dto.EmployeeSubscriptionDto"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="images/agile.png" type="image/x-icon" />
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Geo Tag Report</title>
    <%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
    <%@ taglib uri="http://www.nfl.com" prefix="disp" %>


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
  <body onload="getAddr();">
  		<%
						String message="";
						EmployeeDto empDtoInHeader=null; 
						try{
							
								String role=session.getAttribute("role").toString();
						 		System.out.println("BEFORE SEsion");
								empDtoInHeader = (EmployeeDto)session.getAttribute("userDto");
								System.out.println("AFTER SEsion");
								if(empDtoInHeader==null) {
									empDtoInHeader = new EmployeeService().getEmployeeAccurate(session.getAttribute("user").toString());
								}
								
							}catch(NullPointerException ne) {
						 
								empDtoInHeader = new EmployeeService().getEmployeeAccurate(session.getAttribute("user").toString());
							}
						String employeeId = OtherFunctions.checkUser(session);

						long empid = Long.parseLong(employeeId);
						List<GeoTagDto> list = new ShuttleSocketDao().getemployeeGeoTagDetails();
						String fname1=("GeoTagReport :").concat(new Date().toString()).concat(".csv");
						String fname2=("GeoTagReport :").concat(new Date().toString()).concat(".xls");
						String fname3=("GeoTagReport :").concat(new Date().toString()).concat(".xml");
						
						%>
  
<div class="wrapper">
	<div class="header-wrap">
		<div class="container">
			<div class="row">
				<div class="col-sm-12 text-right">
					<img src="images/user_iocn_header.png" />&nbsp;Welcome <%=empDtoInHeader.getDisplayName() %>  &nbsp;&nbsp;&nbsp;<a href="Logout"><img src="images/logout_icon_header.png" />&nbsp;Log Out</a>
				</div>
			</div>
		</div>
	</div>

	<div class="main-page-container">
		<div class="container">	
			<div class="row">
				<div class="col-sm-12">
				
				<div class="breadcrumb-wrap">
					<a href="employee_home.jsp"><img src="images/home.png" /></a>
					<a href="employee_home.jsp" >My Information </a>
					<a href="surveyStats.jsp" >Survey Status</a>
					<a href="#" class="current">GeoTag Report </a>
				</div>
				<div class="content-wrap">
				
				<%if(session.getAttribute("status")!=null){ %>
						<div class="row mar-top-40">
						<div class="col-sm-12">
							<div class="alert alert-success"><%= session.getAttribute("status")%></div>
						</div>
					</div>
						<%} %>
					<div class="row">
						<div class="col-sm-8 page-heading mar-top-20">
							<h5 class="text-blue text-semi-bold">Geo Tag Report </h5>
						</div>
											
					</div>
				<br/>

<display:table class="alternateColor" name="<%=list%>" id="row"
				export="true" defaultsort="1" defaultorder="descending"
				pagesize="50" >
			<display:column title="EmployeeName" property="empName" sortable="true" headerClass="sortable" />
			<display:column title="EmployeeId" property="personnelNo" sortable="true" headerClass="sortable" />
			<display:column title="Address" property="address" sortable="true" headerClass="sortable" />
			<display:column title="Gender" value="${row.gender=='M'? 'Male' : 'Female' }" sortable="true" headerClass="sortable" /> 
			<display:column title="Email" property="emailid" sortable="true" headerClass="sortable" /> 
			<display:column title="In Time" value="${row.picklat==''? 'NA' : row.login }" sortable="true" headerClass="sortable" />
			<display:column title="Route (IN)" value="${row.picklat==''? 'NA' : row.inroute }" sortable="true" headerClass="sortable" />
			<display:column title="Out Time" value="${row.picklat==''? 'NA' : row.logout }" sortable="true" headerClass="sortable" />
			<display:column title="Route (OUT)" value="${row.picklat==''? 'NA' : row.outroute }" sortable="true" headerClass="sortable" />
			<display:column title="Dist home-Pickup" value="${row.picklat==''? 'NA' : row.hometopickdistance }" sortable="true" headerClass="sortable" />
			<display:column title="Dist home-Drop" value="${row.picklat==''? 'NA' : row.hometodropdistance }" sortable="true" headerClass="sortable" />
			<display:column title="Dist Pickup-Drop" value="${row.picklat==''? 'NA' : row.pickuptodropdistance }" sortable="true" headerClass="sortable" />
			<display:column title="Pickup - per admin" value="${row.picklat==''? 'NA' : row.distanceperadmin }" sortable="true" headerClass="sortable" />
			<display:column title="Drop - per admin" value="${row.picklat==''? 'NA' : row.distanceperadminout }" sortable="true" headerClass="sortable" />

				<display:setProperty name="export.csv.filename" value="<%=fname1%>" />
				<display:setProperty name="export.excel.filename" value="<%=fname2%>" />
				<display:setProperty name="export.xml.filename" value="<%=fname3%>" />
			</display:table>
				<%-- <table>
				<%
				int m=1;
				for(GeoTagDto gdto : list){ %>
				<tr>
				<td>addr</td>
				<td><input type="text" id="<%=m %>" value="5"/></td>
				</tr>
				<%m++; }%>
				</table> --%>
				</div>
				<div class="footer-wrap">
					<div class="row">
						<div class="col-sm-12 text-center">
							 <p class="text-12">The information stored on this website is maintained in accordance with the organization's Data Privacy Policy. </span><br />Copyright © 2016 siemens
 
						</div>
					</div>
					
				</div>
				
		
				</div>
			</div>
	
		</div>
	</div>
</div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script type="text/javascript" src="js/jquery-latest.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
     <script type="text/javascript">
    var ad;
    function getAddr(){
/* 		var script = document.createElement("script");

		script.type = "text/javascript";
		script.src = "https://maps.googleapis.com/maps/api/js?sensor=true&libraries=places&client=gme-leptonsoftwareexport&signature=1t2jNPl7sIPdevsQdfKNrx25bko=&callback=getAddress";			
		document.body.appendChild(script); */
    }
    function getAddress(){
    	<%-- var geocoder = new google.maps.Geocoder;
    	var  k=1;
    	<%
    	for(GeoTagDto gdto : list){
			%>
				
			var lalnToConv = {lat: parseFloat('<%=gdto.getPicklat() %>'), lng: parseFloat('<%= gdto.getPicklong()%>')};
			geocoder.geocode({'location': lalnToConv}, function(results, status) {
		          if (status === google.maps.GeocoderStatus.OK) {
		            if (results[1]) {
		             
		              alert(results[1].formatted_address);
		            //  alert( document.getElementById(k).value);
		              document.getElementById(k).value=""+results[1].formatted_address;
		            } else {
		              window.alert('No results found');
		            }
		          } else {
		            window.alert('Geocoder failed due to: ' + status);
		          }
		        });
			k++;
			<%
			}
    	
    	%>
     --%>}
    </script>
  </body>
</html>