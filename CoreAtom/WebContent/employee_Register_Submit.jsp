<%@page import="com.agiledge.atom.dao.ProjectDao"%>
<%@page import="com.agiledge.atom.service.EmployeeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeDto"%>
<%@page import="com.agiledge.atom.dto.ProjectDto"%>
<%@page import="com.agiledge.atom.commons.OtherFunctions"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.agiledge.atom.service.EmployeeSubscriptionService"%>
<%@page import="com.agiledge.atom.service.LogTimeService"%>
<%@page import="com.agiledge.atom.dto.EmployeeSubscriptionDto"%>
<%@page import="com.agiledge.atom.constants.SettingsConstant"%>
<%@page import="com.agiledge.atom.dto.LogTimeDto"%>
<%@page import="com.agiledge.atom.dao.ShuttleSocketDao"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Employee Details</title>
    <script type="text/javascript" src="js/jquery-latest.js"></script>
    
<script>


function trimFunction() {
	
	var addr=document.getElementById("addr").value;
	document.getElementById("addr").value=addr.trim();
	
    }

function showPopup() {
	
	document.getElementById("alertwarn").innerHTML ="<p align=center > <b><i><u>Disclaimer</u></i></b></p> &nbsp;&nbsp;&nbsp;&nbsp;The employee is solely responsible for providing and for the accuracy of any and all personal information, including the address and contact details of their boarding and de-boarding locations. ";
	
	
	$('#validationAlertModal').modal();
	
	
    }
    
function searchPopup(url) {
	var params = "toolbars=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
	size = "height=400,width=400,top=200,left=600," + params;

	newwindow = window.open(url, 'name', size);

	if (window.focus) {
		newwindow.focus();
	}
} 
function checkproject(){
	
	var proj = document.getElementById("project");
	document.getElementById("projectunit").value = proj.options[proj.selectedIndex].text;

	/* if (document.getElementById("projectunit").value == "O & I") {

		document.getElementById("inTime").innerHTML = "<select name='inTime' id='inTime'  ><option value=''>Select Time</option>"
			+ "<option value='07:00'>07:00</option> <option value='08:30'>08:30</option> <option value='13:00'>13:00</option> "
			+ "</select>";

		document.getElementById("outTime").innerHTML = "<select name='inTime' id='inTime'  ><option value=''>Select Time</option>"
			+ "<option value='16:00'>16:00</option> <option value='17:00'>17:00</option> <option value='22:00'>22:00</option> "
			+ "</select>";
	}
	else{

			
		document.getElementById("inTime").innerHTML = "<select name='inTime' id='inTime'  ><option value=''>Select Time</option>"
			+ "<option value='08:30'>08:30</option> <option value='10:00'>10:00</option>"
			+ "</select>";

		document.getElementById("outTime").innerHTML = "<select name='inTime' id='inTime'  ><option value=''>Select Time</option>"
			+ " <option value='17:00'>17:00</option> <option value='19:00'>19:00</option> "
			+ "</select>";
			
	} */




}
function validate(){
 $('.san').show();
 
 var fname=document.getElementById("fname").value;
var lname=document.getElementById("lname").value;
var gender=document.getElementById("gender").value;
var email=document.getElementById("email").value;
var mob=document.getElementById("mob").value;
var addr=document.getElementById("addr").value;
document.getElementById("addr").value=addr.trim();
var city=document.getElementById("city").value;
var state=document.getElementById("state").value;
//var zip=document.getElementById("zip").value;
var manager=document.getElementById("supervisorName1").value;
var project=document.getElementById("project").value;
/* var inTime=document.getElementById("inTime").value;
var outTime=document.getElementById("outTime").value; */





 var homelatlong=document.getElementById("homelatlong").value; 

if(fname=="" || fname==null || fname==" "){
	document.getElementById("errortag").innerHTML ="Please enter your first name";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#fname').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("fname").focus();
	return false;
}else if(lname=="" || lname==null || lname==" "){
	document.getElementById("errortag").innerHTML ="Please enter your last name";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#lname').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("lname").focus();
	return false;
}else if(email=="" || email==null || email==" "){
	document.getElementById("errortag").innerHTML ="Please enter your  email id";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#email').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("email").focus();
	return false;
}else if(mob=="" || mob==null || mob.length!=10 ||mob<0){
	document.getElementById("errortag").innerHTML ="Please enter a valid mobile number";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#mob').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("mob").focus();
	return false;
}
else if(manager=="" || manager==null || manager==" " || manager.length<=0){
	document.getElementById("errortag").innerHTML ="Please select your Manager";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#supervisor').removeClass( "form-control" ).addClass( "validation-required" );
	return false;
}
else if(project=="" || project==null || project==" " ||project==0){
	document.getElementById("errortag").innerHTML ="Please select your Project";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#project').removeClass( "form-control" ).addClass( "validation-required" );
	
	return false;
} 
else if(addr=="" || addr==null || addr==" " ){
	document.getElementById("errortag").innerHTML ="Please enter your address";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#addr').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("addr").focus();
	return false;
}else if (/^\s+$/.test(addr)){
	document.getElementById("errortag").innerHTML ="Please enter your address";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#addr').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("addr").focus();
	return false;
}else if(city=="" || city==null || city==" "){
	document.getElementById("errortag").innerHTML ="Please enter city name";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#city').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("city").focus();
	return false;
}else if(state=="" || state==null || state==" "){
	document.getElementById("errortag").innerHTML ="Please enter your State";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#state').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("state").focus();
	return false;
}else if(homelatlong=="" || homelatlong==null || homelatlong==" " ||homelatlong=="(null, null)" ||homelatlong=="(, )" ){
	document.getElementById("errortag").innerHTML ="Please pin your address on map";
	document.getElementById("alertwarn").innerHTML ="Please pin your address on map";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#validationAlertModal').modal();
	return false;
} 
/* else if(inTime=="" || inTime==null || inTime==" "){
	document.getElementById("errortag").innerHTML ="Please select shift time[IN]";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#inTime').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("inTime").focus();
	return false;
}else if(outTime=="" || outTime==null || outTime==" "){
	document.getElementById("errortag").innerHTML ="Please select shift time[OUT]";
	$('.validation-required').removeClass( "validation-required" ).addClass( "form-control" );
	$('#outTime').removeClass( "form-control" ).addClass( "validation-required" );
	document.getElementById("outTime").focus();
	return false;
} */
 
else if (!document.UserDetails.termsAndConditions.checked==true) {
	document.getElementById("alertwarn").innerHTML =" Please accept Disclaimer ";
	document.getElementById("errortag").innerHTML ="Please accept Disclaimer ";	
	$('#validationAlertModal').modal();
	return false;
}else{ 
	return true;
}
}
</script>
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
  <body onload="trimFunction()">
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
						EmployeeDto dto = new EmployeeSubscriptionService()
						.getEmpRegisterDetails(employeeId);
						
						String genderm="checked='checked'",genderf="";
						if(dto.getGender().equalsIgnoreCase("f")){
							genderf="checked='checked'" ;
							genderm="";
						}
						 /* ArrayList<LogTimeDto> inLog = new LogTimeService()
							.getAllLogtime("IN");
					ArrayList<LogTimeDto> outLog = new LogTimeService()
							.getAllLogtime("OUT");
					EmployeeDto subdto1 = new ShuttleSocketDao()
					.getShuttlePickUpDrop(employeeId); */
						 
		 	 		String homelatlong = "(" + dto.getLattitude() + ", "
		 					+ dto.getLongitude() + ")"; 
						%>
<div class="wrapper">
	<div class="header-wrap">
		<div class="container">
			<div class="row">
				<div class="col-sm-12 text-right">
					<img src="images/user_iocn_header.png" />&nbsp;Welcome <%=empDtoInHeader.getDisplayName() %> &nbsp;&nbsp;&nbsp;<a href="Logout"><img src="images/logout_icon_header.png" />&nbsp;Log Out</a>
				</div>
			</div>
		</div>
	</div>
	
	<div class="main-page-container">
		<div class="container">	
			<div class="row">
				<div class="col-sm-12">
				
				<div class="breadcrumb-wrap">
					<!-- <a href="employee_home.jsp"><img src="images/home.png" /></a>
					<a href="employee_home.jsp">My Information </a>
					<a href="#" class="current">Edit Information</a> -->
				</div>
				
				<div class="content-wrap">
				<form action="ShuttleSubscribe"  name ="UserDetails" id="UserDetails" method="post">
				<input type="hidden" name="empid" id="empid"
												value="<%=dto.getEmployeeID()%>" />
					<div class="row">
						<div class="col-sm-8 page-heading mar-top-20">
							<i class="page-heading-icon"><img src="images/edit_profile.png" /></i>
							<h5 class="text-blue text-semi-bold">Edit Information</h5>
						</div>
									
					</div>
				<div class="row mar-top-20">
						<div class="col-sm-12">
							<div class="alert alert-danger san" hidden="hidden" style="color: red" ><p id="errortag"></p></div>
						</div>
					</div>
					<div class="section-heading">
						<div class="row">
							<div class="col-sm-12">
								Personal Information
							</div>
						</div>
					</div>
					
					
					<div class="push-15">
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey ">First Name:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15">
											<input type="text" name="fname" id="fname" readonly
							value="<%=dto.getEmployeeFirstName()%>" class="form-control"/>
							 <input type="hidden" name="homelatlong" id="homelatlong" value="<%=homelatlong%>" /> </div> 
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey ">Last Name:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><input type="text" name="lname" id="lname"  readonly value="<%=dto.getEmployeeLastName()%>" class="form-control"  style="width: 150%;"/></div>						
					</div>
					
					
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Employee ID:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><input type="text" id="rollno" name="rollno" readonly value="<%=dto.getPersonnelNo()%>" class="form-control">
						</div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey ">Email:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><input type="text" name="email" id="email" readonly value="<%=dto.getEmailAddress()%>" class="form-control" /></div>
						
		
					</div>
					
					
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">Gender:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15 text-break">Male&nbsp;&nbsp;<input type="radio" name="gender" id="gender"
							value="M" <%=genderm %> />&nbsp;&nbsp;&nbsp;&nbsp;Female&nbsp;&nbsp;<input
							type="radio" name="gender" id="gender" value="F" <%=genderf %>/></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey mandatory">Mobile No:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">
							<input type="text" name="mob" id="mob" value="<%=dto.getContactNo()%>" class="form-control"  style="width: 150%;"/>
						</div>						
					</div>
					
					
		
					
				<div class="row">
				
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">Manager :</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15 text-break"> 
						
						
						<input type="hidden" name="supervisorID1" id="supervisorID1"  value="<%=dto.getLineManager()%>"/> 
						<input
					type="text"  readonly name="supervisorName1" id="supervisorName1" value="<%=dto.getManagerName()%>"
					onclick="searchPopup('SupervisorSearch1.jsp')" class="form-control" />  
						
						
						</div>		
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey mandatory">Project :</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15">
					    <select id="project" name="project" onchange="checkproject();"  class="form-control" style="width: 150%;">
      					<option value="0">Select</option>
						<%
							ArrayList<ProjectDto> projlist = new ProjectDao().getProjects();
							 for (ProjectDto pdto : projlist) {
 								if (dto.getProject().equalsIgnoreCase(pdto.getProject())) {
						%>
						<option value="<%=pdto.getProject()%>" selected	><%=pdto.getDescription()%></option>
						       <%} else { %>
						<option value="<%=pdto.getProject()%>"> <%=pdto.getDescription()%></option>
						      <%}
 								   }
 								     %>



					</select> 
					
						<input type="hidden" id="projectunit" name="projectunit" value="<%=dto.getProjectUnit()%>">

						
						</div>						
					</div>
									
					</div>
					
					
					<div class="section-heading">
						<div class="row">
							<div class="col-sm-12">
							Current Residence Address </div>
						</div>
					</div>
					
					<div class="push-15">
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">Street Address:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15 ">						
						<textarea class="addresstextarea form-control" name="addr" id="addr"><%=dto.getAddress() %>
						</textarea>
						</div>
						
						<div class="col-md-5 col-sm-12 col-md-offset-2">
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">City:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15">
									<input type="text" name="city" id="city" class="form-control" value="<%=dto.getCity()%>" />
								</div>
							</div>
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey mandatory">State:</div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15">
										<input type="text" class="form-control" name="state" id="state" value="<%=dto.getState() %>" />
									
								</div>
							</div>
							
						
							
							<div class="row">
								<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey"></div>
								<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15">
								<a href="#" class="btn btn-light-blue has-icon map-popup-link"  onclick="initialize('home')"  ><i><img src="images/map_icon.png" /></i>Pin Your Home Location</a> 
								</div>
								
							</div>
						
						
						</div>
						
											
					</div>
					</div>
						<%--
					<div class="section-heading">
						<div class="row">
						
							<div class="col-sm-12">
								Shift Information
							</div>
						</div>
					</div>
					
					
					
				 <div class="row">
					
					<div class="row tripping" >
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;In Time:</div>
						<div class="col-md-5 col-sm-7 col-xs-6 mar-top-15"><select name="inTime"
							id="inTime"><option value="">Select Time</option>
						<%
						System.out.println(dto.getProject());
						if (dto.getProject().equalsIgnoreCase("6")) {
							String sevensel="",eightsel="",thirtsel="";
							if(subdto1.getLogin().equalsIgnoreCase("07:00")){
								sevensel="selected";
							}else if(subdto1.getLogin().equalsIgnoreCase("08:30")){
								eightsel="selected";
							}else if(subdto1.getLogin().equalsIgnoreCase("13:00")){
								thirtsel="selected";
							}
							%>
									<option value='07:00' <%=sevensel %>>07:00</option> <option value='08:30' <%=eightsel %>>08:30</option> <option value='13:00' <%=thirtsel %>>13:00</option> 
								<%	
						}else{
						
							String sevensel="",eightsel="",tensel="";
							if(subdto1.getLogin().equalsIgnoreCase("08:30")){
								eightsel="selected";
							}else if(subdto1.getLogin().equalsIgnoreCase("10:00")){
								tensel="selected";
							}
							%>
							<option value='08:30' <%=eightsel %>>08:30</option> <option value='10:00' <%=tensel %>>10:00</option> 
								<%
						
						
						}%></select>
						</div> 
						
						
					
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey ">Out Time:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><select name="outTime"
							id="outTime"><option value="">Select Time</option>
								<%
								if (dto.getProject().equalsIgnoreCase("6")) {
										String sixinsel="",sevinsel="",twtysel="";
										if(subdto1.getLogout().equalsIgnoreCase("16:00")){
											sixinsel="selected";
										}else if(subdto1.getLogout().equalsIgnoreCase("17:00")){
											sevinsel="selected";
										}else if(subdto1.getLogout().equalsIgnoreCase("22:00")){
											twtysel="selected";
										}
									%>
									<option value='16:00' <%=sixinsel %>>16:00</option> <option value='17:00' <%=sevinsel %>>17:00</option> <option value='22:00' <%=twtysel %>>22:00</option> 
									<%
								}else{
									
									String sixinsel="",sevinsel="",twtysel="";
									if(subdto1.getLogout().equalsIgnoreCase("17:00")){
										sevinsel="selected";
									}else if(subdto1.getLogout().equalsIgnoreCase("19:00")){
										twtysel="selected";
									}
								%>
								<option value='17:00' <%=sevinsel %>>17:00</option> <option value='19:00' <%=twtysel %>>19:00</option> 
								<%
								}
								%></select></div>
						
											
					</div>
					</div>
					--%>				
					<div align="center">
					<hr color="white">
						
				<input name="termsAndConditions" type="checkbox" />
							 <a href="#" onclick="showPopup()">
								Disclaimer </a>

					</div>
					
					
					<div class="row">
						<div class="col-sm-12 text-red text-12" style="color: red">*All fields are mandatory</div>
					</div>
					
					<div class="row text-right mar-btm-30">
						<div class="col-sm-11">
							<input type="submit" class="btn btn-blue save-btn" value="Save"  onclick="return validate();"/>&nbsp;&nbsp;
							
						</div>
					</div>
					
					
					
				</form>
				</div>
				
				<div class="footer-wrap">
					<div class="row">
						<div class="col-sm-12 text-center">
							 <p class="text-12">The information stored on this website is maintained in accordance with the organization's Data Privacy Policy. </span><br />Copyright © 2016 Agiledge
 
						</div>
					</div>
					
				</div>
				
		
				</div>
			</div>
	
		</div>
	</div>
	
	
	
	 <div class="popup-wrapper">
		<div class="popup-content-wrap">
			<div class="popup-header">
				Choose Home Location
				<a href="#" class="close-btn">X</a>
			</div>
			
			<div class="popup-inner-content-wrap">
				<div class="row">
					<div class="col-sm-6 mar-top-15">
						<input type="text" id="pac-input" placeholder="Search Box"  class="form-control map-search-input"/>
					</div>
					<div class="col-sm-6 text-right mar-top-15"><a href="#" class="btn btn-blue text-uppercase close-btn" onclick="return setvalues()">Set the location</a></div>
				</div>
				
				<div class="map-wrapper" id="editProfileMap">
				
				</div>
			</div>
			
		</div>
	</div> 
	
	
	<div class="modal fade" tabindex="-1" role="dialog" id="validationAlertModal">
	  <div class="modal-dialog modal-md">
		<div class="modal-content">
		  
		  <div class="modal-body">
			<p class="alert alert-warning" id="alertwarn"><img src="images/alert_icon.png" /></p>
		  </div>
		  <div class="modal-footer text-center">
			<button type="button" class="btn btn-blue" data-dismiss="modal">Ok</button>
		  </div>
		</div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	
</div>
 <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
   
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
<script  src="https://code.jquery.com/jquery-2.2.0.js"></script>
    <script src="js/bootstrap.min.js"></script>
	

	<script src="https://maps.googleapis.com/maps/api/js?sensor=true&libraries=places&client=gme-leptonsoftwareexport4&signature=xghu9DIoNr63z8_al_oJCSPWQh0="></script>
	<script>
	$( document ).ready(function() {
		$(".map-popup-link").click(function(e){
			e.preventDefault();
			$(".popup-wrapper").fadeIn("slow");
		//	initialize();
		});
		
		$(".popup-wrapper .close-btn").click(function(e){
			e.preventDefault();
			$(".popup-wrapper").fadeOut("slow");
		});
		
		 
		 /* $(".save-btn").click(function(e){
			e.preventDefault();
			$('#validationAlertModal').modal();
		}); */
		  
		
	});
		
	</script>
	
 	 <script type="text/javascript"  >
	 var lat=12.9760559;
		var lng=77.5922071;
		var marker;
		var markers;
		var count=0;
		var  map;
		var name;
		var i=0;
		var finalvalue;
			 function initialize(type) {
	if(count>0){
	for (var k = 0; k < markers.length; k++) {
	    markers[k].setMap(null);
	  }}
				 name=type;
			
				 if(document.getElementById(type+"latlong").value!='null'){
						if(document.getElementById(type+"latlong").value!=null){
							if(document.getElementById(type+"latlong").value!=''){	
							var latlong=document.getElementById(type+"latlong").value;
							lat=((latlong.split("("))[1]).split(", ")[0];
							lng=(((latlong.split("("))[1]).split(", ")[1]).split(")")[0];
							}
						}
				 }
				 $(".map-popup-link").click(function(e){
						e.preventDefault();
						$(".popup-wrapper").fadeIn("slow");
					});
					
					$(".popup-wrapper .close-btn").click(function(e){
						e.preventDefault();
						$(".popup-wrapper").fadeOut("slow");
					});
					
					google.maps.event.addDomListener(window, 'load', initialize);
					var mapProp;
					var myLatLng12 = new google.maps.LatLng(lat,lng);
					if(i==0){
						 i=i+1;
			  mapProp = {
				center:new google.maps.LatLng(lat,lng),
				zoom:12,
				mapTypeId:google.maps.MapTypeId.ROADMAP
			  };
			  map=new google.maps.Map(document.getElementById("editProfileMap"),mapProp);
			  google.maps.event.addListener(map, 'click', function(event) {
				    placeMarker(event.latLng);
				});
			  
					}else{
						
						mapProp = {
									center:new google.maps.LatLng(lat,lng),
									zoom:12,
									mapTypeId:google.maps.MapTypeId.ROADMAP
								  };
								  $('#pac-input').val('');
						map.panTo(new google.maps.LatLng(lat,lng));
					}
			  markers=[];
			  
			 placeMarker(myLatLng12);
			 var input = document.getElementById('pac-input');
			  var searchBox = new google.maps.places.SearchBox(input);
			 // map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

			  map.addListener('bounds_changed', function() {
				    searchBox.setBounds(map.getBounds());
				  });
			 	markers = [];
			  searchBox.addListener('places_changed', function() {
				    var places = searchBox.getPlaces();

				    if (places.length == 0) {
				      return;
				    }

				    // Clear out the old markers.
				    markers.forEach(function(marker1) {
				     	marker1.setMap(null); 
				        marker.setMap(null);
				    });
				    markers = [];

				    // For each place, get the icon, name and location.
				    var bounds = new google.maps.LatLngBounds();
				    places.forEach(function(place) {
				    	marker.setMap(null);
				      var icon = {
				        url: place.icon,
				        size: new google.maps.Size(71, 71),
				        origin: new google.maps.Point(0, 0),
				        anchor: new google.maps.Point(17, 34),
				        scaledSize: new google.maps.Size(25, 25)
				      };
				      
				      // Create a marker for each place.
				      markers.push(new google.maps.Marker({
				        map: map,
				      //  icon: icon,
				        position: place.geometry.location
						//icon:'images/house.png'
				      }));

				      finalvalue=place.geometry.location;
				      if (place.geometry.viewport) {
				        // Only geocodes have viewport.
				        bounds.union(place.geometry.viewport);
				      } else {
				        bounds.extend(place.geometry.location);
				      }
				    });
				    map.fitBounds(bounds);
				  });
				  // [END region_getplaces]

			} 
		
			
			function placeMarker(location) {
	for (var k = 0; k < markers.length; k++) {
	    markers[k].setMap(null);
	  }
				if(count>0)
				{
			marker.setMap(null);
				}
	 markers.forEach(function(marker1) {
				     	marker1.setMap(null); 
				        marker.setMap(null);
				    });
				for(var j = 0; j<markers.length;j++){
					markers[j].setMap(null);

					}
				marker = new google.maps.Marker({
					position : location,
					map : map,
					title:  name
					//icon:'images/house.png'
				});

				count=count+1;
				finalvalue=location;
			}
			function setvalues(){
				document.getElementById(name+"latlong").value=finalvalue;
				
				name="";
				finalvalue="";
				
			}

		
	 </script>  
  </body>
</html>