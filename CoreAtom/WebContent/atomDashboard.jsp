
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
    <title>Dashboard</title>
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
<%try{ %>  
  <div class="wrapper">
  <%

	 response.setIntHeader("Refresh",120);  
        long empid=0;
        String employeeId = OtherFunctions.checkUser(session);
        if (employeeId == null||employeeId.equals("null") ) {
            String param = request.getServletPath().substring(1) + "___"+ request.getQueryString(); 	response.sendRedirect("index.jsp?page=" + param);
        } else {
            empid = Long.parseLong(employeeId);
           
        }
        EmployeeDto empDto = new EmployeeService().getEmployeeAccurate(employeeId);
        
        OtherDao ob = null;
        ob = OtherDao.getInstance();
        
        ArrayList<LiveTrackingDto> list=new LiveTrackingService().getNext3ShiftTimes();
       	String date[]=new String[3];
       	String logtype[]=new String[3];
        String logtime[]=new String[3];
        int i=0;
        for(LiveTrackingDto dto:list){
        	date[i]=dto.getDate();
        	logtype[i]=dto.getLogtype();
        	logtime[i]=dto.getLogtime();
        	
        	i++;
        }
        
        LiveTrackingDto adhocdto=new LiveTrackingService().getAdhocDetails(logtime[0],logtype[1],date[0]);
      
        LiveTrackingDto dt =new LiveTrackingService().currentShiftDetails();
		
		 
       
/*         ArrayList<LiveTrackingDto> lastio=new LiveTrackingService().getLastInandOut();
		String lastshift[]=new String[2]; 
	    String lastlog[]=new String[2];
		int j=0;
        for(LiveTrackingDto dto:lastio){
        	lastlog[j]=dto.getLogtype();
        	lastshift[j]=dto.getLogtime();
        	
        	j++;
        }
        LiveTrackingDto lastdto1 =  new LiveTrackingService().getLastInOutDetails(lastshift[0],lastlog[0]);
        LiveTrackingDto lastdto2=  new LiveTrackingService().getLastInOutDetails(lastshift[1],lastlog[1]); */
        
                   
         
          

          LiveTrackingDto indto =  new LiveTrackingService().getLastInOutDetails("IN");
          LiveTrackingDto outdto=  new LiveTrackingService().getLastInOutDetails("OUT");
         


          
          
          LiveTrackingDto vehdto1 =  new LiveTrackingService().getVehicleStatus(logtime[0],logtype[0],date[0]);
          LiveTrackingDto vehdto2 =  new LiveTrackingService().getVehicleStatus(logtime[1],logtype[1],date[1]);
         
  
 
           
          %>
	
	<header>
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-4">
 
                
          
					<a href="#" class="header-logo al">
						<img src="images/logo_white.png" alt="atom" />
					</a>
				</div>
                
               <div class="pull-right">
             
                <div class="collapse navbar-collapse" id="myNavbar">
       
      <ul class="nav navbar-nav account_menu">
      
        <li>
         <a class="dropdown-toggle user-account" data-toggle="dropdown" href="#"> <img src="images/agile.png" class="pro_photo al">  
      <%="Welcome "+empDto.getDisplayName() %> <span class="caret"></span></a>
        <ul class="dropdown-menu">
            <li><a href="Logout" >Logout</a></li>
         </ul> 
        
        </li>
      </ul>
    </div> 
       
             </div>
            
       
      
   
   
                
		</div>
	</header>
	
	<div class="page-main-heading mar-top-40 mar-btm-25 overflow-hidden">
		<div class="col-sm-5">
			<h2 class="text-uppercase text-blue mar-top-5 text-regular"><i>Dashboard</i></h2>
		
		</div>
		<div class="col-sm-7 text-right">
			<button class="btn btn-blue font-16" onClick="location.reload();"><img src="images/icon_refresh.png"  />&nbsp;&nbsp;REFRESH</button>
			<a href="livetracking.jsp"><button class="btn btn-blue font-16" ><img src="images/icon_dashboard.png"  />&nbsp;&nbsp;LiveTracking</button></a>
			<a href="employee_home.jsp"><button class="btn btn-blue font-16"><img src="images/icon_home.png"  />&nbsp;&nbsp;HOME</button>&nbsp;&nbsp;</a>
        
		</div>		
	</div>
	
	<div class="content-wrapper">
 
    
    
      
      
   <div class="col-sm-5">
          <div class="white-box">
        
      <h6 class="text-uppercase text-semi-bold"><img src="images/lv_track_icon.png">&nbsp;&nbsp; Upcoming Trip</h6>  
      
      <table class="table table-condensed upcoming-trip mar-top-20">
    <thead>
      <tr>
        <th class="text-left" width="15%">Trip</th>
        <th class="text-left" width="20%">Schedule</th>
        <th class="text-center" width="30%">Routing</th>
        <th class="text-right" width="25%">Vehicle Allocation</th>
      </tr>
    </thead>
    <tbody>
         <%  if(logtime[0] !=null) {%>
  
         <tr>
        <td><%=logtime[0]+" "+logtype[0] %> </td>
        <td colspan="3"> 
        <div class="routing">
        <div class="rout-round rout-round-green"></div>
        <div class="route-line"> <div class="rout-fil" style="width:100%;"></div></div> 
        <div class="rout-round rout-round-green"></div>
         <div class="route-line"> <div class="rout-fil" style="width:100%;"></div></div> 
         <div class="rout-round rout-round-green"></div>
          </div>
        
           </td> 
      </tr>
      <%} if(logtime[1] !=null) {%>
  
      <tr>
   
        <td><%=logtime[1]+" "+logtype[1] %> </td>
        <td colspan="3"> 
        <div class="routing">
        <div class="rout-round rout-round-green"></div>
        <div class="route-line"> <div class="rout-fil" style="width:100%;"></div></div> 
        <div class="rout-round rout-round-green"></div>
         <div class="route-line"> <div class="rout-fil" style="width:100%;"></div></div> 
         <div class="rout-round rout-round-green"></div>
          </div>
        
           </td> 
           
      </tr>
         <% } if(logtime[2] !=null) {%>
   
      <tr>
        <td><%=logtime[2]+" "+logtype[2] %>  </td>
       <td colspan="3"> 
        <div class="routing">
        <div class="rout-round rout-round-green"></div>
         <div class="route-line"> <div class="rout-fil" style="width:100%;"></div></div> 
        <div class="rout-round rout-round-green"></div>
         <div class="route-line"> <div class="rout-fil" style="width:100%;"></div></div> 
          <div class="rout-round rout-round-green"></div>
          </div>
        
           </td> 
     
      </tr>
           <% }%>
   
    </tbody>
  </table> 
     
			
      </div>
      
 		</div>
	 
  
		<div class="col-sm-7">
			
            <div class="white-box">
            <h6 class="text-uppercase text-semi-bold"> <img src="images/vehicle_allocation.png" />&nbsp;&nbsp; Vehicle Allocation</h6>
            <div class="under-line mar-top-20"></div>
              <%if(logtime[0]!=null){ %>
  
        <div class="col-md-6 mar-top-20 "> 
           
       
            <div class="vehicle-inandout">
            <% if(logtype[0].equalsIgnoreCase("IN")){  %>
            <h3>IN [ <img src="images/vehicle_in.png"> </h3>
            <%} else{ %>
             <h3> OUT ] <img src="images/vehicle_out.png"> </h3>
            <%} %> 
         <table class="table table-condensed mar-top-15">
    <thead>
      <tr> 
        <th class="text-center">Time </th>
        <th class="text-center">Allocated</th>
        <th class="text-center">Pending</th>
        <!--  <th class="text-center">Count Down</th> -->
       </tr>
    </thead> 
    <tbody>
    <tr></tr><tr></tr>
      <tr>
        <td><div class="mar-top-10" align="center"><%=logtime[0] %></div> </td>
         <td> <sapn class="green_round"><%=vehdto1.getVehallocated() %></span>  </td> 
        <td> <sapn class="red_round"><%=vehdto1.getVehpending() %></span>  </td> 
        <!-- <td> <sapn class="count-down">03 : 59</span>  </td>  -->
       </tr>
       
       
    </tbody>
  </table> 
             
             </div>
            </div>
            <div class="under-line mar-top-20"></div>
            <%}if(logtime[1]!=null){ %>
            <div class="col-md-6 mar-top-20 ">
            <div class="vehicle-inandout">
            <% if(logtype[1].equalsIgnoreCase("IN")){  %>
            <h3>IN [ <img src="images/vehicle_in.png"> </h3>
            <%} else{ %>
             <h3> OUT ] <img src="images/vehicle_out.png"> </h3>
            <%} %>     
         <table class="table table-condensed mar-top-15">
    <thead>
      <tr> 
        <th class="text-center">Time </th>
        <th class="text-center">Allocated</th>
        <th class="text-center">Pending</th>
       <!--  <th class="text-center">Count Down</th> -->
      </tr>
    </thead> 
    <tbody>
    
      <tr>
        <td><div class="mar-top-10"  align="center"><%=logtime[1] %></div> </td>
        <td> <sapn class="green_round"  ><%=vehdto2.getVehallocated() %></span>  </td> 
        <td> <sapn class="red_round"><%=vehdto2.getVehpending() %></span>  </td> 
         <!-- <td> <sapn class="count-down">03 : 59</span>  </td>  -->
       </tr>
       
       
    </tbody>
  </table> 
             
             </div>
            </div>
     
           <%} %>
            
           <!--  <h6 class="text-uppercase text-semi-bold"> <a href="#" class="view-more ar"  onClick="allocation_function()">»View All </a></h6> -->
      </div>
		</div>	 
			 
		
		
 <div class="clearfix"></div>
 
 
 
      <div class="col-md-5 mar-top-20">
      <div class="white-box">
            <h6 class="text-uppercase text-semi-bold"> <img src="images/adhoc_request_icon.png">&nbsp;&nbsp; Adhoc Request
            <a href="#" class="view-more ar" onClick="adhoc_function()">»View All Request </a></h6>
            <div class="under-line mar-top-15 "></div>
            
            
             <div class="request-box">
            <div class="request-box-line">
            <div class="request-scheduled mar-top-20"><%=adhocdto.getAdhocrequest() %></div>
            </div>
            <h5>Total  </h5>
            </div>
            
            <div class="request-box">
             <div class="request-box-line">
             <div class="request-total mar-top-20"><%=adhocdto.getAdhocapproved() %></div>
             </div>
            <h5>Scheduled</h5>
            </div>
            
            <div class="request-box">
            <div class="request-box-line">
            <div class="request-pending mar-top-20"><%=adhocdto.getAdhocpending() %></div>
            </div>
            <h5>Pending </h5>
            </div>
            
             <div class="request-box">
             <div class="request-box-line">
             <div class="request-pendingnext mar-top-20"><%=adhocdto.getNextadhocpending()%></div>
             </div>
            <h5>Pending @  <%=logtime[0]+" "+logtype[0]%></h5>
            </div>
    
      </div>
      
      </div>
      
       <div class="col-md-7 mar-top-20">
     <% if( dt!=null) { %>     
      <div class="white-box">
            <h6 class="text-uppercase text-semi-bold"> <img src="images/current_trip_status.png">&nbsp;&nbsp; Current Trip Status
            	
            
            <a href="#" class="view-more ar"  onClick="current_trip_function('<%=dt.getDate()%>','<%=dt.getLogtime() %>','<%=dt.getLogtype() %>')">»View Details</a></h6>
             <div class="under-line mar-top-15 "></div>
            <h5 class="shift-time text-center mar-top-10" > Shift <%=dt.getLogtime()+" "+dt.getLogtype()%>  </h5>
            
             <div class="curent-staus-box">
             <div class="curent-staus-box-line">
             <div class="staus-total mar-top-20"><%=dt.getTotal()%></div>
             </div>
         <h5>    Total </h5>
           
            </div>
            
            
            <div class="curent-staus-box">
            <div class="curent-staus-box-line">
            <div class="staus-download mar-top-20"><%=dt.getDownloaded()%></div>
            </div>
            <h5>  Downloaded   </h5>
            </div>
            <div class="curent-staus-box">
             <div class="curent-staus-box-line">
             <div class="staus-notstarted  mar-top-20"><%=dt.getNotstarted()%></div>
             </div>
            <h5> Not Started  </h5>
            </div>
            
            <div class="curent-staus-box">
            <div class="curent-staus-box-line">
            <div class="staus-started mar-top-20"><%=dt.getStarted()%></div>
            </div>
            <h5>Running  </h5>
            </div>
            
            <div class="curent-staus-box">
             <div class="curent-staus-box-line">
             <div class="staus-completed mar-top-20"><%=dt.getCompleted()%></div>
             </div>
            <h5> Completed </h5>
            </div>
            
            <div class="curent-staus-box">
             <div class="curent-staus-box-line">
             <div class="staus-notcompleted mar-top-20"><%=dt.getNotdownloaded()%></div>
             </div>
            <h5> Not Downloaded </h5>
            </div>
    
      </div>
     <%  } %>   
      </div>

       

 <div class="col-md-5 mar-top-20">
  <div class="white-box">
            <h6 class="text-uppercase text-semi-bold"> <img src="images/woman_employee.png">&nbsp;&nbsp; Women Employee Travel Status </h6>
            
            <div class="row">
 			  <div class="col-md-6 travel-staus mar-top-15">
        <%if ((indto.getLogtime()!=null)||(indto.getLogtime().equalsIgnoreCase("")))  {%>
     <h5 class="text-center"> IN [ <img src="images/vehicle_in.png"> </h5> 
             <p class="text-center mar-top-10">   <a  onClick="employee_function('<%=indto.getLogtime() %>','IN','Women')" >  <u><%=indto.getLogtime() %></u>   </a></p>
     <div class="clearfix"></div>
     
     
    
     <div id="canvas-holder">
			<canvas id="chart-area" width="500" height="500"/>
          
		</div>




     	<%} %>
     
     
  <!--<div class="framework-container  mar-top-25"> 
<div class="example" onpagenavigation id="demo"> <div>    <div class="all-value">30</div></div></div>  
</div>-->
 
               </div>
               
               <div class="col-md-6 travel-staus mar-top-15">
     
        <%if ((outdto.getLogtime()!=null)||(outdto.getLogtime().equalsIgnoreCase("")))  {%>

             <h5 class="text-center"> Out [ <img src="images/vehicle_out.png"> </h5> 
             <p class="text-center mar-top-10"> <a  onClick="employee_function('<%=outdto.getLogtime() %>','OUT','Women')" ><u><%=outdto.getLogtime()%></u></a></p>
             
             
 
 
     <div id="canvas-holder">
       <canvas id="chart-area2" width="100" height="100"/>
          
		</div>
        
             	<%} %>
        
             
               </div>
               
              </div>
            
<div class="legents-wrap mar-top-10">
					<ul class="legents-wrap pull-right">
						<!--<li class="bg-color7">Total</li>-->
						<li class="bg-color8">Boarded</li>
						<li class="bg-color9">Reached</li>
					</ul>
				</div>    
            
 
 </div>
 
 
 </div>
       
       
<div class="col-md-7 mar-top-20">

<div class="row">
<div class="col-md-6">
  <div class="white-box">
            <h6 class="text-uppercase text-semi-bold"> <img src="images/lv_tack_aa_emp.png">&nbsp;&nbsp; All Employee Travel Status</h6>
            
            <div class="row">
            
 			  <div class="col-md-6 travel-staus mar-top-15">
      
              <%if ((indto.getLogtime()!=null)||(indto.getLogtime().equalsIgnoreCase("")))  {%>
            
         <h5 class="text-center"> IN [ <img src="images/vehicle_in.png"> </h5> 
           
    <p class="text-center mar-top-10">   <a  onClick="employee_function('<%=indto.getLogtime() %>','IN','ALL')" >  <u><%=indto.getLogtime() %></u>   </a></p>
  				  <div id="canvas-holder">
       <canvas id="chart-area3" width="500" height="500"/>
          
		</div>
               	<%} %>
        
             
               </div>
               
               <div class="col-md-6 travel-staus mar-top-15">
   <%if ((outdto.getLogtime()!=null)||(outdto.getLogtime().equalsIgnoreCase("")))  {%>
        
             <h5 class="text-center"> Out [ <img src="images/vehicle_out.png"> </h5> 
            
               <p class="text-center mar-top-10"> <a  onClick="employee_function('<%=outdto.getLogtime() %>','OUT','ALL')" ><u><%=outdto.getLogtime()%></u></a></p>
               <div id="canvas-holder">
       <canvas id="chart-area4" width="500" height="500"/>
          
		</div>
     	<%} %>
        
  				  
             
               </div>
               
               </div>
          
            
<div class="legents-wrap mar-top-10">
					<ul class="legents-wrap pull-right">
						<!--<li class="bg-color7">Total</li>-->
						<li class="bg-color8">Boarded</li>
						<li class="bg-color9">Reached</li>
					</ul>
				</div>    
            
 
 </div>
 
 </div>
 
 <div class="col-md-6">
  <div class="white-box">
            <h6 class="text-uppercase text-semi-bold"> <img src="images/trip_staus.png">&nbsp;&nbsp; TRIP Status </h6>
            
            <div class="row">
 			  <div class="col-md-6 travel-staus mar-top-15">
       
        <%if ((indto.getLogtime()!=null)||(indto.getLogtime().equalsIgnoreCase("")))  {%>
          
             <h5 class="text-center"> IN [ <img src="images/vehicle_in.png"> </h5> 
                       <p class="text-center mar-top-10"> <a  onClick="current_trip_function('<%=dt.getDate()%>','<%=indto.getLogtime() %>','IN')" ><u><%=indto.getLogtime()%></u></a></p>
        
             
             
               <div id="canvas-holder">
       <canvas id="chart-area5" width="500" height="500"/>
          
		</div>
              <%} %>
  				  
             
               </div>
               
               <div class="col-md-6 travel-staus mar-top-15">
   <%if ((outdto.getLogtime()!=null)||(outdto.getLogtime().equalsIgnoreCase("")))  {%>

             <h5 class="text-center"> Out [ <img src="images/vehicle_out.png"> </h5> 
     
                    <p class="text-center mar-top-10"> <a  onClick="current_trip_function('<%=dt.getDate()%>','<%=outdto.getLogtime()  %>','OUT')" ><u><%=outdto.getLogtime() %></u></a></p>
        
           
             
               <div id="canvas-holder">
       <canvas id="chart-area6" width="500" height="500"/>
          
		</div>
              <%}%>


  				  
             
               </div>
               
               </div>
                
<div class="legents-wrap mar-top-10">
					<ul class="legents-wrap pull-right">
						<!--<li class="bg-color7">Total</li>-->
						<li class="bg-color8">Started</li>
						<li class="bg-color9">Completed</li>
					</ul>
				</div>    
            
 
 </div>
 
 </div>
 
 </div>
 
 </div>      
      
        
		
	</div>
    
    
  
    
	
  </div>
  
  
<!-- <script src="http://code.jquery.com/jquery-latest.min.js"></script> -->        

  
  
  
  
  
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script> 
    <!-- Include all compiled plugins (below), or include individual files as needed -->
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js" integrity="sha512-K1qjQ+NcF2TYO/eI3M6v8EiNYZfA95pQumfvcVrTHtwQVDG+aHRqLi/ETn2uB+1JqwYqVG3LIvdm9lj6imS/pQ==" crossorigin="anonymous"></script> 
	
    
    
	<script src="js/Chart.Core.js"></script>
		<script src="js/Chart.Doughnut.js"></script> 
     
     
    
    <script>

		var doughnutData = [
				{
					value: <%=indto.getRechfemale()%>,
					label: "Reached ",
					color:"#96d24c"
				},
				{
					value: <%=indto.getBoardfemale()%>,
					label: "Boarded ",
					color:"#f8a20f"
				}
				];
			
			var doughnutData2 = [
				{
					value: <%=outdto.getRechfemale()%>,
					label: "Reached ",
					color:"#96d24c"
				},
				{
					value: <%=outdto.getBoardfemale()%>,
					label: "Boarded ",
					color:"#f8a20f"
				} 
				];
			
			var doughnutData3 = [
				{
					value: <%=indto.getRechemp()%> ,
					label: "Reached ",
					color:"#96d24c"
				},
				{
					value: <%=indto.getBoardemp()%>,
					label: "Boarded ",
					color:"#f8a20f"
				} 

			];
			
			var doughnutData4 = [
				{
					value: <%=outdto.getRechemp() %>,
					label: "Reached ",
					color:"#96d24c"
				},
				{
					value: <%=outdto.getBoardemp() %>,
					label: "Boarded ",
					color:"#f8a20f"
				}
			];
			
			var doughnutData5 = [
				{
					value: <%=indto.getStarted()%>,
					label: "Started",
					color:"#f8a20f"
				},
				{
					value: <%=indto.getCompleted()%>,
					label: "Completed ",
					color:"#96d24c"
				}
			];
			
			var doughnutData6 = [
				{
					value: <%=outdto.getStarted()%>,
					label: "Started ",
					color:"#f8a20f"
				},
				{
					value: <%=outdto.getCompleted()%>,
					label: "Completed ",
					color:"#96d24c"
				} 
			];
			
			

			window.onload = function(){
				var ctx = document.getElementById("chart-area").getContext("2d"); 
				var ctx2 = document.getElementById("chart-area2").getContext("2d"); 
				var ctx3 = document.getElementById("chart-area3").getContext("2d");
				var ctx4 = document.getElementById("chart-area4").getContext("2d");
				var ctx5 = document.getElementById("chart-area5").getContext("2d");
				var ctx6 = document.getElementById("chart-area6").getContext("2d");
				
				window.myDoughnut = new Chart(ctx).Doughnut(doughnutData, {responsive : true});
				window.myDoughnut2 = new Chart(ctx2).Doughnut(doughnutData2, {responsive : true});
				window.myDoughnut3 = new Chart(ctx3).Doughnut(doughnutData3, {responsive : true});
				window.myDoughnut4 = new Chart(ctx4).Doughnut(doughnutData4, {responsive : true});
				window.myDoughnut5 = new Chart(ctx5).Doughnut(doughnutData5, {responsive : true});
				window.myDoughnut6 = new Chart(ctx6).Doughnut(doughnutData6, {responsive : true});
			};



	</script>
     
      
    <% }  catch(Exception e){
    	
    	response.sendRedirect("employee_home.jsp");
    }
    
    
    %>
    
<script src="js/piechart.js"></script>
   
 
   <script type="text/javascript">
  
/*  function allocation_function(){

	 alert("allocation_function");
	 try{ 
			
			var url="view_routing.jsp";
			var size = "height=600,width=1200,top=100,left=60,toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
			var newwindow=window.open(url,size);
			
			}catch(e) {
				alert(e);
			}

} */
  function adhoc_function(){
	
			try{ 
				
				var url="viewNotificationForTransport.jsp";
				var size = "height=600,width=1200,top=100,left=60,toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
				var newwindow=window.open(url,size);
				
				}catch(e) {
					alert(e);
				}

		 
}
 
 function current_trip_function(shiftdate,shifttime,shiftlog){
	 
	 
	 try{ 
		
		 
	
	 
		    
               var url="currentTripStatus.jsp?&triptime="+shifttime+"&date="+shiftdate+"&triplog="+shiftlog;
			
			var size = "height=600,width=1200,top=100,left=60,toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
			var newwindow=window.open(url,size);
			
			}catch(e) {
				alert(e);
			}
	
}
 
function employee_function(time,log,filter){
	 
	 var filter;
	 try{ 
		 
		    
	     var url="employeeTravelStatus.jsp?triplog="+log+"&triptime="+time+"&filter="+filter;
			
			var size = "height=600,width=1200,top=100,left=60,toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
			var newwindow=window.open(url,size);
			
			}catch(e) {
				alert(e);
			}
	
}
 </script> 
   
   
   
   
   
   
 
 
	
  </body>
</html>