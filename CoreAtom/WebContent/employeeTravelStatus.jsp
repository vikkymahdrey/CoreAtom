
<!DOCTYPE html>
<html lang="en">
<%@page import="com.agiledge.atom.dao.OtherDao"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions" %>
<%@page import="com.agiledge.atom.dashboard.service.LiveTrackingService" %>
<%@page import="com.agiledge.atom.dashboard.dto.*" %>
<%@page import="com.agiledge.atom.dto.LogTimeDto" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.usermanagement.service.PageService"%>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>EmployeeStatus</title>
	<link rel="icon" href="images/agile.png" type="image/x-icon" />
	<link href='https://fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic' rel='stylesheet' type='text/css'>
    
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
	
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" integrity="sha512-dTfge/zgoMYpP7QbHy4gWMEGsbsdZeCXz7irItjcC3sPUFtf0kuFbDz/ixG7ArTxmDjLXDmezHubeNikyKGVyQ==" crossorigin="anonymous">
	
	
	<link rel="stylesheet" href="css/atomDashboard.css">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
  <%
    
    String triplog=request.getParameter("triplog");
    String triptime=request.getParameter("triptime");
    String filter=request.getParameter("filter");
    
    ArrayList<LiveTrackingDto> list=new LiveTrackingService().getEmployeeStatus(filter,triptime,triplog);
    
    %>
  <div class="wrapper">
	  
	
	 
	
	<div class="content-wrapper mar-top-20">
		<div class="col-sm-12">
			<div class="white-box">
			 
		 
			 <h6 align="center" ><img src="images/lv_tack_emp_travel_status.png" />&nbsp;&nbsp;<i>Employee Travel Status</i> </h6>
                  
				<div class=" mar-top-15" align="center"><span class="glyphicon glyphicon-time"></span>&nbsp;&nbsp;<%=triptime+" "+triplog %></div>
                
<%  if (list.size()>0){  %>                
                    
             <table class="table table-bordered mar-top-15">
    <thead>


      <tr>
        <th>Employee Name</th>
        <th>Personnel Number</th>
        <th>Gender</th>
        <th>Status </th>
         <th>Trip ID </th>
        <th>Vehicle No </th>
        <th>Driver Name</th>
        <th>Driver Contact</th>
        
    
    
      </tr>
    </thead>
    <tbody>
 
 <%  

 for(LiveTrackingDto dto:list){
	 %>
      <tr>
        <td><%=dto.getEmpname() %></td>
        <td><%=dto.getEmppersonnelno() %></td>
        <td><%=dto.getGender()%></td>
        <td><%=dto.getStatus()%></td>
        <td><%=dto.getTripid()%></td>
        <td><%=dto.getVehicle() %></td>
        <td><%=dto.getDriverName() %></td>
        <td><%=dto.getDriverContact() %></td>
       
      </tr>
      
<%
       
      } }
 else{
	 %> 
	<P align="center"><i> No Employee is boarded at this time!!!!</i></P>
	
	 <%
 }
 %>     
    </tbody>
  </table>
                    
                    
 	</div>
			
			
			 
			
           
			 
			
		</div>
		
		 
		
		
	</div>
	
  </div>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js" integrity="sha512-K1qjQ+NcF2TYO/eI3M6v8EiNYZfA95pQumfvcVrTHtwQVDG+aHRqLi/ETn2uB+1JqwYqVG3LIvdm9lj6imS/pQ==" crossorigin="anonymous"></script>
	
	<script src="http://maps.googleapis.com/maps/api/js"></script>
	
	<script>
function initialize() {
  var mapProp = {
    center:new google.maps.LatLng(12.9542946,77.4908531),
    zoom:12,
    mapTypeId:google.maps.MapTypeId.ROADMAP
  };
  var map=new google.maps.Map(document.getElementById("googleMap"),mapProp);
}
google.maps.event.addDomListener(window, 'load', initialize);
</script>
	
  </body>
</html>